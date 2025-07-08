package com.keensense.admin.service.task;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.admin.entity.task.MonitorGroup;

import java.util.List;
import java.util.Map;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 9:36 2019/6/15
 * @Version v0.1
 */
public interface IMonitorGroupService extends IService<MonitorGroup>{

    /**
     *  分页查询
     * @param pages
     * @param params
     * @return
     */
    Page<MonitorGroup> selectMonitorGroupByPage(Page<MonitorGroup> pages, Map<String, Object> params);

    /**
     * 获取系统当前配置监控组最大可加入监控点的数目
     *
     * @return
     */
    int querySysSettingMgdTotal();

    /**
     * 删除监控组
     *
     * @param monitorGroupList
     * @return
     */
    void deleteMonitorGroup(List<String> monitorGroupList);

    /**
     * 查询监控组id
     * @param groupName
     * @return
     */
    MonitorGroup queryMonitorGroupIdByName(String groupName);

    /**
     * 新增监控组
     * @param monitorGroup
     * @param groupName
     * @param cameraIdLst
     * @param userId
     * @return
     */
    int addMonitorGroup(MonitorGroup monitorGroup, String groupName, List<String> cameraIdLst, Long userId);

    /**
     * 更新监控组
     * @param monitorGroup
     * @param cameraIdLst
     * @param userId
     * @return
     */
    String updateMonitorGroup(MonitorGroup monitorGroup, List<String> cameraIdLst, Long userId);
}
