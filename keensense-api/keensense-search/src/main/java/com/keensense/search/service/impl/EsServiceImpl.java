package com.keensense.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.common.exception.VideoException;
import com.keensense.search.domain.APEResult;
import com.keensense.search.domain.EventResult;
import com.keensense.search.domain.VehicleflowrateResult;
import com.keensense.search.domain.ViolationResult;
import com.keensense.search.repository.AlarmRepository;
import com.keensense.search.repository.StructuringDataRepository;
import com.keensense.search.utils.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Created by zhanx xiaohui on 2019-05-06.
 */
@Service
@Slf4j
@RefreshScope
public class EsServiceImpl {

    @Resource(name = "${structuringData.repository}")
    private StructuringDataRepository structuringDataRepository;
    @Autowired
    private JsonConvertUtil jsonConvertUtil;
    @Autowired
    private EsQueryUtil esQueryUtil;
    @Autowired
    private AlarmRepository alarmRepository;

    /**
     * 批量写入结构化分析结果到ES中
     * {
     * "EventListObject":[],
     * "APEListObject":[],
     * "VehicleflowrateListObject":[],
     * "ViolationListObject":[]
     * }
     *
     * @param structJson  外部的请求json
     * @param listName    结果数据列表名称
     * @param objectName  结果数据对象名称
     * @param targetClass 结果数据对象类型
     * @param idName      id字段名称
     * @return 响应json
     */
    public JSONObject batchInsert(JSONObject structJson, String listName, String objectName,
                                  Class targetClass, String idName) {
        ServletRequestAttributes reqAttrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = reqAttrs.getRequest();
        String url = request.getRequestURL().toString();
        JSONArray targetObjects = structJson.getJSONObject(listName).getJSONArray(objectName);
        JSONArray responseStatusArray = new JSONArray();
        String id = "";
        JSONObject jsonObject = null;
        String ip = Utils.getIpAddress(request);
        //循环处理请求中的多个人脸对象
        for (int i = 0; i < targetObjects.size(); i++) {
            try {
                jsonObject = targetObjects.getJSONObject(i);
                id = jsonObject.getString(idName);
                log.info("handler date, list name is {}, id is {}, ip is {}", listName, id, ip);
                if (StringUtils.isEmpty(id)) {
                    throw new VideoException("id is empty");
                }
                //将json转换为可存储到es的对象
                Object result = parseToTargetObject(jsonObject, targetClass);
                //将装换后的对象存储到es中
                structuringDataRepository.save(result);
                if (targetClass == ViolationResult.class || targetClass == VehicleflowrateResult.class) {
                    JSONObject cacheObject = new JSONObject();
                    cacheObject.put(objectName, jsonObject);
                    alarmRepository.trafficInsert(id, cacheObject.toJSONString());
                }
                if (targetClass == ViolationResult.class || targetClass == EventResult.class) {
                    saveImageAndVideo(result, targetClass);
                }
                responseStatusArray.add(ResponseUtil.createSuccessResponse(id, url));
            } catch (Exception e) {
                log.error("handler request failed,the request is {}", jsonObject, e);
                responseStatusArray.add(ResponseUtil
                        .createFailedResponse(id, url, ResponseUtil.STATUS_CODE_FAILED,
                                e.getMessage()));
            }
        }
        return ResponseUtil.genereteInsertResponse(responseStatusArray);
    }

    /**
     * 查询单个采集设备，暂时未实现
     *
     * @param id 采集设备id
     * @return 单个采集设备
     */
    public <T> String query(String id, String idName, Class<T> tClass, String responseObjectName) {
        try {
            if (StringUtils.isEmpty(id)) {
                throw new VideoException("id is empty");
            }
            String jsonString = null;
            List<T> list = structuringDataRepository.query(idName, id, tClass);
            jsonString = generatorQueryResponse(list.get(0), responseObjectName)
                    .toJSONString();
            return jsonString;
        } catch (Exception e) {
            log.error("", e);
        }
        return jsonConvertUtil.generatorEmptyQueryResponse(responseObjectName).toJSONString();
    }

