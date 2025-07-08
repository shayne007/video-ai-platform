package com.keensense.densecrowd.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "组织机构参数")
public class CtrlUnitRequest implements Serializable {

    @ApiModelProperty(value = "组织机构id")
    private String id;

    @ApiModelProperty(value = "状态 [0:禁用];[1:启用]")
    private Long unitState;

    @ApiModelProperty(value = "行政区划编码")
    private String unitIdentity;

    @ApiModelProperty(value = "组织类型")
    private String orgType;

    @ApiModelProperty(value = "数据维护组织")
    private Long shareUnitId;

    @ApiModelProperty(value = "长编码")
    private String longNumber;

    @ApiModelProperty(value = "层级")
    private Long unitLevel;

    @ApiModelProperty(value = "名称")
    private String displayName;

    @ApiModelProperty(value = "是否叶子节点 0否 1是")
    private Long isLeaf;

    @ApiModelProperty(value = "序号")
    private Long seqNum;

    @ApiModelProperty(value = "名称")
    private String unitName;

    @ApiModelProperty(value = "行政区划编码")
    private String unitNumber;

    @ApiModelProperty(value = "备注")
    private String unitDescription;

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

    @ApiModelProperty(value = "父行政区域编码，如果是顶级区域则为空")
    private String unitParentId;

    @ApiModelProperty(value = "父节点Id")
    private String parentId;
    
}