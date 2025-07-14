package com.keensense.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.task.constants.DeleteTaskConstants;
import com.keensense.task.entity.TaskCleanRetryLog;
import com.keensense.task.mapper.TaskCleanRetryLogMapper;
import com.keensense.task.service.ITaskCleanRetryLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: TaskCleanRetryLogServiceImpl
 * @Description: TaskCleanRetryLog服务实现类
 * @Author: cuiss
 * @CreateDate: 2020/2/20 18:10
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Service
public class TaskCleanRetryLogServiceImpl extends ServiceImpl<TaskCleanRetryLogMapper,TaskCleanRetryLog> implements ITaskCleanRetryLogService {

    /**
     *
     * @param threadIndex
     * @param threadCount
     * @return
     */
    @Override
    public List<TaskCleanRetryLog> queryTaskCleanRetryLogList(int threadIndex, int threadCount) {
        List<TaskCleanRetryLog> taskCleanRetryLogs = baseMapper.selectList(new QueryWrapper<TaskCleanRetryLog>()
                .eq("status", DeleteTaskConstants.CLEAN_STATUS_WAIT)
                .last("and id%"+threadCount+"="+threadIndex));
        return taskCleanRetryLogs;
    }
}
