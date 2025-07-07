package com.keensense.task.async.executor.analysistrack;

import com.keensense.common.config.SpringContext;
import com.keensense.task.async.AsyncQueueConstants;
import com.keensense.task.async.Message;
import com.keensense.task.async.executor.ReloadTaskExecutor;
import com.keensense.task.async.util.MsgUtils;
import com.keensense.task.service.IAnalysisTrackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName: AnalysisTrackTaskExecutor
 * @Description: 分析打点 具体的执行器
 * @Author: cuiss
 * @CreateDate: 2020/4/13 16:44
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
@Component
public class AnalysisTrackTaskExecutor extends ReloadTaskExecutor {

    /**
     * 假如处理失败，并且重试次数低于阈值，则重新推送进队列
     *
     * @param message
     */
    @Override
    public void reloadTask(Message message) {
        int retrycount = message.getRetrycount();
        if (retrycount <= AsyncQueueConstants.TASK_RETRYCOUNT_LIMIT) {
            log.info(">>>>>current thread:{},reloading message:{} ", Thread.currentThread().getName(), message);
            //重新加入队列
            MsgUtils.publish(AsyncQueueConstants.AYALYSIS_TRACK_QUEUE_NAME, MsgUtils.reloadMessage(message));
        }
    }

    @Override
    public String execute(Map<String, Object> param) {
        try {
            log.info(">>>>>>>>current thread:{}, AnalysisTrackTaskExecutor :{}", Thread.currentThread().getName(), param);
            IAnalysisTrackService analysisTrackService = (IAnalysisTrackService) SpringContext.getBean("AnalysisTrackService");
            boolean result = analysisTrackService.syncAnalysisTrack(param.get("serialnumber").toString(),
                    param.get("cameraId").toString(), param.get("analysisTypes").toString(),
                    param.get("trackStartTime").toString(), param.get("trackEndTime").toString());
            if (result) {
                return AsyncQueueConstants.TASK_ERROR_MESSAGE_OK;
            } else {
                return AsyncQueueConstants.TASK_ERROR_MESSAGE_EXECUTE_FAILED;
            }

        } catch (Exception e) {
            log.error(">>>>>>>>>>current thread:{}, Exception in executor:{}", Thread.currentThread().getName(), e.getMessage());
            e.printStackTrace();
            return AsyncQueueConstants.TASK_ERROR_MESSAGE_EXECUTE_FAILED;
        }
    }
}
