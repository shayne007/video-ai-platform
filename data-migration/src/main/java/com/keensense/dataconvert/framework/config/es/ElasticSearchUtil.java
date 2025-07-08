package com.keensense.dataconvert.framework.config.es;

import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.framework.config.es
 * @Description： <p> ElasticSearchUtil  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/23 - 14:10
 * @Modify By：
 * @ModifyTime： 2019/7/23
 * @Modify marker：
 */
public class ElasticSearchUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchUtil.class);

    /**
     * elasticSearchClient
     */
    private ElasticSearchClient elasticSearchClient;

    /**
     * bulkProcessor
     */
    private BulkProcessor bulkProcessor;


    @PostConstruct
    public void init() {
        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                int numberOfActions = request.numberOfActions();
                LOGGER.info("Executing bulk [{}] with {} requests", executionId, numberOfActions);
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                if (response.hasFailures()) {
                    LOGGER.error("Bulk [{}] executed with failures,response = {}", executionId, response.buildFailureMessage());
                } else {
                    LOGGER.info("Bulk [{}] completed in {} milliseconds", executionId, response.getTook().getMillis());
                }
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                LOGGER.error("Failed to execute bulk", failure);
            }
        };
        BulkProcessor bulkProcessor = BulkProcessor.builder(elasticSearchClient.getRestHighLevelClient()::bulkAsync, listener)
                // 1000条数据请求执行一次bulk
                .setBulkActions(1000)
                // 5mb的数据刷新一次bulk
                .setBulkSize(new ByteSizeValue(5L, ByteSizeUnit.MB))
                // 并发请求数量, 0不并发, 1并发允许执行
                .setConcurrentRequests(0)
                // 固定1s必须刷新一次
                .setFlushInterval(TimeValue.timeValueSeconds(1L))
                // 重试5次，间隔1s
                .setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1L), 5))
                .build();
        this.bulkProcessor = bulkProcessor;
    }

    @PreDestroy
    public void destroy() {
        try {
            bulkProcessor.awaitClose(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error("Failed to close bulkProcessor", e);
        }
        LOGGER.info("bulkProcessor closed!");
    }

    /**
     * 修改update
     * @param request
     */
    public void update(UpdateRequest request) {
        this.bulkProcessor.add(request);
    }


    /**
     * 新增插入
     * @param request
     */
    public void insert(IndexRequest request) {
        this.bulkProcessor.add(request);
    }

    /**
     * 注入对应的客户端 - source or target
     * @param elasticSearchClient
     */
    public void setElasticSearchClient(ElasticSearchClient elasticSearchClient) {
        this.elasticSearchClient = elasticSearchClient;
    }
}
