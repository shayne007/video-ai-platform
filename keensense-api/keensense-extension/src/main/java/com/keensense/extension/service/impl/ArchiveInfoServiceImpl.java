package com.keensense.extension.service.impl;

import cn.jiuling.plugin.extend.featureclust.FaceFeatureClusterSpringRedis;
import cn.jiuling.plugin.extend.featureclust.ReidFeatureClusterSpringRedis;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.keensense.common.exception.VideoException;
import com.keensense.common.util.ReponseCode;
import com.keensense.common.util.ResponseStatus;
import com.keensense.common.util.ResponseStatusList;
import com.keensense.common.util.ResultUtils;
import com.keensense.common.util.ResponseStatusList;
import com.keensense.extension.constants.ArchivesConstant;
import com.keensense.extension.constants.LibraryConstant;
import com.keensense.extension.entity.ArchivesBodyInfo;
import com.keensense.extension.entity.ArchivesInfo;
import com.keensense.extension.entity.ArchivesRelationInfo;
import com.keensense.extension.entity.dto.ArchivesDTO;
import com.keensense.extension.entity.dto.TrailConditionDTO;
import com.keensense.extension.entity.dto.TrailDTO;
import com.keensense.extension.feign.IMicroSearchFeign;
import com.keensense.extension.mapper.ArchivesInfoMapper;
import com.keensense.extension.service.IArchivesBodyInfoService;
import com.keensense.extension.service.IArchivesClustService;
import com.keensense.extension.service.IArchivesInfoService;
import com.keensense.extension.service.IArchivesRelationInfoService;
import com.keensense.extension.util.IDUtil;
import com.keensense.extension.util.JsonPatterUtil;
import com.keensense.extension.util.ResponseUtil;
import com.keensense.sdk.algorithm.impl.GLQstFaceSdkInvokeImpl;
import com.keensense.sdk.algorithm.impl.GlstFaceSdkInvokeImpl;
import com.keensense.sdk.algorithm.impl.KsFaceSdkInvokeImpl;
import com.keensense.sdk.algorithm.impl.QstFaceSdkInvokeImpl;
import com.keensense.sdk.algorithm.impl.StFaceSdkInvokeImpl;
import com.keensense.sdk.constants.BodyConstant;
import com.keensense.sdk.constants.FaceConstant;
import com.keensense.sdk.constants.SdkExceptionConst;
import com.keensense.sdk.sys.utils.DbPropUtil;
import com.keensense.sdk.util.ValidUtil;
import com.loocme.security.encrypt.Base64;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.util.PatternUtil;
import com.loocme.sys.util.StringUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/***
 * @description:
 * @author jingege
 * @return:
 */
