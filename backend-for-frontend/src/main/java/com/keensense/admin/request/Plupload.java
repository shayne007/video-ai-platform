package com.keensense.admin.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Plupload实体类固定格式，属性名不可修改
 * 因为MultipartFile要用到Spring web的依赖，而该依赖在web模块中才引入，所以不把该实体类放在entity模块
 */
@Data
@ApiModel("上传离线视频参数")
public class Plupload {
    /**文件原名*/
    @ApiModelProperty("文件原名")
    private String name;
    /**用户上传资料被分解总块数*/
    @ApiModelProperty("用户上传文件被分隔的总块数,默认传1")
    private int chunks = 1;
    /**当前块数（从0开始计数）*/
    @ApiModelProperty("当前块，从0开始")
    private int chunk = 0;
}
