package com.keensense.task.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.common.util.ResultUtils;
import com.keensense.task.entity.VsdSlave;
import com.keensense.task.service.IVsdSlaveService;
import com.keensense.task.util.TaskParamValidUtil;
import com.keensense.task.util.VideoExceptionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:  集群管理
 * @Author: wujw
 * @CreateDate: 2019/7/31 11:00
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Api(value = "集群状态接口")
@RestController
@Slf4j
@RequestMapping("/rest/slave")
public class SlaveController extends BaseController {

    private static String BASE_URL = "/rest/slave";

    @Autowired
    private IVsdSlaveService vsdSlaveService;

    @ApiOperation(value = "获取集群信息列表", notes = "获取集群信息列表")
    @PostMapping(value = "/getVsdSlaveList")
    public String getVsdSlaveList(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+"/getVsdSlaveList");
        Page<VsdSlave> page = getPage(paramJson, 1, 10);
        return ResultUtils.returnSuccess(vsdSlaveService.getVsdSlaveList(page));
    }

    @ApiOperation(value = "根据ID获取节点信息", notes = "根据ID获取节点信息")
    @PostMapping(value = "/getVsdSlaveById")
    public String getVsdSlaveById(@RequestBody String body) {
        JSONObject paramJson = super.getJsonByBody(body,BASE_URL+"/getVsdSlaveById");
        Long id = TaskParamValidUtil.isPositiveLong(paramJson, "id", 0, 1, Long.MAX_VALUE);
        if(id == 0){
            throw VideoExceptionUtil.getValidException("id不能为空!");
        }
        VsdSlave vsdSlave = vsdSlaveService.getById(id);
        if(vsdSlave == null){
            return ResultUtils.renderFailure("7", "节点信息不存在!");
        }
        return ResultUtils.renderSuccess(vsdSlave);
    }
}
