package com.keensense.picturestream.common;

import com.keensense.picturestream.entity.PictureInfo;

import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DownloadImageQueue extends ArrayBlockingQueue<PictureInfo> {

    private static final long serialVersionUID = 1L;

    private static DownloadImageQueue objextImageQueue = null;
    private static DownloadImageQueue vlprImageQueue = null;
    private static DownloadImageQueue faceImageQueue = null;

    public static void initCapacity(int objextCapacity, int vlprCapacity, int faceCapacity) {
        objextImageQueue = getInstance(objextCapacity);
        vlprImageQueue = getInstance(vlprCapacity);
        faceImageQueue = getInstance(faceCapacity);
    }

    public static PictureInfo takeObjext() {
        return Optional.ofNullable(objextImageQueue).map(DownloadImageQueue::takeInfo).orElse(null);
    }

    public static PictureInfo takeVlpr() {
        return Optional.ofNullable(vlprImageQueue).map(DownloadImageQueue::takeInfo).orElse(null);
    }

    public static PictureInfo takeFace() {
        return Optional.ofNullable(faceImageQueue).map(DownloadImageQueue::takeInfo).orElse(null);
    }

    public static void putObjext(PictureInfo picInfo) {
        if (null != objextImageQueue) {
            objextImageQueue.putInfo(picInfo);
        }
    }

    public static void putVlpr(PictureInfo picInfo) {
        if (null != vlprImageQueue) {
            vlprImageQueue.putInfo(picInfo);
        }
    }

    public static void putFace(PictureInfo picInfo) {
        if (null != faceImageQueue) {
            faceImageQueue.putInfo(picInfo);
        }
    }

    public static boolean isQueueNotNull(int type) {
        if (PictureInfo.QUEUE_OBJEXT == type && null != objextImageQueue) {
            return objextImageQueue.isNotNull();
        } else if (PictureInfo.QUEUE_THIRD_VLPR == type && null != vlprImageQueue) {
            return vlprImageQueue.isNotNull();
        } else if (PictureInfo.QUEUE_THIRD_FACE == type && null != faceImageQueue) {
            return faceImageQueue.isNotNull();
        }
        return false;
    }

    private DownloadImageQueue(int capacity) {
        super(capacity);
    }

    public static DownloadImageQueue getInstance(int capacity) {
        if (0 >= capacity) {
            capacity = 200;
        }
        return new DownloadImageQueue(capacity);
    }

    public void putInfo(PictureInfo picInfo) {
        try {
            super.put(picInfo);
        } catch (Exception e) {
            log.error("putInfo error", e);
        }
    }

    public PictureInfo takeInfo() {
        try {
            return super.take();
        } catch (Exception e) {
            log.error("takeInfo  error", e);
            return null;
        }
    }

    public boolean isNotNull() {
        return super.size() > 0;
    }

}
