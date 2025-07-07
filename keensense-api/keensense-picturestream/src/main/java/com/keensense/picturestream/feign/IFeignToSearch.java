package com.keensense.picturestream.feign;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Description: 调用data模块的feign
 * @Author: wujw
 * @CreateDate: 2019-06-21 15:43
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@FeignClient(name = "keensense-search")
public interface IFeignToSearch {
    
    /***
     * 发送图片信息到视图库
     * @return: java.lang.String
     */
    @GetMapping("/VIID/convert/data")
    String sendPictureToKeensense(JSONObject paramMap);
}
