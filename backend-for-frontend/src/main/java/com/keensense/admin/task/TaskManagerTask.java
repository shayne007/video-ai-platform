package com.keensense.admin.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.admin.constants.VideoTaskConstant;
import com.keensense.admin.entity.task.VsdTaskRelation;
import com.keensense.admin.service.ext.VideoObjextTaskService;
import com.keensense.admin.service.task.IVsdTaskRelationService;
import com.keensense.admin.util.IpUtils;
import com.keensense.admin.util.StringUtils;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.datastruct.WeekArray;
import com.loocme.sys.util.ListUtil;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * 定时任务，更新任务状态
     * 每5s进行转码进度查询
     */
    @Scheduled(cron = "4/30 * * * * ?")
    public void updateAllVideoTranscodingProgress() {
        log.info("定时任务，更新任务状态");
        List<VsdTaskRelation> vsdTaskRelations =
                vsdTaskRelationService.list(new QueryWrapper<VsdTaskRelation>().notIn("from_type", 6).and(i -> i.in("task_status", 0, 1, 4).or().isNull("task_status")));
        if (ListUtil.isNull(vsdTaskRelations)) {
            log.info("-------------->updateAllVideoTranscodingProgress,ctrlUnitFiles is null");
            return;
        }

        log.info("-------------->updateAllVideoTranscodingProgress,ctrlUnitFiles size " + vsdTaskRelations.size());
        for (VsdTaskRelation vsdTaskRelation1 : vsdTaskRelations) {
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("type", VideoTaskConstant.Type.OBJEXT);
            requestParams.put("serialnumber", vsdTaskRelation1.getSerialnumber());
            String taskListReponse = "";
            if (vsdTaskRelation1.getFromType().equals(VideoTaskConstant.FROM_TYPE.ONLINE_VIDEO)) {
                requestParams.put("pageNo", 1);
                requestParams.put("pageSize", Integer.MAX_VALUE);
                taskListReponse = videoObjextTaskService.queryVsdTaskService(requestParams);
            } else {
                taskListReponse = videoObjextTaskService.queryVsdTaskAllService(requestParams);
            }
            Var var = Var.fromJson(taskListReponse);
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
                WeekArray tasks = var.getArray("tasks");
                if (tasks.getSize() > 0) {
                    for (int i = 0; i < tasks.getSize(); i++) {
                        Var resultVar = tasks.get(String.valueOf(i));
                        String serialnumber = resultVar.getString("serialnumber");
                        if (StringUtils.isEmpty(serialnumber)) {
                            serialnumber = resultVar.getString("id");
                        }
                        if (vsdTaskRelation.getSerialnumber().equals(serialnumber)) {
                            vsdTaskRelation.setTaskProgress(resultVar.getInt("progress"));
                            vsdTaskRelation.setSlaveip(resultVar.getString("slaveip"));
                            WeekArray subTasks = resultVar.getArray("subTasks");
                            Map<Integer, Var> subStatus = new HashMap<>();
                            if (subTasks.getSize() > 0) {
                                for (int j = 0; j < subTasks.getSize(); j++) {
                                    Var sub = subTasks.get(String.valueOf(j));
                                    int status = sub.getInt("status");
                                    subStatus.put(status, sub);
                                }
                                int status0 = 0;
                                if (subStatus.containsKey(1)) {
                                    status0 = 1;
                                } else if (subStatus.containsKey(2) && !subStatus.containsKey(3)) {
                                    if (!subStatus.containsKey(0)) {
                                        status0 = 2;
                                    }
                                } else if (subStatus.containsKey(3)) {
                                    if (!subStatus.containsKey(0)) {
                                        status0 = 3;
                                    }
                                }
                                vsdTaskRelation.setTaskStatus(status0);
                            } else {
                                vsdTaskRelation.setTaskStatus(resultVar.getInt("status"));
                            }
                            if (vsdTaskRelation.getTaskStatus() != null && (vsdTaskRelation.getTaskStatus().equals(2) || vsdTaskRelation.getTaskStatus().equals(3))) {
                                vsdTaskRelation.setEndTime(new Date());
                                vsdTaskRelation.setIsvalid(0);
                            }
                            vsdTaskRelation.setRemark(IpUtils.getRealIpAddr());
                            vsdTaskRelationService.updateById(vsdTaskRelation);
                        }
                    }

                } else {
                    vsdTaskRelation.setTaskStatus(-1);
                    vsdTaskRelation.setIsvalid(0);
                    vsdTaskRelation.setRemark("任务不存在,ip:" + IpUtils.getRealIpAddr());
                    vsdTaskRelationService.updateById(vsdTaskRelation);
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
//    @Scheduled(cron = "4/5 * * * * ?")
    public void updateIsvalid() {
        log.info("定时任务，更新任务状态updateIsvalid");
        List<VsdTaskRelation> vsdTaskRelations = vsdTaskRelationService.list(new QueryWrapper<VsdTaskRelation>().isNull("isvalid"));
        if (ListUtil.isNull(vsdTaskRelations)) {
            log.info("-------------->updateAllVideoTranscodingProgress,ctrlUnitFiles is null");
            return;
        }

        log.info("-------------->updateAllVideoTranscodingProgress,ctrlUnitFiles size " + vsdTaskRelations.size());
        for (VsdTaskRelation vsdTaskRelation : vsdTaskRelations) {
//            VsdTask vsdTask = vsdTaskService.getOne(new QueryWrapper<VsdTask>().eq("serialnumber", vsdTaskRelation.getSerialnumber()));
//            vsdTaskRelation.setIsvalid(vsdTask.getIsvalid());
//            vsdTaskRelationService.updateById(vsdTaskRelation);
        }
    }
}
