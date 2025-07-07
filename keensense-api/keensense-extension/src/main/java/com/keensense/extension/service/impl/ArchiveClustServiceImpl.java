package com.keensense.extension.service.impl;

import cn.jiuling.plugin.extend.featureclust.FaceFeatureClusterSpringRedis;
import cn.jiuling.plugin.extend.featureclust.ReidFeatureClusterSpringRedis;
import cn.jiuling.plugin.extend.featureclust.entity.ReidRecord;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.extension.config.NacosConfig;
import com.keensense.extension.constants.ArchivesConstant;
import com.keensense.extension.constants.LibraryConstant;
import com.keensense.extension.entity.ArchivesBodyInfo;
import com.keensense.extension.entity.ArchivesInfo;
import com.keensense.extension.entity.ArchivesRelationInfo;
import com.keensense.extension.entity.CameraRelationInfo;
import com.keensense.extension.entity.TbAnalysisTask;
import com.keensense.extension.feign.IFeignService;
import com.keensense.extension.feign.IMicroSearchFeign;
import com.keensense.extension.service.IArchivesBodyInfoService;
import com.keensense.extension.service.IArchivesClustService;
import com.keensense.extension.service.IArchivesInfoService;
import com.keensense.extension.service.IArchivesRelationInfoService;
import com.keensense.extension.service.ICameraRelationInfoService;
import com.keensense.extension.service.ITbAnalysisTaskService;
import com.keensense.extension.util.DownloadImageUtil;
import com.keensense.extension.util.IDUtil;
import com.keensense.sdk.constants.BodyConstant;
import com.keensense.sdk.constants.FaceConstant;
import com.keensense.sdk.sys.utils.DbPropUtil;
import com.loocme.sys.datastruct.Var;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

/***
 * @description:
 * @author jingege
 * @return:
 */
@Service
@Slf4j
public class ArchiveClustServiceImpl implements IArchivesClustService {

    @Autowired
    private IArchivesBodyInfoService archivesBodyInfoService;
    @Autowired
    private IArchivesRelationInfoService archivesRelationInfoService;
    @Autowired
    private IArchivesInfoService archivesInfoService;
    @Autowired
    private IMicroSearchFeign microSearchFeign;
    @Autowired
    private IFeignService feignService;
    @Autowired
    private ICameraRelationInfoService cameraRelationInfoService;
    @Autowired
    private ITbAnalysisTaskService tbAnalysisTaskService;
    @Autowired
    private NacosConfig nacosConfig;

    @Resource(name = "bodyRedisTemplate")
    private RedisTemplate<String, String> bodyRedisTemplate;
    @Resource(name = "faceRedisTemplate")
    private RedisTemplate<String, String> faceRedisTemplate;

    private static final String FEATURE_VECTOR = "featureVector";
    private static final String FACE_FEATURE_ID = "face_feature_id";

    private String archiveFaceLib = StringUtils.EMPTY;
    private String archiveFrontBodyLib = StringUtils.EMPTY;
    private String archiveSideBodyLib = StringUtils.EMPTY;
    private String archiveBackBodyLib = StringUtils.EMPTY;

    private static String AID = "";
    private static String PAID = "";
    private static final int ES_LENGTH = 200;

    private static Map<String, HashSet<String>> CAMERA_RELATION_LIST = new HashMap<>();

    private void initLib() {
        archiveFaceLib = LibraryConstant.getFaceLibraryCache().getId();
        archiveFrontBodyLib = LibraryConstant.getBodyLibraryByAngle(LibraryConstant.BOYD_ANGLE_FRONT, new Date()).getId();
        archiveSideBodyLib = LibraryConstant.getBodyLibraryByAngle(LibraryConstant.BOYD_ANGLE_SIDE, new Date()).getId();
        archiveBackBodyLib = LibraryConstant.getBodyLibraryByAngle(LibraryConstant.BOYD_ANGLE_BACK, new Date()).getId();
    }

