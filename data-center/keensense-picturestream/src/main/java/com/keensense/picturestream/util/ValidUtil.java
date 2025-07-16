package com.keensense.picturestream.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * @Description: 校验工具类
 * @Author: wujw
 * @CreateDate: 2019/12/5 15:21
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class ValidUtil {

    /***
     * @description: 校验参数中的字符串
     * @param paramJson  请求参数
     * @param key        key值
     * @param required  是否必填
     * @return: java.lang.String
     */
    public static String validString(JSONObject paramJson, String key, boolean required) {
        String val = paramJson.getString(key);
        if (required && StringUtils.isEmpty(val)) {
            throw VideoExceptionUtil.getValidException(key + " 不能为空！");
        }
        return val;
    }

    /**
     * @param paramJson 请求参数
     * @param key       键值
     * @param required  是否必填
     * @param minValue  最大值
     * @param maxValue  最小值
     * @description: 从请求参数中获取固定值，若没有则按默认值获取
     * @return: int
     */
    public static Integer getInteger(JSONObject paramJson, String key, int minValue, int maxValue, boolean required) {
        Integer result = null;
        if (paramJson.containsKey(key)) {
            result = isPositiveInteger(paramJson, key, null, minValue, maxValue);
        }
        if (required && result == null) {
            throw VideoExceptionUtil.getValidException(key + "不能为空");
        }
        return result;
    }

    /**
     * @param paramJson    请求参数
     * @param key          键值
     * @param defaultValue 默认值
     * @param minValue     最大值
     * @param maxValue     最小值
     * @description: 从请求参数中获取固定值，若没有则按默认值获取
     * @return: int
     */
    public static Integer isPositiveInteger(JSONObject paramJson, String key, Integer defaultValue, int minValue, int maxValue) {
        if (paramJson.containsKey(key)) {
            String value = paramJson.getString(key);
            if (isInteger(value)) {
                BigDecimal val = new BigDecimal(value);
                BigDecimal min = new BigDecimal(minValue);
                BigDecimal max = new BigDecimal(maxValue);
                if (val.compareTo(min) == -1 || val.compareTo(max) == 1) {
                    throw VideoExceptionUtil.getValidException(key + "的值必须在为[" + minValue + "," + maxValue + "]之间的整数");
                }
                return val.intValue();
            } else {
                throw VideoExceptionUtil.getValidException(key + "的值必须在为[" + minValue + "," + maxValue + "]之间的整数");
            }
        }
        return defaultValue;
    }

    /**
     * @Description: 是否是整数
     * @param a 需要验证的字符串
     * @return 合法返回 true ; 不合法返回false
     */
    private static boolean isInteger(String a){
        return PatternMatchUtils.simpleMatch(a, "^-?\\d+$");
    }
}
