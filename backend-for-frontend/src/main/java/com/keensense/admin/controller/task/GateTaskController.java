package com.keensense.admin.controller.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.admin.base.BaseController;
import com.keensense.admin.constants.CameraCacheConstants;
import com.keensense.admin.constants.CameraConstants;
import com.keensense.admin.constants.CommonConstants;
import com.keensense.admin.entity.task.Camera;
import com.keensense.admin.entity.task.VsdTaskRelation;
import com.keensense.admin.request.CameraListQueryRequest;
import com.keensense.admin.request.CameraRequest;
import com.keensense.admin.service.task.ICameraService;
import com.keensense.admin.service.task.IVsdTaskRelationService;
import com.keensense.admin.service.task.IVsdTaskService;
import com.keensense.admin.util.EntityObjectConverter;
import com.keensense.admin.util.GoogleMapUtil;
import com.keensense.admin.util.PolygonUtil;
import com.keensense.admin.util.QuerySqlUtil;
import com.keensense.admin.util.RandomUtils;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.vo.CameraVo;
import com.keensense.common.util.PageUtils;
import com.keensense.common.util.R;
import com.loocme.sys.util.StringUtil;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "接入源-卡口")
@RestController
@Slf4j
@RequestMapping("/gateTaskManage")
public class GateTaskController extends BaseController {

    @Resource
    private ICameraService cameraService;

    @Resource
    private IVsdTaskService vsdTaskService;

    @Resource
    private IVsdTaskRelationService vsdTaskRelationService;

    @ApiOperation(value = "查询卡口监控点列表")
    @PostMapping(value = "/cameraListQuery")
    public R selectOnlineCameraListByPage(@RequestBody CameraListQueryRequest request) {
        int page = request.getPage(); // 取得当前页数,注意这是jqgrid自身的参数
        int rows = request.getRows(); // 取得每页显示行数，,注意这是jqgrid自身的参数
        String cameraName = request.getCameraName();
        String status = request.getStatus();
        String isvalid = request.getIsvalid();
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
        map.put("cameraType", CameraConstants.CameraType.Gate);
        Page<CameraVo> pages = new Page<CameraVo>(page, rows);
        Page<CameraVo> pageResult = cameraService.selectGateCameraList(pages, map);
        return R.ok().put("page", new PageUtils(pageResult));
    }

    @ApiOperation(value = "新增卡口监控点")
    @PostMapping("/addGateCamera")
    public R addGateCamera(@RequestBody CameraRequest addCameraRequest) {
        R result = R.ok();
        String cameraUrl = addCameraRequest.getUrl();
        if (StringUtil.isNull(cameraUrl)) {
            return R.error("实时流地址不能为空！");
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
        addCameraRequest.setCameratype(CameraConstants.CameraType.Gate);
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
        return result;
    }

    @ApiOperation(value = "启动卡口图片分析任务")
    @PostMapping(value = "/startGateTask")
    @ApiImplicitParam(name = "cameraId", value = "监控点Id")
    public R startGateRealTimePic(String cameraId) {
        // 查询卡口点位信息
        Camera camera = cameraService.getById(cameraId);
        if (null == camera) {
            return R.error("监控点不存在");
        }
        return vsdTaskService.startNewGateTask(getUserId(),camera);
    }

    @ApiOperation(value = "更新卡口监控点配置")
    @PostMapping(value = "/updateGateCamera")
    public R updateGateCamera(@RequestBody CameraRequest cameraRequest) {
        try {
            cameraRequest.setCameratype(CameraConstants.CameraType.Gate);
            cameraService.updateById(EntityObjectConverter.getObject(cameraRequest, Camera.class));
            //停止任务
            VsdTaskRelation vsdTaskRelation = new VsdTaskRelation();
            vsdTaskRelation.setIsvalid(0);//停止分析
            vsdTaskRelation.setTaskStatus(2);//分析完成
            vsdTaskRelation.setLastUpdateTime(new Date());
            vsdTaskRelationService.update(vsdTaskRelation,new QueryWrapper<VsdTaskRelation>().eq("camera_file_id", cameraRequest.getId()));
            CameraCacheConstants.cleanCameraCacheByCameraType(String.valueOf(cameraRequest.getCameratype()));
            return R.ok();
        } catch (Exception e) {
            log.info(e.getMessage());
            return R.error(e.getMessage());
        }
    }

    @ApiOperation(value = "停止卡口实时图片任务")
    @PostMapping(value = "/stopGateTask")
    @ApiImplicitParam(name = "cameraId", value = "监控点id")
    public R stopGateTask(String cameraId) {
        if (StringUtils.isEmptyString(cameraId)) {
            return R.error("cameraId不能为空");
        }
        VsdTaskRelation vsdTaskRelation = new VsdTaskRelation();
        vsdTaskRelation.setIsvalid(0);//停止分析
        vsdTaskRelation.setTaskStatus(2);//分析完成
        vsdTaskRelation.setLastUpdateTime(new Date());
        vsdTaskRelationService.update(vsdTaskRelation,new QueryWrapper<VsdTaskRelation>().eq("camera_file_id", cameraId));
        return R.ok();
    }


    @ApiOperation(value = "单个,批量删除卡口实时图片任务")
    @PostMapping(value = "/deleteGateTask")
    @ApiImplicitParam(name = "cameraIds", value = "逗号分隔的监控点Id")
    public R deleteGateTask(String cameraIds) {
        String[] idsArry = cameraIds.split(",");
        for (String id : idsArry) {
            return vsdTaskService.deleteGateTask(id);
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
        List<CameraVo> cameraData = cameraService.queryCameraAll("4", status, polygonList);
        result.put("list", cameraData);
        return result;
    }

}