    @Override
    public void startFaceClust() {
        try {
            initLib();
            ArchivesConstant.initAngle();
            Map<Integer, List<String>> cluster = FaceFeatureClusterSpringRedis.cluster();
            if (cluster.isEmpty()) {
                return;
            }
            for (Entry<Integer, List<String>> map : cluster.entrySet()) {
                Integer key = map.getKey();
                List<String> value = map.getValue();
                if (value.size() < 2) {
                    log.info("====startFaceClust.value:" + JSONObject.toJSONString(value));
                }
                createArchives(key, value);
            }
        } catch (Exception e) {
            log.error("startFaceClust", e);
        } finally {
            FaceFeatureClusterSpringRedis.endCluster(faceRedisTemplate);
        }
    }

    private void createArchives(Integer mapRid, List<String> faceIds) throws Exception {

        AID = isExitsAId(faceIds);
        if (StringUtils.isNotBlank(AID)) {
            AID = AID.split("-")[1];
        } else {
            AID = IDUtil.uuid();
        }
        //建档
        boolean isSuccess = getArchives(mapRid, faceIds, ArchivesConstant.ARCHIVES_FACE_FRONT);
        AID = IDUtil.uuid();
        if (isSuccess) {
            getArchives(mapRid, faceIds, ArchivesConstant.ARCHIVES_FACE_SIDE);
            AID = IDUtil.uuid();
            getArchives(mapRid, faceIds, ArchivesConstant.ARCHIVES_FACE_BOTTOM);
            AID = IDUtil.uuid();
            FaceFeatureClusterSpringRedis.removeCluster(faceRedisTemplate, mapRid);
        }
    }

    private String isExitsAId(List<String> faceIds) {
        for (String faceId : faceIds) {
            if (faceId.split("-").length == 2 && !faceId.split("-")[1].equals("null") &&
                    StringUtils.isNotBlank(faceId.split("-")[1])) {
                return faceId;
            }
        }
        return StringUtils.EMPTY;
    }

    private boolean getArchives(Integer mapRid, List<String> faceIds, int faceType) throws Exception {
        float[] yaw = new float[2];
        float[] pitch = new float[2];
        if (faceType - ArchivesConstant.ARCHIVES_FACE_FRONT == 0) {
            yaw = ArchivesConstant.frontYaw;
            pitch = ArchivesConstant.frontPitch;
        } else if (faceType - ArchivesConstant.ARCHIVES_FACE_SIDE == 0) {
            yaw = ArchivesConstant.sideYaw;
            pitch = ArchivesConstant.sidePitch;
        } else if (faceType - ArchivesConstant.ARCHIVES_FACE_BOTTOM == 0) {
            yaw = ArchivesConstant.bottomYaw;
            pitch = ArchivesConstant.bottomPitch;
        }
        //两个角度区间
        boolean isPSuccess = false;
        List<String> newFaceIds = new ArrayList<>();
        Map<String, Object> map = initFaceParams(faceIds, newFaceIds, pitch, yaw, 11);
        isPSuccess = getSearchArchives(mapRid, newFaceIds, map, faceType);
        map.clear();
        if (!isPSuccess) {
            AID = IDUtil.uuid();
            map = initFaceParams(faceIds, newFaceIds, pitch, yaw, 12);
            isPSuccess = getSearchArchives(mapRid, newFaceIds, map, faceType);
            map.clear();
        }
        if (!isPSuccess) {
            AID = IDUtil.uuid();
            map = initFaceParams(faceIds, newFaceIds, pitch, yaw, 21);
            isPSuccess = getSearchArchives(mapRid, newFaceIds, map, faceType);
            map.clear();
        }
        if (!isPSuccess) {
            AID = IDUtil.uuid();
            map = initFaceParams(faceIds, newFaceIds, pitch, yaw, 22);
            isPSuccess = getSearchArchives(mapRid, newFaceIds, map, faceType);
            map.clear();
        }
        return isPSuccess;
    }

