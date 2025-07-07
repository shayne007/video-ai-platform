package com.keensense.picturestream.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class IDUtil {

    private IDUtil(){}
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String threadName(String topicName){
        return String.format("%s_%s_%s",
            topicName,new SimpleDateFormat("HHmmssSSS").format(new Date()),
            UUID.randomUUID().toString().toUpperCase().substring(0, 5));
    }
}

