package com.keensense.admin.controller.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.admin.base.BaseController;
import com.keensense.admin.constants.CameraConstants;
import com.keensense.admin.constants.CommonConstants;
import com.keensense.admin.constants.SwaggerTest;
import com.keensense.admin.constants.VideoTaskConstant;
import com.keensense.admin.entity.sys.SysUser;
import com.keensense.admin.entity.task.Camera;
import com.keensense.admin.entity.task.MonitorGroup;
import com.keensense.admin.entity.task.MonitorGroupDetail;
import com.keensense.admin.entity.task.VsdTaskRelation;
import com.keensense.admin.request.MonitorQueryRequest;
import com.keensense.admin.request.ResultQueryRequest;
import com.keensense.admin.service.task.ICameraService;
import com.keensense.admin.service.task.IMonitorGroupDetailService;
import com.keensense.admin.service.task.IMonitorGroupService;
import com.keensense.admin.service.task.IVsdTaskRelationService;
import com.keensense.admin.service.task.IVsdTaskService;
import com.keensense.admin.service.task.ResultService;
import com.keensense.admin.util.EntityObjectConverter;
import com.keensense.admin.util.PolygonUtil;
import com.keensense.admin.util.QuerySqlUtil;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.vo.CameraVo;
import com.keensense.admin.vo.ResultQueryVo;
import com.keensense.common.util.PageUtils;
import com.keensense.common.util.R;
import com.loocme.sys.util.ListUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 9:32 2019/6/15
 * @Version v0.1
 */
@Slf4j
@Api(tags = "接入源-专项监控组")
@RestController
@RequestMapping("/monitorgroup")
public class MonitorGroupController extends BaseController {
    @Resource
    private IMonitorGroupService monitorGroupService;

    @Resource
    private IMonitorGroupDetailService monitorGroupDetailService;

    @Resource
    private IVsdTaskService vsdTaskService;

    @Autowired
    private ResultService resultService;

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
    public R queryCamerasInPolygon(String polygon,String status) {
        R result = R.ok();
        List<Point2D.Double> polygonList = PolygonUtil.paresPolygonList(polygon);
        List<CameraVo> cameraData = cameraService.queryCameraAll(null,status, polygonList);
        result.put("list", cameraData);
        return result;
    }

