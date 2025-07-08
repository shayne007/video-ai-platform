package com.keensense.search.utils;

import com.alibaba.fastjson.JSONObject;
import com.keensense.common.exception.VideoException;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhanx xiaohui on 2019-02-23.
 */
public class ParametercheckUtil {

    private static final String REG = "[\"#${}\'\\[\\]]";

    private ParametercheckUtil() {
    }

    public static String checkEmpty(JSONObject json, String key) {
        String waitingCheckParametor = json.getString(key);
        if (StringUtils.isEmpty(waitingCheckParametor)) {
            throw new VideoException(key + " is Required");
        }
        return waitingCheckParametor;
    }

    public static boolean isTimeLegal(String patternString) {

        Pattern a = Pattern.compile(
            "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s((([0-1][0-9])|(2?[0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");

        Matcher b = a.matcher(patternString);
        return (b.matches() && patternString.length() == 19);
    }

    public static void checkEmpty(String parametor, Object object) {
        if (object == null) {
            throw new VideoException(parametor + " can not be empty");
        }
    }

    public static void checkLength(String parametor, Object value, int startlength, int endLength) {
        if (null != value && !(((String) value).length() >= startlength
            && ((String) value).length() <= endLength)) {
            throw new VideoException(
                parametor + " should be longer than " + startlength + " and shorter than "
                    + endLength);
        }
    }

    public static void checkSpecialChar(String parametor, Object value) {
        if (null == value) {
            return;
        }
        Pattern p = Pattern.compile(REG);
        Matcher m = p.matcher((String) value);
        if (m.find()) {
            throw new VideoException(
                parametor + " contains special char");
        }
    }

    public static void checkInteger(String parametor, Object value) {
        if (StringUtils.isEmpty(value)) {
            return;
        }
        try {
            Integer.parseInt((String) value);
        } catch (Exception e) {
            throw new VideoException("the " + parametor + " is not a integer");
        }
    }

    public static void checkDate(String parametor, Object value) {
        if (null == value) {
            return;
        }
        if (((String)value).length()!=14){
            throw new VideoException(parametor + " is not a Date yyyyMMddHHmmss");
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            format.setLenient(false);//此处指定日期/时间解析是否不严格，在true是不严格，false时为严格
            format.parse((String)value);
        } catch (Exception e) {
            throw new VideoException(parametor + " is not a Date yyyyMMddHHmmss");
        }
    }

    public static void checkLong(String parametor, Object value) {
        if (StringUtils.isEmpty(value)) {
            return;
        }
        try {
            Long.parseLong((String) value);
        } catch (Exception e) {
            throw new VideoException("the " + parametor + " is not a long");
        }
    }

    public static void checkDouble(String parametor, Object value) {
        if (StringUtils.isEmpty(value)) {
            return;
        }
        try {
            boolean isFinite = Double.isFinite(Double.parseDouble((String) value));
            if(!isFinite){
                throw new VideoException("the " + parametor + " is not a float");
            }
        } catch (Exception e) {
            throw new VideoException("the " + parametor + " is not a double");
        }
    }
    public static void checkFloat(String parametor, Object value) {
        if (StringUtils.isEmpty(value)) {
            return;
        }
        try {
            boolean isFinite = Float.isFinite(Float.parseFloat((String) value));
            if(!isFinite){
                throw new VideoException("the " + parametor + " is not a float");
            }
        } catch (Exception e) {
            throw new VideoException("the " + parametor + " is not a float");
        }
    }
}

/**
 * @program: data
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-02-23 14:49
 **/