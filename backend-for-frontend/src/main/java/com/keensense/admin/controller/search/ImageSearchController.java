package com.keensense.admin.controller.search;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.admin.base.BaseController;
import com.keensense.admin.constants.CameraConstants;
import com.keensense.admin.constants.CommonConstants;
import com.keensense.admin.constants.VideoTaskConstant;
import com.keensense.admin.controller.common.ImageCommonController;
import com.keensense.admin.controller.task.TwiceImageQueryController;
import com.keensense.admin.entity.task.ObjextTrackPic;
import com.keensense.admin.entity.task.TbRelaytrackDetail;
import com.keensense.admin.entity.task.TbRelaytrackTask;
import com.keensense.admin.request.ImageSearchRequest;
import com.keensense.admin.request.PageRequest;
import com.keensense.admin.service.ext.FeatureSearchService;
import com.keensense.admin.service.ext.QueryAnalysisResultService;
import com.keensense.admin.service.ext.VideoObjextTaskService;
import com.keensense.admin.service.sys.ICfgMemPropsService;
import com.keensense.admin.service.task.ICameraService;
import com.keensense.admin.service.task.IImageQueryService;
import com.keensense.admin.service.task.IObjextTrackPicService;
import com.keensense.admin.service.task.IObjextTrackService;
import com.keensense.admin.service.task.IPersistPictureService;
import com.keensense.admin.service.task.ITbRelaytrackDetailService;
import com.keensense.admin.service.task.ITbRelaytrackTaskService;
import com.keensense.admin.service.task.ResultService;
import com.keensense.admin.service.util.ImageService;
import com.keensense.admin.util.DateTimeUtils;
import com.keensense.admin.util.RandomUtils;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.vo.CameraVo;
import com.keensense.admin.vo.ResultQueryVo;
import com.keensense.common.platform.bo.feature.DumpQuery;
import com.keensense.common.platform.bo.video.AnalysisResultBo;
import com.keensense.common.util.PageUtils;
import com.keensense.common.util.R;
import com.keensense.common.validator.ValidatorUtils;
import com.loocme.sys.constance.DateFormatConst;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.datastruct.WeekArray;
import com.loocme.sys.util.DateUtil;
import com.loocme.sys.util.ListUtil;
import com.loocme.sys.util.MapUtil;
import com.loocme.sys.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zengyc
 * @Description: 接力追踪
 * @Date: Created in 16:13 2019/7/1
 * @Version v0.1
 */
@Slf4j
@Api(tags = "查询-接力追踪")
@RestController
@RequestMapping("/imageSearch")
public class ImageSearchController extends BaseController {
    @Autowired
    private IImageQueryService imageQueryService;

    @Autowired
    private ITbRelaytrackTaskService tbRelaytrackTaskService;

    @Autowired
    private ITbRelaytrackDetailService tbRelaytrackDetailService;

    @Autowired
    private ICameraService cameraService;

    @Autowired
    private IPersistPictureService persistPictureService;


    @Autowired
    private FeatureSearchService featureSearchService;

    @Autowired
    private QueryAnalysisResultService queryAnalysisResultService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private TwiceImageQueryController twiceImageQueryController;

    @Resource
    private VideoObjextTaskService videoObjextTaskService;

    @Autowired
    private IObjextTrackService objextTrackService;

    @Autowired
    private IObjextTrackPicService objextTrackPicService;

    @Resource
    private ImageService imageService;

    @Autowired
    private ImageCommonController imageCommonController;

    @Autowired
    private ICfgMemPropsService cfgMemPropsService;

