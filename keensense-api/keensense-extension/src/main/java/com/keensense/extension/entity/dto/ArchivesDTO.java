package com.keensense.extension.entity.dto;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.keensense.common.exception.VideoException;
import com.keensense.extension.util.JsonPatterUtil;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/5/15 10:40
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
@Accessors(chain = true)
public class ArchivesDTO implements Serializable {

    private static final long serialVersionUID = -1L;
    @JSONField(name = "BodyID")
    @JsonProperty(value = "BodyID")
    private String bodyID;
    @JSONField(name = "FaceID")
    @JsonProperty(value = "FaceID")
    private String faceID;
    @JSONField(name = "FaceImgUrl")
    @JsonProperty(value = "FaceImgUrl")
    private String faceImgUrl;
    @JSONField(name = "ObjType")
    @JsonProperty(value = "ObjType")
    private Integer objType;
    @JSONField(name = "FaceQuality")
    @JsonProperty(value = "FaceQuality")
    private Float faceQuality;
    @JSONField(name = "BodyImgUrl")
    @JsonProperty(value = "BodyImgUrl")
    private String bodyImgUrl;
    @JSONField(name = "BodyQuality")
    @JsonProperty(value = "BodyQuality")
    private Float bodyQuality;
    /**hight/width(保留三位小数)*/
    @JSONField(name = "Proportion")
    @JsonProperty(value = "Proportion")
    private Float proportion;
    @JSONField(name = "Yaw")
    @JsonProperty(value = "Yaw")
    private Float yaw;
    @JSONField(name = "Pitch")
    @JsonProperty(value = "Pitch")
    private Float pitch;
    @JSONField(name = "Roll")
    @JsonProperty(value = "Roll")
    private Float roll;
    @JSONField(name = "ArchivesID")
    @JsonProperty(value = "ArchivesID")
    private String archivesID;
    @JSONField(name = "TrailSource")
    @JsonProperty(value = "TrailSource")
    private Integer trailSource;
    @JSONField(name = "FaceScore")
    @JsonProperty(value = "FaceScore")
    private Float faceScore;
    @JSONField(name = "BodyScore")
    @JsonProperty(value = "BodyScore")
    private Float bodyScore;
    @JSONField(name = "Angle")
    @JsonProperty(value = "Angle")
    private Integer angle;
    /**左上角X坐标*/
    @JSONField(name = "LeftTopX",serialize=false)
    @JsonProperty(value = "LeftTopX")
    private Integer leftTopX;
    /**左上角Y坐标*/
    @JSONField(name = "LeftTopY",serialize=false)
    @JsonProperty(value = "LeftTopY")
    private Integer leftTopY;
    /**右下角X坐标*/
    @JSONField(name = "RightBtmX",serialize=false)
    @JsonProperty(value = "RightBtmX")
    private Integer rightBtmX;
    /**右下角Y坐标*/
    @JSONField(name = "RightBtmY",serialize=false)
    @JsonProperty(value = "RightBtmY")
    private Integer rightBtmY;
    /**任务标识*/
    @JSONField(name = "TaskID")
    @JsonProperty(value = "TaskID")
    private String taskID;
    /**时间*/
    @JSONField(name = "Time")
    @JsonProperty(value = "Time")
    private String time;
    @JSONField(name = "DeviceID")
    @JsonProperty(value = "DeviceID")
    private String deviceID;
    @JSONField(name = "FaceFeatureId")
    @JsonProperty(value = "FaceFeatureId")
    private String faceFeatureId;
    @JSONField(name = "FaceFeature",serialize=false)
    @JsonProperty(value = "FaceFeature")
    private String faceFeature;
    @JSONField(name = "BodyFeature",serialize=false)
    @JsonProperty(value = "BodyFeature")
    private String bodyFeature;
    @JSONField(name = "FaceImgUrlFront",serialize=false)
    @JsonProperty(value = "FaceImgUrlFront")
    private String faceImgUrlFront;
    @JSONField(name = "FaceImgUrlSide",serialize=false)
    @JsonProperty(value = "FaceImgUrlSide")
    private String faceImgUrlSide;
    @JSONField(name = "FaceImgUrlBottom",serialize=false)
    @JsonProperty(value = "FaceImgUrlBottom")
    private String faceImgUrlBottom;

    public static ArchivesDTO getArchivesInputPatter(JSONObject inputJsonObject,String jsonName) throws VideoException {
        ArchivesDTO archivesDTO = new ArchivesDTO();
        Map<String,String> attrAndDefValues = new HashMap<>();
        attrAndDefValues.put("faceID", JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        attrAndDefValues.put("bodyID",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        attrAndDefValues.put("deviceID",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        attrAndDefValues.put("objType",JsonPatterUtil.NOT_CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.TYPE_PERSON_FACE_PATTER);
        attrAndDefValues.put("faceImgUrl",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        attrAndDefValues.put("faceQuality",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        attrAndDefValues.put("bodyImgUrl",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        attrAndDefValues.put("bodyQuality",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        attrAndDefValues.put("angle",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        attrAndDefValues.put("leftTopX",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        attrAndDefValues.put("leftTopY",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        attrAndDefValues.put("rightBtmX",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        attrAndDefValues.put("rightBtmY",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        attrAndDefValues.put("bodyFeature",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        attrAndDefValues.put("faceFeature",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        attrAndDefValues.put("pitch",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        attrAndDefValues.put("roll",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        attrAndDefValues.put("yaw",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
//        attrAndDefValues.put("taskID",JsonPatterUtil.NOT_CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.ID_PATTER);
//        attrAndDefValues.put("time",JsonPatterUtil.NOT_CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.DATE_PATTER);
        return (ArchivesDTO)JsonPatterUtil.JsonPatter(archivesDTO,attrAndDefValues,inputJsonObject,jsonName);
    }

    public static ArchivesDTO addArchivesIdInputPatter(JSONObject inputJsonObject,String jsonName) throws VideoException {
        ArchivesDTO archivesDTO = new ArchivesDTO();
        Map<String,String> attrAndDefValues = new HashMap<>(1);
        attrAndDefValues.put("faceImgUrlFront",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        attrAndDefValues.put("faceImgUrlSide",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        attrAndDefValues.put("faceImgUrlBottom",JsonPatterUtil.CAN_NULL+JsonPatterUtil.SEPARATOR+JsonPatterUtil.NONE_PATTER_OR_DEFAULT_VALUE);
        return (ArchivesDTO)JsonPatterUtil.JsonPatter(archivesDTO,attrAndDefValues,inputJsonObject,jsonName);
    }

}

