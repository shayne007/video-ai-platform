package com.keensense.admin.request;

import lombok.Data;

/**
 * @Author: shitao
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 10:55 2019/10/22
 * @Version v0.1
 */
@Data
public class JDThreeSenceRequest {

    String uuid;
    String url;
    String deviceId;
    String appearTime;
    String disappearTime;
    String score;
}
