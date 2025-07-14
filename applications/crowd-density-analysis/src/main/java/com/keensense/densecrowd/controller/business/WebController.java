package com.keensense.densecrowd.controller.business;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.common.platform.bo.video.CrowdDensity;
import com.keensense.common.platform.bo.video.CrowdDensityQuery;
import com.keensense.common.util.HttpClientUtil;
import com.keensense.common.util.PageUtils;
import com.keensense.common.util.R;
import com.keensense.common.util.RandomUtils;
import com.keensense.densecrowd.dto.TreeNode;
import com.keensense.densecrowd.entity.task.Camera;
import com.keensense.densecrowd.entity.task.CtrlUnit;
import com.keensense.densecrowd.entity.task.VsdTaskRelation;
import com.keensense.densecrowd.request.CameraListQueryRequest;
import com.keensense.densecrowd.request.DensityRequest;
import com.keensense.densecrowd.service.ext.CrowdDensityService;
import com.keensense.densecrowd.service.task.DownloadService;
import com.keensense.densecrowd.service.task.ICameraService;
import com.keensense.densecrowd.service.task.ICtrlUnitService;
import com.keensense.densecrowd.service.task.IVsdTaskRelationService;
import com.keensense.densecrowd.service.task.IVsdTaskService;
import com.keensense.densecrowd.util.CommonConstants;
import com.keensense.densecrowd.util.DateTimeUtils;
import com.keensense.densecrowd.util.DbPropUtil;
import com.keensense.densecrowd.util.ExcelUtil;
import com.keensense.densecrowd.util.QuerySqlUtil;
import com.keensense.densecrowd.util.StringUtils;
import com.keensense.densecrowd.vo.CameraVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 9:39 2019/9/25
 * @Version v0.1
 */
@Slf4j
@Api
@RestController
@RequestMapping("/densecrowd")
public class WebController {
    @Autowired
    IVsdTaskService vsdTaskService;

    @Autowired
    ICameraService cameraService;

    @Autowired
    ICtrlUnitService ctrlUnitService;

    @Autowired
    IVsdTaskRelationService vsdTaskRelationService;

    @Autowired
    CrowdDensityService crowdDensityService;

    @Autowired
    DownloadService downloadService;

    @ApiOperation(value = "同步vas点位")
    @PostMapping(value = "/syncVasData")
    public R syncVasData() {
        String serviceIp = DbPropUtil.getString("1000video.ip", "172.16.1.68");
        String servicePort = DbPropUtil.getString("1000video.port", "8060");
        String extUrl = "http://" + serviceIp + ":" + servicePort + "/1000video/syncVasData";
        //"http://localhost:8060/1000video/syncVasData";
        HttpClientUtil.requestPost(extUrl, "application/json", "");
        return R.ok();
    }

    @ApiOperation(value = "实时流获取快照")
    @PostMapping(value = "/getCameraSnapshot")
    @ApiImplicitParam(name = "cameraId", value = "监控点Id")
    public R getCameraSnapshot(String cameraId) {
        R result = R.ok();
        if (StringUtils.isEmptyString(cameraId)) {
            return R.error("cameraId不能为空");
        }
        String url = cameraService.getCameraSnapshotByUrl(cameraId);
        result.put("url", url);
        return result;
    }

