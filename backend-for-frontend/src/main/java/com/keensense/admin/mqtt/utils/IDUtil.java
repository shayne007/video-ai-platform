package com.keensense.admin.mqtt.utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class IDUtil {

    public static String getLongId(){
        // 时间戳 17位
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String dateString = simpleDateFormat.format(new Date());

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(dateString);

        // 随机数字 31位
        int count = 31;
        Random random = new Random();
        for(int i=0;i<count;i++){
            Integer num = random.nextInt(10);
            stringBuffer.append(String.valueOf(num));
        }

        return stringBuffer.toString();
    }

    public static void main(String[] args) {
        System.out.println(IDUtil.getLongId());
    }
}
