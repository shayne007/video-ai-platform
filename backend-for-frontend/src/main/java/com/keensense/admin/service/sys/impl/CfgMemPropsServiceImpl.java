package com.keensense.admin.service.sys.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.constants.CommonConstants;
import com.keensense.admin.entity.sys.CfgMemProps;
import com.keensense.admin.mapper.sys.CfgMemPropsMapper;
import com.keensense.admin.service.sys.ICfgMemPropsService;
import com.keensense.admin.util.DbPropUtil;
import com.keensense.admin.util.IpUtils;
import com.keensense.admin.util.StringUtils;
import org.springframework.stereotype.Service;


@Service("cfgMemPropsService")
public class CfgMemPropsServiceImpl extends ServiceImpl<CfgMemPropsMapper, CfgMemProps> implements ICfgMemPropsService {
    @Override
    public String getWs2ServerIp() {
        String server = DbPropUtil.getString("ws2.server", IpUtils.getRealIpAddr());
        if (StringUtils.isEmpty(server)) {
            server = IpUtils.getRealIpAddr();
        }
        return server;
    }

    @Override
    public String getWs2ServerPort() {
        String port = DbPropUtil.getString("ws2.port", "9080");
        if (StringUtils.isEmpty(port)) {
            port = "9080";
        }
        return port;
    }


    @Override
    public String getH5ServerIp() {
        String server = DbPropUtil.getString("h5.server", IpUtils.getRealIpAddr());
        if (StringUtils.isEmpty(server)) {
            server = IpUtils.getRealIpAddr();
        }
        server = "172.16.1.29";
        return server;
    }

    @Override
    public String getH5ServerPort() {
        String port = DbPropUtil.getString("h5.port", "9003");
        if (StringUtils.isEmpty(port)) {
            port = "9003";
        }
        return port;
    }

    @Override
    public String getMonitorGroupLimit() {
        String limit = DbPropUtil.getString("monitor.group.limit", "50");
        if (StringUtils.isEmpty(limit)) {
            limit = "50";
        }
        return limit;
    }

    @Override
    public String getFtpServerHttpurl() {
        String ftpService = DbPropUtil.getString("ftp-server-httpurl", "127.0.0.1");
        return "http://" + ftpService + "/upload";
    }

    @Override
    public String getWsServerIp() {
        return getH5ServerIp();
    }

    @Override
    public String getWsServerPort() {
        return getH5ServerPort();
    }

    @Override
    public String getStandardIp() {
        String serviceIp = DbPropUtil.getString("standard.ip", IpUtils.getRealIpAddr());
        if (StringUtils.isEmpty(serviceIp) || CommonConstants.LOCAL_IP.equals(serviceIp)) {
            serviceIp = IpUtils.getRealIpAddr();
        }
        return serviceIp;
    }

    @Override
    public Integer getStandardPort() {
        Integer servicePort = DbPropUtil.getInt("standard.port", 9999);
        if (servicePort == null) {
            servicePort = 9999;
        }
        return servicePort;
    }

    @Override
    public Integer getScene() {
        Integer scene = DbPropUtil.getInt("analysis.scene.type", 0);
        return scene;
    }

    @Override
    public String getWs2TagServerPort() {
        String port = DbPropUtil.getString("ws2.tag.port", "9402");
        if (StringUtils.isEmpty(port)) {
            port = "9402";
        }
        return port;
    }

}
