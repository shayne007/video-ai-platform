package com.keensense.search.service;

import com.alibaba.fastjson.JSONObject;
import java.util.Map;

/**
 * Created by memory_fu on 2019/2/26.
 */
public interface DataService {


    /**
     * @return 返回消息
     */
    JSONObject batchInsert(JSONObject jsonObject);

    /**
     * query by parameter id
     */
    String query(String id);

    /**
     * query by parameters
     */
    String batchQuery(Map<String, String[]> parameterMap);

    String search(JSONObject jsonObject) throws InterruptedException;

    /**
     * groupBy query
     * @param jsonObject parameters
     * @param index  es index name
     * @return
     */
    String groupByQuery(JSONObject jsonObject,String index);
}
