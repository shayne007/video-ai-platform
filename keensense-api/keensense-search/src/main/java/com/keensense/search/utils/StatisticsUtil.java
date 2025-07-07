package com.keensense.search.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.concurrent.atomic.AtomicLong;

public class StatisticsUtil {
    private StatisticsUtil(){}

    private static AtomicLong esTime = new AtomicLong();
    private static AtomicLong decodeTime = new AtomicLong();
    private static AtomicLong diskTime = new AtomicLong();
    private static AtomicLong kafkaTime = new AtomicLong();
    private static AtomicLong totalTime = new AtomicLong();

    public static void addEsTime(long time) {
        esTime.addAndGet(time);
    }

    public static void addDecodeTime(long time) {
        decodeTime.addAndGet(time);
    }

    public static void addDiskTime(long time) {
        diskTime.addAndGet(time);
    }

    public static void addKafkaTime(long time) {
        kafkaTime.addAndGet(time);
    }

    public static void addTotalTime(long time) {
        totalTime.addAndGet(time);
    }

    public static JSONObject getStatisticsData() {
        JSONObject object = new JSONObject();
        object.put("estime", esTime.get());
        object.put("decodetime", decodeTime.get());
        object.put("disktime", diskTime.get());
        object.put("kafkatime", kafkaTime.get());
        object.put("totaltime", totalTime.get());
        return object;
    }

    public static void clear() {
        esTime.set(0);
        decodeTime.set(0);
        diskTime.set(0);
        kafkaTime.set(0);
        totalTime.set(0);
    }
}
