package com.keensense.dataconvert.api.alg.impl;

import com.keensense.dataconvert.api.alg.IAlgorithm;
import com.keensense.dataconvert.api.request.vo.AlgKafkaVo;
import com.keensense.dataconvert.api.util.PushToKafkaUtil;
import com.keensense.dataconvert.api.util.area.AreaCompare;
import com.keensense.dataconvert.biz.common.consts.CommonConst;
import com.keensense.dataconvert.biz.entity.PictureInfo;
import com.keensense.dataconvert.biz.kafka.producer.AppProducer;
import com.keensense.dataconvert.framework.common.ext.spring.SpringContextHolder;
import com.keensense.dataconvert.framework.common.loadbalance.WeightRoundRobin;
import com.keensense.dataconvert.framework.common.utils.cache.EhCacheUtil;
import com.keensense.dataconvert.framework.config.ehcache.constants.EhcacheConstant;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.datastruct.WeekArray;
import com.loocme.sys.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.api.alg.impl
 * @Description： <p> PictureAlgorithmImpl </p>
 * @Author： - Jason
 * @CreatTime：2019/8/5 - 9:54
 * @Modify By：
 * @ModifyTime： 2019/8/5
 * @Modify marker：
 */
public class PictureAlgorithmImpl implements IAlgorithm, Serializable {

    private static final long serialVersionUID = -3654781618581973049L;

    private static final Logger logger = LoggerFactory.getLogger(PictureAlgorithmImpl.class);

    private static AppProducer appProducer = SpringContextHolder.getBean(AppProducer.class);

    @Override
    public String getRequestParam(Var config, JSONArray imgArr) {
        JSONObject requestParam = new JSONObject();
        JSONObject outputParam = new JSONObject();
        //是否开启细类检测
        outputParam.put("SubClass",1);
        //是否开启人脸检测
        outputParam.put("Face",1);
        requestParam.put("Output",outputParam);
        requestParam.put("ImageList",imgArr);
        return requestParam.toString();
    }


    /**
     * 如果开启多个服务地址就轮询选择
     * @return
     */
    @Override
    public String getRequestUrl() {
        if (CommonConst.RECOG_PICTURE_FEATURE_CLUSTER_ENABLE){
            return WeightRoundRobin.getServer();
        }
        return CommonConst.RECOG_PICTURE_FEATURE_URL;
    }


    @Override
    public String getResponseCode(Var responseVar) {
        if (null == responseVar || responseVar.isNull()) {
            return "PIC NO_RESPONSE";
        }
        if (StringUtil.isNotNull(responseVar.getString(CommonConst.RESULT_CODE_KEY))) {
            return responseVar.getString(CommonConst.RESULT_CODE_KEY);
        }
        return null;
    }


    @Override
    public void dealResponse(List<PictureInfo> list, Var responseVar) {
        // 将数据解析出来 推送到kafka
        WeekArray featureArray = responseVar.getArray(CommonConst.RESULT_PIC_OBJECT_LIST_KEY);
        Map<String,List<AreaCompare>> resultGroupMap;
        List<AreaCompare> compareListAll = new ArrayList<>();
        if (!featureArray.isNull()){
            logger.info("=== [处理特征值&推送到Kafka]dealResponse:featureSize:{} ===",featureArray.getSize());
            Var featureResult;
            AreaCompare areaCompare;
            for (int i = 0; i < featureArray.getSize(); i++) {
                featureResult = featureArray.get(i+"");
                String imageId = featureResult.getString("ImageID");
                Var metadataVar = featureResult.get("Metadata");
                Var objectBoundingBox = metadataVar.get("ObjectBoundingBox");
                int w = objectBoundingBox.getInt("w");
                int h = objectBoundingBox.getInt("h");
                areaCompare = new AreaCompare(i, imageId, w, h);
                compareListAll.add(areaCompare);
            }
            if (!compareListAll.isEmpty()){
                resultGroupMap = PushToKafkaUtil.groupByImageId(compareListAll);
                for (Map.Entry<String,List<AreaCompare>> entry : resultGroupMap.entrySet()) {
                    String imageId = entry.getKey();
                    List<AreaCompare> compareList = entry.getValue();
                    if (!compareList.isEmpty()){
                        Collections.sort(compareList, new Comparator<AreaCompare>() {
                            @Override
                            public int compare(AreaCompare ac1, AreaCompare ac2) {
                                return ac2.getCountArea() - ac1.getCountArea();
                            }
                        });
                        int resultIndex = compareList.get(0).getIndex();
                        Var var = featureArray.get(resultIndex + "");
                        String featureVector = var.getString("Feature");
                        AlgKafkaVo algKafkaVo = (AlgKafkaVo)EhCacheUtil.get(EhcacheConstant.EHCAHCHE_PIC_PUSH_KAFKA_CACHE, imageId);
                        JSONObject kafkaJson = PushToKafkaUtil.convertPushToKafka(algKafkaVo, featureVector);
                        appProducer.sendConVertDataToKafka(kafkaJson.toString(),algKafkaVo.getCreatTime());
                    }
                }
            }
        }
    }

}