    private boolean getSearchArchives(Integer mapRid, List<String> newFaceIds, Map<String, Object> map, int faceType) throws Exception {

        if (faceType - ArchivesConstant.ARCHIVES_FACE_FRONT == 0) {
            PAID = AID;
        }

        ArchivesInfo archivesInfo = archivesInfoService.getOne(new QueryWrapper<ArchivesInfo>().eq("p_id", PAID)
                .eq("angle", faceType));
        if (archivesInfo != null) {
            if (faceType - ArchivesConstant.ARCHIVES_FACE_FRONT == 0) {
                PAID = AID;
            }
            log.info("====getSearchArchives updateAllResultByAid() newFaceIds: " + JSONObject.toJSONString(newFaceIds));
            updateAllResultByAid(newFaceIds);
            return true;
        }

        String respContent = microSearchFeign.getFaces(map);
        JSONArray faceJsonArray = JSONObject.parseObject(respContent).getJSONObject("FaceListObject").getJSONArray("FaceObject");
        if (!faceJsonArray.isEmpty()) {
            //建档，正侧低头
            JSONObject jsonObject = faceJsonArray.getJSONObject(0);
            String url = jsonObject.getString("ImgUrl");
            Var json = FaceConstant.getFaceSdkInvoke().getPicAnalyzeOne(url);
            String faceFeature = json.getString(FEATURE_VECTOR);

            /*判断是否有与档案已经加入搜图模块比对上的，若有，则根据人脸比对成功，使用搜图出的结果*/
            Var similars = FaceConstant.getFaceSdkInvoke().getSimilars(archiveFaceLib, faceFeature,
                    DbPropUtil.getFloat("face.archives.compare.threshold", 0.75f) * 100, 100);
            String[] featureIds = ArchivesConstant.getSearchResultIds(similars);
            if (featureIds != null && featureIds.length > 0) {
                List<ArchivesInfo> archivesInfoList = archivesInfoService.list(new QueryWrapper<ArchivesInfo>()
                        .isNotNull(FACE_FEATURE_ID).in(FACE_FEATURE_ID, Arrays.asList(featureIds)));
                /*选一个存在的,用pid赋值*/
                for (ArchivesInfo aInfo : archivesInfoList) {
                    if (StringUtils.isNotBlank(aInfo.getPId())) {
                        archivesInfo = aInfo;
                        PAID = archivesInfo.getPId();
                        AID = archivesInfo.getPId();
                        log.info("====getSearchArchives updateAllResultByAid() newFaceIds: " + JSONObject.toJSONString(newFaceIds));
                        updateAllResultByAid(newFaceIds);
                        return true;
                    }
                }
            }

            String featureId = FaceConstant.getFaceSdkInvoke().addFaceToLib(archiveFaceLib, faceFeature, url);
            ArchivesInfo newArchivesInfo = new ArchivesInfo();
            newArchivesInfo.initFace(AID, jsonObject, featureId, mapRid, faceType);
            String imageUrl = savePicByArchives(url);
            newArchivesInfo.setFaceImgUrl(imageUrl);
            newArchivesInfo.setPId(StringUtils.isBlank(PAID) ? AID : PAID);
            archivesInfoService.saveOrUpdate(newArchivesInfo);
            log.info("====getSearchArchives updateAllResultByAid() newFaceIds: " + JSONObject.toJSONString(newFaceIds));
            updateAllResultByAid(newFaceIds);
            return true;
        }
        return false;
    }

    public String savePicByArchives(String imgUrl) throws IOException {
        // 下载图片
        String imageBase64 = DownloadImageUtil.downloadImage(imgUrl);
        imageBase64 = imageBase64.replaceAll("\\r|\\n", "");
        // 存储图片到fdfs得到图片url
        String saveImageInfo = feignService.imageArchivesSave(UUID.randomUUID().toString(), imageBase64);
        JSONObject jsonObject = JSONObject.parseObject(saveImageInfo);
        String imageUrl = jsonObject.getString("ImageUrl");
        return imageUrl;
    }

