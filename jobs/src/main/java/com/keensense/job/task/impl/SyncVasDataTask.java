package com.keensense.job.task.impl;

import cn.jiuling.plugin.config.vasclient.ClientSocket;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.keensense.job.config.CleanVasUrlConfig;
import com.keensense.job.config.SocketConfig;
import com.keensense.job.config.VasUrlConfig;
import com.keensense.job.entity.Camera;
import com.keensense.job.entity.CtrlUnit;
import com.keensense.job.entity.TbVasTaskLog;
import com.keensense.job.service.*;
import com.keensense.job.task.ISyncVasDataTask;
import com.keensense.job.util.XmlUtil;
import com.loocme.sys.constance.DateFormatConst;
import com.loocme.sys.util.DateUtil;
import com.loocme.sys.util.PostUtil;
import com.loocme.sys.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @Author cuiss
 * @Description //同步vas点位信息
 * @Date 2018/11/5
 */
@Component
@Slf4j
public class SyncVasDataTask implements ISyncVasDataTask,Runnable {

    private static final Integer TIME_OUT = 200;

    private static final String ENCODE_TYPE = "UTF-8";

    @Autowired
    private SocketConfig socketConfig;

    @Autowired
    private VasUrlConfig vasUrlConfig;

    @Autowired
    private CleanVasUrlConfig cleanVasUrlConfig;

    @Autowired
    private ICameraService cameraService;

    @Autowired
    private ICtrlUnitService ctrlUnitService;

    @Autowired
    private IVsdTaskService vsdTaskService;

    @Autowired
    private IVsdTaskRelationService vsdTaskRelationService;

    @Autowired
    private ITbVasTaskLogService tbVasTaskLogService;

    /**
     * vas数量
     */
    private Integer vasCnt = new Integer(0);
    /**
     * 新增vas数量
     */

     private Integer vasInsertCnt = new Integer(0);
    /**
     * 更新vas数量
     */

     private Integer vasUpdateCnt = new Integer(0);
    /**
     * 删除vas数量
     */

     private Integer vasDeleteCnt = new Integer(0);
    /**
     * org数量
     */

     private Integer orgCnt = new Integer(0);
    /**
     * 新增org数量
     */
     private Integer orgInsertCnt = new Integer(0);
    /**
     * 更新org数量
     */
    private Integer orgUpdateCnt = new Integer(0);
    /**
     * 删除org数量
     */
    private Integer orgDeleteCnt = new Integer(0);

    @Override
    public void syncVasData() {

        log.info("---sync Vas Data......");
        ClientSocket client = null;
        TbVasTaskLog tbVasTaskLog = new TbVasTaskLog();
        try{
            //1、建立长连接 获取点位数据
            log.info("-------socket连接信息：" +socketConfig.getIp()+":"+socketConfig.getPort());
            client= ClientSocket.getInstance(socketConfig.getIp(), socketConfig.getPort(),
                    socketConfig.getUsername(), socketConfig.getPassword());

            if(client != null){
                log.info("---------同步vas信息 start ");
                String resultVasDataXml = client.getAllDeviceString(TIME_OUT,ENCODE_TYPE);
                log.info("xml返回结果==========" + resultVasDataXml);

                if(StringUtil.isNotNull(resultVasDataXml) ){
                    syncVasInfo(resultVasDataXml);
                }
                //调用u2s清理vas监控点缓存
                try {
                    String extUrl = cleanVasUrlConfig.getUrl();
                    PostUtil.requestContent(extUrl, "application/json","");
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("----快检项目未同步最新代码");
                }
                log.info("---------同步vas信息 end");

                log.info("---------同步区域信息 start");
                String resultOrgDataXml = client.getAllOrgString(TIME_OUT,ENCODE_TYPE);
                if(StringUtil.isNotNull(resultOrgDataXml) ){
                    syncOrgInfo(resultOrgDataXml);
                }
                log.info("---------同步区域信息 end");
            }
            generateLog(tbVasTaskLog);
        }catch (Exception e){
            e.printStackTrace();
            log.error("======Exception:"+e.getMessage());
            tbVasTaskLog.setStatus(-10);
            tbVasTaskLog.setMsg(e.getMessage());

        }finally {
            if(client != null){
                ClientSocket.destroyInstance();
            }
            //插入执行日志
            tbVasTaskLog.setCreateTime(new Date());
            tbVasTaskLogService.insert(tbVasTaskLog);
        }
    }

