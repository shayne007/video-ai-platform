package com.keensense.dataconvert.biz.common.consts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.common.consts
 * @Description： <p> EsConst - 常量类 - indexName + typeName </p>
 * @Author： - Jason
 * @CreatTime：2019/7/24 - 10:05
 * @Modify By：
 * @ModifyTime： 2019/7/24
 * @Modify marker：
 */
public class EsConst {

    private static Logger logger = LoggerFactory.getLogger(EsConst.class);

    /**
     * objext_result
     */
    public static String ES_OBJEXT_RESULT_INDEX_NAME= "objext_result";
    public static String ES_OBJEXT_RESULT_INDEX_TYPE= "data";


    /**
     * vlpr_result
     */
    public static String ES_VLPR_RESULT_INDEX_NAME = "vlpr_result";
    public static String ES_VLPR_RESULT_INDEX_TYPE = "data";

    /**
     * bike_result
     */
    public static String ES_BIKE_RESULT_INDEX_NAME = "bike_result";
    public static String ES_BIKE_RESULT_INDEX_TYPE = "data";


    public static String ES_FACE_RESULT_INDEX_NAME = "face_result";
    public static String ES_FACE_RESULT_INDEX_TYPE = "data";

    public static String ES_SUMMARY_RESULT_INDEX_NAME = "summary_result";
    public static String ES_SUMMARY_RESULT_INDEX_TYPE = "data";

    /**
     * 初始化加载
     */
    static {
        load();
    }

    /**
     * app-data-convert.properties
     */
    public static void load() {
        logger.info("=== init load config file [EsConst] ===");
    }

    private EsConst() {}
}
