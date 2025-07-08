package com.keensense.densecrowd.controller.viid;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.common.util.StandardHttpUtil;
import com.keensense.densecrowd.entity.task.DensecrowdWarnResult;
import com.keensense.densecrowd.entity.task.VsdTaskRelation;
import com.keensense.densecrowd.service.sys.ICfgMemPropsService;
import com.keensense.densecrowd.service.task.IDensecrowdWarnResultService;
import com.keensense.densecrowd.service.task.IVsdTaskRelationService;
import com.keensense.densecrowd.util.CommonConstants;
import com.keensense.densecrowd.util.DbPropUtil;
import com.keensense.densecrowd.util.EhcacheUtils;
import com.keensense.densecrowd.util.IpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 15:43 2020/7/22
 * @Version v0.1
 */
@Slf4j
@Api(tags = "视图库接口")
@RestController
@RequestMapping("/VIID")
public class ViidController {
    @Autowired
    private ICfgMemPropsService cfgMemPropsService;

    @Autowired
    private IDensecrowdWarnResultService densecrowdWarnResultService;

    @Autowired
    IVsdTaskRelationService vsdTaskRelationService;


    @ApiOperation("人群密度结构化数据存储")
    @PostMapping("/CrowdDensity")
    public String sendDataToKeensens(@RequestBody String content) {
        String serviceIp = DbPropUtil.getString("standard.ip", IpUtils.getRealIpAddr());
        if (StringUtils.isEmpty(serviceIp) || CommonConstants.LOCAL_IP.equals(serviceIp)) {
            serviceIp = IpUtils.getRealIpAddr();
        }
        String servicePort = DbPropUtil.getString("standard.port", "9999");
        String standardUrl = "http://" + serviceIp + ":" + servicePort + "/VIID/CrowdDensity";
        String data = StandardHttpUtil.postContentWithJson(standardUrl, content);

        JSONObject jsonObject = JSON.parseObject(content);

        JSONArray dataArray = jsonObject.getJSONObject("CrowdDensityListObject").getJSONArray("CrowdDensityObject");
        for (int i = 0; i < dataArray.size(); i++) {
            Object result = JSON.parseObject(dataArray.getString(i), DensecrowdWarnResult.class);
            DensecrowdWarnResult densecrowdWarnResult = (DensecrowdWarnResult) result;
            String serialnumber = densecrowdWarnResult.getSerialnumber();
            VsdTaskRelation vsdTaskRelation = (VsdTaskRelation) EhcacheUtils.getItem(serialnumber);
            if (vsdTaskRelation == null) {
                vsdTaskRelation = vsdTaskRelationService.queryVsdTaskRelationBySerialnumber(serialnumber);
                if (vsdTaskRelation == null) {
                    vsdTaskRelation = new VsdTaskRelation();
                    vsdTaskRelation.setId(null);
                }
                EhcacheUtils.putItem(serialnumber, vsdTaskRelation);
            }
            if (vsdTaskRelation.getAlarmThreshold() <= densecrowdWarnResult.getCount()) {
                densecrowdWarnResult.setAlarmThreshold(vsdTaskRelation.getAlarmThreshold());
                densecrowdWarnResultService.saveOrUpdate(densecrowdWarnResult);
            }
        }


        log.info("VIID:" + data);
        return data;
    }

}
