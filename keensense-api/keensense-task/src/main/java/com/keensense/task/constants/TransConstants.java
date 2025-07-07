package com.keensense.task.constants;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

/**
 * @Description: 转码常量类
 * @Author: wujw
 * @CreateDate: 2019/5/25 11:39
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class TransConstants {

    private TransConstants(){}

    /**转码成功*/
    public static final int STATUS_SUCCESS = 1;
    /**转码失败*/
    public static final int STATUS_FAILED = 3;

    /***
     * @description: 获取转码状态
     * @param obj 请求参数
     * @return: Integer
     */
    public static Integer getTransStatus(JSONObject obj){
        return obj.getInteger("status");
    }

    /***
     * @description: 获取转码进度
     * @param obj 请求参数
     * @return: Integer
     */
    public static Integer getTransProgress(JSONObject obj){
        return obj.getInteger("progress");
    }

    /***
     * @description: 获取转码文件路径
     * @param obj 请求参数
     * @return: String
     */
    public static String getTransFtpUrl(JSONObject obj){
        return obj.getString("ftp_url");
    }

    /***
     * @description: 解析转码返回结果
     * @param result 响应报文
     * @return: com.alibaba.fastjson.JSONObject
     */
    public static JSONObject getTransCodeResult(String result){
        if(StringUtils.isEmpty(result)){
            return null;
        }else{
            return JSON.parseObject(result);
        }
    }
}
