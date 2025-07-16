package com.keensense.sdk.util;

import com.keensense.sdk.constants.CommonConst;
import com.keensense.sdk.jni.U2sRecogHttp;
import com.keensense.sdk.jni.U2sRecogNativeSingle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SDKUtils {
    //调用类型
    private static String FEATURE_EXTRACT_TYPE;

    public static void setFeatureExtractType(String FeatureExtractType) {
        FEATURE_EXTRACT_TYPE = FeatureExtractType;
    }

    public static String getFeatureExtractType() {
        return FEATURE_EXTRACT_TYPE;
    }

//    /**
//     * 提取图片特征
//     *
//     * @param objType
//     * @param pictureByte
//     * @return
//     */
//    public static String getFeatureFromJNI(Integer objType, byte[] pictureByte) {
//        byte[] featureBy = U2sRecogNativeSingle.GetFeature(objType, pictureByte);
//        String feature = new String(Base64.encode(featureBy));
//        log.info("feature byte length:" + feature.length());
//        return feature;
//    }

//    public static String getFeatureFrom(Integer objType, byte[] pictureByte) {
//        if (OESObjectHandleManagerUtil.getIsGstl() && objType == CommonConst.OBJ_TYPE_VEHICLE) {
//            return GstlRecogHttp.getExtractFromPictureByGstl(1, pictureByte);    //GSLT车辆图片特征获取 type默认为1
//        } else {
//            return getFeatureFromSDK(objType, pictureByte);
//        }
//    }

//    public static String getFeatureFromSDK(Integer objType, byte[] pictureByte) {
//        String featureStr = null;
//        if (SDKUtils.getFeatureExtractType().equals(CommonConst.FEATURE_EXTRACT_TYPE_JNI)) {
//            featureStr = SDKUtils.getFeatureFromJNI(objType, pictureByte);
//        } else if (SDKUtils.getFeatureExtractType().equals(CommonConst.FEATURE_EXTRACT_TYPE_HTTP)) {
//            featureStr = U2sRecogHttp.getExtractFromPictureByRest(objType, pictureByte);
//        }
//        return featureStr;
//    }

//    public static Float compareFeatureFrom(String feature1, String feature2) {
//        Float result = -1f;
//        try {
//            if (SDKUtils.getFeatureExtractType().equals(CommonConst.FEATURE_EXTRACT_TYPE_JNI)) {
//                U2sFeatureCompareNative compare = new U2sFeatureCompareNative();
//                byte[] tFeature = Base64.decode(feature1.getBytes());
//                byte[] qFeature = Base64.decode(feature2.getBytes());
//                result = compare.CompareFeature(tFeature, qFeature);
//            } else if (SDKUtils.getFeatureExtractType().equals(CommonConst.FEATURE_EXTRACT_TYPE_HTTP)) {
//                result = U2sRecogHttp.compareFeatureByRest(feature1, feature2);
//            }
//        } catch (Exception e) {
//            log.error("feature1=" + feature1);
//            log.error("feature2=" + feature2);
//            log.error("compareFeatureFrom error", e);
//        }
//        return result;
//    }

    public static String objectDetectionOnImage(int objType, byte[] picture) {
        String featureStr = "";
        if (SDKUtils.getFeatureExtractType().equals(CommonConst.FEATURE_EXTRACT_TYPE_JNI)) {
            featureStr = U2sRecogNativeSingle.ObjectDetectionOnImage(objType, picture);
        } else if (SDKUtils.getFeatureExtractType().equals(CommonConst.FEATURE_EXTRACT_TYPE_HTTP)) {
            featureStr = U2sRecogHttp.objectDetectionOnImageByRest(objType, picture);
        }
        return featureStr;
    }

}
