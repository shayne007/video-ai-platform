package com.keensense.densecrowd.service.task.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.common.platform.bo.video.CrowdDensity;
import com.keensense.common.platform.bo.video.CrowdDensityQuery;
import com.keensense.densecrowd.entity.task.DensecrowdWarnResult;
import com.keensense.densecrowd.mapper.task.DensecrowdWarnResultMapper;
import com.keensense.densecrowd.service.task.IDensecrowdWarnResultService;
import com.keensense.densecrowd.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 11:41 2020/7/20
 * @Version v0.1
 */
@Slf4j
@Service("densecrowdWarnResultServiceImpl")
public class DensecrowdWarnResultServiceImpl extends ServiceImpl<DensecrowdWarnResultMapper, DensecrowdWarnResult> implements IDensecrowdWarnResultService {
    @Override
    public Page<CrowdDensity> getDensityResultList(CrowdDensityQuery crowdDensityQuery) {
        String[] serialnumbers = new String[0];
        if (StringUtils.isNotEmpty(crowdDensityQuery.getSerialnumber())) {
            serialnumbers = crowdDensityQuery.getSerialnumber().split(",");
        }
        String createtime = StringUtils.isNotEmpty(crowdDensityQuery.getStartTime()) ? crowdDensityQuery.getStartTime().replaceAll("[-:\\s]", "") : null;
        String endtime = StringUtils.isNotEmpty(crowdDensityQuery.getEndTime()) ? crowdDensityQuery.getEndTime().replaceAll("[-:\\s]", "") : null;
        String order = StringUtils.isNotEmpty(crowdDensityQuery.getCreateTimeOrder()) ? crowdDensityQuery.getCreateTimeOrder() : "desc";
        Page page = new Page<>(crowdDensityQuery.getPageNo(), crowdDensityQuery.getPageSize());
        IPage pages = this.page(page, new QueryWrapper<DensecrowdWarnResult>()
                .in(serialnumbers == null || serialnumbers.length != 0, "serialnumber", serialnumbers)
                .ge(crowdDensityQuery.getCountMin() != null, "count", crowdDensityQuery.getCountMin())
                .le(crowdDensityQuery.getCountMax() != null, "count", crowdDensityQuery.getCountMax())
                .ge(StringUtils.isNotEmpty(createtime), "create_time", createtime)
                .le(StringUtils.isNotEmpty(endtime), "create_time", endtime)
                .orderBy(StringUtils.isNotEmpty(order), order.equals("asc"), "create_time"));
        List<DensecrowdWarnResult> list = pages.getRecords();
        List<CrowdDensity> densityList = new ArrayList<>();
        for (DensecrowdWarnResult result : list) {
            CrowdDensity crowdDensity = new CrowdDensity();
            BeanUtils.copyProperties(result, crowdDensity);
            densityList.add(crowdDensity);
        }
        page.setRecords(densityList);
        page.setTotal(pages.getTotal());
        return page;
    }
}
