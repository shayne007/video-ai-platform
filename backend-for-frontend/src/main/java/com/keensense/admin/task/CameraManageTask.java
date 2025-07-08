package com.keensense.admin.task;

import com.keensense.admin.entity.task.Camera;
import com.keensense.admin.service.task.ICameraService;
import com.keensense.admin.util.DbPropUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 监控点定时任务
 *
 * @author admin
 */
@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Slf4j
public class CameraManageTask {

    @Resource
    private ICameraService cameraService;

    /**
     * 获取监控点快照
     */
    @Scheduled(cron = "0 1/10 * * * ?")
    public void updateCameraThumbNail() {
        if (DbPropUtil.getBoolean("sync-camera-thumbNail", false)) {
            log.info("--------updateTumbNail----------updateTumbNail----------");
            List<Camera> cameras = cameraService.selectTumbNailCameraList();
            for (Camera camera : cameras) {
                String cameraId = camera.getId() + "";
                cameraService.getCameraSnapshotByUrl(cameraId);
            }
        }
    }
}
