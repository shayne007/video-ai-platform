package com.keensense.picturestream.output.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.common.config.SpringContext;
import com.keensense.picturestream.common.ResultSendThreads;
import com.keensense.picturestream.common.VlprCommonConst;
import com.keensense.picturestream.config.NacosConfig;
import com.keensense.picturestream.entity.PictureInfo;
import com.keensense.picturestream.feign.IFeignService;
import com.keensense.picturestream.feign.IFeignToSearch;
import com.keensense.picturestream.util.ClassType;
import com.keensense.picturestream.util.IDUtil;
import com.keensense.picturestream.util.ImageBaseUtil;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.Mat;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by memory_fu on 2019/7/15.
 */
@Slf4j
public class ResultSendKeensense {

    private static NacosConfig nacosConfig = SpringContext.getBean(NacosConfig.class);

    private IFeignToSearch feignToSearch = SpringContext.getBean(IFeignToSearch.class);
    private IFeignService feignService = SpringContext.getBean(IFeignService.class);

    private static final String DATA_TYPE_PERSON = "1";
    private static final String DATA_TYPE_CAR = "2";
    private static final String DATA_TYPE_FACE = "3";
    private static final String DATA_TYPE_BIKE = "4";
    private static final String DATE_YMDHMS = "yyyyMMddHHmmss";
    private static final String DATE_Y_M_DHMS = "yyyy_MM_ddHHmmss";

    public boolean receiveData(List<PictureInfo> pictureInfos) {
        CompletableFuture[] cfs = pictureInfos.stream()
                .map(pictureInfo -> CompletableFuture.supplyAsync(() -> send(pictureInfo)).exceptionally(e->false))
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(cfs).join();
        return true;
    }


    private boolean send(PictureInfo pictureInfo) {
        Map<String, List<JSONObject>> resultMap = new HashMap<>();//处理中间结果
        resultMap.put(DATA_TYPE_PERSON, new ArrayList<>());
        resultMap.put(DATA_TYPE_CAR, new ArrayList<>());
        resultMap.put(DATA_TYPE_FACE, new ArrayList<>());
        resultMap.put(DATA_TYPE_BIKE, new ArrayList<>());

        try {
            //处理数据
            JSONObject handleReceiveDataResult = handleReceiveData(pictureInfo, resultMap);

            //推送keensense
            log.info("keensense data List__________:" + JSONObject.toJSONString(handleReceiveDataResult));
            pushDataToKeensense(handleReceiveDataResult);
            return true;
        } catch (Exception e) {
            log.error("====receiveData Exception:", e);
            return false;
        }
    }

    private JSONObject handleReceiveData(PictureInfo pictureInfo,
                                         Map<String, List<JSONObject>> resultMap) {
        List<Integer> recogTypeList = pictureInfo.getRecogTypeList();
        List<Map<String,Object>> varList = pictureInfo.getResults();

        if (CollectionUtils.isEmpty(varList)) {
            ResultSendThreads.sendError(pictureInfo);
        }

        for (Map<String,Object> var : varList) {
            log.info("Var data______:" + var.toString());
            JSONObject jsonObject = JSONObject.parseObject(var.toString());
            int algoSource = jsonObject.getIntValue("AlgoSource");

            // 数据分流
            if (algoSource == 0 || algoSource == 1 || algoSource == 2) {
                JSONObject metadata = jsonObject.getJSONObject("Metadata");
                String type = metadata.getString("Type");
                JSONObject handleResult = new JSONObject();

                switch (type) {
                    case DATA_TYPE_PERSON:
                        handleResult = analysisPersonDataNew(jsonObject, metadata, type, pictureInfo);
                        break;
                    case DATA_TYPE_CAR:
                        handleResult = analysisCarDataNew(jsonObject, metadata, type, pictureInfo);
                        break;
                    case DATA_TYPE_FACE:
                        // qst算法人脸
                        break;
                    case DATA_TYPE_BIKE:
                        handleResult = analysisBikeDataNew(jsonObject, metadata, type, pictureInfo);
                        break;
                    default:
                        break;
                }
                log.info("handleResult data_______:" + handleResult.toJSONString());

                judgeData(recogTypeList, type, handleResult, resultMap);
            }

            if (algoSource == 4) {
                //glst车辆
                List<JSONObject> carDataGlst = analysisCarDataGlst(jsonObject, pictureInfo);
                log.info("List<JSONObject> carDataGlst_____: " + JSONObject.toJSONString(carDataGlst));

                List<JSONObject> resultList = resultMap.get(DATA_TYPE_CAR);
                resultList.addAll(carDataGlst);
                resultMap.put(DATA_TYPE_CAR, resultList);
            }

            if (algoSource == 8) {
                List<JSONObject> faceDatas = resultMap.get(DATA_TYPE_FACE);
                JSONObject faceData = analysisFaceData(jsonObject, pictureInfo);
                faceDatas.add(faceData);
                updateFaceInfo(faceDatas, pictureInfo);
                resultMap.put(DATA_TYPE_FACE, faceDatas);
            }
        }

        JSONObject handleReceiveDataResult = assembleResult(connectIdByPersonAndFace(resultMap));
        return handleReceiveDataResult;
    }

