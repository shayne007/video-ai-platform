package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.task.TAnalysetask;
import com.keensense.admin.mapper.task.TAnalysetaskMapper;
import com.keensense.admin.service.task.ITAnalysetaskService;
import org.springframework.stereotype.Service;



@Service("tAnalysetaskService")
public class TAnalysetaskServiceImpl extends ServiceImpl<TAnalysetaskMapper, TAnalysetask> implements ITAnalysetaskService {
}
