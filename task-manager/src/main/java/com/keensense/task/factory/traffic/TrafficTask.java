package com.keensense.task.factory.traffic;

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
 * @Description: 交通任务
 * @Author: wujw
 * @CreateDate: 2019/11/5 9:31
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class TrafficTask extends AbstractTaskManager {

    @Override
    public void insertTask(JSONObject paramJson, String serialnumber, String type, String url) {
        String entryTime = super.getEntryTime(paramJson);
        int taskType = paramJson.getIntValue("taskType");
        String param = TaskParamValidUtil.validString(paramJson, "param", 50000, false);

        insertTbAnalysisTask(paramJson, serialnumber, taskType, param);
        TbAnalysisDetail detail = super.initDefaultDetail(serialnumber, null);
        tbAnalysisDetailMapper.insert(detail);
        Timestamp createTime = new Timestamp(System.currentTimeMillis());
        VsdTask vsdTask = initVsdTask(param, serialnumber, createTime, Timestamp.valueOf(entryTime), TaskConstants.ANALY_TYPE_TRAFFIC, TaskConstants.TASK_STATUS_WAIT);
        int count = vsdTaskMapper.insert(vsdTask);
        if (count <= 0) {
            throw VideoExceptionUtil.getDbException(serialnumber + " insert vsdTask failed");
        }
    }

    /***
     * @description: 插入TbAnalysisTask对象
     * @param paramJson 请求参数
     * @param serialnumber 任务ID
     * @param videoType   视频类型
     * @return: int
     */
    private void insertTbAnalysisTask(JSONObject paramJson, String serialnumber, int videoType, String param) {
        String name = super.getName(paramJson);
        String cameraId = super.getCameraId(paramJson);
        String deviceId = super.getDeviceId(paramJson, false);
        TbAnalysisTask tbAnalysisTask = TbAnalysisTask.init(serialnumber, TaskConstants.ANALY_TYPE_TRAFFIC, 1,
                videoType, cameraId, deviceId, param);
        tbAnalysisTask.setName(name);
        int count = tbAnalysisTaskMapper.insert(tbAnalysisTask);
        if (count <= 0) {
            throw VideoExceptionUtil.getDbException(tbAnalysisTask.getId() + " insert TbAnalysisTask failed!");
        }
    }
}
