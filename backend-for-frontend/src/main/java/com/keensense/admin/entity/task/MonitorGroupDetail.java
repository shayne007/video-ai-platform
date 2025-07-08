package com.keensense.admin.entity.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 专项监控组详情表
 *
 * @Author: dufy
 * @Date: Created in 10:31 2019/5/9
 * @Version v0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_monitor_group_detail")
public class MonitorGroupDetail extends Model<MonitorGroupDetail> {

    private static final long serialVersionUID = -7321547674781273990L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 监控组ID
     */
    private Long monitorGroupId;
    /**
     * 监控点ID
     */
    private Long cameraId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建人编号
     */
    private Long createUserId;

    /**-----临时字段------**/

    private String cameraName;

    private String regionName;

    private Integer isValid;

    private String serialnumber;
}
