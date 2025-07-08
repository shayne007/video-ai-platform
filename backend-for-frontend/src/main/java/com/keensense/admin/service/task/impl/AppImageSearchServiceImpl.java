package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.task.AppImageSearch;
import com.keensense.admin.mapper.task.AppImageSearchMapper;
import com.keensense.admin.service.task.IAppImageSearchService;
import org.springframework.stereotype.Service;



@Service("appImageSearchService")
public class AppImageSearchServiceImpl extends ServiceImpl<AppImageSearchMapper, AppImageSearch> implements IAppImageSearchService {
}
