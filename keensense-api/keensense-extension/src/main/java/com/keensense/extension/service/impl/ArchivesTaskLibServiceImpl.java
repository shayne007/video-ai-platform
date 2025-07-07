package com.keensense.extension.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.extension.constants.LibraryConstant;
import com.keensense.extension.entity.ArchivesTaskLib;
import com.keensense.extension.mapper.ArchivesTaskLibMapper;
import com.keensense.extension.service.IArchivesTaskLibService;
import com.keensense.sdk.algorithm.IFaceSdkInvoke;
import com.keensense.sdk.algorithm.impl.GLQstFaceSdkInvokeImpl;
import com.keensense.sdk.algorithm.impl.GlstFaceSdkInvokeImpl;
import com.keensense.sdk.algorithm.impl.KsFaceSdkInvokeImpl;
import com.keensense.sdk.algorithm.impl.StQstFaceSdkInvokeImpl;
import com.keensense.sdk.constants.FaceConstant;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.datastruct.WeekArray;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * <p>
 * 任务时间库对应 服务实现类
 * </p>
 *
 * @author ycl
 * @since 2019-06-08
 */
@Service
public class ArchivesTaskLibServiceImpl extends ServiceImpl<ArchivesTaskLibMapper, ArchivesTaskLib> implements IArchivesTaskLibService {

    private static final String MAX_TIME = "210011220808";
    private static final String MIN_TIME = "199011220808";
    private static final String FEATURE_KEY = "Feature";

    @Override
    public String enrollFeature(JSONObject featureObject) {
        IFaceSdkInvoke faceSdk = FaceConstant.getFaceSdkInvoke();
        String taskId = featureObject.getString("TaskID");
        if (StringUtils.isEmpty(taskId)) {
            return "";
        }
        if (faceSdk instanceof KsFaceSdkInvokeImpl) {
            String libId = getLibId(faceSdk, taskId, LibraryConstant.KS_TYPE);
            if (StringUtils.isEmpty(libId)) {
                return "";
            }
            return faceSdk.addFaceToLib(libId, featureObject.getString(FEATURE_KEY), "", featureObject.getString("Time"));
        } else if (faceSdk instanceof GlstFaceSdkInvokeImpl) {
            //glst时间范围库为定时任务创建
            LocalDateTime now = LocalDateTime.now();
            List<ArchivesTaskLib> taskLibs = this.list(new LambdaQueryWrapper<ArchivesTaskLib>()
                    .le(ArchivesTaskLib::getIntervalBeginTime, now)
                    .ge(ArchivesTaskLib::getIntervalEndTime, now)
                    .eq(ArchivesTaskLib::getLibType, LibraryConstant.GL_TYPE));
            if (CollectionUtils.isNotEmpty(taskLibs)) {
                return faceSdk.addFaceToLib(taskLibs.get(0).getLibId(), featureObject.getString(FEATURE_KEY), "");
            }
        } else if (faceSdk instanceof GLQstFaceSdkInvokeImpl || faceSdk instanceof StQstFaceSdkInvokeImpl) {
            String libId = getLibId(faceSdk, taskId,
                    faceSdk instanceof StQstFaceSdkInvokeImpl?LibraryConstant.STQST_TYPE:LibraryConstant.GLQST_TYPE);
            if (StringUtils.isEmpty(libId)) {
                return "";
            }
            return faceSdk.addFaceToLib(libId, featureObject.getString(FEATURE_KEY), "");

        }
        return "";
    }

    private String getLibId(IFaceSdkInvoke faceSdk, String taskId, int type) {
        List<ArchivesTaskLib> taskLibs = this.list(new LambdaQueryWrapper<ArchivesTaskLib>()
                .eq(ArchivesTaskLib::getTaskId, taskId)
                .eq(ArchivesTaskLib::getLibType, type));
        String libId;
        if (CollectionUtils.isEmpty(taskLibs)) {
            //创建对应算法库
            libId = faceSdk.createRegistLib();
            if (StringUtils.isEmpty(libId)) {
                return "";
            }
            ArchivesTaskLib archivesTaskLib = new ArchivesTaskLib();
            archivesTaskLib.setTaskId(taskId).setLibId(libId).setLibType(type);
            this.save(archivesTaskLib);
        } else {
            libId = taskLibs.get(0).getLibId();
        }
        return libId;
    }

