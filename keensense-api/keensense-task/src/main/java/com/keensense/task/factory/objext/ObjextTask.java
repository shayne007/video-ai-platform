package com.keensense.task.factory.objext;

import com.alibaba.fastjson.JSONObject;
import com.keensense.task.constants.ObjextTaskConstants;
import com.keensense.task.constants.TaskConstants;
import com.keensense.task.entity.TbAnalysisDetail;
import com.keensense.task.entity.VsdTask;
import com.keensense.task.factory.AbstractTaskManager;
import com.keensense.task.util.TaskParamValidUtil;
import com.keensense.task.util.ValidUtil;
import com.keensense.task.util.VideoExceptionUtil;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;

/**
 * @Description: 结构化任务，先持久化保存至数据库，再由任务调度器进行调度
 * @Author: wujw
 * @CreateDate: 2019/5/9 16:42
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class ObjextTask extends AbstractTaskManager {

    @Override
    public void insertTask(JSONObject paramJson, String serialnumber, String analyType, String url) {
        String entryTime = null;
        int taskType = this.getTaskType(url);
        if(taskType == TaskConstants.TASK_TYPE_OFFLINE){
            entryTime = super.getEntryTime(paramJson);
        }
        JSONObject param = getParamMap(paramJson, serialnumber, url, entryTime, taskType);
        param.put("url", url);
        paramJson.put("param", param);
        super.insertTbAnalysisTask(paramJson, serialnumber, analyType, taskType, 1);
        TbAnalysisDetail detail = super.initDefaultDetail(serialnumber, null);
        detail.setEntryTime(entryTime);
        tbAnalysisDetailMapper.insert(detail);
        Timestamp createTime = new Timestamp(System.currentTimeMillis());
        VsdTask vsdTask = initVsdTask(param.toString(), serialnumber, createTime, entryTime == null ? null :Timestamp.valueOf(entryTime),
                TaskConstants.ANALY_TYPE_OBJEXT, TaskConstants.TASK_STATUS_WAIT,param.getInteger("priority"),param.getInteger("taskType"));
        int count = vsdTaskMapper.insert(vsdTask);
        if (count <= 0) {
            throw VideoExceptionUtil.getDbException(serialnumber + " insert vsdTask failed");
        }
    }

    /***
     * @description: 获取param参数
     * @param paramJson 请求参数
     * @param serialnumber 任务号
     * @param url 视频路径
     * @param entryTime 矫正时间
     * @param taskType 任务类型
     * @return: com.alibaba.fastjson.JSONObject
     */
    protected JSONObject getParamMap(JSONObject paramJson, String serialnumber, String url, String entryTime, int taskType) {
        String paramKey = "param";
        JSONObject paramRoot = paramJson.getJSONObject(paramKey);
        String analysisTypes = paramJson.getString("analysisTypes");
        if(analysisTypes == null){
            analysisTypes = TaskConstants.TASK_DEFAULT_ANALYSIS_TYPE;
        }
        convertAnalysisType(paramJson,analysisTypes);
        if (paramRoot == null) {
            paramRoot = initParamRoot(paramJson, serialnumber, url, entryTime);
            paramRoot.put("analysisCfg", super.getAnalysisCfgMap(paramJson));
        } else {
            String customKey = "custom";
            String isValid = paramRoot.getString("isValid");
            if (!StringUtils.isEmpty(isValid) && ObjextTaskConstants.PARAM_IS_VALID.equals(isValid)) {
                JSONObject customMap = paramRoot.getJSONObject(customKey);
                if (customMap == null) {
                    customMap = getCustomMap(paramJson, entryTime, serialnumber);
                } else {
                    customMap.put("cameraId", paramJson.getString("cameraId"));
                    customMap.put("serialnumber", serialnumber);
                    customMap.put("analysisTypes", paramJson.getString("analysisTypes"));
                }
                paramRoot.put(customKey, customMap);
                paramJson.put(paramKey, paramRoot);
            } else {
                paramRoot.put("url", url);
                JSONObject customMap = getCustomMap(paramJson, entryTime, serialnumber);
                paramRoot.put(customKey, customMap);
                paramRoot.put("startFrameIndex", 0);
                paramRoot.put("endFrameIndex", 0);
                paramRoot.put("analysisCfg", getAnalysisCfgMap(paramJson));
            }
        }
        paramRoot.put("enablePartial", TaskParamValidUtil.isPositiveInteger(paramJson, "enablePartial", 0, 0, 1));
        paramRoot.put("searchCfg", ObjextTaskConstants.getSearchCfgMap());
        paramRoot.put("taskType", taskType);
        if(paramJson.getInteger("priority") != null){
            paramRoot.put("priority", paramJson.getInteger("priority"));
        }
        paramRoot.put("analysisTypes", analysisTypes);
        return paramRoot;
    }

    /***
     * @description: 获取任务视频类型
     * @param url 视频地址
     * @return: int
     */
    private int getTaskType(String url) {
        if (ValidUtil.isOffice(url)) {
            return TaskConstants.TASK_TYPE_OFFLINE;
        } else {
            return TaskConstants.TASK_TYPE_ONLINE;
        }
    }

    /**
     * 将业务的分析类型转换成sdk的参数
     * @param paramJson
     * @param analysisTypes
     */
    private void convertAnalysisType(JSONObject paramJson, String analysisTypes) {
        if(analysisTypes.indexOf("1")==-1){
            paramJson.put("enableHumanDetection",false);
        }
        if(analysisTypes.indexOf("2")==-1){
            paramJson.put("enableVehicleDetection",false);
        }
        if(analysisTypes.indexOf("3")==-1){
            paramJson.put("enableFaceDetection",false);
        }
        if(analysisTypes.indexOf("4")==-1){
            paramJson.put("enableBikeDetection",false);
        }
    }
}
