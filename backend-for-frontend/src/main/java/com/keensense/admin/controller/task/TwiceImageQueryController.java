package com.keensense.admin.controller.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.keensense.admin.config.ComConfig;
import com.keensense.admin.config.ImageSearchConfig;
import com.keensense.admin.constants.BikeGenreConstants;
import com.keensense.admin.constants.CommonConstants;
import com.keensense.admin.constants.SwaggerTest;
import com.keensense.admin.constants.TaskConstants;
import com.keensense.admin.dto.FileBo;
import com.keensense.admin.entity.task.Camera;
import com.keensense.admin.entity.task.CtrlUnit;
import com.keensense.admin.entity.task.MonitorGroup;
import com.keensense.admin.entity.task.ObjextTrack;
import com.keensense.admin.entity.task.ObjextTrackPic;
import com.keensense.admin.entity.task.TbImagesearchRecord;
import com.keensense.admin.entity.task.TbImagesearchTask;
import com.keensense.admin.entity.task.TbRelaytrackDetail;
import com.keensense.admin.entity.task.VsdTaskRelation;
import com.keensense.admin.mapper.task.TbImagesearchRecordMapper;
import com.keensense.admin.mqtt.config.EnumerationConfig;
import com.keensense.admin.request.ObjextResultRequest;
import com.keensense.admin.request.PageRequest;
import com.keensense.admin.service.ext.FeatureSearchService;
import com.keensense.admin.service.ext.QueryAnalysisResultService;
import com.keensense.admin.service.ext.VideoObjextTaskService;
import com.keensense.admin.service.task.ICameraService;
import com.keensense.admin.service.task.ICtrlUnitService;
import com.keensense.admin.service.task.IImageQueryService;
import com.keensense.admin.service.task.IMonitorGroupService;
import com.keensense.admin.service.task.IObjextTrackPicService;
import com.keensense.admin.service.task.IObjextTrackService;
import com.keensense.admin.service.task.ITbImagesearchTaskService;
import com.keensense.admin.service.task.ITbRelaytrackDetailService;
import com.keensense.admin.service.task.IVsdTaskRelationService;
import com.keensense.admin.service.task.ResultService;
import com.keensense.admin.util.DateTimeUtils;
import com.keensense.admin.util.EhcacheUtils;
import com.keensense.admin.util.Page;
import com.keensense.admin.util.RandomUtils;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.vo.ResultQueryVo;
import com.keensense.common.exception.VideoException;
import com.keensense.common.platform.bo.feature.DumpQuery;
import com.keensense.common.util.PageUtils;
import com.keensense.common.util.R;
import com.keensense.common.validator.ValidatorUtils;
import com.loocme.sys.constance.DateFormatConst;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.util.DateUtil;
import com.loocme.sys.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 17:51 2019/6/15
 * @Version v0.1
 */
@Slf4j
@Api(tags = "查询-以图搜图")
@RestController
@RequestMapping("/twiceImageQuery")
public class TwiceImageQueryController {
    @Resource
    private IImageQueryService imageQueryService;

    @Autowired
    private ICameraService cameraService;

    @Autowired
    private ICtrlUnitService ctrlUnitService;

    @Autowired
    private FeatureSearchService featureSearchService;

    @Autowired
    private IObjextTrackService objextTrackService;

    @Autowired
    private IObjextTrackPicService objextTrackPicService;

    @Autowired
    private QueryAnalysisResultService queryAnalysisResultService;

    @Resource
    private IMonitorGroupService monitorGroupService;

    @Autowired
    private ITbImagesearchTaskService tbImagesearchTaskService;

    @Autowired
    private ImageSearchConfig imageSearchConfig;

    @Autowired
    private VideoObjextTaskService videoObjextTaskService;

    @Autowired
    private IVsdTaskRelationService vsdTaskRelationService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private ComConfig comConfig;

    @Autowired
    private EnumerationConfig enumerationConfig;

    @Autowired
    private TbImagesearchRecordMapper imagesearchRecordMapper;

    @Autowired
    private ITbRelaytrackDetailService tbRelaytrackDetailService;

    /**
     * 以图搜图查询分析结果列表
     *
     * @return
     */
    @ApiOperation("以图搜图初始化查询列表" + SwaggerTest.DEBUG)
    @PostMapping("initObjextResult")
    public R initObjextResult(ObjextResultRequest objextResultRequest) {
        ValidatorUtils.validateEntity(objextResultRequest);
        R result = R.ok();
        Page<FileBo> pages = new Page<>(objextResultRequest.getPage(), objextResultRequest.getRows());
        // 查询目标结果
        Map<String, Object> resultBoMap = imageQueryService.initObjextResult(objextResultRequest.getObjtype(), pages, objextResultRequest.getStartTime(), objextResultRequest.getEndTime());
        int totalRecords = (int) resultBoMap.get("totalNum");
        List<Object> resultBoList = (List) resultBoMap.get("resultBoList");
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Object> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(objextResultRequest.getPage(), objextResultRequest.getRows());
        page.setRecords(resultBoList);
        page.setTotal(totalRecords);
        return result.put("page", new PageUtils(page));
    }

