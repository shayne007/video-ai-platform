package com.keensense.dataconvert.api.util;

import com.keensense.dataconvert.api.request.vo.AlgKafkaVo;
import com.keensense.dataconvert.api.util.area.AreaCompare;
import com.keensense.dataconvert.biz.common.consts.CommonConst;
import com.keensense.dataconvert.framework.common.utils.date.DateHelper;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.api.util
 * @Description： <p> PushToKafkaUtil - 推送至kafka </p>
 * @Author： - Jason
 * @CreatTime：2019/8/14 - 20:18
 * @Modify By：
 * @ModifyTime： 2019/8/14
 * @Modify marker：
 */
public class PushToKafkaUtil {

    private static final Logger logger = LoggerFactory.getLogger(PushToKafkaUtil.class);

    /**
     * 封装数据
     */
    private static JSONObject pushToKafka;
    private static JSONObject featureData;

    /**
     * 数据转换 vlpr 设置为2  其他的看mysql 数据库
     * @param algKafkaVo
     * @param featureVector
     * @return
     */
    public static JSONObject convertPushToKafka(AlgKafkaVo algKafkaVo, String featureVector){
        pushToKafka = new JSONObject();
        featureData = new JSONObject();
        pushToKafka.put("uuid",algKafkaVo.getUuid());
        pushToKafka.put("objType",algKafkaVo.getObjType());
        pushToKafka.put("serialNumber",algKafkaVo.getSerialNumber());
        pushToKafka.put("createTime", DateHelper.getTimeStampStr(algKafkaVo.getCreatTime()));
        pushToKafka.put("firstObj", CommonConst.FEATURE_FIRST_OBJ);
        pushToKafka.put("startFramePts",1000);
        featureData.put("featureData",featureVector);
        pushToKafka.put("features", featureData);
        pushToKafka.put("onlyEnrollTypeFeature",1);
        pushToKafka.put("timestamp",1001);
        pushToKafka.put("firm",0);
        return pushToKafka;
    }

    /**
     * Map<imageId,List>  分组
     * @param areaCompareList
     * @return
     */
    public static Map<String, List<AreaCompare>> groupByImageId(List<AreaCompare> areaCompareList){
        Map<String, List<AreaCompare>> resultMap = new HashMap<>(15);
        try{
            for(AreaCompare areaCompare : areaCompareList){
                if (resultMap.containsKey(areaCompare.getImageId())){
                    resultMap.get(areaCompare.getImageId()).add(areaCompare);
                }else{
                    List<AreaCompare> tmpList = new ArrayList<>();
                    tmpList.add(areaCompare);
                    resultMap.put(areaCompare.getImageId(),tmpList);
                }
            }
        }catch(Exception e){
            logger.error("== 特征接口根据ImageId分组取最大值出现异常:{} ===",e.getMessage());
        }
        return resultMap;
    }
}
