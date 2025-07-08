package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.task.TbRelaytrackTask;
import com.keensense.admin.mapper.task.TbRelaytrackTaskMapper;
import com.keensense.admin.service.task.ITbRelaytrackTaskService;
import org.springframework.stereotype.Service;



@Service("tbRelaytrackTaskService")
public class TbRelaytrackTaskServiceImpl extends ServiceImpl<TbRelaytrackTaskMapper, TbRelaytrackTask> implements ITbRelaytrackTaskService {
}
