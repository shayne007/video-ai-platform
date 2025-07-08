package com.keensense.job.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author cuiss
 * @Description //Vas点位信息
 * @Date 2018/11/5
 */
public class Device {
    /**
     * 点位名称
     */
    @Getter
    @Setter
    private String name;

    /**
     * 点位编码
     */
    @Getter
    @Setter
    private Long codeId;

    /**
     * 父结点编码
     */
    @Getter
    @Setter
    private Long parentId;

    /**
     * 点位状态  1 在线 0 离线
     */
    @Getter
    @Setter
    private Integer status;

    /**
     * 经度
     */
    @Getter
    @Setter
    private String longitude;

    /**
     * 纬度
     */
    @Getter
    @Setter
    private String latitude;

    /**
     * 点位类型 1 枪机  2 球机
     */
    @Getter
    @Setter
    private Integer type;
}
