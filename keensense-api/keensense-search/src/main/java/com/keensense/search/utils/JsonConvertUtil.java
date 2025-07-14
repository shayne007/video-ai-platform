package com.keensense.search.utils;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.search.domain.FileResult;
import com.keensense.search.domain.ImageResult;
import com.keensense.search.domain.Result;
import com.keensense.search.repository.FeatureRepository;
import com.keensense.search.repository.ImageRepository;
import com.keensense.search.repository.StructuringDataRepository;
import com.keensense.search.repository.bigdata.FastDfsImageRepository;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


/**
 * Created by zhanx xiaohui on 2019-02-26.
 */
@Component
@Slf4j
@RefreshScope
public class JsonConvertUtil {

    @Resource(name = "${image.repository}")
    private ImageRepository imageRepository;
    @Resource(name = "${feature.repository}")
    private FeatureRepository featureRepository;
    @Resource(name = "${structuringData.repository}")
    protected StructuringDataRepository structuringDataRepository;

    public static final int PERSON_OBJECT_TYPE = 1;
    public static final int MOTORVEHICLES_OBJECT_TYPE = 2;
    public static final int FACE_OBJECT_TYPE = 3;
    public static final int NON_MOTOR_VEHICLES_OBJECT_TYPE = 4;
    public static final int SUMMARY_OBJECT_TYPE = 0;
    public static final String PERSON_OBJECT_TYPE_NAME = "human";
    public static final String MOTOR_VEHICLES_OBJECT_TYPE_NAME = "vehicle";
    public static final String FACE_OBJECT_TYPE_NAME = "face";
    public static final String NON_MOTOR_VEHICLES_OBJECT_TYPE_NAME = "bike";
    public static final String SUMMARY_OBJECT_TYPE_NAME = "summary";

    private static final String STORAGE_PATH = "StoragePath";

    /**
     * 将json中的特征相关的信息提取出来，放入结果中，并将特征值存入搜图模块
     *
     * @param object 请求消息中的json对象
     * @param result 结果
     */
    public void getFeature(JSONObject object, Result result) {
        JSONObject featureObject = object.getJSONObject("FeatureObject");
        result.setFeatureObject(featureObject.getString("FeatureData"));
        result.setFirm(featureObject.getInteger("Firm"));
        //将特征发送到搜图模块
        featureRepository.sendFeature(result);
    }

    /**
     * 将图片存入图片服务器，并将存入的图片的url以及json中的图片相关的信息提取出来，放入结果中
     *
     * @param object 请求消息中的json对象
     * @param result 结果
     * @param objectTypeName 请求对象的类型
     * @param appearTimeName 目标出现时间在某一类型json中的名字
     * @param disappearTimeName 目标消失时间在某一类型json中的名字
     */
    public void getImage(JSONObject object, Result result, String objectTypeName,
        String appearTimeName, String disappearTimeName) {
        JSONObject bigImageInfoObject = null;
        JSONObject imageInfoObject = null;
        JSONArray subImageListObject = getImageObjectArray(object);
        JSONObject subImageInfoObject = subImageListObject.getJSONObject(0);
        //根据json中的type来判断是大图还是小图
        if ("11".equals(subImageInfoObject.getString("Type"))
            || "10".equals(subImageInfoObject.getString("Type"))
            || "1".equals(subImageInfoObject.getString("Type"))
            || "01".equals(subImageInfoObject.getString("Type"))
            || "12".equals(subImageInfoObject.getString("Type"))) {
            bigImageInfoObject = subImageInfoObject;
            imageInfoObject = subImageListObject.getJSONObject(1);
        } else {
            bigImageInfoObject = subImageListObject.getJSONObject(1);
            imageInfoObject = subImageInfoObject;
        }
        //将图片存入图片服务器并获取图片的url
        String imageUrl = imageInfoObject.getString(STORAGE_PATH);
        String bigImageUrl = bigImageInfoObject.getString(STORAGE_PATH);
        String serialnumber = StringUtils.isEmpty(result.getAnalysisId()) ? result.getSerialnumber()
            : result.getAnalysisId();
        if (StringUtils.isEmpty(imageUrl)) {
            imageUrl = imageRepository
                .getUrl(object, imageInfoObject.getString("Data"), "thumb", objectTypeName,
                    serialnumber, "thumb", appearTimeName, disappearTimeName, result.getMarkTime(),
                    result.getId());
            imageInfoObject.put(STORAGE_PATH, imageUrl);
        } else if (imageRepository instanceof FastDfsImageRepository) {
            saveImageResult(imageUrl, "thumb", result);
        }
        if (StringUtils.isEmpty(bigImageUrl)) {
            bigImageUrl = imageRepository
                .getUrl(object, bigImageInfoObject.getString("Data"), "bg", objectTypeName,
                    serialnumber, "bg", appearTimeName, disappearTimeName, result.getMarkTime(),
                    result.getId());
            bigImageInfoObject.put(STORAGE_PATH, bigImageUrl);
        } else if (imageRepository instanceof FastDfsImageRepository) {
            saveImageResult(bigImageUrl, "bg", result);
        }
        bigImageInfoObject.remove("Data");
        imageInfoObject.remove("Data");
        result.setImgUrl(imageUrl);
        result.setBigImgUrl(bigImageUrl);
        result.setSubImageList(encodeSubImageObjectString(subImageListObject));
    }

