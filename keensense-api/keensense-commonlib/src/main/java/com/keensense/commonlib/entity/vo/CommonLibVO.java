package com.keensense.commonlib.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author ycl
 * @date 2019/9/10
 */
@Data
public class CommonLibVO {

    private String id;

    private String name;

    private String remark;

    private Boolean isAllowDel;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    private Integer type;

}
