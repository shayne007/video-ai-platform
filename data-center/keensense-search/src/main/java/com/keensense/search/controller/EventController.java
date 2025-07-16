package com.keensense.search.controller;

import com.alibaba.fastjson.JSONObject;
import com.keensense.search.domain.EventResult;
import com.keensense.search.service.impl.EsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by zhanx xiaohui on 2019-09-09.
 */
@RestController
public class EventController {

    @Autowired
    EsServiceImpl service;

    private static final String EVENT_OBJECT = "EventObject";

    @PostMapping(value = "/VIID/Event", produces = "application/json;charset=UTF-8")
    public String bathAdd(@RequestBody JSONObject json) {
        return service.batchInsert(json,
                "EventListObject", EVENT_OBJECT, EventResult.class, "Id")
                .toJSONString();
    }

    @GetMapping(value = "/VIID/Event/{id}", produces = "application/json;charset=UTF-8")
    public String query(@PathVariable String id) {
        return service.query(id, "id", EventResult.class, EVENT_OBJECT);
    }

    @GetMapping(value = "/VIID/Event", produces = "application/json;charset=UTF-8")
    public String bathAdd(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        return service.batchQuery(parameterMap, "EventListObject", EVENT_OBJECT,
                EventResult.class);
    }

    @DeleteMapping(value = "/VIID/Event/{id}", produces = "application/json;charset=UTF-8")
    public String delete(@PathVariable String id) {
        return service.delete("id", id, EventResult.class);
    }

    @DeleteMapping(value = "/VIID/Event/Delete", produces = "application/json;charset=UTF-8")
    public String batchDelete(
            @RequestParam(value = "Serialnumber", required = false) String serialnumber,
            @RequestParam(value = "Time", required = false) String time) {
        return service.batchDelete(null, serialnumber, "createTime", time, EventResult.class);
    }

    @PostMapping(value = "/VIID/GroupByQuery/Event", produces = "application/json;charset=UTF-8")
    public String groupByQuery(@RequestBody JSONObject json) {
        return service.groupByQuery(json, "event_result");
    }
}
