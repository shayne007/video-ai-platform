package com.keensense.dataconvert.biz.service;

import com.keensense.dataconvert.api.util.HttpClientResult;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.service
 * @Description： <p> HttpClientService - 可用但是无明显效果 [注释] </p>
 * @Author： - Jason
 * @CreatTime：2019/7/31 - 14:56
 * @Modify By：
 * @ModifyTime： 2019/7/31
 * @Modify marker：
 */
//@Service
public class HttpClientService {

    private static CloseableHttpClient httpClient;

    /**
     * 信任SSL证书
     */
    static {
        try {
            SSLContext sslContext = SSLContextBuilder.create().useProtocol(SSLConnectionSocketFactory.SSL)
                    .loadTrustMaterial((x, y) -> true).build();
            RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000).build();
            httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).setSSLContext(sslContext)
                    .setSSLHostnameVerifier((x, y) -> true).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@Autowired
    private RequestConfig config;

    /**
     * 不带参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
     * @param url
     * @return
     * @throws Exception
     */
    public String doGet(String url) throws Exception {
        // 声明 http get 请求
        HttpGet httpGet = new HttpGet(url);
        // 装载配置信息
        httpGet.setConfig(config);
        // 允许重定向
        httpGet.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS,true);
        // 发起请求
        CloseableHttpResponse response = HttpClientService.httpClient.execute(httpGet);
        // 判断状态码是否为200
        if (response.getStatusLine().getStatusCode() == 200) {
            // 返回响应体的内容
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        }
        return null;
    }

    /**
     * 带参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
     * @param url
     * @param map
     * @return
     * @throws Exception
     */
    public String doGet(String url, Map<String, Object> map) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(url);
        if (map != null) {
            // 遍历map,拼接请求参数
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
            }
        }
        // 调用不带参数的get请求
        return this.doGet(uriBuilder.build().toString());

    }

    /**
     * 带参数的post请求
     * @param url
     * @param map
     * @param headers
     * @return
     * @throws Exception
     */
    public HttpClientResult doPost(String url, Map<String, Object> map , Map<String,Object> headers) throws Exception {
        // 声明httpPost请求
        HttpPost httpPost = new HttpPost(url);
        // 加入配置信息
        httpPost.setConfig(config);
        // 判断map是否为空，不为空则进行遍历，封装from表单对象
        if (map != null) {
            List<NameValuePair> list = new ArrayList<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
            // 构造from表单对象
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "UTF-8");

            // 把表单放到post里
            httpPost.setEntity(urlEncodedFormEntity);
        }
        // 设置请求头
        if (headers != null) {
            for (String key : headers.keySet()) {
                String value = headers.get(key).toString();
                httpPost.addHeader(key,value);
            }
        }
        // 发起请求
        CloseableHttpResponse response = HttpClientService.httpClient.execute(httpPost);
        return new HttpClientResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(
                response.getEntity(), "UTF-8"));
    }

    /**
     * post by json
     * @param url
     * @param reqJson
     * @return
     * @throws Exception
     */
    public  HttpClientResult doPostJson(String url, String reqJson) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(config);
        //设置头信息
        httpPost.setHeader("HTTP Method","POST");
        httpPost.setHeader("Connection","Keep-Alive");
        httpPost.setHeader("Content-Type","application/json;charset=utf-8");
        StringEntity entity = new StringEntity(reqJson);
        entity.setContentType("application/json;charset=utf-8");
        httpPost.setEntity(entity);
        // 发起请求
        CloseableHttpResponse response = HttpClientService.httpClient.execute(httpPost);
        return new HttpClientResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(
                response.getEntity(), "UTF-8"));
    }


    /**
     * 不带参数post请求
     * @param url
     * @return
     * @throws Exception
     */
    HttpClientResult doPost(String url) throws Exception {
        return this.doPost(url, null,null);
    }

    /**
     * 带参以JSON方式发送的post请求
     * @param url
     * @param jsonString
     * @param headers
     * @return
     * @throws Exception
     */
    public static HttpClientResult doPostWithJson(String url, String jsonString, Map<String,Object> headers) throws Exception {
        // 声明httpPost请求
        HttpPost httpPost = new HttpPost(url);
        // 设置请求头
        if (headers != null) {
            for (String key : headers.keySet()) {
                String value = headers.get(key).toString();
                httpPost.addHeader(key, value);
            }
        }
        // 设置以Json数据方式发送
        StringEntity stringEntity = new StringEntity(jsonString, "utf-8");
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);

        // 发起请求
        CloseableHttpResponse response = httpClient.execute(httpPost);
        return new HttpClientResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(
                response.getEntity(), "UTF-8"));
    }

}
