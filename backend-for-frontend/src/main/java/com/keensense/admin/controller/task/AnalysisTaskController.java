package com.keensense.admin.controller.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.admin.constants.CommonConstants;
import com.keensense.admin.constants.VideoTaskConstant;
import com.keensense.admin.dto.TaskCondDTO;
import com.keensense.admin.entity.sys.SysUser;
import com.keensense.admin.entity.task.VsdTaskRelation;
import com.keensense.admin.service.ext.VideoObjextTaskService;
import com.keensense.admin.service.sys.ISysUserService;
import com.keensense.admin.service.task.ICameraService;
import com.keensense.admin.service.task.IVsdTaskRelationService;
import com.keensense.admin.service.task.IVsdTaskService;
import com.keensense.admin.util.PolygonUtil;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.vo.CameraVo;
import com.keensense.admin.vo.VsdTaskVo;
import com.keensense.common.util.PageUtils;
import com.keensense.common.util.R;
import com.loocme.sys.constance.DateFormatConst;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/analysisTask")
@Api(tags = "分析任务控制器")
public class AnalysisTaskController {

    @Resource
    private IVsdTaskService vsdTaskService;

    @Resource
    private VideoObjextTaskService videoObjextTaskService;


    @Resource
    private ISysUserService sysUserService;

    @Resource
    private IVsdTaskRelationService vsdTaskRelationService;

    @Resource
    private ICameraService cameraService;