    /**
     * 批量查询采集设备
     *
     * @param parameterMap 查询条件
     * @return 查询结果
     */
    public <T> String batchQuery(Map<String, String[]> parameterMap, String responseListName,
                                 String resposneObjectName, Class<T> tClass) {
        Map<String, String> map = jsonConvertUtil
                .covertInputParameterToResultParameter(parameterMap);

        JSONObject responseObject = new JSONObject();
        Map<Integer, List<T>> resultMap = structuringDataRepository
                .batchQuery(map, tClass);
        Entry<Integer, List<T>> entry = resultMap.entrySet().iterator().next();
        int count = entry.getKey();
        List<T> results = entry.getValue();
        JSONObject object = new JSONObject();
        object.put("Count", count);
        JSONArray objectArray = new JSONArray();
        for (T result : results) {
            JSONObject innerobject = JSONObject.parseObject(JSON.toJSONString(result));
            objectArray.add(innerobject);
        }
        object.put(resposneObjectName, objectArray);
        responseObject.put(responseListName, object);
        return responseObject.toJSONString();
    }

    public String delete(String idName, String id, Class tclass) {
        try {
            if (-1 == structuringDataRepository.delete(idName, id, tclass)) {
                throw new VideoException("batchDeleteData ES failed");
            }
        } catch (Exception e) {
            log.error("", e);
            return generatorResposnse("-1", "Failed");
        }
        return generatorResposnse("0", "Success");
    }

    public String batchDelete(String locationId, String serialnumber, String timeName, String time, Class tclass) {
        try {
            if (StringUtils.isEmpty(locationId) && StringUtils.isEmpty(serialnumber)) {
                throw new VideoException("the request parametor is empty");
            }
            Map<String, String> map = new HashMap<>();
            map.put("serialnumber", serialnumber);
            map.put("locationId", locationId);
            long esNum = structuringDataRepository
                    .batchDelete(map, timeName,
                            DateUtil.generatorDate(time, "start"),
                            DateUtil.generatorDate(time, "end"), tclass);
        } catch (Exception e) {
            log.error("", e);
            return ResponseUtil.generatorDeleteResposnse("-1", "Failed", e.getMessage());
        }
        return ResponseUtil.generatorDeleteResposnse("0", "Success", null);
    }

    private <T> Object parseToTargetObject(JSONObject object, Class<T> tClass) {
        Object result = JSONObject.toJavaObject(object, tClass);
        if (result instanceof APEResult) {
            checkParametorsEmpty((APEResult) result);
            checkParametorsLength((APEResult) result);
        } else if (result instanceof EventResult) {
            ((EventResult) result).setTimeInDay(getTimeInDay(((EventResult) result).getCreateTime()));
        } else if (result instanceof ViolationResult) {
            ((ViolationResult) result).setTimeInDay(getTimeInDay(((ViolationResult) result).getDatetime()));
        }
        return result;
    }

    public String groupByQuery(JSONObject jsonObject, String index) {
        return esQueryUtil.groupByQuery(jsonObject, index);
    }

    private void checkParametorsEmpty(APEResult result) {
        ParametercheckUtil.checkEmpty("ApeID", result.getApeId());
        ParametercheckUtil.checkEmpty("Name", result.getName());
        ParametercheckUtil.checkEmpty("Model", result.getModel());
        ParametercheckUtil.checkEmpty("IPAddr", result.getIpAddr());
        ParametercheckUtil.checkEmpty("Port", result.getPort());
        ParametercheckUtil.checkEmpty("Longitude", result.getLongitude());
        ParametercheckUtil.checkEmpty("Latitude", result.getLatitude());
        ParametercheckUtil.checkEmpty("PlaceCode", result.getPlaceCode());
        ParametercheckUtil.checkEmpty("IsOnline", result.getIsOnline());
    }

