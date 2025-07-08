package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.task.CameraSpecial;
import com.keensense.admin.mapper.task.CameraSpecialMapper;
import com.keensense.admin.service.task.ICameraSpecialService;
import org.springframework.stereotype.Service;



@Service("cameraSpecialService")
public class CameraSpecialServiceImpl extends ServiceImpl<CameraSpecialMapper, CameraSpecial> implements ICameraSpecialService {
}
