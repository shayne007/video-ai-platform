package com.keensense.dataconvert.api.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.keensense.dataconvert.api.handler.IEs2EsHandler;
import com.keensense.dataconvert.api.util.migrate.EsFieldConvertUtil;
import com.keensense.dataconvert.biz.common.cache.redis.RedisConstant;
import com.keensense.dataconvert.biz.common.cache.redis.RedisService;
import com.keensense.dataconvert.biz.common.consts.CommonConst;
import com.keensense.dataconvert.biz.common.consts.ConfigPathConstant;
import com.keensense.dataconvert.biz.common.enums.EsIndexTypeEnum;
import com.keensense.dataconvert.biz.entity.ObjextResult;
import com.keensense.dataconvert.biz.entity.VlprResult;
import com.keensense.dataconvert.biz.service.ElasticSearchService;
import com.keensense.dataconvert.framework.common.ext.spring.SpringContextHolder;
import com.loocme.sys.util.ListUtil;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.api.handler.impl
 * @Description： <p>  将mysql 加载到的数据 存入 es  </p>
 * @Author： - Jason
 * @CreatTime：2019/8/2 - 11:35
 * @Modify By：
 * @ModifyTime： 2019/8/2
 * @Modify marker：
 */
public class Mysql2EsConvertDataHandler implements IEs2EsHandler, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(Mysql2EsConvertDataHandler.class);

    @Autowired
    private RedisService redisService;

    /**
     * 字段转换工具
     */
    private static final EsFieldConvertUtil vlprConvertUtil = new EsFieldConvertUtil(ConfigPathConstant.CONFIG_SUBMETER_VLPR_MIGRATE_ES_ES);
    private static final EsFieldConvertUtil objextConvertUtil = new EsFieldConvertUtil(ConfigPathConstant.CONFIG_SUBMETER_OBJEXT_MIGRATE_ES_ES);
    private static final EsFieldConvertUtil bikeConvertUtil = new EsFieldConvertUtil(ConfigPathConstant.CONFIG_SUBMETER_BIKE_MIGRATE_ES_ES);
    private static ElasticSearchService targetEsService = SpringContextHolder.getBean("targetEsService");

    @Override
    public void loadSourceData(JSONObject loadConfig) {

    }

    @Override
    public void convertData(List<JSONObject> sourceList, JSONObject convertConfig) {
        List<JSONObject> convertSourceList = new LinkedList<>();
        if (ListUtil.isNotNull(sourceList)){
            String indexName = convertConfig.getString("indexName");
            JSONObject convertJsonObject = null;
            for (JSONObject jsonObject :sourceList) {
                if (EsIndexTypeEnum.VLPR.getIndexName().equalsIgnoreCase(indexName)){
                    convertJsonObject = vlprConvertUtil.mapConvertMapCustomer(jsonObject, false,true);
                }else if (EsIndexTypeEnum.OBJEXT.getIndexName().equalsIgnoreCase(indexName)){
                    convertJsonObject = objextConvertUtil.mapConvertMapCustomer(jsonObject, false,true);
                }else if (EsIndexTypeEnum.BIKE.getIndexName().equalsIgnoreCase(indexName)){
                    convertJsonObject = bikeConvertUtil.mapConvertMapCustomer(jsonObject, false,true);
                }else {
                    continue;
                }
                if (null!=convertJsonObject){
                    convertSourceList.add(convertJsonObject);
                }
            }
            saveIntoTarget(convertSourceList,convertConfig);
        }else{
            return;
        }
        return;
    }

    @Override
    public void saveIntoTarget(List<JSONObject> sourceList, JSONObject targetConfig) {
        String ymdIndex = targetConfig.getString("ymdIndex");
        int objType = targetConfig.getIntValue("indexType");
        String indexName = EsIndexTypeEnum.getIndexNameByObjType(objType);
        String indexType = EsIndexTypeEnum.getIndexTypeByObjType(objType);
        String idColumn = "recogId";
        // 批量入库
        if (ListUtil.isNotNull(sourceList)){
            targetEsService.saveBizBulkAsync(sourceList, indexName, indexType, idColumn, new ActionListener<BulkResponse>() {
                @Override
                public void onResponse(BulkResponse bulkItemResponses) {
                    if (bulkItemResponses.hasFailures()) {
                        logger.error("saveIntoTarget ->> 异步批量保存部分失败！index = {},type = {},errorMsg = {}",indexName, indexType, bulkItemResponses.buildFailureMessage());
                    }
                    BulkItemResponse[] responses = bulkItemResponses.getItems();
                    dealFailedItems(responses,ymdIndex);
                }
                @Override
                public void onFailure(Exception e) {
                    logger.error("saveIntoTarget ->> 异步批量保存执行失败,indexName:[{}],indexType:[{}] ...",indexName, indexType, e);
                }
            });

        }
    }


    /**
     * 处理异常的数据
     * @param responses
     */
    public void dealFailedItems(BulkItemResponse[] responses,String ymd) {
        if (null == responses || responses.length == 0) {
            return;
        }
        for (BulkItemResponse bulkItemResponse : responses) {
            if (bulkItemResponse.isFailed()) {
                BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                redisService.lpush(RedisConstant.REDIS_ES_TO_ES_ERROR_LIST_KEY.concat(ymd),failure.getId());
            }
        }
    }


    /**
     * 转换为list
     * @param vlprResults
     * @param objextResults
     * @param ymdIndex
     */
    public void convertJsonList(List<VlprResult> vlprResults,List<ObjextResult> objextResults,String ymdIndex){
        JSONObject convertConfig = new JSONObject();
        List<JSONObject> resultObjectList = new LinkedList<>();
        List<JSONObject> bikeObjectList = new LinkedList<>();
        convertConfig.put("ymdIndex",ymdIndex);
        if (ListUtil.isNotNull(vlprResults)){
            convertConfig.put("indexName", EsIndexTypeEnum.VLPR.getIndexName());
            convertConfig.put("indexType", EsIndexTypeEnum.VLPR.getObjType());
            for (VlprResult vlprResult :vlprResults){
                vlprResult.setIdStr(vlprResult.getRecogId());
                if (CommonConst.HTTP_PICTURE_IS_NEED_MAPPING){
                    vlprResult.setImageurl(vlprResult.getImageurl().replaceAll("http://127.0.0.1:8082", CommonConst.HTTP_PICTURE_PREFIX));
                    vlprResult.setSmallimgurl(vlprResult.getSmallimgurl().replaceAll("http://127.0.0.1:8082", CommonConst.HTTP_PICTURE_PREFIX));
                }/*else{
                    vlprResult.setImageurl(vlprResult.getImageurl());
                    vlprResult.setSmallimgurl(vlprResult.getSmallimgurl());
                }*/
                JSONObject jsonobj = (JSONObject)JSONObject.toJSON(vlprResult);
                jsonobj.put("createtime",vlprResult.getCreatetime().getTime());
                jsonobj.put("inserttime",vlprResult.getInserttime().getTime());
                resultObjectList.add(jsonobj);
            }
        }

        if (ListUtil.isNotNull(objextResults)){
            convertConfig.put("indexName", EsIndexTypeEnum.OBJEXT.getIndexName());
            convertConfig.put("indexType", EsIndexTypeEnum.OBJEXT.getObjType());
            for (ObjextResult objextResult :objextResults){
                objextResult.setIdStr(objextResult.getRecogId());
                if (CommonConst.HTTP_PICTURE_IS_NEED_MAPPING){
                    objextResult.setBigimgurl(objextResult.getBigimgurl().replaceAll("http://127.0.0.1:8082", CommonConst.HTTP_PICTURE_PREFIX));
                    objextResult.setImgurl(objextResult.getImgurl().replaceAll("http://127.0.0.1:8082", CommonConst.HTTP_PICTURE_PREFIX));
                }
                /*else{
                    objextResult.setBigimgurl(objextResult.getBigimgurl());
                    objextResult.setImgurl(objextResult.getImgurl());
                }*/
                JSONObject jsonobj = (JSONObject)JSONObject.toJSON(objextResult);
                jsonobj.put("createtime",objextResult.getCreatetime().getTime());
                jsonobj.put("inserttime",objextResult.getInserttime().getTime());
                if (objextResult.getObjtype() == EsIndexTypeEnum.BIKE.getObjType()){
                    bikeObjectList.add(jsonobj);
                }else{
                    resultObjectList.add(jsonobj);
                }
            }
        }

        /**
         * bike 单独处理
         */
        if (ListUtil.isNotNull(bikeObjectList)){
            JSONObject bikeConfig = new JSONObject();
            bikeConfig.put("ymdIndex",ymdIndex);
            bikeConfig.put("indexName",EsIndexTypeEnum.BIKE.getIndexName());
            bikeConfig.put("indexType",EsIndexTypeEnum.BIKE.getObjType());
            if (CommonConst.SYS_IS_OPEN_MYSQL_TO_ES_NEED_CONVERT_FIELD){
                convertData(bikeObjectList,bikeConfig);
            }else{
                saveIntoTarget(bikeObjectList,bikeConfig);
            }
        }

        //转换为需要存储的样子 - 配置开关
        if (CommonConst.SYS_IS_OPEN_MYSQL_TO_ES_NEED_CONVERT_FIELD){
            convertData(resultObjectList,convertConfig);
        }else{
            saveIntoTarget(resultObjectList,convertConfig);
        }
    }

}
