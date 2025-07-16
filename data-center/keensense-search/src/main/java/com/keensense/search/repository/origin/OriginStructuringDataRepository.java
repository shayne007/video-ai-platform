package com.keensense.search.repository.origin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.common.exception.VideoException;
import com.keensense.common.util.DateUtil;
import com.keensense.common.util.ResponseUtil;
import com.keensense.search.domain.EventResult;
import com.keensense.search.domain.FdfsCapacityResult;
import com.keensense.search.domain.VehicleflowrateResult;
import com.keensense.search.domain.ViolationResult;
import com.keensense.search.domain.VlprResult;
import com.keensense.search.es.ElasticSearchService;
import com.keensense.search.exception.QueryException;
import com.keensense.search.repository.StructuringDataRepository;
import com.keensense.search.schedule.ElasticSearchClusterIpSchedule;
import com.keensense.search.utils.ClassUtil;
import com.keensense.common.util.HttpClientUtil2;
import com.keensense.search.utils.JsonConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Repository("originStructuringDataRepository")
@EnableScheduling
@Slf4j
@RefreshScope
public class OriginStructuringDataRepository implements StructuringDataRepository {

    private static final int DEFAULT_PAGE_RECORD = 500;
    private static final int DEFAULT_PAGE_START_NUMBER = 1;
    private static final int MAX_PAGE_RECORD = 3000;

    @Value("${origin.es.datasource.port}")
    String port;
    @Value("${origin.es.datasource.host}")
    String host;
    @Value("${origin.es.datasource.username}")
    String username;

    @Value("${send.kafka.after.saveES}")
    String sendKafka;
    @Value("${kafka.bootstrap}")
    String kafkaAddress;
    @Value("${send.kafka.motorVehicle.topic}")
    String motorVehicleTopic;// 机动车
    @Value("${send.kafka.violation.topic}")
    String violationTopic;// 违章
    @Value("${send.kafka.event.topic}")
    String eventTopic;// 事件
    @Value("${send.kafka.vehicleflowrate.topic}")
    String vehicleflowrateTopic;// 车流
    @Value("${es.save.fail.compensation}")
    String compensation;// es入库失败数据是开启补偿机制;默认true 开启
    @Value("${es.save.fail.path}")
    String failPath;// es入库失败数据存储文件目录
    @Value("${es.save.pool.size}")
    int poolSize;// es入库线程池大小
    @Value("${es.save.queue.size}")
    int maxQueueSize;// es入库队列初始大小值
    int optCount = 0;// es入库跳过处理的次数

    private static ExecutorService kafkaServicePool = Executors.newSingleThreadExecutor();
    private static KafkaConsumer<String, String> consumer;
    private static Producer producer;
    @Autowired
    protected JsonConvertUtil jsonConvertUtil;

    private String auth;
    @Autowired
    private ElasticSearchClusterIpSchedule elasticSearchClusterIpSchedule;
    // ES数据源
    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Autowired
    ElasticSearchService elasticSearchService;

    private static List<Object> insertList = new LinkedList<>();
    private static Map<String, String> updateMap = new HashMap<>();
    private static ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
    private static Lock insertLock = new ReentrantLock();
    private static Lock updateLock = new ReentrantLock();
    // ES入库线程池
    private ThreadPoolExecutor esSavePool = null;
    // ES入库失败处理线程池
    private ThreadPoolExecutor esFaildPool =
            new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

    @PostConstruct
    private void generatorAuth() {
        String[] user = username.split(",");
        String userName = user[0];
        String userPath = user[1];
        auth = "Basic " + new String(Base64.getEncoder().encode((userName + ":" + userPath).getBytes()));

        esSavePool =
                new ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }

    public DataSource getSecondDataSource() {
        return dataSource;
    }

//    public <T> Select<T> getSelect(Class<T> clazz) {
//        return Select.from(clazz, getSecondDataSource());
//    }

//    public boolean insert(Object object) {
//        return Insert.values(getSecondDataSource(), object);
//    }

//    public <T> Delete<T> getDelete(Class<T> clazz) {
//        return Delete.from(clazz, getSecondDataSource());
//    }

//    public <T> Update<T> getUpdate(Class<T> clazz) {
//        return Update.from(clazz, getSecondDataSource());
//    }

//    public int updateById(Object object) {
//        return Update.byId(getSecondDataSource(), object);
//    }

//    public SqlExecutor getSqlExecutor() {
//        return SqlExecutor.getInstance(getSecondDataSource());
//    }

