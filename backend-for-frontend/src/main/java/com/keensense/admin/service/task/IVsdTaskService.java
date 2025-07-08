package com.keensense.admin.service.task;

import com.keensense.admin.entity.task.Camera;
import com.keensense.admin.request.ResultQueryRequest;
import com.keensense.admin.request.ResultUpdateRequest;
import com.keensense.admin.vo.VsdTaskVo;
import com.keensense.common.util.R;

import java.util.List;
import java.util.Map;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 21:15:11
 */
public interface IVsdTaskService {

    /**
     * 启动监控点实时任务
     *
     * @param cameraId      未启动的监控点
     * @param interestFlag  感兴趣区域标识
     * @param interestParam 感兴趣区域参数
     * @param overlineType 异常行为检测标志
     * @return
     */
    R startRealTimeTask(Long cameraId, Integer interestFlag, String interestParam, Integer chooseType,
                        String tripwires, Long userId, Integer overlineType, Integer enablePartial, Integer scene,
                        String enableIndependentFaceSnap, String enableBikeToHuman);

    /**
     * 联网录像下载失败重新分析任务
     *
     * @param serialnumber
     * @return
     */
    R retryTask(String serialnumber);

    /**
     * 停止监控点实时任务
     *
     * @param serialnumber 任务序列号
     * @return
     */
    Map<String, Object> stopRealtimeTask(String serialnumber);

    Map<String, Object> selectAnalysisProgressByFileId(String fileId);

    List<VsdTaskVo> taskResultHandle(List<VsdTaskVo> vsdTaskList);

    boolean deleteBySerialnumber(String serialnumber);

    R deleteGateTask(String cameraId);

    R startNewGateTask(Long userId, Camera camera);

    R deleteResultData(String uuid);

    R updataResultData(ResultUpdateRequest paramBo);

}

