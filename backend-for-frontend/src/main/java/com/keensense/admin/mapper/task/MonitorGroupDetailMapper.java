package com.keensense.admin.mapper.task;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keensense.admin.entity.task.MonitorGroupDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 9:39 2019/6/15
 * @Version v0.1
 */
@Mapper
public interface MonitorGroupDetailMapper extends BaseMapper<MonitorGroupDetail>{
    /**
     * 根据 MonitorGroupId 查询所有的 MonitorGroupDetail
     *
     * @param monitorGroupId 监控组ID
     * @return
     */
    List<MonitorGroupDetail> selectMonitorGroupDetailList(Long monitorGroupId);

    /**
     * 查询监控组下任务序列号集合
     * @param monitorGroupId
     * @return
     */
    List<String> selectMonitorGroupSerialnumber(Long monitorGroupId);


    /**
     * 查询监控组下是否有任务在执行
     * @param monitorGroupId
     * @return
     */
    List<String> selectMonitorGroupRealTask(Long monitorGroupId);

    /**
     * 根据监控组id集合查询camera
     * @return
     */
    List<String> selectCameraByMonitorGroupId(Long monitorGroupId);
}
