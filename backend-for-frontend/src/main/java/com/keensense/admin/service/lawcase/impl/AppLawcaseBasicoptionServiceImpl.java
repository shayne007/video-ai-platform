package com.keensense.admin.service.lawcase.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.lawcase.AppLawcaseBasicoption;
import com.keensense.admin.mapper.lawcase.AppLawcaseBasicoptionMapper;
import com.keensense.admin.service.lawcase.IAppLawcaseBasicoptionService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("appLawcaseBasicoptionService")
public class AppLawcaseBasicoptionServiceImpl extends ServiceImpl<AppLawcaseBasicoptionMapper, AppLawcaseBasicoption> implements IAppLawcaseBasicoptionService {

    @Override
    public List<AppLawcaseBasicoption> queryAppLawcaseList(String typeCode) {
        return baseMapper.queryAppLawcaseList(typeCode);
    }
}
