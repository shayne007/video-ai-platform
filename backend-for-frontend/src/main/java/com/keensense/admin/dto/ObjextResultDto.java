package com.keensense.admin.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ObjextResultDto {
    private Long id;

    private Long cameraid;
    
    private String serialnumber;

    private String imgurl;

    private Short objtype;

    private Integer maincolor;

    private Integer upcolor;

    private Integer lowcolor;

    private Integer maincolorTag;

    private Integer upcolorTag;
    private Integer upcolorTag1;
    private Integer upcolorTag2;

    private Integer lowcolorTag;
    private Integer lowcolorTag1;
    private Integer lowcolorTag2;
    
    private Byte sex;

    private Byte age;
    
    private Integer helmetcolor;
    
    private String helmetcolorStr;
    
    private String helmetcolorName;
    
    private Integer passengersUpColor;
    
    private String passengersUpColorStr;
    
    private String passengersUpColorName;
    
    private Integer bikeGenre;
    
    private Integer bikeColor;
    
    private Integer seatingCount;
    
    private String helmet;
    
    private String helmet1;
    private String helmet2;
    private Integer helmetcolor1;
    private Integer helmetcolor2;
    
	private Byte wheels;
    

	private Byte size;
    
    private Integer glasses;
    private Integer bag;
    private Integer umbrella;
    private Integer angle;

    private Integer tubeid;

    private Integer objid;

    private Integer startframeidx;

    private Integer endframeidx;

    private Integer startframepts;

    private Integer endframepts;

    private Integer frameidx;

    private Short width;

    private Short height;

    private Short x;

    private Short y;

    private Short w;

    private Short h;

    private Float distance;

    private Date createtime;
    
    private Date inserttime;
    
    private String createtimeStr;

}