    /**
     * 组装日志
     * @param tbVasTaskLog
     */
    private void generateLog(TbVasTaskLog tbVasTaskLog) {
        tbVasTaskLog.setStatus(10);
        tbVasTaskLog.setMsg("Success");
        tbVasTaskLog.setVasCnt(vasCnt);
        tbVasTaskLog.setVasInsertCnt(vasInsertCnt);
        tbVasTaskLog.setVasUpdateCnt(vasUpdateCnt);
        tbVasTaskLog.setVasDeleteCnt(vasDeleteCnt);
        tbVasTaskLog.setOrgCnt(orgCnt);
        tbVasTaskLog.setOrgInsertCnt(orgInsertCnt);
        tbVasTaskLog.setOrgUpdateCnt(orgUpdateCnt);
        tbVasTaskLog.setOrgDeleteCnt(orgDeleteCnt);
    }

    /**
     * 从socket流中解析Xml报文，与ctrl_unit表进行数据比对，更新ctrl_unit信息
     * @param resultOrgDataXml
     * @throws Exception
     */
    private void syncOrgInfo(String  resultOrgDataXml) throws Exception{

        if(StringUtil.isNotNull(resultOrgDataXml) ){
            //2、解析xml数据
            List<CtrlUnit> ctrlUnitVasList = XmlUtil.getCtrlUnits(resultOrgDataXml,vasUrlConfig);
            orgCnt = new Integer(ctrlUnitVasList.size());
            //3、获取mysql侧 区域信息
            if(ctrlUnitVasList != null && ctrlUnitVasList.size() > 0){
                List<CtrlUnit> ctrlUnitList =  ctrlUnitService.selectList(null);
                if(ctrlUnitList != null && ctrlUnitList.size() > 0){
                    snycOrgs(ctrlUnitVasList,ctrlUnitList);
                }else{
                    orgInsertCnt = new Integer(ctrlUnitVasList.size());
                    ctrlUnitService.insertBatch(ctrlUnitVasList);
                }
            }
            //更新根节点
            ctrlUnitService.updateForSet("unit_level=1",new EntityWrapper<CtrlUnit>().eq("unit_parent_id","").or().isNull("unit_parent_id"));
            //更新叶子节点
            List<CtrlUnit> ctrlUnitList =  ctrlUnitService.selectList(null);
            Map<String,Object> parentMap = new HashMap<>();//父节点的map
            List<CtrlUnit> updateList = new ArrayList<>();
            for(CtrlUnit ctrlUnit:ctrlUnitList){
                if(!StringUtils.isEmpty(ctrlUnit.getUnitParentId())){
                    parentMap.put(ctrlUnit.getUnitParentId(),"1");
                }
            }
            for(CtrlUnit ctrlUnit:ctrlUnitList){
                if(parentMap.get(ctrlUnit.getUnitIdentity())==null){//假如当前的unit标识没在父节点的map里，则表示是叶子节点
                    ctrlUnit.setIsLeaf(1L);
                    updateList.add(ctrlUnit);
                }
            }

            if(updateList.size() > 0){
                ctrlUnitService.updateBatchById(updateList);
            }
        }
    }

