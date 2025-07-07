package com.keensense.extension2.linkobject;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ArchiveIn {
    
    private int objType;
    private String imgUrl;
    private double quality;
    private int rectX;
    private int rectY;
    private int rectWidth;
    private int rectHeight;
    private String feature;
    private String featureId;
    private double yaw;
    private double roll;
    private double pitch;
}
