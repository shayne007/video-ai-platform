package com.keensense.picturestream.common;

import com.keensense.picturestream.entity.PictureInfo;
import com.keensense.picturestream.output.IResultSend;
import com.keensense.picturestream.util.IDUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

@Slf4j
public class ResultSendThreads implements Runnable {

    private static ExecutorService service = null;
    private static IResultSend resultSendImpl;
    private static final int SEND_NUMBER = 100;
    private static int capacity = 3;
    private int type;

    public ResultSendThreads() {
        for (int i = 0; i < capacity; i = i + 3) {
            service.execute(new ResultSendThreads(PictureInfo.QUEUE_OBJEXT));
            service.execute(new ResultSendThreads(PictureInfo.QUEUE_THIRD_VLPR));
            service.execute(new ResultSendThreads(PictureInfo.QUEUE_THIRD_FACE));
        }
    }

    public ResultSendThreads(int type) {
        this.type = type;
    }

    public static void initCapacity(int capacityNum, IResultSend resultSend) {
        service = Executors.newFixedThreadPool(capacity);
        capacity = capacityNum;
        resultSendImpl = resultSend;
    }

    @Override
    public void run() {
        Thread.currentThread().setName(IDUtil.threadName("ResultSendThreads_" + type));
        boolean falg = true;
        while (falg) {
            try {
                List<PictureInfo> pictureInfos = new ArrayList<>();
                if (PictureInfo.QUEUE_OBJEXT == type && !PictureStructMain.objextPictureQueue.isEmpty()) {
                    pictureInfos = getPictureInfos(PictureStructMain.objextPictureQueue);
                } else if (PictureInfo.QUEUE_THIRD_VLPR == type && !PictureStructMain.vlprPictureQueue.isEmpty()) {
                    pictureInfos = getPictureInfos(PictureStructMain.vlprPictureQueue);
                } else if (PictureInfo.QUEUE_THIRD_FACE == type && !PictureStructMain.facePictureQueue.isEmpty()) {
                    pictureInfos = getPictureInfos(PictureStructMain.facePictureQueue);
                }
                if (!CollectionUtils.isEmpty(pictureInfos)) {
                    resultSendImpl.receiveData(pictureInfos);
                } else {
                    Thread.sleep(100);
                }
            } catch (Exception e) {
                log.error("PictureQueueTake error ,type=" + type, e);
            }
        }
    }

    private List<PictureInfo> getPictureInfos(ConcurrentLinkedQueue<PictureInfo> queue) {
        List<PictureInfo> pictureInfos = new ArrayList<>();
        for (int i = 0; i < SEND_NUMBER; i++) {
            PictureInfo object = queue.poll();
            if (object != null) {
                pictureInfos.add(object);
            } else {
                break;
            }
        }
        return pictureInfos;
    }

    /**
     * @param pictureInfo 图片信息
     * @description: 异常处理
     * 存在状态如下：
     * 1.picInfo.setStatus(PictureInfo.STATUS_DOWNLOAD_FAIL);
     * 2.picInfo.setStatus(PictureInfo.STATUS_UNKOWN_URL);
     * @return: void
     */
    public static void sendError(PictureInfo pictureInfo) {
        log.error("picture error and donnot send" + pictureInfo.toString());
    }

}
