package com.keensense.common.platform.bo.video;

import java.io.Serializable;

/**
 * 调用结构化接口所用参数
 * @author wangsj
 *
 */
public class U2SHttpBo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6762922338080029718L;
	private String id;
	private String faceFeature;//人脸特征提取对应 0.千视通 1.格林
	/**
	 * 视频路径 支持实时流、在线视频、ftp、本地
	 */
	private String url;
	/**
	 * 任务类型objext：全目标（行人，车辆，骑行）; summary：浓缩
	 */
	private String type;
	/**
	 * 任务序列号
	 * 支持多任务查询，例如：serialNumber1,serialNumber2，.....，以英文状态下“,”逗号分隔
	 */
	private String serialNumber;
	/**
	 * 分析场景
	 * 默认为0.
	 *	1:一般监控视频场景
	 *	2：卡口、类卡口场景
	 *	4：交通流量统计场景
	 *	8：动态人脸场景
	 *	16：混合行人及动态人脸
	 */
	private String scene;
	/**
	 * param 任务扩展参数：
		1、感性区域、非感兴趣区域。
		其他特定参数传递（需要定制）
	 */
	private String param;
	/**
	 * 起始时间 格式：yyyy-MM-dd HH:mm:ss
	 */
	private String startTime;
	/**
	 * 结束时间 格式：yyyy-MM-dd HH:mm:ss
		起始时间-结束时间不能超过15天
	 */
	private String endTime;
	/**
	 * 0:视频浓缩|1:人|2:车|4：人骑车|不传：全部
	 */
	private String objType;
	/**
	 * 当前页默认1
	 */
	private String pageNo;
	/**
	 * 分页条数
	 */
	private String pageSize;
	/**
	 * 图片进行base64
	 */
	private String picture;

	/**
	 * 特征值
	 */
	private String feature;
	
	private String ids;
	
	private String cameraId;
	//摄像头id
	private String deviceId;

	private String limitNum;

	private String threshold;


	/**
	 * 云搜图任务类型（离线图片,实时任务）
	 */
	private String taskTypes;
	
	/**
	 * 入库时间段
	 */
	private String insertTimeStart;
	
	private String insertTimeEnd;
	
	private String sourceID;
	private String personIds;
	private String bikeIds;
	private String carIds;
	private String faceIds;
	
	private String order;//排序方式：  desc降序 asc升序

	public Integer getTradeOff() {
		return tradeOff;
	}

	public void setTradeOff(Integer tradeOff) {
		this.tradeOff = tradeOff;
	}

	/**
	 * 阈值（0-100）
	 */
	private Integer tradeOff;
	/**
	 * 校正时间
	 */
	private String startAt;

	/**
	 * 查询属性
	 */
	private U2SBo u2sBo;
	
	private U2SImageBikeBo u2SImageBikeBo;
	
	private U2SImageCarBo u2SImageCarBo;
	
	private U2SImagePersonBo u2SImagePersonBo;

	public String getStartAt() {
		return startAt;
	}

	public void setStartAt(String startAt) {
		this.startAt = startAt;
	}

	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getLimitNum() {
		return limitNum;
	}

	public void setLimitNum(String limitNum) {
		this.limitNum = limitNum;
	}

	public String getThreshold() {
		return threshold;
	}

	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}
	public String getInsertTimeStart() {
		return insertTimeStart;
	}
	public void setInsertTimeStart(String insertTimeStart) {
		this.insertTimeStart = insertTimeStart;
	}
	public String getInsertTimeEnd() {
		return insertTimeEnd;
	}
	public void setInsertTimeEnd(String insertTimeEnd) {
		this.insertTimeEnd = insertTimeEnd;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public U2SBo getU2sBo() {
		return u2sBo;
	}
	public void setU2sBo(U2SBo u2sBo) {
		this.u2sBo = u2sBo;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getScene() {
		return scene;
	}
	public void setScene(String scene) {
		this.scene = scene;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getObjextType() {
		return objType;
	}
	public void setObjextType(String objextType) {
		this.objType = objextType;
	}
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getCameraId() {
		return cameraId;
	}
	public void setCameraId(String cameraId) {
		this.cameraId = cameraId;
	}
	public U2SImageBikeBo getU2SImageBikeBo() {
		return u2SImageBikeBo;
	}
	public void setU2SImageBikeBo(U2SImageBikeBo u2sImageBikeBo) {
		u2SImageBikeBo = u2sImageBikeBo;
	}
	public U2SImageCarBo getU2SImageCarBo() {
		return u2SImageCarBo;
	}
	public void setU2SImageCarBo(U2SImageCarBo u2sImageCarBo) {
		u2SImageCarBo = u2sImageCarBo;
	}
	public U2SImagePersonBo getU2SImagePersonBo() {
		return u2SImagePersonBo;
	}
	public void setU2SImagePersonBo(U2SImagePersonBo u2sImagePersonBo) {
		u2SImagePersonBo = u2sImagePersonBo;
	}
	public String getSourceID() {
		return sourceID;
	}
	public void setSourceID(String sourceID) {
		this.sourceID = sourceID;
	}

	public String getTaskTypes() {
		return taskTypes;
	}

	public void setTaskTypes(String taskTypes) {
		this.taskTypes = taskTypes;
	}

	public String getBikeIds() {
		return bikeIds;
	}
	public void setBikeIds(String bikeIds) {
		this.bikeIds = bikeIds;
	}
	public String getCarIds() {
		return carIds;
	}
	public void setCarIds(String carIds) {
		this.carIds = carIds;
	}
	public String getFaceIds() {
		return faceIds;
	}
	public void setFaceIds(String faceIds) {
		this.faceIds = faceIds;
	}
	public String getPersonIds() {
		return personIds;
	}
	public void setPersonIds(String personIds) {
		this.personIds = personIds;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getFaceFeature() {
		return faceFeature;
	}

	public void setFaceFeature(String faceFeature) {
		this.faceFeature = faceFeature;
	}
}
