package com.keensense.densecrowd.service.task.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.common.platform.enums.TaskTypeEnums;
import com.keensense.common.platform.enums.TypeEnums;
import com.keensense.common.util.R;
import com.keensense.densecrowd.constant.VideoTaskConstant;
import com.keensense.densecrowd.entity.task.Camera;
import com.keensense.densecrowd.entity.task.VsdTaskRelation;
import com.keensense.densecrowd.mapper.task.CameraMapper;
import com.keensense.densecrowd.service.ext.VideoObjextTaskService;
import com.keensense.densecrowd.service.sys.ICfgMemPropsService;
import com.keensense.densecrowd.service.task.IVsdTaskRelationService;
import com.keensense.densecrowd.service.task.IVsdTaskService;
import com.keensense.densecrowd.util.CommonConstants;
import com.keensense.densecrowd.util.EhcacheUtils;
import com.keensense.densecrowd.util.PropertiesUtil;
import com.keensense.densecrowd.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.PatternMatchUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: shitao
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 11:23 2019/9/25
 * @Version v0.1
 */
@Slf4j
@Service("vsdTaskService")
public class VsdTaskServiceImpl implements IVsdTaskService {

    @Autowired
    IVsdTaskRelationService vsdTaskRelationService;

    @Resource
    private CameraMapper cameraMapper;

    @Autowired
    private VideoObjextTaskService videoObjextTaskService;

    @Autowired
    private ICfgMemPropsService cfgMemPropsService;

    @Override
    public R startDensecrowdTask(Long cameraId, Integer interestFlag, String udrVertices, String startTime,
                                 String endTime) {
        R result = R.ok();
        Map<String, Object> paramMap = new HashedMap();
        Camera camera = cameraMapper.selectById(cameraId);
        String url = camera.getUrl();

        //任务类型
        paramMap.put("type", TypeEnums.PEROSON_DESITTY.getValue());

        paramMap.put("cameraId", cameraId + "");

        if (interestFlag != null && CommonConstants.INTERREST.equals(interestFlag)) {
            paramMap.put("isInterested", true);
            paramMap.put("udrVertices", initInterestParam(udrVertices));
        }

        if (camera != null) {
            paramMap.put("name", camera.getName());
            //设备ID
            paramMap.put("deviceId", camera.getExtcameraid());
        }

        Long cameratype = camera.getCameratype();
        String taskUserId = "";
        String newUrl = "";
        Long fileFromType = null;
        String ws2ServerIp = cfgMemPropsService.getWs2ServerIp();
        String ws2ServerPort = cfgMemPropsService.getWs2ServerPort();

        if (cameratype == CommonConstants.CameraType.VAS) {
            taskUserId = CommonConstants.REALTIME_VIDEO;
            fileFromType = CommonConstants.VAS_FROM_TYPE;
            if (PatternMatchUtils.simpleMatch(url, "^vas://name=.+&psw=.+&srvip=.+&srvport=\\d+&devid=.+&.*$")) {
                return R.error("点位信息有误,请确认点位格式及内容");
            }
            String[] data = url.trim().split("&");
            Map<String, String> urls = new HashMap<>();
            for (String d : data) {
                String[] s = d.split("=");
                urls.put(s[0], s[1]);
            }
            if (camera.getName().indexOf("vastest") > -1) {
                newUrl = url;
            } else {
                newUrl = "ws://" + ws2ServerIp + ":" + ws2ServerPort + "/?nodeType=GB28181&type=real_Platform&channel=" + urls.get("devid") + "&";
            }
        } else if (cameratype == CommonConstants.CameraType.RTSP) {
            //视频路径
            newUrl = url;
            taskUserId = CommonConstants.IPC_VIDEO;
            fileFromType = CommonConstants.IPC_FROM_TYPE;
        }


        paramMap.put("url", newUrl);
        paramMap.put("userId", taskUserId);
        paramMap.put("taskType", TaskTypeEnums.REAL_LINE.getValue());
        paramMap.put("type", TypeEnums.PEROSON_DESITTY.getValue());
        if (camera.getAlarmThreshold() == null) {
            camera.setAlarmThreshold(0);
        }
        paramMap.put("alarmThreshold", camera.getAlarmThreshold());
        String serialnumber = "";
        VsdTaskRelation relation = vsdTaskRelationService.queryVsdTaskRelationByCameraFileId(cameraId.toString(), taskUserId);
        //已添加过任务，重启
        if (null != relation) {
            Long id = relation.getId();
            serialnumber = relation.getSerialnumber();
            paramMap.put("serialnumber", serialnumber);
            String continueJson = continueTask(relation, paramMap);
            JSONObject retCodeString = JSONObject.parseObject(continueJson);
            JSONObject continueResVar = JSONObject.parseObject(retCodeString.getString("result"));
            String retCode = continueResVar.getString("ret");
            String retDesc = continueResVar.getString("desc");
            // -1：任务已经重启失败 -2 启动超路数
            if ("0".equals(retCode) || "1".equals(retCode) || (retDesc != null && retDesc.indexOf("任务已启动") > -1)) {
                EhcacheUtils.removeItem(serialnumber);
                this.insertTaskRelation(id, relation.getSerialnumber(), cameraId, fileFromType, null,
                        0, new Integer(CommonConstants.STATUS.RUNNING), taskUserId, camera.getName(), cameraId + "", url, camera.getAlarmThreshold());
                return R.ok();
            } else {
                return R.error(retDesc);
            }
        }

        String resultJson = "";
        // 添加任务
        if (StringUtils.isEmpty(serialnumber)) {
            serialnumber = vsdTaskRelationService.getSerialnumber();
        }
        //任务序列号
        paramMap.put("serialnumber", serialnumber);
        paramMap.put("taskType", TaskTypeEnums.REAL_LINE.getValue());
        paramMap.put("fromType", fileFromType);


        resultJson = videoObjextTaskService.addVsdTaskService(paramMap, true);
        EhcacheUtils.removeItem(serialnumber);
        JSONObject addResVar = JSONObject.parseObject(resultJson);
        String retCode = addResVar.getString("ret");
        String retDesc = addResVar.getString("desc");
        result.put("code", retCode);
        result.put("taskId", addResVar.getString("taskId"));
        result.put("msg", retDesc);
        return result;
    }

