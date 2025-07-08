package com.keensense.job.service.impl;


import com.keensense.job.mapper.CronMapper;
import com.keensense.job.service.ICronService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author cuiss
 * @Description cron操作的服务类
 * @Date 2018/11/6
 */
@Service
public class CronServiceImpl implements ICronService {

    @Resource
    private CronMapper cronMapper;

    @Override
    public String getCronScript() {
        return cronMapper.getCronScript();
    }
}