    private void snycOrgs(List<CtrlUnit> ctrlUnitVasList, List<CtrlUnit> ctrlUnitList) {
        Map<String,CtrlUnit> unitVasMap = new HashMap<>();
        Map<String,CtrlUnit> unitMap = new HashMap<>();
        String newTime =  DateUtil.getFormat(new Date(), DateFormatConst.YMDHMS_);
        //将Vas区域信息放入hash中，方便后续比较
        for(CtrlUnit ctrlUnit : ctrlUnitVasList ){
            unitVasMap.put(ctrlUnit.getUnitIdentity(),ctrlUnit);
        }
        //将ctrl_unit表数据放入hash,跟Vas区域信息作比较
        log.info("=============" + newTime + "历史区域信息 start");
        for(CtrlUnit ctrlUnit : ctrlUnitList){
            log.info("============="+"id:"+ctrlUnit.getId()+",unit_state:"+ctrlUnit.getUnitState()+",unit_identity:"+ctrlUnit.getUnitIdentity()+",unit_level:"+ctrlUnit.getUnitLevel()+
                    ",display_name:" +ctrlUnit.getDisplayName()+",is_leaf:"+ctrlUnit.getIsLeaf()+",unit_name:"+ctrlUnit.getUnitName()+",unit_number:"+
                    ctrlUnit.getUnitNumber()+",unit_parent_id:"+ctrlUnit.getUnitParentId());
            unitMap.put(ctrlUnit.getUnitIdentity(),ctrlUnit);
        }
        log.info("=============" + newTime + "历史区域信息 end");
        //1、以ctrl_unit表中数据为基表比较
        for(CtrlUnit ctrlUnit : ctrlUnitList){
            if(unitVasMap.get(ctrlUnit.getUnitIdentity()) != null){  //vas中存在相同unit_identity的区域
                CtrlUnit obj = unitVasMap.get(ctrlUnit.getUnitIdentity());
                if(!ctrlUnit.equals(obj)){  //区域信息有差异的时候，更新ctrl_unit表
                    ctrlUnitService.update(obj, new EntityWrapper<CtrlUnit>().eq("unit_identity",ctrlUnit.getUnitIdentity()));
                    ++ orgUpdateCnt;
                }
            }else{  //vas中没有区域信息，则将区域置为不可用状态
                if(ctrlUnit.getUnitState().longValue() == 1){
                    ctrlUnit.setUnitState(0L);
                    ctrlUnitService.update(ctrlUnit, new EntityWrapper<CtrlUnit>().eq("unit_identity",ctrlUnit.getUnitIdentity()));
                    ++ orgDeleteCnt;
                }

            }
        }
        //2、以Vas中数据为基本进行比较,Vas中有，ctrl_unit表没有，则新增
        for(CtrlUnit ctrlUnit : ctrlUnitVasList){
            if(unitMap.get(ctrlUnit.getUnitIdentity()) == null){
                ctrlUnitService.insert(ctrlUnit);
                ++ orgInsertCnt;
            }
        }

    }


    /**
     * 从socket流中解析Xml报文，与camera表进行数据比对，更新camera信息
     * @param resultVasDataXml
     */
    public void syncVasInfo( String resultVasDataXml) throws Exception{

        if(StringUtil.isNotNull(resultVasDataXml) ){
            //2、解析xml数据
            List<Camera> cameraVasList = XmlUtil.getCameras(resultVasDataXml,vasUrlConfig);
            vasCnt = new Integer(cameraVasList.size());
            //3、获取mysql侧vsd点位信息
            if(cameraVasList != null && cameraVasList.size() > 0){
                //只关注国标点位
                List<Camera> cameraList =  cameraService.selectList(new EntityWrapper<Camera>().eq("cameratype", Camera.CAMERA_TYPE_VAS));
                if(cameraList != null && cameraList.size() > 0){
                    snycCameras(cameraVasList,cameraList);
                }else{
                    vasInsertCnt = new Integer(cameraVasList.size());
                    cameraService.insertBatch(cameraVasList);
                }
            }

        }
    }

