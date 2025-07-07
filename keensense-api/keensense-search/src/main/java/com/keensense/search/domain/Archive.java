package com.keensense.search.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

/**
 * Created by zhanx xiaohui on 2019-05-10.
 */
@Data
@ToString(callSuper=true, includeFieldNames=true)
public class Archive {
    private String id;

    @JSONField(name = "FaceID")
    private String faceId;

    @JSONField(name = "ObjType")
    private Integer objType;

    @JSONField(name = "FaceImgUrl")
    private String faceImgUrl;

    @JSONField(name = "FaceQuality")
    private Float faceQuality;

    @JSONField(name = "BodyImgUrl")
    private String bodyImgUrl;

    @JSONField(name = "BodyQuality")
    private Float bodyQuality;

    @JSONField(name = "Proportion")
    private String proportion;

    @JSONField(name = "ArchivesId")
    private String archivesID;

    @JSONField(name = "TrailSource")
    private String trailSource;

    @JSONField(name = "FaceScore")
    private Float faceScore;

    @JSONField(name = "BodyScore")
    private Float bodyScore;

    @JSONField(name = "Angle")
    private Integer angle;

    @JSONField(name = "BodyID")
    private String bodyId;

    @JSONField(name = "FaceFeatureId")
    private String faceFeatureId;
}

/**
 * @program: data
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-05-10 14:01
 **/