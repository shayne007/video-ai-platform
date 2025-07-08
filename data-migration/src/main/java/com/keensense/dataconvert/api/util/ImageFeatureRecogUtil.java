package com.keensense.dataconvert.api.util;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.keensense.dataconvert.api.alg.IAlgorithm;
import com.keensense.dataconvert.api.request.AppAlgRequest;
import com.keensense.dataconvert.api.request.queue.AlgRequestQueue;
import com.keensense.dataconvert.api.request.queue.DownloadImageQueue;
import com.keensense.dataconvert.api.request.vo.AlgKafkaVo;
import com.keensense.dataconvert.biz.common.cache.redis.RedisConstant;
import com.keensense.dataconvert.biz.common.cache.redis.RedisService;
import com.keensense.dataconvert.biz.common.consts.CommonConst;
import com.keensense.dataconvert.biz.common.enums.ResultEnum;
import com.keensense.dataconvert.biz.entity.PictureInfo;
import com.keensense.dataconvert.framework.common.ext.spring.SpringContextHolder;
import com.keensense.dataconvert.framework.common.utils.cache.EhCacheUtil;
import com.keensense.dataconvert.framework.config.ehcache.constants.EhcacheConstant;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.util.ListUtil;
import com.loocme.sys.util.StringUtil;
import net.sf.ehcache.util.NamedThreadFactory;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.Executors.newScheduledThreadPool;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.api.util
 * @Description： <p> ImageFeatureRecogUtil 图片识别服务 </p>
 * @Author： - Jason
 * @CreatTime：2019/7/25 - 15:05
 * @Modify By：
 * @ModifyTime： 2019/7/25
 * @Modify marker：
 */
public class ImageFeatureRecogUtil {

    private static final Logger logger = LoggerFactory.getLogger(ImageFeatureRecogUtil.class);

    /**
     * picList - 保证线程安全 - or - CopyOnWriteArrayList fix by cui 20190808
     * issue:有queen所以 不会出现多线程同时操作同一变量的情况?
     */
    private static List<PictureInfo> picList = Collections.synchronizedList(new ArrayList<>());

    /**
     * name 线程区分
     */
    public static String IMAGE_FEATURE_RECOG_THREAD = "image-feature-recog";
    public static String IMAGE_QUEEN_TAKE_THREAD = "image-queen-take";

    private static String IMAGE_BASE64_PREFIX = "data:image/jpeg;base64,";

    /**
     * 数据中转对象
     */
    private static AlgKafkaVo algKafkaVo;

    /**
     *  redis 记录请求异常的数据
     */
    private static RedisService redisService  = SpringContextHolder.getBean(RedisService.class);

    /**
     * take image 拿图片处理  queen里面takeQueen
     */
    private final static ExecutorService takeImageExec = newFixedThreadPool(CommonConst.DEAL_QUEEN_TAKE_IMAGE_THREAD_SIZE, new NamedThreadFactory(IMAGE_QUEEN_TAKE_THREAD));

    /**
     * 特征接口
     */
    private final static ExecutorService callFeatureExec = newFixedThreadPool(CommonConst.RECOG_FEATURE_REQUEST_SIZE, new NamedThreadFactory(IMAGE_FEATURE_RECOG_THREAD));

    /**
     * 定时处理最后的残余的数据
     */
    private  static ScheduledExecutorService dealLastDataExecutor =  newScheduledThreadPool(1);

    /**
     * 触发定时轮询是否为最后残余数据的逻辑
     */
    static {
        dealLastData();
    }

