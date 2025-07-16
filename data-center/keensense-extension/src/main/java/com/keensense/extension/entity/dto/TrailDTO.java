package com.keensense.extension.entity.dto;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.keensense.extension.constants.ArchivesConstant;
import java.io.Serializable;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * @description:
 * @author jingege
 * @return:
 */
@Data
public class TrailDTO implements Serializable,Comparable {

    private static final long serialVersionUID = -1L;

    @JSONField(name = "ObjType")
    @JsonProperty(value = "ObjType")
    private String objType;
    @JSONField(name = "BodyImgUrl")
    @JsonProperty(value = "BodyImgUrl")
    private String bodyImgUrl;
    @JSONField(name = "FaceImgUrl")
    @JsonProperty(value = "FaceImgUrl")
    private String faceImgUrl;
    @JSONField(name = "MarkTime")
    @JsonProperty(value = "MarkTime")
    private String markTime;
    @JSONField(name = "AppearTime")
    @JsonProperty(value = "AppearTime")
    private String appearTime;
    @JSONField(name = "DisAppearTime")
    @JsonProperty(value = "DisAppearTime")
    private String disAppearTime;
    @JSONField(name = "InsertTime")
    @JsonProperty(value = "InsertTime")
    private String insertTime;
    @JSONField(name = "DeviceID")
    @JsonProperty(value = "DeviceID")
    private String deviceID;
    @JSONField(name = "FaceScore")
    @JsonProperty(value = "FaceScore")
    private Float faceScore;
    @JSONField(name = "FaceQuality")
    @JsonProperty(value = "FaceQuality")
    private Float faceQuality;
    @JSONField(name = "BodyQuality")
    @JsonProperty(value = "BodyQuality")
    private Float bodyQuality;
    @JSONField(name = "BodyScore")
    @JsonProperty(value = "BodyScore")
    private Float bodyScore;
    /**hight/width(保留三位小数)*/
    @JSONField(name = "Proportion")
    @JsonProperty(value = "Proportion")
    private Float proportion;
    @JSONField(name = "ArchivesID")
    @JsonProperty(value = "ArchivesID")
    private String archivesID;
    /**绑定来源 1-人脸 2-人形 默认12（人形+人脸）*/
    @JSONField(name = "TrailSource")
    @JsonProperty(value = "TrailSource")
    private String trailSource;
    @JSONField(name = "Angle")
    @JsonProperty(value = "Angle")
    private String angle;
    @JSONField(name = "ConnectObjectId",serialize = false)
    @JsonProperty(value = "ConnectObjectId")
    private String connectObjectId;
    @JSONField(name = "ConnectObjectType",serialize = false)
    @JsonProperty(value = "ConnectObjectType")
    private Integer connectObjectType;
    @JSONField(name = "PersonID")
    @JsonProperty(value = "PersonID")
    private String personID;
    @JSONField(name = "FaceID")
    @JsonProperty(value = "FaceID")
    private String faceID;
    @JSONField(name = "LeftTopX",serialize = false)
    @JsonProperty(value = "LeftTopX")
    private Float leftTopX;
    @JSONField(name = "LeftTopY",serialize = false)
    @JsonProperty(value = "LeftTopY")
    private Float leftTopY;
    @JSONField(name = "RightBtmX",serialize = false)
    @JsonProperty(value = "RightBtmX")
    private Float rightBtmX;
    @JSONField(name = "RightBtmY",serialize = false)
    @JsonProperty(value = "RightBtmY")
    private Float rightBtmY;
    @JSONField(name = "ArchiveFaceImgUrl")
    @JsonProperty(value = "ArchiveFaceImgUrl")
    private String archiveFaceImgUrl;

    public void initFaceJSON(JSONObject jsonObject){
        this.setFaceID(jsonObject.getString("FaceID"));
        this.setObjType(jsonObject.getString("ObjType"));
        this.setFaceImgUrl(jsonObject.getString("ImgUrl"));
        this.setMarkTime(jsonObject.getString("MarkTime"));
        this.setAppearTime(jsonObject.getString("FaceAppearTime"));
        this.setDisAppearTime(jsonObject.getString("FaceDisAppearTime"));
        this.setInsertTime(jsonObject.getString("InsertTime"));
        this.setDeviceID(jsonObject.getString("DeviceID"));
        this.setFaceScore(jsonObject.getFloat("FaceScore"));
        this.setFaceQuality(jsonObject.getFloat("FaceQuality"));
        this.setProportion(jsonObject.getFloat("Proportion"));
        this.setArchivesID(jsonObject.getString("ArchivesID"));
        this.setTrailSource(jsonObject.getString("TrailSource"));
        this.setConnectObjectId(jsonObject.getString("ConnectObjectId"));
        this.setConnectObjectType(jsonObject.getInteger("ConnectObjectType"));
        this.setBodyImgUrl(StringUtils.EMPTY);
        this.setBodyScore(ArchivesConstant.BODY_SCORE_DEFAULT);
        this.setBodyQuality(ArchivesConstant.BODY_QUALITY_DEFAULT);

    }

    public void initBodyJSON(JSONObject jsonObject){
        this.setObjType(jsonObject.getString("ObjType"));
        this.setBodyImgUrl(jsonObject.getString("ImgUrl"));
        this.setMarkTime(jsonObject.getString("MarkTime"));
        this.setAppearTime(jsonObject.getString("PersonAppearTime"));
        this.setDisAppearTime(jsonObject.getString("PersonDisAppearTime"));
        this.setInsertTime(jsonObject.getString("InsertTime"));
        this.setDeviceID(jsonObject.getString("DeviceID"));
        this.setBodyQuality(jsonObject.getFloat("BodyQuality"));
        this.setBodyScore(jsonObject.getFloat("BodyScore"));
        this.setProportion(jsonObject.getFloat("Proportion"));
        this.setArchivesID(jsonObject.getString("ArchivesID"));
        this.setTrailSource(jsonObject.getString("TrailSource"));
        this.setPersonID(jsonObject.getString("PersonID"));
        this.setAngle(jsonObject.getString("Angle"));
        this.setLeftTopX(jsonObject.getFloat("LeftTopX"));
        this.setLeftTopY(jsonObject.getFloat("LeftTopY"));
        this.setRightBtmX(jsonObject.getFloat("RightBtmX"));
        this.setRightBtmY(jsonObject.getFloat("RightBtmY"));
        this.setFaceImgUrl(StringUtils.EMPTY);
        this.setFaceScore(ArchivesConstant.FACE_SCORE_DEFAULT);
        this.setFaceQuality(ArchivesConstant.FACE_QUALITY_DEFAULT);
    }
    
    @Override
    public int compareTo(Object o) {
        
        return 0;
    }
}
