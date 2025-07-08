package com.keensense.dataconvert.api.util;

import com.alibaba.fastjson.JSONObject;
import com.keensense.dataconvert.api.handler.IEs2EsHandler;
import com.keensense.dataconvert.biz.common.consts.CommonConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.api.util
 * @Description： <p> EsMigrateUtil </p>
 * @Author： - Jason
 * @CreatTime：2019/7/27 - 16:05
 * @Modify By：
 * @ModifyTime： 2019/7/27
 * @Modify marker：
 */
public class EsMigrateUtil {

    private static final Logger logger = LoggerFactory.getLogger(EsMigrateUtil.class);

    /**
     * 开始启动处理
     * @param startConfig
     */
    public static void startMigrate(JSONObject startConfig){
        IEs2EsHandler esHandler = CommonConst.getEsHandler();
        if (null == esHandler) {
            logger.warn("=== esHandler is Null please check your cfg ... ");
            return;
        }
        logger.info("=== loadSourceData:config:[{}] ",startConfig);
        esHandler.loadSourceData(startConfig);
    }


}
