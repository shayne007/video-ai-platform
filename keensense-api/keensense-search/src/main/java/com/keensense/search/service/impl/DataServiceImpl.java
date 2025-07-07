package com.keensense.search.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.common.exception.VideoException;
import com.keensense.search.config.ArchiveRedisConfig;
import com.keensense.search.domain.FaceResult;
import com.keensense.search.domain.NonMotorVehiclesResult;
import com.keensense.search.domain.PersonResult;
import com.keensense.search.domain.Result;
import com.keensense.search.domain.VlprResult;
import com.keensense.search.feign.FeignToArchive;
import com.keensense.search.repository.AlarmRepository;
import com.keensense.search.repository.DocumentRepository;
import com.keensense.search.repository.ImageAnalysisRepository;
import com.keensense.search.repository.StructuringDataRepository;
import com.keensense.search.service.DataService;
import com.keensense.search.service.search.ImageSearchService;
import com.keensense.search.tool_interface.ParameterCheck;
import com.keensense.search.utils.EsQueryUtil;
import com.keensense.search.utils.JsonConvertUtil;
import com.keensense.search.utils.KafkaUtil;
import com.keensense.search.utils.ParametercheckUtil;
import com.keensense.search.utils.ResponseUtil;
import com.keensense.search.utils.Utils;
import com.loocme.sys.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import static com.keensense.search.constant.ParameterChcekConst.checkParameter;

/**
 * Created by zhanx xiaohui on 2019-05-22.
 */
@Slf4j
@RefreshScope
@Service
public abstract class DataServiceImpl implements DataService {

    @Value("${face.feature.firm}")
    private int firm;
    @Value("${face.feature.length}")
    private int featureLength;
    @Value("${face.feature.type}")
    private String featureType;
    @Value("${face.threshold}")
    private float faceThreshold;
    @Value("${face.quality}")
    private float faceQuality;
    @Value("${face.pitch.range.min}")
    private float pitchMin;
    @Value("${face.pitch.range.max}")
    private float pitchMax;
    @Value("${face.yaw.range.min}")
    private float yawMin;
    @Value("${face.yaw.range.max}")
    private float yawMax;
    @Value("${docuemnt.service.exist}")
    private String isExist;
    @Value("${kafka.bootstrap}")
    private String kafkaBrokerList;
    @Value("${archive.kafka.topic.firm.send}")
    private String kafkaTopicFirmSend;
    @Value("${archive.kafka.groupId.firm.send}")
    private String kafkaGroupIdFirmSend;
    @Value("${send.to.feature.save}")
    private String featureSave;//是否存储搜图模块（是否使用搜图模块）
    @Value("${send.to.redis.alarm}")
    private String sendRedisAlarm;//入库后，结构数据（目前只人脸和机动车的部分属性）是否存入redis（供布控告警模块使用），默认true

    @Autowired
    protected JsonConvertUtil jsonConvertUtil;
    @Resource(name = "${structuringData.repository}")
    protected StructuringDataRepository structuringDataRepository;
    @Autowired
    protected AlarmRepository alarmRepository;
    @Autowired
    protected ImageSearchService imageSearchService;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private ImageAnalysisRepository imageAnalysisRepository;
    @Autowired
    private EsQueryUtil esQueryUtil;
    @Autowired
    FeignToArchive feignToArchive;
    @Autowired
    ArchiveRedisConfig archiveRedisConfig;
    private static final String SPLIT_STR = "#@#";


    private static final String FEATURE_OBJECT = "FeatureObject";

