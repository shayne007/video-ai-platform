package com.keensense.admin.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 案件管理图片归档实体类
 */
@Data
public class CaseArchivePicVo implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 图片名称
     */
    private String fileName;

    /**
     * 图片的ID
     */
    private String picId;

    /**
     * 图片类型objType(1:人,2:车辆,4:人骑车 )
     */
    private Integer objType;

    /**
     * 大图地址
     */
    private String picBigPath;

    /**
     * 案件编号
     */
    private String caseCode;

    /**
     * 小图地址
     */
    private String picThumbPath;

    /**
     * 发生时间段：0 事前 1 事中 2 事后
     */
    private Integer happenPeriod;

    /**
     * 分析序列号
     */
    private String serialnumber;

    /**
     * 监控点Id
     */
    private Long cameraId;

    /**
     * 删除标记 ，1:已删除  0:正常
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
     * 创建人
     */
    private String createUser;

    /**
     * 更新时间
     */
    private Date lastUpdateTime;

    /**
     * 图片的标签数据
     */
    private String picInfo;



    /**=======临时字段=========**/

    /**
     * 时间字符串
     */
    private String createTimeStr;

    /**
     * 开始时间
     */
    private String startTimeStr;

    /**
     * 结束时间
     */
    private String endTimeStr;

    /**
     * 监控点名称
     */
    private String cameraName;

    private String latitude;

    private String longitude;

    private ResultQueryVo resultQueryVo;

}