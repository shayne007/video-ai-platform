package com.keensense.admin.controller.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.admin.constants.CameraConstants;
import com.keensense.admin.constants.CommonConstants;
import com.keensense.admin.constants.TaskConstants;
import com.keensense.admin.constants.VideoTaskConstant;
import com.keensense.admin.controller.common.ImageCommonController;
import com.keensense.admin.dto.TreeNode;
import com.keensense.admin.entity.task.Camera;
import com.keensense.admin.entity.task.CtrlUnit;
import com.keensense.admin.entity.task.TbImagesearchTask;
import com.keensense.admin.request.CameraListQueryRequest;
import com.keensense.admin.request.JDThreeSenceRequest;
import com.keensense.admin.service.ext.FeatureSearchService;
import com.keensense.admin.service.ext.QueryAnalysisResultService;
import com.keensense.admin.service.ext.VideoObjextTaskService;
import com.keensense.admin.service.task.ICameraService;
import com.keensense.admin.service.task.ICtrlUnitService;
import com.keensense.admin.service.task.IImageQueryService;
import com.keensense.admin.service.task.IObjextTrackService;
import com.keensense.admin.service.task.ITbImagesearchTaskService;
import com.keensense.admin.service.task.IVsdTaskRelationService;
import com.keensense.admin.service.task.IVsdTaskService;
import com.keensense.admin.util.DateTimeUtils;
import com.keensense.admin.util.EhcacheUtils;
import com.keensense.admin.util.QuerySqlUtil;
import com.keensense.admin.util.RandomUtils;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.vo.CameraVo;
import com.keensense.admin.vo.ResultQueryVo;
import com.keensense.common.exception.VideoException;
import com.keensense.common.util.PageUtils;
import com.keensense.common.util.R;
import com.loocme.sys.constance.DateFormatConst;
import com.loocme.sys.util.DateUtil;
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
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;


/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 21:15:12
 */
@Slf4j
@RestController
@RequestMapping("/camera")
@Api(tags = "基础数据-监控点")
public class CameraController {

    @Autowired
    private ICameraService cameraService;

    @Resource
    private ICtrlUnitService ctrlUnitService;

    @Resource
    private IVsdTaskService vsdTaskService;

    @Resource
    private IVsdTaskRelationService taskRelationService;

    @Resource
    private VideoObjextTaskService videoObjextTaskService;

    @Autowired
    private TwiceImageQueryController twiceImageQueryController;

    @Resource
    private ITbImagesearchTaskService tbImagesearchTaskService;

    @Autowired
    private FeatureSearchService featureSearchService;

    @Autowired
    private ImageCommonController imageCommonController;

    @Autowired
    private IObjextTrackService objextTrackService;

    @Autowired
    private QueryAnalysisResultService queryAnalysisResultService;

