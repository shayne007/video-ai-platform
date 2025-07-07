package com.keensense.search.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.keensense.search.domain.VlprResult;
import com.keensense.search.utils.JsonConvertUtil;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * Created by memory_fu on 2019/2/26.
 */
@org.springframework.stereotype.Service
@Slf4j
@RefreshScope
public class MotorVehiclesServiceImpl extends DataServiceImpl {
    private static final String APPEAR_TIME_NAME = "AppearTime";
    private static final String DISAPPEAR_TIME_NAME = "DisappearTime";

    private static final String MOTORVEHICLE_LIST_OBJECT_NAME = "MotorVehicleObject";
    private static final String MOTORVEHICLE_OBJECT_NAME = "MotorVehicleListObject";
    private static final String RESPONSE_MOTORVEHICLE_OBJECT_NAME = "MotorVehicleObject";

    private static final String MOTORVEHICLE_ID = "MotorVehicleID";
    /**
     * 批量写入机动车数据
     * @param jsonObject 外部的请求json
     * @return 响应json
     */
    @Override
    public JSONObject batchInsert(JSONObject jsonObject) {
        //log.debug("MotorVehicles object is {}", jsonObject);
        return batchInsert(jsonObject,
            MOTORVEHICLE_OBJECT_NAME,
            MOTORVEHICLE_LIST_OBJECT_NAME, MOTORVEHICLE_ID, RESPONSE_MOTORVEHICLE_OBJECT_NAME,
            JsonConvertUtil.MOTORVEHICLES_OBJECT_TYPE,
            JsonConvertUtil.MOTOR_VEHICLES_OBJECT_TYPE_NAME,
            APPEAR_TIME_NAME, DISAPPEAR_TIME_NAME, VlprResult.class);
    }

    /**
     * 查询单个机动车数据
     * @param id 机动车id
     * @return 单个机动车数据
     */
    @Override
    public String query(String id) {
        return query("id", id, RESPONSE_MOTORVEHICLE_OBJECT_NAME, VlprResult.class);
    }

    /**
     * 批量查询机动车
     * @param parameterMap 查询条件
     * @return 查询结果
     */
    @Override
    public String batchQuery(Map<String, String[]> parameterMap) {
        return batchQuery(parameterMap, MOTORVEHICLE_OBJECT_NAME, MOTORVEHICLE_LIST_OBJECT_NAME,
            VlprResult.class);
    }

    /**
     * 以图搜图
     * @param jsonObject 搜图的请求json
     * @return 搜图的结果
     */
    @Override
    public String search(JSONObject jsonObject) throws InterruptedException {
        return imageSearchService.search(jsonObject, JsonConvertUtil.MOTORVEHICLES_OBJECT_TYPE);
    }

    public String update(JSONObject object) {
        return update(object, MOTORVEHICLE_OBJECT_NAME, MOTORVEHICLE_LIST_OBJECT_NAME, MOTORVEHICLE_ID, VlprResult.class);
    }

    public String queryByMultiId(JSONObject object){
        return queryByMultiId(object, "MotorVehicles", MOTORVEHICLE_OBJECT_NAME, MOTORVEHICLE_LIST_OBJECT_NAME, VlprResult.class).toJSONString();
    }
}
