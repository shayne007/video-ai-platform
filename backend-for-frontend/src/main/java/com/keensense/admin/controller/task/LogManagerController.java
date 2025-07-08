package com.keensense.admin.controller.task;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.admin.base.BaseController;
import com.keensense.admin.entity.sys.SysLog;
import com.keensense.admin.request.LogQueryRequest;
import com.keensense.admin.request.RankingListRequest;
import com.keensense.admin.service.sys.ISysLogService;
import com.keensense.admin.util.ExcelHandleUtils;
import com.keensense.admin.util.QuerySqlUtil;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.vo.SysOperateLogVo;
import com.keensense.common.util.PageUtils;
import com.keensense.common.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(tags = "日志管理")
@RestController
@RequestMapping("/log")
public class LogManagerController extends BaseController {

    @Resource
    private ISysLogService logService;

    @ApiOperation(value = "查询日志")
    @PostMapping(value = "/queryLogByPage")
    public R queryLogByPage(@RequestBody LogQueryRequest logQueryRequest) {
        R result = R.ok();
        try {
            int page = logQueryRequest.getPage();
            int rows = logQueryRequest.getRows();
            String startTime = logQueryRequest.getStartTime();
            String actionType = logQueryRequest.getActionType();
            String endTime = logQueryRequest.getEndTime();
            String moduleName = logQueryRequest.getModuleName();
            String userName = logQueryRequest.getUserName();
            Page<SysLog> pages = new Page<>(page, rows);
            Map<String, Object> params = new HashMap<>();
            if (StringUtils.isNotEmpty(userName)) {
                params.put("userName", "%" + userName + "%");
            }
            if (StringUtils.isNotEmpty(moduleName)) {
                params.put("moduleName", "%" + moduleName + "%");
            }
            if (StringUtils.isNotEmpty(startTime)) {
                params.put("startTime", startTime);
            }
            if (StringUtils.isNotEmpty(endTime)) {
                params.put("endTime", endTime);
            }
            if (StringUtils.isNotEmpty(actionType)) {
                params.put("actionType", StringUtils.getInCondition(actionType));
            }
            Page<SysLog> pageResult = logService.selectPageByParams(pages, params);
            result.put("page", new PageUtils(pageResult));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.error(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "导出excel")
    @GetMapping(value = "/exportExcel")
    public R exportExcel(LogQueryRequest logQueryRequest, HttpServletResponse response) {
        try {
            int page = 1;
            int rows = 3000;
            String startTime = logQueryRequest.getStartTime();
            String actionType = logQueryRequest.getActionType();
            String endTime = logQueryRequest.getEndTime();
            String moduleName = logQueryRequest.getModuleName();
            String userName = logQueryRequest.getUserName();
            Page<SysLog> pages = new Page<>(page, rows);
            Map<String, Object> params = new HashMap<>();
            if (StringUtils.isNotEmpty(startTime)) {
                params.put("startTime", startTime);
            }
            if (StringUtils.isNotEmpty(actionType)) {
                params.put("actionType", StringUtils.getInCondition(actionType));
            }
            if (StringUtils.isNotEmpty(endTime)) {
                params.put("endTime", endTime);
            }
            if (StringUtils.isNotEmpty(moduleName)) {
                params.put("moduleName", "%" + moduleName + "%");
            }
            if (StringUtils.isNotEmpty(userName)) {
                params.put("userName", "%" + userName + "%");
            }
            Page<SysLog> logs = logService.selectPageByParams(pages, params);
            String[] headers = {"创建用户", "模块名称", "动作", "创建时间"};
            ExcelHandleUtils.exportLogExcel(response, headers, logs.getRecords());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.error(e.getMessage());
        }
        return R.ok();
    }

    @PostMapping(value = "/queryUserRanking")
    @ApiOperation(value = "查询用户排行榜")
    public R queryUserLogPage(@RequestBody RankingListRequest rankingListRequest) {
        int page = rankingListRequest.getPage();
        int rows = rankingListRequest.getRows();
        String userName = rankingListRequest.getUserName();
        String startTime = rankingListRequest.getStartTime();
        String endTime = rankingListRequest.getEndTime();
        try {
            Page<SysOperateLogVo> pages = new Page<>(page, rows);
            Map<String, Object> params = new HashMap<>();
            if (StringUtils.isNotEmptyString(userName)) {
                String replaceQueryName = QuerySqlUtil.replaceQueryName(userName);
                params.put("userName", "%" + replaceQueryName + "%");
            }
            if (StringUtils.isNotEmptyString(startTime)) {
                params.put("startTime", startTime);
            }
            if (StringUtils.isNotEmptyString(endTime)) {
                params.put("endTime", endTime);
            }
            Page<SysOperateLogVo> pageResult = logService.queryUserPageByParams(pages, params);
            return R.ok().put("page", new PageUtils(pageResult));
        } catch (Exception e) {
            log.error("查询用户登录排行列表出现错误！", e);
            return R.error("查询出错，请重试！");
        }
    }

    @PostMapping(value = "/queryDeptRanking")
    @ApiOperation(value = "查询部门排行榜")
    public R queryDeptLogPage(@RequestBody RankingListRequest rankingListRequest) {
        int page = rankingListRequest.getPage();
        int rows = rankingListRequest.getRows();
        String deptName = rankingListRequest.getDeptName();
        String startTime = rankingListRequest.getStartTime();
        String endTime = rankingListRequest.getEndTime();
        try {
            Page<SysOperateLogVo> pages = new Page<>(page, rows);
            Map<String, Object> params = new HashMap<>();
            if (StringUtils.isNotEmptyString(deptName)) {
                String replaceQueryName = QuerySqlUtil.replaceQueryName(deptName);
                params.put("deptName", "%" + replaceQueryName + "%");
            }
            if (StringUtils.isNotEmptyString(startTime)) {
                params.put("startTime", startTime);
            }
            if (StringUtils.isNotEmptyString(endTime)) {
                params.put("endTime", endTime);
            }
            Page<SysOperateLogVo> pageResult = logService.queryDeptPageByParams(pages, params);
            return R.ok().put("page", new PageUtils(pageResult));
        } catch (Exception e) {
            log.error("查询部门登录排行列表出现错误！", e);
            return R.error("查询出错，请重试！");
        }
    }
}
