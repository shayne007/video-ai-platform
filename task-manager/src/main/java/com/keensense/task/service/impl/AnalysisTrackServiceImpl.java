package com.keensense.task.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.common.util.DateUtil;
import com.keensense.task.async.util.SeqUtil;
import com.keensense.task.entity.CameraAnalysisTrack;
import com.keensense.task.entity.CameraAnalysisTrackBase;
import com.keensense.task.entity.TbAnalysisTask;
import com.keensense.task.mapper.CameraAnalysisTrackHisMapper;
import com.keensense.task.mapper.CameraAnalysisTrackMapper;
import com.keensense.task.mapper.TbAnalysisTaskMapper;
import com.keensense.task.service.IAnalysisTrackService;
import com.keensense.task.util.VideoExceptionUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: AnalysisTrackServiceImpl
 * @Description: IAnalysisTrackService实现类
 * @Author: cuiss
 * @CreateDate: 2020/4/14 15:53
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Service("AnalysisTrackService")
@Slf4j
public class AnalysisTrackServiceImpl implements IAnalysisTrackService {

    @Autowired
    private CameraAnalysisTrackMapper cameraAnalysisTrackMapper;

    @Autowired
    private CameraAnalysisTrackHisMapper cameraAnalysisTrackHisMapper;

    @Autowired
    private TbAnalysisTaskMapper tbAnalysisTaskMapper;

    @Override
    public boolean syncAnalysisTrack(String serialnumber, String cameraId, String analysisTypes, String trackStartTime,
        String trackEndTime) throws Exception {
        log.info(">>>>>>>>>>serialnumber:{},cameraId:{},analysisTypes:{},trackStartTime:{},trackEndTime:{}.....",
            serialnumber, cameraId, analysisTypes, trackStartTime, trackEndTime);
        CameraAnalysisTrack cameraAnalysisTrack = null;
        String[] analysisTypeArray = analysisTypes.split(",");
        String trackDay = trackStartTime.substring(0, 8);
        String trackHour = trackStartTime.substring(8, 10);
        // 当前需要更新的打点信息
        String trackValue = dealTrackValue(trackStartTime, trackEndTime);

        int count = 0;
        if (trackValue != null) {
            log.info(">>>>>>>>>>>>trackValue:{}", trackValue);
            for (String analysisType : analysisTypeArray) {
                cameraAnalysisTrack = new CameraAnalysisTrack();
                cameraAnalysisTrack.setCameraId(cameraId);
                cameraAnalysisTrack.setAnalysisType(Integer.parseInt(analysisType));
                cameraAnalysisTrack.setTrackDay(Integer.parseInt(trackDay));
                cameraAnalysisTrack.setTrackHour(Integer.parseInt(trackHour));
                cameraAnalysisTrack.setTrackValue(trackValue);
                count += syncAnalysisTrack(cameraAnalysisTrack);
            }
            // 假如有数据处理失败，则返回失败，message会加入到重试队列
            if (analysisTypeArray.length != count) {
                log.info(">>>>>>消息处理失败，消息长度：{}，消息入库条数：{}", analysisTypeArray.length, count);
                return false;
            }
        }
        return true;
    }

