package com.keensense.task.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.common.util.ResultUtils;
import com.keensense.task.entity.VsdSlave;
import com.keensense.task.listener.StartListener;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: CheckController
 * @Description: 检测服务接口
 * @Author: cuiss
 * @CreateDate: 2020/5/26 19:36
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Api(value = "检测zk服务接口")
@RestController
@Slf4j
@RequestMapping("/rest/check")
public class CheckController extends BaseController {

    @ApiOperation(value = "检测zk服务状态", notes = "检测zk服务状态")
    @PostMapping(value = "/checkZkStatus")
    public String getVsdSlaveList(@RequestBody String body) {
        try{
            StartListener.zkDistributeLock.hasCurrentLock();
            return ResultUtils.returnSuccess(new JSONObject());
        }catch(Exception e){
            e.printStackTrace();
            return ResultUtils.renderFailure(null);
        }

    }
}
