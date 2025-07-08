package com.keensense.densecrowd.task;

import com.keensense.densecrowd.entity.task.Camera;
import com.keensense.densecrowd.service.task.ICameraService;
import com.keensense.densecrowd.util.DbPropUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

    @Autowired
    private ICameraService cameraService;

    /**
     * 获取监控点快照
     */
    @Scheduled(cron = "0 1/59 * * * ?")
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
