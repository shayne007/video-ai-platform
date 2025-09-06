package com.keensense.search.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.keensense.search.domain.PersonResult;
import com.keensense.search.utils.JsonConvertUtil;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * Created by memory_fu on 2019/2/26.
 */
@org.springframework.stereotype.Service
@Slf4j
@RefreshScope
public class PersonsServiceImpl extends DataServiceImpl {
    private static final String APPEAR_TIME_NAME = "PersonAppearTime";
    private static final String DISAPPEAR_TIME_NAME = "PersonDisAppearTime";

    private static final String PERSON_LIST_OBJECT_NAME = "PersonListObject";
    private static final String PERSON_OBJECT_NAME = "PersonObject";
    private static final String RESPONSE_PERSON_OBJECT_NAME = "PersonObject";

    private static final String PERSON_ID = "PersonID";
    /**
     * 批量写入人员数据
     * @param jsonObject 外部的请求json
     * @return 响应json
     */
    @Override
    public JSONObject batchInsert(JSONObject jsonObject) {
        //log.debug("person object is {}", jsonObject);
        return batchInsert(jsonObject,
            PERSON_LIST_OBJECT_NAME,
            PERSON_OBJECT_NAME, PERSON_ID, RESPONSE_PERSON_OBJECT_NAME,
            JsonConvertUtil.PERSON_OBJECT_TYPE,
            JsonConvertUtil.PERSON_OBJECT_TYPE_NAME,
            APPEAR_TIME_NAME, DISAPPEAR_TIME_NAME, PersonResult.class);
    }

    /**
     * 查询单个人员数据
     * @param id 人员id
     * @return 单个人员数据
     */
    @Override
    public String query(String id) {
        return query("id", id, RESPONSE_PERSON_OBJECT_NAME, PersonResult.class);
    }

    /**
     * 批量查询人员
     * @param parameterMap 查询条件
     * @return 查询结果
     */
    @Override
    public String batchQuery(Map<String, String[]> parameterMap) {
        return batchQuery(parameterMap, PERSON_LIST_OBJECT_NAME, PERSON_OBJECT_NAME,
            PersonResult.class);
    }

    /**
     * 以图搜图
     * @param jsonObject 搜图的请求json
     * @return 搜图的结果
     */
    @Override
    public String search(JSONObject jsonObject) throws InterruptedException {
        return imageSearchService.search(jsonObject, JsonConvertUtil.PERSON_OBJECT_TYPE);
    }

    public String update(JSONObject object) {
        return update(object, PERSON_LIST_OBJECT_NAME,
            PERSON_OBJECT_NAME, PERSON_ID, PersonResult.class);
    }

    public String queryByMultiId(JSONObject object){
        return queryByMultiId(object, "Persons", PERSON_LIST_OBJECT_NAME, PERSON_OBJECT_NAME, PersonResult.class).toJSONString();
    }
}
