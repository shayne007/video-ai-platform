package com.keensense.task.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.common.util.ResultUtils;
import com.keensense.task.constants.DeleteTaskConstants;
import com.keensense.task.constants.TaskConstants;
import com.keensense.task.entity.TaskVO;
import com.keensense.task.entity.TbAnalysisTask;
import com.keensense.task.service.ITbAnalysisTaskService;
import com.keensense.task.service.IVsdTaskService;
import com.keensense.task.util.TaskParamValidUtil;
import com.keensense.task.util.VideoExceptionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 任务管理接口
 * @Author: wujw
 * @CreateDate: 2019/5/10 14:32
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Api(value = "任务管理接口")
@RestController
@RequestMapping("/rest/taskManage")
public class TaskController extends BaseController {

    @Autowired
    private ITbAnalysisTaskService tbAnalysisTaskService;
    @Autowired
    private IVsdTaskService vsdTaskService;

    private static final String BASE_URL ="/rest/taskManage";

    @ApiOperation(value = "添加任务", notes = "添加任务")
    @PostMapping(value = "/addVideoObjextTask")
    public String insertTask(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+"/addVideoObjextTask");
        String serialnumber = TaskParamValidUtil.validSerialNumber(paramJson, true);
        TbAnalysisTask tbAnalysisTask = tbAnalysisTaskService.getTbAnalysisTaskById(serialnumber);
        if (tbAnalysisTask != null) {
            return duplicativeSerialnumber();
        }
        String type = TaskParamValidUtil.validAnalyType(paramJson);
        String url = paramJson.getString("url");
        if (StringUtils.isEmpty(url)) {
            return ResultUtils.renderFailure("7", "url不能为空", null);
        }
        tbAnalysisTaskService.insertTask(paramJson, serialnumber, type, url);
        return ResultUtils.renderSuccess(null);
    }

    @ApiOperation(value = "添加任务Master2.0版本", notes = "添加任务Master2.0版本")
    @PostMapping(value = "/addTask")
    public String addTask(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+"/addTask");
        String serialnumber = TaskParamValidUtil.validSerialNumber(paramJson, true);
        TbAnalysisTask tbAnalysisTask = tbAnalysisTaskService.getTbAnalysisTaskById(serialnumber);
        if (tbAnalysisTask != null) {
            return duplicativeSerialnumber();
        }
        String type = TaskParamValidUtil.validAnalyType(paramJson);
        String url = paramJson.getString("url");
        if (StringUtils.isEmpty(url)) {
            return ResultUtils.renderFailure("7", "url不能为空", null);
        }
        Integer taskType = TaskParamValidUtil.isPositiveInteger(paramJson, "taskType", -1,1,4);
        if(taskType == -1){
            return ResultUtils.renderFailure("7", "taskType不能为空", null);
        }
        Integer priority = TaskParamValidUtil.isPositiveInteger(paramJson, "priority", TaskConstants.TASK_DEFAULT_PRIORITY,0,99);
        paramJson.put("priority",priority);
        String analysisTypes = paramJson.getString("analysisTypes");
        if(analysisTypes == null){
            analysisTypes = TaskConstants.TASK_DEFAULT_ANALYSIS_TYPE;
            paramJson.put("analysisTypes",analysisTypes);
        }
        tbAnalysisTaskService.addTask(paramJson, serialnumber, type, url, taskType);
        return ResultUtils.renderSuccess(null);
    }

    @ApiOperation(value = "暂停任务", notes = "暂停任务")
    @PostMapping(value = "/pauseVideoObjectTask")
    public String pauseVideoObjectTask(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+"/pauseVideoObjectTask");
        return pauseTask(paramJson);
    }

    /**
     * 暂停任务业务方法
     * @param paramJson
     * @return
     */
    private String pauseTask(JSONObject paramJson) {
        String serialnumber = TaskParamValidUtil.validSerialNumber(paramJson, true);
        TbAnalysisTask tbAnalysisTask = tbAnalysisTaskService.getTbAnalysisTaskById(serialnumber);
        if (tbAnalysisTask == null || TaskConstants.TASK_DELETE_TURE == tbAnalysisTask.getStatus()) {
            return taskIsDelete();
        }
        tbAnalysisTaskService.stopTask(tbAnalysisTask);
        return ResultUtils.renderSuccess(null);
    }

    @ApiOperation(value = "停止浓缩任务", notes = "停止浓缩任务")
    @PostMapping(value = "/stopVideoObjectTask")
    public String stopVideoObjectTask(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+"/stopVideoObjectTask");
        return pauseTask(paramJson);
    }

    @ApiOperation(value = "继续实时任务接口", notes = "继续实时任务接口")
    @PostMapping(value = "/continueVideoObjectTask")
    public String continueVideoObjectTask(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+"/continueVideoObjectTask");
        String serialnumber = TaskParamValidUtil.validSerialNumber(paramJson, true);
        TbAnalysisTask tbAnalysisTask = tbAnalysisTaskService.getTbAnalysisTaskById(serialnumber);
        if (tbAnalysisTask == null || TaskConstants.TASK_DELETE_TURE == tbAnalysisTask.getStatus()) {
            return taskIsDelete();
        }