    /**
     * 查询多边形内的点位信息
     */
    @ApiOperation(value = "查询多边形内的点位信息")
    @PostMapping(value = "/queryCamerasInPolygon")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "polygon", value = "经纬度"),
            @ApiImplicitParam(name = "status", value = "状态 [0:离线，1:在线]")
    })
    public R queryCamerasInPolygon(String polygon, String status) {
        R result = R.ok();
        List<Point2D.Double> polygonList = PolygonUtil.paresPolygonList(polygon);
        List<CameraVo> cameraData = cameraService.queryCameraAll(null, status, polygonList);
        result.put("list", cameraData);
        return result;
    }

    //TODO 优化逻辑
    @ApiOperation(value = "查询分析任务列表")
    @PostMapping(value = "/analysisTaskList")
    public R analysisTaskList(@RequestBody TaskCondDTO taskCondDTO) {
        R result = R.ok();
        // 获取分页条件
        int page = taskCondDTO.getPage();
        int rows = taskCondDTO.getRows();
        List<VsdTaskVo> vsdTaskList = new ArrayList<>();
        Map<String, Object> requestParams = new HashMap<>();
        if (StringUtils.isNotEmptyString(taskCondDTO.getCameraName())) {
            requestParams.put("cameraName", taskCondDTO.getCameraName());
        }
        if (StringUtils.isNotEmptyString(taskCondDTO.getStartTime())) {
            String startTime = DateUtil.getFormat(taskCondDTO.getStartTime(), DateFormatConst.YMDHMS_);
            requestParams.put("startTime", startTime);
        }
        if (StringUtils.isNotEmptyString(taskCondDTO.getEndTime())) {
            String endTime = DateUtil.getFormat(taskCondDTO.getEndTime(), DateFormatConst.YMDHMS_);
            requestParams.put("endTime", endTime);
        }
        if (taskCondDTO.getStatus() != null) {
            if (VideoTaskConstant.STATUS.ALL.equals(taskCondDTO.getStatus())) {

            } else {
                requestParams.put("status", taskCondDTO.getStatus());
            }
        }
        requestParams.put("page", page + "");
        requestParams.put("limit", rows + "");
        Long fromType = taskCondDTO.getFromType().longValue();
        Long taskType = null;
        String type = "";
        String userId = "";
        if (fromType == VideoTaskConstant.TASK_TYPE.REAL) {
            //实时联网
            taskType = VideoTaskConstant.TASK_TYPE.REAL;
            type = VideoTaskConstant.Type.OBJEXT;
            userId = VideoTaskConstant.USER_ID.REALTIME_VIDEO;
        } else if (fromType == VideoTaskConstant.TASK_TYPE.IPC) {
            //IPC
            taskType = VideoTaskConstant.TASK_TYPE.REAL;
            type = VideoTaskConstant.Type.OBJEXT;
            userId = VideoTaskConstant.USER_ID.IPC;
        } else if (fromType == VideoTaskConstant.TASK_TYPE.OFFLINE) {
            //离线视频
            taskType = VideoTaskConstant.TASK_TYPE.OFFLINE;
            type = VideoTaskConstant.Type.OBJEXT;
            userId = VideoTaskConstant.USER_ID.OFFLINE_VIDEO;
        } else if (fromType == VideoTaskConstant.TASK_TYPE.CAPTURE) {
            //抓拍机任务
            taskType = VideoTaskConstant.TASK_TYPE.REAL;
            type = VideoTaskConstant.Type.PICTURE;
            userId = VideoTaskConstant.USER_ID.CAPTURE;
        } else if (fromType == VideoTaskConstant.TASK_TYPE.ONLINE_VIDEO) {
            //联网录像
            taskType = VideoTaskConstant.TASK_TYPE.ONLINE_VIDEO;
            type = VideoTaskConstant.Type.OBJEXT;
            userId = VideoTaskConstant.USER_ID.ONLINE_VIDEO;
        }else if (fromType == VideoTaskConstant.TASK_TYPE.GATE.longValue()) {
            //卡口
            taskType = VideoTaskConstant.TASK_TYPE.GATE.longValue();
            type = VideoTaskConstant.Type.OBJEXT;
            userId = VideoTaskConstant.USER_ID.GATE;
        }

        requestParams.put("taskType", taskType);
        requestParams.put("type", type);
        requestParams.put("userId", userId);
        IPage taskListReponse = vsdTaskRelationService.queryVsdTaskAllService(requestParams);
        List<VsdTaskRelation> list = taskListReponse.getRecords();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                VsdTaskRelation varbo = list.get(i);
                // 离线视频文件名称
                String fileName = varbo.getTaskName();
                // 监控点ID
                String cameraId = varbo.getCameraId();
                VsdTaskVo vsdTask = new VsdTaskVo();
                String serialnumber = varbo.getSerialnumber();
                vsdTask.setSerialnumber(serialnumber);
                vsdTask.setStatus(varbo.getTaskStatus());
                vsdTask.setProgress(varbo.getTaskProgress());
                vsdTask.setCreateTimeStr(com.keensense.common.util.DateUtil.formatTime(varbo.getCreatetime()));
                vsdTask.setEndTimeStr(com.keensense.common.util.DateUtil.formatTime(varbo.getEndTime()));
                vsdTask.setType(varbo.getType());
                if (taskCondDTO.getFromType() != null) {
                    vsdTask.setFilefromtype(taskCondDTO.getFromType().intValue());
                }
                vsdTask.setCameraId(cameraId);
                vsdTask.setId(serialnumber);
                vsdTask.setIsValid(varbo.getIsvalid());
                vsdTask.setFileName(fileName);
                vsdTask.setFromType(varbo.getFromType());
                vsdTask.setCameraFileId(varbo.getCameraFileId());
                if (varbo.getCreateuser() != null) {
                    SysUser sysUser = sysUserService.selectUserById(varbo.getCreateuser());
                    if (sysUser != null) {
                        vsdTask.setCreator(sysUser.getUsername());
                    }
                }
                vsdTaskList.add(vsdTask);
            }
        }
        vsdTaskList = vsdTaskService.taskResultHandle(vsdTaskList);
        Page<VsdTaskVo> pages = new Page<>(taskCondDTO.getPage(), taskCondDTO.getRows());
        pages.setRecords(vsdTaskList);
        pages.setTotal(taskListReponse.getTotal());
        return result.put("page", new PageUtils(pages));
    }

    @ApiOperation(value = "删除分析任务")
    @PostMapping(value = "/deleteTaskList")
    @ApiImplicitParam(name = "serialnumbers", value = "任务序列号")
    public R deleteTaskList(String serialnumbers) {
        if (StringUtils.isNotEmptyString(serialnumbers)) {
            String[] strArr = serialnumbers.split(",");
            for (int i = 0; i < strArr.length; i++) {
                String serialnumber = strArr[i];
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("serialnumber", serialnumber);
                String resultJson = videoObjextTaskService.deleteVsdTaskService(paramMap);
                Var resultVar = Var.fromJson(resultJson);
                String retCode = resultVar.getString("ret");
                // 0成功或者9001任务不存在
                if ("0".equals(retCode) || "9001".equals(retCode) || "7".equals(retCode)) {
                    vsdTaskService.deleteBySerialnumber(serialnumber);
                }
            }
        }
        return R.ok();
    }

    @ApiOperation(value = "根据监控点Id查询启动任务状态")
    @PostMapping(value = "/queryTaskStatus")
    @ApiImplicitParam(name = "cameraId", value = "监控点Id")
    public R queryTaskStatus(String cameraId) {
        VsdTaskRelation vsdTaskRelation = vsdTaskRelationService.getOne(new QueryWrapper<VsdTaskRelation>().eq("camera_id", cameraId).in("from_type", VideoTaskConstant.TASK_TYPE.REAL, VideoTaskConstant.TASK_TYPE.IPC, VideoTaskConstant.TASK_TYPE.CAPTURE,VideoTaskConstant.TASK_TYPE.GATE));
        if (vsdTaskRelation == null) {
            vsdTaskRelation = new VsdTaskRelation();
        }
        return R.ok().put("data", vsdTaskRelation);
    }
}
