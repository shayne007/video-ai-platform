package com.keensense.dataconvert.biz.common.cache.redis.impl;

import com.alibaba.fastjson.JSON;
import com.keensense.dataconvert.biz.common.cache.redis.RedisConstant;
import com.keensense.dataconvert.biz.common.cache.redis.RedisService;
import com.keensense.dataconvert.framework.common.exception.SystemException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName：RedisServiceImpl
 * @Description： <p>  redis实现 </p>
 * @Author： - Jason
 * @CreatTime：2019/7/24 - 15:47
 * @Modify By：
 * @ModifyTime： 2019/7/24
 * @Modify marker：
 * @version V1.0
*/
public class RedisServiceImpl implements RedisService {
	
	private static Logger logger = LoggerFactory.getLogger(RedisService.class);
	
	protected JedisPool redisPool;

	/**
	 * 获取jedis池
	 */
	@Override
    public JedisPool getRedisPool() {
		return redisPool;
	}

	/**
	 * 注入redisPool
	 */
	public void setRedisPool(final JedisPool redisPool) {
		this.redisPool = redisPool;
	}

	/**
	 * @Description: getRedisClient 获取jedis 客户端.
	 * @return
	 * @throws SystemException Jedis
	 * @Autor: Jason
	 */
	public Jedis getRedisClient() throws SystemException {
		boolean isContinue = true;
		Jedis jedis = null;
		int count = 3;
		try {
			do {
				try {
					jedis = redisPool.getResource();
					isContinue = false;
				} catch (Exception e) {
					isContinue = true;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					count++;
				}
				if (count > 3) {
					break;
				}
			} while (isContinue);
		} catch (Exception e) {
			logger.error("redis server connect exception-连接失败!", e);
		}
		return jedis;
	}

	@SuppressWarnings("deprecation")
	public void returnResource(Jedis jedis) {
		redisPool.returnResource(jedis);
	}

	@SuppressWarnings("deprecation")
	public void returnResource(Jedis jedis, boolean broken) {
		if (broken) {
			redisPool.returnBrokenResource(jedis);
		} else {
			redisPool.returnResource(jedis);
		}
	}

