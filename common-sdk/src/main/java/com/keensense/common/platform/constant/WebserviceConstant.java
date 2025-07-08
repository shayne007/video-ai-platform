package com.keensense.common.platform.constant;

/**
 * 快检对接视图库接口名称常量定义
 *
 * @author Administrator
 */
public class WebserviceConstant {

    /*************        结构化分析接口          **************/

    /**
     * 添加视频结构化任务接口  http://ip:port/u2s/rest/taskManage/addVideoObjextTask
     * <p>
     * 参数：
     * serialNumber	任务序列号
     * type	任务类型	 	objext：全目标（默认） vlpr: 车辆   face： 人脸
     * url	视频路径	支持实时流、在线视频、ftp、本地、基监控平台设备ID
     * param	扩展属性字段
     * output	任务结果输出定义
     */
    public static final String ADD_VIVEO_OBJECT_TASK = "/rest/taskManage/addTask";
    /**
     * 添加视频结构化任务接口  http://ip:port/u2s/rest/taskManage/addVideoObjextTask
     * <p>
     * 参数：
     * serialNumber	任务序列号
     * type	任务类型	 	objext：全目标（默认） vlpr: 车辆   face： 人脸
     * url	视频路径	支持实时流、在线视频、ftp、本地、基监控平台设备ID
     * param	扩展属性字段
     * output	任务结果输出定义
     */
    public static final String ADD_IMAGE_QUERY_TASK = "/rest/taskManage/addImageQueryTask";
    /**
     * 获取单个任务分析结果  http://ip:port/u2s/rest/taskManage/addVideoObjextTask
     * <p>
     * 参数  ： serialNumber
     */
    public static final String GET_VIDEO_TASK_DETAIL = "/rest/taskManage/getVideoObjectTaskDetailList";
    /**
     * 新增以图搜图任务 http://ip:port/u2s/rest/taskManage/addVideoObjextTask
     * <p>
     * 参数  ： serialNumber	获取单个任务分析结果
     */
    public static final String ADD_IMAGE_SEARCH = "/rest/taskManage/addImageQueryTaskTZ";
    /**
     * 新增二次搜图任务 http://ip:port/u2s/rest/taskManage/addVideoObjextTask
     * <p>
     * 参数  ： serialNumber	获取单个任务分析结果
     */
    public static final String ADD_IMAGE_SEARCH_SECOND = "/rest/taskManage/addImageQuerySecondTask";
    /**
     * 获取以图搜图任务进度 http://ip:port/u2s/rest/taskManage/addVideoObjextTask
     * <p>
     * 参数  ： serialNumber
     */
    public static final String QUERY_IMAGE_SEARCH_PROGRESS = "/rest/taskManage/queryImageSearchTaskProgressTZ";
    /**
     * 获取以图搜图任务进度 http://ip:port/u2s/rest/taskManage/addVideoObjextTask
     * <p>
     * 参数  ： serialNumber
     */
    public static final String GET_IMAGE_QUERT_TASK_LIST = "/rest/taskManage/getImageQueryTaskList";

    /**
     * 删除二次搜图任务结果
     * <p>
     * 参数 ： serialNumber  objid imgurl
     */
    public static final String DEL_IMAGE_QUERY_SECONDTASK = "/rest/taskManage/delImageQuerySecondTask";

    /**
     * 获取以图搜图任务进度 http://ip:port/u2s/rest/taskManage/addVideoObjextTask
     * <p>
     * 参数  ： serialNumber
     */
    public static final String GET_IMAGE_QUERT_RESULT_LIST = "/rest/taskManage/getImageQueryResultList";
    /**
     * 获取以图搜图任务进度 http://ip:port/u2s/rest/taskManage/addVideoObjextTask
     * <p>
     * 参数  ： serialNumber
     */
    public static final String DELETE_TASK_TZ = "/rest/taskManage/deleteVideoObjectTask";
    /**
     * 获取以图搜图任务结果 http://ip:port/u2s/rest/taskManage/addVideoObjextTask
     * <p>
     * 参数  ： serialNumber
     */
    public static final String QUERY_IMAGE_SEARCH_RESULT = "/rest/taskManage/queryImageSearchTaskResultTZ";

    /**
     * 删除视频结构化任务  http://ip:port/u2s/rest/taskManage/deleteVideoObjectTask
     * <p>
     * 参数  ： serialNumber	任务序列号
     */
    public static final String DELETE_VIDEO_OBJECT_TASK = "/rest/taskManage/deleteVideoObjectTask";

