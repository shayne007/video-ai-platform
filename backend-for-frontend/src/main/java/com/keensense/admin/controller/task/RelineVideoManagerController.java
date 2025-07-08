package com.keensense.admin.controller.task;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.admin.base.BaseController;
import com.keensense.admin.constants.CameraConstants;
import com.keensense.admin.constants.CommonConstants;
import com.keensense.admin.constants.SwaggerTest;
import com.keensense.admin.constants.VideoTaskConstant;
import com.keensense.admin.dto.RealVideoBo;
import com.keensense.admin.dto.TaskCondDTO;
import com.keensense.admin.entity.task.Camera;
import com.keensense.admin.entity.task.VsdTaskRelation;
import com.keensense.admin.request.CameraListQueryRequest;
import com.keensense.admin.request.ResultQueryRequest;
import com.keensense.admin.service.ext.VideoObjextTaskService;
import com.keensense.admin.service.sys.ICfgMemPropsService;
import com.keensense.admin.service.task.ICameraService;
import com.keensense.admin.service.task.IVsdTaskRelationService;
import com.keensense.admin.service.task.IVsdTaskService;
import com.keensense.admin.util.InterestUtil;
import com.keensense.admin.util.PolygonUtil;
import com.keensense.admin.util.QuerySqlUtil;
import com.keensense.admin.util.RandomUtils;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.vo.CameraVo;
import com.keensense.common.util.PageUtils;
import com.keensense.common.util.R;
import com.keensense.common.validator.ValidatorUtils;
import com.loocme.sys.datastruct.IVarForeachHandler;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.util.DateUtil;
import com.loocme.sys.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.awt.geom.Point2D;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍原TaskController
 * @Date: Created in 13:47 2019/6/13
 * @Version v0.1
 */
@Slf4j
@Api(tags = "接入源-录像分析-联网录像分析")
@RestController
@RequestMapping("relinevideo")
public class RelineVideoManagerController extends BaseController {

    @Resource
    private ICameraService cameraService;

    @Resource
    private IVsdTaskService vsdTaskService;

    @Autowired
    private IVsdTaskRelationService vsdTaskRelationService;

    @Autowired
    private VideoObjextTaskService videoObjextTaskService;

    @Resource
    private ResultQueryContoller resultQueryContoller;

    @Autowired
    private ICfgMemPropsService cfgMemPropsService;

