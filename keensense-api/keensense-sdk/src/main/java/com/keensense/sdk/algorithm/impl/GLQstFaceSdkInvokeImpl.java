package com.keensense.sdk.algorithm.impl;


import com.keensense.common.exception.VideoException;
import com.keensense.sdk.algorithm.IBodySdkInvoke;
import com.keensense.sdk.algorithm.IFaceSdkInvoke;
import com.keensense.sdk.constants.BodyConstant;
import com.keensense.sdk.constants.CommonConst;
import com.keensense.sdk.constants.SdkExceptionConst;
import com.keensense.sdk.util.ImageBaseUtil;

import lombok.extern.slf4j.Slf4j;


import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ycl
 * @date 2019/7/29
 */
@Slf4j
public class GLQstFaceSdkInvokeImpl implements IFaceSdkInvoke {
    private String glFaceServiceUrl = "";
    private String glStructServiceUrl = "";
    private String glCompareServiceUrl = "";
    private String detectMode = "1";
    private int function = 200;
    private String urlSeparator = "/";
    private String applicationJsonString = "application/json";

    //    private FaceAppMain faceApp;
    private IBodySdkInvoke sdkInvoke = BodyConstant.getBodySdkInvoke();

    @Override
    public void initParams(Map<String, Object> param) {
        glFaceServiceUrl = (String) param.get("faceServiceUrl");
        if (glFaceServiceUrl.endsWith(urlSeparator)) {
            glFaceServiceUrl = glFaceServiceUrl.substring(0, glFaceServiceUrl.length() - 1);
        }


//        glStructServiceUrl = StringUtil.isNull(param.getString("glStructPort")) ?
//                glFaceServiceUrl+":"+"38080/" : glFaceServiceUrl+":"+param.getString("glStructPort")+urlSeparator;
//
//        glCompareServiceUrl = StringUtil.isNull(param.getString("glComparePort")) ?
//                glFaceServiceUrl+":"+"8899/" : glFaceServiceUrl+":"+param.getString("glComparePort")+urlSeparator;
//
//        detectMode = StringUtil.isNull(param.getString("glStructDetectMode")) ?
//                "0" : param.getString("glStructDetectMode");
//
//        Var paramGl = Var.newObject();
//        paramGl.set("faceUrl", glStructServiceUrl);
//        this.faceApp = FaceAppMain.getInstance(FaceConstant.TYPE_COMPANY_GLST, paramGl);
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
    public Map<String, Object> getPicAnalyze(String picture) throws VideoException {
        return Optional.ofNullable(analyze(picture, detectMode))
                .orElseGet(() -> analyze(picture, "1".equals(detectMode) ? "0" : "1"));
    }

    private Map<String, Object> analyze(String picture, String detectMode) {
        picture = picture.replace("\n", "");
        StringBuffer faceBase64 = new StringBuffer();
        Map<String, Integer> wh = ImageBaseUtil.getWH(picture, faceBase64);
        if (wh == null) {
            return null;
        }
//        Var params = Var.newObject();
//        params.set("Context.Functions[0]", function);
//        params.set("Context.Type", 2);
//        params.set("Context.Params.detect_mode", detectMode);
//        params.set("Image.Data.BinData", faceBase64);
//        params.set("Image.Data.BinDataType", 1);
//        String respStr = null;
//        try {
//            respStr = PostUtil.requestContent(
//                    glStructServiceUrl + "vse/face/rec/image",
//                    applicationJsonString, params.toString());
//        } catch (HttpConnectionException e) {
//            log.error("getPicAnalyze error {}", respStr);
//            throw new VideoException(SdkExceptionConst.FAIL_CODE,
//                    "getPicAnalyze error" + e.getMessage());
//        }
//        Var resp = Var.fromJson(respStr);
//        if (!"200".equals(resp.getString("Context.Status"))) {
//            log.error("getPicAnalyze error {}",respStr);
//            throw new VideoException(SdkExceptionConst.FAIL_CODE,respStr);
//        }
//        WeekArray dataArr = resp.getArray("Result.Faces");
//        Var retVar = Var.newArray();
//        AtomicInteger countAto = new AtomicInteger(0);
//        dataArr.foreach(new IVarForeachHandler() {
//
//            private static final long serialVersionUID = 1L;
//
//            @Override
//            public void execute(String index, Var faceVar)
//            {
//                int count = countAto.getAndIncrement();
//                retVar.set("[" + count + "].featureVector",
//                        faceVar.getString("Features"));
//                retVar.set("[" + count + "].quality",
//                        faceVar.getString("Qualities.AlignScore_G_frontface"));
//
//                retVar.set("[" + count + "].x",
//                        faceVar.getString("Img.Cutboard.X"));
//                retVar.set("[" + count + "].y",
//                        faceVar.getString("Img.Cutboard.Y"));
//                retVar.set("[" + count + "].w",
//                        faceVar.getString("Img.Cutboard.Width"));
//                retVar.set("[" + count + "].h",
//                        faceVar.getString("Img.Cutboard.Height"));
//
//                retVar.set("[" + count + "].pose.pitch",
//                        faceVar.getString("Qualities.Pitch"));
//                retVar.set("[" + count + "].pose.roll",
//                        faceVar.getString("Qualities.Roll"));
//                retVar.set("[" + count + "].pose.yaw",
//                        faceVar.getString("Qualities.Yaw"));
//            }
//        });
//
//        return retVar.isNull() ? null : retVar;
        return null;
    }

    @Override
    public Map<String, Object> getPicAnalyzeOne(String picture) throws VideoException {
        Map<String, Object> faces = this.getPicAnalyze(picture);
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
        return sdkInvoke.addBodyToLib(repoId, "", CommonConst.OBJ_TYPE_FACE, feature, 1);
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
    public Map<String, Object> getSimilars(String regIds, String feature, float threshold, int maxResult) throws VideoException {
        return getSimilars(regIds, feature, threshold, maxResult, "", "");
    }

    @Override
    public Map<String, Object> getSimilars(String regIds, String feature, float threshold, int maxResult, String startTime, String endTime) throws VideoException {
        Map<String, Object> result = sdkInvoke.getSimilars(CommonConst.OBJ_TYPE_FACE, regIds, feature,
                threshold, maxResult, false, startTime, endTime, 1);
//        JSONArray resultJson = new JSONArray();
//        if(result != null){
//            result.foreach(new IVarForeachHandler() {
//                private static final long serialVersionUID = 1L;
//                @Override
//                public void execute(String paramString, Var tempVar) {
//                    JSONObject tempJson = new JSONObject();
//                    Float score = tempVar.getFloat("score") *100;
//                    if(score >= threshold){
//                        tempJson.put("score", score);
//                        JSONObject faceJson = new JSONObject();
//                        faceJson.put("id", tempVar.getString("uuid"));
//                        faceJson.put("faceGroupId", tempVar.getString("task"));
//                        tempJson.put("face", faceJson);
//                        resultJson.add(tempJson);
//                    }
//                }
//            });
//        }
//        Var resultVar = Var.fromJson(resultJson.toString());
//        return resultVar;
        return result;
    }

    @Override
    public float compareFeature(String feature1, String feature2) throws VideoException {
//        return (float) this.faceApp.compare(feature1, feature2) * 100;
        return 0;
    }
}