    private void updateFaceInfo(List<JSONObject> faceDatas, PictureInfo pictureInfo) {

        if (!pictureInfo.getKeensenseFlag()) {
            return;
        }

        String pictureInfoExt = pictureInfo.getExt();
        if (StringUtils.isEmpty(pictureInfoExt)) {
            return;
        }

        JSONObject pictureInfoExtObject = JSONObject.parseObject(pictureInfoExt);

        for (JSONObject jsonObject : faceDatas) {
            pictureInfoExtObject.put("LeftTopX", jsonObject.get("LeftTopX"));
            pictureInfoExtObject.put("LeftTopY", jsonObject.get("LeftTopY"));
            pictureInfoExtObject.put("RightBtmX", jsonObject.get("RightBtmX"));
            pictureInfoExtObject.put("RightBtmY", jsonObject.get("RightBtmY"));

            pictureInfoExtObject.put("FaceQuality", jsonObject.get("FaceQuality"));
            pictureInfoExtObject.put("Blurry", jsonObject.get("Blurry"));
            pictureInfoExtObject.put("Roll", jsonObject.get("Roll"));
            pictureInfoExtObject.put("Yaw", jsonObject.get("Yaw"));
            pictureInfoExtObject.put("Pitch", jsonObject.get("Pitch"));
            pictureInfoExtObject.put("Rect", jsonObject.get("Rect"));
            pictureInfoExtObject.put("GlassStyle", jsonObject.get("Glass"));

            JSONObject featureObject = pictureInfoExtObject.getJSONObject("FeatureObject");
            featureObject.put("FeatureData", jsonObject.getJSONObject("FeatureObject").get("FeatureData"));
        }

        faceDatas.clear();
        faceDatas.add(pictureInfoExtObject);
    }

    /**
     * 关联人行和人脸id
     */
    private Map<String, List<JSONObject>> connectIdByPersonAndFace(Map<String, List<JSONObject>> resultMap) {

        List<JSONObject> persons = resultMap.get(DATA_TYPE_PERSON);
        List<JSONObject> faces = resultMap.get(DATA_TYPE_FACE);
        if (CollectionUtils.isEmpty(persons) || CollectionUtils.isEmpty(faces)) {
            return resultMap;
        }

        for (JSONObject person : persons) {
            String personID = person.getString("PersonID");
            int personX = person.getIntValue("LeftTopX");
            int personY = person.getIntValue("LeftTopY");
            int personW = person.getIntValue("RightBtmX");
            int personH = person.getIntValue("RightBtmY");
            for (JSONObject face : faces) {
                //人脸已经关联上人形数据的不做处理
                if (face.containsKey("ConnectObjectId")) {
                    continue;
                }
                String faceID = face.getString("FaceID");
                int faceX = face.getIntValue("LeftTopX");
                int faceY = face.getIntValue("LeftTopY");
                int faceW = face.getIntValue("RightBtmX");
                int faceH = face.getIntValue("RightBtmY");
                if (faceX >= personX && faceY >= personY && faceW <= personW && faceH <= personH) {
                    person.put("FaceUUID", faceID);
                    face.put("ConnectObjectType", DATA_TYPE_PERSON);
                    face.put("ConnectObjectId", personID);
                    break;
                }
            }
        }
        return resultMap;
    }

    /**
     * keensense数据组装
     */
    private JSONObject assembleResult(Map<String, List<JSONObject>> resultMap) {

        JSONObject parentJsonObject = new JSONObject();
        for (Map.Entry<String, List<JSONObject>> entry : resultMap.entrySet()) {
            String key = entry.getKey();
            List<JSONObject> value = entry.getValue();

            JSONObject subJsonObject = new JSONObject();
            JSONArray subJsonArray = new JSONArray();
            String parentJsonObjectStr = "";
            String subJsonArrayStr = "";

            if (DATA_TYPE_PERSON.equals(key)) {
                parentJsonObjectStr = "PersonListObject";
                subJsonArrayStr = "PersonObject";
            }

            if (DATA_TYPE_CAR.equals(key)) {
                parentJsonObjectStr = "MotorVehicleListObject";
                subJsonArrayStr = "MotorVehicleObject";
            }

            if (DATA_TYPE_FACE.equals(key)) {
                parentJsonObjectStr = "FaceListObject";
                subJsonArrayStr = "FaceObject";
            }

            if (DATA_TYPE_BIKE.equals(key)) {
                parentJsonObjectStr = "NonMotorVehicleListObject";
                subJsonArrayStr = "NonMotorVehicleObject";
            }

            subJsonArray.addAll(value);
            subJsonObject.put(subJsonArrayStr, subJsonArray);
            parentJsonObject.put(parentJsonObjectStr, subJsonObject);

        }

        return parentJsonObject;
    }

    private void judgeData(List<Integer> recogTypeList, String type, JSONObject handleResult,
                           Map<String, List<JSONObject>> resultMap) {

        List<JSONObject> resultList = resultMap.get(type);

        if (recogTypeList.size() == 1) {
            resultList.add(handleResult);
        }

        if (recogTypeList.size() == 2) {
            if (recogTypeList.contains(PictureInfo.RECOG_TYPE_OBJEXT)
                    && recogTypeList.contains(PictureInfo.RECOG_TYPE_THIRD_VLPR) && !DATA_TYPE_CAR.equals(type)) {
                resultList.add(handleResult);
            }

            if (recogTypeList.contains(PictureInfo.RECOG_TYPE_OBJEXT)
                    && recogTypeList.contains(PictureInfo.RECOG_TYPE_FACE) && !DATA_TYPE_FACE.equals(type)) {
                resultList.add(handleResult);
            }

            if (recogTypeList.contains(PictureInfo.RECOG_TYPE_OBJEXT)
                    && recogTypeList.contains(PictureInfo.RECOG_TYPE_OBJEXT_WITHOUT_VLPR) && !DATA_TYPE_CAR.equals(type)) {
                resultList.add(handleResult);
            }
        }

        if (recogTypeList.size() == 3 && recogTypeList.contains(PictureInfo.RECOG_TYPE_OBJEXT)
                && recogTypeList.contains(PictureInfo.RECOG_TYPE_THIRD_VLPR)
                && recogTypeList.contains(PictureInfo.RECOG_TYPE_FACE)
                && !DATA_TYPE_CAR.equals(type) && !DATA_TYPE_FACE.equals(type)) {
            resultList.add(handleResult);
        }
        resultMap.put(type, resultList);
    }

