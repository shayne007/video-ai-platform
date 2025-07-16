package com.keensense.commonlib.entity.dto;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.keensense.common.exception.VideoException;
import com.keensense.commonlib.util.JsonPatterUtil;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/5/21 15:49
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
@Accessors(chain = true)
public class CommonLibDTO implements Serializable{

    private static final long serialVersionUID = -1L;

    @JSONField(name = "Name")
    @JsonProperty(value = "Name")
    private String name;

    @JSONField(name = "ID")
    @JsonProperty(value = "ID")
    private String id;

    @JSONField(name = "Type")
    @JsonProperty(value = "Type")
    private Integer type;

    @JSONField(name = "BaseData")
    @JsonProperty(value = "BaseData")
    private String baseData;

    @JSONField(name = "Feature")
    @JsonProperty(value = "Feature")
    private String feature;

    @JSONField(name = "LibIDList")
    @JsonProperty(value = "LibIDList")
    private String libIDList;

    @JSONField(name = "Threshold")
    @JsonProperty(value = "Threshold")
    private Float threshold;

    @JSONField(name = "MaxResult")
    @JsonProperty(value = "MaxResult")
    private Integer maxResult;


    private CommonLibDTO(){}

    /**
     * @description: 入参验证
     * @param inputJsonObject
     * @param jsonName
     * @return: com.keensense.commonlib.entity.dto.CommonLibDTO
     */
    public static CommonLibDTO createLibInputPatter(JSONObject inputJsonObject,String jsonName) throws VideoException{
        CommonLibDTO commonLibDTO = new CommonLibDTO();
        Map<String,String> attrAndDefValues = new HashMap<>(2);
        attrAndDefValues.put("type",JsonPatterUtil.NOT_CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.TYPE_PATTER);
        attrAndDefValues.put("name",JsonPatterUtil.NOT_CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NAME_PATTER);
        return (CommonLibDTO)JsonPatterUtil.JsonPatter(commonLibDTO,attrAndDefValues,inputJsonObject,jsonName);
    }

    public static CommonLibDTO updateLibInputPatter(JSONObject inputJsonObject,String jsonName) throws VideoException{
        CommonLibDTO commonLibDTO = new CommonLibDTO();
        Map<String,String> attrAndDefValues = new HashMap<>(2);
        attrAndDefValues.put("id",JsonPatterUtil.NOT_CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.ID_PATTER);
        attrAndDefValues.put("name",JsonPatterUtil.NOT_CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NAME_PATTER);
        return (CommonLibDTO)JsonPatterUtil.JsonPatter(commonLibDTO,attrAndDefValues,inputJsonObject,jsonName);
    }

    public static CommonLibDTO searchLibInputPatter(JSONObject inputJsonObject,String jsonName) throws VideoException{
        CommonLibDTO commonLibDTO = new CommonLibDTO();
        Map<String,String> attrAndDefValues = new HashMap<>(5);
        attrAndDefValues.put("libIDList",JsonPatterUtil.NOT_CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        attrAndDefValues.put("type",JsonPatterUtil.NOT_CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.TYPE_PATTER);
        attrAndDefValues.put("baseData",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        attrAndDefValues.put("feature",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        attrAndDefValues.put("threshold",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.SEARCH_THRESHOLD_DEFAULT_VALUE);
        attrAndDefValues.put("maxResult",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.SEARCH_MAXSIZE_DEFAULT_VALUE);
        return (CommonLibDTO)JsonPatterUtil.JsonPatter(commonLibDTO,attrAndDefValues,inputJsonObject,jsonName);
    }

}
