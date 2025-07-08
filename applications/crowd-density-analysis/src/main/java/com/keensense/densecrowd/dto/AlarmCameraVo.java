package com.keensense.densecrowd.dto;

import lombok.Data;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 17:24 2019/10/17
 * @Version v0.1
 */
@Data
public class AlarmCameraVo {

    String deviceId;//	int	1-20	是	设备ID
    String areaId;//	string	1-32	是	区域ID
    String areaName;//	string	1-64	是	区域名称
    String deviceCode;//	string	1-32	是	设备编号
    Integer deviceType;//	int	1-2	是	设备类型 4:像机
    String deviceName;//	string	1-64	是	设备名称
    Integer status;//	int	1-2	是	状态 1:启用,0:停用
    String remark;//	string	1-225	是	备注
    String ip;//	string	1-16	是	Ip
    Integer port;//	int	1-5	是	端口
    String accessUser;//	string	1-32	是	用户名
    String accessPwd;//	string	1-64	是	密码
    String addr;//	string	1-128	是	安装地址
    String captureServerId;//	string	1-32	是	抓拍服务器id
    Integer cameraType;//	int	1-2	是	像机类型
    String cameraRtspSuffix;//	string	1-32	是	Rtsp后缀
    String cameraRtsp;//	string	1-128	是	Rtsp流 rtsp:1
    String cameraServerType;//	string	1-10	是	像机服务类型 1:抓拍 2:报警 3:安检排队，4人群计数
    Long updatedTime;//	long	13	是	更新时间
    Long updatedUserid;//	string	1-32	是	更新人
    Long createdTime;//	long	13	是	创建时间
    Long createdUserid;//	string	1-32	是	创建人

}
