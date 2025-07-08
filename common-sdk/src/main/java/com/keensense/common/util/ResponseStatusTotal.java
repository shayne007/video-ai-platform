package com.keensense.common.util;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @description:网关返回结果
 * @author: luowei
 * @createDate:2019年5月7日 下午5:51:14
 * @company:
 */
public class ResponseStatusTotal {
	private ResponseStatusList responseStatusList;
	
	@JSONField(name = "ResponseStatusListObject")
	public ResponseStatusList getResponseStatusList() {
		return responseStatusList;
	}

	public void setResponseStatusList(ResponseStatusList responseStatusList) {
		this.responseStatusList = responseStatusList;
	}
}
