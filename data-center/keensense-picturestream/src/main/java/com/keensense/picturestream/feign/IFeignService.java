package com.keensense.picturestream.feign;

import com.keensense.picturestream.feign.impl.FeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * @author ycl
 */
@FeignClient(name = "keensense-image", fallbackFactory = FeignFallbackFactory.class)
public interface IFeignService {


    @PostMapping(value = "/VIID/Images/{imgId}/Data")
    String getImg(@PathVariable("imgId") String imgId, @RequestBody String body);
}