    /**
     * 联网录像下载
     */
    @ApiOperation("联网录像下载")
    @PostMapping(value = "/addRealVideoTask")
    public Map<String, Object> addRealVideoTask(String cameraIds, String names, String startTime, String endTime,
                                                Long cameraId, Integer interestFlag, String interestParam,
                                                Integer chooseType, String tripwires, Integer overlineType,
                                                Integer scene, String enableIndependentFaceSnap,
                                                String enableBikeToHuman, Integer sensitivity) {
        R result = R.ok();
        String[] idArry = cameraIds.split(",");
        String[] nameArry = names.split(",");
        for (int i = 0; i < idArry.length; i++) {
            Camera camera = cameraService.selectCameraById(idArry[i]);
            if (camera == null) {
                return R.error("监控点不存在:" + idArry[i]);
            }
        }
        if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
            return R.error("开始结束时间不能为空");
        }
        long startSecond = DateUtil.getDate(startTime).getTime();
        long endSecond = DateUtil.getDate(endTime).getTime();
        long instanse = endSecond - startSecond;
        if (instanse < 0) {
            return R.error("结束时间不能小于开始时间");
        }
        if (instanse > 1000 * 60 * 60 * 24) {
            return R.error("间隔时间请勿超过1天");
        }
        for (int i = 0; i < idArry.length; i++) {
            Camera camera = cameraService.selectCameraById(idArry[i]);
            Map<String, Object> requestParams = new HashMap<>();
            String vasUrl = camera.getUrl();
            String newUrl = CameraConstants.transUrl(cfgMemPropsService.getWs2ServerIp(), cfgMemPropsService.getWs2ServerPort(), vasUrl, "playbackPlatform", false);

            requestParams.put("url", newUrl);

            String serialnumber = RandomUtils.get24TimeRandom();
            //异常行为检测标志
            requestParams.put("overlineType", overlineType);
            requestParams.put("cameraId", idArry[i]);
            requestParams.put("serialnumber", serialnumber);
            requestParams.put("type", VideoTaskConstant.Type.OBJEXT);
            requestParams.put("userId", VideoTaskConstant.USER_ID.ONLINE_VIDEO);
            String name = nameArry[i] + "," + startTime + "," + endTime;
            requestParams.put("name", name);
            //设备ID
            requestParams.put("deviceId", camera.getExtcameraid());
            requestParams.put("startTime", startTime);//设备ID
            requestParams.put("endTime", endTime);//设备ID
            requestParams.put("fromType", VideoTaskConstant.FROM_TYPE.ONLINE_VIDEO);
            requestParams.put("createuser", getUserId());
            requestParams.put("vasUrl", vasUrl + "starttime=" + startTime.replaceAll("[-:\\s]", "")
                    + "&endtime=" + endTime.replaceAll("[-:\\s]", "") + "&");
            requestParams.put("scene", scene);
            requestParams.put("enableIndependentFaceSnap", enableIndependentFaceSnap);
            requestParams.put("enableBikeToHuman", enableBikeToHuman);
            requestParams.put("sensitivity", sensitivity);
            requestParams.put("originUrl",vasUrl);
            if (chooseType != null && chooseType == 1) {
                if (StringUtils.isNotEmptyString(interestParam) && interestParam.length() > 1024) {
                    //所选区域点数过多
                    return R.error("所选区域点数过多");
                }
                interestParam = InterestUtil.initInterestParam(interestParam);
                if (interestFlag != null && interestFlag == 1) {//感兴趣
                    requestParams.put("udrVertices", interestParam);
                    requestParams.put("isInterested", true);
                }
                if (interestFlag != null && interestFlag == 0) {
                    requestParams.put("udrVertices", interestParam);
                    requestParams.put("isInterested", false);
                }
            } else if (chooseType != null && chooseType == 2) {
                // 保存跨线参数
                tripwires = InterestUtil.initTripwires(tripwires);
                requestParams.put("tripWires", tripwires);
            }
            String addRealVideoTaskReponse = videoObjextTaskService.addVsdTaskService(requestParams, true);
        }
        return result;
    }

    /**
     * 批量删除任务
     */
    @ApiOperation("删除联网录像下载视频(单个/批量)" + SwaggerTest.DEBUG)
    @PostMapping(value = "/deleteVideoList")
    public R deleteTasks(String ids) {
        R result = R.ok();
        String[] idArry = ids.split(",");
        if (idArry.length <= 0) {
            return R.error("至少选择一条记录");
        }
        for (String userSerialnumber : idArry) {
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("serialnumber", userSerialnumber);
            String deleteReponse = videoObjextTaskService.deleteVsdTaskService(requestParams);
            JSONObject json = JSONObject.fromObject(deleteReponse);
            if (json.get("ret").equals("0") || json.get("ret").equals("7")) {// 7任务不存在的也要删除记录
                vsdTaskService.deleteBySerialnumber(userSerialnumber);
            }
            log.info(deleteReponse);
        }
        return result;
    }

    /**
     * 联网录像查询列表
     */
    @ApiOperation("联网录像查询列表" + SwaggerTest.DEBUG)
    @PostMapping(value = "/list")
    public R queryRealVideoListPage(@RequestBody TaskCondDTO realVideoRequest) {
        R result = R.ok();
        Map<String, Object> requestParams = new HashMap<>();
        if (StringUtils.isNotEmptyString(realVideoRequest.getStartTime())) {
            requestParams.put("startTime", realVideoRequest.getStartTime());
        }
        if (StringUtils.isNotEmptyString(realVideoRequest.getEndTime())) {
            requestParams.put("endTime", realVideoRequest.getEndTime());
        }
        requestParams.put("type", VideoTaskConstant.Type.OBJEXT);
        requestParams.put("userId", VideoTaskConstant.USER_ID.ONLINE_VIDEO);//代表联网录像
        requestParams.put("containName", StringUtil.isNull(realVideoRequest.getFileName()) ? "" : URLDecoder.decode(realVideoRequest.getFileName()));
        requestParams.put("page", realVideoRequest.getPage() + "");
        requestParams.put("limit", realVideoRequest.getRows() + "");
        requestParams.put("status", realVideoRequest.getStatus());
        IPage taskListReponse = vsdTaskRelationService.queryVsdTaskAllService(requestParams);
        List<VsdTaskRelation> list = taskListReponse.getRecords();
        List<RealVideoBo> vsdTaskList = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                VsdTaskRelation varbo = list.get(i);
                RealVideoBo realVideoBo = new RealVideoBo();
                String serialnumber = varbo.getSerialnumber();
                realVideoBo.setSerialnumber(serialnumber);
                realVideoBo.setTaskStatus(varbo.getTaskStatus());
                realVideoBo.setProgress(varbo.getTaskProgress());
                realVideoBo.setTaskName(varbo.getTaskName());
                realVideoBo.setCreateTime(com.keensense.common.util.DateUtil.formatDate(varbo.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
                realVideoBo.setVideoUrl(varbo.getUrl());
                vsdTaskList.add(realVideoBo);
            }
        }
        Page<RealVideoBo> pages = new Page<>(realVideoRequest.getPage(), realVideoRequest.getRows());
        pages.setTotal(taskListReponse.getTotal());
        pages.setRecords(vsdTaskList);
        return result.put("page", new PageUtils(pages));
    }


    @SuppressWarnings("serial")
    public List<RealVideoBo> buildRealVideoBo(String jsonStr) {
        List<RealVideoBo> result = new ArrayList<>();
        Var var = Var.fromJson(jsonStr);
        Var tasks = var.getArray("tasks");
        tasks.foreach(new IVarForeachHandler() {
            public void execute(String index, Var valueVar) {
                RealVideoBo bo = new RealVideoBo();
                String name = valueVar.getString("name");
                String createtime = valueVar.getString("createtime");
                if (StringUtils.isEmpty(createtime)) {
                    createtime = valueVar.getString("createTime");
                }
                Integer progress = valueVar.getInt("progress");
                Integer status = valueVar.getInt("status");
                String serialnumber = valueVar.getString("serialnumber");
                bo.setSerialnumber(serialnumber);
                bo.setTaskName(name);
                bo.setProgress(progress);
                bo.setTaskStatus(status);
                bo.setCreateTime(createtime);
                bo.setVideoUrl(postHandlerVideoUrl(valueVar.getString("videoUrl")));
                Var subTasks = valueVar.getArray("subTasks");
                subTasks.foreach(new IVarForeachHandler() {
                    public void execute(String subTaskIndex, Var subTask) {
                        bo.setTaskDetail(subTask.getString("userserialnumber"), subTask.getString("taskId")
                                , subTask.getString("url"), subTask.getString("progress")
                                , subTask.getString("remark"), bo.getSerialnumber(), subTask.getString("analysisStatus"));
                        bo.setTaskId(subTask.getString("taskId"));
                        bo.setTaskSerinumber(subTask.getString("userserialnumber"));
                    }
                });
                if (bo.getTaskDetailList() != null && bo.getTaskDetailList().size() == 1) {    //只有一条子记录时
                    bo.setRemark(bo.getTaskDetailList().get(0).getRemark());
                }
                result.add(bo);

            }
        });
        return result;
    }

    /**
     * Jmanager返回地址：vas://name=admin&psw=1234&srvip=192.168.0.86&srvport=8350&devid=43010000001310000001&startTime=2019-03-18 14:00:00&endTime=2019-03-18 14:10:00
     * 前台页面播放地址： vas://name=admin&psw=1234&srvip=192.168.0.86&srvport=8350&devid=43010000001310000001&starttime=20190318140000&endtime=20190318140500&
     *
     * @param videoUrl
     * @return
     */
    private String postHandlerVideoUrl(String videoUrl) {

        if (StringUtils.isEmpty(videoUrl)) {
            return videoUrl;
        }

        StringBuilder sb = new StringBuilder();

        int index = videoUrl.indexOf("startTime");
        if (index > -1) {
            sb.append(videoUrl.substring(0, index));

            // 处理时间
            String timeStr = videoUrl.substring(index);
            String replaceAll = timeStr.toLowerCase().replaceAll("[-:\\s]", "");
            sb.append(replaceAll).append("&");
        }
        return sb.toString();
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
     * 离线结构化结果查询
     *
     * @return
     */
    @ApiOperation("联网录像下载结果查询")
    @PostMapping(value = "/getAnalysisDataList")
    public Map<String, Object> getOfflineLargeDataList(@RequestBody ResultQueryRequest paramBo) {
        ValidatorUtils.validateEntity(paramBo);
        return resultQueryContoller.getOfflineLargeDataList(paramBo);
    }

    /**
     * 查询任务分析进度
     */
    @ApiOperation(value = "查询联网录像任务分析进度")
    @PostMapping(value = "queryRealVideProgressBySerinumber")
    @ApiImplicitParam(name = "serinumber", value = "任务号")
    public R queryRealVideProgressBySerinumber(String serinumber) {
        R result = R.ok();
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("serialnumber", serinumber);
        requestParams.put("type", VideoTaskConstant.Type.OBJEXT);
        requestParams.put("userId", VideoTaskConstant.USER_ID.ONLINE_VIDEO);
        requestParams.put("pageNo", 1);
        requestParams.put("pageSize", 1);
        String taskListReponse = videoObjextTaskService.queryVsdTaskService(requestParams);
        Var var = Var.fromJson(taskListReponse);
        if (var == null) {
            result.put("progress", -1);
            return result;
        }
        List<RealVideoBo> bos = buildRealVideoBo(taskListReponse);
        return result.put("data", bos);
    }

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
        map.put("cameraType", CameraConstants.CameraType.VAS);
        map.put("fromType", VideoTaskConstant.FROM_TYPE.REAL);
        Page<CameraVo> pages = new Page<>(page, rows);
        Page<CameraVo> pageResult = cameraService.selectOnlineAndIpCCameraListByPage(pages, map);
        return R.ok().put("page", new PageUtils(pageResult));
    }

    @ApiOperation(value = "重试失败的分片任务")
    @PostMapping(value = "/retryTask")
    @ApiImplicitParam(name = "serialnumber", value = "任务号")
    public R retryTask(String serialnumber) {
        return vsdTaskService.retryTask(serialnumber);
    }

}