    /**
     * 查询结构化任务列表   http://ip:port/u2s/rest/taskManage/getVideoObjectTaskList
     * <p>
     * 参数：
     * serialNumber	任务序列号
     * startTime	起始时间
     * endTime	结束时间
     * type	任务类型
     * status	任务状态
     * pageNo	当前页
     * pageSize	分页条数
     */
    public static final String GET_VIDEO_OBJECTTASK_LIST = "/rest/taskManage/getVideoObjectTaskList";

    /**
     * 分析任务进度查询
     */
    public static final String GET_ALL_VIDEO_OBJECTTASK_LIST = "/rest/taskManage/getAllVideoObjectTaskList";

    /**
     * 视频结构化平台以图搜图结果图片接口  http://ip:port/u2s/rest/taskManage/getImageSearchObjectResultList
     * objType	目标类型	数字	是	1-人 2-车 4-人骑车
     * recogIdList	 目标列表  字符  是	根据分析ID可以查询目标详情，
     * 格式:recogId =date + uuid
     * uuid：以图搜图接口输出的uuid, date:以图搜图输出的date 查询多个recogId 以英文状态下“,”逗号分隔
     */
    //public static final String GET_IMAGE_SEARCH_OBJECT_RESULT_LIST = "/rest/taskManage/getImageSearchObjectResultList";
    public static final String GET_IMAGE_SEARCH_OBJECT_RESULT_LIST = "/rest/taskManage/getAllResultList";
    /**
     * 获取所有的任务数据列表
     */
    public static final String GET_ALLOBJECT_RESULT_LIST = "/rest/taskManage/getAllResultList";
    /**
     * 获取结构化结果信息(人)
     */
    public static final String GET_PERSON_BY_PARAM = "/rest/taskManage/getPersonObjectResultList";

    /**
     * 获取结构化结果信息(人骑车)
     */
    public static final String GET_BIKE_BY_PARAM = "/rest/taskManage/getBikeObjectResultList";

    /**
     * 获取结构化结果信息(车)
     */
    public static final String GET_CAR_BY_PARAM = "/rest/taskManage/getCarObjectResultList";


    /**
     * 暂停实时任务接口
     */
    public static final String PAUSE_VIDEO_OBJECTTASK = "/rest/taskManage/pauseVideoObjectTask";
    /**
     * 继续实时任务接口
     */
    public static final String CONTINUE_VIDEO_OBJECTTASK = "/rest/taskManage/continueVideoObjectTask";

    public static final String ADD_VIDEO_OBJEXT_TASK = "/rest/taskManage/addVideoObjextTask";

    /**
     * 更新感兴趣区域
     */
    public static final String UPDATE_VIDEO_OBJEXT_TASK = "/rest/taskManage/updateUdrSetting";

    /**
     * 视频结构化算法以图搜图接口  http://ip:port/rest/feature/search
     * objType	目标类型	数字		是	1-人 2-车 4-人骑车
     * picture	图片	字符串		是	图片数据base64转换后的字符串或图片的url路径，支持http、ftp
     * starttime	搜索起始时间	字符串		是	格式：yyyy-MM-dd HH:mm:ss
     * endtime	搜索截止时间	字符串		是	格式：yyyy-MM-dd HH:mm:ss
     * feature	图片特征
     * serialNumbers	任务编号列表	字符串		否	serialNumbers不为空，且全部是离线任务时，starttime、endtime可为空
     */
    public static final String IMAGE_FEATURE_SEARCH = "/rest/feature/search";

    /**
     * 语义搜接口，根据筛选条件进行语义搜，返回的有目标的点位集合及各个点位第一个目标快照信息
     */
    public static final String GET_ALLRESULTCOUNT_BYCAMERA = "/rest/taskManage/getAllResultCountByCamera";

    /* 图片结构化接口*/
    public static final String IMAGES_RECOG = "/images/recog";

    /**
     * 录像任务重试
     */
    public static final String RETRY_VTASK_INTER_NAME = "/rest/taskManage/retryTask";

    /**
     * 停止图片流任务
     */
    public static final String  STOP_PICTURE_TASK = "/rest/taskManage/stopPictureObjextTask";

    /**
     * 开始图片流任务
     */
    public static final String  START_PICTURE_TASK = "/rest/taskManage/startPictureObjextTask";

    /**
     * 删除图片流任务
     */
    public static final String  DELETE_PICTURE_TASK = "/rest/taskManage/deletePictureObjextTask";

    public static final String  DELETE_PICTURE_TASK_ASYNC = "/VIID/Result/Delete/Async";

    /**
     * 图片流处理
     */
    public static final String  LOAD_PICTURE_STREAM = "/picturestream/loadPicture";

    /**
     * 调用视图库FDFS模块存储图片数据接口
     */
    public static final String  SAVE_FDFS_IMAGE = "/VIID/Images/";

    /**
     * 调用视图库dump接口
     */
    public static final String FEATURE_DUMP = "/VIID/Feature/Dump";
}
