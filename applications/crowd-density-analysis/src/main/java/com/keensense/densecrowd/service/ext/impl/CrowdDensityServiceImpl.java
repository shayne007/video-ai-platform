package com.keensense.densecrowd.service.ext.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.common.platform.CrowdDensityUtil;
import com.keensense.common.platform.bo.video.CrowdDensity;
import com.keensense.common.platform.bo.video.CrowdDensityQuery;
import com.keensense.common.util.DateUtil;
import com.keensense.densecrowd.entity.task.VsdTaskRelation;
import com.keensense.densecrowd.service.ext.CrowdDensityService;
import com.keensense.densecrowd.service.task.IDensecrowdWarnResultService;
import com.keensense.densecrowd.service.task.IVsdTaskRelationService;
import com.keensense.densecrowd.util.AlarmPageUtils;
import com.keensense.densecrowd.util.StringUtils;
import com.keensense.densecrowd.util.VsdTaskUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 11:55 2019/9/25
 * @Version v0.1
 */
@Slf4j
@Service("crowdDensityService")
public class CrowdDensityServiceImpl extends AbstractService implements CrowdDensityService {
    @Autowired
    IVsdTaskRelationService vsdTaskRelationService;

    @Autowired
    IDensecrowdWarnResultService densecrowdWarnResultService;

    @Override
    public Page<CrowdDensity> getDensityResultList(CrowdDensityQuery crowdDensityQuery) {
        String deviceId = crowdDensityQuery.getDeviceIds();
        if (StringUtils.isNotEmpty(deviceId)) {
            String[] deviceIds = deviceId.split(",");
            List<VsdTaskRelation> taskRelations = vsdTaskRelationService.queryListByDeviceIds(deviceIds);
            if (taskRelations == null || taskRelations.isEmpty()) {
                return new Page<>();
            }
            Set<String> serialnumbers = new HashSet<>();
            for (VsdTaskRelation vsdTaskRelation : taskRelations) {
                serialnumbers.add(vsdTaskRelation.getSerialnumber());
            }
            crowdDensityQuery.setSerialnumber(String.join(",", serialnumbers));
        }
        Page<CrowdDensity> page = new Page<>();
        if (crowdDensityQuery.getRecordType() != null && crowdDensityQuery.getRecordType() == 1) {
            page = densecrowdWarnResultService.getDensityResultList(crowdDensityQuery);
        } else {
            page = CrowdDensityUtil.getDensityResultList(initKeensenseUrl(), crowdDensityQuery);
        }
        List<CrowdDensity> list = page.getRecords();
        for (CrowdDensity crowdDensity : list) {
            crowdDensity.setCameraName(VsdTaskUtil.getTaskName(crowdDensity.getSerialnumber()));
            crowdDensity.setCreateTime(DateUtil.formatTime(DateUtil.parseYmdhms(crowdDensity.getCreateTime())));
            if (crowdDensity.getAlarmThreshold() == null) {
                if (crowdDensityQuery.getCountMin() == null) {
                    crowdDensityQuery.setCountMin(0);
                }
                crowdDensity.setAlarmThreshold(crowdDensityQuery.getCountMin());
            }
        }
        return page;
    }
}
