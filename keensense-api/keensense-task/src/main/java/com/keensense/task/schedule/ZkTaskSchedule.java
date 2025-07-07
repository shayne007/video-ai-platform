package com.keensense.task.schedule;

import com.keensense.task.config.NacosConfig;
import com.keensense.task.listener.StartListener;
import com.keensense.task.service.IZkTaskService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ZkTaskSchedule {

    @Autowired
    private IZkTaskService iZkTaskService;
    @Autowired
    private NacosConfig nacosConfig;

    @Scheduled(cron = "1/5 * * * * ?")
    public void addZkTask() {
        try {
            if (StartListener.zkDistributeLock.hasCurrentLock() && nacosConfig.getZkSwitch()) {
                log.info(">>>>>>>> current zookeeper server is {} .....",nacosConfig.getServer());
                if(!iZkTaskService.isNodeExisted()){
                    log.info(">>>>>>>>>no node exist....");
                    return;
                }
                //检查节点信息，回收挂掉的节点任务
                iZkTaskService.monitorZkNode();
                //重置异常任务
                iZkTaskService.resetErrorTask();
                //reload 因资源不足的任务
                iZkTaskService.reloadTask();
                //结束任务
                iZkTaskService.updExectaskEnd();
                //更新进度
                iZkTaskService.updExectaskProgress();
                //分发任务
                iZkTaskService.addZkTask();
            }else{
                log.info(">>>>>>>> current node is not master role or not use zk model .....");
            }
        } catch (Exception e) {
            log.error("addZkTask error", e);
        }
    }

    @Scheduled(cron = "1/3 * * * * ?")
    public void stopZkTask() {
        try {
            if (StartListener.zkDistributeLock.hasCurrentLock() && nacosConfig.getZkSwitch() && iZkTaskService.isNodeExisted()) {
                iZkTaskService.stopZkTask();
            }
        } catch (Exception e) {
            log.error("stopZkTask error", e);
        }
    }

    //@Scheduled(cron = "0 3/5 * * * ?")
    public void deleteRepeat() {
        try {
            if (StartListener.zkDistributeLock.hasCurrentLock() && nacosConfig.getZkSwitch() && iZkTaskService.isNodeExisted()) {
                iZkTaskService.deleteRepeat();
            }
        } catch (Exception e) {
            log.error("stopZkTask error", e);
        }
    }
}
