package com.keensense.task.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.task.cache.VsdTaskCache;
import com.keensense.task.config.NacosConfig;
import com.keensense.task.constants.ObjextTaskConstants;
import com.keensense.task.constants.TaskConstants;
import com.keensense.task.entity.TbAnalysisDetail;
import com.keensense.task.entity.TbAnalysisTask;
import com.keensense.task.entity.VsdTask;
import com.keensense.task.factory.AbstractTaskManager;
import com.keensense.task.mapper.TbAnalysisDetailMapper;
import com.keensense.task.mapper.TbAnalysisTaskMapper;
import com.keensense.task.mapper.VsdTaskMapper;
import com.keensense.task.service.IVsdTaskService;
import com.keensense.task.util.DateUtil;
import com.keensense.task.util.TaskParamValidUtil;
import com.keensense.task.util.TbAnalysisDetailUtil;
import com.keensense.task.util.VideoExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @Description: vsdTask service 实现类
 * @Author: wujw
 * @CreateDate: 2019/5/17 11:23
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Service
@Slf4j
public class VsdTaskServiceImpl extends ServiceImpl<VsdTaskMapper, VsdTask> implements IVsdTaskService {

    @Autowired
    private TbAnalysisDetailMapper tbAnalysisDetailMapper;
    @Autowired
    private TbAnalysisTaskMapper tbAnalysisTaskMapper;
    @Autowired
    private NacosConfig nacosConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertVsdTask() {
        int count = 0;
        List<TbAnalysisDetail> tbAnalysisDetails = tbAnalysisDetailMapper
                .selectList(TbAnalysisDetailUtil.queryAnalysisDetailByStatus("analysis_status"));
        if (!tbAnalysisDetails.isEmpty()) {
            Map<String, TbAnalysisTask> tbAnalysisTaskMap = getTbAnalysisMap(tbAnalysisDetails);
            TbAnalysisTask tbAnalysisTask;
            List<VsdTask> vsdTasksList = new ArrayList<>();
            for (TbAnalysisDetail tbAnalysisDetail : tbAnalysisDetails) {
                tbAnalysisTask = tbAnalysisTaskMap.get(tbAnalysisDetail.getTaskId());
                if (tbAnalysisTask == null || TaskConstants.TASK_DELETE_TURE == tbAnalysisTask.getStatus()) {
                    tbAnalysisDetail.setLastupdateTime(DateUtil.now());
                    tbAnalysisDetail.setAnalysisStatus(TaskConstants.OPERAT_TYPE_ERROR);
                    continue;
                }
                //状态为等待处理时往vsd_task表中插入数据
                if (TaskConstants.OPERAT_TYPE_WATI == tbAnalysisDetail.getAnalysisStatus()) {
                    vsdTasksList.add(insertVasTask(tbAnalysisTask, tbAnalysisDetail));
                    tbAnalysisDetail.setLastupdateTime(DateUtil.now());
                    updateDetailByType(tbAnalysisDetail, tbAnalysisTask);
                } else {
                    updateDetailProgress(tbAnalysisDetail, tbAnalysisTask);
                }
            }
            if (!vsdTasksList.isEmpty()) {
                baseMapper.insertBatch(vsdTasksList);
            }
            count = tbAnalysisDetailMapper.updateBatch(tbAnalysisDetails);
        }
        return count;
    }

    @Override
    public int updateStatusByIds(Long[] ids, int status) {
        return baseMapper.updateStatusByIds(ids, status);
    }

    @Override
    public int updateStatusById(Long id, int status) {
        return baseMapper.updateStatusById(id, status);
    }

    @Override
    public int updateVsdTask(VsdTask vsdTask) {
        return baseMapper.updateById(vsdTask);
    }

