package com.keensense.densecrowd.request;

import com.keensense.densecrowd.entity.sys.SysDept;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "部门管理参数")
public class SysDeptRequest implements Serializable {

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "业务行政部门代码")
    private String longNumber;

    @ApiModelProperty(value = "名称")
    private String deptName;

    @ApiModelProperty(value = "部门编码")
    private String deptNumber;

    @ApiModelProperty(value = "父节点id")
    private Long parentId;

    @ApiModelProperty(value = "层级")
    private Long deptLevel;

    @ApiModelProperty(value = "显示名称")
    private String displayName;

    @ApiModelProperty(value = "是否叶子节点")
    private Long isLeaf;

    @ApiModelProperty(value = "状态 0禁用1启用")
    private Long deptState;

    @ApiModelProperty(value = "行政组织代码")
    private Long adminIdentity;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "联系电话")
    private Long tel;

    @ApiModelProperty(value = "是否业务行政部门 0否 1是")
    private Long isBizOrg;

    @ApiModelProperty(value = "行政部门id")
    private String bizCode;

    @ApiModelProperty(value = "序号")
    private Long seq;

    @ApiModelProperty(value = "备注")
    private Long description;

    @ApiModelProperty(value = "端口2")
    private Long creatorId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "最新更新用户id")
    private Long lastUpdateUserId;

    @ApiModelProperty(value = "最新更新时间")
    private Date lastUpdatedTime;

    @ApiModelProperty(value = "行政区域id")
    private String ctrlUnitId;

    private List<SysDept> children;


}