package com.keensense.densecrowd.dto;

import lombok.Data;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 17:49 2019/10/17
 * @Version v0.1
 */
@Data
public class AlarmHeadVo {
    String deviceId;//	string	1-20	是	设备编号
    String deviceName;//	string	1-20	是	设备名称
    Long time;//	long	13	是	时刻
    Integer headcount;//	int	1-6	是	人数
    String densityInfo;
}
