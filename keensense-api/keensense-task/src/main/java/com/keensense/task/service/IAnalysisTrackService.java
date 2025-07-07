package com.keensense.task.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName: IAnalysisTrackService
 * @Description: 分析打点服务接口
 * @Author: cuiss
 * @CreateDate: 2020/4/14 15:52
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public interface IAnalysisTrackService {

    /**
     * 处理分析打点数据
     * @param serialnumber
     * @param cameraId
     * @param analysisTypes
     * @param trackStartTime
     * @param trackEndTime
     * @return
     * @throws Exception
     */
     boolean syncAnalysisTrack(String serialnumber,String cameraId,String analysisTypes,String trackStartTime,String trackEndTime) throws Exception;

    /**
     * 查询打点记录
     * @param paramJson
     * @return
     */
      JSONObject getTrackList(JSONObject paramJson)throws Exception;

    /**
     * 根据点位id查询serialnumber列表
     * @param paramJson
     * @return
     */
    JSONObject getSerialnumberByCameras(JSONObject paramJson) throws Exception;

    /**
     * 通过位 或运算汇总打点结果
     * @param oldValue
     * @param newValue
     * @return
     */
    public String calcuteNewTrackValueByOr(String oldValue, String newValue);

    /**
     * 通过位 与运算汇总打点结果
     * @param oldValue
     * @param newValue
     * @return
     */
    public String calcuteNewTrackValueByAnd(String oldValue, String newValue);

    /**
     * 根据点位及时间，将分析打点轨迹转移至his表
     * @param cameraIds
     * @param ymd
     * @return
     */
    public boolean transAnalysisTracksToHis(String cameraIds,String ymd);
}
