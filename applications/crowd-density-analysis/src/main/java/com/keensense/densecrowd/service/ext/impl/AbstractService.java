package com.keensense.densecrowd.service.ext.impl;

import com.keensense.densecrowd.util.CommonConstants;
import com.keensense.densecrowd.util.DbPropUtil;
import com.keensense.densecrowd.util.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

/**
 * @Author: cuiss
 * @Description: 调用jmanager服务接口的base类
 * @Date: 2018/10/8.
 */
@Slf4j
public abstract class AbstractService {

    /**
     * 初始化接口调用keensense
     *
     * @return
     */
    protected String initKeensenseUrl() {
        String serviceIp = DbPropUtil.getString("standard.ip", IpUtils.getRealIpAddr());
        if (StringUtils.isEmpty(serviceIp) || CommonConstants.LOCAL_IP.equals(serviceIp)) {
            serviceIp = IpUtils.getRealIpAddr();
        }
        String servicePort = DbPropUtil.getString("standard.port", "9999");
        return "http://" + serviceIp + ":" + servicePort;
    }

}
