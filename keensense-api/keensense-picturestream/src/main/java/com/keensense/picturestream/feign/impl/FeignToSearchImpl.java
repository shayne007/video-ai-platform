package com.keensense.picturestream.feign.impl;

import com.alibaba.fastjson.JSONObject;
import com.keensense.picturestream.feign.IFeignToSearch;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description:
 * @Author: wujw
 * @CreateDate: 2019/6/24 10:14
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class FeignToSearchImpl implements FallbackFactory<IFeignToSearch> {

    @Override
    public IFeignToSearch create(Throwable throwable) {
        log.error("IMicroSearchFeign error", throwable);
        return new IFeignToSearch() {
            @Override
            public String sendPictureToKeensense(JSONObject paramMap) {
                return "{\"Status\":\"Failed\",\"ErrorCode\":\"-2\",\"ErrorMessage\":\"connection error!\"}";
            }

        };
    }
}
