package com.keensense.densecrowd.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.common.config.SpringContext;
import com.keensense.common.platform.bo.video.CrowdDensity;
import com.keensense.common.platform.bo.video.CrowdDensityQuery;
import com.keensense.common.util.DateUtil;
import com.keensense.common.util.HttpU2sGetUtil;
import com.keensense.common.util.StandardHttpUtil;
import com.keensense.densecrowd.entity.task.VsdTaskRelation;
import com.keensense.densecrowd.service.ext.CrowdDensityService;
import com.keensense.densecrowd.service.task.IVsdTaskRelationService;
import com.keensense.densecrowd.util.DbPropUtil;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
public class TechnologyManagerTask {
    static IVsdTaskRelationService vsdTaskRelationService = SpringContext.getBean(IVsdTaskRelationService.class);

    static CrowdDensityService crowdDensityService = SpringContext.getBean(CrowdDensityService.class);
    static Map<Long, Long> relations = new HashMap<>();

    private static ExecutorService ESERVICE = null;

    private static long lastTime = System.currentTimeMillis();

    public static void start() {
        ESERVICE = Executors.newSingleThreadExecutor();
        ESERVICE.execute(new Runnable() {
            @Override
            public void run() {
                while (DbPropUtil.getBoolean("alarm_technology_task", false)) {
                    try {

                        int threadHold = DbPropUtil.getInt("warning.alarm.threshold", 50);
                        /**
                         * 扫描时间间距(秒)
                         */
                        int distan = DbPropUtil.getInt("alarm_technology_distan", 60);

                        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                        long currentTime = System.currentTimeMillis();

                        Long startTime = lastTime - distan * 1000;
                        CrowdDensityQuery crowdDensityQuery = new CrowdDensityQuery();
                        crowdDensityQuery.setPageNo(1);
                        crowdDensityQuery.setPageSize(1);
                        crowdDensityQuery.setCountMin(threadHold);
                        crowdDensityQuery.setStartTime(DateUtil.formatDate(new Date(startTime), DateUtil.FORMAT_6));
                        crowdDensityQuery.setEndTime(DateUtil.formatDate(new Date(lastTime), DateUtil.FORMAT_6));
                        Page pages = crowdDensityService.getDensityResultList(crowdDensityQuery);
                        List<CrowdDensity> crowdDensities = pages.getRecords();
                        log.info(uuid + " 数据量:" + crowdDensities.size());
                        if (!crowdDensities.isEmpty() || threadHold == 0) {
                            if (crowdDensities.size() > 0) {
                                log.info("alarm:" + crowdDensities.get(0).getCameraName() + "-" + crowdDensities.get(0).getPicUrl());
                            }
                            String username = DbPropUtil.getString("alarm_technology_username", "admin");
                            String pwd = DbPropUtil.getString("alarm_technology_pwd", "888888");
                            String urls = DbPropUtil.getString("alarm_technology_urls", "10.10.10.123");
                            String[] url = urls.split(",");
                            String content = username + ":" + pwd;
                            // 加密
                            BASE64Encoder encoder = new BASE64Encoder();
                            String author = encoder.encode(content.getBytes());
                            log.info(uuid + "alarm:" + url);
                            for (String u : url) {
                                String send = "http://" + u + "/cdor.cgi?open=a";
                                String result = HttpU2sGetUtil.getHttp(send, author);
                                log.info("result:" + result);
                            }

                        }
                        lastTime = currentTime;
                        Thread.sleep(distan * 1000);
                    } catch (InterruptedException var2) {
                        var2.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
    }
}