    @ApiOperation("提交搜图任务" + SwaggerTest.DEBUG)
    @PostMapping("submitImageQueryTaskMem")
    public R sumbitImageQueryTaskMem(String serialnumber, String caseId, String cameraId, String picture,
                                     String feature, String objtype, String startTime, String endTime, String uuid,
                                     String taskId, String userSerialnumber) {
        log.info("start sumbitImageQueryTaskMem...{" + DateTimeUtils.now() + "}");
        R result = R.ok();
        if (DateUtil.getDate(endTime).getTime() - DateUtil.getDate(startTime).getTime() < 1000) {
            return R.error("搜图起止时间异常!");
        }
        if (StringUtils.isEmpty(picture)) {
            return R.error("目标图片不能为空");
        }
        if (StringUtils.isEmpty(serialnumber)) {
            serialnumber = RandomUtils.get24TimeRandom();
        } else {
            EhcacheUtils.removeItem(serialnumber);
        }
        if (feature == null) {
            feature = "";
        }
        String cameraIds = "";
        StringBuilder cameraNames = new StringBuilder();
        //获取监控点信息
        if (StringUtil.isNotNull(cameraId)) {
            if (cameraId.endsWith(",")) {
                cameraId = cameraId.substring(0, cameraId.length() - 1);
            }

            List<String> cameraAndAreaIdList = new ArrayList<>();
            String[] cameraArr = cameraId.split(",");
            for (int j = 0; j < cameraArr.length; j++) {
                cameraAndAreaIdList.add(cameraArr[j]);
            }

            cameraIds = cameraService.handleQueryCameraIds(cameraAndAreaIdList);

            long time3 = System.currentTimeMillis();

            if (StringUtils.isEmptyString(cameraIds)) {
                return R.error("暂时没有监控点相关数据哦...");
            }

            //cameraAndAreaIdList: a_80745444,a_99394041,a_42407631,c_1543290865430
            for (String cameraAndAreaId : cameraAndAreaIdList) {
                String cameraAndAreaName = "";
                String id = cameraAndAreaId.substring(2);
                if (cameraAndAreaId.startsWith("a_")) {
                    CtrlUnit ctrlUnit = ctrlUnitService.selectByUnitIdentity(id);
                    if (ctrlUnit != null) {
                        cameraAndAreaName = ctrlUnit != null ? ctrlUnit.getUnitName() : "";
                    }
                } else if (cameraAndAreaId.startsWith("c_")) {
                    Camera camera = cameraService.getById(id);
                    if (camera != null) {
                        cameraAndAreaName = camera.getName();
                    }
                } else if (cameraAndAreaId.startsWith("m_")) {
                    MonitorGroup monitorGroup = monitorGroupService.getById(id);
                    if (monitorGroup != null) {
                        cameraAndAreaName = monitorGroup.getGroupName();
                    }
                }
                if (StringUtils.isNotEmptyString(cameraAndAreaName)) {
                    if (cameraNames.length() > 0) {
                        cameraNames.append(",");
                    }
                    cameraNames.append(cameraAndAreaName);
                }
            }
            long time4 = System.currentTimeMillis();
            log.info("============>>>>>>deal camaera name time:" + (time4 - time3) + "ms......");
        } else {
            cameraNames.append("全部点位");
        }
        String name = TaskConstants.getNameByTargetType(objtype);
        name += "(" + DateUtil.getFormat(new Date(), DateFormatConst.YMDHMS_) + ")";
        JSONArray uuidPictrueJson = new JSONArray();
        if (StringUtils.isNotEmptyString(uuid)) {
            String[] pictrueStr = picture.split(",");
            String[] uuidStr = uuid.split(",");
            for (int i = 0; i < pictrueStr.length; i++) {
                JSONObject jsonObject = new JSONObject();
                if ("uploadUuid".equals(uuidStr[i])) {
                    jsonObject.put("", pictrueStr[i]);
                } else {
                    jsonObject.put(uuidStr[i], pictrueStr[i]);
                }

                uuidPictrueJson.add(jsonObject);
            }
        }
        TbImagesearchTask imgscTask = new TbImagesearchTask(serialnumber, name, caseId, DateUtil.getDate(startTime),
                DateUtil.getDate(endTime), StringUtil.getInteger(objtype), picture, feature, cameraId, cameraIds,
                cameraNames.toString(), JSONArray.toJSONString(uuidPictrueJson), taskId, userSerialnumber);
        tbImagesearchTaskService.saveOrUpdate(imgscTask);
        result.put("serialnumber", serialnumber);
        result.put("caseId", caseId);
        return result;
    }
    @ApiOperation("搜索以图搜图任务结果" + SwaggerTest.DEBUG)
    @PostMapping("queryImageQueryTaskMemByTaskId")
    public R queryImageListMem(HttpServletRequest request, String serialnumber, Integer page, Integer rows,
                               String userSerialnumber) throws Exception {
        R result = R.ok();
        JSONArray featureSearchResults;
        long time1 = System.currentTimeMillis();
        // 1.查询搜图任务
        TbImagesearchTask imgscTask = tbImagesearchTaskService.getById(serialnumber);
        long time2 = System.currentTimeMillis();
        log.info("============>>>>>>image search history time:" + (time2 - time1) + "ms......");
        if (null == imgscTask) {
            return R.error("搜图历史记录不存在或者已删除!");
        }
        result.put("imgurl", imgscTask.getPicture());

        //使用目标检测接口获取结构化属性值及搜图特征值
        //List<Map<String,Object>> structs = getStructsFromPictures(picture,objType);
        //使用dump接口获取结构化属性及搜图特征
        List<Map<String, Object>> structs = getStructsFromDumps(imgscTask, imgscTask.getUserSerialnumber());

        JSONObject featureSearchResultVar = imageFeatureSearch(imgscTask,structs);
        if (null == featureSearchResultVar) {
            log.error("imageSearchParam ---> resultVar is null");
            return R.error("搜图服务调用异常：无返回，请稍后重试!");
        }

        if (!"0".equals(featureSearchResultVar.getString("ret"))) {
            log.error("imageSearchParam --> resultVar is fail: " + featureSearchResultVar);
            return R.error(featureSearchResultVar.getString("desc"));
        }
        featureSearchResults = featureSearchResultVar.getJSONArray("results");
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<ResultQueryVo> pages =
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page(page, rows);

        pages.setTotal(0);
        pages.setRecords(new ArrayList<>());

        if (featureSearchResults == null || featureSearchResults.isEmpty()) {
            result.put("page", new PageUtils(pages));
            return result;
        }

        String objType = imgscTask.getObjType() + "";
        //过滤开关开启,开启置顶时不过滤
        if (isFilterSwitchOpen(objType) && !imageSearchConfig.getTopgetMem()) {
            handleFeatureSearchResultsByConditionFilter(pages,featureSearchResults,objType,serialnumber,structs);
        }else{
            handleFeatureSearchResults(pages,featureSearchResults,objType,serialnumber,structs);
        }
        result.put("page", new PageUtils(pages));
        return result;
    }