    /***
     * @description: pn  p=1 正数  n=2负数
     * @param faceIds
     * @param pitch
     * @param yaw
     * @param pn
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     */
    private Map<String, Object> initFaceParams(List<String> faceIds, List<String> newFaceIds, float[] pitch, float[] yaw, int pn) {
        String type = "Face";
        StringBuilder archivesSbr = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        newFaceIds.clear();
        for (String id : faceIds) {
            archivesSbr.append(",");
            archivesSbr.append(id.split("-")[0]);
            newFaceIds.add(id.split("-")[0]);
        }
        map.put(type + ".FaceID.In", archivesSbr.substring(1));
        map.put(type + ".FaceQuality.Gte", DbPropUtil.getDouble("face.archives.quality.threshold", 0.8));
        map.put(type + ".FaceQuality.Order", "desc");
        map.put(type + ".PageRecordNum", 10);
        map.put(type + ".RecordStartNo", 1);
        if (pn == 11) {
            map.put(type + ".Yaw.Gte", yaw[0]);
            map.put(type + ".Yaw.Lte", yaw[1]);
            map.put(type + ".Pitch.Gte", pitch[0]);
            map.put(type + ".Pitch.Lte", pitch[1]);
        } else if (pn == 12) {
            map.put(type + ".Yaw.Gte", -yaw[1]);
            map.put(type + ".Yaw.Lte", -yaw[0]);
            map.put(type + ".Pitch.Gte", pitch[0]);
            map.put(type + ".Pitch.Lte", pitch[1]);
        } else if (pn == 21) {
            map.put(type + ".Yaw.Gte", yaw[0]);
            map.put(type + ".Yaw.Lte", yaw[1]);
            map.put(type + ".Pitch.Gte", -pitch[1]);
            map.put(type + ".Pitch.Lte", -pitch[0]);
        } else if (pn == 22) {
            map.put(type + ".Yaw.Gte", -yaw[1]);
            map.put(type + ".Yaw.Lte", -yaw[0]);
            map.put(type + ".Pitch.Gte", -pitch[1]);
            map.put(type + ".Pitch.Lte", -pitch[0]);
        }
        return map;
    }

    @Override
    public void updateEsFace(List<String> ids, String archivesId) {
        if (ids.size() <= ES_LENGTH) {
            updateEsFaceBatch(ids, archivesId);
        } else {
            for (int i = 0; i < ids.size() + 1; ) {
                if (i + ES_LENGTH >= ids.size()) {
                    updateEsFaceBatch(ids.subList(i, ids.size()), archivesId);
                    return;
                }
                updateEsFaceBatch(ids.subList(i, i + ES_LENGTH), archivesId);
                i = i + ES_LENGTH;
            }
        }
    }


    /***
     * @description: 更新es和人脸人形关联表和人形表
     * @param newFaceIds
     * @return: void
     */
    private void updateAllResultByAid(List<String> newFaceIds) {
        //更新
        String aid = StringUtils.isBlank(PAID) ? AID : PAID;
        updateEsFace(newFaceIds, aid);
        List<ArchivesRelationInfo> archivesRelationInfoList = archivesRelationInfoService.list(
                new QueryWrapper<ArchivesRelationInfo>().in("id", newFaceIds));
        if (CollectionUtils.isNotEmpty(archivesRelationInfoList)) {
            List<String> rBodyIds = new ArrayList<>();
            for (ArchivesRelationInfo archivesRelationInfo : archivesRelationInfoList) {
                rBodyIds.add(archivesRelationInfo.getBodyId());
            }
            if (CollectionUtils.isNotEmpty(rBodyIds)) {
                updateEsBody(rBodyIds, aid, ArchivesConstant.TRACE_SOURCE_FACE);
            }
            archivesRelationInfoService.remove(new QueryWrapper<ArchivesRelationInfo>().in("body_id", rBodyIds));
        }
    }