    /**
     * 新增监控组
     *
     * @param groupName 监控组名称
     * @return
     */
    @ApiOperation("新增监控组" + SwaggerTest.DEBUG)
    @PostMapping(value = "/addMonitorGroup")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupName", value = "监控组名称", required = true),
            @ApiImplicitParam(name = "cameraIds", value = "移入的点位id", required = true)
    })
    public R save(String groupName, String cameraIds) {
        R result = R.ok();
        Long userId = getUserId() == null ? 0 : getUserId();
        if (StringUtils.isEmpty(groupName)) {
            return R.error("监控组名称不能空！");
        }
        if (StringUtils.checkRegex_false(groupName, CommonConstants.REGEX.MONITOR_NAME_SUPPORT)) {
            return R.error("监控组名称不符合规则！");
        }
        int count = monitorGroupService.count(new QueryWrapper<MonitorGroup>().eq("group_name", groupName));
        if (count > 0){
            return  R.error("监控组名称不能重复");
        }
        MonitorGroup monitorGroup = new MonitorGroup();
        monitorGroup.setGroupName(groupName);
        SysUser sysUser = getUser();
        if (sysUser != null) {
            monitorGroup.setCreateUserId(sysUser.getUserId());
        } else {
            monitorGroup.setCreateUserId(0L);
        }
        List<String> cameraIdLst = null;
        if (StringUtils.isEmptyString(cameraIds)) {
            cameraIdLst = null;
        } else {
            String[] cameraIdArrys = cameraIds.split(",");
            if (cameraIdArrys.length > 0) {
                cameraIdLst = Arrays.asList(cameraIdArrys);

            }
        }
        try {
            int moveSuccessedNum = monitorGroupService.addMonitorGroup(monitorGroup, groupName, cameraIdLst, userId);
            result.put("total", moveSuccessedNum);
            result.put("msg", "新增监控组成功");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return R.error("新增失败");
        }
    }

    @ApiOperation("查询监控组列表" + SwaggerTest.DEBUG)
    @PostMapping(value = "/list")
    public R queryPageList(@RequestBody MonitorQueryRequest monitorQueryRequest) {
        R result = R.ok();
        if (StringUtils.isNotEmptyString(monitorQueryRequest.getGroupName()) &&
                StringUtils.checkRegex_false(monitorQueryRequest.getGroupName(),
                        CommonConstants.REGEX.MONITOR_NAME_SUPPORT)) {
            return R.error("监控组名称不符合规则！");
        }
        try {
            Page<MonitorGroup> pages = new Page<MonitorGroup>(monitorQueryRequest.getPage(), monitorQueryRequest.getRows());
            Map<String, Object> params = new HashMap<String, Object>();
            String groupName = monitorQueryRequest.getGroupName();
            String status = monitorQueryRequest.getStatus();
            if (status != null) {
                params.put("status", status);
            }
            if (StringUtils.isNotEmpty(groupName)) {
                String retGroupName = QuerySqlUtil.replaceQueryName(groupName);
                params.put("groupName", "%" + retGroupName + "%");
            }
            Page<MonitorGroup> ret = monitorGroupService.selectMonitorGroupByPage(pages, params);
            result.put("page", new PageUtils(ret));
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        return result;
    }


    /**
     * 编辑监控组
     *
     * @param groupName      监控组名称
     * @param monitorGroupId 监控组ID
     * @return
     */
    @ApiOperation("编辑监控组" + SwaggerTest.DEBUG)
    @PostMapping(value = "/updateMonitorGroup")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupName", value = "监控组名称", required = true),
            @ApiImplicitParam(name = "monitorGroupId", value = "监控组Id", required = true),
            @ApiImplicitParam(name = "cameraIds", value = "移入的点位id", required = true)
    })
    public R update(String groupName, Long monitorGroupId, String cameraIds) {
        Long userId = getUserId() == null ? 0 : getUserId();
        R result = R.ok();
        if (StringUtils.isEmpty(groupName)) {
            return R.error("监控组名称不能空！");
        }
        if (StringUtils.checkRegex_false(groupName, CommonConstants.REGEX.MONITOR_NAME_SUPPORT)) {
            return R.error("监控组名称不符合规则！");
        }

        MonitorGroup monitorGroup = new MonitorGroup();
        SysUser sysUser = getUser();
        if (sysUser != null) {
            monitorGroup.setCreateUserId(sysUser.getUserId());
        }
        monitorGroup.setId(monitorGroupId);
        monitorGroup.setGroupName(groupName);
        monitorGroup.setLastUpdateTime(new Date());
        List<String> cameraIdLst = null;
        if (StringUtils.isEmptyString(cameraIds)) {
            cameraIdLst = null;
        } else {
            String[] cameraIdArrys = cameraIds.split(",");
            if (cameraIdArrys.length > 0) {
                cameraIdLst = Arrays.asList(cameraIdArrys);

            }
        }
        try {
            String moveSuccessedNum = monitorGroupService.updateMonitorGroup(monitorGroup, cameraIdLst, userId);
            if (null != moveSuccessedNum) {
                String[] str = moveSuccessedNum.split(",");
                String  msg = "";
                if (!"0".equals(str[0])) {
                    msg = str[0] + "个监控点添加成功 ";
                }
                if (!"0".equals(str[1])) {
                    msg += str[1] + "个监控点已存在 ";
                }
                int move = Integer.parseInt(str[0]) + Integer.parseInt(str[1]);
                if (move != cameraIdLst.size()) {
                    msg += cameraIdLst.size() - move + "个监控点添加失败";
                }
                result.put("msg", msg);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return R.error("编辑失败");
        }
    }


    /**
     * 启动监控组中的任务 [需要修改 Jmanager的代码，验证监控点启动]
     *
     * @param monitorGroupId 监控组ID
     * @return
     */
    @ApiOperation("启动监控组中的任务" + SwaggerTest.DEBUG)
    @PostMapping(value = "/startMonitorGroupTask")
    @ApiImplicitParam(name = "monitorGroupId", value = "监控组Id", required = true)
    public R startMonitorGroupTask(Long monitorGroupId) {
        R result = R.ok();
        // 1、查看当前监控组 未启动任务的监控点
        List<MonitorGroupDetail> cameraIdLst = monitorGroupDetailService.queryMonitorGroupUnStartCamera(monitorGroupId,
                VideoTaskConstant.STATUS.FINISHED);
        if (ListUtil.isNull(cameraIdLst)) {
            return R.error("此监控组中没有需要启动的监控点");
        }
        // 2、调用接口启动，启动前先判断是否之前启动过，如果启动过则，重启 ，否则是新增 （代码复用之前的实时任务启动）
        int startSuccessNum = 0;
        int startFailNum = 0;
        Integer enablePartial = 0;
        for (MonitorGroupDetail mdg : cameraIdLst) {
            Camera camera = cameraService.selectCameraById(mdg.getCameraId());
            if (camera != null && camera.getCameratype() == CameraConstants.CameraType.Gate){//卡口任务单独启动
                vsdTaskService.startNewGateTask(getUserId(),camera);
                startSuccessNum++;
            }else{
                Map<String, Object> resMap = vsdTaskService.startRealTimeTask(mdg.getCameraId(), null,
                        "", null, "", getUserId(),null, enablePartial,
                        null, null, null);
                Integer retCode = MapUtils.getInteger(resMap, "code");
                if (retCode == 0) {
                    startSuccessNum++;
                } else {
                    startFailNum++;
                }
            }
        }
        if (startSuccessNum < 1){
            return R.error(startFailNum + "个任务正在停止中,请稍后");
        }else if(startFailNum > 0){
            return R.ok().put("msg", "启动" + startSuccessNum + "个点位成功," + startFailNum + "个任务正在停止中");
        }else{
            return R.ok().put("msg", "启动" + startSuccessNum + "个点位成功");
        }
    }

    @ApiOperation("停止监控组任务" + SwaggerTest.DEBUG)
    @PostMapping(value = "/stopMonitorGroupTask")
    @ApiImplicitParam(name = "monitorGroupId", value = "监控组Id", required = true)
    public R stopMonitorGroupTask(Long monitorGroupId) {
        R result = R.ok();
        List<MonitorGroupDetail> serialnumberLst = monitorGroupDetailService.queryMonitorGroupUnStartCamera(monitorGroupId,
                VideoTaskConstant.STATUS.RUNNING);

        if (ListUtil.isNull(serialnumberLst)) {
            return R.error().put("msg", "此监控组中没有需要停止的监控点！");
        }

        int stopSuccessNum = 0;
        int stopFailNum = 0;
        Map<String, String> stopRetMap = new HashedMap();
        for (MonitorGroupDetail mgd : serialnumberLst) {
            Camera camera = cameraService.selectCameraById(mgd.getCameraId());
            if (camera != null && camera.getCameratype() == CameraConstants.CameraType.Gate){//卡口任务单独停止
                VsdTaskRelation vsdTaskRelation = new VsdTaskRelation();
                vsdTaskRelation.setIsvalid(0);//停止分析
                vsdTaskRelation.setTaskStatus(2);//分析完成
                vsdTaskRelation.setLastUpdateTime(new Date());
                vsdTaskRelationService.update(vsdTaskRelation,new QueryWrapper<VsdTaskRelation>().eq("camera_file_id", mgd.getCameraId()));
            }else{
                Map<String, Object> resMap = vsdTaskService.stopRealtimeTask(mgd.getSerialnumber());
                String retCode = (String) resMap.get("retCode");
                String retDesc = (String) resMap.get("retDesc");
                stopRetMap.put(mgd.getSerialnumber(), retCode + "_" + retDesc);
                // -1 ： 停止失败
                if ("-1".equals(retCode)) {
                    stopFailNum++;
                    continue;
                }
            }
            stopSuccessNum++;
        }
        result.put("stopTaskInfo", stopRetMap);
        result.put("successNum", stopSuccessNum);
        result.put("failNum", stopFailNum);
        result.put("msg", "停止成功");
        return result;
    }

    /**
     * 将监控点移出监控组
     */
    @ApiOperation("将监控点移出监控组" + SwaggerTest.DEBUG)
    @PostMapping(value = "/deleteCameraInMonitorGroup")
    @ApiImplicitParam(name = "monitorGroupDetailId", value = "监控组详情Id", required = true)
    public R deleteMonitorPoint(String monitorGroupDetailId) {
        if (StringUtils.isEmptyString(monitorGroupDetailId)) {
            return R.error("监控点不存在");
        }
        boolean ret = monitorGroupDetailService.removeById(monitorGroupDetailId);
        if (ret) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    /**
     * 查询监控组详情
     *
     * @param monitorGroupId
     * @return
     */
    @ApiOperation("查询监控组详情")
    @PostMapping(value = "/queryMonitorGroupDetails")
    @ApiImplicitParam(name = "monitorGroupId", value = "监控组Id", required = true)
    public R querymonitorGroupDetails(Long monitorGroupId) {
        R result = R.ok();
        List<MonitorGroupDetail> monitorGroupDetails = monitorGroupDetailService.selectMonitorGroupDetailList(monitorGroupId);
        result.put("list", monitorGroupDetails);
        return result;
    }

    /**
     * 单个删除监控组,批量删除监控组
     */
    @ApiOperation("删除监控组" + SwaggerTest.DEBUG)
    @PostMapping(value = "/deleteMonitorGroup")
    @ApiImplicitParam(name = "monitorGroupIds", value = "逗号分隔的监控组Id", required = true)
    public R deleteMonitorGroup(String monitorGroupIds) {
        R result = R.ok();
        if (StringUtils.isEmptyString(monitorGroupIds)) {
            return R.error("监控组id不能为空");
        }
        String[] monitorGroupIdArray = monitorGroupIds.split(",");
        List<String> monitorGroupList = Arrays.asList(monitorGroupIdArray);
        if (monitorGroupList != null && monitorGroupList.size() > 0) {
            monitorGroupService.deleteMonitorGroup(monitorGroupList);
        }
        return result;
    }

    /**
     * 监控组实时任务 查询结果
     *
     * @return
     */
    @ApiOperation("查询结果")
    @PostMapping(value = "/getMonitorResultList")
    public R getMonRealtimeLargeDataList(@RequestBody ResultQueryRequest paramBo) {
        R result = R.ok();
        int page = paramBo.getPage();
        int rows = paramBo.getRows();
        String type = paramBo.getType();
        String monitorGroupId = paramBo.getMonitorGroupId();
        if (StringUtils.isEmpty(monitorGroupId)) {
            return R.error("监控组id不能为空！");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        // 人/人骑车/车
        map.put("type", type);
        map.put("paramBo", EntityObjectConverter.getObject(paramBo, ResultQueryVo.class));
        map.put("page", page);
        map.put("rows", rows);
        map.put("monitorGroupId", monitorGroupId);
        com.keensense.admin.util.Page<ResultQueryVo> pages = new com.keensense.admin.util.Page<>(page, rows);
        Map<String, Object> resultMap = resultService.getMonRealtimeDataByExt(pages, map);
        Integer totalNum = (Integer) resultMap.get("totalNum");
        IPage<Object> ipage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, rows);
        ipage.setRecords((List<Object>) resultMap.get("resultBoList"));
        ipage.setTotal(totalNum);
        return R.ok().put("page", new PageUtils(ipage));
    }

    /**
     * 查询监控组下是否有任务在执行
     *
     * @param monitorGroupId
     * @return
     */
    @ApiOperation("查询监控组下是否有任务在执行" + SwaggerTest.DEBUG)
    @PostMapping(value = "/getMonRealTaskStatusById")
    @ApiImplicitParam(name = "monitorGroupId", value = "监控组Id", required = true)
    public R getRealTaskStatusById(String monitorGroupId) {
        R result = R.ok();
        List<String> monitorGroupRealTask = monitorGroupDetailService.selectMonitorGroupRealTask(Long.valueOf(monitorGroupId));
        if (monitorGroupRealTask != null && monitorGroupRealTask.size() > 0) {
            result.put("taskStatus", 1);
        } else {
            result.put("taskStatus", 0);
        }
        return result;
    }
}
