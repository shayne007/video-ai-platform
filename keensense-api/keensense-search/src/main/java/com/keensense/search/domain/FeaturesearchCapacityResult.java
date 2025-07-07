package com.keensense.search.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.loocme.sys.annotation.database.Column;
import com.loocme.sys.annotation.database.Id;
import com.loocme.sys.annotation.database.Table;
import lombok.Data;
import lombok.ToString;

/**
 * 搜图模块容量统计
 */
@Data
@Table(TableName = "featuresearch_capacity_result")
@ToString(callSuper=true, includeFieldNames=true)
public class FeaturesearchCapacityResult {
    @Id
    @JSONField(name= "ipaddr")
    @Column(ColumnName = "ipaddr")
    protected String ipaddr;

    @JSONField(name= "usage")
    @Column(ColumnName = "usage")
    protected Long usage;

    @JSONField(name= "total")
    @Column(ColumnName = "total")
    protected Long total;

    @JSONField(name= "date")
    @Column(ColumnName = "date")
    protected String date;
}

/**
 * @program: platform
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-08-30 16:02
 **/