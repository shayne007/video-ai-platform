package com.keensense.task.constants;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 任务相关常量类
 * @Author: wujw
 * @CreateDate: 2019/5/9 15:35
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class TaskConstants {

    private TaskConstants(){}

    /** 任务类型列表*/
    private static final List<String> ANALY_TYPE_LIST = new ArrayList<>(4);
    /** scene类型列表*/
    private static final List<Integer> SCENE_LIST = new ArrayList<>(3);
    /** 结构化任务*/
    public static final String ANALY_TYPE_OBJEXT = "objext";
    /** 浓缩任务*/
    public static final String ANALY_TYPE_SUMMARY = "summary";
    /** 抓拍机任务*/
    public static final String ANALY_TYPE_PICTURE = "picture";
    /** face人脸提取*/
    public static final String ANALY_TYPE_FACE = "face";
    /** 人群密度任务*/
    public static final String ANALY_TYPE_DESITTY = "personDensity";
    /** 交通任务*/
    public static final String ANALY_TYPE_TRAFFIC = "traffic";
    /** 只添加任务不分析*/
    public static final String ANALY_TYPE_OTHER = "other";

    /** 车牌识别*/
    public static final String ANALY_TYPE_VLPR = "vlpr";

    /** 抓拍机、实时任务*/
    public static final int TASK_TYPE_ONLINE = 1;
    /** 离线任务*/
    public static final int TASK_TYPE_OFFLINE = 2;
    /** 联网录像任务*/
    public static final int TASK_TYPE_VIDEO = 3;
    /** 浓缩任务*/
    public static final int TASK_TYPE_SUMMARY = 4;

    /** 任务已删除*/
    public static final int TASK_DELETE_TURE = 2;
    /** 任务未删除*/
    public static final int TASK_DELETE_FALSE = 0;

    /**任务展示状态：等待分析 (1,0)*/
    public static final int TASK_SHOW_STATUS_WAIT = 0;
    /**任务展示状态：分析中 (1,1)*/
    public static final int TASK_SHOW_STATUS_RUNNING = 1;
    /**任务展示状态：成功 (1,2),(0,2)*/
    public static final int TASK_SHOW_STATUS_SUCCESS = 2;
    /**任务展示状态：失败 (1,3),(0,3)(0,0)*/
    public static final int TASK_SHOW_STATUS_FAILED = 3;
    /**任务展示状态：停止中 (0,1)*/
    public static final int TASK_SHOW_STATUS_STOPPING = 4;

    /**任务启动状态: 启动*/
    public static final int TASK_ISVALID_ON = 1;
    /**任务启动状态: 未启动*/
    public static final int TASK_ISVALID_OFF = 0;

    /**任务运行状态：等待分析*/
    public static final int TASK_STATUS_WAIT = 0;
    /**任务运行状态：分析中*/
    public static final int TASK_STATUS_RUNNING = 1;
    /**任务运行状态：成功*/
    public static final int TASK_STATUS_SUCCESS = 2;
    /**任务运行状态：失败*/
    public static final int TASK_STATUS_FAILED = 3;

    /*任务重试次数最大值*/
    public  static  final int MAX_RETRYCOUNT = 	32767;

    /**转码，下载，分析操作状态 1-实时流无须处理*/
    public static final int OPERAT_TYPE_NO_DO = 0;
    /**转码，下载，分析操作状态 1-待操作*/
    public static final int OPERAT_TYPE_WATI = 1;
    /**转码，下载，分析操作状态 2-处理中*/
    public static final int OPERAT_TYPE_RUNNING = 2;
    /**转码，下载，分析操作状态 3-成功*/
    public static final int OPERAT_TYPE_SUCCESS = 3;
    /**转码，下载，分析操作状态 4-失败*/
    public static final int OPERAT_TYPE_FAILED = 4;
    /**转码，下载，分析操作状态 5-异常状态*/
    public static final int OPERAT_TYPE_ERROR = 5;

    /**slave表节点状态 1-启用*/
    public static final int SLAVE_VALID_ON = 1;
    /**slave表节点状态 0-停止*/
    public static final int SLAVE_VALID_OFF = 0;

    /**进度最大值*/
    public static final int PROGRESS_MAX_VALUE = 100;
    /**重用分片进度*/
    public static final int PROGRESS_REUSE = -100;
    /**失败分片进度*/
    public static final int PROGRESS_FAILED = -1;

    /**常用参数的值*/
    public static final String ANALYSIS_CFG_SCENE = "scene";
    public static final String ANALYSIS_CFG_OBJMINSIZE = "objMinSize";
    public static final String ANALYSIS_CFG_OBJMAXSIZE = "objMaxSize";
    public static final String ANALYSIS_CFG_OUTPUTDSFACTOR = "outputDSFactor";
    public static final String ANALYSIS_CFG_SENSITIVITY = "sensitivity";
    public static final String ANALYSIS_CFG_OBJMINTIMEINMS = "objMinTimeInMs";
    public static final String ANALYSIS_CFG_DOMINANTCOLOR = "dominantColor";

    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    public static final String STATUS = "status";
    public static final String SERIALNUMBER = "serialnumber";

    /**
     * 默认的分析优先级
     */
    public static final Integer TASK_DEFAULT_PRIORITY = 20;

    /**
     * 默认的分析类型：全目标结构化
     */
    public static final String TASK_DEFAULT_ANALYSIS_TYPE = "1,2,3,4";

    /**
     * 默认的任务类型:1/实时任务;2/离线视频;3/联网录像;4/浓缩
     */
    public static final String TASK_DEFAULT_TASK_TYPE = "1,2,3";


    /**
     * 由于优先级退出的任务
     * no priority task
     */
    public static final Integer TASK_ERROR_CODE_NO_PRIORITY = -34;

    /**
     * APP没有路数的错误码
     * no available work
     */
    public static final Integer TASK_ERROR_CODE_NO_WORKER = -33;

    /**
     * APP没有硬件资源的错误码
     * no cpu/memory resource
     */
    public static final Integer TASK_ERROR_CODE_NO_RESOURCE = -32;

    /**scene 0-默认场景*/
    private static final int SCENE_DEFAULT = 0;
    /**scene 1-一般场景*/
    private static final int SCENE_GENERAL = 1;
    /**scene 2-卡口、类卡口场景*/
    private static final int SCENE_BAYONET = 2;
    /**scene 4-交通流量统计场景*/
    private static final int SCENE_TRAFFIC = 4;
    /**scene 8-动态人脸场景*/
    private static final int SCENE_FACE = 8;
    /**scene 16-混合行人及动态人脸*/
    private static final int SCENE_HUMAN = 16;

    /** ZK任务类型 0-结构化分析 1-车辆分析 2-人脸分析 3-交通任务分析 */
    private static final int ZK_ANALY_TYPE_OBJEXT = 0;
    private static final int ZK_ANALY_TYPE_VLPR = 1;
    private static final int ZK_ANALY_TYPE_FACE = 2;
    private static final int ZK_ANALY_TYPE_TRAFFIC = 3;

    static{
        ANALY_TYPE_LIST.add(ANALY_TYPE_OBJEXT);
        ANALY_TYPE_LIST.add(ANALY_TYPE_SUMMARY);
        ANALY_TYPE_LIST.add(ANALY_TYPE_PICTURE);
        ANALY_TYPE_LIST.add(ANALY_TYPE_DESITTY);
        ANALY_TYPE_LIST.add(ANALY_TYPE_TRAFFIC);
        ANALY_TYPE_LIST.add(ANALY_TYPE_OTHER);

        SCENE_LIST.add(SCENE_DEFAULT);
        SCENE_LIST.add(SCENE_GENERAL);
        SCENE_LIST.add(SCENE_BAYONET);
        SCENE_LIST.add(SCENE_TRAFFIC);
        SCENE_LIST.add(SCENE_FACE);
        SCENE_LIST.add(SCENE_HUMAN);
    }

    /***
     * @description: 根据任务启动和运行状态，获取任务总状态
     * @param isValid 任务启动状态
     * @param status 任务运行状态
     * @return: int
     */
    public static int getStatus(int isValid, int status) {
        if (status == TASK_STATUS_SUCCESS) {
            return TASK_SHOW_STATUS_SUCCESS;
        } else if (status == TASK_STATUS_FAILED) {
            return TASK_SHOW_STATUS_FAILED;
        } else if (isValid == TASK_ISVALID_ON && status == TASK_STATUS_WAIT) {
            return TASK_SHOW_STATUS_WAIT;
        } else if (isValid == TASK_ISVALID_ON && status == TASK_STATUS_RUNNING) {
            return TASK_SHOW_STATUS_RUNNING;
        } else if (isValid == TASK_ISVALID_OFF && status == TASK_STATUS_RUNNING) {
            return TASK_SHOW_STATUS_STOPPING;
        } else if (isValid == TASK_ISVALID_OFF && status == TASK_STATUS_WAIT) {
            return TASK_SHOW_STATUS_FAILED;
        } else {
            return -1;
        }
    }

    public static List<String> getAnalyTypeList() {
        return ANALY_TYPE_LIST;
    }

    public static List<Integer> getSceneList() {
        return SCENE_LIST;
    }

    public static int getAnalyNum(String analyType){
        int rslt = -1;
        switch (analyType){
            case ANALY_TYPE_OBJEXT:
                rslt = ZK_ANALY_TYPE_OBJEXT;break;
            case ANALY_TYPE_FACE:
                rslt = ZK_ANALY_TYPE_FACE;break;
            case ANALY_TYPE_VLPR:
                rslt = ZK_ANALY_TYPE_VLPR;break;
            case ANALY_TYPE_TRAFFIC:
                rslt = ZK_ANALY_TYPE_TRAFFIC;break;
            default: break;
        }
        return rslt;
    }
}