    /**
     * 添加搜图任务
     *
     * @throws Exception
     */
    @ApiOperation("添加搜图任务")
    @PostMapping(value = "/submitImageAnalysis")
    public R submitImageAnalysis(String imageUrl, String objextType
            , String startTime, String endTime, String cameraList, String caseId) {
        R result = R.ok();
        if (StringUtils.isEmptyString(imageUrl)) {
            return R.error("图片url不能为空");
        }
        JSONArray jsonArray = JSONArray.fromObject(cameraList);
        if (jsonArray == null || jsonArray.isEmpty()) {
            return R.error("监控组不能为空");
        } else {
            String taskId = RandomUtils.get24TimeRandom();
            if (StringUtil.isNull(caseId)) {
                caseId = RandomUtils.get18TimeRandom();
            }
            Date startTimeDt = DateUtil.getDate(startTime);
            Date endTimeDt = DateUtil.getDate(endTime);
            long differTime = endTimeDt.getTime() - startTimeDt.getTime();
            if (differTime > 12 * 60 * 60 * 1000) {
                return R.error("检索时间间隔不能大于12小时");
            }
            TbRelaytrackTask tbRelaytrackTask = new TbRelaytrackTask(taskId, "", caseId, startTimeDt, endTimeDt,
                    StringUtil.getInteger(objextType), imageUrl, "", "", "");
            tbRelaytrackTaskService.save(tbRelaytrackTask);
            for (int i = 0; i < jsonArray.size(); i++) {
                String cameraId = jsonArray.getJSONObject(i).getString("id");
                CameraVo cameraInfo = cameraService.selectByPrimaryKey(cameraId);
                if (cameraInfo == null) {
                    break;
                }
                String analyId = taskId + StringUtil.lpad(i + "", '0', 6);
                TbRelaytrackDetail detail = null;
                if (null == cameraId) {
                    detail = new TbRelaytrackDetail(analyId, taskId, cameraId, null, analyId);
                    detail.setFinishTime(new Date());
                    detail.setRemark("监控点不存在");
                    detail.setStatus(2);
                } else {
                    detail = new TbRelaytrackDetail(analyId, taskId, cameraId, cameraInfo.getName(), analyId);
                    String vasUrl = cameraInfo.getUrl();
                    String newUrl = "";
                    if (!CameraConstants.checkVas(vasUrl)) {
                        detail.setFinishTime(new Date());
                        detail.setRemark("点位信息有误");
                        log.info("点位信息有误:" + analyId + " " + cameraInfo);
                        detail.setStatus(2);
                    } else {
                        newUrl = CameraConstants.transUrl(cfgMemPropsService.getWs2ServerIp(), cfgMemPropsService.getWs2ServerPort(), vasUrl, "playbackPlatform", false);
                        String extcameraid = cameraInfo.getExtcameraid();
                        Map<String, Object> paramMap = new HashMap<>();
//						// 添加录像分析任务
                        paramMap.put("serialnumber", analyId);
                        paramMap.put("type", "objext");
                        paramMap.put("url", newUrl);
                        paramMap.put("userId", VideoTaskConstant.USER_ID.JLZZ);
                        paramMap.put("cameraId", cameraId);
                        paramMap.put("deviceId", extcameraid);//设备ID
                        paramMap.put("startTime", startTime);//设备ID
                        paramMap.put("endTime", endTime);//设备ID
                        paramMap.put("name", cameraInfo.getName() + "," + startTime + "," + endTime);
                        String addRealVideoTaskReponse = videoObjextTaskService.addVsdTaskService(paramMap, false);
                        Var respVar = Var.fromJson(addRealVideoTaskReponse);
                        if (null == respVar || respVar.isNull()) {
                            detail.setFinishTime(new Date());
                            detail.setRemark("添加分析任务失败");
                            detail.setStatus(2);
                        } else {
                            String ret = respVar.getString("ret");
                            if (!"0".equals(ret)) {
                                detail.setFinishTime(new Date());
                                detail.setRemark(respVar.getString("desc"));
                                detail.setStatus(2);
                            } else {
                                detail.setStatus(0);
                            }
                        }
                    }
                }
                tbRelaytrackDetailService.save(detail);

            }
            result.put("imgSearchId", taskId);
            return result;
        }
    }