    private void updateEsFaceBatch(List<String> ids, String archivesId) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for (String faceId : ids) {
            JSONObject subJsonObject = new JSONObject();
            subJsonObject.put("FaceID", faceId);
            JSONObject subJsonObject2 = new JSONObject();
            subJsonObject2.put("ArchivesID", archivesId);
            subJsonObject2.put("TrailSource", ArchivesConstant.TRACE_SOURCE_FACE);
            subJsonObject.put("Data", subJsonObject2);
            jsonArray.add(subJsonObject);
        }
        jsonObject1.put("FaceObject", jsonArray);
        jsonObject.put("FaceListObject", jsonObject1);
        microSearchFeign.updateFaces(jsonObject);
    }

    @Override
    public void updateEsBody(List<String> ids, String archivesId, Integer tsType) {
        if (ids.size() <= ES_LENGTH) {
            updateEsBodyBatch(ids, archivesId, tsType);
        } else {
            for (int i = 0; i < ids.size() + 1; ) {
                if (i + ES_LENGTH >= ids.size()) {
                    updateEsBodyBatch(ids.subList(i, ids.size()), archivesId, tsType);
                    return;
                }
                updateEsBodyBatch(ids.subList(i, i + ES_LENGTH), archivesId, tsType);
                i = i + ES_LENGTH;
            }
        }
    }

    public void updateEsBodyBatch(List<String> ids, String archivesId, Integer tsType) {

        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (String personId : ids) {
            JSONObject subJsonObject = new JSONObject();
            subJsonObject.put("PersonID", personId);
            JSONObject subJsonObject2 = new JSONObject();
            subJsonObject2.put("ArchivesID", archivesId);
            subJsonObject2.put("TrailSource", tsType);
            subJsonObject.put("Data", subJsonObject2);
            jsonArray.add(subJsonObject);
        }
        jsonObject1.put("PersonObject", jsonArray);
        jsonObject.put("PersonListObject", jsonObject1);
        microSearchFeign.updateBodys(jsonObject);
    }

    private void initCameraRelation() {
        List<CameraRelationInfo> cameraRelationInfoList = cameraRelationInfoService.list();
        if (CollectionUtils.isEmpty(cameraRelationInfoList)) {
            List<TbAnalysisTask> tbAnalysisTaskList = tbAnalysisTaskService.list(
                    new QueryWrapper<TbAnalysisTask>().groupBy("camera_id"));
            HashSet<String> dId = new HashSet<>();
            for (TbAnalysisTask tbAnalysisTask : tbAnalysisTaskList) {
                dId.add(tbAnalysisTask.getCameraId());
            }
            CAMERA_RELATION_LIST.clear();
            CAMERA_RELATION_LIST.put(IDUtil.uuid(), dId);
        }
        for (CameraRelationInfo cameraRelationInfo : cameraRelationInfoList) {
            if (CAMERA_RELATION_LIST.containsKey(cameraRelationInfo.getId())) {
                CAMERA_RELATION_LIST.get(cameraRelationInfo.getId()).add(cameraRelationInfo.getDeviceId());
            } else {
                HashSet<String> dId = new HashSet<>();
                dId.add(cameraRelationInfo.getDeviceId());
                CAMERA_RELATION_LIST.put(cameraRelationInfo.getId(), dId);
            }
        }
    }

    @Override
    public void updEsRelationByBody() {
        List<ArchivesRelationInfo> archivesRelationInfoList = archivesRelationInfoService.list();
        Map<String, HashSet<String>> relationMap = new HashMap<>();
        for (ArchivesRelationInfo archivesRelationInfo : archivesRelationInfoList) {
            if (StringUtils.isBlank(archivesRelationInfo.getArchivesId()) || StringUtils.isBlank(archivesRelationInfo.getBodyId())) {
                continue;
            }
            if (relationMap.containsKey(archivesRelationInfo.getArchivesId())) {
                relationMap.get(archivesRelationInfo.getArchivesId()).add(archivesRelationInfo.getBodyId());
            } else {
                HashSet<String> dId = new HashSet<>();
                dId.add(archivesRelationInfo.getBodyId());
                relationMap.put(archivesRelationInfo.getArchivesId(), dId);
            }
        }
        if (!relationMap.isEmpty()) {
            for (Entry<String, HashSet<String>> relation : relationMap.entrySet()) {
                if (CollectionUtils.isNotEmpty(relation.getValue())) {
                    updateEsBody(new ArrayList<>(relation.getValue()), relation.getKey(), ArchivesConstant.TRACE_SOURCE_FACE);
                    archivesRelationInfoService.remove(new QueryWrapper<ArchivesRelationInfo>().in("body_id", new ArrayList<>(relation.getValue())));
                }
            }
        }
    }


    @Override
    public void startBodyClust() {
        try {
            initCameraRelation();
            for (String relationId : CAMERA_RELATION_LIST.keySet()) {
                List<List<ReidRecord>> clusterList = ReidFeatureClusterSpringRedis.cluster(bodyRedisTemplate,
                        new ArrayList<>(CAMERA_RELATION_LIST.get(relationId)),
                        DbPropUtil.getDouble("body.feature.cluster", 0.9d), nacosConfig.getAlgoBodyCluster());
                for (List<ReidRecord> reidRecordList : clusterList) {
                    Map<String, Object> map = initPersonParams(reidRecordList, true);
                    dealPersonEsInfo(map, reidRecordList);
                }
            }
        } catch (Exception e) {
            log.error("startBodyClust", e);
        }
    }

    /***
     * @description: isSelectAid true条件有aid false条件无aid
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     */
    private Map<String, Object> initPersonParams(List<ReidRecord> reidRecordList, boolean isSelectAid) {
        String type = "Person";
        StringBuilder personSbr = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        for (ReidRecord reidRecord : reidRecordList) {
            personSbr.append(",");
            personSbr.append(reidRecord.getId());
        }
        map.put(type + ".PersonID.In", personSbr.substring(1));
        map.put(type + ".PageRecordNum", reidRecordList.size() + 1);
        map.put(type + ".RecordStartNo", 1);
        if (isSelectAid) {

            map.put(type + ".ArchivesID", "NotNull");
        } else {
            map.put(type + ".ArchivesID", "Null");
        }
        return map;
    }

    /***
     * @description: 处理人形聚类数据，如果有aid，取第一个更新无aid的数据，均无则跳出
     * @param map
     * @param reidRecordList
     * @return: boolean
     */
    private boolean dealPersonEsInfo(Map<String, Object> map, List<ReidRecord> reidRecordList) {
        String respContent = microSearchFeign.getBodys(map);
        JSONArray bodyJsonArray = JSONObject.parseObject(respContent).getJSONObject("PersonListObject").getJSONArray("PersonObject");
        if (bodyJsonArray.isEmpty()) {
            return false;
        } else {
            JSONObject jsonObject = bodyJsonArray.getJSONObject(0);
            String archivesId = jsonObject.getString("ArchivesID");
            if (StringUtils.isNotBlank(archivesId)) {
                map.clear();
                map = initPersonParams(reidRecordList, false);
                String content = microSearchFeign.getBodys(map);
                JSONArray jsonArray = JSONObject.parseObject(content).getJSONObject("PersonListObject").getJSONArray("PersonObject");
                List<String> newbodyList = new ArrayList<>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject bodyJsonObject = jsonArray.getJSONObject(i);
                    newbodyList.add(bodyJsonObject.getString("PersonID"));
                }
                if (CollectionUtils.isNotEmpty(newbodyList)) {
                    updateEsBody(newbodyList, archivesId, ArchivesConstant.TRACE_SOURCE_BODY);
                }
            }
            return true;
        }
    }

    @Override
    public void addArchivesBodyInfo(Float bodyQuality, Integer angle, String archivesId, String bodyImgUrl, String bodyFeature) {
        if (StringUtils.isBlank(bodyFeature)) {
            Var var = BodyConstant.getBodySdkInvoke().getPicAnalyzeOne(BodyConstant.BODY_TYPE, bodyImgUrl);
            bodyFeature = var.getString("featureVector");
        }
        if (bodyQuality != null && bodyQuality >= DbPropUtil.getFloat("body.archives.quality.threshold", 0.7f)) {

            ArchivesBodyInfo archivesBodyInfo = archivesBodyInfoService.getOne(new QueryWrapper<ArchivesBodyInfo>()
                    .eq("angle", angle)
                    .eq("archives_id", archivesId));
            if (archivesBodyInfo == null) {
                String bodyFeatureId = IDUtil.uuid();
                BodyConstant.getBodySdkInvoke().addBodyToLib(archivesInfoService.findBodyLibByAngle(angle), bodyFeatureId, BodyConstant.BODY_TYPE,
                        bodyFeature);
                ArchivesBodyInfo bodyInfo = new ArchivesBodyInfo(IDUtil.uuid(), bodyImgUrl, String.valueOf(angle),
                        bodyFeatureId, archivesId);
                archivesBodyInfoService.saveOrUpdate(bodyInfo);
            }
        }
    }

}
