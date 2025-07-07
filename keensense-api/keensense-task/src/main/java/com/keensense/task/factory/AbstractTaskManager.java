package com.keensense.task.factory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.common.config.SpringContext;
import com.keensense.task.config.NacosConfig;
import com.keensense.task.constants.TaskConstants;
import com.keensense.task.entity.TbAnalysisDetail;
import com.keensense.task.entity.TbAnalysisTask;
import com.keensense.task.entity.VsdTask;
import com.keensense.task.mapper.TbAnalysisDetailMapper;
import com.keensense.task.mapper.TbAnalysisTaskMapper;
import com.keensense.task.mapper.VsdTaskMapper;
import com.keensense.task.service.ITaskCleanLogService;
import com.keensense.task.util.DateUtil;
import com.keensense.task.util.TaskParamValidUtil;
import com.keensense.task.util.ValidUtil;
import com.keensense.task.util.VideoExceptionUtil;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;


/**
 * @Description: 任务管理抽象类
 * @Author: wujw
 * @CreateDate: 2019/5/9 15:50
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public abstract class AbstractTaskManager {

    protected TbAnalysisTaskMapper tbAnalysisTaskMapper = SpringContext.getBean(TbAnalysisTaskMapper.class);

    protected TbAnalysisDetailMapper tbAnalysisDetailMapper = SpringContext.getBean(TbAnalysisDetailMapper.class);

    protected VsdTaskMapper vsdTaskMapper = SpringContext.getBean(VsdTaskMapper.class);

    protected ITaskCleanLogService taskCleanLogService = SpringContext.getBean(ITaskCleanLogService.class);

    protected static NacosConfig nacosConfig = SpringContext.getBean(NacosConfig.class);

    /***
     * 添加任务
     * @param paramJson 请求参数
     * @param serialnumber 任务ID
     * @param type 任务类型
     * @param url 视频或流地址
     * @return: int
     */
    public abstract void insertTask(JSONObject paramJson, String serialnumber, String type, String url);

    /****
     * @description: 停止任务
     * @param tbAnalysisTask 任务对象
     * @return: int
     */
    public void stopTask(TbAnalysisTask tbAnalysisTask) {
        int count = tbAnalysisTaskMapper.stopTask(tbAnalysisTask.getId());
        if (count <= 0) {
            throw VideoExceptionUtil.getDbException("任务停止失败！");
        }
    }

    /***
     * @description: 继续任务
     * @param tbAnalysisTask 任务对象
     * @return: void
     */
    public void continueTask(TbAnalysisTask tbAnalysisTask) {
        List<VsdTask> list = vsdTaskMapper.selectList(new QueryWrapper<VsdTask>().eq("userserialnumber", tbAnalysisTask.getId()));
        list.forEach(p -> {
            if (TaskConstants.TASK_ISVALID_ON == p.getIsValid()) {
                throw VideoExceptionUtil.getValidException(tbAnalysisTask.getId() + "任务已启动！");
            } else if (TaskConstants.TASK_STATUS_RUNNING == p.getStatus()) {
                throw VideoExceptionUtil.getValidException(tbAnalysisTask.getId() + "任务停止中，请稍后再试！");
            }
        });
        int count = tbAnalysisTaskMapper.continueTask(tbAnalysisTask.getId());
        if (count <= 0) {
            throw VideoExceptionUtil.getDbException("任务更新失败！");
        }
    }

    /***
     * @description: 删除任务
     * @param tbAnalysisTask 任务
     * @param optSource 操作类型
     * @return: void
     */
    public int deleteTask(TbAnalysisTask tbAnalysisTask, int optSource) {
        int result = deleteTask(tbAnalysisTask.getId());
        return result;
    }

    /**
     * 删除任务
     *
     * @param id
     * @return
     */
    public int deleteTask(String id) {
        TbAnalysisTask tbAnalysisTask = new TbAnalysisTask();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        tbAnalysisTask.setStatus(TaskConstants.TASK_STATUS_SUCCESS);
        tbAnalysisTask.setLastupdateTime(timestamp);
        tbAnalysisTask.setId(id);
        tbAnalysisTaskMapper.updateById(tbAnalysisTask);
        int result = vsdTaskMapper.deleteVsdTask(id);
        return result;
    }

    /****
     * @description: 任务重试
     * @param serialnumber 任务ID
     */
    public void retryTask(String serialnumber) {
        tbAnalysisDetailMapper.updateResetStatus(serialnumber);
        vsdTaskMapper.updateResetStatus(serialnumber);
    }

    /***
     * @description: 插入TbAnalysisTask对象
     * @param paramJson 请求参数
     * @param serialnumber 任务ID
     * @param taskType  任务类型
     * @param videoType   视频类型
     * @param sliceNumber 分片数量
     * @return: int
     */
    protected void insertTbAnalysisTask(JSONObject paramJson, String serialnumber, String taskType, int videoType, int sliceNumber) {
        String cameraId = getCameraId(paramJson);
        String deviceId = getDeviceId(paramJson, false);
        String userId = getUserId(paramJson);
        String name = getName(paramJson);
        String slaveIp = getSlaveIp(paramJson, false);
        TbAnalysisTask tbAnalysisTask = TbAnalysisTask.init(serialnumber, taskType, sliceNumber, videoType, cameraId,
                deviceId, paramJson.toString());
        tbAnalysisTask.setName(name);
        tbAnalysisTask.setRemark(slaveIp);
        tbAnalysisTask.setCreateUserid(userId);
        int count = tbAnalysisTaskMapper.insert(tbAnalysisTask);
        if (count <= 0) {
            throw VideoExceptionUtil.getDbException(tbAnalysisTask.getId() + " insert TbAnalysisTask failed!");
        }
    }

    /****
     * @description: 初始化TbAnalysisDetail对象
     * @param serialnumber 任务ID
     * @return: com.keensense.task.entity.TbAnalysisDetail
     */
    protected TbAnalysisDetail initTbAnalysisDetail(String serialnumber) {
        TbAnalysisDetail detail = new TbAnalysisDetail();
        detail.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        detail.setTaskId(serialnumber);
        detail.setProgress(0);
        detail.setCreateTime(DateUtil.now());
        return detail;
    }

    /****
     * @description: 初始化TbAnalysisDetail对象(仅入库 ， 不走定时任务生成vsd_task数据)
     * @param serialnumber 任务ID
     * @param url 路径
     * @return: com.keensense.task.entity.TbAnalysisDetail
     */
    protected TbAnalysisDetail initDefaultDetail(String serialnumber, String url) {
        TbAnalysisDetail detail = initTbAnalysisDetail(serialnumber);
        detail.setAnalysisId(serialnumber);
        detail.setDownloadUrl(url);
        detail.setAnalysisStatus(0);
        return detail;
    }

    /***
     * @description: 初始化VsdTask对象
     * @param param 请求参数
     * @param serialnumber 任务ID
     * @param createTime 时间
     * @param entryTime 矫正时间
     * @param type 分析类型
     * @param status 初始化任务状态
     * @return: com.keensense.task.entity.VsdTask
     */
    protected VsdTask initVsdTask(String param, String serialnumber, Timestamp createTime, Timestamp entryTime,
                                  String type, int status) {
        JSONObject jsonObject = JSONObject.parseObject(param);
        Integer taskType = 1;
        if (jsonObject.getInteger("taskType") != null) {
            taskType = jsonObject.getInteger("taskType").intValue();
        }
        return initVsdTask(param, serialnumber, createTime, entryTime, type, status, TaskConstants.TASK_DEFAULT_PRIORITY, taskType);
    }

    /**
     * @param param        请求参数
     * @param serialnumber 任务ID
     * @param createTime   时间
     * @param entryTime    矫正时间
     * @param type         分析类型
     * @param status       初始化任务状态
     * @param priority     优先级
     * @param taskType     任务类型 :1/实时任务;2/离线视频;3/联网录像;4/浓缩
     * @return
     */
    protected VsdTask initVsdTask(String param, String serialnumber, Timestamp createTime, Timestamp entryTime,
                                  String type, int status, Integer priority, Integer taskType) {
        VsdTask vsdTask = new VsdTask();
        vsdTask.setSerialnumber(serialnumber).setUserserialnumber(serialnumber);
        vsdTask.setIsValid(TaskConstants.TASK_ISVALID_ON);
        vsdTask.setCreatetime(createTime);
        vsdTask.setEntrytime(entryTime);
        vsdTask.setParam(param);
        vsdTask.setProgress(0);
        vsdTask.setRetryCount(0);
        vsdTask.setStatus(status);
        vsdTask.setType(type);
        vsdTask.setVersion(1);
        vsdTask.setPriority(priority);
        vsdTask.setTaskType(taskType);
        return vsdTask;
    }

    /***
     * @description: 初始化param参数
     * @param paramJson 请求参数
     * @param url 路径
     * @param entryTime 矫正时间
     * @return: com.alibaba.fastjson.JSONObject
     */
    protected JSONObject initParamRoot(JSONObject paramJson, String serialnumber, String url, String entryTime) {
        JSONObject paramRoot = new JSONObject();
        paramRoot.put("url", url);
        JSONObject customMap = getCustomMap(paramJson, entryTime, serialnumber);
        paramRoot.put("custom", customMap);
        int startFrameIndex = TaskParamValidUtil.isPositiveInteger(paramJson, "startFrameIndex", 0, 0, Integer.MAX_VALUE);
        paramRoot.put("startFrameIndex", startFrameIndex);
        int endFrameIndex = TaskParamValidUtil.isPositiveInteger(paramJson, "endFrameIndex", 0, 0, Integer.MAX_VALUE);
        paramRoot.put("endFrameIndex", endFrameIndex);
        return paramRoot;
    }

    /***
     * @description: 获取custom参数对象
     * @param paramJson 请求参数
     * @param entryTime 校准时间
     * @param serialnumber 任务号
     * @return: com.alibaba.fastjson.JSONObject
     */
    protected JSONObject getCustomMap(JSONObject paramJson, String entryTime, String serialnumber) {
        String cameraIdKey = "cameraId";
        JSONObject customMap = new JSONObject(2);
        customMap.put("entryTime", entryTime);
        customMap.put("serialnumber", serialnumber);
        customMap.put(cameraIdKey, paramJson.getString(cameraIdKey));
        customMap.put("analysisTypes", paramJson.getString("analysisTypes"));
        return customMap;
    }

    /***
     * @description: 获取analysisCfgMap参数
     * @param paramJson 请求参数
     * @return: com.alibaba.fastjson.JSONObject
     */
    protected JSONObject getAnalysisCfgMap(JSONObject paramJson) {
        JSONObject analysisCfgMap = new JSONObject();

        int objMinSize = this.getObjMinSize(paramJson);
        analysisCfgMap.put(TaskConstants.ANALYSIS_CFG_OBJMINSIZE, objMinSize);

        int objMaxSize = this.getObjMaxSize(paramJson);
        analysisCfgMap.put(TaskConstants.ANALYSIS_CFG_OBJMAXSIZE, objMaxSize);

        int scene = this.getScene(paramJson);
        analysisCfgMap.put(TaskConstants.ANALYSIS_CFG_SCENE, scene);

        int sensitivity = this.getSensitivity(paramJson);
        analysisCfgMap.put(TaskConstants.ANALYSIS_CFG_SENSITIVITY, sensitivity);

        int outputDSFactor = this.getOutputDSFactor(paramJson);
        analysisCfgMap.put(TaskConstants.ANALYSIS_CFG_OUTPUTDSFACTOR, outputDSFactor);

        int objMinTimeInMs = this.getObjMinTimeInMs(paramJson);
        analysisCfgMap.put(TaskConstants.ANALYSIS_CFG_OBJMINTIMEINMS, objMinTimeInMs);

        analysisCfgMap.put("enableIndependentFaceSnap", TaskParamValidUtil.getBoolean(paramJson, "enableIndependentFaceSnap", false, false));
        analysisCfgMap.put("enableBikeToHuman", TaskParamValidUtil.getBoolean(paramJson, "enableBikeToHuman", false, false));

        //是否开启人员检测 默认为true
        analysisCfgMap.put("enableHumanDetection", TaskParamValidUtil.getBoolean(paramJson, "enableHumanDetection", true, false));
        //是否开启车辆检测 默认为true
        analysisCfgMap.put("enableVehicleDetection", TaskParamValidUtil.getBoolean(paramJson, "enableVehicleDetection", true, false));
        //是否开启骑行检测 默认为true
        analysisCfgMap.put("enableBikeDetection", TaskParamValidUtil.getBoolean(paramJson, "enableBikeDetection", true, false));
        //是否开启人脸检测 默认为true
        analysisCfgMap.put("enableFaceDetection", TaskParamValidUtil.getBoolean(paramJson, "enableFaceDetection", true, false));

        JSONObject udrSetting = TaskParamValidUtil.getInterestedByParam(paramJson);
        analysisCfgMap.put("udrSetting", udrSetting);

        analysisCfgMap.put("eventConfig", getTripWires(paramJson));
        return analysisCfgMap;
    }

    /***
     * @description: 获取跨线参数
     * @param paramJson 请求参数
     * @return: com.alibaba.fastjson.JSONObject
     */
    public static JSONObject getTripWires(JSONObject paramJson) {
        JSONArray tripWires = TaskParamValidUtil.getTripWires(paramJson.getJSONArray("tripWires"));
        if (!tripWires.isEmpty()) {
            JSONObject eventConfig = new JSONObject(5);
            eventConfig.put("crowdDetThreshold", -1);
            eventConfig.put("trafficJamThreshold", -1);
            eventConfig.put("leftOverItemSensitivity", 0);
            eventConfig.put("leftOverItemDuration", 0);

            JSONObject tripwiresSetting = new JSONObject(3);
            tripwiresSetting.put("wireRelationship", 0);
            tripwiresSetting.put("tripwireNum", tripWires.size());
            tripwiresSetting.put("tripwires", tripWires);

            eventConfig.put("tripwiresSetting", tripwiresSetting);
            return eventConfig;
        } else {
            return null;
        }
    }

    /***
     * @description: 获取校准时间
     * @param paramJson 请求参数
     * @return: java.lang.String
     */
    protected String getEntryTime(JSONObject paramJson) {
        String entryTimeKey = "entryTime";
        String entryTime = paramJson.getString(entryTimeKey);
        if (!StringUtils.isEmpty(entryTime)) {
            if (!ValidUtil.isTimeLegal(entryTime)) {
                throw VideoExceptionUtil.getValidException("矫正时间格式不正确,格式为[yyyy-MM-dd HH:mm:ss]");
            }
            paramJson.remove(entryTimeKey);
            return entryTime;
        } else {
            return DateUtil.getFirstSecondStringToday();
        }
    }

    /***
     * @description: 获取cameraId
     * @param paramJson 请求参数
     * @return: java.lang.String
     */
    protected String getCameraId(JSONObject paramJson) {
        String cameraId = TaskParamValidUtil.validString(paramJson, "cameraId", 20, false);
        if (StringUtils.isEmpty(cameraId)) {
            cameraId = "1";
        }
        return cameraId;
    }

    /***
     * @description: 获取deviceId
     * @param paramJson 请求参数
     * @return: java.lang.String
     */
    protected String getDeviceId(JSONObject paramJson, boolean required) {
        return TaskParamValidUtil.validString(paramJson, "deviceId", 48, required);
    }

    /***
     * @description: 获取userId
     * @param paramJson 请求参数
     * @return: java.lang.String
     */
    protected String getUserId(JSONObject paramJson) {
        return TaskParamValidUtil.validString(paramJson, "userId", 32, false);
    }

    /***
     * @description: 获取cameraId
     * @param paramJson 请求参数
     * @return: java.lang.String
     */
    protected String getName(JSONObject paramJson) {
        return TaskParamValidUtil.validString(paramJson, "name", 255, false);
    }

    /***
     * @description: 获取 scene
     * @param paramJson 请求参数
     * @return: java.lang.String
     */
    private int getScene(JSONObject paramJson) {
        int scene = TaskParamValidUtil.getScene(paramJson, TaskConstants.ANALYSIS_CFG_SCENE, 0);
        paramJson.remove(TaskConstants.ANALYSIS_CFG_SCENE);
        return scene;
    }

    /***
     * @description: 获取 objMinSize
     * @param paramJson 请求参数
     * @return: java.lang.String
     */
    private int getObjMinSize(JSONObject paramJson) {
        int objMinSize = TaskParamValidUtil.getObjMinSize(paramJson, TaskConstants.ANALYSIS_CFG_OBJMINSIZE, 0);
        paramJson.remove(TaskConstants.ANALYSIS_CFG_OBJMINSIZE);
        return objMinSize;
    }

    /***
     * @description: 获取 objMaxSize
     * @param paramJson 请求参数
     * @return: java.lang.String
     */
    private int getObjMaxSize(JSONObject paramJson) {
        int objMaxSize = TaskParamValidUtil.getObjMaxSize(paramJson, TaskConstants.ANALYSIS_CFG_OBJMAXSIZE, 0);
        paramJson.remove(TaskConstants.ANALYSIS_CFG_OBJMAXSIZE);
        return objMaxSize;
    }

    /***
     * @description: 获取 sensitivity
     * @param paramJson 请求参数
     * @return: java.lang.String
     */
    private int getSensitivity(JSONObject paramJson) {
        int sensitivity = TaskParamValidUtil.getSensitivity(paramJson, TaskConstants.ANALYSIS_CFG_SENSITIVITY, 0);
        paramJson.remove(TaskConstants.ANALYSIS_CFG_SENSITIVITY);
        return sensitivity;
    }

    /***
     * @description: 获取slaveIp
     * @param paramJson 请求参数
     * @return: java.lang.String
     */
    protected String getSlaveIp(JSONObject paramJson, boolean required) {
        return TaskParamValidUtil.validString(paramJson, "slaveIp", 64, required);
    }

    /***
     * @description: 获取 sensitivity
     * @param paramJson 请求参数
     * @return: java.lang.String
     */
    private int getOutputDSFactor(JSONObject paramJson) {
        int outputDSFactor = TaskParamValidUtil.getOutputDSFactor(paramJson, TaskConstants.ANALYSIS_CFG_OUTPUTDSFACTOR, 2);
        paramJson.remove(TaskConstants.ANALYSIS_CFG_OUTPUTDSFACTOR);
        return outputDSFactor;
    }

    /***
     * @description: 获取 objMinTimeInMs
     * @param paramJson 请求参数
     * @return: java.lang.String
     */
    private int getObjMinTimeInMs(JSONObject paramJson) {
        int objMinTimeInMs = TaskParamValidUtil.isPositiveInteger(paramJson, TaskConstants.ANALYSIS_CFG_OBJMINTIMEINMS, 500, 0, 10000);
        paramJson.remove(TaskConstants.ANALYSIS_CFG_OBJMINTIMEINMS);
        return objMinTimeInMs;
    }
}
