package com.keensense.admin.service.ext.impl;

import com.keensense.admin.service.sys.ICfgMemPropsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: cuiss
 * @Description: 调用jmanager服务接口的base类
 * @Date: 2018/10/8.
 */
@Slf4j
public abstract class AbstractService {
    static final String KS = "";

    @Autowired
    private ICfgMemPropsService cfgMemPropsService;

     /**
     * 初始化接口调用keensense
     *
     * @return
     */
    protected String initKeensenseUrl() {
        String serviceIp = cfgMemPropsService.getStandardIp();
        Integer servicePort = cfgMemPropsService.getStandardPort();
        return "http://" + serviceIp + ":" + servicePort;
    }

}
