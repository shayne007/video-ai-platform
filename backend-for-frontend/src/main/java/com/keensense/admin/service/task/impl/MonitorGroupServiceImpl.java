package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.task.MonitorGroup;
import com.keensense.admin.entity.task.MonitorGroupDetail;
import com.keensense.admin.mapper.task.MonitorGroupDetailMapper;
import com.keensense.admin.mapper.task.MonitorGroupMapper;
import com.keensense.admin.service.task.IMonitorGroupDetailService;
import com.keensense.admin.service.task.IMonitorGroupService;
import com.keensense.admin.util.DbPropUtil;
import com.loocme.sys.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 9:37 2019/6/15
 * @Version v0.1
 */
@Service("monitorGroupService")
public class MonitorGroupServiceImpl extends ServiceImpl<MonitorGroupMapper,MonitorGroup> implements IMonitorGroupService{
    @Autowired
    private MonitorGroupDetailMapper monitorGroupDetailMapper;
    @Autowired
    private MonitorGroupMapper monitorGroupMapper;
    @Resource
    private IMonitorGroupDetailService monitorGroupDetailService;
    @Resource
    private IMonitorGroupService monitorGroupService;
    @Override
    public Page<MonitorGroup> selectMonitorGroupByPage(Page<MonitorGroup> pages, Map<String, Object> params) {
        List<MonitorGroup> monitors = baseMapper.selectMonitorGroupByPage(pages,params);
        return pages.setRecords(monitors);
    }

    @Override
    public int querySysSettingMgdTotal() {
        return DbPropUtil.getInt("monitor.group.limit", 50);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMonitorGroup(List<String> monitorGroupList) {
        for (String groupId : monitorGroupList) {
            baseMapper.deleteById(groupId);
            monitorGroupDetailMapper.delete(new QueryWrapper<MonitorGroupDetail>().eq("monitor_group_id",groupId));
        }
    }

    @Override
    public boolean save(MonitorGroup entity) {
        entity.setCreateTime(new Date());
        entity.setLastUpdateTime(new Date());
        return super.save(entity);
    }

    @Override
    public MonitorGroup queryMonitorGroupIdByName(String groupName) {
        return monitorGroupMapper.queryMonitorGroupIdByName(groupName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addMonitorGroup(MonitorGroup monitorGroup, String groupName, List<String> cameraIdLst, Long userId) {
        this.save(monitorGroup);
        String moveStr = "";
        MonitorGroup queryMonitorGroup = monitorGroupService.queryMonitorGroupIdByName(groupName);
        if (ListUtil.isNotNull(cameraIdLst)) {
            moveStr = monitorGroupDetailService.moveCameraInMonitorGroup(queryMonitorGroup.getId(), cameraIdLst, userId);
        }
        String[] moveSuccessedNum = moveStr.split(",");
        if (!"".equals(moveSuccessedNum[0])) {
            return Integer.parseInt(moveSuccessedNum[0]);
        } else {
            return 0;
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateMonitorGroup(MonitorGroup monitorGroup, List<String> cameraIdLst, Long userId) {
        String moveSuccessedNum = null;
        this.updateById(monitorGroup);
        if (ListUtil.isNotNull(cameraIdLst)) {
            moveSuccessedNum = monitorGroupDetailService.moveCameraInMonitorGroup(monitorGroup.getId(), cameraIdLst,
                    userId);
        }
        return moveSuccessedNum;
    }
}