    private List<JSONObject> analysisCarDataGlst(JSONObject object, PictureInfo pictureInfo) {
        log.info("glst data______:" + object.toJSONString());
        List<JSONObject> result = new ArrayList<>();
        JSONArray vehicles = object.getJSONArray("Vehicles");
        if (null == vehicles) {
            return result;
        }
        try {
            for (int i = 0; i < vehicles.size(); i++) {
                JSONObject resultJsonObject = new JSONObject();
                JSONObject vehiclesJSONObject = vehicles.getJSONObject(i);

                setPublicAttribute(resultJsonObject, DATA_TYPE_CAR, pictureInfo);
                setGlstPulicAttribute(vehiclesJSONObject, resultJsonObject);

                //车身
                JSONObject modelType = vehiclesJSONObject.getJSONObject("ModelType");
                Integer styleId = ClassType.getVehicleTypeByGstl(modelType.getIntValue("StyleId"));
                resultJsonObject.put("MotorVehicleID", new SimpleDateFormat(DATE_Y_M_DHMS).format(new Date()) + IDUtil.uuid());

                resultJsonObject.put("VehicleClass", ClassType.convertQstVehicleClass(String.valueOf(styleId)));
                resultJsonObject.put("VehicleBrand", ClassType.convertQstVehicleBrand(modelType.getString("Brand")));

                //颜色
                JSONObject color = vehiclesJSONObject.getJSONObject("Color");
                if (null != color) {
                    resultJsonObject.put("VehicleColor", ClassType.convertGlstVehicleColor(color.getString("ColorId")));
                }

                //年检，遮阳板，挂件 1-年检 2-遮阳板 3-挂件
                JSONArray symbols = vehiclesJSONObject.getJSONArray("Symbols");
                resultJsonObject.put("Paper", 0);
                resultJsonObject.put("Drop", 0);
                resultJsonObject.put("Sunvisor", 0);
                if (null != symbols) {
                    for (int j = 0; j < symbols.size(); j++) {
                        JSONObject symbolsJSONObject = symbols.getJSONObject(j);
                        int symbolId = symbolsJSONObject.getIntValue("SymbolId");
                        if (symbolId == 2) {
                            resultJsonObject.put("Paper", 1);
                        }
                        if (symbolId == 3) {
                            resultJsonObject.put("Drop", 1);
                        }
                        if (symbolId == 4 || symbolId == 5) {
                            resultJsonObject.put("Sunvisor", 1);
                        }
                    }
                }

                //车牌
                JSONArray plates = vehiclesJSONObject.getJSONArray("Plates");
                if (null != plates && !plates.isEmpty()) {
                    JSONObject platesJSONObject = plates.getJSONObject(0);
                    resultJsonObject.put("PlateNo", platesJSONObject.get("PlateText"));
                    JSONObject plateColor = platesJSONObject.getJSONObject("Color");
                    String colorId = plateColor.getString("ColorId");
                    resultJsonObject.put("PlateColor", ClassType.convertGlstVehicleColor(colorId));

                    Integer platesStyleId = ClassType.getPlateTypeByGstl(platesJSONObject.getIntValue("StyleId"));
                    resultJsonObject.put("PlateClass", ClassType.convertGlstPlateClass(String.valueOf(platesStyleId)));
                }

                //车辆内人员
                JSONArray passengers = vehiclesJSONObject.getJSONArray("Passengers");
                if (null != passengers) {
                    for (int j = 0; j < passengers.size(); j++) {
                        JSONObject passengersJSONObject = passengers.getJSONObject(j);
                        String driver = passengersJSONObject.getString("Driver");

                        resultJsonObject.put(Boolean.valueOf(driver) ? "SafetyBelt" : "SecondBelt", passengersJSONObject.get("BeltFlag"));
                        if (Boolean.valueOf(driver)) {
                            resultJsonObject.put("Calling", passengersJSONObject.get("PhoneFlag"));
                        }
                    }
                }
                setPicInfo(resultJsonObject, pictureInfo);
                result.add(resultJsonObject);
            }
        } catch (Exception e) {
            log.error("======analysisCarDataGlst Exception:", e);
        }
        return result;
    }

    private JSONObject analysisPersonData(JSONObject object, JSONObject metadata, String typeCode, PictureInfo pictureInfo) {

        JSONObject resultJsonObject = new JSONObject();
        try {
            setPublicAttribute(resultJsonObject, typeCode, pictureInfo);
            setQstPulicAttribute(object, resultJsonObject);
            setQstAge(resultJsonObject, metadata);
            setQstAppendant(resultJsonObject, metadata);

            String gender = metadata.getString("Gender");
            resultJsonObject.put("PersonID", new SimpleDateFormat(DATE_Y_M_DHMS).format(new Date()) + IDUtil.uuid());
            resultJsonObject.put("GenderCode", "-1".equals(gender) ? 0 : gender);
            resultJsonObject.put("Angle", metadata.get("Angle"));
            resultJsonObject.put("Handbag", metadata.get("HasCarrybag"));
            String coatLength = metadata.getString("CoatLength");
            resultJsonObject.put("CoatStyle", ClassType.getCoatStyleCode(coatLength));

            JSONArray coatColors = metadata.getJSONArray("CoatColor");
            if (!coatColors.isEmpty()) {
                resultJsonObject.put("CoatColor", getColor(coatColors.get(0).toString()));
            }
            String trousersLength = metadata.getString("TrousersLength");
            resultJsonObject.put("TrousersStyle", ClassType.getTrousersStyleCode(trousersLength));

            JSONArray trousersColors = metadata.getJSONArray("TrousersColor");
            if (!trousersColors.isEmpty()) {
                resultJsonObject.put("TrousersColor", getColor(trousersColors.get(0).toString()));
            }
            String hairStyle = metadata.getString("HairStyle");
            resultJsonObject.put("HairStyle", ClassType.getHairStyleCode(hairStyle));
            resultJsonObject.put("CoatTexture", metadata.get("CoatTexture"));
            resultJsonObject.put("TrousersTexture", metadata.get("TrousersTexture"));
            resultJsonObject.put("Trolley", metadata.get("HasTrolley"));
            resultJsonObject.put("Luggage", metadata.get("HasLuggage"));
            String bag = metadata.getString("HasBackpack");
            String glasses = metadata.getString("HasGlasses");
            String umbrella = metadata.getString("HasUmbrella");
            String respirator = metadata.getString("HasHat");
            String cap = metadata.getString("HasMask");
            resultJsonObject.put("Bag", bag);
            resultJsonObject.put("Glasses", glasses);
            resultJsonObject.put("Umbrella", umbrella);
            resultJsonObject.put("Respirator", respirator);
            resultJsonObject.put("Cap", cap);
            setPicInfo(resultJsonObject, pictureInfo);
        } catch (Exception e) {
            log.error("======analysisPersonData Exception:", e);
        }

        return resultJsonObject;
    }