    /** 检查是否开启属性过滤
     * */
    private boolean isFilterSwitchOpen(String objType){
        boolean flag = false;
        if (!imageSearchConfig.getEnable()) {
            return flag;
        }
        if (imageSearchConfig.getFilterType() != null && imageSearchConfig.getFilterType().indexOf(objType)>-1) {
            flag = true;
        }

        return flag;

    }
    /**
     *
     * */
    private String getAllIdsAsString(JSONArray results){
        StringBuilder sbUids = new StringBuilder();
        for (int i = 0; i < results.size(); i++) {
            JSONObject searchResultVar = results.getJSONObject(i);
            if (null == searchResultVar) {
                continue;
            }
            String uuid = searchResultVar.getString("uuid");
            sbUids.append(uuid).append(",");
        }

        return sbUids.toString();
    }
    /**
     *
     * */
    private String getPageIdsAsString(JSONArray results,int startNum, int endNum){
        StringBuilder sbUids = new StringBuilder();
        for (int i = startNum; i < endNum; i++) {
            JSONObject searchResultVar = results.getJSONObject(i);
            if (null == searchResultVar) {
                continue;
            }
            String uuid = searchResultVar.getString("uuid");
            sbUids.append(uuid).append(",");
        }

        return sbUids.toString();
    }
    /**
    *  uuid与相似度映射关系
     * */
    private HashMap<String, Float> getAllIdScoreMapping(JSONArray results){
        HashMap<String, Float> scoreMap = new HashMap<>();
        for (int i = 0; i < results.size(); i++) {
            JSONObject searchResultVar = results.getJSONObject(i);
            if (null == searchResultVar) {
                continue;
            }
            String uuid = searchResultVar.getString("uuid");
            float score = searchResultVar.getFloat("score");
            // [将查询的id和score进行封装，使用JManager批量id查询]
            scoreMap.put(uuid, score);
        }

        return scoreMap;
    }
    /**
     *  uuid与相似度映射关系
     * */
    private HashMap<String, Float> getIdScoreMapping(JSONArray results,int startNum, int endNum){
        HashMap<String, Float> scoreMap = new HashMap<>();
        for (int i = startNum; i < endNum; i++) {
            JSONObject searchResultVar = results.getJSONObject(i);
            if (null == searchResultVar) {
                continue;
            }
            String uuid = searchResultVar.getString("uuid");
            float score = searchResultVar.getFloat("score");
            // [将查询的id和score进行封装，使用JManager批量id查询]
            scoreMap.put(uuid, score);
        }

        return scoreMap;
    }


    /**
     *  获取前top个数据ids
     * */
    private String getTopIdsAsString(JSONArray results){
        StringBuilder topIds = new StringBuilder();
        for (int i = 0; i < results.size(); i++) {
            JSONObject searchResultVar = results.getJSONObject(i);
            if (null == searchResultVar) {
                continue;
            }
            String uuid = searchResultVar.getString("uuid");
            //根据配置保留前top位数据不做过滤处理
            String topStr = imageSearchConfig.getTop();
            if (StringUtils.isNotEmptyString(topStr)) {
                int top = Integer.parseInt(topStr);
                if (top > i) {
                    topIds.append(uuid).append(",");
                }
            }
        }

        return topIds.toString();
    }
    /**
     * 搜图模块返回结果处理
     * */
    private void handleFeatureSearchResults(com.baomidou.mybatisplus.extension.plugins.pagination
                                                                     .Page<ResultQueryVo>
                                                                     pages, JSONArray results, String objType, String serialnumber, List<Map<String, Object>> structs) {
        int pageNum = (int) pages.getCurrent();
        int pageSize = (int) pages.getSize();
        int startNum = (pageNum - 1) * pageSize;
        int endNum = startNum + pageSize;
        List<ResultQueryVo> rstData = new ArrayList<>();

        HashMap<String, Float> scoreMap;
        String sbUids;

        if (endNum > results.size()) {
            endNum = results.size();
        }
        if (imageSearchConfig.getTopgetMem()) {
            Map<String, Object> secondSortMap = topTarget(results, startNum, endNum, serialnumber);
            sbUids = (String) secondSortMap.get("sbUids");
            scoreMap = (HashMap<String, Float>) secondSortMap.get("scoreMap");
        } else {
            sbUids = getPageIdsAsString(results, startNum, endNum);
            scoreMap = getIdScoreMapping(results, startNum, endNum);
        }


        long esTime = 0;
        if (sbUids.length() > 0 && StringUtils.isNotEmptyString(sbUids)) {
            String uids = sbUids.substring(0, sbUids.length() - 1);
            Map<String, Object> pamaMap = new HashedMap();
            pamaMap.put("objType", objType);
            pamaMap.put("uuid", uids);

            String returnJson = queryAnalysisResultService.doHttpService(pamaMap);

            esTime = System.currentTimeMillis();

            List<ResultQueryVo> resultBoList = queryAnalysisResultService.transAllResultToBo(returnJson);

            if (resultBoList != null && !resultBoList.isEmpty()) {
                for (ResultQueryVo restBo : resultBoList) {
                    float score = scoreMap.get(restBo.getId());
                    long returnTime = System.currentTimeMillis();
                    Set<String> trackResultIds = objextTrackService.queryObjextTrackResultidsBySerialnumber(serialnumber);
                    long trackTime = System.currentTimeMillis();
                    log.info("============>>>>>>track_time:" + (trackTime - returnTime) + "ms......");
                    Map<String, String[]> cameraInfos = new HashMap<>();
                    postHandleResultBo(rstData, restBo, score, cameraInfos, trackResultIds);
                }
            } else {
                R.error("暂时没有相关数据哦...");
            }
        }
        rstData = handerBathQueryUidSort(sbUids, rstData);

        long lastTime = System.currentTimeMillis();
        log.info("============>>>>>>last_time:" + (lastTime - esTime) + "ms......");
        pages.setTotal(results.size());
        pages.setRecords(rstData);
    }

    /**
     *
     * */
    private Map<String,Object> queryResultsOrderedIdsByConditionFilter(Map<String, Object> pamaMap, String uids, String
            topIds){
        int size = 0;
        Map<String,Object> resultMap = new HashMap<>();
        String returnJson = queryAnalysisResultService.doHttpService(pamaMap);
        JSONObject var = JSONObject.parseObject(returnJson);
        JSONArray weekArray = null;
        int len = 0;
        JSONObject object;
        object = var.getJSONObject("MotorVehicleListObject");
        if (object != null) {
            weekArray = object.getJSONArray("MotorVehicleObject");
            len = weekArray.size();
        }
        if (len == 0) {
            object = var.getJSONObject("NonMotorVehicleListObject");
            if (object != null) {
                weekArray = object.getJSONArray("NonMotorVehicleObject");
            }
        }
        if (len == 0) {
            object = var.getJSONObject("PersonListObject");
            if (object != null) {
                weekArray = object.getJSONArray("PersonObject");
            }
        }

        if(weekArray == null){
            resultMap.put("size",0);
            resultMap.put("uuids","");
            return resultMap ;
        }
        StringBuilder idsES = new StringBuilder();
        for (int i = 0; i < weekArray.size(); i++) {
            JSONObject obj = weekArray.getJSONObject(i);
            String id = obj.getString("Id");
            idsES.append(id).append(",");
        }
        String[] splitUids = uids.split(",");
        StringBuilder resultUids = new StringBuilder();
        for (String uuid : splitUids) {

            if ((StringUtils.isNotEmptyString(topIds) && topIds.contains(uuid)) ||
                    idsES.toString().contains(uuid)) {
                resultUids.append(uuid).append(",");
                size++;
            }
        }
        resultMap.put("size",size);
        resultMap.put("uuids",resultUids.toString());
        return resultMap ;
    }

