package com.keensense.task.util.picture;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.common.config.SpringContext;
import com.keensense.common.util.HttpClientUtil;
import com.keensense.task.config.NacosConfig;
import com.keensense.task.constants.PictureConstants;
import com.keensense.task.util.VideoExceptionUtil;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @Description: 调用APP接口工具类
 * @Author: wujw
 * @CreateDate: 2019/5/27 15:02
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class ObjextAppHttp {

    private ObjextAppHttp() {
    }

    private static NacosConfig nacosConfig = SpringContext.getBean(NacosConfig.class);

    private static String getUrl(String path) {
        return nacosConfig.getObjextUrl() + path;
    }

    /**
     * 通过Http获取图片特征
     */
    public static String getExtractFromPictureByRest(Integer objType, byte[] picBy) {
        String featureStr;
        JSONObject paramVar = JSONObject.parseObject("");
        paramVar.put("mainType", PictureConstants.getObjTypeName(objType));
        //传入图片二进制base64编码后字符串
        paramVar.put("imageData", PictureTypeUtil.getMimeType(picBy) + new String(Base64.encode(picBy)));
        String resultData = HttpClientUtil.requestPost(getUrl(PictureConstants.REST_GET_FEATURE_URL) + ":90000", "", paramVar.toString(), PictureConstants.CHARACTER_ENCODING, 60000, PictureConstants.CONTENT_TYPE);
        JSONObject objextsVar = JSONObject.parseObject(resultData);
        if (PictureConstants.REQUEST_CODE_SUCCESS.equals((objextsVar.getString("ret")))) {
            featureStr = getRequestResult(objextsVar).toString();
        } else {
            log.error("rest获取图片矢量特征失败：" + objextsVar.getString("ret"));
            log.error(getErrorMsg(objextsVar));
            throw VideoExceptionUtil.getCfgException("rest获取图片矢量特征失败：" + objextsVar.getString("ret"));
        }
        return featureStr;
    }

    /**
     * Rest图片结构化处理，Rest可以支持多张图片处理，但是目前方法仅支持处理一张图片
     */
    public static String objectDetectionOnImageByRest(byte[] picture) {
        return objectDetectionOnImageByRest(picture, 0, 1);
    }

    /**
     * Rest图片结构化处理，Rest可以支持多张图片处理，但是目前方法仅支持处理一张图片
     */
    private static String objectDetectionOnImageByRest(byte[] picture, int scenes, int isDetectFullFrame) {
        String resutStr = "";
        Map<String, Object> paramVar = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> objectMap = new HashMap<>(2);
        objectMap.put("data", PictureTypeUtil.getMimeType(picture) + new String(Base64.encode(picture)));
        objectMap.put("id", "1000");
        list.add(objectMap);
        paramVar.put("images", list);
        paramVar.put("scenes", scenes);
        paramVar.put("isDetectFullFrame", isDetectFullFrame);
        String resultData = HttpClientUtil.requestPost(getUrl(PictureConstants.REST_GET_OBJECT_URL) + ":90000", ""
                , paramVar.toString());
        JSONObject objextsVar = JSONObject.parseObject(resultData);
        if (PictureConstants.REQUEST_CODE_SUCCESS.equals(objextsVar.getString("ret"))) {
            String results = "results";
            String mainType = "mainType";
            JSONArray weekArray = objextsVar.getJSONArray(results);
            List<Map<String, Object>> faceArray = new ArrayList<>();
            weekArray.stream().forEach(new Consumer<Object>() {
                @Override
                public void accept(Object objextVar) {
                    JSONObject object = (JSONObject) objextVar;
                    object.put(mainType, PictureConstants.getObjTypeCode(((JSONObject) objextVar).getString(mainType)));
                    String faceFeature = ((JSONObject) objextVar).getString("faceFeature.featureVector");
                    if (StringUtils.isNotEmpty(faceFeature)) {
                        Map<String, Object> faceVar = new HashMap<>();
                        faceVar.put("featureVector", faceFeature);
                        faceVar.put("x", object.get("features.faceBoundingBox.x"));
                        faceVar.put("y", object.get("features.faceBoundingBox.y"));
                        faceVar.put("w", object.get("features.faceBoundingBox.w"));
                        faceVar.put("h", object.get("features.faceBoundingBox.h"));
                        faceVar.put(mainType, PictureConstants.OBJ_TYPE_FACE);
                        faceArray.add(faceVar);
                    }
                }
            });
            //替换掉结果集ID，与JNI结果集ID相同
            objextsVar.put("objexts", weekArray);
            objextsVar.put("faces", faceArray);
            objextsVar.remove(results);
            resutStr = objextsVar.toString();
        } else {
            log.error("rest获取图片结构化失败：" + objextsVar.get("ret"));
            log.error(getErrorMsg(objextsVar));
        }

        return resutStr;
    }

    /***
     * @description: 从返回结果中获取结果
     * @param requestVar 返回结果
     * @return: com.loocme.sys.datastruct.Var
     */
    private static Object getRequestResult(JSONObject requestVar) {
        return requestVar.get("results");
    }

    /***
     * @description: 从返回结果中获取错误信息
     * @param requestVar 返回结果
     * @return: String
     */
    private static String getErrorMsg(JSONObject requestVar) {
        return requestVar.getString("error_msg");
    }
}
