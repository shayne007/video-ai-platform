package com.keensense.extension.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("archives_info")
public class ArchivesInfo {
    
    private String id;
    
    private String faceImgUrl;
    
    private String faceFeatureId;
    
    //需自填
    private String pId;
    
    private Date createTime;
    
    private Integer objType;
    
    private Integer relationId;
    
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
        Integer relationId, int angle) {
        this.id = id;
        this.faceImgUrl = faceImgUrl;
        this.faceFeatureId = faceFeatureId;
        this.createTime = new Date();
        this.objType = objType;
        this.relationId = relationId;
        this.angle = angle;
    }
    
    public ArchivesInfo(String id, String faceImgUrl, String faceFeatureId, Integer objType,
        Integer relationId, int angle, String pId) {
        this.id = id;
        this.faceImgUrl = faceImgUrl;
        this.faceFeatureId = faceFeatureId;
        this.createTime = new Date();
        this.objType = objType;
        this.relationId = relationId;
        this.angle = angle;
        this.pId = pId;
    }
    
    public void initFace(String id, JSONObject jsonObject, String faceFeatureId, Integer relationId,
        Integer angle) {
        this.id = id;
        this.setObjType(jsonObject.getInteger("ObjType"));
        this.setFaceImgUrl(jsonObject.getString("ImgUrl"));
        this.createTime = new Date();
        this.faceFeatureId = faceFeatureId;
        this.relationId = relationId;
        this.angle = angle;
    }
    
}
