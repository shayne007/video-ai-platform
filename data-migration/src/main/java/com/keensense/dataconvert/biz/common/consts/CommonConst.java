package com.keensense.dataconvert.biz.common.consts;

import com.keensense.dataconvert.api.alg.IAlgorithm;
import com.keensense.dataconvert.api.alg.impl.AppAlgorithmImpl;
import com.keensense.dataconvert.api.alg.impl.PictureAlgorithmImpl;
import com.keensense.dataconvert.api.handler.IEs2EsHandler;
import com.keensense.dataconvert.api.handler.impl.Es2EsConvertDataHandler;
import com.keensense.dataconvert.framework.common.utils.properties.PropertiesHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.common.consts
 * @Description： <p> CommonConst </p>
 * @Author： - Jason
 * @CreatTime：2019/7/23 - 15:33
 * @Modify By：
 * @ModifyTime： 2019/7/23
 * @Modify marker：
 */
public class CommonConst {

    private static Logger logger = LoggerFactory.getLogger(CommonConst.class);

    /**
     * 数据库模块名
     */
    public static String APP_U2S_RECOG_MODULE= "u2s_recog";

    public static String APP_QST_U2S_MODULE = "qst_u2s";

    /**
     * 类型
     */
    public static int OBJECT_TYPE_OBJEXT = 1;
    public static int OBJECT_TYPE_VLPR = 2;
    public static int OBJECT_TYPE_BIKE = 4;

    /**
     * lam 默认传0 或者不传
     */
    public static int FEATURE_FIRST_OBJ = 0;


    /**
     * 系统配置 是否 开启build构建es索引结构
     */
    public static Boolean SYS_IS_OPEN_BUILD_ES_INDEX = true;

    /**
     * 是否开 es - es 转换功能
     */
    public static Boolean SYS_IS_OPEN_ES_TO_ES = true;

    /**
     * 是否开启 mysql - es - 推送kafka
     */
    public static Boolean SYS_IS_OPEN_MYSQL_TO_ES = true;


    /**
     * 是否需要重新 添加任务 和修改任务关系
     */
    public static Boolean SYS_IS_NEED_RE_ADD_TASK = false;

    /**
     * mysql - 到es入库是否需要转换字段 默认为不转换
     * [4.0.3.7,4.0.4] --> [4.0.5,4.0.6,4.0.4]   - false
     * [4.0.3.7,4.0.4]  --> (5.0+           - true
     */
    public static Boolean SYS_IS_OPEN_MYSQL_TO_ES_NEED_CONVERT_FIELD = false;

    public static final  String INDEX_BIKE_JSON = "-index-bike.json";
    public static final  String INDEX_VLPR_JSON = "-index-vlpr.json";
    public static final  String INDEX_OBJEXT_JSON = "-index-objext.json";
    public static final  String INDEX_FACE_JSON = "-index-face.json";
    public static final  String INDEX_SUMMARY_JSON = "-index-summary.json";

    /**
     * 返回结果code
     */
    public static final String  RESULT_CODE_KEY = "ret";
    /**
     * 返回内容
     */
    public static final String  RESULT_CONTENT_KEY = "results";


    /**
     * 图片结构化接口响应
     */
    public static final String  RESULT_PIC_OBJECT_LIST_KEY = "ObjectList";


    /**
     * 返回信息
     */
    public static final String RESULT_ERROR_MSG_KEY = "error_msg";

    /**
     * 推送到特征提取服务的批处理个数 - 默认为8个
     */
    public static  Integer RECOG_FEATURE_PIC_BATCH = 8;

    /**
     * 识别服务的类型
     */
    public static  int RECOG_SERVICE_CHOSE_TYPE = 0;

    //----------------------------------------------------------------------------------------
    /**
     * 视频结构化的接口 多个接口提供服务 轮询路由选择
     */
    public static String  RECOG_VIDEO_FEATURE_CLUSTER_URLS;

