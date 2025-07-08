package com.keensense.admin.request;

import lombok.Data;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 9:47 2019/7/4
 * @Version v0.1
 */
@Data
public class RealVideoRequest extends PageRequest {
    String fileName;
    String startTime;
    String endTime;
    String status;
}
