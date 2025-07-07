package com.keensense.sdk.algorithm.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.keensense.common.exception.VideoException;
import com.keensense.sdk.algorithm.IFaceSdkInvoke;
import com.keensense.sdk.constants.SdkExceptionConst;
import com.keensense.sdk.util.ImageBaseUtil;
import com.keensense.sdk.util.OperateImage;
import com.keensense.sdk.util.PritureTypeUtil;
import com.keensense.sdk.util.RandomUtils;
import com.loocme.sys.datastruct.IVarForeachHandler;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.exception.HttpConnectionException;
import com.loocme.sys.util.HttpGetUtil;
import com.loocme.sys.util.HttpUtil;
import com.loocme.sys.util.PatternUtil;
import com.loocme.sys.util.PostUtil;
import com.loocme.sys.util.StringUtil;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sun.misc.BASE64Encoder;

@Slf4j
public class GlstFaceSdkInvokeImpl implements IFaceSdkInvoke {

    private String faceGlstTempPath = "";
    private String glFaceServiceUrl = "";
    private String separator = "/";
    private String featureVectorString = "featureVector";
    private String qualityString = "quality";
    private String countString = "count";
    private String urlPatterString = "^(http|ftp).*$";
    private String apiRespositoriesString = "api/repositories/";
    private String errorString = "error";
    private String applicationJsonString = "application/json";

    @Override
    public void initParams(Var param) {
        glFaceServiceUrl = param.getString("faceServiceUrl");
        if (!glFaceServiceUrl.endsWith(separator)) {
            glFaceServiceUrl += separator;
        }
        faceGlstTempPath = param.getString("faceGlstTempPath");
        if (!new File(faceGlstTempPath).exists()) {
            new File(faceGlstTempPath).mkdirs();
        }
        if (!faceGlstTempPath.endsWith(separator)) {
            faceGlstTempPath = faceGlstTempPath + separator;
        }
    }

    @Override
    public String createRegistLib() throws VideoException {
        Var params = Var.newObject();
        params.set("type", "person");
        params.set("name", RandomUtils.getRandom6ValiteCode(10));
        params.set("mode", "white_list");
        JSONObject resp = null;
        try {
            String r = PostUtil.requestContent(glFaceServiceUrl + apiRespositoriesString, applicationJsonString,
                params.toString());
            resp = Optional.ofNullable(r).map(str -> JSONObject.fromObject(r)).orElse(null);

        } catch (HttpConnectionException e) {
            log.error(String.format("createRegistLib[createLib]-[%s]", e));
            throw new VideoException(SdkExceptionConst.FAIL_CODE, String.format("createRegistLib[createLib]-[%s]", e));
        }
        if (resp == null) {
            log.error("createRegistLib error resp is null");
            throw new VideoException(SdkExceptionConst.FAIL_CODE, "createRegistLib error resp is null");
        }
        String id = "";
        if (resp.containsKey("id")) {
            id = resp.getString("id");
        }
        if (StringUtil.isNull(id)) {
            log.error("createRegistLib error id is" + resp.getString(errorString));
            throw new VideoException(SdkExceptionConst.FAIL_CODE,
                "createRegistLib error id is" + resp.getString(errorString));
        }
        return id;
    }

    @Override
    public String deleteRegistLib(String repoId) throws VideoException {
        String resp = "";
        JSONObject result = new JSONObject();
        result.put("code", -1);
        try {
            resp = HttpUtil.delete(glFaceServiceUrl + apiRespositoriesString + repoId);
        } catch (HttpConnectionException e) {
            log.error("deleteRegistLib,error :", e);
            throw new VideoException(SdkExceptionConst.FAIL_CODE, "deleteRegistLib,error :" + e.getMessage());
        }
        if (StringUtil.isNull(resp)) {
            log.error("deleteRegistLib,error : resp is null");
        }
        JSONObject stResultJson = JSONObject.fromObject(resp);
        String retId = "";
        if (stResultJson.containsKey("id")) {
            retId = stResultJson.getString("id");
            result.put("code", 0);
        }
        if (StringUtil.isNull(retId)) {
            log.error("删除底库失败，原因：" + stResultJson.getString(errorString));
        }
        return result.toString();
    }

