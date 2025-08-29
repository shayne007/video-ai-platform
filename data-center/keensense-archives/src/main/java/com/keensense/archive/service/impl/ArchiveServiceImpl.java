package com.keensense.archive.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.archive.config.ArchiveConfig;
import com.keensense.archive.service.ArchiveService;
import com.keensense.archive.utils.ElasticsearchUtil;
import com.keensense.archive.utils.HttpClientUtil;
import com.keensense.common.config.SpringContext;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by memory_fu on 2020/1/7.
 */
@Service
@Slf4j
public class ArchiveServiceImpl implements ArchiveService{
    
    @Autowired
    private ArchiveConfig archiveConfig;
    @Autowired
    private ElasticsearchUtil elasticsearchUtil;
    
    @Override
    public String archiveTitleSearch(JSONObject jsonObject) {
        
        log.info("==== archiveTitleSearch request para:"+jsonObject.toJSONString());
        JSONObject jsonResultParent = createResponse("0", "Success");
        if (jsonObject == null ){
            return jsonResultParent.toJSONString();
        }
        
        try {
            // 请求参数解析
            String feature = jsonObject.getString("query");
            Integer maxResult = jsonObject.getInteger("max_result");
            Integer firm = jsonObject.getInteger("firm");
            Integer type = jsonObject.getInteger("type");
            
            // 请求搜图模块参数组装
            String featureSearchPara = createFeatureSearchPara(feature, maxResult, firm, type);
            
            // 请求搜图模块 & 组装返回数据
            JSONArray jsonArray = featureSearchRequest(featureSearchPara);
            
            jsonResultParent.put("results",jsonArray);
            
        }catch (Exception e){
            log.error("==== archiveTitleSearch exception:",e);
            jsonResultParent = createResponse("-1", "Fail");
        }
        return jsonResultParent.toJSONString();
    }
    
    @Override
    public String updateArchive(JSONObject jsonObject) {
        JSONObject response = createResponse("0", "Success");
        try {
            log.info("==== updateArchive request para:"+jsonObject.toJSONString());
            String index = jsonObject.getString("index");
            String query_field = jsonObject.getString("query_field");
            JSONArray query_values = jsonObject.getJSONArray("query_values");
            String update_field = jsonObject.getString("update_field");
            String update_value = jsonObject.getString("update_value");
    
            List<Object> queryValues = query_values.toJavaList(Object.class);
            int updateByQueryCount = elasticsearchUtil
                .updateByQuery(query_field, queryValues, update_field, update_value, index);
            response.put("update_count",updateByQueryCount);
        }catch (Exception e){
            response = createResponse("-1", "Fail");
            response.put("err_msg",e.getMessage());
            log.error("==== updateArchive exception:",e);
        }
        return response.toJSONString();
    }
    
    private JSONArray featureSearchRequest(String featureSearchPara) throws IOException {
        
        JSONArray jsonArray = new JSONArray();
    
        String[] featureHost = archiveConfig.getFeatureHost().split(",");
    
        for (String  host : featureHost) {
            
            String url = "http://" + host + ":" + ArchiveConfig.featurePort
                + "/search";
            String respone = HttpClientUtil.doPost(url, featureSearchPara, null);
    
            JSONObject jsonObject = JSONObject.parseObject(respone);
            JSONArray results = jsonObject.getJSONArray("results");
            if(null != results){
                for (int i = 0; i < results.size(); i++) {
                    JSONObject resultsJSONObject = results.getJSONObject(i);
                    JSONObject object = new JSONObject();
                    object.put("clusterIndex",resultsJSONObject.get("uuid"));
                    object.put("date",resultsJSONObject.get("date"));
                    object.put("score",resultsJSONObject.get("score"));
                    jsonArray.add(object);
                }
            }
        }
    
        List<JSONObject> objects = JSONObject.parseArray(jsonArray.toJSONString(), JSONObject.class);
    
        Collections.sort(objects, new Comparator<JSONObject>() {
        
            @Override
            public int compare(JSONObject a, JSONObject b) {
                float score1 = a.getFloatValue("score");
                float score2 = b.getFloatValue("score");
    
                return score1 > score2 ? -1 : 1;
            }
        });
    
        JSONArray parseArray = JSONArray.parseArray(JSON.toJSONString(objects));
    
        return parseArray;
    }
    
    
    /**
     * 构建搜图模块请求参数
     * @param feature
     * @param maxResult
     * @param firm
     * @param type
     * @return
     */
    private String createFeatureSearchPara(String feature,Integer maxResult,Integer firm,Integer type){
        
        JSONObject jsonObject = new JSONObject();
        String[] featureHost = archiveConfig.getFeatureHost().split(",");
        jsonObject.put("firm",null == firm ? archiveConfig.getFirm():firm );
        jsonObject.put("type", null == type ? 3 : type);
        jsonObject.put("query",feature);
        jsonObject.put("max_result",null == maxResult ? 20 : maxResult/featureHost.length);
        
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObjectTasks = new JSONObject();
        jsonObjectTasks.put("id",archiveConfig.getTaskNum());
        jsonArray.add(jsonObjectTasks);
        
        jsonObject.put("tasks",jsonArray);
        
        return jsonObject.toJSONString();
    }
    
    private JSONObject createResponse(String ret, String desc) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ret", ret);
        jsonObject.put("desc", desc);
        return jsonObject;
    }
}
