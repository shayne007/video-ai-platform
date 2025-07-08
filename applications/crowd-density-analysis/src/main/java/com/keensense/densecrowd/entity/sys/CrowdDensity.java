package com.keensense.densecrowd.entity.sys;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;

/**
 * @Author: shitao
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 15:31 2019/9/26
 * @Version v0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CrowdDensity extends Model<CrowdDensity> {

    /**
     * 记录标志
     */
    private String Id;

    /**
     * 总人数
     */
    private int count;

    /**
     * 任务编号
     */
    private String serialnumber;

    /**
     * 抓拍场景图片访问地址
     */
    private String picUrl;

    /**
     * 时间
     */
    private Date createTime;

    /**
     * 则该字段用于存储每个ROI的人群密度信息
     */
    private String densityInfo;

    /**
     * 人头位置信息
     */
    private String headPosition;

    /**
     * 监控点名称
     */
    private String cameraName;

}