    @ApiOperation(value = "设置感兴趣区域")
    @PostMapping(value = "/addInterestSettingsAndTask")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cameraId", value = "监控点Id"),
            @ApiImplicitParam(name = "interestFlag", value = "是否感兴趣: 1,感兴趣 0,不感兴趣"),
            @ApiImplicitParam(name = "udrVertices", value = "节点字符串"),
            @ApiImplicitParam(name = "startTiem", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "结束时间")
    })
    public R addInterestSettingsAndTask(String cameraId, String interestFlag, String udrVertices, String startTime,
                                        String endTime) {
        if (StringUtils.isEmptyString(cameraId)) {
            return R.error("cameraId不能为空");
        }
        if (StringUtils.isNotEmptyString(startTime) && StringUtils.isNotEmptyString(endTime)) {
            if (Integer.valueOf(startTime) > Integer.valueOf(endTime)) {
                return R.error("开始时间不能大于结束时间");
            }
        }
        return vsdTaskService.startDensecrowdTask(Long.valueOf(cameraId), Integer.valueOf(interestFlag), udrVertices,
                startTime, endTime);
    }

    @ApiOperation(value = "批量停止任务")
    @PostMapping(value = "/batchStopRealtimeTask")
    @ApiImplicitParam(name = "taskId", value = "任务Id")
    public R batchStopDensecrowdTask(String taskId) {
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
                Map<String, Object> map = vsdTaskService.stopDensecrowdTask(serialnumber);
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

    @ApiOperation(value = "查询监控点列表")
    @PostMapping(value = "/cameraListQuery")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isAnalysis", value = "是否在分析中"),
            @ApiImplicitParam(name = "areaId", value = "区域ID"),
            @ApiImplicitParam(name = "cameraName", value = "监控点名称")
    })
    public R analysisTaskList(String isAnalysis, String areaId, String cameraName) {
        if (StringUtils.isNotEmptyString(cameraName)) {
            if (StringUtils.isEmpty(cameraName.trim())) {
                return R.error("不允许全为空格");
            }
        }
        List<TreeNode> listData = new ArrayList<TreeNode>();
        if (StringUtils.isNotEmptyString(areaId) && StringUtils.isNotEmptyString(cameraName)) { //按区域查询监控点名称
            List<CameraVo> cameras = cameraService.selectCameraByName(cameraName, areaId, isAnalysis);
            List<CameraVo> cameras1 = cameraService.selectCameraByName(cameraName, null, isAnalysis);
            List<CtrlUnit> mList = ctrlUnitService.findUnitByCameraRegion(areaId, cameras1, null);
            listData = cameraService.bulidCameraTree(mList, cameras);
        } else if (StringUtils.isNotEmptyString(cameraName) && StringUtils.isEmptyString(areaId)) { //第一次监控点名称查询
            List<CameraVo> cameras = cameraService.selectCameraByName(cameraName, null, isAnalysis);
            List<CtrlUnit> mList = ctrlUnitService.findUnitByCameraRegion(null, cameras, "1");
            listData = cameraService.bulidCameraTree(mList, null);
        } else if (StringUtils.isNotEmptyString(areaId)) { //不查监控点名称，只查子区域
            List<CameraVo> cameras = cameraService.selectCameraByAreaId(areaId, isAnalysis);
            List<CtrlUnit> mList = new ArrayList<CtrlUnit>();
            if (StringUtils.isEmpty(isAnalysis)) {
                mList = ctrlUnitService.queryAreaChildrenByParentId(areaId);
            } else {
                List<CameraVo> cameras1 = cameraService.selectCameraByName(null, null, isAnalysis);
                mList = ctrlUnitService.findUnitByCameraRegion(areaId, cameras1, null);
            }
            listData = cameraService.bulidCameraTree(mList, cameras);
        } else { //初始化加载
            List<CtrlUnit> mList = new ArrayList<CtrlUnit>();
            if (StringUtils.isEmpty(isAnalysis)) {
                mList = ctrlUnitService.queryTopNode(1L);
            } else {
                List<CameraVo> cameras1 = cameraService.selectCameraByName(null, null, isAnalysis);
                mList = ctrlUnitService.findUnitByCameraRegion(areaId, cameras1, "1");
            }
            listData = cameraService.selectBulidTree(mList);
        }
        return R.ok().put("list", listData);
    }

    /**
     * 根据区域查询监控点树
     */
    @ApiOperation("区域点位列表树")
    @PostMapping(value = "getCameraListByArea")
    @ApiImplicitParam(name = "id", value = "点位id")
    public R getCameraListByArea(String id) {
        List<Camera> mList = cameraService.selectCameraByArea(id);
        return R.ok().put("list", mList);
    }

    @ApiOperation(value = "查询最新告警的4条分析任务,及各种拥挤状态下的任务数量")
    @PostMapping(value = "/queryLatelyTask")
    public R queryLatelyTask() {
        R result = R.ok();
        String bsuyThreshold = DbPropUtil.getString("warning.busy.threshold", "50");
        String crowdThreshold = DbPropUtil.getString("warning.crowd.threshold", "60");
        String alarmThreshold = DbPropUtil.getString("warning.alarm.threshold", "70");

        int count = Integer.valueOf(alarmThreshold);//拥挤告警阀值
        int pageSize = 4;//最新4条告警
        CrowdDensityQuery crowdDensityQuery = new CrowdDensityQuery();
        crowdDensityQuery.setPageNo(1);
        crowdDensityQuery.setPageSize(pageSize);
//        crowdDensityQuery.setCountMin(count);
        crowdDensityQuery.setRecordType(1);
        Page pages = crowdDensityService.getDensityResultList(crowdDensityQuery);

        int law_count = Integer.valueOf(bsuyThreshold);//拥挤阀值
        int hight_count = Integer.valueOf(crowdThreshold);//繁忙阀值

        //TODO 确认如何查询数据回来判断
        CrowdDensityQuery crowdDensityQuery_0 = new CrowdDensityQuery();
        List<VsdTaskRelation> vsdList = vsdTaskRelationService.list(new QueryWrapper<VsdTaskRelation>()
                .eq("isvalid", CommonConstants.CameraStatus.START));
        String serialnumber = "";
        int total = 0;
        int free = 0;
        int crowd = 0;
        int alarm = 0;
        if (vsdList != null && vsdList.size() > 0) {
            for (int i = 0; i < vsdList.size(); i++) {
                crowdDensityQuery_0.setPageNo(1);
                crowdDensityQuery_0.setPageSize(1);
                crowdDensityQuery_0.setCreateTimeOrder("desc");
                crowdDensityQuery_0.setSerialnumber(vsdList.get(i).getSerialnumber());
                Page alllPage = crowdDensityService.getDensityResultList(crowdDensityQuery_0);
                List<CrowdDensity> allList = alllPage.getRecords();
                if (allList != null && allList.size() > 0) {
                    CrowdDensity crowdDensity = allList.get(0);
                    if (crowdDensity.getCount() <= law_count) {
                        free++;
                    } else if (crowdDensity.getCount() > law_count && crowdDensity.getCount() <= hight_count) {
                        crowd++;
                    } else if (crowdDensity.getCount() > hight_count) {
                        alarm++;
                    }
                }
            }
        }
        total = free + crowd + alarm;
        result.put("resultList", pages.getRecords());
        result.put("total", total);
        result.put("free", free);
        result.put("crowd", crowd);
        result.put("alarm", alarm);

        return result;
    }

    @ApiOperation(value = "查询最新的实时监控点位")
    @PostMapping(value = "queryNewStartCamera")
    @ApiImplicitParam(name = "num", value = "播放路数")
    public R queryNewStartCamera(String num) {
        List<TreeNode> list = new ArrayList<TreeNode>();
        List<CameraVo> cameraList = cameraService.selectNewStartCamera(Integer.valueOf(num));
        if (cameraList != null && cameraList.size() > 0) {
            for (CameraVo camera : cameraList) {
                TreeNode treeNode = new TreeNode();
                treeNode.setId(camera.getId().toString());
                treeNode.setName(camera.getName());
                treeNode.setpId(camera.getRegion());
                treeNode.setIsParent(false);
                treeNode.setNodeType(2);
                treeNode.setUrl(camera.getUrl());
                treeNode.setSerialnumber(camera.getSerialnumber());
                treeNode.setIsvalid(camera.getIsvalid());
                treeNode.setThumbNail(camera.getThumbNail());
                treeNode.setTaskId(camera.getSerialnumber());
                list.add(treeNode);
            }
        }
        return R.ok().put("cameraList", list);
    }

    @ApiOperation(value = "地图查询监控点列表")
    @PostMapping(value = "/cameraListQueryByName")
    public R selectOnlineCameraListByPage(@RequestBody CameraListQueryRequest request) {
        if (StringUtils.isNotEmptyString(request.getCameraName())) {
            if (StringUtils.isEmpty(request.getCameraName().trim())) {
                return R.error("不允许全为空格");
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
        if (StringUtils.isNotEmptyString(status) && !"-1".equals(status)) {
            map.put("status", status);
        }
        if (StringUtils.isNotEmptyString(isvalid)) {
            map.put("isvalid", isvalid);
        }
        Page<CameraVo> pages = new Page<>(page, rows);
        Page<CameraVo> pageResult = cameraService.selectOnlineAndIpCCameraListByPage(pages, map);
        return R.ok().put("page", new PageUtils(pageResult));
    }

    @ApiOperation(value = "人群密度检索数据导出")
    @GetMapping(value = "/exportCrowdData")
    public void exportLargeData(DensityRequest densityRequest, HttpServletResponse response) {
        try {
            CrowdDensityQuery crowdDensityQuery = new CrowdDensityQuery();
            crowdDensityQuery.setCountMin(densityRequest.getCountMin());
            crowdDensityQuery.setCountMax(densityRequest.getCountMax());
            crowdDensityQuery.setStartTime(densityRequest.getStartTime());
            crowdDensityQuery.setEndTime(densityRequest.getEndTime());
            crowdDensityQuery.setCreateTimeOrder(densityRequest.getCreateTimeOrder());
            if (densityRequest.getDeviceIds() == null) {
                densityRequest.setDeviceIds("");
            }
            String[] deviceIds = densityRequest.getDeviceIds().split(",");
            List<VsdTaskRelation> vsdTaskRelationsList = vsdTaskRelationService.queryListByDeviceIds(deviceIds);
            String serialnumber = "";
            if (null != vsdTaskRelationsList && vsdTaskRelationsList.size() > 0) {
                for (int i = 0; i < vsdTaskRelationsList.size(); i++) {
                    if (i == 0) {
                        serialnumber = vsdTaskRelationsList.get(i).getSerialnumber();
                    } else {
                        serialnumber = serialnumber + "," + vsdTaskRelationsList.get(i).getSerialnumber();
                    }
                }
                crowdDensityQuery.setSerialnumber(serialnumber);
            }

            List<CrowdDensity> resultList = new ArrayList<>();
            int pageStart = densityRequest.getPageStart();
            int pageEnd = densityRequest.getPageEnd();
            int rows = 1000;
            int j = (pageEnd * 10) / 1000;
            long startTime = System.currentTimeMillis();
            if (pageEnd / 100 != pageStart / 100) {
                for (int i = j; i <= j + 1; i++) {
                    crowdDensityQuery.setPageNo(i);
                    crowdDensityQuery.setPageSize(rows);
                    Page pages = crowdDensityService.getDensityResultList(crowdDensityQuery);
                    List<CrowdDensity> resultBoList = pages.getRecords();
                    List<CrowdDensity> resultQueryVos = new ArrayList<CrowdDensity>();
                    if (i == j) {
                        int start = ((pageStart - 1) * 10) % rows;
                        for (int k = 0; k < resultBoList.size(); k++) {
                            if (k >= start) {
                                resultQueryVos.add(resultBoList.get(k));
                            }
                        }
                        resultList.addAll(resultQueryVos);
                    }
                    if (i == j + 1) {
                        int end = (pageEnd * 10) % rows;
                        for (int k = 0; k < resultBoList.size(); k++) {
                            if (k < end) {
                                resultQueryVos.add(resultBoList.get(k));
                            }
                        }
                        resultList.addAll(resultQueryVos);
                    }
                }
            } else {
                crowdDensityQuery.setPageNo(pageStart / 100 + 1);
                crowdDensityQuery.setPageSize(rows);
                Page pages = crowdDensityService.getDensityResultList(crowdDensityQuery);
                List<CrowdDensity> resultBoList = pages.getRecords();
                List<CrowdDensity> resultQueryVos = new ArrayList<CrowdDensity>();
                int start = ((pageStart - 1) * 10) % rows;
                int end = ((pageEnd) * 10) % rows;
                if (pageStart == pageEnd) {
                    end = start + 10;
                }
                if (end > resultBoList.size()) {
                    end = resultBoList.size();
                }
                for (int k = start; k < end; k++) {
                    resultQueryVos.add(resultBoList.get(k));
                }
                resultList.addAll(resultQueryVos);
            }
            String filePath = downloadService.downloadTotalComprehensive(resultList,
                    RandomUtils.get8RandomValiteCode(8));
            File zipFile = new File(filePath);
            if (StringUtils.isNotEmptyString(filePath)) {
                ExcelUtil.downloadZip(zipFile, response);
            }
            log.info("结束导出" + (System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("exportLargeData error---->" + e.getMessage());
        }
    }
}
