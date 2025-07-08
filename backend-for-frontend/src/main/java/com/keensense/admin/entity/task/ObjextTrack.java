package com.keensense.admin.entity.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 20:11:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("objext_track")
public class ObjextTrack extends Model<ObjextTrack> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 关联任务id
     */
    private String serialnumber;
    /**
     * 监控点id
     */
    private Long cameraid;
    /**
     * 目标出现时间
     */
    private Date tracktime;
    /**
     * 目标类型,-1:未知,1:人,2:汽车,4:人骑车
     */
    private Integer objtype;

    /**
     * 查询标识
     */
    private Integer searchStatus;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 结果id
     */
    private String resultid;
    /**
     * 扩展字段1
     */
    private String info1;
    /**
     * 扩展字段2
     */
    private String info2;
    /**
     * 扩展字段3
     */
    private String info3;

    /**
     * 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