    /**
     * 查询搜图任务进度
     *
     * @throws Exception
     */
    @PostMapping(value = "/getImageSearchProgress")
    @ApiOperation("查询搜图任务进度")
    public R getImageSearchProgress(String imgSearchId) {
        R result = R.ok();
        if (StringUtils.isEmptyString(imgSearchId)) {
            return R.error("任务号不能为空");
        }

        int progress = -1;
        com.alibaba.fastjson.JSONArray anataskList = new com.alibaba.fastjson.JSONArray();
        List<TbRelaytrackDetail> detailList = tbRelaytrackDetailService.list(new QueryWrapper<TbRelaytrackDetail>().eq("task_id", imgSearchId));
        if (ListUtil.isNotNull(detailList)) {
            progress = 0;
            for (TbRelaytrackDetail detail : detailList) {
                int analyProgress = 0;
                int analyStatus = 0;
                int status = 0;
                String analyRemark = "";
                if (0 == detail.getStatus()) {
                    // 调用任务详情查询接口
                    Map<String, Object> requestParams = new HashMap<>();
                    requestParams.put("serialnumber", detail.getAnalysisId());
                    requestParams.put("type", VideoTaskConstant.Type.OBJEXT);
                    requestParams.put("userId", VideoTaskConstant.USER_ID.JLZZ);
                    requestParams.put("page", 1);
                    requestParams.put("rows", 10);
                    String taskListReponse = videoObjextTaskService.queryVsdTaskService(requestParams);
                    Var var = Var.fromJson(taskListReponse);
                    int totalCount = var.getInt("totalcount");
                    if (0 < totalCount) {
                        analyProgress = var.getInt("tasks[0].progress");
                        analyStatus = var.getInt("tasks[0].status");
                        analyRemark = var.getString("tasks[0].remark");
                    }

                    if (2 == analyStatus) {
                        // 分析成功.
                        status = 1;
                    } else if (3 == analyStatus) {
                        // 分析失败
                        status = 2;
                    }
                    TbRelaytrackDetail tbRelaytrackDetail = new TbRelaytrackDetail();
                    tbRelaytrackDetail.setAnalysisProgress(analyProgress);
                    tbRelaytrackDetail.setAnalysisStatus(analyStatus);
                    tbRelaytrackDetail.setStatus(status);
                    tbRelaytrackDetail.setFinishTime(new Date());
                    tbRelaytrackDetail.setRemark(analyRemark);
                    tbRelaytrackDetail.setId(detail.getId());
                    tbRelaytrackDetailService.updateById(tbRelaytrackDetail);
                } else {
                    // 直接返回结果
                    analyProgress = detail.getAnalysisProgress();
                    status = detail.getStatus();
                }

                if (status == 0) {
                    progress += analyProgress;
                } else {
                    progress += 100;
                }
                JSONObject json = new JSONObject();
                json.put("cameraId", detail.getCameraId());
                json.put("cameraName", detail.getCameraName());
                json.put("progress", analyProgress);
                json.put("taskStatus", status);
                json.put("analysisStatus", detail.getAnalysisStatus());
                json.put("analysisTaskId", detail.getId());
                anataskList.add(json);
            }

            progress /= detailList.size();
        }
        result.put("anataskList", anataskList);
        result.put("progress", progress);
        return result;
    }

    /**
     * 以图搜图历史任务
     *
     * @param pageRequest
     * @return
     */
    @PostMapping(value = "/getHistorySearchImages")
    @ApiOperation("查询接力追踪历史任务,status为剩余任务数量")
    public R getHistorySearchImages(PageRequest pageRequest) {
        Page<TbRelaytrackTask> trackTaskPage = new Page<>(pageRequest.getPage(), pageRequest.getRows());
        IPage iPage = tbRelaytrackTaskService.page(trackTaskPage, new QueryWrapper<TbRelaytrackTask>().orderByDesc("create_time"));
        List<TbRelaytrackTask> reList = iPage.getRecords();
        for (TbRelaytrackTask relaytrackTask : reList) {
            String taskId = relaytrackTask.getId();
            Map<String, Object> mp = tbRelaytrackDetailService.getNameMap(taskId);
            if (mp != null && mp.containsKey("names") && mp.get("names") != null) {
                String cameraNames = mp.get("names").toString();
                relaytrackTask.setCameraNames(cameraNames);
            }
            if (mp != null && mp.containsKey("ids") && mp.get("ids") != null) {
                String cameraIds = mp.get("ids").toString();
                relaytrackTask.setCameraIds(cameraIds);
            }
            int count = tbRelaytrackDetailService.count(new QueryWrapper<TbRelaytrackDetail>().eq("status", 0).eq("task_id", relaytrackTask.getId()));
            relaytrackTask.setStatus(count);    //为0时表示任务都已完成，可以删除
        }
        return R.ok().put("page", new PageUtils(iPage));

    }

    /**
     * 持久化图片地址
     *
     * @param picture 图片地址
     * @return 持久化的图片地址
     */
    //TODO
    private String getPersistImageUrl(String picture) throws Exception {
        return persistPictureService.savePicture(picture);
    }

