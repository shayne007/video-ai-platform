package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.task.ImageSearchRelation;
import com.keensense.admin.mapper.task.ImageSearchRelationMapper;
import com.keensense.admin.service.task.IImageSearchRelationService;
import org.springframework.stereotype.Service;



@Service("imageSearchRelationService")
public class ImageSearchRelationServiceImpl extends ServiceImpl<ImageSearchRelationMapper, ImageSearchRelation> implements IImageSearchRelationService {
}
