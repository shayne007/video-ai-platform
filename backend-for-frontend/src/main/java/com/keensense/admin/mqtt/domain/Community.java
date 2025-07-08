package com.keensense.admin.mqtt.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class Community {

    @JSONField(name= "id")
    protected String id;

    @JSONField(name= "name")
    protected String name;
}
