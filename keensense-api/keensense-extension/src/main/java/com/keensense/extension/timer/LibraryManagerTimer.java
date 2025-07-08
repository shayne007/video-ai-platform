package com.keensense.extension.timer;

import com.keensense.extension.service.IArchivesClustService;
import com.keensense.extension.service.impl.ArchiveInfoServiceImpl;
import com.keensense.extension.util.BodyLibraryUtil;
import com.keensense.extension.util.FaceLibraryUtil;
import com.keensense.extension.util.LibraryUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Description: 底库管理定时任务
 * @Author: kellen
 * @CreateDate: 2019/2/26 15:45
 * @Version: 1.0
 */
@Slf4j
@Component
public class LibraryManagerTimer {

    private static int SLEEP = ThreadLocalRandom.current().nextInt(2000, 5000);

    private static ThreadLocalRandom random = ThreadLocalRandom.current();
    @Autowired
    private IArchivesClustService archivesClustService;
    @Resource(name = "bodyRedisTemplate")
    private RedisTemplate<String, String> bodyRedisTemplate;

    @Scheduled(cron = "* * 1/24 * * ?")
    public void libraryManager() {
        try {
            Thread.sleep(SLEEP);
            Thread.sleep(random.nextInt());
            LibraryUtil.isExistLibrary();
            BodyLibraryUtil.deleteInvalidBodyLibrary(); //删除过期的人形底库
            LibraryUtil.updateLibraryCache();           //更新缓存
        } catch (Exception e) {
            log.error("LibraryManagerTimer error", e);
        }
    }

    @Scheduled(cron = "* * 2/24 * * ?")
    public void clearOldArchivesCache() {
        try {
            ArchiveInfoServiceImpl.mapFaceIdAId.clear();
            ArchiveInfoServiceImpl.mapPersonIdArchivesDTO.clear();
        } catch (Exception e) {
            log.error("clearOldArchivesCache Exception", e);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void glLibraryCreate() {
        FaceLibraryUtil.createGLCaptureFaceLibrary();
    }

    @Scheduled(cron = "0/6 * * * * ? ")
    public void updateFaceClust() {
        try {
            archivesClustService.startFaceClust();
        } catch (Exception e) {
            log.info("start face cluster timer error", e);
        }
    }

    /***
     * @description: 更新人脸和人形关系表
     * @param
     * @return: void
     */
    @Scheduled(cron = "0/10 * * * * ? ")
    public void updEsRelationByBody() {
        try {
            archivesClustService.updEsRelationByBody();
        } catch (Exception e) {
            log.info("start face cluster timer error", e);
        }
    }

    @Scheduled(cron = "0 0 4 * * ? ")
    public void startBodyClust() {
        try {
            archivesClustService.startBodyClust();
//            ReidFeatureClusterSpringRedis.clean(bodyRedisTemplate);
        } catch (Exception e) {
            log.info("start body cluster timer error", e);
        }
    }
}
