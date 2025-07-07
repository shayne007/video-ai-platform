package com.keensense.common.util;

/**
 * 返回结果集
 * @author luowei
 * @date 2019年5月6日
 * @description
 */
public class ResultBean {
	/**
	 * 返回状态码
	 */
	private String ret;
	
	/**
	 * 状态码含义
	 */
	private String desc;
	
	/**
	 * 返回的具体结果
	 */
	private Object data;
	
	
	public String getRet() {
		return ret;
	}
	public void setRet(String ret) {
		this.ret = ret;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "ResultBean [ret=" + ret + ", desc=" + desc + ", data=" + data + "]";
	}
	
}