    /**
     * 根据所传点位id、起始时间、分析类型来查询打点信息 如果起止日期是是同一天，则按小时返回；否则，返回起止日期内的所有信息
     * 
     * @param paramJson
     * @return
     * @throws Exception
     */
    @Override
    public JSONObject getTrackList(JSONObject paramJson) throws Exception {
        String cameraIds = paramJson.getString("cameraIds");
        String analysisTypes = paramJson.getString("analysisTypes");
        String startTime = paramJson.getString("startTime");
        String endTime = paramJson.getString("endTime");
        if (cameraIds == null || analysisTypes == null || startTime == null || endTime == null) {
            log.info(">>>>>>>>>>>>>Exception:cameraIds,analysisTypes,startTime,endTime为必传参数");
            throw VideoExceptionUtil.getValidException("cameraIds,analysisTypes,startTime,endTime为必传参数");
        }
        if (DateUtil
            .getDays(DateUtil.parseDate(startTime, DateUtil.FORMAT_6), DateUtil.parseDate(endTime, DateUtil.FORMAT_6))
            .intValue() > 7) {
            log.info(">>>>>>>>>>>>>Exception:startTime：{}、endTime：{}不能超过7天", startTime, endTime);
            throw VideoExceptionUtil.getValidException("startTime、endTime不超过7天");
        }
        startTime = startTime.replaceAll(":", "").replaceAll("-", "").replaceAll(" ", "");
        endTime = endTime.replaceAll(":", "").replaceAll("-", "").replaceAll(" ", "");
        int startDay = Integer.parseInt(startTime.substring(0, 8));
        int endDay = Integer.parseInt(endTime.substring(0, 8));
        List<CameraAnalysisTrack> cameraAnalysisTracks = null;
        if (startDay == endDay) {
            int startHour = Integer.parseInt(startTime.substring(8, 10));
            int endHour = Integer.parseInt(endTime.substring(8, 10));
            cameraAnalysisTracks = cameraAnalysisTrackMapper.selectList(
                new QueryWrapper<CameraAnalysisTrack>().eq("track_day", startDay).inSql("camera_id", cameraIds)
                    .inSql("analysis_type", analysisTypes).between("track_hour", startHour, endHour));
        } else {
            cameraAnalysisTracks = cameraAnalysisTrackMapper
                .selectList(new QueryWrapper<CameraAnalysisTrack>().inSql("camera_id", cameraIds)
                    .inSql("analysis_type", analysisTypes).between("track_day", startDay, endDay));
        }
        return transferToJson(cameraAnalysisTracks);
    }

    @Override
    public JSONObject getSerialnumberByCameras(JSONObject paramJson) throws Exception {
        String cameraId = paramJson.getString("cameraId");
        if (cameraId == null) {
            log.info(">>>>>>>>>>>>>Exception:cameraId为必传参数");
            throw VideoExceptionUtil.getValidException("cameraId为必传参数");
        }
        String[] cameraIds = cameraId.split(",");
        List<TbAnalysisTask> tbAnalysisTasks = tbAnalysisTaskMapper.getSerialnumberByCameras(cameraIds);
        return transferJson(tbAnalysisTasks);
    }

