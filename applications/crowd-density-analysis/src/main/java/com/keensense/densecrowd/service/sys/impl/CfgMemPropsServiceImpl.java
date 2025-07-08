package com.keensense.densecrowd.service.sys.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.densecrowd.entity.sys.CfgMemProps;
import com.keensense.densecrowd.mapper.sys.CfgMemPropsMapper;
import com.keensense.densecrowd.service.sys.ICfgMemPropsService;
import com.keensense.densecrowd.util.DbPropUtil;
import com.keensense.densecrowd.util.IpUtils;
import com.keensense.densecrowd.util.StringUtils;
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
    public String getW2TagServerPort() {
        String port = DbPropUtil.getString("ws2.tag.port", "9402");
        if (StringUtils.isEmpty(port)) {
            port = "9402";
        }
        return port;
    }

    @Override
    public String getH5ServerIp() {
        String server = DbPropUtil.getString("h5.server", IpUtils.getRealIpAddr());
        if (StringUtils.isEmpty(server)) {
            server = IpUtils.getRealIpAddr();
        }
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
    public int getDetectionFrameSkipInterval() {
        int limit = DbPropUtil.getInt("task.detection_frame_skip_interval", 150);
        return limit;
    }

    @Override
    public int getPushFrameMaxWaitTime() {
        int limit = DbPropUtil.getInt("task.push_frame_max_wait_time", 0);
        return limit;
    }

    @Override
    public int getDetectionScaleFactor() {
        int limit = DbPropUtil.getInt("task.detection_sale_factor", 1);
        return limit;
    }

    @Override
    public boolean getEnableDensityMapOutput() {
        boolean limit = DbPropUtil.getBoolean("task.enable_density_map_output", true);
        return limit;
    }

    @Override
    public float getHeatmapWeight() {
        float limit = DbPropUtil.getFloat("task.heatmap_weight", 0f);
        return limit;
    }
}
