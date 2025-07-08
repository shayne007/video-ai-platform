package com.keensense.dataconvert.biz.task.constant;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.task.constant
 * @Description： <p> TaskConstant  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/24 - 11:39
 * @Modify By：
 * @ModifyTime： 2019/7/24
 * @Modify marker：
 */
public class TaskConstant {


    private TaskConstant() {}

    /**
     * 每核Cpu负载的最大线程队列数
     */
    public static final float POOL_SIZE = 1.5f;

    /**
     * 线程
     */
    public static final int THREAD_NUM;

    /**
     * 静态代码块初始化数据-CPU核心数
     */
    static {
        //cpu核数
        int cpuNums = Runtime.getRuntime().availableProcessors();
        //1.5倍扩充
        float mathNum = cpuNums * TaskConstant.POOL_SIZE;
        //可以支持的线程数
        THREAD_NUM = (int) mathNum;
    }

}
