package com.keensense.admin.dto;

import lombok.Data;

import java.util.Date;

@Data
public class VlprResultDto {
    private Long serialnumber;

    private Long taskid;

    private Integer cameraid;

    private String license;

    private String licenseattribution;

    private String platecolor;

    private Short platetype;

    private Short confidence;

    private Short bright;

    private Short direction;

    private Short locationleft;

    private Short locationtop;

    private Short locationright;

    private Short locationbottom;

    private Short costtime;

    private String carbright;

    private String carcolor;

    private String carlogo;

    private String imagepath;

    private String imageurl;

    private Date resulttime;

    private Date createtime;

    private Long frameIndex;

    private Double carspeed;

    private String labelinfodata;

    private String vehiclekind;

    private String vehiclebrand;

    private String vehicleseries;

    private String vehiclestyle;

    private Byte tag;

    private Byte paper;

    private Byte sun;

    private Byte drop;

    private Byte call;

    private Byte crash;

    private Byte danger;

    private Byte mainbelt;

    private Byte secondbelt;

    private Short vehicleleft;

    private Short vehicletop;

    private Short vehicleright;

    private Short vehiclebootom;

    private Short vehicleconfidence;

    private Byte face;

    private String faceUrl1;

    private String faceUrl2;

    private String vehicleurl;
    
    private String createtimeStr;
    
    private String serialnum;
    
}