package com.keensense.alarm.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.keensense.alarm.dto.DispositionNotification;
import com.keensense.alarm.entity.DispositionEntity;
import com.keensense.alarm.entity.DispositionNotificationEntity;
import com.keensense.alarm.mapper.DispositionMapper;
import com.keensense.alarm.mapper.DispositionNotificationMapper;
import com.keensense.alarm.service.IDispositionNotificationService;
import com.keensense.common.config.NacosConfigCenter;
import com.keensense.common.constant.AlarmConstant;
import com.keensense.common.util.*;
import com.keensense.sdk.algorithm.IFaceSdkInvoke;
import com.keensense.sdk.constants.FaceConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ycl
 * @since 2019-05-14
 */
@Service
@Slf4j
public class DispositionNotificationServiceImpl extends ServiceImpl<DispositionNotificationMapper, DispositionNotificationEntity> implements IDispositionNotificationService {

    @Resource
    private DispositionMapper dispositionMapper;

    @Resource
    private NacosConfigCenter configCenter;

    private static final String FEATURE_KEY = "featureVector";
    private static final String DEVICE_ID_KEY = "DeviceID";
    private static final String DATE_FORMATTER = "yyyyMMddHHmmss";


    /**
     * 生成告警信息
     */
    @Override
    public List<DispositionNotificationEntity> generateNotifications(String msg) {
        return null;
    }


    @Override
    public List<DispositionNotificationEntity> generateNotifications(List<String> msgs) {
        List<DispositionNotificationEntity> alarms = Lists.newArrayList();
        List<JSONObject> faceNotis = new ArrayList<>();
        List<JSONObject> vehicleNotis = new ArrayList<>();

        for (String msg : msgs) {
            if (msg.contains("FaceObject")) {
                faceNotis.add(JSON.parseObject(msg).getJSONObject("FaceObject"));
            } else if (msg.contains("NonMotorVehicleObject")) {
                //非机动车布控
            } else if (msg.contains("MotorVehicleObject")) {
                vehicleNotis.add(JSON.parseObject(msg).getJSONObject("MotorVehicleObject"));
            }
        }
        //对人脸告警
        alarms.addAll(generateFaceAlarm(faceNotis));
        //对车辆告警
        alarms.addAll(generateVehicleAlarm(vehicleNotis));

        return alarms;
    }

    /**
     * 车牌告警
     */
    private List<DispositionNotificationEntity> generateVehicleAlarm(List<JSONObject> vehicleNotis) {
        List<DispositionNotificationEntity> alarms = Lists.newArrayList();
        for (JSONObject vehicleNoti : vehicleNotis) {
            List<DispositionEntity> dispositions = DispositionUtil
                    .list(vehicleNoti.getString(DEVICE_ID_KEY), AlarmConstant.MOTOR_VEHICLE);
            for (DispositionEntity disposition : dispositions) {
                if (disposition.getDispositionCategory() == 2 && StringUtils
                        .isNotEmpty(disposition.getLicensePlate())) {
                    boolean isHit = isVehicleAlarmHit(vehicleNoti.getString("PlateNo"),
                            disposition.getLicensePlate());
                    if (isHit) {
                        DispositionNotificationEntity entity = generateAlarm("MotorVehicleObject", vehicleNoti,
                                disposition, 100f, null);
                        alarms.add(entity);
                    }
                }
            }

        }
        return alarms;
    }


    private boolean isVehicleAlarmHit(String plateNo, String licensePlate) {
        for (int i = 0; i < licensePlate.length(); i++) {
            char target = licensePlate.charAt(i);
            if (StringUtils.isEmpty(plateNo) || plateNo.length() != licensePlate.length()) {
                return false;
            }
            char data = plateNo.charAt(i);
            if (target != '_' && data != target) {
                return false;
            }
        }
        return true;
    }

