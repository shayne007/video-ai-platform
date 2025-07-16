package com.keensense.search.feign;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by zhanx xiaohui on 2019-05-24.
 */
@FeignClient(name = "keensense-extension", fallbackFactory = FeignToArchiveFactory.class)
public interface FeignToArchive {
    @PostMapping("/VIID/Archives")
    String getDocument(@RequestBody String body);

    @Headers({"Content-Type: application/json"})
    @PostMapping(value = "/VIID/Feature/Search")
    String sendFeature(@RequestBody String body);

    @Headers({"Content-Type: application/json"})
    @PostMapping(value = "/VIID/Picture/Face/Struct")
    String getFeature(@RequestBody String body);
}