    @Override
    public void save(Object result) {
        insertLock.lock();
        try {
            insertList.add(result);
        } finally {
            insertLock.unlock();
        }
    }

    /**
     * ES异步存储方法，每500ms一次，在高峰值情况下是肯定处理不过来的，所以要做成入库线程池异步入库，
     * 不能再该方法阻塞式入库，避免堆积太多定时任务导致队列过大
     */
    @Scheduled(fixedRate = 500)
    protected void bulkSave() {
        int queueize = esSavePool.getQueue().size();
        // 如果es入库的线程池队列超过了maxQueueSize，说明有任务堆积，这时候可能需要放大每次批量的量级（扩大切片时间）
        if (queueize > maxQueueSize) {
            optCount++;
            if (optCount == 1) {// 第1次进来，则跳过，扩大时间分片
                log.info("ES save queue size {}, list size {} ", queueize, insertList.size());
                return;
            } else if (optCount == 2 && (queueize > (maxQueueSize + 4))) {
                // 第2次进来，且队列超过了maxQueueSize+4，则跳过，再扩大一次时间分片;出现堆积，应该大量处于该区域处理，所以它的队列设置大点
                log.info("ES save queue size {}, list size {}, opt count {} ", queueize, insertList.size(), optCount);
                return;
            } else if (optCount == 3 && (queueize > (maxQueueSize + 6))) {
                // 第3次进来，且队列超过了maxQueueSize+6，则跳过，再扩大一次时间分片
                log.info("ES save queue size {}, list size {}, opt count {} ", queueize, insertList.size(), optCount);
                return;
            } else if (queueize > (maxQueueSize + 8)) {// 队列超过了maxQueueSize+8，则跳过，不能让队列无限堆积
                log.info("ES save queue size {} , list size {}, opt count {} ", queueize, insertList.size(), optCount);
                return;
            }
        }
        // 如果能经过筛选，则跳过次数置为0
        optCount = 0;
        // 整个方法catch异常，防止出现异常后线程退出，无法继续推送
        try {
            // 使用try finally,保证锁的释放
            List<Object> insertExecuteResult = null;
            try {
                insertLock.lock();
                insertExecuteResult = insertList;
                insertList = new LinkedList<>();
            } finally {
                insertLock.unlock();
            }
            // 将插入请求发送到es
            if (null != insertExecuteResult && !insertExecuteResult.isEmpty()) {
                List<Object> dataList = insertExecuteResult;
                esSavePool.execute(() -> batchSaveEs(dataList));
            }
        } catch (Exception e) {
            log.error("push to es save failed");
            log.error("printstack:", e);
        }
    }

    /**
     * ES异步更新存储方法，每500ms一次，在高峰值情况下是肯定处理不过来的，所以要做成入库线程池异步入库，不能再该方法阻塞式入库
     */
    @Scheduled(fixedRate = 500)
    protected void bulkUpdate() {
        // 整个方法catch异常，防止出现异常后线程退出，无法继续推送
        try {
            // 使用try finally,保证锁的释放
            Map<String, String> updateExecuteResult = null;
            try {
                updateLock.lock();
                updateExecuteResult = updateMap;
                updateMap = new HashMap<String, String>();
            } finally {
                updateLock.unlock();
            }
            // 将更新请求发动到ES
            if (null != updateExecuteResult && !updateExecuteResult.isEmpty()) {
                sendUpdateRequest(updateExecuteResult);
            }
        } catch (Exception e) {
            log.error("push to es update failed");
            log.error("printstack:", e);
        }
    }

    /**
     * 批量存储es数据
     *
     * @param dataList
     */
    private void batchSaveEs(List<Object> dataList) {
//        SqlExecutor executor = getSqlExecutor();
//        for (Object result : dataList) {
//            executor.insert(result);
//        }
//        int r = executor.commit();
//        if (r < 0) {// 存储es失败，进行失败处理
//            log.info("batch insert to es err {} .", r);
//            esFaildPool.execute(() -> faildSaveEsOpt(dataList));
//        } else {
//            if ("true".equals(sendKafka)) {// 存储成功后发送kafka（交通）
//                kafkaServicePool.execute(() -> sendKafka(dataList));
//            }
//        }
    }

