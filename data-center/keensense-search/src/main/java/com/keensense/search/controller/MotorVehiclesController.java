package com.keensense.search.controller;

import com.alibaba.fastjson.JSONObject;
import com.keensense.search.service.impl.MotorVehiclesServiceImpl;
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
public class MotorVehiclesController {
  @Autowired
  private MotorVehiclesServiceImpl motorVehiclesService;

  @PostMapping(value = "/VIID/MotorVehicles", produces = "application/json;charset=UTF-8")
  public String batchAdd(@RequestBody JSONObject jsonObject) {
    return motorVehiclesService.batchInsert(jsonObject).toJSONString();
  }


  @GetMapping(value = "/VIID/MotorVehicles/{motorVehicleID}", produces = "application/json;charset=UTF-8")
  public String query(@PathVariable String motorVehicleID){
    return motorVehiclesService.query(motorVehicleID);
  }

  @GetMapping(value = "/VIID/MotorVehicles", produces = "application/json;charset=UTF-8")
  public String batchQuery(HttpServletRequest request){
    Map<String, String[]> parameterMap = request.getParameterMap();
    return motorVehiclesService.batchQuery(parameterMap);
  }

  @PostMapping(value = "/VIID/MotorVehicles/Search", produces = "application/json;charset=UTF-8")
  public String search(@RequestBody JSONObject json) throws InterruptedException {
    return motorVehiclesService.search(json);
  }

  @ResponseBody
  @PostMapping(value = "/VIID/GroupByQuery/MotorVehicles", produces = "application/json;charset=UTF-8")
  public String groupByQuery(@RequestBody JSONObject json){
    return motorVehiclesService.groupByQuery(json,"vlpr_result");
  }

  @PostMapping(value = "/VIID/MotorVehicles/Update", produces = "application/json;charset=UTF-8")
  public String update(@RequestBody JSONObject json) {
    return motorVehiclesService.update(json);
  }

  @PostMapping(value = "/VIID/MotorVehicles/MultiId", produces = "application/json;charset=UTF-8")
  public String queryByMultiId(@RequestBody JSONObject json) {
    return motorVehiclesService.queryByMultiId(json);
  }
}