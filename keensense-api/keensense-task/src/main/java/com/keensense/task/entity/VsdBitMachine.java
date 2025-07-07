package com.keensense.task.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Description: 盒子对象
 * @Author: wujw
 * @CreateDate: 2019/5/21 16:47
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Data
@TableName("vsd_bit_machine")
public class VsdBitMachine {

    private Long id;

    private String bitName;

    private Integer bitStatus;

    private String url;

    private Integer numberRoad;

    private Integer userRoad;

    private Integer executeStatus;

    private Integer retryCount;

    private Date lastFailTime;
}
