package com.keensense.admin.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.admin.entity.task.Camera;
import com.keensense.admin.service.task.ICameraService;
import com.keensense.admin.service.task.ICtrlUnitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Slf4j
public class CameraSyncTask {

    @Resource
    private ICameraService cameraService;

    @Resource
    private ICtrlUnitService ctrlUnitService;

    /**selectOnlineAndIpCCameraListByPage
     * 佳都 卡口同步过来的点位的type 类型为 4
     */
    private final static  long  CAMERA_TYPE = 4L;

    /**
     * 点位状态  - 1
     */
    private final static  long  CAMERA_STATUS = 1L;

    /**
     * 接口服务PORT - 0 0 0 * * ? * 每天零点  -
    */
    public static final String  CAMERA_SYNC_CORN = "0 0 0 * * ? *";


    /**
     * 注解无法实现动态赋值
     */
    static {
        //CAMERA_SYNC_CORN = PropertiesUtil.getParameterKey("sync.gate.realtime.camera.task");
    }


    /**
     * cameraSyncFromJiaDu - 同步点位信息 0 0 0 * * ? *
     */
    //@Scheduled(cron = CAMERA_SYNC_CORN)
    public void cameraSyncFromJiaDu(){
        log.info("=== 从佳都定时同步卡口信息,同步至数据库 ===");
        try {
            syncCameraInfo();
        } catch (Exception e) {
            log.error("=== 同步数据error:{} ===",e.getMessage());
        }
    }


    /**
     * 同步数据逻辑
     */
    public void syncCameraInfo(){
        List<Camera> cameraList = loadJdCameraList();
        if (null != cameraList && !cameraList.isEmpty()){
            for (Camera jdCamera : cameraList) {
                List<Camera> cameras = cameraService.list(new QueryWrapper<Camera>().eq("extcameraid", jdCamera.getExtcameraid()));
                if (cameras != null && !cameras.isEmpty()){
                    log.info("=== [Update]更新佳都同步过来的点位信息 ===");
                    cameraService.update(new QueryWrapper<Camera>().eq("extcameraid", jdCamera.getExtcameraid()));
                }else{
                    log.info("=== [Insert]新增佳都同步过来的点位信息 ===");
                    cameraService.save(jdCamera);
                }
            }
        }
    }

    /**
     * example:
     * [
     *     {
     *         "DEVICE_ID": "112223332221112222",
     *         "INSTALL_ADDR": "中国广东佛山顺德",
     *         "LATITUDE": "22.83135",
     *         "LONGITUDE": "113.242927",
     *         "MANUFACTURE_NAME": "海康威视",
     *         "NAME": "顺德卡口",
     *         "ORG_CODE": "44060650",
     *         "REMARKS": "推送千视通"
     *     }
     * ]
     * @return
     */
    public List<Camera> loadJdCameraList(){
        List<Camera> cameraList = new ArrayList<>();
        List<Camera> jdCameras = cameraService.selectJdCamera();
        if (null != jdCameras && jdCameras.size() > 0){
            for (Camera jdCamera : jdCameras) {
                Camera camera = new Camera();
                // 点位id
                camera.setExtcameraid(jdCamera.getExtcameraid());
                camera.setName(jdCamera.getName());
                camera.setLatitude(jdCamera.getLatitude());
                camera.setLongitude(jdCamera.getLongitude());
                // 区域code
                camera.setRegion(jdCamera.getRegion());
                camera.setLocation(jdCamera.getLocation());
                //camera.setBrandname(resultSet.getString("MANUFACTURE_NAME"));
                // 状态默认为1 - 激活状态
                camera.setStatus(CAMERA_STATUS);
                camera.setCameratype(CAMERA_TYPE);
                cameraList.add(camera);
            }
        }else{
            log.info("=== 暂无需要同步数据 ===");
        }
        return cameraList;
    }


    /**
     * 同步佳都的点位信息数据  ctrlUnitService
     */
    /*public void syncCtrlUnitInfo(){
        List<CtrlUnit> ctrlUnitList = loadJdCameraCtrlUnitList();
        if (null != ctrlUnitList && !ctrlUnitList.isEmpty()){
            for (CtrlUnit jdCtrlUnit : ctrlUnitList) {
                CtrlUnit ctrlUnit = ctrlUnitService.selectByUnitIdentity(jdCtrlUnit.getUnitIdentity());
                if (null != ctrlUnit){
                    jdCtrlUnit.setId(ctrlUnit.getId());
                    ctrlUnitService.updateById(jdCtrlUnit);
                }else{
                    ctrlUnitService.save(jdCtrlUnit);
                }
            }
        }
    }*/

    /**
     * 加载佳都位置区域信息
     * @return
     */
    /*public static List<CtrlUnit> loadJdCameraCtrlUnitList(){
        LinkedList<CtrlUnit> ctrlUnitList = new LinkedList<>();
        try {
            JdDbUtils.getConn();
            *//**
             * 从佳都提供的 区域信息视图里面拿到位置对应的信息
             *//*
            String querySql = "SELECT * FROM V_PUSH_QST_FACE_DEVICE_ID_INFO order by ? ? ";
            Object[] queryParams = new Object[]{"DEVICE_ID", "DESC"};
            ResultSet resultSet = JdDbUtils.executeSelect(querySql, queryParams);
            if (null != resultSet){
                while(resultSet.next()){
                    CtrlUnit ctrlUnit = new CtrlUnit();
                    *//**
                     * [默认配置]
                     * 叶子节点 - 1L
                     * 开启状态1L  - 层级关系 2L
                     * 社会点位id  - 配置化?
                     *//*
                    ctrlUnit.setIsLeaf(1L);
                    ctrlUnit.setUnitState(1L);
                    ctrlUnit.setUnitLevel(2L);
                    ctrlUnit.setUnitParentId("80745444");
                    ctrlUnit.setLastUpdatedTime(new Date());


                    ctrlUnit.setDisplayName(resultSet.getString("FULL_NAME"));
                    ctrlUnit.setUnitName(resultSet.getString("NAME"));
                    ctrlUnit.setUnitIdentity(resultSet.getString("ORG_CODE"));
                    ctrlUnitList.add(ctrlUnit);
                }
            }else{
                log.info("=== 暂无需要同步数据 ===");
            }
        } catch (ClassNotFoundException e) {
            log.error("=== ClassNotFoundException:error:{} ===",e.getMessage());
        } catch (SQLException e) {
            log.error("=== SQLException:error:{} ===",e.getMessage());
        }
        return ctrlUnitList;
    }*/


}
