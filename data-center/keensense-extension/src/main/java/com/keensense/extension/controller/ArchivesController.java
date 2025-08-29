package com.keensense.extension.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.keensense.common.exception.VideoException;
import com.keensense.common.util.ReponseCode;
import com.keensense.common.util.ResponseStatus;
import com.keensense.common.util.ResponseStatusList;
import com.keensense.common.util.ResultUtils;
import com.keensense.extension.config.NacosConfig;
import com.keensense.extension.constants.ArchivesConstant;
import com.keensense.extension.constants.LibraryConstant;
import com.keensense.extension.entity.ArchivesInfo;
import com.keensense.extension.entity.dto.ArchivesDTO;
import com.keensense.extension.entity.dto.TrailConditionDTO;
import com.keensense.extension.entity.dto.TrailDTO;
import com.keensense.extension.service.IArchivesClustService;
import com.keensense.extension.service.IArchivesInfoService;
import com.keensense.extension.service.IArchivesTaskLibService;
import com.keensense.extension.service.ICameraRelationInfoService;
import com.keensense.extension.util.IDUtil;
import com.keensense.sdk.algorithm.IFaceSdkInvoke;
import com.keensense.sdk.constants.CommonConst;
import com.keensense.sdk.constants.FaceConstant;
import com.keensense.sdk.sys.utils.DbPropUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/5/18 10:40
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Api(value = "一人一档")
@RestController
@RequestMapping("/VIID")
@Slf4j
public class ArchivesController {

    @Autowired
    private IArchivesInfoService archivesService;

    @Autowired
    private IArchivesTaskLibService taskLibService;

    @Autowired
    private IArchivesClustService archivesClustService;

    @Autowired
    private NacosConfig nacosConfig;

    @Autowired
    private ICameraRelationInfoService cameraRelationInfoService;

    private String dateFormat = "yyyyMMddHHmmss";
    private static final String FEATURE_VECTOR = "featureVector";
    private static final String QUALITY = "quality";
    private static final String ARCHIVES_INPUT_JSON = "ArchivesObject";

