package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.task.CarColorModel;
import com.keensense.admin.mapper.task.CarColorModelMapper;
import com.keensense.admin.service.task.ICarColorModelService;
import org.springframework.stereotype.Service;



@Service("carColorModelService")
public class CarColorModelServiceImpl extends ServiceImpl<CarColorModelMapper, CarColorModel> implements ICarColorModelService {
}
