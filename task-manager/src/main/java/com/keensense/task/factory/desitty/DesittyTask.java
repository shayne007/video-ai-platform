package com.keensense.task.factory.desitty;

import com.alibaba.fastjson.JSONObject;
import com.keensense.task.constants.TaskConstants;
import com.keensense.task.entity.TbAnalysisDetail;
import com.keensense.task.entity.TbAnalysisTask;
import com.keensense.task.entity.VsdTask;
import com.keensense.task.factory.AbstractTaskManager;
import com.keensense.task.util.TaskParamValidUtil;
import com.keensense.task.util.VideoExceptionUtil;

import java.sql.Timestamp;

/**
 * @Description:
 * @Author: wujw
 * @CreateDate: 2019/9/17 16:20
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class DesittyTask extends AbstractTaskManager {

    @Override
    public void insertTask(JSONObject paramJson, String serialnumber, String type, String url) {
        int taskType = paramJson.getIntValue("taskType");
        this.insertTbAnalysisTask(paramJson, serialnumber, taskType);
        TbAnalysisDetail detail = super.initDefaultDetail(serialnumber, null);
        tbAnalysisDetailMapper.insert(detail);
        JSONObject taskJson = getParamMap(paramJson, url);
        Timestamp createTime = new Timestamp(System.currentTimeMillis());
        taskJson.put("taskType", taskType);
        VsdTask vsdTask = initVsdTask(taskJson.toString(), serialnumber, createTime, null, TaskConstants.ANALY_TYPE_DESITTY, TaskConstants.TASK_STATUS_WAIT);
        int count = vsdTaskMapper.insert(vsdTask);
        if (count <= 0) {
            throw VideoExceptionUtil.getDbException(serialnumber + " insert vsdTask failed");
        }
    }

    /***
     * @description: 获取param参数
     * @param paramJson 请求参数
     * @param url 视频路径
     * @return: com.alibaba.fastjson.JSONObject
     */
    private JSONObject getParamMap(JSONObject paramJson, String url) {
        JSONObject paramRoot = new JSONObject();

        JSONObject inputJson = new JSONObject(5);
        inputJson.put("PushFrameMaxWaitTime", TaskParamValidUtil.isPositiveInteger(paramJson, "pushFrameMaxWaitTime", 0, 0, 2000));
        inputJson.put("EnableDensityMapOutput", TaskParamValidUtil.getBoolean(paramJson, "enableDensityMapOutput", false, false));
        inputJson.put("DetectionScaleFactor", TaskParamValidUtil.getOutputDSFactor(paramJson, "detectionScaleFactor", 1));
        inputJson.put("DetectionFrameSkipInterval", TaskParamValidUtil.isPositiveInteger(paramJson, "detectionFrameSkipInterval", 0, 0, 10000));
        inputJson.put("HeatmapWeight", TaskParamValidUtil.getFloat(paramJson, "heatmapWeight", 4, 0f, 1f, 0.4f));
        paramRoot.put("inputJson", inputJson);

        JSONObject analysisCfg = new JSONObject(1);
        JSONObject udrSetting = TaskParamValidUtil.getInterestedByParam(paramJson);
        analysisCfg.put("udrSetting", udrSetting);
        paramRoot.put("analysisCfg", analysisCfg);

        paramRoot.put("startFrameIndex", 0);
        paramRoot.put("endFrameIndex", 0);
        paramRoot.put("url", url);
        return paramRoot;
    }

    /***
     * @description: 插入TbAnalysisTask对象
     * @param paramJson 请求参数
     * @param serialnumber 任务ID
     * @param videoType   视频类型
     * @return: int
     */
    private void insertTbAnalysisTask(JSONObject paramJson, String serialnumber, int videoType) {
        String name = TaskParamValidUtil.validString(paramJson, "name", 255, true);
        String cameraId = super.getCameraId(paramJson);
        String deviceId = super.getDeviceId(paramJson, false);
        TbAnalysisTask tbAnalysisTask = TbAnalysisTask.init(serialnumber, TaskConstants.ANALY_TYPE_DESITTY, 1,
                videoType, cameraId, deviceId, null);
        tbAnalysisTask.setName(name);
        int count = tbAnalysisTaskMapper.insert(tbAnalysisTask);
        if (count <= 0) {
            throw VideoExceptionUtil.getDbException(tbAnalysisTask.getId() + " insert TbAnalysisTask failed!");
        }
    }

}
