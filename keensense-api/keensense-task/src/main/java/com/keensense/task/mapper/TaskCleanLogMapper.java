package com.keensense.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keensense.task.entity.TaskCleanLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: 按照时间删除离线文件
 * @Author: wujw
 * @CreateDate: 2019/6/25 16:37
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Mapper
public interface TaskCleanLogMapper extends BaseMapper<TaskCleanLog> {
}
