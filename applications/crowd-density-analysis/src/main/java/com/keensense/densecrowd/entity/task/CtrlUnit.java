package com.keensense.densecrowd.entity.task;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.*;
import java.util.*;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 20:11:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ctrl_unit")
public class CtrlUnit extends Model<CtrlUnit> {
	private static final long serialVersionUID = 1L;

	/**
	 * 组织机构id
	 */
	@TableId
	private Long id;
	/**
	 * 状态 枚举字段说明 [0:禁用];[1:启用]
	 */
	private Long unitState;
	/**
	 * 行政区划编码
	 */
	private String unitIdentity;
	/**
	 * 组织类型
	 */
	private String orgType;
	/**
	 * 数据维护组织
	 */
	private Long shareUnitId;
	/**
	 * 长编码
	 */
	private String longNumber;
	/**
	 * 层级
	 */
	private Long unitLevel;
	/**
	 * 状态 0 禁用1启用
	 */
	private String displayName;
	/**
	 * 是否叶子节点 0否 1是
	 */
	private Long isLeaf;
	/**
	 * 序号
	 */
	private Long seqNum;
	/**
	 * 名称
	 */
	private String unitName;
	/**
	 * 备注
	 */
	private String unitNumber;
	/**
	 * 备注
	 */
	private String unitDescription;
	/**
	 * 端口2
	 */
	private Long creatorId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 最新更新用户id
	 */
	private Long lastUpdateUserId;
	/**
	 * 最新更新时间
	 */
	private Date lastUpdatedTime;
	/**
	 * 行政区域id
	 */
	private String ctrlUnitId;
	/**
	 * 父行政区域编码，如果是顶级区域则为空
	 */
	private String unitParentId;

	@TableField(exist = false)
	private CtrlUnit children;

  /**
   * 主键值
   */
  @Override
  protected Serializable pkVal() {
    return this.id;
  }
}
