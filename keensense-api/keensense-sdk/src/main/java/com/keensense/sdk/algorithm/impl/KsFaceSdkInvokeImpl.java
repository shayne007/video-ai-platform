package com.keensense.sdk.algorithm.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.common.exception.VideoException;
import com.keensense.sdk.algorithm.IFaceSdkInvoke;
import com.keensense.sdk.constants.FaceConstant;
import com.keensense.sdk.constants.SdkExceptionConst;
import com.keensense.sdk.util.ImageBaseUtil;
import com.loocme.sys.datastruct.IVarForeachHandler;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

import com.loocme.security.encrypt.Base64;
import com.loocme.sys.constance.DateFormatConst;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.exception.HttpConnectionException;
import com.loocme.sys.util.DateUtil;
import com.loocme.sys.util.HttpGetUtil;
import com.loocme.sys.util.PostUtil;
import com.loocme.sys.util.StringUtil;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author jingege
 * @return:
 */
@Service
@Slf4j
public class KsFaceSdkInvokeImpl implements IFaceSdkInvoke
{

    private static final String URL_PLACE_HOLDER = "http://www.baidu.com";
    private static final String SCORE_KEY = "score";

    private String baseUrl = "";
    private String rangesString = "ranges";
    private String applicationJsonString = "application/json";

    @Override
    public void initParams(Var param)
    {
        this.baseUrl = param.getString("faceServiceUrl") + FaceConstant.KSAPP_VERSION;
    }

    @Override
    public String deleteRegistLib(String repoId) throws VideoException
    {
        String resp = "";
        try
        {
            resp = HttpGetUtil.request(this.baseUrl + "faceGroups/" + repoId,
                "");
        }
        catch (HttpConnectionException e)
        {
            log.error("deleteRegistLib,error :" ,e);
            throw new VideoException(SdkExceptionConst.FAIL_CODE,"获取相册id失败:" + e.getMessage());
        }

        if (StringUtil.isNull(resp)) {
            throw new VideoException(SdkExceptionConst.FAIL_CODE, "获取相册id失败,结果为null");
        }

        Var respVar = Var.fromJson(resp);
        String photoAlbumId = respVar.getString("photoAlbumId");
        try
        {
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(50, TimeUnit.SECONDS);
            client.setReadTimeout(50,TimeUnit.SECONDS);
            client.setWriteTimeout(50,TimeUnit.SECONDS);
            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .url(this.baseUrl + "photoAlbums/" + photoAlbumId)
                .delete()
                .build();

            Response response = client.newCall(request).execute();
            String r = response.body().string();
            if (!response.isSuccessful()) {
                log.error("deleteRegistLib http响应code异常：" +response.code()+"::"+r);
                throw new VideoException(SdkExceptionConst.FAIL_CODE,"deleteRegistLib http响应code异常：" +response.code()+"::"+r);
            }
        }
        catch (IOException e)
        {
            log.error("deleteRegistLib,error :" ,e);
            throw new VideoException(SdkExceptionConst.FAIL_CODE, "删除相册id通讯失败");
        }
        if (StringUtil.isNotNull(resp) && resp.contains("NotFound"))
        {
            log.error(resp+"结果NotFount");
        }
        return resp;
    }
    
    /**
     * @description: 可用于判定库是否在算法中真实存在
     * @param repoId
     * @return: java.lang.String
     */
    @Override
    public String getRegistLib(String repoId) throws VideoException{
        String resp = "";
        try{
            resp = HttpGetUtil.request(this.baseUrl + "faceGroups/" + repoId, "");

        }catch(Exception e){
            log.error(String.format("[getRegistLib]-[%s]", e));
            throw new VideoException(SdkExceptionConst.FAIL_CODE, String.format("[getRegistLib]-[%s]", e));
        }
        Var respVar = Var.fromJson(resp);
        return respVar.getString("id");
    }

