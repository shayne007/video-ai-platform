package com.keensense.alarm.service.feign;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ycl
 * @date 2019/5/21
 */
@Component
@Slf4j
public class FeignFallbackFactory implements FallbackFactory<IFeignService> {
    @Override
    public IFeignService create(Throwable throwable) {
        log.error(throwable.getMessage());
        return new IFeignService() {
            @Override
            public JSONObject getOnePerson(String personsId) {
                return JSON.parseObject("{}");
            }

            @Override
            public JSONObject listPersons(String personIds) {
                return JSON.parseObject("{}");
            }

            @Override
            public JSONObject getOneMotorVehicle(String motorVehicleId) {
                return JSON.parseObject("{}");
            }

            @Override
            public JSONObject listMotorVehicles(String motorVehicleIds) {
                return JSON.parseObject("{}");
            }
        };
    }
}
