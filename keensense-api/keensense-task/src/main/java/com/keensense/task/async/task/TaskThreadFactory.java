package com.keensense.task.async.task;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: TaskThreadFactory
 * @Description: 任务线程工厂类
 * @Author: cuiss
 * @CreateDate: 2019/8/11 11:44
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class TaskThreadFactory implements ThreadFactory {
    /**
     * 线程池个数
     */
    private static final AtomicInteger poolNum = new AtomicInteger(1);

    /**
     * 线程个数
     */
    private final AtomicInteger threadNum = new AtomicInteger(1);

    private final String DEFAULT_THREAD_NAME = "task";

    /**
     * 线程名称前缀
     */
    @Setter @Getter
    private String namePrefix;

    @Setter @Getter
    private ThreadGroup threadGroup;

    public TaskThreadFactory(){
        SecurityManager sm =  System.getSecurityManager();
        this.threadGroup = sm != null ? sm.getThreadGroup():Thread.currentThread().getThreadGroup();
        this.namePrefix = "pool-" + poolNum.getAndIncrement() + "-"+DEFAULT_THREAD_NAME+"-";
    }

    public TaskThreadFactory(String threadName){
        SecurityManager sm =  System.getSecurityManager();
        this.threadGroup = sm != null ? sm.getThreadGroup():Thread.currentThread().getThreadGroup();
        this.namePrefix = "pool-" + poolNum.getAndIncrement() + "-"+threadName+"-";
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(this.threadGroup,r,this.namePrefix+threadNum.getAndIncrement());

        if(thread.isDaemon()){
            thread.setDaemon(false);
        }

        if(thread.getPriority() != 5){
            thread.setPriority(5);
        }
        return thread;
    }
}
