package com.keensense.admin.service.lawcase.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.lawcase.CaseCameraMedia;
import com.keensense.admin.mapper.lawcase.CaseCameraMediaMapper;
import com.keensense.admin.service.lawcase.ICaseCameraMediaService;
import org.springframework.stereotype.Service;



@Service("caseCameraMediaService")
public class CaseCameraMediaServiceImpl extends ServiceImpl<CaseCameraMediaMapper, CaseCameraMedia> implements ICaseCameraMediaService {
}
