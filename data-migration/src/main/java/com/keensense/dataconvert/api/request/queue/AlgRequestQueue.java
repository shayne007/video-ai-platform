package com.keensense.dataconvert.api.request.queue;

import com.keensense.dataconvert.api.request.AppAlgRequest;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.api.request.queue
 * @Description： <p> AlgRequestQueue  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/25 - 14:05
 * @Modify By：
 * @ModifyTime： 2019/7/25
 * @Modify marker：
 */
public class AlgRequestQueue extends ArrayBlockingQueue<AppAlgRequest> {

    private static final long serialVersionUID = -7160837726281860928L;

    /**
     * 初始化
     * @param capacity
     */
    private AlgRequestQueue(int capacity) {
        super(capacity);
    }

    /**
     * WebserviceRequestQueue
     */
    public static volatile AlgRequestQueue queue = null;

    /**
     * initCapacity 初始化容量
     * @param capacity
     */
    public static void initCapacity(int capacity) {
        synchronized (AlgRequestQueue.class){
            if(queue == null){
                queue = new AlgRequestQueue(capacity);
            }

            try {
                for (int i = 0; i < capacity; i++) {
                    queue.put(new AppAlgRequest());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * takeRequest
     * @return
     */
    public static AppAlgRequest takeRequest() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * putRequest
     * @param request
     */
    public static void putRequest(AppAlgRequest request) {
        if (null == queue) {
            return;
        }
        try {
            queue.put(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int getSize() {
        return queue.size();
    }
}
