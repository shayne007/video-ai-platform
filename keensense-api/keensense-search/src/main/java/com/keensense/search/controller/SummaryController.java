package com.keensense.search.controller;

import com.alibaba.fastjson.JSONObject;
import com.keensense.search.service.impl.SummaryServiceImpl;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhanx xiaohui on 2019-05-08.
 */
@RestController
public class SummaryController {
    @Autowired
    SummaryServiceImpl service;

    @PostMapping(value = "/VIID/Summary", produces = "application/json;charset=UTF-8")
    public String bathAdd(@RequestBody JSONObject json) {

        return service.batchInsert(json).toJSONString();
    }

    @GetMapping(value = "/VIID/Summary/{id}", produces = "application/json;charset=UTF-8")
    public String query(@PathVariable String id){
        return service.query(id);
    }

    @GetMapping(value = "/VIID/Summary", produces = "application/json;charset=UTF-8")
    public String batchQuery(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        return service.batchQuery(parameterMap);
    }

    @PostMapping(value = "/rest/taskManage/getAllResultList", produces = "application/json;charset=UTF-8")
    public String getAllResultList(@RequestBody JSONObject json) {
        return service.getAllResultList(json);
    }
}

/**
 * @program: data
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-05-08 09:12
 **/