    /**
     * 获取以图搜图结果
     */
    @PostMapping(value = "/queryImageSearchTaskResult")
    @ApiOperation(("接力追踪结果查询"))
    public R queryImageSearchTaskResult(@RequestBody ImageSearchRequest imageSearchRequest) throws Exception {
        R r = R.ok();
        Page<ResultQueryVo> page = new Page<>(imageSearchRequest.getPage(), imageSearchRequest.getRows());
        ValidatorUtils.validateEntity(imageSearchRequest);
        TbRelaytrackTask relayTask = tbRelaytrackTaskService.getOne(new QueryWrapper<TbRelaytrackTask>().eq("id", imageSearchRequest.getSerialnumber()));
        if (StringUtils.isNotEmpty(imageSearchRequest.getPicture())) {
            relayTask.setPicture(imageSearchRequest.getPicture());
            tbRelaytrackTaskService.updateById(relayTask);
        }
        if (null == relayTask) {
            return R.error("流水号不存在");
        }

        r.put("caseId", relayTask.getCaseId());
        int objType = relayTask.getObjType();
        int pageNum = imageSearchRequest.getPage();
        int pageSize = imageSearchRequest.getRows();
        int startNum = (pageNum - 1) * pageSize;
        int endNum = startNum + pageSize;


        List<ResultQueryVo> rstData = new ArrayList<>();
        StringBuilder sbUids = new StringBuilder();
        Map<String, Float> scoreMap = new HashMap<>();


        WeekArray results = null;


        if (true) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("objtype", objType + "");
            String feature = relayTask.getFeature();
            if (StringUtils.isEmptyString(imageSearchRequest.getUuid())) {
                feature = imageCommonController.getFeatureByImageUrl(objType + "", relayTask.getPicture());
                paramMap.put("feature", feature);
            } else {
                //更改获取特征模式为dump接口
                DumpQuery dumpQuery = new DumpQuery();
                //时间范围默认六个月
                Date endTime = DateTimeUtils.now();
                Date startTime = DateTimeUtils.getBeforeDaysDateTime(180);
                dumpQuery.setFrom(DateTimeUtils.formatDate(startTime, null));
                dumpQuery.setTo(DateTimeUtils.formatDate(endTime, null));
                dumpQuery.setTask(imageSearchRequest.getAnalysisId());
                dumpQuery.setUuid(imageSearchRequest.getUuid());
                dumpQuery.setObjType(objType);
                TbRelaytrackTask tbRelaytrackTask = tbRelaytrackTaskService.getById(imageSearchRequest.getSerialnumber());
                dumpQuery.setStartAt(DateTimeUtils.formatDate(tbRelaytrackTask.getStartTime(), null));
                Map<String, Object> structs = twiceImageQueryController.getStructsFromdump(dumpQuery);
                if (StringUtils.isEmptyString(String.valueOf(structs.get("feature")))) {
                    feature = imageCommonController.getFeatureByImageUrl(objType + "", relayTask.getPicture());
                } else {
                    feature = structs.get("feature").toString();
                }
                paramMap.put("feature", feature);
                relayTask.setFeature(feature);
                relayTask.setUuid(imageSearchRequest.getUuid());
                relayTask.setAnalysisId(imageSearchRequest.getAnalysisId());
            }
            tbRelaytrackTaskService.updateById(relayTask);
            String startTime = DateUtil.getFormat(relayTask.getStartTime(), DateFormatConst.YMDHMS_);
            String endTime = DateUtil.getFormat(relayTask.getEndTime(), DateFormatConst.YMDHMS_);
            paramMap.put("starttime", startTime);
            paramMap.put("endtime", endTime);

            paramMap.put("autoFetch", 0);

            StringBuilder serialBuff = new StringBuilder();


            List<TbRelaytrackDetail> detailList = tbRelaytrackDetailService.list(new QueryWrapper<TbRelaytrackDetail>().eq("task_id", imageSearchRequest.getSerialnumber()).eq(StringUtils.isNotEmpty(imageSearchRequest.getAnalysisTaskId()), "analysis_id", imageSearchRequest.getAnalysisTaskId()));
            if (ListUtil.isNull(detailList)) {
                return R.error("未查询到点位信息");
            }
            for (int i = 0; i < detailList.size(); i++) {
                if (serialBuff.length() != 0) {
                    serialBuff.append(",");
                }
                if (detailList.get(i).getAnalysisProgress() > 0) {
                    serialBuff.append(detailList.get(i).getAnalysisId());
                }
            }
            if (serialBuff.length() == 0) {
                page.setRecords(new ArrayList<>());
                page.setTotal(0);
                return r.put("page", new PageUtils(page));
            }
            paramMap.put("serialnumbers", serialBuff.toString());
            // 调用搜图接口
            String resultJson = featureSearchService.doSearchService(paramMap);
            Var resultVar = Var.fromJson(resultJson);
            if (!"0".equals(resultVar.getString("ret"))) {
                return R.error(resultVar.getString("msg"));
            }
            results = resultVar.getArray("results");
        }
        if (results.getSize() != 0) {
            for (int i = startNum; i < endNum; i++) {
                Var searchResultVar = results.get(i + "");
                if (null == searchResultVar || searchResultVar.isNull()) {
                    continue;
                }
                String uuid = searchResultVar.getString("uuid");
                float score = searchResultVar.getFloat("score");

                sbUids.append(uuid).append(",");
                scoreMap.put(uuid, score);
            }
            String uids = sbUids.toString();
            uids = uids.substring(0, sbUids.length() - 1);
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("uuid", uids);
            paramMap.put("objType", objType);

            String resultJson = queryAnalysisResultService.doHttpService(paramMap);

            List<ResultQueryVo> resultBoList = queryAnalysisResultService.transAllResultToBo(resultJson);

            if (resultBoList != null && !resultBoList.isEmpty()) {
                for (ResultQueryVo resultBo : resultBoList) {
                    float score = scoreMap.get(resultBo.getId());
                    resultBo.setDistance(score);
                    rstData.add(resultBo);
                }
            }

            rstData = handerBathQueryUidSort(uids, rstData);
            page.setRecords(rstData);
            page.setTotal(results.getSize());
        } else {
            page.setRecords(rstData);
            page.setTotal(results.getSize());
        }

