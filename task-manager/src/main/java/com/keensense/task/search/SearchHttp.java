package com.keensense.task.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @Description:
 * @Author: wujw
 * @CreateDate: 2019/12/5 9:37
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class SearchHttp {

    private SearchHttp(){}

    private static String nacosUrl;

    private static RestTemplate restTemplate = new RestTemplate();

    private static final int KEENSENSE_PORT = 9999;
    /**获取ES数据*/
    private static final String ES_RESULT_URL = "/VIID/Result";
    /**获取磁盘容量*/
    private static final String DATA_CAPACITY = "/VIID/Capacity";
    /**删除文件接口*/
    private static final String FILE_DELETE_URL = "/VIID/File";
    /**结构化删除数据接口*/
    private static final String OBJEXT_DELETE_DATA_URL = "/VIID/Result/Delete/Data";
    /**结构化删除数据接口*/
    private static final String OBJEXT_DELETE_IMAGE_URL = "/VIID/Result/Delete/Image";
    /**人群密度删除数据接口*/
    private static final String DENSITY_DELETE_DATA_URL = "/VIID/CrowdDensity/Delete/Data";
    /**人群密度删除图片接口*/
    private static final String DENSITY_DELETE_IMAGE_URL = "/VIID/CrowdDensity/Delete/Image";
    /**交通违章删除数据接口*/
    private static final String TRAFFIC_DELETE_VIOLATION_URL = "/VIID/Violation/Delete";
    /**交通流量删除数据接口*/
    private static final String TRAFFIC_DELETE_FLOWRATE_URL = "/VIID/Vehicleflowrate/Delete";
    /**交通事件删除数据接口w*/
    private static final String TRAFFIC_DELETE_EVENT_URL = "/VIID/Event/Delete";
    /** 删除数据失败返回消息 */
    public static final String DELETE_FAILED_MESSAGE = "{\"Status\":\"Failed\",\"ErrorCode\":\"-2\",\"ErrorMessage\":\"connection error!\"}";

    /***
     * 获取磁盘容量
     * @return: java.lang.String
     */
    public static String getDataSpace(){
        ResponseEntity<String> responseEntity = restTemplate.exchange(getUrl(DATA_CAPACITY), HttpMethod.GET, null, String.class);
        if(responseEntity.hasBody()){
            return responseEntity.getBody();
        } else {
            return "{\"fastdfs\":{\"total\":0,\"usage\":0},\"es\":{\"total\":0,\"usage\":0}}";
        }
    }

    /***
     * 根据条件获取快照数据
     * @param paramMap 请求入参
     * @return: java.lang.String
     */
    public static String getResultOrderByCreateTime(Map<String, Object> paramMap){
        ResponseEntity<String> responseEntity = restTemplate.exchange(getUrlWithParam(ES_RESULT_URL,paramMap), HttpMethod.GET, null, String.class);
        if(responseEntity.hasBody()){
            return responseEntity.getBody();
        } else {
            return "{\"UnitListObject\":{\"Count\":-1}}";
        }
    }

    /***
     * 异步特征和es数据
     * @param paramMap 请求入参
     * @return: java.lang.String
     */
    public static String deleteData(Map<String, Object> paramMap){
        return deleteRequest(OBJEXT_DELETE_DATA_URL, paramMap);
    }

    /***
     * 同步删除快照，特征和es数据
     * @param paramMap 请求入参
     * @return: java.lang.String
     */
    public static String deleteDataImage(Map<String, Object> paramMap){
        return deleteRequest(OBJEXT_DELETE_IMAGE_URL, paramMap);
    }

    /***
     * 删除人群密度快照，特征和es数据
     * @param paramMap 请求入参
     * @return: java.lang.String
     */
    public static String deleteDensityData(Map<String, Object> paramMap){
        return deleteRequest(DENSITY_DELETE_DATA_URL, paramMap);
    }

    /***
     * 人群密度快照，特征和es数据
     * @param paramMap 请求入参
     * @return: java.lang.String
     */
    public static String deleteDensityDataImage(Map<String, Object> paramMap){
        return deleteRequest(DENSITY_DELETE_IMAGE_URL, paramMap);
    }

    /***
     * 删除违章数据
     * @return: java.lang.String
     */
    public static String deleteViolationData(Map<String, Object> paramMap){
        return deleteRequest(TRAFFIC_DELETE_VIOLATION_URL, paramMap);
    }

    /***
     * 删除流量数据
     * @return: java.lang.String
     */
    public static String deleteVehicleflowrateData(Map<String, Object> paramMap){
        return deleteRequest(TRAFFIC_DELETE_FLOWRATE_URL, paramMap);
    }

    /***
     * 删除事件数据
     * @return: java.lang.String
     */
    public static String deleteEventData(Map<String, Object> paramMap){
        return deleteRequest(TRAFFIC_DELETE_EVENT_URL, paramMap);
    }

    /***
     * 删除事件数据
     * @return: java.lang.String
     */
    public static String deleteFile(Map<String, Object> paramMap){
        return deleteRequest(FILE_DELETE_URL, paramMap);
    }

    /***
     * @description: 删除数据通用调用方法
     * @param interfaceUrl 接口地址
     * @param paramMap 参数列表
     * @return: java.lang.String
     */
    private static String deleteRequest(String interfaceUrl, Map<String, Object> paramMap){
        String url = getUrlWithParam(interfaceUrl,paramMap);
        log.info("delete with url = " + url);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
        if(responseEntity.hasBody()){
            return responseEntity.getBody();
        } else {
            return DELETE_FAILED_MESSAGE;
        }
    }

    /***
     * @description: 获取完整请求路径
     * @param interfaceUrl 接口地址
     * @return: java.lang.String
     */
    private static String getUrl(String interfaceUrl){
        StringBuilder sb = new StringBuilder(nacosUrl);
        sb.append(":");
        sb.append(KEENSENSE_PORT);
        sb.append(interfaceUrl);
        return sb.toString();
    }

    /***
     * @description: 获取完整请求路径
     * @param interfaceUrl 接口地址
     * @return: java.lang.String
     */
    private static String getUrlWithParam(String interfaceUrl, Map<String,Object> map){
        StringBuilder sb = new StringBuilder(nacosUrl);
        sb.append(":");
        sb.append(KEENSENSE_PORT);
        sb.append(interfaceUrl);
        sb.append("?");
        map.forEach((key , value) -> {
            if(value != null){
                sb.append(key).append("=").append(value).append("&");
            }
        });
        return sb.toString();
    }

    public static void setNacosUrl(String nacosUrl) {
        SearchHttp.nacosUrl = nacosUrl;
    }

}
