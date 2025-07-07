package com.keensense.search.controller;

import com.alibaba.fastjson.JSONObject;
import com.keensense.search.domain.VehicleflowrateResult;
import com.keensense.search.domain.ViolationResult;
import com.keensense.search.service.impl.EsServiceImpl;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhanx xiaohui on 2019-07-09.
 */
@RestController
public class ViolationController {

    @Autowired
    EsServiceImpl service;

    private static final String VIOLATION_OBJECT = "ViolationObject";

    @PostMapping(value = "/VIID/Violation", produces = "application/json;charset=UTF-8")
    public String bathAdd(@RequestBody JSONObject json) {
        return service
            .batchInsert(json, "ViolationListObject", VIOLATION_OBJECT, ViolationResult.class,
                "Id")
            .toJSONString();
    }

    @GetMapping(value = "/VIID/Violation/{id}", produces = "application/json;charset=UTF-8")
    public String query(@PathVariable String id) {
        return service.query(id, "id", ViolationResult.class, VIOLATION_OBJECT);
    }

    @GetMapping(value = "/VIID/Violation", produces = "application/json;charset=UTF-8")
    public String bathAdd(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        return service.batchQuery(parameterMap, "ViolationListObject", VIOLATION_OBJECT,
            ViolationResult.class);
    }

    @DeleteMapping(value = "/VIID/Violation/{id}", produces = "application/json;charset=UTF-8")
    public String delete(@PathVariable String id) {
        return service.delete("id", id, ViolationResult.class);
    }

    @DeleteMapping(value = "/VIID/Violation/Delete", produces = "application/json;charset=UTF-8")
    public String batchDelete(
        @RequestParam(value = "LocationId", required = false) String locationId,
        @RequestParam(value = "Serialnumber", required = false) String serialnumber,
        @RequestParam(value = "Time", required = false) String time) {
        return service
            .batchDelete(locationId, serialnumber, "datetime", time, ViolationResult.class);
    }

    @PostMapping(value = "/VIID/GroupByQuery/Violation", produces = "application/json;charset=UTF-8")
    public String groupByQuery(@RequestBody JSONObject json) {
        return service.groupByQuery(json, "violation_result");
    }
}