package com.keensense.densecrowd.entity.task;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.*;
import java.util.*;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 20:11:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ctrl_unit_file")
public class CtrlUnitFile extends Model<CtrlUnitFile> {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 文件类型2视频，1图片,3表示文件夹
     */
    private String fileType;
    /**
     * 文件后缀
     */
    private String fileSuffix;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 上传后的文件名
     */
    private String fileNameafterupload;
    /**
     * 上传后文件的ftp路径
     */
    private String filePathafterupload;
    /**
     * 上传的本地路径
     */
    private String fileLocalPath;
    /**
     * 行政区域id
     */
    private String ctrlUnitId;
    /**
     * 点位id
     */
    private Long cameraId;
    /**
     * 文件描述
     */
    private String fileDescription;

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        if (thumbNail != null && thumbNail.length() < 40) {
            thumbNail = null;
        }
        this.thumbNail = thumbNail;
    }

    /**
     * 缩略图
     */
    private String thumbNail;
    /**
     * 文件大小
     */
    private String fileSize;
    /**
     * 创建用户id
     */
    private Long createUserId;
    /**
     * 视频录入时间
     */
//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date entryTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 系列Id
     */
    private String serialnumber;
    /**
     * 转码文件ID
     */
    private String transcodingId;
    /**
     * 转码进度
     */
    private String transcodingProgress;

    /**
     * 转码进度描述
     */
    @TableField(exist = false)
    private String transcodingProgressStr;

    public String getTranscodingProgressStr() {

        return transcodingProgress + "%";
    }

    /**
     * 视频帧数
     */
    private Integer frameCount;
    /**
     * 每秒帧数
     */
    private Integer framerate;
    /**
     * 分辨率
     */
    private String resolution;
    /**
     * 转码状态
     */
    private Integer transcodeStatus;
    /**
     *
     */
    private String tasticsentityid;
    /**
     * ftp文件路径
     */
    private String fileFtpPath;
    /**
     *
     */
    private String slaveIp;

    /**
     * 是否自动分析,0:否,1:是
     */
    private Integer autoAnalysisFlag;
    /**
     *
     */
    private String interestParam;
    /**
     *
     */
    private String uninterestParam;

    /**
     * 删除标志
     */
    private Integer delFlag;

    /**
     * 跨线参数(数组，单个之间用逗号隔开)
     * [0] : 方向类别
     * [1] : 起点X 坐标
     * [2] : 起点Y 坐标
     * [3] : 终点X 坐标
     * [4] : 终点Y 坐标
     */
    private String tripwires;

    @TableField
    private String videoType;

    @TableField(exist = false)
    private String cameraName;

    @TableField(exist = false)
    private String seriNum;

    @TableField(exist = false)
    private Integer analysisProgress;

    @TableField(exist = false)
    private Integer taskStatus;

    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
