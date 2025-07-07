/**
 * All rights Reserved, Designed By qianshitong
 *
 * @Title: AlarmHandleTimer.java
 * @Package cn.jiuling.recog.alarm
 * @Description: 告警处理类
 * @version V1.0
 * @Copyright: 2019 www.1000video.com.cn Inc. All rights reserved.
 * 注意：本内容仅限于苏州千视通视觉科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
package com.keensense.alarm.service.consumer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.keensense.alarm.entity.DispositionEntity;
import com.keensense.alarm.service.IDispositionService;
import com.keensense.common.config.NacosConfigCenter;
import com.keensense.common.constant.AlarmConstant;
import com.keensense.common.util.DispositionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: AlarmHandleTimer
 * @Description:定时从redis获取数据进行去重存入队列进行处理 并定期清理map数据
 * @author: admin
 * @date: 2019年3月21日 下午2:21:24
 */
@Component
@Slf4j
@EnableScheduling
public class AlarmHandlerTimer {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private NacosConfigCenter configCenter;

    private static final String CONSUME_FLAG = "checked_";

    /**
     * @throws
     * @Title: putData2Queue
     * @Description: 定时从redis获取数据并去重存入队列
     * @param:
     * @return: void
     */
    @Scheduled(cron = "*/1 * * * * ?")
    public void putData2Queue() {
        if (configCenter.isAlarmStart()) {
            Set<String> keys = redisTemplate.keys("*");
            if (keys == null) {
                return;
            }
            List<String> msgs = Lists.newArrayList();
            keys.stream().filter(key -> !key.startsWith(CONSUME_FLAG)).forEach(key -> {
                Boolean setNX = redisTemplate.opsForValue()
                        .setIfAbsent(CONSUME_FLAG + key, "1", 60, TimeUnit.SECONDS);
                if (setNX != null && setNX) {
                    String msg = redisTemplate.opsForValue().get(key);
                    if (!StringUtils.isEmpty(msg)) {
                        checkMsg(msgs, msg);
                        redisTemplate.delete(Lists.newArrayList(key, CONSUME_FLAG + key));
                        log.info("id {} is added. msg is {}", key, msg.substring(0, 20));
                    } else {
                        log.error("id {} value is null", key);
                    }
                } else {
                    log.info("id {} is already consumed", key);
                }
            });
            if (!msgs.isEmpty()) {
                Iterable<List<String>> partition = Iterables.partition(msgs, AlarmConstant.RECEIVE_MESSAGE_SIZE);
                partition.forEach(AlarmQueue::addMsgsToQueue);
            }
        }
    }

    private void checkMsg(List<String> msgs, String msg) {
        if (msg.contains("FaceObject")) {
            msgs.add(msg);
        } else if (msg.contains("NonMotorVehicleObject")) {
            //非机动车布控
        } else if (msg.contains("MotorVehicleObject")) {
            msgs.add(msg);
        }
    }

    @Autowired
    private IDispositionService dispositionService;

    /**
     * 查询布控
     */
    @Scheduled(cron = "*/1 * * * * ?")
    public void queryDisposition() {
        LocalDateTime now = LocalDateTime.now();
        List<DispositionEntity> dispositions = dispositionService
                .list(Wrappers.<DispositionEntity>lambdaQuery()
                        .eq(DispositionEntity::getOperateType, true)
                        .le(DispositionEntity::getBeginTime, now)
                        .ge(DispositionEntity::getEndTime, now));
        DispositionUtil.emit(dispositions);
    }


    /**
     * 5s 修改告警任务状态
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void changeDispositionStatus() {
        dispositionService.update(Wrappers.<DispositionEntity>lambdaUpdate()
                .ge(DispositionEntity::getBeginTime, new Date())
                .notIn(DispositionEntity::getDispositionStatus, 0, 9)
                .setSql("disposition_status = 9"));
        dispositionService.update(Wrappers.<DispositionEntity>lambdaUpdate()
                .le(DispositionEntity::getBeginTime, new Date())
                .eq(DispositionEntity::getDispositionStatus, 9)
                .setSql("disposition_status = 1"));
        dispositionService.update(Wrappers.<DispositionEntity>lambdaUpdate()
                .le(DispositionEntity::getEndTime, new Date())
                .notIn(DispositionEntity::getDispositionStatus, 0, 2)
                .setSql("disposition_status = 2"));

    }

}
