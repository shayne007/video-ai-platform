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

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/5/15 10:40
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
public class TrailConditionDTO implements Serializable {

    private static final long serialVersionUID = -1L;


    @JSONField(name = "BeginTime")
    @JsonProperty(value = "BeginTime")
    private String beginTime;
    @JSONField(name = "EndTime")
    @JsonProperty(value = "EndTime")
    private String endTime;
    @JSONField(name = "Threshold")
    @JsonProperty(value = "Threshold")
    private Float threshold;
    @JSONField(name = "MaxArchivesNum")
    @JsonProperty(value = "MaxArchivesNum")
    private Integer maxArchivesNum;
    @JSONField(name = "BaseData")
    @JsonProperty(value = "BaseData")
    private String baseData;
    /**绑定来源 1-人脸 2-人形,入参默认1,2*/
    @JSONField(name = "TrailSource")
    @JsonProperty(value = "TrailSource")
    private String trailSource;
    /**设备ids，查询对应search服务的deviceid*/
    @JSONField(name = "monitorIDList")
    @JsonProperty(value = "monitorIDList")
    private String monitorIDList;

    public static TrailConditionDTO getTrailInputPatter(JSONObject inputJsonObject,String jsonName) throws VideoException {
        TrailConditionDTO trailConditionDTO = new TrailConditionDTO();
        Map<String,String> attrAndDefValues = new HashMap<>(7);
        attrAndDefValues.put("beginTime", JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        attrAndDefValues.put("endTime",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        attrAndDefValues.put("baseData",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        attrAndDefValues.put("trailSource",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.TRAIL_SOURCE_DEFAULT_VALUE);
        attrAndDefValues.put("threshold",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.SEARCH_THRESHOLD_DEFAULT_VALUE);
        attrAndDefValues.put("maxArchivesNum",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.SEARCH_MAXSIZE_DEFAULT_VALUE);
        attrAndDefValues.put("monitorIDList",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        return (TrailConditionDTO)JsonPatterUtil.JsonPatter(trailConditionDTO,attrAndDefValues,inputJsonObject,jsonName);
    }

    public static TrailConditionDTO getArchivesIdInputPatter(JSONObject inputJsonObject,String jsonName) throws VideoException {
        TrailConditionDTO trailConditionDTO = new TrailConditionDTO();
        Map<String,String> attrAndDefValues = new HashMap<>(3);
        attrAndDefValues.put("baseData",JsonPatterUtil.NOT_CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        attrAndDefValues.put("threshold",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.SEARCH_THRESHOLD_DEFAULT_VALUE);
        attrAndDefValues.put("maxArchivesNum",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.SEARCH_MAXSIZE_DEFAULT_VALUE);
        return (TrailConditionDTO)JsonPatterUtil.JsonPatter(trailConditionDTO,attrAndDefValues,inputJsonObject,jsonName);
    }

}

