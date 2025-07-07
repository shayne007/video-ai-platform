package com.keensense.alarm.service.feign;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author ycl
 */
@FeignClient(name = "keensense-search", fallbackFactory = FeignFallbackFactory.class)
public interface IFeignService {
    /**
     * 根据id查询人员
     *
     * @param personsId id
     * @return Person
     */
    @GetMapping("/VIID/Persons/{personsId}")
    JSONObject getOnePerson(@PathVariable("personsId") String personsId);

    /**
     * 获取人员列表
     *
     * @param personIds ids ,分隔
     * @return Persons
     */
    @GetMapping("/VIID/Persons")
    JSONObject listPersons(@RequestParam("Person.Id.In") String personIds);

    /**
     * 根据id查询车辆
     *
     * @param motorVehicleId id
     * @return MotorVehicle
     */
    @GetMapping("/VIID/MotorVehicles/{motorVehicleId}")
    JSONObject getOneMotorVehicle(@PathVariable("motorVehicleId") String motorVehicleId);

    /**
     * 获取车辆列表
     *
     * @param motorVehicleIds ids ,分隔
     * @return MotorVehicles
     */
    @GetMapping("/VIID/MotorVehicles")
    JSONObject listMotorVehicles(@RequestParam("MotorVehicle.Id.In") String motorVehicleIds);


}
