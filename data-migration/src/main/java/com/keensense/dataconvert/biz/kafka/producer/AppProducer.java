package com.keensense.dataconvert.biz.kafka.producer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.keensense.dataconvert.biz.common.cache.redis.RedisConstant;
import com.keensense.dataconvert.biz.common.cache.redis.RedisLock;
import com.keensense.dataconvert.biz.common.cache.redis.RedisService;
import com.keensense.dataconvert.biz.common.consts.CommonConst;
import com.keensense.dataconvert.framework.common.utils.date.DateHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.kafka.producer
 * @Description： <p> AppProducer - 分析完数据后 生产到指定的kafka </p>
 * @Author： - Jason
 * @CreatTime：2019/7/23 - 16:21
 * @Modify By：
 * @ModifyTime： 2019/7/23
 * @Modify marker：
 */
@Component
public class AppProducer {

    private static Logger logger = LoggerFactory.getLogger(AppProducer.class);

    @Resource
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private RedisService redisService;


    @Resource
    protected RedisLock redisLock;

    /**
     * 推送数据到指定的kafka
     * 注意:[redis incrBy redis内部是单线程的 故线程是安全的]
     * issue : redis 锁住同一个数据? 不符合业务逻辑 kafka出现重复的原因是?
     */
    public synchronized void sendConVertDataToKafkaLock(String jsonStr, Date creatDate){
        if (StringUtils.isEmpty(jsonStr)){
            logger.error("=== sendConVertDataToKafka:NoData ===");
            return;
        }
        if (creatDate == null){
            creatDate = new Date();
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        String countKey = jsonStr;
        String ymdStr = DateHelper.dateStrYMD(creatDate);
        try {
            boolean isLock;
            do{
                // uuid 做redis 锁的 key
                isLock = redisLock.singleLock(jsonObject.getString("uuid"));
            }while(!isLock);
            kafkaTemplate.send(CommonConst.RECOG_KAKFA_TOPIC,jsonStr);
            redisService.incrBy(RedisConstant.REDIS_PUSH_TO_KAFKA_SUCCESS_COUNT_KEY.concat(ymdStr),1L);
        } catch (Exception e) {
            redisService.incrBy(RedisConstant.REDIS_PUSH_TO_KAFKA_FAIL_COUNT_KEY.concat(ymdStr),1L);
            logger.info("=== sendConVertDataToKafka:error:{} ===",e);
        }finally {
            redisLock.singleUnlock(countKey);
        }
    }


    /**
     * issue:数据出现重复,少量偶现 ? Mysql-数据库里面数据问题?
     *      SELECT
     * 			recog_id
     * 		FROM
     * 			`objext_result20190728`
     * 		GROUP BY
     * 			recog_id
     * 		HAVING
     * 			COUNT(*) > 1
     * @param jsonStr      需要存入的数据
     * @param creatDate    创建的日期
     */
    public synchronized void sendConVertDataToKafka(String jsonStr, Date creatDate){
        if (StringUtils.isEmpty(jsonStr)){
            logger.error("=== SendConVertDataToKafka:Data is empty ===");
            return;
        }
        if (creatDate == null){
            creatDate = new Date();
        }
        String ymdStr = DateHelper.dateStrYMD(creatDate);
        try {
            kafkaTemplate.send(CommonConst.RECOG_KAKFA_TOPIC,jsonStr);
            redisService.incrBy(RedisConstant.REDIS_PUSH_TO_KAFKA_SUCCESS_COUNT_KEY.concat(ymdStr),1L);
        } catch (Exception e) {
            redisService.incrBy(RedisConstant.REDIS_PUSH_TO_KAFKA_FAIL_COUNT_KEY.concat(ymdStr),1L);
            logger.info("=== sendConVertDataToKafka:error:{} ===",e);
        }
    }

    /**
     * 推送到 kafka
     * @param topic
     * @param value
     */
    public void sendByTopicValue(String topic,Object value){
        try {
            logger.info("=== topic:{},data:\n{}",topic, value);
            kafkaTemplate.send(topic, JSON.toJSON(value));
        } catch (Exception e) {
            logger.info("=== sendConVertDataToKafka:error:{} ===",e);
        }
    }
}
