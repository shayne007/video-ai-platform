package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.task.TbAnalysisDetail;
import com.keensense.admin.mapper.task.TbAnalysisDetailMapper;
import com.keensense.admin.service.task.ITbAnalysisDetailService;
import org.springframework.stereotype.Service;



@Service("tbAnalysisDetailService")
public class TbAnalysisDetailServiceImpl extends ServiceImpl<TbAnalysisDetailMapper, TbAnalysisDetail> implements ITbAnalysisDetailService {
}
