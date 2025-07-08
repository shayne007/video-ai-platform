package com.keensense.job.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * 组织机构表
 * </p>
 *
 * @author cuiss
 * @since 2018-11-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ctrl_unit")
public class CtrlUnit extends Model<CtrlUnit> {

    private static final long serialVersionUID = 1L;

    /**
     * 组织机构id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Setter
    @Getter
    private Long id;
    /**
     * 状态 枚举字段说明 [0:禁用];[1:启用]
     */
    @TableField("unit_state")
    @Setter
    @Getter
    private Long unitState;
    /**
     * 行政区划编码
     */
    @TableField("unit_identity")
    @Setter
    @Getter
    private String unitIdentity;
    /**
     * 组织类型
     */
    @TableField("org_type")
    @Setter
    @Getter
    private String orgType;
    /**
     * 数据维护组织
     */
    @TableField("share_unit_id")
    @Setter
    @Getter
    private Long shareUnitId;
    /**
     * 长编码
     */
    @TableField("long_number")
    @Setter
    @Getter
    private String longNumber;
    /**
     * 层级
     */
    @TableField("unit_level")
    @Setter
    @Getter
    private Long unitLevel;
    /**
     * 状态 0 禁用1启用
     */
    @TableField("display_name")
    @Setter
    @Getter
    private String displayName;
    /**
     * 是否叶子节点 0否 1是
     */
    @TableField("is_leaf")
    @Setter
    @Getter
    private Long isLeaf;
    /**
     * 序号
     */
    @TableField("seq_num")
    @Setter
    @Getter
    private Long seqNum;
    /**
     * 名称
     */
    @TableField("unit_name")
    @Setter
    @Getter
    private String unitName;
    /**
     * 备注
     */
    @TableField("unit_number")
    @Setter
    @Getter
    private String unitNumber;
    /**
     * 备注
     */
    @TableField("unit_description")
    @Setter
    @Getter
    private String unitDescription;
    /**
     * 端口2
     */
    @TableField("creator_id")
    @Setter
    @Getter
    private Long creatorId;
    /**
     * 创建时间
     */
    @TableField("create_time")
    @Setter
    @Getter
    private Date createTime;
    /**
     * 最新更新用户id
     */
    @TableField("last_update_user_id")
    @Setter
    @Getter
    private Long lastUpdateUserId;
    /**
     * 最新更新时间
     */
    @TableField("last_updated_time")
    @Setter
    @Getter
    private Date lastUpdatedTime;
    /**
     * 行政区域id
     */
    @TableField("ctrl_unit_id")
    @Setter
    @Getter
    private String ctrlUnitId;
    /**
     * 父行政区域编码，如果是顶级区域则为空
     */
    @TableField("unit_parent_id")
    @Setter
    @Getter
    private String unitParentId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    /**
     * unitName,unitNumber,unitParentId
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null || getClass() != obj.getClass()) {return false;}
        CtrlUnit ctrlUnit = (CtrlUnit) obj;
        return Objects.equals(unitName, ctrlUnit.unitName) &&
                Objects.equals(unitNumber, ctrlUnit.unitNumber) &&
                Objects.equals(unitParentId, ctrlUnit.unitParentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unitName, unitNumber, unitParentId);
    }

    @Override
    public String toString() {
        return "CtrlUnit{" +
        ", id=" + id +
        ", unitState=" + unitState +
        ", unitIdentity=" + unitIdentity +
        ", orgType=" + orgType +
        ", shareUnitId=" + shareUnitId +
        ", longNumber=" + longNumber +
        ", unitLevel=" + unitLevel +
        ", displayName=" + displayName +
        ", isLeaf=" + isLeaf +
        ", seqNum=" + seqNum +
        ", unitName=" + unitName +
        ", unitNumber=" + unitNumber +
        ", unitDescription=" + unitDescription +
        ", creatorId=" + creatorId +
        ", createTime=" + createTime +
        ", lastUpdateUserId=" + lastUpdateUserId +
        ", lastUpdatedTime=" + lastUpdatedTime +
        ", ctrlUnitId=" + ctrlUnitId +
        ", unitParentId=" + unitParentId +
        "}";
    }
}
