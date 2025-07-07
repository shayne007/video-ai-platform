/**
 * All rights Reserved, Designed By qianshitong
 *
 * @Title: AlarmHandle.java
 * @Package cn.jiuling.recog.alarm
 * @Description: 告警处理计算
 * @version V1.0
 * @Copyright: 2019 www.1000video.com.cn Inc. All rights reserved. 注意：本内容仅限于苏州千视通视觉科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
package com.keensense.alarm.service.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.alarm.entity.DispositionEntity;
import com.keensense.alarm.entity.DispositionNotificationEntity;
import com.keensense.alarm.service.IDispositionNotificationService;
import com.keensense.common.config.NacosConfigCenter;
import com.keensense.common.constant.AlarmConstant;
import com.keensense.common.util.DispositionUtil;
import com.loocme.sys.exception.HttpConnectionException;
import com.loocme.sys.util.PostUtil;
import com.loocme.sys.util.ThreadUtil;
import com.loocme.sys.util.ThreadUtil.ExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @ClassName: AlarmHandle
 * @Description:告警处理:从队列获取目标进行告警处理
 * @author: admin
 * @date: 2019年3月21日 下午4:15:49
 */
@Component
@Slf4j
public class AlarmHandler {

    private static final ExecutorService SERVICE = ThreadUtil.newSingleThreadExecutor();
    private static final ExecutorService EXECUTOR = ThreadUtil
            .newFixedThreadPool(AlarmConstant.THREAD_POOL_NUMBER);

    @Autowired
    private IDispositionNotificationService dispositionNotificationService;

    @Autowired
    private NacosConfigCenter configCenter;

    public void start() {
        boolean alarmStart = configCenter.isAlarmStart();
        SERVICE.execute(() -> {
            while (alarmStart) {
                try {
                    List<String> msgs = AlarmQueue.popFromQueue();
                    if (msgs == null || msgs.isEmpty()) {
                        continue;
                    }
                    handlerMsg(msgs);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
    }

    private void handlerMsg(List<String> msgs) {
        EXECUTOR.execute(() -> {
            List<DispositionNotificationEntity> result = dispositionNotificationService
                    .generateNotifications(msgs);
            if (result.isEmpty()) {
                return;
            }
            result.stream()
                    .collect(Collectors.groupingBy(DispositionNotificationEntity::getDispositionId))
                    .forEach((k, v) -> {
                        dispositionNotificationService.saveBatch(v);

                        DispositionEntity disp = DispositionUtil.getDispById(k);
                        if (disp == null) {
                            return;
                        }
                        String subscriberUrl = disp.getReceiveAddr();
                        //实时展示只推一条
                        DispositionNotificationEntity showNoti = v.get(0);
                        JSONObject cntObj = JSON.parseObject(showNoti.getCntObject());
                        showNoti.setCntObject("");
                        JSONObject pushObj = JSON.parseObject(JSON.toJSONString(showNoti));
                        String key = cntObj.keySet().toArray(new String[]{})[0];
                        JSONObject value = cntObj.getJSONObject(key);
                        value.remove("SubImageList");
                        value.remove("FeatureObject");
                        value.remove("Yaw");
                        value.remove("Blurry");
                        value.remove("Pitch");
                        value.remove("Roll");
                        value.remove("FaceQuality");
                        value.remove("BodyQuality");
                        value.remove("InfoKind");
                        pushObj.put(key, value);

                        JSONArray ja = new JSONArray();
                        ja.add(pushObj);
                        push2Subscriber(ja, subscriberUrl);
                    });

        });

    }

    private void push2Subscriber(JSONArray jarr, String subscriberUrl) {
        if (StringUtils.isNotEmpty(subscriberUrl) && subscriberUrl.startsWith("http://")) {
            CompletableFuture.runAsync(() -> {
                try {
                    PostUtil.requestContent(subscriberUrl, "application/json", jarr.toJSONString());
                } catch (HttpConnectionException e) {
                    log.error(jarr.toJSONString(), e);
                }

            });
        }
    }

}