    @Override
    public String createRegistLib() throws VideoException
    {
        String time = DateUtil.getFormat(new Date(), DateFormatConst.YMDHMS_);
        Var param = Var.newObject();
        param.set("name", time);
        param.set("description", time);
        param.set("creator", "ks");
        param.set("type", "STATIC");
        String resp = "";
        try
        {
            resp = PostUtil.requestContent(this.baseUrl + "photoAlbums",
                    applicationJsonString, param.toString());
        }
        catch (HttpConnectionException e)
        {
            log.error(String.format("[createRegistLib]-[%s]", e));
            throw new VideoException(SdkExceptionConst.FAIL_CODE, String.format("[createLib]-[%s]", e));
        }

        if (StringUtil.isNull(resp)) {
            throw new VideoException(SdkExceptionConst.FAIL_CODE,"resp is null");
        }

        Var respVar = Var.fromJson(resp);
        return respVar.getString("faceGroupId");
    }

    @Override
    public Var getPicAnalyze(String picture) throws VideoException
    {
    	if (StringUtil.isNull(picture)) {
            throw new VideoException(SdkExceptionConst.FAIL_CODE,"picture is null");
        }
        byte[] picBy = ImageBaseUtil.getPictureBytes(picture);
        if (null == picBy)
        {
            log.error(String.format(
                    "[getBodyPicAnalyze] 获取base64失败 param-[%s]", picture));
            throw new VideoException(SdkExceptionConst.FAIL_CODE,String.format(
                "[getBodyPicAnalyze] 获取base64失败 param-[%s]", picture));
        }
    	
        Var param = Var.newObject();
        param.set("photoData", new String(Base64.encode(picBy)));
        param.set("analyzeOptions.extractFeature", true);
        param.set("analyzeOptions.extractLandmark", true);
        param.set("analyzeOptions.qualityCheckOptions.scenario", "STATIC");
        param.set("analyzeOptions.attributeTypes.age", true);
        param.set("analyzeOptions.attributeTypes.blurriness", true);
        param.set("analyzeOptions.attributeTypes.eyeStatus", true);
        param.set("analyzeOptions.attributeTypes.gender", true);
        param.set("analyzeOptions.attributeTypes.minority", true);
        param.set("analyzeOptions.attributeTypes.mouthStatus", true);
        param.set("analyzeOptions.attributeTypes.pose", true);
        param.set("analyzeOptions.attributeTypes.quality", true);

        String resp = "";
        try
        {
            resp = PostUtil.requestContent(this.baseUrl + "query/analyze",
                    applicationJsonString, param.toString());
        }
        catch (HttpConnectionException e)
        {
            log.error("getPicAnalyze error",e);
            throw new VideoException(SdkExceptionConst.FAIL_CODE,e.getMessage());
        }
        Var respVar = Var.fromJson(resp);

        Var retVar = Var.newArray();
        AtomicInteger countAto = new AtomicInteger(0);
        respVar.getArray("faces").foreach(new IVarForeachHandler() {

            private static final long serialVersionUID = 1L;

            @Override
            public void execute(String index, Var faceVar)
            {
                int count = countAto.getAndIncrement();
                retVar.set("[" + count + "].featureVector",
                        faceVar.getString("faceFeature.featureData"));
                retVar.set("[" + count + "].quality",
                        faceVar.getString("attributes.quality"));

                retVar.set("[" + count + "].x",
                        faceVar.getString("locator.rect.leftPixels"));
                retVar.set("[" + count + "].y",
                        faceVar.getString("locator.rect.topPixels"));
                retVar.set("[" + count + "].w",
                        faceVar.getString("locator.rect.widthPixels"));
                retVar.set("[" + count + "].h",
                        faceVar.getString("locator.rect.heightPixels"));
                
                retVar.set("[" + count + "].pose.pitch",
                        faceVar.getString("attributes.pose.pitch"));
                retVar.set("[" + count + "].pose.roll",
                        faceVar.getString("attributes.pose.roll"));
                retVar.set("[" + count + "].pose.yaw",
                        faceVar.getString("attributes.pose.yaw"));
            }
        });

        return retVar.isNull() ? null : retVar;
    }

