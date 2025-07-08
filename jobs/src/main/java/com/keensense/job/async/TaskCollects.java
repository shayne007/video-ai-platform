package com.keensense.job.async;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @ClassName: TaskCollects
 * @Description: 任务收集器
 * @Author: cuiss
 * @CreateDate: 2019/8/9 16:47
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class TaskCollects {

    private TaskCollects(){}

    private static volatile TaskCollects instance;

    /**
     * 任务列表
     */
    private static List taskList = Collections.synchronizedList(new ArrayList<>());

    /**
     * 双重加锁机制，保障线程安全
     * @return
     */
    public static TaskCollects getInstance(){
        synchronized (TaskCollects.class){
            if(instance == null){
                synchronized (TaskCollects.class){
                    instance = new TaskCollects();
                }
            }
        }
        return instance;
    }

    /**
     * 添加任务至指定队列
     * @param queueName  队列名称
     * @param payload  队列任务
     */
    public synchronized  void lpush(byte[] queueName,byte[] payload){
        Map<byte[],byte[]> taskMap = new HashMap<>();
        taskMap.put(queueName,payload);
        taskList.add(taskMap);
        log.info(">>>>>>>>任务发布到队列成功.....");
    }

    /**
     * 从指定队列中取任务
     * @param queueName
     * @return
     */
    public synchronized byte[] lpop(byte[] queueName){
        for(int i=0;i<taskList.size();i++){
            Map<byte[],byte[]> taskMap = (Map<byte[],byte[]>)taskList.get(i);
            if(taskMap.containsKey(queueName)){
                taskMap = (Map<byte[],byte[]>)taskList.remove(i);
                return taskMap.get(queueName);
            }
        }
        return null;
    }

}