    /**
     * 批量往视图库中插入数据
     *
     * @param jsonObject         请求消息中的json
     * @param jsonObjectName     请求消息中承载数据的object名称
     * @param jsonArrayName      请求消息中承载数据的objectArray名称
     * @param idName             数据中表示ID的字段的名称
     * @param responseObjectName 返回消息中外层的objete的名称
     * @param objectType         数据类型
     * @param objectTypeName     数据类型名称
     * @param appearTimeName     数据中表示出现时间的字段的名称
     * @param disappearTimeName  数据中表示消失时间的字段的名称
     * @param tClass             实体类的class类型，便于后面做转换
     * @param <T>                实体类的类型，需要继承与Result
     * @return 返回结果的object
     */
    protected <T extends Result> JSONObject batchInsert(JSONObject jsonObject,
                                                        String jsonObjectName,
                                                        String jsonArrayName, String idName, String responseObjectName,
                                                        int objectType, String objectTypeName,
                                                        String appearTimeName, String disappearTimeName,
                                                        Class<T> tClass) {
        //log.debug("begin to batch insert {}", jsonObject);
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String url = request.getRequestURL().toString();
        //由于1400标准的反人类，在端到端里面改了数据的格式
        //为了支持端到端里的格式和标准的1400格式，使用这个方法来获取承载数据的array
        JSONArray listObject = getObjectList(jsonObject, jsonObjectName, jsonArrayName);
        jsonObject = null;
        JSONObject responseObject = new JSONObject();
        JSONArray responseStatusArray = new JSONArray();
        String typeID = "";
        String ip = Utils.getIpAddress(request);
        JSONObject object = null;
        //循环处理请求中的多个对象
        for (int i = 0; i < listObject.size(); i++) {
            try {
                object = listObject.getJSONObject(i);
                typeID = object.getString(idName);
                log.info("handler data {}, the id is {}, ip is {}", tClass.getSimpleName(), typeID, ip);

                //将json转换为实体类，然后分别往不同地方存储图片、特征、以及实体类
                convertJson2Result(object, idName,
                        objectType,
                        objectTypeName, responseObjectName,
                        appearTimeName, disappearTimeName, tClass);

                responseStatusArray.add(ResponseUtil.createSuccessResponse(typeID, url));
            } catch (VideoException e) {
                log.error("handler request failed,because {},the request is {}", e.getMessage(),
                        object);
                responseStatusArray.add(ResponseUtil
                        .createFailedResponse(typeID, url, ResponseUtil.STATUS_CODE_FAILED,
                                e.getMessage()));
            } catch (Exception e) {
                log.error("handler request failed,the request is {}",
                        object, e);
                responseStatusArray.add(ResponseUtil
                        .createFailedResponse(typeID, url, ResponseUtil.STATUS_CODE_FAILED,
                                e.getMessage()));
            }
        }
        responseObject.put("ResponseStatusObject", responseStatusArray);
        JSONObject response = new JSONObject();
        response.put("ResponseStatusListObject", responseObject);
        //log.debug("end to batch insert {}, the response is {}", jsonObjectName, responseObject.toJSONString());
        return response;
    }

    /**
     * 根据ID进行单条数据的查询
     *
     * @param idName             表示ID的字段的名字
     * @param id                 要查询的id
     * @param responseObjectName 返回json中外层json的名称
     * @param tClass             实体类的class
     * @param <T>                实体类，必须继承与Result
     * @return 返回查询到的数据，未查询到为空
     */
    protected <T extends Result> String query(String idName, String id, String responseObjectName,
                                              Class<T> tClass) {
        //log.debug("begin to query face");
        try {
            if (StringUtils.isEmpty(id)) {
                throw new VideoException("id is empty");
            }
            String jsonString = null;
            List<T> list = structuringDataRepository.query(idName, id, tClass);
            jsonString = jsonConvertUtil.generatorQueryResponse(list.get(0), responseObjectName)
                    .toJSONString();
            //log.debug("end to query face, the response is {}", jsonString);
            return jsonString;
        } catch (Exception e) {
            log.error("", e);
        }
        return jsonConvertUtil.generatorEmptyQueryResponse(responseObjectName).toJSONString();
    }

    /**
     * 批量查询
     *
     * @param parameterMap   请求参数的map。
     * @param objectName     返回json中的外层object名称
     * @param listObjectName 返回json中的外层array的名称
     * @param tClass         实体类的class
     * @param <T>            实体类，必须继承与Result
     * @return 返回查询到数据
     */
    protected <T extends Result> String batchQuery(Map<String, String[]> parameterMap,
                                                   String objectName, String listObjectName, Class<T> tClass) {
        //log.debug("begin to batch query face");
        try {
            Map<String, String> map = jsonConvertUtil
                    .covertInputParameterToResultParameter(parameterMap);

            JSONObject responseObject;
            Map<Integer, List<T>> resultMap = structuringDataRepository
                    .batchQuery(map, tClass);
            Entry<Integer, List<T>> entry = resultMap.entrySet().iterator().next();
            int count = entry.getKey();
            List<T> results = entry.getValue();
            responseObject = jsonConvertUtil
                    .generatorBatchQueryResponse(results, count, objectName, listObjectName);
            //log.debug("end to batch query face, the response is {}", responseObject);
            return responseObject.toJSONString();
        } catch (Exception e) {
            log.error("", e);
        }
        return jsonConvertUtil.generatorEmptyResponse(objectName, listObjectName)
                .toJSONString();
    }

