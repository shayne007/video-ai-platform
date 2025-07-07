package com.keensense.archive.feign.impl;

import com.keensense.archive.feign.FeignImageService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class FeignFallbackImageFactory implements FallbackFactory<FeignImageService> {
    @Override
    public FeignImageService create(Throwable throwable) {
        
        log.error(throwable.getMessage());
        return new FeignImageService() {
            @Override
            public String imageArchivesSave(String imageArchivesId,String imgBase64) {
                return "{\"ResponseStatusListObject\": {\"ResponseStatusObject\": [{\"StatusCode\":1}]}}";
            }
        };
        
    }
}
