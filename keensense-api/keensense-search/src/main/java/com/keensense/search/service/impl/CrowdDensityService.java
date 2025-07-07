package com.keensense.search.service.impl;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.common.exception.VideoException;
import com.keensense.search.domain.CrowdDensityResult;
import com.keensense.search.domain.ImageResult;
import com.keensense.search.tool_interface.ParameterCheck;
import com.keensense.search.repository.ImageRepository;
import com.keensense.search.repository.StructuringDataRepository;
import com.keensense.search.utils.DateUtil;
import com.keensense.search.utils.EsQueryUtil;
import com.keensense.search.utils.JsonConvertUtil;
import com.keensense.search.utils.ResponseUtil;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Created by zhanx xiaohui on 2019-09-10.
 */
@Service
@Slf4j
public class CrowdDensityService {

    @Resource(name = "${structuringData.repository}")
    protected StructuringDataRepository structuringDataRepository;
    @Resource(name = "${image.repository}")
    private ImageRepository imageRepository;
    @Autowired
    protected JsonConvertUtil jsonConvertUtil;
    @Autowired
    private EsQueryUtil esQueryUtil;
    private static final String SUCCESS = "Success";

    private static ExecutorService service = Executors.newSingleThreadExecutor();

    /**
     * 批量写入人群密度到ES中
     *
     * @param jsonObject 外部的请求json
     * @param tClass 插入的实体类class
     * @param idName 主键的字段名
     * @return 响应json
     */
    public String batchInsert(JSONObject jsonObject, String objectName,
        String listObjectName, String idName, Class tClass) {
        //log.debug("begin to batch insert {}", jsonObject);
        JSONArray resposneArray = new JSONArray();
        JSONArray dataArray = jsonObject.getJSONObject(objectName).getJSONArray(listObjectName);
        for (int i = 0; i < dataArray.size(); i++) {
            resposneArray.add(insert(dataArray.getJSONObject(i), idName, tClass));
        }

        return ResponseUtil.genereteInsertResponse(resposneArray).toJSONString();
    }


    /**
     * 写入人群密度到ES中
     *
     * @param jsonObject 外部的请求json
     * @param tClass 插入的实体类class
     * @param idName 主键的字段名
     * @return 响应json
     */
    public JSONObject insert(JSONObject jsonObject, String idName, Class tClass) {
        //log.debug("begin to batch insert {}", jsonObject);
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
            .getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String url = request.getRequestURL().toString();
        JSONObject responseObject = new JSONObject();
        JSONArray responseStatusArray = new JSONArray();
        String id = "";
        //循环处理请求中的多个人脸对象
        try {
            //将json转换为可存储到es的对象
            Object result = JSON.parseObject(jsonObject.toJSONString(), tClass);

            //log.debug("convert json to result is {}", result);
            checkParametor(result, tClass);
            id = jsonObject.getString(idName);
            log.info("handler date, id is {}", id);

            //将装换后的对象存储到es中
            if (tClass == CrowdDensityResult.class) {
                transferDataToBase64((CrowdDensityResult) result);
                saveImageResult(((CrowdDensityResult) result).getPicUrl(), "crowddensity",
                    (CrowdDensityResult) result);
            }
            structuringDataRepository.save(result);
            responseObject = ResponseUtil.createSuccessResponse(id, url);
        } catch (Exception e) {
            log.error("handler request failed,the request is {}", jsonObject, e);
            responseObject = ResponseUtil
                .createFailedResponse(id, url, ResponseUtil.STATUS_CODE_FAILED,
                    e.getMessage());
        }
        return responseObject;
    }

