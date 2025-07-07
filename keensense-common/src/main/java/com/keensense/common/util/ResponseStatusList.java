package com.keensense.common.util;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
* @description:网关返回结果
* @author: luowei
* @createDate:2019年5月7日 下午5:50:42
* @company:
 */
public class ResponseStatusList {
	private Object responseStatus;

	@JSONField(name = "ResponseStatusObject")
	public Object getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(Object responseStatus) {
		this.responseStatus = responseStatus;
	}

}
