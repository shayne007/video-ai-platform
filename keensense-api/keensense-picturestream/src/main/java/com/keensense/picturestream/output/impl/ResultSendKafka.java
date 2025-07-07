package com.keensense.picturestream.output.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.common.config.SpringContext;
import com.keensense.picturestream.common.ResultSendThreads;
import com.keensense.picturestream.common.VlprCommonConst;
import com.keensense.picturestream.config.NacosConfig;
import com.keensense.picturestream.entity.PictureInfo;
import com.keensense.picturestream.util.ClassType;
import com.keensense.picturestream.util.IDUtil;
import com.keensense.picturestream.util.ImageBaseUtil;
import com.keensense.picturestream.util.kafka.KafkaUtil;
import com.loocme.sys.datastruct.Var;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

/**
 * Created by memory_fu on 2019/7/4.
 */
@Slf4j
public class ResultSendKafka {
    
    private static NacosConfig nacosConfig = SpringContext.getBean(NacosConfig.class);
    
    private static final String DATA_TYPE_PERSON = "1";
    private static final String DATA_TYPE_CAR = "2";
    private static final String DATA_TYPE_FACE = "3";
    private static final String DATA_TYPE_BIKE = "4";
    
    public boolean receiveData(List<PictureInfo> pictureInfos) {
        
        for (PictureInfo pictureInfo : pictureInfos) {
            
            List<JSONObject> resultList = new ArrayList<>(); //处理结果
            
            try {
                //处理数据
                handleReceiveData(pictureInfo, resultList);
                
                //推送kafka
                log.info("kafka data List__________:" + JSONObject.toJSONString(resultList));
                pushDataTokafka(resultList);
            } catch (Exception e) {
                log.error("====receiveData Exception:", e);
                return false;
            }
        }
        
        return true;
    }
    
    private void handleReceiveData(PictureInfo pictureInfo, List<JSONObject> resultList) {
        List<Integer> recogTypeList = pictureInfo.getRecogTypeList();
        List<Var> varList = pictureInfo.getResults();
        
        if (CollectionUtils.isEmpty(varList)) {
            ResultSendThreads.sendError(pictureInfo);
        }
        
        for (Var var : varList) {
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
                        handleResult = analysisPersonData(jsonObject, metadata, type, pictureInfo);
                        break;
                    case DATA_TYPE_CAR:
                        handleResult = analysisCarData(jsonObject, metadata, type, pictureInfo);
                        break;
                    case DATA_TYPE_FACE:
                        // qst算法人脸
                        break;
                    case DATA_TYPE_BIKE:
                        handleResult = analysisBikeData(jsonObject, metadata, type, pictureInfo);
                        break;
                    default:
                        break;
                }
                log.info("handleResult data_______:" + handleResult.toJSONString());
                
                judgeData(recogTypeList, type, handleResult, resultList);
            }
            
            if (algoSource == 4) {
                //glst车辆
                List<JSONObject> carDataGlst = analysisCarDataGlst(jsonObject, pictureInfo);
                log.info("List<JSONObject> carDataGlst_____: " + JSONObject.toJSONString(carDataGlst));
                resultList.addAll(carDataGlst);
            }
            
