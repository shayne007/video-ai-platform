package com.keensense.task.constants;

/**
 * @Description: 任务清理常量类
 * @Author: wujw
 * @CreateDate: 2019/6/24 11:19
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class DeleteTaskConstants {

    private DeleteTaskConstants(){}

    /**清理类型 1-主动删除 2-定时任务删除*/
    public static final int CLEAN_DATA_SOURCE_ACTIVE = 1;
    public static final int CLEAN_DATA_SOURCE_AUTO = 2;

    /**清理进度 0-等待删除 1-执行中或已删除1次 100-执行成功 */
    public static final int CLEAN_STATUS_WAIT = 0;
    public static final int CLEAN_STATUS_RUNNING = 1;
    public static final int CLEAN_STATUS_SUCCESS = 100;

    /**请求返回 0-成功 */
    public static final int SUCCESS = 0;

    public static final int RETRY_COUNT_LIMIT = 5;
}
