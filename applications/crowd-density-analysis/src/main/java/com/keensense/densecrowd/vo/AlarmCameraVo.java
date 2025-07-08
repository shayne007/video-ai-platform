package com.keensense.densecrowd.vo;

import lombok.Data;

/**
 * @Author: zengyc
 * @Description: 接口数据返回格式
 * @Date: Created in 10:49 2019/9/29
 * @Version v0.1
 */
@Data
public class AlarmCameraVo {
    /**
     * 设备ID
     */
    private String deviceId;//	int	1-20	是
    /**
     * 区域ID
     */
    private String areaId;//	string	1-32	是
    /**
     * 区域名称
     */
    private String areaName;//	string	1-64	是

    /**
     * 设备编号
     */
    private String deviceCode;//	string	1-32	是

    /**
     * 设备类型 4:像机
     */
    private int deviceType;//	int	1-2	是

    /**
     * 设备名称
     */
    private String deviceName;//	string	1-64	是
    /**
     * 状态 1:启用,0:停用
     */
    private int status;//	int	1-2	是
    /**
     * 备注
     */
    private String remark;//	string	1-225	是
    /**
     * Ip
     */
    private String ip;//	string	1-16	是
    /**
     * 端口
     */
    private String port;//	int	1-5	是
    /**
     * 用户名
     */
    private String accessUser;//	string	1-32	是
    /**
     * 密码
     */
    private String accessPwd;//	string	1-64	是
    /**
     * 安装地址
     */
    private String addr;//	string	1-128	是

    /**
     * 抓拍服务器id
     */
    private String captureServerId;//	string	1-32	是
    /**
     * 像机类型
     */
    private String cameraType;//	int	1-2	是
    /**
     * Rtsp后缀
     */
    private String cameraRtspSuffix;//	string	1-32	是
    /**
     * Rtsp流 rtsp:1
     */
    private String cameraRtsp;//	string	1-128	是
    /**
     * 像机服务类型 1:抓拍 2:报警 3:安检排队，4人群计数
     */
    private String cameraServerType;//	string	1-10	是
    /**
     * 更新时间
     */
    private String updatedTime;//	long	13	是
    /**
     * 更新人
     */
    private String updatedUserid;//	string	1-32	是
    /**
     * 创建时间
     */
    private String createdTime;//	long	13	是
    /**
     * 创建人
     */
    private String createdUserid;//	string	1-32	是

}
