package com.keensense.admin.vo;

import lombok.Data;

/**
 * @Author: shitao
 * @Description: 封装返回告警数据
 * @Date: Created in 16:03 2019/10/14
 * @Version v0.1
 */
@Data
public class WarningVo {

    private String id;
    private Integer warningType;

    private String picUrl;

    private String cameraName;

    private String time;
}
