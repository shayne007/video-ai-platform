package com.keensense.extension.feign.impl;

import com.keensense.extension.feign.IFeignService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
            public String imageArchivesSave(String imageArchivesId,String imgBase64) {
                return "{\"ResponseStatusListObject\": {\"ResponseStatusObject\": [{\"StatusCode\":1}]}}";
            }
        };
        
    }
}
