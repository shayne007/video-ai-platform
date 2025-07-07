package com.keensense.extension.feign;

import com.keensense.extension.feign.impl.FeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "keensense-image", fallbackFactory = FeignFallbackFactory.class)
public interface IFeignService {
    
    @PostMapping(value = "/VIID/Images/{imageArchivesId}/Data")
    String imageArchivesSave(@PathVariable("imageArchivesId") String imageArchivesId,@RequestBody String imageBase64);
    
}
