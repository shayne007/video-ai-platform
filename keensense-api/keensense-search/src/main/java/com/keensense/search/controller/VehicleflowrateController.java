package com.keensense.search.controller;

import com.alibaba.fastjson.JSONObject;
import com.keensense.search.domain.VehicleflowrateResult;
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
public class VehicleflowrateController {

    @Autowired
    EsServiceImpl service;

    private static final String VEHICLEFLOWRATE_OBJECT = "VehicleflowrateObject";

    @PostMapping(value = "/VIID/Vehicleflowrate", produces = "application/json;charset=UTF-8")
    public String bathAdd(@RequestBody JSONObject json) {
        return service
            .batchInsert(json, "VehicleflowrateListObject", VEHICLEFLOWRATE_OBJECT,
                VehicleflowrateResult.class,
                "Id")
            .toJSONString();
    }

    @GetMapping(value = "/VIID/Vehicleflowrate/{id}", produces = "application/json;charset=UTF-8")
    public String query(@PathVariable String id) {
        return service.query(id, "id", VehicleflowrateResult.class, VEHICLEFLOWRATE_OBJECT);
    }

    @GetMapping(value = "/VIID/Vehicleflowrate", produces = "application/json;charset=UTF-8")
    public String bathAdd(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        return service.batchQuery(parameterMap, "VehicleflowrateListObject", VEHICLEFLOWRATE_OBJECT,
            VehicleflowrateResult.class);
    }

    @DeleteMapping(value = "/VIID/Vehicleflowrate/{id}", produces = "application/json;charset=UTF-8")
    public String delete(@PathVariable String id) {
        return service.delete("id", id, VehicleflowrateResult.class);
    }

    @DeleteMapping(value = "/VIID/Vehicleflowrate/Delete", produces = "application/json;charset=UTF-8")
    public String batchDelete(
        @RequestParam(value = "LocationId", required = false) String locationId,
        @RequestParam(value = "Serialnumber", required = false) String serialnumber,
        @RequestParam(value = "Time", required = false) String time) {
        return service.batchDelete(locationId, serialnumber, "datetime", time, VehicleflowrateResult.class);
    }

    @PostMapping(value = "/VIID/GroupByQuery/Vehicleflowrate", produces = "application/json;charset=UTF-8")
    public String groupByQuery(@RequestBody JSONObject json) {
        return service.groupByQuery(json, "vehicleflowrate_result");
    }

}