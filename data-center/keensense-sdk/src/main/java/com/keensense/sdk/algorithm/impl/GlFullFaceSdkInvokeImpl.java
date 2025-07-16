package com.keensense.sdk.algorithm.impl;

import com.keensense.common.exception.VideoException;
import com.keensense.sdk.algorithm.IFaceSdkInvoke;
import com.keensense.sdk.constants.SdkExceptionConst;
import com.keensense.sdk.util.ImageBaseUtil;
import com.keensense.sdk.util.RandomUtils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ycl
 * @date 2019/7/17
 */
@Slf4j
public class GlFullFaceSdkInvokeImpl implements IFaceSdkInvoke {

    private String glFaceServiceUrl = "";
    private String urlSeparator = "/";
    private String applicationJsonString = "application/json";

    @Override
    public void initParams(Map<String,Object> param) {
        glFaceServiceUrl = (String) param.get("faceServiceUrl");
        if (!glFaceServiceUrl.endsWith(urlSeparator)) {
            glFaceServiceUrl += urlSeparator;
        }
    }

    @Override
    public String createRegistLib() throws VideoException {
//        Var params = Var.newObject();
//        params.set("ObjType", "1");
//        params.set("RegName", RandomUtils.getRandom6ValiteCode(10));
//        String uuid = UUID.randomUUID().toString().replace("-", "");
//        params.set("UniqueRepoId", uuid);
//        JSONObject resp = null;
//        try {
//            String r = PostUtil.requestContent(glFaceServiceUrl + "api/bingo/registry",
//                    applicationJsonString, params.toString());
//            resp = Optional.ofNullable(r).map(str -> JSONObject.fromObject(r)).orElse(null);
//
//        } catch (HttpConnectionException e) {
//            log.error(String.format("createRegistLib[createLib]-[%s]", e));
//            throw new VideoException(SdkExceptionConst.FAIL_CODE,
//                    String.format("createRegistLib[createLib]-[%s]", e));
//        }
//        if (resp == null) {
//            log.error("createRegistLib error resp is null");
//            throw new VideoException(SdkExceptionConst.FAIL_CODE, "createRegistLib error resp is null");
//        }
//
//        if (resp.getInt("Code") == 1) {
//            return uuid;
//        } else{
//            log.error("createRegistLib error id is" + resp);
//            throw new VideoException(SdkExceptionConst.FAIL_CODE,
//                    "createRegistLib error id is" + resp.getString("Msg"));
//        }
        return null;
    }

    @Override
    public String deleteRegistLib(String repoId) throws VideoException {
//        JSONArray ja = new JSONArray();
//        ja.add(repoId);

//        return deleteWithJson(ja,"api/bingo/registry");
        return null;
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
//        Var params = Var.newObject();
//        params.set("BinData", faceBase64);
//        String respStr = null;
//        try {
//            respStr = PostUtil.requestContent(
//                    glFaceServiceUrl + "api/biz/image/feature",
//                    applicationJsonString, params.toString());
//        } catch (HttpConnectionException e) {
//            log.error("getPicAnalyze error {}", respStr);
//            throw new VideoException(SdkExceptionConst.FAIL_CODE,
//                    "getPicAnalyze error" + e.getMessage());
//        }
//        Var resp = Var.fromJson(respStr);
//        if (resp.getInt("Code") != 1) {
//            log.error("getPicAnalyze error {}",respStr);
//            throw new VideoException(SdkExceptionConst.FAIL_CODE,respStr);
//        }
//        WeekArray dataArr = resp.getArray("Data.Faces");
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
//                        faceVar.getString("Qualities.AlignScore"));
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
//        Var param = Var.newObject();
//        param.set("Name", RandomUtils.getRandom6ValiteCode(10));
//        param.set("UniqueRegId", repoId);
//        param.set("Images.Feature", feature);
//        param.set("Images.ImageUri", url);
//        String respStr;
//        try
//        {
//            respStr = PostUtil.requestContent(glFaceServiceUrl + "api/bingo/civil", applicationJsonString,param.toString());
//        }
//        catch (HttpConnectionException e)
//        {
//            log.error("addFaceToLib error",e);
//            throw new VideoException(SdkExceptionConst.FAIL_CODE,e.getMessage());
//        }
//        Var respVar = Var.fromJson(respStr);
//        if (respVar.getInt("Code") != 1) {
//            log.error("getPicAnalyze error {}",respStr);
//            throw new VideoException(SdkExceptionConst.FAIL_CODE,respStr);
//        }
//        Map<String, Object> data = respVar.get("Data").getObjectMap();
//        for (String key : data.keySet()) {
//            Boolean success = (Boolean) data.get(key);
//            if (success != null && success) {
//                return key;
//            }
//        }
//        throw new VideoException(SdkExceptionConst.FAIL_CODE,respStr);
        return null;
    }

