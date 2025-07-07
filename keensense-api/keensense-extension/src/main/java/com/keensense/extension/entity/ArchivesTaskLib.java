package com.keensense.extension.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 任务时间库对应
 * </p>
 *
 * @author ycl
 * @since 2019-06-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ArchivesTaskLib implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 1: face++, 2: GLST
     */
    private Integer libType;

    /**
     * face++此参数不为空，glst暂为空
     */
    private String taskId;

    /**
     * 算法库id
     */
    private String libId;

    /**
     * glst库 能够录入时间开始时间
     */
    private LocalDateTime intervalBeginTime;

    /**
     * glst库 能够录入时间结束时间
     */
    private LocalDateTime intervalEndTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
