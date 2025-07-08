package com.keensense.search.service.impl;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @since 2025/7/8
 */
public class JviaFeatureSearch {
    public static boolean deleteFeature(String ip, String serialnumber, Integer objType, String id, String startTime, String endTime, int i) {

        return false;
    }

    public static boolean deleteOfflineFeatures(String serialnumber, int i) {
        return false;
    }

    public static boolean deleteOnlineFeatures(String serialnumber, String time, int i) {
        return false;
    }

    public static void initInstance(String redisHost, int redisPort, String brokerList) {
    }

    public static Map<String,Object> searchFeature(String toJSONString, float threshold, int i) {
        return new HashMap<>();
    }
}
