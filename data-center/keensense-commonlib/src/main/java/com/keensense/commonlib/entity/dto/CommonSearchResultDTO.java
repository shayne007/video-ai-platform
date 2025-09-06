package com.keensense.commonlib.entity.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/5/21 15:49
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
public class CommonSearchResultDTO {

    @JSONField(name = "ID")
    @JsonProperty(value = "ID")
    private String id;

    @JSONField(name = "Score")
    @JsonProperty(value = "Score")
    private Float score;

    @JSONField(name = "LibID")
    @JsonProperty(value = "LibID")
    private String libID;

    public CommonSearchResultDTO(){}

    public CommonSearchResultDTO(String id,Float score,String libID){
        this.id = id;
        this.score = score;
        this.libID = libID;
    }

}
