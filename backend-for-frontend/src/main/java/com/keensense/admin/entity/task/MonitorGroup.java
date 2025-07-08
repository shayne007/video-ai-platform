package com.keensense.admin.entity.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 专项监控组表
 *
 * @Author: dufy
 * @Date: Created in 10:30 2019/5/9
 * @Version v0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_monitor_group")
public class MonitorGroup extends Model<MonitorGroup> {

    private static final long serialVersionUID = -4764717081688316247L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 组名称
     */
    private String groupName;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date lastUpdateTime;
    /**
     * 创建人编号
     */
    private Long createUserId;
    /**
     * 备注
     */
    private String remark;

    /**-----临时字段----------**/
    /**
     * 启动的任务数目
     */
    @TableField(exist = false)
    private Integer startNum;

    /**
     * 监控组中监控点数目
     */
    @TableField(exist = false)
    private Integer totalNum;

}
