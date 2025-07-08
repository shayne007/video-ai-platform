package com.keensense.admin.controller.task;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.admin.constants.CameraConstants;
import com.keensense.admin.constants.CommonConstants;
import com.keensense.admin.constants.VideoTaskConstant;
import com.keensense.admin.dto.TaskParamBo;
import com.keensense.admin.entity.task.Camera;
import com.keensense.admin.request.CameraListQueryRequest;
import com.keensense.admin.request.CameraRequest;
import com.keensense.admin.service.ext.PictureObjectTaskService;
import com.keensense.admin.service.task.ICameraService;
import com.keensense.admin.service.task.IVsdTaskService;
import com.keensense.admin.util.AesUtil;
import com.keensense.admin.util.DbPropUtil;
import com.keensense.admin.util.IdUtils;
import com.keensense.admin.util.PolygonUtil;
import com.keensense.admin.util.QuerySqlUtil;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.vo.CameraVo;
import com.keensense.common.util.PageUtils;
import com.keensense.common.util.R;
import com.loocme.sys.datastruct.Var;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
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
 * 任务管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/captureManage")
@Api(tags = "接入源-实时分析-抓拍机接入")
public class CaptureManageController {

    @Resource
    private PictureObjectTaskService pictureObjectTaskService;

    @Resource
    private ICameraService cameraService;

    @Resource
    private IVsdTaskService vsdTaskService;

