package com.keensense.sdk.algorithm.impl;

import com.alibaba.fastjson.JSONObject;
import com.keensense.common.exception.VideoException;
import com.keensense.common.util.HttpClientUtil;
import com.keensense.sdk.algorithm.IBodySdkInvoke;
import com.keensense.sdk.constants.CommonConst;
import com.keensense.sdk.constants.FaceConstant;
import com.keensense.sdk.constants.SdkExceptionConst;
import com.keensense.sdk.util.ImageBaseUtil;
import com.keensense.sdk.util.SDKUtils;
import com.keensense.sdk.util.ValidUtil;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.PatternMatchUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jingege
 * @description:
 * @return:
 */
@Service
@Slf4j
public class QstBodySdkInvokeImpl implements IBodySdkInvoke {

    private String baseUrl = "";
    private String mainTypeString = "mainType";
    private String featureVectorString = "featureVector";
    private String errorMsgString = "error_msg";
    private String applicationJsonString = "application/json";

    @Override
    public void initParams(Map<String,Object> param) {
        this.baseUrl = (String) param.get("bodyServiceUrl");
    }

    @Override
    public String createRegistLib() throws VideoException {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public String createRegistLib(String id) throws VideoException {
        return id;
    }

    @Override
    public String deleteRegistLib(String repoId) throws VideoException {
        String resp = "";
        resp = HttpClientUtil.requestPost(this.baseUrl + "delete/" + repoId,
                applicationJsonString, "");
        JSONObject respVar = (JSONObject) JSONObject.parse(resp);
        String msg = respVar.getString(errorMsgString);
        if (StringUtils.isEmpty(msg)) {
            log.error("删除特征库失败:响应为空");
        }
        if ("OK".equals(msg)) {
            return "";
        }
        if (msg.contains("has no data")) {
            log.info("has no data");
            return "";
        }
        return msg;
    }

    @Override
    public Map<String,Object> getPicAnalyze(int objType, String picture) throws VideoException {
        if (StringUtils.isEmpty(picture)) {
            throw new VideoException(SdkExceptionConst.FAIL_CODE, "input picture is null");
        }

        byte[] picBy;
        if (PatternMatchUtils.simpleMatch(picture, "^(http|ftp).*$")) {

            picBy = ImageBaseUtil.getPictureBytes(picture);
            if (null == picBy) {
                log.error(String.format(
                        "[getBodyPicAnalyze] 获取base64失败 param-[%s]", picture));
                throw new VideoException(SdkExceptionConst.FAIL_CODE, String.format(
                        "[getBodyPicAnalyze] 获取base64失败 param-[%s]", picture));
            }
        } else {
            picBy = Base64.decode(Arrays.toString(picture.getBytes()));
        }
//
//        try {
//            String objexts = SDKUtils.objectDetectionOnImage(objType, picBy);
//            Var objextsVar = Var.fromJson(objexts);
//            Var retVar = Var.newArray();
//            AtomicInteger countAto = new AtomicInteger(0);
//            objextsVar.get("objexts").foreach(new IVarForeachHandler() {
//
//                private static final long serialVersionUID = 1L;
//
//                @Override
//                public void execute(String index, Var objextVar) {
//                    soluExecute(index, objextVar, retVar, countAto, objType);
//                }
//            });
//
//            return retVar.isNull() ? null : retVar;
//        } catch (Exception e) {
//            log.error(String.format("picanalyze [%s]", e));
//            throw new VideoException(SdkExceptionConst.FAIL_CODE, String.format("picanalyze [%s]", e));
//        }
        return null;
    }

//    private void soluExecute(String index, Var objextVar, Var retVar, AtomicInteger countAto, int objType) {
//        if (objType == CommonConst.OBJ_TYPE_FACE && FaceConstant.getFaceSdkInvoke() instanceof QstFaceSdkInvokeImpl) {
//            handlerObjectV8(objextVar, retVar, countAto, objType);
//        } else {
//            int count = countAto.getAndIncrement();
//            int mainType = ValidUtil.changeMainType(objextVar.getString(mainTypeString));
//            if (0 != objType && mainType != objType) {
//                return;
//            }
//            retVar.set("[" + count + "].mainType", mainType);
//            retVar.set("[" + count + "].featureVector",
//                    objextVar.getString(featureVectorString));
//            retVar.set("[" + count + "].quality",
//                    objextVar.getString("features.bodyQuality"));
//            retVar.set("[" + count + "].x", objextVar.getString("x"));
//            retVar.set("[" + count + "].y", objextVar.getString("y"));
//            retVar.set("[" + count + "].w", objextVar.getString("w"));
//            retVar.set("[" + count + "].h", objextVar.getString("h"));
//        }
//    }
//
//    private void handlerObjectV8(Var objextVar, Var retVar, AtomicInteger countAto, int objType) {
//        if (StringUtils.isNotEmpty(objextVar.getString("faceFeature.featureVector"))) {
//            int count = countAto.getAndIncrement();
//            retVar.set("[" + count + "].mainType", objType);
//            retVar.set("[" + count + "].featureVector", objextVar.getString("faceFeature.featureVector"));
//            retVar.set("[" + count + "].quality", objextVar.getString("faceFeature.faceQuality"));
//            retVar.set("[" + count + "].x", objextVar.getString("features.faceBoundingBox.x"));
//            retVar.set("[" + count + "].y", objextVar.getString("features.faceBoundingBox.y"));
//            retVar.set("[" + count + "].w", objextVar.getString("features.faceBoundingBox.w"));
//            retVar.set("[" + count + "].h", objextVar.getString("features.faceBoundingBox.h"));
//        }
//    }

//    private void handlerFace18051(Var objextVar, Var retVar, int count, int objType) {
//        if (objextVar.getInt("total") > 0) {
//            retVar.set("[" + count + "].mainType", objType);
//            retVar.set("[" + count + "].featureVector", objextVar.getString("faces[0].feature"));
//            retVar.set("[" + count + "].quality", objextVar.getString("faces[0].quality"));
//            retVar.set("[" + count + "].x", objextVar.getString("faces[0].box.x"));
//            retVar.set("[" + count + "].y", objextVar.getString("faces[0].box.y"));
//            retVar.set("[" + count + "].w", objextVar.getString("faces[0].box.w"));
//            retVar.set("[" + count + "].h", objextVar.getString("faces[0].box.h"));
//        }
//    }

    @Override
    public Map<String,Object> getPicAnalyzeOne(int objType, String picture) throws VideoException {
        Map<String,Object> objexts = this.getPicAnalyze(objType, picture);
        if (null == objexts) {
            log.error(SdkExceptionConst.FAIL_CODE + "getPicAnaly is null");
            return null;
        }

//        Var objext = Var.newObject();
//        AtomicInteger squareAto = new AtomicInteger(0);
//        objexts.foreach(new IVarForeachHandler() {
//
//            private static final long serialVersionUID = 1L;
//
//            @Override
//            public void execute(String index, Var objextVar) {
//                int mainType = ValidUtil.changeMainType(objextVar.getString(mainTypeString));
//                if (0 != objType && mainType != objType) {
//                    return;
//                }
//
//                int square = objextVar.getInt("w") * objextVar.getInt("h");
//                if (square > squareAto.get()) {
//                    objext.set(mainTypeString, mainType);
//                    objext.set(featureVectorString,
//                            objextVar.getString(featureVectorString));
//                    objext.set("quality", objextVar.getString("quality"));
//                    objext.set("x", objextVar.getString("x"));
//                    objext.set("y", objextVar.getString("y"));
//                    objext.set("w", objextVar.getString("w"));
//                    objext.set("h", objextVar.getString("h"));
//                    squareAto.set(square);
//                }
//            }
//        });
//        return objext.isNull() ? null : objext;
        return null;
    }

    @Override
    public String addBodyToLib(String repoId, String uuid, int objType, String feature) throws VideoException {
        return addBodyToLib(repoId, uuid, objType, feature, 0);
    }

    @Override
    public String addBodyToLib(String repoId, String uuid, int objType,
                               String feature, Integer firm) throws VideoException {
//        if (StringUtil.isNull(uuid)) {
//
//            uuid = UUID.randomUUID().toString().replace("-", "");
//        }
//        Var param = Var.newObject();
//        param.set("repo", repoId);
//        param.set("uuid", uuid);
//        param.set("type", objType);
//        param.set("feature", feature);
//        param.set("firm", firm == null ? 0 : firm);
//
//        String resp = "";
//        try {
//            resp = PostUtil.requestContent(this.baseUrl + "enrollfeature",
//                    applicationJsonString, param.toString());
//        } catch (HttpConnectionException e) {
//            log.error("addBodyToLib error", e);
//            throw new VideoException(SdkExceptionConst.FAIL_CODE, e.getMessage());
//        }
//        Var respVar = Var.fromJson(resp);
//        if ("OK".equals(respVar.getString(errorMsgString))) {
//            return uuid;
//        }

        return null;
    }

    @Override
    public String delBodyFromLib(String repoId, int objType, String featureId) throws VideoException {
//        Var param = Var.newObject();
//        param.set("repo", repoId);
//        param.set("uuid", featureId);
//        param.set("type", objType);
//
//        String resp = "";
//        try {
//            resp = PostUtil.requestContent(this.baseUrl + "deletefeature",
//                    applicationJsonString, param.toString());
//        } catch (HttpConnectionException e) {
//            log.error("delBodyFromLib error", e);
//            throw new VideoException(SdkExceptionConst.FAIL_CODE, "删除接口调用失败:" + e.getMessage());
//        }
//        Var respVar = Var.fromJson(resp);
//        String retMsg = respVar.getString(errorMsgString);
//        if (StringUtil.isNull(retMsg)) {
//            log.error(retMsg + "删除接口调用失败:响应msg为空");
//        }
//        if ("OK".equals(retMsg)) {
//            return "";
//        }
//        if (retMsg.contains("does not exist")) {
//            log.error(retMsg);
//            return "";
////            throw new VideoException(SdkExceptionConst.FAIL_CODE,retMsg);
//        }
        return "retMsg";
    }

    /**
     * 阈值没用，需要在外层自己判定
     */
    @Override
    public Map<String,Object> getSimilars(int objType, String regIds, String feature,
                           Float threshold, int maxResult, boolean reRank) throws VideoException {
        String[] ids = regIds.split(",");
//        Var param = Var.newObject();
//        for (int i = 0; i < ids.length; i++) {
//            param.set("tasks[" + i + "].id", ids[i]);
//        }
//        param.set("type", objType);
//        param.set("query", feature);
//        if (reRank) {
//            param.set("reRank", 0);
//        }
//        param.set("max_result", maxResult <= 0 ? 10 : maxResult);
//        if (objType == CommonConst.OBJ_TYPE_FACE) {
//            param.set("tradeoff", 50);
//        }
//        String resp = "";
//        try {
//            resp = PostUtil.requestContent(this.baseUrl + "search",
//                    applicationJsonString, param.toString());
//        } catch (HttpConnectionException e) {
//            log.error("getSimilars error", e);
//            throw new VideoException(SdkExceptionConst.FAIL_CODE, e.getMessage());
//        }
//        Var respVar = Var.fromJson(resp);
//
//        String retMsg = respVar.getString(errorMsgString);
//        if ("OK".equals(retMsg)) {
//            return respVar.get("results");
//        }
//        if (respVar.getString("code").equals("NotFound")) {
//            return null;
//        }
        return null;
    }

    @Override
    public Map<String,Object> getSimilars(int objType, String regIds, String feature, Float threshold, int maxResult,
                           boolean reRank, String beginTime, String endTime, int firm) throws VideoException {

        String[] ids = regIds.split(",");
//        Var param = Var.newObject();
//        for (int i = 0; i < ids.length; i++) {
//            param.set("tasks[" + i + "].id", ids[i]);
//        }
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            if (StringUtil.isNotNull(beginTime)) {
//                Date parseTime = sdf.parse(beginTime);
//                param.set("from", sdf1.format(parseTime));
//            }
//            if (StringUtil.isNotNull(endTime)) {
//                Date parseTime = sdf.parse(endTime);
//                param.set("to", sdf1.format(parseTime));
//            }
//        } catch (ParseException e) {
//            log.error("getSimilars error", e);
//        }
//
//        param.set("firm", firm);
//        param.set("type", objType);
//        param.set("query", feature);
//
//        if (reRank) {
//            param.set("reRank", 0);
//        }
//        param.set("max_result", maxResult <= 0 ? 10 : maxResult);
//        if (objType == CommonConst.OBJ_TYPE_FACE) {
//            //param.set("tradeoff", 50);
//        }
//        String resp = "";
//        try {
//            resp = PostUtil.requestContent(this.baseUrl + "search",
//                    applicationJsonString, param.toString());
//        } catch (HttpConnectionException e) {
//            log.error("getSimilars error", e);
//            throw new VideoException(SdkExceptionConst.FAIL_CODE, e.getMessage());
//        }
//        Var respVar = Var.fromJson(resp);
//
//        String retMsg = respVar.getString(errorMsgString);
//        if ("OK".equals(retMsg)) {
//            return respVar.get("results");
//        }
//        if (respVar.getString("code").equals("NotFound")) {
//            return null;
//        }
        return null;

    }

    @Override
    public float compareFeature(int objType, String feature1, String feature2) throws VideoException {
        Double similar = null;
//        try {
//
//            similar = QstCompareFeatureExec.getInstance().compareFeatures(1,
//                    Base64.decode(feature1.getBytes()),
//                    Base64.decode(feature2.getBytes()));
//        } catch (Exception e) {
//            log.error("compareFeature,error :", e);
//            throw new VideoException(SdkExceptionConst.FAIL_CODE, e.getMessage());
//        }
        return similar.floatValue();
    }

}
