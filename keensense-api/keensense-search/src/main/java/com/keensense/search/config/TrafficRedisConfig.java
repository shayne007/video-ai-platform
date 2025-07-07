package com.keensense.search.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by zhanx xiaohui on 2019-08-29.
 */
@Configuration
@RefreshScope
@Data
public class TrafficRedisConfig {
    @Value("${spring.redis.host}")
    private String redisIp;
    @Value("${spring.redis.port}")
    private int redisPort;
    @Value("${spring.redis.password}")
    private String redisPassword;
    @Value("${spring.redis.pool.max-active}")
    private int redisPoolMaxActive;
    @Value("${spring.redis.pool.max-wait}")
    private int redisPoolMaxWait;
    @Value("${spring.redis.pool.max-idle}")
    private int redisPoolMaxIdle;
    @Value("${spring.redis.pool.min-idle}")
    private int redisPoolMinIdle;
    @Value("${spring.redis.database}")
    private int dbIndex;
    @Value("${spring.redis.timeout}")
    private int timeout;

    @Bean(name = "trafficRedisTemplate")
    public RedisTemplate trafficRedisTemplate() {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(createJedisConnectionFactory(dbIndex));
        setSerializer(template);
        template.afterPropertiesSet();
        return template;
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate redisTemplate() {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(createJedisConnectionFactory(0));
        setSerializer(template);
        template.afterPropertiesSet();
        return template;
    }

    public JedisConnectionFactory createJedisConnectionFactory(int dbIndex) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setDatabase(dbIndex);
        jedisConnectionFactory.setHostName(redisIp);
        jedisConnectionFactory.setPort(redisPort);
        jedisConnectionFactory.setPassword("");
        if (!StringUtils.isEmpty(redisPassword)) {
            jedisConnectionFactory.setPassword(redisPassword);
        }
        jedisConnectionFactory.setTimeout(timeout);
        jedisConnectionFactory.setPoolConfig(setPoolConfig(redisPoolMaxIdle, redisPoolMinIdle, redisPoolMaxActive, redisPoolMaxWait, true));
        return jedisConnectionFactory;
    }

    /**
     * 设置连接池属性
     *
     * @param maxIdle
     * @param minIdle
     * @param maxActive
     * @param maxWait
     * @param testOnBorrow
     * @return
     */
    public JedisPoolConfig setPoolConfig(int maxIdle, int minIdle, int maxActive, int maxWait, boolean testOnBorrow) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxWaitMillis(maxWait);
        poolConfig.setTestOnBorrow(testOnBorrow);
        return poolConfig;
    }

    /**
     * 设置RedisTemplate的序列化方式
     *
     * @param redisTemplate
     */
    public void setSerializer(RedisTemplate redisTemplate) {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        //设置键（key）的序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置值（value）的序列化方式
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
    }
}
