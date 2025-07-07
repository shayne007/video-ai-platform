package com.keensense.task.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.task.config.NacosConfig;
import com.keensense.task.constants.DeleteTaskConstants;
import com.keensense.task.constants.TaskConstants;
import com.keensense.task.entity.*;
import com.keensense.task.factory.AbstractTaskManager;
import com.keensense.task.factory.TaskFactory;
import com.keensense.task.mapper.TbAnalysisTaskMapper;
import com.keensense.task.service.IAnalysisTrackService;
import com.keensense.task.service.ITaskCleanLogService;
import com.keensense.task.service.ITbAnalysisTaskService;
import com.keensense.task.util.DateUtil;
import com.keensense.task.util.TaskParamValidUtil;
import com.keensense.task.util.VideoExceptionUtil;
import com.loocme.sys.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.*;
import java.util.*;

import static java.util.Comparator.comparing;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2019-05-06
 */
@Service
@Slf4j
public class TbAnalysisTaskServiceImpl extends ServiceImpl<TbAnalysisTaskMapper, TbAnalysisTask> implements ITbAnalysisTaskService {

    @Autowired
    private NacosConfig nacosConfig;

    @Autowired
    IAnalysisTrackService analysisTrackService;

    @Autowired
    ITaskCleanLogService taskCleanLogService;

