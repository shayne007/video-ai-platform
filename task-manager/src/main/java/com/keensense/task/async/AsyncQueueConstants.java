package com.keensense.task.async;

/**
 * @ClassName: AsyncQueueConstants
 * @Description: 异步消息队列常量类
 * @Author: cuiss
 * @CreateDate: 2020/4/14 9:26
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class AsyncQueueConstants {

    /***
     * 错误码定义
     * 1XX doBefore异常
     * 200 消息处理成功
     * 3XX 执行器异常
     *
     */

    /**
     * 消息过期异常
     */
    public static final String TASK_ERROR_MESSAGE_OVERTIME = "100";

    /**
     * 消息处理成功
     */
    public static final String TASK_ERROR_MESSAGE_OK= "200";

    /**
     * 消息在执行器中失败
     */
    public static final String TASK_ERROR_MESSAGE_EXECUTE_FAILED= "300";

    /**
     * 消息重试次数上限
     */
    public static final  int TASK_RETRYCOUNT_LIMIT = 50;

    /**
     * 分析打点轨迹队列名
     */
    public static final String AYALYSIS_TRACK_QUEUE_NAME = "analysis_track";

    /**
     * 分析打点轨迹消息处理执行器
     */
    public static final Class AYALYSIS_TRACK_CLASS = com.keensense.task.async.executor.analysistrack.AnalysisTrackTaskExecutor.class;



}
