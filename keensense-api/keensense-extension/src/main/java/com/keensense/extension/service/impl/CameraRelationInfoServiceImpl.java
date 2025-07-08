package com.keensense.extension.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.keensense.extension.entity.CameraRelationInfo;
import com.keensense.extension.mapper.CameraRelationInfoMapper;
import com.keensense.extension.service.ICameraRelationInfoService;
import com.keensense.common.util.ResponseUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/***
 * @description:
 * @author jingege
 * @return:
 */
@Service
@Slf4j
public class CameraRelationInfoServiceImpl extends
    ServiceImpl<CameraRelationInfoMapper, CameraRelationInfo> implements
    ICameraRelationInfoService {
    
    @Autowired
    public CameraRelationInfoMapper cameraRelationInfoMapper;
    
    private static final SimpleDateFormat DATE_SDF_TIME = new SimpleDateFormat("yyyyMMdd HHmmss");
    
    @Override
    public String insertCameraRelation(String jsonStr) {
    
        JSONObject response = ResponseUtil.createSuccessResponse(   "0", "success");
        
        try {
            JSONArray parseArray = JSONArray.parseArray(jsonStr);
            List<CameraRelationInfo> list = Lists.newArrayList();
            for (int i = 0; i < parseArray.size(); i++) {
                String parseStr = parseArray.getString(i);
                CameraRelationInfo cameraRelationInfo = JSONObject
                    .parseObject(parseStr, CameraRelationInfo.class);
                if(cameraRelationInfo.getUpdateTime() == null){
                    cameraRelationInfo.setUpdateTime(new Date());
                }
                list.add(cameraRelationInfo);
            }
            int count = cameraRelationInfoMapper.batchInsert(list);
            response.put("count", count);
        } catch (Exception e) {
            response = ResponseUtil.createSuccessResponse("-1", "fail");
            log.error("==== insertCameraRelation exception : ", e);
        }
        return response.toJSONString();
    }
    
    @Override
    public String deleteCameraRelation(String jsonStr) {
        
        String response = ResponseUtil.generatorDeleteResposnse("0", "success", ""  );
        
        try {
            Map<String, List<Object>> map = analysisPara(jsonStr);
            List<String> idList = (List<String>) (List) map.get("idList");
            List<String> deviceIdList = (List<String>) (List) map.get("deviceIdList");
            List<Date> updateTimeList = (List<Date>) (List) map.get("updateTimeList");
            
            if (CollectionUtils.isEmpty(idList) && CollectionUtils.isEmpty(deviceIdList)
                && CollectionUtils.isEmpty(updateTimeList)) {
                return response;
            }
            cameraRelationInfoMapper.deleteByCondition(idList, deviceIdList, updateTimeList);
        } catch (Exception e) {
            response = String.valueOf(ResponseUtil.createSuccessResponse("-1", "fail"));
            log.error("==== deleteCameraRelation exception : ", e);
        }
        return response;
    }
    
    @Override
    public String queryCameraRelation(String jsonStr) {
    
        JSONObject response = ResponseUtil.createSuccessResponse("0", "success");
    
        try {
            Map<String, List<Object>> map = analysisPara(jsonStr);
            List<String> idList = (List<String>) (List) map.get("idList");
            List<String> deviceIdList = (List<String>) (List) map.get("deviceIdList");
            List<Date> updateTimeList = (List<Date>) (List) map.get("updateTimeList");
    
            List<CameraRelationInfo> cameraRelationInfos = cameraRelationInfoMapper
                .queryByCondition(idList, deviceIdList, updateTimeList);
            response.put("data",JSONObject.toJSONString(cameraRelationInfos));
        } catch (Exception e) {
            response = ResponseUtil.createSuccessResponse("-1", "fail");
            log.error("==== queryCameraRelation exception : ", e);
        }
        return response.toJSONString();
    }
    
    /**
     * 解析请求参数
     * @param jsonStr
     * @return
     * @throws ParseException
     */
    private Map<String, List<Object>> analysisPara(String jsonStr) throws ParseException {
        
        Map<String, List<Object>> map = Maps.newHashMap();
        
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        
        JSONArray updateTimes = jsonObject.getJSONArray("updateTimes");
        List<Object> updateTimeList = Lists.newArrayList();
        if (updateTimes != null) {
            for (int i = 0; i < updateTimes.size(); i++) {
                String dateStr = updateTimes.getString(i);
                updateTimeList.add(DATE_SDF_TIME.parse(dateStr));
            }
        }
        
        JSONArray ids = jsonObject.getJSONArray("ids");
        List<Object> idList = Lists.newArrayList();
        if (ids != null) {
            idList = JSONArray
                .parseArray(ids.toJSONString(), Object.class);
        }
        
        JSONArray deviceIds = jsonObject.getJSONArray("deviceIds");
        List<Object> deviceIdList = Lists.newArrayList();
        if (deviceIds != null) {
            deviceIdList = JSONArray
                .parseArray(deviceIds.toJSONString(), Object.class);
        }
        
        map.put("updateTimeList", updateTimeList);
        map.put("idList", idList);
        map.put("deviceIdList", deviceIdList);
        return map;
    }
    
}
