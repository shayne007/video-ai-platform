package com.keensense.admin.controller.task;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.admin.base.BaseController;
import com.keensense.admin.constants.CameraConstants;
import com.keensense.admin.constants.CommonConstants;
import com.keensense.admin.constants.VideoTaskConstant;
import com.keensense.admin.dto.TaskParamBo;
import com.keensense.admin.request.CameraListQueryRequest;
import com.keensense.admin.request.CameraRequest;
import com.keensense.admin.service.task.ICameraService;
import com.keensense.admin.service.task.IVsdTaskService;
import com.keensense.admin.util.PolygonUtil;
import com.keensense.admin.util.QuerySqlUtil;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.vo.CameraVo;
import com.keensense.common.util.PageUtils;
import com.keensense.common.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 11:54 2019/6/19
 * @Version v0.1
 */
@Slf4j
@Api(tags = "接入源-实时分析-IPC直连")
@RestController
@RequestMapping("/ipcManager")
public class IpcManagerController extends BaseController {

    @Resource
    ICameraService cameraService;

    @Resource
    private IVsdTaskService vsdTaskService;

    @Autowired
    private CameraController cameraController;

    @ApiOperation(value = "查询Ipc监控点列表")
    @PostMapping(value = "/cameraListQuery")
    public R selectOnlineCameraListByPage(@RequestBody CameraListQueryRequest request) {
        if (StringUtils.isNotEmptyString(request.getCameraName())) {
            if (StringUtils.isEmpty(request.getCameraName().trim())) {
                return R.error("不允许全为空格");
            }
            if (request.getCameraName().length() > CommonConstants.REGEX.CAMERA_CASE_LENGTH) {
                return R.error("监控点名称超过32位");
            }
            if (StringUtils.checkRegex_false(request.getCameraName(),
                    CommonConstants.REGEX.CAMERA_CASE_RELINE_FILE_NAME_NOT_SUPPORT)) {
                return R.error("监控点名称不符合规则");
            }
        }
        int page = request.getPage(); // 取得当前页数,注意这是jqgrid自身的参数
        int rows = request.getRows(); // 取得每页显示行数，,注意这是jqgrid自身的参数
        String cameraName = request.getCameraName();
        String status = request.getStatus();
        String isvalid = request.getIsvalid();
        Integer overlineType = request.getOverlineType();
        Map<String, Object> map = new HashMap<String, Object>();
        String retCameraName = QuerySqlUtil.replaceQueryName(cameraName);
        map.put("cameraName", "%" + retCameraName + "%");
        if (StringUtils.isNotEmptyString(status)) {
            if (!"-1".equals(status)) {
                map.put("status", status);
            }
        }
        map.put("isvalid", isvalid);
        map.put("overlineType", overlineType);
        map.put("cameraType", CameraConstants.CameraType.RTSP);
        map.put("fromType", VideoTaskConstant.FROM_TYPE.IPC);
        Page<CameraVo> pages = new Page<>(page, rows);
        Page<CameraVo> pageResult = cameraService.selectOnlineAndIpCCameraListByPage(pages, map);
        return R.ok().put("page", new PageUtils(pageResult));
    }

    @ApiOperation(value = "更新Ipc监控点配置")
    @PostMapping(value = "/updateCamera")
    public R updateCamera(@RequestBody CameraRequest cameraRequest) {
        cameraRequest.setCameratype(CameraConstants.CameraType.RTSP);
        if (StringUtils.isEmpty(cameraRequest.getName())) {
            return R.error("监控点名称不能为空");
        }
        if (StringUtils.isEmpty(cameraRequest.getName().trim())) {
            return R.error("不允许全为空格");
        }
        if (cameraRequest.getName().length() > CommonConstants.REGEX.CAMERA_CASE_LENGTH) {
            return R.error("监控点名称超过32位");
        }
        if (StringUtils.checkRegex_false(cameraRequest.getName(),
                CommonConstants.REGEX.CAMERA_CASE_RELINE_FILE_NAME_NOT_SUPPORT)) {
            return R.error("监控点名称不符规则");
        }
        return cameraService.updateCamera(cameraRequest);
    }


    @ApiOperation(value = "提交Ipc点位分析")
    @PostMapping(value = "/submitIpc")
    public R submitCamera(@RequestBody CameraRequest camera) {
        camera.setCameratype(CameraConstants.CameraType.RTSP);
        if (StringUtils.isEmpty(camera.getName())) {
            return R.error("监控点名称不能为空");
        }
        if (StringUtils.isEmpty(camera.getName().trim())) {
            return R.error("不允许全为空格");
        }
        if (camera.getName().length() > CommonConstants.REGEX.CAMERA_CASE_LENGTH) {
            return R.error("监控点名称超过32位");
        }
        if (StringUtils.checkRegex_false(camera.getName(),
                CommonConstants.REGEX.CAMERA_CASE_RELINE_FILE_NAME_NOT_SUPPORT)) {
            return R.error("监控点名称不符规则");
        }
        return cameraService.submitCamera(camera);
    }