    /**
     * 搜图模块返回结果处理-配置属性条件过滤版
     * */
    private void handleFeatureSearchResultsByConditionFilter(com.baomidou.mybatisplus.extension.plugins.pagination
                                                        .Page<ResultQueryVo>
                                                    pages, JSONArray results, String objType, String serialnumber, List<Map<String, Object>> structs) {
        int pageNum = (int) pages.getCurrent();
        int pageSize = (int) pages.getSize();
        int startNum = (pageNum - 1) * pageSize;
        int endNum = startNum + pageSize;

        HashMap<String, Float> scoreMap;
        String sbUids;
        String topIds = "";
        sbUids = getAllIdsAsString(results);
        scoreMap = getAllIdScoreMapping(results);
        topIds = getTopIdsAsString(results);
        if (StringUtils.isEmpty(sbUids)) {
            return;
        }

        String uids = sbUids.substring(0, sbUids.length() - 1);
        Map<String, Object> pamaMap = new HashedMap();
        pamaMap.put("objType", objType);
        pamaMap.put("uuid", uids);
        pamaMap.put("rows", results.size());

        //根据上传图片的结构化数据，添加条件过滤搜图结果
        Map<String, Object> condition = getSearchCondition(structs);
        pamaMap.putAll(condition);

        Map<String, Object> resultMap = queryResultsOrderedIdsByConditionFilter(pamaMap, uids, topIds);
        int size = (int)resultMap.get("size");
        String resultUids = (String)resultMap.get("uuids");

        if (endNum > size) {
            endNum = size;
        }

        StringBuffer pageIds = new StringBuffer();
        String[] ids = resultUids.substring(0, resultUids.length() - 1).split(",");
        for (int i = startNum; i < endNum; i++) {
            pageIds.append(ids[i]).append(",");
        }

        Map<String, Object> pamaMap2 = new HashedMap();
        pamaMap2.put("objType", objType);
        pamaMap2.put("uuid", pageIds);
        String returnJsonPage = queryAnalysisResultService.doHttpService(pamaMap2);
        long esTime = System.currentTimeMillis();
        List<ResultQueryVo> resultBoList = queryAnalysisResultService.transAllResultToBo(returnJsonPage);
        log.info("============>>>>>>es_query_amount:" + size + "......");

        List<ResultQueryVo> rstData = new ArrayList<>();
        if (resultBoList != null && !resultBoList.isEmpty()) {
            for (ResultQueryVo restBo : resultBoList) {
                float score = scoreMap.get(restBo.getId());
                long returnTime = System.currentTimeMillis();
                Set<String> trackResultIds = objextTrackService.queryObjextTrackResultidsBySerialnumber(serialnumber);
                long trackTime = System.currentTimeMillis();
                log.info("============>>>>>>track_time:" + (trackTime - returnTime) + "ms......");
                log.info("============>>>>>>featuresearch_amount:" + results.size() + "......");
                Map<String, String[]> cameraInfos = new HashMap<>();
                postHandleResultBo(rstData, restBo, score, cameraInfos, trackResultIds);
            }
        } else {
            R.error("暂时没有相关数据哦...");
        }
        rstData = handerBathQueryUidSort(sbUids, rstData);
        long lastTime = System.currentTimeMillis();
        log.info("============>>>>>>last_time:" + (lastTime - esTime) + "ms......");
        pages.setTotal(size);
        pages.setRecords(rstData);

    }


    private JSONObject imageFeatureSearch(TbImagesearchTask imgscTask, List<Map<String, Object>> structs) throws Exception {
        String objType = imgscTask.getObjType() + "";
        String startTime = DateUtil.getFormat(imgscTask.getStartTime(), DateFormatConst.YMDHMS_);
        String endTime = DateUtil.getFormat(imgscTask.getEndTime(), DateFormatConst.YMDHMS_);
        long prepareTime = System.currentTimeMillis();
        JSONObject resultVar = (JSONObject) EhcacheUtils.getItem(imgscTask.getId());
        if (resultVar == null) {
            String features = imgscTask.getFeature();
            if (StringUtils.isEmpty(features)) {
                features = getFeatures(structs);
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
                imageSearchParam.put("cameraids", imgscTask.getCameraIds());
                imageSearchParam.put("resultLimit", imageSearchConfig.getResultLimit());
                imageSearchParam.put("similarityLimit", setSimilarityByType(objType));
            }
            long feature = System.currentTimeMillis();
            log.info("============>>>>>>get feature_time:" + (feature - prepareTime) + "ms......");
            // 调用搜图接口
            String resultJson = featureSearchService.doSearchService(imageSearchParam);
            long jmTime = System.currentTimeMillis();
            log.info("============>>>>>>feature_search_time:" + (jmTime - feature) + "ms......");
            resultVar = JSONObject.parseObject(resultJson);


            if (resultVar != null && !"0".equals(resultVar.getString("ret"))) {
                EhcacheUtils.putItem(imgscTask.getId(), resultVar);
            }


        }
        return resultVar;
    }

    private String setSimilarityByType(String type){
        String sim = "0.5";
        if(CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_PERSON.equals(type)){
            sim = imageSearchConfig.getThresholdPerson();
        }else if(CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_CAR.equals(type)){
            sim = imageSearchConfig.getThresholdVehicle();
        }else if(CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_CAR_PERSON.equals(type)){
            sim = imageSearchConfig.getThresholdBike();
        }

        return sim;
    }

