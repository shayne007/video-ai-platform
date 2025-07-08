package com.keensense.common.platform.bo.video;

import java.io.Serializable;

public class U2SBo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6483270855945112317L;
	private String analysisTaskId;
	private String upColor;//上衣颜色
	private String lowColor;//下衣颜色
	private String mainColor;//车辆颜色
	private String lastupdateTime;
	private String startTime;
	private String endTime;
	private String objType;
	private String targetimgurl;
	private String searchSerinumList;
	private Byte sex;//性别
	private Byte age;//年龄
	private Integer glasses;//眼镜
	private Integer handbag;//手提包
	private Integer umbrella;//打伞
	private Integer helmet;//头盔
	private Short bikeHasPlate;//挂牌
	private String helmetcolorStr;//头盔颜色
	private Integer angle;//姿态
	private Integer bag;//是否背包
	private Byte wheels;//类别
	private Integer bikeGenre;//类型
	private String vehiclekind;//车型
    private String vehiclebrand;//品牌
    private String vehicleseries;//车系
    private String license;//牌照
	private String plateColor;//车牌颜色
    private String upStyle;//上衣款式
    private Integer lawStyle;//下衣款式
    private Integer angle2;//方向
    private String type;
    private String cap;
    private String respirator;

	public String getHairStyle() {
		return hairStyle;
	}

	public void setHairStyle(String hairStyle) {
		this.hairStyle = hairStyle;
	}

	private String hairStyle;


	public String getRespirator() {
		return respirator;
	}

	public void setRespirator(String respirator) {
		this.respirator = respirator;
	}

	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getVehiclekind() {
		return vehiclekind;
	}
	public void setVehiclekind(String vehiclekind) {
		this.vehiclekind = vehiclekind;
	}
	public String getVehiclebrand() {
		return vehiclebrand;
	}
	public void setVehiclebrand(String vehiclebrand) {
		this.vehiclebrand = vehiclebrand;
	}
	public String getVehicleseries() {
		return vehicleseries;
	}
	public void setVehicleseries(String vehicleseries) {
		this.vehicleseries = vehicleseries;
	}
	public Integer getBikeGenre() {
		return bikeGenre;
	}
	public void setBikeGenre(Integer bikeGenre) {
		this.bikeGenre = bikeGenre;
	}
	public Byte getWheels() {
		return wheels;
	}
	public void setWheels(Byte wheels) {
		this.wheels = wheels;
	}
	public Integer getAngle() {
		return angle;
	}
	public void setAngle(Integer angle) {
		this.angle = angle;
	}
	public Integer getBag() {
		return bag;
	}
	public void setBag(Integer bag) {
		this.bag = bag;
	}
	public String getHelmetcolorStr() {
		return helmetcolorStr;
	}
	public void setHelmetcolorStr(String helmetcolorStr) {
		this.helmetcolorStr = helmetcolorStr;
	}
	public Integer getHelmet() {
		return helmet;
	}
	public void setHelmet(Integer helmet) {
		this.helmet = helmet;
	}
	public Short getBikeHasPlate() {
		return bikeHasPlate;
	}
	public void setBikeHasPlate(Short bikeHasPlate) {
		this.bikeHasPlate = bikeHasPlate;
	}
	public Integer getGlasses() {
		return glasses;
	}
	public void setGlasses(Integer glasses) {
		this.glasses = glasses;
	}
	public Integer getHandbag() {
		return handbag;
	}
	public void setHandbag(Integer handbag) {
		this.handbag = handbag;
	}
	public Integer getUmbrella() {
		return umbrella;
	}
	public void setUmbrella(Integer umbrella) {
		this.umbrella = umbrella;
	}
	public Byte getSex() {
		return sex;
	}
	public void setSex(Byte sex) {
		this.sex = sex;
	}
	public Byte getAge() {
		return age;
	}
	public void setAge(Byte age) {
		this.age = age;
	}
	public String getSearchSerinumList() {
		return searchSerinumList;
	}
	public void setSearchSerinumList(String searchSerinumList) {
		this.searchSerinumList = searchSerinumList;
	}
	public String getTargetimgurl() {
		return targetimgurl;
	}
	public void setTargetimgurl(String targetimgurl) {
		this.targetimgurl = targetimgurl;
	}
	public String getObjType() {
		return objType;
	}
	public void setObjType(String objType) {
		this.objType = objType;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getLastupdateTime() {
		return lastupdateTime;
	}
	public void setLastupdateTime(String lastupdateTime) {
		this.lastupdateTime = lastupdateTime;
	}
	public String getAnalysisTaskId() {
		return analysisTaskId;
	}
	public void setAnalysisTaskId(String analysisTaskId) {
		this.analysisTaskId = analysisTaskId;
	}
	public String getUpColor() {
		return upColor;
	}
	public void setUpColor(String upColor) {
		this.upColor = upColor;
	}
	public String getLowColor() {
		return lowColor;
	}
	public void setLowColor(String lowColor) {
		this.lowColor = lowColor;
	}
	public String getMainColor() {
		return mainColor;
	}
	public void setMainColor(String mainColor) {
		this.mainColor = mainColor;
	}
	public String getUpStyle() {
		return upStyle;
	}
	public void setUpStyle(String upStyle) {
		this.upStyle = upStyle;
	}
	public Integer getLawStyle() {
		return lawStyle;
	}
	public void setLawStyle(Integer lawStyle) {
		this.lawStyle = lawStyle;
	}
	public Integer getAngle2() {
		return angle2;
	}
	public void setAngle2(Integer angle2) {
		this.angle2 = angle2;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getPlateColor() {
		return plateColor;
	}

	public void setPlateColor(String plateColor) {
		this.plateColor = plateColor;
	}
}
