package com.keensense.archive.utils;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Created by memory_fu on 2019/12/26.
 */
@Slf4j
@Service
public class RedisUtil {
    
    private static ShardedJedisPool jedisPool = null;
    
    @Value("${spring.redis.host}")
    private String host;
    @Value("${archive.redis.port}")
    private String port;
    @Value("${archive.redis.password}")
    private String password;
    @Value("${archive.redis.database}")
    private String database;
    
    @PostConstruct
    private void init(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    
        // 设置最大对象数
        jedisPoolConfig.setMaxTotal(20);
    
        // 最大能够保持空闲状态的对象数
        jedisPoolConfig.setMaxIdle(10);
    
        // 超时时间
        jedisPoolConfig.setMaxWaitMillis(10000);
    
        // 在获取连接的时候检查有效性, 默认false
        jedisPoolConfig.setTestOnBorrow(true);
    
        // 在返回Object时, 对返回的connection进行validateObject校验
        jedisPoolConfig.setTestOnReturn(true);
    
        // 如果是集群，可以全部加入list中
        List<JedisShardInfo> shardInfos = new ArrayList<JedisShardInfo>();
        String redisUrl = "reids://:"+password+"@"+host+":"+port+"/"+database;
//        JedisShardInfo shardInfo = new JedisShardInfo(host, port);
        JedisShardInfo shardInfo = new JedisShardInfo(redisUrl);
        shardInfo.setPassword(password);
        shardInfos.add(shardInfo);
        jedisPool = new ShardedJedisPool(jedisPoolConfig, shardInfos);
    }
    
    /**
     * 从连接池中获取一个ShardedJedis对象
     */
    private static ShardedJedis getJedis() {
        return jedisPool.getResource();
    }
    
    /**
     * 根据key获取value
     * @param key
     * @return
     */
    public static String getValueByKey(String key) {
        ShardedJedis jedis = getJedis();
        String value = null;
        try {
            value = jedis.get(key);
        }catch (Exception e){
            log.error("==== RedisUtil.getValueByKey() Exception",e);
        }finally {
            jedis.close();
        }
        return value;
    }
    
    /**
     * 设置redis数据
     * @param key
     * @param value
     * @return
     */
    public static String addValue(String key, String value) {
        ShardedJedis jedis = getJedis();
        String setFlag = null;
        try {
            setFlag = jedis.set(key, value);
        }catch (Exception e){
            log.error("==== RedisUtil.addValue() Exception",e);
        }finally {
            jedis.close();
        }
        return setFlag;
    }
    
}
