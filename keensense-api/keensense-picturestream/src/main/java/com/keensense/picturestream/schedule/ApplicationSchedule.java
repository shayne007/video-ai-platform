package com.keensense.picturestream.schedule;

import com.keensense.picturestream.algorithm.IFaceStruct;
import com.keensense.picturestream.algorithm.IObjextStruct;
import com.keensense.picturestream.algorithm.IVlprStruct;
import com.keensense.picturestream.algorithm.impl.FaceStructImpl;
import com.keensense.picturestream.algorithm.impl.ObjextStructImpl;
import com.keensense.picturestream.algorithm.impl.VlprStructImpl;
import com.keensense.picturestream.common.DownloadImageQueue;
import com.keensense.picturestream.common.DownloadImageThreads;
import com.keensense.picturestream.common.PictureStructMain;
import com.keensense.picturestream.common.ResultSendThreads;
import com.keensense.picturestream.config.NacosConfig;
import com.keensense.picturestream.entity.PictureInfo;
import com.keensense.picturestream.input.IPictureStream;
import com.keensense.picturestream.input.impl.PictureStreamKafkaImpl;
import com.keensense.picturestream.output.IResultSend;
import com.keensense.picturestream.output.impl.ResultSendImpl;
import com.keensense.picturestream.util.ImageBaseUtil;
import com.loocme.sys.util.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description: 图片处理定时任务
 * @Author: wujw
 * @CreateDate: 2019/9/17 9:44
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Component
@Slf4j
public class ApplicationSchedule {

    @Scheduled(cron = "0/1 * * * * ?")
    public void takeObjext(){
        try {
            PictureInfo picInfo = DownloadImageQueue.takeObjext();
            if (picInfo != null && PictureInfo.STATUS_ERROR != picInfo.getStatus()) {
                PictureStructMain.recogObjext(picInfo);
            }
        } catch (Exception e) {
            log.error("PictureQueueTake objext error", e);
        }
    }

    @Scheduled(cron = "0/1 * * * * ?")
    public void takeVlpr(){
        try {
            PictureInfo picInfo = DownloadImageQueue.takeVlpr();
            if (picInfo != null && PictureInfo.STATUS_ERROR != picInfo.getStatus()) {
                PictureStructMain.recogVlpr(picInfo);
            }
        } catch (Exception e) {
            log.error("PictureQueueTake vlpr error", e);
        }
    }

}
