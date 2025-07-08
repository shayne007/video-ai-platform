package com.keensense.dataconvert.biz.service.impl;

import com.keensense.dataconvert.biz.dao.CfgMemPropsMapper;
import com.keensense.dataconvert.biz.entity.CfgMemProps;
import com.keensense.dataconvert.biz.entity.CfgMemPropsKey;
import com.keensense.dataconvert.biz.service.CfgMemPropsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.service.impl
 * @Description： <p> CfgMemPropsServiceImpl </p>
 * @Author： - Jason
 * @CreatTime：2019/7/25 - 9:21
 * @Modify By：
 * @ModifyTime： 2019/7/25
 * @Modify marker：
 */
@Service("cfgMemPropsService")
public class CfgMemPropsServiceImpl implements CfgMemPropsService {

    @Resource
    private CfgMemPropsMapper cfgMemPropsMapper;

    /**
     * findConfigByModuleAndKey
     * @param moduleName
     * @param propKey
     * @return
     */
    @Override
    public CfgMemProps findConfigByModuleAndKey(String moduleName, String propKey) {
        CfgMemPropsKey cfgMemPropsKey = new CfgMemPropsKey();
        cfgMemPropsKey.setModuleName(moduleName);
        cfgMemPropsKey.setPropKey(propKey);
        return cfgMemPropsMapper.selectByPrimaryKey(cfgMemPropsKey);
    }
}
