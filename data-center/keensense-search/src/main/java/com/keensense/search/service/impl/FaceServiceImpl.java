package com.keensense.search.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.keensense.search.domain.FaceResult;
import com.keensense.search.utils.JsonConvertUtil;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

/**
 * Created by zhanx xiaohui on 2019-02-27.
 */
@Service
@Slf4j
@RefreshScope
public class FaceServiceImpl extends DataServiceImpl {

    private static final String APPEAR_TIME_NAME = "FaceAppearTime";
    private static final String DISAPPEAR_TIME_NAME = "FaceDisAppearTime";
    private static final String FACE_LIST_OBJECT_NAME = "FaceObject";
    private static final String FACE_OBJECT_NAME = "FaceListObject";
    private static final String RESPONSE_FACE_OBJECT_NAME = "FaceObject";
    private static final String FACE_ID = "FaceID";
    /**
     * 批量写入人脸数据
     *
     * @param jsonObject 外部的请求json
     * @return 响应json
     */
    @Override
    public JSONObject batchInsert(JSONObject jsonObject) {
        //log.debug("face object is {}", jsonObject);
        return batchInsert(jsonObject,
            FACE_OBJECT_NAME,
            FACE_LIST_OBJECT_NAME, FACE_ID, RESPONSE_FACE_OBJECT_NAME,
            JsonConvertUtil.FACE_OBJECT_TYPE,
            JsonConvertUtil.FACE_OBJECT_TYPE_NAME,
            APPEAR_TIME_NAME, DISAPPEAR_TIME_NAME, FaceResult.class);
    }

    /**
     * 查询单个人脸
     *
     * @param id 人脸id
     * @return 单个人脸数据
     */
    @Override
    public String query(String id) {
        return query("id", id, RESPONSE_FACE_OBJECT_NAME, FaceResult.class);
    }

    /**
     * 批量查询人脸
     *
     * @param parameterMap 查询条件
     * @return 查询结果
     */
    @Override
    public String batchQuery(Map<String, String[]> parameterMap) {
        return batchQuery(parameterMap, FACE_OBJECT_NAME, FACE_LIST_OBJECT_NAME,
            FaceResult.class);
    }

    /**
     * 以图搜图
     *
     * @param jsonObject 搜图的请求json
     * @return 搜图的结果
     */
    @Override
    public String search(JSONObject jsonObject) throws InterruptedException {
        return imageSearchService.search(jsonObject, JsonConvertUtil.FACE_OBJECT_TYPE);
    }

    public String update(JSONObject object) {
        return update(object, FACE_OBJECT_NAME, FACE_LIST_OBJECT_NAME, FACE_ID, FaceResult.class);
    }

    public String queryByMultiId(JSONObject object){
        return queryByMultiId(object, "Faces",FACE_OBJECT_NAME, FACE_LIST_OBJECT_NAME, FaceResult.class).toJSONString();
    }
}

/**
 * @program: data
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-02-27 10:48
 **/