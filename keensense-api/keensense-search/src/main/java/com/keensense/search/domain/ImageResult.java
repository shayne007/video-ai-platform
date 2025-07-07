package com.keensense.search.domain;

import com.loocme.sys.annotation.database.Column;
import com.loocme.sys.annotation.database.Id;
import com.loocme.sys.annotation.database.Table;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * Created by zhanx xiaohui on 2019-08-20.
 */
@Table(TableName = "image_result")
@Data
@ToString(callSuper = true, includeFieldNames = true)
public class ImageResult {
    @Id
    @Column(ColumnName = "id")
    private String id;

    @Id
    @Column(ColumnName = "type")
    private String type;

    @Column(ColumnName = "analysisid")
    private String analysisId;

    @Column(ColumnName = "group")
    private String group;

    @Column(ColumnName = "url")
    private String url;

    @Column(ColumnName = "datetime")
    private Date datetime;
}
