package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.task.BikeColorModel;
import com.keensense.admin.mapper.task.BikeColorModelMapper;
import com.keensense.admin.service.task.IBikeColorModelService;
import org.springframework.stereotype.Service;



@Service("bikeColorModelService")
public class BikeColorModelServiceImpl extends ServiceImpl<BikeColorModelMapper, BikeColorModel> implements IBikeColorModelService {
}
