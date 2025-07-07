package com.keensense.task.schedule;

import com.keensense.task.cache.VsdTaskCache;
import com.keensense.task.entity.VsdTask;
import com.keensense.task.listener.StartListener;
import com.keensense.task.mapper.VsdSlaveMapper;
import com.keensense.task.mapper.VsdTaskMapper;
import com.keensense.task.service.IVsdTaskService;
import com.keensense.task.util.oldclean.MysqlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: vsdTask表相关定时任务
 * @Author: wujw
 * @CreateDate: 2019/5/17 14:29
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Component
public class VsdTaskSchedule {

    @Autowired
    public VsdTaskSchedule(VsdTaskMapper vsdTaskMapper,VsdSlaveMapper vsdSlaveMapper){
        this.vsdTaskMapper = vsdTaskMapper;
        this.vsdSlaveMapper = vsdSlaveMapper;
    }

    private final VsdTaskMapper vsdTaskMapper;

    private final VsdSlaveMapper vsdSlaveMapper;

    @Autowired
    private IVsdTaskService vsdTaskService;

    /***
     * @description: 更新vsdTask缓存
     * @return: void
     */
    @Deprecated
    //@Scheduled(cron = "1/5 * * * * ?")
    public void updateVadTaskCache() {
        List<VsdTask> list = vsdTaskMapper.selectByRunningDetail();
        //有数据时才更新缓存
        if(!list.isEmpty()){
            Map<String,VsdTask> cache = new HashMap<>(list.size());
            for(VsdTask vsdTask : list){
                cache.put(vsdTask.getSerialnumber(), vsdTask);
            }
            VsdTaskCache.setVsdTaskCache(cache);
        }
    }

    /***
     * @description: mysql模式下，如果分析模块心跳停止，重置分析模块任务状态(已弃用MySQL模式)
     * @return: void
     */
    @Scheduled(cron = "0 1/1 * * * ?")
    public void updateVsdTask() {
        if(StartListener.zkDistributeLock.hasCurrentLock()){
            vsdSlaveMapper.updateTimeOut();
            if(MysqlUtil.isMysql()){
                vsdTaskMapper.updateTimeOutTask();
            }
        }
    }
}
