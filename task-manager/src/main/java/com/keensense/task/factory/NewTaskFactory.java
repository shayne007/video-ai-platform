package com.keensense.task.factory;

import com.keensense.common.config.SpringContext;
import com.keensense.task.config.NacosConfig;
import com.keensense.task.constants.ObjextTaskConstants;
import com.keensense.task.constants.TaskConstants;
import com.keensense.task.entity.TbAnalysisTask;
import com.keensense.task.factory.desitty.DesittyTask;
import com.keensense.task.factory.objext.ObjextTask;
import com.keensense.task.factory.objext.vedio.VideoTask;
import com.keensense.task.factory.objext.vedio.impl.VideoDownloadTask;
import com.keensense.task.factory.objext.vedio.impl.VideoOnlineTask;
import com.keensense.task.factory.objext.vedio.impl.WsVideoDownloadTask;
import com.keensense.task.factory.objext.vedio.impl.WsVideoOnlineTask;
import com.keensense.task.factory.other.OtherTask;
import com.keensense.task.factory.picture.PictureTask;
import com.keensense.task.factory.summary.SummaryTask;
import com.keensense.task.factory.traffic.TrafficTask;
import com.keensense.task.util.VideoExceptionUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 任务工厂类
 * @Author: wujw
 * @CreateDate: 2019/5/9 15:43
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class NewTaskFactory {

    private static final Map<Integer, AbstractTaskManager> cachedTaskManager = new HashMap<>();

    static {
        cachedTaskManager.put(ObjextTaskConstants.VIDEO_TYPE_DOWNLAND , new VideoOnlineTask());
        cachedTaskManager.put(ObjextTaskConstants.VIDEO_TYPE_DOWNLAND , new VideoDownloadTask());
    }
    private static NacosConfig nacosConfig = SpringContext.getBean(NacosConfig.class);

    /***
     * @description: 根据任务类型和url获取任务对象,仅在添加任务的时候使用
     * @param analyType 任务类型
     * @param url 任务地址
     * @return: com.qstvideo.task.factory.ITaskManager
     */
    public static AbstractTaskManager getTaskByUrl(String analyType, String url){
        if(TaskConstants.ANALY_TYPE_OBJEXT.equals(analyType)){
            if(ObjextTaskConstants.isSliceVideo(url)){
                Integer analyMethod = nacosConfig.getAnalysisMethod();
                if (analyMethod == null) {
                    throw VideoExceptionUtil.getCfgException("未配置正确的录像流分析方式，请与管理员确认");
                }
                AbstractTaskManager taskManager = cachedTaskManager.get(analyMethod);
                return taskManager;

            }
            return new ObjextTask();
        }else if(TaskConstants.ANALY_TYPE_SUMMARY.equals(analyType)){
            return new SummaryTask();
        //抓拍机没有不需要url参数，所以url为null
        }else if(TaskConstants.ANALY_TYPE_PICTURE.equals(analyType)){
            return new PictureTask();
        }
        throw VideoExceptionUtil.getCfgException("未知的任务类型！");
    }

    /***
     * @description: 根据任务类型和url获取任务对象,仅在添加任务的时候使用
     * @param analyType 任务类型
     * @param taskType 视频类型
     * @return: com.qstvideo.task.factory.ITaskManager
     */
    public static AbstractTaskManager getTaskByType(String analyType, Integer taskType){
        if(TaskConstants.ANALY_TYPE_OBJEXT.equals(analyType)){
            if(TaskConstants.TASK_TYPE_VIDEO == taskType){
                int analyMethod = nacosConfig.getAnalysisMethod();
                if(ObjextTaskConstants.VIDEO_TYPE_ONLINE == analyMethod){
                    return new WsVideoOnlineTask();
                }else if(ObjextTaskConstants.VIDEO_TYPE_DOWNLAND == analyMethod){
                    return new WsVideoDownloadTask();
                }else{
                    throw VideoExceptionUtil.getCfgException("未配置正确的录像流分析方式，请与管理员确认");
                }
            }
            return new ObjextTask();
        }else if(TaskConstants.ANALY_TYPE_SUMMARY.equals(analyType)){
            return new SummaryTask();
            //抓拍机没有不需要url参数，所以url为null
        }else if(TaskConstants.ANALY_TYPE_PICTURE.equals(analyType)){
            return new PictureTask();
        }else if(TaskConstants.ANALY_TYPE_DESITTY.equals(analyType)){
            return new DesittyTask();
        }else if(TaskConstants.ANALY_TYPE_TRAFFIC.equals(analyType)){
            return new TrafficTask();
        }else if(TaskConstants.ANALY_TYPE_OTHER.equals(analyType)){
            return new OtherTask();
        }
        throw VideoExceptionUtil.getCfgException("未知的任务类型！");
    }

    /***
     * @description: 根据任务类型和url获取任务对象
     * @param tbAnalysisTask 任务对象
     * @return: com.qstvideo.task.factory.ITaskManager
     */
    public static AbstractTaskManager getTaskByType(TbAnalysisTask tbAnalysisTask){
        if(TaskConstants.ANALY_TYPE_OBJEXT.equals(tbAnalysisTask.getAnalyType())){
            //如果是联网录像则用录像下载的处理类处理
            if(TaskConstants.TASK_TYPE_OFFLINE == tbAnalysisTask.getTaskType()){
                return new ObjextTask();
            }else{
                return new VideoTask();
            }
        }else if(TaskConstants.ANALY_TYPE_SUMMARY.equals(tbAnalysisTask.getAnalyType())){
            return new SummaryTask();
        }else if(TaskConstants.ANALY_TYPE_PICTURE.equals(tbAnalysisTask.getAnalyType())){
            return new PictureTask();
        }else if(TaskConstants.ANALY_TYPE_DESITTY.equals(tbAnalysisTask.getAnalyType())){
            return new DesittyTask();
        }else if(TaskConstants.ANALY_TYPE_TRAFFIC.equals(tbAnalysisTask.getAnalyType())){
            return new TrafficTask();
        }else if(TaskConstants.ANALY_TYPE_OTHER.equals(tbAnalysisTask.getAnalyType())){
            return new OtherTask();
        }else{
            throw VideoExceptionUtil.getCfgException("unknown task type wiht "+tbAnalysisTask.getAnalyType());
        }
    }

}
