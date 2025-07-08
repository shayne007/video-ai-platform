package com.keensense.admin.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.admin.constants.CommonConstants;
import com.keensense.admin.entity.sys.CfgMemProps;
import com.keensense.admin.util.DbPropUtil;
import com.keensense.admin.util.IpUtils;
import com.keensense.admin.util.StringUtils;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 21:15:12
 */
public interface ICfgMemPropsService extends IService<CfgMemProps> {

    String getWs2ServerIp();

    String getWs2ServerPort();

    String getH5ServerIp();

    String getH5ServerPort();

    String getMonitorGroupLimit();

    String getFtpServerHttpurl();

    String getWsServerIp();

    String getWsServerPort();

    String getStandardIp();

    /**
     * 分析场景
     *
     * @return
     */
    Integer getScene();

    Integer getStandardPort();

    String getWs2TagServerPort();
}

