package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.task.TRelaytrackTaskdetail;
import com.keensense.admin.mapper.task.TRelaytrackTaskdetailMapper;
import com.keensense.admin.service.task.ITRelaytrackTaskdetailService;
import org.springframework.stereotype.Service;



@Service("tRelaytrackTaskdetailService")
public class TRelaytrackTaskdetailServiceImpl extends ServiceImpl<TRelaytrackTaskdetailMapper, TRelaytrackTaskdetail> implements ITRelaytrackTaskdetailService {
}