    private void checkParametorsLength(APEResult result) {
        ParametercheckUtil.checkLength("ApeID", result.getApeId(), 20, 20);
        ParametercheckUtil.checkLength("Name", result.getName(), 0, 100);
        ParametercheckUtil.checkLength("Model", result.getModel(), 0, 100);
        ParametercheckUtil.checkLength("IpAddr", result.getIpAddr(), 0, 30);
        ParametercheckUtil.checkLength("IPV6Addr", result.getIpv6Addr(), 64, 64);
        ParametercheckUtil.checkLength("PlaceCode", result.getPlaceCode(), 6, 6);
        ParametercheckUtil.checkLength("Place", result.getPlace(), 0, 256);
        ParametercheckUtil.checkLength("OrgCode", result.getOrgCode(), 12, 12);
        ParametercheckUtil.checkLength("MonitorDirection", result.getMonitorDirection(), 1, 1);
        ParametercheckUtil.checkLength("MonitorAreaDesc", result.getMonitorAreaDesc(), 0, 256);
        ParametercheckUtil.checkLength("IsOnline", result.getIsOnline(), 1, 1);
        ParametercheckUtil.checkLength("OwnerApsID", result.getOwnerApsID(), 20, 20);
        ParametercheckUtil.checkLength("UserId", result.getUserId(), 0, 64);
        ParametercheckUtil.checkLength("Password", result.getPassword(), 0, 32);
    }


    private JSONObject generatorQueryResponse(Object object, String responseObjectName) {
        JSONObject responseObject = new JSONObject();
        responseObject.put(responseObjectName, object);
        return responseObject;
    }

    private String generatorResposnse(String errorCode, String status) {
        JSONObject object = new JSONObject();
        object.put("ErrorCode", errorCode);
        object.put("Status", status);
        return object.toJSONString();
    }

    private Integer getTimeInDay(Date date) {
        String time = new SimpleDateFormat("HHmmss").format(date);
        Integer timeInDay = Integer.parseInt(time);
        return timeInDay;
    }

    private void saveImageAndVideo(Object result, Class tclass) {
        if (tclass == ViolationResult.class) {
            jsonConvertUtil
                    .saveImageResult(((ViolationResult) result).getViolationVideoPath(), "video",
                            ((ViolationResult) result).getDatetime(), ((ViolationResult) result).getId(),
                            ((ViolationResult) result).getSerialnumber());
            jsonConvertUtil
                    .saveImageResult(((ViolationResult) result).getImage1(), "image1",
                            ((ViolationResult) result).getDatetime(), ((ViolationResult) result).getId(),
                            ((ViolationResult) result).getSerialnumber());
            jsonConvertUtil
                    .saveImageResult(((ViolationResult) result).getImage2(), "image2",
                            ((ViolationResult) result).getDatetime(), ((ViolationResult) result).getId(),
                            ((ViolationResult) result).getSerialnumber());
            jsonConvertUtil
                    .saveImageResult(((ViolationResult) result).getImage3(), "image3",
                            ((ViolationResult) result).getDatetime(), ((ViolationResult) result).getId(),
                            ((ViolationResult) result).getSerialnumber());
            jsonConvertUtil
                    .saveImageResult(((ViolationResult) result).getImageCompose(), "imagecompose",
                            ((ViolationResult) result).getDatetime(), ((ViolationResult) result).getId(),
                            ((ViolationResult) result).getSerialnumber());
        } else if (tclass == EventResult.class) {
            jsonConvertUtil
                    .saveImageResult(((EventResult) result).getRemark(), "video",
                            ((EventResult) result).getCreateTime(), ((EventResult) result).getId(),
                            ((EventResult) result).getSerialnumber());
            jsonConvertUtil
                    .saveImageResult(((EventResult) result).getImgUrl(), "image",
                            ((EventResult) result).getCreateTime(), ((EventResult) result).getId(),
                            ((EventResult) result).getSerialnumber());
        }
    }
}
