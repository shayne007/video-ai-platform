package com.keensense.dataconvert.framework.config.http;

import org.apache.http.conn.HttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.framework.config.http
 * @Description： <p> IdleConnectionEvictor - 可用 但无明显效果 注释掉 </p>
 * @Author： - Jason
 * @CreatTime：2019/7/31 - 14:54
 * @Modify By：
 * @ModifyTime： 2019/7/31
 * @Modify marker：
 */

//@Component
public class IdleConnectionEvictor extends Thread{


    @Autowired
    private HttpClientConnectionManager connMgr;

    private volatile boolean shutdown;

    public IdleConnectionEvictor() {
        super();
        super.start();
    }

    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(5000);
                    // 关闭失效的连接
                    connMgr.closeExpiredConnections();
                }
            }
        } catch (InterruptedException ex) {
            // 结束
        }
    }

    /**
     * 关闭清理无效连接的线程
     */
    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }
}