    /**
     * 视频结构化的接口 是否开启多个接口提供服务
     */
    public static Boolean RECOG_VIDEO_FEATURE_CLUSTER_ENABLE = false;

    /**
     * 外部识别接口
     */
    public static String RECOG_VIDEO_FEATURE_URL = "http://127.0.0.1:8100/v8/images/objects";
    //----------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------
    /**
     * 图片结构化的接口 多个接口提供服务 轮询路由选择
     */
    public static String  RECOG_PICTURE_FEATURE_CLUSTER_URLS;

    /**
     * 图片结构化的接口 是否开启多个接口提供服务
     */
    public static Boolean RECOG_PICTURE_FEATURE_CLUSTER_ENABLE = false;

    /**
     * 图片结构化的接口
     */
    public static String RECOG_PICTURE_FEATURE_URL = "http://127.0.0.1:8100/images/recog";
    //----------------------------------------------------------------------------------------

    /**
     * 图片地址是否需要做地址映射
     */
    public static boolean HTTP_PICTURE_IS_NEED_MAPPING = false;

    /**
     * 图片访问前缀 windows 测试需要
     */
    public static String HTTP_PICTURE_PREFIX = "http://127.0.0.1:8082";

    /**
     * kafka topic
     */
    public static String RECOG_KAKFA_TOPIC;


    /**
     * 获取时间范围方式 默认为 index方式 即[n1-n2]
     */
    public static Boolean DEAL_OLD_MYSQL_TO_ES_DATE_INDEX_ENABLE = true;

    /**
     *  mysql 旧数据 迁移到 es  - 起始时间
     */
    public static String DEAL_OLD_MYSQL_TO_ES_START;
    public static Integer DEAL_OLD_MYSQL_TO_ES_START_INDEX;

    /**
     *  mysql 旧数据 迁移到 es  - 结束时间
     */
    public static String DEAL_OLD_MYSQL_TO_ES_END;
    public static Integer DEAL_OLD_MYSQL_TO_ES_END_INDEX;


    /**
     * es 需要处理的indexName list
     */
    public static String ES_NEED_CONVERT_INDEX_NAME_LIST;
    public static String ES_NEED_CONVERT_INDEX_TYPE_LIST;
    public static String ES_NEED_CONVERT_ID_COLUMNS_LIST;

    /**
     * es 滚动查询大小 - 默认取 500
     */
    public static int ES_OLD_ES_LOAD_BATCH_SIZE;

    /**
     * 源es库
     */
    public static String SOURCE_ELASTICSEARCH_VERSION;
    public static String SOURCE_ELASTICSEARCH_HOST;
    public static int SOURCE_ELASTICSEARCH_PORT;
    public static String SOURCE_ELASTICSEARCH_USERNAME;
    public static String SOURCE_ELASTICSEARCH_PASSWORD;

    /**
     * 目的es库
     */
    public static String TARGET_ELASTICSEARCH_VERSION;
    public static String TARGET_ELASTICSEARCH_HOST;
    public static int TARGET_ELASTICSEARCH_PORT;
    public static String TARGET_ELASTICSEARCH_USERNAME;
    public static String TARGET_ELASTICSEARCH_PASSWORD;

    /**
     * 需要刷新的库
     */
    public static String REFRESH_ELASTICSEARCH_VERSION;
    public static String REFRESH_ELASTICSEARCH_HOST;
    public static int REFRESH_ELASTICSEARCH_PORT;
    public static String REFRESH_ELASTICSEARCH_USERNAME;
    public static String REFRESH_ELASTICSEARCH_PASSWORD;


    /**
     * HttpClient - 建立连接的超时时间 (s) * 1000
     */
    public static int HTTP_CLIENT_CONNECT_TIMEOUT = 10;

    /**
     * 指客户端和服务进行数据交互的时间  (s) * 1000
     * 是指两者之间如果两个数据包之间的时间大于该时间则认为超时 而不是整个交互的整体时间
     * eg：比如如果设置1秒超时,如果每隔0.8秒传输一次数据传输10次总共8秒,这样是不超时的,而如果任意两个数据包之间的时间超过了1秒则超时.
     */
    public static int HTTP_CLIENT_SOCKET_TIMEOUT = 10;

