package com.keensense.search.domain;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by zhanx xiaohui on 2019-08-20.
 */
@Table(name = "image_result")
@Data
@ToString(callSuper = true, includeFieldNames = true)
public class ImageResult {
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