    public static String getColor(String value) {
        Integer color = Integer.valueOf(value);
        String str = "";
        switch (color)
        {
            //黑
            case 0:
                str = "1";
                break;
            //蓝
            case 16724484:
            case 12423793:
            case 15311656:
            case 8327170:
                str = "5";
                break;
            //棕
            case 343174:
            case 2111058:
            case 8761028:
                str = "8";
                break;
            //绿
            case 16776448:
            case 5287936:
                str = "9";
                break;
            //灰
            case 137887:
            case 5263440:
            case 6579300:
                str = "3";
                break;
            //橙
            case 37887:
                str = "7";
                break;
            //粉
            case 16743167:
            case 11306222:
                str = "12";
                break;
            //紫
            case 9576596:
                str = "10";
                break;
            //红
            case 9983:
                str = "4";
                break;
            //白
            case 16777215:
                str = "2";
                break;
            //黄
            case 65535:
                str = "6";
                break;
            default:
                str = "-1";
                break;
        }
        return str;
    }

    public static String getColorOld(String value) {
        Integer color = Integer.valueOf(value);
        String str = "";
        switch (color)
        {
            //黑
            case 1:
                str = "0";
                break;
            //蓝
            case 5:
                str = "16724484";
                break;
            //棕
            case 8:
                str = "343174";
                break;
            //绿
            case 9:
                str = "16776448";
                break;
            //灰
            case 3:
                str = "11842740";
                break;
            //橙
            case 7:
                str = "37887";
                break;
            //粉
            case 12:
                str = "16743167";
                break;
            //紫
            case 10:
                str = "9576596";
                break;
            //红
            case 4:
                str = "9983";
                break;
            //白
            case 2:
                str = "16777215";
                break;
            //黄
            case 6:
                str = "65535";
                break;
            default:
                str = "-1";
                break;
        }
        return str;
    }
    
    private JSONObject analysisCarData(JSONObject object, JSONObject metadata, String typeCode,
                                       PictureInfo pictureInfo) {

        JSONObject resultJsonObject = new JSONObject();
        try {
            setPublicAttribute(resultJsonObject, typeCode, pictureInfo);
            setQstPulicAttribute(object, resultJsonObject);

            String vehicleClass = metadata.getString("VehicleClass");
            resultJsonObject.put("MotorVehicleID", new SimpleDateFormat(DATE_Y_M_DHMS).format(new Date()) + IDUtil.uuid());
            resultJsonObject.put("VehicleClass", ClassType.convertQstVehicleClass(vehicleClass));
            JSONArray vehicleColors = metadata.getJSONArray("VehicleColor");
            if (!vehicleColors.isEmpty()) {
                resultJsonObject.put("VehicleColor",
                        ClassType.convertQstVehicleColorType(vehicleColors.getString(0)));
            }

            resultJsonObject.put("VehicleBrand", ClassType.convertQstVehicleBrand(metadata.getString("VehicleBrand")));
            resultJsonObject.put("HasPlate", metadata.getString("HasPlate"));
            resultJsonObject.put("PlateClass", metadata.getString("PlateClass"));
            resultJsonObject.put("PlateColor", ClassType.convertQstPlateColorType(metadata.getString("PlateColor")));
            String plateNo = metadata.getString("PlateNo");
            if (org.apache.commons.lang.StringUtils.isNotEmpty(plateNo)){
                plateNo = plateNo.equals("-1") ? "" : plateNo;
            }
            resultJsonObject.put("PlateNo", plateNo);
            resultJsonObject.put("Sunvisor", metadata.getString("Sunvisor"));
            resultJsonObject.put("Paper", metadata.getString("Paper"));
            resultJsonObject.put("Drop", metadata.getString("Drop"));
            resultJsonObject.put("Tag", metadata.getString("Tag"));
            JSONObject safetyBelt = metadata.getJSONObject("SafetyBelt");
            if (null != safetyBelt) {
                resultJsonObject.put("SafetyBelt", safetyBelt.get("MainDriver"));
                resultJsonObject.put("SecondBelt", safetyBelt.get("CoDriver"));
            }
            resultJsonObject.put("Calling", metadata.getString("HasCall"));
            resultJsonObject.put("Crash", metadata.getString("HasCrash"));
            resultJsonObject.put("Danger", metadata.getString("HasDanger"));

            setPicInfo(resultJsonObject, pictureInfo);
        } catch (Exception e) {
            log.error("======analysisCarData Exception:", e);
        }

        return resultJsonObject;
    }