    @Override
    public String delFaceFromLib(String repoId, String featureId) throws VideoException {
//        JSONArray ja = new JSONArray();
//        ja.add(featureId);
//        return deleteWithJson(ja,"api/bingo/civil");
        return null;
    }

//    private String deleteWithJson(JSONArray ja,String s) {
//        JSONObject result = new JSONObject();
//        result.put("code", -1);
//        try {
//            String uri = glFaceServiceUrl + s;
//            HttpResponse myResponse = Request
//                    .Delete(uri)
//                    .bodyString(ja.toString(), ContentType.APPLICATION_JSON)
//                    .connectTimeout(10 * 1000)
//                    .version(HttpVersion.HTTP_1_0)
//                    .socketTimeout(10 * 1000)
//                    .useExpectContinue()
//                    .execute()
//                    .returnResponse();
//            int statusCode = myResponse.getStatusLine().getStatusCode();
//            String r = IOUtils.toString(myResponse.getEntity().getContent(), "UTF-8");
//            if (statusCode != HttpStatus.SC_OK || Var.fromJson(r).getInt("Code") != 1) {
//                log.error("[url]-[{}] [statusCode]-[{}]-[result]-[{}]", uri, statusCode, r);
//                return result.toString();
//            }
//            result.put("code", 0);
//            return result.toString();
//        } catch (IOException e) {
//            log.error("delete,error :", e);
//            throw new VideoException(SdkExceptionConst.FAIL_CODE,
//                    "delete,error :" + e.getMessage());
//        }
//    }

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
//        Var params = Var.newObject();
//        params.set("BaseQuery.Limit", maxResult);
//        params.set("BaseQuery.Features[0]", feature);
//        params.set("RankConfidence", threshold / 100);
//        String[] ids = regIds.split(",");
//        for (int i = 0; i < ids.length; i++) {
//            params.set("UniqueRegIds["+i+"]", ids[i]);
//        }
//        Var dataVar;
//        try {
//            String r = PostUtil.requestContent(glFaceServiceUrl + "api/bingo/civil/rank/list",
//                    applicationJsonString, params.toString());
//            Var var = Var.fromJson(r);
//            int code = var.getInt("Code");
//            if (code != 1) {
//                log.error(String.format("[getSimilars]-[%s]", var.getString("Msg")));
//                throw new VideoException(SdkExceptionConst.FAIL_CODE, String.format("[getSimilars]-[%s]", r));
//            }
//            dataVar = var.get("Data");
//        } catch (HttpConnectionException e) {
//            log.error(String.format("[getSimilars]-[%s]", e));
//            throw new VideoException(SdkExceptionConst.FAIL_CODE,
//                    String.format("[getSimilars]-[%s]", e));
//        }
//        JSONArray resultJson = new JSONArray();
//        dataVar.getArray("RegisteredCivilAttrs").foreach(new IVarForeachHandler() {
//            JSONObject tempJson = new JSONObject();
//            @Override
//            public void execute(String s, Var tempVar) {
//                tempJson.put("score", tempVar.getFloat("Score"));
//                JSONObject faceJson = new JSONObject();
//                faceJson.put("id", tempVar.getString("UniqueCivilAttrId"));
//                faceJson.put("faceGroupId", tempVar.getString("UniqueRegId"));
//                tempJson.put("face", faceJson);
//            }
//        });
//        return Var.fromJson(resultJson.toString());
return null;
    }

    @Override
    public float compareFeature(String feature1, String feature2) throws VideoException {
//        Var param = Var.newObject();
//        param.set("Img1.Feature", feature1);
//        param.set("Img2.Feature", feature2);
//
//        String resp = "";
//        try
//        {
//            resp = PostUtil.requestContent(glFaceServiceUrl + "api/biz/feature/match",
//                    applicationJsonString, param.toString());
//        }
//        catch (HttpConnectionException e)
//        {
//            log.error("compareFeature error",e);
//            throw new VideoException(SdkExceptionConst.FAIL_CODE,e.getMessage());
//        }
//        Var respVar = Var.fromJson(resp);
//        return respVar.getFloat("Data");
    return 0;
    }
}