    @Override
    public JSONArray searchFeature(JSONObject searchParam) {
        JSONArray taskList = searchParam.getJSONArray("TaskList");
        Float threshold = searchParam.getFloat("Threshold");
        IFaceSdkInvoke faceSdk = FaceConstant.getFaceSdkInvoke();
        JSONArray ja = new JSONArray();
        List<ArchivesTaskLib> taskLibs = null;
        if (faceSdk instanceof KsFaceSdkInvokeImpl && CollectionUtils.isNotEmpty(taskList)) {
            Object[] ids = taskList.stream().map(obj -> (String) ((Map) obj).get("id")).toArray();
            taskLibs = this.list(new LambdaQueryWrapper<ArchivesTaskLib>()
                    .in(ArchivesTaskLib::getTaskId, ids)
                    .eq(ArchivesTaskLib::getLibType, LibraryConstant.KS_TYPE));

        } else if (faceSdk instanceof GlstFaceSdkInvokeImpl) {
            String begin = searchParam.getString("StartTime");
            String end = searchParam.getString("EndTime");
            begin = StringUtils.isEmpty(begin) ? MIN_TIME : begin;
            end = StringUtils.isEmpty(end) ? MAX_TIME : end;
            taskLibs = this.list(new LambdaQueryWrapper<ArchivesTaskLib>()
                    .ge(ArchivesTaskLib::getIntervalEndTime, begin)
                    .le(ArchivesTaskLib::getIntervalBeginTime, end)
                    .eq(ArchivesTaskLib::getLibType, LibraryConstant.GL_TYPE));
        } else if (faceSdk instanceof GLQstFaceSdkInvokeImpl || faceSdk instanceof StQstFaceSdkInvokeImpl) {
            Object[] ids = taskList.stream().map(obj -> (String) ((Map) obj).get("id")).toArray();
            taskLibs = this.list(new LambdaQueryWrapper<ArchivesTaskLib>()
                    .in(ArchivesTaskLib::getTaskId, ids)
                    .eq(ArchivesTaskLib::getLibType,
                            faceSdk instanceof StQstFaceSdkInvokeImpl ? LibraryConstant.STQST_TYPE : LibraryConstant.GLQST_TYPE));
        }
        if (CollectionUtils.isEmpty(taskLibs)) {
            return ja;
        }
        String libIds = taskLibs.stream().map(ArchivesTaskLib::getLibId).collect(Collectors.joining(","));
        WeekArray searchData = (WeekArray) faceSdk.getSimilars(libIds,
                searchParam.getString(FEATURE_KEY),
                getThreshold(threshold),
                searchParam.getIntValue("LimitNum"),
                searchParam.getString("StartTime"),
                searchParam.getString("EndTime"));

        if (searchData != null) {
            for (int i = 0; i < searchData.getSize(); i++) {
                Var tempVar = searchData.get("[" + i + "]");
                JSONObject jo = new JSONObject();
                jo.put("uuid", tempVar.getString("face.id"));
                jo.put("score", tempVar.getString("score"));
                jo.put("task", tempVar.getString("face.faceGroupId"));
                String createTime = tempVar.getString("face.createTime");
                jo.put("date", transformTime(createTime));
                ja.add(jo);
            }
        }
        return ja;
    }

    private float getThreshold(Float threshold) {
        return (threshold != null && Float.floatToRawIntBits(threshold) != 0) ? threshold * 100 : 1f;
    }


    private String transformTime(String source) {
        if (StringUtils.isEmpty(source)) {
            return "";
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date parseTime = sdf1.parse(source);
            return com.keensense.common.util.DateUtil.formatDate(parseTime, "yyyyMMddHHmmss");
        } catch (ParseException e) {
            log.error("transformTime error", e);
        }
        return "";
    }
}
