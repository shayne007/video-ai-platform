package com.keensense.task.controller;

import com.alibaba.fastjson.JSONObject;
import com.keensense.common.util.ResultUtils;
import com.keensense.task.entity.TbAnalysisTask;
import com.keensense.task.service.IAnalysisTrackService;
import com.keensense.task.service.ITaskCleanInterimService;
import com.keensense.task.service.ITbAnalysisTaskService;
import com.keensense.task.util.TaskParamValidUtil;
import com.keensense.task.util.VideoExceptionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 回调接口
 * @Author: wujw
 * @CreateDate: 2019/11/18 11:34
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Api(value = "回调接口管理")
@RestController
@RequestMapping("/callback")
public class CallbackController extends BaseController {

    @Autowired
    private ITaskCleanInterimService taskCleanInterimService;

    @Autowired
    private IAnalysisTrackService analysisTrackService;

    @Autowired
    private ITbAnalysisTaskService tbAnalysisTaskService;

    private static final String BASE_URL = "/callback";

    private static final String METHOD_NAME="/delete/task/image";

    @ApiOperation(value = "更新任务清理状态", notes = "更新任务清理状态")
    @PostMapping(value = "/delete/task/image")
    public String insertTask(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+METHOD_NAME);
        String serialnumber = TaskParamValidUtil.validSerialNumber(paramJson, true);
        boolean status = TaskParamValidUtil.getBoolean(paramJson, "status", false, true);
        String time = TaskParamValidUtil.validString(paramJson, "time", 20, false);
        if(taskCleanInterimService.updateStatus(serialnumber, status, time)){
            //在刪除完数据之后，需要在callback中把对应的分析打点数据也处理一下，
            // 将打点数据移到his表之后 并删掉
            TbAnalysisTask tbAnalysisTask = tbAnalysisTaskService.getById(serialnumber);
            if(tbAnalysisTask != null){
                analysisTrackService.transAnalysisTracksToHis(tbAnalysisTask.getCameraId(),time);
            }else{
                analysisTrackService.transAnalysisTracksToHis(null,time);
            }
            return ResultUtils.renderSuccess(null);
        } else {
            throw VideoExceptionUtil.getDbException("任务不存在或状态已变更！");
        }
    }
}
