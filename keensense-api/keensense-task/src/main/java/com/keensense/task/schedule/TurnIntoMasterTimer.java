package com.keensense.task.schedule;

import com.keensense.task.listener.StartListener;
import com.keensense.task.lock.zk.ZkDistributeLock;
import com.keensense.task.mapper.CfgMemPropsMapper;
import com.keensense.task.mapper.VsdSlaveMapper;
import com.loocme.sys.constance.DateFormatConst;
import com.loocme.sys.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
//@Component
@Deprecated
public class TurnIntoMasterTimer {

    @Autowired
    public TurnIntoMasterTimer(CfgMemPropsMapper cfgMemPropsMapper, VsdSlaveMapper vsdSlaveMapper){
        this.cfgMemPropsMapper = cfgMemPropsMapper;
        this.vsdSlaveMapper = vsdSlaveMapper;
    }

    private static boolean isMaster = false;

    private final CfgMemPropsMapper cfgMemPropsMapper;
    private final VsdSlaveMapper vsdSlaveMapper;


    @Scheduled(cron = "0/5 * * * * ?")
    public void updateSlaveId() {
        vsdSlaveMapper.updateSlaveId(MasterSchedule.getMasterId(), MasterSchedule.getMasterIp());
    }

    @Scheduled(cron = "1/2 * * * * ?")
    public void turnIntoMaster() {
        log.info(">>>>>>zk lock:{}", StartListener.zkDistributeLock.hasCurrentLock());
        if (isMaster) {
            // 校验并更新数据库代表存活
            if (0 == cfgMemPropsMapper.heart(MasterSchedule.getMasterId(), DateUtil.getFormat(new Date(), DateFormatConst.YMDHMS_))) {
                setIsMaster(false);
            }
        } else {
            cfgMemPropsMapper.updateConfig();
            // 抢占成为master
            if (cfgMemPropsMapper.changeMaster(MasterSchedule.getMasterId(), DateUtil.getFormat(new Date(), DateFormatConst.YMDHMS_)) == 1) {
                setIsMaster(true);
            }
        }
    }

    public static boolean getIsMaster() {
        return TurnIntoMasterTimer.isMaster;
    }

    public static void setIsMaster(boolean isMaster) {
        TurnIntoMasterTimer.isMaster = isMaster;
    }
}
