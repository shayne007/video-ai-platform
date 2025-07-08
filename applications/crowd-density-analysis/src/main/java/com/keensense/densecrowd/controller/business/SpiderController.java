package com.keensense.densecrowd.controller.business;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.common.platform.bo.video.CrowdDensity;
import com.keensense.common.platform.bo.video.CrowdDensityQuery;
import com.keensense.common.platform.enums.TypeEnums;
import com.keensense.common.util.DateUtil;
import com.keensense.common.util.R;
import com.keensense.densecrowd.dto.AlarmHeadVo;
import com.keensense.densecrowd.entity.task.Camera;
import com.keensense.densecrowd.entity.task.VsdTaskRelation;
import com.keensense.densecrowd.service.ext.CrowdDensityService;
import com.keensense.densecrowd.service.ext.VideoObjextTaskService;
import com.keensense.densecrowd.service.sys.ICfgMemPropsService;
import com.keensense.densecrowd.service.task.ICameraService;
import com.keensense.densecrowd.service.task.IVsdTaskRelationService;
import com.keensense.densecrowd.util.AlarmPageUtils;
import com.keensense.densecrowd.util.CameraConstants;
import com.keensense.densecrowd.util.StringUtils;
import com.keensense.densecrowd.vo.AlarmCameraVo;
import com.keensense.densecrowd.vo.AlarmDensecrowdRequest;
import com.keensense.densecrowd.vo.AlarmDeviceRequest;
import com.keensense.densecrowd.vo.AlarmDeviceRoiRequest;
import com.keensense.densecrowd.vo.AlarmTaskAddRequest;
import com.keensense.densecrowd.vo.AlarmTaskDelRequest;
import com.keensense.densecrowd.vo.AlarmTaskQueryRequest;
import com.keensense.densecrowd.vo.roi.Points;
import com.keensense.densecrowd.vo.roi.Rois;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 14:25 2019/9/25
 * @Version v0.1
 */
@Slf4j
@Api(tags = "人群密度平台接口规范1.0")
@RestController
@RequestMapping("/spider/cloudWalkService")
public class SpiderController {

    @Autowired
    ICameraService cameraService;

    @Autowired
    CrowdDensityService crowdDensityService;

    @Autowired
    IVsdTaskRelationService vsdTaskRelationService;

    @Autowired
    VideoObjextTaskService videoObjextTaskService;

    @Autowired
    ICfgMemPropsService cfgMemPropsService;

    /**
     * 1.1	相机ROI设置
     * 格式见人群密度平台接口规范V1.0文档
     *
     * @return
     */
    @ApiOperation("1.1 相机ROI设置")
    @PostMapping("/device/roi")
    public R deviceRoi(@RequestBody @Valid AlarmDeviceRoiRequest alarmDeviceRoiRequest) {
        List<Camera> cameras = cameraService.selectCameraByDeviceId(alarmDeviceRoiRequest.getDeviceId());
        if (cameras.isEmpty()) {
            return R.error("设备编号不存在");
        }
        int width = alarmDeviceRoiRequest.getParam().getWidth();
        int height = alarmDeviceRoiRequest.getParam().getHeight();
        if (width == 0 || height == 0) {
            return R.error("相机分辨率不能为0");
        }
        List<Rois> rois = alarmDeviceRoiRequest.getParam().getRois();
        Set<String> vertices = new HashSet<>();
        for (Rois roi : rois) {
            StringBuilder vertice = new StringBuilder();
            List<Points> points = roi.getPoints();
            if (points.size() < 3) {
                return R.error("ROI的点坐标不能少于3个");
            }
            for (Points point : points) {
                if (vertice.length() != 0) {
                    vertice.append(",");
                }
                BigDecimal x = new BigDecimal(point.getX()).divide(new BigDecimal(width), 5, BigDecimal.ROUND_HALF_EVEN);
                vertice.append(x.doubleValue() < 1 ? x : 1);
                vertice.append(",");
                BigDecimal y = new BigDecimal(point.getY()).divide(new BigDecimal(height), 5, BigDecimal.ROUND_HALF_EVEN);
                vertice.append(y.doubleValue() < 1 ? y : 1);
            }
            vertices.add("\"" + vertice + "\"");
        }
        for (Camera camera : cameras) {
            Camera cameraTemp = new Camera();
            cameraTemp.setId(camera.getId());
            cameraTemp.setFollwarea(vertices.toString());
            cameraService.updateById(cameraTemp);
        }
        return R.ok();
    }

