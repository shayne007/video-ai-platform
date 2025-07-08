package com.keensense.search.domain;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 搜图模块容量统计
 */
@Data
@Table(name = "featuresearch_capacity_result")
@ToString(callSuper=true, includeFieldNames=true)
public class FeaturesearchCapacityResult {
    @Id
    @JSONField(name= "ipaddr")
    @Column(name = "ipaddr")
    protected String ipaddr;

    @JSONField(name= "usage")
    @Column(name = "usage")
    protected Long usage;

    @JSONField(name= "total")
    @Column(name = "total")
    protected Long total;

    @JSONField(name= "date")
    @Column(name = "date")
    protected String date;
}

/**
 * @program: platform
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-08-30 16:02
 **/