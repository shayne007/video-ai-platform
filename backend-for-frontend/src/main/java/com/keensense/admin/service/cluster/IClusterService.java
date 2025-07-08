package com.keensense.admin.service.cluster;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.admin.request.cluster.ClusterRecordPageRequest;
import com.keensense.admin.dto.cluster.FeatureVo;
import com.keensense.admin.entity.cluster.TbClusterTask;
import com.keensense.common.platform.enums.ObjTypeEnum;

import java.util.List;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 15:28 2019/11/21
 * @Version v0.1
 */
public interface IClusterService extends IService<TbClusterTask> {
    void execute(String operateId, List<FeatureVo> featureVos, ObjTypeEnum objType, Float threshold);

    IPage<TbClusterTask> recordPage(ClusterRecordPageRequest relineListRequest);
}
