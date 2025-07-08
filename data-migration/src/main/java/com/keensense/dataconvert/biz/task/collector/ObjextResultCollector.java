package com.keensense.dataconvert.biz.task.collector;

import com.alibaba.fastjson.JSON;
import com.keensense.dataconvert.biz.common.cache.redis.RedisConstant;
import com.keensense.dataconvert.biz.common.cache.redis.RedisService;
import com.keensense.dataconvert.biz.common.enums.ResultEnum;
import com.keensense.dataconvert.biz.entity.ObjextResult;
import com.keensense.dataconvert.biz.task.callable.ObjextResultCollectCall;
import com.keensense.dataconvert.biz.task.constant.TaskConstant;
import com.keensense.dataconvert.biz.task.vo.TaskResultInfo;
import net.sf.ehcache.util.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.task.collector
 * @Description： <p> ObjextResultCollector </p>
 * @Author： - Jason
 * @CreatTime：2019/7/25 - 18:22
 * @Modify By：
 * @ModifyTime： 2019/7/25
 * @Modify marker：
 */
@Component
public class ObjextResultCollector {

    public static final Logger logger = LoggerFactory.getLogger(VlprResultCollector.class);

    /**
     * name 线程区分
     */
    public static String COLLECTOR_NAME = "convert-data-objext";


    @Autowired
    private RedisService redisService;

    /**
     * 开始启动
     */
    public void startCollector(List<ObjextResult> resultsList, String tableYmd) {
        int threadNum = TaskConstant.THREAD_NUM;
        logger.info("cpu的核心数:{}",threadNum);
        /**
         * 开始采集数据
         */
        if (threadNum > 0) {
            /**
             * 创建一个定长线程池  可控制线程最大并发数, 超出的线程会在队列中等待
             */
            ExecutorService collectExec = newFixedThreadPool(threadNum, new NamedThreadFactory(COLLECTOR_NAME));
            List<FutureTask<TaskResultInfo>> taskList = new ArrayList<>();
            /**
             * callable
             */
            ObjextResultCollectCall resultCollectCall;
            for (ObjextResult objextResult : resultsList) {
                resultCollectCall = new ObjextResultCollectCall(objextResult,tableYmd);
                /**
                 * resultCollectCallTask
                 */
                FutureTask<TaskResultInfo> resultCollectCallTaskList = new FutureTask<>(resultCollectCall);
                /**
                 * submit 有返回值,Runnable接口
                 */
                taskList.add(resultCollectCallTaskList);
                collectExec.submit(resultCollectCallTaskList);
            }

            for (FutureTask<TaskResultInfo> futureTask : taskList) {
                try {
                    TaskResultInfo taskResultInfo = futureTask.get();
                    if (taskResultInfo.getCollectCallCode() != ResultEnum.BIZ_DEAL_SUCCESS.getResultCode()){
                        String collectTableYmdStr = taskResultInfo.getCollectTableYmdStr();
                        redisService.lpush(RedisConstant.REDIS_MYSQL_OBJEXT_TO_ES_ERROR_LIST_KEY.concat(collectTableYmdStr),
                                JSON.toJSONString(taskResultInfo.getTaskResult()));
                    }
                } catch (InterruptedException e) {
                    logger.info("== InterruptedException:error:{} ==",e.getMessage());
                } catch (ExecutionException e) {
                    logger.info("== ExecutionException:error:{} ==",e.getMessage());
                }
            }
            collectExec.shutdown();
        }

    }
}
