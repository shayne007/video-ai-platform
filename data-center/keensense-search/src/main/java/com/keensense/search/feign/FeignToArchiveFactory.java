package com.keensense.search.feign;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by zhanx xiaohui on 2019-05-24.
 */
@Component
@Slf4j
public class FeignToArchiveFactory implements FallbackFactory<FeignToArchive> {
    @Override
    public FeignToArchive create(Throwable cause) {
        log.error(cause.getMessage(), cause);
        return new FeignToArchive() {
            @Override
            public String getDocument(String body) {
                return null;
            }

            @Override
            public String sendFeature(String body) {
                return null;
            }

            @Override
            public String getFeature(String body) {
                return null;
            }

        };
    }
}

/**
 * @program: platform
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-05-24 17:01
 **/