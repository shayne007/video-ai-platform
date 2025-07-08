package com.keensense.dataconvert.biz.common.cache.redis;

/**
 * @ClassName：RedisConstant
 * @Description： <p> redis常量 - 存储处理异常的数据  拿出来再次处理[异常队列处理]  </p>
 * @Author： - Jason
 * @CreatTime：2019/3/28
 * @Modify By：
 * @ModifyTime： 2019/3/28
 * @Modify marker：
 * @version V1.0
*/
public class RedisConstant {
	
	//----------------------- Redis常量信息 Start -----------------------------
	/**
	 * redis 缓存key不存在返回的标志
	 */
	public static final String CACHE_KEY_NOT_EXIST = "nil";
	
	/**
	 * 缓存key的分割符
	 */
	public static final String CACHE_KEY_SPLIT_CHAR = ":";

	/**
	 * key模糊匹配通配符
	 */
	public static final String CACHE_KEY_PATTERN_CHAR = "*";

    /**
     * 10秒
     */
    public static final int INT_TEN_SECOND_EXPIRE = 10;


    /**
     * 一分钟
     */
    public static final int INT_MINUTE_EXPIRE = 60;


    /**
     * 一小时
     */
    public static final int INT_HOUR_EXPIRE = 60*60;

    /**
     *INT_DAY_EXPIRE 1天
     */
    public static final int INT_DAY_EXPIRE = 24*60*60;

    /**
     *INT_WEEK_EXPIRE 7 天
     */
    public static final int INT_WEEK_EXPIRE = 7*24*60*60;

    /**
     * INT_MONTH_EXPIRE 30天
     */
    public static final int INT_MONTH_EXPIRE = 30*24*60*60;
	
	
	//------------------------ Redis常量信息 End  -----------------------------

    /**
     * redis list mysql的数据存入到es失败的 异常list
     */
    public static final String REDIS_MYSQL_VLPR_TO_ES_ERROR_LIST_KEY = "mysql_vlpr_2_es_error:";
    public static final String REDIS_MYSQL_OBJEXT_TO_ES_ERROR_LIST_KEY = "mysql_objext_2_es_error:";


    /**
     * redis 批量插入es 时候出现异常的id
     */
    public static final String REDIS_ES_TO_ES_ERROR_LIST_KEY = "es_to_es_error:";

    /**
     * REDIS_FEATURE_NET_ERROR_LIST_KEY
     */
    public static final String REDIS_FEATURE_NET_ERROR_LIST_KEY = "feature_net_error:";


    /**
     * 响应出的数据异常
     */
    public static final String REDIS_FEATURE_CODE_ERROR_LIST_KEY = "feature_code_error:";

    /**
     * mysql进入处理数据的总量
     */
    public static final String REDIS_MYSQL_INTO_QUEEN_TODEAL_COUNT = "mysql_to_queen_total_count:";

    /**
     * 数据统计  - 每次入库多少
     */
    public static final String REDIS_PUSH_TO_KAFKA_SUCCESS_COUNT_KEY = "push_to_kafka_success_count:";
    public static final String REDIS_PUSH_TO_KAFKA_FAIL_COUNT_KEY = "push_to_kafka_fail_count:";

    /**
     * serialNumber k,v
     */
    public static final String REDIS_SERIAL_NUMBER_MAP = "serial_number_map";


}
