package com.keensense.picturestream.starter;

import com.keensense.common.config.SpringContext;
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
import com.keensense.picturestream.util.IDUtil;
import com.keensense.picturestream.util.ImageBaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/5/14 14:54
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info(" ********     AlgoStartup start   ******** ");
        //数据库初始化配置加载
        if (event.getApplicationContext().getParent() instanceof AnnotationConfigApplicationContext) {
            run();
        }
        log.info(" ********     AlgoStartup end     ******** ");
    }


    private void run() {
        final ExecutorService service = Executors.newFixedThreadPool(2);
        NacosConfig nacosConfig = SpringContext.getBean(NacosConfig.class);
        log.info("start");
        // 初始化下载图片队列大小
        DownloadImageQueue.initCapacity(nacosConfig.getCapabilityQueueObjext(), nacosConfig.getCapabilityQueueVlpr(),
                nacosConfig.getCapabilityQueueVlpr());

        // 初始化图片下载线程
        DownloadImageThreads.initCapacity(nacosConfig.getCapabilityImageDownload());

        // 初始化数据来源实现方式
        IPictureStream pictureStream = new PictureStreamKafkaImpl();
        pictureStream.init();

        // 初始化数据输出实现方式
        IResultSend resultSend = new ResultSendImpl();
        ResultSendThreads.initCapacity(nacosConfig.getCapabilitySendUpload(), resultSend);

        // 初始化识别资源
        IObjextStruct iObjextStruct = new ObjextStructImpl();
        IVlprStruct iVlprStruct = new VlprStructImpl();
        IFaceStruct iFaceStruct = new FaceStructImpl();
        PictureStructMain.initAlgorithm(iObjextStruct, iVlprStruct, iFaceStruct);

        //图片存储配置
        ImageBaseUtil.init(nacosConfig.getSlaveImageUrlLocal(), nacosConfig.getSlaveImageUrl());

        /**
         * 启动下载线程，从消息队列读取图片数据，下载至本地
         */
        service.execute(() -> {
            Thread.currentThread().setName(IDUtil.threadName("start-monitor"));
            List<PictureInfo> picInfoList;
            boolean flag = true;
            while (flag) {
                try {
                    picInfoList = pictureStream.loadPictureRecords();
                    if (!CollectionUtils.isEmpty(picInfoList)) {
                        log.info("load picture records to download succ:" + picInfoList.size());
                        // 交给多线程处理下载
                        for (int j = 0; j < picInfoList.size(); j++) {
                            DownloadImageThreads.downloadResource(picInfoList.get(j));
                        }
                    } else {
                        log.info("load picture records to download succ:0");
                        // 休眠2s
                        Thread.sleep(500);
                    }
                } catch (Exception e) {
                    log.error("auto download picture error", e);
                }
            }
        });

        //将下载后的图片数据，进行结构化分析后将结果推送至视图库
        new ResultSendThreads();

        /**
         * 启动人脸识别的线程
         */
        service.execute(() -> {
            while (true) {
                try {
                    PictureInfo picInfo = DownloadImageQueue.takeFace();
                    if (picInfo != null && PictureInfo.STATUS_ERROR != picInfo.getStatus()) {
                        PictureStructMain.recogFace(picInfo);
                    } else {
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    log.error("PictureQueueTake face error", e);
                }
            }
        });
        log.info("start finish");
    }
}