    private JSONObject analysisFaceData(JSONObject object, PictureInfo pictureInfo) {
        JSONObject jsonObject = new JSONObject();
        setPublicAttribute(jsonObject, DATA_TYPE_FACE, pictureInfo);
        jsonObject.put("FaceID", new SimpleDateFormat(DATE_Y_M_DHMS).format(new Date()) + IDUtil.uuid());

        JSONObject boxJsonObject = object.getJSONObject("box");
        if (null != boxJsonObject) {
            jsonObject.put("LeftTopX", boxJsonObject.get("x"));
            jsonObject.put("LeftTopY", boxJsonObject.get("y"));
            jsonObject.put("RightBtmX", boxJsonObject.getInteger("x") + boxJsonObject.getInteger("w"));
            jsonObject.put("RightBtmY", boxJsonObject.getInteger("y") + boxJsonObject.getInteger("h"));
        }

        jsonObject.put("Blurry", object.get("blurry"));
        jsonObject.put("Roll", object.get("roll"));
        jsonObject.put("Yaw", object.get("yaw"));
        jsonObject.put("Pitch", object.get("pitch"));
        jsonObject.put("Feature", object.get("feature"));
        jsonObject.put("Rect", object.get("rect"));
        jsonObject.put("Glass", object.get("glass"));

        JSONObject featureObject = new JSONObject();
        featureObject.put("FeatureData", object.get("feature"));
        jsonObject.put("FaceQuality", object.get("quality"));

        jsonObject.put("FeatureObject", featureObject);
        setPicInfo(jsonObject, pictureInfo);
        return jsonObject;
    }

    private JSONObject analysisBikeData(JSONObject object, JSONObject metadata, String typeCode,
                                        PictureInfo pictureInfo) {

        JSONObject resultJsonObject = new JSONObject();
        try {
            setPublicAttribute(resultJsonObject, typeCode, pictureInfo);
            setQstPulicAttribute(object, resultJsonObject);

            resultJsonObject.put("NonMotorVehicleID", new SimpleDateFormat(DATE_Y_M_DHMS).format(new Date()) + IDUtil.uuid());
            resultJsonObject.put("BikeGenre", getBikeGenre(metadata.getInteger("BikeClass")));
            resultJsonObject.put("Sex", metadata.get("Gender"));
            resultJsonObject.put("Age", metadata.get("Age"));
            resultJsonObject.put("Angle", metadata.get("Angle"));
            resultJsonObject.put("Bag", metadata.get("HasBackpack"));
            resultJsonObject.put("Glasses", metadata.get("HasGlasses"));
            resultJsonObject.put("Handbag", metadata.get("HasCarrybag"));
            resultJsonObject.put("Umbrella", metadata.get("HasUmbrella"));
            resultJsonObject.put("CoatStyle", metadata.get("CoatLength"));
            resultJsonObject.put("HasPlate", metadata.get("HasPlate"));
            resultJsonObject.put("Helmet", metadata.get("HasHelmet"));
            resultJsonObject.put("HelmetColorTag1", metadata.get("HelmetColor"));
            resultJsonObject.put("Respirator", metadata.get("HasMask"));
            resultJsonObject.put("CoatTexture", metadata.get("CoatTexture"));

            String coatLength = metadata.getString("CoatLength");
            resultJsonObject.put("CoatStyle", ClassType.getCoatStyleCode(coatLength));

            JSONArray coatColors = metadata.getJSONArray("CoatColor");
            if (!coatColors.isEmpty()) {
                resultJsonObject.put("CoatColor", getColor(coatColors.get(0).toString()));
            }

            setPicInfo(resultJsonObject, pictureInfo);
        } catch (Exception e) {
            log.error("======analysisBikeData Exception:", e);
        }

        return resultJsonObject;
    }

    public static String getBikeGenre(Integer value) {
        String str = "";
        switch (value) {
            //二轮摩托车
            case 1:
                str = "2";
                break;
            //自行车
            case 2:
                str = "3";
                break;
             //三轮车
            case 3:
                str = "5";
                break;
            default:
                str = "-1";
                break;
        }
        return str;
    }