//        else if (TaskConstants.TASK_TYPE_ONLINE != tbAnalysisTask.getTaskType()) {
//            return ResultUtils.renderFailure("7", "重启任务仅针对实时流有效", null);
//        }
        tbAnalysisTaskService.continueTask(tbAnalysisTask);
        return ResultUtils.renderSuccess(null);
    }

    @ApiOperation(value = "删除任务接口", notes = "删除任务接口")
    @PostMapping(value = "/deleteVideoObjectTask")
    public String deleteVideoObjectTask(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+"/deleteVideoObjectTask");
        return deleteTask(paramJson);
    }

    /**
     * 停止任务业务方法
     * @param paramJson
     * @return
     */
    private String deleteTask(JSONObject paramJson) {
        String serialnumber = TaskParamValidUtil.validSerialNumber(paramJson, true);
        TbAnalysisTask tbAnalysisTask = tbAnalysisTaskService.getTbAnalysisTaskById(serialnumber);
        if (tbAnalysisTask == null || TaskConstants.TASK_DELETE_TURE == tbAnalysisTask.getStatus()) {
            return taskIsDelete();
        }
        tbAnalysisTaskService.deleteTask(tbAnalysisTask, DeleteTaskConstants.CLEAN_DATA_SOURCE_ACTIVE);
        return ResultUtils.renderSuccess(null);
    }

    @ApiOperation(value = "更新感兴趣区域", notes = "更新感兴趣区域")
    @PostMapping(value = "/updateUdrSetting")
    public String updateUdrSetting(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+"/updateUdrSetting");
        String serialnumber = TaskParamValidUtil.validSerialNumber(paramJson, true);
        TbAnalysisTask tbAnalysisTask = tbAnalysisTaskService.getTbAnalysisTaskById(serialnumber);
        if (tbAnalysisTask == null || TaskConstants.TASK_DELETE_TURE == tbAnalysisTask.getStatus()) {
            return taskIsDelete();
        } else if (TaskConstants.TASK_TYPE_VIDEO == tbAnalysisTask.getTaskType()
                || TaskConstants.ANALY_TYPE_PICTURE.equals(tbAnalysisTask.getAnalyType())) {
            return ResultUtils.renderFailure("7", "录像分片、抓拍机任务不能更新感兴趣区域", null);
        }
        vsdTaskService.updateUdrSetting(serialnumber, paramJson);
        return ResultUtils.renderSuccess(null);
    }

    @ApiOperation(value = "分析任务进度查询(不包含录像分片任务)", notes = "分析任务进度查询(不包含录像分片任务)")
    @PostMapping(value = "/getAllVideoObjectTaskList")
    public String getAllVideoObjectTaskList(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+"/getAllVideoObjectTaskList");
        Page<TaskVO> page = super.getPage(paramJson, 1, 10);
        JSONObject result = tbAnalysisTaskService.getTaskList(paramJson, page);
        return ResultUtils.returnSuccess(result);
    }

    @ApiOperation(value = "分析任务进度查询", notes = "分析任务进度查询")
    @PostMapping(value = "/getVideoObjectTaskList")
    public String getVideoObjectTaskList(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+"/getVideoObjectTaskList");
        Page<TbAnalysisTask> page = super.getPage(paramJson, 1, 10);
        JSONObject result = tbAnalysisTaskService.getVideoTaskList(paramJson, page);
        return ResultUtils.returnSuccess(result);
    }

    @ApiOperation(value = "任务重试", notes = "任务重试")
    @PostMapping(value = "/retryTask")
    public String retryTask(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+"/retryTask");
        String serialnumber = TaskParamValidUtil.validSerialNumber(paramJson, true);
        TbAnalysisTask tbAnalysisTask = tbAnalysisTaskService.getTbAnalysisTaskById(serialnumber);
        if (tbAnalysisTask == null || TaskConstants.TASK_DELETE_TURE == tbAnalysisTask.getStatus()) {
            return taskIsDelete();
        }
        tbAnalysisTaskService.retryTask(tbAnalysisTask);
        return ResultUtils.renderSuccess(null);
    }

    @ApiOperation(value = "添加抓拍机任务", notes = "添加抓拍机任务")
    @PostMapping(value = "/startPictureObjextTask")
    public String startPictureObjextTask(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+"/startPictureObjextTask");
        String serialnumber = TaskParamValidUtil.validSerialNumber(paramJson, true);
        TbAnalysisTask tbAnalysisTask = tbAnalysisTaskService.getTbAnalysisTaskById(serialnumber);
        JSONObject paramObject = paramJson.getJSONObject("param");
        if (paramObject == null) {
            throw VideoExceptionUtil.getValidException("param不能为空");
        }
        paramJson.put("type", TaskConstants.ANALY_TYPE_PICTURE);
        if (tbAnalysisTask != null) {
            tbAnalysisTaskService.continueTask(tbAnalysisTask);
        } else {
            tbAnalysisTaskService.insertTask(paramJson, serialnumber, TaskConstants.ANALY_TYPE_PICTURE, null);
        }
        return ResultUtils.renderSuccess(null);
    }

    @ApiOperation(value = "暂停抓拍机任务", notes = "暂停抓拍机任务")
    @PostMapping(value = "/stopPictureObjextTask")
    public String stopPictureObjextTask(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+"/stopPictureObjextTask");
        return pauseTask(paramJson);
    }

    @ApiOperation(value = "停止抓拍机任务", notes = "停止抓拍机任务")
    @PostMapping(value = "/deletePictureObjextTask")
    public String deletePictureObjextTask(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+"/deletePictureObjextTask");
        return deleteTask(paramJson);
    }

    @ApiOperation(value = "批量停止任务接口", notes = "批量停止任务接口")
    @PostMapping(value = "/stopBatchVideoObjectTask")
    public String stopBatchVideoObjectTask(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+"/stopBatchVideoObjectTask");
        String serialnumber = TaskParamValidUtil.validString(paramJson, TaskConstants.SERIALNUMBER, 6000, true);
        String[] ids = serialnumber.split(",");
        tbAnalysisTaskService.stopBatchTask(ids);
        return ResultUtils.renderSuccess(null);
    }

    @ApiOperation(value = "批量删除任务接口", notes = "批量删除任务接口")
    @PostMapping(value = "/deleteBatchVideoObjectTask")
    public String deleteBatchVideoObjectTask(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+"/deleteBatchVideoObjectTask");
        String serialnumber = TaskParamValidUtil.validString(paramJson, TaskConstants.SERIALNUMBER, 6000, true);
        String[] ids = serialnumber.split(",");
        tbAnalysisTaskService.deleteBatchTask(ids);
        return ResultUtils.renderSuccess(null);
    }

    @ApiOperation(value = "批量启动任务接口", notes = "批量启动任务接口")
    @PostMapping(value = "/continueBatchVideoObjectTask")
    public String continueBatchVideoObjectTask(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+"/continueBatchVideoObjectTask");
        String serialnumber = TaskParamValidUtil.validString(paramJson, TaskConstants.SERIALNUMBER, 6000, true);
        String[] ids = serialnumber.split(",");
        tbAnalysisTaskService.continueBatchTask(ids);
        return ResultUtils.renderSuccess(null);
    }

    @ApiOperation(value = "获取搜图参数接口", notes = "获取搜图参数接口")
    @PostMapping(value = "/getTaskForSearch")
    public String getTaskForSearch(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+"/getTaskForSearch");
        return ResultUtils.renderSuccess(tbAnalysisTaskService.getTaskForSearch(paramJson));
    }

    @ApiOperation(value = "获取录像分析进度", notes = "获取录像分析进度")
    @PostMapping(value = "/getVideoTaskProgress")
    public int getVideoTaskProgress(@RequestBody String body){
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+"/getVideoTaskProgress");
        return tbAnalysisTaskService.getVideoTaskProgress(paramJson);
    }

    @ApiOperation(value = "修改param参数", notes = "修改param参数")
    @PostMapping(value = "/updateParam")
    public String updateParam(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+"/updateParam");
        String serialnumber = TaskParamValidUtil.validSerialNumber(paramJson, true);
        String param = TaskParamValidUtil.validString(paramJson, "param", 50000, true);
        TbAnalysisTask tbAnalysisTask = tbAnalysisTaskService.getTbAnalysisTaskById(serialnumber);
        if (tbAnalysisTask == null || TaskConstants.TASK_DELETE_TURE == tbAnalysisTask.getStatus()) {
            return taskIsDelete();
        }
        vsdTaskService.updateParam(serialnumber, param);
        return ResultUtils.renderSuccess(null);
    }

    /***
     * @description: 任务已删除提示
     * @return: java.lang.String
     */
    private String taskIsDelete(){
        return ResultUtils.renderFailure("7", "任务不存在或者已删除", null);
    }

    /***
     * @description: 重复的serialnumber
     * @return: java.lang.String
     */
    private String duplicativeSerialnumber(){
        return ResultUtils.renderFailure("8", "重复的任务号", null);
    }


}
