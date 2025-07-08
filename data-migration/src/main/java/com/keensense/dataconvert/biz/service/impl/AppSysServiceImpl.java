package com.keensense.dataconvert.biz.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.keensense.dataconvert.api.util.BuildEsIndexUtil;
import com.keensense.dataconvert.biz.dao.AppSysMapper;
import com.keensense.dataconvert.biz.service.AppSysService;
import com.keensense.dataconvert.biz.service.ElasticSearchService;
import com.keensense.dataconvert.framework.common.utils.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.service.impl
 * @Description： <p> AppSysServiceImpl - app 系统 全局service </p>
 * @Author： - Jason
 * @CreatTime：2019/7/25 - 10:55
 * @Modify By：
 * @ModifyTime： 2019/7/25
 * @Modify marker：
 */
@Service("appSysService")
public class AppSysServiceImpl implements AppSysService {

    private static final Logger logger = LoggerFactory.getLogger(AppSysServiceImpl.class);

    @Resource
    private AppSysMapper appSysMapper;

    /**
     * 创建索引
     * @param elasticSearchService    客户端
     * @param indexName 索引name
     * @param indexType 索引type
     * @param path classpath: submeter/xxx.txt
     */
    @Override
    public void buildEsIndex(ElasticSearchService elasticSearchService, String indexName, String indexType, String path) {
        List<String> columns = BuildEsIndexUtil.buildByPathFile(path);
        elasticSearchService.createIndexByColumnsList(indexName,indexType,columns,null);
    }


    /**
     * buildEsIndexByJson
     * @param elasticSearchService 目标es库
     * @param indexName  目标索引name
     * @param indexType  索引type
     * @param jsonPath   json文件位置
     */
    @Override
    public void buildEsIndexByJson(ElasticSearchService elasticSearchService, String indexName, String indexType, String jsonPath) {
        try {
            JSONObject jsonObject = JsonUtil.readJsonFromClassPath(jsonPath, JSONObject.class);
            elasticSearchService.createIndexByColumnsList(indexName,indexType,null,jsonObject);
        } catch (IOException e) {
            logger.error("[buildEsIndexByJson]Please check your json file & path ...");
        }
    }

    @Override
    public List<String> selectAllDataTables(String tableNamePrefix) {
        try {
            Assert.notNull(tableNamePrefix,"不允许为Null");
            return appSysMapper.selectAllDataTables(tableNamePrefix);
        } catch (Exception e) {
            logger.error("[SelectAllDataTables]:error:{} ...",e);
        }
        return null;
    }
}
