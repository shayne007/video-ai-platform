package com.keensense.search.controller;

import com.alibaba.fastjson.JSONObject;
import com.keensense.search.domain.APEResult;
import com.keensense.search.service.impl.EsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by zhanx xiaohui on 2019-05-06.
 */
@RestController
public class APEsController {

    @Autowired
    EsServiceImpl service;

    @PostMapping(value = "/VIID/APEs", produces = "application/json;charset=UTF-8")
    public String bathAdd(@RequestBody JSONObject json) {
        return service.batchInsert(json,
                "APEListObject", "APEObject", APEResult.class, "ApeID")
                .toJSONString();
    }

    @GetMapping(value = "/VIID/APEs", produces = "application/json;charset=UTF-8")
    public String bathAdd(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        return service.batchQuery(parameterMap,
                "APEListObject", "APEObject", APEResult.class);
    }
}