    @Override
    public TbAnalysisTask getTbAnalysisTaskById(String id) {
        return baseMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertTask(JSONObject paramJson, String serialnumber, String type, String url) {
        AbstractTaskManager abstractTaskManager = TaskFactory.getTaskByUrl(type, url);
        abstractTaskManager.insertTask(paramJson, serialnumber, type, url);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTask(JSONObject paramJson, String serialnumber, String type, String url, Integer videoType) {
        AbstractTaskManager abstractTaskManager = TaskFactory.getTaskByType(type, videoType);
        abstractTaskManager.insertTask(paramJson, serialnumber, type, url);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stopTask(TbAnalysisTask tbAnalysisTask) {
        AbstractTaskManager abstractTaskManager = TaskFactory.getTaskByType(tbAnalysisTask);
        abstractTaskManager.stopTask(tbAnalysisTask);
    }

    @Override
    public void continueTask(TbAnalysisTask tbAnalysisTask) {
        AbstractTaskManager abstractTaskManager = TaskFactory.getTaskByType(tbAnalysisTask);
        abstractTaskManager.continueTask(tbAnalysisTask);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTask(TbAnalysisTask tbAnalysisTask, int optSource) {
        AbstractTaskManager abstractTaskManager = TaskFactory.getTaskByType(tbAnalysisTask);
        abstractTaskManager.deleteTask(tbAnalysisTask, optSource);
    }

    @Override
    @Transactional
    public void stopBatchTask(String[] ids) {
        TbAnalysisTask tbAnalysisTask;
        for(String id : ids){
            tbAnalysisTask = baseMapper.selectById(id);
            if(tbAnalysisTask == null || tbAnalysisTask.getStatus() == TaskConstants.TASK_DELETE_TURE){
                throw VideoExceptionUtil.getValidException(id+"任务不存在或者已删除!");
            }
            AbstractTaskManager abstractTaskManager = TaskFactory.getTaskByType(tbAnalysisTask);
            abstractTaskManager.stopTask(tbAnalysisTask);
        }
    }

    @Override
    @Transactional
    public void deleteBatchTask(String[] ids) {
        TbAnalysisTask tbAnalysisTask;
        for(String id : ids){
            tbAnalysisTask = baseMapper.selectById(id);
            if(tbAnalysisTask == null){
                throw VideoExceptionUtil.getValidException(id+" 任务不存在或者已删除!");
            }
            AbstractTaskManager abstractTaskManager = TaskFactory.getTaskByType(tbAnalysisTask);
            abstractTaskManager.deleteTask(tbAnalysisTask, DeleteTaskConstants.CLEAN_DATA_SOURCE_ACTIVE);
        }
    }

    @Override
    public void continueBatchTask(String[] ids) {
        TbAnalysisTask tbAnalysisTask;
        for(String id : ids){
            tbAnalysisTask = baseMapper.selectById(id);
            if(tbAnalysisTask == null){
                throw VideoExceptionUtil.getValidException(id+" 任务不存在或者已删除!");
            }
            AbstractTaskManager abstractTaskManager = TaskFactory.getTaskByType(tbAnalysisTask);
            abstractTaskManager.continueTask(tbAnalysisTask);
        }
    }

    @Override
    public JSONObject getTaskList(JSONObject paramObject, Page<TaskVO> page) {
        paramObject.put(TaskConstants.START_TIME, TaskParamValidUtil.validTime(paramObject, TaskConstants.START_TIME, false));
        paramObject.put(TaskConstants.END_TIME, TaskParamValidUtil.validTime(paramObject, TaskConstants.END_TIME, false));
        paramObject.put("statusSql", this.setSqlStatus(paramObject.getString("status")));
        String deviceIds = TaskParamValidUtil.validString(paramObject, "deviceIds", 5000, false);
        if(!StringUtils.isEmpty(deviceIds)){
            paramObject.put("deviceIdArr", deviceIds.split(","));
        }
        String cameraIds = TaskParamValidUtil.validString(paramObject, "cameraIds", 5000, false);
        if(!StringUtils.isEmpty(cameraIds)){
            paramObject.put("cameraIdArr", cameraIds.split(","));
        }
        page = baseMapper.selectListByParam(page, paramObject);
        List<TaskVO> list = page.getRecords();
        for (TaskVO taskVO : list) {
            taskVO.setStatus(TaskConstants.getStatus(taskVO.getIsvalid(), taskVO.getStatus()));
            taskVO.setIsvalid(null);
        }
        return this.getPageResult(list, page.getTotal());
    }

    @Override
    public Set<SearchTaskVO> getTaskForSearch(JSONObject paramObject) {
        Timestamp startTime = TaskParamValidUtil.validTime(paramObject, TaskConstants.START_TIME, false);
        if (startTime != null) {
            paramObject.put(TaskConstants.START_TIME, new Timestamp(startTime.getTime() - 12 * 60 * 60 * 1000));
        }
        paramObject.put(TaskConstants.END_TIME, TaskParamValidUtil.validTime(paramObject, TaskConstants.END_TIME, false));
        String serialnumbers = paramObject.getString("serialnumbers");
        if (!StringUtils.isEmpty(serialnumbers)) {
            paramObject.put("serialnumbers", serialnumbers.split(","));
        }
        String cameraIds = paramObject.getString("cameraIds");
        if (!StringUtils.isEmpty(cameraIds)) {
            paramObject.put("cameraIds", cameraIds.split(","));
        }
        List<SearchTaskEntity> resultList = baseMapper.getTaskForSearch(paramObject);
        if (resultList.isEmpty()) {
            return new HashSet<>(0);
        } else {
            Set<SearchTaskVO> resultSet = new HashSet<>(resultList.size());
            for (SearchTaskEntity searchTaskEntity : resultList) {
                if (!StringUtils.isEmpty(searchTaskEntity.getSerialnumber())) {
                    resultSet.add(getSearchTask(searchTaskEntity));
                }
            }
            return resultSet;
        }
    }

    @Override
    public JSONObject getVideoTaskList(JSONObject paramObject, Page<TbAnalysisTask> page) {
        QueryWrapper<TbAnalysisTask> wrapper = getVideoParam(paramObject);
        IPage<TbAnalysisTask> resultPage = baseMapper.selectPage(page, wrapper);
        List<TbAnalysisTask> taskList = resultPage.getRecords();
        if (taskList.isEmpty()) {
            return getPageResult(Collections.emptyList(), 0L);
        } else {
            String[] idArr = taskList.stream().map(TbAnalysisTask::getId).toArray(String[]::new);
            List<VideoTaskMap> videoTaskMaps = baseMapper.getVideoObjectTaskList(idArr);
            List<VideoTaskVO> list = getVideoTaskList(videoTaskMaps, idArr);
            return getPageResult(list, resultPage.getTotal());
        }
    }

    @Override
    public List<TbAnalysisTask> selectList(QueryWrapper<TbAnalysisTask> wrapper) {
        return baseMapper.selectList(wrapper);
    }

    @Override
    public TbAnalysisTask selectEarliestOfficeTask() {
        return baseMapper.selectEarliestOfficeTask();
    }

    @Override
    public List<TbAnalysisTask> selectOfficeForDelete(Timestamp endTime) {
        return baseMapper.selectOfficeForDelete(endTime);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void retryTask(TbAnalysisTask tbAnalysisTask) {
        AbstractTaskManager abstractTaskManager = TaskFactory.getTaskByType(tbAnalysisTask);
        abstractTaskManager.retryTask(tbAnalysisTask.getId());
    }

    @Override
    public void updateProgressByTracks() {
        //取tb_analysis_task表中没有删除、进度<100 的录像任务
        List<TbAnalysisTask> tbAnalysisTasks = selectList(new QueryWrapper<TbAnalysisTask>()
                .eq("task_type",TaskConstants.TASK_TYPE_VIDEO)
                .ne("status",TaskConstants.TASK_DELETE_TURE)
                .lt("progress",100));
        if(tbAnalysisTasks != null && tbAnalysisTasks.size() > 0){
            for(TbAnalysisTask tbAnalysisTask:tbAnalysisTasks){
                int newProgress = calcuProgress(tbAnalysisTask);
                //进度有变化时更新
                if(tbAnalysisTask.getProgress() != newProgress){
                    TbAnalysisTask newTask = new TbAnalysisTask();
                    newTask.setId(tbAnalysisTask.getId());
                    newTask.setProgress(newProgress);
                    updateById(newTask);
                }
            }
        }
    }

    @Override
    public int getVideoTaskProgress(JSONObject paramJson) {
        int progress = 0;
        String serialnumber = paramJson.getString("serialnumber");
        if(serialnumber == null){
            throw VideoExceptionUtil.getValidException("serialnumber为必传参数!");
        }

        TbAnalysisTask tbAnalysisTask = getTbAnalysisTaskById(serialnumber);
        if(tbAnalysisTask != null){
            progress = calcuProgress(tbAnalysisTask);
        }
        return progress;
    }

    @Override
    public int insertTaskCleanLog(TbAnalysisTask tbAnalysisTask,int optSource) {
        int result = 1;
        if (result > 0) {
            TaskCleanLog taskCleanLog = new TaskCleanLog(tbAnalysisTask.getId(), tbAnalysisTask.getId(),
                    tbAnalysisTask.getAnalyType(), tbAnalysisTask.getTaskType(), DateUtil.now(), optSource);
            result = taskCleanLogService.insert(taskCleanLog);
            if (result <= 0) {
                throw VideoExceptionUtil.getValidException("insert taskCleanLog  failed! id = " + tbAnalysisTask.getId());
            }
        }
        return result;
    }

    /**
     * 根据打点数据更新录像分析进度
     * @param tbAnalysisTask
     * @return
     */
    private int calcuProgress(TbAnalysisTask tbAnalysisTask){
        int progress = 0;
        int tracked = 0;
        int total = 0;
        String param = tbAnalysisTask.getAnalyParam();
        JSONObject jsonObject = JSON.parseObject(param);
        String startTime = jsonObject.getString("startTime");
        String endTime = jsonObject.getString("endTime");
        JSONObject requestParma = new JSONObject();
        requestParma.put("analysisTypes",jsonObject.getString("analysisTypes"));
        requestParma.put("cameraIds",jsonObject.getString("cameraId"));
        requestParma.put("startTime",startTime);
        requestParma.put("endTime",endTime);
        log.info(">>>>>>requestParam:{}",requestParma);
        try {
            JSONObject result = analysisTrackService .getTrackList(requestParma);
            startTime = startTime.replaceAll(":","").replaceAll("-","").replaceAll(" ","");
            endTime = endTime.replaceAll(":","").replaceAll("-","").replaceAll(" ","");
            Map<String,String> trackMap = getTrackMap(result);
            if(trackMap != null && !trackMap.isEmpty()){
                //根据起止时间，与打点的轨迹进行比对，判断是否已经分析
                String data = dealTrackData(startTime,endTime,trackMap);
                String [] array = data.split(",");
                tracked = Integer.parseInt(array[0]);
                total = Integer.parseInt(array[1]);
            }
            if(total > 0){
                progress = tracked*100/total;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return progress;
    }

    /**
     * 针对打点及传入的起始时间，统计打点数及时间总数，以统计分析进度
     * @param startTime
     * @param endTime
     * @param trackMap
     * @return
     * @throws Exception
     */
    private static String dealTrackData(String startTime, String endTime, Map<String, String> trackMap) throws Exception{

        int tracked = 0;
        int total = 0;
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        String start = startTime.substring(0,12);
        String end = endTime.substring(0,12);

        log.info(">>>>>>>>start:{},end:{}",start,end);
        //將时间转成 毫秒
        long startD = df.parse(start).getTime();
        long endD = df.parse(end).getTime();

        while (startD < endD){
            Date date = new Date(startD);
            String hourKey = df.format(date).substring(0,10);
            //假如该小时已经打点，则判断当前分钟的track_value是否标记成1,，假如是，则计数器+1
            if(trackMap.get(hourKey) != null){
                int min = Integer.parseInt(df.format(date).substring(10,12));
                String trackValue = trackMap.get(hourKey);
                if(trackValue.charAt(59-min) == '1'){
                    tracked ++;
                }
            }
            total ++;
            //一分钟统计一次
            startD = startD + 60000;
        }
        return tracked+","+total;
    }


    /**
     * 以yyyymmddhh为key，打点数据为value，组装成map，方便后续检索
     * @param result
     * @return
     */
    private Map<String,String> getTrackMap(JSONObject result) {
        JSONArray tracks = result.getJSONArray("tracks");
        Map<String,String> trackMap = new HashMap<>();
        if(tracks != null && tracks.size() > 0){
            JSONObject track = tracks.getJSONObject(0);
            JSONArray subTracks = track.getJSONArray("subTracks");
            if(subTracks != null && subTracks.size() > 0){
                List<CameraAnalysisTrackBase> analysisTrackBases = JSON.parseArray(subTracks.toString(),CameraAnalysisTrackBase.class);
                DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
                for(CameraAnalysisTrackBase cameraAnalysisTrack:analysisTrackBases){
                    String trackDay = cameraAnalysisTrack.getTrackDay().toString();
                    //对小时在0-9的数据前面补0
                    String trackHour = cameraAnalysisTrack.getTrackHour()<10 ? "0"+cameraAnalysisTrack.getTrackHour().intValue():cameraAnalysisTrack.getTrackHour().toString();
                    //以yyyymmddhh做key
                    String key = trackDay + trackHour;
                    //假如map中没有这个时间的轨迹，则新增；否则，则把结果汇总起来
                    if(trackMap.get(key) == null){
                        trackMap.put(key,cameraAnalysisTrack.getTrackValue());
                    }else{
                        String newValue =  analysisTrackService.calcuteNewTrackValueByOr(trackMap.get(key).toString(),cameraAnalysisTrack.getTrackValue());
                        trackMap.put(key,newValue);
                    }
                }
            }
        }
        return trackMap;
    }

    /***
     * @description: 根据查询结果封装搜图对象
     * @param searchTaskEntity 搜图任务查询结果
     * @return: com.keensense.task.entity.SearchTaskVO
     */
    private SearchTaskVO getSearchTask(SearchTaskEntity searchTaskEntity) {
        if (TaskConstants.TASK_TYPE_ONLINE == searchTaskEntity.getTaskType()) {
            return new SearchTaskVO(searchTaskEntity.getSerialnumber(), searchTaskEntity.getId());
        } else {
            return new SearchTaskVO(searchTaskEntity.getSerialnumber(), searchTaskEntity.getEntryTime(), searchTaskEntity.getId());
        }
    }

    /***
     * @description: 根据任务ID，对查询的任务结果进行处理
     * @param videoTaskList 查询的任务结果
     * @param idArr 任务ID数组
     * @return: com.keensense.task.entity.VideoTaskVO
     */
    private List<VideoTaskVO> getVideoTaskList(List<VideoTaskMap> videoTaskList, String[] idArr) {
        Map<String, List<VideoTaskMap>> videoMap = getVideoTaskMap(videoTaskList, idArr.length);

        List<VideoTaskVO> videoTaskVOList = new ArrayList<>(videoMap.size());
        List<VideoTaskMap> videoTaskMaps;
        for (String id : idArr) {
            videoTaskMaps = videoMap.get(id);
            if (videoTaskMaps != null) {
                VideoTaskVO videoTaskVo = getVideoTaskVO(videoTaskMaps.get(0));
                Integer mainProgress = videoTaskVo.getProgress();
                if (TaskConstants.TASK_TYPE_VIDEO == videoTaskMaps.get(0).getTaskType()) {
                    List<VideoSlice> videoSliceList = getVideoSlice(videoTaskMaps);
                    videoTaskVo.setSubTasks(videoSliceList);
                    computerProgress(videoTaskVo);
                    //假如已經开启了分析打点，则以分析打点的数据来统计进度
                    if(nacosConfig.getAnalysisTrackSwitch()){
                        videoTaskVo.setProgress(mainProgress);
                    }
                } else {
                    VideoTaskMap videoTaskMap = videoTaskMaps.get(0);
                    videoTaskVo.setProgress(Optional.ofNullable(videoTaskMap.getProgress()).orElse(0));
                    videoTaskVo.setStatus(Optional.ofNullable(videoTaskMap.getStatus()).orElse(0));
                    videoTaskVo.setSlaveip(Optional.ofNullable(videoTaskMap.getSlaveIp()).orElse(""));
                    if (TaskConstants.TASK_TYPE_SUMMARY == videoTaskMap.getTaskType() && TaskConstants.PROGRESS_MAX_VALUE == videoTaskMap.getProgress()) {
                        String baseSumaryUrl = "http://" + videoTaskVo.getSlaveip() + ":8082/summary_video/" + videoTaskVo.getSerialnumber();
                        videoTaskVo.setSummaryVideoUrl(baseSumaryUrl + ".avi");
                        videoTaskVo.setSummaryDatHttp(baseSumaryUrl + ".dat");
                    }
                }
                videoTaskVOList.add(videoTaskVo);
            }
        }
        return videoTaskVOList;
    }

    /***
     * @description: 将集合转换成MAP
     * @param videoTaskList 查询结果集合
     * @param size 集合长度
     * @return: java.util.Map<java.lang.String , java.util.List < com.keensense.task.entity.VideoTaskMap>>
     */
    private Map<String, List<VideoTaskMap>> getVideoTaskMap(List<VideoTaskMap> videoTaskList, int size) {
        Map<String, List<VideoTaskMap>> videoMap = new HashMap<>(size);
        for (VideoTaskMap videoTaskMap : videoTaskList) {
            if (videoMap.containsKey(videoTaskMap.getId())) {
                videoMap.get(videoTaskMap.getId()).add(videoTaskMap);
            } else {
                List<VideoTaskMap> list = new ArrayList<>();
                list.add(videoTaskMap);
                videoMap.put(videoTaskMap.getId(), list);
            }
        }
        return videoMap;
    }

    /***
     * @description: 获取返回对象
     * @param videoTaskMap 任务返回数据对象
     * @return: com.keensense.task.entity.VideoTaskVO
     */
    private VideoTaskVO getVideoTaskVO(VideoTaskMap videoTaskMap) {
        VideoTaskVO videoTaskVO = new VideoTaskVO();
        videoTaskVO.setSerialnumber(videoTaskMap.getId());
        videoTaskVO.setName(videoTaskMap.getName());
        videoTaskVO.setType(videoTaskMap.getAnalyType());
        videoTaskVO.setCreateTime(videoTaskMap.getCreateTime());
        videoTaskVO.setVideoUrl(getUrl(videoTaskMap.getAnalyParam()));
        videoTaskVO.setErrcode(videoTaskMap.getErrcode());
        videoTaskVO.setErrmsg(videoTaskMap.getErrmsg());
        videoTaskVO.setProgress(videoTaskMap.getMainProgress());
        return videoTaskVO;
    }

    /***
     * @description: 根据子片集合计算主片进度
     * @param videoTaskVO 返回展示对象
     * @return: void
     */
    private void computerProgress(VideoTaskVO videoTaskVO) {
        //是否完成
        boolean isEnd = true;
        //是否失败
        boolean isFailed = true;
        int sum = 0;
        for (VideoSlice videoSlice : videoTaskVO.getSubTasks()) {
            if (TaskConstants.PROGRESS_FAILED == videoSlice.getProgress()) {
                sum += TaskConstants.PROGRESS_MAX_VALUE;
            } else if (TaskConstants.PROGRESS_MAX_VALUE == videoSlice.getProgress()
                    || TaskConstants.PROGRESS_REUSE == videoSlice.getProgress()) {
                sum += TaskConstants.PROGRESS_MAX_VALUE;
                isFailed = false;
            } else {
                sum += videoSlice.getProgress();
                isEnd = false;
                isFailed = false;
            }
        }
        if (isFailed) {
            videoTaskVO.setStatus(TaskConstants.TASK_SHOW_STATUS_FAILED);
            videoTaskVO.setProgress(TaskConstants.PROGRESS_FAILED);
        } else if (isEnd) {
            videoTaskVO.setStatus(TaskConstants.TASK_SHOW_STATUS_SUCCESS);
            videoTaskVO.setProgress(TaskConstants.PROGRESS_MAX_VALUE);
        } else {
            videoTaskVO.setStatus(TaskConstants.TASK_SHOW_STATUS_RUNNING);
            videoTaskVO.setProgress(sum / videoTaskVO.getSubTasks().size());
        }
    }

    /***
     * @description: 对子片集合根据url进行重新排序
     * @param subList 子片集合
     * @return: void
     */
    private void sortList(List<VideoSlice> subList) {
        subList.sort(comparing(VideoSlice::getUrl));
    }

    /***
     * @description: 获取分片对象
     * @param videoTaskMaps 查询结果映射对象
     * @return: List<com.keensense.task.entity.VideoSlice>
     */
    private List<VideoSlice> getVideoSlice(List<VideoTaskMap> videoTaskMaps) {
        List<VideoSlice> videoSliceList = new ArrayList<>(videoTaskMaps.size());
        for (VideoTaskMap videoTaskMap : videoTaskMaps) {
            VideoSlice videoSlice = new VideoSlice();
            videoSlice.setStatus(videoTaskMap.getStatus());
            videoSlice.setSerialnumber(videoTaskMap.getAnalysisId());
            videoSlice.setUserserialnumber(videoTaskMap.getUserserialnumber());
            videoSlice.setTaskId(videoTaskMap.getDetailId());
            //录像流分析 analyId 和 id 相同则表示是录像流分析
            if (videoTaskMap.getDetailId().equals(videoTaskMap.getAnalysisId())) {
                videoSlice.setUrl(videoTaskMap.getAnalysisUrl());
            } else {
                //录像下载分析
                videoSlice.setUrl(videoTaskMap.getDownloadUrl());
            }
            videoSlice.setProgress(getProgress(videoTaskMap.getDetailProgress(), videoTaskMap.getVsdProgress()));
            videoSlice.setRemark(videoTaskMap.getRemark());
            videoSlice.setLastUpdateTime(videoTaskMap.getLastUpdateTime());
            videoSlice.setCreateTime(videoTaskMap.getDetailCreateTime());

            videoSlice.setErrcode(videoTaskMap.getErrcode());
            videoSlice.setErrmsg(videoTaskMap.getErrmsg());
            videoSliceList.add(videoSlice);
        }
        sortList(videoSliceList);
        return videoSliceList;
    }

    /***
     * @description: 获取录像分片进度
     * @param detailProgress 子任务中的进度
     * @param vsdProgress     vsd_task表中进度
     * @return: java.lang.Integer
     */
    private Integer getProgress(Integer detailProgress, Integer vsdProgress) {
        if (TaskConstants.PROGRESS_FAILED == detailProgress) {
            return detailProgress;
        } else if (TaskConstants.PROGRESS_REUSE == detailProgress) {
            if (vsdProgress == null) {
                return 0;
            } else {
                if(2 == nacosConfig.getAnalysisMethod()){
                    return 50 + vsdProgress/2;
                }else{
                    return vsdProgress;
                }
            }
        } else {
            if (vsdProgress == null) {
                return detailProgress;
            } else {
                if(2 == nacosConfig.getAnalysisMethod()){
                    return 50 + vsdProgress/2;
                }else{
                    return vsdProgress;
                }
            }
        }
    }

    /***
     * @description: 从配置参数中获取url
     * @param analyParam 配置参数
     * @return: java.lang.String
     */
    private String getUrl(String analyParam) {
        if (!StringUtils.isEmpty(analyParam)) {
            JSONObject jsonObject = JSON.parseObject(analyParam);
            JSONObject param = jsonObject.getJSONObject("param");
            if (param != null) {
                return param.getString("url");
            }
        }
        return null;
    }

    /***
     * @description: 获取查询参数
     * @param paramObject 请求参数
     * @return: com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<com.keensense.task.entity.TbAnalysisTask>
     */
    private QueryWrapper<TbAnalysisTask> getVideoParam(JSONObject paramObject) {
        QueryWrapper<TbAnalysisTask> wrapper = new QueryWrapper<>();
        String serialnumber = TaskParamValidUtil.validString(paramObject, "serialnumber", 1000, false);
        if (!StringUtils.isEmpty(serialnumber)) {
            String[] serialnumberArr = serialnumber.split(",");
            wrapper.in("id", serialnumberArr);
        }
        String type = TaskParamValidUtil.validAnalyType(paramObject);
        if (!StringUtils.isEmpty(type)) {
            wrapper.eq("analy_type", type);
        }
        String userId = paramObject.getString("userId");
        if (!StringUtils.isEmpty(userId)) {
            wrapper.eq("create_userid", userId);
        }

        String name = paramObject.getString("containName");
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name);
        }
        wrapper.ne(TaskConstants.STATUS, TaskConstants.TASK_DELETE_TURE);

        String createTime = "create_time";
        Timestamp startTime = TaskParamValidUtil.validTime(paramObject, TaskConstants.START_TIME, false);
        if (!StringUtils.isEmpty(startTime)) {
            wrapper.ge(createTime, startTime);
        }
        Timestamp endTime = TaskParamValidUtil.validTime(paramObject, TaskConstants.END_TIME, false);
        if (!StringUtils.isEmpty(endTime)) {
            wrapper.le(createTime, endTime);
        }
        wrapper.orderByDesc(createTime);
        return wrapper;
    }

    /***
     * @description: 根据展示状态转换成查询状态语句，语句中的v.和xml文件中的别名匹配
     * @param statusStr 状态
     * @return: java.lang.String
     */
    private String setSqlStatus(String statusStr) {
        if (StringUtil.isNotNull(statusStr)) {
            if (!StringUtil.isInteger(statusStr)) {
                throw VideoExceptionUtil.getValidException("status必须是整数!");
            }
            StringBuilder sb = new StringBuilder();
            int status = Integer.parseInt(statusStr);
            if (status == TaskConstants.TASK_SHOW_STATUS_WAIT || status == TaskConstants.TASK_SHOW_STATUS_RUNNING) {
                sb.append(" AND").append(getStatus(status, TaskConstants.TASK_ISVALID_ON));
            } else if (status == TaskConstants.TASK_SHOW_STATUS_STOPPING) {
                sb.append(" AND").append(getStatus(TaskConstants.TASK_STATUS_RUNNING, TaskConstants.TASK_ISVALID_OFF));
            } else if (status == TaskConstants.TASK_SHOW_STATUS_FAILED) {
                sb.append(" AND (( ").append((getStatus(TaskConstants.TASK_STATUS_WAIT, TaskConstants.TASK_ISVALID_OFF))).append(")");
                sb.append(" OR v.`status` = ").append(status).append(")");
            } else {
                sb.append(" AND v.`status` = ").append(status);
            }
            return sb.toString();
        }
        return null;
    }

    /***
     * @description:
     * @param status    任务状态
     * @param validStatus 是否有效
     * @return: java.lang.StringBuilder
     */
    private StringBuilder getStatus(int status, int validStatus) {
        StringBuilder sb = new StringBuilder();
        sb.append(" v.`status` = ").append(status).append(" AND v.isvalid = ").append(validStatus);
        return sb;
    }

    /***
     * @description: 返回查询列表结果
     * @param list 列表集合
     * @return: com.alibaba.fastjson.JSONObject
     */
    private JSONObject getPageResult(List list, long total) {
        JSONObject result = new JSONObject(4);
        result.put("totalcount", total);
        result.put("tasks", list);
        return result;
    }
}
