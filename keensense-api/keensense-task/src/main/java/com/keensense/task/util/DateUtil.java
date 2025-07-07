package com.keensense.task.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @Description:
 * @Author: wujw
 * @CreateDate: 2019/5/13 15:39
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class DateUtil {

    private DateUtil() {
    }

    public static final DateTimeFormatter DEFAULT_MATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter YYYYMMDDHHMMSS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");

    /***
     * @description: 获取当天的00:00:00格式化日期
     * @return: LocalDateTime
     */
    public static String getFirstSecondStringToday() {
        return DEFAULT_MATTER.format(getTimeDateToday(0, 0, 0));
    }

    /***
     * @description: 获取当天的指定时间戳
     * @return: LocalDateTime
     */
    public static long getTimestampToday(int hour, int minute, int second) {
        return getTimestampOfDateTime(getTimeDateToday(hour, minute, second));
    }

    /***
     * @description: 获取当天的指定时间
     * @return: LocalDateTime
     */
    public static LocalDateTime getTimeDateToday(int hour, int minute, int second) {
        return LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, minute, second));
    }

    /****
     * @description: 将yyyy-MM-dd HH:mm:ss转换成时间
     * @param dateStr 时间字符串
     * @return: java.time.LocalDateTime
     */
    public static LocalDateTime parseDate(String dateStr) {
        return LocalDateTime.parse(dateStr, DEFAULT_MATTER);
    }

    /****
     * @description: 将时间戳转换成[yyyy-MM-dd HH:mm:ss]
     * @param current 时间戳
     * @return: java.time.LocalDateTime
     */
    public static String formatDate(long current) {
        return DEFAULT_MATTER.format(getDateTimeOfTimestamp(current));
    }

    /****
     * @description: 将时间戳转换成[yyyy-MM-dd HH:mm:ss]
     * @param current 时间戳
     * @return: java.time.LocalDateTime
     */
    public static String formatDate(long current, DateTimeFormatter dateTimeFormatter) {
        return dateTimeFormatter.format(getDateTimeOfTimestamp(current));
    }

    /****
     * @description: 时间戳转LocalDateTime
     * @param timestamp 时间戳
     * @return: java.time.LocalDateTime
     */
    public static LocalDateTime getDateTimeOfTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /****
     * @description: LocalDateTime转时间戳
     * @param localDateTime LocalDateTime转时间戳
     * @return: java.time.LocalDateTime
     */
    public static long getTimestampOfDateTime(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }

    /***
     * @description: 获取当前时间的Timestamp
     * @return: Timestamp
     */
    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

    /***
     * @description:   字符串转换成LocalDate
     * @param date     时间字符串
     * @param formatter 时间格式
     * @return: java.time.LocalDate
     */
    public static LocalDate parseLocalDate(String date, DateTimeFormatter formatter) {
        return LocalDate.parse(date, formatter);
    }

}