    /**
     * 转换JSONObject
     * 
     * @param cameraAnalysisTracks
     * @return
     */
    public JSONObject transferToJson(List<CameraAnalysisTrack> cameraAnalysisTracks) throws Exception {
        JSONObject returnJsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        if (cameraAnalysisTracks == null || cameraAnalysisTracks.size() == 0) {
            log.info(">>>>>>>> no tracks found...");
            returnJsonObject.put("tracks", jsonArray);
            return returnJsonObject;
        }
        JSONObject jsonObject = null;
        Map<String, List<CameraAnalysisTrackBase>> trackMap = new HashMap<>();
        List<CameraAnalysisTrackBase> tracks = null;
        for (CameraAnalysisTrack cameraAnalysisTrack : cameraAnalysisTracks) {
            if (trackMap.get(cameraAnalysisTrack.getCameraId()) != null) {
                tracks = trackMap.get(cameraAnalysisTrack.getCameraId());
                CameraAnalysisTrackBase cameraAnalysisTrackBase = new CameraAnalysisTrackBase();
                BeanUtils.copyProperties(cameraAnalysisTrack, cameraAnalysisTrackBase);
                tracks.add(cameraAnalysisTrackBase);
                trackMap.put(cameraAnalysisTrack.getCameraId(), tracks);
            } else {
                tracks = new ArrayList<>();
                CameraAnalysisTrackBase cameraAnalysisTrackBase = new CameraAnalysisTrackBase();
                BeanUtils.copyProperties(cameraAnalysisTrack, cameraAnalysisTrackBase);
                tracks.add(cameraAnalysisTrackBase);
                trackMap.put(cameraAnalysisTrack.getCameraId(), tracks);
            }
        }
        Iterator iterator = trackMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<CameraAnalysisTrackBase>> track =
                (Map.Entry<String, List<CameraAnalysisTrackBase>>)iterator.next();
            jsonObject = new JSONObject();
            jsonObject.put("cameraId", track.getKey());
            jsonObject.put("subTracks", JSONArray.parseArray(JSON.toJSONString(track.getValue())));
            jsonArray.add(jsonObject);
        }
        if (jsonArray.size() > 0) {
            returnJsonObject.put("tracks", jsonArray);
        }
        return returnJsonObject;
    }

    /**
     * 转换JSONObject
     * 
     * @param tbAnalysisTasks
     * @return
     */
    private JSONObject transferJson(List<TbAnalysisTask> tbAnalysisTasks) throws Exception {
        log.info(">>>>>>>>>> task size :{}", tbAnalysisTasks.size());
        JSONObject returnJsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        if (tbAnalysisTasks == null || tbAnalysisTasks.size() == 0) {
            log.info(">>>>>>>> no task data found...");
            returnJsonObject.put("data", jsonArray);
            return returnJsonObject;
        }
        JSONObject jsonObject = null;
        Map<String, String> taskMap = new HashMap<>();
        String serialnumbers = "";
        for (TbAnalysisTask tbAnalysisTask : tbAnalysisTasks) {
            if (taskMap.get(tbAnalysisTask.getCameraId()) != null) {
                serialnumbers = taskMap.get(tbAnalysisTask.getCameraId());
                serialnumbers = serialnumbers + "," + tbAnalysisTask.getId();
                taskMap.put(tbAnalysisTask.getCameraId(), serialnumbers);
            } else {
                taskMap.put(tbAnalysisTask.getCameraId(), tbAnalysisTask.getId());
            }
        }
        Iterator iterator = taskMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> task = (Map.Entry<String, String>)iterator.next();
            jsonObject = new JSONObject();
            jsonObject.put("cameraId", task.getKey());
            jsonObject.put("serialnumbers", task.getValue());
            jsonArray.add(jsonObject);
        }
        if (jsonArray.size() > 0) {
            returnJsonObject.put("data", jsonArray);
        }
        return returnJsonObject;
    }

    /**
     * 组装打点字符串
     *
     * @param trackStartTime
     * @param trackEndTime
     * @return
     */
    private String dealTrackValue(String trackStartTime, String trackEndTime) {
        char[] trackValueChar = "000000000000000000000000000000000000000000000000000000000000".toCharArray();
        log.info(">>>>>>>>trackStartTime:{},trackEndTime:{}", trackStartTime, trackEndTime);
        int startHour = Integer.parseInt(trackStartTime.substring(8, 10));
        int startMinute = Integer.parseInt(trackStartTime.substring(10, 12));
        int startSecond = Integer.parseInt(trackStartTime.substring(12, 14));
        int endHour = Integer.parseInt(trackEndTime.substring(8, 10));
        int endMinute = Integer.parseInt(trackEndTime.substring(10, 12));
        // 假如不是完整的分钟，则往后推一分钟
        if (startSecond > 0) {
            startMinute++;
        }
        // 假如不够一分钟，则不处理
        if (endMinute <= startMinute && startHour == endHour) {
            log.info(">>>>>>>打点数据不够完整的一分钟,start:{},end:{}", trackStartTime, trackEndTime);
            return null;
        }
        // 假如跨小时，如20:59:00到21:00:00
        if (startHour < endHour && endMinute == 0) {
            endMinute = 60;
        }
        for (int i = startMinute; i < endMinute; i++) {
            trackValueChar[59 - i] = '1';
        }
        return new String(trackValueChar);
    }

    @Transactional
    public int syncAnalysisTrack(CameraAnalysisTrack cameraAnalysisTrack) {
        List<CameraAnalysisTrack> cameraAnalysisTrackList = cameraAnalysisTrackMapper
            .selectList(new QueryWrapper<CameraAnalysisTrack>().eq("camera_id", cameraAnalysisTrack.getCameraId())
                .eq("analysis_type", cameraAnalysisTrack.getAnalysisType())
                .eq("track_day", cameraAnalysisTrack.getTrackDay())
                .eq("track_hour", cameraAnalysisTrack.getTrackHour()));

        int count = 0;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if (cameraAnalysisTrackList != null && cameraAnalysisTrackList.size() > 0) {
            String newValue = cameraAnalysisTrack.getTrackValue();
            cameraAnalysisTrack = cameraAnalysisTrackList.get(0);
            int oldVersion = cameraAnalysisTrack.getVersion().intValue();
            long id = cameraAnalysisTrack.getId().longValue();
            String oldValue = cameraAnalysisTrack.getTrackValue();
            String trackValue = calcuteNewTrackValueByOr(oldValue, newValue);
            cameraAnalysisTrack.setTrackValue(trackValue);
            cameraAnalysisTrack.setLastUpdateTime(timestamp);
            cameraAnalysisTrack.setVersion(oldVersion + 1);
            // 使用乐观锁更新
            count = cameraAnalysisTrackMapper.update(cameraAnalysisTrack,
                new QueryWrapper<CameraAnalysisTrack>().eq("id", id).eq("version", oldVersion));
            // log.info(">>>>>>>>>>>>> update result:{},id:{}",count,id);
        } else {
            cameraAnalysisTrack.setCreateTime(timestamp);
            cameraAnalysisTrack.setLastUpdateTime(timestamp);
            count = cameraAnalysisTrackMapper.insert(cameraAnalysisTrack);
        }
        return count;
    }

    /**
     * 通过位 或运算计算新的二进制字符串 因long是有符号类型，故在位运算之前需要在前面补0
     *
     * @param oldValue
     * @param newValue
     * @return
     */
    @Override
    public String calcuteNewTrackValueByOr(String oldValue, String newValue) {
        oldValue = "0" + oldValue;
        newValue = "0" + newValue;
        long newValue1 = Long.valueOf(newValue, 2);
        long oldValue1 = Long.valueOf(oldValue, 2);
        long trackValue = newValue1 | oldValue1;
        return SeqUtil.paddingString(Long.toBinaryString(trackValue), '0', 60);
    }

    @Override
    public String calcuteNewTrackValueByAnd(String oldValue, String newValue) {
        oldValue = "0" + oldValue;
        newValue = "0" + newValue;
        long newValue1 = Long.valueOf(newValue, 2);
        long oldValue1 = Long.valueOf(oldValue, 2);
        long trackValue = newValue1 & oldValue1;
        return SeqUtil.paddingString(Long.toBinaryString(trackValue), '0', 60);
    }

    /**
     * 先把camera_analysis_track的数据转移到his表，然后再删除。
     * 
     * @param cameraIds
     * @param ymd
     * @return
     */
    @Override
    @Transactional
    public boolean transAnalysisTracksToHis(String cameraIds, String ymd) {
        if (ymd != null) {
            if (cameraIds != null) {
                String[] cameraIdArray = cameraIds.split(",");
                for (String cameraId : cameraIdArray) {
                    cameraAnalysisTrackMapper.insertAnalysisTrackHisFromTrack(cameraId, ymd);
                    cameraAnalysisTrackMapper.deleteAnalysisTrack(cameraId, ymd);
                }
            } else {
                cameraAnalysisTrackMapper.insertAnalysisTrackHisFromTrack(null, ymd);
                cameraAnalysisTrackMapper.deleteAnalysisTrack(null, ymd);
            }
        }
        return true;
    }

}
