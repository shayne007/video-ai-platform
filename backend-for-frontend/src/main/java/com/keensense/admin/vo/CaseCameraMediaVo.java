package com.keensense.admin.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CaseCameraMediaVo implements Serializable {
    private String id;

    private String fileType;

    private String fileSuffix;

    private String fileName;

    private String fileNameafterupload;

    private String filePathafterupload;

    /**
     * 上传后的大图文件名
     */
    private String fileBigImage;

    private Long videobitId;

    private String targetAppeartime;

    private String fileDescription;

    private String createTime;

    private String createUserid;

    private String thumbNail;

    private String fileSize;

    private String useruploadvideoid;

    private String useruploadPath;

    private String useruploadStatus;

    private String c1;

    private String c2;

    private String c3;

    private String c4;

    private String c5;

    private String clueType;

    private String c6;

    private String c7;

    private String c8;

    private String verifyingStatus;

    private String syncStatus;

    private String lawcaseid;
    
    private String latitude;
    
    private String longitude;
    
    private String persistPicture;

    /**
     * 图片的标签数据
     */
    private String picInfo;

    private ResultQueryVo resultQueryVo;

    //0未删除， 1删除
    private int deleted = 0;

    private String pictureBase64;
    
}