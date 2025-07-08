package com.keensense.admin.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "导入监控点参数")
public class ExportCameraExcelRequest implements Serializable {

    @ApiModelProperty(value = "文件url")
    private String fileJson;

    @ApiModelProperty(value = "文件名")
    private String fileName;
}