package com.keensense.dataconvert.api.request.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.api.request.queue
 * @Description： <p> ImageDownloadClientQueue </p>
 * @Author： - Jason
 * @CreatTime：2019/7/25 - 14:10
 * @Modify By：
 * @ModifyTime： 2019/7/25
 * @Modify marker：
 */
public class ImageDownloadClientQueue extends ArrayBlockingQueue<Integer> {

    private static final long serialVersionUID = 4280862489162701907L;

    private static final Logger logger = LoggerFactory.getLogger(ImageDownloadClientQueue.class);

    /**
     * ImageDownloadClientQueue
     * @param capacity
     */
    public ImageDownloadClientQueue(int capacity) {
        super(capacity);
    }

    /**
     * ImageDownloadClientQueue
     */
    private static ImageDownloadClientQueue queue = null;

    /**
     * 初始化容量大小
     * @param capacity
     */
    public static void initCapacity(int capacity) {
        queue = new ImageDownloadClientQueue(capacity);
        try {
            for (int i = 0; i < capacity; i++) {
                queue.put(i);
            }
        } catch (Exception e) {
            logger.error("=== Exception:{} ===",e.getMessage());
        }
    }

    /**
     * take
     * @return
     */
    public static Integer takeIndex() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            logger.error("=== InterruptedException:{} ===",e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        }
    }

    /**
     * put index
     * @param index
     */
    public static void putIndex(Integer index) {
        if (null == queue) {
            return;
        }
        try {
            queue.put(index);
        } catch (InterruptedException e) {
            logger.error("=== InterruptedException:{} ===",e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public static int getSize() {
        return queue.size();
    }

}