    /**
     * 符合1400规范样式的查询接口
     *
     * @param parameterMap 参数键值对
     * @param objectName 外层jsonobject的key值
     * @param listObjectName 内层jsonarray的key值
     * @param tclass 查询的实体类的类名
     * @param <T> 实体类的类别
     * @return 查询到的值
     */
    public <T> String get(Map<String, String[]> parameterMap, String objectName,
        String listObjectName, Class<T> tclass) {
        //log.debug("begin to batch query crowddensity");
        try {
            Map<String, String> map = jsonConvertUtil
                .covertInputParameterToResultParameter(parameterMap);

            JSONObject responseObject;
            Map<Integer, List<T>> resultMap = structuringDataRepository
                .batchQuery(map, tclass);
            Entry<Integer, List<T>> entry = resultMap.entrySet().iterator().next();
            int count = entry.getKey();
            List<T> results = entry.getValue();
            responseObject = generatorBatchQueryResponse(results, count, objectName,
                listObjectName);
            //log.debug("end to batch query face, the response is {}", responseObject);
            return responseObject.toJSONString();
        } catch (Exception e) {
            log.error("", e);
        }
        return jsonConvertUtil.generatorEmptyResponse(objectName, listObjectName)
            .toJSONString();
    }

    public String getBySomeSpecifiedParametor(JSONObject object) {
        Map<String, String> map = generateCrowdDensityRequestMap(object);
        Map<Integer, List<CrowdDensityResult>> resultMap = structuringDataRepository.batchQuery(map, CrowdDensityResult.class);
        Entry<Integer, List<CrowdDensityResult>> entry = resultMap.entrySet().iterator().next();
        int count = entry.getKey();
        List<CrowdDensityResult> results = entry.getValue();
        JSONObject resultObject = generateCrowdDensityResponse(results, count);
        return resultObject.toJSONString();
    }

