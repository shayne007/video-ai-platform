package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.constants.VideoTaskConstant;
import com.keensense.admin.entity.task.MonitorGroupDetail;
import com.keensense.admin.mapper.task.MonitorGroupDetailMapper;
import com.keensense.admin.service.task.IMonitorGroupDetailService;
import com.keensense.admin.service.task.IMonitorGroupService;
import com.keensense.common.exception.VideoException;
import com.loocme.sys.util.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 9:37 2019/6/15
 * @Version v0.1
 */
@Slf4j
@Service("monitorGroupDetailService")
public class MonitorGroupDetailServiceImpl extends ServiceImpl<MonitorGroupDetailMapper, MonitorGroupDetail> implements IMonitorGroupDetailService {

    @Autowired
    private IMonitorGroupService monitorGroupService;

    @Override
    public List<MonitorGroupDetail> queryMonitorGroupUnStartCamera(Long monitorGroupId, String state) {
        List<MonitorGroupDetail> result = new ArrayList<>();
        List<MonitorGroupDetail> monitorGroupDetails = baseMapper.selectMonitorGroupDetailList(monitorGroupId);

        if (ListUtil.isNull(monitorGroupDetails)) {
            return result;
        }

        for (MonitorGroupDetail mgd : monitorGroupDetails) {
            Integer isValid = mgd.getIsValid();
            // 未启动的
            if (VideoTaskConstant.STATUS.FINISHED.equals(state)) {
                if (isValid == null || isValid == Integer.parseInt(state)) {
                    result.add(mgd);
                }
            } else if (VideoTaskConstant.STATUS.RUNNING.equals(state)) {
                if (isValid != null && isValid == Integer.parseInt(state)) {
                    result.add(mgd);
                }
            }
        }
        return result;
    }

    /**
     * 监控组移入监控点
     *
     * @param monitorGroupId 监控组ID
     * @param cameraIdList   监控点集合
     * @return 移入的总数
     */
    @Override
    public String moveCameraInMonitorGroup(Long monitorGroupId, List<String> cameraIdList, Long createUserId) {
        String moveNum = "0";
        if (ListUtil.isNull(cameraIdList)) {
            return moveNum;
        }
        // 获取系统当前配置最大的监控组数目
        int sysSettingMgTotalNum = monitorGroupService.querySysSettingMgdTotal();

        // 需要加锁，防止并发导致一个组加入的监控总数超过系统设置的最大值
        synchronized (MonitorGroupDetailServiceImpl.class) {
            // 查询当前监控组中的监控点总数
            int currentMgTotalNum = baseMapper.selectCount(new QueryWrapper<MonitorGroupDetail>().eq("monitor_group_id", monitorGroupId));
            if (currentMgTotalNum >= sysSettingMgTotalNum) {
                log.warn("当前监控组可添加的监控点已满，不能在添加监控点：monitorGroupId：{}，currentMgTotalNum：{}",
                        monitorGroupId, currentMgTotalNum);
                return moveNum;
            }

            List<MonitorGroupDetail> waitInsertMgdLst = null;
            int emptyNum = sysSettingMgTotalNum - currentMgTotalNum;
            int waitInsertNum = cameraIdList.size();

            // 判断当前监控组还能插入多少条数据
            if (waitInsertNum <= emptyNum) {
                waitInsertMgdLst = packageMonitorGroupDetail(cameraIdList, waitInsertNum, monitorGroupId, createUserId);
            } else {
                waitInsertMgdLst = packageMonitorGroupDetail(cameraIdList, emptyNum, monitorGroupId, createUserId);
            }

            log.info("当前监控组monitorGroupId：{},currentMgTotalNum:{},emptyNum:{},waitInsertMgdLst size:{}",
                    monitorGroupId, currentMgTotalNum, emptyNum, waitInsertMgdLst.size());

            int insertTotalNum = 0;
            int defeatTotalNum = 0;
            for (MonitorGroupDetail mgd : waitInsertMgdLst) {

                // 检查监控点是否已经加入了监控组
                if (checkCameraExistMonitorGroup(mgd.getMonitorGroupId(), mgd.getCameraId())) {
                    defeatTotalNum++;
                    log.info("当前监控组monitorGroupId：{},cameraId:{} 已存在！", monitorGroupId, mgd.getCameraId());
                    continue;
                }

                insertTotalNum += baseMapper.insert(mgd);
            }
            moveNum = insertTotalNum + "," + defeatTotalNum;
        }

        return moveNum;
    }

    @Override
    public List<MonitorGroupDetail> selectMonitorGroupDetailList(Long monitorGroupId) {
        return baseMapper.selectMonitorGroupDetailList(monitorGroupId);
    }

    /**
     * 封装 需要插入数据库的 MonitorGroupDetail 集合
     *
     * @param cameraIdList   监控点集合
     * @param insertNum      可插入的数量
     * @param monitorGroupId 监控组ID
     * @param createUserId   当前用户ID
     * @return
     */
    private List<MonitorGroupDetail> packageMonitorGroupDetail(List<String> cameraIdList, int insertNum,
                                                               Long monitorGroupId, Long createUserId) {
        List<MonitorGroupDetail> insertList = new ArrayList<>();
        for (int i = 0; i < insertNum; i++) {
            MonitorGroupDetail mgd = new MonitorGroupDetail();
            String cameraId = cameraIdList.get(i);
            mgd.setCameraId(Long.valueOf(cameraId));
            mgd.setMonitorGroupId(monitorGroupId);
            mgd.setCreateTime(new Date());
            mgd.setUpdateTime(new Date());
            mgd.setCreateUserId(createUserId);
            insertList.add(mgd);
        }
        return insertList;
    }

    public boolean checkCameraExistMonitorGroup(Long monitorGroupId, Long cameraId) {
        if (monitorGroupId == null || cameraId == null) {
            throw new VideoException("monitorGroupId 或 cameraId 不能为null");
        }
        int num = baseMapper.selectCount(new QueryWrapper<MonitorGroupDetail>().eq("monitor_group_id", monitorGroupId).eq("camera_id", cameraId));
        if (num > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<String> selectMonitorGroupRealTask(Long monitorGroupId) {
        return baseMapper.selectMonitorGroupRealTask(monitorGroupId);
    }
}