    private void setPublicAttribute(JSONObject resultJsonObject, String typeCode,
                                    PictureInfo pictureInfo) {

        String uuid = IDUtil.uuid();
        resultJsonObject.put("objType", typeCode);
        resultJsonObject.put("InsertTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        resultJsonObject.put("Serialnumber", pictureInfo.getSerialNumber());
        resultJsonObject.put("DeviceID", pictureInfo.getDeviceId());

        Long captureTime = pictureInfo.getCaptureTime();
        Long enterTime = pictureInfo.getEnterTime();
        Long leaveTime = pictureInfo.getLeaveTime();

        String markTime = "MarkTime";
        String appearTime = "AppearTime";
        String disappearTime = "DisappearTime";

        if (DATA_TYPE_PERSON.contains(typeCode)) {
            markTime = "LocationMarkTime";
            appearTime = "PersonAppearTime";
            disappearTime = "PersonDisAppearTime";
        }
        if (DATA_TYPE_FACE.contains(typeCode)) {
            markTime = "LocationMarkTime";
            appearTime = "FaceAppearTime";
            disappearTime = "FaceDisAppearTime";
        }

        resultJsonObject.put(markTime, new SimpleDateFormat(DATE_YMDHMS).format(new Date(captureTime)));
        resultJsonObject.put(appearTime, new SimpleDateFormat(DATE_YMDHMS).format(new Date(enterTime)));
        resultJsonObject.put(disappearTime, new SimpleDateFormat(DATE_YMDHMS).format(new Date(leaveTime)));

        resultJsonObject.put("uuid", uuid);
        if (!StringUtils.isEmpty(pictureInfo.getExt())) {
            resultJsonObject.put("ext", pictureInfo.getExt());
        }
    }

    private void setGlstPulicAttribute(JSONObject vehicle, JSONObject resultJsonObject) {
        JSONObject img = vehicle.getJSONObject("Img");
        if (null != img) {
            JSONObject cutboard = img.getJSONObject("Cutboard");
            if (null != cutboard) {
                resultJsonObject.put("LeftTopX", cutboard.get("X"));
                resultJsonObject.put("LeftTopY", cutboard.get("Y"));
                resultJsonObject.put("RightBtmX", cutboard.get("Width"));
                resultJsonObject.put("RightBtmY", cutboard.get("Height"));
            }
        }
        JSONObject featureObject = new JSONObject();
        featureObject.put("FeatureData", VlprCommonConst.getGlstFeature(vehicle.getString("Features")));
        resultJsonObject.put("FeatureObject", featureObject);
    }

    private void setQstPulicAttribute(JSONObject jsonObject, JSONObject resultJsonObject) {
        JSONObject objectBoundingBox = Optional.ofNullable(jsonObject).map(p -> p.getJSONObject("Metadata"))
                .map(p -> p.getJSONObject("ObjectBoundingBox")).orElse(null);
        if (null != objectBoundingBox) {
            resultJsonObject.put("LeftTopX", objectBoundingBox.get("x"));
            resultJsonObject.put("LeftTopY", objectBoundingBox.get("y"));
            resultJsonObject.put("RightBtmX", objectBoundingBox.getInteger("x") + objectBoundingBox.getInteger("w"));
            resultJsonObject.put("RightBtmY", objectBoundingBox.getInteger("y") + objectBoundingBox.getInteger("h"));
            JSONObject featureObject = new JSONObject();
            featureObject.put("FeatureData", jsonObject.get("Feature"));
            resultJsonObject.put("FeatureObject", featureObject);
        }
    }

    private void setQstPulicAttributeNew(JSONObject jsonObject, JSONObject resultJsonObject) {
            resultJsonObject.put("LeftTopX", jsonObject.get("LeftTopX"));
            resultJsonObject.put("LeftTopY", jsonObject.get("LeftTopY"));
            resultJsonObject.put("RightBtmX", jsonObject.getInteger("RightBtmX"));
            resultJsonObject.put("RightBtmY", jsonObject.getInteger("RightBtmY"));
            JSONObject featureObject = new JSONObject();
            featureObject.put("FeatureData", jsonObject.get("Feature"));
            resultJsonObject.put("FeatureObject", featureObject);
    }


    private void setPicInfo(JSONObject resultJsonObject, PictureInfo pictureInfo) {
        int[] pois = new int[4];
        pois[0] = resultJsonObject.getIntValue("LeftTopX");
        pois[1] = resultJsonObject.getIntValue("LeftTopY");
        pois[2] = resultJsonObject.getIntValue("RightBtmX");
        pois[3] = resultJsonObject.getIntValue("RightBtmY");
        try {
            if (pictureInfo.getInterceptFlag()) {
                byte[][] splitPic = ImageBaseUtil.splitPic(pictureInfo, pois);
                if (splitPic.length > 0) {
                    JSONArray subImageInfoObject = new JSONArray();
                    for (int i = 0; i < splitPic.length; i++) {
                        JSONObject imgeInfo = new JSONObject();
                        imgeInfo.put("Type", i == 0 ? "10" : "14");// 10:小图  14:大图
                        imgeInfo.put("DeviceID", pictureInfo.getDeviceId());
                        imgeInfo.put("Data", splitPic[i]);
                        subImageInfoObject.add(imgeInfo);
                    }
                    JSONObject subImageList = new JSONObject();
                    subImageList.put("SubImageInfoObject", subImageInfoObject);
                    resultJsonObject.put("SubImageList", subImageList);
                }
            } else {
                // pictureType为1时候 picBase64为小图
                Integer pictureType = pictureInfo.getPictureType();
                String picBigBase64 = pictureInfo.getPicBase64();
                //调用图片服务获取图片url,并存储
                //String picBase64 = getImageBase64(pois[0], pois[1], pois[2], pois[3], pictureInfo.getPicUrl());
                String jsonStr = feignService.getImg(IDUtil.uuid(),picBigBase64);
                JSONObject jsonObject = JSONObject.parseObject(jsonStr);
                String imageUrl = jsonObject.getString("ImageUrl");
                JSONObject imgeInfo = new JSONObject();
                imgeInfo.put("Type", "10");// 14:小图  10:大图
                imgeInfo.put("DeviceID", pictureInfo.getDeviceId());
                imgeInfo.put("StoragePath", imageUrl);
                //小图裁剪
                pois[2] = pois[2] - pois[0];
                pois[3] = pois[3] - pois[1];
                long startTime = System.currentTimeMillis();
                String picMinBase64 = getMinPic(pictureInfo, pois);//裁剪小图获取base64
                log.info("裁剪小图时间" + (System.currentTimeMillis() - startTime));
                String jsonStrMin = feignService.getImg(IDUtil.uuid(),picMinBase64);
                JSONObject jsonObjectMin = JSONObject.parseObject(jsonStrMin);
                String imageUrlMin = jsonObjectMin.getString("ImageUrl");
                JSONObject imgeInfo2 = new JSONObject();
                imgeInfo2.put("Type", "14");
                imgeInfo2.put("DeviceID", pictureInfo.getDeviceId());
                imgeInfo2.put("StoragePath", imageUrlMin);

                JSONObject subImageList = new JSONObject();
                JSONArray subImageInfoObject = new JSONArray();
                subImageInfoObject.add(imgeInfo);
                subImageInfoObject.add(imgeInfo2);
                subImageList.put("SubImageInfoObject", subImageInfoObject);
                resultJsonObject.put("SubImageList", subImageList);
            }
        } catch (Exception e) {
            log.error("======setPicUrl Exception:", e);
        }
    }

    private void setQstAge(JSONObject resultJsonObject, JSONObject metadata) {
        String age = metadata.getString("Age");
        int maxAge;
        int minAge;
        if ("4".equals(age)) {
            maxAge = 16;
            minAge = 1;
        } else if ("8".equals(age)) {
            maxAge = 30;
            minAge = 17;
        } else if ("16".equals(age)) {
            maxAge = 55;
            minAge = 31;
        } else if ("32".equals(age)) {
            maxAge = 100;
            minAge = 56;
        } else {
            maxAge = 100;
            minAge = 1;
        }
        resultJsonObject.put("AgeUpLimit", maxAge);
        resultJsonObject.put("AgeLowerLimit", minAge);
    }

    private void setQstAppendant(JSONObject resultJsonObject, JSONObject metadata) {
        List<String> list = new ArrayList<>();
        String hasBackpack = metadata.getString("HasBackpack");
        String hasGlasses = metadata.getString("HasGlasses");
        String hasUmbrella = metadata.getString("HasUmbrella");
        String hasHat = metadata.getString("HasHat");
        String hasMask = metadata.getString("HasMask");

        if ("1".equals(hasBackpack)) {
            list.add("8");
        }

        if ("1".equals(hasGlasses)) {
            list.add("6");
        }

        if ("1".equals(hasUmbrella)) {
            list.add("2");
        }

        if ("1".equals(hasHat)) {
            list.add("7");
        }

        if ("1".equals(hasMask)) {
            list.add("3");
        }

        StringBuilder appendant = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            appendant.append((i == list.size() - 1) ? list.get(i) : list.get(i) + ";");
        }

        resultJsonObject.put("Appendant", appendant);
    }

