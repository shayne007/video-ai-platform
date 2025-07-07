package com.keensense.search.repository.origin;

import com.keensense.search.repository.AlarmRepository;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhanx xiaohui on 2019-05-09.
 */
@Repository("originAlarmRepository")
@RefreshScope
public class OriginAlarmRepository implements AlarmRepository {
    @Resource(name = "redisTemplate")
    private RedisTemplate<String, String> redisTemplate;
    @Resource(name = "trafficRedisTemplate")
    private RedisTemplate<String, String> trafficRedisTemplate;

    @Override
    public void insert(String key, String jsonString) {
        redisTemplate.opsForValue().set(key, jsonString, 60, TimeUnit.SECONDS);
    }

    @Override
    public void trafficInsert(String key, String jsonString) {
        trafficRedisTemplate.opsForValue().set(key, jsonString, 60, TimeUnit.SECONDS);
    }


}
