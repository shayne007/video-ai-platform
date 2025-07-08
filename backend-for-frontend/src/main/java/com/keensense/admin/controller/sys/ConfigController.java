package com.keensense.admin.controller.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.admin.annotation.Login;
import com.keensense.admin.base.BaseController;
import com.keensense.admin.config.ImageSearchConfig;
import com.keensense.admin.entity.sys.CfgMemProps;
import com.keensense.admin.request.CfgMemPropsRequest;
import com.keensense.admin.service.sys.ICfgMemPropsService;
import com.keensense.admin.service.sys.ITokenService;
import com.keensense.admin.util.DbPropUtil;
import com.keensense.admin.util.EntityObjectConverter;
import com.keensense.common.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@Slf4j
@Api(tags = "常规-配置参数")
@RequestMapping(value = "/config")
public class ConfigController extends BaseController {
    @Resource
    private ICfgMemPropsService cfgMemPropsService;

    @Autowired
    private ImageSearchConfig imageSearchConfig;

    @Resource
    private ITokenService tokenService;

    static Map<String, Boolean> refreshKey = new HashMap<>();

    static {
        refreshKey.put("ws2.server", true);
    }

    @Login
    @ApiOperation("获取全局配置(完善中)")
    @PostMapping(value = "/selectAllConfigMap")
    public R selectAllConfigMap() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("h5.server", cfgMemPropsService.getH5ServerIp());
        configMap.put("h5.port", cfgMemPropsService.getH5ServerPort());

        configMap.put("ws2.server", cfgMemPropsService.getWs2ServerIp());
        configMap.put("ws2.port", cfgMemPropsService.getWs2ServerPort());

        configMap.put("ws2.tag.port", cfgMemPropsService.getWs2TagServerPort());

        configMap.put("ws.server", cfgMemPropsService.getWsServerIp());
        configMap.put("ws.port", cfgMemPropsService.getWsServerPort());

        configMap.put("monitor.group.limit", cfgMemPropsService.getMonitorGroupLimit());

        configMap.put("topgetMem", String.valueOf(imageSearchConfig.getTopgetMem()));

        configMap.put("map_latitude", DbPropUtil.getString("map-latitude", "28.195033121678538"));
        configMap.put("map_longitude", DbPropUtil.getString("map-longitude", "112.97601699829102"));
        return R.ok().put("configMap", configMap);
    }


    @ApiOperation("查询管理服务配置")
    @PostMapping(value = "getManagerConfig")
    public R getManagerConfig() {
        R result = R.ok();
        List<CfgMemProps> cfgMemProps = cfgMemPropsService.getBaseMapper().selectList(new QueryWrapper<CfgMemProps>().eq("module_name", "qst_u2s"));
        result.put("list", cfgMemProps);
        return result;
    }


    @ApiOperation("保存管理服务配置")
    @PostMapping(value = "updateManagerConfig")
    public R updateManagerConfig(@RequestBody CfgMemPropsRequest cfgMemPropsRequest) {
        CfgMemProps cfgMemProps = EntityObjectConverter.getObject(cfgMemPropsRequest, CfgMemProps.class);
        cfgMemProps.setUpdateTime(new Date());
        String propKey = cfgMemPropsRequest.getPropKey();
        boolean update = cfgMemPropsService.update(cfgMemProps, new QueryWrapper<CfgMemProps>().eq("module_name", "qst_u2s").eq("prop_key", propKey));
        if (update) {
            if (refreshKey.get(propKey) != null && refreshKey.get(propKey)) {
                tokenService.expireToken(getUserId());
            }
            DbPropUtil.PROP_MAP.put(cfgMemProps.getPropKey(), cfgMemProps.getPropValue());
            log.info("管理服务配置参数:" + DbPropUtil.PROP_MAP);
            return R.ok();
        } else {
            return R.error("更新失败");
        }

    }
}
