package com.keensense.dataconvert.api.request;

import cn.hutool.core.date.SystemClock;
import com.alibaba.fastjson.JSON;
import com.keensense.dataconvert.api.util.HttpClientResult;
import com.keensense.dataconvert.api.util.http.HttpConnectPoolUtil;
import com.keensense.dataconvert.biz.common.consts.CommonConst;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName：AppAlgRequest
 * @Description： <p> AppAlgRequest </p>
 * @Author： - Jason
 * @CreatTime：2019/7/24 - 17:39
 * @Modify By：
 * @ModifyTime： 2019/7/24
 * @Modify marker：
 * @version V1.0
*/
public class AppAlgRequest implements java.io.Serializable{

    private static final long serialVersionUID = 2440476351853853408L;

    private static final Logger logger = LoggerFactory.getLogger(AppAlgRequest.class);

    private static int CONNECT_TIME_OUT = 10000;
    private static int SOCKET_TIME_OUT = 12000;

    /**
     *id
     */
    private String id;

    /**
     * 请求标志位
     */
    public AppAlgRequest() {
        super();
        this.id = UUID.randomUUID().toString();
    }


    /**
     * post 请求接口获取数据
     * @param url 请求地址
     * @param paramsJson 请求参数
     * @return
     * @throws Exception
     */
    public String httpClientPost(String url, String paramsJson) throws Exception {
        long startTime,endTime,costTime;
        double qps;
        startTime = SystemClock.now();
        HttpClientResult httpClientResult = HttpConnectPoolUtil.doPostJson(url, paramsJson);
        endTime = SystemClock.now();
        costTime = endTime-startTime;
        qps = (CommonConst.RECOG_FEATURE_PIC_BATCH * 1.0 / (costTime/1000));
        logger.info("=== [接口性能]-HttpClientPost-QPS:[{}(r/s)],costTime:[{}(ms)] ...",String.format("%.2f", qps),costTime);
        int statusCode = httpClientResult.getCode();
        if (statusCode != HttpStatus.SC_OK) {
            logger.error(" == AppAlgRequest:post:http响应code异常：{} === ", statusCode);
            throw new Exception("AppAlgRequest:post:http响应code异常:"+statusCode);
        }
        String respStr = httpClientResult.getContent();
        logger.debug("=== AppAlgRequest:post:响应信息 === \n {} ",respStr);
        return respStr;
    }

    /**
     * post
     * @param url
     * @param content
     * @return
     * @throws Exception
     */
    public String post(String url, String content) throws Exception {
        HttpResponse httpResponse = getHttpResponse(url, content);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        logger.debug("=== AppAlgRequest:post:返回http响应码:{} ===",statusCode);
        if (statusCode != HttpStatus.SC_OK) {
            logger.error(" == AppAlgRequest:post:http响应code异常：{} === ", statusCode);
            throw new Exception("AppAlgRequest:post:http响应code异常:"+statusCode);
        }
        String respStr = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
        logger.debug("=== AppAlgRequest:post:响应信息 === \n {} ",respStr);
        return respStr;
    }


    /**
     * 获取响应
     * @param url  请求地址
     * @param content
     * @return
     * @throws Exception
     */
    public static HttpResponse getHttpResponse(String url, String content) throws Exception{
        HttpResponse response = Request.Post(url)
                .connectTimeout(CONNECT_TIME_OUT).socketTimeout(SOCKET_TIME_OUT).useExpectContinue()
                .version(HttpVersion.HTTP_1_1).bodyString(content, ContentType.APPLICATION_JSON)
                .execute().returnResponse();
        return response;
    }



    public String getId() {
        return this.id;
    }

    /**
     * just for test
     * @param args
     */
    public static void main(String[] args) {
        AppAlgRequest appAlgRequest = new AppAlgRequest();
        String post = null;
        try {
            Map<String, String> params = new HashMap<>(10);
            params.put("id","e6b535b720c347e29accb3f419dc9e72");
            post = appAlgRequest.httpClientPost("http://localhost:9095/test/getTargetById", JSON.toJSONString(params));
            String post1 = appAlgRequest.post("http://localhost:9095/test/getTargetById", JSON.toJSONString(params));
            logger.info(post1);
        } catch (Exception e) {
            logger.error("=== requestError:{} ===",e.getMessage());
        }
        logger.info("==请求Url:{},响应结果:{} ==","http://localhost:9095/test/getTargetById",post);
    }
}