    @ApiOperation(value = "添加结构化任务")
    @PostMapping(value = "/addVSDTask")
    public R addVSDTask(TaskParamBo taskParamBo) {
        return cameraService.addVSDTask(taskParamBo);
    }

    @ApiOperation(value = "单个,批量删除ipc监控点")
    @PostMapping(value = "/deleteIpc")
    @ApiImplicitParam(name = "cameraIds", value = "逗号分隔的监控点Id")
    public R deleteIpc(String cameraIds) {
        String[] idsArry = cameraIds.split(",");
        for (String id : idsArry) {
            cameraService.deleteCameraAndTaskById(id);
        }
        return R.ok();
    }

    @ApiOperation(value = "设置ipc感兴趣、不感兴趣区域")
    @PostMapping(value = "/addInterestSettingsAndTask")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cameraId", value = "监控点id"),
            @ApiImplicitParam(name = "interestFlag", value = "是否感兴趣: 1,感兴趣 0,不感兴趣"),
            @ApiImplicitParam(name = "interestParam", value = "兴趣区 节点字符串"),
            @ApiImplicitParam(name = "chooseType", value = "1 ：感兴趣区域 ; 2 跨线"),
            @ApiImplicitParam(name = "tripwires", value = "跨线参数"),
            @ApiImplicitParam(name = "overlineType", value = "异常检测"),
            @ApiImplicitParam(name = "enablePartial", value = "开启标注"),
            @ApiImplicitParam(name = "scene", value = "默认场景"),
            @ApiImplicitParam(name = "enableIndependentFaceSnap", value = "独立人脸"),
            @ApiImplicitParam(name = "enableBikeToHuman", value = "室内室外场景")
    })
    public R addInterestSettingsAndTask(String cameraId, String interestFlag, String interestParam,
                                        Integer chooseType, String tripwires, Integer overlineType,
                                        Integer enablePartial,Integer scene, String enableIndependentFaceSnap,
                                        String enableBikeToHuman) {
        if (StringUtils.isEmptyString(cameraId)) {
            return R.error("cameraId不能为空");
        }
        return vsdTaskService.startRealTimeTask(Long.valueOf(cameraId), Integer.valueOf(interestFlag), interestParam,
                chooseType, tripwires, getUserId(), overlineType, enablePartial, scene, enableIndependentFaceSnap,
                enableBikeToHuman);
    }


    @ApiOperation(value = "实时流获取快照")
    @PostMapping(value = "/getCameraSnapshot")
    @ApiImplicitParam(name = "cameraId", value = "监控点Id")
    public R getCameraSnapshot(String cameraId) {
        return cameraController.getCameraSnapshot(cameraId);
    }

    @ApiOperation(value = "停止实时任务")
    @PostMapping(value = "/batchStopRealtimeTask")
    @ApiImplicitParam(name = "taskId", value = "任务Id")
    public R batchStopRealtimeTask(String taskId) {
        R result = R.ok();
        if (StringUtils.isEmptyString(taskId)) {
            return R.error("停止失败: 任务编号为空");
        }
        try {
            String[] strArr = taskId.split(",");
            int arryLength = strArr.length;
            int successNum = 0;
            int failNum = 0;
            Map<String, String> stopRetMap = new HashedMap();
            if (arryLength > 0) {
                for (int i = 0; i < arryLength; i++) {
                    String serialnumber = strArr[i];
                    Map<String, Object> map = vsdTaskService.stopRealtimeTask(serialnumber);
                    String retCode = (String) map.get("retCode");
                    String retDesc = (String) map.get("retDesc");
                    if (!"0".equals(retCode)) {
                        failNum++;
                    } else {
                        successNum++;
                    }
                    stopRetMap.put(serialnumber, retCode + "_" + retDesc);
                }
            }
            result.put("successNum", successNum);
            result.put("failNum", failNum);
            result.put("stopTaskInfo", stopRetMap);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("停止任务失败，taskId：" + taskId);
            return R.error("停止失败");
        }
        return result;
    }

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
        List<CameraVo> cameraData = cameraService.queryCameraAll("2", status, polygonList);
        result.put("list", cameraData);
        return result;
    }

    @ApiOperation(value = "导入监控点")
    @PostMapping(value = "/exportCameraExcel")
    public R exportCameraExcel(MultipartFile file) {
        return cameraService.exportCameraExcel(file, "2");
    }
}