    @Override
    public String getRegistLib(String repoId) throws VideoException {
        String resp = "";
        try {
            resp = HttpGetUtil.request(glFaceServiceUrl + apiRespositoriesString + repoId, "");

        } catch (Exception e) {
            log.error(String.format("[getLib]-[%s]", e));
            throw new VideoException(SdkExceptionConst.FAIL_CODE, String.format("[getRegistLib]-[%s]", e));
        }
        JSONObject stResultJson = JSONObject.fromObject(resp);
        return stResultJson.getString("id");
    }

    /**
     * @param picture
     *            picture
     * @description: 格林算法没有质量，默认为1
     * @return: com.loocme.sys.datastruct.Var
     */
    @Override
    public Var getPicAnalyze(String picture) throws VideoException {
        Var result = Var.newObject();
        picture = picture.replace("\n", "");
        StringBuffer faceBase64 = new StringBuffer();
        Map<String, Integer> wh = ImageBaseUtil.getWH(picture, faceBase64);
        if (wh == null) {
            return null;
        }

        Var params = Var.newObject();
        if (PatternUtil.isMatch(picture, urlPatterString, Pattern.CASE_INSENSITIVE)) {
            params.set("url", picture);
        } else {
            params.set("bin", picture);
        }

        String resp = null;
        try {
            resp = PostUtil.requestContent(glFaceServiceUrl + "api/multiface", "application/json;charset=utf-8",
                params.toString());
        } catch (HttpConnectionException e) {
            log.error("getPicAnalyze error", e);
            throw new VideoException(SdkExceptionConst.FAIL_CODE, "getPicAnalyze error" + e.getMessage());
        }
        if (StringUtil.isNull(resp)) {
            log.error("getPicAnalyze error rsp is null");
            throw new VideoException(SdkExceptionConst.FAIL_CODE, "getPicAnalyze error rsp is null ");
        }
        JSONObject stResultJson = JSONObject.fromObject(resp);
        if (stResultJson.containsKey("code") && stResultJson.getInt("code") != 200) {
            log.warn("getPicAnalyze error code is " + stResultJson.getString(errorString));
            throw new VideoException(SdkExceptionConst.FAIL_CODE,
                "getPicAnalyze error code is" + stResultJson.getString("msg"));
        }
        String count = "";
        if (stResultJson.containsKey(countString)) {
            count = stResultJson.getString(countString);
        }
        if (StringUtil.isNull(count)) {
            log.warn("getPicAnalyze count is null" + stResultJson.getString(errorString));
            throw new VideoException(SdkExceptionConst.FAIL_CODE,
                "getPicAnalyze count is null" + stResultJson.getString(errorString));
        }
        int cnt = StringUtil.getInteger(count);
        if (cnt <= 0) {
            log.warn(
                String.format("getPicAnalyze:code[%s],msg[%s]", stResultJson.get("code"), stResultJson.get("msg")));
            throw new VideoException(SdkExceptionConst.FAIL_CODE,
                String.format("getPicAnalyze:code[%s],msg[%s]", stResultJson.get("code"), stResultJson.get("msg")));
        }
        Var maxFaceVar = null;
        JSONArray results = new JSONArray();
        getResultByAnalyze(cnt, picture, stResultJson, maxFaceVar, faceBase64, results);
        result = Var.fromJson(results.toString());
        return result;
    }

