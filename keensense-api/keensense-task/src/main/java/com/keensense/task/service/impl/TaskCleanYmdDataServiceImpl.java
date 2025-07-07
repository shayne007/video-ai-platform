package com.keensense.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.task.constants.DeleteTaskConstants;
import com.keensense.task.entity.TaskCleanYmdData;
import com.keensense.task.entity.TbAnalysisTask;
import com.keensense.task.mapper.TaskCleanYmdDataMapper;
import com.keensense.task.service.ITaskCleanYmdDataService;
import com.keensense.task.util.DateUtil;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TaskCleanYmdData service 实现类
 * @Author: wujw
 * @CreateDate: 2019/6/25 16:34
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Service
public class TaskCleanYmdDataServiceImpl  extends
        ServiceImpl<TaskCleanYmdDataMapper, TaskCleanYmdData> implements ITaskCleanYmdDataService {

    @Override
    public boolean insertBatch(List<TaskCleanYmdData> list) {
        return saveBatch(list);
    }

    @Override
    public List<TaskCleanYmdData> getListForDelete() {
        return baseMapper.selectList(new QueryWrapper<TaskCleanYmdData>()
                .eq("status", DeleteTaskConstants.CLEAN_STATUS_WAIT).orderByAsc("retry_count"));
    }

    @Override
    public List<TaskCleanYmdData> getTaskCleanYmdDataList(List<TbAnalysisTask> list, String yyyyMMdd) {
        List<TaskCleanYmdData> insertList = new ArrayList<>(list.size());
        Timestamp now = DateUtil.now();
        List<TaskCleanYmdData> taskCleanYmdDataList;
        for (TbAnalysisTask tbAnalysisTask : list) {
            taskCleanYmdDataList = baseMapper.selectList(new QueryWrapper<TaskCleanYmdData>()
                    .eq("serialnumber", tbAnalysisTask.getId())
                    .eq("ymd", yyyyMMdd)
                    .eq("status", DeleteTaskConstants.CLEAN_STATUS_WAIT));
            if(taskCleanYmdDataList.isEmpty()){
                insertList.add(new TaskCleanYmdData(yyyyMMdd, tbAnalysisTask.getId(), tbAnalysisTask.getAnalyType(), now));
            }
        }
        return insertList;
    }

    @Override
    public void updateBatch(List<TaskCleanYmdData> list) {
        this.updateBatchById(list);
    }

}
