package com.keensense.search.controller;

import com.alibaba.fastjson.JSONObject;
import com.keensense.search.domain.CrowdDensityResult;
import com.keensense.search.service.impl.CrowdDensityService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zhanx xiaohui on 2019-09-10.
 */
@RestController
public class CrowdDensityController {

    @Autowired
    CrowdDensityService service;

    @PostMapping(value = "/VIID/CrowdDensity", produces = "application/json;charset=UTF-8")
    public String batchadd(@RequestBody JSONObject json) {
        return service.batchInsert(json, "CrowdDensityListObject", "CrowdDensityObject", "Id",
            CrowdDensityResult.class);
    }

    @PostMapping(value = "/VIID/CrowdDensity/Add", produces = "application/json;charset=UTF-8")
    public String add(@RequestBody JSONObject json) {
        return service.insert(json, "Id", CrowdDensityResult.class).toJSONString();
    }

    @GetMapping(value = "/VIID/CrowdDensity", produces = "application/json;charset=UTF-8")
    public String get(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        return service.get(parameterMap, "CrowdDensityListObject", "CrowdDensityObject",
            CrowdDensityResult.class);
    }

    @PostMapping(value = "/VIID/CrowdDensity/GetDensityResultList", produces = "application/json;charset=UTF-8")
    public String getBySomeSpecifiedParametor(@RequestBody JSONObject json) {
        return service.getBySomeSpecifiedParametor(json);
    }

    /**
     * 批量删除人群密度相关信息
     * @param serialnumber 任务编号
     * @param time 时间 yyyyMMdd
     * @return
     */
    @DeleteMapping(value = "/VIID/CrowdDensity/Delete/Data")
    public String batchDelete(@RequestParam("Serialnumber") String serialnumber,
                              @RequestParam(value = "Time", required = false) String time){
        return service.batchDeleteData(serialnumber, time);
    }

    /**
     * 批量删除人群密度相关信息(同步删除ES数据和异步删除fastdfs图片)
     * @param serialnumber 任务编号
     * @param time 时间 yyyyMMdd
     * @return
     */
    @DeleteMapping(value = "/VIID/CrowdDensity/Delete/Image")
    public String batchDeleteAsync(@RequestParam("Serialnumber") String serialnumber,
                              @RequestParam(value = "Time", required = false) String time){
        return service.batchDeleteImage(serialnumber, time);
    }

    @ResponseBody
    @PostMapping(value = "/VIID/GroupByQuery/CrowdDensity", produces = "application/json;charset=UTF-8")
    public String groupByQuery(@RequestBody JSONObject json){
        return service.groupByQuery(json,"crowddensity_result");
    }
}