    public void saveImageResult(String imageurl, String type, CrowdDensityResult result) {
        try {
            URL url = new URL(imageurl);
            String path = url.getPath();
            path = path.substring(1);
            //fastdfs的group名字以group开头，如果不是，则说明不是fastdfs，不需要写入es
            if (!path.startsWith("group")) {
                return;
            }
            String[] paths = path.split("/");
            String group = paths[0];
            String fdfsUrl = path.substring(path.indexOf("/"));
            ImageResult imageResult = new ImageResult();
            imageResult.setDatetime(result.getCreateTime());
            imageResult.setId(result.getId());
            imageResult.setAnalysisId(result.getSerialnumber());
            imageResult.setType(type);
            imageResult.setGroup(group);
            imageResult.setUrl(fdfsUrl);
            structuringDataRepository.save(imageResult);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private void checkParametor(Object object, Class tclass) {
        if (object instanceof ParameterCheck) {
            ((ParameterCheck) object).checkParameter();
        }
    }

    private Map<String, String> generateCrowdDensityRequestMap(JSONObject object) {
        String serialnumber = object.getString("Serialnumber");
        String startTime = object.getString("StartTime");
        String endTime = object.getString("EndTime");
        String count = object.getString("Count");
        String pageNo = object.getString("PageNo");
        String pageSize = object.getString("PageSize");
        Map<String, String> map = new HashMap<>();
        if (serialnumber != null) {
            map.put("CrowdDensity.Serialnumber.In", serialnumber);
        }
        if (endTime != null) {
            map.put("CrowdDensity.CreateTime.Lte", endTime);
        }
        if (startTime != null) {
            map.put("CrowdDensity.CreateTime.Gte", startTime);
        }
        if (count != null) {
            map.put("CrowdDensity.Count.Gt", count);
        }
        if (pageNo != null) {
            map.put("CrowdDensity.RecordStartNo", pageNo);
        }
        if (pageSize != null) {
            map.put("CrowdDensity.PageRecordNum", pageSize);
        }

        return map;
    }

    private JSONObject generateCrowdDensityResponse(List<CrowdDensityResult> results, int count) {
        JSONObject object = new JSONObject();
        object.put("ret", "0");
        object.put("desc", "");
        for (CrowdDensityResult result : results) {
            transferBase64ToData(result);
        }
        object.put("resultList", results);
        return object;
    }

    public <T> JSONObject generatorBatchQueryResponse(List<T> results, int count,
        String objectName,
        String listObjectName) {
        JSONObject responseObject = new JSONObject();
        JSONObject object = new JSONObject();
        object.put("Count", count);
        JSONArray objectArray = new JSONArray();
        for (T result : results) {
            if (result instanceof CrowdDensityResult) {
                transferBase64ToData((CrowdDensityResult) result);
            }
            objectArray.add(result);
        }
        object.put(listObjectName, objectArray);
        responseObject.put(objectName, object);

        return responseObject;
    }

    private void transferDataToBase64(CrowdDensityResult result) {
        if (!StringUtils.isEmpty(result.getDensityInfo())) {
            result.setDensityInfo(
                new String(Base64.getEncoder().encode(result.getDensityInfo().getBytes(UTF_8)), UTF_8));
        }
        if (!StringUtils.isEmpty(result.getHeadPosition())) {
            result.setHeadPosition(
                new String(Base64.getEncoder().encode(result.getHeadPosition().getBytes(UTF_8)),UTF_8));
        }
    }

    private void transferBase64ToData(CrowdDensityResult result) {
        if (!StringUtils.isEmpty(result.getDensityInfo())) {
            result.setDensityInfo(
                new String(Base64.getDecoder().decode(result.getDensityInfo().getBytes(UTF_8)), UTF_8));
        }
        if (!StringUtils.isEmpty(result.getHeadPosition())) {
            result.setHeadPosition(
                new String(Base64.getDecoder().decode(result.getHeadPosition().getBytes(UTF_8)), UTF_8));
        }
    }

    /**
     * 批量删除人群密度相关信息（ES数据和fastdfs图片）
     * @param serialnumber 任务编号
     * @param time 时间 yyyyMMdd
     * @return
     */
    public String batchDeleteData(String serialnumber, String time) {
        log.info("begin to remove data, serialnumer is {}, time is {}", serialnumber, time);
        try {
            if (StringUtils.isEmpty(serialnumber) && StringUtils.isEmpty(time)) {
                throw new VideoException("the parameter is empty");
            }
            //删除ES数据
            Map<String, String> map = new HashMap<>();
            map.put("serialnumber", serialnumber);
            long esNum = structuringDataRepository
                    .batchDelete(map, "createTime",
                            DateUtil.generatorDate(time, "start"),
                            DateUtil.generatorDate(time, "end"), CrowdDensityResult.class);
            //删除fastdfs图片数据
//            long imageNum = imageRepository.batchDeleteData(serialnumber, time);
            if (esNum == -1) {
                throw new VideoException("batchDeleteData es data failed");
            }
//            if (imageNum == -1) {
//                throw new VideoException("batchDeleteData image data failed");
//            }
        } catch (Exception e) {
            log.error("", e);
            return ResponseUtil.generatorDeleteResposnse("-1", "Failed", e.getMessage());
        }
        return ResponseUtil.generatorDeleteResposnse("0", SUCCESS, null);
    }

    /**
     * 批量删除人群密度相关信息（同步删除ES数据和异步删除fastdfs图片）
     * @param serialnumber 任务编号
     * @param time 时间 yyyyMMdd
     * @return
     */
    public String batchDeleteImage(String serialnumber, String time) {
        try {
//            if (StringUtils.isEmpty(serialnumber) && StringUtils.isEmpty(time)) {
//                throw new VideoException("the parameter is empty");
//            }
//
//            Map<String, String> map = new HashMap<>();
//            map.put("serialnumber", serialnumber);
//            long esNum = structuringDataRepository
//                    .batchDeleteData(map, "createTime",
//                            DateUtil.generatorDate(time, "start"),
//                            DateUtil.generatorDate(time, "end"), CrowdDensityResult.class);
//            if (esNum == -1) {
//                throw new VideoException("batchDeleteData es data failed");
//            }

            service.submit(()->imageRepository.batchDelete(serialnumber, time));
        } catch (Exception e) {
            log.error("", e);
            return ResponseUtil.generatorDeleteResposnse("-1", "Failed", e.getMessage());
        }
        return ResponseUtil.generatorDeleteResposnse("0", SUCCESS, null);
    }

    /**
     * groupBy params
     */
    public String groupByQuery(JSONObject json, String index) {
        return esQueryUtil.groupByQuery(json, index);
    }
}

/**
 * @program: platform
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-09-10 14:26
 **/