    private String getFeatures(List<Map<String, Object>> structResults) {
        StringBuilder features = new StringBuilder();
        for (int i = 0; i < structResults.size(); i++) {
            if (features.length() > 0) {
                features.append(",");
            }
            features.append(structResults.get(i).get("feature"));
        }
        return features.toString();
    }

    /**
     * 根据配置设置过滤条件
     *
     * @param structResults 图片结构化返回数据
     * @return
     */
    private Map<String, Object> getSearchCondition(List<Map<String, Object>> structResults) {
        Map<String, Object> resutlMap = new HashedMap();
        String optimizeItems = imageSearchConfig.getOptimizeItems();
        if (StringUtils.isNotEmptyString(optimizeItems)) {

            String[] conditions = optimizeItems.split(",");
            for (int i = 0; i < conditions.length; i++) {
                StringBuilder condition = new StringBuilder();
                String cond = conditions[i];
                Set<String> types = new HashSet<>();
                for (int j = 0; j < structResults.size(); j++) {
                    Map<String, Object> structResult = structResults.get(j);
                    if (structResults.get(j).containsKey(cond)) {
                        if (condition.length() > 0) {
                            condition.append(",");
                        }
                        if (cond.indexOf("color") >= 0) {
                            String conret = (String) structResult.get(cond);
                            condition.append(getColorGroup(conret));
                        } else if (cond.indexOf("cartype") >= 0) {
                            String conret = (String) structResult.get(cond);
                            condition.append(getCarTypeGroup(conret));
                        } else if (cond.indexOf("wheels") >= 0) {
                            String wheels = (String) structResult.get(cond);
                            //非机动车只针对二轮车进行过滤
                            if ("2".equals(wheels)) {
                                types.add(wheels);
                            }

                        } else if (cond.indexOf("bikegenre") >= 0) {
                            String bikegenre = (String) structResult.get(cond);
                            if (!"-1".equals(bikegenre)) {
                                types.add(bikegenre);
                            }

                        } else if (cond.indexOf("sex") >= 0) {
                            String sex = (String) structResult.get(cond);
                            if (!"-1".equals(sex)) {
                                types.add(sex);
                            }

                        }
                        else {
                            condition.append((String) structResult.get(cond));
                        }

                    }
                }
                if (types.size() == 1) {
                    resutlMap.put(conditions[i], types.iterator().next());
                } else {
                    resutlMap.put(conditions[i], condition);
                }

            }

        }
        return resutlMap;
    }

    /**
     * 查询颜色所属色系配置
     */
    private String getColorGroup(String color) {
        String result = "";
        String[] colorGroups = imageSearchConfig.getColorGroups();
        for (int i = 0; i < colorGroups.length; i++) {
            String[] group = colorGroups[i].split(",");
            for (int j = 0; j < group.length; j++) {
                if (color.equals(group[j])) {
                    result = colorGroups[i];
                }
            }
        }
        return result;
    }

    /**
     * 查询车型所在分组
     */
    private String getCarTypeGroup(String cartype) {
        String result = "";
        String[] typeGroups = imageSearchConfig.getCarTypeGroups();
        for (int i = 0; i < typeGroups.length; i++) {
            String[] group = typeGroups[i].split(",");
            for (int j = 0; j < group.length; j++) {
                if (cartype.equals(group[j])) {
                    result = typeGroups[i];
                }
            }
        }
        return result;
    }

