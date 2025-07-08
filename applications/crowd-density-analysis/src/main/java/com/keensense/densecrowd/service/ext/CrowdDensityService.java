package com.keensense.densecrowd.service.ext;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.common.platform.bo.video.CrowdDensity;
import com.keensense.common.platform.bo.video.CrowdDensityQuery;

import java.util.List;
import java.util.Map;

/**
 * @Author: zengyc
 * @Description: 人群密度检测接口
 * @Date: Created in 11:52 2019/9/25
 * @Version v0.1
 */
public interface CrowdDensityService {
    /**
     * 13.2.3.	批量人群密度对象查询
     *
     * @param crowdDensityQuery
     * @return
     */
    Page<CrowdDensity> getDensityResultList(CrowdDensityQuery crowdDensityQuery);
}