    private JSONObject analysisPersonDataNew(JSONObject object, JSONObject metadata, String typeCode, PictureInfo pictureInfo) {

        JSONObject resultJsonObject = new JSONObject();
        try {
            setPublicAttribute(resultJsonObject, typeCode, pictureInfo);
            metadata.put("Feature", object.get("Feature"));
            setQstPulicAttributeNew(metadata, resultJsonObject);//kk x.y
            setQstAge(resultJsonObject, metadata);//kk AgeUpLimit
            setQstAppendant(resultJsonObject, metadata);//kk Appendant
            resultJsonObject.put("PersonID", new SimpleDateFormat(DATE_Y_M_DHMS).format(new Date()) + IDUtil.uuid());
            resultJsonObject.put("GenderCode", metadata.getString("Sex"));
            resultJsonObject.put("Age", metadata.getString("Age"));
            resultJsonObject.put("CoatColor", metadata.getString("CoatColor"));
            resultJsonObject.put("TrousersColor", metadata.getString("TrousersColor"));
            resultJsonObject.put("CoatStyle", metadata.getString("CoatStyle"));
            resultJsonObject.put("TrousersStyle", metadata.getString("TrousersStyle"));
            //resultJsonObject.put("AgeUpLimit", metadata.getString("AgeUpLimit"));
            //resultJsonObject.put("AgeLowerLimit", metadata.getString("AgeLowerLimit"));
            resultJsonObject.put("Angle", metadata.getString("Angle"));
            resultJsonObject.put("Bag", metadata.getString("Bag"));
            resultJsonObject.put("Handbag", metadata.getString("Handbag"));
            resultJsonObject.put("Glasses", metadata.getString("Glasses"));
            resultJsonObject.put("Umbrella", metadata.getString("Umbrella"));
            resultJsonObject.put("Respirator", metadata.getString("Respirator"));
            resultJsonObject.put("Cap", metadata.getString("Cap"));
            resultJsonObject.put("HairStyle", metadata.getString("HairStyle"));
            resultJsonObject.put("CoatTexture", metadata.getString("CoatTexture"));
            resultJsonObject.put("TrousersTexture", metadata.getString("TrousersTexture"));
            resultJsonObject.put("Luggage", metadata.getString("Luggage"));
            resultJsonObject.put("Trolley", metadata.getString("Trolley"));
            resultJsonObject.put("HasKnife", metadata.getString("HasKnife"));
            setPicInfo(resultJsonObject, pictureInfo);
        } catch (Exception e) {
            log.error("======analysisPersonData Exception:", e);
        }

        return resultJsonObject;
    }

    private JSONObject analysisBikeDataNew(JSONObject object, JSONObject metadata, String typeCode,
                                        PictureInfo pictureInfo) {

        JSONObject resultJsonObject = new JSONObject();
        try {
            setPublicAttribute(resultJsonObject, typeCode, pictureInfo);
            metadata.put("Feature", object.get("Feature"));
            setQstPulicAttributeNew(metadata, resultJsonObject);

            resultJsonObject.put("NonMotorVehicleID", new SimpleDateFormat(DATE_Y_M_DHMS).format(new Date()) + IDUtil.uuid());
            resultJsonObject.put("Sex", metadata.get("Sex"));
            resultJsonObject.put("BikeGenre", metadata.get("BikeGenre"));
            resultJsonObject.put("CoatColor1", metadata.getString("CoatColor1"));//输出的新的转老的,醉了
            resultJsonObject.put("Angle", metadata.get("Angle"));
            resultJsonObject.put("Age", metadata.get("Age"));
            resultJsonObject.put("CoatStyle", metadata.get("CoatStyle"));
            resultJsonObject.put("CoatTexture", metadata.get("CoatTexture"));
            resultJsonObject.put("Glasses", metadata.get("Glasses"));
            resultJsonObject.put("Respirator", metadata.get("Respirator"));
            resultJsonObject.put("Wheels", metadata.get("Wheels"));
            resultJsonObject.put("HasPlate", metadata.get("HasPlate"));
            resultJsonObject.put("Helmet", metadata.get("Helmet"));
            //resultJsonObject.put("HelmetColorTag1", getColor(metadata.getString("HelmetColorTag1")));
            resultJsonObject.put("HelmetColorTag1", metadata.getString("HelmetColorTag1"));//输出的老的
            resultJsonObject.put("Bag", metadata.get("Bag"));
            resultJsonObject.put("Umbrella", metadata.get("Umbrella"));

            setPicInfo(resultJsonObject, pictureInfo);
        } catch (Exception e) {
            log.error("======analysisBikeData Exception:", e);
        }

        return resultJsonObject;
    }