    @ApiOperation(value = "手动开启人形聚类", notes = "手动开启人形聚类")
    @GetMapping(value = "/StartBodyClust", produces = "application/json;charset=UTF-8")
    public String startBodyClust() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                archivesClustService.startBodyClust();
            }
        });
        thread.start();
        return "{\"sendResult\":\"ok\"}";
    }

    @ApiOperation(value = "新增档案", notes = "新增档案")
    @PostMapping(value = "/Archives/Add1", produces = "application/json;charset=UTF-8")
    public ResponseStatusList addArchives1(HttpServletRequest request, @RequestBody String input) {
        JSONObject inputJsonObject = JSON.parseObject(input);
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setRequestURL(request.getRequestURI());
        responseStatus.setLocalTime(com.keensense.common.util.DateUtil.formatDate(new Date(), dateFormat));
        try {
            ArchivesDTO archivesDTO = ArchivesDTO.addArchivesIdInputPatter(inputJsonObject, ARCHIVES_INPUT_JSON);
            String archiveFaceLib = LibraryConstant.getFaceLibraryCache().getId();
            Map<String,Object> var = FaceConstant.getFaceSdkInvoke().getPicAnalyzeOne(archivesDTO.getFaceImgUrlFront());
            if (!var.isEmpty() && (Float)var.get("pose.pitch") >= -ArchivesConstant.frontPitch[1]
                    && (Float)var.get("pose.pitch") <= ArchivesConstant.frontPitch[1]
                    && (Float)var.get("pose.yaw") >= -ArchivesConstant.frontYaw[1]
                    && (Float)var.get("pose.yaw") <= ArchivesConstant.frontYaw[1]) {
                String feature = (String) var.get(FEATURE_VECTOR);
                String faceFeatureId = FaceConstant.getFaceSdkInvoke().addFaceToLib(archiveFaceLib, feature,
                        archivesDTO.getFaceImgUrlFront());
                ArchivesInfo archivesInfo = new ArchivesInfo(IDUtil.uuid(), archivesDTO.getFaceImgUrlFront(),
                        faceFeatureId, CommonConst.OBJ_TYPE_FACE, null, ArchivesConstant.ARCHIVES_FACE_FRONT);
                archivesService.save(archivesInfo);
                return getResponseStatusList(responseStatus, ReponseCode.CODE_0.getCode(), ReponseCode.CODE_0.getMsg(),
                        archivesInfo.getId());
            }
        } catch (VideoException e) {
            log.error("addFeature error", e);
        }
        return getResponseStatusList(responseStatus, ReponseCode.CODE_4.getCode(), ReponseCode.CODE_4.getMsg(),
                StringUtils.EMPTY);
    }

    @ApiOperation(value = "新增档案", notes = "新增档案")
    @PostMapping(value = "/Archives/Add2", produces = "application/json;charset=UTF-8")
    public ResponseStatusList addArchives2(HttpServletRequest request, @RequestBody String input) {
        ResponseStatus responseStatus = createResponseStatus(request);
        String archiveID = IDUtil.uuid();
        try {
            JSONObject inputJsonObject = JSON.parseObject(input);
            String faceImgUrl = inputJsonObject.getJSONObject("ArchivesObject").getString("FaceImgUrl");
            String archiveFaceLib = LibraryConstant.getFaceLibraryCache().getId();
            Map<String,Object> var = FaceConstant.getFaceSdkInvoke().getPicAnalyzeOne(faceImgUrl);

            if (var.isEmpty()) {
                return getResponseStatusList(responseStatus, ReponseCode.CODE_4.getCode(), ReponseCode.CODE_4.getMsg(),
                        StringUtils.EMPTY);
            }
            String feature = (String) var.get(FEATURE_VECTOR);

            String faceFeatureId = FaceConstant.getFaceSdkInvoke().addFaceToLib(archiveFaceLib, feature, faceImgUrl);

            float pitch = (float) var.get("pose.pitch");
            float yaw = (float) var.get("pose.yaw");

            ArchivesInfo archivesInfo = new ArchivesInfo();
            // 正面
            if (pitch >= -ArchivesConstant.frontPitch[1] && pitch <= ArchivesConstant.frontPitch[1]
                    && yaw >= -ArchivesConstant.frontYaw[1] && yaw <= ArchivesConstant.frontYaw[1]) {
                archivesInfo = new ArchivesInfo(archiveID, faceImgUrl, faceFeatureId, CommonConst.OBJ_TYPE_FACE, null,
                        ArchivesConstant.ARCHIVES_FACE_FRONT);
                archivesInfo.setPId(archiveID);
            }

            // 侧面
            if (pitch < ArchivesConstant.sidePitch[1] && pitch > -ArchivesConstant.sidePitch[1]) {
                if ((yaw < ArchivesConstant.sideYaw[1] && yaw > ArchivesConstant.sideYaw[0])
                        || (yaw > -ArchivesConstant.sideYaw[1] && yaw < -ArchivesConstant.sideYaw[0])) {
                    archivesInfo = new ArchivesInfo(archiveID, faceImgUrl, faceFeatureId, CommonConst.OBJ_TYPE_FACE,
                            null, ArchivesConstant.ARCHIVES_FACE_SIDE);
                }
            }

            // 抬头低头
            if (yaw < ArchivesConstant.bottomYaw[1] && yaw > -ArchivesConstant.bottomYaw[1]) {
                if ((pitch < ArchivesConstant.bottomPitch[1] && pitch > ArchivesConstant.bottomPitch[0])
                        || (pitch > -ArchivesConstant.bottomPitch[1] && pitch < -ArchivesConstant.bottomPitch[0])) {
                    archivesInfo = new ArchivesInfo(archiveID, faceImgUrl, faceFeatureId, CommonConst.OBJ_TYPE_FACE,
                            null, ArchivesConstant.ARCHIVES_FACE_BOTTOM);
                }
            }

            if (archivesInfo.getAngle() != null) {
                archivesService.save(archivesInfo);
            }

        } catch (VideoException e) {
            log.error("==== : addArchives Exception", e);
            return getResponseStatusList(responseStatus, ReponseCode.CODE_4.getCode(), ReponseCode.CODE_4.getMsg(),
                    StringUtils.EMPTY);
        }
        return getResponseStatusList(responseStatus, ReponseCode.CODE_0.getCode(), ReponseCode.CODE_0.getMsg(),
                archiveID);
    }

    @ApiOperation(value = "新增档案", notes = "新增档案")
    @PostMapping(value = "/Archives/Add", produces = "application/json;charset=UTF-8")
    public ResponseStatusList addArchives(HttpServletRequest request, @RequestBody String input) {

        ResponseStatus responseStatus = createResponseStatus(request);
        List<String> resultIds = Lists.newArrayList();
        try {
            float threshold = DbPropUtil.getFloat("face.archives.compare.threshold", 0.75f);

            List<ArchivesInfo> list = Lists.newArrayList();
            JSONObject parseObject = JSONObject.parseObject(input);
            JSONArray jsonArray = parseObject.getJSONObject("ArchivesListObject").getJSONArray("ArchivesObject");

            for (int i = 0; i < jsonArray.size(); i++) {
                String archiveID = IDUtil.uuid();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String frontFaceImg = jsonObject.getString("FrontFaceImg");

                if (StringUtils.isEmpty(frontFaceImg)) {
                    continue;
                }

                String archiveFaceLib = LibraryConstant.getFaceLibraryCache().getId();
                Map<String,Object> var = FaceConstant.getFaceSdkInvoke().getPicAnalyzeOne(frontFaceImg);
                float frontQuality = (float) var.get("quality");// 获取质量阈值

                if (frontQuality < threshold) {
                    log.info("==== frontFaceImg quality is low. front picUrl:" + frontFaceImg);
                    log.info(
                            "==== frontFaceImg quality is low. frontQuality:" + frontQuality + " threshold:" + threshold);
                    continue;// 正面人脸质量不满足数据不入库
                } else {
                    boolean flag = addFeatureAndCreateVo(var, archiveID, archiveID, frontFaceImg,
                            ArchivesConstant.ARCHIVES_FACE_FRONT, archiveFaceLib, list);
                    if (flag) {
                        String id = jsonObject.getString("Id");
                        resultIds.add(id);
                    }
                }

                // 侧面图
                String sideFaceImg = jsonObject.getString("SideFaceImg");
                if (!StringUtils.isEmpty(sideFaceImg)) {
                    addFeatureAndCreateVo(null, IDUtil.uuid(), archiveID, sideFaceImg,
                            ArchivesConstant.ARCHIVES_FACE_SIDE, archiveFaceLib, list);
                }

                // 抬头低头图
                String bottomFaceImg = jsonObject.getString("BottomFaceImg");
                if (!StringUtils.isEmpty(bottomFaceImg)) {
                    addFeatureAndCreateVo(null, IDUtil.uuid(), archiveID, bottomFaceImg,
                            ArchivesConstant.ARCHIVES_FACE_BOTTOM, archiveFaceLib, list);
                }

            }
            if (!CollectionUtils.isEmpty(list)) {
                archivesService.saveBatch(list);
            }
        } catch (Exception e) {
            log.error("==== : addArchives Exception", e);
            return getResponseStatusList(responseStatus, ReponseCode.CODE_4.getCode(), ReponseCode.CODE_4.getMsg(),
                    StringUtils.EMPTY);
        }
        return getResponseStatusList(responseStatus, ReponseCode.CODE_0.getCode(), ReponseCode.CODE_0.getMsg(),
                String.join(",", resultIds));
    }

    /**
     * @description:
     * @return: java.lang.String
     */
    @ApiOperation(value = "视图库一人一档流程数据入口", notes = "视图库一人一档流程数据入口&录入非千视通人脸抓拍数据")
    @PostMapping(value = "/Archives", produces = "application/json;charset=UTF-8")
    public String getArchivesInfo(@RequestBody String input) {
        JSONObject inputJsonObject = JSON.parseObject(input);
        ArchivesDTO archivesDTO = new ArchivesDTO();
        StringBuilder sbr = new StringBuilder();
        try {
            archivesDTO = ArchivesDTO.getArchivesInputPatter(inputJsonObject, ARCHIVES_INPUT_JSON);
            log.info("====archivesDTO:" + archivesDTO.toString());

            /** 调用一人一档 1代表老版本 2代表新版本 */
            int archivesType = nacosConfig.getArchivesType();
            if (archivesType == 1) {
                archivesService.saveObjext(archivesDTO);
            } else if (archivesType == 2) {
                archivesService.saveConvergenceObjext(archivesDTO);
            }

        } catch (Exception e) {
            log.error("-----getArchivesInfo error-----", e);
        } finally {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ARCHIVES_INPUT_JSON, archivesDTO);
            sbr.append(JSON.toJSONString(jsonObject));
        }
        return sbr.toString();
    }

    /**
     * 删除档案信息，删除相机时调用
     *
     * @param input
     * @return
     */
    @ApiOperation(value = "视图库一人一档 档案数据删除", notes = "视图库一人一档 档案数据删除&特征数据删除")
    @PostMapping(value = "/deleteArchives", produces = "application/json;charset=UTF-8")
    public String deleteArchives(@RequestBody String input) {
        return archivesService.deleteArchives(input);
    }
    /*
     */
    /**
     * @description:
     * @return: java.lang.String
     *//*
       
       @ApiOperation(value = "视图库一人一档流程数据入口&录入非千视通人脸抓拍数据", notes = "视图库一人一档流程数据入口&录入非千视通人脸抓拍数据")
       @PostMapping(value = "/Archives", produces = "application/json;charset=UTF-8")
       public String getArchivesInfo(@RequestBody String input) {
           JSONObject inputJsonObject = JSON.parseObject(input);
           ArchivesDTO archivesDTO = new ArchivesDTO();
           StringBuilder sbr = new StringBuilder();
           try {
       
               archivesDTO = ArchivesDTO.getArchivesInputPatter(inputJsonObject, ARCHIVES_INPUT_JSON);
               if (StringUtils.isNotBlank(archivesDTO.getFaceImgUrl())) {
                   Var json = FaceConstant.getFaceSdkInvoke()
                       .getPicAnalyzeOne(archivesDTO.getFaceImgUrl());
                   if (json != null && StringUtils.isNotBlank(json.getString(FEATURE_VECTOR))) {
                       String faceFeature = json.getString(FEATURE_VECTOR);
                       archivesDTO.setFaceFeature(faceFeature).setRoll(json.getFloat("pose.roll"))
                       .setYaw(json.getFloat("pose.yaw")).setPitch(json.getFloat("pose.pitch"));
       
                       if (!(FaceConstant.getFaceSdkInvoke() instanceof GlstFaceSdkInvokeImpl)) {
       
                           archivesDTO.setFaceQuality(json.getFloat(QUALITY));
                       }
                       archivesDTO.setPitch(json.getFloat("pose.pitch"));
                       archivesDTO.setRoll(json.getFloat("pose.roll"));
                       */
    /** 人脸抓拍库录入 *//*
                  
                          if (!(FaceConstant.getFaceSdkInvoke() instanceof QstFaceSdkInvokeImpl) &&
                              archivesDTO.getObjType() == 3) {
                              JSONObject taskLibJsonObject = JSON.parseObject(input);
                              taskLibJsonObject.put("Feature", faceFeature);
                              taskLibJsonObject.put("Time", archivesDTO.getTime());
                              taskLibJsonObject.put("TaskID", archivesDTO.getTaskID());
                              String faceFeatureId = taskLibService.enrollFeature(taskLibJsonObject);
                              archivesDTO.setFaceFeatureId(faceFeatureId);
                          }
                      }
                  }
                  */
    /** 调用一人一档 *//*
                 
                  if (nacosConfig.getDocuemntServiceExist()) {
                      if(FaceConstant.getFaceSdkInvoke() instanceof KsFaceSdkInvokeImpl ||
                          FaceConstant.getFaceSdkInvoke() instanceof GlstFaceSdkInvokeImpl ||
                          FaceConstant.getFaceSdkInvoke() instanceof StFaceSdkInvokeImpl){
                 
                          archivesService.saveObjext(archivesDTO);
                      }else{
                          archivesService.saveConvergenceObjext(archivesDTO);
                      }
                  }
                 } catch (Exception e) {
                  log.error("-----getArchivesInfo error-----", e);
                 } finally {
                  JSONObject jsonObject = new JSONObject();
                  jsonObject.put(ARCHIVES_INPUT_JSON, archivesDTO);
                  sbr.append(JSON.toJSONString(jsonObject));
                 }
                 return sbr.toString();
                 }
                 */

    /**
     * @param input input
     * @description:
     * @return: java.lang.String
     */
    @ApiOperation(value = "获取一人一档轨迹数据", notes = "获取一人一档轨迹数据")
    @PostMapping(value = "/Trail", produces = "application/json;charset=UTF-8")
    public String getTrailInfo(@RequestBody String input) {

        JSONObject inputJsonObject = JSON.parseObject(input);
        String inputJsonName = "TrailCondition";
        String rslt;
        List<TrailDTO> trailDTOList = new ArrayList<>();
        try {
            TrailConditionDTO trailConditionDTO = TrailConditionDTO.getTrailInputPatter(inputJsonObject, inputJsonName);
            if (trailConditionDTO == null) {
                throw new VideoException(-1, "trailConditionDTO is null");
            }
            trailDTOList = archivesService.getTrailInfos(trailConditionDTO);

        } catch (VideoException e) {
            log.error("getTrailInfo error", e);
        } finally {
            rslt = getJsonObjectByTrail(trailDTOList);
        }
        return rslt;
    }

    /**
     * @param input input
     * @description:
     * @return: java.lang.String
     */
    @ApiOperation(value = "根据base64搜图一人一档档案ID数据", notes = "可用于数据研判中获取相关Aid搜图")
    @PostMapping(value = "/Archives/Search", produces = "application/json;charset=UTF-8")
    public ResponseStatusList getArchivesIds(HttpServletRequest request, @RequestBody String input) {

        JSONObject inputJsonObject = JSON.parseObject(input);
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setRequestURL(request.getRequestURI());
        responseStatus.setLocalTime(com.keensense.common.util.DateUtil.formatDate(new Date(), dateFormat));
        try {
            TrailConditionDTO trailConditionDTO =
                    TrailConditionDTO.getArchivesIdInputPatter(inputJsonObject, ARCHIVES_INPUT_JSON);
            if (trailConditionDTO == null) {
                throw new VideoException(-1, "ArchivesObject is null");
            }
            String archivesIds = archivesService.getArchivesIdInfos(trailConditionDTO);
            return getResponseStatusList(responseStatus, ReponseCode.CODE_0.getCode(), ReponseCode.CODE_0.getMsg(),
                    archivesIds);
        } catch (VideoException e) {
            log.error("getArchivesIds error", e);
        }
        return getResponseStatusList(responseStatus, ReponseCode.CODE_4.getCode(), ReponseCode.CODE_4.getMsg(),
                StringUtils.EMPTY);

    }

    @PostMapping(value = "/Feature/Enroll", produces = "application/json;charset=UTF-8")
    public ResponseStatusList enrollFeature(HttpServletRequest request, @RequestBody JSONObject input) {
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setRequestURL(request.getRequestURI());
        responseStatus.setLocalTime(com.keensense.common.util.DateUtil.formatDate(new Date(), dateFormat));

        JSONObject featureObject =
                Optional.ofNullable(input).map(jo -> jo.getJSONObject("FeatureObject")).orElse(new JSONObject());

        String id = taskLibService.enrollFeature(featureObject);
        if (StringUtils.isNotEmpty(id)) {
            return getResponseStatusList(responseStatus, ReponseCode.CODE_0.getCode(), ReponseCode.CODE_0.getMsg(), id);
        }

        return getResponseStatusList(responseStatus, ReponseCode.CODE_4.getCode(), ReponseCode.CODE_4.getMsg(),
                StringUtils.EMPTY);
    }

    @PostMapping(value = "/Feature/Search")
    public JSONObject searchFeature(@RequestBody String input) {
        JSONObject jsonObject = JSON.parseObject(input);
        JSONObject searchParam =
                Optional.ofNullable(jsonObject).map(jo -> jo.getJSONObject("Search")).orElse(new JSONObject());

        JSONArray ja = taskLibService.searchFeature(searchParam);
        JSONObject resultJo = new JSONObject();
        resultJo.put("results", ja);
        return resultJo;
    }

    @PostMapping(value = "/Picture/Face/Struct", produces = "application/json;charset=UTF-8")
    public JSONObject pictureStruct(@RequestBody String input) {
        JSONObject jsonObject = JSON.parseObject(input);
        JSONObject result = new JSONObject();
        result.put("ret", 0);
        result.put("desc", "Success");

        Map<String,Object> var = Optional.ofNullable(jsonObject).map(jo -> jo.getString("Picture"))
                .map(img -> FaceConstant.getFaceSdkInvoke().getPicAnalyze(img)).orElse(null);
        if (var != null) {
            result.put("objexts", var.get("objexts"));
        } else {
            result.put("ret", -1);
            result.put("desc", "特征检测失败");
        }
        return result;
    }

    @PostMapping(value = "/Picture/Face/Compare", produces = "application/json;charset=UTF-8")
    public JSONObject compareFeature(@RequestBody String input) {
        JSONObject jsonObject = JSON.parseObject(input);
        JSONObject result = new JSONObject();
        result.put("ret", 0);
        result.put("desc", "Success");

        IFaceSdkInvoke faceSdk = FaceConstant.getFaceSdkInvoke();
        if (StringUtils.isNotEmpty(jsonObject.getString("queryFeature"))
                && StringUtils.isNotEmpty(jsonObject.getString("targetFeature"))) {
            float score =
                    faceSdk.compareFeature(jsonObject.getString("queryFeature"), jsonObject.getString("targetFeature"));
            result.put("score", score);
            return result;
        }
        String queryPicture = jsonObject.getString("queryPicture");
        String targetPicture = jsonObject.getString("targetPicture");
        if (StringUtils.isNotEmpty(queryPicture) && StringUtils.isNotEmpty(targetPicture)) {
            Optional<String> query =
                    Optional.ofNullable(faceSdk.getPicAnalyzeOne(queryPicture)).map(var -> var.get(FEATURE_VECTOR).toString());
            Optional<String> target =
                    Optional.ofNullable(faceSdk.getPicAnalyzeOne(targetPicture)).map(var -> var.get(FEATURE_VECTOR).toString());

            if (query.isPresent() && target.isPresent()) {
                float score = faceSdk.compareFeature(query.get(), target.get());
                result.put("score", score);
                return result;
            }
        }
        result.put("ret", -1);
        result.put("desc", "Fail");
        return result;
    }

    @PostMapping(value = "/CameraRelation/Insert", produces = "application/json;charset=UTF-8")
    public String insertCameraRelation(@RequestBody String jsonStr) {
        return cameraRelationInfoService.insertCameraRelation(jsonStr);
    }

    @PostMapping(value = "/CameraRelation/Delete", produces = "application/json;charset=UTF-8")
    public String deleteCameraRelation(@RequestBody String jsonStr) {
        return cameraRelationInfoService.deleteCameraRelation(jsonStr);
    }

    @PostMapping(value = "/CameraRelation/Query", produces = "application/json;charset=UTF-8")
    public String queryCameraRelation(@RequestBody String jsonStr) {
        return cameraRelationInfoService.queryCameraRelation(jsonStr);
    }

    @PostMapping(value = "/Archives/UnbindTrail", produces = "application/json;charset=UTF-8")
    public ResponseStatusList unbindTrail(HttpServletRequest request, @RequestBody String personIds) {
        archivesService.unbindTrail(personIds);
        return getResponseStatusList(createResponseStatus(request), ReponseCode.CODE_0.getCode(),
                ReponseCode.CODE_0.getMsg(), StringUtils.EMPTY);
    }

    @PostMapping(value = "/Archives/QueryArchives", produces = "application/json;charset=UTF-8")
    public String queryArchives(@RequestBody String jsonStr) {
        return archivesService.queryArchives(jsonStr);
    }

    @PostMapping(value = "/Archives/MergeArchives", produces = "application/json;charset=UTF-8")
    public ResponseStatusList mergeArchives(HttpServletRequest request, @RequestBody String jsonStr) {
        return archivesService.mergeArchives(request, jsonStr);
    }

    private boolean addFeatureAndCreateVo(Map<String,Object> var, String aid, String pId, String frontFaceImg, Integer objectType,
                                          String archiveFaceLib, List<ArchivesInfo> list) {

        float threshold = DbPropUtil.getFloat("face.archives.compare.threshold", 0.75f);

        if (var == null) {
            var = FaceConstant.getFaceSdkInvoke().getPicAnalyzeOne(frontFaceImg);
        }
        float quality = (float) var.get("quality");// 获取质量

        log.info("==== addFeatureAndCreateVo picUrl:" + frontFaceImg);
        log.info("==== addFeatureAndCreateVo : quality" + quality + " threshold: " + threshold);

        if (quality > threshold) {
            String feature = (String) var.get(FEATURE_VECTOR);
            String faceFeatureId = FaceConstant.getFaceSdkInvoke().addFaceToLib(archiveFaceLib, feature, frontFaceImg);
            ArchivesInfo archivesInfo = new ArchivesInfo(aid, frontFaceImg, faceFeatureId, 0, null, objectType, pId);
            list.add(archivesInfo);
            return true;
        }
        return false;
    }

    private ResponseStatusList getResponseStatusList(ResponseStatus responseStatus, String code, String message,
                                                     String data) {
        responseStatus.setStatusCode(code);
        responseStatus.setStatusString(message);
        responseStatus.setId(data);
        ResponseStatusList responseStatusList = new ResponseStatusList();
        try {
            responseStatusList = ResultUtils.returnStatus(responseStatus);
        } catch (Exception e) {
            log.error("getResponseStatusList error", e);
        }
        return responseStatusList;
    }

    private String getJsonObjectByTrail(List<TrailDTO> trailDTOList) {
        JSONObject subJsonObject = new JSONObject();
        subJsonObject.put("Count", trailDTOList.size());
        subJsonObject.put("TrailObject", trailDTOList);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TrailListObject", subJsonObject);
        return JSON.toJSONString(jsonObject);
    }

    private ResponseStatus createResponseStatus(HttpServletRequest request) {
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setRequestURL(request.getRequestURI());
        responseStatus.setLocalTime(com.keensense.common.util.DateUtil.formatDate(new Date(), dateFormat));
        return responseStatus;
    }
}