    @Override
    public Var getPicAnalyzeOne(String picture) throws VideoException
    {
        try{

            Var faces = this.getPicAnalyze(picture);
            if (null == faces) {
                return null;
            }

            Var face = Var.newObject();
            AtomicInteger squareAto = new AtomicInteger(0);
            faces.foreach(new IVarForeachHandler() {

                private static final long serialVersionUID = 1L;

                @Override
                public void execute(String index, Var faceVar)
                {
                    int square = faceVar.getInt("w") * faceVar.getInt("h");
                    if (square > squareAto.get())
                    {
                        face.set("featureVector",
                            faceVar.getString("featureVector"));
                        face.set("quality", faceVar.getString("quality"));
                        face.set("x", faceVar.getString("x"));
                        face.set("y", faceVar.getString("y"));
                        face.set("w", faceVar.getString("w"));
                        face.set("h", faceVar.getString("h"));

                        face.set("pose.pitch", faceVar.getString("pose.pitch"));
                        face.set("pose.roll", faceVar.getString("pose.roll"));
                        face.set("pose.yaw", faceVar.getString("pose.yaw"));
                        squareAto.set(square);
                    }
                }
            });
            return face.isNull() ? null : face;
        }catch (VideoException e){
            log.error("getPicAnalyzeOne,error :" ,e);
            throw new VideoException(SdkExceptionConst.FAIL_CODE,e.getMessage());
        }
    }

    @Override
    public String addFaceToLib(String repoId, String feature, String url) throws VideoException
    {
        return addFaceToLib(repoId, feature, url, "");
    }