    /**
     * 比对点位信息，更新点位表
     * @param cameraVasList
     * @param cameraList
     */
    private void snycCameras(List<Camera> cameraVasList, List<Camera> cameraList) {
        Map<String,Camera> cameraVasMap = new HashMap<>();
        Map<String,Camera> cameraMap = new HashMap<>();
        String newTime =  DateUtil.getFormat(new Date(), DateFormatConst.YMDHMS_);
        //将Vas点位信息放入hash中，方便后续比较
        for(Camera camera : cameraVasList ){
            cameraVasMap.put(camera.getExtcameraid(),camera);
        }
        log.info("=============" + newTime + "历史Vas点位 start");
        //将camera表数据放入hash,跟Vas点位信息作比较
        for(Camera camera : cameraList){
            //日志打出历史Vas点位的信息
            log.info("============="+"id:"+camera.getId()+",name:"+camera.getName()+",cameratype:"+camera.getCameratype()+",type:"+camera.getType()
                    +",region:"+camera.getRegion()+",longitude:"+camera.getLongitude()+",latitude:"+camera.getLatitude()+
                    ",status:"+camera.getStatus()+",brandid:"+camera.getBrandid()+",extcameraid"+camera.getExtcameraid()+",url:"+camera.getUrl());
            cameraMap.put(camera.getExtcameraid(),camera);
        }
        log.info("=============" + newTime + "历史Vas点位 end");
        //1、以camera表中数据为基表比较
        for(Camera camera : cameraList){
            if(cameraVasMap.get(camera.getExtcameraid()) != null){  //vas中存在相同extcameraid的点位
                Camera obj = cameraVasMap.get(camera.getExtcameraid());
                if(!camera.equals(obj)){  //点位信息有差异的时候，更新camera表
                    cameraService.update(obj,new EntityWrapper<Camera>().eq("extcameraid",obj.getExtcameraid()).eq("cameratype",Camera.CAMERA_TYPE_VAS));
                    ++ vasUpdateCnt ;
                }
            }else{  //vas中没有点位信息，则将点位置为不可用状态，有分析任务的话，则停止任务
                /*if(camera.getStatus() != null &&  camera.getStatus().longValue() == 1){
                    camera.setStatus(0L);
                    cameraService.update(camera,new EntityWrapper<Camera>().eq("extcameraid",camera.getExtcameraid()).eq("cameratype",Camera.CAMERA_TYPE_VAS));

                    List<VsdTaskRelation> vsdTaskRelations = vsdTaskRelationService.selectList(new EntityWrapper<VsdTaskRelation>().eq("camera_file_id",camera.getId()));
                    if(vsdTaskRelations != null && vsdTaskRelations.size() > 0){
                        for(VsdTaskRelation vsdTaskRelation : vsdTaskRelations){
                            vsdTaskRelation.setC1("0");
                            vsdTaskRelationService.updateById(vsdTaskRelation);
                            vsdTaskService.updateForSet("isvalid=0",new EntityWrapper<VsdTask>().eq("serialnumber",vsdTaskRelation.getSerialnumber()));
                        }
                    }
                    ++ vasDeleteCnt;
                }*/
            }
        }
        //2、以Vas中数据为基本进行比较,Vas中有，camera表没有，则新增
        for(Camera camera:cameraVasList){
            if(cameraMap.get(camera.getExtcameraid()) == null){
                cameraService.insert(camera);
                ++ vasInsertCnt;
            }
        }

    }



    @Override
    public void run() {
        log.info("任务执行线程---------");
        syncVasData();
    }


    public static void main(String[] args)throws Exception{
        ClientSocket client = ClientSocket.getInstance("192.168.0.228",8350,
                "admin", "admin");
        if(client != null){
            String result = client.getAllDeviceString(60,"UTF-8");
            System.out.println("======="+result);
            String utfResult = new String(result.getBytes("UTF-8"),"UTF-8");
            System.out.println("=======size:"+result.length());
            String result2 = client.getAllOrgString(60,"UTF-8");
            System.out.println("======="+result2);

            ClientSocket.destroyInstance();
        }

    }

}
