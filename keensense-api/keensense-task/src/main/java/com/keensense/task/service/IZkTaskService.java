package com.keensense.task.service;

/**
 * @Description: vsdTask Service
 * @Author: wujw
 * @CreateDate: 2019/5/17 11:22
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public interface IZkTaskService {

    /**
     * @description: 定时器创建zk任务
     */
    void addZkTask();

    /**
     * @description: 定时器触发暂停zk任务
     */
    void stopZkTask();

    /**
     * @description: 定时器更新zk任务进度
     */
    void updExectaskProgress();

    /**
     * @description: 定时器完成zk任务进度
     */
    void updExectaskEnd();

    /**
     * @description: 定时器完成zk节点任务进度
     */
    void monitorZkNode();

    /**
     * @description: 重置异常任务
     */
    void resetErrorTask();

    /**
     * @description: 删除重复任务
     */
    void deleteRepeat();

    /**
     * @description: 判断分析节点是否已存在
     */
    boolean isNodeExisted();

    /**
     * 由于任务状态延迟，部分任务加入zk之后没有获取到分析资源，
     * 这部分任务需要重新加载成待分析状态
     */
    void reloadTask();
}
