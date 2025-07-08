package com.keensense.common.platform.bo.video;

import java.io.Serializable;

public class U2SImageBikeBo implements Serializable{
	private static final long serialVersionUID = -6483270855945112317L;
	private String sex;//性别
	private String age;//年龄
	private Long helmet;//头盔
	private String coatStyle;//上衣类型 
	private String upColorTag1;//上衣颜色 
	private String upColorTag2;//上衣颜色 
	private Long glasses; //是否带眼镜
	private Long bag; //是否背包
	private Long umbrella; //是否带雨伞
	private String feature;
	private Integer leftTopX;
	private Integer leftTopY;
	private Integer rightBtmX;
	private Integer rightBtmY;
	private String nonMotorVehicleID;
	private Long handbag;
	private String helmetColorTag1;//头盔颜色.
	private String storagePath;//图片路径.
	private String deviceID;
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public Long getHelmet() {
		return helmet;
	}
	public void setHelmet(Long helmet) {
		this.helmet = helmet;
	}
	public String getCoatStyle() {
		return coatStyle;
	}
	public void setCoatStyle(String coatStyle) {
		this.coatStyle = coatStyle;
	}
	public String getUpColorTag1() {
		return upColorTag1;
	}
	public void setUpColorTag1(String upColorTag1) {
		this.upColorTag1 = upColorTag1;
	}
	public String getUpColorTag2() {
		return upColorTag2;
	}
	public void setUpColorTag2(String upColorTag2) {
		this.upColorTag2 = upColorTag2;
	}
	public Long getGlasses() {
		return glasses;
	}
	public void setGlasses(Long glasses) {
		this.glasses = glasses;
	}
	public Long getBag() {
		return bag;
	}
	public void setBag(Long bag) {
		this.bag = bag;
	}
	public Long getUmbrella() {
		return umbrella;
	}
	public void setUmbrella(Long umbrella) {
		this.umbrella = umbrella;
	}
	public String getFeature() {
		return feature;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}
	public Integer getLeftTopX() {
		return leftTopX;
	}
	public void setLeftTopX(Integer leftTopX) {
		this.leftTopX = leftTopX;
	}
	public Integer getLeftTopY() {
		return leftTopY;
	}
	public void setLeftTopY(Integer leftTopY) {
		this.leftTopY = leftTopY;
	}
	public Integer getRightBtmX() {
		return rightBtmX;
	}
	public void setRightBtmX(Integer rightBtmX) {
		this.rightBtmX = rightBtmX;
	}
	public Integer getRightBtmY() {
		return rightBtmY;
	}
	public void setRightBtmY(Integer rightBtmY) {
		this.rightBtmY = rightBtmY;
	}

	public Long getHandbag() {
		return handbag;
	}
	public void setHandbag(Long handbag) {
		this.handbag = handbag;
	}
	public String getHelmetColorTag1() {
		return helmetColorTag1;
	}
	public void setHelmetColorTag1(String helmetColorTag1) {
		this.helmetColorTag1 = helmetColorTag1;
	}
	public String getStoragePath() {
		return storagePath;
	}
	public void setStoragePath(String storagePath) {
		this.storagePath = storagePath;
	}
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public String getNonMotorVehicleID() {
		return nonMotorVehicleID;
	}
	public void setNonMotorVehicleID(String nonMotorVehicleID) {
		this.nonMotorVehicleID = nonMotorVehicleID;
	}

	
	
	

}
