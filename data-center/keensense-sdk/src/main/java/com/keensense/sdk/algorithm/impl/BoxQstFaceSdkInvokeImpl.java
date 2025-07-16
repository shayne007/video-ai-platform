package com.keensense.sdk.algorithm.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.common.exception.VideoException;
import com.keensense.common.util.HttpClientUtil;
import com.keensense.sdk.algorithm.IBodySdkInvoke;
import com.keensense.sdk.algorithm.IFaceSdkInvoke;
import com.keensense.sdk.constants.BodyConstant;
import com.keensense.sdk.constants.CommonConst;
import com.keensense.sdk.constants.SdkExceptionConst;
import com.keensense.sdk.util.ImageBaseUtil;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ycl
 * @date 2019/7/29
 */
@Slf4j
public class BoxQstFaceSdkInvokeImpl implements IFaceSdkInvoke {

    private IBodySdkInvoke sdkInvoke = BodyConstant.getBodySdkInvoke();
    private String faceUrl = "";


    @Override
    public void initParams(Map<String,Object> param) {
        this.faceUrl = (String) param.get("faceServiceUrl");
        String urlSeparator = "/";
        if (!faceUrl.endsWith(urlSeparator)) {
            faceUrl += urlSeparator;
        }
    }

    @Override
    public String createRegistLib() throws VideoException {
        return sdkInvoke.createRegistLib();
    }

    @Override
    public String deleteRegistLib(String repoId) throws VideoException {
        return sdkInvoke.deleteRegistLib(repoId);
    }

    @Override
    public String getRegistLib(String repoId) throws VideoException {
        return repoId;
    }

    @Override
    public Map<String,Object> getPicAnalyze(String picture) throws VideoException {
        picture = picture.replace("\n", "");
        StringBuffer faceBase64 = new StringBuffer();
        Map<String, Integer> wh = ImageBaseUtil.getWH(picture, faceBase64);
        if (wh == null) {
            return null;
        }
        Map<String,Object> params = new HashMap<>();
        params.put("imageData", faceBase64);
        String respStr = null;
        respStr = HttpClientUtil.requestPost(faceUrl + "verify/face/detect",
                "application/json", params.toString());
        com.alibaba.fastjson.JSONObject result = JSON.parseObject(respStr);
        if (result.getInteger("code") != 200) {
            log.error("getPicAnalyze error {}", respStr);
            throw new VideoException(SdkExceptionConst.FAIL_CODE, respStr);
        }
        com.alibaba.fastjson.JSONArray data = result.getJSONArray("data");
        Map<String,Object> retVar = new HashMap<>();
        AtomicInteger countAto = new AtomicInteger(0);
        data.forEach(jo -> {
            int count = countAto.getAndIncrement();
            com.alibaba.fastjson.JSONObject bean = (com.alibaba.fastjson.JSONObject) jo;
            retVar.put("[" + count + "].featureVector", bean.getString("feature"));
            retVar.put("[" + count + "].quality", bean.getString("quality"));
            com.alibaba.fastjson.JSONArray rect = bean.getJSONArray("rect");
            retVar.put("[" + count + "].x", rect.getInteger(0));
            retVar.put("[" + count + "].y", rect.getInteger(1));
            retVar.put("[" + count + "].w", rect.getInteger(2) - rect.getInteger(0));
            retVar.put("[" + count + "].h", rect.getInteger(3) - rect.getInteger(1));

            retVar.put("[" + count + "].pose.pitch", bean.getString("pitch"));
            retVar.put("[" + count + "].pose.roll", bean.getString("roll"));
            retVar.put("[" + count + "].pose.yaw", bean.getString("yaw"));

        });


        return Objects.isNull(retVar) ? null : retVar;
    }

    @Override
    public Map<String,Object> getPicAnalyzeOne(String picture) throws VideoException {
        Map<String,Object> faces = this.getPicAnalyze(picture);
        if (null == faces) {
            return null;
        }
        return (Map<String, Object>) faces.get("[0]");
    }

    @Override
    public String addFaceToLib(String repoId, String feature, String url) throws VideoException {
        return addFaceToLib(repoId, feature, url, "");
    }

    @Override
    public String addFaceToLib(String repoId, String feature, String url, String time) throws VideoException {
        return sdkInvoke.addBodyToLib(repoId, "", CommonConst.OBJ_TYPE_FACE, feature, 2);
    }

    @Override
    public String delFaceFromLib(String repoId, String featureId) throws VideoException {
        return sdkInvoke.delBodyFromLib(repoId, CommonConst.OBJ_TYPE_FACE, featureId);
    }

    @Override
    public String getFaceFeature(String repoId, String featureId) throws VideoException {
        return null;
    }

    @Override
    public Map<String,Object> getSimilars(String regIds, String feature, float threshold, int maxResult) throws VideoException {
        return getSimilars(regIds, feature, threshold, maxResult, "", "");
    }

    @Override
    public Map<String,Object> getSimilars(String regIds, String feature, float threshold, int maxResult, String startTime, String endTime) throws VideoException {
        Map<String,Object> result = sdkInvoke.getSimilars(CommonConst.OBJ_TYPE_FACE, regIds, feature,
                threshold, maxResult, false, startTime, endTime, 2);
        JSONArray resultJson = new JSONArray();
        if (result != null) {
//            result.foreach(new IVarForeachHandler() {
//                private static final long serialVersionUID = 1L;
//
//                @Override
//                public void execute(String paramString, Var tempVar) {
//                    JSONObject tempJson = new JSONObject();
//                    Float score = tempVar.getFloat("score") * 100;
//                    if (score >= threshold) {
//                        tempJson.put("score", score);
//                        JSONObject faceJson = new JSONObject();
//                        faceJson.put("id", tempVar.getString("uuid"));
//                        faceJson.put("faceGroupId", tempVar.getString("task"));
//                        tempJson.put("face", faceJson);
//                        resultJson.add(tempJson);
//                    }
//                }
//            });
        }
        Map<String,Object> resultVar = JSONObject.parseObject(resultJson.toString());
        return resultVar;
    }

    @Override
    public float compareFeature(String feature1, String feature2) throws VideoException {
        Map<String,Object> param = new HashMap<>();
        param.put("feature1", feature1);
        param.put("feature2", feature2);
        String resp = "";
        resp = HttpClientUtil.requestPost(faceUrl + "verify/feature/compare",
                "application/json", param.toString());
        com.alibaba.fastjson.JSONObject result = JSON.parseObject(resp);
        if (result.getInteger("code") == 200) {
            return result.getFloat("score") * 100;
        } else {
            log.error(String.format("特征比对失败:%s", result.toJSONString()));
            return 0;
        }
    }
}
