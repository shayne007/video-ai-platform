package com.keensense.task.factory.picture;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.task.constants.CaptureApis;
import com.keensense.task.constants.TaskConstants;
import com.keensense.task.entity.TbAnalysisDetail;
import com.keensense.task.entity.TbAnalysisTask;
import com.keensense.task.entity.VsdTask;
import com.keensense.task.factory.AbstractTaskManager;
import com.keensense.task.util.DateUtil;
import com.keensense.task.util.TaskParamValidUtil;
import com.keensense.task.util.VideoExceptionUtil;

import java.sql.Timestamp;

/**
 * @Description: 抓拍机任务子类
 * @Author: wujw
 * @CreateDate: 2019/5/9 16:43
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class PictureTask extends AbstractTaskManager {

    @Override
    public void insertTask(JSONObject requestJson, String serialnumber, String type, String url) {
        JSONObject paramJson = requestJson.getJSONObject("param");
        String ip = TaskParamValidUtil.getIp(paramJson);
        String port = TaskParamValidUtil.getPort(paramJson);
        String userName = TaskParamValidUtil.validString(paramJson, "username", 256, true);
        String password = TaskParamValidUtil.validString(paramJson, "password", 256, true);
        String deviceId = super.getDeviceId(paramJson, true);
        String cameraId = super.getCameraId(requestJson);

        Timestamp time = new Timestamp(System.currentTimeMillis());
        String entryTime = DateUtil.formatDate(time.getTime());
        paramJson.put("custom", getCustomMap(paramJson, entryTime,serialnumber));
        this.insertTbAnalysisTask(paramJson, serialnumber, TaskConstants.ANALY_TYPE_PICTURE,
                TaskConstants.TASK_TYPE_ONLINE, deviceId, cameraId);
        TbAnalysisDetail detail = super.initDefaultDetail(serialnumber, url);
        tbAnalysisDetailMapper.insert(detail);
        VsdTask vsdTask = initVsdTask(paramJson.toString(), serialnumber, time, time, TaskConstants.ANALY_TYPE_PICTURE,TaskConstants.TASK_STATUS_RUNNING);
        int count = vsdTaskMapper.insert(vsdTask);
        if (count > 0) {
            startCapture(serialnumber, ip, port, userName, password, deviceId, cameraId);
        } else {
            throw VideoExceptionUtil.getDbException(serialnumber + " insert vsdTask failed");
        }
    }

    @Override
    public void stopTask(TbAnalysisTask tbAnalysisTask) {
        int count = tbAnalysisTaskMapper.stopPictureTask(tbAnalysisTask.getId());
        if (count <= 0) {
            throw VideoExceptionUtil.getDbException("任务停止失败！");
        }
        stopCapture(tbAnalysisTask.getId());
    }

    @Override
    public int deleteTask(TbAnalysisTask tbAnalysisTask, int optSource) {
        int count = super.deleteTask(tbAnalysisTask, optSource);
        stopCapture(tbAnalysisTask.getId());
        return count;
    }

    @Override
    public void continueTask(TbAnalysisTask tbAnalysisTask) {
        int count = tbAnalysisTaskMapper.continuePictureTask(tbAnalysisTask.getId());
        if (count <= 0) {
            throw VideoExceptionUtil.getDbException("任务更新失败！");
        }else{
            JSONObject param = getVsdTaskParam(tbAnalysisTask.getId());
            JSONObject postResult = CaptureApis.startCapture(tbAnalysisTask.getId(), param.getString("ip"),
                    param.getString("port"), param.getString("username"), param.getString("password"),
                    tbAnalysisTask.getDeviceId(), tbAnalysisTask.getCameraId());
            String statusCode = postResult.getString(CaptureApis.STATUS_CODE);
            if (!CaptureApis.isSuccess(statusCode)) {
                throw VideoExceptionUtil.getDbException(postResult.getString("message"));
            }
        }
    }

    /***
     * @description: 启动抓拍机任务
     * @param serialnumber 任务ID
     * @param ip    抓拍机IP
     * @param port  抓拍机端口
     * @param userName  抓拍机用户名
     * @param password  抓拍机密码
     * @param deviceId  设备ID
     * @param cameraId  点位ID
     * @return: void
     */
    private void startCapture(String serialnumber, String ip, String port, String userName, String password, String deviceId, String cameraId) {
        JSONObject postResult = CaptureApis.startCapture(serialnumber, ip, port, userName, password, deviceId, cameraId);
        String statusCode = postResult.getString(CaptureApis.STATUS_CODE);
        if (!CaptureApis.isSuccess(statusCode)) {
            throw VideoExceptionUtil.getDbException(postResult.getString("message"));
        }
    }

    /***
     * @description: 停止抓拍机任务
     * @param id 任务ID
     * @return: void
     */
    private void stopCapture(String id) {
        JSONObject param = getVsdTaskParam(id);
        JSONObject postResult = CaptureApis.stopCapture(id, param.getString("deviceId"));
        String statusCode = postResult.getString(CaptureApis.STATUS_CODE);
        if (!CaptureApis.isSuccess(statusCode)) {
            throw VideoExceptionUtil.getDbException(postResult.getString("message"));
        }
    }
    
    /***
     * @description: 获取抓拍机配置参数
     * @param taskId  任务ID
     * @return: com.alibaba.fastjson.JSONObject
     */
    private JSONObject getVsdTaskParam(String taskId){
        VsdTask vsdTask = vsdTaskMapper.selectOne(new QueryWrapper<VsdTask>().eq("serialnumber", taskId));
        if(vsdTask == null){
            throw VideoExceptionUtil.getDbException("任务不存在!");
        }
        return JSON.parseObject(vsdTask.getParam());
    }

    /***
     * @description: 插入TbAnalysisTask对象
     * @param paramJson 请求参数
     * @param serialnumber 任务ID
     * @param taskType  任务类型
     * @param videoType   视频类型
     * @return: int
     */
    private void insertTbAnalysisTask(JSONObject paramJson, String serialnumber, String taskType, int videoType,
                                      String deviceId, String cameraId) {
        String userId = super.getUserId(paramJson);
        String name = super.getName(paramJson);
        TbAnalysisTask tbAnalysisTask = TbAnalysisTask.init(serialnumber, taskType, 1, videoType, cameraId,
                deviceId, null);
        tbAnalysisTask.setName(name);
        tbAnalysisTask.setCreateUserid(userId);
        int count = tbAnalysisTaskMapper.insert(tbAnalysisTask);
        if (count <= 0) {
            throw VideoExceptionUtil.getDbException(tbAnalysisTask.getId() + " insert TbAnalysisTask failed!");
        }
    }

}
