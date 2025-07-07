package com.keensense.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.task.constants.DeleteTaskConstants;
import com.keensense.task.entity.TaskCleanInterim;
import com.keensense.task.mapper.TaskCleanInterimMapper;
import com.keensense.task.service.ITaskCleanInterimService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 清理机制临时类
 * @Author: wujw
 * @CreateDate: 2019/9/28 10:26
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
@Service
public class TaskCleanInterimServiceImpl extends ServiceImpl<TaskCleanInterimMapper, TaskCleanInterim> implements ITaskCleanInterimService {

    @Override
    public boolean insertBatch(List<TaskCleanInterim> list) {
        return this.saveBatch(list);
    }

    @Override
    public boolean insert(TaskCleanInterim taskCleanInterim) {
        return this.save(taskCleanInterim);
    }

    @Override
    public void resetRunningStatus() {
        baseMapper.resetRunningStatus(DeleteTaskConstants.CLEAN_STATUS_RUNNING, DeleteTaskConstants.CLEAN_STATUS_WAIT);
    }

    @Override
    public List<TaskCleanInterim> selectListBySlave(String slaveId) {
        QueryWrapper<TaskCleanInterim> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", DeleteTaskConstants.CLEAN_STATUS_WAIT);
        if(slaveId != null){
            queryWrapper.eq("slave_id", slaveId);
        }
        queryWrapper.orderByAsc("retry_count");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public int selectListForExce() {
        QueryWrapper<TaskCleanInterim> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("status", DeleteTaskConstants.CLEAN_STATUS_SUCCESS);
        queryWrapper.le("retry_count",DeleteTaskConstants.RETRY_COUNT_LIMIT);
        return baseMapper.selectCount(queryWrapper);
    }

    @Override
    public boolean updateStatus(String serialnumber, boolean status, String ymd) {
        log.info("callback delete request serialnumber = " + serialnumber + ",status = " + status + ",ymd ="+ymd);
        if(status){
            return baseMapper.updateStatus(serialnumber, DeleteTaskConstants.CLEAN_STATUS_SUCCESS, ymd) > 0;
        } else {
            return baseMapper.updateStatusFailed(serialnumber, DeleteTaskConstants.CLEAN_STATUS_WAIT, ymd) > 0;
        }
    }

}
