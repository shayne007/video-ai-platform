package com.keensense.picturestream.util.picture;

import com.keensense.common.config.SpringContext;
import com.keensense.picturestream.config.NacosConfig;
import com.keensense.picturestream.constants.PictureConstants;
import com.keensense.picturestream.util.VideoExceptionUtil;
import com.loocme.security.encrypt.Base64;
import com.loocme.sys.datastruct.IVarForeachHandler;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.datastruct.WeekArray;
import com.loocme.sys.exception.HttpConnectionException;
import com.loocme.sys.util.HttpUtil;
import com.loocme.sys.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        try {
            Var paramVar = Var.newObject();
            paramVar.set("mainType", PictureConstants.getObjTypeName(objType));
            //传入图片二进制base64编码后字符串
            paramVar.set("imageData", PictureTypeUtil.getMimeType(picBy) + new String(Base64.encode(picBy)));
            String resultData = HttpUtil.postContent(getUrl(PictureConstants.REST_GET_FEATURE_URL), PictureConstants.CHARACTER_ENCODING,
                    90000, null, PictureConstants.CONTENT_TYPE, paramVar.toString());
            Var objextsVar = Var.fromJson(resultData);
            if (PictureConstants.REQUEST_CODE_SUCCESS.equals((objextsVar.getString("ret")))) {
                Var results = getRequestResult(objextsVar);
                featureStr = results.getString("featureVector");
                log.info("----featureData string length:" + featureStr.length());
                log.info(Base64.decode(featureStr.getBytes()).length + "");
            } else {
                log.error("rest获取图片矢量特征失败：" + objextsVar.getString("ret"));
                log.error(getErrorMsg(objextsVar));
                throw VideoExceptionUtil.getCfgException("rest获取图片矢量特征失败：" + objextsVar.getString("ret"));
            }
        } catch (HttpConnectionException e) {
            log.error("http connect get feature failed", e);
            throw VideoExceptionUtil.getCfgException("调用特征提取接口失败!");
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
        try {
            Var paramVar = Var.newObject();
            List<Map<String, Object>> list = new ArrayList<>();
            Map<String, Object> objectMap = new HashMap<>(2);
            objectMap.put("data", PictureTypeUtil.getMimeType(picture) + new String(Base64.encode(picture)));
            objectMap.put("id", "1000");
            list.add(objectMap);
            paramVar.set("images", list);
            paramVar.set("scenes", scenes);
            paramVar.set("isDetectFullFrame", isDetectFullFrame);
            String resultData = HttpUtil.postContent(getUrl(PictureConstants.REST_GET_OBJECT_URL), PictureConstants.CHARACTER_ENCODING,
                    90000, null, PictureConstants.CONTENT_TYPE, paramVar.toString());
            Var objextsVar = Var.fromJson(resultData);
            if (PictureConstants.REQUEST_CODE_SUCCESS.equals(objextsVar.getString("ret"))) {
                String results = "results";
                String mainType = "mainType";
                WeekArray weekArray = objextsVar.getArray(results);
                List<Var> faceArray = new ArrayList<>();
                weekArray.foreach(new IVarForeachHandler() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void execute(String index, Var objextVar) {
                        objextVar.set(mainType, PictureConstants.getObjTypeCode(objextVar.getString(mainType)));
                        String faceFeature = objextVar.getString("faceFeature.featureVector");
                        if (StringUtil.isNotNull(faceFeature)) {
                            Var faceVar = Var.newObject();
                            faceVar.set("featureVector", faceFeature);
                            faceVar.set("x", objextVar.get("features.faceBoundingBox.x"));
                            faceVar.set("y", objextVar.get("features.faceBoundingBox.y"));
                            faceVar.set("w", objextVar.get("features.faceBoundingBox.w"));
                            faceVar.set("h", objextVar.get("features.faceBoundingBox.h"));
                            faceVar.set(mainType, PictureConstants.OBJ_TYPE_FACE);
                            faceArray.add(faceVar);
                        }
                    }
                });
                //替换掉结果集ID，与JNI结果集ID相同
                objextsVar.set("objexts", weekArray);
                objextsVar.set("faces", faceArray);
                objextsVar.remove(results);
                resutStr = objextsVar.toString();
            } else {
                log.error("rest获取图片结构化失败：" + objextsVar.get("ret"));
                log.error(getErrorMsg(objextsVar));
            }
        } catch (HttpConnectionException e) {
            log.error("获取HTTP请求失败");
            log.error("http connect objext failed", e);
        }
        return resutStr;
    }

    /***
     * @description: 从返回结果中获取结果
     * @param requestVar 返回结果
     * @return: com.loocme.sys.datastruct.Var
     */
    private static Var getRequestResult(Var requestVar) {
        return requestVar.get("results");
    }

    /***
     * @description: 从返回结果中获取错误信息
     * @param requestVar 返回结果
     * @return: String
     */
    private static String getErrorMsg(Var requestVar) {
        return requestVar.getString("error_msg");
    }
}
