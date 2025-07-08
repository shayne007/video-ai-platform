package com.keensense.densecrowd.util;

import com.keensense.common.exception.VideoException;
import com.loocme.sys.util.StringUtil;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.net.*;
import java.util.*;

public class FtpHttpGetUtils {
    private static final Log log = LogFactory.getLog(FtpHttpGetUtils.class);


    /**
     * @param ipAddress   服务名称
     * @param serviceName 请求参数集合
     * @param param       接口响应数据
     * @return
     */
    public static String getHttp(String ipAddress, String serviceName, String param) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String responseMsg = "";
        // 1.构造HttpClient的实例
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setSoTimeout(DbPropUtil.getInt("ftp_server_timeout", 30000));


        // 用于测试的http接口的url
        String url = "http://" + ipAddress + "/" + serviceName + "?" + param;
        long startTime = System.currentTimeMillis();
        log.info(uuid + " url : " + url);
        log.info(uuid + " paramMap : " + param);
        // 2.创建GetMethod的实例
        GetMethod getMethod = new GetMethod(url);

        // 使用系统系统的默认的恢复策略
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

        try {
            // 3.执行getMethod,调用http接口
            httpClient.executeMethod(getMethod);

            // 4.读取内容
            byte[] responseBody = getMethod.getResponseBody();

            // 5.处理返回的内容
            responseMsg = new String(responseBody, "utf-8");

            log.debug(responseMsg);

        } catch (SocketTimeoutException e) {
            log.error(uuid + " SocketTimeoutException " + e);
            responseMsg = "";
        } catch (HttpException e) {
            log.error(uuid + " HttpException " + e);
            e.printStackTrace();
        } catch (ConnectException e) {
            log.error(uuid + " ConnectException " + e);
            throw new VideoException("无法连接到请求地址");
        } catch (IOException e) {
            log.error(uuid + " IOException " + e);
            e.printStackTrace();
        } catch (Exception e) {
            log.error(uuid + " Exception" + e);
            e.printStackTrace();
        } finally {
            // 6.释放连接
            if (getMethod != null) {
                getMethod.releaseConnection();
            }
            if (httpClient != null && httpClient.getHttpConnectionManager() != null) {
                httpClient.getHttpConnectionManager().closeIdleConnections(0);
            }
        }
        log.info(uuid + " cost:" + (System.currentTimeMillis() - startTime) + " responseMsg : " + responseMsg);
        return responseMsg;
    }

    /**
     * get方式
     *
     * @param param1
     * @param param2
     * @return
     */
    public static String getHttp(String serviceName, String params) {
        String ipAddress = DbPropUtil.getString("ftp-server-httpurl", "127.0.0.1:8081");
        return getHttp(ipAddress, serviceName, params);
    }

    /**
     * get方式
     *
     * @param param1
     * @param param2
     * @return
     */
    public static String getHttp(String serviceName, Map<String, String> paramMap) {
        String ipAddress = DbPropUtil.getString("ftp-server-httpurl", "127.0.0.1:8081");
        // 构造参数
        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        if (null != paramMap) {
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {

                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue())); // 增加参数1
            }
        }
        String param = URLEncodedUtils.format(params, "UTF-8");// 对参数编码
        return getHttp(ipAddress, serviceName, param);
    }

    /**
     * get方式
     *
     * @param url
     * @return
     */
    public static String getHttp(String url) {
        String responseMsg = "";

        // 1.构造HttpClient的实例
        HttpClient httpClient = new HttpClient();

        log.debug(url);
        // 2.创建GetMethod的实例
        GetMethod getMethod = new GetMethod(url);

        // 使用系统系统的默认的恢复策略
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

        try {
            // 3.执行getMethod,调用http接口
            httpClient.executeMethod(getMethod);

            // 4.读取内容
            byte[] responseBody = getMethod.getResponseBody();

            // 5.处理返回的内容
            responseMsg = new String(responseBody);
            log.debug(responseMsg);

        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 6.释放连接
            if (getMethod != null) {
                getMethod.releaseConnection();
            }
            if (httpClient != null && httpClient.getHttpConnectionManager() != null) {
                httpClient.getHttpConnectionManager().closeIdleConnections(0);
            }
        }
        return responseMsg;
    }


    /**
     * post方式
     *
     * @param serviceName
     * @param paramMap
     * @return
     */
    public static String postHttp(String serviceName, Map<String, String> paramMap) {
        String responseMsg = "";

        String ipAddress = DbPropUtil.getString("ftp-server-httpurl", "127.0.0.1:8081");

        // 1.构造HttpClient的实例
        HttpClient httpClient = new HttpClient();

        httpClient.getParams().setContentCharset("GBK");

        // 用于测试的http接口的url
        String url = "http://" + ipAddress + "/" + serviceName;

        // 2.构造PostMethod的实例
        PostMethod postMethod = new PostMethod(url);

        // 3.把参数值放入到PostMethod对象中
        // 方式1：
        // 构造参数
        // NameValuePair[] data = { new NameValuePair("param1", param1),
        // new NameValuePair("param2", param2) };
        // postMethod.setRequestBody(params);

        // 方式2：
        // postMethod.addParameter("param1", param1);
        // postMethod.addParameter("param2", param2);

        if (null != paramMap) {
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                postMethod.addParameter(entry.getKey(), entry.getValue());// 增加参数1
            }
        }

        try {
            // 4.执行postMethod,调用http接口
            httpClient.executeMethod(postMethod);// 200

            // 5.读取内容
            responseMsg = postMethod.getResponseBodyAsString().trim();
            log.info(responseMsg);

            // 6.处理返回的内容
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 7.释放连接
            if (postMethod != null) {
                postMethod.releaseConnection();
            }
            if (httpClient != null && httpClient.getHttpConnectionManager() != null) {
                httpClient.getHttpConnectionManager().closeIdleConnections(0);
            }
        }
        return responseMsg;
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }


    public static String parseURL(String url) {
        String host = "127.0.0.1";
        if (StringUtil.isNotNull(url) && url.startsWith("http")) {
            try {
                URL dbUrl = new URL(url);
                host = dbUrl.getHost();
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return host;
    }

    /**
     * 测试的main方法
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(sendGet("http://192.68.0.63:9999/VIID/Faces", "Faces.DeviceID=1562582932859&Faces.PageRecordNum=1000&Faces.RecordStartNo=1"));
    }

}