    /**
     * 1.2	人群密度布控任务下发
     *
     * @return
     */
    @ApiOperation("1.2\t人群密度布控任务下发")
    @PostMapping("/density/alarm/distribute")
    public R alarmDistribute(@RequestBody @Valid AlarmTaskAddRequest alarmTaskAddRequest) {
        Camera camera = cameraService.getById(alarmTaskAddRequest.getDeviceId());
        log.info("camera:" + camera);
        if (camera == null) {
            return R.error("设备编号不存在");
        }
        if (alarmTaskAddRequest.getStartTime() != 0 && alarmTaskAddRequest.getEndTime() != 0) {
            if (alarmTaskAddRequest.getStartTime() > alarmTaskAddRequest.getEndTime()) {
                return R.error("布控开始时间不能大于结束时间");
            }
        }
        VsdTaskRelation relation = vsdTaskRelationService.queryVsdTaskRelationByCameraFileId(camera.getId() + "", null);
        if (relation != null) {
            return R.error("点位任务已存在");
        }
        Map<String, Object> paramMap = new HashMap();
        paramMap.put("url", CameraConstants.transUrl(cfgMemPropsService.getWs2ServerIp(), cfgMemPropsService.getWs2ServerPort(), camera.getUrl(), "real_Platform"));
        paramMap.put("serialnumber", vsdTaskRelationService.getSerialnumber());
        paramMap.put("type", TypeEnums.PEROSON_DESITTY.getValue());
        paramMap.put("taskType", "1");

        paramMap.put("deviceId", alarmTaskAddRequest.getDeviceId());
        paramMap.put("cameraId", camera.getId());
        paramMap.put("fromType", 4);
        paramMap.put("name", alarmTaskAddRequest.getMonitorName());
        if (StringUtils.isNotEmpty(camera.getFollwarea())) {
            paramMap.put("isInterested", true);
            paramMap.put("udrVertices", camera.getFollwarea());
        }
        paramMap.put("alarmEndTime", alarmTaskAddRequest.getEndTime());
        paramMap.put("alarmStartTime", alarmTaskAddRequest.getStartTime());
        paramMap.put("alarmInterval", alarmTaskAddRequest.getInterval());
        paramMap.put("alarmThreshold", alarmTaskAddRequest.getThreshold());
        log.info("paramMap:" + paramMap);
        String resultJson = videoObjextTaskService.addVsdTaskService(paramMap, true);
        return R.ok();
    }

