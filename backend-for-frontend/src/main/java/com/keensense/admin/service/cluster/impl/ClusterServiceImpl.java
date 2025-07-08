package com.keensense.admin.service.cluster.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.request.cluster.ClusterRecordPageRequest;
import com.keensense.admin.dto.cluster.FeatureVo;
import com.keensense.admin.entity.cluster.TbClusterTask;
import com.keensense.admin.entity.cluster.TbClusterTaskDetail;
import com.keensense.admin.mapper.cluster.TbClusterTaskMapper;
import com.keensense.admin.service.cluster.IClusterDetailService;
import com.keensense.admin.service.cluster.IClusterService;
import com.keensense.admin.util.ClusterUtil;
import com.keensense.admin.util.ThreadTaskUtil;
import com.keensense.common.platform.enums.ObjTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 15:28 2019/11/21
 * @Version v0.1
 */
@Slf4j
@Service("clusterService")
public class ClusterServiceImpl extends ServiceImpl<TbClusterTaskMapper, TbClusterTask> implements IClusterService {

    @Autowired
    IClusterDetailService clusterDetailService;

    @Override
    public void execute(String operateId, List<FeatureVo> featureVos, ObjTypeEnum objType, Float threshold) {
        executeCluster(operateId, featureVos, objType, threshold);
    }

    @Override
    public IPage<TbClusterTask> recordPage(ClusterRecordPageRequest relineListRequest) {
        Page<TbClusterTask> pages = new Page<>(relineListRequest.getPage(), relineListRequest.getRows());
        IPage<TbClusterTask> page = baseMapper.selectPage(pages, new QueryWrapper<TbClusterTask>().orderByDesc("create_time"));
        return page;
    }

    /**
     * 执行聚类操作
     *
     * @param operateId
     * @param featureVos
     * @param objType
     * @param threshold
     */
    private void executeCluster(String operateId, List<FeatureVo> featureVos, ObjTypeEnum objType, Float threshold) {
        ThreadTaskUtil.submit(() -> {
            TbClusterTask tbClusterTask = new TbClusterTask();
            tbClusterTask.setId(operateId);
            tbClusterTask.setStatus(1);
            baseMapper.updateById(tbClusterTask);
            try {
                JSONObject cluster = ClusterUtil.cluster(operateId, featureVos, threshold,
                        30 * 60L, objType, "1");
                log.info("cluster_result" + cluster);
                if (cluster != null) {
                    JSONArray objects = cluster.getJSONArray("clusters");
                    objects.sort((o1, o2) -> ((JSONArray) o2).size() - ((JSONArray) o1).size());
                    List<TbClusterTaskDetail> details = new ArrayList<>();
                    Date date = new Date();
                    for (int i = 0; i < objects.size(); i++) {
                        JSONArray jsonObject = objects.getJSONArray(i);
                        TbClusterTaskDetail tbClusterTaskDetail = new TbClusterTaskDetail();
                        tbClusterTaskDetail.setCountNum(jsonObject.size());
                        tbClusterTaskDetail.setCreateTime(date);
                        tbClusterTaskDetail.setPid(operateId);
                        tbClusterTaskDetail.setResult(jsonObject.toJSONString());
                        details.add(tbClusterTaskDetail);
                    }
                    clusterDetailService.saveBatch(details);
                    tbClusterTask.setStatus(2);
                } else {
                    tbClusterTask.setStatus(3);
                }
            } catch (IOException e) {
                log.error("cluster fail", e);
                tbClusterTask.setStatus(3);
            }
            baseMapper.updateById(tbClusterTask);
        });
    }

//    /**
//     * 写入结果文件
//     *
//     * @param cluster 聚类结果
//     */
//    private void writeData(String operateId, JSONObject cluster, ClusterTypeEnum objType) {
//        JSONArray clusters = cluster.getJSONArray("clusters");
//        List<ClusterDO> list = new ArrayList<>();
//        for (int i = 0; i < clusters.size(); i++) {
//            JSONArray jsonArray = clusters.getJSONArray(i);
//            for (int j = 0; j < jsonArray.size(); j++) {
//                String uuid = jsonArray.getString(j);
//                String[] info = uuid.split("@@");
//                String cameraName = info[1];
//                String id = info[2];
//                id = id.substring(0, id.lastIndexOf('_'));
//                ClusterDO clusterDO = new ClusterDO();
//                clusterDO.setOperateId(operateId);
//                clusterDO.setClusterIndex(String.valueOf(i));
//                clusterDO.setCameraName(cameraName);
//                clusterDO.setId(id);
//                list.add(clusterDO);
//            }
//
//        }
//        Map<String, List<ClusterDO>> collect = list.stream().collect(Collectors.groupingBy(ClusterDO::getClusterIndex));
//        JSONArray ja = new JSONArray();
//        collect.forEach((k, v) -> {
//            JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(v));
//            ja.add(jsonArray);
//        });
//    }
}
