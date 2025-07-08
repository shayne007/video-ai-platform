package com.keensense.admin.util;

/**
 * 数据库主键获取
 *
 * @author admin
 */
public class IdUtils {
    private IdUtils() {
    }

    /**
     * VSD_task表ID获取
     *
     * @return
     */
    public static Long getTaskId() {
        String taskIdStr = "1" + RandomUtils.getRandom6Number(9);
        return Long.valueOf(taskIdStr);
    }

    /**
     * si_task表serialnumber获取
     *
     * @return
     */
    public static String getSerialnumber() {
        return RandomUtils.getRandom32BeginTimePK();
    }

    /**
     * t_analysetask
     */
    public static String getU2sAnalysisTask() {
        return RandomUtils.get24TimeRandom();
    }

}
