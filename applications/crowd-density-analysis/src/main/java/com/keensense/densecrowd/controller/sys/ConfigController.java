package com.keensense.densecrowd.controller.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.common.util.R;
import com.keensense.densecrowd.entity.sys.CfgMemProps;
import com.keensense.densecrowd.request.CfgMemPropsRequest;
import com.keensense.densecrowd.service.sys.ICfgMemPropsService;
import com.keensense.densecrowd.util.DbPropUtil;
import com.keensense.densecrowd.util.EntityObjectConverter;
import com.keensense.densecrowd.util.IpUtils;
import com.keensense.densecrowd.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 15:55 2019/6/14
 * @Version v0.1
 */
@RestController
@Api(tags = "常规-配置参数")
@ApiIgnore
@RequestMapping(value = "/config")
public class ConfigController {
    @Resource
    private ICfgMemPropsService cfgMemPropsService;

    public String getWs2ServerIp() {
        String server = DbPropUtil.getString("ws2.server", IpUtils.getRealIpAddr());
        if (StringUtils.isEmpty(server)) {
            server = IpUtils.getRealIpAddr();
        }
        return server;
    }

    public String getWs2ServerPort() {
        String port = DbPropUtil.getString("ws2.port", "9080");
        if (StringUtils.isEmpty(port)) {
            port = "9080";
        }
        return port;
    }


    public String getH5ServerIp() {
        String server = DbPropUtil.getString("h5.server", IpUtils.getRealIpAddr());
        if (StringUtils.isEmpty(server)) {
            server = IpUtils.getRealIpAddr();
        }
        return server;
    }

    public String getH5ServerPort() {
        String port = DbPropUtil.getString("h5.port", "9003");
        if (StringUtils.isEmpty(port)) {
            port = "9003";
        }
        return port;
    }

    public String getW2TagServerIp() {
        String server = DbPropUtil.getString("ws2.server", IpUtils.getRealIpAddr());
        if (StringUtils.isEmpty(server)) {
            server = IpUtils.getRealIpAddr();
        }
        return server;
    }

    public String getW2TagServerPort() {
        String port = DbPropUtil.getString("ws2.tag.port", "9402");
        if (StringUtils.isEmpty(port)) {
            port = "9402";
        }
        return port;
    }

    public String getMonitorGroupLimit() {
        String limit = DbPropUtil.getString("monitor.group.limit", "50");
        if (StringUtils.isEmpty(limit)) {
            limit = "50";
        }
        return limit;
    }


    public String getBusyThreshold() {
        String bsuyThreshold = DbPropUtil.getString("warning.busy.threshold", "50");
        if (StringUtils.isEmpty(bsuyThreshold)) {
            bsuyThreshold = "50";
        }
        return bsuyThreshold;
    }


    public String getCrowdThreshold() {
        String crowdThreshold = DbPropUtil.getString("warning.crowd.threshold", "60");
        if (StringUtils.isEmpty(crowdThreshold)) {
            crowdThreshold = "60";
        }
        return crowdThreshold;
    }


    public String getAlarmThreshold() {
        String alarmThreshold = DbPropUtil.getString("warning.alarm.threshold", "70");
        if (StringUtils.isEmpty(alarmThreshold)) {
            alarmThreshold = "70";
        }
        return alarmThreshold;
    }

    public String getStandardIp() {
        String standardIp = DbPropUtil.getString("standard.ip", "127.0.0.1");
        if (StringUtils.isEmpty(standardIp)) {
            standardIp = "127.0.0.1";
        }
        return standardIp;
    }

    public String getStandardPort() {
        String standardPort = DbPropUtil.getString("standard.port", "9999");
        if (StringUtils.isEmpty(standardPort)) {
            standardPort = "9999";
        }
        return standardPort;
    }

    public String getSysAuthorizeConnectNumber() {
        String sysAuthorizeConnectNumber = DbPropUtil.getString("task-authorize-connect-number", "100");
        if (StringUtils.isEmpty(sysAuthorizeConnectNumber)) {
            sysAuthorizeConnectNumber = "100";
        }
        return sysAuthorizeConnectNumber;
    }

    public String getFtpServerHttpurl() {
        String FtpServerHttpurl = DbPropUtil.getString("ftp-server-httpurl", "127.0.0.1:8081");
        if (StringUtils.isEmpty(FtpServerHttpurl)) {
            FtpServerHttpurl = "127.0.0.1:8081";
        }
        return FtpServerHttpurl;
    }

