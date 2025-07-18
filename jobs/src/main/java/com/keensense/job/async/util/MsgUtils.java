package com.keensense.job.async.util;


import com.keensense.job.async.Message;
import com.keensense.job.async.TaskCollects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationUtils;

import java.util.Map;

/**
 * @ClassName: MsgUtils
 * @Description: 消息处理类
 * @Author: cuiss
 * @CreateDate: 2019/8/10 14:10
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class MsgUtils {

    private static final int DEFAULT_TIMEOUT_SECONDS = 24*60*60;

    /**
     * 组装消息
     * @param param 消息内容
     * @param ssl  消息过期时间（单位：秒）
     * @return
     */
    public static final byte[] buildMessage(Map<String, Object> param,long ssl){
        long currTime = System.currentTimeMillis();
        String sequence = SeqUtil.getSequence();
        Message message = new Message();
        message.setTaskId(sequence);
        message.setTaskLogId(sequence);
        message.setTtl(ssl);
        //message.setTimeoutSecond(timeoutSecond);
        message.setSendtime(currTime);
        message.setParam(param);
        return SerializationUtils.serialize(message);

    }

    /**
     * 组装消息
     * @param param
     * @return
     */
    public static  final byte[] buildMessage(Map<String,Object> param){
        return buildMessage(param,DEFAULT_TIMEOUT_SECONDS);
    }

    /**
     * 消息发布至队列
     * @param queueName  队列名称
     * @param payload  消息内容
     */
    public static final void publish(byte[] queueName,byte[] payload){
        TaskCollects taskCollects = TaskCollects.getInstance();
        taskCollects.lpush(queueName,payload);
        log.info("消息发布至："+queueName.toString()+" success....");
    }
}