    /**
     * 人脸告警
     */
    private List<DispositionNotificationEntity> generateFaceAlarm(List<JSONObject> faceNotis) {
        List<DispositionNotificationEntity> alarms = Lists.newArrayList();
        if (faceNotis.isEmpty()) {
            return alarms;
        }

        //库布控批量检索
        List<DispositionEntity> libDispositions = DispositionUtil
                .list(true, 1, 3);
        for (DispositionEntity disp : libDispositions) {
            List<JSONObject> faces = faceNotis.stream()
                    .filter(face -> {
                        if (StringUtils.isEmpty(disp.getTollgateList())) {
                            return true;
                        }
                        return disp.getTollgateList().contains(face.getString(DEVICE_ID_KEY));
                    })
                    .collect(Collectors.toList());
            if (!faces.isEmpty()) {
                alarms.addAll(matchSearch(faces, disp));
            }
        }
        //图片布控
        List<DispositionEntity> dispositions = DispositionUtil
                .list(false, 1, 3);

        IFaceSdkInvoke faceSdkInvoke = FaceConstant.getFaceSdkInvoke();
        for (DispositionEntity disp : dispositions) {
            String dispFeature = disp.getTargetImageFeature();
            if (!configCenter.getFirm().equals(disp.getFirm()) || StringUtils
                    .isEmpty(dispFeature)) {
                dispFeature = (String) Optional
                        .ofNullable(faceSdkInvoke.getPicAnalyzeOne(disp.getTargetImageUri()))
                        .map(var -> var.get(FEATURE_KEY)).orElse("");
                if (StringUtils.isNotEmpty(dispFeature)) {
                    disp.setFirm(configCenter.getFirm());
                    disp.setTargetImageFeature(dispFeature);
                    dispositionMapper.updateById(disp);
                } else {
                    continue;
                }
            }

            for (JSONObject face : faceNotis) {
                if (StringUtils.isEmpty(face.getString("FeatureObject"))) {
                    continue;
                }
                Float score = faceSdkInvoke
                        .compareFeature(face.getString("FeatureObject"), dispFeature);
                if (score >= disp.getScoreThreshold()
                        && disp.getControlType() != AlarmConstant.CONTROL_STRANGER) {

                    alarms.add(generateAlarm("FaceObject", face, disp, score, null));

                } else if (score < disp.getScoreThreshold()
                        && disp.getControlType() == AlarmConstant.CONTROL_STRANGER) {

                    alarms.add(generateAlarm("FaceObject", face, disp, 0f, null));
                }
            }

        }
        return alarms;

    }

    /**
     * 静态库multiSearch
     */
    private List<DispositionNotificationEntity> matchSearch(List<JSONObject> faces,
                                                            DispositionEntity disp) {
        List<DispositionNotificationEntity> alarms = Lists.newArrayList();
        List<String> hitNotiIds = Lists.newArrayList();
        Map<String,Object> params = new HashMap<>();
        params.put("type", 3);
        params.put("firm", configCenter.getFirm());
        params.put("threshold", disp.getScoreThreshold() / 100.0f);

        Map<String, JSONObject> notiMap = Maps.newHashMap();
        params.put("repo", disp.getRegIds().split(","));
        List<JSONObject> featureFaces = faces.stream()
                .filter(face -> StringUtils.isNotEmpty(face.getString("FeatureObject")))
                .collect(Collectors.toList());
        if (featureFaces.isEmpty()) {
            return alarms;
        }
        for (int i = 0; i < featureFaces.size(); i++) {
            JSONObject face = featureFaces.get(i);
            notiMap.put(face.getString("Id"), face);
            params.put("candidates[" + i + "].uuid", face.getString("Id"));
            params.put("candidates[" + i + "].feat", face.getString("FeatureObject"));
        }
        String resp = "";
        String url = "http://" + configCenter.getFeatureExtractUrl().split(":")[0] + ":39082/match";
        resp = HttpClientUtil
                .requestPost(url, "application/json;charset=utf-8", params.toString());
        JSONObject jsonObject = JSON.parseObject(resp);
        JSONArray results = jsonObject.getJSONArray("results");
        for (int i = 0; i < results.size(); i++) {
            JSONArray matches = results.getJSONObject(i).getJSONArray("matches");
            for (int j = 0; j < matches.size(); j++) {
                String uuid = matches.getJSONObject(j).getString("uuid");
                String target = matches.getJSONObject(j).getString("target");
                String score = matches.getJSONObject(j).getString("score");
                hitNotiIds.add(uuid);
                JSONObject hitNoti = notiMap.get(uuid);
                alarms.add(generateAlarm("FaceObject", hitNoti, disp, Float.valueOf(score) * 100.0f, target));
            }

        }
        //白名单布控
        if (disp.getControlType() == AlarmConstant.CONTROL_STRANGER) {
            alarms.clear();
            List<JSONObject> alarmNotis = featureFaces.stream()
                    .filter(face -> !hitNotiIds.contains(face.getString("Id")))
                    .collect(Collectors.toList());
            for (JSONObject alarmNoti : alarmNotis) {
                alarms.add(generateAlarm("FaceObject", alarmNoti, disp, 0f, null));
            }
        }
        return alarms;
    }


