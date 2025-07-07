package com.keensense.task.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Description: detail定时任务工具类
 * @Author: admin
 * @CreateDate: 2019/5/16 17:50
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class TranscodeHttpUtils {

    private TranscodeHttpUtils() {
    }

    /**
     * get方式
     *
     * @param ipAddress   ip地址
     * @param serviceName 服务名
     * @param paramMap    参数
     * @return String
     */
    public static String getHttp(String ipAddress, String serviceName, Map<String, String> paramMap) {
        String responseMsg = "";

        // 1.构造HttpClient的实例
        HttpClient httpClient = new HttpClient();
        httpClient.setConnectionTimeout(60000);
        httpClient.setTimeout(60000);
        // 构造参数
        List<BasicNameValuePair> params = new LinkedList<>();
        if (null != paramMap) {
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                // 增加参数1
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        // 对参数编码
        String param = URLEncodedUtils.format(params, "UTF-8");
        // 用于测试的http接口的url
        String url = "http://" + ipAddress + ":8081/" + serviceName + "?" + param;
        log.debug(url);
        // 2.创建GetMethod的实例
        GetMethod getMethod = new GetMethod(url);
        // 使用系统的默认的恢复策略
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        try {
            // 3.执行getMethod,调用http接口
            httpClient.executeMethod(getMethod);
            // 4.读取内容
            byte[] responseBody = getMethod.getResponseBody();
            // 5.处理返回的内容
            responseMsg = new String(responseBody);
            log.debug(responseMsg);
        } catch (IOException e) {
            log.error("http failed", e);
        } finally {
            // 6.释放连接
            getMethod.releaseConnection();
        }
        return responseMsg;
    }

}
