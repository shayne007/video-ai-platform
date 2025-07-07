package com.keensense.archive.utils;

import java.io.IOException;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {
    
    private static final String EN_CODED = "UTF-8";
    
    public static String doPost(String url, String JSONBody,String auth) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        
        if(StringUtils.isNotEmpty(auth)){
            httpPost.addHeader("Authorization",auth);;
        }
        
        if (StringUtils.isNotEmpty(JSONBody)) {
            httpPost.setEntity(new StringEntity(JSONBody));
        }
        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String responseContent = EntityUtils.toString(entity, EN_CODED);
        response.close();
        httpClient.close();
        return responseContent;
    }
    
    public static String doGet(String url, Map<String, String> para) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        if (MapUtils.isNotEmpty(para)) {
            for (Map.Entry<String, String> entry : para.entrySet()) {
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
        }
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String responseContent = EntityUtils.toString(entity, EN_CODED);
        response.close();
        httpClient.close();
        return responseContent;
    }
    
}
