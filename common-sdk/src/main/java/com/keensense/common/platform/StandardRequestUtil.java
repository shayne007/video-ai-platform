package com.keensense.common.platform;

import com.alibaba.fastjson.JSON;
import com.keensense.common.platform.bo.feature.DumpQuery;
import com.keensense.common.platform.bo.video.AnalysisResultBo;
import com.keensense.common.platform.bo.video.ObjextTaskBo;
import com.keensense.common.platform.bo.video.U2SHttpBo;
import com.keensense.common.platform.constant.StandardUrlConstant;
import com.keensense.common.platform.constant.WebserviceConstant;
import com.keensense.common.platform.domain.NonMotorVehiclesResult;
import com.keensense.common.platform.domain.PersonResult;
import com.keensense.common.platform.domain.VlprResult;
import com.keensense.common.platform.enums.Age;
import com.keensense.common.platform.enums.ObjTypeEnum;
import com.keensense.common.util.HttpU2sGetUtil;
import com.keensense.common.util.StandardHttpUtil;
import com.keensense.common.validator.ValidatorUtils;
import com.loocme.sys.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 按1400标准请求工具类
 *
 * @author shixt
 */
@Slf4j
public class StandardRequestUtil {

    /**
     * 根据类型获取结果 语义搜查询
     *
     * @param bo
     */

    public static String queryAnalysisResult(String requestUrl, AnalysisResultBo bo) {
        StringBuffer requestUrlBuffer = new StringBuffer(requestUrl);
        Map<String, String> params = new HashMap<String, String>();
        //查人
        String objType = bo.getObjType();
        if (String.valueOf(ObjTypeEnum.PERSON.getValue()).equals(objType)) {
            requestUrlBuffer.append(StandardUrlConstant.PERSONS);
        } else if (String.valueOf(ObjTypeEnum.CAR.getValue()).equals(objType)) {
            requestUrlBuffer.append(StandardUrlConstant.MOTORVEHICLES);
        } else if (String.valueOf(ObjTypeEnum.BIKE.getValue()).equals(objType)) {
            requestUrlBuffer.append(StandardUrlConstant.NONMOTORVEHICLES);
        } else if (String.valueOf(ObjTypeEnum.FACES.getValue()).equals(objType)) {
            requestUrlBuffer.append(StandardUrlConstant.FACES);
        } else {
            requestUrlBuffer.append(StandardUrlConstant.RESULT);
        }
        params = buildgetResultsParams(bo);
        String resultJson = StandardHttpUtil.getHttp(requestUrlBuffer.toString(), params);
        return resultJson;
    }

    /**
     * 以图搜图
     *
     * @param bo
     * @return
     */
    public static String getImageSearchResults(String requestUrl, U2SHttpBo bo) {
        StringBuffer requestUrlBuffer = new StringBuffer(requestUrl);
        JSONObject searchParams = new JSONObject();
        JSONObject requestParams = new JSONObject();
        //查人
        if (String.valueOf(ObjTypeEnum.PERSON.getValue()).equals(bo.getObjextType())) {
            requestUrlBuffer.append(StandardUrlConstant.PERSONS_SEARCH);
        } else if (String.valueOf(ObjTypeEnum.CAR.getValue()).equals(bo.getObjextType())) {
            requestUrlBuffer.append(StandardUrlConstant.MOTORVEHICLES_SEARCH);
        } else if (String.valueOf(ObjTypeEnum.BIKE.getValue()).equals(bo.getObjextType())) {
            requestUrlBuffer.append(StandardUrlConstant.NONMOTORVEHICLES_SEARCH);
        } else if (String.valueOf(ObjTypeEnum.FACES.getValue()).equals(bo.getObjextType())) {
            requestUrlBuffer.append(StandardUrlConstant.FACES_SEARCH);
            String faceFeature = bo.getFaceFeature();
            if (StringUtils.isNotEmpty(faceFeature) && faceFeature.equals("1")) {
                requestParams.put("Firm", "1");//格林人脸搜素
            }
        }
        if (StringUtils.isNotEmpty(bo.getFeature())) {
            requestParams.put("Feature", bo.getFeature());
        }
        if (StringUtils.isNotEmpty(bo.getStartTime())) {
            requestParams.put("StartTime", bo.getStartTime());
        }
        if (StringUtils.isNotEmpty(bo.getEndTime())) {
            requestParams.put("EndTime", bo.getEndTime());
        }
        if (null != bo.getThreshold()) {
            requestParams.put("Threshold", bo.getThreshold());
        } else if (null != bo.getTradeOff()) {

            requestParams.put("Threshold", Float.parseFloat(String.valueOf(bo.getTradeOff() * 0.01)));
        }
        List<Map<String, String>> taskList = new ArrayList<>();
        if (StringUtils.isNotEmpty(bo.getSerialNumber())) {
            String serialNumber = bo.getSerialNumber();
            String[] serialNumberArray = serialNumber.split(",");

            for (String s : serialNumberArray) {
                if (StringUtils.isNotEmpty(s)) {
                    HashMap<String, String> task = new HashMap<>();
                    task.put("id", s);
                    if (StringUtils.isNotEmpty(bo.getStartAt())) {
                        task.put("start_at", bo.getStartAt());
                    }
                    taskList.add(task);
                }
            }
        }
        if (StringUtils.isNotEmpty(bo.getCameraId())) {
            requestParams.put("DeviceId", bo.getCameraId());
        }
        if (StringUtils.isNotEmpty(bo.getLimitNum())) {
            requestParams.put("LimitNum", bo.getLimitNum());
        }

        requestParams.put("TaskList", taskList);
        searchParams.put("Search", requestParams);
        Long start1 = System.currentTimeMillis();
        String resultJson = StandardHttpUtil.postContentWithJson(requestUrlBuffer.toString(), searchParams.toString());
        Long end1 = System.currentTimeMillis();
        log.info("提交搜图任务耗时------" + (end1 - start1));
        return resultJson;
    }

    /**
     * 图片特征提取接口
     *
     * @param requestUrl
     * @param inParam
     * @return
     */
    public static String doExtractFromPictureService(String requestUrl, U2SHttpBo inParam) {
        JSONObject featureParams = new JSONObject();
        featureParams.put("objtype", inParam.getObjextType());
        featureParams.put("picture", inParam.getPicture());
        //根据图片获取特征值，现在搜图需要用特征值去搜
        Long start = System.currentTimeMillis();
        String featureRequestUrl = requestUrl + StandardUrlConstant.EXTRACT_FROM_PICTURE;
        String featureResponse = StandardHttpUtil.postContentWithJson(featureRequestUrl, featureParams.toString());
        Long end = System.currentTimeMillis();
        log.info("获取特征值耗时------" + (end - start));
        return featureResponse;
    }