    /**
     * 存储es失败处理
     *
     * @param dataList
     */
    private void faildSaveEsOpt(List<Object> dataList) {
        String ip = elasticSearchClusterIpSchedule.getEsIp();
        if (StringUtils.isEmpty(ip)) {// 如果插入失败，且没有可用的ES ip，则不进行重试了
            log.error("No available ES ip");
            if ("true".equals(compensation)) {// 如果开启了补偿机制,则将失败的数据写入文件来记录
                writeFailDataToFile(dataList);
            }
        } else {// 如果插入失败，重试一次，防止因为网络波动等情况导致连接问题
//            log.info("batch insert to es err , retry it.");
//            SqlExecutor executor = getSqlExecutor();
//            for (Object result : dataList) {
//                executor.insert(result);
//            }
//            int r = executor.commit();
//            if (r < 0) {// 存储es失败，进行失败处理
//                log.error("Retry batch insert to es err {} .", r);
//                if ("true".equals(compensation)) {// 如果开启了补偿机制,则将失败的数据写入文件来记录
//                    writeFailDataToFile(dataList);
//                }
//            } else {
//                if ("true".equals(sendKafka)) {// 存储成功后发送kafka（交通）
//                    kafkaServicePool.execute(() -> sendKafka(dataList));
//                }
//            }
        }
    }

