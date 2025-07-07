package com.keensense.search.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.loocme.sys.annotation.database.Column;
import com.loocme.sys.annotation.database.Id;
import com.loocme.sys.annotation.database.Table;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Table(TableName = "slavecapacity_result")
@Data
@ToString(callSuper=true, includeFieldNames=true)
public class SlaveCapacityResult {
    @Id
    @JSONField(name = "ServerIp")
    @Column(ColumnName = "serverip")
    private String serverIp;//服务器ip

    @JSONField(name = "TotalSpace")
    @Column(ColumnName = "totalspace")
    private Long totalSpace;//总空间

//    @JSONField(name = "FreeSpace")
//    @Column(ColumnName = "freespace")
//    private Long freeSpace;

    @JSONField(name = "UsableSpace")
    @Column(ColumnName = "usablespace")
    private Long usableSpace;//可用空间

    @JSONField(name = "UsedSpace")
    @Column(ColumnName = "usedspace")
    private Long usedSpace;//已用空间=总空间-可用空间

    @JSONField(name = "CreateTime" ,format = "yyyyMMddHHmmss")
    @Column(ColumnName = "createtime")
    private Date createTime;//时间
}
