package com.keensense.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 基于HttpClient 4.5
 *
 * @author Administrator
 */
@Slf4j
public class HttpClientUtil {
    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static SSLConnectionSocketFactory sslsf = null;
    private static PoolingHttpClientConnectionManager cm = null;
    private static SSLContextBuilder builder = null;

    static {
        try {
            builder = new SSLContextBuilder();
            // 全部信任 不做身份鉴定
            builder.loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            });
            sslsf = new SSLConnectionSocketFactory(builder.build(),
                    new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register(HTTP, new PlainConnectionSocketFactory()).register(HTTPS, sslsf).build();
            cm = new PoolingHttpClientConnectionManager(registry);
            // max connection
            cm.setMaxTotal(200);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).setConnectionManager(cm)
                .setConnectionManagerShared(true).build();
        return httpClient;
    }

    /**
     * http请求post
     *
     * @param url       发送url
     * @param urlParam  请求get内容
     * @param encode    编码格式
     * @param timeout   超时时间
     * @param postParam 请求Post内容
     * @return contentType 请求头格式
     * @throws Exception
     */
    public static String requestPost(String url, String urlParam, String postParam, String encode, Integer timeout,
                                     String contentType) {
        String respContent = null;
        if (StringUtils.isNotBlank(urlParam)) {
            url = url + "?" + urlParam;
        }
        int timeOutCommon = 3000;
        String encodeCommon = "UTF-8";
        String contentTypeCommon = "application/json";
        CloseableHttpClient httpClient = null;
        try {
            httpClient = getHttpClient();
        } catch (Exception e1) {
            log.error(e1.getMessage(), e1);
            return  respContent;
        }
        // 创建HttpPost对象
        HttpPost httpPost = new HttpPost(url);
        if (timeout != null) {
            timeOutCommon = timeout;
        }
        // 配置请求参数
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.DEFAULT)
                .setExpectContinueEnabled(true).setSocketTimeout(timeOutCommon).setConnectTimeout(timeOutCommon)
                .setConnectionRequestTimeout(timeOutCommon).build();
        httpPost.setConfig(requestConfig);
        // 设置参数和请求方式
        if (StringUtils.isNotBlank(encode)) {
            encodeCommon = encode;
        }
        if (StringUtils.isNotBlank(postParam)) {
            if (StringUtils.isNotBlank(contentType)) {
                contentTypeCommon = contentType;
            }
            // 设置请求头和请求内容的编码格式
            ContentType contentType2 = ContentType.create(contentTypeCommon, encodeCommon);
            // post请求需表单提交
            StringEntity entity = new StringEntity(postParam, contentType2);
            // 设置请求头的编码格式
            entity.setContentEncoding(encodeCommon);
            httpPost.setEntity(entity);
        }
        HttpResponse resp;
        try {
            // 执行请求
            resp = httpClient.execute(httpPost);
            HttpEntity responseObj = resp.getEntity();
            respContent = EntityUtils.toString(responseObj, encodeCommon);
            if (StringUtils.isBlank(respContent)) {
                respContent = resp.toString();
            }
        } catch (IOException e) {
            log.error("HttpTool.requestPost 异常 请求url：" + url + ", 参数：" + postParam + "， 异常信息：" + e, e);
        } finally {
            // 关闭连接,释放资源
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return respContent;
    }

    /**
     * http请求post
     *
     * @param url       发送url
     * @param urlParam  请求get内容
     * @param encode    编码格式
     * @param timeout   超时时间
     * @param postParam 请求Post内容
     * @return contentType 请求头格式
     * @throws Exception
     */
    public static String requestPost(String url, String urlParam, String postParam, String encode, Integer timeout)
            {
        return requestPost(url, urlParam, postParam, encode, timeout, null);
    }

    /**
     * http请求post
     *
     * @param url       发送url
     * @param urlParam  请求get内容
     * @param encode    编码格式
     * @param postParam 请求Post内容
     * @return contentType 请求头格式
     */
    public static String requestPost(String url, String urlParam, String postParam, String encode) {
        return requestPost(url, urlParam, postParam, encode, null, null);
    }

    /**
     * http请求post
     *
     * @param url       发送url
     * @param urlParam  请求get内容
     * @param postParam 请求Post内容
     * @return contentType 请求头格式
     */
    public static String requestPost(String url, String urlParam, String postParam){
        return requestPost(url, urlParam, postParam, null, null, null);
    }

    /**
     * 发送 get请求 get("http://www.baidu.com","tn=98012&ch=15" ,1000)
     *
     * @param url     地址
     * @param param   中文需要encode
     * @param timeout utf
     * @param encode
     * @return
     */
    public static String get(String url, String param, Integer timeout, String encode) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String respContent = null;
        int timeOutCommon = 10000;
        String encodeCommon = "UTF-8";
        try {
            // 设置参数的超时
            if (timeout != null) {
                timeOutCommon = timeout;
            }
            if (StringUtils.isNotBlank(param)) {
                url = url + "?" + param;
            }
            if (StringUtils.isNotBlank(encode)) {
                encodeCommon = encode;
            }
            // 创建httpget.
            HttpGet httpget = new HttpGet(url);
            // 配置请求参数
            RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.DEFAULT)
                    .setExpectContinueEnabled(true).setSocketTimeout(timeOutCommon).setConnectTimeout(timeOutCommon)
                    .setConnectionRequestTimeout(timeOutCommon).build();
            httpget.setConfig(requestConfig);
            // 执行get请求
            CloseableHttpResponse resp = httpclient.execute(httpget);
            try {
                // 获取响应实体
                HttpEntity responseObj = resp.getEntity();
                respContent = EntityUtils.toString(responseObj, encodeCommon);
            } finally {
                resp.close();
            }
        } catch (IOException e) {
            log.error("http 请求地址错误:" + url + param + " 错误异常:" + e.getMessage(), e);
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return respContent;
    }

    public static void main(String[] args) throws Exception {
        String url = "http://192.168.0.71:9091/camera/startCapture";
        String string = HttpClientUtil.requestPost(url, null, "{\"password\":\"B5DEE41326398A4211E819CB6E0ABB1C\",\"monitorID\":\"1\",\"port\":\"8000\",\"ip\":\"192.168.0.199\",\"deviceID\":\"deviceID20bytesTotal\",\"taskID\":\"111100008888\",\"username\":\"admin\"}");
//		String url ="http://127.0.0.1:8082/user/post";
//		String string = HttpClientUtil.requestPost(url, null, "{\"password\":\"B5DEE41326398A4211E819CB6E0ABB1C\",\"monitorID\":\"1\",\"port\":\"8000\",\"ip\":\"192.168.0.199\",\"deviceID\":\"deviceID20bytesTotal\",\"taskID\":\"111100008888\",\"username\":\"admin\"}");

        log.info("requestPost:" + string);
    }
}
