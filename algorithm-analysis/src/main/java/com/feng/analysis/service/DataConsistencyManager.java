package com.feng.analysis.service;

import cn.hutool.extra.tokenizer.engine.analysis.AnalysisResult;
import com.keensense.common.platform.bo.video.AnalysisResultBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO
 *
 * @since 2025/7/9
 */
@Component
public class DataConsistencyManager {

    @Autowired
    private KafkaTemplate<String, AnalysisResultBo> kafkaTemplate;

    @Transactional
    public void processAnalysisResult(AnalysisResultBo result) {
        try {
            // Store in primary storage
//            elasticSearchService.index(result);

            // Store image embeddings
//            milvusService.insert(result.getFeatureObject());

            // Store original image
//            fastDfsService.upload(result.getBigImgUrl());

            // Publish success event
            kafkaTemplate.send("analysis-completed", result);

        } catch (Exception e) {
            // Implement compensation logic
            handlePartialFailure(result, e);
        }
    }

    private void handlePartialFailure(AnalysisResultBo result, Exception e) {
    }
}