    @Override
    public List<VsdTask> selectByWrapper(QueryWrapper<VsdTask> queryWrapper) {
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void updateUdrSetting(String serialnumber, JSONObject param) {
        Integer scene = Optional.ofNullable(param.getInteger("scene")).orElse(null);
        Boolean isInterested = TaskParamValidUtil.getBoolean(param, "isInterested", true, false);
        JSONObject udrSetting = Optional.ofNullable(param.getJSONArray("udrVertices"))
                .map(p -> TaskParamValidUtil.getUdrSetting(p,isInterested)).orElse(null);
        String url = Optional.ofNullable(param.getString("url")).orElse(null);
        Integer enablePartial = Optional.ofNullable(param.getInteger("enablePartial")).orElse(null);
        JSONObject tripWires = AbstractTaskManager.getTripWires(param);
        if (udrSetting == null && scene == null && url == null && enablePartial == null && tripWires == null) {
            throw VideoExceptionUtil.getValidException("请输入要修改的参数!");
        }
        List<VsdTask> vsdTaskList = baseMapper.selectList(new QueryWrapper<VsdTask>().eq("userserialnumber", serialnumber));
        if (vsdTaskList.isEmpty()) {
            throw VideoExceptionUtil.getValidException("can not find vsdTask records with serialnumber = " + serialnumber);
        } else {
            JSONObject paramObject;
            for (VsdTask vsdTask : vsdTaskList) {
                paramObject = JSON.parseObject(vsdTask.getParam());
                JSONObject analysisCfg = Optional.ofNullable(paramObject.getJSONObject("analysisCfg")).orElse(new JSONObject());
                if (scene != null) {
                    analysisCfg.put("scene", scene);
                }
                if (udrSetting != null) {
                    analysisCfg.put("udrSetting", udrSetting);
                }
                if(tripWires != null){
                    analysisCfg.put("eventConfig", tripWires);
                }
                if(!StringUtils.isEmpty(url)){
                    paramObject.put("url", url);
                }
                if(enablePartial != null){
                    paramObject.put("enablePartial", TaskParamValidUtil.isPositiveInteger(param, "enablePartial", 0, 0, 1));
                }
                paramObject.put("analysisCfg", analysisCfg);
                vsdTask.setParam(paramObject.toString());
            }
            if (!this.updateBatchById(vsdTaskList)) {
                throw VideoExceptionUtil.getDbException("更新失败【数据库操作失败】");
            }
        }
    }

    @Override
    public VsdTask selectOne(QueryWrapper<VsdTask> queryWrapper) {
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<VsdTask> selectByPage(Page<VsdTask> page, QueryWrapper<VsdTask> queryWrapper) {
        IPage<VsdTask> vsdTaskPage = baseMapper.selectPage(page, queryWrapper);
        return vsdTaskPage.getRecords();
    }

    @Override
    public boolean updateParam(String serialnumber, String param) {
        List<VsdTask> vsdTaskList = baseMapper.selectList(new QueryWrapper<VsdTask>().eq("userserialnumber", serialnumber));
        if(vsdTaskList.isEmpty()){
            throw VideoExceptionUtil.getValidException("任务号不存在！");
        }else{
            vsdTaskList.forEach(p -> p.setParam(param));
        }
        return this.updateBatchById(vsdTaskList);
    }

    /***
     * @description: 根据任务类型更新detail
     * @param tbAnalysisDetail 子任务对象
     * @param tbAnalysisTask   主任务对象
     * @return: void
     */
    private void updateDetailByType(TbAnalysisDetail tbAnalysisDetail, TbAnalysisTask tbAnalysisTask) {
        if (TaskConstants.TASK_TYPE_ONLINE == tbAnalysisTask.getTaskType()) {
            tbAnalysisDetail.setAnalysisStatus(TaskConstants.OPERAT_TYPE_NO_DO);
        } else {
            tbAnalysisDetail = TbAnalysisDetailUtil.updateProgress(tbAnalysisDetail,
                    TbAnalysisDetailUtil.ANALYSIS, 0, tbAnalysisTask);
            tbAnalysisDetail.setAnalysisStatus(TaskConstants.OPERAT_TYPE_RUNNING);
        }
    }

    /***
     * @description: 更新子任务进度
     * @param tbAnalysisDetail 子任务对象
     * @param tbAnalysisTask   主任务对象
     * @return: void
     */
    private void updateDetailProgress(TbAnalysisDetail tbAnalysisDetail, TbAnalysisTask tbAnalysisTask) {
        //实时流任务初始化后状态直接变更为0，所以不可能进入此判断中
        VsdTask vsdTask = VsdTaskCache.getVsdTaskBySerialnumber(tbAnalysisDetail.getAnalysisId());
        Timestamp timestamp = DateUtil.now();
        if (vsdTask == null || TaskConstants.TASK_STATUS_FAILED == vsdTask.getStatus()) {
            tbAnalysisDetail = TbAnalysisDetailUtil.updateProgress(tbAnalysisDetail,
                    TbAnalysisDetailUtil.ANALYSIS, -1, tbAnalysisTask);
            tbAnalysisDetail.setAnalysisStatus(TaskConstants.OPERAT_TYPE_FAILED);
            String errMsg = StringUtils.isEmpty(nacosConfig.getAnalysisError()) ?
                    "分析status is 3." : nacosConfig.getAnalysisError();
            tbAnalysisDetail.setRemark(errMsg);
            tbAnalysisDetail.setFinishTime(timestamp);
        } else {
            tbAnalysisDetail = TbAnalysisDetailUtil.updateProgress(tbAnalysisDetail,
                    TbAnalysisDetailUtil.ANALYSIS, vsdTask.getProgress(), tbAnalysisTask);
            if (tbAnalysisDetail.getProgress() == 100) {
                tbAnalysisDetail.setAnalysisStatus(TaskConstants.OPERAT_TYPE_SUCCESS);
                tbAnalysisDetail.setFinishTime(timestamp);
            }
        }
        tbAnalysisDetail.setLastupdateTime(timestamp);
    }

    /***
     * @description: 获取vsdTask初始化对象
     * @param task 主任务对象
     * @param detail 子任务对象
     * @return: com.keensense.task.entity.VsdTask
     */
    private VsdTask insertVasTask(TbAnalysisTask task, TbAnalysisDetail detail) {
        JSONObject jsonObject = getParam(task, detail);
        JSONObject custom = Optional.ofNullable(jsonObject).map(p->p.getJSONObject("custom")).orElse(new JSONObject());
        String entryTime = detail.getEntryTime();
        custom.put("entryTime", entryTime);
        jsonObject.put("custom", custom);
        VsdTask vsdTask = new VsdTask();
        vsdTask.setSerialnumber(detail.getAnalysisId());
        vsdTask.setType(task.getAnalyType());
        vsdTask.setProgress(0);
        vsdTask.setRetryCount(0);
        vsdTask.setReserve("");
        vsdTask.setErrmsg("");
        vsdTask.setSlaveip(Optional.ofNullable(task.getRemark()).orElse(""));
        vsdTask.setParam(jsonObject.toString());
        vsdTask.setIsValid(TaskConstants.TASK_ISVALID_ON);
        vsdTask.setStatus(TaskConstants.TASK_STATUS_WAIT);
        vsdTask.setCreatetime(DateUtil.now());
        vsdTask.setUserserialnumber(task.getId());
        vsdTask.setEntrytime(Timestamp.valueOf(entryTime));
        vsdTask.setVersion(1);
        vsdTask.setTaskType(jsonObject.getInteger("taskType"));
        return vsdTask;
    }

    /***
     * @description: 转换task中param参数至vsdTask表
     * @param task 主任务对象
     * @param detail 子任务对象
     * @return: com.alibaba.fastjson.JSONObject
     */
    private JSONObject getParam(TbAnalysisTask task, TbAnalysisDetail detail) {
        final String entryTime = "entryTime";
        JSONObject jsonObject = JSON.parseObject(task.getAnalyParam());

        JSONObject param = jsonObject.getJSONObject("param");
        param.put("url", detail.getAnalysisUrl());

        String custom = "custom";
        JSONObject customObj = param.getJSONObject(custom);
        if (customObj == null) {
            customObj = new JSONObject();
            customObj.put(entryTime, DateUtil.formatDate(System.currentTimeMillis()));
            param.put(custom, custom);
        } else {
            if (StringUtils.isEmpty(customObj.getString(entryTime))) {
                customObj.put(entryTime, DateUtil.formatDate(System.currentTimeMillis()));
            }
        }
        return param;
    }

    /***
     * @description: 根据子任务查询主任务对象
     * @param tbAnalysisDetails 子任务对象
     * @return: Map
     */
    private Map<String, TbAnalysisTask> getTbAnalysisMap(List<TbAnalysisDetail> tbAnalysisDetails) {
        Set<String> taskIdSet = new HashSet<>(tbAnalysisDetails.size());
        for (TbAnalysisDetail tbAnalysisDetail : tbAnalysisDetails) {
            taskIdSet.add(tbAnalysisDetail.getTaskId());
        }
        List<TbAnalysisTask> tbAnalysisTasks = tbAnalysisTaskMapper.selectList(queryAnalysisTaskByIds(taskIdSet));
        if (!tbAnalysisTasks.isEmpty()) {
            Map<String, TbAnalysisTask> resultMap = new HashMap<>(tbAnalysisTasks.size());
            for (TbAnalysisTask tbAnalysisTask : tbAnalysisTasks) {
                resultMap.put(tbAnalysisTask.getId(), tbAnalysisTask);
            }
            return resultMap;
        }
        return new HashMap<>(0);
    }

    /***
     * @description: 查询分析中的任务
     * @param ids id集合
     * @return: com.baomidou.mybatisplus.core.conditions.Wrapper
     */
    private Wrapper<TbAnalysisTask> queryAnalysisTaskByIds(Set<String> ids) {
        return new QueryWrapper<TbAnalysisTask>().in("id", ids);
    }

}
