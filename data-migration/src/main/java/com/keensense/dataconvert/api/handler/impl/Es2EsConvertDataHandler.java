package com.keensense.dataconvert.api.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.keensense.dataconvert.api.handler.IEs2EsHandler;
import com.keensense.dataconvert.api.util.es.EsScrollQueryUtil;
import com.keensense.dataconvert.api.util.migrate.EsFieldConvertUtil;
import com.keensense.dataconvert.biz.common.cache.redis.RedisConstant;
import com.keensense.dataconvert.biz.common.cache.redis.RedisService;
import com.keensense.dataconvert.biz.common.consts.ConfigPathConstant;
import com.keensense.dataconvert.biz.common.enums.EsIndexTypeEnum;
import com.keensense.dataconvert.biz.service.ElasticSearchService;
import com.keensense.dataconvert.biz.task.constant.TaskConstant;
import com.keensense.dataconvert.framework.common.ext.spring.SpringContextHolder;
import com.loocme.sys.util.ListUtil;
import net.sf.ehcache.util.NamedThreadFactory;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.api.handler.es2es.handler
 * @Description： <p> Es62Es6ConvertDataHandler es6.x --> es6.x  索引结构变化  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/23 - 16:38
 * @Modify By：
 * @ModifyTime： 2019/7/23
 * @Modify marker：
 */
public class Es2EsConvertDataHandler implements IEs2EsHandler, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(Es2EsConvertDataHandler.class);

    /**
     * 字段转换工具
     */
    private static final EsFieldConvertUtil vlprConvertUtil = new EsFieldConvertUtil(ConfigPathConstant.CONFIG_SUBMETER_VLPR_MIGRATE_ES_ES);
    private static final EsFieldConvertUtil objextConvertUtil = new EsFieldConvertUtil(ConfigPathConstant.CONFIG_SUBMETER_OBJEXT_MIGRATE_ES_ES);
    private static final EsFieldConvertUtil bikeConvertUtil = new EsFieldConvertUtil(ConfigPathConstant.CONFIG_SUBMETER_BIKE_MIGRATE_ES_ES);

    private static ElasticSearchService targetEsService = SpringContextHolder.getBean("targetEsService");

    @Autowired
    private RedisService redisService;

    public static String ES6_ES6_MIGRATE_THREAD = "es6-es6-migrate-handler";

    /**
     * issue: [4.0.3.7,4.0.4] | [4.0.5,4.0.6,4.0.7] | [5.0]
     *  问题存在于 [4.0.5,4.0.6,4.0.7] (es6.x) ---> [5.0](es6.x) 之间
     *  主要是字段索引结构的变化
     */
    @Override
    public void loadSourceData(final JSONObject loadConfig) {
        int threadNum = TaskConstant.THREAD_NUM;
        EsScrollQueryUtil esScrollQueryUtil = new EsScrollQueryUtil();
        final String indexName = loadConfig.getString("indexName");
        final String host = loadConfig.getString("host");
        int loadSize = loadConfig.getIntValue("loadSize");
        // 解决引用盖掉原因
        JSONObject convertConfig = new JSONObject();
        convertConfig.putAll(loadConfig);
        ExecutorService esMigrateExec = newFixedThreadPool(threadNum, new NamedThreadFactory(ES6_ES6_MIGRATE_THREAD));
        esMigrateExec.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<JSONObject> list;
                            while (true){
                                list = esScrollQueryUtil.queryData(indexName, host, loadSize);
                                logger.info(">>> loadSourceData:indexName:[{}],listSize:[{}] ...", indexName,list.size());
                                if (list.size() == 0) {
                                    esScrollQueryUtil.clear();
                                    break;
                                }
                                convertData(list, convertConfig);
                            }
                        } catch (IOException e) {
                            logger.error("=== IOException:[{}] ===",e.getMessage(),e);
                        }
                    }
                }
        );
    }

    @Override
    public void convertData(List<JSONObject> sourceList,final JSONObject convertConfig) {
        List<JSONObject> convertSourceList = new LinkedList<>();
        if (ListUtil.isNotNull(sourceList)){
            String indexName = convertConfig.getString("indexName");
            logger.info(">>> convertData:IndexName:[{}],ListSize:[{}] ...",indexName,sourceList.size());
            logger.debug(">>> 转换前的List,进行字段转换:\n{}",sourceList);
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
    public void saveIntoTarget(List<JSONObject> sourceList,final JSONObject targetConfig) {
        if (ListUtil.isNotNull(sourceList)){
            logger.debug(">>> 转换后的List,存入es库:\n{}",sourceList);
            String indexName = targetConfig.getString("indexName");
            String indexType = targetConfig.getString("indexType");
            String idColumn = targetConfig.getString("idColumn");
            logger.info(">>> saveIntoTarget:IndexName:[{}],ListSize:[{}] ...",indexName,sourceList.size());
            /**
             * 批量保存入库
             */
            targetEsService.saveBizBulkAsync(sourceList, indexName, indexType, idColumn, new ActionListener<BulkResponse>() {
                @Override
                public void onResponse(BulkResponse bulkItemResponses) {
                    if (bulkItemResponses.hasFailures()) {
                        logger.error("saveIntoTarget ->> 异步批量保存部分失败！index = {},type = {},errorMsg = {}",indexName, indexType, bulkItemResponses.buildFailureMessage());
                    }
                    BulkItemResponse[] responses = bulkItemResponses.getItems();
                    dealFailedItems(responses);
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
    public void dealFailedItems(BulkItemResponse[] responses) {
        if (null == responses || responses.length == 0) {
            return;
        }
        for (BulkItemResponse bulkItemResponse : responses) {
            if (bulkItemResponse.isFailed()) {
                BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                redisService.lpush(RedisConstant.REDIS_ES_TO_ES_ERROR_LIST_KEY,failure.getId());
            }
        }
    }


}