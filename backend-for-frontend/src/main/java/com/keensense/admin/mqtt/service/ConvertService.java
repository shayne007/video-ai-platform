package com.keensense.admin.mqtt.service;

import com.alibaba.fastjson.JSONObject;
import com.keensense.admin.mqtt.domain.Result;

public interface ConvertService {

    void dataConvert(JSONObject jsonObject, Result result);
}