    protected String update(JSONObject object, String jsonObjectName,
                            String jsonListObjectName,
                            String idName, Class tclass) {
        try {
            log.info("update json is {}", object);
            JSONArray array = getObjectList(object, jsonObjectName, jsonListObjectName);
            Map inputMap = transferJson2Map(array, idName, tclass);
            Map<String, String> updateDataList = generateBulkJson(inputMap, tclass);
            return structuringDataRepository.update(updateDataList);
        } catch (Exception e) {
            log.error("{}", e.getMessage());
            return ResponseUtil.createFailedResponse("", "", 500, e.getMessage()).toJSONString();
        }
    }

    /**
     * 将单个json转换成实体类，并存储到视图库
     *
     * @param object             单个json数据
     * @param idName             数据中表示ID的字段的名称
     * @param objType            数据的类型 1：人、2：车、3：人脸、4：人骑车
     * @param objTypeName        数据的类型名称 1：人、2：车、3：人脸、4：人骑车
     * @param responseObjectName 返回参数中的外层object名称
     * @param appearTimeName     数据中表示出现时间的字段的名称
     * @param disappearTimeName  数据中表示消失时间的字段的名称
     * @param tClass             实体类的class类型，便于后面做转换
     * @param <T>                实体类的类型，需要继承与Result
     */
    @Async
    protected <T extends Result> void convertJson2Result(JSONObject object, String idName,
                                                         int objType,
                                                         String objTypeName, String responseObjectName,
                                                         String appearTimeName, String disappearTimeName, Class<T> tClass) {
        //log.debug("begin to covert nonMotorVehicle json");
        Result result = getPrivate(object, objType, tClass);
        ParametercheckUtil.checkEmpty(object, idName);

        Integer featureFirm = object.getJSONObject(FEATURE_OBJECT).getInteger("Firm");
        featureFirm = featureFirm == null ? 0 : featureFirm;
        if (tClass == FaceResult.class && firm != featureFirm) {
            /**
             * 上传图片到图片服务器，并将图片的url存入结果。由于AppearTime与DisappearTime参数在各个里面都不相同。
             * 所有需要将该参数在各个类型里面的名称传入
             */
            log.info("send request to picturestream, id is {}", result.getId());
            jsonConvertUtil.getImage(object, result, objTypeName,
                    appearTimeName, disappearTimeName);
            //将装换后的对象存储到es中（picturestream分析提取特征还会再存一次，但是因为id相同，会覆盖；之所以要存两次是为了防止，分析提取特征失败，导致入库失败，那么这条数据就丢失了）
//            structuringDataRepository.save(result);//该逻辑还是得取消，因为如果有些图片在GL算法提取不到特征，那么就会造成搜图没数据，但是ES有数据，虽然不会造成丢图现象，但是会导致语义搜索有数据，但是以图搜图搜不到原图现象
            //然后将结构化数据放到kafka供picturestream分析提取特征
            JSONObject featureObject = object.getJSONObject(FEATURE_OBJECT);
            featureObject.put("Firm", firm);
            object.put(FEATURE_OBJECT, featureObject);
            JSONObject imageAnalysisRequest = generateImageAnalysisRequest(object, result);
            imageAnalysisRepository.sendRequest(imageAnalysisRequest);
        } else {

            setHandBagParametor(tClass, result);
            setVehicleFrontItem(tClass, result);
            /**
             * 上传图片到图片服务器，并将图片的url存入结果。由于AppearTime与DisappearTime参数在各个里面都不相同。
             * 所有需要将该参数在各个类型里面的名称传入
             */
//            long imageStart = System.currentTimeMillis();
            jsonConvertUtil.getImage(object, result, objTypeName, appearTimeName, disappearTimeName);
//            long imageEnd = System.currentTimeMillis();
            //将装换后的对象存储到es中
            structuringDataRepository.save(result);
//            long esEnd = System.currentTimeMillis();
            //将特征存入搜图模块，并将其他特征相关的信息存入结果
            if ("true".equals(featureSave)) {
                jsonConvertUtil.getFeature(object, result);
            }
            object = null;
//            long featureEnd = System.currentTimeMillis();
            // 判断是否是非千视通算法或者需要进行一人一档流程。  这个是园区的一人一档逻辑，目前不再使用，注释掉
//            if ("true".equalsIgnoreCase(isExist) && (FaceResult.class == tClass
//                || PersonResult.class == tClass)) {
//                sendToDocument(result, tClass);
//            }
//            long docemntEnd = System.currentTimeMillis();
            // 人脸数据推送到kafka进行聚类
            pushArchiveData(result, tClass);
            //如果打开了开关，且是人脸和机动车，将封装后的对象存入到redis中以供告警模块使用
            if ("true".equals(sendRedisAlarm) && (tClass == FaceResult.class || tClass == VlprResult.class)) {
                String key = result.getSerialnumber() + "_" + result.getId();
//                String resultString = jsonConvertUtil.generatorQueryResponse(result, responseObjectName).toJSONString();
                String resultString = sendRedisAlarmData(result, responseObjectName).toJSONString();
                alarmRepository.insert(key, resultString);
            }
//            long redisEnd = System.currentTimeMillis();
//            log.debug("image:{}ms,es:{}ms,feature:{}ms,document:{}ms,redis:{}ms",  imageEnd - imageStart, esEnd - imageEnd, featureEnd - esEnd, docemntEnd - featureEnd, redisEnd - docemntEnd);
//            log.debug("end to covert face json, the result is {}", result);
        }
    }

