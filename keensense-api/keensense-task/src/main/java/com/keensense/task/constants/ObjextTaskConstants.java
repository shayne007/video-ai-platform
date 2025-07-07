package com.keensense.task.constants;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @Description: 结构化任务参数常用配置
 * @Author: wujw
 * @CreateDate: 2019/5/10 10:13
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class ObjextTaskConstants {

    private ObjextTaskConstants(){}

    /**
     * 录像流分析
     */
    public static final int VIDEO_TYPE_ONLINE = 1;
    /**
     * 联网录像下载
     */
    public static final int VIDEO_TYPE_DOWNLAND = 2;
    /**
     * 是否直接使用请求参数中的param
     */
    public static final String PARAM_IS_VALID = "1";
    /**
     * 随机数工具类
     */
    private static Random random = new Random();
    /**
     * 目标类型列表
     */
    private static final List<String> TARGET_TYPE_LIST = new ArrayList<>(3);

    /***
     * @description: 判断是否是分片录像任务
     * @param url 任务路径
     * @return: boolean
     */
    public static boolean isSliceVideo(String url) {
        String urlLow = url.toLowerCase();
        return urlLow.startsWith("vas") && urlLow.contains("starttime") && urlLow.contains("endtime");
    }

    /**
     * 搜索参数默认配置
     */
    private static final Map<String, Object> SEARCH_CFG_MAP = new LinkedHashMap<>(8);
    /**
     * 感兴趣区域参数默认配置
     */
    private static final JSONObject UDRSET_DEFAULT_MAP = new JSONObject(3);

    static {
        TARGET_TYPE_LIST.add("HUMAN");
        TARGET_TYPE_LIST.add("VEHICLE");
        TARGET_TYPE_LIST.add("BIKE");

        SEARCH_CFG_MAP.put("targetTypes", TARGET_TYPE_LIST);
        SEARCH_CFG_MAP.put("mode", "ACCURATE");
        SEARCH_CFG_MAP.put("tripArea", new String[0]);
        SEARCH_CFG_MAP.put("tripWires", new String[0]);
        SEARCH_CFG_MAP.put("dominantColor", new String[0]);
        SEARCH_CFG_MAP.put("humanUpperBodyColor", new String[0]);
        SEARCH_CFG_MAP.put("humanLowerBodyColor", new String[0]);
        SEARCH_CFG_MAP.put("queryImage", "url");

        UDRSET_DEFAULT_MAP.put("isInterested", false);
        UDRSET_DEFAULT_MAP.put("udrNum", 0);
        UDRSET_DEFAULT_MAP.put("udrVertices", new ArrayList<>(0));
    }

    /**
     * VSD_task表ID获取
     * @return Long
     */
    public static Long getTaskId() {
        String taskIdStr = "1" + getRandom6Number(9);
        return Long.valueOf(taskIdStr);
    }

    /**
     * @description: 获取随机数
     * @param size 字符长度
     * @return String
     */
    private static String getRandom6Number(int size) {
        // 随机产生的字符串
        String randString = "0123456789";
        // 随机种子
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(randString.charAt(random.nextInt(6)));
        }
        return sb.toString();
    }

    public static Map<String, Object> getSearchCfgMap() {
        return SEARCH_CFG_MAP;
    }

    public static JSONObject getUdrsetDefaultMap() {
        return UDRSET_DEFAULT_MAP;
    }
}
