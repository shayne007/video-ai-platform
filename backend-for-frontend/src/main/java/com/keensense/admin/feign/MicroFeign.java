package com.keensense.admin.feign;

import com.keensense.admin.feign.impl.MicroFeignFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 
 * @description:消费者Fegin调用类 name为服务提供者的服务名，fallbackFactory为当服务降级时候触发的返回类
 * @author: luowei
 * @createDate:2019年5月15日 下午6:05:04
 * @company:
 */
@FeignClient(name = "keensense-demo", fallbackFactory = MicroFeignFactory.class)
public interface MicroFeign {

	/**
	 * 调用提供者的参数值
	 * 
	 * @date 2018年7月17日
	 * @param param       url请求参数
	 * @param body        post请求参数
	 * @return
	 */
	@GetMapping("user/get")
	String test1(@RequestParam("param") String param, @RequestBody String body);

	@GetMapping("user/post")
	String post(@RequestParam("param") String param, @RequestBody String body);
}
