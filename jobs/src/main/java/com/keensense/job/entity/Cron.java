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

/**
 * @Author cuiss
 * @Description //tb_cron的映射类
 * @Date 2018/11/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_cron")
public class Cron extends Model<Cron> {

    private static final long serialVersionUID = 1L;

    /**
     * 点位id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Setter
    @Getter
    private Long id;
    /**
     * cron表达式
     */
    @Setter
    @Getter
    private String cron;
    /**
     * 是否启用  0：停用  1：启用
     */
    @Setter
    @Getter
    private Integer enable;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @Setter
    @Getter
    private Date createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
