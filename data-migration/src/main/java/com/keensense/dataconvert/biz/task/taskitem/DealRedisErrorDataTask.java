package com.keensense.dataconvert.biz.task.taskitem;

import com.alibaba.fastjson.JSON;
import com.keensense.dataconvert.api.util.RealTimeImageDownloadUtil;
import com.keensense.dataconvert.biz.common.cache.redis.RedisConstant;
import com.keensense.dataconvert.biz.common.cache.redis.RedisService;
import com.keensense.dataconvert.biz.entity.PictureInfo;
import com.loocme.sys.util.ListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.task.taskitem
 * @Description： <p> DealRedisErrorDataTask - 配置定时任务 定时处理redis 里面的出现异常的数据 </p>
 * @Author： - Jason
 * @CreatTime：2019/7/26 - 16:13
 * @Modify By：
 * @ModifyTime： 2019/7/26
 * @Modify marker：
 */
@Component
public class DealRedisErrorDataTask {

    private static final Logger logger = LoggerFactory.getLogger(DealRedisErrorDataTask.class);

    @Autowired
    private RedisService redisService;


    /**
     * 将redis里面网络异常的数据重新处理  l_push 队头  r_pop 队尾
     * issue:有线程安全问题
     */
    @Scheduled(cron ="0/1 * * * * *")
    public void aSyncRedisNetErrorDeal(){
        dealRedisErrorData(RedisConstant.REDIS_FEATURE_NET_ERROR_LIST_KEY);
        dealRedisErrorData(RedisConstant.REDIS_FEATURE_CODE_ERROR_LIST_KEY);
    }


    /**
     * 处理redis异常数据
     * @param redisKey
     */
    public void dealRedisErrorData(String redisKey){
        Long lSize = redisService.llen(redisKey);
        if (lSize != null && lSize > 0L){
            String jsonData = redisService.rpop(redisKey);
            List<PictureInfo> pictureInfoList = JSON.parseArray(jsonData, PictureInfo.class);
            if (ListUtil.isNotNull(pictureInfoList)){
                for (PictureInfo pictureInfo : pictureInfoList) {
                    pictureInfo.setStatus(PictureInfo.STATUS_INIT);
                    RealTimeImageDownloadUtil.downloadResource(pictureInfo);
                }
            }
            logger.info("=== dealRedisErrorData:处理redis 网络请求异常数据Size:[{}] ... ",lSize);
        }else {
            return;
        }
    }



}
