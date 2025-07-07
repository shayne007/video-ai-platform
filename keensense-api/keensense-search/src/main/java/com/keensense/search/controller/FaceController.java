package com.keensense.search.controller;

import com.alibaba.fastjson.JSONObject;
import com.keensense.search.service.impl.FaceServiceImpl;
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
 * Created by zhanx xiaohui on 2019-02-27.
 */
@RestController
public class FaceController {
    @Autowired
    private FaceServiceImpl service;

    @PostMapping(value = "/VIID/Faces", produces = "application/json;charset=UTF-8")
    public String bathAdd(@RequestBody JSONObject json){
        return service.batchInsert(json).toJSONString();
    }

    @GetMapping(value = "/VIID/Faces/{faceId}", produces = "application/json;charset=UTF-8")
    public String query(@PathVariable String faceId){
        return service.query(faceId);
    }

    @GetMapping(value = "/VIID/Faces", produces = "application/json;charset=UTF-8")
    public String batchQuery(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        return service.batchQuery(parameterMap);
    }

    @PostMapping(value = "/VIID/Faces/Search", produces = "application/json;charset=UTF-8")
    public String search(@RequestBody JSONObject json) throws InterruptedException {
        return service.search(json);
    }

    @ResponseBody
    @PostMapping(value = "/VIID/GroupByQuery/Faces", produces = "application/json;charset=UTF-8")
    public String groupByQuery(@RequestBody JSONObject json){
        return service.groupByQuery(json,"face_result");
    }

    @PostMapping(value = "/VIID/Faces/Update", produces = "application/json;charset=UTF-8")
    public String update(@RequestBody JSONObject json) {
        return service.update(json);
    }

    @PostMapping(value = "/VIID/Faces/MultiId", produces = "application/json;charset=UTF-8")
    public String queryByMultiId(@RequestBody JSONObject json) {
        return service.queryByMultiId(json);
    }
}