    /**
     * 图片目标检测接口
     *
     * @param requestUrl
     * @param inParam
     * @return
     */
    public static String doStructPictureService(String requestUrl, U2SHttpBo inParam) {
        JSONObject structParams = new JSONObject();
        structParams.put("objtype", inParam.getObjextType());
        structParams.put("picture", inParam.getPicture());
        //根据图片获取特征值，现在搜图需要用特征值去搜
        Long start = System.currentTimeMillis();
        String structRequestUrl = requestUrl + StandardUrlConstant.STRUCT_PICTURE;
        String structResponse = StandardHttpUtil.postContentWithJson(structRequestUrl, structParams.toString());
        Long end = System.currentTimeMillis();
        log.info("获取特征值耗时------" + (end - start));
        return structResponse;
    }

    /**
     * 语义搜查询结果构造查询条件
     */
    public static Map<String, String> buildgetResultsParams(AnalysisResultBo analysisResultBo) {
        Map<String, String> params = new HashMap<String, String>();
        String objextType = analysisResultBo.getObjType();
        if (String.valueOf(ObjTypeEnum.PERSON.getValue()).equals(objextType)) {
            PersonResult personResult = analysisResultBo.getPersonResult();
            if (StringUtil.isNotNull(analysisResultBo.getSourceId())) {
                params.put("Person.SourceID", analysisResultBo.getSourceId());
            }
            if (StringUtil.isNotNull(analysisResultBo.getSerialnumber())) {
                params.put("Person.SerialNumber.In", analysisResultBo.getSerialnumber());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getIds())) {
                params.put("Person.Id.In", analysisResultBo.getIds());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getDeviceId())) {
                params.put("Person.DeviceID.In", analysisResultBo.getDeviceId());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getPageNo())) {
                params.put("Person.RecordStartNo", analysisResultBo.getPageNo());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getPageSize())) {
                params.put("Person.PageRecordNum", analysisResultBo.getPageSize());
            }

            if (StringUtils.isNotEmpty(analysisResultBo.getStartTime())) {
                params.put("Person.MarkTime.Gte", analysisResultBo.getStartTime());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getEndTime())) {
                params.put("Person.MarkTime.Lt", analysisResultBo.getEndTime());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getOrder())) {
                params.put("Person.MarkTime.Order", analysisResultBo.getOrder());
            }
            if (null != personResult) {
                if (null != personResult.getGenderCode()) {
                    params.put("Person.GenderCode", String.valueOf(personResult.getGenderCode()));
                }
                if (null != personResult.getAge()) {
                    String[] ages = getAges(personResult.getAge());
                    params.put("Person.AgeUpLimit", ages[1]);
                    params.put("Person.AgeLowerLimit", ages[0]);
                }
                if (StringUtils.isNotEmpty(personResult.getCoatColor())) {
                    params.put("Person.CoatColor.In", personResult.getCoatColor());
                }
                if (StringUtils.isNotEmpty(personResult.getTrousersColor())) {
                    params.put("Person.TrousersColor.In", personResult.getTrousersColor());
                }
                if (StringUtils.isNotEmpty(personResult.getCoatStyle())) {
                    params.put("Person.CoatStyle", personResult.getCoatStyle());
                }
                if (StringUtils.isNotEmpty(personResult.getTrousersStyle())) {
                    params.put("Person.TrousersStyle", personResult.getTrousersStyle());
                }
                if (StringUtils.isNotEmpty(personResult.getCoatTexture())) {
                    params.put("Person.CoatTexture", personResult.getCoatTexture());
                }
                if (StringUtils.isNotEmpty(personResult.getTrousersTexture())) {
                    params.put("Person.TrousersTexture", personResult.getTrousersTexture());
                }
                if (null != personResult.getAngle()) {
                    params.put("Person.Angle", String.valueOf(personResult.getAngle()));
                }
                if (null != personResult.getBag()) {
                    params.put("Person.Bag", String.valueOf(personResult.getBag()));
                }
                if (null != personResult.getCap()) {
                    params.put("Person.Cap", personResult.getCap());
                }
                if (null != personResult.getRespirator()) {
                    params.put("Person.Respirator", personResult.getRespirator());
                }
                if (null != personResult.getHairStyle()) {
                    params.put("Person.HairStyle", personResult.getHairStyle());
                }
                if (null != personResult.getHandbag()) {
                    params.put("Person.Handbag", String.valueOf(personResult.getHandbag()));
                }
                if (null != personResult.getUpperClothing()) {
                    params.put("Person.UpperClothing", personResult.getUpperClothing());
                }
                if (null != personResult.getUmbrella()) {
                    params.put("Person.Umbrella", String.valueOf(personResult.getUmbrella()));
                }
                if (null != personResult.getGlasses()) {
                    params.put("Person.Glasses", String.valueOf(personResult.getGlasses()));
                }
                if (null != personResult.getLowerClothing()) {
                    params.put("Person.LowerClothing", personResult.getLowerClothing());
                }
                if (null != personResult.getTrolley()) {
                    params.put("Person.Trolley", String.valueOf(personResult.getTrolley()));
                }
                if (null != personResult.getLuggage()) {
                    params.put("Person.Luggage", String.valueOf(personResult.getLuggage()));
                }
                if (null != personResult.getHasKnife()) {
                    params.put("Person.HasKnife", String.valueOf(personResult.getHasKnife()));
                }
                if (null != personResult.getMinority()) {
                    params.put("Person.Minority", String.valueOf(personResult.getMinority()));
                }
                if (null != personResult.getChestHold()) {
                    params.put("Person.ChestHold", String.valueOf(personResult.getChestHold()));
                }
                if (null != personResult.getShape()) {
                    params.put("Person.Shape", String.valueOf(personResult.getShape()));
                }

            }
        } else if (String.valueOf(ObjTypeEnum.CAR.getValue()).equals(objextType)) {
            VlprResult vlprResult = analysisResultBo.getVlprResult();
            if (StringUtil.isNotNull(analysisResultBo.getSourceId())) {
                params.put("MotorVehicle.SourceID", analysisResultBo.getSourceId());
            }
            if (StringUtil.isNotNull(analysisResultBo.getSerialnumber())) {
                params.put("MotorVehicle.SerialNumber.In", analysisResultBo.getSerialnumber());
            }
            if (null != vlprResult) {
                if (StringUtils.isNotEmpty(vlprResult.getVehicleColor())) {
                    params.put("MotorVehicle.VehicleColor.In", vlprResult.getVehicleColor());
                }
                if (StringUtils.isNotEmpty(vlprResult.getPlateNo())) {
                    params.put("MotorVehicle.PlateNo.Like", vlprResult.getPlateNo());
                }
                if (StringUtils.isNotEmpty(vlprResult.getPlateColor())) {
                    params.put("MotorVehicle.PlateColor", vlprResult.getPlateColor());
                }
                if (StringUtils.isNotEmpty(vlprResult.getVehicleBrand())) {
                    params.put("MotorVehicle.VehicleBrand", vlprResult.getVehicleBrand());
                }
                if (StringUtils.isNotEmpty(vlprResult.getVehicleModel())) {
                    params.put("MotorVehicle.VehicleModel", vlprResult.getVehicleModel());
                }
                if (StringUtils.isNotEmpty(vlprResult.getVehicleClass())) {
                    params.put("MotorVehicle.VehicleClass.In", vlprResult.getVehicleClass());
                }
                if (StringUtils.isNotEmpty(vlprResult.getDrop())) {
                    params.put("MotorVehicle.drop", vlprResult.getDrop());
                }
                if (StringUtils.isNotEmpty(vlprResult.getDirection())) {
                    params.put("MotorVehicle.Direction", vlprResult.getDirection());
                }
                if (StringUtils.isNotEmpty(vlprResult.getPaper())) {
                    params.put("MotorVehicle.paper", vlprResult.getPaper());
                }
                if (StringUtils.isNotEmpty(vlprResult.getTagNum())) {
                    params.put("MotorVehicle.TagNum", vlprResult.getTagNum());
                }
                if (StringUtils.isNotEmpty(vlprResult.getVehicleModel())) {
                    params.put("MotorVehicle.VehicleModel", vlprResult.getVehicleModel());
                }
                if (StringUtils.isNotEmpty((vlprResult.getDecoration()))) {
                    params.put("MotorVehicle.Decoration", vlprResult.getDecoration());
                }
                if (null != vlprResult.getSafetyBelt()) {
                    params.put("MotorVehicle.SafetyBelt", vlprResult.getSafetyBelt().toString());
                }
                if (null != vlprResult.getSecondBelt()) {
                    params.put("MotorVehicle.SecondBelt", vlprResult.getSecondBelt().toString());
                }
                if (null != vlprResult.getCalling()) {
                    params.put("MotorVehicle.Calling", vlprResult.getCalling().toString());
                }
                if (null != vlprResult.getRack()) {
                    params.put("MotorVehicle.Rack", vlprResult.getRack().toString());
                }
                if (null != vlprResult.getSunvisor()) {
                    params.put("MotorVehicle.Sun", vlprResult.getSunvisor().toString());
                }
                if (null != vlprResult.getSunRoof()) {
                    params.put("MotorVehicle.sunRoof", vlprResult.getSunRoof().toString());
                }
                if (null != vlprResult.getAngle()) {
                    params.put("MotorVehicle.Angle", vlprResult.getAngle().toString());
                }
                if (null != vlprResult.getHasCrash()) {
                    params.put("MotorVehicle.HasCrash", vlprResult.getHasCrash());
                }
                if (null != vlprResult.getHasDanger()) {
                    params.put("MotorVehicle.HasDanger", vlprResult.getHasDanger());
                }
                if (null != vlprResult.getPlateClass()) {
                    params.put("MotorVehicle.PlateClass", vlprResult.getPlateClass());
                }
                if (StringUtils.isNotEmpty(vlprResult.getHasPlate())) {
                    params.put("MotorVehicle.Hasplate", vlprResult.getHasPlate());
                }
                if (StringUtils.isNotEmpty(vlprResult.getAerial())) {
                    params.put("MotorVehicle.aerial", vlprResult.getAerial());
                }
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getIds())) {
                params.put("MotorVehicle.Id.In", analysisResultBo.getIds());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getDeviceId())) {
                params.put("MotorVehicle.DeviceID.In", analysisResultBo.getDeviceId());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getPageNo())) {
                params.put("MotorVehicle.RecordStartNo", analysisResultBo.getPageNo());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getPageSize())) {
                params.put("MotorVehicle.PageRecordNum", analysisResultBo.getPageSize());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getStartTime())) {
                params.put("MotorVehicle.MarkTime.Gte", analysisResultBo.getStartTime());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getEndTime())) {
                params.put("MotorVehicle.MarkTime.Lt", analysisResultBo.getEndTime());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getOrder())) {
                params.put("MotorVehicle.MarkTime.Order", analysisResultBo.getOrder());
            }
        } else if (String.valueOf(ObjTypeEnum.BIKE.getValue()).equals(objextType)) {
            NonMotorVehiclesResult nonMotorVehiclesResult = analysisResultBo.getNonMotorVehiclesResult();
            String ot = "NonMotorVehicle";
            if (StringUtil.isNotNull(analysisResultBo.getSourceId())) {
                params.put(ot + ".SourceID", analysisResultBo.getSourceId());
            }
            if (StringUtil.isNotNull(analysisResultBo.getSerialnumber())) {
                params.put(ot + ".SerialNumber.In", analysisResultBo.getSerialnumber());
            }
            if (null != nonMotorVehiclesResult) {
                if (null != nonMotorVehiclesResult.getSex()) {
                    params.put(ot + ".Sex", String.valueOf(nonMotorVehiclesResult.getSex()));
                }
                if (null != nonMotorVehiclesResult.getAge()) {
                    params.put(ot + ".Age", String.valueOf(nonMotorVehiclesResult.getAge()));
                }
                if (null != nonMotorVehiclesResult.getWheels()) {
                    Byte wheels = nonMotorVehiclesResult.getWheels();
                    if(wheels == 2){
                        params.put(ot + ".Wheels.In", "1,"+String.valueOf(nonMotorVehiclesResult.getWheels()));
                    }else{
                        params.put(ot + ".Wheels.In", String.valueOf(nonMotorVehiclesResult.getWheels()));
                    }
                }
                if (null != nonMotorVehiclesResult.getAngle()) {
                    params.put(ot + ".Angle", String.valueOf(nonMotorVehiclesResult.getAngle()));
                }
                if (null != nonMotorVehiclesResult.getBikeGenre()) {
                    params.put(ot + ".BikeGenre.In", String.valueOf(nonMotorVehiclesResult.getBikeGenre()));
                }
                if (null != nonMotorVehiclesResult.getCoatStyle()) {
                    params.put(ot + ".CoatStyle.In", nonMotorVehiclesResult.getCoatStyle());
                }
                if (StringUtils.isNotEmpty(nonMotorVehiclesResult.getCoatColor1())) {
                    params.put(ot + ".coatcolor1.In", nonMotorVehiclesResult.getCoatColor1());
                }
                if (null != nonMotorVehiclesResult.getCoatTexture()) {
                    params.put(ot + ".CoatTexture", String.valueOf(nonMotorVehiclesResult.getCoatTexture()));
                }
                if (null != nonMotorVehiclesResult.getGlasses()) {
                    params.put(ot + ".Glasses", String.valueOf(nonMotorVehiclesResult.getGlasses()));
                }
                if (null != nonMotorVehiclesResult.getRespirator()) {
                    params.put(ot + ".Respirator", String.valueOf(nonMotorVehiclesResult.getRespirator()));
                }
                if (StringUtils.isNotEmpty(nonMotorVehiclesResult.getVehicleColor())) {
                    params.put(ot + ".VehicleColor", nonMotorVehiclesResult.getVehicleColor());
                }
                if (null != nonMotorVehiclesResult.getBag()) {
                    params.put(ot + ".Bag", String.valueOf(nonMotorVehiclesResult.getBag()));
                }
                if (null != nonMotorVehiclesResult.getHelmet()) {
                    params.put(ot + ".Helmet", String.valueOf(nonMotorVehiclesResult.getHelmet()));
                }
                if (StringUtils.isNotEmpty(nonMotorVehiclesResult.getHelmetColorTag1())) {
                    params.put(ot + ".helmetcolortag1.In", nonMotorVehiclesResult.getHelmetColorTag1());
                }
                if (null != nonMotorVehiclesResult.getHasPlate()) {
                    params.put(ot + ".HasPlate", String.valueOf(nonMotorVehiclesResult.getHasPlate()));
                }
                if (null != nonMotorVehiclesResult.getUmbrella()) {
                    params.put(ot + ".Umbrella", String.valueOf(nonMotorVehiclesResult.getUmbrella()));
                }
                if (StringUtils.isNotEmpty(nonMotorVehiclesResult.getLampShape())) {
                    params.put(ot + ".LampShape", nonMotorVehiclesResult.getLampShape());
                }
                if (StringUtils.isNotEmpty(nonMotorVehiclesResult.getCarryPassenger())) {
                    params.put(ot + ".CarryPassenger", nonMotorVehiclesResult.getCarryPassenger());
                }
                if (null != nonMotorVehiclesResult.getSocialAttribute()) {
                    params.put(ot + ".SocialAttribute", String.valueOf(nonMotorVehiclesResult.getSocialAttribute()));
                }
                if (null != nonMotorVehiclesResult.getEnterprise()) {
                    params.put(ot + ".Enterprise", String.valueOf(nonMotorVehiclesResult.getEnterprise()));
                }
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getIds())) {
                params.put(ot + ".Id.In", analysisResultBo.getIds());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getDeviceId())) {
                params.put(ot + ".DeviceID.In", analysisResultBo.getDeviceId());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getPageNo())) {
                params.put(ot + ".RecordStartNo", analysisResultBo.getPageNo());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getPageSize())) {
                params.put(ot + ".PageRecordNum", analysisResultBo.getPageSize());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getStartTime())) {
                params.put(ot + ".MarkTime.Gte", analysisResultBo.getStartTime());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getEndTime())) {
                params.put(ot + ".MarkTime.Lt", analysisResultBo.getEndTime());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getOrder())) {
                params.put(ot + ".MarkTime.Order", analysisResultBo.getOrder());
            }

        } else if (String.valueOf(ObjTypeEnum.FACES.getValue()).equals(objextType)) {
            if (StringUtils.isNotEmpty(analysisResultBo.getIds())) {
                params.put("Faces.Id.In", analysisResultBo.getIds());
            }
            if (StringUtil.isNotNull(analysisResultBo.getSerialnumber())) {
                params.put("Faces.SerialNumber.In", analysisResultBo.getSerialnumber());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getDeviceId())) {
                params.put("Faces.DeviceID.In", analysisResultBo.getDeviceId());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getPageNo())) {
                params.put("Faces.RecordStartNo", analysisResultBo.getPageNo());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getPageSize())) {
                params.put("Faces.PageRecordNum", analysisResultBo.getPageSize());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getStartTime())) {
                params.put("Faces.MarkTime.Gte", analysisResultBo.getStartTime());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getEndTime())) {
                params.put("Faces.MarkTime.Lt", analysisResultBo.getEndTime());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getOrder())) {
                params.put("Faces.MarkTime.Order", analysisResultBo.getOrder());
            }
        } else {
            if (StringUtil.isNotNull(analysisResultBo.getIds())) {
                params.put("Result.Id.In", analysisResultBo.getIds());
            }
            if (StringUtil.isNotNull(analysisResultBo.getSourceId())) {
                params.put("Result.SourceID", analysisResultBo.getSourceId());
            }
            if (StringUtil.isNotNull(analysisResultBo.getSerialnumber())) {
                params.put("Result.SerialNumber.In", analysisResultBo.getSerialnumber());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getDeviceId())) {
                params.put("Result.DeviceId.In", analysisResultBo.getDeviceId());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getPageSize())) {
                params.put("Result.PageRecordNum", analysisResultBo.getPageSize());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getPageNo())) {
                params.put("Result.RecordStartNo", analysisResultBo.getPageNo());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getStartTime())) {
                params.put("Result.MarkTime.Gte", analysisResultBo.getStartTime());
            }
            if (StringUtils.isNotEmpty(analysisResultBo.getEndTime())) {
                params.put("Result.MarkTime.Lt", analysisResultBo.getEndTime());
            }
