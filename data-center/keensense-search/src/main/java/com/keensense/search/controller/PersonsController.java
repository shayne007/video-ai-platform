package com.keensense.search.controller;

import com.alibaba.fastjson.JSONObject;
import com.keensense.search.service.impl.PersonsServiceImpl;
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
 * Created by memory_fu on 2019/2/26.
 */
@RestController
public class PersonsController {

    @Autowired
    PersonsServiceImpl personsService;

    @PostMapping(value = "/VIID/Persons", produces = "application/json;charset=UTF-8")
    public String batchAdd(@RequestBody JSONObject jsonObject) {
        return personsService.batchInsert(jsonObject).toJSONString();
    }

    @GetMapping(value = "/VIID/Persons/{personsId}", produces = "application/json;charset=utf-8")
    public String query(@PathVariable String personsId) {
        return personsService.query(personsId);
    }

    @GetMapping(value = "/VIID/Persons", produces = "application/json;charset=utf-8")
    public String batchQuery(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        return personsService.batchQuery(parameterMap);
    }

    @PostMapping(value = "/VIID/Persons/Search", produces = "application/json;charset=UTF-8")
    public String search(@RequestBody JSONObject json) throws InterruptedException {
        return personsService.search(json);
    }

    @ResponseBody
    @PostMapping(value = "/VIID/GroupByQuery/Persons", produces = "application/json;charset=UTF-8")
    public String groupByQuery(@RequestBody JSONObject json) {
        return personsService.groupByQuery(json, "objext_result");
    }


    @PostMapping(value = "/VIID/Persons/Update", produces = "application/json;charset=UTF-8")
    public String update(@RequestBody JSONObject json) {
        return personsService.update(json);
    }

    @PostMapping(value = "/VIID/Persons/MultiId", produces = "application/json;charset=UTF-8")
    public String queryByMultiId(@RequestBody JSONObject json) {
        return personsService.queryByMultiId(json);
    }

}
