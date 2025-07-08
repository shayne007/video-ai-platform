package com.keensense.dataconvert.api.processor;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.keensense.dataconvert.api.handler.impl.Mysql2EsConvertDataHandler;
import com.keensense.dataconvert.api.util.EsMigrateUtil;
import com.keensense.dataconvert.api.util.ImageFeatureRecogUtil;
import com.keensense.dataconvert.api.util.RealTimeImageDownloadUtil;
import com.keensense.dataconvert.biz.common.cache.redis.RedisConstant;
import com.keensense.dataconvert.biz.common.cache.redis.RedisService;
import com.keensense.dataconvert.biz.common.consts.CommonConst;
import com.keensense.dataconvert.biz.common.consts.ConfigPathConstant;
import com.keensense.dataconvert.biz.common.consts.EsConst;
import com.keensense.dataconvert.biz.common.enums.EsIndexTypeEnum;
import com.keensense.dataconvert.biz.entity.ObjextResult;
import com.keensense.dataconvert.biz.entity.PictureInfo;
import com.keensense.dataconvert.biz.entity.VlprResult;
import com.keensense.dataconvert.biz.service.AppSysService;
import com.keensense.dataconvert.biz.service.ElasticSearchService;
import com.keensense.dataconvert.biz.service.ObjextResultService;
import com.keensense.dataconvert.biz.service.VlprResultService;
import com.keensense.dataconvert.framework.common.ext.spring.SpringContextHolder;
import com.keensense.dataconvert.framework.common.utils.date.DateHelper;
import com.loocme.sys.util.ListUtil;
import net.sf.ehcache.util.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.api.processor
 * @Description： <p> AppProcessor 数据处理器  - 根据配置 启动需要运行的 数据转化程序 -  处理-入口 - 监听器容器初始化 开始运行 </p>
 * @Author： - Jason
 * @CreatTime：2019/7/23 - 17:00
 * @Modify By：
 * @ModifyTime： 2019/7/23
 * @Modify marker：
 */
