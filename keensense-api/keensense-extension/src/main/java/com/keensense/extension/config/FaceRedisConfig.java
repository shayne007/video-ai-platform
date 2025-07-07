package com.keensense.extension.config;/**
 * Created by zhanx xiaohui on 2019/8/26.
 */

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/8/26 14:59
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Configuration
@RefreshScope
@Data
public class FaceRedisConfig extends RedisConfig {

    @Value("${spring.redis2.database}")
    private int dbIndex;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis2.port}")
    private int port;

    @Value("${spring.redis2.password}")
    private String password;

    @Value("${spring.redis2.timeout}")
    private int timeout;

    /**
     * 配置redis连接工厂
     *
     * @return
     */
    @Bean
    public RedisConnectionFactory faceRedisConnectionFactory() {
        return createJedisConnectionFactory(dbIndex, host, port, password, timeout);
    }

    /**
     * 配置redisTemplate 注入方式使用@Resource(name="") 方式注入
     *
     * @return
     */
    @Bean(name = "faceRedisTemplate")
    public RedisTemplate faceRedisTemplate() {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(faceRedisConnectionFactory());
        setSerializer(template);
        template.afterPropertiesSet();
        return template;
    }
}

