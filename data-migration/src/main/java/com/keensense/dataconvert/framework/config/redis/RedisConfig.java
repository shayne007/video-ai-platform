package com.keensense.dataconvert.framework.config.redis;

import com.keensense.dataconvert.biz.common.cache.redis.RedisLock;
import com.keensense.dataconvert.biz.common.cache.redis.impl.RedisServiceImpl;
import com.keensense.dataconvert.biz.common.consts.ConfigPathConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @ClassName：RedisConfig
 * @Description： <p> RedisConfig  - 做异常队列 - 定时处理出现异常的数据 </p>
 * @Author： - Jason
 * @CreatTime：2019/7/24 - 15:39
 * @Modify By：
 * @ModifyTime： 2019/7/24
 * @Modify marker：
 * @version V1.0
*/
@Configuration
@PropertySources(value = {@PropertySource(ConfigPathConstant.CONFIG_REDIS_PATH)})
public class RedisConfig {

    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    /**
     * 主机地址
     */
    @Value("${redis.hostName}")
    private String hostName;

    /**
     * 端口
     */
    @Value("${redis.port}")
    private Integer port;

    /**
     * 密码没有不填写
     */
    @Value("${redis.password}")
    private String password;

    /**
     * redis 索引 默认0
     */
    @Value("${redis.index}")
    private int index;


    /**
     * 最大空闲数
     */
    @Value("${redis.maxIdle}")
    private Integer maxIdle;

    /**
     * 最小空闲数
     */
    @Value("${redis.minIdle}")
    private Integer minIdle;

    /**
     * 控制一个pool可分配多少个jedis实例
     */
    @Value("${redis.maxTotal}")
    private Integer maxTotal;

    /**
     * 最大建立连接等待时间
     */
    @Value("${redis.maxWaitMillis}")
    private Integer maxWaitMillis;

    /**
     * 读取超时时间
     */
    @Value("${redis.readTimeOut}")
    private Integer readTimeOut;

    /**
     * 连接的最小空闲时间
     */
    @Value("${redis.minEvictableIdleTimeMillis}")
    private Integer minEvictableIdleTimeMillis;

    /**
     * 每次释放连接的最大数目
     */
    @Value("${redis.numTestsPerEvictionRun}")
    private Integer numTestsPerEvictionRun;

    /**
     * 逐出扫描的时间间隔(毫秒)
     */
    @Value("${redis.timeBetweenEvictionRunsMillis}")
    private long timeBetweenEvictionRunsMillis;

    /**
     * 是否在从池中取出连接前进行检验
     */
    @Value("${redis.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${redis.testOnReturn}")
    private boolean testOnReturn;

    /**
     * 在空闲时检查有效性-是否开启
     */
    @Value("${redis.testWhileIdle}")
    private boolean testWhileIdle;


    /**
     * 集群配置节点
     */
    @Value("${spring.redis.cluster.nodes}")
    private String clusterNodes;

    /**
     * mmaxRedirectsac
     */
    @Value("${spring.redis.cluster.max-redirects}")
    private Integer mmaxRedirectsac;


    @Bean(name="jedisPoolConfig")
    public JedisPoolConfig bulidJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //1.控制一个pool可分配多少个jedis实例
        jedisPoolConfig.setMaxTotal(maxTotal);
        //2.控制一个pool最多有多少个状态为idle(空闲)的jedis实例
        jedisPoolConfig.setMaxIdle(maxIdle);
        //3.控制一个pool最少有多少个状态为idle(空闲)的jedis实例
        jedisPoolConfig.setMinIdle(minIdle);
        //4.表示当borrow(引入)一个jedis实例时,最大等待毫秒数,如果超过等待时间,则直接抛出JedisConnectionException 小于零:阻塞不确定的时间,  默认-1
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        //5.是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        //6.在return给pool时,是否检查连接可用性(ping())
        jedisPoolConfig.setTestOnReturn(testOnReturn);
        //7.如果为true,表示有一个idle object evitor线程对idle object进行扫描,如果validate失败,此object会被从pool中drop掉;这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
        jedisPoolConfig.setTestWhileIdle(testWhileIdle);
        //8.每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        jedisPoolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        //9.逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        //10.表示一个对象至少停留在idle状态的最短时间,然后才能被idle object evitor扫描并驱逐;这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义;默认值60000(60秒)
        jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        return jedisPoolConfig;
    }


    @Bean(name="redisPool")
    public JedisPool bulidJedisPool() {
        //config hostName port 超时 password index clientName
        JedisPool jedisPool = new JedisPool(bulidJedisPoolConfig(),hostName,port,readTimeOut,password,index,null);
        return jedisPool;
    }



    /**
     * @Description: redisService
     * @return RedisServiceImpl  - 直接注入使用就行
     * @Autor: Jason
     */
    @Bean(name="redisService")
    public RedisServiceImpl buildRedisServiceImpl(JedisPool redisPool){
        RedisServiceImpl redisService = new RedisServiceImpl();
        redisService.setRedisPool(redisPool);
        logger.info(">>> [初始化]Single redis service success ! >>>");
        return redisService;
    }


    /**
     * @Description: Redis单机锁Bean
     * @param redisPool
     * @return RedisLock
     * @Autor: Jason
     */
    @Bean(name="redisLock")
    public RedisLock buildRedisLock(JedisPool redisPool){
        RedisLock  redisLock = new RedisLock(redisPool);
        return redisLock;
    }

}
