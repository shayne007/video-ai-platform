package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.task.TbImagesearchTask;
import com.keensense.admin.mapper.task.TbImagesearchTaskMapper;
import com.keensense.admin.service.task.ITbImagesearchTaskService;
import org.springframework.stereotype.Service;



@Service("tbImagesearchTaskService")
public class TbImagesearchTaskServiceImpl extends ServiceImpl<TbImagesearchTaskMapper, TbImagesearchTask> implements ITbImagesearchTaskService {
}
