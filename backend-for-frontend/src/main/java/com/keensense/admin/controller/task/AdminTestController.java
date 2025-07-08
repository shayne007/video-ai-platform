package com.keensense.admin.controller.task;

import com.keensense.admin.request.ResultUpdateRequest;
import com.keensense.admin.service.task.IVsdTaskService;
import com.keensense.common.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: shitao
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 9:42 2019/11/27
 * @Version v0.1
 */
@Slf4j
@RestController
@RequestMapping("/admintest")
@Api(tags = "admintest控制器")
public class AdminTestController {

    @Resource
    private IVsdTaskService vsdTaskService;

    /**
     * 删除es数据
     * @param uuid
     * @return
     */
    @ApiOperation(value = "删除es数据")
    @PostMapping(value = "/deleteResultData")
    @ApiImplicitParam(name = "uuid", value = "uuid")
    public R deleteResultData(String uuid) {
        return vsdTaskService.deleteResultData(uuid);
    }

    /**
     * 更新es数据
     * @param paramBo
     * @return
     */
    @ApiOperation(value = "更新es数据")
    @PostMapping(value = "/updateResultData")
    public R updateResultData(@RequestBody ResultUpdateRequest paramBo) {
        return vsdTaskService.updataResultData(paramBo);
    }
}
