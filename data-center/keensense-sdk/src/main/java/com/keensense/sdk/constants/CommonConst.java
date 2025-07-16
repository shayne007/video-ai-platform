package com.keensense.sdk.constants;

import com.keensense.sdk.algorithm.impl.GlFullFaceSdkInvokeImpl;
import com.keensense.sdk.algorithm.impl.StFaceSdkInvokeImpl;

public class CommonConst {

    // 对象类型，1-人；2-车；3-人脸；4-人骑车
    public static final int OBJ_TYPE_HUMAN = 1;
    public static final int OBJ_TYPE_VEHICLE = 2;
    public static final int OBJ_TYPE_FACE = 3;
    public static final int OBJ_TYPE_BIKE = 4;

    public static final int QST_FIRM = 0;
    public static final int GL_FULL_FIRM = 1;
    public static final int ST_FIRM = 2;

    public static String getObjTypeName(int objType) {
        switch (objType) {
            case OBJ_TYPE_HUMAN:
                return "human";
            case OBJ_TYPE_VEHICLE:
                return "vehicle";
            case OBJ_TYPE_FACE:
                return "face";
            case OBJ_TYPE_BIKE:
                return "bike";
            default:
                return null;
        }
    }

    public static String getObjTypeCode(String objName) {
        switch (objName) {
            case "human":
                return "1";
            case "vehicle":
                return "2";
            case "face":
                return "3";
            case "bike":
                return "4";
            default:
                return null;
        }
    }

    public static final String FEATURE_EXTRACT_TYPE_JNI = "JNI";
    public static final String FEATURE_EXTRACT_TYPE_HTTP = "HTTP";
    public static final String REQUEST_CODE_SUCCESS = "0";

    public static final String GSTL_REQUEST_SUCCESS = "200";

    /**
     * restful接口
     */
    public static final String REST_URL = "127.0.0.1:8100";
    public static final String REST_GET_FEATURE_URL = "/v8/images/features";            //rest获取图片矢量特征
    public static final String REST_COMPARE_FEATURE_URL = "/v8/images/compare";        //rest比较图片相似度
    public static final String REST_GET_OBJECT_URL = "/v8/images/objects";            //rest获取图片结构化
    /**
     * KS
     */
    public static final Integer IMAGES_OBJECTS_SCENES = 0;
    /**
     * ks是否检测整张图片，只有在scenes=8动态人脸时才有效。0-表示输入图片是人脸证件照；1-表示检测整张图片，一般为检测视频画面中的人脸；默认值为1
     */
    public static final Integer IMAGES_OBJECTS_IS_DETECT_FULL_FRAME = 1;

    /**
     * 人脸检测接口
     */
    public static final String FACE_SERVICE_REST_HOST = "127.0.0.1:18051";
    /**
     * 人脸检测接口URL
     */
    public static final String FACE_SERVICE_REST_URL = "/service/qstface/getFacesOnImageBase64";


    public static int whichFirm(){
        int firm = QST_FIRM;
        if(FaceConstant.getFaceSdkInvoke() instanceof GlFullFaceSdkInvokeImpl){
            firm = GL_FULL_FIRM;
        }else if(FaceConstant.getFaceSdkInvoke() instanceof StFaceSdkInvokeImpl){
            firm = ST_FIRM;
        }
        return firm;
    }
}