    private JSONObject analysisCarDataNew(JSONObject object, JSONObject metadata, String typeCode,
                                       PictureInfo pictureInfo) {

        JSONObject resultJsonObject = new JSONObject();
        try {
            setPublicAttribute(resultJsonObject, typeCode, pictureInfo);
            metadata.put("Feature", object.get("Feature"));
            setQstPulicAttributeNew(metadata, resultJsonObject);

            resultJsonObject.put("MotorVehicleID", new SimpleDateFormat(DATE_Y_M_DHMS).format(new Date()) + IDUtil.uuid());
            resultJsonObject.put("VehicleBrand", metadata.getString("VehicleBrand"));
            resultJsonObject.put("VehicleModel", metadata.getString("VehicleModel"));
            resultJsonObject.put("VehicleStyles", metadata.getString("VehicleStyles"));
            resultJsonObject.put("PlateNo", metadata.getString("PlateNo"));
            resultJsonObject.put("PlateColor", metadata.getString("PlateColor"));
            resultJsonObject.put("PlateClass", metadata.getString("PlateClass"));
            resultJsonObject.put("VehicleClass", metadata.getString("VehicleClass"));
            resultJsonObject.put("VehicleColor", metadata.getString("VehicleColor"));
            resultJsonObject.put("Calling", metadata.getString("Calling"));
            resultJsonObject.put("HasCrash", metadata.getString("HasCrash"));
            resultJsonObject.put("HasDanger", metadata.getString("HasDanger"));
            resultJsonObject.put("TagNum", metadata.getString("Tagnum"));
            JSONObject safetyBelt = metadata.getJSONObject("SafetyBelt");
            if (null != safetyBelt) {
                resultJsonObject.put("SafetyBelt", safetyBelt.get("MainDriver"));
                resultJsonObject.put("SecondBelt", safetyBelt.get("CoDriver"));
            }
            resultJsonObject.put("Sun", metadata.getString("Sunvisor"));
            resultJsonObject.put("Drop", metadata.getString("Drop"));
            resultJsonObject.put("Paper", metadata.getString("Paper"));
            resultJsonObject.put("Direction", metadata.getString("Direction"));
            resultJsonObject.put("Angle", getAngle(metadata.getString("Angle")));
            resultJsonObject.put("Rack", metadata.getString("Rack"));
            resultJsonObject.put("SunRoof", metadata.getString("SunRoof"));
            resultJsonObject.put("Speed", metadata.getString("Speed"));
            resultJsonObject.put("Decoration", metadata.getString("Decoration"));

            setPicInfo(resultJsonObject, pictureInfo);
        } catch (Exception e) {
            log.error("======analysisCarData Exception:", e);
        }

        return resultJsonObject;
    }

    private String getAngle(String angle){
        switch (angle) {
            case "1":
                return "128";
            case "2":
                return "256";
            case "3":
                return "512";
            default:
                return "";
        }
    }

    /**
     * push data to keensense
     */
    private void pushDataToKeensense(JSONObject handleReceiveDataResult) {
        try {
            JSONObject faceListObject = handleReceiveDataResult.getJSONObject("FaceListObject");
            JSONArray faceArray = faceListObject.getJSONArray("FaceObject");
           /* if (faceArray != null && faceArray.size() == 0){
                return;
            }*/
            if (faceArray != null && faceArray.size() > 0){
                JSONObject facesJSONObject = (JSONObject)faceArray.get(0);
                JSONObject featureObject = facesJSONObject.getJSONObject("FeatureObject");
                String featureData = featureObject.getString("FeatureData");
                if (StringUtils.isEmpty(featureData)){
                    return;
                }
            }
            String a = feignToSearch.sendPictureToKeensense(handleReceiveDataResult);
            log.info("send picture info to keensense return message = " + a);
        } catch (Exception e) {
            log.error("======pushDataToKeensense Exception:", e);
        }
    }

    private String getImageBase64(int leftTopX, int leftTopY, int rightBtmX, int rightBtmY, String imageUrl) throws IOException {
        //new一个URL对象
        URL url = new URL(imageUrl);
        //打开链接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置请求方式为"GET"
        conn.setRequestMethod("GET");
        //超时响应时间为5秒
        conn.setConnectTimeout(5 * 1000);
        //通过输入流获取图片数据
        InputStream inStream = conn.getInputStream();
        //得到图片的二进制数据，以二进制封装得到数据，具有通用性
        BufferedImage image = ImageIO.read(inStream);
        Graphics g = image.getGraphics();
        g.setColor(Color.RED);//画笔颜色
        int width = rightBtmX - leftTopX;
        int height = rightBtmY - leftTopY;
        g.drawRect(leftTopX, leftTopY, width, height);//矩形框(原点x坐标，原点y坐标，矩形的长，矩形的宽)
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
        ImageIO.write(image, "jpeg", imOut);
        InputStream is = new ByteArrayInputStream(bs.toByteArray());
        byte[] data = readInputStream(is);
        BASE64Encoder encode = new BASE64Encoder();
        String base64 = encode.encode(data).replaceAll("\\r|\\n", "");
        return base64;
    }

    private byte[] readInputStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    public String getMinPic(PictureInfo pictureInfo, int[] pois){
//        OpencvUtil util = OpencvUtil.getInstance();
//        byte[] bytes = Base64.decode(Arrays.toString(pictureInfo.getPicBase64().getBytes()));
//        Mat img = util.loadImage(bytes);
//        byte[][] targetImages = util.cutTargetImages(img, pois, 20, 20);
//        if (null != targetImages) {
//            BASE64Encoder encode = new BASE64Encoder();
//            String base64 = encode.encode(targetImages[0]).replaceAll("\\r|\\n", "");
//            return base64;
//        }
        return null;
    }

}
