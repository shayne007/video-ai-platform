package com.keensense.commonlib.entity.dto;

import lombok.Data;

/**
 * @author ycl
 * @date 2019/9/10
 */
@Data
public class CommonFeatureQueryDto {
    private String id;

    private String libraryId;

    private String imgUrl;

    private Integer featureType;

    private String startTime;
    private String endTime;

}
