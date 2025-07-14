package com.keensense.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.keensense.search.service.impl.CapacityService;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by zhanx xiaohui on 2019-08-30.
 */
@RestController
@Slf4j
public class CapacityController {
    @Autowired
    CapacityService capacityService;

    @PutMapping(value = "/VIID/Capacity", produces = "application/json;charset=UTF-8")
    public String setCapacity(@RequestBody JSONObject object) {
        return capacityService.setCapacity(object);
    }

    @PutMapping(value = "/VIID/Capacity/featuresearch", produces = "application/json;charset=UTF-8")
    public String featuresearchCapacity(@RequestBody JSONObject object) {
        return capacityService.setFeaturesearchCapacity(object);
    }

    @GetMapping(value = "/VIID/Capacity", produces = "application/json;charset=UTF-8")
    public String queryCapacity() {
        return capacityService.queryCapacity();
    }

}