    public void saveImageResult(String imageurl, String type, Result result) {
        saveImageResult(imageurl, type, result.getMarkTime(), result.getId(), result.getAnalysisId());
    }

    public void saveImageResult(String imageurl, String type, Date datetime, String id, String analysisId) {
        if(StringUtils.isEmpty(imageurl)){
            return;
        }
        try {
            URL url = new URL(imageurl);
            String path = url.getPath();
            path = path.substring(1);
            //fastdfs的group名字以group开头，如果不是，则说明不是fastdfs，不需要写入es
            if(!path.startsWith("group")){
                return ;
            }
            String[] paths = path.split("/");
            String group = paths[0];
            String fdfsUrl = path.substring(path.indexOf("/"));
            ImageResult imageResult = new ImageResult();
            imageResult.setDatetime(datetime);
            imageResult.setId(id);
            imageResult.setAnalysisId(analysisId);
            imageResult.setType(type);
            imageResult.setGroup(group);
            imageResult.setUrl(fdfsUrl);
            structuringDataRepository.save(imageResult);
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
    }

    public void saveFileResult(String imageurl, String type, Date datetime, String id, String analysisId) {
        if(StringUtils.isEmpty(imageurl)){
            return;
        }
        try {
            URL url = new URL(imageurl);
            String path = url.getPath();
            path = path.substring(1);
            //fastdfs的group名字以group开头，如果不是，则说明不是fastdfs，不需要写入es
            if(!path.startsWith("group")){
                return ;
            }
            String[] paths = path.split("/");
            String group = paths[0];
            String fdfsUrl = path.substring(path.indexOf("/"));
            FileResult fileResult = new FileResult();
            fileResult.setDatetime(datetime);
            fileResult.setId(id);
            fileResult.setAnalysisId(analysisId);
            fileResult.setType(type);
            fileResult.setGroup(group);
            fileResult.setUrl(fdfsUrl);
            structuringDataRepository.save(fileResult);
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
    }

    public <T extends Result> JSONArray decodeSubImageObjectString(T result) {
        String encodeString = result.getSubImageList();
        String decodeString = new String(Base64.getDecoder().decode(encodeString.getBytes(UTF_8)),
            UTF_8);
        return JSON.parseArray(decodeString);
    }

    public String encodeSubImageObjectString(JSONArray object) {
        return new String(Base64.getEncoder().encode(object.toJSONString().getBytes(UTF_8)), UTF_8);
    }

    public JSONObject generatorQueryResponse(Result result, String responseObjectName) {
        JSONObject responseObject = new JSONObject();
        JSONObject object = convertResult2Json(result);
        responseObject.put(responseObjectName, object);

        return responseObject;
    }

    public JSONObject generatorEmptyQueryResponse(String responseObjectName) {
        JSONObject responseObject = new JSONObject();
        JSONObject object = new JSONObject();
        responseObject.put(responseObjectName, object);

        return responseObject;
    }

    public JSONObject generatorEmptyResponse(String objectName,
        String listObjectName) {
        JSONObject responseObject = new JSONObject();
        JSONObject object = new JSONObject();
        object.put("Count", 0);
        JSONArray objectArray = new JSONArray();
        object.put(listObjectName, objectArray);
        responseObject.put(objectName, object);

        return responseObject;
    }

    public Map<String, String> covertInputParameterToResultParameter(
        Map<String, String[]> parameterMap) {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            if (null != value && value.length == 1) {
                map.put(key, value[0]);
            }
        }
        return map;
    }

    public <T extends Result> JSONObject generatorBatchQueryResponse(List<T> results, int count,
        String objectName,
        String listObjectName) {
        JSONObject responseObject = new JSONObject();
        JSONObject object = new JSONObject();
        object.put("Count", count);
        JSONArray objectArray = new JSONArray();
        for (Result result : results) {
            JSONObject innerobject = convertResult2Json(result);
            objectArray.add(innerobject);
        }
        object.put(listObjectName, objectArray);
        responseObject.put(objectName, object);

        return responseObject;
    }

    private JSONObject convertResult2Json(Result result) {
        JSONArray subImageArray = decodeSubImageObjectString(result);
        JSONObject innerobject = JSONObject.parseObject(JSON.toJSONString(result));
        JSONObject subImageList = new JSONObject();
        subImageList.put("SubImageInfoObject", subImageArray);
        innerobject.put("SubImageList", subImageList);

        return innerobject;
    }

    private JSONArray getImageObjectArray(JSONObject inputObject) {
        JSONArray imageObjectArray = null;
        imageObjectArray = inputObject.getJSONArray("SubImageListObject");
        if (imageObjectArray == null) {
            imageObjectArray = inputObject.getJSONObject("SubImageList")
                .getJSONArray("SubImageInfoObject");
        }
        return imageObjectArray;
    }
}