    /**
     * 1.3	布控任务修改
     *
     * @return
     */
    @ApiOperation("1.3\t布控任务修改")
    @PostMapping("/density/alarm/update")
    public R alarmUpdate(@RequestBody @Valid AlarmTaskAddRequest alarmTaskAddRequest) {
        if (StringUtils.isEmpty(alarmTaskAddRequest.getId())) {
            return R.error("布控任务不能为空");
        }
        VsdTaskRelation vsdTaskRelation = vsdTaskRelationService.getById(alarmTaskAddRequest.getId());
        if (vsdTaskRelation == null) {
            return R.error("布控任务不存在");
        }
        if (!vsdTaskRelation.getCameraId().equals(alarmTaskAddRequest.getDeviceId())) {
            return R.error("布控任务不存在");
        }
        Camera camera = cameraService.getById(alarmTaskAddRequest.getDeviceId());
        if (camera == null) {
            return R.error("设备编号不存在");
        }
        if (alarmTaskAddRequest.getStartTime() != 0 && alarmTaskAddRequest.getEndTime() != 0) {
            if (alarmTaskAddRequest.getStartTime() > alarmTaskAddRequest.getEndTime()) {
                return R.error("布控开始时间不能大于结束时间");
            }
        }

        Map<String, Object> paramMap = new HashMap();
        String url = CameraConstants.transUrl(cfgMemPropsService.getWs2ServerIp(), cfgMemPropsService.getWs2ServerPort(), camera.getUrl(), "real_Platform");
        paramMap.put("url", url);
        paramMap.put("name", alarmTaskAddRequest.getMonitorName());
        if (StringUtils.isNotEmpty(camera.getFollwarea())) {
            paramMap.put("isInterested", true);
            paramMap.put("udrVertices", camera.getFollwarea());
        }
        paramMap.put("alarmEndTime", alarmTaskAddRequest.getEndTime());
        paramMap.put("alarmStartTime", alarmTaskAddRequest.getStartTime());
        paramMap.put("alarmInterval", alarmTaskAddRequest.getInterval());
        paramMap.put("alarmThreshold", alarmTaskAddRequest.getThreshold());
        paramMap.put("serialnumber", vsdTaskRelation.getSerialnumber());
        String pause = videoObjextTaskService.pauseVsdTaskService(paramMap);
        log.info("pause:" + pause);

        {
            VsdTaskRelation taskRelation = new VsdTaskRelation();
            vsdTaskRelation.setId(vsdTaskRelation.getId());
            vsdTaskRelation.setLastUpdateTime(vsdTaskRelation.getCreatetime());
            vsdTaskRelation.setIsvalid(1);
            vsdTaskRelation.setTaskStatus(0);
            vsdTaskRelation.setTaskName(alarmTaskAddRequest.getMonitorName());
            vsdTaskRelation.setTaskName(alarmTaskAddRequest.getMonitorName());
            vsdTaskRelation.setUrl(url);

            if (alarmTaskAddRequest.getStartTime() != null) {
                if (alarmTaskAddRequest.getStartTime() > 0) {
                    vsdTaskRelation.setAlarmStartTime(new Date(alarmTaskAddRequest.getStartTime()));
                }
            }
            if (alarmTaskAddRequest.getEndTime() != null) {
                if (alarmTaskAddRequest.getEndTime() > 0) {
                    vsdTaskRelation.setAlarmEndTime(new Date(alarmTaskAddRequest.getEndTime()));
                }
            }
            if (vsdTaskRelation.getAlarmStartTime() != null && System.currentTimeMillis() < vsdTaskRelation.getAlarmStartTime().getTime()) {
                vsdTaskRelation.setIsvalid(2);
            } else if (vsdTaskRelation.getAlarmEndTime() != null && System.currentTimeMillis() < vsdTaskRelation.getAlarmEndTime().getTime()) {
                vsdTaskRelation.setIsvalid(2);
            } else {
                vsdTaskRelation.setIsvalid(1);
            }
            if (alarmTaskAddRequest.getInterval() != null) {
                vsdTaskRelation.setAlarmInterval(alarmTaskAddRequest.getInterval());
            }
            if (alarmTaskAddRequest.getThreshold() != null) {
                vsdTaskRelation.setAlarmThreshold(alarmTaskAddRequest.getThreshold());
            }
            boolean flag = vsdTaskRelationService.saveOrUpdate(vsdTaskRelation);
            log.info("flag:" + flag);
        }
        String update = videoObjextTaskService.updateVsdTaskService(paramMap);
        log.info("update:" + update);
        if (vsdTaskRelation.getIsvalid() == 2) {
            return R.ok();
        }
        String continues = videoObjextTaskService.continueVsdTaskService(paramMap);
        log.info("continues:" + continues);
        return R.ok();
    }

