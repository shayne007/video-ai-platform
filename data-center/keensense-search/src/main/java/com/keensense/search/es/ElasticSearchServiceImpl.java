package com.keensense.search.es;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.service.impl
 * @Description： <p> ElasticSearchServiceImpl  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/29 - 9:34
 * @Modify By：
 * @ModifyTime： 2019/7/29
 * @Modify marker：
 */
public class ElasticSearchServiceImpl implements ElasticSearchService {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchServiceImpl.class);

    /**
     * 客户端
     */
    RestHighLevelClient restHighLevelClient;

    /**
     * client - 初始化配置等
     */
    protected ElasticSearchClient elasticSearchClient;


    /**
     * 上下文时间 - 支持该批次
     */
    private static final Long SCROLL_ALIVE_TIME = 5L;


    @Override
    public ElasticSearchClient getElasticSearchClient() {
        return elasticSearchClient;
    }


    /**
     * setter 注入ElasticSearchClient
     *
     * @param elasticSearchClient
     */
    public void setElasticSearchClient(ElasticSearchClient elasticSearchClient) {
        this.elasticSearchClient = elasticSearchClient;
        this.restHighLevelClient = elasticSearchClient.getRestHighLevelClient();
    }


//    public RestHighLevelClient getEsClient() throws SystemException {
//        boolean isContinue = true;
//        RestHighLevelClient restHighLevelClient = null;
//        int count = 3;
//        try {
//            do {
//                try {
//                    restHighLevelClient = elasticSearchClient.getRestHighLevelClient();
//                    isContinue = false;
//                } catch (Exception e) {
//                    isContinue = true;
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e1) {
//                        logger.error("== InterruptedException：{} ===",e1.getMessage());
//                    }
//                    count++;
//                }
//                if (count > 3) {
//                    break;
//                }
//            } while (isContinue);
//        } catch (Exception e) {
//            logger.error("RestHighLevelClient  connect exception-连接失败!", e);
//        }
//        return restHighLevelClient;
//    }


    /**
     * 根据id查询
     *
     * @param indexName 索引名
     * @param indexType 索引类型
     * @param id        文档id
     * @return
     */
    @Override
    public GetResponse getDocument(String indexName, String indexType, String id) {
        GetRequest getRequest = new GetRequest(indexName, indexType, id);
        GetResponse response = null;
        try {
            response = restHighLevelClient.get(getRequest);
        } catch (IOException e) {
            logger.error("getDocument ->> 根据id查询失败！index = {},type = {},id = {}", indexName, indexType, id, e);
        }
        return response;
    }

    /**
     * 保存文档
     *
     * @param jsonMap
     * @param indexName
     * @param indexType
     * @param id
     * @return
     */
    @Override
    public IndexResponse saveDocument(Map<String, Object> jsonMap, String indexName, String indexType, String id) {
        IndexRequest indexRequest = new IndexRequest(indexName, indexType, id).source(jsonMap);
        IndexResponse response = null;
        try {
            response = restHighLevelClient.index(indexRequest);
        } catch (IOException e) {
            logger.error("saveDocument ->> 保存失败！obj = {},index = {},type = {},id = {}", jsonMap, indexName, indexType, id, e);
        }
        return response;
    }

    /**
     * updateDocument
     *
     * @param jsonMap
     * @param indexName
     * @param indexType
     * @param id
     * @return
     */
    @Override
    public UpdateResponse updateDocument(Map<String, Object> jsonMap, String indexName, String indexType, String id) {
        UpdateRequest request = new UpdateRequest(indexName, indexType, id).doc(jsonMap);
        UpdateResponse response = null;
        try {
            response = restHighLevelClient.update(request);
        } catch (IOException e) {
            logger.error("updateDocument ->> 修改失败！str = {},index = {},type = {},id = {}", jsonMap, indexName, indexType, id, e);
        }
        return response;
    }


    /**
     * 部分修改
     *
     * @param jsonMap
     * @param indexName
     * @param indexType
     * @param id
     * @return
     */
    @Override
    public UpdateResponse upsertDocument(Map<String, Object> jsonMap, String indexName, String indexType, String id) {
        UpdateRequest request = new UpdateRequest(indexName, indexType, id).doc(jsonMap);
        request.upsert(jsonMap, XContentType.JSON);
        UpdateResponse response = null;
        try {
            response = restHighLevelClient.update(request);
        } catch (IOException e) {
            logger.error("update insert Document ->> 修改失败！str = {},index = {},type = {},id = {}", jsonMap, indexName, indexType, id, e);
        }
        return response;
    }


    /**
     * 批量插入
     *
     * @param entityList
     * @param indexName
     * @param indexType
     * @param isAsync
     * @param idColumn   - id字段
     * @param <T>
     * @return
     */
    @Override
    public <T> BulkResponse saveEntityBulk(List<T> entityList, String indexName, String indexType, Boolean isAsync, String idColumn) {
        BulkRequest request = new BulkRequest();
        BulkResponse bulkResponse = null;
        for (T t : entityList) {
            Map<String, Object> jsonMap = BeanUtil.beanToMap(t, true, true);
            IndexRequest indexRequest = new IndexRequest(indexName, indexType, String.valueOf(jsonMap.get(idColumn))).source(jsonMap);
            request.add(indexRequest);
        }
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        if (isAsync) {
            saveBulkAsync(request, indexName, indexType, "saveEntityBulk");
        } else {
            saveBulkSync(request, indexName, indexType, "saveEntityBulk");
        }
        return bulkResponse;
    }


    /**
     * saveBizBulkAsync 异步批量保存
     *
     * @param jsonObjectList
     * @param indexName
     * @param indexType
     * @param idColumn
     * @param listener
     */
    @Override
    public void saveBizBulkAsync(List<JSONObject> jsonObjectList, String indexName, String indexType, String idColumn, ActionListener<BulkResponse> listener) {
        BulkRequest request = new BulkRequest();
        for (JSONObject jsonObject : jsonObjectList) {
            if (StringUtils.isEmpty(idColumn)) {
                continue;
            }
            IndexRequest indexRequest = new IndexRequest(indexName, indexType, jsonObject.getString(idColumn)).source(jsonObject);
            request.add(indexRequest);
        }
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        restHighLevelClient.bulkAsync(request, RequestOptions.DEFAULT, listener);
    }

    /**
     * 批量更新
     *
     * @param entityList
     * @param indexName
     * @param indexType
     * @param isAsync    - 同步异步
     * @param <T>
     * @return
     */
    @Override
    public <T> BulkResponse updateEntityBulk(List<T> entityList, String indexName, String indexType, Boolean isAsync) {
        BulkRequest request = new BulkRequest();
        BulkResponse response = null;
        UpdateRequest updateRequest;
        for (T t : entityList) {
            Map<String, Object> jsonMap = BeanUtil.beanToMap(t, true, true);
            updateRequest = new UpdateRequest(indexName, indexType, String.valueOf(jsonMap.get("id"))).doc(jsonMap);
            request.add(updateRequest);
        }
        if (isAsync) {
            saveBulkAsync(request, indexName, indexType, "updateEntityBulk");
        } else {
            saveBulkSync(request, indexName, indexType, "updateEntityBulk");
        }
        return response;
    }


    /**
     * 批量bulk(同步)
     *
     * @param request
     * @param index
     * @param type
     * @param method
     * @return
     */
    @Override
    public BulkResponse saveBulkSync(BulkRequest request, String index, String type, String method) {
        BulkResponse bulkResponse = null;
        try {
            bulkResponse = restHighLevelClient.bulk(request);
            if (bulkResponse.hasFailures()) {
                logger.error("saveBulkSync ->> 批量保存部分失败！index = {},type = {},errorMsg = {}",
                        index, type, bulkResponse.buildFailureMessage());
            }
            BulkItemResponse[] responses = bulkResponse.getItems();
            printFailedItems(responses, method);
        } catch (IOException e) {
            logger.error("saveBulkSync ->> 批量保存失败！index = {},type = {}",
                    index, type, e);
        }
        return bulkResponse;
    }

    /**
     * 批量bulk(异步)
     *
     * @param request
     * @param index
     * @param type
     * @param method
     */
    @Override
    public void saveBulkAsync(BulkRequest request, String index, String type, String method) {
        restHighLevelClient.bulkAsync(request, new ActionListener<BulkResponse>() {
            @Override
            public void onResponse(BulkResponse bulkResponse) {
                if (bulkResponse.hasFailures()) {
                    logger.error("saveEntityBulkAsync ->> 异步批量保存部分失败！index = {},type = {},errorMsg = {}",
                            index, type, bulkResponse.buildFailureMessage());
                }
                BulkItemResponse[] responses = bulkResponse.getItems();
                printFailedItems(responses, method);
            }

            @Override
            public void onFailure(Exception e) {
                logger.error("saveEntityBulkAsync ->> 异步批量保存执行失败,indexName:[{}],indexType:[{}] ...", index, type, e);
            }
        });
    }

    /**
     * Scroll分页自定义搜索
     *
     * @param boolQueryBuilder
     * @param indexName
     * @param indexType
     * @param pageSize
     * @param sortName
     * @return
     */
    @Override
    public SearchResponse searchByScroll(BoolQueryBuilder boolQueryBuilder, String indexName, String indexType, Integer pageSize, String sortName) {
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.size(pageSize);
        if (StringUtils.isNotEmpty(sortName)) {
            searchSourceBuilder.sort(sortName, SortOrder.DESC);
        }
        searchRequest.source(searchSourceBuilder);
        searchRequest.scroll(TimeValue.timeValueMinutes(SCROLL_ALIVE_TIME));
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest);
        } catch (IOException e) {
            logger.error("searchByScroll ->> scroll分页搜索失败,indexName:[{}],indexType:[{}] ...", indexName, indexType, e);
        }
        return searchResponse;
    }


    /**
     * Scroll分页根据scrollId搜索
     *
     * @param indexName
     * @param indexType
     * @param scrollId
     * @return
     */
    @Override
    public SearchResponse searchByScrollId(String indexName, String indexType, String scrollId) {
        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(SCROLL_ALIVE_TIME));
        SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
        scrollRequest.scroll(scroll);
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.searchScroll(scrollRequest);
        } catch (IOException e) {
            logger.error("searchByScrollId ->> scroll分页搜索失败！index:[{}],type:[{}] ===", indexName, indexType, e);
        }
        return searchResponse;
    }


    /**
     * 删除scrollId
     *
     * @param scrollId
     */
    @Override
    public void clearScrollId(String scrollId) {
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        try {
            ClearScrollResponse clearScrollResponse = restHighLevelClient.clearScroll(clearScrollRequest);
            boolean succeeded = clearScrollResponse.isSucceeded();
            logger.info("clearScrollId ->> result:[{}] ===", succeeded);
        } catch (IOException e) {
            logger.error("clearScrollId ->> 删除scrollId失败！scrollId:[{}] ===", scrollId, e);
        }
    }


    /**
     * 根据多个条件搜索
     *
     * @param boolQueryBuilder 条件
     * @param indexName        索引名
     * @param indexType        索引类型
     * @param pageNum          页码
     * @param pageSize         页大小
     * @param sortName         排序名
     * @return
     */
    @Override
    public SearchResponse searchByQueryBuilder(BoolQueryBuilder boolQueryBuilder, String indexName, String indexType, Integer pageNum, Integer pageSize, String sortName) {
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.from(pageNum);
        searchSourceBuilder.size(pageSize);
        if (StringUtils.isNotEmpty(sortName)) {
            searchSourceBuilder.sort(sortName, SortOrder.DESC);
        }
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(searchRequest);
        } catch (IOException e) {
            logger.error("searchByQueryBuilder ->> 根据多个条件搜索失败！index = {},boolQueryBuilder = {}", indexName, boolQueryBuilder.toString(), e);
        }
        return response;
    }


    /**
     * 根据多个条件搜索聚合
     *
     * @param boolQueryBuilder   查询条件
     * @param indexName          索引name
     * @param indexType          索引type
     * @param aggregationBuilder 聚合条件
     * @return
     */
    @Override
    public SearchResponse aggByQueryBuilder(BoolQueryBuilder boolQueryBuilder, String indexName, String indexType, AggregationBuilder aggregationBuilder) {
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(aggregationBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(searchRequest);
        } catch (IOException e) {
            logger.error("aggByQueryBuilder ->> 根据多个条件搜索失败！index = {},boolQueryBuilder = {}", indexName, boolQueryBuilder.toString(), e);
        }
        return response;
    }


    /**
     * print Error info
     *
     * @param responses
     * @param method
     * @param <T>
     */
    @Override
    public <T> void printFailedItems(BulkItemResponse[] responses, String method) {
        if (null == responses || responses.length == 0) {
            return;
        }
        List<String> ids = new ArrayList<>();
        for (BulkItemResponse bulkItemResponse : responses) {
            if (bulkItemResponse.isFailed()) {
                BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                ids.add(failure.getId());
            }
        }
        if (ids.size() > 0) {
            logger.error("=== method:[{}],保存失败ids为:{},收集起来下次再次入库 ===", method, ids.toString());
        }
    }

    /**
     * ***************************** 操作es ***************************************************
     */

    /**
     * 判断当前索引是否存在
     *
     * @param indexName 索引名
     * @return
     */
    @Override
    public boolean checkIndexExists(String indexName) {
        GetIndexRequest request = new GetIndexRequest().indices(indexName);
        try {
            return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            logger.error("=== checkIndexExists:error:{} ===", e.getMessage());
        }
        return false;
    }

    /**
     * 创建索引 通过读取文件
     *
     * @param indexName   索引name
     * @param indexType   索引type
     * @param columns     字段名 - 配置文件properties
     * @param jsonColumns json格式的配置文件
     */
    @Override
    public void createIndexByColumnsList(String indexName, String indexType, List<String> columns, JSONObject jsonColumns) {
        if (null == restHighLevelClient) {
            return;
        }
        try {
            if (checkIndexExists(indexName)) {
                logger.error("== IndexName:[{}] has exist! please check it.", indexName);
                return;
            }
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            // 使用默认的配置 5个副本 [warn] the default number of shards will change from [5] to [1] in 7.0.0
            request.settings(Settings.builder().put("index.number_of_shards", 5).put("index.number_of_replicas", 2));
            request.mapping(indexType, generateMappingBuilder(columns, jsonColumns));
            CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            boolean acknowledged = response.isAcknowledged();
            boolean shardsAcknowledged = response.isShardsAcknowledged();
            if (acknowledged || shardsAcknowledged) {
                logger.info("== es indexName:{} build success ...", indexName);
            }
        } catch (IOException e) {
            logger.error("=== createIndexByListStr error:{} ===", e.getMessage());
        }
    }

    /**
     * 构建 mapping
     *
     * @param columns
     * @return
     * @throws IOException
     */
    @Override
    public XContentBuilder generateMappingBuilder(List<String> columns, JSONObject jsonColumns) throws IOException {
        XContentBuilder contentBuilder = XContentFactory.jsonBuilder().startObject();
        contentBuilder.startObject("properties");
        if (CollectionUtils.isEmpty(columns) && jsonColumns != null) {
            Set<String> strKey = jsonColumns.keySet();
            for (String columnName : strKey) {
                JSONObject valueJson = jsonColumns.getJSONObject(columnName);
                String analyzeType = valueJson.getString("index");
                String columnType = valueJson.getString("type");
                contentBuilder
                        .startObject(columnName)
                        .field("type", columnType)
                        .field("index", analyzeType == null ? "false" : analyzeType)
                        .endObject();
            }
        } else {
            for (String str : columns) {
                if (StringUtils.isNotEmpty(str)) {
                    String[] params = str.split(",");
                    // 字段名
                    String columnName = params[0];
                    // 字段类型
                    String columnType = params[1];
                    //是否创建索引
                    String analyzeType = params[2];
                    contentBuilder
                            .startObject(columnName)
                            .field("type", columnType)
                            .field("index", analyzeType)
                            .endObject();
                }
            }
        }
        contentBuilder.endObject().endObject();
        return contentBuilder;
    }


    /**
     * 删除索引
     *
     * @param indexName
     * @throws IOException
     */
    @Override
    public void deleteIndex(String indexName) throws IOException {
        try {
            DeleteIndexResponse response = (DeleteIndexResponse) restHighLevelClient.indices()
                    .delete(new DeleteIndexRequest(indexName), RequestOptions.DEFAULT);
            if (response.isAcknowledged()) {
                logger.info(" == [{}]索引删除成功!", indexName);
            }
        } catch (ElasticsearchException ex) {
            if (ex.status() == RestStatus.NOT_FOUND) {
                logger.error("== [{}]索引名不存在!", indexName);
            }
            logger.error("== deleteIndex:error:{} ==", ex.getMessage());
        }
    }

    /**
     * 创建更新文档
     *
     * @param indexName  索引名称
     * @param indexType  索引类型
     * @param documentId 文档id
     * @param jsonStr    文档str
     * @return
     * @throws Exception
     */
    @Override
    public boolean documentCreate(String indexName, String indexType, String documentId, String jsonStr) throws Exception {
        IndexRequest request = new IndexRequest(indexName, indexType, documentId);
        request.source(jsonStr, XContentType.JSON);
        IndexResponse indexResponse = restHighLevelClient.index(request);
        if (indexResponse.getResult() == DocWriteResponse.Result.CREATED
                || indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
            return true;
        }
        ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
        if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
            return true;
        }
        if (shardInfo.getFailed() > 0) {
            for (ReplicationResponse.ShardInfo.Failure failure : shardInfo
                    .getFailures()) {
                throw new Exception(failure.reason());
            }
        }
        return false;
    }

}