@Service
@Slf4j
public class ArchiveInfoServiceImpl extends ServiceImpl<ArchivesInfoMapper, ArchivesInfo> implements
        IArchivesInfoService {

    public static Map<String, String> mapFaceIdAId = Maps.newHashMap();
    public static Map<String, ArchivesDTO> mapPersonIdArchivesDTO = Maps.newHashMap();

    @Autowired
    private IArchivesBodyInfoService archivesBodyInfoService;
    @Autowired
    private IArchivesRelationInfoService archivesRelationInfoService;
    @Autowired
    private IArchivesClustService archiveClustService;
    @Autowired
    private IMicroSearchFeign microSearchFeign;
    @Autowired
    private ArchivesInfoMapper archivesInfoMapper;
    @Resource(name = "bodyRedisTemplate")
    private RedisTemplate<String, String> bodyRedisTemplate;
    @Resource(name = "faceRedisTemplate")
    private RedisTemplate<String, String> faceRedisTemplate;

    private String archiveFaceLib = StringUtils.EMPTY;
    private String archiveFrontBodyLib = StringUtils.EMPTY;
    private String archiveSideBodyLib = StringUtils.EMPTY;
    private String archiveBackBodyLib = StringUtils.EMPTY;

    private static final String FEATURE_VECTOR = "featureVector";
    private static final String FACE_FEATURE_ID = "face_feature_id";
    private static final String BODY_ARCHIVESDTO_QUALITY_THRESHOID = "body.archives.quality.threshold";
    private static final SimpleDateFormat DATE_SDF_TIME = new SimpleDateFormat("yyyyMMdd HHmmss");

    private void initLib() {
        archiveFaceLib = LibraryConstant.getFaceLibraryCache().getId();
        archiveFrontBodyLib = LibraryConstant.getBodyLibraryByAngle(LibraryConstant.BOYD_ANGLE_FRONT, new Date()).getId();
        archiveSideBodyLib = LibraryConstant.getBodyLibraryByAngle(LibraryConstant.BOYD_ANGLE_SIDE, new Date()).getId();
        archiveBackBodyLib = LibraryConstant.getBodyLibraryByAngle(LibraryConstant.BOYD_ANGLE_BACK, new Date()).getId();
    }

    /**
     * 保存轨迹
     *
     * @param archivesDTO archivesDTO
     */
    @Override
    public void saveObjext(ArchivesDTO archivesDTO) throws VideoException {
        initLib();
        String faceFeature = archivesDTO.getFaceFeature();
        String bodyUrl = archivesDTO.getBodyImgUrl();
        Float faceQuality = archivesDTO.getFaceQuality();
        Float bodyQuality = archivesDTO.getBodyQuality();
        saveTrailToResult(archivesDTO, Strings.EMPTY, ArchivesConstant.TRACE_SOURCE_EXCEPTION,
                faceQuality, ArchivesConstant.FACE_SCORE_DEFAULT, ArchivesConstant.BODY_SCORE_DEFAULT);
        if (StringUtils.isNotBlank(faceFeature)) {
            havingFaceUrl(archivesDTO);
            /*人脸不可用，判断人形质量,人形质量达标，用人形比对，关联轨迹*/
        } else if (StringUtils.isNotBlank(bodyUrl) && bodyQuality != null && bodyQuality >= DbPropUtil
                .getFloat(BODY_ARCHIVESDTO_QUALITY_THRESHOID, 0.7f)) {
            log.info("face no body ok");
            useBodyToTrail(archivesDTO);
        }
        archivesDTO.setBodyQuality(ArchivesConstant.formatFloat(archivesDTO.getBodyQuality()));

        updateEsData(archivesDTO);
    }

    /**
     * 更新es数据，添加档案信息
     *
     * @param archivesDTO
     */
    private void updateEsData(ArchivesDTO archivesDTO) {
        String faceID = archivesDTO.getFaceID();
        String archivesID = archivesDTO.getArchivesID();
        String bodyID = archivesDTO.getBodyID();
        Integer trailSource = archivesDTO.getTrailSource();

        if (StringUtils.isEmpty(archivesID)) {
            return;
        }

        if (StringUtils.isNotEmpty(faceID)) {
            JSONObject jsonObject = new JSONObject();
            JSONObject faceListObject = new JSONObject();
            JSONArray faceObjects = new JSONArray();
            JSONObject faceObject = new JSONObject();
            JSONObject dataObject = new JSONObject();
            dataObject.put("ArchivesId", archivesID);
            dataObject.put("TrailSource", 1);

            faceObject.put("Data", dataObject);
            faceObject.put("FaceID", faceID);

            faceObjects.add(faceObject);
            faceListObject.put("FaceObject", faceObjects);
            jsonObject.put("FaceListObject", faceListObject);

            microSearchFeign.updateFaces(jsonObject);
        }

        if (StringUtils.isNotEmpty(bodyID)) {
            JSONObject jsonObject = new JSONObject();
            JSONObject personListObject = new JSONObject();
            JSONArray personObjects = new JSONArray();
            JSONObject personObject = new JSONObject();
            JSONObject dataObject = new JSONObject();
            dataObject.put("ArchivesId", archivesID);
            dataObject.put("TrailSource", 1);

            personObject.put("Data", dataObject);
            personObject.put("PersonID", bodyID);

            personObjects.add(personObject);
            personListObject.put("PersonObject", personObjects);
            jsonObject.put("PersonListObject", personListObject);

            microSearchFeign.updateBodys(jsonObject);

        }
    }

    private void havingFaceUrl(ArchivesDTO archivesDTO) {
        String bodyUrl = archivesDTO.getBodyImgUrl();
        Float faceQuality = archivesDTO.getFaceQuality();
        Float bodyQuality = archivesDTO.getBodyQuality();
        /*有人脸时*/
        if (faceQuality >= DbPropUtil.getFloat("face.archives.quality.threshold", 0.7f)) {
            log.info("face ok");
            /*人脸质量达标*/
            Var similars = FaceConstant.getFaceSdkInvoke().getSimilars(archiveFaceLib, archivesDTO.getFaceFeature(),
                    DbPropUtil.getFloat("face.archives.compare.threshold", 0.75f) * 100, 1);
            Float score = similars.isNull() ? ArchivesConstant.FACE_SCORE_DEFAULT : similars.getFloat("[0].score");
            archivesDTO.setFaceScore(score);
            /*判断是否有与档案比对上的，若有，则根据人脸比对成功，添加至轨迹*/
            String faceId = similars.isNull() ? "" : similars.getString("[0].face.id");
            if (StringUtil.isNotNull(faceId)) {
                addTrailByArchives(faceId, faceQuality, score, bodyQuality, archivesDTO);
            } else {
                /*人脸没有匹配上*/
                try {
                    addArchives(archivesDTO.getPitch(), archivesDTO.getRoll(), faceQuality, bodyQuality, archivesDTO.getFaceFeature(), archivesDTO);
                } catch (Exception e) {
                    System.out.println("=====================================");
                    e.printStackTrace();
                }
            }
        } else if (StringUtils.isNotBlank(bodyUrl) && bodyQuality != null && bodyQuality >= DbPropUtil
                .getFloat(BODY_ARCHIVESDTO_QUALITY_THRESHOID, 0.7f)) {
            log.info("face no body ok");
            useBodyToTrail(archivesDTO);
        }
        log.info("face no body no");
    }

    private void addTrailByArchives(String faceId, Float faceQuality, Float score, Float bodyQuality, ArchivesDTO archivesDTO) {
        ArchivesInfo archivesInfo = baseMapper.selectOne(new QueryWrapper<ArchivesInfo>()
                .isNotNull(FACE_FEATURE_ID).eq(FACE_FEATURE_ID, faceId));
        /*添加分数、轨迹等信息*/
        if (archivesInfo != null) {
            log.info("face ok exits");
            String archivesId = archivesInfo.getId();
            saveTrailToResult(archivesDTO, archivesId, ArchivesConstant.TRACE_SOURCE_FACE,
                    faceQuality, score, ArchivesConstant.BODY_SCORE_DEFAULT);
            /*判断这个角度的人形是否绑定，未绑定则新增*/
            ArchivesBodyInfo archiveBodyInfo = archivesBodyInfoService.getOne(
                    new QueryWrapper<ArchivesBodyInfo>().eq("archives_id", archivesId)
                            .eq("angle", archivesDTO.getAngle()));
            if (archiveBodyInfo == null && bodyQuality != null && bodyQuality >= DbPropUtil.getFloat(BODY_ARCHIVESDTO_QUALITY_THRESHOID, 0.7f)) {
                log.info("face ok exits body add");
                addBodyInfoToArchive(archivesDTO.getAngle(), archivesDTO.getBodyImgUrl(), archivesInfo.getId());
                archivesDTO.setBodyScore(ArchivesConstant.BODY_SCORE_COMPARE_DEFAULT);
            }
            log.info("face ok exits body noadd");
        } else {
            addArchives(archivesDTO.getPitch(), archivesDTO.getRoll(), faceQuality, bodyQuality, archivesDTO.getFaceFeature(), archivesDTO);
        }
    }

    private void addArchives(float pitch, float roll, Float faceQuality, Float bodyQuality,
                             String featureVector, ArchivesDTO archivesDTO) {
        /*人脸没有匹配上*/
        boolean ksAlgo = FaceConstant.getFaceSdkInvoke() instanceof KsFaceSdkInvokeImpl
                && -0.26 < pitch && pitch < 0.26 && -0.26 < roll && roll < 0.26;
        boolean qstAlgo = FaceConstant.getFaceSdkInvoke() instanceof QstFaceSdkInvokeImpl && faceQuality >= 0.8;
        boolean glstAlgo = FaceConstant.getFaceSdkInvoke() instanceof GlstFaceSdkInvokeImpl && faceQuality >= 0.8;
        boolean glstQstAlgo = FaceConstant.getFaceSdkInvoke() instanceof GLQstFaceSdkInvokeImpl && faceQuality >= 0.8;
        boolean stAlgo = FaceConstant.getFaceSdkInvoke() instanceof StFaceSdkInvokeImpl && faceQuality >= 0.8;
        if (ksAlgo || qstAlgo || glstAlgo || glstQstAlgo || stAlgo) {
            /*人脸角度正，可用于建档案，并新增人形*/
            log.info("face ok body add");
            String archivesId = IDUtil.uuid();
            saveTrailToResult(archivesDTO, archivesId, ArchivesConstant.TRACE_SOURCE_FACE,
                    faceQuality, ArchivesConstant.FACE_SCORE_COMPARE_DEFAULT, ArchivesConstant.BODY_SCORE_DEFAULT);
            String faceFeatureId = FaceConstant.getFaceSdkInvoke().addFaceToLib(archiveFaceLib,
                    featureVector, archivesDTO.getFaceImgUrl());
            ArchivesInfo archivesInfo = new ArchivesInfo(
                    archivesId, archivesDTO.getFaceImgUrl(), faceFeatureId, archivesDTO.getObjType());
            archivesInfo.setPId(archivesInfo.getId());
            baseMapper.insert(archivesInfo);
            if (bodyQuality != null && bodyQuality >= DbPropUtil.getFloat(BODY_ARCHIVESDTO_QUALITY_THRESHOID, 0.7f)) {
                addBodyInfoToArchive(archivesDTO.getAngle(), archivesDTO.getBodyImgUrl(), archivesId);
                archivesDTO.setBodyScore(ArchivesConstant.BODY_SCORE_COMPARE_DEFAULT);
            }

            /** 临时修改20190910*/
            mapFaceIdAId.put(archivesDTO.getFaceID(), archivesId);
            ArchivesDTO archivesDTO1 = mapPersonIdArchivesDTO.get(archivesDTO.getBodyID());
            if (archivesDTO1 != null) {
                String bodyFeatureId = IDUtil.uuid();
                String bodyFeature = archivesDTO1.getBodyFeature();
                Integer angle = archivesDTO1.getAngle();
                String bodyUrl = archivesDTO1.getBodyImgUrl();
                BodyConstant.getBodySdkInvoke().addBodyToLib(findBodyLibByAngle(angle),
                        bodyFeatureId, BodyConstant.BODY_TYPE, bodyFeature);
                ArchivesBodyInfo bodyInfo = new ArchivesBodyInfo(IDUtil.uuid(), bodyUrl, String.valueOf(angle),
                        bodyFeatureId, archivesId);
                archivesBodyInfoService.saveOrUpdate(bodyInfo);
            }

            log.info("pitch & roll" + pitch + "//" + roll + "//" + archivesDTO.getFaceID() + "//");
        } else if (StringUtils.isNotBlank(archivesDTO.getBodyImgUrl()) && bodyQuality != null && bodyQuality >= DbPropUtil
                .getFloat(BODY_ARCHIVESDTO_QUALITY_THRESHOID, 0.7f)) {
            log.info("face half ok body ok");
            useBodyToTrail(archivesDTO);
        }
    }

    /**
     * 给人形底库录入档案人形，并加入档案人形表
     *
     * @param angle      angle
     * @param bodyUrl    bodyUrl
     * @param archivesId archivesId
     */
    private void addBodyInfoToArchive(Integer angle, String bodyUrl, String archivesId) {

        if (angle != null && StringUtils.isNotBlank(bodyUrl)) {
            Var bodyFeatureInfo = BodyConstant.getBodySdkInvoke().getPicAnalyzeOne(BodyConstant.BODY_TYPE, bodyUrl);
            if (bodyFeatureInfo != null) {

                String bodyFeatureId = IDUtil.uuid();
                String repoId = findBodyLibByAngle(angle);
                if (StringUtils.isBlank(repoId)) {
                    log.info("angle get repoid is null");
                    throw new VideoException(-1, "angle=" + angle + " get repoid is null");
                }
                BodyConstant.getBodySdkInvoke().addBodyToLib(findBodyLibByAngle(angle), bodyFeatureId, BodyConstant.BODY_TYPE,
                        bodyFeatureInfo.getString(FEATURE_VECTOR));
                ArchivesBodyInfo bodyInfo = new ArchivesBodyInfo(IDUtil.uuid(), bodyUrl, String.valueOf(angle),
                        bodyFeatureId, archivesId);
                archivesBodyInfoService.saveOrUpdate(bodyInfo);
            }

        }
    }

    /**
     * 根据人脸Id从es中获取档案Id
     *
     * @param faceId
     * @return
     */
    public String getArchivesByFaceId(String faceId) {

        String archivesId = StringUtils.EMPTY;

        Map<String, Object> map = new HashMap<>();
        map.put("Faces.FaceID=", faceId);
        map.put("Faces.ArchivesID", "NotNull");
        String faces = microSearchFeign.getFaces(map);

        JSONObject jsonObject = JSONObject.parseObject(faces);
        JSONObject faceListObject = jsonObject.getJSONObject("FaceListObject");
        JSONArray faceObject = faceListObject.getJSONArray("FaceObject");

        if (null != faceObject && faceObject.size() > 0) {
            archivesId = faceObject.getJSONObject(0).getString("ArchivesId");
        }

        return archivesId;
    }

    /**
     * 去除特征头
     *
     * @param archivesDTO
     */
    private void deleteFeatureHead(ArchivesDTO archivesDTO, Var bodyInfoVar) {
        String feature = bodyInfoVar.getString(FEATURE_VECTOR);
        archivesDTO.setBodyFeature(feature);
    }

    /**
     * 人形质量达标，用人形比对，关联轨迹
     *
     * @param archivesDTO archivesDTO
     */
    private void useBodyToTrail(ArchivesDTO archivesDTO) {
        log.info("useBodyToTrail");
        Var bodyInfoVar = BodyConstant.getBodySdkInvoke().getPicAnalyzeOne(BodyConstant.BODY_TYPE,
                archivesDTO.getBodyImgUrl());
        if (null != bodyInfoVar) {

            /**临时修改20190910*/
            String faceID = archivesDTO.getFaceID();
            if (StringUtils.isNotEmpty(faceID)) {
                deleteFeatureHead(archivesDTO, bodyInfoVar);
                mapPersonIdArchivesDTO.put(archivesDTO.getBodyID(), archivesDTO);
                String aid = mapFaceIdAId.get(faceID);
                if (StringUtils.isNotEmpty(aid)) {
                    String bodyFeatureId = IDUtil.uuid();
                    String bodyFeature = archivesDTO.getBodyFeature();
                    Integer angle = archivesDTO.getAngle();
                    String bodyUrl = archivesDTO.getBodyImgUrl();
                    BodyConstant.getBodySdkInvoke().addBodyToLib(findBodyLibByAngle(angle),
                            bodyFeatureId, BodyConstant.BODY_TYPE, bodyFeature);
                    ArchivesBodyInfo bodyInfo = new ArchivesBodyInfo(IDUtil.uuid(), bodyUrl, String.valueOf(angle),
                            bodyFeatureId, aid);
                    archivesBodyInfoService.saveOrUpdate(bodyInfo);
                }

            }

//			String bodyFeatureId = IDUtil.uuid();
//			String bodyFeature = archivesDTO.getBodyFeature();
//			Integer angle = archivesDTO.getAngle();
//			String faceID = archivesDTO.getFaceID();
//			String bodyUrl = archivesDTO.getBodyImgUrl();
//			ArchivesRelationInfo archivesRelationInfo = archivesRelationInfoService
//				.getOne(new QueryWrapper<ArchivesRelationInfo>().eq("id", faceID));
//			if(StringUtils.isNotEmpty(bodyFeature) && StringUtils.isNotEmpty(faceID) &&
//				null != angle && null!=archivesRelationInfo){
//				BodyConstant.getBodySdkInvoke().addBodyToLib(findBodyLibByAngle(angle), bodyFeatureId, BodyConstant.BODY_TYPE,
//					bodyFeature);
//				ArchivesBodyInfo bodyInfo = new ArchivesBodyInfo(IDUtil.uuid(), bodyUrl, String.valueOf(angle),
//					bodyFeatureId, archivesRelationInfo.getArchivesId());
//				archivesBodyInfoService.saveOrUpdate(bodyInfo);
//			}


            // 人形比对质量达标
            Var similars = BodyConstant.getBodySdkInvoke().getSimilars(BodyConstant.BODY_TYPE,
                    findBodyLibByAngle(archivesDTO.getAngle()),
                    bodyInfoVar.getString(FEATURE_VECTOR),
                    DbPropUtil.getFloat("body.archives.compare.threshold", 0.8f)
                            * 100, 1, false);

            Float score = similars.isNull() ? ArchivesConstant.BODY_SCORE_DEFAULT
                    : similars.getFloat("[0].score");
            archivesDTO.setBodyScore(score);

            String bodyId = similars.isNull() ? "" : similars.getString("[0].uuid");
            if (StringUtil.isNotNull(bodyId) &&
                    score - DbPropUtil.getFloat("body.archives.compare.threshold", 0.8f) >= 0) {
                ArchivesBodyInfo archiveBodyInfo = archivesBodyInfoService.getOne(
                        new QueryWrapper<ArchivesBodyInfo>().eq("body_feature_id", bodyId));
                String archiveId = archiveBodyInfo.getArchivesId();
                if (StringUtil.isNotNull(archiveId)) {
                    saveTrailToResult(archivesDTO, archiveId, ArchivesConstant.TRACE_SOURCE_BODY,
                            archivesDTO.getFaceQuality(), ArchivesConstant.FACE_SCORE_DEFAULT, score);
                } else {
                    // 需要添加默认值
                    log.info("gb1400data donnot find archive by bodyid=" + bodyId);
                }
            }
        }
    }

    /**
     * 根据角度选取底库 128-正面 256-侧面 512-背面
     *
     * @param angle angle
     */
    @Override
    public String findBodyLibByAngle(Integer angle) {

        String rstLib = Strings.EMPTY;
        switch (angle) {
            case LibraryConstant.BOYD_ANGLE_FRONT:
                rstLib = archiveFrontBodyLib;
                break;
            case LibraryConstant.BOYD_ANGLE_SIDE:
                rstLib = archiveSideBodyLib;
                break;
            case LibraryConstant.BOYD_ANGLE_BACK:
                rstLib = archiveBackBodyLib;
                break;
            default:
                break;
        }
        return rstLib;
    }

    @Override
    public String queryArchives(String jsonStr) {

        JSONObject response = ResponseUtil.createResponse();

        try {
            JSONObject parseObject = JSONObject.parseObject(jsonStr);
            JSONObject archivesObject = parseObject.getJSONObject("ArchivesObject");

            JSONArray archivesID = archivesObject.getJSONArray("ArchivesID");
            List<String> archivesIDList = Lists.newArrayList();
            if (null != archivesID) {
                archivesIDList = JSONArray.parseArray(archivesID.toJSONString(), String.class);
            }

            JSONArray angle = archivesObject.getJSONArray("Angle");
            List<Integer> angleList = Lists.newArrayList();
            if (null != angle) {
                angleList = JSONArray.parseArray(angle.toJSONString(), Integer.class);
            }

            JSONArray pId = archivesObject.getJSONArray("PId");
            List<String> pIdList = Lists.newArrayList();
            if (null != pId) {
                pIdList = JSONArray.parseArray(pId.toJSONString(), String.class);
            }

            JSONArray createTime = archivesObject.getJSONArray("CreateTime");
            List<Date> createTimeList = Lists.newArrayList();
            if (null != createTime) {
                for (int i = 0; i < createTime.size(); i++) {
                    createTimeList.add(DATE_SDF_TIME.parse(createTime.getString(i)));
                }
            }

            List<ArchivesInfo> archivesInfos = archivesInfoMapper
                    .queryByCondition(archivesIDList, angleList, pIdList, createTimeList);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ArchivesObject", archivesInfos);
            response.put("ArchivesListObject", jsonObject);
            response.put("Count", archivesInfos.size());
        } catch (Exception e) {
            response = ResponseUtil.createResponse("-1", "fail");
            log.error("==== queryArchives exception : ", e);
        }
        return response.toJSONString();
    }

    @Override
    public String unbindTrail(String jsonStr) {

        JSONObject response = ResponseUtil.createResponse();
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            JSONArray persons = jsonObject.getJSONObject("TrailCondition").getJSONArray("PersonId");
            List<String> personIds = JSONArray.parseArray(JSONObject.toJSONString(persons), String.class);

            JSONObject personListObject = new JSONObject();
            JSONArray personObject = new JSONArray();
            JSONObject data = new JSONObject();
            data.put("ArchivesId", "");
            for (String personId : personIds) {
                JSONObject object = new JSONObject();
                object.put("PersonID", personId);
                object.put("Data", data);
                personObject.add(object);
            }
            personListObject.put("PersonObject", personObject);
            JSONObject requestJsonObject = new JSONObject();
            requestJsonObject.put("PersonListObject", personListObject);
            microSearchFeign.updateBodys(requestJsonObject);
        } catch (Exception e) {
            response = ResponseUtil.createResponse("-1", "fail");
            log.error("==== unbindTrail exception : ", e);
        }
        return response.toJSONString();
    }

    @Override
    public ResponseStatusList mergeArchives(HttpServletRequest request, String jsonStr) {

        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            JSONObject archivesObject = jsonObject.getJSONObject("ArchivesObject");
            String archivesID = archivesObject.getString("ArchivesID");
            List<String> archivesIds = JSONArray
                    .parseArray(archivesObject.getJSONArray("NeedUpdateArchivesID").toJSONString(), String.class);

            //循环查找顶层PID
            List<ArchivesInfo> archivesInfos = archivesInfoMapper.selectList(new QueryWrapper<>());
            Map<String, String> map = Maps.newHashMap();
            for (ArchivesInfo archivesInfo : archivesInfos) {
                map.put(archivesInfo.getId(), archivesInfo.getPId());
            }
            String topOnePID = archivesID;//顶层PID
            String tempPid = map.get(topOnePID);
            if (StringUtils.isNotEmpty(tempPid)) {
                topOnePID = tempPid;
            }

            //修改档案中PID为顶层PID
            Set<String> pIdSets = Sets.newHashSet();
            for (String archivesId : archivesIds) {
                pIdSets.add(map.get(archivesId));
            }
            if (StringUtils.isNotBlank(archivesID) && CollectionUtils.isNotEmpty(archivesIds)) {
                archivesInfoMapper.updateByArchivesID(topOnePID, Lists.newArrayList(pIdSets));
            }

        } catch (Exception e) {
            log.error("==== mergeArchives exception : ", e);
            return getResponseStatusList(createResponseStatus(request), ReponseCode.CODE_4.getCode(),
                    ReponseCode.CODE_4.getMsg(), org.apache.commons.lang3.StringUtils.EMPTY);
        }

        return getResponseStatusList(createResponseStatus(request), ReponseCode.CODE_0.getCode(),
                ReponseCode.CODE_0.getMsg(), org.apache.commons.lang3.StringUtils.EMPTY);
    }

    @Override
    public String deleteArchives(String jsonStr) {

        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            // 根据serialnumber查询es中档案id不为空的数据
            Map<String, Object> map = Maps.newHashMap();
            map.put("Face.AnalysisId", jsonObject.get("serialnumber"));
            map.put("Face.ArchivesId", "NotNull");
            map.put("Face.PageRecordNum", 1000);
            map.put("Face.RecordStartNo", 1);
            String faces = microSearchFeign.getFaces(map);
            JSONArray faceJsonArray = JSONObject.parseObject(faces).getJSONObject("FaceListObject").getJSONArray("FaceObject");
            Set<String> archivesIdsSet = Sets.newHashSet();
            for (int i = 0; i < faceJsonArray.size(); i++) {
                archivesIdsSet.add(faceJsonArray.getJSONObject(i).getString("ArchivesID"));
            }

            // 根据档案id从mysql中查出所有face_feature_id
            List<ArchivesInfo> archivesInfos = archivesInfoMapper.queryByCondition(null,
                    null, Lists.newArrayList(archivesIdsSet), null);
            Set<String> faceFeatureIds = Sets.newHashSet();
            for (ArchivesInfo archivesInfo : archivesInfos) {
                faceFeatureIds.add(archivesInfo.getFaceFeatureId());
            }

            // 根据face_feature_id删除搜图模块特征、根据档案id删除档案信息
            for (String faceFeatureId : faceFeatureIds) {
                FaceConstant.getFaceSdkInvoke().delFaceFromLib(archiveFaceLib, faceFeatureId);
            }
            archivesInfoMapper.deleteByPID(Lists.newArrayList(archivesIdsSet));

        } catch (Exception e) {
            log.error("==== deleteArchives Exception :", e);
            return ResponseUtil.createResponse("-1", "fail").toJSONString();
        }

        return ResponseUtil.createResponse().toJSONString();
    }

    /**
     * @param archivesDTO archivesDTO
     * @param archivesId  archivesId
     * @param trailSource trailSource
     * @param faceQuality faceQuality
     * @param faceScore   faceScore
     * @param bodyScore   bodyScore
     * @description: 保存轨迹对象
     * @return: void
     */
    private void saveTrailToResult(ArchivesDTO archivesDTO, String archivesId,
                                   Integer trailSource, Float faceQuality, Float faceScore, Float bodyScore) {
        if (StringUtils.isNotBlank(archivesId)) {
            archivesDTO.setArchivesID(archivesId);
        }
        if (trailSource != null) {
            archivesDTO.setTrailSource(trailSource);
        }
        if (faceQuality != null) {
            archivesDTO.setFaceQuality(ArchivesConstant.formatFloat(faceQuality));
        }
        if (faceScore != null) {
            archivesDTO.setFaceScore(ArchivesConstant.formatFloat(faceScore));
        }
        if (bodyScore != null) {
            archivesDTO.setBodyScore(ArchivesConstant.formatFloat(bodyScore));
        }

        if (archivesDTO.getRightBtmY() != null && archivesDTO.getLeftTopY() != null &&
                archivesDTO.getRightBtmX() != null && archivesDTO.getLeftTopX() != null) {
            float denominator = (float) archivesDTO.getRightBtmX() - archivesDTO.getLeftTopX();
            float molecule = (float) archivesDTO.getRightBtmY() - archivesDTO.getLeftTopY();
            if (Float.floatToRawIntBits(denominator) != 0) {
                archivesDTO.setProportion(ArchivesConstant.formatFloat(molecule * 1.000f / denominator));
            }
        } else {
            archivesDTO.setProportion(0f);
        }
    }


    @Override
    public String getArchivesIdInfos(TrailConditionDTO trailConditionDTO) throws VideoException {
        StringBuilder archivesIds = new StringBuilder();
        String baseData = trailConditionDTO.getBaseData();
        Float thresholdStr = trailConditionDTO.getThreshold();
        Integer maxArchivesNum = trailConditionDTO.getMaxArchivesNum();

        if (StringUtil.isNotNull(baseData) && !ValidUtil.validImageSize(baseData, 3)) {
            throw new VideoException(SdkExceptionConst.FAIL_CODE, "picture base64>3m");
        }
        Var faceInfoVar = FaceConstant.getFaceSdkInvoke().getPicAnalyzeOne(baseData);
        String faceFeature = faceInfoVar.getString(FEATURE_VECTOR);

        if (StringUtils.isEmpty(faceFeature)) {
            throw new VideoException(SdkExceptionConst.FAIL_CODE, "donot have feature");
        }

        Var similars = FaceConstant.getFaceSdkInvoke()
                .getSimilars(LibraryConstant.getFaceLibraryCache().getId(),
                        faceFeature, thresholdStr * 100, maxArchivesNum);
        if (!similars.isNull()) {
            String[] featureIds = ArchivesConstant.getSearchResultIds(similars);
            if (featureIds != null && featureIds.length > 0) {
                List<ArchivesInfo> archivesInfos = this.list(new QueryWrapper<ArchivesInfo>()
                        .in(FACE_FEATURE_ID, Arrays.asList(featureIds)).groupBy("id"));
                for (ArchivesInfo archives : archivesInfos) {
                    archivesIds.append(",");
                    archivesIds.append(archives.getId());
                }
            }
        }
        if (StringUtils.isBlank(archivesIds.toString())) {
            return "";
        }
        return archivesIds.substring(1);
    }


    public List<TrailDTO> getTrailInfosOld(TrailConditionDTO trailConditionDTO) throws VideoException {
        List<TrailDTO> trailDTOList = new ArrayList<>();
        String baseData = trailConditionDTO.getBaseData();
        String startTime = trailConditionDTO.getBeginTime();
        String endTime = trailConditionDTO.getEndTime();
        Float thresholdStr = trailConditionDTO.getThreshold();
        Integer maxArchivesNum = trailConditionDTO.getMaxArchivesNum();

        if (StringUtils.isNotBlank(startTime) && !PatternUtil.isMatch(startTime, JsonPatterUtil.DATE_PATTER)) {
            log.info("begin time is incompatible format --" + JsonPatterUtil.DATE_PATTER);
            return trailDTOList;
        }
        if (StringUtils.isNotBlank(endTime) && !PatternUtil.isMatch(endTime, JsonPatterUtil.DATE_PATTER)) {
            log.info("endTime time is incompatible format --" + JsonPatterUtil.DATE_PATTER);
            return trailDTOList;
        }

        if (StringUtil.isNotNull(baseData) && !ValidUtil.validImageSize(baseData, 3)) {
            log.info("picture base64>3m");
            return trailDTOList;
        }
        Var faceInfoVar = FaceConstant.getFaceSdkInvoke().getPicAnalyzeOne(baseData);
        String faceFeature = faceInfoVar.getString(FEATURE_VECTOR);

        if (StringUtils.isEmpty(faceFeature)) {
            log.info("donot have feature");
            return trailDTOList;
        }

        Var similars = FaceConstant.getFaceSdkInvoke().getSimilars(LibraryConstant.getFaceLibraryCache().getId(),
                faceFeature, thresholdStr * 100, maxArchivesNum);
        if (similars.isNull()) {
            log.info("face to archivesLib similars is null");
            return trailDTOList;
        }
        String[] featureIds = ArchivesConstant.getSearchResultIds(similars);
        Map<String, Float> aidScore = ArchivesConstant.getSearchResultIdsAndScore(similars);
        if (featureIds == null || featureIds.length <= 0) {
            log.info("donot have suitable archives ");
            return trailDTOList;
        }
        List<ArchivesInfo> archivesInfos = this.list(new QueryWrapper<ArchivesInfo>()
                .in(FACE_FEATURE_ID, Arrays.asList(featureIds)).groupBy("id"));
        if (CollectionUtils.isEmpty(archivesInfos)) {
            log.info("donnot have suitable archives by face_featureid --" + Arrays.asList(featureIds));
            return trailDTOList;
        }
        for (ArchivesInfo archivesInfo : archivesInfos) {
            if (aidScore.containsKey(archivesInfo.getFaceFeatureId())) {
                aidScore.put(archivesInfo.getId(), aidScore.get(archivesInfo.getFaceFeatureId()));
                aidScore.remove(archivesInfo.getFaceFeatureId());
            }
        }
        return getGbToTrail(trailConditionDTO, archivesInfos, aidScore);
    }

    @Override
    public List<TrailDTO> getTrailInfos(TrailConditionDTO trailConditionDTO) throws VideoException {
        List<TrailDTO> trailDTOList = new ArrayList<>();
        String baseData = trailConditionDTO.getBaseData();
        String startTime = trailConditionDTO.getBeginTime();
        String endTime = trailConditionDTO.getEndTime();
        Float thresholdStr = trailConditionDTO.getThreshold();
        Integer maxArchivesNum = trailConditionDTO.getMaxArchivesNum();

        if (StringUtils.isNotBlank(startTime) && !PatternUtil.isMatch(startTime, JsonPatterUtil.DATE_PATTER)) {
            log.info("begin time is incompatible format --" + JsonPatterUtil.DATE_PATTER);
            return trailDTOList;
        }
        if (StringUtils.isNotBlank(endTime) && !PatternUtil.isMatch(endTime, JsonPatterUtil.DATE_PATTER)) {
            log.info("endTime time is incompatible format --" + JsonPatterUtil.DATE_PATTER);
            return trailDTOList;
        }

        if (StringUtil.isNotNull(baseData) && !ValidUtil.validImageSize(baseData, 3)) {
            log.info("picture base64>3m");
            return trailDTOList;
        }
        Var faceInfoVar = FaceConstant.getFaceSdkInvoke().getPicAnalyzeOne(baseData);
        String faceFeature = faceInfoVar.getString(FEATURE_VECTOR);

        if (StringUtils.isEmpty(faceFeature)) {
            log.info("donot have feature");
            return trailDTOList;
        }

        Var similars = FaceConstant.getFaceSdkInvoke().getSimilars(LibraryConstant.getFaceLibraryCache().getId(),
                faceFeature, thresholdStr * 100, maxArchivesNum);
        if (similars.isNull()) {
            log.info("face to archivesLib similars is null");
            return trailDTOList;
        }
        String[] featureIds = ArchivesConstant.getSearchResultIds(similars);
        Map<String, Float> aidScore = ArchivesConstant.getSearchResultIdsAndScore(similars);
        if (featureIds == null || featureIds.length <= 0) {
            log.info("donot have suitable archives ");
            return trailDTOList;
        }
        log.info("====getTrailInfos featureIds : " + JSONObject.toJSONString(featureIds));
        List<ArchivesInfo> archivesInfos = this.list(new QueryWrapper<ArchivesInfo>()
                .in(FACE_FEATURE_ID, Arrays.asList(featureIds)).groupBy("id"));
        if (CollectionUtils.isEmpty(archivesInfos)) {
            log.info("donnot have suitable archives by face_featureid --" + Arrays.asList(featureIds));
            return trailDTOList;
        }

        // 找出所有档案信息
        Set<String> pidSets = Sets.newHashSet();
        for (ArchivesInfo archivesInfo : archivesInfos) {
            pidSets.add(archivesInfo.getPId());
        }
        List<ArchivesInfo> archivesInfoAll = archivesInfoMapper.queryByCondition(null, null,
                Lists.newArrayList(pidSets), null);


        Map<String, Set<Float>> mapPidScoreAll = Maps.newHashMap();
        Map<String, Float> mapPidScore = Maps.newHashMap();
        Map<String, String> mapAidPid = Maps.newHashMap();
        Map<String, String> mapPidImgUrl = Maps.newHashMap();
        log.info("====getTrailInfos aidScore : " + JSONObject.toJSONString(aidScore));

        for (ArchivesInfo archivesInfo : archivesInfoAll) {

            mapPidImgUrl.put(archivesInfo.getPId(), archivesInfo.getFaceImgUrl());
            // 组装<Pid,Score>
            String pId = archivesInfo.getPId();
            Set<Float> floatSet = mapPidScoreAll.get(pId);
            Float aFloat = aidScore.get(archivesInfo.getFaceFeatureId());
            if (null == floatSet) {
                Set<Float> set = Sets.newHashSet();
                set.add(aFloat == null ? 0 : aFloat);
                mapPidScoreAll.put(pId, set);
            } else {
                floatSet.add(aFloat == null ? 0 : aFloat);
                mapPidScoreAll.put(pId, floatSet);
            }

            mapAidPid.put(archivesInfo.getId(), archivesInfo.getPId());

            if (aidScore.containsKey(archivesInfo.getFaceFeatureId())) {
                aidScore.put(archivesInfo.getId(), aidScore.get(archivesInfo.getFaceFeatureId()));
                aidScore.remove(archivesInfo.getFaceFeatureId());
            }
        }

        for (Map.Entry<String, Set<Float>> entry : mapPidScoreAll.entrySet()) {
            String pId = entry.getKey();
            Set<Float> value = entry.getValue();
            Float maxScore = 0f;
            for (Float score : value) {
                maxScore = (maxScore < score) ? score : maxScore;
            }
            mapPidScore.put(pId, maxScore);
        }

        List<TrailDTO> gbToTrail = getGbToTrail(trailConditionDTO, archivesInfoAll, aidScore);
        log.info("====getTrailInfos gbToTrail size():" + gbToTrail.size());

        return backArchives(mapAidPid, gbToTrail, aidScore, mapPidScore, mapPidImgUrl);
    }

    /**
     * 获取maxArchivesNum数据。
     *
     * @param maxArchivesNum
     * @param gbToTrail
     * @return
     */
    private List<TrailDTO> getMaxArchivesNum(Integer maxArchivesNum, List<TrailDTO> gbToTrail) {


        return null;
    }


    /**
     * 反填档案Id 和 分数,对于有档案合并情况时有用
     *
     * @param mapAidPid
     * @param gbToTrail
     * @param aidScore
     * @return
     */
    private List<TrailDTO> backArchives(Map<String, String> mapAidPid, List<TrailDTO> gbToTrail,
                                        Map<String, Float> aidScore, Map<String, Float> mapPidScore, Map<String, String> mapPidImgUrl) {

        if (CollectionUtils.isEmpty(gbToTrail)) {
            return gbToTrail;
        }
        // 反填档案ID
        for (TrailDTO trailDTO : gbToTrail) {
            String archivesID = mapAidPid.get(trailDTO.getArchivesID());
            trailDTO.setArchivesID(archivesID);
        }

        // 反填分数
        for (TrailDTO trailDTO : gbToTrail) {
            Float score = mapPidScore.get(trailDTO.getArchivesID());
            String archiveFaceImgUrl = mapPidImgUrl.get(trailDTO.getArchivesID());
            trailDTO.setArchiveFaceImgUrl(archiveFaceImgUrl);
            trailDTO.setFaceScore(score);
            trailDTO.setBodyScore(score);
        }
        return gbToTrail;
    }

    /**
     * 根据人脸Id查找人形数据（完善人形数据）
     *
     * @param faceTrailList
     * @param bodyTrailList
     */
    private void findPersonByFaceData(List<TrailDTO> faceTrailList, List<TrailDTO> bodyTrailList) {

        Set<String> faceIdsSet = Sets.newHashSet();
        for (TrailDTO trailDTO : faceTrailList) {
            faceIdsSet.add(trailDTO.getFaceID());
        }
        String faceIdStr = "";
        List<String> faceIdsList = Lists.newArrayList(faceIdsSet);
        for (int i = 0; i < faceIdsList.size(); i++) {
            faceIdStr += (i == faceIdsList.size() - 1 ? faceIdsList.get(i) : faceIdsList.get(i) + ",");
        }
        Map<String, Object> requestPara = Maps.newHashMap();
        requestPara.put("Persons.FaceUUID.In", faceIdStr);
        // 查询es数据
        List<TrailDTO> list = Lists.newArrayList();
        if (StringUtils.isNotEmpty(faceIdStr)) {
            getBodyToTrail(list, requestPara);
        }

        // 将查询到的人形加入已有人形数据
        Map<String, TrailDTO> mapPersonId = Maps.newHashMap();
        for (TrailDTO trailDTO : bodyTrailList) {
            mapPersonId.put(trailDTO.getPersonID(), trailDTO);
        }
        for (TrailDTO trailDTO : list) {
            if (mapPersonId.get(trailDTO.getPersonID()) == null) {
                bodyTrailList.add(trailDTO);
            }
        }
    }

    private List<TrailDTO> getGbToTrail(TrailConditionDTO trailConditionDTO, List<ArchivesInfo> archivesInfos, Map<String, Float> aidScore) {
        List<TrailDTO> trailDTOList = new ArrayList<>();
        Map<String, Object> facesPara = getGbUrlCondition("Faces", trailConditionDTO, archivesInfos);
        List<TrailDTO> faceTrailList = getFaceToTrail(facesPara);
        Map<String, Object> personsPara = getGbUrlCondition("Persons", trailConditionDTO,
                archivesInfos);
        List<TrailDTO> bodyTrailList = getBodyToTrail(personsPara);

        log.info("====getGbToTrail faceTrailList size() find start: " + faceTrailList.size());
        log.info("====getGbToTrail bodyTrailList size() find start: " + bodyTrailList.size());

        findPersonByFaceData(faceTrailList, bodyTrailList);

        log.info("====getGbToTrail faceTrailList size() find end: " + faceTrailList.size());
        log.info("====getGbToTrail bodyTrailList size() find end: " + bodyTrailList.size());

        for (TrailDTO faceTrail : faceTrailList) {
            Iterator<TrailDTO> it = bodyTrailList.iterator();
            if (aidScore.containsKey(faceTrail.getArchivesID())) {
                faceTrail.setFaceScore(aidScore.get(faceTrail.getArchivesID()));
            }
            while (it.hasNext()) {
                TrailDTO bodyTrail = it.next();
                if (faceTrail.getConnectObjectId().equals(bodyTrail.getPersonID())) {
                    faceTrail.setPersonID(bodyTrail.getPersonID());
                    faceTrail.setBodyScore(bodyTrail.getBodyScore());
                    faceTrail.setBodyQuality(bodyTrail.getBodyQuality());
                    faceTrail.setBodyImgUrl(bodyTrail.getBodyImgUrl());
                    faceTrail.setAngle(bodyTrail.getAngle());
                    faceTrail.setProportion(ArchivesConstant.calProportion(bodyTrail.getRightBtmX(), bodyTrail.getRightBtmY(),
                            bodyTrail.getLeftTopX(), bodyTrail.getLeftTopY()));
                    it.remove();
                }
            }
            trailDTOList.add(faceTrail);
        }
        if (CollectionUtils.isNotEmpty(bodyTrailList)) {
            for (TrailDTO bodyTrail : bodyTrailList) {
                if (aidScore.containsKey(bodyTrail.getArchivesID())) {
                    bodyTrail.setFaceScore(aidScore.get(bodyTrail.getArchivesID()));
                    bodyTrail.setProportion(ArchivesConstant.calProportion(bodyTrail.getRightBtmX(), bodyTrail.getRightBtmY(),
                            bodyTrail.getLeftTopX(), bodyTrail.getLeftTopY()));
                }
                trailDTOList.add(bodyTrail);
            }
        }
        return trailDTOList;
    }

    private Map<String, Object> getGbUrlCondition(String type, TrailConditionDTO trailConditionDTO, List<ArchivesInfo> archivesInfos) {
        StringBuilder archivesSbr = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        for (ArchivesInfo archives : archivesInfos) {
            archivesSbr.append(",");
            archivesSbr.append(archives.getId());
        }
        map.put(type + ".ArchivesID.In", archivesSbr.substring(1));
        map.put(type + ".MarkTime.Order", "desc");
        map.put(type + ".PageRecordNum", 3000);
        map.put(type + ".RecordStartNo", 1);
        if (StringUtils.isNotBlank(trailConditionDTO.getMonitorIDList())) {
            map.put(type + ".DeviceID.In", trailConditionDTO.getMonitorIDList());
        }
        if (StringUtils.isNotBlank(trailConditionDTO.getBeginTime())) {
            map.put(type + ".MarkTime.Gte", trailConditionDTO.getBeginTime());
        }
        if (StringUtils.isNotBlank(trailConditionDTO.getEndTime())) {
            map.put(type + ".MarkTime.Lte", trailConditionDTO.getEndTime());
        }
        if (StringUtils.isNotBlank(trailConditionDTO.getTrailSource())) {
            map.put(type + ".TrailSource.In", trailConditionDTO.getTrailSource());
        }
        return map;
    }

    private List<TrailDTO> getFaceToTrail(Map<String, Object> paramSbr) {
        List<TrailDTO> trailDTOList = new ArrayList<>();
        int count = getFaceToTrail(trailDTOList, paramSbr);
        if (count > 3000) {
            for (int i = 1; i < (count / 3000) + 1; i++) {
                paramSbr.put("Faces.PageRecordNum", 3000);
                paramSbr.put("Faces.RecordStartNo", i + 1);
                getFaceToTrail(trailDTOList, paramSbr);
            }
        }
        return trailDTOList;
    }

    private Integer getFaceToTrail(List<TrailDTO> trailDTOList, Map<String, Object> paramSbr) {
        String respContent = microSearchFeign.getFaces(paramSbr);
        JSONArray faceJsonArray = JSONObject.parseObject(respContent).getJSONObject("FaceListObject").getJSONArray("FaceObject");
        int count = 0;
        if (!faceJsonArray.isEmpty()) {
            count = JSONObject.parseObject(respContent).getJSONObject("FaceListObject").getInteger("Count");
            for (int i = 0; i < faceJsonArray.size(); i++) {
                JSONObject jsonObject = faceJsonArray.getJSONObject(i);
                TrailDTO trailDTO = new TrailDTO();
                trailDTO.initFaceJSON(jsonObject);
                trailDTOList.add(trailDTO);
            }
        }
        return count;
    }

    private void addInfoToClust(ArchivesDTO archivesDTO) {
        if (StringUtils.isNotBlank(archivesDTO.getArchivesID())) {
            FaceFeatureClusterSpringRedis.addFeature(faceRedisTemplate, archivesDTO.getFaceID() + "-" + archivesDTO.getArchivesID(),
                    Base64.decode(archivesDTO.getFaceFeature().getBytes()));
        } else {
            FaceFeatureClusterSpringRedis.addFeature(faceRedisTemplate, archivesDTO.getFaceID(),
                    Base64.decode(archivesDTO.getFaceFeature().getBytes()));
        }
    }

    private List<TrailDTO> getBodyToTrail(Map<String, Object> paramSbr) {
        List<TrailDTO> trailDTOList = new ArrayList<>();
        int count = getBodyToTrail(trailDTOList, paramSbr);
        if (count > 3000) {
            for (int i = 1; i < (count / 3000) + 1; i++) {
                paramSbr.put("Persons.PageRecordNum", 3000);
                paramSbr.put("Persons.RecordStartNo", i + 1);
                getBodyToTrail(trailDTOList, paramSbr);
            }
        }
        return trailDTOList;
    }

    private Integer getBodyToTrail(List<TrailDTO> trailDTOList, Map<String, Object> paramSbr) {
        String respContent = microSearchFeign.getBodys(paramSbr);
        JSONArray bodyJsonArray = JSONObject.parseObject(respContent).getJSONObject("PersonListObject").getJSONArray("PersonObject");
        int count = 0;
        if (!bodyJsonArray.isEmpty()) {
            count = JSONObject.parseObject(respContent).getJSONObject("PersonListObject").getInteger("Count");
            for (int i = 0; i < bodyJsonArray.size(); i++) {
                JSONObject jsonObject = bodyJsonArray.getJSONObject(i);
                TrailDTO trailDTO = new TrailDTO();
                trailDTO.initBodyJSON(jsonObject);
                trailDTOList.add(trailDTO);
            }
        }
        return count;
    }

    /***
     * @description: 新版一人一档，使用人脸汇聚+人形汇聚
     * @param archivesDTO
     * @return: void
     */
    @Override
    public void saveConvergenceObjext(ArchivesDTO archivesDTO) throws VideoException {

        initLib();
        if (StringUtils.isNotBlank(archivesDTO.getFaceFeature()) && StringUtils.isNotBlank(archivesDTO.getFaceID())) {
            log.info("into faceSwitchClust" + archivesDTO.getFaceID() + "---/---" + archivesDTO.getFaceFeature());
            faceSwitchClust(archivesDTO);
        }
        if (StringUtils.isNotBlank(archivesDTO.getBodyFeature()) && StringUtils.isNotBlank(archivesDTO.getBodyID())
                && StringUtils.isNotBlank(archivesDTO.getDeviceID())) {
            log.info("into bodySwitchClust" + archivesDTO.getFaceID() + "---/---" + archivesDTO.getBodyFeature());
            bodySwitchClust(archivesDTO);
        }
    }

    /***
     * @description:
     * @param archivesDTO
     * @return: void
     */
    private void faceSwitchClust(ArchivesDTO archivesDTO) throws VideoException {

        String archivesId = StringUtils.EMPTY;
        Var similars = FaceConstant.getFaceSdkInvoke().getSimilars(archiveFaceLib, archivesDTO.getFaceFeature(),
                DbPropUtil.getFloat("face.archives.compare.threshold", 0.75f) * 100, 100);
        /*判断是否有与档案比对上的，若有，则根据人脸比对成功，添加至轨迹*/
        String[] featureIds = ArchivesConstant.getSearchResultIds(similars);
        log.info("====featureIds:" + JSONObject.toJSONString(featureIds));
        if (featureIds != null && featureIds.length > 0) {
            log.info("similars faceSwitchClust" + featureIds.length);
            List<ArchivesInfo> archivesInfoList = baseMapper.selectList(new QueryWrapper<ArchivesInfo>()
                    .isNotNull(FACE_FEATURE_ID).in(FACE_FEATURE_ID, Arrays.asList(featureIds)));
            /*添加分数、轨迹等信息*/
            ArchivesInfo archivesInfo = null;
            for (ArchivesInfo aInfo : archivesInfoList) {
                if (StringUtils.isNotBlank(aInfo.getPId())) {
                    archivesInfo = aInfo;
                    archivesId = archivesInfo.getPId();
                    archiveClustService.updateEsFace(Arrays.asList(archivesDTO.getFaceID()), archivesId);
                    archivesDTO.setArchivesID(archivesId);
                    log.info("similars faceSwitchClust" + archivesId);
                    break;
                }
            }
        }
        addInfoToClust(archivesDTO);
        if (StringUtils.isNotBlank(archivesDTO.getFaceID()) && StringUtils.isNotBlank(archivesDTO.getBodyID())) {
            ArchivesRelationInfo archivesRelationInfo = new ArchivesRelationInfo(archivesDTO.getFaceID(),
                    archivesDTO.getBodyID(), archivesId);
            archivesRelationInfoService.saveOrUpdate(archivesRelationInfo);
        }
    }

    private void bodySwitchClust(ArchivesDTO archivesDTO) {

        if (archivesDTO.getBodyQuality() - DbPropUtil.getFloat("body.archives.quality.threshold", 0f) >= 0 &&
                ArchivesConstant.calProportion(archivesDTO) - DbPropUtil.getFloat("body.archives.proportion", 2f) >= 0) {
            ReidFeatureClusterSpringRedis.addFeature(bodyRedisTemplate, archivesDTO.getDeviceID(),
                    archivesDTO.getBodyID(), Base64.decode(archivesDTO.getBodyFeature().getBytes()));
        }

        ArchivesRelationInfo archivesRelationInfo = archivesRelationInfoService.getOne(new QueryWrapper<ArchivesRelationInfo>()
                .eq("body_id", archivesDTO.getBodyID()));
        if (archivesRelationInfo != null && StringUtils.isNotBlank(archivesRelationInfo.getArchivesId())) {
            archiveClustService.updateEsBody(Arrays.asList(archivesDTO.getBodyID()),
                    archivesRelationInfo.getArchivesId(), ArchivesConstant.TRACE_SOURCE_FACE);
            archivesRelationInfoService.remove(new QueryWrapper<ArchivesRelationInfo>().eq("body_id", archivesDTO.getBodyID()));
            //确定存在aid后，填充人形表正背侧面
            archiveClustService.addArchivesBodyInfo(archivesDTO.getBodyQuality(), archivesDTO.getAngle(),
                    archivesRelationInfo.getArchivesId(), archivesDTO.getBodyImgUrl(), archivesDTO.getBodyFeature());
        }

    }


    private ResponseStatusList getResponseStatusList(ResponseStatus responseStatus, String code,
                                                     String message, String data) {
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

    private ResponseStatus createResponseStatus(HttpServletRequest request) {
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setRequestURL(request.getRequestURI());
        responseStatus
                .setLocalTime(com.keensense.common.util.DateUtil.formatDate(new Date(), "yyyyMMddHHmmss"));
        return responseStatus;
    }
}
