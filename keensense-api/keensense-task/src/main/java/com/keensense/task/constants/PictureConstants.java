package com.keensense.task.constants;

/**
 * @Description: 图片结构化接口
 * @Author: wujw
 * @CreateDate: 2019/5/27 15:11
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class PictureConstants {

    private PictureConstants(){}

    /**最大上传文件长度*/
    public static final long MAX_UPLOAD_SIZE = 1024 * 1024 * 2L;

    /**restful接口*/
    public static final String REST_URL = "127.0.0.1:8100";
    /**rest获取图片矢量特征*/
    public static final String REST_GET_FEATURE_URL = "/v8/images/features";
    /**rest比较图片相似度*/
    public static final String REST_COMPARE_FEATURE_URL = "/v8/images/compare";
    /**rest获取图片结构化*/
    public static final String REST_GET_OBJECT_URL = "/v8/images/objects";
    /**rest 请求响应成功*/
    public static final String REQUEST_CODE_SUCCESS = "0";

    public static final String GSTL_REQUEST_SUCCESS = "200";
    /**字符集*/
    public static final String CHARACTER_ENCODING = "UTF-8";
    /**请求参数类型*/
    public static final String CONTENT_TYPE = "application/json";

    /** 对象类型，0-全部 1-人；2-车；3-人脸；4-人骑车*/
    public static final int OBJ_TYPE_ALL = 0;
    public static final int OBJ_TYPE_HUMAN = 1;
    public static final int OBJ_TYPE_VEHICLE = 2;
    public static final int OBJ_TYPE_FACE = 3;
    public static final int OBJ_TYPE_BIKE = 4;


    private static final String OBJ_NAME_HUMAN = "human";
    private static final String OBJ_NAME_VEHICLE = "vehicle";
    private static final String OBJ_NAME_FACE = "face";
    private static final String OBJ_NAME_BIKE = "bike";
    private static final String OBJ_NAME_OTHER = "other";

    public static String getObjTypeName(int objType) {
        switch (objType) {
            case OBJ_TYPE_HUMAN:
                return OBJ_NAME_HUMAN;
            case OBJ_TYPE_VEHICLE:
                return OBJ_NAME_VEHICLE;
            case OBJ_TYPE_FACE:
                return OBJ_NAME_FACE;
            case OBJ_TYPE_BIKE:
                return OBJ_NAME_BIKE;
            default:
                return OBJ_NAME_OTHER;
        }
    }

    public static Integer getObjTypeCode(String objName) {
        switch (objName) {
            case OBJ_NAME_HUMAN:
                return OBJ_TYPE_HUMAN;
            case OBJ_NAME_VEHICLE:
                return OBJ_TYPE_VEHICLE;
            case OBJ_NAME_FACE:
                return OBJ_TYPE_FACE;
            case OBJ_NAME_BIKE:
                return OBJ_TYPE_BIKE;
            default:
                return 0;
        }
    }

}
