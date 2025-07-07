package com.keensense.task.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.common.util.ResultUtils;
import com.keensense.task.entity.VsdSlave;
import com.keensense.task.entity.VsdTask;
import com.keensense.task.service.IVsdSlaveService;
import com.keensense.task.service.IVsdTaskService;
import com.keensense.task.util.DateUtil;
import com.keensense.task.util.TaskParamValidUtil;
import com.keensense.task.util.ValidUtil;
import com.keensense.task.util.VideoExceptionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: 盒子版本接口
 * @Author: wujw
 * @CreateDate: 2019/8/16 9:29
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Api(value = "盒子版本接口")
@RestController
@RequestMapping("/rest/vsdTask")
public class VsdTaskController extends BaseController {

    @Autowired
    private IVsdTaskService vsdTaskService;
    @Autowired
    private IVsdSlaveService vsdSlaveService;

    private static final String BASE_URL = "/rest/vsdTask";

    @ApiOperation(value = "获取等待处理的任务", notes = "获取等待处理的任务")
    @PostMapping(value = "/getTaskList")
    public String getTaskList(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+"/getTaskList");
        String slaveIp = TaskParamValidUtil.validString(paramJson, "slaveIp", true);
        if(!ValidUtil.isIp(slaveIp)){
            return ResultUtils.renderFailure("7","slaveIp 非法IP");
        }
        Page<VsdTask> page = super.getPage(paramJson, 1, 10);
        List<VsdTask> list = vsdTaskService.selectByPage(page, new QueryWrapper<VsdTask>()
                .eq("slaveip", slaveIp));
        return ResultUtils.renderSuccess(list);
    }

    @ApiOperation(value = "获取等待处理的任务", notes = "获取等待处理的任务")
    @PostMapping(value = "/updateTask")
    public String updateTask(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+"/updateTask");
        String id = TaskParamValidUtil.validString(paramJson, "id", true);
        VsdTask vsdTask = vsdTaskService.selectOne(new QueryWrapper<VsdTask>().eq("id", id));
        if(vsdTask == null){
            return ResultUtils.renderFailure("7", "任务序列号不存在", null);
        }
        Integer status = TaskParamValidUtil.getInteger(paramJson, "status", 0, 3, true);
        Integer errCode =  TaskParamValidUtil.getInteger(paramJson, "errCode", Integer.MIN_VALUE, Integer.MAX_VALUE, false);
        String errMsg = TaskParamValidUtil.validString(paramJson, "errMsg", 1024, false);
        vsdTask.setStatus(status);
        if(errCode != null){
            vsdTask.setErrcode(errCode);
        }
        if(errMsg != null){
            vsdTask.setErrmsg(errMsg);
        }
        vsdTaskService.updateVsdTask(vsdTask);
        return ResultUtils.renderSuccess(null);
    }

    @ApiOperation(value = "获取等待处理的任务", notes = "获取等待处理的任务")
    @PostMapping(value = "/heartBox")
    public String heartBox(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+"/heartBox");
        String slaveIp = TaskParamValidUtil.validString(paramJson, "slaveIp", true);
        if(!ValidUtil.isIp(slaveIp)){
            return ResultUtils.renderFailure("7","slaveIp 非法IP");
        }
        Integer objextCapability = TaskParamValidUtil.isPositiveInteger(paramJson, "objextCapability", 0, 0, Integer.MAX_VALUE);
        String payload = TaskParamValidUtil.validString(paramJson, "payload", true);
        Integer valid = TaskParamValidUtil.isPositiveInteger(paramJson, "valid", 1, 0, 1);
        VsdSlave vsdSlave = vsdSlaveService.selectByWrapper(new QueryWrapper<VsdSlave>().eq("slave_ip", slaveIp));
        int result = 0;
        if(vsdSlave == null){
            vsdSlave = new VsdSlave();
            vsdSlave.setObjextCapability(objextCapability);
            vsdSlave.setReserve("");
            vsdSlave.setSlaveIp(slaveIp);
            vsdSlave.setValid(valid);
            vsdSlave.setPayload(payload);
            vsdSlave.setLastupdateTime(DateUtil.now());
            result = vsdSlaveService.insert(vsdSlave);
        }else{
            vsdSlave.setObjextCapability(objextCapability);
            vsdSlave.setValid(valid);
            vsdSlave.setPayload(payload);
            vsdSlave.setLastupdateTime(DateUtil.now());
            result = vsdSlaveService.updateVsdSlave(vsdSlave);
        }
        if(result > 0){
            return ResultUtils.renderSuccess(null);
        }else{
            throw VideoExceptionUtil.getDbException("更新数据库失败!");
        }
    }
}
