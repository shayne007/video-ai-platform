package com.keensense.commonlib.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author ycl
 * @date 2019/9/10
 */
@Data
@Accessors(chain = true)
@TableName("lib_common_feature_record")
public class CommonFeatureRecord {

    private String id;

    private String libraryId;

    private String imgUrl;

    private Integer featureType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    private Boolean deleted;

}
