package com.keensense.admin.service.lawcase.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.lawcase.AppLawcaseBasicconf;
import com.keensense.admin.mapper.lawcase.AppLawcaseBasicconfMapper;
import com.keensense.admin.service.lawcase.IAppLawcaseBasicconfService;
import org.springframework.stereotype.Service;



@Service("appLawcaseBasicconfService")
public class AppLawcaseBasicconfServiceImpl extends ServiceImpl<AppLawcaseBasicconfMapper, AppLawcaseBasicconf> implements IAppLawcaseBasicconfService {
}
