package com.keensense.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.task.constants.DeleteTaskConstants;
import com.keensense.task.entity.TaskCleanLog;
import com.keensense.task.mapper.TaskCleanLogMapper;
import com.keensense.task.service.ITaskCleanLogService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: TaskCleanLog Service实现类
 * @Author: wujw
 * @CreateDate: 2019/6/25 16:34
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Service
public class TaskCleanLogServiceImpl extends ServiceImpl<TaskCleanLogMapper, TaskCleanLog> implements ITaskCleanLogService {

    @Override
    public int insert(TaskCleanLog taskCleanLog) {
        return baseMapper.insert(taskCleanLog);
    }

    @Override
    public boolean insertBatch(List<TaskCleanLog> list) {
        return saveBatch(list);
    }

    @Override
    public List<TaskCleanLog> getListForDelete(int status) {
        return baseMapper.selectList(new QueryWrapper<TaskCleanLog>()
                .eq("status", status).ne("retry_count",5).orderByAsc("retry_count").last("limit 50"));
    }

    @Override
    public void updateBatch(List<TaskCleanLog> list) {
        this.updateBatchById(list);
    }

}