    @Resource
    private IImageQueryService imageQueryService;


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
            if (name.length() > CommonConstants.REGEX.CAMERA_CASE_LENGTH) {
                return R.error("监控点名称超过32位");
            }
            if (StringUtils.isNotEmptyString(name) && StringUtils.checkRegex_false(name,
                    CommonConstants.REGEX.CAMERA_CASE_RELINE_FILE_NAME_NOT_SUPPORT)) {
                return R.error("监控点名称不符合规则");
            }
        }
        List<TreeNode> listData = new ArrayList<TreeNode>();
        if (StringUtils.isNotEmptyString(id) && StringUtils.isNotEmptyString(name)) { //按区域查询监控点名称
            List<Camera> cameras = cameraService.selectCameraByName(name, id);
            List<Camera> cameras1 = cameraService.selectCameraByName(name, null);
            List<CtrlUnit> mList = ctrlUnitService.findUnitByCameraRegion(id, cameras1, null);
            listData = cameraService.bulidCameraTree(mList, cameras);
        } else if (StringUtils.isNotEmptyString(name) && StringUtils.isEmptyString(id)) { //第一次监控点名称查询
            List<Camera> cameras = cameraService.selectCameraByName(name, null);
            List<CtrlUnit> mList = ctrlUnitService.findUnitByCameraRegion(null, cameras, "1");
            listData = cameraService.bulidCameraTree(mList, null);
        } else if (StringUtils.isNotEmptyString(id)) { //不查监控点名称，只查子区域
            List<Camera> cameras = cameraService.findCameraByAreaId(id);
            List<CtrlUnit> mList = ctrlUnitService.queryAreaChildrenByParentId(id);
            listData = cameraService.bulidCameraTree(mList, cameras);
        } else { //初始化加载
            List<CtrlUnit> mList = ctrlUnitService.queryTopNode(1L);
            listData = cameraService.bulidTree(mList);
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
        List<Camera> mList = cameraService.queryCameraListByArea(id);
        return R.ok().put("list", mList);
    }

    /**
     * 查询特定地理区域范围内的监控点
     */
    @ApiOperation(value = "查询监控点信息")
    @PostMapping(value = "/queryCameraInfoById")
    @ApiImplicitParam(name = "cameraId", value = "监控点Id")
    public R queryCameraInfoById(String cameraId) {
        R result = R.ok();
        CameraVo camera = cameraService.selectByPrimaryKey(cameraId);
        result.put("camera", camera);
        return result;
    }

    /**
     * 查询监控点树
     */
    @ApiOperation("查询Vas点位")
    @PostMapping(value = "getCameraListTreeVas")
    public R getCameraListTreeVas(String id) {
        List<TreeNode> listData = new ArrayList<>();
        if (StringUtils.isEmptyString(id)) {
            List<CtrlUnit> mList = ctrlUnitService.queryTopNode(1L);
            listData = cameraService.bulidTree(mList);
        } else {
            List<Camera> cameras = cameraService.findCameraByAreaIdVas(id);
            List<CtrlUnit> mList = ctrlUnitService.queryAreaChildrenByParentId(id);
            listData = cameraService.bulidCameraTree(mList, cameras);
        }
        return R.ok().put("list", listData);
    }

    @PostMapping(value = "/getCameraTreeByCameraName")
   /* @ApiOperation("根据监控点name查询监控点树")
    @ApiImplicitParam(name = "cameraName", value = "监控点名称")*/
    public R getCameraTreeByCameraName(String cameraName) {
        if (cameraName.length() > CommonConstants.REGEX.CAMERA_CASE_LENGTH) {
            return R.error("监控点名称超过32位");
        }
        if (StringUtils.checkRegex_false(cameraName,
                CommonConstants.REGEX.CAMERA_CASE_RELINE_FILE_NAME_NOT_SUPPORT)) {
            return R.error("监控点名称不符合规则");
        }
        Map<String, String> param = new HashMap<String, String>();
        String showType = "1";
        try {
            String retCameraName = QuerySqlUtil.replaceQueryName(URLDecoder.decode(cameraName, "UTF-8"));
            if (StringUtil.isNotNull(retCameraName)) {
                param.put("cameraName", "%" + retCameraName + "%");
            }

        } catch (UnsupportedEncodingException e) {
            throw new VideoException("监控点名称有误");
        }
        param.put("showType", showType);
        List<TreeNode> listData = cameraService.selectCameraByName(param);
        return R.ok().put("list", listData);
    }


    /**
     * 查询在分析中监控点树
     */
    @PostMapping(value = "getCameraAnalysisListTreeById")
    public R getCameraAnalysisListTreeById(String cameraEventType, Integer enablePartial) {
        Map<String, Object> param = new HashMap<>();
        param.put("isvalid", 1);
        param.put("fromType", "1,4");
        if (enablePartial != null) {
            param.put("enablePartial", enablePartial);
        }
        if (null != cameraEventType) {
            param.put("cameraEventType", cameraEventType);
        }
        List<Camera> cameras = cameraService.selectOnlineAndIpCCameraList(param);
        List<CtrlUnit> mList = ctrlUnitService.findUnitByCameraRegion(null, cameras, null);
        List<TreeNode> listData = cameraService.bulidCameraTreeByCtrlUnit(mList, cameras);
        Map<String, Object> data = new HashMap<>();
        data.put("list", listData);
        return R.ok().put("data", data);
    }

    @ApiOperation(value = "实时流获取快照")
    @PostMapping(value = "/getCameraSnapshot")
    @ApiImplicitParam(name = "cameraId", value = "监控点Id")
    public R getCameraSnapshot(String cameraId) {
        R result = R.ok();
        String url = cameraService.getCameraSnapshotByUrl(cameraId);
        result.put("url", url);
        return result;
    }

    @ApiOperation(value = "根据监控点名称查询监控点树")
    @ApiImplicitParam(name = "cameraName", value = "监控点名称")
    @PostMapping(value = "getTreeByCameraName")
    public R getTreeByCameraName(String cameraName) {
        if (StringUtils.isNotEmptyString(cameraName)) {
            if (StringUtils.isEmpty(cameraName)) {
                return R.error("监控点名称不能为空");
            }
            if (StringUtils.isEmpty(cameraName.trim())) {
                return R.error("不允许全为空格");
            }
            if (cameraName.length() > CommonConstants.REGEX.CAMERA_CASE_LENGTH) {
                return R.error("监控点名称超过32位");
            }
            if (StringUtils.checkRegex_false(cameraName,
                    CommonConstants.REGEX.CAMERA_CASE_RELINE_FILE_NAME_NOT_SUPPORT)) {
                return R.error("监控点名称不符合规则");
            }
        }
        List<Camera> cameraList = cameraService.list(new QueryWrapper<Camera>().like("name", cameraName));
        List<CtrlUnit> ctrlUnitList = new ArrayList<>();
        if (cameraList != null && !cameraList.isEmpty()) {
            for (Camera camera : cameraList) {
                String region = camera.getRegion();
                if (StringUtils.isNotEmptyString(region)) {
                    CtrlUnit ctrlUnit = ctrlUnitService.getOne(new QueryWrapper<CtrlUnit>().eq("unit_identity", region));
                    CtrlUnit ctrlUnitParent = getCtrlUnitParent(ctrlUnit);
                    ctrlUnitList.add(ctrlUnitParent);
                }
            }
        }
        return R.ok().put("list", ctrlUnitList);
    }

    private CtrlUnit getCtrlUnitParent(CtrlUnit ctrlUnit) {
        CtrlUnit ctrlUnitParent = ctrlUnitService.getOne(new QueryWrapper<CtrlUnit>().eq("unit_identity", ctrlUnit.getUnitParentId()));
        if (ctrlUnitParent != null) {
            ctrlUnitParent.setChildren(ctrlUnit);
            ctrlUnitParent = getCtrlUnitParent(ctrlUnitParent);
        } else {
            return ctrlUnit;
        }
        return ctrlUnitParent;
    }

    /**
     * 查询监控点树
     */
    @ApiOperation("vas区域点位列表树")
    @PostMapping(value = "getVasCameraListTree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "点位id"),
            @ApiImplicitParam(name = "name", value = "点位名称")
    })
    public R getVasCameraListTree(String id, String name) {
        if (name.length() > CommonConstants.REGEX.CAMERA_CASE_LENGTH) {
            return R.error("监控点名称超过32位");
        }
        if (StringUtils.isNotEmptyString(name) && StringUtils.checkRegex_false(name,
                CommonConstants.REGEX.CAMERA_CASE_RELINE_FILE_NAME_NOT_SUPPORT)) {
            return R.error("监控点名称不符合规则");
        }
        List<TreeNode> listData = new ArrayList<TreeNode>();
        if(StringUtils.isNotEmptyString(id) && StringUtils.isNotEmptyString(name)) { //按区域查询监控点名称
            List<Camera> cameras = cameraService.selectVasCameraByName(name, id);
            List<Camera> cameras1 = cameraService.selectVasCameraByName(name, null);
            List<CtrlUnit> mList = ctrlUnitService.findUnitByCameraRegion(id, cameras1, null);
            listData = cameraService.bulidCameraTree(mList, cameras);
        } else if (StringUtils.isNotEmptyString(name) && StringUtils.isEmptyString(id)) { //第一次监控点名称查询
            List<Camera> cameras = cameraService.selectVasCameraByName(name, null);
            List<CtrlUnit> mList = ctrlUnitService.findUnitByCameraRegion(null, cameras, "1");
            listData = cameraService.bulidCameraTree(mList, null);
        } else if (StringUtils.isNotEmptyString(id)) { //不查监控点名称，只查子区域
            List<Camera> cameras = cameraService.findVasCameraByAreaId(id);
            List<CtrlUnit> mList = ctrlUnitService.queryAreaChildrenByParentId(id);
            listData = cameraService.bulidCameraTree(mList, cameras);
        } else { //初始化加载
            List<CtrlUnit> mList = ctrlUnitService.queryTopNode(1L);
            listData = cameraService.bulidTree(mList);
        }
        return R.ok().put("list", listData);
    }

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
        map.put("fromType", VideoTaskConstant.FROM_TYPE.REAL);
        Page<CameraVo> pages = new Page<>(page, rows);
        Page<CameraVo> pageResult = cameraService.selectOnlineAndIpCCameraListByPage(pages, map);
        return R.ok().put("page", new PageUtils(pageResult));
    }

    @ApiOperation("默认监控点查询")
    @PostMapping(value = "/selectDefaultId")
    public R selectDefaultId() {
        Camera camera = cameraService.getById(CameraConstants.DEFAULT_ID);
        return R.ok().put("camera", camera);
    }

    @ApiOperation("佳都低点图片确认接口")
    @PostMapping(value = "/confirmLowerPic")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "图片url字符串"),
            @ApiImplicitParam(name = "startTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "结束时间"),
            @ApiImplicitParam(name = "threshold", value = "相似度阈值"),
            @ApiImplicitParam(name = "page", value = "页码"),
            @ApiImplicitParam(name = "rows", value = "每页数据数")
    })
    public Map<String, Object> confirmLowerPic(String url, String startTime, String endTime, Float threshold,
                                               String page, String rows) throws Exception {
        log.info("start sumbitImageQueryTaskMem...{" + DateTimeUtils.now() + "}");
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("ret", "0");
        result.put("desc", "Success");
        if (DateUtil.getDate(endTime).getTime() - DateUtil.getDate(startTime).getTime() < 1000) {
            result.put("ret", "1");
            result.put("desc", "搜图起止时间异常!");
            return result;
        }
        if (StringUtils.isEmpty(url)) {
            result.put("ret", "1");
            result.put("desc", "目标图片不能为空");
            return result;
        }
        String serialnumber = RandomUtils.get24TimeRandom();
        String objtype = CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_PERSON;
        StringBuilder cameraNames = new StringBuilder();
        cameraNames.append("全部点位");

        String name = TaskConstants.getNameByTargetType(objtype);
        name += "(" + DateUtil.getFormat(new Date(), DateFormatConst.YMDHMS_) + ")";
        TbImagesearchTask imgscTask = new TbImagesearchTask(serialnumber, name, null,
                DateUtil.getDate(startTime), DateUtil.getDate(endTime), StringUtil.getInteger(objtype),
                url, null, null, null, cameraNames.toString());
        tbImagesearchTaskService.save(imgscTask);


        long start = System.currentTimeMillis();
        long returnTime = 0;
        JSONArray results = null;
        long time1 = System.currentTimeMillis();
        // 进行搜图
        long time2 = System.currentTimeMillis();
        String thresholdStr = "";
        if (threshold != null) {
            thresholdStr = String.valueOf((int) (threshold * 100));
        }
        log.info("============>>>>>>image search history time:" + (time2 - time1) + "ms......");
        if (null == imgscTask) {
            result.put("ret", "1");
            result.put("desc", "搜图历史记录不存在或者已删除!");
            return result;
        }
        String picture = imgscTask.getPicture();
        result.put("imgurl", picture);

        String objType = imgscTask.getObjType() + "";
        String start_time = DateUtil.getFormat(startTime, DateFormatConst.YMDHMS_);
        String end_time = DateUtil.getFormat(endTime, DateFormatConst.YMDHMS_);
        long prepareTime = System.currentTimeMillis();
        log.info("============>>>>>>prepare_time:" + (prepareTime - start) + "ms......");

        JSONObject resultVar = (JSONObject) EhcacheUtils.getItem(serialnumber);
        if (resultVar == null) {
            String features = imgscTask.getFeature();
            if (StringUtils.isEmpty(features)) {
                features = imageCommonController.getFeatureByImageUrls(objType, picture);
                imgscTask.setFeature(features);
                tbImagesearchTaskService.updateById(imgscTask);
            }
            Map<String, Object> imageSearchParam = new HashMap<>();
            {
                imageSearchParam.put("objtype", objType);
                imageSearchParam.put("feature", features);
                imageSearchParam.put("autoFetch", 0);
                imageSearchParam.put("starttime", startTime);
                imageSearchParam.put("endtime", endTime);
                imageSearchParam.put("threshold", thresholdStr);
                // 调用搜图接口
                imageSearchParam.put("cameraids", imgscTask.getCameraIds());
            }
            long feature = System.currentTimeMillis();
            log.info("============>>>>>>feature_time:" + (feature - prepareTime) + "ms......");
            String resultJson = featureSearchService.doSearchService(imageSearchParam);
            long jmTime = System.currentTimeMillis();
            log.info("============>>>>>>feature_search_time:" + (jmTime - feature) + "ms......");
            resultVar = JSONObject.parseObject(resultJson);
            if (null == resultVar) {
                log.error("imageSearchParam ---> resultVar is null");
                result.put("ret", "1");
                result.put("desc", "搜图服务调用异常：无返回，请稍后重试!");
                return result;
            }

            if (!"0".equals(resultVar.getString("ret"))) {
                log.error("imageSearchParam --> resultVar is fail: " + resultVar);
                result.put("ret", "1");
                result.put("desc", resultVar.getString("desc"));
                return result;
            }

            long updateTime = System.currentTimeMillis();
            log.info("============>>>>>>update_time:" + (updateTime - jmTime) + "ms......");
            EhcacheUtils.putItem(serialnumber, resultVar);
        }
        long updateTime = System.currentTimeMillis();
        results = resultVar.getJSONArray("results");
        if (results == null || results.isEmpty()) {
            result.put("count", 0);
            result.put("picList", new ArrayList<>());
        } else {
            returnTime = System.currentTimeMillis();
            log.info("============>>>>>>return_time:" + (returnTime - updateTime) + "ms......");
            int pageNum = Integer.valueOf(page);
            int pageSize = Integer.valueOf(rows);
            int startNum = (pageNum - 1) * pageSize;
            int endNum = startNum + pageSize;
            List<JDThreeSenceRequest> picList = new ArrayList<>();
            HashMap<String, Float> scoreMap = new HashMap<>();
            StringBuilder sbUids = new StringBuilder();
            long trackTime = System.currentTimeMillis();
            log.info("============>>>>>>track_time:" + (trackTime - returnTime) + "ms......");
            if (endNum > results.size()) {
                endNum = results.size();
            }
            for (int i = startNum; i < endNum; i++) {
                JSONObject searchResultVar = results.getJSONObject(i);
                if (null == searchResultVar) {
                    continue;
                }

                String uuid = searchResultVar.getString("uuid");
                String date = searchResultVar.getString("date");
                float score = searchResultVar.getFloat("score");
                // [将查询的id和score进行封装，使用JManager批量id查询]
                sbUids.append(uuid).append(",");
                scoreMap.put(uuid, score);
            }
            long esTime = 0;
            if (sbUids.length() > 0 && StringUtils.isNotEmptyString(sbUids.toString())) {
                String uids = sbUids.toString().substring(0, sbUids.length() - 1);
                Map<String, Object> pamaMap = new HashedMap();
                pamaMap.put("objType", objType);
                pamaMap.put("uuid", uids);

                String returnJson = queryAnalysisResultService.doHttpService(pamaMap);

                esTime = System.currentTimeMillis();
                log.info("============>>>>>>es_time:" + (esTime - trackTime) + "ms......");

                List<ResultQueryVo> resultBoList = queryAnalysisResultService.transAllResultToBo(returnJson);

                if (resultBoList != null && !resultBoList.isEmpty()) {
                    for (ResultQueryVo restBo : resultBoList) {
                        float score = scoreMap.get(restBo.getId());

                        postHandleResultBo(picList, restBo, score);
                    }
                } else {
                    result.put("ret", "1");
                    result.put("desc", "暂时没有相关数据！");
                    return result;
                }
            }

            picList = handerBathQueryUidSort(sbUids.toString(), picList);

            long lastTime = System.currentTimeMillis();
            log.info("============>>>>>>last_time:" + (lastTime - esTime) + "ms......");
            result.put("count", picList.size());
            result.put("picList", picList);
            log.info("==============>>>>>>>total_time:" + (lastTime - start) + "ms......");
        }
        return result;
    }

    /**
     * 对返回的resultBo对象进行后置的处理
     *
     * @param picList
     * @param resultBo
     * @param score
     */
    public void postHandleResultBo(final List<JDThreeSenceRequest> picList, final ResultQueryVo resultBo, final float score) {
        resultBo.setDistance(score);
        imageQueryService.resultHandle(resultBo);
        JDThreeSenceRequest jdThreeSenceRequest = new JDThreeSenceRequest();
        jdThreeSenceRequest.setUuid(resultBo.getRecogId());
        jdThreeSenceRequest.setUrl(resultBo.getImgurl());
        jdThreeSenceRequest.setDeviceId(resultBo.getCameraId());
        jdThreeSenceRequest.setAppearTime(resultBo.getStartframepts() + "");
        jdThreeSenceRequest.setDisappearTime(resultBo.getEndframepts() + "");
        jdThreeSenceRequest.setScore(score + "");
        picList.add(jdThreeSenceRequest);
    }

    /**
     * 按照查询的uids的顺序进行数据的封装
     *
     * @param uids    查询的uids
     * @param picList 根据uids查询方法的数据集合
     * @return
     */
    private List<JDThreeSenceRequest> handerBathQueryUidSort(String uids, List<JDThreeSenceRequest> picList) {
        List<JDThreeSenceRequest> sortRsData = new ArrayList<>();
        String[] splitUids = uids.split(",");

        if (splitUids.length == 0) {
            return picList;
        }
        for (String uuid : splitUids) {
            for (int i = 0; i < picList.size(); i++) {
                JDThreeSenceRequest pic = picList.get(i);
                if (pic != null && uuid.equals(pic.getUuid())) {
                    sortRsData.add(pic);
                    break;
                }
            }
        }

        return sortRsData;
    }
}
