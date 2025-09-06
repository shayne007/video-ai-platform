package com.keensense.extension.feign.impl;

import com.alibaba.fastjson.JSONObject;
import com.keensense.extension.feign.IMicroSearchFeign;
import feign.hystrix.FallbackFactory;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Feign调用服务降级触发的类
 * 
 * @description:
 * @author: luowei
 * @createDate:2019年5月15日 下午6:05:25
 * @company:
 */
@Component
@Slf4j
public class MicroSearchFeignFactory implements FallbackFactory<IMicroSearchFeign> {

	@Override
	public IMicroSearchFeign create(Throwable cause) {
		log.error("IMicroSearchFeign error", cause);
		return new IMicroSearchFeign() {
			@Override
			public String getFaces(Map<String, Object> map) {
				return "{\"FaceListObject\": {\"Count\": 0,\"FaceObject\": []}}";
			}
			@Override
			public String getBodys(Map<String, Object> map) {
				return "{\"PersonListObject\": {\"Count\": 0,\"PersonObject\": []}}";
			}
			@Override
			public String updateFaces(JSONObject json) {
				return "{\"ResponseStatusListObject\": {\"ResponseStatusObject\": [{\"StatusCode\":1}]}}";
			}
			@Override
			public String updateBodys(JSONObject json) {
				return "{\"ResponseStatusListObject\": {\"ResponseStatusObject\": [{\"StatusCode\":1}]}}";
			}
		};
	}

}
