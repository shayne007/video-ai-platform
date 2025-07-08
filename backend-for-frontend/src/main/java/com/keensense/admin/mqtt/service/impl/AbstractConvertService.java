package com.keensense.admin.mqtt.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.admin.entity.task.Camera;
import com.keensense.admin.entity.task.CtrlUnit;
import com.keensense.admin.entity.task.VsdTaskRelation;
import com.keensense.admin.mapper.task.CameraMapper;
import com.keensense.admin.mapper.task.CtrlUnitMapper;
import com.keensense.admin.mqtt.domain.Community;
import com.keensense.admin.mqtt.domain.FaceResult;
import com.keensense.admin.mqtt.domain.MessageSource;
import com.keensense.admin.mqtt.domain.NonMotorVehiclesResult;
import com.keensense.admin.mqtt.domain.PersonResult;
import com.keensense.admin.mqtt.domain.Result;
import com.keensense.admin.mqtt.domain.VlprResult;
import com.keensense.admin.mqtt.enums.ResultEnums;
import com.keensense.admin.mqtt.service.ConvertService;
import com.keensense.admin.service.task.IVsdTaskRelationService;
import com.keensense.admin.util.DateTimeUtils;
import com.keensense.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public abstract class AbstractConvertService implements ConvertService {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RedisTemplate countRedisTemplate;

    @Autowired
    private CameraMapper cameraMapper;

    @Resource
    private CtrlUnitMapper ctrlUnitMapper;

    @Resource
    private IVsdTaskRelationService vsdTaskRelationService;

    private static final Long OVER_TIME = 60L * 60L * 24L;

    private static final Long KAKOU_TIME = 60L * 60L * 1L;

    /**
     * 存储公共数据
     */
    @SuppressWarnings("all")
    protected void setCommonProperties(JSONObject jsonObject, Result result, String serialnumberName, String faceUrl) {
        // uuid
        //result.setId(String.valueOf(jsonObject.get("uuid")));

        // 数据类型
        result.setObjType(Integer.parseInt(String.valueOf(jsonObject.get("objType"))));

        // objId
        result.setObjId(Integer.parseInt(String.valueOf(jsonObject.get("objId"))));

        String serialnumber = String.valueOf(jsonObject.get("serialNumber"));

        // 子任务序列号
        result.setAnalysisId(serialnumber);

        JSONObject snapshot = (JSONObject) jsonObject.get("snapshot");

        // 处理cameraId 和 pts
        // 任务号
        Map<String, Object> map = (Map<String, Object>) redisTemplate.opsForValue().get(serialnumberName + "+" + serialnumber);
        if (Objects.isNull(map) || map.size() == 0) {
            // 从数据库查询
            //log.info(serialnumberName + "任务号:{}未从缓存中查询到，将查询数据库",serialnumber);

            VsdTaskRelation taskRelation = vsdTaskRelationService.getOne(new QueryWrapper<VsdTaskRelation>().eq("serialnumber", serialnumber));
            map = new HashMap<>();
            if (taskRelation != null) {
                map.put("userserialnumber", taskRelation.getSerialnumber());
                map.put("camera_id", taskRelation.getCameraId());
                map.put("task_type", getTaskType(taskRelation.getFromType()));
                map.put("entrytime", taskRelation.getEntryTime());
            }
            if (Objects.isNull(map) || map.size() == 0) {
                throw new RuntimeException("任务号:" + serialnumber + "未查询到任何结果");
            }

            // 存入缓存
            redisTemplate.opsForValue().set(serialnumberName + "+" + serialnumber, map, AbstractConvertService.OVER_TIME, TimeUnit.SECONDS);
        }

        // 将任务号存入缓存
        synchronized (this) {
            Map countMap = countRedisTemplate.opsForHash().entries(serialnumber);
            if (Objects.isNull(countMap) || countMap.size() == 0) {
                countRedisTemplate.opsForHash().put(serialnumber, ResultEnums.PERSON_COUNT.getValue(), 0);
                countRedisTemplate.opsForHash().put(serialnumber, ResultEnums.FACE_COUNT.getValue(), 0);
                countRedisTemplate.opsForHash().put(serialnumber, ResultEnums.CAR_COUNT.getValue(), 0);
                countRedisTemplate.opsForHash().put(serialnumber, ResultEnums.BIKE_COUNT.getValue(), 0);
            }
        }

        // 主任务序列号
        result.setSerialnumber(String.valueOf(map.get("userserialnumber")));

        // cameraId
        String cameraId = String.valueOf(map.get("camera_id"));
        if (StringUtils.isEmpty(cameraId)) {
            cameraId = "1";
        }
        result.setDeviceId(cameraId);

        //同步点位,区域字段
        Map<String, Object> resultMap = (Map<String, Object>) redisTemplate.opsForValue().get("kakou6_" + cameraId);
        if (Objects.isNull(resultMap) || resultMap.size() == 0) {
            resultMap = new HashMap<>();
            Camera camera = cameraMapper.selectById(cameraId);
            if (camera != null) {
                CtrlUnit ctrlUnit = ctrlUnitMapper.selectOne(new QueryWrapper<CtrlUnit>().eq("unit_identity", camera.getRegion()));
                if (ctrlUnit != null) {
                    Community community = new Community();
                    community.setId(ctrlUnit.getId().toString());
                    community.setName(ctrlUnit.getUnitName());
                    resultMap.put("community", JSONObject.toJSONString(community));
                }
                MessageSource messageSource = new MessageSource();
                messageSource.setEquipmentId(camera.getId().toString());
                messageSource.setEquipmentName(camera.getName());
                messageSource.setLat(camera.getLatitude());
                messageSource.setLng(camera.getLongitude());
                resultMap.put("messageSource", JSONObject.toJSONString(messageSource));
                // 存入缓存
                redisTemplate.opsForValue().set("kakou6_" + cameraId, resultMap, AbstractConvertService.KAKOU_TIME, TimeUnit.SECONDS);
            }

        }
        String communityStr = resultMap.get("community").toString();
        Community community = JSONObject.parseObject(communityStr, Community.class);
        result.setCommunity(community);
        MessageSource messageSource = JSONObject.parseObject(resultMap.get("messageSource").toString(), MessageSource.class);
        result.setMessageSource(messageSource);
        result.setSequenceNo(UUID.randomUUID().toString().replaceAll("-", ""));
        result.setPackageNo(UUID.randomUUID().toString().replaceAll("-", ""));
        result.setMessagetTime(new Date());
        Integer objType = result.getObjType();
        result.setMessageKind(getMessageKind(objType));//获取消息类型
        // pts
        Object framePtsObj = snapshot.get("framePts");
        Object startFramePtsObj = jsonObject.get("startFramePts");
        // 返回 startFramePts
        result.setStartFramePts(String.valueOf(startFramePtsObj));
        Object endFramePtsObj = jsonObject.get("endFramePts");

        Date date = null;
        Integer frameIdx = Integer.parseInt(String.valueOf(snapshot.get("frameIdx")));
        result.setFrameIdx(frameIdx);

        Integer taskType = Integer.parseInt(String.valueOf(map.get("task_type")));
        if (taskType != 1) {
            // taskType不为1 非实时任务  pts将是个int
            Integer framePts = Integer.parseInt(String.valueOf(framePtsObj));
            Integer startFramePts = Integer.parseInt(String.valueOf(startFramePtsObj));
            Integer endFramePts = Integer.parseInt(String.valueOf(endFramePtsObj));

            Object entrytimeObject = map.get("entrytime");
            Date entrytime = null;
            if (entrytimeObject instanceof Date) {
                entrytime = (Date) entrytimeObject;
            } else if (entrytimeObject instanceof Long) {
                entrytime = new Date((Long) entrytimeObject);
            } else {
                entrytime = DateTimeUtils.getBeginDateTime();
            }

            //date = DateUtil.getCreateTime(entrytime,frameIdx);
            // 采用帧率来校准时间（原来是采用帧数）
            date = new Date(entrytime.getTime() + Long.parseLong(String.valueOf(framePts)));


            if (!Objects.isNull(framePts)) {
                result.setMarkTime(date);
            }

            if (!Objects.isNull(startFramePts)) {
                result.setAppearTime(new Date(entrytime.getTime() + Long.parseLong(String.valueOf(startFramePts))));
            }

            if (!Objects.isNull(endFramePts)) {
                result.setDisappearTime(new Date(entrytime.getTime() + Long.parseLong(String.valueOf(endFramePts))));
            }
        } else {
            // taskType为1  实时任务 pts 将是个long
            String framePtsString = String.valueOf(framePtsObj);
            if ("0".equals(framePtsString)) {
                result.setMarkTime(new Date());
            } else {
                result.setMarkTime(new Date(Long.parseLong(framePtsString)));
            }

            String startFramePtsString = String.valueOf(startFramePtsObj);
            if ("0".equals(startFramePtsString)) {
                result.setAppearTime(new Date());
            } else {
                result.setAppearTime(new Date(Long.parseLong(startFramePtsString)));
            }

            String endFramePtsString = String.valueOf(endFramePtsObj);
            if ("0".equals(endFramePtsString)) {
                result.setDisappearTime(new Date());
            } else {
                result.setDisappearTime(new Date(Long.parseLong(endFramePtsString)));
            }

        }

        // 大图
        result.setBigImgUrl(String.valueOf(jsonObject.get("bigImgURL")));

        // 小图 需要单独处理 faceResult需要存faceUrl

        if (result instanceof FaceResult) {
            result.setImgUrl(faceUrl);
        } else {
            result.setImgUrl(String.valueOf(jsonObject.get("imgURL")));
        }

        // 开始帧序
        result.setStartFrameidx(Integer.parseInt(String.valueOf(jsonObject.get("startFrameIdx"))));

        // 结束帧序
        result.setEndFrameidx(Integer.parseInt(String.valueOf(jsonObject.get("endFrameIdx"))));

        // 创建时间
        result.setCreateTime(DateUtil.parse(String.valueOf(jsonObject.get("createTime"))));

        // 插入时间
        result.setInsertTime(DateUtil.parse(String.valueOf(jsonObject.get("createTime"))));

        //
        result.setFirstObj(Integer.parseInt(String.valueOf(jsonObject.get("firstObj"))));

        // 快照图片宽度
        result.setWidth(String.valueOf(snapshot.get("width")));

        // 快照图片高度
        result.setHeight(String.valueOf(snapshot.get("height")));

        // 目标坐标
        JSONObject boundingBox = (JSONObject) snapshot.get("boundingBox");
        if (!Objects.isNull(boundingBox)) {
            result.setLeftTopX(Integer.parseInt(String.valueOf(boundingBox.get("x"))));
            result.setLeftTopY(Integer.parseInt(String.valueOf(boundingBox.get("y"))));
            result.setRightBtmX(result.getLeftTopX() + Integer.parseInt(String.valueOf(boundingBox.get("w"))));
            result.setRightBtmY(result.getLeftTopY() + Integer.parseInt(String.valueOf(boundingBox.get("h"))));
        }

        JSONObject features = (JSONObject) jsonObject.get("features");

        // 特征类型
        result.setFeatureType(String.valueOf(features.get("featureType")));

        // 特征数据
        // result.setFeatureData(String.valueOf(features.get("featureData")));

        // 人脸质量
        result.setFaceQuality(Float.parseFloat(String.valueOf(features.get("faceQuality"))));

        // 人形质量
        result.setBodyQuality(Float.parseFloat(String.valueOf(features.get("bodyQuality"))));

        JSONObject objectDataAttr = (JSONObject) features.get("objectDataAttr");

        result.setQueryBucketId((Integer) objectDataAttr.get("queryBucketId"));

        // 人脸特征 Bucket ID: -1是没有
        result.setFaceBucketId(String.valueOf(objectDataAttr.get("faceBucketId")));

        result.setAssistSnapshot(((JSONArray) jsonObject.get("assistSnapshot")).toJSONString());

        // 封装SubImageList
        JSONObject subImageList = new JSONObject();
        JSONArray subImageInfoObject = new JSONArray();

        JSONObject bigImageJSONArray = new JSONObject();
        bigImageJSONArray.put("StoragePath", result.getBigImgUrl());

        // 大图Type
        if (result instanceof PersonResult) {
            bigImageJSONArray.put("Type", "10");
        } else if (result instanceof FaceResult) {
            bigImageJSONArray.put("Type", "11");
        } else if (result instanceof VlprResult) {
            bigImageJSONArray.put("Type", "01");
        } else if (result instanceof NonMotorVehiclesResult) {
            bigImageJSONArray.put("Type", "12");
        }

        bigImageJSONArray.put("FileFormat", "Jpeg");

        // 小图Type
        JSONObject imageJSONArray = new JSONObject();

        // 不需要把小图转成base64
        imageJSONArray.put("StoragePath", result.getImgUrl());

        imageJSONArray.put("Type", "100");
        imageJSONArray.put("FileFormat", "Jpeg");

        subImageInfoObject.add(bigImageJSONArray);
        subImageInfoObject.add(imageJSONArray);
        subImageList.put("SubImageInfoObject", subImageInfoObject);

        result.setSubImageList(subImageList.toJSONString());

        // 封装特征数据
        JSONObject featureObject = new JSONObject();
        featureObject.put("BucketID", result.getQueryBucketId());
        featureObject.put("AlgorithmVersion", "2.0");
        featureObject.put("FeatureData", String.valueOf(features.get("featureData")));
        result.setFeatureObject(featureObject.toJSONString());

    }

    //任务类型  1 实时流分析  2 离线文件分析  3 录像分析
    private Integer getTaskType(Integer fromType) {
        switch (fromType) {
            case 3:
                return 1;
            case 4:
                return 1;
            case 5:
                return 3;
            default:
                return fromType;
        }
    }

    //101车辆、  1-人 2-车 4-人骑车
    //102行人
    //104 人骑车
    private String getMessageKind(Integer messageKind) {
        switch (messageKind) {
            case 1:
                return "102";
            case 2:
                return "101";
            case 4:
                return "104";
            default:
                return "102";
        }
    }
}
