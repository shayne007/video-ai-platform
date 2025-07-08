package com.keensense.admin.service.task;

import com.keensense.admin.util.Page;
import com.keensense.admin.vo.ResultQueryVo;

import java.util.List;
import java.util.Map;

public interface ResultService {

    /**
     * 时间时序标识
     */
    public static final String ASC ="asc";
    /**
     * 时间降序标识
     */
    public static final String DESC ="desc";
    /**
     * id升序标识
     */
    public static final String ID_ASC ="idasc";
    /**
     * Id降序标识
     */
    public static final String ID_DESC ="iddesc";

    /***
     * 通过JManager接口获取实时分析数据
     * @param page
     * @param map
     * @return
     */
     Map<String, Object> getRealtimeDataByExt(Page<ResultQueryVo> page, Map<String, Object> map);

    /**
     * 通过JManager接口获取离线分析数据
     * @param page
     * @param map
     * @return
     */
     Map<String, Object> getOfflinetDataByExt(Page<ResultQueryVo> page, Map<String, Object> map);


    void dealDataByJManager(Map<String, Object> pamaMap, Map<String, Object> resultMap, ResultQueryVo paramBo, List<ResultQueryVo> resultList);

    /**
     * 根据图片ID和指定类型查询目标图片信息
     * @param objtype
     * @param resultId
     * @return
     */
    ResultQueryVo getResultBoById(String objtype, String resultId);

    /**
     * 监控组实时任务 查询结果
     * @param page
     * @param map
     * @return
     */
    Map<String, Object> getMonRealtimeDataByExt(Page<ResultQueryVo> page, Map<String, Object> map);

    /**
     * 目标检索数据
     * @param map
     * @return
     */
    Map<String,Object> queryResultByJManager(Map<String, Object> map);
}
