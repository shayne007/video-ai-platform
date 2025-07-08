package com.keensense.densecrowd.service.task;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.common.platform.bo.video.CrowdDensity;
import com.keensense.common.platform.bo.video.CrowdDensityQuery;
import com.keensense.densecrowd.entity.task.DensecrowdWarnResult;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 11:13 2020/7/20
 * @Version v0.1
 */
public interface IDensecrowdWarnResultService extends IService<DensecrowdWarnResult>{
    Page<CrowdDensity> getDensityResultList(CrowdDensityQuery crowdDensityQuery);
}
