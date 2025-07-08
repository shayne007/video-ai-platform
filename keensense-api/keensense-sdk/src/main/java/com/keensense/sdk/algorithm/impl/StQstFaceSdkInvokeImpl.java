package com.keensense.sdk.algorithm.impl;

import com.keensense.common.exception.VideoException;
import com.keensense.sdk.algorithm.IBodySdkInvoke;
import com.keensense.sdk.algorithm.IFaceSdkInvoke;
import com.keensense.sdk.constants.BodyConstant;
import com.keensense.sdk.constants.CommonConst;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author ycl
 * @date 2019/7/29
 */
@Slf4j
public class StQstFaceSdkInvokeImpl implements IFaceSdkInvoke {

    private IBodySdkInvoke sdkInvoke = BodyConstant.getBodySdkInvoke();
    private IFaceSdkInvoke stSdkInvoke = new StFaceSdkInvokeImpl();

    @Override
    public void initParams(Map<String,Object> param) {
        this.stSdkInvoke.initParams(param);
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
        return stSdkInvoke.getPicAnalyze(picture);
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
    public Var getSimilars(String regIds, String feature, float threshold, int maxResult) throws VideoException {
        return getSimilars(regIds, feature, threshold, maxResult, "", "");
    }

    @Override
    public Var getSimilars(String regIds, String feature, float threshold, int maxResult, String startTime, String endTime) throws VideoException {
        Var result = sdkInvoke.getSimilars(CommonConst.OBJ_TYPE_FACE, regIds, feature,
                threshold, maxResult, false, startTime, endTime, 2);
        JSONArray resultJson = new JSONArray();
        if (result != null) {
            result.foreach(new IVarForeachHandler() {
                private static final long serialVersionUID = 1L;

                @Override
                public void execute(String paramString, Var tempVar) {
                    JSONObject tempJson = new JSONObject();
                    Float score = tempVar.getFloat("score") * 100;
                    if (score > threshold) {
                        tempJson.put("score", score);
                        JSONObject faceJson = new JSONObject();
                        faceJson.put("id", tempVar.getString("uuid"));
                        faceJson.put("faceGroupId", tempVar.getString("task"));
                        tempJson.put("face", faceJson);
                        resultJson.add(tempJson);
                    }
                }
            });
        }
        Var resultVar = Var.fromJson(resultJson.toString());
        return resultVar;
    }

    @Override
    public float compareFeature(String feature1, String feature2) throws VideoException {
        return stSdkInvoke.compareFeature(feature1, feature2);
    }
}