    /**
     * 1.4	布控任务删除
     *
     * @return
     */
    @ApiOperation("1.4\t布控任务删除")
    @PostMapping("/density/alarm/delete")
    public R alarmDelete(@RequestBody @Valid AlarmTaskDelRequest alarmTaskDelRequest) {
        VsdTaskRelation vsdTaskRelation = vsdTaskRelationService.getById(alarmTaskDelRequest.getId());
        if (vsdTaskRelation == null) {
            return R.error("布控任务不存在");
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("serialnumber", vsdTaskRelation.getSerialnumber());
        String ret = videoObjextTaskService.deleteVsdTaskService(paramMap);
        JSONObject retJson = JSONObject.parseObject(ret);
        int retcode = retJson.getInteger("ret");
        String retMsg = retJson.getString("desc");
        if (retcode == 0 || retcode == 7) {
            vsdTaskRelationService.removeById(vsdTaskRelation);
        }
        if (retcode == 0) {
            return R.ok();
        } else {
            return R.error(retMsg);
        }

    }

    /**
     * 1.5	布控任务查询
     *
     * @return
     */
    @ApiOperation("1.5 布控任务查询")
    @PostMapping("/density/alarm/gets")
    public R alarmGets(@RequestBody @Valid AlarmTaskQueryRequest alarmTaskQueryRequest) {
        IPage<VsdTaskRelation> pages = vsdTaskRelationService.queryListByDeviceIds(alarmTaskQueryRequest);
        List<VsdTaskRelation> list = pages.getRecords();

        //转换数据
        IPage<AlarmTaskAddRequest> revertPage = new Page<>();
        List<AlarmTaskAddRequest> requests = new ArrayList<>();
        for (VsdTaskRelation relation : list) {
            Camera camera = cameraService.getById(relation.getCameraId());
            AlarmTaskAddRequest alarmTaskAddRequest = new AlarmTaskAddRequest();
            alarmTaskAddRequest.setDeviceId(relation.getCameraId());
            alarmTaskAddRequest.setId(relation.getId() + "");
            if (relation.getAlarmEndTime() != null) {
                alarmTaskAddRequest.setEndTime(relation.getAlarmEndTime().getTime());
            }
            if (relation.getAlarmStartTime() != null) {
                alarmTaskAddRequest.setStartTime(relation.getAlarmStartTime().getTime());
            }
            alarmTaskAddRequest.setInterval(relation.getAlarmInterval());
            alarmTaskAddRequest.setMonitorName(relation.getTaskName());
            alarmTaskAddRequest.setThreshold(relation.getAlarmThreshold());
            alarmTaskAddRequest.setStatus(relation.getIsvalid());
            requests.add(alarmTaskAddRequest);
        }
        revertPage.setRecords(requests);
        revertPage.setTotal(pages.getTotal());
        revertPage.setCurrent(pages.getCurrent());
        revertPage.setSize(pages.getSize());
        return R.ok().put("datas", new AlarmPageUtils(revertPage));
    }

    /**
     * 1.8	人群密度查询v2
     *
     * @return
     */
    @ApiOperation("1.8 人群密度查询v2")
    @PostMapping("/device/head/gets/v2")
    public R headGets(@RequestBody @Valid AlarmDensecrowdRequest alarmDensecrowdRequest) {
        R result = R.ok();
        CrowdDensityQuery crowdDensityQuery = new CrowdDensityQuery();
        List<String> deviceId = alarmDensecrowdRequest.getDeviceId();
        if (deviceId != null && !deviceId.isEmpty()) {
            crowdDensityQuery.setDeviceIds(String.join(",", deviceId));
        }
        crowdDensityQuery.setPageNo(alarmDensecrowdRequest.getCurrentPage());
        crowdDensityQuery.setPageSize(alarmDensecrowdRequest.getPageSize());
        if (alarmDensecrowdRequest.getStartTime() != null && alarmDensecrowdRequest.getStartTime() != 0) {
            crowdDensityQuery.setStartTime(DateUtil.formatDate(new Date(alarmDensecrowdRequest.getStartTime()), DateUtil.FORMAT_6));
        }
        if (alarmDensecrowdRequest.getEndTime() != null && alarmDensecrowdRequest.getEndTime() != 0) {
            crowdDensityQuery.setEndTime(DateUtil.formatDate(new Date(alarmDensecrowdRequest.getEndTime()), DateUtil.FORMAT_6));
        }
        Page<CrowdDensity> pages = crowdDensityService.getDensityResultList(crowdDensityQuery);
        List<CrowdDensity> crowdDensities = pages.getRecords();

        Page<AlarmHeadVo> headVoPage = new Page<>();

        headVoPage.setTotal(pages.getTotal());
        headVoPage.setCurrent(pages.getCurrent());
        headVoPage.setSize(pages.getSize());
        List<AlarmHeadVo> alarmCameraVos = new ArrayList<>();
        for (CrowdDensity crowdDensity : crowdDensities) {

            VsdTaskRelation vsdTaskRelation = vsdTaskRelationService.getOne(new QueryWrapper<VsdTaskRelation>().eq("serialnumber", crowdDensity.getSerialnumber()));
            if (vsdTaskRelation != null) {
                AlarmHeadVo alarmHeadVo = new AlarmHeadVo();
                alarmHeadVo.setDeviceId(vsdTaskRelation.getCameraId());
                alarmHeadVo.setDeviceName(crowdDensity.getCameraName());
                alarmHeadVo.setHeadcount(crowdDensity.getCount());
                alarmHeadVo.setTime(DateUtil.parse(crowdDensity.getCreateTime()).getTime());
                alarmHeadVo.setDensityInfo(crowdDensity.getDensityInfo());
                alarmCameraVos.add(alarmHeadVo);
            }
        }
        headVoPage.setRecords(alarmCameraVos);
        return result.put("page", new AlarmPageUtils(headVoPage));
    }

    /**
     * 1.9	像机分页查询
     *
     * @return
     */
    @ApiOperation("1.9 像机分页查询")
    @PostMapping("/device/pages")
    public R devicePages(@RequestBody @Valid AlarmDeviceRequest deviceRequest) {
        R r = R.ok();
        IPage<Camera> pages = cameraService.selectCameraList(deviceRequest);
        List<Camera> cameras = pages.getRecords();

        IPage<AlarmCameraVo> alarmCameraVoIPage = new Page<>();
        List<AlarmCameraVo> alarmCameraVos = new ArrayList<>();
        for (Camera camera : cameras) {
            AlarmCameraVo alarmCameraVo = new AlarmCameraVo();
            alarmCameraVo.setDeviceId(camera.getId() + "");
            alarmCameraVo.setDeviceName(camera.getName());
            alarmCameraVo.setCameraRtsp(camera.getUrl());
            alarmCameraVo.setCameraType(camera.getCameratype() + "");
            alarmCameraVos.add(alarmCameraVo);
        }
        alarmCameraVoIPage.setRecords(alarmCameraVos);
        alarmCameraVoIPage.setTotal(pages.getTotal());
        alarmCameraVoIPage.setCurrent(pages.getCurrent());
        alarmCameraVoIPage.setSize(pages.getSize());
        return r.put("data", new AlarmPageUtils(alarmCameraVoIPage));
    }

    /**
     * 1.6	报警数据推送
     *
     * @return
     */
    @ApiOperation("1.6\t报警数据推送")
    @PostMapping("/density/alarm/send")
    public R alarmSend(HttpServletRequest request) {
        log.info("报警数据推送");
        String alarmUrl = request.getParameter("alarmUrl");
        log.info("alarmUrl:" + alarmUrl);
        return R.ok();
    }
}
