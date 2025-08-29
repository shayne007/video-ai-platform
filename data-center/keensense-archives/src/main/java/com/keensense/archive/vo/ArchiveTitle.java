package com.keensense.archive.vo;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Date;
import java.util.List;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by memory_fu on 2019/12/26.
 */
@Data
@Table(name = "archive_title")
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
    
    @Column(name = "clusterIndex")
    @JSONField(name= "clusterIndex")
    private String clusterIndex;
    
    @Column(name = "url")
    @JSONField(name= "url")
    private String url;
    
    @Column(name = "createTime")
    @JSONField(name= "createTime")
    private long createTime;
    
    @Column(name = "clusterFlag")
    @JSONField(name= "clusterFlag")
    private String clusterFlag;
    
    @JSONField(name= "combineCluster")
    private List<String> combineCluster;
    
}
