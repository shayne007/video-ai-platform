package com.keensense.extension.feign;

import com.alibaba.fastjson.JSONObject;
import com.keensense.extension.feign.impl.MicroSearchFeignFactory;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 
 * @description:消费者Fegin调用类 name为服务提供者的服务名，fallbackFactory为当服务降级时候触发的返回类
 * @author: luowei
 * @createDate:2019年5月15日 下午6:05:04
 * @company:
 */
@FeignClient(name = "keensense-search", fallbackFactory = MicroSearchFeignFactory.class)
public interface IMicroSearchFeign {

	/**
	 *  获取人脸信息
	 * @param map
	 * @date 2018年7月17日
	 * @return 人脸信息
	 */
	@GetMapping(value="/VIID/Faces")
	String getFaces(@RequestParam Map<String, Object> map);

	/***
	 *  获取人形信息
	 * @param map
	 * @return: java.lang.String
	 */
	@GetMapping(value="/VIID/Persons")
	String getBodys(@RequestParam Map<String, Object> map);

	/***
	 * @description: 人脸更新
	 * @param json
	 * @return: java.lang.String
	 */
	@PostMapping(value="/VIID/Faces/Update")
	String updateFaces(@RequestBody JSONObject json);

	/***
	 * @description: 人脸更新
	 * @param json
	 * @return: java.lang.String
	 */
	@PostMapping(value="/VIID/Persons/Update")
	String updateBodys(@RequestBody JSONObject json);
}
