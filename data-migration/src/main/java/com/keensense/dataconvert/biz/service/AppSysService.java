package com.keensense.dataconvert.biz.service;

import java.util.List;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.service
 * @Description： <p> AppSysService  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/25 - 10:55
 * @Modify By：
 * @ModifyTime： 2019/7/25
 * @Modify marker：
 */
public interface AppSysService {


    /**
     * 根据索引文件 创建索引mapping
     * @param elasticSearchService    客户端
     * @param indexName 索引name
     * @param indexType 索引type
     * @param path classpath: submeter/xxx.txt
     */
    void buildEsIndex(ElasticSearchService elasticSearchService ,String indexName, String indexType, String path);


    /**
     * 根据版本号 创建es的索引
     * @param elasticSearchService 目标es库
     * @param indexName  目标索引name
     * @param indexType  索引type
     * @param jsonPath   json文件位置
     */
    void buildEsIndexByJson(ElasticSearchService elasticSearchService ,String indexName, String indexType, String jsonPath);


    /**
     * 查询数据需要更新的表结构
     * @param tableNamePrefix
     * @return
     */
    List<String> selectAllDataTables(String tableNamePrefix);


}