    private String initInterestParam(String featurFollowarea) {
        List<JSONObject> udrVerticesList = new LinkedList<>();
        // 感兴趣区域节点
        if (StringUtils.isNotEmptyString(featurFollowarea)) {
            String[] featureArr = featurFollowarea.split(";");
            if (featureArr != null && featureArr.length > 0) {
                String[] verticesNumArray = null;
                String[] verticesArr = null;
                Double minRate = 0.01;
                Double maxRate = 0.98;
                try {
                    String minRateStr = PropertiesUtil.getParameterPackey("min.rate");
                    String maxRateStr = PropertiesUtil.getParameterPackey("max.rate");
                    if (StringUtils.isNotEmptyString(minRateStr)) {
                        minRate = Double.valueOf(minRateStr);
                    }
                    if (StringUtils.isNotEmptyString(maxRateStr)) {
                        maxRate = Double.valueOf(maxRateStr);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                // 每个区域循环
                for (int i = 0; i < featureArr.length; i++) {
                    JSONObject featureJSONObject = new JSONObject();
                    // 节点数量
                    verticesNumArray = featureArr[i].split("\\|");
                    String verticesStr = featureArr[i].replace("|", ",");
                    verticesArr = verticesStr.split(",");
                    // 点位信息
                    if (verticesArr != null) {
                        List<Double> verticesList = new LinkedList<>();
                        Double d = 0D;
                        for (int j = 0; j < verticesArr.length; j++) {
                            if (Double.valueOf(verticesArr[j]) > maxRate) {
                                d = Double.valueOf(1);
                            } else if (Double.valueOf(verticesArr[j]) < minRate) {
                                d = Double.valueOf(0);
                            } else {
                                d = Double.valueOf(verticesArr[j]);
                            }
                            verticesList.add(d);
                        }
                        featureJSONObject.put("vertices", verticesList);
                    }
                    udrVerticesList.add(featureJSONObject);
                }
            }
        }

        Set<String> verticesSet = new HashSet<>();
        for (int i = 0; i < udrVerticesList.size(); i++) {
            String vertice = "";
            JSONArray vertices = udrVerticesList.get(i).getJSONArray("vertices");
            for (int j = 0; j < vertices.size(); j++) {
                String v = vertices.getString(j);
                if (StringUtils.isEmpty(vertice)) {
                    vertice = v;
                } else {
                    vertice = vertice + "," + v;
                }
            }
            verticesSet.add("\"" + vertice + "\"");
        }
        return verticesSet.toString();
    }

    /**
     * 监控点任务重启（重启任务后更新感兴趣区域）
     *
     * @param relation
     * @param paramMap
     * @return
     */
    private String continueTask(VsdTaskRelation relation, Map<String, Object> paramMap) {
        String serialnumber = relation.getSerialnumber();
        //重启更新感兴趣区域
        String url = MapUtils.getString(paramMap, "url");
        String param = MapUtils.getString(paramMap, "param");
        String tripWires = MapUtils.getString(paramMap, "tripWires");
        String udrVertices = MapUtils.getString(paramMap, "udrVertices");
        JSONObject paramVar = JSONObject.parseObject(param);
//        String analysisCfg = paramVar.getString("analysisCfg");
//        Var analysisCfgVar = Var.fromJson(analysisCfg);
//        String scene = analysisCfgVar.getString("scene");
//        Var udrSettingVar = Var.fromJson(udrSetting);
        String isInterested = MapUtils.getString(paramMap, "isInterested");
        Map<String, Object> map = new HashMap<>();
//        WeekArray udrVertices = udrSettingVar.getArray("udrVertices");

        //格式："0.8021309,0.21680216,0.81430745,0.5745258,0.9421613,0.40921408\", \"0.12937595,0.1897019,0.10350076,0.7208672,0.36834094,0.73170733,0.35920852,0.25203252\", \"0.6347032,0.28455284,0.5449011,0.6124661,0.6818874,0.76151764,0.7427702,0.75609756\"]");"[\"0.8021309,0.21680216,0.81430745,0.5745258,0.9421613,0.40921408\", \"0.12937595,0.1897019,0.10350076,0.7208672,0.36834094,0.73170733,0.35920852,0.25203252\", \"0.6347032,0.28455284,0.5449011,0.6124661,0.6818874,0.76151764,0.7427702,0.75609756"
        map.put("udrVertices", udrVertices);
//        map.put("scene", scene);
        map.put("isInterested", isInterested);
        map.put("serialnumber", serialnumber);
        map.put("url", url);
//        map.put("tripWires", tripwires.toString());
        videoObjextTaskService.updateVsdTaskService(paramMap);

        //重启任务
        Map<String, Object> operMap = new HashMap<>();
        operMap.put("serialnumber", serialnumber);
        String resultJson = videoObjextTaskService.continueVsdTaskService(operMap);
        return resultJson;

    }

    /**
     * 根据id更新VsdTaskRelation
     *
     * @param relationId
     * @param isvalid
     */
    public void updateVsdTaskRelations(Long relationId, String isvalid) {
        VsdTaskRelation relation = new VsdTaskRelation();
        relation.setId(relationId);
        relation.setIsvalid(Integer.parseInt(isvalid));
        relation.setTaskStatus(0);
        relation.setLastUpdateTime(new Date());
        vsdTaskRelationService.updateById(relation);
    }

    public boolean insertTaskRelation(Long id, String serialnumber, Long fileId, Long fileFromType, Long createUser, Integer taskStatus, Integer isvalid, String taskUserId, String taskName, String cameraId, String url, Integer alarmThreshold) {
        VsdTaskRelation vsdTaskRelation = new VsdTaskRelation();
        vsdTaskRelation.setId(id);
        vsdTaskRelation.setTaskId(System.currentTimeMillis());
        vsdTaskRelation.setSerialnumber(serialnumber);
        vsdTaskRelation.setCreatetime(new Date());
        vsdTaskRelation.setLastUpdateTime(vsdTaskRelation.getCreatetime());
        vsdTaskRelation.setCameraFileId(fileId);
        vsdTaskRelation.setFromType(fileFromType);
        vsdTaskRelation.setCreateuser(createUser);
        vsdTaskRelation.setIsvalid(isvalid);
        vsdTaskRelation.setTaskStatus(taskStatus);
        vsdTaskRelation.setTaskProgress(0);
        vsdTaskRelation.setTaskUserId(taskUserId);
        vsdTaskRelation.setTaskName(taskName);
        vsdTaskRelation.setCameraId(cameraId);
        vsdTaskRelation.setUrl(url);
        vsdTaskRelation.setAlarmThreshold(alarmThreshold);
        boolean ret = vsdTaskRelationService.saveOrUpdate(vsdTaskRelation);
        return ret;
    }

    @Override
    public Map<String, Object> stopDensecrowdTask(String serialnumber) {
        Map<String, Object> result = new HashedMap();
        result.put("Code", "0");
        Map<String, Object> pauseMap = new HashMap<>();
        pauseMap.put("serialnumber", serialnumber);
        String resultJson = videoObjextTaskService.pauseVsdTaskService(pauseMap);
        JSONObject updateRestVar = JSONObject.parseObject(resultJson);
        if (resultJson == null) {
            return R.error();
        }
        String retCode = updateRestVar.getString("ret");
        String retDesc = updateRestVar.getString("desc");
        if ("0".equals(retCode) || "1".equals(retCode) || "7".equals(retCode) || "8".equals(retCode)) {
            VsdTaskRelation vsdTaskRelation = new VsdTaskRelation();
            vsdTaskRelation.setIsvalid(Integer.parseInt(CommonConstants.STATUS.FINISHED));
            vsdTaskRelationService.update(vsdTaskRelation, new QueryWrapper<VsdTaskRelation>().eq("serialnumber", serialnumber));
        } else {
            result.put("code", retCode);
        }
        result.put("msg", retDesc);
        return result;
    }

    @Override
    public Map<String, Object> stopRealtimeTask(String serialnumber) {
        Map<String, Object> result = new HashedMap();
        result.put("retCode", "0");
        Map<String, Object> pauseMap = new HashMap<>();
        pauseMap.put("serialnumber", serialnumber);
        String resultJson = videoObjextTaskService.pauseVsdTaskService(pauseMap);
        JSONObject updateRestVar = JSONObject.parseObject(resultJson);
        if (resultJson == null) {
            return R.error();
        }
        String retCode = updateRestVar.getString("ret");
        String retDesc = updateRestVar.getString("desc");
        if ("0".equals(retCode) || "1".equals(retCode) || "7".equals(retCode) || "8".equals(retCode)) {
            VsdTaskRelation vsdTaskRelation = new VsdTaskRelation();
//            vsdTaskRelation.setC1(VideoTaskConstant.STATUS.FINISHED);//后续需要抛弃的字段
            vsdTaskRelation.setIsvalid(Integer.parseInt(VideoTaskConstant.STATUS.FINISHED));
            vsdTaskRelationService.update(vsdTaskRelation, new QueryWrapper<VsdTaskRelation>().eq("serialnumber", serialnumber));
        } else {
            result.put("retCode", retCode);
        }
        result.put("retDesc", retDesc);
        return result;
    }
}
