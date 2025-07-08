package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.task.ImageSearchTask;
import com.keensense.admin.mapper.task.ImageSearchTaskMapper;
import com.keensense.admin.service.task.IImageSearchTaskService;
import org.springframework.stereotype.Service;



@Service("imageSearchTaskService")
public class ImageSearchTaskServiceImpl extends ServiceImpl<ImageSearchTaskMapper, ImageSearchTask> implements IImageSearchTaskService {
}
