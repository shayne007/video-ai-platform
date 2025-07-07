package com.keensense.task.util;

import com.loocme.sys.util.PatternUtil;

/**
 * @Description: 常规校验类
 * @Author: kellen
 * @CreateDate: 2019/3/16 14:53
 * @Version: 1.0
 */
public class ValidUtil {

    private ValidUtil(){}

    /**
     * 校验时间格式：yyyy-MM-dd HH:mm:ss
     */
    private static final String MATCH_TIME_FORMAT = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?" +
            "((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?" +
            "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|" +
            "(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?" +
            "((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?" +
            "((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s((([0-1][0-9])|(2?[0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
    /**
     * 校验时间格式：yyyy-MM-dd HH:mm:ss的字符串长度
     */
    private static final int TIME_LEGAL_LENGTH = 19;

    /**
     * @Description: 大于等于0的整数
     * @param a 需要验证的字符串
     * @return 合法返回 true ; 不合法返回false
     */
    public static boolean isPositiveIntegerAndZero(String a){
        return PatternUtil.isMatch(a, "^(0|\\+?[1-9][0-9]*)$");
    }

    /**
     * @Description: 是否是整数
     * @param a 需要验证的字符串
     * @return 合法返回 true ; 不合法返回false
     */
    public static boolean isInteger(String a){
        return PatternUtil.isMatch(a, "^-?\\d+$");
    }

    /**
     * @Description: 是否是float
     * @param a 需要验证的字符串
     * @return 合法返回 true ; 不合法返回false
     */
    public static boolean isFloat(String a){
        return PatternUtil.isMatch(a, "^([1-9]\\d*|0)(\\.\\d*)?$");
    }

    /**
     * @Description: 断是否是离线文件
     * @param a 需要验证的字符串
     * @return 合法返回 true ; 不合法返回false
     */
    public static boolean isOffice(String a){
        return PatternUtil.isMatch(a, "^\\s*(http|ftp|https|file|/).*$");
    }

    /**
     * @Description: 判断输入的字符串是否满足时间格式 ： yyyy-MM-dd HH:mm:ss
     * @param patternString 需要验证的字符串
     * @return 合法返回 true ; 不合法返回false
     */
    public static boolean isTimeLegal(String patternString) {
        return patternString.length() == TIME_LEGAL_LENGTH && PatternUtil.isMatch(patternString, MATCH_TIME_FORMAT);
    }

    /**
     * @Description: 是否是IP
     * @param a 需要验证的字符串
     * @return 合法返回 true ; 不合法返回false
     */
    public static boolean isIp(String a){
        return PatternUtil.isMatch(a, "^(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\." +
                "(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\." +
                "(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])$");
    }

    /**
     * @Description: 是否是端口
     * @param a 需要验证的字符串
     * @return 合法返回 true ; 不合法返回false
     */
    public static boolean isPort(String a){
        return PatternUtil.isMatch(a, "^([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-4]\\d{4}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])$");
    }

}
