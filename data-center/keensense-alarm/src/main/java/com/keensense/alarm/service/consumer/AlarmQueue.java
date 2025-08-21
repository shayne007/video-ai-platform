/**
 * All rights Reserved, Designed By qianshitong
 *
 * @Title: AlarmQueue.java
 * @Package cn.jiuling.recog.alarm
 * @Description: 告警队列实现类
 * @version V1.0
 * @Copyright: 2019 www.1000video.com.cn Inc. All rights reserved.
 * 注意：本内容仅限于苏州千视通视觉科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
package com.keensense.alarm.service.consumer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @ClassName: AlarmQueue
 * @Description:定义告警队列入队出队操作
 * @author: admin
 * @date: 2019年3月21日 下午2:08:15
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AlarmQueue {

    private static LinkedBlockingQueue<List<String>> readQueue = new LinkedBlockingQueue<>();

    public static void addMsgsToQueue(List<String> msgs) {
        readQueue.add(msgs);
    }

    public static List<String> popFromQueue() {
        try {
            return readQueue.take();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
            return null;
        }
    }
}
