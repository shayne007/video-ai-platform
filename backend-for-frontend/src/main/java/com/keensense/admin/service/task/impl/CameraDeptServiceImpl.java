package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.task.CameraDept;
import com.keensense.admin.mapper.task.CameraDeptMapper;
import com.keensense.admin.service.task.ICameraDeptService;
import org.springframework.stereotype.Service;



@Service("cameraDeptService")
public class CameraDeptServiceImpl extends ServiceImpl<CameraDeptMapper, CameraDept> implements ICameraDeptService {
}
