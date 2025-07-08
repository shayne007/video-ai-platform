package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.constants.CacheItemConstants;
import com.keensense.admin.entity.task.BikeModel;
import com.keensense.admin.mapper.task.BikeModelMapper;
import com.keensense.admin.service.task.IBikeModelService;
import com.keensense.admin.util.EhcacheUtils;
import com.keensense.admin.util.ValidateHelper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("bikeModelService")
public class BikeModelServiceImpl extends ServiceImpl<BikeModelMapper, BikeModel> implements IBikeModelService {
    @Override
    public List<BikeModel> getBikeModelList() {
        List<BikeModel> bikeModelList = null;
        bikeModelList = (List<BikeModel>) EhcacheUtils.getItem(CacheItemConstants.BIKE_MODEL);
        if (ValidateHelper.isEmptyList(bikeModelList)) {
            bikeModelList = baseMapper.selectList(new QueryWrapper<>());
            EhcacheUtils.putItem(CacheItemConstants.BIKE_MODEL, bikeModelList);
        }
        return bikeModelList;
    }
}
