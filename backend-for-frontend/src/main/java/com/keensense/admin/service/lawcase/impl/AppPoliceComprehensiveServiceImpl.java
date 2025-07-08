package com.keensense.admin.service.lawcase.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.lawcase.AppPoliceComprehensive;
import com.keensense.admin.mapper.lawcase.AppPoliceComprehensiveMapper;
import com.keensense.admin.service.lawcase.IAppPoliceComprehensiveService;
import org.springframework.stereotype.Service;



@Service("appPoliceComprehensiveService")
public class AppPoliceComprehensiveServiceImpl extends ServiceImpl<AppPoliceComprehensiveMapper, AppPoliceComprehensive> implements IAppPoliceComprehensiveService {
}
