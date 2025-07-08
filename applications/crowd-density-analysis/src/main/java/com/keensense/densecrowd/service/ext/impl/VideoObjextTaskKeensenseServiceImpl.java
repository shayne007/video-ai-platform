package com.keensense.densecrowd.service.ext.impl;

import com.alibaba.fastjson.JSONObject;
import com.keensense.common.exception.VideoException;
import com.keensense.common.platform.FdfsUtil;
import com.keensense.common.platform.TaskUtil;
import com.keensense.common.platform.bo.video.CrowdDensityTaskBo;
import com.keensense.common.platform.bo.video.ObjextTaskBo;
import com.keensense.common.platform.enums.TaskTypeEnums;
import com.keensense.common.platform.enums.TypeEnums;
import com.keensense.densecrowd.entity.task.VsdTaskRelation;
import com.keensense.densecrowd.service.ext.CrowdDensityService;
import com.keensense.densecrowd.service.ext.VideoObjextTaskService;
import com.keensense.densecrowd.service.sys.ICfgMemPropsService;
import com.keensense.densecrowd.service.task.IVsdTaskRelationService;
import com.keensense.densecrowd.util.CommonConstants;
import com.keensense.densecrowd.util.DbPropUtil;
import com.keensense.densecrowd.util.StringUtils;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.util.MapUtil;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @Author cuiss
 * @Description JManager增加vsd_task任务接口
 * @Date 2018/10/11
 */
@Service("videoObjextTaskService")
public class VideoObjextTaskKeensenseServiceImpl extends AbstractService implements VideoObjextTaskService {
    @Autowired
    private ICfgMemPropsService cfgMemPropsService;
    @Autowired
    IVsdTaskRelationService vsdTaskRelationService;

    @Override
    public String addVsdTaskService(Map<String, Object> paramMap, boolean addRelation) {
        JSONObject retcode = new JSONObject();
        int startCount = vsdTaskRelationService.countRealTask(CommonConstants.CameraStatus.START);
        int routes = DbPropUtil.getInt("task-authorize-connect-number", 100);
        if (startCount >= routes) {
            throw new VideoException(3000, "已达到系统最大支持路数" + routes + "路");
        }
        String alarmStartTime = MapUtil.getString(paramMap, "alarmStartTime");
        String alarmEndTime = MapUtil.getString(paramMap, "alarmEndTime");
        String alarmThreshold = MapUtil.getString(paramMap, "alarmThreshold");
        String alarmInterval = MapUtil.getString(paramMap, "alarmInterval");

        ObjextTaskBo params = initAddInputParam(paramMap);
        if (addRelation) {
            VsdTaskRelation vsdTaskRelation = new VsdTaskRelation();
            vsdTaskRelation.setId(System.currentTimeMillis());
            vsdTaskRelation.setTaskId(System.currentTimeMillis());
            vsdTaskRelation.setSerialnumber(params.getSerialnumber());
            vsdTaskRelation.setCreatetime(new Date());
            vsdTaskRelation.setLastUpdateTime(vsdTaskRelation.getCreatetime());
            vsdTaskRelation.setCameraFileId(Long.valueOf(params.getCameraId()));
            vsdTaskRelation.setFromType(Long.valueOf(params.getFromType()));
            vsdTaskRelation.setCreateuser(0L);


            vsdTaskRelation.setTaskStatus(0);
            vsdTaskRelation.setTaskProgress(0);
            vsdTaskRelation.setTaskUserId(params.getUserId());
            vsdTaskRelation.setTaskName(params.getName());
            vsdTaskRelation.setCameraId(params.getCameraId());
            vsdTaskRelation.setUrl(params.getUrl());

            if (StringUtils.isNotEmptyString(alarmStartTime)) {
                Long startTime = Long.parseLong(alarmStartTime);
                if (startTime > 0) {
                    vsdTaskRelation.setAlarmStartTime(new Date(startTime));
                }
            }
            if (StringUtils.isNotEmptyString(alarmEndTime)) {
                Long endTime = Long.parseLong(alarmEndTime);
                if (endTime > 0) {
                    vsdTaskRelation.setAlarmEndTime(new Date(Long.parseLong(alarmEndTime)));
                }
            }
            if (vsdTaskRelation.getAlarmStartTime() != null && System.currentTimeMillis() < vsdTaskRelation.getAlarmStartTime().getTime()) {
                vsdTaskRelation.setIsvalid(2);
            } else if (vsdTaskRelation.getAlarmEndTime() != null && System.currentTimeMillis() > vsdTaskRelation.getAlarmEndTime().getTime()) {
                vsdTaskRelation.setIsvalid(2);
            } else {
                vsdTaskRelation.setIsvalid(1);
            }
            if (StringUtils.isNotEmptyString(alarmInterval)) {
                vsdTaskRelation.setAlarmInterval(Integer.parseInt(alarmInterval));
            }
            if (StringUtils.isNotEmptyString(alarmThreshold)) {
                vsdTaskRelation.setAlarmThreshold(Integer.parseInt(alarmThreshold));
            }
            vsdTaskRelationService.saveOrUpdate(vsdTaskRelation);
            if (vsdTaskRelation.getIsvalid() == 2) {
                return retcode.toString();
            }
        }

        retcode.put("ret", 0);

        CrowdDensityTaskBo crowdDensityBo = new CrowdDensityTaskBo();
        BeanUtils.copyProperties(params, crowdDensityBo);
        crowdDensityBo.setDetectionFrameSkipInterval(cfgMemPropsService.getDetectionFrameSkipInterval());
        crowdDensityBo.setDetectionScaleFactor(cfgMemPropsService.getDetectionScaleFactor());
        crowdDensityBo.setEnableDensityMapOutput(cfgMemPropsService.getEnableDensityMapOutput());
        crowdDensityBo.setHeatmapWeight(cfgMemPropsService.getHeatmapWeight());
        crowdDensityBo.setPushFrameMaxWaitTime(cfgMemPropsService.getPushFrameMaxWaitTime());
        String result = TaskUtil.addCrowdDensityVideoObjextTask(initKeensenseUrl(), crowdDensityBo);
        Var resultVar = Var.fromJson(result);
        String ret = resultVar.getString("ret");
        if ("0".equals(ret)) {

            retcode.put("taskId", params.getSerialnumber());
        } else {
            retcode.put("ret", ret);
            retcode.put("desc", resultVar.getString("desc"));
        }
        return retcode.toString();
    }