    /**
     *下载超时时间
     */
    public static int DOWNLOAD_READ_TIMEOUT = 10;

    /**
     * 连接超时
     */
    public static int DOWNLOAD_CONNECT_TIME_OUT = 5;


    /**
     * 下载queen
     */
    public static int DOWNLOAD_QUEUE_CAPACITY = 500;

    /**
     * 请求queen大小
     */
    public static int DOWNLOAD_QUEUE_REQUEST_SIZE = 300;

    /**
     * 调用特征接口queen大小
     */
    public static int RECOG_FEATURE_REQUEST_SIZE = 6;

    /**
     * 下载图片线程大小
     */
    public static  int DOWNLOAD_IMAGE_THREAD_SIZE = 3;

    /**
     * 取queen里面的图片处理
     */
    public static  int DEAL_QUEEN_TAKE_IMAGE_THREAD_SIZE = 3;


    /**
     * 分页相关pageSize - 默认10000条一次
     */
    public static  Integer DEAL_MYSQL_DAY_LOAD_PAGE_SIZE = 10000;

    /**
     * 分批处理 - 每次处理的数据量
     */
    public static  Integer DEAL_MYSQL_DAY_LOAD_BATCH_DEAL = 10000;


    /**
     * 定时处理 - 批处理最后的残余数据 单位 分钟 - 默认5分钟处理一次
     */
    public static  long DEAL_TIMING_BATCH_DATA_TIME_MINUTES = 5L;

    /**
     * mysql task 对任务进行添加处理
     */
    public static String BIZ_TASK_MANAGER_ADD_TASK_URL;
    public static String BIZ_SOURCE_MYSQL_TABLE;
    public static String BIZ_SOURCE_MYSQL_TABLE_TEMP;
    public static String BIZ_SOURCE_MYSQL_URL;
    public static String BIZ_SOURCE_MYSQL_URL_TEMP;
    public static String BIZ_SOURCE_MYSQL_USER_NAME;
    public static String BIZ_SOURCE_MYSQL_USER_PASS_WORD;

    /**
     * 具体的算法实现
     */
    private static IAlgorithm algorithm = null;

    /**
     * 具体处理器
     */
    private static IEs2EsHandler esHandler = null;

    public static IAlgorithm getAlgorithm() {
        return algorithm;
    }

    public static IEs2EsHandler getEsHandler() {
        return esHandler;
    }

    /**
     * 初始化加载
     */
    static {
        load();
    }