    /**
     * 分装需要存储到redis供布控告警使用的数据（目前只处理人脸和机动车的）
     *
     * @param result
     * @param responseObjectName
     * @return
     */
    private JSONObject sendRedisAlarmData(Result result, String responseObjectName) {
        JSONObject responseObject = new JSONObject();
        JSONObject object = new JSONObject();
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        object.put("Id", result.getId());
        object.put("ObjType", result.getObjType());
        object.put("Serialnumber", result.getSerialnumber());
        object.put("DeviceID", result.getDeviceId());
        object.put("ImgUrl", result.getImgUrl());
        object.put("BigImgUrl", result.getBigImgUrl());
        object.put("RightBtmY", result.getRightBtmY());
        object.put("RightBtmX", result.getRightBtmX());
        object.put("LeftTopY", result.getLeftTopY());
        object.put("LeftTopX", result.getLeftTopX());
        Date markTime = result.getMarkTime();
        if (markTime != null) {
            object.put("MarkTime", df.format(markTime));
        }
        Date appearTime = result.getAppearTime();
        if (appearTime != null) {
            object.put("AppearTime", df.format(appearTime));
        }
        if ("MotorVehicleObject".equals(responseObjectName)) {
            object.put("VehicleClass", ((VlprResult) result).getVehicleClass());
            object.put("PlateClass", ((VlprResult) result).getPlateClass());
            object.put("PlateNo", ((VlprResult) result).getPlateNo());
            object.put("VehicleStyles", ((VlprResult) result).getVehicleStyles());
            object.put("HasDanger", ((VlprResult) result).getHasDanger());
            object.put("Sun", ((VlprResult) result).getSun());
            object.put("Drop", ((VlprResult) result).getDrop());
            object.put("VehicleModel", ((VlprResult) result).getVehicleModel());
            object.put("PlateColor", ((VlprResult) result).getPlateColor());
            object.put("VehicleBrand", ((VlprResult) result).getVehicleBrand());
            object.put("TagNum", ((VlprResult) result).getTagNum());
            object.put("Angle", ((VlprResult) result).getAngle());
            object.put("VehicleColor", ((VlprResult) result).getVehicleColor());
            object.put("Paper", ((VlprResult) result).getPaper());
            object.put("SafetyBelt", ((VlprResult) result).getSafetyBelt());
            object.put("HasCrash", ((VlprResult) result).getHasCrash());
            object.put("SecondBelt", ((VlprResult) result).getSecondBelt());
            object.put("Calling", ((VlprResult) result).getCalling());
        } else if ("FaceObject".equals(responseObjectName)) {
            object.put("ConnectObjectType", ((FaceResult) result).getConnectObjectType());
            object.put("ConnectObjectId", ((FaceResult) result).getConnectObjectId());
            object.put("FeatureObject", ((FaceResult) result).getFeatureObject());
        }
        responseObject.put(responseObjectName, object);
        return responseObject;
    }

