package com.keensense.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keensense.task.entity.TaskCleanRetryLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: TaskCleanRetryLogMapper
 * @Description: 操作TaskCleanRetryLog基础类
 * @Author: cuiss
 * @CreateDate: 2020/2/13 12:05
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Mapper
public interface TaskCleanRetryLogMapper extends BaseMapper<TaskCleanRetryLog> {
}
