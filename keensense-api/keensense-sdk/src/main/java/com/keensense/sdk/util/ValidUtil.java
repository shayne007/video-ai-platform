package com.keensense.sdk.util;

import org.apache.commons.lang.StringUtils;

/**
 * @Description:
 * @Author: kellen
 * @CreateDate: 2019/3/22 14:31
 * @Version: 1.0
 */
public class ValidUtil {

    public static boolean validImageSize(String img,int size){
        int length = size * 1024 * 1024;
        if(img != null && img.length() <= length){
            return true;
        }
        return false;
    }

    public static int changeMainType(String mainType) {
        int rslt = 0;
        switch(mainType) {
            case "human":
            case "1":
                rslt = 1;
                break;
            case "vehicle":
            case "2":
                rslt = 2;
                break;
            case "face":
            case "3":
                rslt = 3;
                break;
            case "bike":
            case "4":
                rslt = 4;
                break;
            default: break;

        }
        return rslt;
    }
}