        return r.put("page", new PageUtils(page));
    }

    /**
     * 按照查询的uids的顺序进行数据的封装
     *
     * @param uids    查询的uids
     * @param rstData 根据uids查询方法的数据集合
     * @return
     */
    private List<ResultQueryVo> handerBathQueryUidSort(String uids, List<ResultQueryVo> rstData) {
        List<ResultQueryVo> sortRsData = new ArrayList<>();
        String[] splitUids = uids.split(",");

        if (splitUids.length == 0) {
            return rstData;
        }
        for (String uuid : splitUids) {
            for (int i = 0; i < rstData.size(); i++) {
                ResultQueryVo resultBo = rstData.get(i);
                if (resultBo != null && uuid.equals(resultBo.getRecogId())) {
                    sortRsData.add(resultBo);
                    break;
                }
            }
        }

        return sortRsData;
    }

    /**
     * 根据任务id查询当前任务下所有的监控点
     *
     * @return
     */
    @PostMapping("queryImageTractTarget")
    @ApiOperation("根据流水号查询目标库")
    public R queryCurrentTaskCamerasTz(String targetId) {
        if (StringUtils.isEmptyString(targetId)) {
            return R.error("流水号不能为空");
        }
        List<Map<String, Object>> trackList = currentTaskCamerasTarget(targetId);
        return R.ok().put("list", trackList);
    }

    /**
     * 保存图片到轨迹库
     * <p>
     * trackType 1代表结果图片 2代表轨迹图片
     *
     * @return
     */
    @PostMapping("addImageTractTarget")
    @ApiOperation("添加目标库")
    public R saveImg2TractTz(String resultId, String objtype, String cameraId, String targetId,
                             String trackTime, String trackType, String trackImg, String analysisId, String uuid,
                             String serialnumber) {
        return twiceImageQueryController.saveImg2TractTz(resultId, objtype, cameraId, targetId, trackTime, trackType,
                trackImg, analysisId, uuid, serialnumber);
    }

    /**
     * 轨迹库以图搜图
     *
     * @return
     */
    @PostMapping("searchImageTractTarget")
    @ApiOperation("目标库以图搜图")
    public R searchImageTractTarget(String serialnumber, String cameraId, String objtype, String startTime, String endTime) {
        return twiceImageQueryController.searchImageTractTarget(serialnumber, cameraId, objtype, startTime, endTime);
    }

    /**
     * 根据id 删除轨迹库数据
     *
     * @return
     */
    @PostMapping("deleteTractById")
    @ApiOperation("删除目标库")
    public R deleteTractById(String trackType, String id) {
        return twiceImageQueryController.deleteTractById(trackType, id);
    }

    /**
     * 根据任务号,图片id,类型 删除轨迹库数据
     *
     * @return
     */
    @PostMapping("deleteTractByResultid")
    @ApiOperation("删除目标库(根据任务号,图片id,类型 删除轨迹库数据)")
    public R deleteTractByResultid(String resultid, String objtype, String serialnumber) {
        int delete = imageQueryService.deleteTract(resultid, objtype, serialnumber);
        if (delete > 0) {
            return R.ok();
        } else {
            return R.error("更新失败");
        }
    }


    /**
     * 删除以图搜图任务
     *
     * @param searchId
     * @return
     */
    @PostMapping(value = "/deleteSearchImage")
    @ApiOperation("删除任务")
    public Map<String, Object> deleteSearchImage(String searchId) {

        R result = R.ok();
        if (StringUtils.isEmptyString(searchId)) {
            return R.error("流水号不能为空");
        }

        List<TbRelaytrackDetail> detailList = tbRelaytrackDetailService.list(new QueryWrapper<TbRelaytrackDetail>().eq("task_id", searchId));
        boolean flag = true;
        for (int i = 0; i < detailList.size(); i++) {
            String analysisId = detailList.get(i).getAnalysisId();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("serialnumber", analysisId);
            String resultJson = videoObjextTaskService.deleteVsdTaskService(paramMap);
            if (StringUtils.isNotEmptyString(resultJson)) {
                Var reVar = Var.fromJson(resultJson);
                String retCode = reVar.getString("ret");
                // {"ret":"9001","desc":"请求的任务不存在或者已删除"}
                if (!"0".equals(retCode) && !"9001".equals(retCode) && !"7".equals(retCode)) {
                    log.warn("调用接口删除任务出错...searchId:{}, analysisId{} ", searchId, analysisId);
                    flag = false;
                }
            }
        }
        if (flag) {
            tbRelaytrackTaskService.removeById(searchId);
            tbRelaytrackDetailService.remove(new UpdateWrapper<TbRelaytrackDetail>().eq("task_id", searchId));
        } else {
            return R.error("删除失败");
        }

        return result;
    }

    @ApiOperation("目标库批量导出")
    @GetMapping(value = "/batchDownLoadImg")
    public R batchDownLoadImg(HttpServletResponse response, String targetId) {
        List<Map<String, Object>> mapList = currentTaskCamerasTarget(targetId);
        List<String> imgUrls = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            imgUrls.add(MapUtils.getString(map, "imgurl", ""));
        }
        return imageService.batchDownLoadImg(response, imgUrls);
    }

    public List<Map<String, Object>> currentTaskCamerasTarget(String serialnumber) {
        List<Map<String, Object>> trackList = objextTrackService.queryObjextTrackResultListBySerialnumber(serialnumber);
        for (Map<String, Object> track : trackList) {
            track.put("tracktype", CommonConstants.JLZZ_TRACKTYPE_RESULT);
            track.put("serialnumber", MapUtil.getString(track, "serialnumber"));
            track.put("cameraid", MapUtil.getString(track, "cameraid"));
            track.put("tracktime", MapUtil.getString(track, "tracktime"));
            int objType = MapUtil.getInteger(track, "objtype");
            track.put("objtype", objType);
            String resultId = MapUtil.getString(track, "resultid");
            track.put("resultid", resultId);
            track.put("longitude", MapUtil.getString(track, "longitude"));
            track.put("latitude", MapUtil.getString(track, "latitude"));
            track.put("name", MapUtil.getString(track, "name") == "" ? "未找到点位信息" : MapUtil.getString(track, "name"));
            Map<String, Object> paramMap = new HashMap<>();//根据特征id查找图片信息获取url
            paramMap.put("objType", objType);
            paramMap.put("uuid", resultId);
            String resultJson = queryAnalysisResultService.doHttpService(paramMap);
            if (StringUtils.isNotEmptyString(resultJson)) {
                AnalysisResultBo analysisResultBo = queryAnalysisResultService.transOneResultToBo(resultJson);
                if (analysisResultBo != null) {
                    track.put("imgurl", analysisResultBo.getImgUrl());
                }
            }

        }
        //目标库展示保存的轨迹图片
        List<ObjextTrackPic> objextTrackPicList = objextTrackPicService.list(new QueryWrapper<ObjextTrackPic>().eq("serialnumber", serialnumber).orderByDesc("createtime"));
        if (objextTrackPicList != null && !objextTrackPicList.isEmpty()) {
            ObjextTrackPic objextTrackPic = objextTrackPicList.get(0);
            Map<String, Object> map = new HashMap<>();
            map.put("imgurl", objextTrackPic.getImgurl());
            map.put("id", objextTrackPic.getId());
            map.put("serialnumber", objextTrackPic.getSerialnumber());
            map.put("tracktype", CommonConstants.JLZZ_TRACKTYPE_TRACK);
            trackList.add(map);
        }
        return trackList;
    }

}
