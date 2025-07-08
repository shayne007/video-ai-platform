package com.keensense.common.platform.bo.video;

import java.io.Serializable;

public class U2SImageCarBo implements Serializable{
	private static final long serialVersionUID = -6483270855945112317L;
	private Integer calling;//是否打电话
	private String isCovered;//是否被遮挡
	private String plateNo; //车牌号码
	private String plateColor; //车牌颜色
	private String plateClass; //车牌类型
	private String vehicleColor; //车身颜色
	private String feature;
	private Integer leftTopX;
	private Integer leftTopY;
	private Integer rightBtmX;
	private Integer rightBtmY;
	private String motorVehicleID;
	private String vehicleBrand;//车辆品牌
	private String vehicleClass;//车辆类型
	private String hasPlate;//有无车牌
	private String storageUrl1;
	private Integer sunvisor;//遮阳板
	private Integer crash;//是否撞车
	private String deviceID;
	private String appearTime;//目标出现时间
	public Integer getCalling() {
		return calling;
	}
	public void setCalling(Integer calling) {
		this.calling = calling;
	}
	public String getIsCovered() {
		return isCovered;
	}
	public void setIsCovered(String isCovered) {
		this.isCovered = isCovered;
	}
	public String getPlateNo() {
		return plateNo;
	}
	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}
	public String getPlateColor() {
		return plateColor;
	}
	public void setPlateColor(String plateColor) {
		this.plateColor = plateColor;
	}
	public String getPlateClass() {
		return plateClass;
	}
	public void setPlateClass(String plateClass) {
		this.plateClass = plateClass;
	}
	public String getVehicleColor() {
		return vehicleColor;
	}
	public void setVehicleColor(String vehicleColor) {
		this.vehicleColor = vehicleColor;
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
	public String getMotorVehicleID() {
		return motorVehicleID;
	}
	public void setMotorVehicleID(String motorVehicleID) {
		this.motorVehicleID = motorVehicleID;
	}
	public String getVehicleBrand() {
		return vehicleBrand;
	}
	public void setVehicleBrand(String vehicleBrand) {
		this.vehicleBrand = vehicleBrand;
	}
	public String getVehicleClass() {
		return vehicleClass;
	}
	public void setVehicleClass(String vehicleClass) {
		this.vehicleClass = vehicleClass;
	}
	public String getHasPlate() {
		return hasPlate;
	}
	public void setHasPlate(String hasPlate) {
		this.hasPlate = hasPlate;
	}
	public String getStorageUrl1() {
		return storageUrl1;
	}
	public void setStorageUrl1(String storageUrl1) {
		this.storageUrl1 = storageUrl1;
	}
	public Integer getSunvisor() {
		return sunvisor;
	}
	public void setSunvisor(Integer sunvisor) {
		this.sunvisor = sunvisor;
	}
	public Integer getCrash() {
		return crash;
	}
	public void setCrash(Integer crash) {
		this.crash = crash;
	}
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getAppearTime() {
		return appearTime;
	}

	public void setAppearTime(String appearTime) {
		this.appearTime = appearTime;
	}
}
