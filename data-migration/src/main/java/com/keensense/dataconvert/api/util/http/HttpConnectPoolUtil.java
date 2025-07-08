package com.keensense.dataconvert.api.util.http;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.dataconvert.api.util.HttpClientResult;
import com.keensense.dataconvert.biz.common.consts.CommonConst;
import net.sf.ehcache.util.NamedThreadFactory;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.Executors.newScheduledThreadPool;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.api.util.http
 * @Description： <p> HttpConnectPoolUtil - 提高吞吐能力 - 测试 </p>
 * @Author： - Jason
 * @CreatTime：2019/7/31 - 12:12
 * @Modify By：
 * @ModifyTime： 2019/7/31
 * @Modify marker：
 */
public class HttpConnectPoolUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpConnectPoolUtil.class);

    /**
     * 编码格式。发送编码格式统一用UTF-8
     */
    private static final String ENCODING = "UTF-8";

    /**
     * 设置连接建立的超时时间为10s
     */
    private static final int CONNECT_TIMEOUT = CommonConst.HTTP_CLIENT_CONNECT_TIMEOUT;

    /**
     * socket 连接超时
     */
    private static final int SOCKET_TIMEOUT = CommonConst.HTTP_CLIENT_SOCKET_TIMEOUT;


    /**
     * maxTotal 是整个连接池的最大连接数
     * defaultMaxPerRoute 是每个route默认的最大连接数
     * setMaxPerRoute(final HttpRoute route, final int max) route的最大连接数，优先于defaultMaxPerRoute。
     */
    /**
     * 整个连接池的最大连接数
     */
    private static final int MAX_CONN = 500;

    /**
     * 每个route默认的连接数
     */
    private static final int MAX_ROUTE = 50;
    private static final int MAX_PRE_ROUTE = 50;

    /**
     * 发送请求的客户端单例
     */
    private static CloseableHttpClient httpClient;

    /**
     * 连接池管理类
     */
    private static PoolingHttpClientConnectionManager manager;


    /**
     * 处理异常
     */
    private static ScheduledExecutorService monitorExecutor;

    private static final ExecutorService executorService = newFixedThreadPool(20, new NamedThreadFactory("-- test---"));

    /**
     * 错误重试次数
     */
    private static final int RETRY_COUNT = 5;

    private static  final long IDLE_TIME_OUT = 20 * 1000L;
    private static  final long INITIAL_DELAY = 3 * 1000L;
    private static  final long PERIOD = 10 * 1000L;


    private static final String COLON = ":";
    private static final String SLASH = "/";

    /**
     * 相当于线程锁,用于线程安全
     */
    private final static Object syncLock = new Object();


    /**
     *  对http请求进行基本设置
     * @param httpRequestBase  http请求
     */
    private static void setRequestConfig(HttpRequestBase httpRequestBase){
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECT_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT * 1000)
                .setSocketTimeout(SOCKET_TIMEOUT * 1000).build();
        httpRequestBase.setConfig(requestConfig);
    }


    /**
     * 获取httpClient
     * @param url
     * @return
     */
    public static CloseableHttpClient getHttpClient(String url){
        String hostName = url.split(SLASH)[2];
        int port = 80;
        if (hostName.contains(COLON)){
            String[] args = hostName.split(COLON);
            hostName = args[0];
            port = Integer.parseInt(args[1]);
        }
        if (httpClient == null){
            synchronized (syncLock){
                if (httpClient == null){
                    httpClient = createHttpClient(hostName, port);
                    monitorExecutor = newScheduledThreadPool(1);
                    /**
                     * 处理掉异常的连接
                     */
                    monitorExecutor.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            manager.closeExpiredConnections();
                            manager.closeIdleConnections(IDLE_TIME_OUT, TimeUnit.MILLISECONDS);
                            logger.warn("[HttpClient]Close expired and idle for over 5s connection ...");
                        }
                    }, INITIAL_DELAY, PERIOD, TimeUnit.MILLISECONDS);
                }
            }
        }
        return httpClient;
    }



    /**
     * 根据host和port构建httpclient实例
     * @param host 要访问的域名
     * @param port 要访问的端口
     * @return
     */
    public static CloseableHttpClient createHttpClient(String host, int port){

        ConnectionSocketFactory plainSocketFactory = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", plainSocketFactory).register("https", sslSocketFactory).build();

        /**
         * 长时间极高频次的访问服务器，启用keep-alive非常合适
         */
        ConnectionKeepAliveStrategy algReqStrategy = new ConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                HeaderElementIterator it = new BasicHeaderElementIterator
                        (response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (it.hasNext()) {
                    HeaderElement he = it.nextElement();
                    String param = he.getName();
                    String value = he.getValue();
                    if (value != null && param.equalsIgnoreCase("timeout")) {
                        return Long.parseLong(value) * 1000;
                    }
                }
                return 60 * 1000;
            }
        };

        manager = new PoolingHttpClientConnectionManager(registry);
        //设置连接参数 // 最大连接数
        manager.setMaxTotal(MAX_CONN);
        // 路由最大连接数
        manager.setDefaultMaxPerRoute(MAX_PRE_ROUTE);
        HttpHost httpHost = new HttpHost(host, port);
        manager.setMaxPerRoute(new HttpRoute(httpHost), MAX_ROUTE);
        //请求失败时,进行请求重试
        HttpRequestRetryHandler handler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException e, int i, HttpContext httpContext) {
                if (i > RETRY_COUNT){
                    logger.error("[重试超过5次,放弃请求]retry has more than 5 time, give up request.");
                    return false;
                }
                if (e instanceof NoHttpResponseException){
                    logger.error("[服务器没有响应]Receive no response from server, retry.");
                    return true;
                }
                if (e instanceof SSLHandshakeException){
                    logger.error("[SSL握手异常]SSL hand shake exception.");
                    return false;
                }
                if (e instanceof InterruptedIOException){
                    logger.error("[超时]InterruptedIOException.");
                    return false;
                }
                if (e instanceof UnknownHostException){
                    logger.error("[服务器不可达]Server host unknown.");
                    return false;
                }
                if (e instanceof ConnectTimeoutException){
                    logger.error("[连接超时]Connection Time out.");
                    return false;
                }
                if (e instanceof SSLException){
                    logger.error("[SSL异常]SSLException.");
                    return false;
                }
                HttpClientContext context = HttpClientContext.adapt(httpContext);
                HttpRequest request = context.getRequest();
                if (!(request instanceof HttpEntityEnclosingRequest)){
                    //如果请求不是关闭连接的请求
                    return true;
                }
                return false;
            }
        };
        CloseableHttpClient client = HttpClients.custom()
                .setConnectionManager(manager)
                .setConnectionManagerShared(true)
                .setKeepAliveStrategy(algReqStrategy)
                .setRetryHandler(handler).build();
        return client;
    }

    /**
     * 设置post请求的参数 - 参数进行封装设置
     * @param httpPost
     * @param params
     */
    private static void setPostParams(HttpPost httpPost, Map<String, String> params){
        List<NameValuePair> paramsList = new ArrayList<>();
        Set<String> keys = params.keySet();
        for (String key: keys){
            paramsList.add(new BasicNameValuePair(key, params.get(key)));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(paramsList, ENCODING));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    /**
     * json 格式数据post请求
     * @param url      请求地址
     * @param reqJson  请求json数据
     * @return
     * @throws Exception
     */
    public static HttpClientResult doPostJson(String url, String reqJson) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        setRequestConfig(httpPost);
        CloseableHttpResponse response = null;
        httpPost.setHeader("HTTP Method","POST");
        httpPost.setHeader("Connection","Keep-Alive");
        httpPost.setHeader("Content-Type","application/json;charset=utf-8");
        StringEntity entity = new StringEntity(reqJson);
        entity.setContentType("application/json;charset=utf-8");
        httpPost.setEntity(entity);
        try {
            response = getHttpClient(url).execute(httpPost, HttpClientContext.create());
            return getHttpClientResult(response, httpClient, httpPost);
        } finally {
            release(response, httpClient);
        }
    }


    /**
     * 获得响应结果
     * @param httpResponse
     * @param httpClient
     * @param httpMethod
     * @return
     * @throws Exception
     */
    public static HttpClientResult getHttpClientResult(CloseableHttpResponse httpResponse,
                                                       CloseableHttpClient httpClient, HttpRequestBase httpMethod) throws Exception {
        httpResponse = httpClient.execute(httpMethod);
        if (httpResponse != null && httpResponse.getStatusLine() != null) {
            String content = "";
            if (httpResponse.getEntity() != null) {
                content = EntityUtils.toString(httpResponse.getEntity(), ENCODING);
            }
            return new HttpClientResult(httpResponse.getStatusLine().getStatusCode(), content);
        }
        return new HttpClientResult(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    /**
     * Description: 释放资源
     *
     * @param httpResponse
     * @param httpClient
     * @throws IOException
     */
    public static void release(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) throws IOException {
        // 释放资源
        if (httpResponse != null) {
            httpResponse.close();
        }
        if (httpClient != null) {
            httpClient.close();
        }

    }


    /**
     * post请求
     * @param url
     * @param params
     * @return
     */
    public static HttpClientResult post(String url, Map<String, String> params) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        setRequestConfig(httpPost);
        setPostParams(httpPost, params);
        CloseableHttpResponse response = null;
        try {
            response = getHttpClient(url).execute(httpPost, HttpClientContext.create());
            if (response != null && response.getStatusLine() != null) {
                String content = "";
                if (response.getEntity() != null) {
                    content = EntityUtils.toString(response.getEntity(), ENCODING);
                }
                return new HttpClientResult(response.getStatusLine().getStatusCode(), content);
            }
        }finally {
            release(response, httpClient);
        }
        return new HttpClientResult(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    /**
     * 关闭连接池
     */
    public static void closeConnectionPool(){
        try {
            httpClient.close();
            manager.close();
            monitorExecutor.shutdown();
        } catch (IOException e) {
            logger.info("=== 连接池关闭异常:{} ===",e.getMessage());
        }
    }


    /**
     * 多线才并发测试
     * @param args
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            threadDeal();
        }
        long endTime = System.currentTimeMillis();
        logger.warn("Cost time:[{}]ms",endTime-startTime);

    }


    /**
     *
     * batchSize 12
     *
     */
    public static void  threadDeal(){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("scenes", 0);
                jsonObject.put("isDetectFullFrame", 1);

                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < 10; i++) {
                    JSONObject imageObject = new JSONObject();
                    imageObject.put("data", "data:image/jpeg;base64,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");
                    imageObject.put("id", IdUtil.fastSimpleUUID());
                    jsonArray.add(imageObject);
                }

                jsonObject.put("images", jsonArray);
                HttpClientResult httpClientResult = null;
                try {
                    logger.info("imageArray:Size:{}",jsonArray.size());
                    httpClientResult = HttpConnectPoolUtil.doPostJson("http://172.16.1.68:8100/v8/images/objects", jsonObject.toString());
                } catch (Exception e) {
                    logger.error("=== \n error:\n{}\n ===",e.getMessage());
                }
                logger.info("== result:\n{}", httpClientResult.getContent());
            }
        });
    }

}
