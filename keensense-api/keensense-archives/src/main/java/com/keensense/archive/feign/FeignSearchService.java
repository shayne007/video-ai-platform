package com.keensense.archive.feign;

import com.alibaba.fastjson.JSONObject;
import com.keensense.archive.feign.impl.FeignFallbackImageFactory;
import com.keensense.archive.feign.impl.FeignFallbackSearchFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "keensense-search", fallbackFactory = FeignFallbackSearchFactory.class)
public interface FeignSearchService {
    
    /***
     * @description: 人脸更新
     * @param json
     * @return: java.lang.String
     */
    @PostMapping(value="/VIID/Faces/Update")
    String updateFaces(@RequestBody JSONObject json);
    
    
}
