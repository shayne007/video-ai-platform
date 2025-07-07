package com.keensense.extension.entity.dto;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.keensense.common.exception.VideoException;
import com.keensense.extension.util.JsonPatterUtil;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/5/15 10:40
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
@Accessors(chain = true)
public class CameraRelationDTO implements Serializable {

    private static final long serialVersionUID = -1L;
    @JSONField(name = "RelationID")
    @JsonProperty(value = "RelationID")
    private String id;
    @JSONField(name = "DeviceList")
    @JsonProperty(value = "DeviceList")
    private String deviceList;

    public static CameraRelationDTO addCameraInputPatter(JSONObject inputJsonObject,String jsonName) throws VideoException {
        CameraRelationDTO archivesDTO = new CameraRelationDTO();
        Map<String,String> attrAndDefValues = new HashMap<>();
        attrAndDefValues.put("deviceList",JsonPatterUtil.NOT_CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.DATE_PATTER);
        return (CameraRelationDTO)JsonPatterUtil.JsonPatter(archivesDTO,attrAndDefValues,inputJsonObject,jsonName);
    }
    
    /***
     * @description: 有的就覆盖，没有就新增
     * @param inputJsonObject
 * @param jsonName
     * @return: com.keensense.extension.entity.dto.CameraRelationDTO
     */
    public static CameraRelationDTO updOrDelCameraInputPatter(JSONObject inputJsonObject,String jsonName) throws VideoException {
        CameraRelationDTO archivesDTO = new CameraRelationDTO();
        Map<String,String> attrAndDefValues = new HashMap<>();
        attrAndDefValues.put("relationID",JsonPatterUtil.NOT_CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.ID_PATTER);
        attrAndDefValues.put("deviceList",JsonPatterUtil.NOT_CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.DATE_PATTER);
        return (CameraRelationDTO)JsonPatterUtil.JsonPatter(archivesDTO,attrAndDefValues,inputJsonObject,jsonName);
    }
    
    /***
     * @description: 全量删除此关系下所有deviceid
     * @param inputJsonObject
 * @param jsonName
     * @return: com.keensense.extension.entity.dto.CameraRelationDTO
     */
    public static CameraRelationDTO deleteCameraRelationInputPatter(JSONObject inputJsonObject,String jsonName) throws VideoException {
        CameraRelationDTO archivesDTO = new CameraRelationDTO();
        Map<String,String> attrAndDefValues = new HashMap<>();
        attrAndDefValues.put("relationID",JsonPatterUtil.NOT_CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.ID_PATTER);
        return (CameraRelationDTO)JsonPatterUtil.JsonPatter(archivesDTO,attrAndDefValues,inputJsonObject,jsonName);
    }


}

