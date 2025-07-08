package com.keensense.densecrowd.controller.business;

/**
 * @Author: zengyc
 * @Description: 人群密度检测接口
 * @Date: Created in 17:53 2019/9/25
 * @Version v0.1
 */

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.common.platform.bo.video.CrowdDensity;
import com.keensense.common.platform.bo.video.CrowdDensityQuery;
import com.keensense.common.util.PageUtils;
import com.keensense.common.util.R;
import com.keensense.densecrowd.dto.TreeNode;
import com.keensense.densecrowd.entity.task.CtrlUnit;
import com.keensense.densecrowd.request.DensityRequest;
import com.keensense.densecrowd.service.ext.CrowdDensityService;
import com.keensense.densecrowd.service.task.ICameraService;
import com.keensense.densecrowd.service.task.ICtrlUnitService;
import com.keensense.densecrowd.util.CameraConstants;
import com.keensense.densecrowd.util.DateTimeUtils;
import com.keensense.densecrowd.util.PolygonUtil;
import com.keensense.densecrowd.util.StringUtils;
import com.keensense.densecrowd.vo.CameraVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.geom.*;
import java.util.*;

/**
 * 查询接口
 */
@Slf4j
@RestController
@RequestMapping("density")
@Api(tags = "人群密度数据")
public class DensityResultController {
    @Autowired
    CrowdDensityService crowdDensityService;

    @Autowired
    ICameraService cameraService;

    @Autowired
    ICtrlUnitService ctrlUnitService;

    @PostMapping("/list")
    @ApiOperation("列表查询")
    public R list(DensityRequest densityRequest) {
        R result = R.ok();
        CrowdDensityQuery crowdDensityQuery = new CrowdDensityQuery();
        crowdDensityQuery.setSerialnumber(densityRequest.getSerialnumber());
        crowdDensityQuery.setPageNo(densityRequest.getPage());
        crowdDensityQuery.setPageSize(densityRequest.getRows());
        crowdDensityQuery.setCountMin(densityRequest.getCountMin());
        crowdDensityQuery.setCountMax(densityRequest.getCountMax());
        crowdDensityQuery.setStartTime(densityRequest.getStartTime());
        crowdDensityQuery.setEndTime(densityRequest.getEndTime());
        crowdDensityQuery.setCreateTimeOrder(densityRequest.getCreateTimeOrder());
        crowdDensityQuery.setDeviceIds(densityRequest.getDeviceIds());
        crowdDensityQuery.setRecordType(densityRequest.getRecordType());
        Page pages = crowdDensityService.getDensityResultList(crowdDensityQuery);
        return result.put("page", new PageUtils(pages));
    }

    /**
     * 查询监控点树
     */
    @ApiOperation("区域点位列表树")
    @PostMapping(value = "getCameraListTree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "区域id"),
            @ApiImplicitParam(name = "name", value = "点位名称")
    })
    public R getCameraListTree(String id, String name) {
        if (StringUtils.isNotEmptyString(name)) {
            if (StringUtils.isEmpty(name.trim())) {
                return R.error("不允许全为空格");
            }
        }
        List<TreeNode> listData = new ArrayList<TreeNode>();
        if (StringUtils.isNotEmptyString(id) && StringUtils.isNotEmptyString(name)) { //按区域查询监控点名称
            List<CameraVo> cameras = cameraService.selectCameraByName(name, id, null);
            List<CameraVo> cameras1 = cameraService.selectCameraByName(name, null, null);
            List<CtrlUnit> mList = ctrlUnitService.findUnitByCameraRegion(id, cameras1, null);
            listData = cameraService.bulidCameraTree(mList, cameras);
        } else if (StringUtils.isNotEmptyString(name) && StringUtils.isEmptyString(id)) { //第一次监控点名称查询
            List<CameraVo> cameras = cameraService.selectCameraByName(name, null, null);
            List<CtrlUnit> mList = ctrlUnitService.findUnitByCameraRegion(null, cameras, "1");
            listData = cameraService.bulidCameraTree(mList, null);
        } else if (StringUtils.isNotEmptyString(id)) { //不查监控点名称，只查子区域
            List<CameraVo> cameras = cameraService.selectCameraByAreaId(id, null);
            List<CtrlUnit> mList = ctrlUnitService.queryAreaChildrenByParentId(id);
            listData = cameraService.bulidCameraTree(mList, cameras);
        } else { //初始化加载
            List<CtrlUnit> mList = ctrlUnitService.queryTopNode(1L);
            listData = cameraService.selectBulidTree(mList);
        }
        return R.ok().put("list", listData);
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
        List<CameraVo> cameraData = cameraService.selectCameraAll(status, polygonList);
        result.put("list", cameraData);
        return result;
    }
}
