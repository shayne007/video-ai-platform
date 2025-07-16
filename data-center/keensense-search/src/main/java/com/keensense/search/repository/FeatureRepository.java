package com.keensense.search.repository;


import com.keensense.search.domain.Result;

/**
 * Created by zhanx xiaohui on 2019-03-13.
 */
public interface FeatureRepository {
    int sendFeature(Result result);
    boolean init();


}
