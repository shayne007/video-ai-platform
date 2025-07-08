package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.task.TbImagesearchRecord;
import com.keensense.admin.mapper.task.TbImagesearchRecordMapper;
import com.keensense.admin.service.task.ITbImagesearchRecordService;
import org.springframework.stereotype.Service;



@Service("tbImagesearchRecordService")
public class TbImagesearchRecordServiceImpl extends ServiceImpl<TbImagesearchRecordMapper, TbImagesearchRecord> implements ITbImagesearchRecordService {
}
