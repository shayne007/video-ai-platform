package com.keensense.densecrowd.service.task;

import com.keensense.common.util.R;
import com.keensense.densecrowd.vo.TaskParamVo;
import com.keensense.densecrowd.vo.VsdTaskVo;

import java.util.*;

/**
 * @Author: shitao
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 11:21 2019/9/25
 * @Version v0.1
 */
public interface IVsdTaskService {

    /**
     * 启动人群密度分析任务
     * @param cameraId
     * @param udrVertices
     * @return
     */
    R startDensecrowdTask(Long cameraId, Integer interestFlag, String udrVertices, String startTime, String endTime);

    /**
     * 停止人群密度任务
     * @param serialnumber
     * @return
     */
    public Map<String, Object> stopDensecrowdTask(String serialnumber);

    /**
     * 停止监控点实时任务
     *
     * @param serialnumber 任务序列号
     * @return
     */
    Map<String, Object> stopRealtimeTask(String serialnumber);

}