    /**
     * @description: 图片特征处理
     * @return: void
     */
    private void getResultByAnalyze(int cnt, String picture, JSONObject stResultJson, Var maxFaceVar,
        StringBuffer faceBase64, JSONArray results) {
        int currSquare = 0;
        byte[] picBy = null;
        String picPath = "", suffix = "";
        File orgFile = null;
        if (cnt > 1) {
            /** 1、保存原图 */
            picBy = ImageBaseUtil.getPictureBytes(picture);
            suffix = PritureTypeUtil.readImageType(picBy);
            StringBuilder picPathBuilder = new StringBuilder();
            picPathBuilder.append(faceGlstTempPath);
            picPathBuilder.append("glstpic").append(File.separator);
            picPathBuilder.append(UUID.randomUUID().toString().replaceAll("-", "")).append(".").append(suffix);
            picPath = picPathBuilder.toString();
            orgFile = new File(picPath);
            File parentFile = orgFile.getParentFile();
            mkdirFile(parentFile);
            ImageBaseUtil.byte2image(picBy, picPath.toString());
        }
        for (int i = 0; i < cnt; i++) {
            JSONArray jsonList = stResultJson.getJSONArray("list");
            int leftPixels = jsonList.getJSONObject(i).getInt("left");
            int topPixels = jsonList.getJSONObject(i).getInt("top");
            int widthPixels = jsonList.getJSONObject(i).getInt("right") - leftPixels;
            int heightPixels = jsonList.getJSONObject(i).getInt("bottom") - topPixels;
            int square = widthPixels * heightPixels;
            if (square > currSquare) {
                maxFaceVar = Var.newObject();
                maxFaceVar.set("x", leftPixels);
                maxFaceVar.set("y", topPixels);
                maxFaceVar.set("w", widthPixels);
                maxFaceVar.set("h", heightPixels);
                maxFaceVar.set(qualityString, 1);
                initMaxFaceVarQq1(cnt, picture, faceBase64, maxFaceVar);
                if (cnt > 1) {
                    try {
                        // 2、截图后的图片
                        OperateImage operateImage = new OperateImage(leftPixels, topPixels, widthPixels, heightPixels);
                        operateImage.setSrcpath(orgFile.getPath());
                        operateImage.setSubpath(
                            orgFile.getPath().substring(0, orgFile.getPath().lastIndexOf(".")) + i + ".jpg");
                        operateImage.cut(suffix);
                        File subFile = new File(operateImage.getSubpath());
                        picBy = ImageBaseUtil.input2byte(new FileInputStream(subFile));
                        // 对字节数组Base64编码,返回Base64编码过的字节数组字符串
                        BASE64Encoder encoder = new BASE64Encoder();
                        String image = encoder.encode(picBy).replace("\r\n", "");
                        maxFaceVar.set(featureVectorString, image);
                        deleteFile(subFile, "gl getPicAnalyze delete error");
                    } catch (Exception e) {
                        log.error("getPicAnalyze", e);
                        maxFaceVar.set(featureVectorString, picture);
                        throw new VideoException(SdkExceptionConst.FAIL_CODE, e.getMessage());
                    }
                }
            }
            if (null != maxFaceVar) {
                results.add(JSONObject.fromObject(maxFaceVar.toString()));
            }
        }
        deleteFile(orgFile, "gl getPicAnalyze delete error");
    }

    private void initMaxFaceVarQq1(int cnt, String picture, StringBuffer faceBase64, Var maxFaceVar) {
        if (cnt == 1) {
            if (StringUtils.isNotBlank(faceBase64)) {
                maxFaceVar.set(featureVectorString, faceBase64);
            } else {
                maxFaceVar.set(featureVectorString, picture);
            }
        }
    }

    private void deleteFile(File file, String errorMsg) {
        if (null != file && !file.delete()) {
            log.warn(errorMsg);
        }
    }

    private void mkdirFile(File file) {
        if (file != null && !file.exists()) {
            file.mkdirs();
        }
    }

