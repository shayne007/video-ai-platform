package com.keensense.extension.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.keensense.extension.util.IDUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @description:关联表
 * @author jingege
 * @CreateDate: 2019.05.09
 * @return:
 */
@Data
@Accessors(chain = true)
@TableName("archives_relation_info")
public class ArchivesRelationInfo {

    //即faceId
    private String id;
    private String bodyId;
    private String archivesId;


    public ArchivesRelationInfo(){}

    public ArchivesRelationInfo(String faceId, String bodyId,String archivesId) {
    	this.id = faceId;
    	this.bodyId = bodyId;
    	this.archivesId = archivesId;
    }

}
