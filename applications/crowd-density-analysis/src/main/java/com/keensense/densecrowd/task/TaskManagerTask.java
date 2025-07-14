package com.keensense.densecrowd.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.common.platform.enums.TypeEnums;
import com.keensense.common.util.DateUtil;
import com.keensense.densecrowd.entity.task.Camera;
import com.keensense.densecrowd.entity.task.VsdTaskRelation;
import com.keensense.densecrowd.service.ext.VideoObjextTaskService;
import com.keensense.densecrowd.service.sys.ICfgMemPropsService;
import com.keensense.densecrowd.service.task.ICameraService;
import com.keensense.densecrowd.service.task.IVsdTaskRelationService;
import com.keensense.densecrowd.util.CameraConstants;
import com.keensense.densecrowd.util.IpUtils;
import com.keensense.densecrowd.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 21:24 2019/7/25
 * @Version v0.1
 */

@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Slf4j
public class TaskManagerTask {
    @Autowired
    IVsdTaskRelationService vsdTaskRelationService;

    @Autowired
    VideoObjextTaskService videoObjextTaskService;

    @Autowired
    ICfgMemPropsService cfgMemPropsService;

    @Autowired
    ICameraService cameraService;

    /**
     * 定时任务，更新任务状态
     * 每5s进行转码进度查询
     */
    @Scheduled(cron = "4/50 * * * * ?")
    public void updateAllVideoTranscodingProgress() {
        log.info("定时任务，更新任务状态");
        List<VsdTaskRelation> vsdTaskRelations = vsdTaskRelationService.list(new QueryWrapper<VsdTaskRelation>().ne("isvalid", 2).and(i -> i.in("task_status", 0, 1, 4).or().isNull("task_status")));
        if (CollectionUtils.isEmpty(vsdTaskRelations)) {
            log.info("-------------->updateAllVideoTranscodingProgress,ctrlUnitFiles is null");
            return;
        }

        log.info("-------------->updateAllVideoTranscodingProgress,ctrlUnitFiles size " + vsdTaskRelations.size());
        for (VsdTaskRelation vsdTaskRelation1 : vsdTaskRelations) {
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("serialnumber", vsdTaskRelation1.getSerialnumber());
            String taskListReponse = videoObjextTaskService.queryVsdTaskAllService(requestParams);
            JSONObject var = JSONObject.parseObject(taskListReponse);
            if (var == null) {
                return;
            }
            VsdTaskRelation vsdTaskRelation = new VsdTaskRelation();
            vsdTaskRelation.setId(vsdTaskRelation1.getId());
            vsdTaskRelation.setLastUpdateTime(new Date());
            vsdTaskRelation.setSerialnumber(vsdTaskRelation1.getSerialnumber());
            vsdTaskRelation.setTaskStatus(vsdTaskRelation1.getTaskStatus());
            String ret = var.getString("ret");
            if ("0".equals(ret)) {
                JSONArray tasks = var.getJSONArray("tasks");
                if (tasks.size() > 0) {
                    for (int i = 0; i < tasks.size(); i++) {
                        JSONObject resultVar = (JSONObject) tasks.get(i);
                        String serialnumber = resultVar.getString("serialnumber");
                        if (StringUtils.isEmpty(serialnumber)) {
                            serialnumber = resultVar.getString("id");
                        }
                        if (vsdTaskRelation.getSerialnumber().equals(serialnumber)) {
                            vsdTaskRelation.setTaskProgress(resultVar.getInteger("progress"));
                            vsdTaskRelation.setSlaveip(resultVar.getString("slaveip"));
                            JSONArray subTasks = resultVar.getJSONArray("subTasks");
                            if (subTasks.size() > 0) {
                                int status0 = 0;
                                for (int j = 0; j < subTasks.size(); j++) {
                                    JSONObject sub = (JSONObject) subTasks.get(j);
                                    int status = sub.getInteger("status");
                                    if (status == 1) {
                                        status0 = 1;
                                    }
                                    if (status == 2 && status0 == 0) {
                                        status0 = 2;
                                    }
                                    if (status == 3 && status0 == 0) {
                                        status0 = 3;
                                    }
                                }
                                vsdTaskRelation.setTaskStatus(status0);
                            } else {
                                vsdTaskRelation.setTaskStatus(resultVar.getInteger("status"));
                            }
                            if (vsdTaskRelation.getTaskStatus() != null && (vsdTaskRelation.getTaskStatus().equals(2) || vsdTaskRelation.getTaskStatus().equals(3))) {
                                vsdTaskRelation.setEndTime(new Date());
                                vsdTaskRelation.setIsvalid(0);
                            } else {
                                vsdTaskRelation.setIsvalid(1);
                            }
                            vsdTaskRelation.setRemark(IpUtils.getRealIpAddr());
                            vsdTaskRelationService.updateById(vsdTaskRelation);
                        }
                    }

                } else {
                    vsdTaskRelation.setTaskStatus(-1);
                    vsdTaskRelation.setIsvalid(0);
                    vsdTaskRelation.setRemark("任务不存在,ip:" + IpUtils.getRealIpAddr());
//                    vsdTaskRelationService.updateById(vsdTaskRelation);
                }
            } else {
                vsdTaskRelation.setRemark(var.toString());
                vsdTaskRelationService.updateById(vsdTaskRelation);
                log.error(var.toString());
            }
        }
    }

