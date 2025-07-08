package com.keensense.admin.dto;

/**
 * @Author: dufy
 * @Description: 搜图特征业务类
 * @Date: Created in 16:37 2019/4/26
 * @Version v0.1
 */
public class SearchImageBo {

    /**
     * 特征区域的面积
     */
    private double featureArea;
    /**
     * 特征区域的坐标和宽高
     */
    private String xywh;
    /**
     * 特征数据
     */
    private String feature;
    /**
     * 类型
     */
    private int objType;

    /**
     * 搜图结构化特征-骑行-上衣颜色
     */
    private int uppercolor;

    /**
     * 搜图结构化特征-骑行-车型
     */
    private String biketype;

    /**
     * 搜图结构化特征-车辆-车身颜色
     */
    private int vehicleColor;


    public double getFeatureArea() {
        return featureArea;
    }

    public void setFeatureArea(double featureArea) {
        this.featureArea = featureArea;
    }

    public String getXywh() {
        return xywh;
    }

    public void setXywh(String xywh) {
        this.xywh = xywh;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public int getObjType() {
        return objType;
    }

    public void setObjType(int objType) {
        this.objType = objType;
    }

    public int getUppercolor() {
        return uppercolor;
    }

    public void setUppercolor(int uppercolor) {
        this.uppercolor = uppercolor;
    }

    public String getBiketype() {
        return biketype;
    }

    public void setBiketype(String biketype) {
        this.biketype = biketype;
    }

    public int getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(int vehicleColor) {
        this.vehicleColor = vehicleColor;
    }
}
