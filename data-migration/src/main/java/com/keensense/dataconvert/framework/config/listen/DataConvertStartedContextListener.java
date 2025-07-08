package com.keensense.dataconvert.framework.config.listen;

import com.keensense.dataconvert.api.processor.AppProcessor;
import com.keensense.dataconvert.api.request.queue.AlgRequestQueue;
import com.keensense.dataconvert.api.request.queue.DownloadImageQueue;
import com.keensense.dataconvert.api.request.queue.ImageDownloadClientQueue;
import com.keensense.dataconvert.api.util.VsdTaskUtil;
import com.keensense.dataconvert.biz.common.consts.CommonConst;
import com.keensense.dataconvert.framework.common.utils.cache.EhCacheUtil;
import com.keensense.dataconvert.framework.config.ehcache.constants.EhcacheConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName：NoSeatBeltRecogStartedContextListener
 * @Description： <p> DataConvertStartedContextListener - 启动时候加载配置初始化 </p>
 * @Author： - Jason
 * @CreatTime：2019/7/23 - 11:11
 * @Modify By：
 * @ModifyTime： 2019/7/23
 * @Modify marker：
 * @version V1.0
*/
@Configuration
public class DataConvertStartedContextListener implements ApplicationListener<ApplicationStartedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(DataConvertStartedContextListener.class);

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        if(null == event.getApplicationContext().getParent()) {
            String applicationName = event.getApplicationContext().getApplicationName();
            logger.info(">>>> applicationName:{} - DataConvertStartedContextListener Context Started ...<<<",applicationName);
            initQueue();
            clearEhCache();
            VsdTaskUtil.startDealVsdTask();
            AppProcessor.buildEsIndex();
            AppProcessor.startMysql2Es();
            AppProcessor.startEs2Es();
        }

    }


    /**
     * 初始化queen
     */
    public static void initQueue(){
        logger.info("\n[Queue初始化][initQueue]DownloadImageSize:[{}],DownloadClientSize:[{}],AlgRequestSize:[{}]",
                CommonConst.DOWNLOAD_QUEUE_CAPACITY,CommonConst.DOWNLOAD_QUEUE_REQUEST_SIZE,CommonConst.DOWNLOAD_QUEUE_REQUEST_SIZE);
        DownloadImageQueue.initCapacity(CommonConst.DOWNLOAD_QUEUE_CAPACITY);
        ImageDownloadClientQueue.initCapacity(CommonConst.DOWNLOAD_QUEUE_REQUEST_SIZE);
        AlgRequestQueue.initCapacity(CommonConst.DOWNLOAD_QUEUE_REQUEST_SIZE);
    }


    /**
     * 由于缓存持久化到磁盘了 防止干扰数据
     * 启动时清理一下缓存数据
     */
    public static void clearEhCache(){
        logger.info("=== [clearEhCache]清理EhCache缓存 ===");
        EhCacheUtil.removeAll(EhcacheConstant.EHCAHCHE_PIC_PUSH_KAFKA_CACHE);
    }
}
