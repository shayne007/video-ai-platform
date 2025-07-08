package com.keensense.densecrowd.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

@Data
@ApiModel("监控点列表查询参数")
public class CameraListQueryRequest extends PageRequest implements Serializable {

    @ApiModelProperty("监控点名称")
    private String cameraName;

    @ApiModelProperty("开启状态 [0:未启动，1:已启动]")
    private String isvalid;

    @ApiModelProperty("状态 [0:离线，1:在线]")
    private String status;

    @ApiModelProperty("监控点类型: 1,联网实时视频 2,IPC直连 3,抓拍机")
    private String cameraType;
}