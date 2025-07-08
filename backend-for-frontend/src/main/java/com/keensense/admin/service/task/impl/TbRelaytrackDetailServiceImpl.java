package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.task.TbRelaytrackDetail;
import com.keensense.admin.mapper.task.TbRelaytrackDetailMapper;
import com.keensense.admin.service.task.ITbRelaytrackDetailService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("tbRelaytrackDetailService")
public class TbRelaytrackDetailServiceImpl extends ServiceImpl<TbRelaytrackDetailMapper, TbRelaytrackDetail> implements ITbRelaytrackDetailService {
    @Override
    public Map<String, Object> getNameMap(String taskId) {
        return baseMapper.getNameMap(taskId);
    }
}
