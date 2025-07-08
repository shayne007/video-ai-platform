package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.constants.CacheItemConstants;
import com.keensense.admin.entity.task.HumanColorModel;
import com.keensense.admin.mapper.task.BikeColorModelMapper;
import com.keensense.admin.mapper.task.CarColorModelMapper;
import com.keensense.admin.mapper.task.HumanColorModelMapper;
import com.keensense.admin.service.task.IHumanColorModelService;
import com.keensense.admin.util.EhcacheUtils;
import com.keensense.admin.util.EntityObjectConverter;
import com.keensense.admin.util.ValidateHelper;
import com.keensense.admin.vo.ColorVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("humanColorModelService")
public class HumanColorModelServiceImpl extends ServiceImpl<HumanColorModelMapper, HumanColorModel> implements IHumanColorModelService {

    @Resource
    private BikeColorModelMapper bikeColorModelMapper;
    @Resource
    private CarColorModelMapper carColorModelMapper;

    @Override
    public List<ColorVo> getColorModelList(String type) {
        List<ColorVo> colorModelList = null;
        if (ColorVo.HUMAN_COLOR_TYPE.equalsIgnoreCase(type)) {
            colorModelList = (List<ColorVo>) EhcacheUtils.getItem(CacheItemConstants.PERSON_COLOR_VALUE);
            if (ValidateHelper.isEmptyList(colorModelList)) {
                colorModelList = EntityObjectConverter.getList(baseMapper.selectList(new QueryWrapper<>()), ColorVo.class);
                EhcacheUtils.putItem(CacheItemConstants.PERSON_COLOR_VALUE,colorModelList);
            }
        } else if (ColorVo.BIKE_COLOR_TYPE.equalsIgnoreCase(type)) {
            colorModelList = (List<ColorVo>) EhcacheUtils.getItem(CacheItemConstants.BIKE_COLOR_VALUE);
            if (ValidateHelper.isEmptyList(colorModelList)) {
                colorModelList = EntityObjectConverter.getList(bikeColorModelMapper.selectList(new QueryWrapper<>()), ColorVo.class);
                EhcacheUtils.putItem(CacheItemConstants.BIKE_COLOR_VALUE,colorModelList);
            }
        } else if (ColorVo.CAR_COLOR_TYPE.equalsIgnoreCase(type)) {
            colorModelList = (List<ColorVo>) EhcacheUtils.getItem(CacheItemConstants.CAR_COLOR_VALUE);
            if (ValidateHelper.isEmptyList(colorModelList)) {
                colorModelList = EntityObjectConverter.getList(carColorModelMapper.selectList(new QueryWrapper<>()), ColorVo.class);
                EhcacheUtils.putItem(CacheItemConstants.CAR_COLOR_VALUE,colorModelList);
            }
        }
        return colorModelList;
    }
}
