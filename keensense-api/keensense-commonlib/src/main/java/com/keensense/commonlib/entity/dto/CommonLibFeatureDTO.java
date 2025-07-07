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
public class CommonLibFeatureDTO implements Serializable{

    private static final long serialVersionUID = -1L;

    @JSONField(name = "ID")
    @JsonProperty(value = "ID")
    /**库id*/
    private String id;

    @JSONField(name = "BaseData")
    @JsonProperty(value = "BaseData")
    private String baseData;


    private CommonLibFeatureDTO(){}

    /**
     * @description: 入参校验
     * @param inputJsonObject
     * @param jsonName
     * @return: com.keensense.commonlib.entity.dto.CommonLibFeatureDTO
     */
    public static CommonLibFeatureDTO addFeatureInputPatter(JSONObject inputJsonObject,String jsonName) throws VideoException {
        CommonLibFeatureDTO commonLibFeatureDTO = new CommonLibFeatureDTO();
        Map<String,String> attrAndDefValues = new HashMap<>();
        attrAndDefValues.put("id", JsonPatterUtil.NOT_CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.ID_PATTER);
        attrAndDefValues.put("baseData",JsonPatterUtil.NOT_CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        return (CommonLibFeatureDTO)JsonPatterUtil.JsonPatter(commonLibFeatureDTO,attrAndDefValues,inputJsonObject,jsonName);
    }

}
