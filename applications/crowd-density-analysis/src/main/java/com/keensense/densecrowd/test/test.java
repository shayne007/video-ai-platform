package com.keensense.densecrowd.test;

import com.keensense.common.util.StandardHttpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 17:05 2020/2/19
 * @Version v0.1
 */
public class test {
    public static void main(String[] args) {
        testexeRealPlayFile();
    }

    public static void testgetzoneterminaldata() {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("jsondata[pageIndex]", "1");
        paramMap.put("jsondata[pageCount]", "20");
        paramMap.put("jsondata[groupName]", "*");
        paramMap.put("jsondata[showType]", "0");
        String result = StandardHttpUtil.postHttp("http://172.16.0.72/php/getzoneterminaldata.php", paramMap);
        System.out.println(result);
    }

    public static void testexeRealPlayFile() {
        String text = "11";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("jsondata[rtype]", "startbct");
        paramMap.put("jsondata[param1]", "1");
        paramMap.put("jsondata[param2]", "3");
        paramMap.put("jsondata[param3]", "0");
        paramMap.put("jsondata[param4]", text);
        paramMap.put("jsondata[param6]", "0");
        paramMap.put("jsondata[param7]", "3");
        paramMap.put("jsondata[param8]", "0");
        paramMap.put("jsondata[param9]", "3");
        String result = StandardHttpUtil.postHttp("http://172.16.0.72/php/exeRealPlayFile.php", paramMap);
        System.out.println(result);
    }
}