    private DispositionNotificationEntity generateAlarm(String obj, JSONObject jsonObject,
                                                        DispositionEntity disposition, Float score, String faceId) {
        JSONObject cntObj = new JSONObject();
        cntObj.put(obj, jsonObject);
        return new DispositionNotificationEntity()
                .setLicensePlate(jsonObject.getString("PlateNo"))
                .setDispositionId(disposition.getDispositionId())
                .setNotificationId(IDUtil.randomUUID())
                .setCntObjectId(jsonObject.getString("Id"))
                .setCntObject(cntObj.toJSONString())
                .setControlType(disposition.getControlType())
                .setDispositionCategory(disposition.getDispositionCategory())
                .setTitle(disposition.getTitle())
                .setFaceId(faceId)
                .setScore(score)
//        .setTriggerTime(LocalDateTime.now());
                // 告警时间，显示目标出现时间，不是当前时间。因为离线视频校准时间可以随便填！！！2020-07-01
                .setTriggerTime(DateUtil.parseDate(jsonObject.getString("AppearTime"), DATE_FORMATTER)
                        .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

    }


    @Override
    public List<DispositionNotification> findDispositionByParam(String param) {
        //解析url参数
        List<ParamProcessUtil.QueryParam> queryList = ParamProcessUtil.getQueryList(param);

        boolean isOrderBy = false;
        boolean isGroupBy = false;
        for (int i = queryList.size() - 1; i >= 0; i--) {
            if ("plate_count".equals(queryList.get(i).getValue())) {
                queryList.remove(i);
                isOrderBy = true;
            }
            String column = queryList.get(i).getColumn();
            if (StringUtils.isNotBlank(column) && column.equals("group_by_flag")) {
                String queryflag = queryList.get(i).getValue();
                if (StringUtils.isNotBlank(queryflag) && queryflag.equals("1")) {
                    isGroupBy = true;
                    queryList.remove(i);
                }
            }
        }

        Page<DispositionNotificationEntity> queryPage = ParamProcessUtil.getQueryPage(queryList);
        QueryWrapper<DispositionNotificationEntity> wrapper = ParamProcessUtil.getWrapper(queryList);
        if (isGroupBy) {
            wrapper = wrapper.groupBy("license_plate", "disposition_id");
        }
        if (isOrderBy) {
//            wrapper = wrapper.orderByDesc("count(1)");
            wrapper = wrapper.last("ORDER BY count(1) desc");
        }
        //查询告警主表
        queryPage.setSearchCount(false);
        List<Integer> totals = baseMapper.selectNotisCount(wrapper);
        IPage<DispositionNotificationEntity> iPage = baseMapper.selectPage(queryPage, wrapper);
        if (CollectionUtils.isNotEmpty(totals)) {
            iPage.setTotal(isGroupBy ? totals.size() : totals.get(0));
        }
        List<DispositionNotificationEntity> records = iPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return Collections.emptyList();
        }
        Messenger.sendMsg(String.valueOf(iPage.getTotal()));

        List<DispositionNotification> dispNotis = new ArrayList<>();
        for (DispositionNotificationEntity entity : records) {
            DispositionNotification dispNoti = new DispositionNotification();
            BeanUtils.copyProperties(entity, dispNoti);

            if (StringUtils.isNotEmpty(entity.getLicensePlate())) {
                IPage<DispositionNotificationEntity> notificationPage = baseMapper.selectPage(new Page<>(0, 1),
                        Wrappers.<DispositionNotificationEntity>lambdaQuery()
                                .eq(DispositionNotificationEntity::getLicensePlate, entity.getLicensePlate())
                                .eq(DispositionNotificationEntity::getDispositionId, entity.getDispositionId()));
                dispNoti.setPlateCount(notificationPage.getTotal());
            }
            dispNotis.add(dispNoti);
        }
        if (isOrderBy) {
            dispNotis = dispNotis.stream()
                    .sorted(Comparator.comparing(DispositionNotification::getPlateCount).reversed())
                    .collect(Collectors.toList());
        }

        return dispNotis;
    }

    @Override
    public boolean addDispositionNotifications(List<DispositionNotification> dtos) {
        List<DispositionNotificationEntity> dispNotis = new ArrayList<>();
        for (DispositionNotification dn : dtos) {
            DispositionNotificationEntity dispNoti = new DispositionNotificationEntity();
            BeanUtils.copyProperties(dn, dispNoti);
            if (StringUtils.isEmpty(dispNoti.getNotificationId())) {
                dispNoti.setNotificationId(IDUtil.randomUUID());
            }
            dispNotis.add(dispNoti);
        }
        return this.saveBatch(dispNotis);
    }
}
