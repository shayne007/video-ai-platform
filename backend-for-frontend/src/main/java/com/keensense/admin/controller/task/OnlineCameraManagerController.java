package com.keensense.admin.controller.task;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.admin.base.BaseController;
import com.keensense.admin.constants.CameraCacheConstants;
import com.keensense.admin.constants.CameraConstants;
import com.keensense.admin.constants.CommonConstants;
import com.keensense.admin.constants.VideoTaskConstant;
import com.keensense.admin.entity.task.Camera;
import com.keensense.admin.request.CameraListQueryRequest;
import com.keensense.admin.request.CameraRequest;
import com.keensense.admin.service.ext.VideoObjextTaskService;
import com.keensense.admin.service.task.ICameraService;
import com.keensense.admin.service.task.IVsdTaskRelationService;
import com.keensense.admin.service.task.IVsdTaskService;
import com.keensense.admin.util.DbPropUtil;
import com.keensense.admin.util.EntityObjectConverter;
import com.keensense.admin.util.GoogleMapUtil;
import com.keensense.admin.util.PolygonUtil;
import com.keensense.admin.util.QuerySqlUtil;
import com.keensense.admin.util.RandomUtils;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.vo.CameraVo;
import com.keensense.admin.vo.TaskParamVo;
import com.keensense.admin.vo.VsdTaskVo;
import com.keensense.common.util.PageUtils;
import com.keensense.common.util.R;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.exception.HttpConnectionException;
import com.loocme.sys.util.PatternUtil;
import com.loocme.sys.util.PostUtil;
import com.loocme.sys.util.StringUtil;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@Api(tags = "接入源-实时分析-联网实时视频分析")
@RestController
@RequestMapping("/onlineCameraManager")
public class OnlineCameraManagerController extends BaseController {

    @Autowired
    private ICameraService cameraService;

    @Resource
    private IVsdTaskService vsdTaskService;

    @Resource
    private IVsdTaskRelationService vsdTaskRelationService;

    @Autowired
    private VideoObjextTaskService videoObjextTaskService;

    @Autowired
    private CameraController cameraController;

