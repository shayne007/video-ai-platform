package com.keensense.search.controller;

import com.alibaba.fastjson.JSONObject;
import com.keensense.search.service.impl.ConvertServiceImpl;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/VIID")
public class ConvertController {

    @Autowired
    private ConvertServiceImpl convertService;

    @PostMapping(value = "/convert/data", produces = "application/json;charset=UTF-8")
    public Map<String,Object> data(@RequestBody JSONObject jsonObject){
        Map<String,Object> map = new HashMap<>();
        try {
            if (log.isDebugEnabled()) {
                log.debug(jsonObject.toJSONString());
            }
            convertService.data(jsonObject);
            map.put("state",true);
        } catch (Exception e) {
            map.put("state",false);
            map.put("message",e.getMessage());
            log.error("error message:",e);
        }
        return map;
    }
}
