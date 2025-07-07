package com.keensense.task.factory.objext.vedio.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.common.config.SpringContext;
import com.keensense.task.constants.TaskConstants;
import com.keensense.task.entity.*;
import com.keensense.task.factory.objext.vedio.OnlineTask;
import com.keensense.task.mapper.CameraAnalysisTrackMapper;
import com.keensense.task.service.IAnalysisTrackService;
import com.keensense.task.util.TaskParamValidUtil;
import com.keensense.task.util.VideoExceptionUtil;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description:
 * @Author: wujw
 * @CreateDate: 2019/8/7 14:23
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class WsVideoOnlineTask extends OnlineTask {

    private CameraAnalysisTrackMapper cameraAnalysisTrackMapper = SpringContext.getBean(CameraAnalysisTrackMapper.class);

    private IAnalysisTrackService analysisTrackService = (IAnalysisTrackService)SpringContext.getBean("AnalysisTrackService");

    @Override
    public void insertTask(JSONObject paramJson, String serialnumber, String type, String url) {
        String entryTime = super.getEntryTime(paramJson);
        paramJson.put("param", getParamMap(paramJson, serialnumber,url, entryTime, TaskConstants.TASK_TYPE_VIDEO));
        Timestamp startTime = TaskParamValidUtil.validTime(paramJson, "startTime", true);
        Timestamp endTime = TaskParamValidUtil.validTime(paramJson, "endTime", true);
        VasUrlEntity vasUrlEntity = new VasUrlEntity(startTime.getTime(), endTime.getTime(), url);
        List<TbAnalysisDetail> insertDetailList = null ;
        //判断是否开启分析打点，假如开启，不需要提交已经分析过的子片任务
        //否则，则按默认录像流分片规则划分子片
        String analysisTypes = paramJson.getString("analysisTypes");
        String cameraId = paramJson.getString("cameraId");
        if(nacosConfig.getAnalysisTrackSwitch()){
            insertDetailList = getSliceDetailByTrack(serialnumber,vasUrlEntity,cameraId,analysisTypes,paramJson.getString("startTime"),paramJson.getString("endTime"));
        }else{
            insertDetailList = super.getSliceDetail(serialnumber, vasUrlEntity, true);
        }
        if(insertDetailList != null && insertDetailList.size() > 0){
            super.insertTbAnalysisTask(paramJson, serialnumber, TaskConstants.ANALY_TYPE_OBJEXT, TaskConstants.TASK_TYPE_VIDEO,insertDetailList.size());
            int count = tbAnalysisDetailMapper.insertBatch(insertDetailList);
            if (count <= 0) {
                throw VideoExceptionUtil.getDbException(serialnumber + " insert TbAnalysisDetail failed!");
            }
        }
    }

    /**
     * * 根据分片规则及打点数据初始化子分片任务
     *  先根据分片规则切好，然后跟打点轨迹去比较，假如打点轨迹中包含，则无需重复提交
     * 否则只提交未打点的部分
     *
     * @param serialnumber 任务ID
     * @param vasUrlEntity 分片参数对象
     * @param cameraId 点位ID
     * @param analysisTypes 分析类型
     * @param startTime 录像开始时间
     * @param endTime 录像结束时间
     * @return
     */
    private List<TbAnalysisDetail> getSliceDetailByTrack(String serialnumber, VasUrlEntity vasUrlEntity,String cameraId, String  analysisTypes, String startTime,String endTime) {
        List<VasLice> sliceList = super.getSliceList(vasUrlEntity,analysisTypes);
        startTime = startTime.replaceAll(":","").replaceAll("-","").replaceAll(" ","");
        endTime = endTime.replaceAll(":","").replaceAll("-","").replaceAll(" ","");
        Integer trackDayStart = Integer.parseInt(startTime.substring(0,8));
        Integer trackDayEnd = Integer.parseInt(endTime.substring(0,8));
        Integer trackHourStart = Integer.parseInt(startTime.substring(8,10));
        Integer trackHourEnd = Integer.parseInt(endTime.substring(8,10));
        List<CameraAnalysisTrack> tracks = getTrackValueByParam(cameraId,analysisTypes,trackDayStart,trackDayEnd,trackHourStart,trackHourEnd);
        //假如有轨迹信息，需要将轨迹与提交的分片进行碰撞，拆出没有分析过的时间碎片
        if(tracks != null && tracks.size() > 0){
            sliceList = resliceFromTrack(sliceList, tracks);
        }
        //假定最大子片数量被分析打点切分成三倍数量
        List<TbAnalysisDetail> insertDetailList = new ArrayList<>(sliceList.size()*3);
        for (VasLice vas : sliceList) {
            String detailUrl = super.getUrl(vasUrlEntity.getVasUrl(), vas, true);
            TbAnalysisDetail detail = super.initTbAnalysisDetail(serialnumber);
            detail.setEntryTime(super.getEntryTime(vas.getStartSecond()));
            detail.setAnalysisUrl(detailUrl);
            detail.setAnalysisId(detail.getId());
            detail.setAnalysisStatus(1);
            detail.setAnalysisProgress(0);
            insertDetailList.add(detail);
        }
        return insertDetailList;
    }

    /**
     * 根据轨迹 信息，重新分片
     * @param sliceList
     * @param tracks
     * @return
     */
    private List<VasLice> resliceFromTrack(List<VasLice> sliceList, List<CameraAnalysisTrack> tracks) {

        List<VasLice> newSlice = new ArrayList<>();
        Map<String, String> trackMap = getTrackMapByAnalysisTyppes(sliceList, tracks);

        //遍历每一个分片，与打点数据做碰撞，假如已分析，则从分片中剔除
        Iterator<VasLice> iterator = sliceList.iterator();
        //用一个开始时间列表、一个结束时间列表来表示多个时间片的起始时间
        List<Long> startList = new ArrayList<>();
        List<Long> endList = new ArrayList<>();
        while(iterator.hasNext()){
            VasLice slice = iterator.next();
            long start = slice.getStartSecond();
            long end = slice.getEndSecond();
            //遍历每一分钟
            while(start < end){
                Date date = new Date(start);
                DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                String dateStr = df.format(date);
                char value = '1';
                if(trackMap.get(dateStr.substring(0,10)) !=null ){
                    String val = trackMap.get(dateStr.substring(0,10));
                    int offset = Integer.parseInt(dateStr.substring(10,12));
                    value = val.charAt(59-offset);
                }else{
                    value = '0';
                }
                //假如取到一个未打点的时间片，则设置一个开始时间
                if(value == '0' && startList.size() == endList.size()){
                    startList.add(start);
                }
                //假如取到一个已打点的时间片，并且已经有一个开始时间了，
                // 则把当前打点的时间的前一分钟置为片段的结束时间
                if(value == '1' && startList.size() > endList.size()){
                    endList.add(start);
                }
                start += 60000;
            }
            //最后一位缺少结束标志位时，用结束时间来填充
            if(startList.size()>endList.size()){
                endList.add(end);
            }
        }

        for(int i=0;i<startList.size();i++){
            if(startList.get(i).longValue() != endList.get(i).longValue()){
                VasLice vasLice = new VasLice(startList.get(i).longValue(),endList.get(i).longValue());
                newSlice.add(vasLice);
            }
        }
        return newSlice;

    }

    /**
     * 比较提交的analysisTypes是否在轨迹中有，如果都有，则复用轨迹；否则，则不复用
     * @param sliceList
     * @param tracks
     * @return
     */
    private Map<String, String> getTrackMapByAnalysisTyppes(List<VasLice> sliceList, List<CameraAnalysisTrack> tracks) {
        //用两组map分别存储轨迹值及分析类型，且map对应的key为同一值
        Map<String,String> trackMap = new HashMap<>();
        Map<String,List<Integer>> analysisTypeMap = new HashMap<>();
        //按yyyymmddhh的格式做key放入map
        for(CameraAnalysisTrack track : tracks){
            String hour = track.getTrackHour().intValue() <10 ? "0"+track.getTrackHour().intValue():""+track.getTrackHour().intValue();
            String key = track.getTrackDay().intValue()+hour;
            if(trackMap.get(key) == null){
                trackMap.put(key,track.getTrackValue());

                List<Integer> initList = new ArrayList<>();
                initList.add(track.getAnalysisType());
                analysisTypeMap.put(key,initList);
            }else{
                //假如有多个分析类型的打点轨迹时，则取轨迹的交集，只有交集才不分析；否则，则需要重新提交分析
                String oldValue = trackMap.get(key).toString();
                String newValue = analysisTrackService.calcuteNewTrackValueByAnd(oldValue,track.getTrackValue());
                trackMap.put(key,newValue);

                List<Integer> oldList = analysisTypeMap.get(key);
                oldList.add(track.getAnalysisType());
                analysisTypeMap.put(key,oldList);
            }
        }

        //比较分析类型是否一致，如果不一致，则从trackMap中移除打点
        String analysisTypes = sliceList.get(0).getAnalysisTypes();
        String[] analysisTypeArray = analysisTypes.split(",");
        List<Integer> analysisTypeList = new ArrayList<>();
        for(String analysisType : analysisTypeArray){
            analysisTypeList.add(Integer.parseInt(analysisType));
        }
        Collections.sort(analysisTypeList);

        for(Map.Entry<String,List<Integer>> entry : analysisTypeMap.entrySet()){
            List<Integer> value = entry.getValue();
            //假如长度不一致，则analysisType不相等
            if(value.size() != analysisTypeList.size()){
                trackMap.remove(entry.getKey());
                continue;
            }
            //排序之后，任一一个值不相等，则analysisType不相等
            Collections.sort(value);
            for(int i=0;i<value.size();i++){
                if(value.get(i).intValue() != analysisTypeList.get(i).intValue()){
                    trackMap.remove(entry.getKey());
                    continue;
                }
            }

        }
        return trackMap;
    }

    /**
     * 根据点位id、分析类型、trackDay、trackHour查询轨迹信息
     * @param cameraId
     * @param analysisTypes
     * @param trackDayStart
     * @param trackDayEnd
     * @param trackHourStart
     * @param trackHourEnd
     * @return
     */
    private List<CameraAnalysisTrack> getTrackValueByParam(String cameraId, String analysisTypes, Integer trackDayStart,Integer trackDayEnd,Integer trackHourStart, Integer trackHourEnd){
        if(trackDayStart == null || trackDayEnd == null){
            return cameraAnalysisTrackMapper.selectList(new QueryWrapper<CameraAnalysisTrack>().eq("camera_id",cameraId)
                    .between("track_day",trackDayStart.intValue(),trackHourEnd.intValue()).inSql("analysis_type",analysisTypes));
        }else{
            return cameraAnalysisTrackMapper.selectList(new QueryWrapper<CameraAnalysisTrack>().eq("camera_id",cameraId)
                    .between("track_day",trackDayStart.intValue(),trackDayEnd.intValue())
                    .between("track_hour",trackHourStart.intValue(),trackHourEnd.intValue())
                    .inSql("analysis_type",analysisTypes));
        }
    }

    private String splitSlice(int offset ,String trackValue){
        List<Integer> trackList = new ArrayList<>();
        for(int i=0;i<trackValue.length();i++){


        }
        return  null;
    }

}
