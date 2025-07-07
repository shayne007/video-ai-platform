package com.keensense.sdk.jni;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.keensense.sdk.algorithm.impl.KsFaceSdkInvokeImpl;
import com.keensense.sdk.algorithm.impl.QstFaceSdkInvokeImpl;
import com.keensense.sdk.constants.CommonConst;
import com.keensense.sdk.constants.FaceConstant;
import com.keensense.sdk.sys.utils.DbPropUtil;
import com.keensense.sdk.util.PritureTypeUtil;
import com.loocme.security.encrypt.Base64;
import com.loocme.sys.datastruct.IVarForeachHandler;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.datastruct.WeekArray;
import com.loocme.sys.exception.HttpConnectionException;
import com.loocme.sys.util.HttpUtil;

import com.loocme.sys.util.StringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Data
public class U2sRecogHttp {

    private static final String MAIN_TYPE_STRING = "mainType";
    private static final String UTF8_STRING = "UTF-8";
    private static final String APPLICATION_JSON_STRING = "application/json";
    private static final String RESULT_STRING = "results";
    private static String FEATURE_EXTRACE_URL;
    private static String FACE_FEATURE_EXTRACE_URL;

    public static void initUrl(String featureExtractUrl,String faceFeatureExtractUrl){
        FEATURE_EXTRACE_URL = featureExtractUrl;
        FACE_FEATURE_EXTRACE_URL = faceFeatureExtractUrl;
    }

    private static String getUrl(String path) {
        return "http://" + FEATURE_EXTRACE_URL + path;
    }

    /**
     * @description: 千视通人脸提取
     * @return: java.lang.String
     */
    private static String getFaceFeatureUrl(String url) {
        return "http://" + FACE_FEATURE_EXTRACE_URL + url;
    }

    /**
     * 通过Http获取图片特征
     */
    public static String getExtractFromPictureByRest(Integer objType, byte[] picBy) {
        String featureStr = "";
        try {
            Var paramVar = Var.newObject();
            paramVar.set(MAIN_TYPE_STRING, CommonConst.getObjTypeName(objType));
            //传入图片二进制base64编码后字符串
            paramVar.set("imageData",
                PritureTypeUtil.getMimeType(picBy) + new String(Base64.encode(picBy)));

            String resultData = HttpUtil
                .postContent(getUrl(CommonConst.REST_GET_FEATURE_URL), UTF8_STRING,
                    90000, null, APPLICATION_JSON_STRING, paramVar.toString());
            Var objextsVar = Var.fromJson(resultData);
            if (CommonConst.REQUEST_CODE_SUCCESS.equals((objextsVar.getString("ret")))) {
                Var results = objextsVar.get(RESULT_STRING);
                featureStr = results.getString("featureVector");
            } else {
                log.error("rest获取图片矢量特征失败：" + objextsVar.getString("ret"));
            }
        } catch (HttpConnectionException e) {
            log.error("rest获取图片矢量特征失败",e);
        }
        return featureStr;
    }

    /**
     * 通过Http获取图片特征
     */
    public static Float compareFeatureByRest(String imageFeatureVector1,
        String imageFeatureVector2) {
        Float featureStr = -1f;
        Var paramVar = Var.newObject();
        paramVar.set("imageFeatureVector1", imageFeatureVector1);
        paramVar.set("imageFeatureVector2", imageFeatureVector2);
        String resultData = null;
        try {
            resultData = HttpUtil.postContent(getUrl(CommonConst.REST_COMPARE_FEATURE_URL), UTF8_STRING,
                90000, null, APPLICATION_JSON_STRING, paramVar.toString());
        } catch (HttpConnectionException e) {
           log.error("compareFeatureByRest error",e);
        }
        Var objextsVar = Var.fromJson(resultData);
        if (CommonConst.REQUEST_CODE_SUCCESS.equals(objextsVar.getString("ret"))) {
            Var results = objextsVar.get(RESULT_STRING);
            featureStr = Float.valueOf(results.getString("distance"));
            log.info("----featureData string length:" + featureStr);
        } else {
            log.error("rest比较图片特征失败：" + objextsVar.getString("ret"));
        }
        return featureStr;
    }

    /**
     * Rest图片结构化处理，Rest可以支持多张图片处理，但是目前方法仅支持处理一张图片
     */
    public static String objectDetectionOnImageByRest(int objType, byte[] picture) {

        String resutStr = "";
        Var paramVar = Var.newObject();
        if (objType == CommonConst.OBJ_TYPE_FACE && FaceConstant.getFaceSdkInvoke() instanceof KsFaceSdkInvokeImpl) {

            List<Map<String, Object>> list = new ArrayList<>();
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("data",
                    PritureTypeUtil.getMimeType(picture) + new String(Base64.encode(picture)));
            objectMap.put("id", "1000");
            list.add(objectMap);
            paramVar.set("images", list);
            // type=3时使用旷视算法，新增以下两个参数
            paramVar.set("scenes", CommonConst.IMAGES_OBJECTS_SCENES);
            paramVar.set("isDetectFullFrame", CommonConst.IMAGES_OBJECTS_IS_DETECT_FULL_FRAME);
            resutStr = getBaseObjectDetectionOnImageByRest(paramVar);

        } else {
            List<Map<String, Object>> list = new ArrayList<>();
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("data",
                PritureTypeUtil.getMimeType(picture) + new String(Base64.encode(picture)));
            objectMap.put("id", "1000");
            list.add(objectMap);
            paramVar.set("images", list);
            if (objType == CommonConst.OBJ_TYPE_FACE) {
                paramVar.set("scenes", 8);
            }
            resutStr = getBaseObjectDetectionOnImageByRest(paramVar);
        }
        return resutStr;

    }

    private static String getBaseObjectDetectionOnImageByRest(Var paramVar) {
        String resutStr = "";
        try {
            String resultData = HttpUtil.postContent(getUrl(CommonConst.REST_GET_OBJECT_URL),
                UTF8_STRING, 90000, null,
                APPLICATION_JSON_STRING, paramVar.toString());
            Var objextsVar = Var.fromJson(resultData);
            if (CommonConst.REQUEST_CODE_SUCCESS.equals(objextsVar.getString("ret"))) {
                WeekArray weekArray = objextsVar.getArray(RESULT_STRING);
                weekArray.foreach(new IVarForeachHandler() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void execute(String index, Var objextVar) {
                        String type= CommonConst.getObjTypeCode(objextVar.getString(MAIN_TYPE_STRING));
                        objextVar.set(MAIN_TYPE_STRING,type);
                    }
                });
                // 替换掉结果集ID，与JNI结果集ID相同
                objextsVar.set("objexts", weekArray);
                objextsVar.remove(RESULT_STRING);
                resutStr = objextsVar.toString();
            } else {
                log.error("rest获取图片结构化失败：" + String.valueOf(objextsVar.get("ret")));
            }

        } catch (HttpConnectionException e) {
            log.error("获取HTTP请求失败",e);
        }
        return resutStr;
    }
}
