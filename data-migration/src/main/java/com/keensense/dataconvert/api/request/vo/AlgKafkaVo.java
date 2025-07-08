package com.keensense.dataconvert.api.request.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.api.request.vo
 * @Description： <p> AlgKafkaVo - 相当于当做是透传参数去使用</p>
 * @Author： - Jason
 * @CreatTime：2019/7/26 - 9:36
 * @Modify By：
 * @ModifyTime： 2019/7/26
 * @Modify marker：
 */
public class AlgKafkaVo implements Serializable {

    /**
     * 推送到 特征分析算法的 uuid
     */
    private String uuid;

    /**
     * objType - 1 ＝ ⼈人 ｜ 2 ＝⾞車車 ｜ 3 ＝⼈人臉 ｜ 4 ＝⼈人騎⾞車車
     */
    private int objType;

    /**
     * serialNumber
     */
    private String serialNumber;

    /**
     * startFramePts
     */
    private String startFramePts;

    /**
     * creatTime - 源头数据时间 [mysql]
     */
    private Date creatTime;

    /**
     * featureData
     */
    private String featureData;

    /**
     * 参数
     */
    private Long frameIndex;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getObjType() {
        return objType;
    }

    public void setObjType(int objType) {
        this.objType = objType;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getStartFramePts() {
        return startFramePts;
    }

    public void setStartFramePts(String startFramePts) {
        this.startFramePts = startFramePts;
    }

    public String getFeatureData() {
        return featureData;
    }

    public void setFeatureData(String featureData) {
        this.featureData = featureData;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public Long getFrameIndex() {
        return frameIndex;
    }

    public void setFrameIndex(Long frameIndex) {
        this.frameIndex = frameIndex;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }
}