    @Override
    public Var getPicAnalyzeOne(String picture) throws VideoException {
        Var faces = this.getPicAnalyze(picture);
        if (null == faces) {
            return null;
        }

        Var face = Var.newObject();
        AtomicInteger squareAto = new AtomicInteger(0);
        faces.foreach(new IVarForeachHandler() {

            private static final long serialVersionUID = 1L;

            @Override
            public void execute(String index, Var faceVar) {
                int square = faceVar.getInt("w") * faceVar.getInt("h");
                if (square > squareAto.get()) {
                    face.set(featureVectorString, faceVar.getString(featureVectorString));
                    face.set(qualityString, faceVar.getString(qualityString));
                    face.set("x", faceVar.getString("x"));
                    face.set("y", faceVar.getString("y"));
                    face.set("w", faceVar.getString("w"));
                    face.set("h", faceVar.getString("h"));
                    squareAto.set(square);
                }
            }
        });
        return face.isNull() ? null : face;
    }

    @Override
    public String addFaceToLib(String repoId, String feature, String url) throws VideoException {
        Var params = Var.newObject();
        params.set("address", "");
        params.set("id_type", "");
        params.set("id_no", "");
        params.set("name", RandomUtils.getRandom6ValiteCode(6));
        if (PatternUtil.isMatch(feature, urlPatterString, Pattern.CASE_INSENSITIVE)) {
            params.set("images[" + 0 + "].url", feature);
        } else {
            params.set("images[" + 0 + "].bin", feature);
        }
        JSONObject resp = null;
        try {
            String r = PostUtil.requestContent(glFaceServiceUrl + apiRespositoriesString + repoId + "/entities",
                applicationJsonString, params.toString());
            resp = Optional.ofNullable(r).map(str -> JSONObject.fromObject(r)).orElse(null);

        } catch (HttpConnectionException e) {
            log.error(String.format("[addFaceToLib]-[%s]", e));
            throw new VideoException(SdkExceptionConst.FAIL_CODE, String.format("[addFaceToLib]-[%s]", e));
        }

        if (resp == null) {
            log.error("添加人员失败，原因：响应为空");
            throw new VideoException(SdkExceptionConst.FAIL_CODE, "添加人员失败，原因：响应为空");
        }
        String retId = "";
        if (resp.containsKey("id")) {
            retId = resp.getString("id");
        }
        if (StringUtil.isNull(retId)) {
            log.error("添加人员失败，原因：" + resp.getString(errorString));
            throw new VideoException(SdkExceptionConst.FAIL_CODE, "添加人员失败，原因：" + resp.getString(errorString));
        }
        return retId;
    }

    @Override
    public String addFaceToLib(String repoId, String feature, String url, String time) throws VideoException {
        return addFaceToLib(repoId, feature, url);
    }

    @Override
    public String delFaceFromLib(String repoId, String featureId) throws VideoException {
        String resp = "";
        try {
            resp = HttpUtil.delete(glFaceServiceUrl + apiRespositoriesString + repoId + "/entities/" + featureId);
        } catch (HttpConnectionException e) {
            log.error("delFaceFromLib error:", e);
            throw new VideoException(SdkExceptionConst.FAIL_CODE, "删除人员失败,原因:" + e.getMessage());
        }
        if (StringUtil.isNull(resp)) {
            log.error("delFaceFromLib resp is null");
        }
        JSONObject stResultJson = JSONObject.fromObject(resp);
        String retId = "";
        if (stResultJson.containsKey("id")) {
            retId = stResultJson.getString("id");
        }
        if (StringUtil.isNull(retId)) {
            log.error("delFaceFromLib error retId" + stResultJson.getString(errorString));
        }
        return retId;
    }

    @Override
    public String getFaceFeature(String repoId, String featureId) throws VideoException {
        return null;
    }

