package com.keensense.image.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by zhanx xiaohui on 2019-05-10.
 */
@Slf4j
public class HttpClientUtil {

    private HttpClientUtil() {
    }

    private static final String RESPONSE_LOG = "the response is {}.";

    public static String post(String url, String object, Map<String, String> headerMap) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 创建Post请求
        HttpPost httpPost = new HttpPost(url);

        // 设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            httpPost.setHeader(entry.getKey(), entry.getValue());
        }
        HttpEntity entity = null;
        entity = new StringEntity(object, "UTF-8");
        httpPost.setEntity(entity);
        // 响应模型
        CloseableHttpResponse response = null;
        String result = "";
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null && 200 == response.getStatusLine().getStatusCode()) {
                result = EntityUtils.toString(responseEntity);
                //log.debug("the response is {}", result);
                return result;
            }
            log.error("response code is not 200, the response is {}", JSON.toJSONString(response));
        } catch (Exception e) {
            log.error(RESPONSE_LOG, result, e);
        } finally {
            try {
                // 释放资源
                httpClient.close();
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("", e);
            }
        }
        return null;
    }

    public static String delete(String url, String object, Map<String, String> headerMap) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 创建Post请求
        HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(url);

        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            httpDelete.setHeader(entry.getKey(), entry.getValue());
        }
        HttpEntity entity = null;
        try {
            entity = new StringEntity(object);
        } catch (UnsupportedEncodingException e) {
            log.error("", e);
        }
        httpDelete.setEntity(entity);
        // 响应模型
        CloseableHttpResponse response = null;
        String result = "";
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpDelete);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null && 200 == response.getStatusLine().getStatusCode()) {
                result = EntityUtils.toString(responseEntity);
                //log.debug("the response is {}", result);
                return result;
            }
            log.error("response code is not 200, the response is {}", JSON.toJSONString(response));
        } catch (Exception e) {
            log.error(RESPONSE_LOG, result, e);
        } finally {
            try {
                // 释放资源
                httpClient.close();
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("", e);
            }
        }
        return null;
    }

    public static String get(String url, Map<String, String> headerMap) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 创建Post请求
        HttpGet httpGet = new HttpGet(url);

        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            httpGet.setHeader(entry.getKey(), entry.getValue());
        }
        // 响应模型
        CloseableHttpResponse response = null;
        String result = "";
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null && 200 == response.getStatusLine().getStatusCode()) {
                result = EntityUtils.toString(responseEntity);
//                //log.debug("the response is {}", result);
                return result;
            }
            log.error("response code is not 200, the response is {}", JSON.toJSONString(response));
        } catch (Exception e) {
            log.error(RESPONSE_LOG, result, e);
        } finally {
            try {
                // 释放资源
                httpClient.close();
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("", e);
            }
        }
        return null;
    }
}