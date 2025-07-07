package com.keensense.archive.feign;

import com.keensense.archive.feign.impl.FeignFallbackImageFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "keensense-image", fallbackFactory = FeignFallbackImageFactory.class)
public interface FeignImageService {
    
    @PostMapping(value = "/VIID/Images/{imageArchivesId}/Data")
    String imageArchivesSave(@PathVariable("imageArchivesId") String imageArchivesId,
        @RequestBody String imageBase64);
    
}
