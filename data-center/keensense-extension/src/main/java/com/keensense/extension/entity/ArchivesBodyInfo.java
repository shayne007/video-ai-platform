package com.keensense.extension.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @description:一人一档人形表
 * @author jingege
 * @CreateDate: 2019.05.09
 * @return:
 */
@Data
@Accessors(chain = true)
@TableName("archives_body_info")
public class ArchivesBodyInfo {

    private String id;

    private String bodyImgUrl;

    private String angle;

    private Date createTime;

    private String bodyFeatureId;

    private String archivesId;

    public ArchivesBodyInfo(){}

    public ArchivesBodyInfo(String id, String bodyImgUrl, String angle,
    		String bodyFeatureId, String archiveId) {
    	this.id = id;
    	this.bodyImgUrl = bodyImgUrl;
    	this.angle = angle;
    	this.bodyFeatureId = bodyFeatureId;
    	this.archivesId = archiveId;
    	this.createTime = new Date();
    }

}
