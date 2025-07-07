package com.keensense.extension.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("camera_relation")
public class CameraRelationInfo {
    
    @JSONField(name= "id")
    private String id;
    
    @JSONField(name= "device_id")
    private String deviceId;
    
    @JSONField(name= "update_time", format = "yyyyMMdd HHmmss")
    private Date updateTime;

}