    /**
     * 推送需要聚类数据到kafka && 存储临时数据到redis
     *
     * @param result
     * @param tClass
     * @param <T>
     */
    protected <T extends Result> void pushArchiveData(Result result, Class<T> tClass) {

        if (!("true".equalsIgnoreCase(isExist))) {
            return;
        }

        if (FaceResult.class != tClass) {
            return; //仅推送人脸数据
        }

        FaceResult faceResult = (FaceResult) result;
        Float yaw = faceResult.getYaw();
        Float pitch = faceResult.getPitch();
        //yaw左右  pitch上下
        if (!(pitch >= pitchMin && pitch <= pitchMax && yaw >= yawMin && yaw <= yawMax)) {
            return;// 如果不是正脸图片则不进行推送
        }

        Float quality = faceResult.getFaceQuality();
        if ((null == quality) || (faceQuality > quality)) {
            return;// 只推送人脸质量大于阈值的数据
        }

        String featureObject = result.getFeatureObject();
        String faceID = faceResult.getFaceID();
        String imgUrl = result.getImgUrl();

        // 推送kafka
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArrayData = new JSONArray();
        JSONObject jsonObjectData = new JSONObject();

        //是否大角度(左右45度并且上下30度内为非大角度)
        int angle = 1; //0:非大角度  1:大角度
        double pitchAngle = pitch * 180 / 3.14;
        double yawAngle = yaw * 180 / 3.14;
        if (pitchAngle < 30 && pitchAngle > -30 && yawAngle < 45 && yawAngle > -45) {
            angle = 0;
        }

        jsonObjectData.put("featureVector", featureObject);
        jsonObjectData.put("uuid", faceID);
        jsonObjectData.put("angle", angle);
        jsonArrayData.add(jsonObjectData);

        jsonObject.put("operate", "add");
        jsonObject.put("featureLen", featureLength);
        jsonObject.put("featureType", featureType);
        jsonObject.put("tholdhold", faceThreshold);
        jsonObject.put("data", jsonArrayData);

        try {
            log.info("==== pushArchiveData faceID:" + faceID);
            KafkaUtil.sendMessage(kafkaTopicFirmSend, kafkaGroupIdFirmSend, jsonObject.toJSONString(),
                    kafkaBrokerList);

            // 存储redis
//            long time = result.getStartFramePts() == null ? result.getMarkTime().getTime()
//                : result.getStartFramePts();
            long time = result.getAppearTime() == null ? 0L : result.getAppearTime().getTime();
            String genderCode = faceResult.getGenderCode();
            String deviceId = faceResult.getDeviceId();
            String bigImgUrl = faceResult.getBigImgUrl();
            String value = featureObject + SPLIT_STR + imgUrl + SPLIT_STR + firm + SPLIT_STR + time +
                    SPLIT_STR + quality + SPLIT_STR + genderCode + SPLIT_STR + deviceId + SPLIT_STR + bigImgUrl;
            RedisTemplate redisTemplate = archiveRedisConfig.archiveRedisTemplate();
            redisTemplate.opsForValue().set(faceID, value, archiveRedisConfig.getExpirationTime(), TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("==== push data to cluster exception:", e);
        }
    }

    /**
     * 将json中的数据映射到实体类中，并将某些需要网关插入的数据插入
     *
     * @param object  数据的json
     * @param objType 数据类型1：人、2：车、3：人脸、4：人骑车
     * @param tClass  实体类的class类型，便于后面做转换
     * @param <T>     实体类的类型，需要继承与Result
     * @return 返回转换后的实体类
     */
    protected <T extends Result> T getPrivate(JSONObject object, int objType, Class<T> tClass) {
        for (String str : object.keySet()) {
            checkParameter(tClass, str.toLowerCase(), object.getString(str));
        }
        T result = JSONObject.toJavaObject(object, tClass);
        if (result instanceof ParameterCheck) {
            ((ParameterCheck) result).checkParameter();
        }
        result.setObjType(objType);
        setAnalysisId(result);
        setId(tClass, result);
        setTime(tClass, result);
        return result;
    }

    /**
     * 由于1400标准的反人类，在端到端里面改了数据的格式 为了支持端到端里的格式和标准的1400格式，使用这个方法来获取承载数据的array
     *
     * @param inputObject    请求中的json
     * @param jsonObjectName 外层object名字
     * @param jsonArrayName  外层中的array名字
     * @return 返回获取到的jsonarray
     */
    protected JSONArray getObjectList(JSONObject inputObject, String jsonObjectName,
                                      String jsonArrayName) {
        JSONArray objectArray = null;
        try {
            objectArray = inputObject.getJSONArray(jsonObjectName);
            return objectArray;
        } catch (Exception e) {
            //log.debug("", e);
        }
        objectArray = inputObject.getJSONObject(jsonObjectName)
                .getJSONArray(jsonArrayName);

        return objectArray;
    }

    protected <T extends Result> JSONObject queryByMultiId(JSONObject idObject, String type,
                                                           String objectName,
                                                           String listObjectName, Class<T> tclass) {
        String ids = idObject.getString("IdList");

        Map<String, String> map = new HashMap<>();
        map.put(type + ".Id.In", ids);
        map.put(type + ".RecordStartNo", "1");
        map.put(type + ".PageRecordNum", "3000");
        try {
            Map<Integer, List<T>> resultMap = structuringDataRepository
                    .batchQuery(map, tclass);
            Entry<Integer, List<T>> entry = resultMap.entrySet().iterator().next();
            int count = entry.getKey();
            List<T> results = entry.getValue();
            JSONObject responseObject = jsonConvertUtil
                    .generatorBatchQueryResponse(results, count, objectName, listObjectName);
            //log.debug("end to batch query face, the response is {}", responseObject);
            return responseObject;
        } catch (Exception e) {
            log.error("", e);
        }
        return jsonConvertUtil.generatorEmptyResponse(objectName, listObjectName);
    }

    /**
     * groupBy params
     */
    public String groupByQuery(JSONObject json, String index) {
        return esQueryUtil.groupByQuery(json, index);
    }

    private void sendToDocument(Result result, Class tClass) {
        try {
            JSONObject documentJsonobject;
            if (tClass == FaceResult.class) {
                documentJsonobject = generatorFaceSendToDocumentJson(
                        (FaceResult) result);
            } else {
                documentJsonobject = generatorPersonSendToDocumentJson(
                        (PersonResult) result);
            }
            //log.debug("request json is {}", documentJsonobject);
            documentRepository.getDocument(documentJsonobject);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 生成往扩展服务发送人脸的一人一档请求json
     *
     * @param result 人脸实体对象
     * @return 生成的json
     */
    protected JSONObject generatorFaceSendToDocumentJson(FaceResult result) {
        JSONObject object = new JSONObject();
        object.put("ObjType", result.getObjType());
        object.put("FaceID", result.getFaceID());
        object.put("BodyID", result.getConnectObjectId());
        object.put("DeviceID", result.getDeviceId());
        object.put("FaceImgUrl", result.getImgUrl());
        object.put("FaceQuality", result.getFaceQuality());
        object.put("LeftTopX", result.getLeftTopX());
        object.put("LeftTopY", result.getLeftTopY());
        object.put("RightBtmX", result.getRightBtmX());
        object.put("RightBtmY", result.getRightBtmY());
        object.put("FaceScore", result.getFaceScore());
        object.put("Pitch", result.getPitch());
        object.put("Yaw", result.getYaw());
        object.put("Roll", result.getRoll());
        object.put("FaceFeature", result.getFeatureObject());
        JSONObject requestObject = new JSONObject();
        requestObject.put("ArchivesObject", object);

        return requestObject;
    }

    /**
     * 生成往扩展服务发送人形的一人一档请求json
     *
     * @param result 人脸实体对象
     * @return 生成的json
     */
    protected JSONObject generatorPersonSendToDocumentJson(PersonResult result) {
        JSONObject object = new JSONObject();
        object.put("BodyID", result.getPersonID());
        object.put("FaceID", result.getFaceUUID());
        object.put("DeviceID", result.getDeviceId());
        object.put("ObjType", result.getObjType());
        object.put("BodyImgUrl", result.getImgUrl());
        object.put("BodyQuality", result.getBodyQuality());
        object.put("Angle", result.getAngle());
        object.put("LeftTopX", result.getLeftTopX());
        object.put("LeftTopY", result.getLeftTopY());
        object.put("RightBtmX", result.getRightBtmX());
        object.put("RightBtmY", result.getRightBtmY());
        object.put("BodyScore", result.getBodyScore());
        object.put("BodyFeature", result.getFeatureObject());
        JSONObject requestObject = new JSONObject();
        requestObject.put("ArchivesObject", object);
        return requestObject;
    }


    /**
     * 人脸和人形对象的AppearTime，DisappearTime，MarkTime字段都有前缀，人为给他们增加一个统一的属性值
     *
     * @param tClass class对象
     * @param result 实体对象
     * @param <T>    泛型
     */
    private <T extends Result> void setTime(Class<T> tClass, Result result) {
        if (tClass == PersonResult.class) {
            result.setAppearTime(((PersonResult) result).getPersonAppearTime());
            result.setDisappearTime(((PersonResult) result).getPersonDisAppearTime());
            result.setMarkTime(((PersonResult) result).getLocationMarkTime());
        } else if (tClass == FaceResult.class) {
            result.setAppearTime(((FaceResult) result).getFaceAppearTime());
            result.setDisappearTime(((FaceResult) result).getFaceDisAppearTime());
            result.setMarkTime(((FaceResult) result).getLocationMarkTime());
        }
    }

    private <T extends Result> void setHandBagParametor(Class<T> tClass, Result result) {
        if (tClass == PersonResult.class && ((PersonResult) result).getCarryBag() != null) {
            ((PersonResult) result).setHandbag(((PersonResult) result).getCarryBag());
        }
    }

    private <T extends Result> void setVehicleFrontItem(Class<T> tClass, Result result) {
        if (VlprResult.class != tClass) {
            return;
        }
        String vehicleFrontItem = ((VlprResult) result).getVehicleFrontItem();
        if (StringUtils.isEmpty(vehicleFrontItem)) {
            return;
        }
        for (String item : vehicleFrontItem.split(",")) {
            if ("1".equals(item)) {
                ((VlprResult) result).setTag(1);
            } else if ("3".equals(item)) {
                ((VlprResult) result).setDrop(1);
            } else if ("4".equals(item)) {
                ((VlprResult) result).setSun(1);
            } else if ("6".equals(item)) {
                ((VlprResult) result).setDecoration(1);
            } else if ("7".equals(item)) {
                ((VlprResult) result).setPaper(1);
            } else if ("10".equals(item)) {
                ((VlprResult) result).setSunRoof(1);
            } else if ("11".equals(item)) {
                ((VlprResult) result).setRack(1);
            } else if ("12".equals(item)) {
                ((VlprResult) result).setAerial(1);
            }
        }
    }

    /**
     * 使用属性中的FaceID，PersonID，MotorVehicleID，NonMotorVehicleID替换es中的主键
     *
     * @param tClass class对象
     * @param result 实体对象
     * @param <T>    泛型
     */
    private <T extends Result> void setId(Class<T> tClass, Result result) {
        if (!StringUtils.isEmpty(result.getId())) {
            return;
        }
        if (tClass == PersonResult.class) {
            result.setId(((PersonResult) result).getPersonID());
        } else if (tClass == FaceResult.class) {
            result.setId(((FaceResult) result).getFaceID());
        } else if (tClass == VlprResult.class) {
            result.setId(((VlprResult) result).getMotorVehicleID());
        } else if (tClass == NonMotorVehiclesResult.class) {
            result.setId(((NonMotorVehiclesResult) result).getNonMotorVehicleID());
        }
    }

    private void setAnalysisId(Result result) {
        if (StringUtils.isEmpty(result.getAnalysisId())) {
            result.setAnalysisId(result.getSerialnumber());
        }
    }

    private Map<String, JSONObject> transferJson2Map(JSONArray array, String idName, Class tclass) {
        int size = array.size();
        Map<String, JSONObject> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            JSONObject object = array.getJSONObject(i);
            String id = object.getString(idName);
            JSONObject data = object.getJSONObject("Data");
            JSONObject newData = new JSONObject();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                String key = entry.getKey().toLowerCase();
                if (ParameterCanNotUpdate(key)) {
                    continue;
                }
                Object value = transferValue(key, entry.getValue() == null ? null : entry.getValue().toString());
                checkParameter(tclass, key, value);
                newData.put(key, value);
            }
            map.put(id, newData);
        }
        return map;
    }

    public boolean ParameterCanNotUpdate(String key) {
        if ("MotorVehicleID".equalsIgnoreCase(key) || "Id".equalsIgnoreCase(key) ||
                "FeatureObject".equalsIgnoreCase(key) || "SubImageList".equalsIgnoreCase(key) ||
                "ImgUrl".equalsIgnoreCase(key) || "BigImgUrl".equalsIgnoreCase(key) ||
                "faceID".equalsIgnoreCase(key) || "PersonID".equalsIgnoreCase(key) ||
                "NonMotorVehicleID".equalsIgnoreCase(key)) {
            return true;
        }
        return false;
    }

    private Object transferValue(String key, Object value) {
        Object transferValue = value;
        if ("marktime".equalsIgnoreCase(key) || "appeartime".equalsIgnoreCase(key) ||
                "disappeartime".equalsIgnoreCase(key) || "inserttime".equalsIgnoreCase(key) ||
                "createtime".equalsIgnoreCase(key) || "locationmarktime".equalsIgnoreCase(key) ||
                "faceappeartime".equalsIgnoreCase(key) || "facedisappeartime".equalsIgnoreCase(key) ||
                "personappeartime".equalsIgnoreCase(key) || "persondiskappeartime".equalsIgnoreCase(key) ||
                "passtime".equalsIgnoreCase(key)) {
            transferValue = DateUtil.getDate(value.toString()).getTime();
        }

        return transferValue;
    }


    private String generateBulkUpdateRequest(List<JSONObject> list) {
        StringBuffer buffer = new StringBuffer();
        for (JSONObject object : list) {
            buffer.append(object.toJSONString() + "\n");
        }
        return buffer.toString();
    }

    private Map<String, String> generateBulkJson(Map<String, JSONObject> map, Class tClass) {
        Map<String, String> updateMap = new HashMap<>(map.entrySet().size());
        String index = getElasticSearchIndex(tClass);
        for (Entry<String, JSONObject> entry : map.entrySet()) {
            StringBuilder builder = new StringBuilder();
            String key = entry.getKey();
            JSONObject value = entry.getValue();
            JSONObject object = new JSONObject();
            JSONObject update = new JSONObject();
            update.put("_index", index);
            update.put("_type", "data");
            update.put("_id", key);
            object.put("update", update);
            builder.append(object.toJSONString() + "\n");
            JSONObject doc = new JSONObject();
            doc.put("doc", value);
            builder.append(doc.toJSONString() + "\n");
            updateMap.put(key, builder.toString());
        }
        return updateMap;
    }

    private String getElasticSearchIndex(Class tClass) {
        if (tClass == FaceResult.class) {
            return "face_result";
        } else if (tClass == PersonResult.class) {
            return "objext_result";
        } else if (tClass == NonMotorVehiclesResult.class) {
            return "bike_result";
        } else if (tClass == VlprResult.class) {
            return "vlpr_result";
        }
        return null;
    }

    private JSONObject generateImageAnalysisRequest(JSONObject object, Result result) {
        SimpleDateFormat dateSdf = new SimpleDateFormat("yyyyMMddHHmmss");
        JSONObject requestObject = new JSONObject();
        requestObject.put("intercept_flag", "false");
        requestObject.put("picture_type", "1");
        requestObject.put("device_id", result.getDeviceId());
        requestObject.put("serial_number", result.getSerialnumber());
        requestObject.put("frame_img", result.getImgUrl());
        requestObject.put("enter_time",
                result.getAppearTime() == null ? null : dateSdf.format(result.getAppearTime()));
        requestObject.put("capture_time",
                result.getMarkTime() == null ? null : dateSdf.format(result.getMarkTime()));
        requestObject.put("leave_time",
                result.getDisappearTime() == null ? null : dateSdf.format(result.getDisappearTime()));
        requestObject.put("keensense_flag", "true");
        requestObject.put("recog_type", "8");

        requestObject.put("ext", object);
        return requestObject;
    }
}


/**
 * @program: platform
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-05-22 15:21
 **/