package com.keensense.picturestream.entity;

import com.loocme.sys.datastruct.Var;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class PictureInfo implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;

    public static final int STATUS_INIT = 0;
    public static final int STATUS_DOWNLOADING = 1;
    public static final int STATUS_DOWNLOAD_SUCC = 2;
    public static final int STATUS_DOWNLOAD_FAIL = 3;
    public static final int STATUS_TRANSCODE_ING = 4;
    public static final int STATUS_TRANSCODE_SUCC = 5;
    public static final int STATUS_TRANSCODE_FAIL = 6;
    public static final int STATUS_RECOG_ING = 7;
    public static final int STATUS_RECOG_SUCC = 8;
    public static final int STATUS_RECOG_FAIL = 9;
    public static final int STATUS_RESULTPUSH_ING = 10;
    public static final int STATUS_RESULTPUSH_SUCC = 11;
    public static final int STATUS_RESULTPUSH_FAIL = 12;
    public static final int STATUS_UNKOWN_URL = 91;
    public static final int STATUS_ERROR = 99;

    public static final Integer RECOG_TYPE_UNDEFINED = 0;
    public static final Integer RECOG_TYPE_OBJEXT_WITHOUT_VLPR = 1;
    public static final Integer RECOG_TYPE_OBJEXT = 2;
    public static final Integer RECOG_TYPE_THIRD_VLPR = 4;
    public static final Integer RECOG_TYPE_FACE = 8;


    public static final int QUEUE_OBJEXT = 1;
    public static final int QUEUE_THIRD_VLPR = 2;
    public static final int QUEUE_THIRD_FACE = 3;

    public static final int OBJEXT_PERSON = 1;
    public static final int OBJEXT_VLPR = 2;
    public static final int OBJEXT_FACE = 3;
    public static final int OBJEXT_BIKE = 4;

    public static final int IMAGE_SEGMENTATION = 1;
    public static final int IMAGE_BACKGROUD = 0;

    private String id;
    private String deviceId;
    private String picUrl;
    private String serialNumber;
    //视图库传送的json--1400数据
    private String ext;
    //图片类型
    //抠图1 最大的一张W*h
    //背景图0 全部分析
    private Integer pictureType;
    //是否截取图片,
    //true 根据坐标点位，截取图片，url转换base64,推送base64
    //false 直接推送url
    private Boolean interceptFlag;
    //是否为视图库选用的
    private Boolean keensenseFlag;
    private Long leaveTime;
    private Long captureTime;
    private Long enterTime;
    
    /*
    *  当recogTypeList的size=1时,直接推送
    *  当recogTypeList的size>1时:
    *       如果２和４同时出现，用４中车辆数据替换２中的车辆数据后推送
    *       如果２和８同时出现，用８中人脸数据替换２中的人脸数据后推送
    *       如果２和１同时出现，丢弃车辆数据后推送
    *       如果２、４、８同时出现，取２中人形４中车辆８中人脸数据后推送
    */
    private List<Integer> recogTypeList = new ArrayList<>();
    private Date downloadTime;
    private Date finishTime;
    private int downloadCost = 0;
    private int status = STATUS_INIT;
    private String picBase64;
    private int retryDownloadCount = 0;
    private int retryRecogCount = 0;
    private List<Var> results = new ArrayList<>();

    private String extendId = "";
    private String interestRegion = "";
    private String returnUrl = "";

    public void addResult(Var result){
        if(!result.isNull()){
            results.add(result);
        }
    }

    public void addRecogTypeList(String types){
        recogTypeList.clear();
        for(String type:types.split(",")){
            recogTypeList.add(Integer.parseInt(type));
        }
        if(recogTypeList.isEmpty()){
            recogTypeList.add(RECOG_TYPE_OBJEXT);
        }
    }
}
