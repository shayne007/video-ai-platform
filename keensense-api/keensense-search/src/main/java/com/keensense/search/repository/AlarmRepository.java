package com.keensense.search.repository;


/**
 * Created by zhanx xiaohui on 2019-05-09.
 */
public interface AlarmRepository {
    void insert(String key, String jsonString);
    void trafficInsert(String key, String jsonString);
}