    /**
     * 启动识别服务 从queen里面拿数据处理
     */
    public static void startFeatureRecognize(){
        takeImageExec.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        PictureInfo picInfo;
                        while (true) {
                            picInfo = DownloadImageQueue.takeInfo();
                            logger.debug(">>> [DownloadImageQueue - pic take] Success.");
                            /**
                             * null & error 跳过
                             */
                            if (null == picInfo || PictureInfo.STATUS_ERROR == picInfo.getStatus()) {
                                continue;
                            }
                            callFeatureRecognizePicture(picInfo);
                        }
                    }
                }
        );
    }

    /**
     * 调用算法开始识别
     * 将数据推送到kafka
     */
    public static void callFeatureRecognizePicture(PictureInfo picInfo) {
        //下载成功的 推到算法
        if (PictureInfo.STATUS_DOANLOAD_SUCC == picInfo.getStatus()) {
            picInfo.setStatus(PictureInfo.STATUS_RECOG_ING);
            // 设置为(1) -> 一个一个处理
            if (1 == CommonConst.RECOG_FEATURE_PIC_BATCH) {
                featureOrBatchRecognizePicture(picInfo,null,false);
                return;
            } else {
                picList.add(picInfo);
            }
        } else {
            logger.info("====[{}] rId picture recognize failed. ====",picInfo.getRecogId());
            return;
        }


        /**
         * 批量处理 >= n 的时候调用处理一次
         */
        if (ListUtil.isNotNull(picList)){
            if (picList.size() >= CommonConst.RECOG_FEATURE_PIC_BATCH){
                featureOrBatchRecognizePicture(null,picList,true);
                picList = new ArrayList<>();
            }else{
                // 数据没有 - 数据有残余 只能通过数据计算是不是最后的几个数据
                if (!DownloadImageQueue.isNull()){
                    return;
                }
            }
        }
    }


    /**
     * 定时清理 queue里面最后的数据 BUG 不知道什么时间段是满座条件的  相当于是定时轮询判断
     */
    public static void dealLastData(){
        // queue里面当前没有数据 且数据 < batchSize  -- 当做是最后的数据 推过去处理
        dealLastDataExecutor.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (ListUtil.isNotNull(picList)){
                    // 小于batchSize 且 queue为null
                    if (picList.size() < CommonConst.RECOG_FEATURE_PIC_BATCH && DownloadImageQueue.isNull()){
                        logger.warn("[DealLastData]定时清理最后残余待处理的数据Size:[{}] ...",picList.size());
                        featureOrBatchRecognizePicture(null,picList,true);
                        picList = new ArrayList<>();
                    }
                }else {
                    logger.warn("[dealLastData]暂时没有剩余需要处理的数据 ... ");
                }
            }
        }, 1L, CommonConst.DEAL_TIMING_BATCH_DATA_TIME_MINUTES, TimeUnit.MINUTES);
    }

    /**
     * 调用算法识别数据 - 发送到算法 webService 接口 识别pic base64的图片
     * @param picInfo - 一个一个处理
     * @param pictureInfoList   -- 批量处理list
     * @param isBatch   是否批量处理
     */
    public static void featureOrBatchRecognizePicture(PictureInfo picInfo,final List<PictureInfo> pictureInfoList,boolean isBatch) {
        callFeatureExec.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        IAlgorithm algorithm = CommonConst.getAlgorithm();
                        if (null == algorithm) {
                            logger.warn("=== algorithm is Null please check your cfg ... ");
                            return;
                        }
                        AppAlgRequest appAlgReq = AlgRequestQueue.takeRequest();
                        String algRespStr = "";

                        /**
                         * 封装参数
                         */
                        JSONObject singlePic;
                        JSONArray imgArr = new JSONArray();
                        if(isBatch){
                            if (ListUtil.isNull(pictureInfoList)) {
                                return;
                            }
                            for (int i = 0; i < pictureInfoList.size(); i++) {
                                singlePic = new JSONObject();
                                String uuid = IdUtil.fastUUID();
                                convertAlgParams(singlePic,uuid,pictureInfoList.get(i).getPicBase64());
                                convertParamsTransfer(uuid,pictureInfoList.get(i));
                                pictureInfoList.get(i).setPicBase64("");
                                imgArr.add(singlePic);
                            }
                        }else{
                            singlePic = new JSONObject();
                            String uuid = IdUtil.fastUUID();
                            convertAlgParams(singlePic,uuid,picInfo.getPicBase64());
                            convertParamsTransfer(uuid,picInfo);
                            imgArr.add(singlePic);
                        }
                        try {
                            algRespStr = appAlgReq.httpClientPost(algorithm.getRequestUrl(), algorithm.getRequestParam(null, imgArr));
                            logger.info(">>> [调用特征服务]:Url:[{}],imgBatchSize:[{}] ...",algorithm.getRequestUrl(),imgArr.size());
                            Thread.sleep(new Random().nextInt(10) * 1000);
                        } catch (Exception e) {
                            if (isBatch){
                                redisService.lpush(RedisConstant.REDIS_FEATURE_NET_ERROR_LIST_KEY, JSON.toJSONString(pictureInfoList));
                            }else{
                                List<PictureInfo> picList = new ArrayList<>();
                                picList.add(picInfo);
                                redisService.lpush(RedisConstant.REDIS_FEATURE_NET_ERROR_LIST_KEY, JSON.toJSONString(picList));
                            }
                            logger.error("=== [特征服务]Error -> 接口响应异常 ===",e);
                            // fix bug kafka出现少量异常重复数据
                            return;
                        }finally {
                            AlgRequestQueue.putRequest(appAlgReq);
                        }
                        // 如果响应为null将图片状态标记为 识别失败
                        if (isBatch){
                            pushFeature2Kafka(pictureInfoList, algRespStr, appAlgReq.getId());
                        }else{
                            List<PictureInfo> picList = new ArrayList<>();
                            picList.add(picInfo);
                            pushFeature2Kafka(picList, algRespStr, appAlgReq.getId());
                        }
                    }
                }
        );
    }


    /**
     * 推送 数据到 kafka  - kafka需要的参数 组合起来 - 这里无法感知是否推送成功  默认为推送成功
     * @param picInfoList     需要转换的原始图片信息
     * @param algRespStr      算法返回出来的数据
     * @param reqId           请求id 请求唯一标志
     */
    private static void pushFeature2Kafka(final List<PictureInfo> picInfoList, String algRespStr, String reqId) {
        if (StringUtil.isNull(algRespStr)) {
            for (int i = 0; i < picInfoList.size(); i++) {
                picInfoList.get(i).setStatus(PictureInfo.STATUS_RECOG_FAIL);
            }
            return;
        }
        logger.info("=== 调用特征服务接口,返回的1.5K特征值为:\n{}\n",algRespStr);

        /**
         * FIX BUG 响应参数如果为首字母大写的话 无法封装进去
         * FIX BUG 由于loocme[公司的包]里面json工具不同版本依赖一直在变化
         * 以前是net.sf的包 现在是fast_json 导致解析出现异常
         */
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(algRespStr);
        Var retVar = Var.fromObject(jsonObject);
        IAlgorithm algorithm = CommonConst.getAlgorithm();
        if (null == algorithm) {
            logger.warn("=== algorithm is Null please Check your cfg ... ");
            return;
        }
        String errorMsg = retVar.getString(CommonConst.RESULT_ERROR_MSG_KEY);
        String errorCode = algorithm.getResponseCode(retVar);

        //|| !ResultEnum.ALG_FEATURE_SUCCESS.getStrCode().equals(errorCode)
        // 状态码不为0 且 errorCode为null 即接口出现异常
        // issue 问题的原因在于 接口规范不统一  [视频结构化是:0] [图片结构化是:200] 表示成功
        if (CommonConst.RECOG_SERVICE_CHOSE_TYPE ==0 && !ResultEnum.ALG_FEATURE_SUCCESS.getStrCode().equals(errorCode)){
            logger.error("=== [视频结构化接口响应异常]errorCode{},errorMsg:{} ===",errorCode,errorMsg);
            redisService.lpush(RedisConstant.REDIS_FEATURE_CODE_ERROR_LIST_KEY, JSON.toJSONString(picInfoList));
            return;
        }

        if (CommonConst.RECOG_SERVICE_CHOSE_TYPE ==1 && !ResultEnum.BIZ_DEAL_SUCCESS.getStrCode().equals(errorCode)){
            logger.error("=== [图片结构化接口响应异常]errorCode{},errorMsg:{} ===",errorCode,errorMsg);
            redisService.lpush(RedisConstant.REDIS_FEATURE_CODE_ERROR_LIST_KEY, JSON.toJSONString(picInfoList));
            return;
        }

        logger.info("=== reqId:[{}],reqCode:[{}],reqMsg:[{}],推送到kafka ... ",reqId,errorCode,errorMsg);

        /**
         * 开始处理结果 并且推到kafka
         */
        algorithm.dealResponse(picInfoList, retVar);
    }


    /**
     * 将有需要的参数中转存下 - k,v 形式保存到 [ehcache] - 数据封装
     * @param uuid   - k
     * @param picInfo - v
     */
    private static void convertParamsTransfer(String uuid,PictureInfo picInfo){
        algKafkaVo = new AlgKafkaVo();
        algKafkaVo.setUuid(picInfo.getRecogId());
        algKafkaVo.setSerialNumber(picInfo.getSerialNumber());
        algKafkaVo.setStartFramePts(picInfo.getStartFramePts());
        algKafkaVo.setCreatTime(picInfo.getSourceCreatTime());
        algKafkaVo.setObjType(picInfo.getObjType());
        algKafkaVo.setFrameIndex(picInfo.getFrameIndex());
        EhCacheUtil.put(EhcacheConstant.EHCAHCHE_PIC_PUSH_KAFKA_CACHE,uuid,algKafkaVo);
    }


    /**
     * 参数封装  - 不同的算法实现不同的参数封装
     * @param singlePic
     * @param uuid
     * @param base64Url
     */
    private static void convertAlgParams(JSONObject singlePic,String uuid,String base64Url){
        if (CommonConst.RECOG_SERVICE_CHOSE_TYPE == 0){
            singlePic.put("id",uuid);
            singlePic.put("data",IMAGE_BASE64_PREFIX.concat(base64Url));
        }else{
            singlePic.put("ImageID",uuid);
            singlePic.put("Data",base64Url);
        }
    }

}