//            params.put("Result.ObjType.In", "1,2,4");
            if (StringUtils.isNotEmpty(analysisResultBo.getOrder())) {
                params.put("Result.MarkTime.Order", analysisResultBo.getOrder());
            }
        }
        return params;
    }

    /**
     * 分组查询
     *
     * @return
     */
    public static String groupByQuery(String requestUrl, AnalysisResultBo u2SHttpBo) {
        StringBuffer requestUrlBuffer = new StringBuffer(requestUrl);
        Integer objType = Integer.parseInt(u2SHttpBo.getObjType());
        if (ObjTypeEnum.PERSON.getValue() == objType) {
            requestUrlBuffer.append(StandardUrlConstant.PERSON_GROUPBY);
        } else if (ObjTypeEnum.CAR.getValue() == objType) {
            requestUrlBuffer.append(StandardUrlConstant.MOTORVEHICLES_GROUPBY);
        } else if (ObjTypeEnum.BIKE.getValue() == objType) {
            requestUrlBuffer.append(StandardUrlConstant.NONMOTORVEHICLES_GROUPBY);
        } else if (ObjTypeEnum.FACES.getValue() == objType) {
            requestUrlBuffer.append(StandardUrlConstant.FACES_GROUPBY);
        }
        com.alibaba.fastjson.JSONObject request = new com.alibaba.fastjson.JSONObject();
        JSONArray params = new JSONArray();
        if (StringUtils.isNotEmpty(u2SHttpBo.getDeviceId())) {
            com.alibaba.fastjson.JSONObject deviceId = new com.alibaba.fastjson.JSONObject();
            deviceId.put("field", "deviceid");
            deviceId.put("type", "TERM");
            deviceId.put("value", u2SHttpBo.getDeviceId());
            params.add(deviceId);
        }
        com.alibaba.fastjson.JSONObject objtype = new com.alibaba.fastjson.JSONObject();
        objtype.put("field", "objtype");
        objtype.put("type", "TERM");
        objtype.put("value", objType);
        params.add(objtype);
        com.alibaba.fastjson.JSONObject field1 = new com.alibaba.fastjson.JSONObject();
        field1.put("field", "marktime");
        field1.put("type", "GROUPBYDATE");
        field1.put("value", "hour");

        com.alibaba.fastjson.JSONObject field2 = new com.alibaba.fastjson.JSONObject();
        field2.put("field", "marktime");
        field2.put("type", "RANGE");
        JSONArray data = new JSONArray();
        data.add(u2SHttpBo.getStartTime());
        data.add(u2SHttpBo.getEndTime());
        field2.put("value", data);

        params.add(field1);
        params.add(field2);
        request.put("params", params);
        String paramJson = JSON.toJSONString(request);
        String responseJson = StandardHttpUtil.postContentWithJson(requestUrlBuffer.toString(), paramJson);
        System.out.println(responseJson);
        return responseJson;
    }

    /**
     * 按監控點分組查詢
     *
     * @param bo
     */
    public String getGroupByResults(String requestUrl, U2SHttpBo bo) {
        StringBuffer requestUrlBuffer = new StringBuffer(requestUrl);
        //StringBuffer requestUrl = new StringBuffer("http://192.168.0.70:9999");
        Map<String, String> params = new HashMap<String, String>();
        //查人
        if (String.valueOf(ObjTypeEnum.PERSON.getValue()).equals(bo.getObjextType())) {
            requestUrlBuffer.append(StandardUrlConstant.PERSON_GROUPBY);
        } else if (String.valueOf(ObjTypeEnum.CAR.getValue()).equals(bo.getObjextType())) {
            requestUrlBuffer.append(StandardUrlConstant.MOTORVEHICLES_GROUPBY);
        } else if (String.valueOf(ObjTypeEnum.BIKE.getValue()).equals(bo.getObjextType())) {
            requestUrlBuffer.append(StandardUrlConstant.NONMOTORVEHICLES_GROUPBY);
        } else if (String.valueOf(ObjTypeEnum.FACES.getValue()).equals(bo.getObjextType())) {
            requestUrlBuffer.append(StandardUrlConstant.FACES_GROUPBY);
        }
        JSONObject requestParams = new JSONObject();

        String resultJson = StandardHttpUtil.getHttp(requestUrl.toString(), params);
        return resultJson;
    }

    public static String[] getAges(int age) {
        String[] ages = new String[2];
        if (Age.CHILD.getValue() == age) {
            ages[0] = "1";
            ages[1] = "16";
        } else if (Age.YOUNG.getValue() == age) {
            ages[0] = "17";
            ages[1] = "30";
        } else if (Age.MIDDLE.getValue() == age) {
            ages[0] = "31";
            ages[1] = "55";
        } else if (Age.OLD.getValue() == age) {
            ages[0] = "56";
            ages[1] = "100";
        } else {
            ages[0] = "0";
            ages[1] = "0";
        }
        return ages;
    }

    public static void main(String[] args) {
        System.out.println(67 * 0.01);

    }


    /**
     * 重试失败的分片任务
     */
    public static String retryTask(String requestUrl, ObjextTaskBo bo) {
        requestUrl += WebserviceConstant.RETRY_VTASK_INTER_NAME;
        JSONObject requestParams = new JSONObject();
        requestParams.put("serialnumber", bo.getSerialnumber());//任务序列号
        String addVideoObjextTaskReponse = HttpU2sGetUtil.postContent(requestUrl, requestParams.toString());
        return addVideoObjextTaskReponse;
    }

    public static String stopGateTask(String requestUrl, ObjextTaskBo bo) {
        JSONObject requestParams = new JSONObject();
        requestParams.put("serialnumber", bo.getSerialnumber());//任务序列号
        requestParams.put("cameraId", bo.getCameraId());
        requestUrl += WebserviceConstant.STOP_PICTURE_TASK;
        String addVideoObjextTaskReponse = HttpU2sGetUtil.postContent(requestUrl, requestParams.toString());
        return addVideoObjextTaskReponse;
    }

    public static String deleteGateTask(String requestUrl, ObjextTaskBo bo) {
        JSONObject requestParams = new JSONObject();
        requestParams.put("serialnumber", bo.getSerialnumber());//任务序列号
        requestParams.put("cameraId", bo.getCameraId());
        requestUrl += WebserviceConstant.DELETE_PICTURE_TASK;
        String addVideoObjextTaskReponse = HttpU2sGetUtil.postContent(requestUrl, requestParams.toString());
        return addVideoObjextTaskReponse;
    }

    public static String startGateTask(String requestUrl, ObjextTaskBo bo) {
        JSONObject requestParams = new JSONObject();
        requestParams.put("serialnumber", bo.getSerialnumber());//任务序列号
        requestParams.put("cameraId", bo.getCameraId());
        requestUrl += WebserviceConstant.START_PICTURE_TASK;
        String addVideoObjextTaskReponse = HttpU2sGetUtil.postContent(requestUrl, requestParams.toString());
        return addVideoObjextTaskReponse;
    }

    public static String startNewGateTask(String requestUrl, ObjextTaskBo bo) {
        JSONObject requestParams = new JSONObject();
        requestParams.put("url", bo.getUrl());
        requestParams.put("serialnumber", bo.getSerialnumber());//任务序列号
        requestParams.put("type", bo.getType().getValue());
        requestParams.put("taskType", bo.getTaskType().getValue());
        requestParams.put("name", bo.getName());
        requestParams.put("cameraId", bo.getCameraId());
        requestUrl += WebserviceConstant.ADD_VIVEO_OBJECT_TASK;
        String addVideoObjextTaskReponse = HttpU2sGetUtil.postContent(requestUrl, requestParams.toString());
        return addVideoObjextTaskReponse;
    }

    public static String deleteTaskData(String requestUrl, ObjextTaskBo bo) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("Serialnumber", bo.getSerialnumber());//任务序列号
        requestUrl += WebserviceConstant.DELETE_PICTURE_TASK_ASYNC;
        String resultJson = StandardHttpUtil.deleteHttp(requestUrl, paramMap);
        return resultJson;
    }

    public static String doDumpService(String requestUrl, DumpQuery dumpQuery) {
        ValidatorUtils.validateEntity(dumpQuery);
        JSONObject requestParams = new JSONObject();
        requestParams.put("task", dumpQuery.getTask());
        requestParams.put("from", dumpQuery.getFrom());//任务序列号
        requestParams.put("to", dumpQuery.getTo());
        requestParams.put("type", dumpQuery.getObjType());
        if (StringUtils.isNotEmpty(dumpQuery.getStartAt())) {
            requestParams.put("start_at", dumpQuery.getStartAt());
        }
        requestParams.put("uuid", "");
        requestUrl += "/dump";
        return HttpU2sGetUtil.postContent(requestUrl, requestParams.toString());
    }

    public static String doFeatureDumpService(String requestUrl, DumpQuery dumpQuery) {
        ValidatorUtils.validateEntity(dumpQuery);
        JSONObject requestParams = new JSONObject();
        //任务序列号
        requestParams.put("task", dumpQuery.getTask());
        if (StringUtils.isNotEmpty(dumpQuery.getFrom())) {
            requestParams.put("from", dumpQuery.getFrom());
        }
        if (StringUtils.isNotEmpty(dumpQuery.getTo())) {
            requestParams.put("to", dumpQuery.getTo());
        }
        requestParams.put("type", dumpQuery.getObjType());
        if (StringUtils.isNotEmpty(dumpQuery.getStartAt())) {
            requestParams.put("start_at", dumpQuery.getStartAt());
        }
        requestParams.put("uuid", dumpQuery.getUuid());
        requestUrl += WebserviceConstant.FEATURE_DUMP;
        return HttpU2sGetUtil.postContent(requestUrl, requestParams.toString());
    }

    public static String deleteResultDate(String requestUrl, ObjextTaskBo bo) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("Id", bo.getUuid());//任务序列号
        requestUrl += StandardUrlConstant.DELETE_RESULT_DATA;
        String resultJson = StandardHttpUtil.deleteHttp(requestUrl, paramMap);
        return resultJson;
    }

    public static String updateResultDate(String requestUrl, AnalysisResultBo bo) {
        StringBuffer requestUrlBuffer = new StringBuffer(requestUrl);
        //查人
        String objType = bo.getObjType();
        if (String.valueOf(ObjTypeEnum.PERSON.getValue()).equals(objType)) {
            requestUrlBuffer.append(StandardUrlConstant.PERSONS_UPDATE);
        } else if (String.valueOf(ObjTypeEnum.CAR.getValue()).equals(objType)) {
            requestUrlBuffer.append(StandardUrlConstant.MOTORVEHICLES_UPDATE);
        } else if (String.valueOf(ObjTypeEnum.BIKE.getValue()).equals(objType)) {
            requestUrlBuffer.append(StandardUrlConstant.NONMOTORVEHICLES_UPDATE);
        }
        com.alibaba.fastjson.JSONObject params = buildUpdataResultParams(bo);
        String resultJson = StandardHttpUtil.postContentWithJson(requestUrlBuffer.toString(), params.toJSONString());
        return resultJson;
    }

    public static com.alibaba.fastjson.JSONObject buildUpdataResultParams(AnalysisResultBo analysisResultBo) {
        com.alibaba.fastjson.JSONObject params = new com.alibaba.fastjson.JSONObject();
        String objType = analysisResultBo.getObjType();
        if (String.valueOf(ObjTypeEnum.PERSON.getValue()).equals(objType)) {
            //PersonListObject
            com.alibaba.fastjson.JSONObject personListObject = new com.alibaba.fastjson.JSONObject();
            //PersonObject
            com.alibaba.fastjson.JSONArray PersonObject = new com.alibaba.fastjson.JSONArray();
            //PersonObject中的每一个实体
            com.alibaba.fastjson.JSONObject personJsonArryObject = new com.alibaba.fastjson.JSONObject();
            //data
            com.alibaba.fastjson.JSONObject personJSONData = new com.alibaba.fastjson.JSONObject();

            //获取人形对象
            PersonResult personResult = analysisResultBo.getPersonResult();
            //添加细类属性
            if (null != personResult.getGenderCode()) {
                personJSONData.put("GenderCode", String.valueOf(personResult.getGenderCode()));
            }
            if (null != personResult.getAge()) {
                String[] ages = getAges(personResult.getAge());
                personJSONData.put("AgeUpLimit", ages[1]);
                personJSONData.put("AgeLowerLimit", ages[0]);
            }
            if (null != personResult.getMinority()) {
                personJSONData.put("Minority", String.valueOf(personResult.getMinority()));
            }
            if (null != personResult.getShape()) {
                personJSONData.put("Shape", String.valueOf(personResult.getShape()));
            }
            if (StringUtils.isNotEmpty(personResult.getHairStyle())) {
                personJSONData.put("HairStyle", personResult.getHairStyle());
            }
            if (StringUtils.isNotEmpty(personResult.getCoatStyle())) {
                personJSONData.put("CoatStyle", personResult.getCoatStyle());
            }
            if (StringUtils.isNotEmpty(personResult.getCoatColor())) {
                personJSONData.put("CoatColor", personResult.getCoatColor());
            }
            if (StringUtils.isNotEmpty(personResult.getTrousersStyle())) {
                personJSONData.put("TrousersStyle", personResult.getTrousersStyle());
            }
            if (StringUtils.isNotEmpty(personResult.getTrousersColor())) {
                personJSONData.put("TrousersColor", personResult.getTrousersColor());
            }
            if (null != personResult.getAngle()) {
                personJSONData.put("Angle", String.valueOf(personResult.getAngle()));
            }
            if (null != personResult.getHandbag()) {
                personJSONData.put("Handbag", String.valueOf(personResult.getHandbag()));
            }
            if (StringUtils.isNotEmpty(personResult.getCap())) {
                personJSONData.put("Cap", personResult.getCap());
            }
            if (StringUtils.isNotEmpty(personResult.getCoatTexture())) {
                personJSONData.put("CoatTexture", personResult.getCoatTexture());
            }
            if (StringUtils.isNotEmpty(personResult.getTrousersTexture())) {
                personJSONData.put("TrousersTexture", personResult.getTrousersTexture());
            }
            if (null != personResult.getTrolley()) {
                personJSONData.put("Trolley", String.valueOf(personResult.getTrolley()));
            }
            if (null != personResult.getLuggage()) {
                personJSONData.put("Luggage", String.valueOf(personResult.getLuggage()));
            }
            if (null != personResult.getHasKnife()) {
                personJSONData.put("HasKnife", String.valueOf(personResult.getHasKnife()));
            }
            if (StringUtils.isNotEmpty(personResult.getRespirator())) {
                personJSONData.put("Respirator", personResult.getRespirator());
            }
            if (null != personResult.getBag()) {
                personJSONData.put("Bag", String.valueOf(personResult.getBag()));
            }
            if (null != personResult.getGlasses()) {
                personJSONData.put("Glasses", String.valueOf(personResult.getGlasses()));
            }
            if (null != personResult.getUmbrella()) {
                personJSONData.put("Umbrella", String.valueOf(personResult.getUmbrella()));
            }
            if (null != personResult.getChestHold()) {
                personJSONData.put("ChestHold", String.valueOf(personResult.getChestHold()));
            }

            personJsonArryObject.put("Data", personJSONData);
            personJsonArryObject.put("PersonID", personResult.getPersonID());
            PersonObject.add(personJsonArryObject);
            personListObject.put("PersonObject", PersonObject);
            params.put("PersonListObject", personListObject);
        } else if (String.valueOf(ObjTypeEnum.BIKE.getValue()).equals(objType)) {
            //NonMotorVehicleListObject
            com.alibaba.fastjson.JSONObject nonMotorVehicleListObject = new com.alibaba.fastjson.JSONObject();
            //NonMotorVehicleObject
            com.alibaba.fastjson.JSONArray nonMotorVehicleObject = new com.alibaba.fastjson.JSONArray();
            //NonMotorVehicleObject中的每一个实体
            com.alibaba.fastjson.JSONObject nonMotorVehicleArrayObject = new com.alibaba.fastjson.JSONObject();
            //NonMotorVehicleArrayObject中的Data
            com.alibaba.fastjson.JSONObject nonMotorVehicleArrayObjectData = new com.alibaba.fastjson.JSONObject();

            //获取骑行对象
            NonMotorVehiclesResult nonMotorVehiclesResult = analysisResultBo.getNonMotorVehiclesResult();
            //添加细类属性
            if (null != nonMotorVehiclesResult.getHasPlate()) {
                nonMotorVehicleArrayObjectData.put("HasPlate", String.valueOf(nonMotorVehiclesResult.getHasPlate()));
            }
            if (null != nonMotorVehiclesResult.getSex()) {
                nonMotorVehicleArrayObjectData.put("Sex", String.valueOf(nonMotorVehiclesResult.getSex()));
            }
            if (null != nonMotorVehiclesResult.getAge()) {
                nonMotorVehicleArrayObjectData.put("Age", String.valueOf(nonMotorVehiclesResult.getAge()));
            }
            if (null != nonMotorVehiclesResult.getHelmet()) {
                nonMotorVehicleArrayObjectData.put("Helmet", String.valueOf(nonMotorVehiclesResult.getHelmet()));
            }
            if (null != nonMotorVehiclesResult.getHelmetColorTag1()) {
                nonMotorVehicleArrayObjectData.put("HelmetColorTag1", nonMotorVehiclesResult.getHelmetColorTag1());
            }
            if (null != nonMotorVehiclesResult.getGlasses()) {
                nonMotorVehicleArrayObjectData.put("Glasses", String.valueOf(nonMotorVehiclesResult.getGlasses()));
            }
            if (null != nonMotorVehiclesResult.getBag()) {
                nonMotorVehicleArrayObjectData.put("Bag", String.valueOf(nonMotorVehiclesResult.getBag()));
            }
            if (null != nonMotorVehiclesResult.getUmbrella()) {
                nonMotorVehicleArrayObjectData.put("Umbrella", String.valueOf(nonMotorVehiclesResult.getUmbrella()));
            }
            if (null != nonMotorVehiclesResult.getAngle()) {
                nonMotorVehicleArrayObjectData.put("Angle", String.valueOf(nonMotorVehiclesResult.getAngle()));
            }
            if (StringUtils.isNotEmpty(nonMotorVehiclesResult.getCoatStyle())) {
                nonMotorVehicleArrayObjectData.put("CoatStyle", nonMotorVehiclesResult.getCoatStyle());
            }
            if (null != nonMotorVehiclesResult.getRespirator()) {
                nonMotorVehicleArrayObjectData.put("Respirator", String.valueOf(nonMotorVehiclesResult.getRespirator()));
            }
            if (null != nonMotorVehiclesResult.getCoatTexture()) {
                nonMotorVehicleArrayObjectData.put("CoatTexture", String.valueOf(nonMotorVehiclesResult.getCoatTexture()));
            }
            if (null != nonMotorVehiclesResult.getWheels()) {
                nonMotorVehicleArrayObjectData.put("Wheels", String.valueOf(nonMotorVehiclesResult.getWheels()));
            }
            if (null != nonMotorVehiclesResult.getBikeGenre()) {
                nonMotorVehicleArrayObjectData.put("BikeGenre", String.valueOf(nonMotorVehiclesResult.getBikeGenre()));
            }
            if (StringUtils.isNotEmpty(nonMotorVehiclesResult.getCoatColor1())) {
                nonMotorVehicleArrayObjectData.put("CoatColor1", nonMotorVehiclesResult.getCoatColor1());
            }
            if (StringUtils.isNotEmpty(nonMotorVehiclesResult.getLampShape())) {
                nonMotorVehicleArrayObjectData.put("LampShape", nonMotorVehiclesResult.getLampShape());
            }
            if (StringUtils.isNotEmpty(nonMotorVehiclesResult.getCarryPassenger())) {
                nonMotorVehicleArrayObjectData.put("CarryPassenger", nonMotorVehiclesResult.getCarryPassenger());
            }

            nonMotorVehicleArrayObject.put("NonMotorVehicleID", nonMotorVehiclesResult.getNonMotorVehicleID());
            nonMotorVehicleArrayObject.put("Data", nonMotorVehicleArrayObjectData);
            nonMotorVehicleObject.add(nonMotorVehicleArrayObject);
            nonMotorVehicleListObject.put("NonMotorVehicleObject", nonMotorVehicleObject);
            params.put("NonMotorVehicleListObject", nonMotorVehicleListObject);

        } else if (String.valueOf(ObjTypeEnum.CAR.getValue()).equals(objType)) {
            //MotorVehicleListObject
            com.alibaba.fastjson.JSONObject MotorVehicleListObject = new com.alibaba.fastjson.JSONObject();
            //MotorVehicleObject
            com.alibaba.fastjson.JSONArray MotorVehicleObject = new com.alibaba.fastjson.JSONArray();
            //MotorVehicleObject中的实体
            com.alibaba.fastjson.JSONObject MotorVehicleArrayObject = new com.alibaba.fastjson.JSONObject();
            //Data
            com.alibaba.fastjson.JSONObject MotorVehicleArrayObjectData = new com.alibaba.fastjson.JSONObject();

            //获取车辆对象
            VlprResult vlprResult = analysisResultBo.getVlprResult();
            //获取细类属性值
            if (StringUtils.isNotEmpty(vlprResult.getHasPlate())) {
                MotorVehicleArrayObjectData.put("HasPlate", vlprResult.getHasPlate());
            }
            if (StringUtils.isNotEmpty(vlprResult.getPlateClass())) {
                MotorVehicleArrayObjectData.put("PlateClass", vlprResult.getPlateClass());
            }
            if (StringUtils.isNotEmpty(vlprResult.getPlateColor())) {
                MotorVehicleArrayObjectData.put("PlateColor", vlprResult.getPlateColor());
            }
            if (StringUtils.isNotEmpty(vlprResult.getPlateNo())) {
                MotorVehicleArrayObjectData.put("PlateNo", vlprResult.getPlateNo());
            }
            if (null != vlprResult.getSpeed()) {
                MotorVehicleArrayObjectData.put("Speed", String.valueOf(vlprResult.getSpeed()));
            }
            if (StringUtils.isNotEmpty(vlprResult.getDirection())) {
                MotorVehicleArrayObjectData.put("Direction", vlprResult.getDirection());
            }
            if (StringUtils.isNotEmpty(vlprResult.getVehicleClass())) {
                MotorVehicleArrayObjectData.put("VehicleClass", vlprResult.getVehicleClass());
            }
            if (StringUtils.isNotEmpty(vlprResult.getVehicleBrand())) {
                MotorVehicleArrayObjectData.put("VehicleBrand", vlprResult.getVehicleBrand());
            }
            if (StringUtils.isNotEmpty(vlprResult.getVehicleStyles())) {
                MotorVehicleArrayObjectData.put("VehicleStyles", vlprResult.getVehicleStyles());
            }
            if (StringUtils.isNotEmpty(vlprResult.getVehicleColor())) {
                MotorVehicleArrayObjectData.put("VehicleColor", vlprResult.getVehicleColor());
            }
            if (null != vlprResult.getSafetyBelt()) {
                MotorVehicleArrayObjectData.put("SafetyBelt", String.valueOf(vlprResult.getSafetyBelt()));
            }
            if (null != vlprResult.getCalling()) {
                MotorVehicleArrayObjectData.put("Calling", String.valueOf(vlprResult.getCalling()));
            }
            if (null != vlprResult.getSecondBelt()) {
                MotorVehicleArrayObjectData.put("SecondBelt", String.valueOf(vlprResult.getSecondBelt()));
            }
            if (null != vlprResult.getSunRoof()) {
                MotorVehicleArrayObjectData.put("SunRoof", String.valueOf(vlprResult.getSunRoof()));
            }
            if (null != vlprResult.getRack()) {
                MotorVehicleArrayObjectData.put("Rack", String.valueOf(vlprResult.getRack()));
            }
            if (StringUtils.isNotEmpty(vlprResult.getHasDanger())) {
                MotorVehicleArrayObjectData.put("HasDanger", vlprResult.getHasDanger());
            }
            if (StringUtils.isNotEmpty(vlprResult.getHasCrash())) {
                MotorVehicleArrayObjectData.put("HasCrash", vlprResult.getHasCrash());
            }
            if (StringUtils.isNotEmpty(vlprResult.getTagNum())) {
                MotorVehicleArrayObjectData.put("TagNum", vlprResult.getTagNum());
            }
            if (StringUtils.isNotEmpty(vlprResult.getSun())) {
                MotorVehicleArrayObjectData.put("Sun", vlprResult.getSun());
            }
            if (StringUtils.isNotEmpty(vlprResult.getDrop())) {
                MotorVehicleArrayObjectData.put("Drop", vlprResult.getDrop());
            }
            if (StringUtils.isNotEmpty(vlprResult.getPaper())) {
                MotorVehicleArrayObjectData.put("Paper", vlprResult.getPaper());
            }
            if (StringUtils.isNotEmpty(vlprResult.getDecoration())) {
                MotorVehicleArrayObjectData.put("Decoration", vlprResult.getDecoration());
            }
            if (null != vlprResult.getAngle()) {
                MotorVehicleArrayObjectData.put("Angle", String.valueOf(vlprResult.getAngle()));
            }
            if (StringUtils.isNotEmpty(vlprResult.getAerial())) {
                MotorVehicleArrayObjectData.put("Aerial", vlprResult.getAerial());
            }
            if (StringUtils.isNotEmpty(vlprResult.getVehicleModel())) {
                MotorVehicleArrayObjectData.put("VehicleModel", vlprResult.getVehicleModel());
            }
            if (StringUtils.isNotEmpty(vlprResult.getVehicleStyles())) {
                MotorVehicleArrayObjectData.put("VehicleStyles", vlprResult.getVehicleStyles());
            }


            MotorVehicleArrayObject.put("MotorVehicleID", vlprResult.getMotorVehicleID());
            MotorVehicleArrayObject.put("Data", MotorVehicleArrayObjectData);
            MotorVehicleObject.add(MotorVehicleArrayObject);
            MotorVehicleListObject.put("MotorVehicleObject", MotorVehicleObject);
            params.put("MotorVehicleListObject", MotorVehicleListObject);
        }
        return params;
    }
}
