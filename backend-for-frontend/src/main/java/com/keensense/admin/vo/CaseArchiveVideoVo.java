package com.keensense.admin.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 案件归档视频数据实体类
 */
@Data
public class CaseArchiveVideoVo implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件类型 实时 1 ，离线 2
     */
    private Integer fileType;

    /**
     * 文件远程路径
     */
    private String fileRemotePath;

    /**
     * 文件本地路径
     */
    private String fileLocalPath;

    /**
     * 发生时间段：0 事前 1 事中 2 事后
     */
    private Integer happenPeriod;

    /**
     * 是否证据视频 （0 否 1 是）
     */
    private int isProof;

    /**
     * 视频开始时间
     */
    private Date videoStartTime;

    /**
     * 视频结束时间
     */
    private Date videoEndTime;

    /**
     * 视频关联图片 地址[视频封面]
     */
    private String relatePictureUrl;

    /**
     * 关联案件编号
     */
    private String caseCode;

    /**
     * 分析序列号
     */
    private String serialnumber;

    /**
     * 关联监控点编号
     */
    private Long cameraId;

    /**
     * 删除标识  1:已删除  0:正常
     */
    private int deleted;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人编号
     */
    private String createUser;

    /**
     * 更新时间
     */
    private Date lastUpdateTime;

    /**
     * 视频下载的Id
     */
    private String videoDownloadId;


    /**
     * ======临时字段===========
     **/

    /**
     * 监控点名称
     */
    private String cameraName;
    /**
     * 创建时间 字符串
     */
    private String createTimeStr;

    /**
     * 视频开始时间 字符串
     */
    private String videoStartTimeStr;

    /**
     * 转移的状态，1 等待处理 2 已完成  3处理失败
     */
    private Integer transferStatus;

    /**
     * 进度
     * 视频下载成功 ： 10 ~ 43%
     * 视频转码成功:   43 ~ 76%
     * 视频转移成功：  100%
     */
    private Integer progress;

}