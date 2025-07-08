package com.keensense.admin.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ColorVo implements Serializable {
    // 人
    public static final String HUMAN_COLOR_TYPE = "1";
    
    // 非机动车
    public static final String BIKE_COLOR_TYPE = "2";
    
    // 机动车
    public static final String CAR_COLOR_TYPE = "3";
    
    
    private Integer id;

    private String colorId;

    private Integer colorRgbTag;

    private Integer colorBgrTag;

    private String colorHexTag;

    private String colorName;

    private String fuzzyColor;

    /**
     * 色号
     */
    private String colorNumber;

    private String info1;

    private String info2;

    private String colorBgrTagIds;

    private String colorCode;

}