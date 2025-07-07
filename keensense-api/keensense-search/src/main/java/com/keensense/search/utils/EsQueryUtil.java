package com.keensense.search.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.keensense.search.domain.EsQueryEnum;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by memory_fu on 2019/6/11.
 */
@Slf4j
@Service
public class EsQueryUtil {
    
    public static final String GROUPBY_FIELD_STR = "groupByField";
    public static final String GROUPBY_DATE_STR = "groupByDate";
    
    @Value("${origin.es.datasource.host}")
    private String esHost;
    @Value("${origin.es.datasource.port}")
    private String esPort;
    @Value("${origin.es.datasource.clusterName}")
    private String esCluster;
    @Value("${origin.es.datasource.username}")
    String username;
    
    private String auth;
    
    @PostConstruct
    private void initailList() {
        String[] user = username.split(",");
        String userName = user[0];
        String userPath = user[1];
        auth = "Basic " + new String(
            Base64.getEncoder().encode((userName + ":" + userPath).getBytes()));
    }
    
    /**
     * 获取响应字符串
     *
     * @param boolVo 过滤条件
     * @param aggsVo 分组条件
     * @param requestUrl 请求url
     */
    public String getResponseStr(JSONObject boolVo, JSONObject aggsVo, String requestUrl)
        throws IOException {
        
        String result;
        // 组装请求参数
        JSONObject jsonObject = createRequestBody(boolVo, aggsVo);
        log.info("======request body:" + JSONObject.toJSONString(jsonObject));
        log.info("======requestUrl body:" + requestUrl);
        String httpResopnseStr = HttpUtil
            .requestMethod(HttpUtil.HTTP_POST, requestUrl, jsonObject.toJSONString(), auth);
//        log.info("======response body:" + httpResopnseStr);
        
        // 解析请求结果
        JSONObject resultStr = getResult(JSONObject.parseObject(httpResopnseStr));
        result = JSONObject
            .toJSONString(resultStr, SerializerFeature.DisableCircularReferenceDetect);
        return result;
    }
    