public class AppProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AppProcessor.class);

    private static final String DEAL_MYSQL_DATE_THREAD = "deal-mysql-data";
    private static VlprResultService vlprResultService = SpringContextHolder.getBean(VlprResultService.class);
    private static ObjextResultService objextResultService = SpringContextHolder.getBean(ObjextResultService.class);
    private static AppSysService appSysService = SpringContextHolder.getBean(AppSysService.class);
    private static ElasticSearchService refreshEsService = SpringContextHolder.getBean("refreshEsService");
    private static Mysql2EsConvertDataHandler mysql2EsHandler = new Mysql2EsConvertDataHandler();
    private static List<VlprResult> vlprResults;
    private static List<ObjextResult> objextResults;
    private static PictureInfo picInfo;

    /**
     *  redis 记录总的mysql进入的数据量
     */
    private static RedisService redisService  = SpringContextHolder.getBean(RedisService.class);

    /**
     * 实时图片下载多线程
     */
    private final static ExecutorService dealMysqlDataExec = newFixedThreadPool(3, new NamedThreadFactory(DEAL_MYSQL_DATE_THREAD));

    /**
     * 需要处理的数据
     */
    private static List<PictureInfo> picInfoList = new LinkedList<>();

    /**
     * 构建索引结构
     */
    public static void buildEsIndex(){
        if (!CommonConst.SYS_IS_OPEN_BUILD_ES_INDEX){
            logger.info(" === 当前:buildEsIndex:方法为[关闭]状态 ===");
            return;
        }
        appSysService.buildEsIndexByJson(refreshEsService, EsConst.ES_VLPR_RESULT_INDEX_NAME,EsConst.ES_VLPR_RESULT_INDEX_TYPE,
                ConfigPathConstant.CONFIG_STRUCTURE_BUILD_FILE.concat(CommonConst.REFRESH_ELASTICSEARCH_VERSION).concat(CommonConst.INDEX_VLPR_JSON));
        appSysService.buildEsIndexByJson(refreshEsService, EsConst.ES_OBJEXT_RESULT_INDEX_NAME,EsConst.ES_OBJEXT_RESULT_INDEX_TYPE,
                ConfigPathConstant.CONFIG_STRUCTURE_BUILD_FILE.concat(CommonConst.REFRESH_ELASTICSEARCH_VERSION).concat(CommonConst.INDEX_OBJEXT_JSON));
        appSysService.buildEsIndexByJson(refreshEsService, EsConst.ES_BIKE_RESULT_INDEX_NAME,EsConst.ES_BIKE_RESULT_INDEX_TYPE,
                ConfigPathConstant.CONFIG_STRUCTURE_BUILD_FILE.concat(CommonConst.REFRESH_ELASTICSEARCH_VERSION).concat(CommonConst.INDEX_BIKE_JSON));
        appSysService.buildEsIndexByJson(refreshEsService, EsConst.ES_FACE_RESULT_INDEX_NAME,EsConst.ES_FACE_RESULT_INDEX_TYPE,
                ConfigPathConstant.CONFIG_STRUCTURE_BUILD_FILE.concat(CommonConst.REFRESH_ELASTICSEARCH_VERSION).concat(CommonConst.INDEX_FACE_JSON));
        appSysService.buildEsIndexByJson(refreshEsService, EsConst.ES_SUMMARY_RESULT_INDEX_NAME,EsConst.ES_SUMMARY_RESULT_INDEX_TYPE,
                ConfigPathConstant.CONFIG_STRUCTURE_BUILD_FILE.concat(CommonConst.REFRESH_ELASTICSEARCH_VERSION).concat(CommonConst.INDEX_SUMMARY_JSON));
    }


    /**
     * 根据配置文件来决定是否启动该程序
     */
    public static void startEs2Es(){
        if (!CommonConst.SYS_IS_OPEN_ES_TO_ES){
            logger.info(" === 当前startEs2Es方法为[关闭]状态 ===");
            return;
        }

        /**
         * es 数据迁移 - 源es参数
         */
        JSONObject startConfig = new JSONObject();
        startConfig.put("host",CommonConst.SOURCE_ELASTICSEARCH_HOST);
        startConfig.put("loadSize",CommonConst.ES_OLD_ES_LOAD_BATCH_SIZE);
        String[] indexNames = CommonConst.ES_NEED_CONVERT_INDEX_NAME_LIST.split(",");
        String[] indexTypes = CommonConst.ES_NEED_CONVERT_INDEX_TYPE_LIST.split(",");
        String[] idColumns = CommonConst.ES_NEED_CONVERT_ID_COLUMNS_LIST.split(",");
        if (null != indexNames && null != indexTypes){
            if (indexNames.length == indexTypes.length){
                for (int i = 0; i < indexNames.length; i++) {
                    startConfig.put("indexName",indexNames[i]);
                    startConfig.put("indexType",indexTypes[i]);
                    startConfig.put("idColumn",idColumns[i]);
                    // 源es-->目的es
                    EsMigrateUtil.startMigrate(startConfig);
                }
            }else{
                logger.warn("== 请检查配置文件[app-data-convert.properties],indexNames,indexTypes ==");
            }
        }else {
            logger.error("=== es迁移配置为Null,请检查后重试 ===");
            return;
        }
        logger.info("=== 开始处理数据,当前数据处理方法为:startEs2Es ==");
    }


    /**
     *根据配置文件来决定是否启动该程序
     */
    public static void startMysql2Es(){
        if (!CommonConst.SYS_IS_OPEN_MYSQL_TO_ES){
            logger.info(" === 当前:startMysql2Es:方法为[关闭]状态 ===");
            return;
        }
        String ymdByIndex;
        Map<String, Integer> daysIndex = getDaysIndex(CommonConst.DEAL_OLD_MYSQL_TO_ES_DATE_INDEX_ENABLE);
        Integer startYmd = daysIndex.get("startYmd");
        Integer endYmd = daysIndex.get("endYmd");
        Integer pageSize = CommonConst.DEAL_MYSQL_DAY_LOAD_PAGE_SIZE;
        for (int index = startYmd; index <= endYmd; index++) {
            List<PictureInfo> dealMysqlList = new LinkedList<>();
            ymdByIndex = DateHelper.getYmdByIndex(index);
            boolean flagVlprLoad = true;
            boolean flagObjextLoad = true;
            Integer vlprPageNum = 1;
            Integer objextPageNum = 1;
            while(flagVlprLoad){
                Page<VlprResult> vlprResultPage = new Page<>(vlprPageNum,pageSize);
                vlprResults = vlprResultService.selectListByPage(ymdByIndex,vlprResultPage).getList();
                if (ListUtil.isNotNull(vlprResults)){
                    //批量保存数据
                    mysql2EsHandler.convertJsonList(vlprResults,null,ymdByIndex);
                    for (VlprResult vlprResult: vlprResults) {
                        picInfoList.add(convertInitPicInfo(vlprResult, null));
                    }
                    vlprPageNum += 1;
                    batchDealMysqlData(picInfoList,dealMysqlList,true,ymdByIndex);
                }else {
                    flagVlprLoad = false;
                }
            }

            while (flagObjextLoad){
                Page<ObjextResult> objextResultPage = new Page<>(objextPageNum,pageSize);
                objextResults = objextResultService.selectListByPage(ymdByIndex,objextResultPage).getList();
                if (ListUtil.isNotNull(objextResults)){
                    mysql2EsHandler.convertJsonList(null,objextResults,ymdByIndex);
                    for (ObjextResult objextResult:objextResults) {
                        picInfoList.add(convertInitPicInfo(null, objextResult));
                    }
                    objextPageNum += 1 ;
                    batchDealMysqlData(picInfoList,dealMysqlList,true,ymdByIndex);
                }else {
                    flagObjextLoad = false;
                }
            }
            // 最后是没有批处理完的 理论上是<= batchSize
            batchDealMysqlData(picInfoList,dealMysqlList,false,ymdByIndex);
        }

    }


    /**
     * 数据达到一定的量 就去处理
     * @param list 全局数据
     * @param dealMysqlList 中转数据
     * @param isBatch   是否为patch处理
     * @param ymdStr    对应的ymd的数据
     */
    public static void batchDealMysqlData(List<PictureInfo> list,List<PictureInfo> dealMysqlList,boolean isBatch,String ymdStr){
        if (isBatch){
            if (judgeDealMysqlData(list)){
                dealMysqlList = list;
                dealMysqlData(dealMysqlList,ymdStr);
                ImageFeatureRecogUtil.startFeatureRecognize();
                picInfoList = new ArrayList<>();
            }
        }else {
            logger.info(">>> Deal NotBatch Data Size:[{}] ...",dealMysqlList.size());
            if (ListUtil.isNotNull(picInfoList)){
                dealMysqlList = list;
                dealMysqlData(dealMysqlList,ymdStr);
                ImageFeatureRecogUtil.startFeatureRecognize();
                picInfoList = new ArrayList<>();
            }
        }
    }


    /**
     * 判断是否需要处理 批量处理
     * @param list
     * @return
     */
    public static boolean judgeDealMysqlData(List<PictureInfo> list){
        if (ListUtil.isNotNull(list)){
            if (list.size() > CommonConst.DEAL_MYSQL_DAY_LOAD_BATCH_DEAL){
                return true;
            }
        }
        return false;
    }




    /**
     * 数据量超过 设定数据就处理一次
     * @param dealPicInfoList 需要处理的数据
     * @param ymdStr   - 统计数据的ymd节点
     */
    public static void dealMysqlData(List<PictureInfo> dealPicInfoList,String ymdStr){
        logger.info(">>> Deal Batch Data Size:[{}] ...",dealPicInfoList.size());
        redisService.incrBy(RedisConstant.REDIS_MYSQL_INTO_QUEEN_TODEAL_COUNT.concat(ymdStr),dealPicInfoList.size());
        dealMysqlDataExec.execute(new Runnable() {
            @Override
            public void run() {
                /**
                 * 下载图片到 queen - 多线程 处理到 queen   - 一天一天处理
                 */
                if (ListUtil.isNotNull(dealPicInfoList)){
                    for (PictureInfo pictureInfo: dealPicInfoList) {
                        RealTimeImageDownloadUtil.downloadResource(pictureInfo);
                    }
                }
                try {
                    Thread.sleep(new Random().nextInt(10) * 1000);
                } catch (InterruptedException e) {
                    logger.error("=== InterruptedException:error:[{}] ===",e.getMessage());
                }
            }
        });
    }

    /**
     * 透传参数封装 - 需要冗余的数据 StartFramePts RecogId CreatTime  --> 后面推入到kafka
     * @param vlprResult
     * @param objextResult
     * @return
     */
    public static PictureInfo convertInitPicInfo(VlprResult vlprResult,ObjextResult objextResult){
        picInfo = new PictureInfo();
        picInfo.setStatus(0);

        if (vlprResult!=null){
            picInfo.setStartFramePts(String.valueOf(vlprResult.getStartframepts()));
            picInfo.setRecogId(vlprResult.getRecogId());
            picInfo.setSourceCreatTime(vlprResult.getCreatetime());
            picInfo.setSerialNumber(vlprResult.getSerialnumber());
            picInfo.setObjType(EsIndexTypeEnum.VLPR.getObjType());
            //vlprResult
            picInfo.setFrameIndex(vlprResult.getFrameIndex() * 45);
            String smallImgUrl = vlprResult.getSmallimgurl();
            if (CommonConst.HTTP_PICTURE_IS_NEED_MAPPING){
                smallImgUrl = smallImgUrl.replaceAll("http://127.0.0.1:8082", CommonConst.HTTP_PICTURE_PREFIX);
            }
            picInfo.setPicUrl(smallImgUrl);
        }

        if (objextResult!=null){
            picInfo.setStartFramePts(String.valueOf(objextResult.getStartframepts()));
            picInfo.setRecogId(objextResult.getRecogId());
            picInfo.setSourceCreatTime(objextResult.getCreatetime());
            picInfo.setSerialNumber(objextResult.getSerialnumber());
            BigDecimal nums = NumberUtil.add(objextResult.getStartframepts(), objextResult.getEndframepts());
            BigDecimal div = NumberUtil.div(nums, 2);
            picInfo.setFrameIndex(div.longValue());
            logger.debug("===[数据帧对比]FrameIndex:[{}],startFramePts:[{}] ...",div.longValue(),objextResult.getStartframepts());
            picInfo.setObjType(objextResult.getObjtype());
            String imgUrl = objextResult.getImgurl();
            if (CommonConst.HTTP_PICTURE_IS_NEED_MAPPING){
                imgUrl = imgUrl.replaceAll("http://127.0.0.1:8082", CommonConst.HTTP_PICTURE_PREFIX);
            }
            picInfo.setPicUrl(imgUrl);
        }
        return picInfo;
    }


    /**
     * 获取起始时间配置
     * @param enableIndex
     * @return
     */
    public static Map<String,Integer> getDaysIndex(Boolean enableIndex){
        HashMap<String,Integer> indexDaysMap = new HashMap<>(5);
        if (enableIndex){
            indexDaysMap.put("startYmd",CommonConst.DEAL_OLD_MYSQL_TO_ES_START_INDEX);
            indexDaysMap.put("endYmd",CommonConst.DEAL_OLD_MYSQL_TO_ES_END_INDEX);
        }else {
            Date startDateStr = DateHelper.getDateByStr(CommonConst.DEAL_OLD_MYSQL_TO_ES_START);
            Date endDateStr = DateHelper.getDateByStr(CommonConst.DEAL_OLD_MYSQL_TO_ES_END);
            indexDaysMap.put("startYmd",DateHelper.getDateDiffDays(startDateStr, new Date()));
            indexDaysMap.put("endYmd",DateHelper.getDateDiffDays(endDateStr, new Date()));
        }
        return  indexDaysMap;
    }

}
