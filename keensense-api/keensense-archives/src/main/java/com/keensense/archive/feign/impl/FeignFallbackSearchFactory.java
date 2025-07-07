package com.keensense.archive.feign.impl;

import com.alibaba.fastjson.JSONObject;
import com.keensense.archive.feign.FeignImageService;
import com.keensense.archive.feign.FeignSearchService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class FeignFallbackSearchFactory implements FallbackFactory<FeignSearchService> {
    
    @Override
    public FeignSearchService create(Throwable throwable) {
        log.error(throwable.getMessage());
        return new FeignSearchService() {
            @Override
            public String updateFaces(JSONObject json) {
                return "{\"ResponseStatusListObject\": {\"ResponseStatusObject\": [{\"StatusCode\":1}]}}";
            }
        };
    }
}
