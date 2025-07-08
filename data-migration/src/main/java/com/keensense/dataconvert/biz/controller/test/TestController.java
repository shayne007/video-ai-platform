package com.keensense.dataconvert.biz.controller.test;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.keensense.dataconvert.api.request.queue.DownloadImageQueue;
import com.keensense.dataconvert.api.util.BuildEsIndexUtil;
import com.keensense.dataconvert.biz.common.cache.redis.RedisConstant;
import com.keensense.dataconvert.biz.common.cache.redis.RedisService;
import com.keensense.dataconvert.biz.common.consts.ConfigPathConstant;
import com.keensense.dataconvert.biz.entity.PictureInfo;
import com.keensense.dataconvert.biz.kafka.producer.AppProducer;
import com.keensense.dataconvert.biz.service.ElasticSearchService;
import com.keensense.dataconvert.framework.common.base.vo.RespBody;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.action.get.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.controller.test
 * @Description： <p> TestController - 测试 just for 开发  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/23 - 14:28
 * @Modify By：
 * @ModifyTime： 2019/7/23
 * @Modify marker：
 */
@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    String index = "vlpr_result";

    String type = "data";

    @Qualifier("sourceEsService")
    @Autowired
    private ElasticSearchService sourceEsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private AppProducer appProducer;

    /**
     * getSourceById
     * @param id
     */
    @ApiOperation("根据id查询源es库数据")
    @ResponseBody
    @RequestMapping(value="/getSourceById/{id}",method= RequestMethod.GET)
    public RespBody getSourceById(@PathVariable String id){
        RespBody respBody = new RespBody();
        Assert.notNull(id);
        try {
            GetResponse response = sourceEsService.getDocument(index, type, id);
            respBody.addOK(response,"sourceElasticSearchClient");
        } catch (Exception e) {
            logger.error(e.getMessage());
            respBody.addError(e.getMessage());
        }
        return respBody;
    }

    @ResponseBody
    @ApiOperation("测试多线程任务处理")
    @RequestMapping(value = "/testTask", method = RequestMethod.GET)
    public RespBody testTask() {
        RespBody respBody = new RespBody();
        logger.info("testTask");
        return  respBody;
    }


    /**
     * 推数据到list - 相当于做异常队列
     * @param key
     * @param count
     * @return
     */
    @ResponseBody
    @ApiOperation("testPushRedis")
    @RequestMapping(value="/testPushRedis/{key}/{count}",method= RequestMethod.GET)
    public RespBody testPushRedis(@PathVariable String key,@PathVariable Integer count) {
        RespBody respBody = new RespBody();
        Assert.notNull(key);
        Assert.notNull(count);
        for (int i = 0; i < count; i++) {
            redisService.lpush(RedisConstant.REDIS_MYSQL_VLPR_TO_ES_ERROR_LIST_KEY.concat(key), IdUtil.randomUUID());
        }
        respBody.addOK(count,"Push成功!");
        return  respBody;
    }

    @ResponseBody
    @ApiOperation("testPopRedis")
    @RequestMapping(value="/testPopRedis/{key}",method= RequestMethod.GET)
    public RespBody testPopRedis(@PathVariable String key) {
        RespBody respBody = new RespBody();
        Assert.notNull(key);
        String popStr = redisService.lpop(RedisConstant.REDIS_MYSQL_OBJEXT_TO_ES_ERROR_LIST_KEY.concat(key));
        respBody.addOK(popStr,"popStr成功!");
        return  respBody;
    }


    @ResponseBody
    @ApiOperation("pushToKafka")
    @RequestMapping(value="/pushToKafka/{topic}/{value}",method= RequestMethod.GET)
    public RespBody pushToKafka(@PathVariable String topic,@PathVariable String value) {
        RespBody respBody = new RespBody();
        Assert.notNull(topic);
        respBody.addOK(value);
        appProducer.sendByTopicValue(topic,respBody);
        respBody.addOK(topic,"topic成功!");
        return  respBody;
    }



    @ResponseBody
    @ApiOperation("cache test")
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public RespBody test() {
        RespBody respBody = new RespBody();
        logger.info("test");
        return  respBody;
    }


    @ResponseBody
    @ApiOperation("takeInfoPic ")
    @RequestMapping(value = "/takeInfoPic", method = RequestMethod.GET)
    public RespBody takeInfo() {
        RespBody respBody = new RespBody();
        PictureInfo pictureInfo = DownloadImageQueue.takeInfo();
        logger.info("== DownloadImageQueue:size:{} ==",DownloadImageQueue.getSize());
        respBody.addOK(pictureInfo,"takeInfo pic ");
        return  respBody;
    }


}
