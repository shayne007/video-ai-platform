package com.keensense.search.repository;

import com.alibaba.fastjson.JSONObject;
import com.keensense.search.domain.Archive;

/**
 * Created by zhanx xiaohui on 2019-05-09.
 */
public interface DocumentRepository {
    Archive getDocument(JSONObject object);
}
