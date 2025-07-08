package com.keensense.dataconvert.framework.config.es;

import com.keensense.dataconvert.biz.service.impl.ElasticSearchServiceImpl;
import com.keensense.dataconvert.framework.config.common.EsConfigBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.framework.config.es
 * @Description： <p> ElasticsearchConfig - 数据迁移es-配置 - 多数据源配置 </p>
 * @Author： - Jason
 * @CreatTime：2019/7/23 - 11:55
 * @Modify By：
 * @ModifyTime： 2019/7/23
 * @Modify marker：
 */
@Configuration
public class ElasticsearchConfig {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchConfig.class);

    @Autowired
    private EsConfigBean esConfigBean;


    @Bean(name="sourceEsService")
    public ElasticSearchServiceImpl buildSourceEsServiceImpl(){
        ElasticSearchServiceImpl esService = new ElasticSearchServiceImpl();
        esService.setElasticSearchClient(buildSourceElasticSearchClient());
        logger.info(">>> [初始化]Single Source Es service success ! >>>");
        return esService;
    }


    @Bean(name="targetEsService")
    public ElasticSearchServiceImpl buildTargetEsServiceImpl(){
        ElasticSearchServiceImpl esService = new ElasticSearchServiceImpl();
        esService.setElasticSearchClient(buildTargetElasticSearchClient());
        logger.info(">>> [初始化]Single Source Es service success ! >>>");
        return esService;
    }


    @Bean(name="refreshEsService")
    public ElasticSearchServiceImpl buildRefreshEsServiceImpl(){
        ElasticSearchServiceImpl esService = new ElasticSearchServiceImpl();
        esService.setElasticSearchClient(buildRefreshElasticSearchClient());
        logger.info(">>> [初始化]Single Source Es service success ! >>>");
        return esService;
    }


    /**
     *  源es客户端
     * @return
     */
    @Bean(name = "sourceElasticSearchClient", initMethod = "init", destroyMethod = "close")
    public ElasticSearchClient buildSourceElasticSearchClient() {
        ElasticSearchClient elasticSearchClient = new ElasticSearchClient();
        elasticSearchClient.setHost(esConfigBean.getSourceHost());
        elasticSearchClient.setPort(esConfigBean.getSourcePort());
        elasticSearchClient.setUsername(esConfigBean.getSourceUsername());
        elasticSearchClient.setPassword(esConfigBean.getSourcePassword());
        return elasticSearchClient;
    }

    /**
     * 目标es源
     * @return
     */
    @Bean(name = "targetElasticSearchClient", initMethod = "init", destroyMethod = "close")
    public ElasticSearchClient buildTargetElasticSearchClient() {
        ElasticSearchClient elasticSearchClient = new ElasticSearchClient();
        elasticSearchClient.setHost(esConfigBean.getTargetHost());
        elasticSearchClient.setPort(esConfigBean.getTargetPort());
        elasticSearchClient.setUsername(esConfigBean.getTargetUsername());
        elasticSearchClient.setPassword(esConfigBean.getTargetPassword());
        return elasticSearchClient;
    }

    /**
     * 版本 4.0.3.7 mysql 的素具 推送至的es 库
     * @return
     */
    @Bean(name = "refreshElasticSearchClient", initMethod = "init", destroyMethod = "close")
    public ElasticSearchClient buildRefreshElasticSearchClient() {
        ElasticSearchClient elasticSearchClient = new ElasticSearchClient();
        elasticSearchClient.setHost(esConfigBean.getRefreshHost());
        elasticSearchClient.setPort(esConfigBean.getRefreshPort());
        elasticSearchClient.setUsername(esConfigBean.getRefreshUsername());
        elasticSearchClient.setPassword(esConfigBean.getRefreshPassword());
        return elasticSearchClient;
    }

}
