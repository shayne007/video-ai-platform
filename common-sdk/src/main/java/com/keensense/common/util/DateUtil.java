package com.keensense.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期格式转换，处理工具
 *
 * @description:
 * @author: luowei
 * @createDate:2019年5月13日 下午4:43:29
 * @company:
 */
@Slf4j
public class DateUtil {

    public static final String FORMAT_1 = "yyyy";
    public static final String FORMAT_2 = "yyyy-MM";
    public static final String FORMAT_3 = "yyyy-MM-dd";
    public static final String FORMAT_4 = "yyyy-MM-dd HH";
    public static final String FORMAT_5 = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_6 = "yyyy-MM-dd HH:mm:ss";

    private static final SimpleDateFormat YMDHMS = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 按照指定的格式，将日期类型对象转换成字符串，例如：yyyy-MM-dd,yyyy/MM/dd,yyyy/MM/dd hh:mm:ss
     * 如果传入的日期为null,则返回空值
     *
     * @param date   日期类型对象
     * @param format 需转换的格式
     * @return 日期格式字符串
     */
    public static String formatDate(Date date, String format) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formater = new SimpleDateFormat(format);
        return formater.format(date);
    }

    /**
     * 将日期类型对象转换成yyyy-MM-dd类型字符串 如果传入的日期为null,则返回空值
     *
     * @param date 日期类型对象
     * @return 日期格式字符串
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        return formater.format(date);
    }

    /**
     * 将日期类型对象转换成yyyy-MM-dd HH:mm:ss类型字符串 如果传入的日期为null,则返回空值
     *
     * @param date 日期类型对象
     * @return 日期格式字符串
     */
    public static String formatTime(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formater.format(date);
    }

    /**
     * 按照指定的格式，将字符串解析成日期类型对象，例如：yyyy-MM-dd,yyyy/MM/dd,yyyy/MM/dd hh:mm:ss
     *
     * @param dateStr 日期格式的字符串
     * @param format  字符串的格式
     * @return 日期类型对象
     */
    public static Date parseDate(String dateStr, String format) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            return formater.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将字符串（yyyy-MM-dd）解析成日期
     *
     * @param dateStr 日期格式的字符串
     * @return 日期类型对象
     */
    public static Date parseDate(String dateStr) {
        String ss = "/";
        if (dateStr.indexOf(ss) != -1) {
            dateStr = dateStr.replaceAll("/", "-");
        }
        return parseDate(dateStr, "yyyy-MM-dd");
    }

    /**
     * 将字符串解析成对应日期格式的日期
     *
     * @param value 日期格式字符串
     * @return 日期类型对象
     */
    public static Date parse(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        value = value.trim().replaceAll("/", "-");
        if (value.length() == FORMAT_1.length()) {
            return parseDate(value, FORMAT_1);
        } else if (value.length() == FORMAT_2.length()) {
            return parseDate(value, FORMAT_2);
        } else if (value.length() == FORMAT_3.length()) {
            return parseDate(value, FORMAT_3);
        } else if (value.length() == FORMAT_4.length()) {
            return parseDate(value, FORMAT_4);
        } else if (value.length() == FORMAT_5.length()) {
            return parseDate(value, FORMAT_5);
        } else if (value.length() == FORMAT_6.length()) {
            return parseDate(value, FORMAT_6);
        } else {
            throw new RuntimeException("解析日期格式出错，与指定格式不匹配.");
        }
    }

    /**
     * 两个时间相差的天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static Integer getDays(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        // 先将时分秒都去掉
        Date start = DateUtil.parseDate(DateUtil.formatDate(startDate, "yyyy-MM-dd"));
        Date end = DateUtil.parseDate(DateUtil.formatDate(endDate, "yyyy-MM-dd"));
        Long intervalDays = (end.getTime() - start.getTime()) / (24 * 3600 * 1000);
        return intervalDays.intValue();
    }

    public static Date addDays(Date startDate, int d) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
//		calendar.add(calendar.YEAR, 1);// 把日期往后增加一年.整数往后推,负数往前移动
//		calendar.add(calendar.DAY_OF_MONTH, 1);// 把日期往后增加一个月.整数往后推,负数往前移动
        // 把日期往后增加一天.整数往后推,负数往前移动
        calendar.add(Calendar.DATE, d);
//		calendar.add(calendar.WEEK_OF_MONTH, 1);// 把日期往后增加一个月.整数往后推,负数往前移动
        startDate = calendar.getTime();
        return startDate;
    }

    public static void main(String[] args) {
//		System.out.println(formatDate(new Date(), "yyyyMMddHHmmss"));
        System.out.println(formatDate(addDays(parseDate("2018-10-14"), 1)));
//		System.out.println(formatDate(new Date(), "yyyyMMddHHmm"));
    }

    /**
     * 字符串转时间
     *
     * @param rq 格式：yyyyMMdd
     * @return
     * @throws Exception
     */
    public static Date parseYmdhms(String rq) {
        if (rq == null) {
            return null;
        }
        try {
            return new SimpleDateFormat("yyyyMMddHHmmss").parse(rq);
        } catch (ParseException e) {
            System.out.println("【字符转换时间异常】：" + e.getMessage());
            throw new RuntimeException("解析日期格式出错，与指定格式不匹配.");
        }
    }
}
