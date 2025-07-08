package com.keensense.admin.vo;

import lombok.Data;

/**
 * 用于封装 上传数据的信息
 *
 * @author:dufy
 * @version:1.0.0
 * @date 2018/12/19
 */
@Data
public class UploadDataToJdVo {

    /** ====公共字段===**/
    /**
     * 用户账号
     */
    private String remoteUser;
    /**
     * 案件编号
     */
    private String caseId;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 存储路径（http 全路径）
     */
    private String cclj;
    /**
     * 文件来源 （0 未知 1 公安来源 2 社会来源）
     */
    private String fileSource;
    /**
     * 发生时间 （0 事前 1 事中 2 事后）
     */
    private String label;

    /** ====图片上传===**/

    /**
     * 文件类型（1 全景图）
     */
    private int fileType;


    /** ====视频上传===**/

    /**
     * 视频封面路径（http 全路径） 必填 否
     */
    private String sltcclj;
    /**
     * 视频后缀名
     */
    private String fileExt;
    /**
     * 是否证据视频（0 否 1 是）
     */
    private int isEvidence;
    /**
     * 视频描述 必填 否
     */
    private String videoDesc;

    /** ====视频点位信息上传===**/

    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备编号
     */
    private String deviceCode;
    /**
     * 安装地址 必填 否
     */
    private String deviceAddr;
    /**
     * 经度
     */
    private float longitude;
    /**
     * 纬度
     */
    private float latitude;
    /**
     *1 东 2 南 3 西 4 北 5 东南 6 东北 7 西南 8 西北
     * 必填 否
     */
    private int monitorDirection;
}