    /**
     * 定时任务，更新任务状态
     * 每5s进行转码进度查询
     */
    @Scheduled(cron = "0 1/1 * * * ?")
    public void startTask() {
        log.info("定时任务，提交任务");
        List<VsdTaskRelation> vsdTaskRelations = vsdTaskRelationService.list(new QueryWrapper<VsdTaskRelation>().eq("isvalid", 2).and(i -> i.lt("alarm_start_time", DateUtil.formatTime(new Date()))).and(i -> i.gt("alarm_end_time", DateUtil.formatTime(new Date())).or().isNull("alarm_end_time")));
        if (CollectionUtils.isEmpty(vsdTaskRelations)) {
            log.info("-------------->startTask null");
            return;
        }

        log.info("-------------->startTask size " + vsdTaskRelations.size());
        for (VsdTaskRelation vsdTaskRelation1 : vsdTaskRelations) {
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("serialnumber", vsdTaskRelation1.getSerialnumber());
            String taskListReponse = videoObjextTaskService.queryVsdTaskAllService(requestParams);
            JSONObject var = JSONObject.parseObject(taskListReponse);
            if (var == null) {
                return;
            }
            VsdTaskRelation vsdTaskRelation = new VsdTaskRelation();
            vsdTaskRelation.setId(vsdTaskRelation1.getId());
            vsdTaskRelation.setLastUpdateTime(new Date());
            vsdTaskRelation.setSerialnumber(vsdTaskRelation1.getSerialnumber());
            vsdTaskRelation.setTaskStatus(vsdTaskRelation1.getTaskStatus());
            String ret = var.getString("ret");
            if ("0".equals(ret)) {
                JSONArray tasks = var.getJSONArray("tasks");
                Camera camera = cameraService.getById(vsdTaskRelation1.getCameraId());
                Map<String, Object> paramMap = new HashMap();
                paramMap.put("url", CameraConstants.transUrl(cfgMemPropsService.getWs2ServerIp(), cfgMemPropsService.getWs2ServerPort(), camera.getUrl(), "real_Platform"));
                paramMap.put("serialnumber", vsdTaskRelation1.getSerialnumber());
                paramMap.put("type", TypeEnums.PEROSON_DESITTY.getValue());
                paramMap.put("taskType", "1");

                paramMap.put("deviceId", vsdTaskRelation1.getCameraId());
                paramMap.put("cameraId", vsdTaskRelation1.getId());
                paramMap.put("name", vsdTaskRelation1.getTaskName());
                if (StringUtils.isNotEmpty(camera.getFollwarea())) {
                    paramMap.put("isInterested", true);
                    paramMap.put("udrVertices", camera.getFollwarea());
                }
                log.info("paramMap:" + paramMap);
                String resultJson = "";
                if (tasks.size() == 0) {
                    resultJson = videoObjextTaskService.addVsdTaskService(paramMap, false);
                } else {
                    resultJson = videoObjextTaskService.continueVsdTaskService(paramMap);
                }
                VsdTaskRelation relation = new VsdTaskRelation();
                relation.setIsvalid(1);
                relation.setId(vsdTaskRelation.getId());
                vsdTaskRelationService.updateById(relation);
                System.out.println(resultJson);
            }
        }
    }

    /**
     * 定时任务，更新任务状态
     * 每5s进行转码进度查询
     */
    @Scheduled(cron = "0 1/1 * * * ?")
    public void stopTask() {
        log.info("定时任务，停止任务");
        List<VsdTaskRelation> vsdTaskRelations = vsdTaskRelationService.list(new QueryWrapper<VsdTaskRelation>().lt("alarm_end_time", DateUtil.formatTime(new Date())));
        if (CollectionUtils.isEmpty(vsdTaskRelations)) {
            log.info("-------------->stopTask null");
            return;
        }

        log.info("-------------->stopTask size " + vsdTaskRelations.size());
        for (VsdTaskRelation vsdTaskRelation1 : vsdTaskRelations) {
            Map<String, Object> paramMap = new HashMap();
            paramMap.put("serialnumber", vsdTaskRelation1.getSerialnumber());
            videoObjextTaskService.pauseVsdTaskService(paramMap);
        }
    }
}