    /**
     * app-data-convert.properties
     */
    public static void load() {
        logger.info("=== load:初始化配置[CommonConst] ===");
        PropertiesHelper props = new PropertiesHelper(ConfigPathConstant.CONFIG_DATA_CONVERT_CLASS_PATH);

        // ------------------------------[结构化类型]---------------------------------------------------
        RECOG_SERVICE_CHOSE_TYPE = props.getInteger("api.recognize.service.chose.type", 0);
        // ------------------------------[图片结构化]---------------------------------------------------
        RECOG_PICTURE_FEATURE_URL = props.getProperty("api.recognize.picture.feature.request.url");
        RECOG_PICTURE_FEATURE_CLUSTER_ENABLE = props.getBoolean("api.recognize.picture.feature.cluster.enable",false);
        RECOG_PICTURE_FEATURE_CLUSTER_URLS = props.getProperty("api.recognize.picture.feature.cluster.urls");
        // ------------------------------[视频结构化]--------------------------------------------------
        RECOG_VIDEO_FEATURE_URL = props.getProperty("api.recognize.video.feature.request.url");
        RECOG_VIDEO_FEATURE_CLUSTER_ENABLE = props.getBoolean("api.recognize.video.feature.cluster.enable",false);
        RECOG_VIDEO_FEATURE_CLUSTER_URLS = props.getProperty("api.recognize.video.feature.cluster.urls");
        // ---------------------------------------------------------------------------------

        // ------------------------------[图片地址映射]----------------------------------------
        HTTP_PICTURE_PREFIX = props.getProperty("http.picture.prefix");
        HTTP_PICTURE_IS_NEED_MAPPING = props.getBoolean("http.picture.is.need.mapping");
        // ------------------------------[图片地址映射]----------------------------------------

        RECOG_KAKFA_TOPIC = props.getProperty("api.kafka.recognize.object.topic");

        ES_NEED_CONVERT_INDEX_NAME_LIST = props.getProperty("deal.old.es.to.es.index.names");
        ES_NEED_CONVERT_INDEX_TYPE_LIST = props.getProperty("deal.old.es.to.es.index.types");
        ES_NEED_CONVERT_ID_COLUMNS_LIST = props.getProperty("deal.old.es.to.es.id.columns");
        ES_OLD_ES_LOAD_BATCH_SIZE = props.getInteger("deal.old.es.to.es.load.batch.size", 500);

        SOURCE_ELASTICSEARCH_VERSION = props.getProperty("source.elasticsearch.version");
        SOURCE_ELASTICSEARCH_HOST = props.getProperty("source.elasticsearch.host","127.0.0.1");
        SOURCE_ELASTICSEARCH_PORT = props.getInteger("source.elasticsearch.port",9200);
        SOURCE_ELASTICSEARCH_USERNAME = props.getProperty("source.elasticsearch.username");
        SOURCE_ELASTICSEARCH_PASSWORD = props.getProperty("source.elasticsearch.password");

        TARGET_ELASTICSEARCH_VERSION = props.getProperty("target.elasticsearch.version");
        TARGET_ELASTICSEARCH_HOST = props.getProperty("target.elasticsearch.host");
        TARGET_ELASTICSEARCH_PORT = props.getInteger("target.elasticsearch.port",9200);
        TARGET_ELASTICSEARCH_USERNAME = props.getProperty("target.elasticsearch.username");
        TARGET_ELASTICSEARCH_PASSWORD = props.getProperty("target.elasticsearch.password");

        REFRESH_ELASTICSEARCH_VERSION = props.getProperty("refresh.elasticsearch.version");
        REFRESH_ELASTICSEARCH_HOST = props.getProperty("refresh.elasticsearch.host");
        REFRESH_ELASTICSEARCH_PORT = props.getInteger("refresh.elasticsearch.port",9200);
        REFRESH_ELASTICSEARCH_USERNAME = props.getProperty("refresh.elasticsearch.username");
        REFRESH_ELASTICSEARCH_PASSWORD = props.getProperty("refresh.elasticsearch.password");

        DEAL_OLD_MYSQL_TO_ES_DATE_INDEX_ENABLE = props.getBoolean("deal.old.mysql.to.es.date.index.enable",true);
        DEAL_OLD_MYSQL_TO_ES_START = props.getProperty("deal.old.mysql.to.es.start");
        DEAL_OLD_MYSQL_TO_ES_START_INDEX = props.getInteger("deal.old.mysql.to.es.start.index",-3);
        DEAL_OLD_MYSQL_TO_ES_END = props.getProperty("deal.old.mysql.to.es.end");
        DEAL_OLD_MYSQL_TO_ES_END_INDEX = props.getInteger("deal.old.mysql.to.es.end.index",3);

        /**
         * 根据配置来选择不同的算法实现
         * [0]视频结构化算法
         * [1]图片结构化算法
         */
        if (RECOG_SERVICE_CHOSE_TYPE == 0){
            algorithm = new AppAlgorithmImpl();
        }else{
            algorithm = new PictureAlgorithmImpl();
        }
        // 根据配置来
        esHandler = new Es2EsConvertDataHandler();

        SYS_IS_OPEN_BUILD_ES_INDEX = props.getBoolean("sys.switch.case.open.build.es",true);
        SYS_IS_OPEN_ES_TO_ES = props.getBoolean("sys.switch.case.open.es.to.es",true);
        SYS_IS_OPEN_MYSQL_TO_ES = props.getBoolean("sys.switch.case.open.mysql.to.es",true);
        SYS_IS_NEED_RE_ADD_TASK = props.getBoolean("sys.switch.case.re.add.task",false);
        SYS_IS_OPEN_MYSQL_TO_ES_NEED_CONVERT_FIELD = props.getBoolean("sys.mysql.to.es.need.convert.field",false);
        logger.info("=== ES_INDEX:[{}],ES_ES:[{}],MYSQL_ES:[{}] ===",SYS_IS_OPEN_BUILD_ES_INDEX,SYS_IS_OPEN_ES_TO_ES,SYS_IS_OPEN_MYSQL_TO_ES);
        RECOG_FEATURE_PIC_BATCH = props.getInteger("biz.deal.recog.feature.pic.batch",8);
        DOWNLOAD_QUEUE_CAPACITY = props.getInteger("biz.deal.download.queue.capacity",500);
        HTTP_CLIENT_CONNECT_TIMEOUT = props.getInteger("biz.deal.http.client.connect.timeout",10);
        HTTP_CLIENT_SOCKET_TIMEOUT = props.getInteger("biz.deal.http.client.socket.timeout",10);
        DOWNLOAD_READ_TIMEOUT = props.getInteger("biz.deal.download.read.timeout",10);
        DOWNLOAD_CONNECT_TIME_OUT = props.getInteger("biz.deal.download.connect.time.out",5);
        DOWNLOAD_QUEUE_REQUEST_SIZE = props.getInteger("biz.deal.download.queue.request.size",3);
        RECOG_FEATURE_REQUEST_SIZE = props.getInteger("biz.deal.recog.feature.request.size",CommonConst.RECOG_FEATURE_REQUEST_SIZE);
        DOWNLOAD_IMAGE_THREAD_SIZE = props.getInteger("biz.deal.download.image.thread.size",CommonConst.RECOG_FEATURE_REQUEST_SIZE);
        DEAL_QUEEN_TAKE_IMAGE_THREAD_SIZE = props.getInteger("biz.deal.queen.take.image.thread.size",CommonConst.RECOG_FEATURE_REQUEST_SIZE);
        //mysql page_helper pageSize
        DEAL_MYSQL_DAY_LOAD_PAGE_SIZE = props.getInteger("biz.deal.mysql.day.load.page.size",10000);
        DEAL_TIMING_BATCH_DATA_TIME_MINUTES = props.getInteger("biz.deal.timing.batch.data.time.minutes",5);
        DEAL_MYSQL_DAY_LOAD_BATCH_DEAL =  props.getInteger("biz.deal.mysql.day.load.batch.deal",10000);

        // task mysql
        BIZ_TASK_MANAGER_ADD_TASK_URL = props.getProperty("biz.task.manager.add.task.url");
        BIZ_SOURCE_MYSQL_TABLE = props.getProperty("biz.source.mysql.table","vsd_task");
        BIZ_SOURCE_MYSQL_TABLE_TEMP = props.getProperty("biz.source.mysql.table.temp","vsd_task_temp");
        BIZ_SOURCE_MYSQL_URL = props.getProperty("biz.source.mysql.url","jdbc:mysql://127.0.0.1:3306/u2s");
        BIZ_SOURCE_MYSQL_URL_TEMP = props.getProperty("biz.source.mysql.url.temp","jdbc:mysql://127.0.0.1:3306/u2s");
        BIZ_SOURCE_MYSQL_USER_NAME = props.getProperty("biz.source.mysql.user.name","root");
        BIZ_SOURCE_MYSQL_USER_PASS_WORD = props.getProperty("biz.source.mysql.user.pass.word","Ast4HS");


        logger.info("=== BatchSize:{} ===",RECOG_FEATURE_PIC_BATCH);
    }

    private CommonConst() {}
}
