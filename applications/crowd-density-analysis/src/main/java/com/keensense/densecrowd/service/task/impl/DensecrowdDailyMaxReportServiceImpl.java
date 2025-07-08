package com.keensense.densecrowd.service.task.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.densecrowd.entity.task.DensecrowdDailyMaxReport;
import com.keensense.densecrowd.mapper.task.DensecrowdDailyMaxReportMapper;
import com.keensense.densecrowd.service.task.IDensecrowdDailyMaxReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 20:46 2020/7/27
 * @Version v0.1
 */
@Slf4j
@Service("densecrowdDailyMaxReportService")
public class DensecrowdDailyMaxReportServiceImpl extends ServiceImpl<DensecrowdDailyMaxReportMapper, DensecrowdDailyMaxReport> implements IDensecrowdDailyMaxReportService {

}
