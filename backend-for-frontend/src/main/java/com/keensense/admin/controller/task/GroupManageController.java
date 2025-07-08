package com.keensense.admin.controller.task;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.admin.service.ext.VideoObjextTaskService;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.vo.VsdSlavestatusVo;
import com.keensense.common.util.PageUtils;
import com.keensense.common.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 集群管理
 **/
@Slf4j
@Api(tags = "集群管理")
@RestController
@RequestMapping("/groupManage")
public class GroupManageController {

    @Resource
    private VideoObjextTaskService videoObjextTaskService;

    /**
     * 系统信息统计
     *
     * @return
     */
    @ApiOperation(value = "定时查询集群信息")
    @PostMapping(value = "/initResourceReport")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码"),
            @ApiImplicitParam(name = "rows", value = "每页显示条数"),
            @ApiImplicitParam(name = "id", value = "id"),
    })
    public R initResourceReport(int page, int rows, String id) {
        R result = R.ok();
        Page<VsdSlavestatusVo> pages = new Page<>(page, rows);
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmptyString(id)) {
            params.put("id", id);
        }
        Page<VsdSlavestatusVo> pageResult = videoObjextTaskService.getVsdSlaveList(pages, params);
        result.put("page", new PageUtils(pageResult));
        return result;
    }
}
