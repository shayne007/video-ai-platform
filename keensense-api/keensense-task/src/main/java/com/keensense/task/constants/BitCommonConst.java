package com.keensense.task.constants;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @Description: bit盒子相关常量
 * @Author: wujw
 * @CreateDate: 2019/5/23 17:07
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class BitCommonConst {

    private BitCommonConst() {
    }

    /***比特盒子接口:添加任务*/
    public static final String BIT_START_TASK = "/v8/videos/startTask";
    /***比特盒子接口:查询任务*/
    public static final String BIT_QUERY_TASK = "/v8/videos/status";
    /***比特盒子接口:停止任务*/
    public static final String BIT_STOP_TASK = "/v8/videos/stopTask";
    /***比特盒子接口:request请求成功*/
    public static final String BIT_SUCCESS = "OK";

    /**
     * 状态：成功
     */
    public static final int SUCCESS = 0;
    /**
     * 状态：失败
     */
    public static final int FAIL = 1;
    /**
     * 状态：异常
     */
    public static final int ERROR = 2;
    /**
     * 状态：进行中
     */
    public static final int PROCESS = 3;
    /**
     * 状态：完成
     */
    public static final int COMPLETE = 4;
    /**
     * 状态：未查找到任务
     */
    public static final int NOT_FOUND = 5;
    /**
     * 最大错误次数
     */
    public static final int MAX_FAIL_COUNT = 5;

    /**
     * 最小错误时间间隔 2分钟
     */
    public static final int MIN_FAIL_TIME = 2 * 60 * 1000;

    /**
     * 盒子满负载状态
     */
    public static final int EXECUTE_STATUS_TRUE = 1;
    /**
     * 盒子还有空闲路数
     */
    public static final int EXECUTE_STATUS_FALSE = 0;
    /**
     * 盒子状态正常
     */
    public static final int MACHINE_STATUS_NORMAL = 1;
    /**
     * 盒子状态异常
     */
    public static final int MACHINE_STATUS_ERROR = 0;

    /***
     * @description: 根据路径判断文件是否是离线文件
     * @param paramsStr 文件配置param
     * @return: boolean
     */
    public static boolean isRealTimeTask(String paramsStr) {
        JSONObject jsonObject = JSON.parseObject(paramsStr);
        int taskType = Optional.ofNullable(jsonObject).map(p -> p.getIntValue("taskType")).orElse(0);
        if(taskType == TaskConstants.TASK_TYPE_ONLINE){
            return false;
        }else if(taskType > TaskConstants.TASK_TYPE_ONLINE){
            return true;
        }else{
            if((paramsStr.contains("starttime") && paramsStr.contains("endtime"))
                    || (paramsStr.contains("startTime") && paramsStr.contains("stopTime"))){
                return true;
            }
            paramsStr = paramsStr.replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", "");
            return Pattern.matches("([\\s\\S]*)(\"url\":\\s*\"http|\"url\":\\s*\"ftp|\"url\":\\s*\"file)([\\s\\S]*)", paramsStr);
        }
    }

}
