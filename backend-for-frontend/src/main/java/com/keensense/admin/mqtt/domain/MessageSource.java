package com.keensense.admin.mqtt.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class MessageSource {

    @JSONField(name= "equipmentId")
    private String equipmentId;

    @JSONField(name= "equipmentName")
    private String equipmentName;

    @JSONField(name= "lat")
    private String lat;

    @JSONField(name= "lng")
    private String lng;
}
