package com.keensense.densecrowd.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.common.config.SpringContext;
import com.keensense.common.platform.bo.video.CrowdDensity;
import com.keensense.common.platform.bo.video.CrowdDensityQuery;
import com.keensense.common.util.DateUtil;
import com.keensense.common.util.StandardHttpUtil;
import com.keensense.densecrowd.entity.task.DensecrowdWarnResult;
import com.keensense.densecrowd.entity.task.VsdTaskRelation;
import com.keensense.densecrowd.service.ext.CrowdDensityService;
import com.keensense.densecrowd.service.task.IDensecrowdWarnResultService;
import com.keensense.densecrowd.service.task.IVsdTaskRelationService;
import com.keensense.densecrowd.util.DbPropUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 13:45 2019/11/11
 * @Version v0.1
 */
@Slf4j
public class AlarmManagerTask {
    static IVsdTaskRelationService vsdTaskRelationService = SpringContext.getBean(IVsdTaskRelationService.class);

    static CrowdDensityService crowdDensityService = SpringContext.getBean(CrowdDensityService.class);
    static Map<Long, Long> relations = new HashMap<>();

    private static ExecutorService ESERVICE = null;

    private static IDensecrowdWarnResultService densecrowdWarnResultService = SpringContext.getBean(IDensecrowdWarnResultService.class);

    /**
     * 定时任务，更新任务状态
     * 每5s进行转码进度查询
     */
    public static void reload() {
        log.info("定时任务,推送数据");
        List<VsdTaskRelation> vsdTaskRelations = vsdTaskRelationService.list(new QueryWrapper<VsdTaskRelation>().eq("isvalid", 1).and(i -> i.gt("alarm_interval", 0)));
        if (CollectionUtils.isEmpty(vsdTaskRelations)) {
            log.info("-------------->reload,reload is null");
            return;
        }
        log.info("-------------->reload,reload size " + vsdTaskRelations.size());
        for (VsdTaskRelation vsdTaskRelation1 : vsdTaskRelations) {
            if (relations.get(vsdTaskRelation1.getId()) == null) {
                relations.put(vsdTaskRelation1.getId(), System.currentTimeMillis());
            }
        }
    }

    public static void start() {
        ESERVICE = Executors.newSingleThreadExecutor();
        ESERVICE.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String url = DbPropUtil.getString("task.callback_url", "127.0.0.1:9801/densecrowd/spider/cloudWalkService/density/alarm/send");
                        url = "http://" + url;
                        for (Map.Entry<Long, Long> relation : relations.entrySet()) {
                            VsdTaskRelation vsdTaskRelation = vsdTaskRelationService.getById(relation.getKey());
                            if (vsdTaskRelation.getIsvalid() == 0) {
                                relations.remove(vsdTaskRelation.getId());
                            }
                            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                            if ((System.currentTimeMillis() - relation.getValue()) > vsdTaskRelation.getAlarmInterval() * 60 * 1000) {
                                log.info(uuid + " 定时发送任务地址:" + url);
                                Long endTime = System.currentTimeMillis();
                                Long startTime = endTime - vsdTaskRelation.getAlarmInterval() * 60 * 1000;
                                CrowdDensityQuery crowdDensityQuery = new CrowdDensityQuery();
                                crowdDensityQuery.setPageNo(1);
                                crowdDensityQuery.setPageSize(Integer.MAX_VALUE);
                                crowdDensityQuery.setDeviceIds(vsdTaskRelation.getCameraId());
                                crowdDensityQuery.setCountMin(vsdTaskRelation.getAlarmThreshold());
                                crowdDensityQuery.setStartTime(DateUtil.formatDate(new Date(startTime), DateUtil.FORMAT_6));
                                crowdDensityQuery.setEndTime(DateUtil.formatDate(new Date(endTime), DateUtil.FORMAT_6));
                                Page pages = crowdDensityService.getDensityResultList(crowdDensityQuery);
                                List<CrowdDensity> crowdDensities = pages.getRecords();
                                log.info(uuid + " 数据量:" + crowdDensities.size());
                                for (CrowdDensity crowdDensity : crowdDensities) {
                                    Map<String, String> paramMap = new HashMap<>();
                                    paramMap.put("deviceId", vsdTaskRelation.getCameraId());
                                    paramMap.put("deviceName", vsdTaskRelation.getTaskName());
                                    paramMap.put("threshold", vsdTaskRelation.getAlarmThreshold() + "");
                                    paramMap.put("count", crowdDensity.getCount() + "");
                                    paramMap.put("alarmUrl", crowdDensity.getPicUrl());
                                    paramMap.put("alarmTime", crowdDensity.getCreateTime());
                                    String result = StandardHttpUtil.postHttp(url, paramMap);
                                    log.info(uuid + " result:" + result);
//                                    DensecrowdWarnResult densecrowdWarnResult = new DensecrowdWarnResult();
//                                    BeanUtils.copyProperties(crowdDensity, densecrowdWarnResult);
//                                    String time = densecrowdWarnResult.getCreateTime().replaceAll("-", "").replaceAll(" ", "").replaceAll(":","");
//                                    densecrowdWarnResult.setCreateTime(time);
//                                    densecrowdWarnResult.setAlarmThreshold(vsdTaskRelation.getAlarmThreshold());
//                                    densecrowdWarnResultService.saveOrUpdate(densecrowdWarnResult);
                                }
                                relation.setValue(System.currentTimeMillis());
                            }
                        }
                        Thread.sleep(10000L);
                    } catch (InterruptedException var2) {
                        var2.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                    reload();
                }
            }
        });
    }
}