    @ApiOperation(value = "查询vas监控点列表")
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
        if (StringUtils.isNotEmpty(retCameraName)) {
            map.put("cameraName", "%" + retCameraName + "%");
        }
        if (StringUtils.isNotEmptyString(status)) {
            map.put("status", status);
        }
        if (StringUtils.isNotEmptyString(isvalid)) {
            map.put("isvalid", isvalid);
        }
        if (null != overlineType) {
            map.put("overlineType", overlineType);
        }
        map.put("cameraType", CameraConstants.CameraType.VAS);
        map.put("fromType", VideoTaskConstant.FROM_TYPE.REAL);
        Page<CameraVo> pages = new Page<CameraVo>(page, rows);
        Page<CameraVo> pageResult = cameraService.selectOnlineAndIpCCameraListByPage(pages, map);
        return R.ok().put("page", new PageUtils(pageResult));
    }

    @ApiOperation(value = "更新vas监控点配置")
    @PostMapping(value = "/updateCamera")
    public R updateCamera(@RequestBody CameraRequest cameraRequest) {
        cameraRequest.setCameratype(CameraConstants.CameraType.VAS);
        if (StringUtils.isEmpty(cameraRequest.getName().trim())) {
            return R.error("不允许全为空格");
        }
        if (cameraRequest.getName().length() > CommonConstants.REGEX.CAMERA_CASE_LENGTH) {
            return R.error("监控点名称长度不能超过32位");
        }
        if (StringUtils.checkRegex_false(cameraRequest.getName(),
                CommonConstants.REGEX.CAMERA_CASE_RELINE_FILE_NAME_NOT_SUPPORT)) {
            return R.error("监控点名称不符规则");
        }
        return cameraService.updateCamera(cameraRequest);
    }

    @ApiOperation(value = "同步vas点位")
    @PostMapping(value = "/syncVasData")
    public R syncVasData() {
        try {
            String serviceIp = DbPropUtil.getString("1000video.ip", "127.0.0.1");
            String servicePort = DbPropUtil.getString("1000video.port", "8060");
            String extUrl = "http://" + serviceIp + ":" + servicePort + "/1000video/syncVasData";
            //"http://localhost:8060/1000video/syncVasData";
            PostUtil.requestContent(extUrl, "application/json", "");
        } catch (HttpConnectionException e) {
            e.printStackTrace();
            return R.error("同步vas点位失败");
        }
        return R.ok();
    }

    @ApiOperation(value = "新增vas监控点")
    @PostMapping("/addOnlineCamera")
    public R addOnlineCamera(@RequestBody CameraRequest addCameraRequest) {
        R result = R.ok();
        String cameraUrl = addCameraRequest.getUrl();
        // 实时流地址为空
        if (StringUtil.isNull(cameraUrl)) {
            return R.error("实时流地址不能为空！");
        }
        if (PatternUtil.isNotMatch(cameraUrl, "^vas://name=.+&psw=.+&srvip=.+&srvport=\\d+&devid=.+&.*$")) {
            return R.error("点位信息有误");
        }
        if (StringUtils.isEmpty(addCameraRequest.getName().trim())) {
            return R.error("不允许全为空格");
        }
        if (addCameraRequest.getName().length() > CommonConstants.REGEX.CAMERA_CASE_LENGTH) {
            return R.error("长度不能超过32位");
        }
        if (StringUtils.checkRegex_false(addCameraRequest.getName(),
                CommonConstants.REGEX.CAMERA_CASE_RELINE_FILE_NAME_NOT_SUPPORT)) {
            return R.error("监控点名称不符规则");
        }
        addCameraRequest.setName(addCameraRequest.getName().trim());
        Long cameraId = RandomUtils.getCurrentTime();
        addCameraRequest.setId(cameraId);
        addCameraRequest.setStatus(CameraConstants.CameraStatus.START);

        if (cameraUrl.startsWith(CameraConstants.CameraUrlPrefix.PREFIX_VAS)) {
            addCameraRequest.setCameratype(CameraConstants.CameraType.VAS);
        } else if (cameraUrl.startsWith(CameraConstants.CameraUrlPrefix.PREFIX_RT)) {
            addCameraRequest.setCameratype(CameraConstants.CameraType.RTSP);
        }

        addCameraRequest.setType(1L);
        addCameraRequest.setBrandid(2L);
        addCameraRequest.setInsertFlag(true);
        String[] latAndLon = GoogleMapUtil.getPointByDecrypt(addCameraRequest.getLatitude(), addCameraRequest.getLongitude());
        addCameraRequest.setLatitude(latAndLon[0]);
        addCameraRequest.setLongitude(latAndLon[1]);
        result.put("camera", addCameraRequest);
        Camera camera = EntityObjectConverter.getObject(addCameraRequest, Camera.class);
        Boolean insert = cameraService.save(camera);
        if (insert) {
            CameraCacheConstants.cleanCameraCacheByCameraType(String.valueOf(addCameraRequest.getCameratype()));
        }
        // 如果勾选了跳转
        if (checkAutoAnalysis(addCameraRequest.getStarttask())) {
            String serialnumber = vsdTaskRelationService.getSerialnumber();
            String url = "";

            Map<String, Object> paramMap = new HashedMap();
            paramMap.put("serialnumber", serialnumber);//任务序列号
            paramMap.put("type", "objext");//任务类型
            paramMap.put("url", url);//视频路径
            paramMap.put("cameraId", addCameraRequest.getId());//监控点id
            paramMap.put("deviceId", addCameraRequest.getExtcameraid());//设备ID
            paramMap.put("name", addCameraRequest.getName());//设备ID
            String resultJson = videoObjextTaskService.addVsdTaskService(paramMap, true);
            Var resultVar = Var.fromJson(resultJson);
            String retCode = resultVar.getString("ret");
            if ("0".equals(retCode)) {
            } else {
                return R.error(resultVar.getString("desc"));
            }
        }
        return result;
    }

    /**
     * 检查是否需要进行自动分析
     *
     * @param starttask 启动标识 ，启动:true； 不启动:false
     * @return
     */
    private boolean checkAutoAnalysis(String starttask) {
        return StringUtils.isNotEmptyString(starttask) && "true".equals(starttask);
    }

    /**
     * 单个,批量删除vas监控点
     */
    @ApiOperation(value = "单个,批量删除vas监控点")
    @PostMapping(value = "/batchDelete")
    @ApiImplicitParam(name = "cameraIds", value = "逗号分隔的监控点Id")
    public R batchDelete(String cameraIds) {
        if (StringUtils.isNotEmptyString(cameraIds)) {
            try {
                cameraService.deleteCameraByIds(cameraIds);
            } catch (Exception e) {
                e.printStackTrace();
                return R.error(CommonConstants.DELETE_FAILURE);
            }
        }
        return R.ok();
    }

    @ApiOperation(value = "实时流获取快照")
    @PostMapping(value = "/getCameraSnapshot")
    @ApiImplicitParam(name = "cameraId", value = "监控点Id")
    public R getCameraSnapshot(String cameraId) {
        return cameraController.getCameraSnapshot(cameraId);
    }

    @ApiOperation(value = "设置vas感兴趣、不感兴趣区域,启动任务")
    @PostMapping(value = "/addInterestSettingsAndTask")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cameraId", value = "监控点id"),
            @ApiImplicitParam(name = "interestFlag", value = "是否感兴趣: 1,感兴趣 0,不感兴趣"),
            @ApiImplicitParam(name = "interestParam", value = "兴趣区 节点字符串"),
            @ApiImplicitParam(name = "chooseType", value = "1 ：感兴趣区域 ; 2 跨线"),
            @ApiImplicitParam(name = "tripwires", value = "跨线参数"),
            @ApiImplicitParam(name = "overlineType", value = "异常检测"),
            @ApiImplicitParam(name = "enablePartial", value = "是否开启标注"),
            @ApiImplicitParam(name = "scene", value = "默认场景"),
            @ApiImplicitParam(name = "enableIndependentFaceSnap", value = "独立人脸"),
            @ApiImplicitParam(name = "enableBikeToHuman", value = "室内室外")
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

    @ApiOperation(value = "停止实时任务")
    @PostMapping(value = "/batchStopRealtimeTask")
    @ApiImplicitParam(name = "taskId", value = "任务Id")
    public R batchStopRealtimeTask(String taskId) {
        R result = R.ok();
        if (StringUtils.isEmptyString(taskId)) {
            return R.error("停止失败: 任务编号为空");
        }
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
        List<CameraVo> cameraData = cameraService.queryCameraAll("1", status, polygonList);
        result.put("list", cameraData);
        return result;
    }

    /**
     * 查询点位信息(根据名称)
     */
    @ApiOperation(value = "查询点位信息(根据名称)")
    @PostMapping(value = "/queryCamerasByName")
    @ApiImplicitParam(name = "cameraName", value = "点位信息")
    public R queryCamerasByName(@RequestParam("cameraName") String cameraName) {
        if (StringUtils.isNotEmptyString(cameraName)) {
            if (StringUtils.isEmpty(cameraName.trim())) {
                return R.error("不允许全为空格");
            }
            if (cameraName.length() > CommonConstants.REGEX.CAMERA_CASE_LENGTH) {
                return R.error("长度不能超过32位");
            }
            if (StringUtils.checkRegex_false(cameraName,
                    CommonConstants.REGEX.CAMERA_CASE_RELINE_FILE_NAME_NOT_SUPPORT)) {
                return R.error("监控点名称不符合规则");
            }
        }
        R result = R.ok();
        List<Camera> cameraData = cameraService.list(new QueryWrapper<Camera>().like("name", cameraName));
        result.put("list", cameraData);
        return result;
    }
}
