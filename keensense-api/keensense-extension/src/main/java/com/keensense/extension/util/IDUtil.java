package com.keensense.extension.util;

import java.util.UUID;

public class IDUtil {

    private IDUtil(){}
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