    /**
     * 将ES入库失败的数据写入文件中，待后面进行补偿
     *
     * @param list 入库list
     */
    private void writeFailDataToFile(List<Object> list) {
        if (list != null && list.size() > 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            File path = new File(failPath);
            if (!path.exists()) {// 如果目录不存在，则创建
                path.mkdirs();
            }
            String failFile = failPath + format.format(new Date()) + Thread.currentThread().getId();// 完整文件名，文件夹目录+yyyyMMddHHmmssSSS+线程id
            File file = new File(failFile);
            try {
                file.createNewFile();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));

                boolean flag = false;// 是否存在有效需要补偿得数据，默认false没有
                for (Object obj : list) {
                    if (obj instanceof FdfsCapacityResult) {// 如果是统计FDFS容量得数据，则一定不要补偿了，否则会覆盖最新容量数据
                        continue;
                    } else {
                        if (flag) {// 如果第一次，那么肯定是false，这个时候是不需要newLine的，如果是第二次开始，肯定是true，必须先newLine
                            bw.newLine();
                        }
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(obj.getClass().getSimpleName(), obj);
                        bw.write(jsonObject.toJSONString());// 一条数据一行
                        flag = true;
                    }
                }
                bw.close();
                if (!flag) {// 如果没有有效得数据写入，为了避免空文件得生成，直接干掉
                    file.delete();
                }
            } catch (Exception e) {
                log.error("write es fail data to file faild. ", e);
            }
        }
    }

    private Producer getProducerInstance() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", kafkaAddress);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("acks", "all");
        properties.put("retries", 3);
        properties.put("batch.size", 16384);
        properties.put("linger.ms", 100);
        properties.put("buffer.memory", 33554432);
        try {
            producer = (producer == null ? new KafkaProducer<String, String>(properties) : producer);
        } catch (Exception e) {
            log.error("can not connect to kafka.", e);
        }
        return producer;
    }

    /**
     * 交通四类数据要求在存储ES成功后发送kafka
     *
     * @param dataList
     */
    private void sendKafka(List<Object> dataList) {
        Producer producer = getProducerInstance();
        for (Object obj : dataList) {
            try {
                if (obj instanceof VlprResult) {
                    String jsonString =
                            jsonConvertUtil.generatorQueryResponse((VlprResult) obj, "MotorVehicleObject").toJSONString();
                    producer.send(new ProducerRecord(motorVehicleTopic, jsonString));

                } else if (obj instanceof ViolationResult) {
                    String jsonString = generatorQueryResponse(obj, "ViolationObject").toJSONString();
                    producer.send(new ProducerRecord(violationTopic, jsonString));

                } else if (obj instanceof EventResult) {
                    String jsonString = generatorQueryResponse(obj, "EventObject").toJSONString();
                    producer.send(new ProducerRecord(eventTopic, jsonString));

                } else if (obj instanceof VehicleflowrateResult) {
                    String jsonString = generatorQueryResponse(obj, "VehicleflowrateObject").toJSONString();
                    producer.send(new ProducerRecord(vehicleflowrateTopic, jsonString));
                }
            } catch (Exception e) {
                log.error("send kafka error: ", obj);
                log.error("send kafka exception: ", e);
            }

        }
    }

    private JSONObject generatorQueryResponse(Object object, String responseObjectName) {
        JSONObject responseObject = new JSONObject();
        responseObject.put(responseObjectName, object);

        return responseObject;
    }

    @Override
    public String update(Map<String, String> updatemap) {
        updateLock.lock();
        try {
            updateMap.putAll(updatemap);
        } finally {
            updateLock.unlock();
        }
        return ResponseUtil.createSuccessResponse("", "update").toJSONString();
    }

    private void sendUpdateRequest(Map<String, String> updateMap) {
        String url = "http://" + elasticSearchClusterIpSchedule.getEsIp() + ":" + port + "/_bulk?pretty";
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("Authorization", auth);
        StringBuilder requestBody = new StringBuilder();
        for (Entry updateRequest : updateMap.entrySet()) {
            requestBody.append(updateRequest.getValue());
        }
        String response = HttpClientUtil2.post(url, requestBody.toString(), header);
        JSONObject responseObject = JSON.parseObject(response);
        JSONArray items = responseObject.getJSONArray("items");
        int size = items.size();
        List<String> failedList = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            JSONObject updateObject = items.getJSONObject(i).getJSONObject("update");
            if (!"200".equals(updateObject.getString("status"))) {
                String id = updateObject.getString("_id");
                log.info("update failed request id is {}", id);
                failedList.add(updateMap.get(id));
            }
        }
        service.schedule(() -> pushFailedUpdateRequest(failedList), 10, TimeUnit.SECONDS);
    }

    private void pushFailedUpdateRequest(List<String> failedList) {
        String url = "http://" + host + ":" + port + "/_bulk?pretty";
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("Authorization", auth);
        StringBuilder requestBody = new StringBuilder();

        Iterator iterator = failedList.iterator();
        while (iterator.hasNext()) {
            String request = (String) iterator.next();
            requestBody.append(request);
        }
        log.info("execute failed request {}", requestBody);
        HttpClientUtil2.post(url, requestBody.toString(), header);
    }

    @Override
    public <T> List<T> query(String idKey, String idValue, Class<T> tClass) {
//        Select select = getSelect(tClass);
//        return select.addWhereCause(idKey, SqlCompare.EQ, idValue).list();
        return null;
    }

    @Override
    public <T> Map<Integer, List<T>> batchQuery(Map<String, String> map, Class<T> tClass) {
        int pageRecordNumber = DEFAULT_PAGE_RECORD;
        int pageStartNumber = DEFAULT_PAGE_START_NUMBER;
        // log.debug("#######################start inner batch query");
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();


        for (Map.Entry<String, String> entry : map.entrySet()) {
            String[] parametorKeyArray = covertKeyToDomainMember(entry.getKey(), tClass);
            String key = parametorKeyArray[0];
            String operation = parametorKeyArray[1];
            String value = entry.getValue();
            if ("PageRecordNum".equalsIgnoreCase(key)) {
                pageRecordNumber = getPageNum(value);
            } else if ("RecordStartNo".equalsIgnoreCase(key)) {
                pageStartNumber = Integer.valueOf(value);
            } else if ("Like".equals(operation)) {
                if (("PLATENO".equals(key.toUpperCase())) || "PLATELICENCE".equals(key.toUpperCase())) {
                    boolQueryBuilder.queryName(value);
                }
            } else if ("In".equals(operation)) {
                Object[] valueArray = getInOpeartionVlaueArray(value);
                boolQueryBuilder.should(new RangeQueryBuilder(key).from(valueArray[0]).to(valueArray[1]));
            } else {
                throw new VideoException(operation + " is not support");
            }
        }
        // log.debug("#######################start query ES");
        String index = tClass.getName();
        SearchResponse searchResponse = elasticSearchService.searchByQueryBuilder(boolQueryBuilder, index, "", 1, 1000, "");
        SearchHits hits = searchResponse.getHits();
        List<T> list = new ArrayList<>();
        Arrays.stream(hits.getHits()).forEach(hit -> {
            T t = JSON.parseObject(hit.getSourceAsString(), tClass);
            list.add(t);
        });
        Page page = new Page(pageRecordNumber, pageStartNumber);
        page.setTotal(hits.getTotalHits());
        // log.debug("#######################end query ES");
        Map<Integer, List<T>> resultMap = new HashedMap();
        resultMap.put((int) page.getTotal(), list);
        // log.debug("#######################end inner batch query");
        return new HashMap<>();
    }

    @Override
    public <T> long batchDelete(Map<String, String> map, String timeName, Date startTime, Date endTime,
                                Class<T> tClass) {
//        Delete<T> delete = getDelete(tClass);
//        for (Entry<String, String> entry : map.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            if (!StringUtils.isEmpty(value)) {
//                delete.addWhereCause(key, SqlCompare.EQ, value);
//            }
//        }
//        if (null != startTime && null != endTime) {
//            delete.addWhereCause(timeName, SqlCompare.LESS, endTime);
//            delete.addWhereCause(timeName, SqlCompare.MORE_EQ, startTime);
//        }
//        return delete.commit();
        return 0;
    }

    @Override
    public <T> long delete(String key, String value, Class<T> tClass) {
//        Delete<T> delete = getDelete(tClass);
//        delete.addWhereCause(key, SqlCompare.EQ, value);
//        return delete.commit();
        return 0;
    }


    private static String[] removePrefix(String key) {
        String[] keyArray = key.split("\\.");
        if (keyArray.length < 2) {
            throw new QueryException(key + " is not correct");
        }
        String[] parameterKeyArray = new String[2];
        parameterKeyArray[0] = keyArray[1];
        if (keyArray.length >= 3) {
            parameterKeyArray[1] = keyArray[2];
        }
        return parameterKeyArray;
    }

    private <T> String[] covertKeyToDomainMember(String key, Class<T> tclass) {
        String[] parameterKeyArray = removePrefix(key);
        String convertedKey = null;
        if (!"PageRecordNum".equals(parameterKeyArray[0]) && !"RecordStartNo".equals(parameterKeyArray[0])) {
            convertedKey = ClassUtil.getFieldStr(parameterKeyArray[0], tclass);
        } else {
            convertedKey = parameterKeyArray[0];
        }
        if (StringUtils.isEmpty(convertedKey)) {
            throw new VideoException(key + " is not the member");
        }
        parameterKeyArray[0] = convertedKey;
        return parameterKeyArray;
    }

    private <T> Object getValueType(String key, Class<T> tClass, String value) {
        Field field = null;
        try {
            field = tClass.getDeclaredField(key);
        } catch (NoSuchFieldException e) {
            // log.debug("", e);
        }
        if (null == field) {
            try {
                Class<?> superClazz = tClass.getSuperclass();
                field = superClazz.getDeclaredField(key);
            } catch (NoSuchFieldException e) {
                throw new VideoException(e.getMessage());
            }
        }

        AnnotatedType type = field.getAnnotatedType();
        String typeName = type.getType().getTypeName();

        Object result;
        if ("java.util.Date".equals(typeName)) {
            result = DateUtil.parseDate(value);
        } else if ("java.lang.Long".equals(typeName)) {
            result = new Long(value);
        } else if ("java.lang.Integer".equals(typeName)) {
            result = new Integer(value);
        } else if ("java.lang.Float".equals(typeName)) {
            result = new Float(value);
        } else if ("java.lang.Double".equals(typeName)) {
            result = new Double(value);
        } else {
            throw new VideoException("this type can not compare");
        }

        return result;
    }

    private int getPageNum(String value) {
        int number = Integer.parseInt(value);
        if (number > MAX_PAGE_RECORD) {
            number = MAX_PAGE_RECORD;
        }
        return number;
    }

    private String[] getInOpeartionVlaueArray(String value) {
        String[] valueArray = value.split(",");
        if (valueArray.length < 1) {
            throw new VideoException("IN opeartion need more value");
        }
        return valueArray;
    }
}