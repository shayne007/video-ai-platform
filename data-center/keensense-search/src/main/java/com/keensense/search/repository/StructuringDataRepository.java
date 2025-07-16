package com.keensense.search.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanx xiaohui on 2019-03-13.
 */
public interface StructuringDataRepository {


    void save(Object result);

    /**
     * query by ID
     * @param idKey
     * @param tClass
     * @param <T>
     * @return
     */
    <T> List<T> query(String idKey,String idValue,Class<T> tClass);

    /**
     * query by parameters
     * @param map
     * @param tClass
     * @param <T>
     * @return
     */
    <T> Map<Integer ,List<T>> batchQuery(Map<String, String> map,Class<T> tClass);

    <T>long delete( String key, String value, Class<T> tClass);

    <T>long batchDelete(Map<String, String> map, String timeName,Date startTime, Date endTime, Class<T> tClass);

    String update(Map<String, String> updateMap);
}

/**
 * @program: data
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-03-13 09:45
 **/