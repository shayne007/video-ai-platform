package com.keensense.search.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.springframework.util.StringUtils;

/**
 * Created by zhanx xiaohui on 2019-08-21.
 */
public class DateUtil {
    public static Date generatorDate(String time, String type) throws ParseException {
        if (StringUtils.isEmpty(time)) {
            return null;
        }
        Date date;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        time += "000000";
        date = format.parse(time);
        if (!"start".equals(type)) {
            date = addDay(date, 1);
        }

        return date;
    }

    public static Date addDay(Date date, int change) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, change);

        return c.getTime();
    }
}
