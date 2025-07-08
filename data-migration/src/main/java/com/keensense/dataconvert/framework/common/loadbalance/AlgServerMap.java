package com.keensense.dataconvert.framework.common.loadbalance;

import com.keensense.dataconvert.biz.common.consts.CommonConst;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.framework.common.loadbalance
 * @Description： <p> AlgServerMap 算法map  </p>
 * @Author： - Jason
 * @CreatTime：2019/8/7 - 16:34
 * @Modify By：
 * @ModifyTime： 2019/8/7
 * @Modify marker：
 */
public class AlgServerMap {
    private AlgServerMap(){}
    private static final Logger logger = LoggerFactory.getLogger(AlgServerMap.class);

    /**
     * Key代表算法地址,Value代表该接口的权重
     */
    public static HashMap<String, Integer> serverWeightMap =  new HashMap<>();


    /**
     * 初始化接口信息
     */
    static{
        //0 为视频 && 开启了视频多接口功能
        if (CommonConst.RECOG_SERVICE_CHOSE_TYPE == 0 && CommonConst.RECOG_VIDEO_FEATURE_CLUSTER_ENABLE){
            initServerMap(CommonConst.RECOG_VIDEO_FEATURE_CLUSTER_URLS);
        }
        //1 为图片识别接口 && 开启了图片多接口功能
        if (CommonConst.RECOG_SERVICE_CHOSE_TYPE == 1 && CommonConst.RECOG_PICTURE_FEATURE_CLUSTER_ENABLE){
            initServerMap(CommonConst.RECOG_PICTURE_FEATURE_CLUSTER_URLS);
        }
    }


    /**
     * 根据配置来初始化接口
     */
    public static void initServerMap(String props){
        if (StringUtils.isNotEmpty(props)){
            String[] urls = props.split(",");
            for (int i = 0; i < urls.length; i++) {
                String[] algStr = urls[i].split("#");
                String url = algStr[0];
                String weight = algStr[1];
                if (algStr.length != 2){
                    weight = "1";
                }
                logger.info("=== urls:[{}],weight:[{}] ===",url,weight);
                serverWeightMap.put(url,Integer.valueOf(weight));
            }
        }
    }

}
