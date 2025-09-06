package com.keensense.search.controller;

import com.alibaba.fastjson.JSONObject;
import com.keensense.search.service.impl.NonMotorVehiclesServiceImpl;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhanx xiaohui on 2019-02-23.
 */
@RestController
public class NonMotorVehiclesController {
    @Autowired
    private NonMotorVehiclesServiceImpl service;

    @PostMapping(value = "/VIID/NonMotorVehicles", produces = "application/json;charset=UTF-8")
    public String batchAdd(@RequestBody JSONObject json) {
        return service.batchInsert(json).toJSONString();
    }

    @GetMapping(value = "/VIID/NonMotorVehicles/{nonMotorVehicleID}", produces = "application/json;charset=UTF-8")
    public String query(@PathVariable String nonMotorVehicleID){
        return service.query(nonMotorVehicleID);
    }

    @GetMapping(value = "/VIID/NonMotorVehicles", produces = "application/json;charset=UTF-8")
    public String batchQuery(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        return service.batchQuery(parameterMap);
    }

    @PostMapping(value = "/VIID/NonMotorVehicles/Search", produces = "application/json;charset=UTF-8")
    public String search(@RequestBody JSONObject json) throws InterruptedException {
        return service.search(json);
    }

    @ResponseBody
    @PostMapping(value = "/VIID/GroupByQuery/NonMotorVehicles", produces = "application/json;charset=UTF-8")
    public String groupByQuery(@RequestBody JSONObject json){
        return service.groupByQuery(json,"bike_result");
    }

    @PostMapping(value = "/VIID/NonMotorVehicles/Update", produces = "application/json;charset=UTF-8")
    public String update(@RequestBody JSONObject json) {
        return service.update(json);
    }

    @PostMapping(value = "/VIID/NonMotorVehicles/MultiId", produces = "application/json;charset=UTF-8")
    public String queryByMultiId(@RequestBody JSONObject json) {
        return service.queryByMultiId(json);
    }
}

/**
 * @program: data
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-02-23 13:38
 **/