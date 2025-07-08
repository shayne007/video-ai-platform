package com.keensense.dataconvert.biz.entity;

import java.io.Serializable;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.entity
 * @Description： <p> FeatureResult - 识别后的特征结果 </p>
 * @Author： - Jason
 * @CreatTime：2019/7/25 - 13:54
 * @Modify By：
 * @ModifyTime： 2019/7/25
 * @Modify marker：
 */
public class FeatureResult implements Serializable {

    /**
     * 特征矢量
     */
    private  String featureVector;

    /**
     * distance
     */
    private String distance;

    public String getFeatureVector() {
        return featureVector;
    }

    public void setFeatureVector(String featureVector) {
        this.featureVector = featureVector;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
