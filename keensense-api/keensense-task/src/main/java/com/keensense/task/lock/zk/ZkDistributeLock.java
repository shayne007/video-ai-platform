package com.keensense.task.lock.zk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.keensense.common.config.SpringContext;
import com.keensense.common.exception.VideoException;
import com.keensense.task.config.NacosConfig;
import com.keensense.task.schedule.MasterSchedule;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: ZkDistributeLock
 * @Description: Zookeeper分布式锁
 * @Author: cuiss
 * @CreateDate: 2020/5/18 17:58
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class ZkDistributeLock implements Lock, Watcher {

    /**
     * zk服务集群
     */
    private static String zkServers;

    /**
     * 分布式锁的根节点
     */
    private static final String LOCK_ROOT_PATH = "/tLock";

    /**
     * 分布式锁的前缀
     */
    private static final String LOCK_PREFIX = "-lk-";

    /**
     * 连接zookeeper服务器的超时时间
     */
    private static final int DEFAULT_SESSION_TIMEOUT = 30000;

    public static final String DEFAULT_LOCK_NAME = "task";

    /**
     * zk客户端
     */
    private ZooKeeper zk;

    /**
     * 竞争资源的标志
     */
    private String lockName;

    /**
     * 等待前一个锁
     */
    private String waitNode;

    /**
     * 当前锁
     */
    private String myZnode;

    /**
     * 计数器
     */
    private CountDownLatch latch;

    /**
     * 连接zookeeper服务器的超时时间
     */
    private int sessionTimeout;

    static {
        NacosConfig nacosConfig = SpringContext.getBean(NacosConfig.class);
        zkServers = nacosConfig.getServer();

    }

    public void init() {
        try {
            log.info(">>>>> zkservers:{}", zkServers);
            zk = new ZooKeeper(zkServers, sessionTimeout, this);
            Stat stat = zk.exists(LOCK_ROOT_PATH, false);
            if (stat == null) {
                // 创建根节点
                zk.create(LOCK_ROOT_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            log.error(">>>>>>>>>Exception:{}", e.getMessage());
            throw new VideoException(e.getMessage());
        }
    }

    public void destroy() {
        this.unlock();
    }

    public ZkDistributeLock() {
        this(DEFAULT_LOCK_NAME);
    }

    public ZkDistributeLock(String lockName) {
        this(lockName, DEFAULT_SESSION_TIMEOUT);
    }

    /**
     * @param lockName
     * @param sessionTimeout
     */
    public ZkDistributeLock(String lockName, int sessionTimeout) {
        if (lockName.contains(LOCK_PREFIX)) {
            throw new VideoException("lockName 不能包含[" + LOCK_PREFIX + "]");
        }
        this.lockName = lockName;
        this.sessionTimeout = sessionTimeout;
    }

    @Override
    public void lock() {
        if (this.tryLock()) {
            return;
        } else {
            try {
                waitForLock(waitNode, sessionTimeout);
            } catch (Exception e) {
                log.error(">>>>>>>>>Exception:{}", e.getMessage());
                throw new VideoException(e.getMessage());
            }
        }

    }

    @Override
    public void lockInterruptibly() {
        this.lock();
    }

    @Override
    public boolean tryLock() {
        try {
            // 创建临时子节点---zookeeper临时有序节点 /tLock/test-lk-0000000001
            myZnode = zk.create(LOCK_ROOT_PATH + "/" + lockName + LOCK_PREFIX, MasterSchedule.getMasterIp().getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            // 取出所有子节点
            List<String> subNodes = zk.getChildren(LOCK_ROOT_PATH, false);
            if (subNodes.size() == 1) {
                log.info("================>>> get {} lock....", lockName);
                return true;
            }
            // 取出所有lockName的锁
            List<String> lockObjNodes = subNodes.stream()
                    .filter(node -> node.split(LOCK_PREFIX)[0].equals(lockName))
                    .collect(Collectors.toList());
            Collections.sort(lockObjNodes);
            // 如果是最小的节点,则表示取得锁
            if (myZnode.equals(LOCK_ROOT_PATH + "/" + lockObjNodes.get(0))) {
                log.info("================>>> get {} lock....", lockName);
                return true;
            }
            if (lockObjNodes.size() > 1) {
                // 如果不是最小的节点，找到比自己小1的节点
                String myZnodeName = myZnode.substring(myZnode.lastIndexOf("/") + 1);
                waitNode = lockObjNodes.get(Collections.binarySearch(lockObjNodes, myZnodeName) - 1);
            }
        } catch (KeeperException e) {
            log.error(">>>>>>>>> Exception:{}", e.getMessage());
            throw new VideoException(e.getMessage());
        } catch (InterruptedException e) {
            log.error(">>>>>>>>> Exception:{}", e.getMessage());
            throw new VideoException(e.getMessage());
        }
        log.info("================>>> cann't get {} lock....", lockName);
        return false;
    }

    public boolean hasCurrentLock() {
        boolean ret = false;
        // 取出所有子节点
        try {
            List<String> subNodes = zk.getChildren(LOCK_ROOT_PATH, false);

            if (subNodes == null || subNodes.size() <= 1) {
                log.info("================>>> get {} lock....", lockName);
                return true;
            }
            // 取出所有lockName的锁
            List<String> lockObjNodes = new ArrayList<>();
            for (String node : subNodes) {
                if (node.split(LOCK_PREFIX)[0].equals(lockName)) {
                    lockObjNodes.add(node);
                }
            }
            Collections.sort(lockObjNodes);
            // 如果是最小的节点,则表示取得锁
            if (myZnode.equals(LOCK_ROOT_PATH + "/" + lockObjNodes.get(0))) {
                ret = true;
            }
        } catch (Exception e) {
            log.error(">>>>>> Exception:{}", e.getMessage());
            throw new VideoException(e.getMessage());
        }
        if (ret) {
            log.info("================>>> get {} lock....", lockName);
        } else {
            log.info("================>>> cann't get {} lock....", lockName);
        }
        return ret;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) {
        try {
            if (tryLock()) {
                return true;
            } else {
                return waitForLock(waitNode, time);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public void unlock() {
        try {
            zk.delete(myZnode, -1);
            log.info(">>>>>>>>>>> delete znode:{}", myZnode);
            myZnode = null;
            zk.close();
        } catch (InterruptedException e) {
            log.error(">>>>>>>>> Exception:{}", e.getMessage());
            e.printStackTrace();
        } catch (KeeperException e) {
            log.error(">>>>>>>>> Exception:{}", e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * @param waitNode 等待节点
     * @param waitTime 等待时间
     * @return
     * @throws Exception
     */
    private boolean waitForLock(String waitNode, long waitTime) throws Exception {
        Stat stat = zk.exists(LOCK_ROOT_PATH + "/" + waitNode, true);
        // 判断比自己小一个数的节点是否存在,如果不存在则无需等待锁
        if (stat != null) {
            this.latch = new CountDownLatch(1);
            this.latch.await(waitTime, TimeUnit.MILLISECONDS);
            this.latch = null;
        }
        return true;
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    /**
     * zookeeper的监视事件,当有节点删除时，countDown()减1，表示有节点释放锁
     *
     * @param watchedEvent
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        if (this.latch != null) {
            // 监视到有节点删除，计数器减1
            if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
                this.latch.countDown();
            }
        }

    }

    public static void main(String[] args) {

        new Thread(() -> {
            ZkDistributeLock zkDistributeLock = new ZkDistributeLock("test");
            zkDistributeLock.init();
            // 首次获取锁
            zkDistributeLock.lock();
            log.info("thread 1 get lock:{}", zkDistributeLock.myZnode);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            zkDistributeLock.unlock();
        }).start();

        new Thread(() -> {
            ZkDistributeLock zkDistributeLock = new ZkDistributeLock("test");
            zkDistributeLock.init();
            // 首次获取锁
            zkDistributeLock.lock();
            log.info("thread 2 get lock:{}", zkDistributeLock.myZnode);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            zkDistributeLock.unlock();
        }).start();

        new Thread(() -> {
            ZkDistributeLock zkDistributeLock = new ZkDistributeLock("test", 1000);
            zkDistributeLock.init();
            // 首次获取锁
            zkDistributeLock.lock();
            log.info("thread 3 get lock:{}", zkDistributeLock.myZnode);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            zkDistributeLock.unlock();
        }).start();

        new Thread(() -> {
            ZkDistributeLock zkDistributeLock = new ZkDistributeLock("test", 1000);
            zkDistributeLock.init();
            // 首次获取锁
            zkDistributeLock.lock();
            log.info("thread 4 get lock:{}", zkDistributeLock.myZnode);
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            zkDistributeLock.lock();
            zkDistributeLock.unlock();
        }).start();

    }
}
