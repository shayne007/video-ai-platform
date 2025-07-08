package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.task.ObjextResultTrack;
import com.keensense.admin.mapper.task.ObjextResultTrackMapper;
import com.keensense.admin.service.task.IObjextResultTrackService;
import org.springframework.stereotype.Service;



@Service("objextResultTrackService")
public class ObjextResultTrackServiceImpl extends ServiceImpl<ObjextResultTrackMapper, ObjextResultTrack> implements IObjextResultTrackService {
}
