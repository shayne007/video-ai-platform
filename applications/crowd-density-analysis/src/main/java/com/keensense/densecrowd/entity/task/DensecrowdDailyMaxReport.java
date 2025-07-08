package com.keensense.densecrowd.entity.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 20:35 2020/7/27
 * @Version v0.1
 */
@Data
@TableName("densecrowd_daily_max_report")
public class DensecrowdDailyMaxReport {
    @TableId(type = IdType.AUTO)
    private int id;

    private String serialnumber;

    private Integer maxCount;

    private Long totalCount;

    private String reportDate;

    private Date insertTime;
}