    /**
     * @param regIds
     *            regIds
     * @param feature
     *            feature
     * @param threshold
     *            threshold0.8
     * @param maxResult
     *            maxResult
     * @description: "threshold": 0.8
     * @return: com.loocme.sys.datastruct.Var
     */
    @Override
    public Var getSimilars(String regIds, String feature, float threshold, int maxResult) throws VideoException {
        Var params = Var.newObject();
        params.set("topn", maxResult);
        params.set("threshold", threshold / 100);
        params.set("repositories", regIds);
        if (PatternUtil.isMatch(feature, urlPatterString, Pattern.CASE_INSENSITIVE)) {
            params.set("url", feature);
        } else {
            feature = feature.replace("\n", "");
            params.set("bin", feature);
        }
        JSONObject resp = null;
        try {
            String r = PostUtil.requestContent(glFaceServiceUrl + apiRespositoriesString + "search",
                applicationJsonString, params.toString());
            resp = Optional.ofNullable(r).map(str -> JSONObject.fromObject(r)).orElse(null);

        } catch (HttpConnectionException e) {
            log.error(String.format("[getSimilars]-[%s]", e));
            throw new VideoException(SdkExceptionConst.FAIL_CODE, String.format("[getSimilars]-[%s]", e));
        }

        Var resultVar = Var.newObject();
        int count = 0;
        if (resp == null) {
            log.error("人脸底库搜图失败，原因：响应为空");
            throw new VideoException(SdkExceptionConst.FAIL_CODE, "人脸底库搜图失败，原因：响应为空");
        }
        if (resp.containsKey(countString)) {
            count = resp.getInt(countString);
        } else {
            log.error("检索值为null，人脸底库搜图失败,原因：" + resp.getString(errorString));
        }
        if (0 >= count) {
            log.warn("人脸底库搜图结果为空");
        }
        JSONArray resultJson = new JSONArray();
        for (int i = 0; i < count; i++) {
            JSONObject tempJson = new JSONObject();
            JSONArray listJson = resp.getJSONArray("list");
            String personId = listJson.getJSONObject(i).getJSONObject("entity").getString("id");
            Double score = listJson.getJSONObject(i).getDouble("confidence") * 100;
            tempJson.put("score", score);
            JSONObject faceJson = new JSONObject();
            faceJson.put("id", personId);
            faceJson.put("faceGroupId", regIds);
            tempJson.put("face", faceJson);
            resultJson.add(tempJson);
        }
        resultVar = Var.fromJson(resultJson.toString());
        return resultVar;
    }

    @Override
    public Var getSimilars(String regIds, String feature, float threshold, int maxResult, String startTime,
        String endTime) throws VideoException {
        return getSimilars(regIds, feature, threshold, maxResult);
    }

    @Override
    public float compareFeature(String feature1, String feature2) throws VideoException {
        Var params = Var.newObject();
        if (PatternUtil.isMatch(feature1, urlPatterString, Pattern.CASE_INSENSITIVE)) {
            params.set("url1", feature1);
        } else {
            params.set("bin1", feature1);
        }
        if (PatternUtil.isMatch(feature2, urlPatterString, Pattern.CASE_INSENSITIVE)) {
            params.set("url2", feature2);
        } else {
            params.set("bin2", feature2);
        }

        String resp = null;
        try {
            resp = PostUtil.requestContent(glFaceServiceUrl + "api/compare", "application/json;charset=utf-8",
                params.toString());
        } catch (HttpConnectionException e) {
            log.error("1V1比对失败，原因:", e);
            throw new VideoException(SdkExceptionConst.FAIL_CODE, "1V1比对失败，原因:" + e.getMessage());
        }
        if (StringUtil.isNull(resp)) {
            log.error("1V1比对失败，原因：响应为空");
            throw new VideoException(SdkExceptionConst.FAIL_CODE, "1V1比对失败，原因：响应为空");
        }
        JSONObject stResultJson = JSONObject.fromObject(resp);
        String confidence = "";
        if (stResultJson.containsKey("Confidence")) {
            confidence = stResultJson.getString("Confidence");
        }
        if (StringUtil.isNull(confidence)) {
            log.error("1V1比对失败，原因：" + stResultJson.getString(errorString));
            throw new VideoException(SdkExceptionConst.FAIL_CODE, "1V1比对失败，原因：" + stResultJson.getString(errorString));
        }
        Float score = StringUtil.getFloat(confidence) * 100;
        return score;
    }
}
