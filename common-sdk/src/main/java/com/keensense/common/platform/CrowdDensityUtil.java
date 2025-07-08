package com.keensense.common.platform;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.common.platform.bo.video.CrowdDensity;
import com.keensense.common.platform.bo.video.CrowdDensityQuery;
import com.keensense.common.platform.constant.CrowdDensityConstant;
import com.keensense.common.util.StandardHttpUtil;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zengyc
 * @Description: 人群密度检测
 * @Date: Created in 10:31 2019/9/25
 * @Version v0.1
 */
public class CrowdDensityUtil {
    private CrowdDensityUtil() {
    }

    /**
     * 批量人群密度对象查询
     *
     * @return
     */
    public static Page<CrowdDensity> getDensityResultList(String requestUrl, CrowdDensityQuery crowdDensityQuery) {
        Page<CrowdDensity> pages = new Page<>(crowdDensityQuery.getPageNo(), crowdDensityQuery.getPageSize());
        Map<String, String> densityParams = new HashMap<>();
        String target = "CrowdDensity";
        if (StringUtils.isNotEmpty(crowdDensityQuery.getSerialnumber())) {
            densityParams.put(target + ".serialnumber.In", crowdDensityQuery.getSerialnumber());
        }
        if (StringUtils.isNotEmpty(crowdDensityQuery.getStartTime())) {
            densityParams.put(target + ".createTime.Gte", crowdDensityQuery.getStartTime().replaceAll("[-:\\s]", ""));
        }
        if (StringUtils.isNotEmpty(crowdDensityQuery.getEndTime())) {
            densityParams.put(target + ".createTime.Lte", crowdDensityQuery.getEndTime().replaceAll("[-:\\s]", ""));
        }
        if (crowdDensityQuery.getCountMin() != null) {
            densityParams.put(target + ".count.Gte", crowdDensityQuery.getCountMin() + "");
        }
        if (crowdDensityQuery.getCountMax() != null) {
            densityParams.put(target + ".count.Lte", crowdDensityQuery.getCountMax() + "");
        }
        if (StringUtils.isEmpty(crowdDensityQuery.getCreateTimeOrder()) && StringUtils.isEmpty(crowdDensityQuery.getOrderField())) {
            crowdDensityQuery.setCreateTimeOrder("desc");
        }
        if (StringUtils.isNotEmpty(crowdDensityQuery.getCreateTimeOrder())) {
            densityParams.put(target + ".createTime.Order", crowdDensityQuery.getCreateTimeOrder());
        }
        if (StringUtils.isNotEmpty(crowdDensityQuery.getOrderField())) {
            densityParams.put(target + "." + crowdDensityQuery.getOrderField() + ".Order", crowdDensityQuery.getOrderMethod());
        }
        densityParams.put(target + ".RecordStartNo", crowdDensityQuery.getPageNo() + "");
        densityParams.put(target + ".PageRecordNum", crowdDensityQuery.getPageSize() + "");
        String densityRequestUrl = requestUrl + CrowdDensityConstant.CROWD_DENSITY_RESULT;
        //处理数据
        String densityResponse = StandardHttpUtil.getHttp(densityRequestUrl, densityParams);
        JSONObject crowdDensityListObject = JSONObject.parseObject(densityResponse).getJSONObject("CrowdDensityListObject");
        String crowdDensityObject = crowdDensityListObject.getString("CrowdDensityObject");
        List<CrowdDensity> list = JSONObject.parseArray(crowdDensityObject, CrowdDensity.class);
        int count = crowdDensityListObject.getInteger("Count");
        pages.setTotal(count);
        pages.setRecords(list);
        return pages;
    }
}
