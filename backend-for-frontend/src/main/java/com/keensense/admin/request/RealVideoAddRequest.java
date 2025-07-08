package com.keensense.admin.request;

import lombok.Data;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 15:13 2019/7/1
 * @Version v0.1
 */
@Data
public class RealVideoAddRequest {
    String cameraIds;
    String names;
    String startTime;
    String endTime;
    Long cameraId;
    Integer interestFlag;
    String interestParam;
}
