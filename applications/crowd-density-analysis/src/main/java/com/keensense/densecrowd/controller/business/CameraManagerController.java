package com.keensense.densecrowd.controller.business;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.common.util.PageUtils;
import com.keensense.common.util.R;
import com.keensense.common.util.RandomUtils;
import com.keensense.densecrowd.base.BaseController;
import com.keensense.densecrowd.constant.VideoTaskConstant;
import com.keensense.densecrowd.entity.task.Camera;
import com.keensense.densecrowd.entity.task.VsdTaskRelation;
import com.keensense.densecrowd.request.CameraListQueryRequest;
import com.keensense.densecrowd.request.CameraRequest;
import com.keensense.densecrowd.service.task.ICameraService;
import com.keensense.densecrowd.service.task.IVsdTaskRelationService;
import com.keensense.densecrowd.service.task.IVsdTaskService;
import com.keensense.densecrowd.util.CameraConstants;
import com.keensense.densecrowd.util.CommonConstants;
import com.keensense.densecrowd.util.EntityObjectConverter;
import com.keensense.densecrowd.util.GoogleMapUtil;
import com.keensense.densecrowd.util.QuerySqlUtil;
import com.keensense.densecrowd.util.StringUtils;
import com.keensense.densecrowd.vo.CameraVo;
import com.loocme.sys.util.PatternUtil;
import com.loocme.sys.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 11:54 2019/6/19
 * @Version v0.1
 */
@Slf4j
@Api(tags = "接入源-监控点位")
@RestController
@RequestMapping("/cameraManager")
public class CameraManagerController extends BaseController {

    @Resource
    ICameraService cameraService;

    @Resource
    private IVsdTaskService vsdTaskService;

    @Autowired
    private CameraManagerController cameraController;

    @Resource
    private IVsdTaskRelationService vsdTaskRelationService;

    @ApiOperation(value = "查询监控点列表")
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
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(cameraName)) {
            String retCameraName = QuerySqlUtil.replaceQueryName(cameraName);
            map.put("cameraName", "%" + retCameraName + "%");
        }
        if (StringUtils.isNotEmptyString(status)) {
            if (!"-1".equals(status)) {
                map.put("status", status);
            }
        }
        map.put("isvalid", isvalid);
        Page<CameraVo> pages = new Page<>(page, rows);
        Page<CameraVo> pageResult = cameraService.selectOnlineAndIpCCameraListByPage(pages, map);
        return R.ok().put("page", new PageUtils(pageResult));
    }

    @ApiOperation(value = "更新监控点")
    @PostMapping(value = "/updateCamera")
    public R updateCamera(@RequestBody CameraRequest cameraRequest) {
        if (cameraRequest.getUrl().indexOf("vas") == 0) {
            cameraRequest.setCameratype(CameraConstants.CameraType.VAS);
        } else {
            cameraRequest.setCameratype(CameraConstants.CameraType.RTSP);
        }
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


    @ApiOperation(value = "新增监控点")
    @PostMapping("/addCamera")
    public R addCamera(@RequestBody CameraRequest addCameraRequest) {
        R result = R.ok();
        String cameraUrl = addCameraRequest.getUrl();
        // 实时流地址为空
        if (StringUtil.isNull(cameraUrl)) {
            return R.error("实时流地址不能为空！");
        }
        if (cameraUrl.indexOf("vas") == 0) {
            if (PatternUtil.isNotMatch(cameraUrl, "^vas://name=.+&psw=.+&srvip=.+&srvport=\\d+&devid=.+&.*$")) {
                return R.error("点位信息有误");
            }
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
        return R.ok();
    }


    @ApiOperation(value = "单个,批量删除监控点")
    @PostMapping(value = "/deleteCamera")
    @ApiImplicitParam(name = "cameraIds", value = "逗号分隔的监控点Id")
    public R deleteCamera(String cameraIds) {
        String[] idsArry = cameraIds.split(",");
        for (String id : idsArry) {
            cameraService.deleteCameraAndTaskById(id);
        }
        return R.ok();
    }

    @ApiOperation(value = "根据监控点Id查询启动任务状态")
    @PostMapping(value = "/queryTaskStatus")
    @ApiImplicitParam(name = "cameraId", value = "监控点Id")
    public R queryTaskStatus(String cameraId) {
        VsdTaskRelation vsdTaskRelation = vsdTaskRelationService.getOne(new QueryWrapper<VsdTaskRelation>().eq("camera_id", cameraId).in("from_type", VideoTaskConstant.TASK_TYPE.REAL, VideoTaskConstant.TASK_TYPE.IPC));
        if (vsdTaskRelation == null) {
            vsdTaskRelation = new VsdTaskRelation();
        }
        return R.ok().put("data", vsdTaskRelation);
    }
}
