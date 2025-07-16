package com.keensense.search.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.keensense.search.domain.NonMotorVehiclesResult;
import com.keensense.search.utils.JsonConvertUtil;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

/**
 * Created by zhanx xiaohui on 2019-02-26.
 */
@Service
@Slf4j
@RefreshScope
public class NonMotorVehiclesServiceImpl extends DataServiceImpl {
    private static final String APPEAR_TIME_NAME = "AppearTime";
    private static final String DISAPPEAR_TIME_NAME = "DisappearTime";

    private static final String NONMOTORVEHICLE_LIST_OBJECT_NAME = "NonMotorVehicleObject";
    private static final String NONMOTORVEHICLE_OBJECT_NAME = "NonMotorVehicleListObject";
    private static final String RESPONSE_NONMOTORVEHICLE_OBJECT_NAME = "NonMotorVehicleObject";

    private static final String NONMOTORVEHICLE_ID = "NonMotorVehicleID";
    /**
     * 批量写入非机动车数据
     * @param jsonObject 外部的请求json
     * @return 响应json
     */
    @Override
    public JSONObject batchInsert(JSONObject jsonObject) {
        //log.debug("NonMotorVehicles object is {}", jsonObject);
        return batchInsert(jsonObject,
            NONMOTORVEHICLE_OBJECT_NAME,
            NONMOTORVEHICLE_LIST_OBJECT_NAME, NONMOTORVEHICLE_ID, RESPONSE_NONMOTORVEHICLE_OBJECT_NAME,
            JsonConvertUtil.NON_MOTOR_VEHICLES_OBJECT_TYPE,
            JsonConvertUtil.NON_MOTOR_VEHICLES_OBJECT_TYPE_NAME,
            APPEAR_TIME_NAME, DISAPPEAR_TIME_NAME, NonMotorVehiclesResult.class);
    }

    /**
     * 查询单个非机动车数据
     * @param id 机动车id
     * @return 单个机动车数据
     */
    @Override
    public String query(String id) {
        return query("id", id, RESPONSE_NONMOTORVEHICLE_OBJECT_NAME,
            NonMotorVehiclesResult.class);
    }

    /**
     * 批量查询非机动车
     * @param parameterMap 查询条件
     * @return 查询结果
     */
    @Override
    public String batchQuery(Map<String, String[]> parameterMap) {
        return batchQuery(parameterMap, NONMOTORVEHICLE_OBJECT_NAME, NONMOTORVEHICLE_LIST_OBJECT_NAME,
            NonMotorVehiclesResult.class);
    }

    /**
     * 以图搜图
     * @param jsonObject 搜图的请求json
     * @return 搜图的结果
     */
    @Override
    public String search(JSONObject jsonObject) throws InterruptedException {
        return imageSearchService
            .search(jsonObject, JsonConvertUtil.NON_MOTOR_VEHICLES_OBJECT_TYPE);
    }

    public String update(JSONObject object) {
        return update(object, NONMOTORVEHICLE_OBJECT_NAME,
            NONMOTORVEHICLE_LIST_OBJECT_NAME, NONMOTORVEHICLE_ID, NonMotorVehiclesResult.class);
    }

    public String queryByMultiId(JSONObject object){
        return queryByMultiId(object, "NonMotorVehicles", NONMOTORVEHICLE_OBJECT_NAME, NONMOTORVEHICLE_LIST_OBJECT_NAME, NonMotorVehiclesResult.class).toJSONString();
    }
}

/**
 * @program: data
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-02-26 16:19
 **/