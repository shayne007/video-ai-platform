package com.keensense.common.util;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * @description:网关返回结果
 * @author: luowei
 * @createDate:2019年5月7日 下午5:48:12
 * @company:
 */
public class ResponseStatus {
	private String requestURL;
	private String statusCode;
	private String statusString;
	private String id;
	private String localTime;
	
	@JSONField(name = "RequestURL")
	public String getRequestURL() {
		return requestURL;
	}
	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}
	
	@JSONField(name = "StatusCode")
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
	@JSONField(name = "StatusString")
	public String getStatusString() {
		return statusString;
	}
	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}
	
	@JSONField(name = "Id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@JSONField(name = "LocalTime")
	public String getLocalTime() {
		return localTime;
	}
	public void setLocalTime(String localTime) {
		this.localTime = localTime;
	}
}
