package com.keensense.dataconvert.biz.common.consts;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.framework.config.common
 * @Description： <p> ConfigPathConstant - 配置文件路径常量  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/23 - 11:41
 * @Modify By：
 * @ModifyTime： 2019/7/23
 * @Modify marker：
 */
public class ConfigPathConstant {

    /**
     * config/ehcache/ehcache.xml 缓存配置文件地址
     */
    public static final  String CONFIG_EHCACHE_XML = "config/ehcache/ehcache.xml";

    /**
     * config/kafka/kafka.properties kafka 配置文件
     */
    public static final  String CONFIG_KAFKA_PATH = "config/kafka/kafka.properties";

    /**
     * config/redis/redis.properties redis 配置文件
     */
    public static final  String CONFIG_REDIS_PATH = "config/redis/redis.properties";

    /**
     * app-data-convert.properties
     */
    public static final  String CONFIG_DATA_CONVERT_CLASS_PATH = "app-data-convert.properties";

    /**
     * structure
     */
    public static  String CONFIG_STRUCTURE_BUILD_FILE = "structure/build-es/";

    /**
     * OBJECT_RESULT es 索引构建
     */
    public static  String CONFIG_SUBMETER_ES_OBJECT_RESULT_BULID_FILE = "submeter/bulidObjextResultIndex.txt";

    /**
     * VLPR_RESULT 索引构建
     */
    public static  String CONFIG_SUBMETER_ES_VLPR_RESULT_BULID_FILE = "submeter/bulidVlprResultIndex.txt";

    /**
     * BIKE_RESULT
     */
    public static  String CONFIG_SUBMETER_ES_BIKE_RESULT_BULID_FILE = "submeter/bulidBikeResultIndex.txt";


    /**
     * es 字段转换表
     */
    public static String  CONFIG_SUBMETER_VLPR_MIGRATE_ES_ES = "submeter/migrate/vlpr_es2es.properties";
    public static String  CONFIG_SUBMETER_OBJEXT_MIGRATE_ES_ES = "submeter/migrate/objext_es2es.properties";
    public static String  CONFIG_SUBMETER_BIKE_MIGRATE_ES_ES = "submeter/migrate/bike_es2es.properties";

    private ConfigPathConstant() {}

}
