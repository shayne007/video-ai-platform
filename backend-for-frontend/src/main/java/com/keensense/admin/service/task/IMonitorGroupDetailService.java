package com.keensense.admin.service.task;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.admin.entity.task.MonitorGroupDetail;

import java.util.List;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 9:36 2019/6/15
 * @Version v0.1
 */
public interface IMonitorGroupDetailService extends IService<MonitorGroupDetail>{
    /**
     * 查询出监控组中没有启动的监控点
     *
     * @param monitorGroupId 监控组ID
     * @param state 任务状态
     * @return 监控点ID集合
     */
    List<MonitorGroupDetail> queryMonitorGroupUnStartCamera(Long monitorGroupId, String state);

    /**
     * 将监控点移入到指定的监控组中
     * @param monitorGroupId 监控组ID
     * @param cameraIdList 监控点集合
     * @return 移入的成功的监控点总数
     */
    String moveCameraInMonitorGroup(Long monitorGroupId,List<String> cameraIdList,Long userId);

    /**
     * 根据 MonitorGroupId 查询所有的 MonitorGroupDetail
     *
     * @param monitorGroupId 监控组ID
     * @return 监控点详情集合
     */
    List<MonitorGroupDetail> selectMonitorGroupDetailList(Long monitorGroupId);

    /**
     * 查询监控组下是否有任务在执行
     * @param monitorGroupId
     * @return
     */
    List<String> selectMonitorGroupRealTask(Long monitorGroupId);
}
