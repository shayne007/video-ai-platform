package com.keensense.admin.feign.impl;

import org.springframework.stereotype.Component;

import com.keensense.admin.feign.MicroFeign;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

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
public class MicroFeignFactory implements FallbackFactory<MicroFeign> {

	@Override
	public MicroFeign create(Throwable cause) {
		log.error(cause.getMessage(), cause);
		return new MicroFeign() {
			@Override
			public String test1(String param, String body) {
				return "调用Get失败，请确定服务是否启动或稍后重试！";
			}

			@Override
			public String post(String param, String body) {
				return "调用post失败，请确定服务是否启动或稍后重试！";
			}
		};
	}

}
