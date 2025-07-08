package com.keensense.dataconvert.biz.service;

import com.keensense.dataconvert.biz.entity.CfgMemProps;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.service
 * @Description： <p> CfgMemPropsService  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/25 - 9:21
 * @Modify By：
 * @ModifyTime： 2019/7/25
 * @Modify marker：
 */
public interface CfgMemPropsService {


    /**
     * findConfigByModuleAndKey
     * @param moduleName
     * @param propKey
     * @return
     */
    CfgMemProps findConfigByModuleAndKey(String moduleName,String propKey);



}
