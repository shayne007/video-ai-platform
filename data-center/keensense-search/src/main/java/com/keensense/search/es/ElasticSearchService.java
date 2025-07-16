package com.keensense.search.es;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.service
 * @Description： <p> ElasticSearchService  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/29 - 9:34
 * @Modify By：
 * @ModifyTime： 2019/7/29
 * @Modify marker：
 */
public interface ElasticSearchService {


    /**
     * 获取客户端
     * @return
     */
    ElasticSearchClient getElasticSearchClient();


    /**
     * 获取文档by id
     * @param indexName 索引名
     * @param indexType 索引类型
     * @param id        文档id
     * @return
     */
    GetResponse getDocument(String indexName, String indexType, String id);


    /**
     * 保存文档
     * @param jsonMap
     * @param indexName
     * @param indexType
     * @param id
     * @return
     */
    IndexResponse saveDocument(Map<String, Object> jsonMap, String indexName, String indexType, String id);


    /**
     *  根据id 更新document
     * @param jsonMap
     * @param indexName
     * @param indexType
     * @param id
     * @return
     */
    UpdateResponse updateDocument(Map<String, Object> jsonMap, String indexName, String indexType, String id);


    /**
     * 批量更新
     * @param entityList
     * @param indexName
     * @param indexType
     * @param isAsync  - 同步异步
     * @param <T>
     * @return
     */
    <T> BulkResponse updateEntityBulk(List<T> entityList, String indexName, String indexType, Boolean isAsync);


    /**
     * update or insert
     * @param jsonMap
     * @param indexName
     * @param indexType
     * @param id
     * @return
     */
    UpdateResponse upsertDocument(Map<String, Object> jsonMap, String indexName, String indexType, String id);


    /**
     * saveEntityBulk
     * @param entityList
     * @param indexName
     * @param indexType
     * @param isAsync
     * @param idColumn    - id字段
     * @param <T>
     * @return
     */
    <T> BulkResponse saveEntityBulk(List<T> entityList, String indexName, String indexType, Boolean isAsync,String idColumn);



    /**
     * saveBizBulkAsync 异步批量插入
     * @param entityList
     * @param indexName
     * @param indexType
     * @param idColumn
     * @param listener
     */
    void saveBizBulkAsync(List<JSONObject> entityList, String indexName, String indexType, String idColumn, ActionListener<BulkResponse> listener);


    /**
     * saveBulkSync 批量保存
     * @param request   请求
     * @param indexName 索引名
     * @param indexType 索引类型
     * @param method    -- 批量操作的方法-- 异常处理
     * @return
     */
    BulkResponse saveBulkSync(BulkRequest request, String indexName, String indexType, String method);


    /**
     * saveBulkAsync 批量保存
     * @param request
     * @param indexName
     * @param indexType
     * @param method
     */
    void saveBulkAsync(BulkRequest request, String indexName, String indexType, String method);


    /**
     * 收集异常数据
     * @param responses
     * @param method
     * @param <T>
     */
    <T> void printFailedItems(BulkItemResponse[] responses, String method);


    /**
     * 自定义分页查询
     * @param boolQueryBuilder
     * @param indexName
     * @param indexType
     * @param pageSize
     * @param sortName
     * @return
     */
    SearchResponse searchByScroll(BoolQueryBuilder boolQueryBuilder, String indexName, String indexType, Integer pageSize, String sortName);


    /**
     * searchByQueryBuilder 根据多个条件查询
     * @param boolQueryBuilder  条件
     * @param indexName   索引名
     * @param indexType   索引类型
     * @param pageNum     页码
     * @param pageSize    页大小
     * @param sortName    排序名
     * @return
     */
    SearchResponse searchByQueryBuilder(BoolQueryBuilder boolQueryBuilder, String indexName, String indexType, Integer pageNum, Integer pageSize, String sortName);


    /**
     * 聚合查询   统计等
     * @param boolQueryBuilder   查询条件
     * @param indexName          索引name
     * @param indexType          索引type
     * @param aggregationBuilder 聚合条件
     * @return
     */
    SearchResponse aggByQueryBuilder(BoolQueryBuilder boolQueryBuilder, String indexName, String indexType, AggregationBuilder aggregationBuilder);


    /**
     * searchByScrollId 滚动查询
     * @param indexName
     * @param indexType
     * @param scrollId
     * @return
     */
    SearchResponse searchByScrollId(String indexName, String indexType, String scrollId);


    /**
     * 清除滚动id
     * @param scrollId
     */
    void clearScrollId(String scrollId);

    /**
     * documentCreate 创建文档
     * @param indexName
     * @param indexType
     * @param documentId
     * @param jsonStr
     * @return
     * @throws Exception
     */
    boolean documentCreate(String indexName, String indexType,String documentId, String jsonStr) throws Exception;


    /**
     * 删除索引
     * @param indexName
     * @throws IOException
     */
    void deleteIndex(String indexName) throws IOException;


    /**
     * generateMappingBuilder 构建mapping
     * @param columns
     * @return
     * @throws IOException
     */
    XContentBuilder generateMappingBuilder(List<String> columns,JSONObject jsonColumns) throws IOException;


    /**
     * 通过字段配置来创建mapping结构
     * @param indexName  索引name
     * @param indexType  索引type
     * @param columns    字段名 - 配置文件properties
     * @param jsonColumns json格式的配置文件
     */
    void createIndexByColumnsList(String indexName, String indexType, List<String> columns,JSONObject jsonColumns);


    /**
     *  判断当前索引是否存在
     * @param indexName
     * @return  true - 存在  false 不存在
     */
    boolean checkIndexExists(String indexName);


}
