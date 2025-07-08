package com.keensense.job.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.name;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *   任务执行日志
 * </p>
 *
 * @author cuiss
 * @since 2018-11-11
 */
@TableName("tb_vas_task_log")
public class TbVasTaskLog extends Model<TbVasTaskLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Getter
    @Setter
    private Long id;
    /**
     * 状态(10:成功；-10：失败)
     */
    @Getter
    @Setter
    private Integer status;
    /**
     * 返回信息
     */
    @Getter
    @Setter
    private String msg;
    /**
     * vas数量
     */
    @TableField("vas_cnt")
    @Getter
    @Setter
    private Integer vasCnt;
    /**
     * 新增vas数量
     */
    @TableField("vas_insert_cnt")
    @Getter
    @Setter
    private Integer vasInsertCnt;
    /**
     * 更新vas数量
     */
    @TableField("vas_update_cnt")
    @Getter
    @Setter
    private Integer vasUpdateCnt;
    /**
     * 删除vas数量
     */
    @TableField("vas_delete_cnt")
    @Getter
    @Setter
    private Integer vasDeleteCnt;
    /**
     * org数量
     */
    @TableField("org_cnt")
    @Getter
    @Setter
    private Integer orgCnt;
    /**
     * 新增org数量
     */
    @TableField("org_insert_cnt")
    @Getter
    @Setter
    private Integer orgInsertCnt;
    /**
     * 更新org数量
     */
    @TableField("org_update_cnt")
    @Getter
    @Setter
    private Integer orgUpdateCnt;
    /**
     * 删除org数量
     */
    @TableField("org_delete_cnt")
    @Getter
    @Setter
    private Integer orgDeleteCnt;
    /**
     * 创建时间
     */
    @TableField("create_time")
    @Getter
    @Setter
    private Date createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TbVasTaskLog{" +
        ", id=" + id +
        ", status=" + status +
        ", msg=" + msg +
        ", vasCnt=" + vasCnt +
        ", vasInsertCnt=" + vasInsertCnt +
        ", vasUpdateCnt=" + vasUpdateCnt +
        ", vasDeleteCnt=" + vasDeleteCnt +
        ", orgCnt=" + orgCnt +
        ", orgInsertCnt=" + orgInsertCnt +
        ", orgUpdateCnt=" + orgUpdateCnt +
        ", orgDeleteCnt=" + orgDeleteCnt +
        ", createTime=" + createTime +
        "}";
    }
}
