package com.keensense.search.domain;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "slavecapacity_result")
@Data
@ToString(callSuper=true, includeFieldNames=true)
public class SlaveCapacityResult {
    @Id
    @JSONField(name = "ServerIp")
    @Column(name = "serverip")
    private String serverIp;//服务器ip

    @JSONField(name = "TotalSpace")
    @Column(name = "totalspace")
    private Long totalSpace;//总空间

//    @JSONField(name = "FreeSpace")
//    @Column(name = "freespace")
//    private Long freeSpace;

    @JSONField(name = "UsableSpace")
    @Column(name = "usablespace")
    private Long usableSpace;//可用空间

    @JSONField(name = "UsedSpace")
    @Column(name = "usedspace")
    private Long usedSpace;//已用空间=总空间-可用空间

    @JSONField(name = "CreateTime" ,format = "yyyyMMddHHmmss")
    @Column(name = "createtime")
    private Date createTime;//时间
}
