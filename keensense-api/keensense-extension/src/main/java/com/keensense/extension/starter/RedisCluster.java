package com.keensense.extension.starter;/**
 * Created by zhanx xiaohui on 2019/8/26.
 */

import cn.jiuling.plugin.extend.featureclust.FaceFeatureClusterSpringRedis;
import cn.jiuling.plugin.extend.featureclust.ReidFeatureClusterSpringRedis;
import com.keensense.sdk.algorithm.impl.BoxQstFaceSdkInvokeImpl;
import com.keensense.sdk.algorithm.impl.GLQstFaceSdkInvokeImpl;
import com.keensense.sdk.algorithm.impl.QstFaceSdkInvokeImpl;
import com.keensense.sdk.algorithm.impl.StQstFaceSdkInvokeImpl;
import com.keensense.sdk.constants.FaceConstant;
import com.keensense.sdk.sys.utils.DbPropUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/8/26 14:24
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Component
public class RedisCluster {
    @Resource(name = "bodyRedisTemplate")
    private RedisTemplate<String, String> bodyRedisTemplate;
    @Resource(name = "faceRedisTemplate")
    private RedisTemplate<String, String> faceRedisTemplate;

    public void initRedisCluster() {
        if (FaceConstant.getFaceSdkInvoke() instanceof QstFaceSdkInvokeImpl) {
            FaceFeatureClusterSpringRedis.initInstance(faceRedisTemplate,
                    24 * 60 * 60, cn.jiuling.plugin.extend.FaceConstant.TYPE_COMPANY_QST,
                    DbPropUtil.getFloat("face.feature.cluster", 0.8f));
        }
        if (FaceConstant.getFaceSdkInvoke() instanceof GLQstFaceSdkInvokeImpl) {
            FaceFeatureClusterSpringRedis.initInstance(faceRedisTemplate,
                    24 * 60 * 60, cn.jiuling.plugin.extend.FaceConstant.TYPE_COMPANY_GLST,
                    DbPropUtil.getFloat("face.feature.cluster", 0.8f));
        }
        if (FaceConstant.getFaceSdkInvoke() instanceof StQstFaceSdkInvokeImpl) {
            FaceFeatureClusterSpringRedis.initInstance(faceRedisTemplate,
                    24 * 60 * 60, cn.jiuling.plugin.extend.FaceConstant.TYPE_COMPANY_ST,
                    DbPropUtil.getFloat("face.feature.cluster", 0.8f));
        }
        if (FaceConstant.getFaceSdkInvoke() instanceof BoxQstFaceSdkInvokeImpl) {
            FaceFeatureClusterSpringRedis.initInstance(faceRedisTemplate,
                    24 * 60 * 60, cn.jiuling.plugin.extend.FaceConstant.TYPE_COMPANY_STM,
                    DbPropUtil.getFloat("face.feature.cluster", 0.8f));
        }
        ReidFeatureClusterSpringRedis.initInstance(24 * 60 * 60);
    }
}