    /**
     * 请求body
     *
     * @param boolVo 过滤条件
     * @param aggsVo 分组条件
     */
    public JSONObject createRequestBody(JSONObject boolVo, JSONObject aggsVo) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("size", 0);
        jsonObject.put("query", boolVo);
        jsonObject.put("aggs", aggsVo);
        return jsonObject;
    }
    
    
    public String groupByQuery(JSONObject json, String index) {
        
        String jsonStr = json.toJSONString();
        log.info("====== groupByQuery json:" + jsonStr);
        
        if(jsonStr.contains("GROUPBYDATE")){
            if(!jsonStr.contains("RANGE") && !jsonStr.contains("TERM") && !jsonStr.contains("LIKE")){
                return createResponse("-1", "Fail").toJSONString();
            }
        }
        
        String result = createResponse("0", "Success").toJSONString();
        try {
            JSONArray params = json.getJSONArray("params");
            JSONObject boolVo = createBoolVo();
            JSONObject aggsVo = createAggsVo();
            for (int i = 0; i < params.size(); i++) {
                JSONObject jsonObject = params.getJSONObject(i);
                String type = jsonObject.getString("type");
                
                if (EsQueryEnum.TERM.toString().equalsIgnoreCase(type) ||
                    EsQueryEnum.TERMS.toString().equalsIgnoreCase(type) ||
                    EsQueryEnum.RANGE.toString().equalsIgnoreCase(type) ||
                    EsQueryEnum.LIKE.toString().equalsIgnoreCase(type)) {
                    
                    createBoolQuery(jsonObject, boolVo);
                }
                
                if (EsQueryEnum.GROUPBY.toString().equalsIgnoreCase(type) ||
                    EsQueryEnum.HAVING.toString().equalsIgnoreCase(type) ||
                    EsQueryEnum.LIMIT.toString().equalsIgnoreCase(type) ||
                    EsQueryEnum.SORT.toString().equalsIgnoreCase(type) ||
                    EsQueryEnum.SIZE.toString().equalsIgnoreCase(type)) {
                    
                    createAggsQueryByGroupField(jsonObject, aggsVo);
                }
                
                if (EsQueryEnum.GROUPBYDATE.toString().equalsIgnoreCase(type)) {
                    createAggsQueryByGroupDate(jsonObject, aggsVo);
                }
            }
            
            String requestUrl = createRequestUrl(index, esHost, esPort);
            result = getResponseStr(boolVo, aggsVo, requestUrl);
            
        } catch (Exception e) {
            log.error("======groupBy query exception:", e);
            JSONObject failResult = createResponse("-1", "Fail");
            return failResult.toJSONString();
        }
        
        return result;
    }
    
    /**
     * 创建请求url
     *
     * @param index 索引名
     * @param esHost es ip
     * @param esPort es port
     */
    public String createRequestUrl(String index, String esHost, String esPort) {
        StringBuilder url = new StringBuilder(
            "http://" + esHost + ":" + esPort + "/" + index + "/_search");
        return url.toString();
    }
    
    public JSONObject createBoolVo() {
        JSONObject jsonObjectParent = new JSONObject();
        JSONObject jsonObjectBool = new JSONObject();
        JSONArray jsonArrayMust = new JSONArray();
        jsonObjectBool.put("must", jsonArrayMust);
        jsonObjectParent.put("bool", jsonObjectBool);
        return jsonObjectParent;
    }
    
    public JSONObject createAggsVo() {
        return new JSONObject();
    }
    
    public JSONObject createBoolQuery(JSONObject jsonObject, JSONObject boolVo)
        throws ParseException {
        String type = jsonObject.getString("type");
        Object value = jsonObject.get(EsQueryEnum.VALUE.getOpt());
        String field = String.valueOf(jsonObject.get(EsQueryEnum.FIELD.getOpt()));
        JSONObject jsonObjectParent = new JSONObject();
        JSONObject jsonObjectSub = new JSONObject();
        
        if ("id".equalsIgnoreCase(field)) {
            jsonObjectSub.put("_" + field, value);
        } else {
            jsonObjectSub.put(field, value);
        }
        
        if (EsQueryEnum.TERM.toString().equalsIgnoreCase(type)) {
            jsonObjectParent.put(EsQueryEnum.TERM.getOpt(), jsonObjectSub);
        } else if (EsQueryEnum.TERMS.toString().equalsIgnoreCase(type)) {
            jsonObjectParent.put(EsQueryEnum.TERMS.getOpt(), jsonObjectSub);
        } else if (EsQueryEnum.RANGE.toString().equalsIgnoreCase(type)) {
            JSONArray jsonArray = JSONArray.parseArray(String.valueOf(value));
            JSONObject jsonObjectRange = new JSONObject();
            
            Object gte = jsonArray.get(0);
            Object lte = jsonArray.get(1);
            
            if (field.toLowerCase().contains("time") && field.toLowerCase().endsWith("time")) {
                jsonObjectRange
                    .put(EsQueryEnum.GTE.getOpt(), dateStrToEsFormat(String.valueOf(gte)));
                jsonObjectRange
                    .put(EsQueryEnum.LTE.getOpt(), dateStrToEsFormat(String.valueOf(lte)));
            } else {
                jsonObjectRange.put(EsQueryEnum.GTE.getOpt(), gte);
                jsonObjectRange.put(EsQueryEnum.LTE.getOpt(), lte);
            }
            
            jsonObjectSub.put(field, jsonObjectRange);
            jsonObjectParent.put(EsQueryEnum.RANGE.getOpt(), jsonObjectSub);
        } else if (EsQueryEnum.LIKE.toString().equalsIgnoreCase(type)) {
            jsonObjectParent.put(EsQueryEnum.LIKE.getOpt(), jsonObjectSub);
        }
        boolVo.getJSONObject("bool").getJSONArray("must").add(jsonObjectParent);
        return jsonObjectParent;
    }
    
    public long dateStrToEsFormat(String dateStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date date = dateFormat.parse(dateStr);
        return date.getTime();
    }
    
    public JSONObject createAggsQueryByGroupDate(JSONObject jsonObject, JSONObject aggsVo) {
        
        createDefaultAggs(aggsVo, GROUPBY_DATE_STR);
        
        Object field = jsonObject.get(EsQueryEnum.FIELD.getOpt());
        Object value = jsonObject.get(EsQueryEnum.VALUE.getOpt());
        
        JSONObject groupByDate = aggsVo.getJSONObject(GROUPBY_DATE_STR);
        JSONObject jsonObjectSub = new JSONObject();
        
        jsonObjectSub.put(EsQueryEnum.FIELD.getOpt(), field);
        jsonObjectSub.put("interval", value);
        jsonObjectSub.put("time_zone", "+08:00");
        groupByDate.put("date_histogram", jsonObjectSub);
        
        aggsVo.put(GROUPBY_DATE_STR, groupByDate);
        
        return groupByDate;
    }
    
    public JSONObject createDefaultAggs(JSONObject aggsVo, String groupByStr) {
        
        JSONObject jsonObject = aggsVo.getJSONObject(groupByStr);
        
        if (null != jsonObject) {
            return aggsVo;
        }
        
        JSONObject parentJsonObject = new JSONObject();
        JSONObject aggsJsonObject = new JSONObject();
        JSONObject topJsonObject = new JSONObject();
        JSONObject topHitsJsonObject = new JSONObject();
        
        topHitsJsonObject.put("size", 1);
        topJsonObject.put(EsQueryEnum.TOP_HITS.getOpt(), topHitsJsonObject);
        aggsJsonObject.put("top", topJsonObject);
        parentJsonObject.put("aggs", aggsJsonObject);
        
        aggsVo.put(groupByStr, parentJsonObject);
        return parentJsonObject;
    }
    
    public JSONObject createAggsQueryByGroupField(JSONObject jsonObject, JSONObject aggsVo) {
        
        createDefaultAggs(aggsVo, GROUPBY_FIELD_STR);
        
        String fieldStr = EsQueryEnum.FIELD.getOpt();
        String type = jsonObject.getString("type");
        Object value = jsonObject.get(EsQueryEnum.VALUE.getOpt());
        Object field = jsonObject.get(fieldStr);
        JSONObject jsonObjectParent = new JSONObject();
        
        JSONObject jsonGroupByField = aggsVo.getJSONObject(GROUPBY_FIELD_STR);
        JSONObject aggsSub = jsonGroupByField.getJSONObject("aggs");
        JSONObject terms = jsonGroupByField.getJSONObject(EsQueryEnum.TERMS.getOpt());
        
        if (EsQueryEnum.GROUPBY.toString().equalsIgnoreCase(type) ||
            EsQueryEnum.SIZE.toString().equalsIgnoreCase(type)) {
            
            if (null != terms) {
                jsonObjectParent = terms;
            }
            
            if (EsQueryEnum.GROUPBY.toString().equalsIgnoreCase(type)) {
                jsonObjectParent.put(fieldStr, field);
                jsonGroupByField.put(EsQueryEnum.TERMS.getOpt(), jsonObjectParent);
            }
            
            if (EsQueryEnum.SIZE.toString().equalsIgnoreCase(type)) {
                jsonObjectParent.put(EsQueryEnum.SIZE.getOpt(), value);
                jsonGroupByField.put(EsQueryEnum.TERMS.getOpt(), jsonObjectParent);
            }
            
        } else if (EsQueryEnum.HAVING.toString().equalsIgnoreCase(type)) {
            JSONObject jsonObjectBucketSelector = new JSONObject();
            JSONObject jsonObjectBucketsPath = new JSONObject();
            jsonObjectBucketsPath.put("havingCount", "_count");
            JSONObject jsonObjectScript = new JSONObject();
            jsonObjectScript.put("source", "params.havingCount >= " + value);
            
            jsonObjectBucketSelector.put("buckets_path", jsonObjectBucketsPath);
            jsonObjectBucketSelector.put("script", jsonObjectScript);
            
            jsonObjectParent.put("bucket_selector", jsonObjectBucketSelector);
            
            aggsSub.put("having", jsonObjectParent);
            
        } else if (EsQueryEnum.LIMIT.toString().equalsIgnoreCase(type) ||
            EsQueryEnum.SORT.toString().equalsIgnoreCase(type)) {
            JSONObject jsonTop = aggsSub.getJSONObject("top");
            
            JSONObject jsonTopHits = jsonTop.getJSONObject(EsQueryEnum.TOP_HITS.getOpt());
            
            if (EsQueryEnum.LIMIT.toString().equalsIgnoreCase(type)) {
                jsonTopHits.put("size", value);
            } else {
                JSONObject jsonSort = new JSONObject();
                jsonSort.put(String.valueOf(field), String.valueOf(value));
                jsonTopHits.put("sort", jsonSort);
            }
            
            jsonTop.put(EsQueryEnum.TOP_HITS.getOpt(), jsonTopHits);
            aggsSub.put("top", jsonTop);
        }
        
        return jsonObjectParent;
    }
    
    public JSONArray analysisAggs(JSONObject jsonResultParent, JSONObject jsonAggs,
        String groupByStr) {
        
        JSONArray jsonResultSub = new JSONArray();
        JSONObject aggregations = jsonAggs.getJSONObject("aggregations");
        if (null == aggregations) {
            return jsonResultSub;
        }
        
        JSONObject groupByField = aggregations.getJSONObject(groupByStr);
        if (null == groupByField) {
            return jsonResultSub;
        }
        
        JSONArray buckets = groupByField.getJSONArray("buckets");
        if (null == buckets) {
            return jsonResultSub;
        }
        
        for (int i = 0; i < buckets.size(); i++) {
            JSONObject jsonResultBucket = new JSONObject();
            
            JSONObject bucketsJSONObject = buckets.getJSONObject(i);
            String groupByKey = bucketsJSONObject.getString("key");
            Long groupByCount = bucketsJSONObject.getLong("doc_count");
    
    
            JSONObject top = bucketsJSONObject.getJSONObject("top");
            if (top != null) {
                JSONObject parentHits = top.getJSONObject("hits");
                JSONArray subHits = parentHits.getJSONArray("hits");
    
                JSONArray jsonResultHits = new JSONArray();
                for (int j = 0; j < subHits.size(); j++) {
                    JSONObject source = subHits.getJSONObject(j).getJSONObject("_source");
                    jsonResultHits.add(source);
                }
    
                jsonResultBucket.put("hits", jsonResultHits);
            }
    
            jsonResultBucket.put("groupByKey", groupByKey);
            jsonResultBucket.put("groupByCount", groupByCount);
            jsonResultSub.add(jsonResultBucket);
        }
        
        jsonResultParent.put(groupByStr + "TotalCount", jsonResultSub.size());
        jsonResultParent.put(groupByStr + "List", jsonResultSub);
        
        return jsonResultSub;
        
    }
    
    public JSONObject getResult(JSONObject jsonObject) {
        JSONObject jsonResultParent = createResponse("0", "Success");
        
        try {
            
            analysisAggs(jsonResultParent, jsonObject, GROUPBY_FIELD_STR);
            analysisAggs(jsonResultParent, jsonObject, GROUPBY_DATE_STR);
            
        } catch (Exception e) {
            log.error("======analysis groupBy query result exception:", e);
            jsonResultParent = createResponse("-1", "Fail");
        }
        
        return jsonResultParent;
    }
    
    public JSONObject createResponse(String ret, String desc) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ret", ret);
        jsonObject.put("desc", desc);
        return jsonObject;
    }
    
}
