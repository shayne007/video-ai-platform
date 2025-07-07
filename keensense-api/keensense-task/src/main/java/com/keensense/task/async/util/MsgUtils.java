package com.keensense.task.async.util;


import com.keensense.task.async.Message;
import com.keensense.task.async.task.TaskCollects;
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

    private static final int DEFAULT_TIMEOUT_SECONDS = 24*60*60*1000;

    private static final int DEFAULT_RETRYCOUNT = 0;

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
        message.setRetrycount(DEFAULT_RETRYCOUNT);
        message.setSendtime(currTime);
        message.setParam(param);
        return SerializationUtils.serialize(message);

    }

    /**
     * 重新组装消息，reload流程
     * @param message 消息内容
     * @return
     */
    public static final byte[] reloadMessage(Message message){
        long currTime = System.currentTimeMillis();
        message.setSendtime(currTime);
        message.setRetrycount(message.getRetrycount()+1);
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
        String queueNameStr = (String)com.keensense.task.async.util.SerializationUtils.deserialize(queueName);
        publish(queueNameStr,payload);
    }

    /**
     * 消息发布至队列
     * @param queueName  队列名称
     * @param payload  消息内容
     */
    public static final void publish(String queueName,byte[] payload){
        TaskCollects taskCollects = TaskCollects.getInstance();
        taskCollects.lpush(queueName,payload);
        Message message = (Message) com.keensense.task.async.util.SerializationUtils.deserialize(payload);
        log.info("消息发布至：{} success....,消息id:{}", queueName,message.getTaskId());
    }
}
