package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.task.TbAnalysisTask;
import com.keensense.admin.mapper.task.TbAnalysisTaskMapper;
import com.keensense.admin.service.task.ITbAnalysisTaskService;
import org.springframework.stereotype.Service;



@Service("tbAnalysisTaskService")
public class TbAnalysisTaskServiceImpl extends ServiceImpl<TbAnalysisTaskMapper, TbAnalysisTask> implements ITbAnalysisTaskService {
}
