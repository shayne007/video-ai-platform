package com.keensense.schedule.timer;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.keensense.schedule.utils.IoUtil;
import com.keensense.schedule.vo.FaceResult;
import com.keensense.schedule.vo.ImageResult;
import com.keensense.schedule.vo.NonMotorVehiclesResult;
import com.keensense.schedule.vo.PersonResult;
import com.keensense.schedule.vo.Result;
import com.keensense.schedule.vo.VlprResult;
import com.loocme.plugin.spring.comp.SqlExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 将推送Es失败的数据，通过定时任务进行补偿
 * Created by memory_fu on 2020/5/11.
 */
@Slf4j
@Component
public class DataCompensateTimer {

    //ES数据源
    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;
    @Value("${es.save.pool.size}")
    private int poolSize;//es入库线程池大小
    @Value("${es.save.fail.path}")
    private String path;//es失败数据路径
    private ExecutorService threadPoolEs = null;


    @PostConstruct
    public void init() {
        threadPoolEs = Executors.newFixedThreadPool(poolSize);
    }

    @Scheduled(cron = "0 0/30 * * * ? ")
    public void dataCompensateForEs() {

        try {
            List<String> fileByCatalog = IoUtil.getFileByCatalog(path);

            log.info("====start dataCompensateForEs,fileByCatalog count is {}.",
                    fileByCatalog.size());
            for (String file : fileByCatalog) {
                threadPoolEs.submit(() -> {
                    try {
                        saveEs(file);
                    } catch (Exception e) {
                        log.error("====saveEs exception:", e);
                    }
                });
            }
        } catch (Exception e) {
            log.error("====dataCompensateForEs exception:", e);
        }

    }

    /**
     * 推送es数据
     */
    private void saveEs(String file) throws Exception {

        List<String> objs = IoUtil.readByFilePath(file);

        List<Object> objects = Lists.newArrayList();
        for (String line : objs) {
            JSONObject jsonObject = JSONObject.parseObject(line);
            Set<String> keySet = jsonObject.keySet();
            for (String key : keySet) {
                if (StringUtils.isEmpty(key) || !key.contains("Result")) {
                    continue;
                }
                Result result = null;
                String value = jsonObject.getString(key);
                if ("ImageResult".equals(key)) {
                    ImageResult imageResult = JSONObject.parseObject(value, ImageResult.class);
                    objects.add(imageResult);
                    continue;
                } else if ("PersonResult".equals(key)) {
                    result = JSONObject.parseObject(value, PersonResult.class);
                } else if ("VlprResult".equals(key)) {
                    result = JSONObject.parseObject(value, VlprResult.class);
                } else if ("FaceResult".equals(key)) {
                    result = JSONObject.parseObject(value, FaceResult.class);
                } else if ("NonMotorVehiclesResult".equals(key)) {
                    result = JSONObject.parseObject(value, NonMotorVehiclesResult.class);
                }
                objects.add(result);
            }
        }

        SqlExecutor executor = SqlExecutor.getInstance(dataSource);
        for (Object obj : objects) {
            executor.insert(obj);
        }
        int r = executor.commit();

        if (objects.size() == r) {
            log.info("====saveEs succ,commit count is {},delete file {}", r, file);
            IoUtil.deleteFile(file);
        } else {
            log.error("=====saveEs fail, Do not delete files.");
        }
    }

}
