package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.task.CarBrandModel;
import com.keensense.admin.mapper.task.CarBrandModelMapper;
import com.keensense.admin.service.task.ICarBrandModelService;
import org.springframework.stereotype.Service;



@Service("carBrandModelService")
public class CarBrandModelServiceImpl extends ServiceImpl<CarBrandModelMapper, CarBrandModel> implements ICarBrandModelService {
}