	@Override
	public Boolean set(String key, String value) {
		Boolean result = false;

		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			jedis.set(key, value);
			result = true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
    public Boolean set(String key, Object value, int expire) throws SystemException {
        Boolean result = false;

        Jedis jedis = getRedisClient();
        if (jedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            String valueJson = JSON.toJSONString(value);
            jedis.set(key, valueJson);
            jedis.expire(key,expire);
            result = true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            returnResource(jedis, broken);
        }
        return result;
    }

    @Override
	public Boolean set(String key, Object value) {
		Boolean result = false;

		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		System.out.println("key : " + key);
		boolean broken = false;
		try {
			String valueJson = JSON.toJSONString(value);
			jedis.set(key, valueJson);
			result = true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	/**
	 * 得到List对象数据
	 * 
	 * @param key
	 * @param clazz
	 *            list中元素的Class
	 * @return
	 */
    @Override
	public <T> List<T> getList(String key, Class<T> clazz) {
		List<T> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			String value = jedis.get(key);
			result = JSON.parseArray(value, clazz);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	/**
	 * @param key
	 * @param clazz
	 * @return
	 */
	@Override
	public <T> T get(String key, Class<T> clazz) {
		T result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			String value = jedis.get(key);
			result = JSON.parseObject(value, clazz);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	@Override
	public String get(String key) {
		String result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}

		boolean broken = false;
		try {
			result = jedis.get(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	/** 
	 * 删除
	 * @param keys
	 * @return
	 */
    @Override
	public Boolean del(String... keys) {
		Boolean result = false;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return false;
		}
		boolean broken = false;
		try {
			jedis.del(keys);
			result = true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    /**
     * 删除
     * @param key
     * @return
     */
    @Override
	public Boolean del(String key) {
		Boolean result = false;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return false;
		}
		boolean broken = false;
		try {
			jedis.del(key);
			result = true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	/**
	 * 判断key键是否存在
	 * @param key
	 * @return
	 */
    @Override
	public Boolean exists(String key) {
		Boolean result = false;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.exists(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	@Override
    public String type(String key) {
		String result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.type(key);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    /**
     * 在某段时间后失效
     * @param key
     * @param seconds
     * @return
     */
    @Override
	public Long expire(String key, int seconds) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.expire(key, seconds);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	/**
	 * 在某个时间点失效
	 * @param key
	 * @param unixTime
	 * @return
	 */
    @Override
	public Long expireAt(String key, long unixTime) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.expireAt(key, unixTime);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long ttl(String key) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.ttl(key);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public boolean setbit(String key, long offset, boolean value) {
		Jedis jedis = getRedisClient();
		boolean result = false;
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.setbit(key, offset, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public boolean getbit(String key, long offset) {
		Jedis jedis = getRedisClient();
		boolean result = false;
		if (jedis == null) {
			return result;
		}
		boolean broken = false;

		try {
			result = jedis.getbit(key, offset);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long setrange(String key, long offset, String value) {
		Jedis jedis = getRedisClient();
		long result = 0;
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.setrange(key, offset, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public String getrange(String key, long startOffset, long endOffset) {
		Jedis jedis = getRedisClient();
		String result = null;
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.getrange(key, startOffset, endOffset);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public String getSet(String key, String value) {
		String result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.getSet(key, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long setnx(String key, String value) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.setnx(key, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public String setex(String key, int seconds, String value) {
		String result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.setex(key, seconds, value);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long decrBy(String key, long integer) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.decrBy(key, integer);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long decr(String key) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.decr(key);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long incrBy(String key, long integer) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.incrBy(key, integer);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long incr(String key) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.incr(key);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long append(String key, String value) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.append(key, value);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public String substr(String key, int start, int end) {
		String result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.substr(key, start, end);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long hset(String key, String field, String value) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hset(key, field, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public String hget(String key, String field) {
		String result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hget(key, field);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long hsetnx(String key, String field, String value) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hsetnx(key, field, value);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public String hmset(String key, Map<String, String> hash) {
		String result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hmset(key, hash);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public List<String> hmget(String key, String... fields) {
		List<String> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hmget(key, fields);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long hincrBy(String key, String field, long value) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hincrBy(key, field, value);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Boolean hexists(String key, String field) {
		Boolean result = false;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hexists(key, field);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long hdel(String key, String... field) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hdel(key, field);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long hlen(String key) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hlen(key);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<String> hkeys(String key) {
		Set<String> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hkeys(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public List<String> hvals(String key) {
		List<String> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hvals(key);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Map<String, String> hgetAll(String key) {
		Map<String, String> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hgetAll(key);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	// ================list ====== l表示 list或 left, r表示right====================
    @Override
	public Long rpush(String key, String... vals) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.rpush(key, vals);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long lpush(String key, String string) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.lpush(key, string);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long llen(String key) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.llen(key);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public List<String> lrange(String key, long start, long end) {
		List<String> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.lrange(key, start, end);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public String ltrim(String key, long start, long end) {
		String result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.ltrim(key, start, end);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public String lindex(String key, long index) {
		String result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.lindex(key, index);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public String lset(String key, long index, String value) {
		String result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.lset(key, index, value);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long lrem(String key, long count, String value) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.lrem(key, count, value);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public String lpop(String key) {
		String result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.lpop(key);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public String rpop(String key) {
		String result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.rpop(key);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	// return 1 add a not exist value ,
	// return 0 add a exist value
    ///** 锁的超时时间（秒），过期删除 */
    //
    @Override
	public Long sadd(String key, String member,int expire) {

		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.sadd(key, member);
			jedis.expire(key, expire);//EXPIRE 默认3分钟.
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<String> smembers(String key) {
		Set<String> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.smembers(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long srem(String key, String member) {
		Jedis jedis = getRedisClient();

		Long result = null;
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.srem(key, member);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public String spop(String key) {
		Jedis jedis = getRedisClient();
		String result = null;
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.spop(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	@Override
	public Long scard(String key) {
		Jedis jedis = getRedisClient();
		Long result = null;
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.scard(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Boolean sismember(String key, String member) {
		Jedis jedis = getRedisClient();
		Boolean result = null;
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.sismember(key, member);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public String srandmember(String key) {
		Jedis jedis = getRedisClient();
		String result = null;
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.srandmember(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long zadd(String key, double score, String member) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zadd(key, score, member);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<String> zrange(String key, int start, int end) {
		Set<String> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrange(key, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long zrem(String key, String member) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrem(key, member);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Double zincrby(String key, double score, String member) {
		Double result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zincrby(key, score, member);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long zrank(String key, String member) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrank(key, member);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long zrevrank(String key, String member) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrevrank(key, member);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<String> zrevrange(String key, int start, int end) {
		Set<String> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrevrange(key, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<Tuple> zrangeWithScores(String key, int start, int end) {
		Set<Tuple> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrangeWithScores(key, start, end);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<Tuple> zrevrangeWithScores(String key, int start, int end) {
		Set<Tuple> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrevrangeWithScores(key, start, end);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long zcard(String key) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zcard(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Double zscore(String key, String member) {
		Double result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zscore(key, member);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public List<String> sort(String key) {
		List<String> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.sort(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public List<String> sort(String key, SortingParams sortingParameters) {
		List<String> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.sort(key, sortingParameters);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long zcount(String key, double min, double max) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zcount(key, min, max);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<String> zrangeByScore(String key, double min, double max) {
		Set<String> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrangeByScore(key, min, max);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<String> zrevrangeByScore(String key, double max, double min) {
		Set<String> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrevrangeByScore(key, max, min);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
		Set<String> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrangeByScore(key, min, max, offset, count);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
		Set<String> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrevrangeByScore(key, max, min, offset, count);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
		Set<Tuple> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrangeByScoreWithScores(key, min, max);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
		Set<Tuple> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrevrangeByScoreWithScores(key, max, min);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
		Set<Tuple> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrangeByScoreWithScores(key, min, max, offset, count);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	@Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
		Set<Tuple> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	@Override
    public Long zremrangeByRank(String key, int start, int end) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zremrangeByRank(key, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	@Override
    public Long zremrangeByScore(String key, double start, double end) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zremrangeByScore(key, start, end);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	@Override
    public Long linsert(String key, LIST_POSITION where, String pivot, String value) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.linsert(key, where, pivot, value);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public String set(byte[] key, byte[] value) {
		String result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.set(key, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public byte[] get(byte[] key) {
		byte[] result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.get(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Boolean exists(byte[] key) {
		Boolean result = false;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.exists(key);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public String type(byte[] key) {
		String result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.type(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long expire(byte[] key, int seconds) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.expire(key, seconds);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long expireAt(byte[] key, long unixTime) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.expireAt(key, unixTime);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long ttl(byte[] key) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.ttl(key);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public byte[] getSet(byte[] key, byte[] value) {
		byte[] result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.getSet(key, value);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long setnx(byte[] key, byte[] value) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.setnx(key, value);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public String setex(byte[] key, int seconds, byte[] value) {
		String result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.setex(key, seconds, value);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long decrBy(byte[] key, long integer) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.decrBy(key, integer);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long decr(byte[] key) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.decr(key);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long incrBy(byte[] key, long integer) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.incrBy(key, integer);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long incr(byte[] key) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.incr(key);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long append(byte[] key, byte[] value) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.append(key, value);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public byte[] substr(byte[] key, int start, int end) {
		byte[] result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.substr(key, start, end);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long hset(byte[] key, byte[] field, byte[] value) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.hset(key, field, value);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public byte[] hget(byte[] key, byte[] field) {
		byte[] result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.hget(key, field);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long hsetnx(byte[] key, byte[] field, byte[] value) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.hsetnx(key, field, value);

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public String hmset(byte[] key, Map<byte[], byte[]> hash) {
		String result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.hmset(key, hash);

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public List<byte[]> hmget(byte[] key, byte[]... fields) {
		List<byte[]> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hmget(key, fields);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long hincrBy(byte[] key, byte[] field, long value) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hincrBy(key, field, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Boolean hexists(byte[] key, byte[] field) {
		Boolean result = false;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hexists(key, field);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long hdel(byte[] key, byte[] field) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hdel(key, field);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long hlen(byte[] key) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hlen(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<byte[]> hkeys(byte[] key) {
		Set<byte[]> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hkeys(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Collection<byte[]> hvals(byte[] key) {
		Collection<byte[]> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.hvals(key);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Map<byte[], byte[]> hgetAll(byte[] key) {
		Map<byte[], byte[]> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hgetAll(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long rpush(byte[] key, byte[] string) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.rpush(key, string);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long lpush(byte[] key, byte[] string) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.lpush(key, string);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long llen(byte[] key) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.llen(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public List<byte[]> lrange(byte[] key, int start, int end) {
		List<byte[]> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.lrange(key, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public String ltrim(byte[] key, int start, int end) {
		String result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.ltrim(key, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public byte[] lindex(byte[] key, int index) {
		byte[] result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.lindex(key, index);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	/**
	 * 右边set
	 */
    @Override
	public String lset(byte[] key, int index, byte[] value) {
		String result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.lset(key, index, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long lrem(byte[] key, int count, byte[] value) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.lrem(key, count, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	/**
	 * 左边弹出
	 */
    @Override
	public byte[] lpop(byte[] key) {
		byte[] result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.lpop(key);

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	/**
	 * rpop 右边弹出pop
	 */
    @Override
	public byte[] rpop(byte[] key) {
		byte[] result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.rpop(key);

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long sadd(byte[] key, byte[] member) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.sadd(key, member);

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<byte[]> smembers(byte[] key) {
		Set<byte[]> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.smembers(key);

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long srem(byte[] key, byte[] member) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.srem(key, member);

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public byte[] spop(byte[] key) {
		byte[] result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.spop(key);

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long scard(byte[] key) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.scard(key);

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Boolean sismember(byte[] key, byte[] member) {
		Boolean result = false;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.sismember(key, member);

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public byte[] srandmember(byte[] key) {
		byte[] result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.srandmember(key);

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long zadd(byte[] key, double score, byte[] member) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zadd(key, score, member);

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<byte[]> zrange(byte[] key, int start, int end) {
		Set<byte[]> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrange(key, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long zrem(byte[] key, byte[] member) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrem(key, member);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Double zincrby(byte[] key, double score, byte[] member) {
		Double result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zincrby(key, score, member);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long zrank(byte[] key, byte[] member) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrank(key, member);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long zrevrank(byte[] key, byte[] member) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrevrank(key, member);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<byte[]> zrevrange(byte[] key, int start, int end) {
		Set<byte[]> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrevrange(key, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<Tuple> zrangeWithScores(byte[] key, int start, int end) {
		Set<Tuple> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrangeWithScores(key, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<Tuple> zrevrangeWithScores(byte[] key, int start, int end) {
		Set<Tuple> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrevrangeWithScores(key, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long zcard(byte[] key) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zcard(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Double zscore(byte[] key, byte[] member) {
		Double result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zscore(key, member);

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public List<byte[]> sort(byte[] key) {
		List<byte[]> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.sort(key);

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
		List<byte[]> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.sort(key, sortingParameters);

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long zcount(byte[] key, double min, double max) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zcount(key, min, max);

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
		Set<byte[]> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrangeByScore(key, min, max);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
		Set<byte[]> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrangeByScore(key, min, max, offset, count);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
		Set<Tuple> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrangeByScoreWithScores(key, min, max);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
		Set<Tuple> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrangeByScoreWithScores(key, min, max, offset, count);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
		Set<byte[]> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrevrangeByScore(key, max, min);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
		Set<byte[]> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrevrangeByScore(key, max, min, offset, count);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
		Set<Tuple> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrevrangeByScoreWithScores(key, max, min);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count) {
		Set<Tuple> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long zremrangeByRank(byte[] key, int start, int end) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zremrangeByRank(key, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long zremrangeByScore(byte[] key, double start, double end) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zremrangeByScore(key, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Long linsert(byte[] key, LIST_POSITION where, byte[] pivot, byte[] value) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.linsert(key, where, pivot, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public Boolean pipelineLDatas(Map<String, List<String>> datas) {
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return false;
		}
		boolean broken = false;
		try {
			jedis.pipelined();

			for (String dataKey : datas.keySet()) {
				List<String> data = datas.get(dataKey);
				String[] array = new String[data.size()];
				jedis.rpush(dataKey, data.toArray(array));
			}
			jedis.sync();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return true;
	}

	/**
	 * 批量把数据写入redis缓存服务器
	 * 
	 * @param datas
	 * @return
	 */
    @Override
	public Boolean pipelineDatas(Map<String, Map<String, String>> datas) {
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return false;
		}
		boolean broken = false;
		try {
			jedis.pipelined();
			for (String dataKey : datas.keySet()) {
				if (datas.get(dataKey) != null) {
					jedis.hmset(dataKey, datas.get(dataKey));
				}
			}

			jedis.sync();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return true;
	}

	@Override
	public <T> List<T> pipelinedHash(String cacheKey, Map<String, List<T>> dataMap) {
		Jedis jedis = getRedisClient();
		List<T> result = null;
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			String hashValue = null;
			jedis.pipelined();
			for (String key : dataMap.keySet()) {
				hashValue = JSON.toJSONString(dataMap.get(key));
				jedis.hset(cacheKey, key, hashValue);
			}
			jedis.sync();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

    @Override
	public List<Object> pipelined(Map<String, String> setDataMap, List<String> getDataKeys) {
		Jedis jedis = getRedisClient();
		List<Object> result = null;
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			jedis.pipelined();
			for (String key : setDataMap.keySet()) {
				jedis.set(key, setDataMap.get(key));
			}
			if (getDataKeys != null && getDataKeys.size() > 0) {
				result = new ArrayList<Object>();
				for (String key : getDataKeys) {
					Object obj = jedis.get(key);
					result.add(obj);
				}
			}
			jedis.sync();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	@Override
	public Long persist(String key) {
		Long result = 0L;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.persist(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	/**
	 * 删除keys对应的记录,可以是多个key
	 * 
	 * @param String
	 *            ... keys
	 * @return 删除的记录数
	 * */
    @Override
	public Long del(byte[]... keys) {
		Long result = 0L;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.del(keys);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	@Override
	public Set<String> keys(String pattern) {
		Set<String> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.keys(pattern);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	@Override
	public Set<String> sinter(String... keys) {
		Set<String> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.sinter(keys);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	@Override
	public Long sinterstore(String newkey, String... keys) {
		Long result = 0L;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.sinterstore(newkey, keys);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	@Override
	public Long smove(String srckey, String dstkey, String member) {
		Long result = 0L;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.smove(srckey, dstkey, member);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	@Override
	public List<String> mget(String... keys) {
		List<String> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.mget(keys);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	@Override
	public String mset(String... keysvalues) {
		String result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.mset(keysvalues);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	@Override
	public Long strlen(String key) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.strlen(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	@Override
	public Set<String> sunion(String... keys) {
		Set<String> result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.sunion(keys);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	@Override
	public Long sunionstore(String newkey, String... keys) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.sunionstore(newkey, keys);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	@Override
	public Long zrem(String key) {
		Long result = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrem(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;

		} finally {
			returnResource(jedis, broken);
		}
		return result;
	}

	/**
	 * hash get
	 */
	@Override
	public <T> T hget(String key, String field, Class<T> clazz) {
		T t = null;
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return null;
		}
		boolean broken = false;
		try {
			String json = jedis.hget(key, field);
			if (StringUtils.isNotBlank(json) && !RedisConstant.CACHE_KEY_NOT_EXIST.equalsIgnoreCase(json)) {
				t = JSON.parseObject(json, clazz);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			returnResource(jedis, broken);
		}
		return t;
	}

	@Override
	public <T> List<T> pipelineGetHDatas(Collection<String> keys, String field, Class<T> valueType) {
		List<T> dataList = new ArrayList<T>();
		Jedis jedis = getRedisClient();
		if (jedis == null) {
			return null;
		}
		boolean broken = false;
		try {
			jedis.pipelined();
			for (String key : keys) {
				String valString = jedis.hget(key, field);
				if (StringUtils.isNotBlank(valString) && !RedisConstant.CACHE_KEY_NOT_EXIST.equalsIgnoreCase(valString)) {
					dataList.addAll(JSON.parseArray(valString, valueType));
				}
			}

			jedis.sync();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			broken = true;
		} finally {
			returnResource(jedis, broken);
		}
		return dataList;
	}
}