            if (algoSource == 8) {
                JSONObject faceData = analysisFaceData(jsonObject, pictureInfo);
                resultList.add(faceData);
            }
            
        }
        
    }

    private void judgeData(List<Integer> recogTypeList, String type, JSONObject handleResult,
        List<JSONObject> resultList) {
        
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
        
    }
    
    private List<JSONObject> analysisCarDataGlst(JSONObject object, PictureInfo pictureInfo) {
        List<JSONObject> result = new ArrayList<>();
        JSONArray vehicles = object.getJSONArray("Vehicles");
        log.info("glst data______:" + object.toJSONString());
        if (null == vehicles) {
            return result;
        }
        try {
            for (int i = 0; i < vehicles.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                JSONObject vehiclesJSONObject = vehicles.getJSONObject(i);
                
                setPulicAttribute(jsonObject, DATA_TYPE_CAR, pictureInfo);
                JSONObject snapshotGlst = createSnapshotGlst(pictureInfo, vehiclesJSONObject);
                
                //车身
                JSONObject features = new JSONObject();
                features.put("featureData", VlprCommonConst.getGlstFeature(vehiclesJSONObject.getString("Features")));
                JSONObject modelType = vehiclesJSONObject.getJSONObject("ModelType");
                features.put("typeCode", ClassType.getVehicleTypeByGstl(modelType.getIntValue("StyleId")));
                features.put("type", ClassType.getVehicleNameByType(features.getIntValue("typeCode")));
                features.put("brandName", modelType.get("Brand"));
                
                //颜色
                JSONObject color = vehiclesJSONObject.getJSONObject("Color");
                features.put("colorCode", color.get("ColorId"));
                features.put("colorName", color.get("ColorName"));
                
                //年检，遮阳板，挂件 1-年检 2-遮阳板 3-挂件
                JSONArray symbols = vehiclesJSONObject.getJSONArray("Symbols");
                features.put("tagNum", 0);
                features.put("paper", 0);
                features.put("drop", 0);
                features.put("sun", 0);
                if (null != symbols) {
                    for (int j = 0; j < symbols.size(); j++) {
                        JSONObject symbolsJSONObject = symbols.getJSONObject(j);
                        int symbolId = symbolsJSONObject.getIntValue("SymbolId");
                        if (symbolId == 1) {
                            features.put("tagNum", features.getIntValue("tagNum") + 1);
                        }
                        if (symbolId == 2) {
                            features.put("paper", 1);
                        }
                        if (symbolId == 3) {
                            features.put("drop", 1);
                        }
                        if (symbolId == 4 || symbolId == 5) {
                            features.put("sun", 1);
                        }
                    }
                }
                
                //车牌
                JSONArray plates = vehiclesJSONObject.getJSONArray("Plates");
                if (null != plates && !plates.isEmpty()) {
                    JSONObject platesJSONObject = plates.getJSONObject(0);
                    features.put("plateLicence", platesJSONObject.get("PlateText"));
                    JSONObject plateColor = platesJSONObject.getJSONObject("Color");
                    features.put("plateColorCode", plateColor.get("ColorId"));
                    features.put("plateColorName", plateColor.get("ColorName"));
                    
                    features.put("plateClassCode", ClassType.getPlateTypeByGstl(platesJSONObject.getIntValue("StyleId")));
                    features.put("plateClass", ClassType.getPlateNameByType(features.getIntValue("plateClassCode")));
                }
                
                //车辆内人员
                JSONArray passengers = vehiclesJSONObject.getJSONArray("Passengers");
                JSONArray passengersJsonArray = new JSONArray();
                if (null != passengers) {
                    for (int j = 0; j < passengers.size(); j++) {
                        JSONObject passengersJson = new JSONObject();
                        JSONObject passengersJSONObject = passengers.getJSONObject(j);
                        String driver = passengersJSONObject.getString("Driver");
                        passengersJson.put("driver", 0);
                        if (Boolean.valueOf(driver)) {
                            passengersJson.put("driver", 1);
                        }
                        passengersJson.put("hasBelt", passengersJSONObject.get("BeltFlag"));
                        passengersJson.put("hasSmoke", passengersJSONObject.get("SmokingFlag"));
                        passengersJson.put("hasFaceCover", passengersJSONObject.get("FacecoverFlag"));
                        passengersJson.put("hasCall", passengersJSONObject.get("PhoneFlag"));
                        
                        passengersJsonArray.add(passengersJson);
                    }
                }
                
                features.put("passengers", passengersJsonArray);
                
                jsonObject.put("snapshot", snapshotGlst);
                jsonObject.put("features", features);
                setPicUrl(jsonObject, DATA_TYPE_CAR, pictureInfo);
                result.add(jsonObject);
            }
        } catch (Exception e) {
            log.error("======analysisCarDataGlst Exception:", e);
        }
        return result;
    }
    
    private JSONObject analysisPersonData(JSONObject object, JSONObject metadata, String typeCode,
        PictureInfo pictureInfo) {
        
        JSONObject jsonObject = new JSONObject();
        try {
            setPulicAttribute(jsonObject, typeCode, pictureInfo);
            JSONObject snapshot = createSnapshot(metadata, pictureInfo);
            
            JSONObject features = new JSONObject();
            features.put("sex", metadata.get("Gender"));
            features.put("age", metadata.get("Age"));
            features.put("angle", metadata.get("Angle"));
            features.put("bag", metadata.get("HasBackpack"));
            features.put("carryBag", metadata.get("HasCarrybag"));
            features.put("glasses", metadata.get("HasGlasses"));
            features.put("umbrella", metadata.get("HasUmbrella"));
            features.put("cap", metadata.get("HasHat"));
            features.put("respirator", metadata.get("HasMask"));
            features.put("hairStyle", metadata.get("HairStyle"));
            features.put("coatTexture", metadata.get("CoatTexture"));
            features.put("trousersTexture", metadata.get("TrousersTexture"));
            features.put("trolley", metadata.get("HasTrolley"));
            features.put("luggage", metadata.get("HasLuggage"));
            features.put("featureData", object.get("Feature"));
            
            JSONObject coatStyle = new JSONObject();
            coatStyle.put("code", metadata.get("CoatLength"));
            coatStyle.put("value", ClassType.coatStyle(metadata.getString("CoatLength")));
            features.put("coatStyle", coatStyle);
            JSONArray coatColors = metadata.getJSONArray("CoatColor");
            for (int i = 0; i < coatColors.size(); i++) {
                String coatColor = coatColors.getString(i);
                JSONObject coatColorTemp = new JSONObject();
                coatColorTemp.put("code", coatColor);
                coatColorTemp.put("value", ClassType.getColorNameByCode(coatColor));
                coatColorTemp.put("key", ClassType.getColorKeyByCode(coatColor));
                features.put("coatColor" + (i + 1), coatColorTemp);
            }
            
            JSONObject trousersStyle = new JSONObject();
            trousersStyle.put("code", metadata.get("TrousersLength"));
            trousersStyle.put("value", ClassType.trousersStyle(metadata.getString("TrousersLength")));
            features.put("trousersStyle", trousersStyle);
            JSONArray trousersColors = metadata.getJSONArray("TrousersColor");
            for (int i = 0; i < trousersColors.size(); i++) {
                String trousersColor = trousersColors.getString(i);
                JSONObject trousersColorTemp = new JSONObject();
                trousersColorTemp.put("code", trousersColor);
                trousersColorTemp.put("value", ClassType.getColorNameByCode(trousersColor));
                trousersColorTemp.put("key", ClassType.getColorKeyByCode(trousersColor));
                features.put("trousersColor" + (i + 1), trousersColorTemp);
            }
            
            jsonObject.put("snapshot", snapshot);
            jsonObject.put("features", features);
            setPicUrl(jsonObject, typeCode, pictureInfo);
        } catch (Exception e) {
            log.error("======analysisPersonData Exception:", e);
        }
        
        return jsonObject;
    }
    
    private JSONObject analysisCarData(JSONObject object, JSONObject metadata, String typeCode,
        PictureInfo pictureInfo) {
        
        JSONObject jsonObject = new JSONObject();
        try {
            setPulicAttribute(jsonObject, typeCode, pictureInfo);
            JSONObject snapshot = createSnapshot(metadata, pictureInfo);
            
            JSONObject features = new JSONObject();
            features.put("typeCode", metadata.get("VehicleClass"));
            JSONArray vehicleColor = metadata.getJSONArray("VehicleColor");
            StringBuilder colorCode = new StringBuilder();
            for (int i = 0; i < vehicleColor.size(); i++) {
                if (i == vehicleColor.size() - 1) {
                    colorCode.append(vehicleColor.getString(i));
                    break;
                }
                colorCode.append(vehicleColor.getString(i)).append(",");
            }
            features.put("colorCode", colorCode);
            features.put("brandName", metadata.get("VehicleBrand"));
            features.put("hasPlate", metadata.get("HasPlate"));
            features.put("plateClass", metadata.get("PlateClass"));
            features.put("plateColorCode", metadata.get("PlateColor"));
            features.put("plateLicence", metadata.get("PlateNo"));
            features.put("sun", metadata.get("Sunvisor"));
            features.put("paper", metadata.get("Paper"));
            features.put("drop", metadata.get("Drop"));
            features.put("tags", metadata.get("Tag"));
            JSONObject safetyBelt = new JSONObject();
            safetyBelt.put("mainDriver", metadata.getJSONObject("SafetyBelt").get("MainDriver"));
            safetyBelt.put("coDriver", metadata.getJSONObject("SafetyBelt").get("CoDriver"));
            features.put("safetyBelt", safetyBelt);
            features.put("hasCall", metadata.get("HasCall"));
            features.put("hasCrash", metadata.get("HasCrash"));
            features.put("hasDanger", metadata.get("HasDanger"));
            features.put("featureData", object.get("Feature"));
            
            jsonObject.put("snapshot", snapshot);
            jsonObject.put("features", features);
            setPicUrl(jsonObject, typeCode, pictureInfo);
        } catch (Exception e) {
            log.error("======analysisCarData Exception:", e);
        }
        
        return jsonObject;
    }
    
    private JSONObject analysisFaceData(JSONObject object, PictureInfo pictureInfo) {
        JSONObject jsonObject = new JSONObject();
        setPulicAttribute(jsonObject, DATA_TYPE_FACE, pictureInfo);
        
        JSONObject boxJsonObject = object.getJSONObject("box");
        JSONObject snapshot = new JSONObject();
        JSONObject boundingBox = new JSONObject();
        if (null != boxJsonObject) {
            boundingBox.put("x", boxJsonObject.get("x"));
            boundingBox.put("y", boxJsonObject.get("y"));
            boundingBox.put("w", boxJsonObject.get("w"));
            boundingBox.put("h", boxJsonObject.get("h"));
            snapshot.put("boundingBox", boundingBox);
        }
        
        JSONObject features = new JSONObject();
        features.put("featureData", object.get("feature"));
        features.put("bodyQuality", object.get("quality"));
        
        jsonObject.put("snapshot", snapshot);
        jsonObject.put("features", features);
        setPicUrl(jsonObject, DATA_TYPE_FACE, pictureInfo);
        return jsonObject;
    }
    
    private JSONObject analysisBikeData(JSONObject object, JSONObject metadata, String typeCode,
        PictureInfo pictureInfo) {
        
        JSONObject jsonObject = new JSONObject();
        try {
            setPulicAttribute(jsonObject, typeCode, pictureInfo);
            JSONObject snapshot = createSnapshot(metadata, pictureInfo);
            
            JSONObject features = new JSONObject();
            features.put("bikeGenre", metadata.get("BikeClass"));
            features.put("sex", metadata.get("Gender"));
            features.put("age", metadata.get("Age"));
            features.put("angle", metadata.get("Angle"));
            features.put("bag", metadata.get("HasBackpack"));
            features.put("glasses", metadata.get("HasGlasses"));
            features.put("carryBag", metadata.get("HasCarrybag"));
            features.put("umbrella", metadata.get("HasUmbrella"));
            JSONObject coatStyle = new JSONObject();
            coatStyle.put("code", metadata.get("CoatLength"));
            coatStyle.put("value", ClassType.coatStyle(metadata.getString("CoatLength")));
            features.put("coatStyle", coatStyle);
            features.put("bikeHasPlate", metadata.get("HasPlate"));
            JSONObject helmet = new JSONObject();
            helmet.put("have", metadata.get("HasHelmet"));
            helmet.put("color", metadata.get("HelmetColor"));
            helmet.put("value", ClassType.getColorNameByCode(metadata.getString("HelmetColor")));
            features.put("helmet", helmet);
            features.put("respirator", metadata.get("HasMask"));
            features.put("coatTexture", metadata.get("CoatTexture"));
            features.put("featureData", object.get("Feature"));
            
            jsonObject.put("snapshot", snapshot);
            jsonObject.put("features", features);
            setPicUrl(jsonObject, typeCode, pictureInfo);
        } catch (Exception e) {
            log.error("======analysisBikeData Exception:", e);
        }
        
        return jsonObject;
    }
    
    private JSONObject createSnapshotGlst(PictureInfo pictureInfo, JSONObject vehicle) {
        JSONObject snapshot = new JSONObject();
        snapshot.put("frameIdx", 0);
        snapshot.put("framePts", pictureInfo.getCaptureTime());
        
        JSONObject img = vehicle.getJSONObject("Img");
        if (null != img) {
            JSONObject cutboard = img.getJSONObject("Cutboard");
            if (null == cutboard) {
                return snapshot;
            }
            JSONObject boundingBox = new JSONObject();
            boundingBox.put("x", cutboard.get("X"));
            boundingBox.put("y", cutboard.get("Y"));
            boundingBox.put("w", cutboard.get("Width"));
            boundingBox.put("h", cutboard.get("Height"));
            snapshot.put("boundingBox", boundingBox);
        }
        return snapshot;
    }
    
    private JSONObject createSnapshot(JSONObject metadata, PictureInfo pictureInfo) {
        JSONObject objectBoundingBox = metadata.getJSONObject("ObjectBoundingBox");
        JSONObject snapshot = new JSONObject();
        JSONObject boundingBox = new JSONObject();
        if (null != objectBoundingBox) {
            boundingBox.put("x", objectBoundingBox.get("x"));
            boundingBox.put("y", objectBoundingBox.get("y"));
            boundingBox.put("w", objectBoundingBox.get("w"));
            boundingBox.put("h", objectBoundingBox.get("h"));
            snapshot.put("boundingBox", boundingBox);
        }
        snapshot.put("frameIdx", 0);
        snapshot.put("framePts", pictureInfo.getCaptureTime());
        return snapshot;
    }
    
    private void setPulicAttribute(JSONObject jsonObject, String typeCode,
        PictureInfo pictureInfo) {
        
        String uuid = IDUtil.uuid();
        jsonObject.put("objType", typeCode);
        jsonObject.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        jsonObject.put("serialNumber", pictureInfo.getDeviceId());
        jsonObject.put("startFramePts", pictureInfo.getEnterTime());
        jsonObject.put("endFramePts", pictureInfo.getLeaveTime());
        jsonObject.put("bigImgURL", pictureInfo.getPicUrl());
        jsonObject.put("uuid", uuid);
    }
    
    private void setPicUrl(JSONObject jsonObject, String typeCode, PictureInfo pictureInfo) {
        
        JSONObject boundingBox = Optional.ofNullable(jsonObject).map(p -> p.getJSONObject("snapshot"))
                .map(p -> p.getJSONObject("boundingBox")).orElse(null);
        if (null == boundingBox) {
            return ;
        }

        int[] pois = new int[4];
        pois[0] = boundingBox.getIntValue("x");
        pois[1] = boundingBox.getIntValue("y");
        pois[2] = boundingBox.getIntValue("w");
        pois[3] = boundingBox.getIntValue("h");
        try {
            String uuid = jsonObject.getString("uuid");
            String[] picUrl = ImageBaseUtil.getPicUrl(pictureInfo, typeCode, uuid, pois);
            if (picUrl.length > 1) {
                jsonObject.put("imgURL", picUrl[1]);
            }
        } catch (Exception e) {
            log.error("======setPicUrl Exception:", e);
        }
    }
    
    
    /**
     * push data to kafka
     */
    private void pushDataTokafka(List<JSONObject> jsonObjects) {
        for (JSONObject jsonObject : jsonObjects) {
            KafkaUtil.sendMessage(nacosConfig.getKafkaTopicQstsend(),
                    nacosConfig.getKafkaGroupIdQstsend(), jsonObject.toJSONString(),
                    nacosConfig.getKafkaBootstrapQstsend());
        }
    }
    
}
