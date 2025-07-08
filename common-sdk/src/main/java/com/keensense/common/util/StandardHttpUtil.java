package com.keensense.common.util;

import com.alibaba.fastjson.JSONObject;
import com.keensense.common.exception.VideoException;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * 1400标准接口 请求工具类
 *
 * @author shixt
 */
public class StandardHttpUtil {
    static Logger log = LoggerFactory.getLogger(StandardHttpUtil.class);

    /**
     * post方式
     *
     * @param param1
     * @param param2
     * @return
     */
    public static String postHttp(String url, Map<String, String> paramMap) {
        String responseMsg = "";
        // 1.构造HttpClient的实例
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams()
                .setConnectionTimeout(20000);
        //读取数据超时：soTimeout
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(20000);
        httpClient.getParams().setContentCharset("UTF-8");
        // 用于测试的http接口的url
        // 2.构造PostMethod的实例
        PostMethod postMethod = new PostMethod(url);
        // 3.把参数值放入到PostMethod对象中
        if (null != paramMap) {
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                postMethod.addParameter(entry.getKey(), entry.getValue());// 增加参数1
            }
        }
        try {
            httpClient.executeMethod(postMethod);// 4.执行postMethod,调用http接口
            responseMsg = postMethod.getResponseBodyAsString().trim();// 5.读取内容
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
        log.info("result:" + responseMsg);
        return responseMsg;
    }

    /**
     * get方式
     *
     * @param param1
     * @param param2
     * @return
     */
    public static String getHttp(String url, Map<String, String> paramMap) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        long startTime = System.currentTimeMillis();
        String responseMsg = "";
        HttpClient httpClient = new HttpClient();// 1.构造HttpClient的实例
        httpClient.getHttpConnectionManager().getParams()
                .setConnectionTimeout(50000);
        //读取数据超时：soTimeout
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(50000);
        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();// 构造参数
        if (null != paramMap) {
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue())); // 增加参数1
            }
        }
        String param = URLEncodedUtils.format(params, "UTF-8");// 对参数编码
        log.info(uuid + " 语义搜 获取结果 提交参数------requestUrl:" + url);
        log.info(uuid + " 语义搜 获取结果 提交参数------param:" + param.toString());
        url = url + "?" + param;// 用于测试的http接口的url
//        log.info(url);
        // 2.创建GetMethod的实例
        GetMethod getMethod = new GetMethod(url);
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
            throw new VideoException("视图库连接超时");
        } catch (ConnectException e) {
            log.error(uuid + " error:" + e.getMessage());
            throw new VideoException("视图库连接异常");
        } catch (IOException e) {
            log.error(uuid + " error:" + e.getMessage());
            throw new VideoException("视图库未知异常");
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

    /**
     * 删除
     *
     * @param url
     * @param paramMap
     * @return
     */
    public static String deleteHttp(String url, Map<String, String> paramMap) {
        String uuid = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();
        String responseMsg = "";
        HttpClient httpClient = new HttpClient();// 1.构造HttpClient的实例
        httpClient.getHttpConnectionManager().getParams()
                .setConnectionTimeout(20000);
        //读取数据超时：soTimeout
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(20000);
        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();// 构造参数
        if (null != paramMap) {
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue())); // 增加参数1
            }
        }
        String param = URLEncodedUtils.format(params, "UTF-8");// 对参数编码
        log.info(uuid + "语义搜 获取结果 提交参数------requestUrl:" + url);
        log.info(uuid + "语义搜 获取结果 提交参数------params:" + params.toString());
        url = url + "?" + param;// 用于测试的http接口的url
        log.info(url);
        // 2.创建GetMethod的实例
        DeleteMethod deleteMethod = new DeleteMethod(url);
        // 使用系统系统的默认的恢复策略
        deleteMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        try {
            httpClient.executeMethod(deleteMethod);// 3.执行getMethod,调用http接口
            byte[] responseBody = deleteMethod.getResponseBody();// 4.读取内容
            responseMsg = new String(responseBody);// 5.处理返回的内容
            int statusCode = httpClient.executeMethod(deleteMethod);
            if (statusCode != HttpStatus.SC_OK) {
                log.error(uuid + "getHttp 请求失败，http status = " + statusCode + ",responseMsg:" + responseMsg);
//                return null;
                throw new VideoException("视图库连接失败:" + statusCode);
            }
        } catch (SocketTimeoutException e) {
            log.error(uuid + " error:" + e.getMessage());
            throw new VideoException("视图库连接超时");
        } catch (ConnectException e) {
            log.error(uuid + " error:" + e.getMessage());
            throw new VideoException("视图库连接异常");
        } catch (IOException e) {
            log.error(uuid + " error:" + e.getMessage());
            throw new VideoException("视图库未知异常");
        } finally {
            deleteMethod.releaseConnection();// 6.释放连接
        }
        log.info(uuid + " cost:" + (System.currentTimeMillis() - startTime));
        log.info(uuid + " result:" + responseMsg);
        return responseMsg;
    }


    public static String postContentWithJson(String url, String content) {
        String uuid = UUID.randomUUID().toString();
        long start = System.currentTimeMillis();
        log.info(uuid + " 开始url:" + url);
        log.info(uuid + " 开始content:" + content);
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        HttpClient httpClient = new HttpClient();// 1.构造HttpClient的实例
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
            throw new UnsupportedOperationException(e);
        }
        try {
            int statusCode = httpClient.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                log.error(uuid + " 请求失败，http status = " + statusCode);
                throw new VideoException(" 视图库连接失败:" + statusCode);
//                return null;
            }
            InputStream inputStream = method.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            StringBuffer stringBuffer = new StringBuffer();
            String respStr = "";
            while ((respStr = br.readLine()) != null) {
                stringBuffer.append(respStr);
            }
            br.close();
            String ret = stringBuffer.toString();
            log.info(uuid + " 返回，ret = " + ret);
            log.info(uuid + " cost:" + (System.currentTimeMillis() - start));
            boolean isvalid = JSONObject.isValid(ret);
            if (!isvalid) {
                log.error(uuid + " " + "非json格式错误");
                throw new VideoException("视图库错误:" + ret);
            }
            return ret;
        } catch (SocketTimeoutException e) {
            log.error(uuid + " " + e.getMessage(), e);
            throw new VideoException("视图库连接超时");
        } catch (ConnectException e) {
            log.error(uuid + " error:" + e.getMessage(), e);
            throw new VideoException("视图库连接异常");
        } catch (IOException e) {
            log.error(uuid + " " + e.getMessage(), e);
            throw new VideoException("视图库未知异常");
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
            if (httpClient != null && httpClient.getHttpConnectionManager() != null) {
                httpClient.getHttpConnectionManager().closeIdleConnections(0);
            }
        }
    }

/*    public static void main(String[] args) {
        Map<String,String> params = new HashMap<String,String>();
		System.out.println(StandardHttpUtil.getHttp("http://192.168.0.70:9999/keensense-search/VIID/Persons", params));
	}*/
}