    @ApiOperation("获取全局配置(完善中)")
    @PostMapping(value = "/selectAllConfigMap")
    public R selectAllConfigMap() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("warning.busy.threshold", getBusyThreshold());
        configMap.put("warning.crowd.threshold", getCrowdThreshold());
        configMap.put("warning.alarm.threshold", getAlarmThreshold());
        configMap.put("standard.ip", getStandardIp());
        configMap.put("standard.port", getStandardPort());
        configMap.put("ws2.server", getWs2ServerIp());
        configMap.put("ws2.port", getWs2ServerPort());
        configMap.put("ftp-server-httpurl", getFtpServerHttpurl());
        configMap.put("h5.server", getH5ServerIp());
        configMap.put("h5.port", getH5ServerPort());
        configMap.put("ws2.tag.server", getW2TagServerIp());
        configMap.put("ws2.tag.port", getW2TagServerPort());
        configMap.put("sys-authorize-connect-number", getSysAuthorizeConnectNumber());
        configMap.put("monitor.group.limit", getMonitorGroupLimit());
        configMap.put("map_latitude", DbPropUtil.getString("map-latitude", "28.195033121678538"));
        configMap.put("map_longitude", DbPropUtil.getString("map-longitude", "112.97601699829102"));
        return R.ok().put("configMap", configMap);
    }


    @ApiOperation("查询管理服务配置")
    @PostMapping(value = "getManagerConfig")
    public R getManagerConfig() {
        R result = R.ok();
        List<CfgMemProps> cfgMemProps =
                cfgMemPropsService.getBaseMapper().selectList(new QueryWrapper<CfgMemProps>().eq("module_name",
                        "qst_densecrowd"));
        result.put("list", cfgMemProps);
        return result;
    }


    @ApiOperation("保存管理服务配置")
    @PostMapping(value = "updateManagerConfig")
    public R updateManagerConfig(@RequestBody CfgMemPropsRequest cfgMemPropsRequest) {
        R result = R.ok();
        if ("warning.busy.alarmThreshold".equals(cfgMemPropsRequest.getPropKey())) {
            Integer crowd = Integer.parseInt(getCrowdThreshold());
            Integer busy = Integer.parseInt(cfgMemPropsRequest.getPropValue());
            if (crowd < busy) {
                result.put("code", 500);
                result = R.error().put("msg", "拥挤阈值不能小于繁忙阈值");
                return result;
            }
        }
        if ("warning.crowd.alarmThreshold".equals(cfgMemPropsRequest.getPropKey())) {
            Integer busy = Integer.parseInt(getBusyThreshold());
            Integer crowd = Integer.parseInt(cfgMemPropsRequest.getPropValue());
            if (crowd < busy) {
                result.put("code", 500);
                result = R.error().put("msg", "拥挤阈值不能小于繁忙阈值");
                return result;
            }
        }
        CfgMemProps cfgMemProps = EntityObjectConverter.getObject(cfgMemPropsRequest, CfgMemProps.class);
        cfgMemProps.setUpdateTime(new Date());
        boolean update = cfgMemPropsService.update(cfgMemProps, new QueryWrapper<CfgMemProps>().eq("module_name", "qst_densecrowd").eq("prop_key", cfgMemPropsRequest.getPropKey()));
        if (update) {
            return R.ok();
        } else {
            return R.error("更新失败");
        }

    }

    @ApiOperation("查询分析服务配置")
    @PostMapping(value = "getAnalysisConfig")
    public R getAnalysisConfig() {
        R result = R.ok();
        List<CfgMemProps> cfgMemProps = cfgMemPropsService.getBaseMapper().selectList(new QueryWrapper<CfgMemProps>().eq("module_name", "u2s_recog"));
        result.put("list", cfgMemProps);
        return result;
    }

    @ApiOperation("保存分析服务配置")
    @PostMapping(value = "updateAnalysisConfig")
    public R updateAnalysisConfig(@RequestBody CfgMemPropsRequest cfgMemPropsRequest) {
        CfgMemProps cfgMemProps = EntityObjectConverter.getObject(cfgMemPropsRequest, CfgMemProps.class);
        cfgMemProps.setUpdateTime(new Date());
        boolean update = cfgMemPropsService.update(cfgMemProps, new QueryWrapper<CfgMemProps>().eq("module_name", "u2s_recog").eq("prop_key", cfgMemPropsRequest.getPropKey()));
        if (update) {
            return R.ok();
        } else {
            return R.error("更新失败");
        }
    }
}