    @ApiOperation(value = "查询抓拍机监控点列表")
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
        Map<String, Object> map = new HashMap<>();
        String retCameraName = QuerySqlUtil.replaceQueryName(cameraName);
        if (StringUtils.isNotEmpty(retCameraName)) {
            map.put("cameraName", "%" + retCameraName + "%");
        }
        if (StringUtils.isNotEmptyString(status)) {
            map.put("status", status);
        }
        if (StringUtils.isNotEmptyString(isvalid)) {
            map.put("isvalid", isvalid);
        }
        map.put("cameraType", CameraConstants.CameraType.CAPTURE);
        Page<CameraVo> pages = new Page<>(page, rows);
        try {
            Page<CameraVo> pageResult = cameraService.selectPicCameraListByPage(pages, map);
            return R.ok().put("page", new PageUtils(pageResult));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.error(CommonConstants.QUERY_FAILURE);
        }
    }

    @ApiOperation(value = "更新抓拍机监控点配置")
    @PostMapping(value = "/updateCamera")
    public R updateCamera(@RequestBody CameraRequest cameraRequest) {
        cameraRequest.setCameratype(CameraConstants.CameraType.CAPTURE);
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
            return R.error("监控点名称不符合规则");
        }
        return cameraService.updateCamera(cameraRequest);
    }

    @ApiOperation(value = "提交抓拍机分析")
    @PostMapping(value = "/submitCapture")
    public R submitCamera(@RequestBody CameraRequest camera) {
        camera.setCameratype(CameraConstants.CameraType.CAPTURE);
        return cameraService.submitCamera(camera);
    }

    @ApiOperation(value = "添加结构化任务")
    @PostMapping(value = "/addVSDTask")
    public R addVSDTask(TaskParamBo taskParamBo) {
        return cameraService.addVSDTask(taskParamBo);
    }

    public R startPictureTask(String cameraId) throws Exception{
        R result = R.ok();
        String resultJson = "";
        Map<String, Object> paramMap = new HashedMap();
        Map<String, Object> param = new HashedMap();
        String serialnumber = String.valueOf(IdUtils.getTaskId());
        paramMap.put("serialnumber", serialnumber);//任务序列号
        paramMap.put("cameraId", cameraId + "");
        Camera camera = cameraService.selectCameraById(cameraId);
        if (camera != null) {
            param.put("name", camera.getName());
            param.put("deviceId", camera.getExtcameraid());//设备ID
            param.put("ip", camera.getIp());
            param.put("port", camera.getPort1());
            param.put("username", camera.getAccount());
            String PASSWORD_KEY = DbPropUtil.getString("capture.service.password.key", "abcdef0123456789");
            String pwd = AesUtil.encrypt(camera.getPassword(), PASSWORD_KEY);
            param.put("password", pwd);
            param.put("deviceNo", camera.getExtcameraid());
            param.put("deviceNo", "deviceID20bytesTotal");
            param.put("type", VideoTaskConstant.Type.PICTURE);//任务类型
            param.put("userId", VideoTaskConstant.USER_ID.CAPTURE);
        }
        paramMap.put("param", com.alibaba.fastjson.JSONObject.toJSONString(param));

        resultJson = pictureObjectTaskService.startPictureTaskService(paramMap);
        Var resultVar = Var.fromJson(resultJson);
        result.put("msg", resultVar.getString("desc"));
        return result;
    }

    /**
     * 删除抓拍机监控点(支持删除多个)
     * 1、先删除抓拍机的任务
     * 2、在删除监控点
     */
    @ApiOperation(value = "删除抓拍机监控点(支持删除多个")
    @PostMapping(value = "/deletePictureRealTimeList")
    @ApiImplicitParam(name = "cameraIds", value = "逗号分隔的监控点Id")
    public R deletePictureRealTimeList(String cameraIds) {
        if (StringUtils.isEmptyString(cameraIds)) {
            return R.error("请选择需要删除的监控点");
        }

        String[] idsArry = cameraIds.split(",");
        for (String id : idsArry) {

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("cameraId", id);
            String resultJson = pictureObjectTaskService.deletePictureTaskService(paramMap);
            Var resultVar = Var.fromJson(resultJson);
            String ret = resultVar.getString("ret");
            if ("0".equals(ret) || "-1".equals(ret) || "-202".equals(ret)) {
                cameraService.deleteCameraAndTaskById(id);
            } else {
                return R.error(resultVar.getString("desc"));
            }
        }
        return R.ok();
    }

    @ApiOperation(value = "启动抓拍机任务")
    @PostMapping(value = "/startPictureRealTimeList")
    @ApiImplicitParam(name = "cameraIds", value = "逗号分隔的监控点Id")
    public R startRealTimeBatchPic(String cameraIds) throws Exception{
        R result = R.ok();
        if (StringUtils.isEmptyString(cameraIds)) {
            return R.error("id is null");
        }
        String[] array = cameraIds.split(",");
        for (String cameraId : array) {
            result = startPictureTask(cameraId);
        }
        return result;
    }

    /**
     * 停止任务(支持停止多个)
     */
    @ApiOperation(value = "停止抓拍机任务(支持停止多个)")
    @PostMapping(value = "/stopPictureRealTimeList")
    @ApiImplicitParam(name = "taskIds", value = "逗号分隔的任务Id")
    public R stopPictureRealTimeList(String taskIds) {
        if (StringUtils.isNotEmptyString(taskIds)) {
            String[] strArr = taskIds.split(",");
            for (int i = 0; i < strArr.length; i++) {
                String serialnumber = strArr[i];
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("serialnumber", serialnumber);
                String resultJson = pictureObjectTaskService.stopPictureTaskService(paramMap);
                Var resultVar = Var.fromJson(resultJson);
                String ret = resultVar.getString("ret");
                if (!("0".equals(ret) || "-1".equals(ret) || "-202".equals(ret))) {
                    return R.error(resultVar.getString("desc"));
                }
            }
        }
        return R.ok();
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
        List<CameraVo> cameraData = cameraService.queryCameraAll("3", status, polygonList);
        result.put("list", cameraData);
        return result;
    }

    @ApiOperation(value = "导入监控点")
    @PostMapping(value = "/exportCameraExcel")
    public R exportCameraExcel(MultipartFile file) {
        return cameraService.exportCameraExcel(file, "3");
    }

}
