package com.keensense.dataconvert.api.request.queue;

import com.keensense.dataconvert.biz.entity.PictureInfo;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.api.request.queue
 * @Description： <p> DownloadImageQueue  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/25 - 14:03
 * @Modify By：
 * @ModifyTime： 2019/7/25
 * @Modify marker：
 */
public class DownloadImageQueue extends ArrayBlockingQueue<PictureInfo> {

    private static final long serialVersionUID = -2902416059178191138L;

    /**
     * 初始化
     * @param capacity
     */
    public DownloadImageQueue(int capacity) {
        super(capacity);
    }


    private static DownloadImageQueue queue = null;

    /**
     * initCapacity
     * @param capacity
     */
    public static void initCapacity(int capacity) {
        queue = new DownloadImageQueue(capacity);
    }

    /**
     * putInfo 添加put
     * @param picInfo
     */
    public static void putInfo(PictureInfo picInfo) {
        if (null == queue) {
            return;
        }
        try {
            queue.put(picInfo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * takeInfo 移除并返回头部
     * @return
     */
    public static PictureInfo takeInfo() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * isNull
     * @return
     */
    public static boolean isNull() {
        return queue.size() == 0;
    }

    /**
     * getSize 获取queue的大小
     * @return
     */
    public static int getSize() {
        return queue.size();
    }
}