    @Override
    public String queryVsdTaskAllService(Map<String, Object> paramMap) {
        ObjextTaskBo params = initQueryInputParam(paramMap);
        return TaskUtil.getAllVideoObjectTaskList(initKeensenseUrl(), params);
    }

    @Override
    public String deleteVsdTaskService(Map<String, Object> paramMap) {
        ObjextTaskBo params = initOperInputParam(paramMap);
        return TaskUtil.deleteVideoObjectTask(initKeensenseUrl(), params);
    }

    @Override
    public String pauseVsdTaskService(Map<String, Object> paramMap) {
        ObjextTaskBo params = initOperInputParam(paramMap);
        return TaskUtil.pauseVideoObjectTask(initKeensenseUrl(), params);
    }

    @Override
    public String continueVsdTaskService(Map<String, Object> paramMap) {
        ObjextTaskBo params = initOperInputParam(paramMap);
        JSONObject retcode = new JSONObject();
        String result = TaskUtil.continueVideoObjectTask(initKeensenseUrl(), params);
        retcode.put("taskId", params.getSerialnumber());
        retcode.put("result", result);
        return retcode.toString();
    }

    public ObjextTaskBo initAddInputParam(Map<String, Object> paramMap) {
        ObjextTaskBo inParam = new ObjextTaskBo();
        String serialnumber = MapUtil.getString(paramMap, "serialnumber");
        String type = MapUtil.getString(paramMap, "type");
        String url = MapUtil.getString(paramMap, "url");
        Long cameraId = MapUtil.getLong(paramMap, "cameraId");
        String name = MapUtil.getString(paramMap, "name");
        String userId = MapUtil.getString(paramMap, "userId");
        String param = MapUtil.getString(paramMap, "param");
        String deviceId = MapUtil.getString(paramMap, "deviceId");
        String startTime = MapUtil.getString(paramMap, "startTime");
        String endTime = MapUtil.getString(paramMap, "endTime");
        String entryTime = MapUtil.getString(paramMap, "entryTime");
        Integer fromType = MapUtil.getInteger(paramMap, "fromType");
        Integer taskType = MapUtil.getInteger(paramMap, "taskType");
        String udrVertices = MapUtil.getString(paramMap, "udrVertices");
        String interestFlag = MapUtil.getString(paramMap, "interestFlag");


        inParam.setDeviceId(deviceId);//设备ID

        if (StringUtils.isNotEmptyString(serialnumber)) {
            inParam.setSerialnumber(serialnumber);//任务序列号
        }
        if (StringUtils.isNotEmptyString(type)) {
            inParam.setType(TypeEnums.get(type));//任务类型
        }
        if (StringUtils.isNotEmptyString(url)) {
            inParam.setUrl(url);//视频路径
        }
        if (StringUtils.isNotEmptyString(interestFlag)) {
            inParam.setInterested(true);//感兴趣标志
        }
        if (StringUtils.isNotEmptyString(udrVertices)) {
            inParam.setUdrVertices(udrVertices);//感兴趣区域
        }
        if (cameraId != null && cameraId.longValue() > 0) {
            inParam.setCameraId(cameraId + "");//视频监控点
        }
        if (StringUtils.isNotEmptyString(name)) {
            inParam.setName(name);//任务名称
        }
        if (StringUtils.isNotEmptyString(userId)) {
            inParam.setUserId(userId);//创建用户
        }
        if (StringUtils.isNotEmptyString(startTime)) {
            inParam.setStartTime(startTime);
        }
        if (StringUtils.isNotEmptyString(endTime)) {
            inParam.setEndTime(endTime);
        }
        if (StringUtils.isNotEmptyString(entryTime)) {
            inParam.setEntryTime(entryTime); //校正时间
        }
        if (fromType != null) {
            inParam.setFromType(fromType);
        }
        if (taskType != null) {
            inParam.setTaskType(TaskTypeEnums.get(taskType));
        }
        return inParam;
    }

