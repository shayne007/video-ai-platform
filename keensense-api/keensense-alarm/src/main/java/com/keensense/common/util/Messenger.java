package com.keensense.common.util;


import org.apache.commons.lang3.StringUtils;

/**
 * @author ycl
 */
public class Messenger {
    private static final ThreadLocal<String> MSGER = new ThreadLocal<>();

    public static void sendMsg(String message) {
        MSGER.set(message);
    }

    public static String acceptMsg() {
        try {
            String s = MSGER.get();
            if (StringUtils.isEmpty(s)) {
                return "0";
            }
            return s;
        } finally {
            MSGER.set(null);
            MSGER.remove();
        }


    }

    public static void cancel() {
        MSGER.set(null);
        MSGER.remove();
    }

}