    @Override
    public String addFaceToLib(String repoId, String feature, String url, String time) throws VideoException {
        Var param = Var.newObject();
        param.set("featureData", feature);
        param.set("featureVersion", FaceConstant.FEATURE_VERSION);
        param.set("faceGroupId", repoId);
        if (StringUtil.isNotNull(time)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                Date parseTime = sdf.parse(time);
                param.set("photoMeta.photoTime", sdf1.format(parseTime));
            } catch (ParseException e) {
                log.error("addFaceToLib error",e);
            }
        }
        if (StringUtil.isNull(url))
        {
            url = URL_PLACE_HOLDER;
        }
        param.set("imgUri", url);
        String resp = "";
        try
        {
            resp = PostUtil.requestContent(this.baseUrl + "faces", applicationJsonString,param.toString());
        }
        catch (HttpConnectionException e)
        {
            log.error("addFaceToLib error",e);
            throw new VideoException(SdkExceptionConst.FAIL_CODE,e.getMessage());
        }
        Var respVar = Var.fromJson(resp);
        return respVar.getString("id");
    }

    @Override
    public String delFaceFromLib(String repoId, String featureId) throws VideoException {
        String resp = "";
        try
        {
        	OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(50, TimeUnit.SECONDS);
            client.setReadTimeout(50,TimeUnit.SECONDS);
            client.setWriteTimeout(50,TimeUnit.SECONDS);
        	com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                    .url(this.baseUrl + "faces/" + featureId)
                    .delete()
                    .build();

            Response response = client.newCall(request).execute();
            String r = response.body().string();
            if (!response.isSuccessful()) {
                log.error("delFaceFromLib http响应code异常：" +response.code()+"::"+r);
                throw new VideoException(SdkExceptionConst.FAIL_CODE,"delFaceFromLib http响应code异常：" +response.code()+"::"+r);
            }
        }
        catch (IOException e)
        {
            log.error("delFaceFromLib,error :" ,e);
            throw new VideoException(SdkExceptionConst.FAIL_CODE, "删除特征id通讯失败:" + e.getMessage());
        }
        if (StringUtil.isNotNull(resp) && resp.contains("NotFound"))
        {
            log.error(resp+"result not found");
        }
        return resp;
    }

    @Override
    public String getFaceFeature(String repoId, String featureId) throws VideoException
    {
        String resp = "";
        try
        {
            resp = HttpGetUtil.request(this.baseUrl + "faces/" + featureId, "");
        }
        catch (HttpConnectionException e)
        {
            log.error("getFaceFeature error",e);
            throw new VideoException(SdkExceptionConst.FAIL_CODE,e.getMessage());
        }
        Var respVar = Var.fromJson(resp);
        return respVar.getString("featureData");
    }

    /**
     * KS结果未排序
     */
    @Override
    public Var getSimilars(String regIds, String feature, float threshold,
            int maxResult) throws VideoException
    {
        return getSimilars(regIds, feature, threshold, maxResult,"","");
    }

    @Override
    public Var getSimilars(String regIds, String feature, float threshold, int maxResult, String startTime, String endTime) throws VideoException {
        String[] ids = regIds.split(",");
        Var param = Var.newObject();
        param.set("limit", maxResult == 0 ? 10 : maxResult);
        param.set("threshold", Float.floatToRawIntBits(threshold) == 0 ? 85.0f : threshold);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
        for (int i = 0; i < ids.length; i++)
        {
            param.set(rangesString+"[" + i + "].faceGroupId", ids[i]);
            try {
                if (StringUtil.isNotNull(startTime)) {
                    Date parseTime = sdf.parse(startTime);
                    param.set(rangesString+"[" + i + "].timeSpan.beginInclusive", sdf1.format(parseTime));
                }
                if (StringUtil.isNotNull(endTime)) {
                    Date parseTime = sdf.parse(endTime);
                    param.set(rangesString+"[" + i + "].timeSpan.endExclusive", sdf1.format(parseTime));
                }
            } catch (ParseException e) {
                log.error("getSimilars error",e);
            }
        }
        param.set("targets[0].featureData", feature);
        param.set("targets[0].featureVersion", FaceConstant.FEATURE_VERSION_SEARCH);

        String resp = "";
        try
        {
            resp = PostUtil.requestContent(this.baseUrl + "query/search",
                    applicationJsonString, param.toString());
        }
        catch (HttpConnectionException e)
        {
            log.error("getSimilars error",e);
            throw new VideoException(SdkExceptionConst.FAIL_CODE,e.getMessage());
        }
        return sort(resp);
    }

    public Var sort(String json) {
        JSONArray results = Optional.ofNullable(json)
                .map(JSON::parseObject)
                .map(jo -> jo.getJSONArray("results"))
                .orElse(null);
        if (results == null) {
            log.info("getSimilars fail; result is {}",json);
            return null;
        }
        results.sort((o1, o2) -> {
            JSONObject jo1 = (JSONObject) JSON.parse(JSON.toJSONString(o1));
            JSONObject jo2 = (JSONObject) JSON.parse(JSON.toJSONString(o2));
            return jo2.getFloat(SCORE_KEY).compareTo(jo1.getFloat(SCORE_KEY));
        });
        return Var.fromJson(results.toJSONString());
    }

    @Override
    public float compareFeature(String feature1, String feature2) throws VideoException
    {
        Var param = Var.newObject();
        param.set("target1.featureData", feature1);
        param.set("target1.featureVersion", FaceConstant.FEATURE_VERSION);
        param.set("target2.featureData", feature2);
        param.set("target2.featureVersion", FaceConstant.FEATURE_VERSION);
        param.set("threshold", 0);

        String resp = "";
        try
        {
            resp = PostUtil.requestContent(this.baseUrl + "query/verify",
                    applicationJsonString, param.toString());
         }
        catch (HttpConnectionException e)
        {
            log.error("compareFeature error",e);
            throw new VideoException(SdkExceptionConst.FAIL_CODE,e.getMessage());
        }
        Var respVar = Var.fromJson(resp);
        return respVar.getFloat(SCORE_KEY);
    }

}