    protected ObjextTaskBo initQueryInputParam(Map<String, Object> map) {
        ObjextTaskBo inParam = new ObjextTaskBo();
        String startTime = MapUtil.getString(map, "startTime");
        String endTime = MapUtil.getString(map, "endTime");
        String type = MapUtil.getString(map, "type");
        String serialnumber = MapUtil.getString(map, "serialnumber");
        String userId = MapUtil.getString(map, "userId");
        String containName = MapUtil.getString(map, "containName");
        String name = MapUtil.getString(map, "name");

        if (!StringUtils.isEmptyString(name)) {
            inParam.setName(name);
        }

        if (!StringUtils.isEmptyString(startTime)) {
            inParam.setStartTime(startTime);
        }
        if (!StringUtils.isEmptyString(endTime)) {
            inParam.setEndTime(endTime);
        }
        if (!StringUtils.isEmptyString(type)) {
            inParam.setType(TypeEnums.get(type));//任务类型
        }
        if (!StringUtils.isEmptyString(serialnumber)) {
            inParam.setSerialnumber(serialnumber);
        }
        if (!StringUtils.isEmptyString(userId)) {
            inParam.setUserId(userId);
        }

        if (null != map && map.containsKey("status")) {
            inParam.setStatus(MapUtil.getInteger(map, "status"));
        }

        if (null != map && map.containsKey("pageSize")) {
            inParam.setPageSize(MapUtil.getInteger(map, "pageSize"));
        }
        if (null != map && map.containsKey("pageNo")) {
            inParam.setPageNo(MapUtil.getInteger(map, "pageNo"));
        }
        return inParam;
    }

    protected ObjextTaskBo initOperInputParam(Map<String, Object> paramMap) {
        ObjextTaskBo var = new ObjextTaskBo();
        var.setSerialnumber(MapUtil.getString(paramMap, "serialnumber"));
        var.setScene(MapUtils.getInteger(paramMap, "scene"));
        var.setInterested(MapUtils.getBoolean(paramMap, "isInterested"));
        var.setUdrVertices(MapUtils.getString(paramMap, "udrVertices"));
        var.setUrl(MapUtils.getString(paramMap, "url"));
        return var;
    }

    @Override
    public String updateVsdTaskService(Map<String, Object> paramMap) {
        ObjextTaskBo params = initOperInputParam(paramMap);
        return TaskUtil.updateVideoObjectTask(initKeensenseUrl(), params);
    }

    @Override
    public String saveImageToFdfs(String imageBase64) {
        return FdfsUtil.saveImage(initKeensenseUrl(),imageBase64);
    }
}
