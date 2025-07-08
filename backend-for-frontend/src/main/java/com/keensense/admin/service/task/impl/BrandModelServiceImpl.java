package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.task.BrandModel;
import com.keensense.admin.mapper.task.BrandModelMapper;
import com.keensense.admin.service.task.IBrandModelService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("brandModelService")
public class BrandModelServiceImpl extends ServiceImpl<BrandModelMapper, BrandModel> implements IBrandModelService {

    @Override
    public List<String> selectAllCarBrand() {
        return baseMapper.selectAllCarBrand();
    }

    @Override
    public List<BrandModel> selectAllCarSeries(){
        return baseMapper.selectAllCarSeries();
    }

}