    /**
     * 调用dump接口获取图片结构化特征
     *
     * @param imgscTask
     * @return
     */
    public List<Map<String, Object>> getStructsFromDumps(TbImagesearchTask imgscTask, String userSerialnumber) throws Exception {
        List<Map<String, Object>> resultMap = new ArrayList<>();
        String[] serialnumberStr = imgscTask.getTaskId().split(",");
        String[] userSerialnumberStr = userSerialnumber.split(",");
        DumpQuery dumpQuery = new DumpQuery();
        //时间范围默认六个月 结束时间往后推两天避免客户端服务器与分析服务器时间不一致
        Date endTime = DateTimeUtils.getAfterDaysDateTime(2);
        Date startTime = DateTimeUtils.getBeforeDaysDateTime(180);
        dumpQuery.setFrom(DateTimeUtils.formatDate(startTime, null));
        dumpQuery.setTo(DateTimeUtils.formatDate(endTime, null));
        dumpQuery.setObjType(imgscTask.getObjType());
        String objType = String.valueOf(imgscTask.getObjType());
        JSONArray jsonArray = JSONArray.parseArray(imgscTask.getUuidPictrueJson());
        String[] pictureArray = imgscTask.getPicture().split(",");

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Iterator<String> key = jsonObject.keySet().iterator();
            String uuid = key.next();
            Map<String, Object> structs;
            ResultQueryVo resultQueryVo = resultService.getResultBoById(objType, uuid);
            if (resultQueryVo == null || StringUtils.isEmptyString(uuid)) {
                structs = getStructsFromPicture(pictureArray[i], objType);
            } else {
                VsdTaskRelation vsdTaskRelation =
                        vsdTaskRelationService.queryVsdTaskRelationBySerialnumber(userSerialnumberStr[i]);
                if (vsdTaskRelation == null) {
                    TbRelaytrackDetail tbRelaytrackDetail = tbRelaytrackDetailService.getById(userSerialnumberStr[i]);
                    if (tbRelaytrackDetail != null) {
                        dumpQuery.setStartAt(DateTimeUtils.formatDate(tbRelaytrackDetail.getCreateTime(), null));
                    }
                } else {
                    if (2 == vsdTaskRelation.getFromType() || 5 == vsdTaskRelation.getFromType()) {
                        dumpQuery.setStartAt(DateTimeUtils.formatDate(vsdTaskRelation.getEntryTime(), null));
                    }
                }
                dumpQuery.setTask(serialnumberStr[i]);
                dumpQuery.setUuid(uuid);
                structs = getStructsFromdump(dumpQuery);
                if (StringUtils.isEmptyString(String.valueOf(structs.get("feature")))) {
                    structs = getStructsFromPicture(jsonObject.getString(uuid), objType);
                }
                //满足搜图结果过滤
                structs.put("picture", jsonObject.getString(uuid));
                structs.put("objtype", objType);


                if ("1".equals(objType)) {
                    String sex = String.valueOf(resultQueryVo.getSex());
                    structs.put("sex", sex);
                    Map<String, String> personCoatcolor =
                            StringUtils.splitEnumerationNameForCode(enumerationConfig.getColorGroups());
                    structs.put("coatcolor1", personCoatcolor.get(resultQueryVo.getUpcolorStr())==null?"":personCoatcolor.get(resultQueryVo.getUpcolorStr()));
                } else if ("2".equals(objType)) {
                    Map<String, String> vehiclecolor =
                            StringUtils.splitEnumerationNameForCode(enumerationConfig.getColorGroups());
                    structs.put("vehiclecolor", vehiclecolor.get(resultQueryVo.getCarcolor())==null?"":vehiclecolor.get(resultQueryVo.getCarcolor()));
                    Map<String, String> vehicleClass =
                            StringUtils.splitEnumerationNameForCode(enumerationConfig.getVehicleClass());
                    structs.put("cartype", vehicleClass.get(resultQueryVo.getVehiclekind())==null?"":vehicleClass.get
                            (resultQueryVo.getVehiclekind()));
                } else if ("4".equals(objType)) {
                    Map<String, String> bikeCoatcolor =
                            StringUtils.splitEnumerationNameForCode(enumerationConfig.getColorGroups());
                    structs.put("coatcolor1", bikeCoatcolor.get(resultQueryVo.getPassengersUpColorStr())==null?"":bikeCoatcolor.get(resultQueryVo.getPassengersUpColorStr()));
                    String bikeGenre = String.valueOf(resultQueryVo.getBikeGenre());
                    String wheels = "";
                    if ("2".equals(bikeGenre) || "3".equals(bikeGenre)) {
                        wheels = "2";//轮子数为2：摩托车、自行车
                    }
                    structs.put("wheels", wheels);
                    structs.put("bikegenre", bikeGenre);
                }
            }
            resultMap.add(structs);
        }
        return resultMap;
    }

    public Map<String, Object> getStructsFromdump(DumpQuery dumpQuery) {
        Map<String, Object> var = new HashMap<>();
        String reString = featureSearchService.doFeatureDumpService(dumpQuery);
        JSONObject jsonObject = JSONObject.parseObject(reString);
        String ret = jsonObject.getString("ret");
        if (StringUtils.isEmptyString(ret) || !ret.equals("0")) {
            return var;
        }
        JSONArray jsonArray = jsonObject.getJSONArray("features");
        if (jsonArray != null && jsonArray.size() > 0) {
            JSONObject jsonObjectOne = (JSONObject) jsonArray.get(0);
            String feartureData = jsonObjectOne.getString("feat");
            var.put("feature", feartureData);
        } else {
            return var;
        }
        return var;
    }

    /**
     * 调用图片检测接口获取图片结构化特征
     *
     * @param pictureUrls 上传图片
     * @param objtype     目标类型
     * @return
     */
    public List<Map<String, Object>> getStructsFromPictures(String pictureUrls, String objtype) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        String[] pics = pictureUrls.split(",");
        for (String pic : pics) {
            Map<String, Object> structsFromPicture = getStructsFromPicture(pic, objtype);
            result.add(structsFromPicture);
        }
        return result;
    }

    /**
     * 根据图片获取特征值
     *
     * @param objtype
     * @param pictureUrl
     * @return
     */
    private Map<String, Object> getStructsFromPicture(String pictureUrl, String objtype) throws Exception {
        Map<String, Object> var = new HashMap<>();
        var.put("picture", com.keensense.common.util.ImageUtils.getURLImage(pictureUrl));
        var.put("objtype", objtype);
        String faceFeature = comConfig.getFaceFeature();
        log.info("faceFeature格林人脸特征搜索+++:" + faceFeature);
        if (StringUtils.isNotEmptyString(objtype) && "3".equals(objtype) && "1".equals(faceFeature)) {//格林人脸单独调pictureStream接口获取特征
            String reString = featureSearchService.doExtractFromPictureGLFace(pictureUrl);
            JSONObject jsonObject = JSONObject.parseObject(reString);
            String ret = jsonObject.getString("ret");
            if (StringUtils.isEmptyString(ret) || !ret.equals("0")) {
                log.error("调用pictureStream人脸结构化失败" + jsonObject.toString());
                throw new VideoException("调用pictureStream人脸结构化失败");
            }
            JSONArray jsonArray = jsonObject.getJSONArray("faces");
            if (jsonArray != null && jsonArray.size() > 0) {
                JSONObject jsonObjectOne = (JSONObject) jsonArray.get(0);
                String feartureData = jsonObjectOne.getString("features");
                var.put("feature", feartureData);
            } else {
                throw new VideoException("调用pictureStream获取人脸特征数据缺失");
            }
            return var;
        }
        String reString = featureSearchService.doExtractFromPictureService(var);
        Var resultVar = Var.fromJson(reString);
        if (StringUtil.isNull(reString)) {
            throw new VideoException(700, "结构化数据获取失败");
        }
        String ret = resultVar.getString("ret");
        if (!"0".equals(ret)) {
            throw new VideoException("视图库:" + resultVar.getString("desc"));
        }

        String feartureData = resultVar.getString("feature");
        if (StringUtils.isEmpty(feartureData)) {
            throw new VideoException(700, "搜图模块异常");
        }
        String color = resultVar.getString("color");
        var.put("vehiclecolor", color);
        var.put("coatcolor1", color);
        String bikeGenre = BikeGenreConstants.getGbBikeGenre(resultVar.getString("bikeGenre"));
        String wheels = "";

        if ("2".equals(bikeGenre) || "3".equals(bikeGenre)) {
            wheels = "2";//轮子数为2：摩托车、自行车
        }
        String cartype = resultVar.getString("VehicleClass");

        String sex = resultVar.getString("sexType");

        var.put("wheels", wheels);
        var.put("sex", sex);
        var.put("bikegenre", bikeGenre);
        var.put("feature", feartureData);
        var.put("cartype", cartype);
        return var;
    }

    /**
     * 对返回的resultBo对象进行后置的处理
     *
     * @param rstData
     * @param resultBo
     * @param score
     * @param cameraInfos
     * @param trackResultIds 本次查询序列号下目标库的数据
     */
    public void postHandleResultBo(final List<ResultQueryVo> rstData, final ResultQueryVo resultBo, final float score,
                                   final Map<String, String[]> cameraInfos, final Set<String> trackResultIds) {

        resultBo.setDistance(score);

        String serialnum = resultBo.getSerialnumber();
        String id = resultBo.getId();
        if (!cameraInfos.containsKey(serialnum)) {
            // 通过serialnumber获取监控点id和name
//            Map<String, Object> cameraMap = Select.searchOne("select * from camera where id in (select t1.camera_id from tb_relaytrack_detail t1 where t1.analysis_id='" + serialnum + "')");
//            if (null != cameraMap) {
//                cameraInfos.put(serialnum, new String[]{MapUtil.getString(cameraMap, "id"), MapUtil.getString(cameraMap, "name")});
//            }
        }
        String[] cameraInfo = cameraInfos.get(serialnum);
        if (null != cameraInfo) {
            resultBo.setCameraId(cameraInfo[0]);
            resultBo.setCameraName(cameraInfo[1]);
        }
        if (trackResultIds != null && !trackResultIds.isEmpty()) {
            // 判断当前返回的图片是否在目标库中
            boolean contains = trackResultIds.contains(id);
            if (contains) {
                resultBo.setIsTrack("1");
            }
        }
        imageQueryService.resultHandle(resultBo);
        rstData.add(resultBo);
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
        Map<String, ResultQueryVo> resultQueryVoMap = new HashMap<>();
        if (splitUids.length == 0) {
            return rstData;
        }

        for (ResultQueryVo resultQueryVo : rstData) {
            resultQueryVoMap.put(resultQueryVo.getRecogId(), resultQueryVo);
        }
        for (String uuid : splitUids) {
            ResultQueryVo resultBo = resultQueryVoMap.get(uuid);
            if (resultBo != null) {
                sortRsData.add(resultBo);
            } else {
                log.info("uuid lost:" + uuid);
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
        List<ResultQueryVo> trackList = camerasTarget(targetId);
        return R.ok().put("list", trackList);
    }

    public List<ResultQueryVo> camerasTarget(String serialnumber) {
        List<ResultQueryVo> resultBoList = new ArrayList<>();
        List<ObjextTrack> trackList = objextTrackService.queryObjextTrackBySerialnumber(serialnumber, null);
        if (trackList != null && !trackList.isEmpty()) {
            Map<String, ObjextTrack> trackMap = new HashMap<>();
            trackList.forEach(track -> trackMap.put(track.getResultid(), track));
            String ids = String.join(",", trackMap.keySet());
            Map<String, Object> pamaMap = new HashedMap();
            pamaMap.put("uuid", ids);
            Map<String, Object> returnJson = queryAnalysisResultService.doHttpService(pamaMap, null);
            resultBoList = (List<ResultQueryVo>) returnJson.get("resultBoList");
            for (ResultQueryVo resultQueryVo : resultBoList) {
                //添加已搜图状态
                resultQueryVo.setSearchStatus(trackMap.get(resultQueryVo.getId()).getSearchStatus());
                //添加收藏id号
                resultQueryVo.setTrackId((String.valueOf(trackMap.get(resultQueryVo.getId()).getId())));
            }
        }

        return resultBoList;
    }

    /**
     * 保存图片到轨迹库
     * <p>
     * trackType 1代表结果图片 2代表轨迹图片
     *
     * @return
     */
//    @PostMapping("addImageTractTarget")
//    @ApiOperation("添加目标库")
    public R saveImg2TractTz(String resultId, String objtype, String cameraId, String targetId,
                             String trackTime, String trackType, String trackImg, String analysiaId, String uuid,
                             String serialnumber) {
        R result = R.ok();
        Map<String, Object> datas = new HashMap<>();
        if (StringUtils.isEmptyString(trackType)) {
            return R.error("目标库类型为空");
        }

        boolean flag = true;
        if (Integer.valueOf(trackType) == CommonConstants.JLZZ_TRACKTYPE_RESULT) {
            //先查看是否已经插入目标库
            ObjextTrack track = objextTrackService.getOne(new QueryWrapper<ObjextTrack>().eq("resultid", resultId).eq("serialnumber", targetId));
            if (track == null) {
                ObjextTrack objextTrack = new ObjextTrack();
                objextTrack.setResultid(resultId);
                objextTrack.setSerialnumber(targetId);

                objextTrack.setCameraid(Long.parseLong(cameraId));

                objextTrack.setTracktime(new Date());
                objextTrack.setObjtype(Integer.parseInt(objtype));
                objextTrack.setInfo1(analysiaId);
                objextTrack.setInfo2(uuid);
                objextTrack.setInfo3(serialnumber);
                flag = objextTrackService.save(objextTrack);
                datas.put("trackId", objextTrack.getId());
                return result.put("data", datas);
            } else {
                return R.error("目标库已存在");
            }
        } else {
            String trackImgUrl = videoObjextTaskService.saveImageToFdfs(trackImg);
            log.info("=================保存轨迹图截图：" + trackImgUrl);
            ObjextTrackPic objextTrackPic = new ObjextTrackPic();
            objextTrackPic.setSerialnumber(targetId);
            objextTrackPic.setImgurl(trackImgUrl);
            objextTrackPic.setCreatetime(new Date());
            objextTrackPicService.save(objextTrackPic);
            return R.ok();
        }
    }

    /**
     * 根据id 删除轨迹库数据
     *
     * @return
     */
    @PostMapping("deleteTractById")
    public R deleteTractById(String trackType, String id) {
        if (StringUtils.isEmptyString(trackType) || StringUtils.isEmptyString(id)) {
            return null;
        }
        //TODO 需事务控制
        objextTrackService.remove(new UpdateWrapper<ObjextTrack>().eq("id", id));
        objextTrackPicService.remove(new UpdateWrapper<ObjextTrackPic>().eq("id", id));
        return R.ok();
    }

    /**
     * 查询以图搜图历史搜索
     *
     * @return
     */
    @ApiOperation("查询以图搜图历史搜索")
    @PostMapping("queryImageQueryTaskMemPage")
    public R queryImageTaskListMem(PageRequest pageRequest) {
        R result = R.ok();
        com.baomidou.mybatisplus.extension.plugins.pagination.Page taskPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page(pageRequest.getPage(), pageRequest.getRows());
        IPage<TbImagesearchTask> page = tbImagesearchTaskService.page(taskPage, new QueryWrapper<TbImagesearchTask>().orderByDesc("create_time"));
        return result.put("page", new PageUtils(page));
    }

    /**
     * 删除以图搜图历史记录
     *
     * @param serialnumbers
     * @return
     */
    @ApiOperation("删除以图搜图历史记录（根据流水号删除）")
    @PostMapping("/deleteImageQueryTaskMem")
    public Map<String, Object> deleteImageQueryTaskMem(String serialnumbers) {
        R result = R.ok();
        if (StringUtils.isEmptyString(serialnumbers)) {
            return R.error("流水号不能为空");
        } else {
            String[] serialnumberArr = serialnumbers.split(",");
            for (int i = 0; i < serialnumberArr.length; i++) {
                tbImagesearchTaskService.removeById(serialnumberArr[i]);
            }
        }
        return result;
    }

    public R searchImageTractTarget(String targetId, String cameraId, String objtype, String startTime, String endTime) {
        TbImagesearchTask imgscTask = tbImagesearchTaskService.getById(targetId);
        if (imgscTask == null) {
            return R.error("任务不存在");
        }
        List<ObjextTrack> trackList = objextTrackService.queryObjextTrackBySerialnumber(targetId, 1);
        if (trackList.size() == 0) {
            return R.error("没有新增目标");
        }
        List<String> idsSet = new ArrayList<>();
        List<String> analisysIdSet = new ArrayList<>();
        List<String> userSetialnumnerSet = new ArrayList<>();
        for (ObjextTrack objextTrack : trackList) {
            if (objtype.equals(String.valueOf(objextTrack.getObjtype()))) {
                idsSet.add(objextTrack.getResultid());
                analisysIdSet.add(objextTrack.getInfo1());
                userSetialnumnerSet.add(objextTrack.getInfo3());
            }
        }
        if (idsSet.size() == 0) {
            return R.error("没有对应搜图类型新增目标");
        }
        String ids = String.join(",", idsSet);
        String analysisIds = String.join(",", analisysIdSet);
        String userSerialnumbers = String.join(",", userSetialnumnerSet);
        Map<String, Object> pamaMap = new HashedMap();
        pamaMap.put("uuid", ids);
        String returnJson = queryAnalysisResultService.doHttpService(pamaMap);
        System.out.println(returnJson);
        List<ResultQueryVo> resultBoList = queryAnalysisResultService.transAllResultToBo(returnJson);
        Set<String> urls = new HashSet<>();
        for (ResultQueryVo resultQueryVoe : resultBoList) {
            urls.add(resultQueryVoe.getImgurl());
        }

        objextTrackService.update(new UpdateWrapper<ObjextTrack>().set("search_status", 1)
                .eq("objtype", objtype)
                .eq("serialnumber", targetId).and(i -> i.isNull("search_status")
                        .or().eq("search_status", 0)));
        String pics = String.join(",", urls);
        R r = sumbitImageQueryTaskMem(targetId, imgscTask.getCaseId(), cameraId, pics, "", objtype, startTime,
                endTime, ids, analysisIds, userSerialnumbers);
        return r;
    }

    /**
     * 佛山置顶
     */
    private Map<String, Object> topTarget(JSONArray results, int startNum, int endNum, String serialnumber) {
        Float scoreArray[];
        String sbUidsArray[];
        List<TbImagesearchRecord> imagesearchRecordList =
                imagesearchRecordMapper.selectList(new QueryWrapper<TbImagesearchRecord>().eq("task_id",
                        serialnumber).orderByDesc("score"));
        int k = startNum / 27 * 27;
        int j = 0;
        int z = startNum;
        int recordListSize = imagesearchRecordList.size();

        if (results.size() <= 27) {
            scoreArray = new Float[results.size()];
            sbUidsArray = new String[results.size()];
        } else if (results.size() > 27 && ((startNum / 27) < (results.size() / 27))) {
            scoreArray = new Float[27];
            sbUidsArray = new String[27];
        } else {
            int num = results.size() - startNum;
            scoreArray = new Float[num];
            sbUidsArray = new String[num];
        }
        TbImagesearchRecord imagesearchRecord = null;
        for (int i = startNum; i < endNum; i++) {
            if (i < imagesearchRecordList.size()) {
                imagesearchRecord = imagesearchRecordList.get(k);
                sbUidsArray[j] = imagesearchRecord.getRecordId();
                scoreArray[j] = Float.valueOf(imagesearchRecord.getScore());
                k++;
                j++;
            } else {
                JSONObject searchResultVar = null;
                if (z >= 27 && recordListSize > z) {
                    z = recordListSize;
                }
                if (startNum >= 27) {
                    searchResultVar = results.getJSONObject(z - recordListSize);
                } else {
                    searchResultVar = results.getJSONObject(z);
                }
                z++;
                if (null == searchResultVar) {
                    continue;
                }
                String uuid = searchResultVar.getString("uuid");
                float score = searchResultVar.getFloat("score");
                if (isExits(uuid, imagesearchRecordList)) {
                    i--;
                    continue;
                }
                sbUidsArray[j] = uuid;
                scoreArray[j] = score;
                j++;
            }
        }
        return secondSord(sbUidsArray, scoreArray);
    }

    /**
     * 置顶剔重
     *
     * @param uuid
     * @param imagesearchRecordList
     * @return
     */
    private boolean isExits(String uuid, List<TbImagesearchRecord> imagesearchRecordList) {
        for (TbImagesearchRecord imagesearchRecord : imagesearchRecordList) {
            if (imagesearchRecord.getRecordId().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 佛山置顶后对搜图结果排序
     */
    private Map<String, Object> secondSord(String[] sbUidsArray, Float[] scoreArray) {
        float score = 0;
        String suid = "";
        Map<String, Object> topTargetMap = new HashMap<>();
        HashMap<String, Float> scoreMap = new HashMap<>();
        StringBuilder sbUids = new StringBuilder();
        for (int i = 0; i < scoreArray.length; i++) {
            for (int j = i + 1; j < scoreArray.length - 1; j++) {
                if (scoreArray[i] < scoreArray[j]) {
                    score = scoreArray[i];
                    scoreArray[i] = scoreArray[j];
                    scoreArray[j] = score;
                    suid = sbUidsArray[i];
                    sbUidsArray[i] = sbUidsArray[j];
                    sbUidsArray[j] = suid;
                }
            }
        }
        for (int i = 0; i < sbUidsArray.length; i++) {
            sbUids.append(sbUidsArray[i]).append(",");
            scoreMap.put(sbUidsArray[i], scoreArray[i]);
        }
        topTargetMap.put("sbUids", sbUids.toString());
        topTargetMap.put("scoreMap", scoreMap);
        return topTargetMap;
    }
}
