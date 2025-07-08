package com.keensense.extension2.dataobject;

import com.loocme.sys.annotation.database.Column;
import com.loocme.sys.annotation.database.Table;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@Table(name = "archives_info")
public class ArchivesInfo {
    
    @Column(name = "id")
    private String id;
    @Column(name = "face_img_url")
    private String faceImgUrl;
    @Column(name = "face_feature_id")
    private String faceFeatureId;
    
    //需自填
    @Column(name = "p_id")
    private String pId;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "obj_type")
    private Integer objType;
    @Column(name = "relation_id")
    private String relationId;
    @Column(name = "angle")
    private Integer angle;
    
    public ArchivesInfo() {
    }
    
    public ArchivesInfo(String id, String faceImgUrl, String faceFeatureId, Integer objType) {
        this.id = id;
        this.faceImgUrl = faceImgUrl;
        this.faceFeatureId = faceFeatureId;
        this.createTime = new Date();
        this.objType = objType;
    }
    
    public ArchivesInfo(String id, String faceImgUrl, String faceFeatureId, Integer objType,
        String relationId, int angle) {
        this.id = id;
        this.faceImgUrl = faceImgUrl;
        this.faceFeatureId = faceFeatureId;
        this.createTime = new Date();
        this.objType = objType;
        this.relationId = relationId;
        this.angle = angle;
    }
    
    public ArchivesInfo(String id, String faceImgUrl, String faceFeatureId, Integer objType,
        String relationId, int angle, String pId) {
        this.id = id;
        this.faceImgUrl = faceImgUrl;
        this.faceFeatureId = faceFeatureId;
        this.createTime = new Date();
        this.objType = objType;
        this.relationId = relationId;
        this.angle = angle;
        this.pId = pId;
    }
}
