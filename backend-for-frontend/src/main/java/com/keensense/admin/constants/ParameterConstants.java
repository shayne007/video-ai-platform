package com.keensense.admin.constants;

import com.loocme.sys.util.StringUtil;

/**
 * 配置参数常量 与parameter.properties 配置参数相同
 *
 * @author admin
 */
public final class ParameterConstants {
    private ParameterConstants() {
    }

    /**
     * 实时视频快照获取接口
     * index.page.flag=1
     */
    public static final String INDEX_PAGE_FLAG = "index.page.flag";

    /**
     * 离线动态展示界面配置  1 为默认  2：
     */
    public static final String OFFLINE_DYNAMIC_PAGE_FLAG = "offline.dynamic.page.flag";

    /**
     * 任务管理是否增加 卡口车辆结构化标志
     */
    public static final String GATE_VEHICLE_STRUCTURE_FLAG = "gate.vehicle.structure.flag";


    /**
     * 获取slave_ip
     *
     * @return
     */
    public static String getSlaveIp() {
        String slaveip = "127.0.0.1";
        if (StringUtil.isNotNull(CommonConstants.SERVICE_DEPLOY_ADDRESS)) {
            slaveip = CommonConstants.SERVICE_DEPLOY_ADDRESS;
        }
        return slaveip;
    }
}
