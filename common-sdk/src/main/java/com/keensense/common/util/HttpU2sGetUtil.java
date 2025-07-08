package com.keensense.common.util;

import com.keensense.common.exception.VideoException;
import com.loocme.sys.entities.HttpManagerParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 11:29 2019/10/9
 * @Version v0.1
 */
@Slf4j
public class HttpU2sGetUtil {
    public static HttpClient client;

    static {
        HttpManagerParams params = new HttpManagerParams();
        HttpConnectionManagerParams managerParams = new HttpConnectionManagerParams();
        managerParams.setConnectionTimeout(params.getConnectionTimeout());
        managerParams.setSoTimeout(params.getSoTimeout());
        managerParams.setStaleCheckingEnabled(params.getStaleCheckEnabled());
        managerParams.setTcpNoDelay(params.getTcpNoDelay());
        managerParams.setDefaultMaxConnectionsPerHost(
                params.getDefaultMaxConnectionsPerHost());
        managerParams.setMaxTotalConnections(params.getMaxTotalConnections());
        managerParams.setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(0, false));

        HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.setParams(managerParams);

        client = new HttpClient(connectionManager);
    }

    public static String postContent(String url, String content) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        log.info(uuid + " 开始http调用地址:" + url);
//        if (content.length() < 1000) {
//            log.info(uuid + " 参数：" + content);
//        } else {
//            log.info(uuid + "content large");
//        }
        log.info(String.format("[%s] content %s", uuid, content.length() > 1500 ? content.substring(0, 1500) : content));
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }

        PostMethod method = new PostMethod(url);
        method.addRequestHeader("Connection", "Keep-Alive");
        method.addRequestHeader("Content-Type", "application/json");
        method.getParams().setContentCharset("UTF-8");
        method.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        method.getParams().setUriCharset("UTF-8");
        try {
            method.setRequestEntity(new StringRequestEntity(content,
                    "application/json", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        try {
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                HttpU2sGetUtil.log.error(uuid + " 请求失败，http status = " + statusCode);
                throw new VideoException(1000 + statusCode, "连接视图库失败:" + statusCode);
            }
            InputStream inputStream = method.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));

            String respStr = "";
            while ((respStr = br.readLine()) != null) {
                stringBuffer.append(respStr);
            }
            br.close();

        } catch (SocketTimeoutException e) {
            log.error(uuid + " 连接视图库连接超时" + e);
            throw new VideoException("连接视图库连接超时");
        } catch (IOException e) {
            log.error(uuid + " 连接视图库未知异常" + e);
            throw new VideoException("连接视图库未知异常");
        } finally {
            method.releaseConnection();
        }
        log.info(uuid + " 返回：" + stringBuffer);
        return stringBuffer.toString();
    }

    /**
     * get方式
     *
     * @return
     */
    public static String getHttp(String url, String author) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        long startTime = System.currentTimeMillis();
        String responseMsg = "";
        HttpClient httpClient = new HttpClient();// 1.构造HttpClient的实例
        httpClient.getHttpConnectionManager().getParams()
                .setConnectionTimeout(50000);
        //读取数据超时：soTimeout
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(50000);
        log.info(uuid + " 语义搜 获取结果 提交参数------requestUrl:" + url);
//        log.info(url);
        // 2.创建GetMethod的实例
        GetMethod getMethod = new GetMethod(url);
        if (StringUtils.isNotEmpty(author)) {
            getMethod.addRequestHeader("Authorization", "Basic " + author);
        }
        // 使用系统系统的默认的恢复策略
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        try {
            httpClient.executeMethod(getMethod);// 3.执行getMethod,调用http接口
            byte[] responseBody = getMethod.getResponseBody();// 4.读取内容
            responseMsg = new String(responseBody);// 5.处理返回的内容
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                log.error(uuid + "getHttp 请求失败，http status = " + statusCode + ",responseMsg:" + responseMsg);
//                return null;
                throw new VideoException("视图库连接失败:" + statusCode);
            }
        } catch (SocketTimeoutException e) {
            log.error(uuid + " error:" + e.getMessage());
            throw new VideoException("连接超时");
        } catch (ConnectException e) {
            log.error(uuid + " error:" + e.getMessage());
            throw new VideoException("连接异常");
        } catch (IOException e) {
            log.error(uuid + " error:" + e.getMessage());
            throw new VideoException("未知异常");
        } finally {
            if (getMethod != null) {
                getMethod.releaseConnection();
            }
            if (httpClient != null && httpClient.getHttpConnectionManager() != null) {
                httpClient.getHttpConnectionManager().closeIdleConnections(0);
            }
        }
        log.info(uuid + " cost:" + (System.currentTimeMillis() - startTime));
        log.info(uuid + " result:" + responseMsg);
        return responseMsg;
    }

}