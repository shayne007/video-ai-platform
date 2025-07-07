package com.keensense.task.util.zookeeper;

import com.keensense.common.config.SpringContext;
import com.keensense.task.config.NacosConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

import java.util.Collections;
import java.util.List;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/6/29 16:22
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
@Data
public class ZkClient {

    public CuratorFramework client;
    private NacosConfig zookeeperProperties = SpringContext.getBean(NacosConfig.class);

    private static ZkClient zkClient;

    public static ZkClient getZkClient() {
        // Double-Check idiom
        if (zkClient == null) {
            synchronized (ZkClient.class) {
                // 只需在第一次创建实例时才同步
                if (zkClient == null) {
                    zkClient = new ZkClient();
                }
            }
        }
        return zkClient;
    }

    /**
     * 注册事件监听器
     *
     * @param listener
     * @param path
     */
    public void addListener(CuratorFramework client, CuratorListener listener, String path) {
        client.getCuratorListenable().addListener(listener);

    }

    /**
     * 同步创建zk示例，原生api是异步的 这一步是设置重连策略 ExponentialBackoffRetry构造器参数： curator链接zookeeper的策略:ExponentialBackoffRetry
     * baseSleepTimeMs：初始sleep的时间 maxRetries：最大重试次数 maxSleepMs：最大重试时间
     */
    private ZkClient() {
        RetryPolicy retryPolicy =
                new ExponentialBackoffRetry(zookeeperProperties.getBaseSleepTimeMs(), zookeeperProperties.getMaxRetries());
        client = CuratorFrameworkFactory.builder()
                .connectString(zookeeperProperties.getServer())
                .connectionTimeoutMs(zookeeperProperties.getConnectionTimeoutMs())
                .sessionTimeoutMs(zookeeperProperties.getSessionTimeoutMs())
                .retryPolicy(retryPolicy)
                .build();

//        client = CuratorFrameworkFactory.newClient(zookeeperProperties.getServer(),retryPolicy);
        // 启动Curator客户端,连接zookeeper服务器
        client.start();
    }

    /**
     * @description:关闭zk客户端连接
     * @return:
     */
    public void closeZKClient() {
        if (client != null) {
            this.client.close();
        }
    }

    /**
     * 创建节点
     * <p>
     * 节点类型 1、PERSISTENT 持久化目录节点，存储的数据不会丢失。 2、PERSISTENT_SEQUENTIAL顺序自动编号的持久化目录节点，存储的数据不会丢失
     * 3、EPHEMERAL临时目录节点，一旦创建这个节点的客户端与服务器端口也就是session 超时，这种节点会被自动删除
     * 4、EPHEMERAL_SEQUENTIAL临时自动编号节点，一旦创建这个节点的客户端与服务器端口也就是session 超时，这种节点会被自动删除，并且根据当前已近存在的节点数自动加
     * 1，然后返回给客户端已经成功创建的目录节点名。
     *
     * @param path     节点名称
     * @param nodeData 节点数据
     */
    public void createNode(String path, String nodeData) {
        try {
            // 使用creatingParentContainersIfNeeded()之后Curator能够自动递归创建所有所需的父节点
            client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                    .withACL((ZooDefs.Ids.OPEN_ACL_UNSAFE)).forPath(path, nodeData.getBytes("UTF-8"));
            log.info(">>>>>>>>>>>>>>>zk create node: " + path);

        } catch (Exception e) {
            log.error("create node exception:" + e);
        }
    }

    /**
     * 删除节点数据
     */
    public void deleteNode(String path) {
        try {
            deleteNode(path, true);
            log.info(">>>>>> zk client delete zkNode:" + path);
        } catch (Exception ex) {
            log.error("{zookeeper deleteNode}", ex);
        }
    }

    /**
     * 删除节点数据
     *
     * @param deleteChildre 是否删除子节点
     */
    public void deleteNode(final String path, Boolean deleteChildre) {
        try {
            if (deleteChildre) {
                // guaranteed()删除一个节点，强制保证删除,
                // 只要客户端会话有效，那么Curator会在后台持续进行删除操作，直到删除节点成功
                client.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
            } else {
                client.delete().guaranteed().forPath(path);
            }
        } catch (Exception e) {
            log.error("{zookeeper deleteNode}", e);
        }
    }

    /**
     * 获取指定节点的数据
     */
    public String getData(String path) {
        try {
            byte[] data = client.getData().watched().forPath(path);
            if (data == null || data.length <= 0) {
                return null;
            }
            return new String(client.getData().forPath(path));
        } catch (Exception ex) {
            log.error("{zookeeper getNodeData error}", ex);
        }
        return null;
    }

    /**
     * 判断路径是否存在
     */
    public boolean isExistNode(final String path) {
        client.sync();
        try {
            return null != client.checkExists().forPath(path);
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 获取节点的子节点
     */
    public List<String> getChildren(String path) {
        try {
            return client.getChildren().forPath(path);
        } catch (Exception e) {
            log.error("zookeeper getChildren error", e);
        }
        return Collections.emptyList();
    }

}
