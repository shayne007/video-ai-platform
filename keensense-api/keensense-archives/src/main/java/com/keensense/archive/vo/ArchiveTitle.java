package com.keensense.archive.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.loocme.sys.annotation.database.Column;
import com.loocme.sys.annotation.database.Table;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * Created by memory_fu on 2019/12/26.
 */
@Data
@Table(TableName = "archive_title")
public class ArchiveTitle {
    
    public ArchiveTitle(List<String> combineCluster) {
    
    }
    
    public ArchiveTitle(String clusterIndex, String url, long createTime,
        List<String> combineCluster,String clusterFlag) {
        this.clusterIndex = clusterIndex;
        this.url = url;
        this.createTime = createTime;
        this.clusterFlag = clusterFlag;
        this.combineCluster = combineCluster;
    }
    
    @Column(ColumnName = "clusterIndex")
    @JSONField(name= "clusterIndex")
    private String clusterIndex;
    
    @Column(ColumnName = "url")
    @JSONField(name= "url")
    private String url;
    
    @Column(ColumnName = "createTime")
    @JSONField(name= "createTime")
    private long createTime;
    
    @Column(ColumnName = "clusterFlag")
    @JSONField(name= "clusterFlag")
    private String clusterFlag;
    
    @JSONField(name= "combineCluster")
    private List<String> combineCluster;
    
}
