package com.keensense.search.repository;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhanx xiaohui on 2019-08-08.
 */
public interface ImageAnalysisRepository {
    void sendRequest(JSONObject object);
}
