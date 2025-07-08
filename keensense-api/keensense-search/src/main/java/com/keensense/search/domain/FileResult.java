package com.keensense.search.domain;


import java.util.Date;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by zhanx xiaohui on 2019-08-20.
 */
@Table(name = "file_result")
@Data
@ToString(callSuper=true, includeFieldNames=true)
public class FileResult {
    @Id
    @Column(name = "id")
    private String id;

    @Id
    @Column(name = "type")
    private String type;

    @Column(name = "analysisid")
    private String analysisId;

    @Column(name = "group")
    private String group;

    @Column(name = "url")
    private String url;

    @Column(name = "datetime")
    private Date datetime;
}