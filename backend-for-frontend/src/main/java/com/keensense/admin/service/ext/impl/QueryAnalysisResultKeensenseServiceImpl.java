package com.keensense.admin.service.ext.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.admin.constants.CommonConstants;
import com.keensense.admin.constants.RgbConstants;
import com.keensense.admin.constants.VehicleClassConstants;
import com.keensense.admin.entity.task.Camera;
import com.keensense.admin.entity.task.HumanColorModel;
import com.keensense.admin.entity.task.VsdTaskRelation;
import com.keensense.admin.mapper.task.CameraMapper;
import com.keensense.admin.mapper.task.HumanColorModelMapper;
import com.keensense.admin.mqtt.config.EnumerationConfig;
import com.keensense.admin.service.ext.QueryAnalysisResultService;
import com.keensense.admin.service.task.IVsdTaskRelationService;
import com.keensense.admin.service.task.ResultService;
import com.keensense.admin.util.FeatureFormatUtil;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.util.VsdTaskUtil;
import com.keensense.admin.vo.ResultQueryVo;
import com.keensense.common.platform.StandardRequestUtil;
import com.keensense.common.platform.bo.video.AnalysisResultBo;
import com.keensense.common.platform.domain.FaceResult;
import com.keensense.common.platform.domain.NonMotorVehiclesResult;
import com.keensense.common.platform.domain.PersonResult;
import com.keensense.common.platform.domain.VlprResult;
import com.keensense.common.platform.enums.Age;
import com.keensense.common.platform.enums.ObjTypeEnum;
import com.loocme.sys.constance.DateFormatConst;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.datastruct.WeekArray;
import com.loocme.sys.util.DateUtil;
import com.loocme.sys.util.MapUtil;
import com.loocme.sys.util.StringUtil;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: cuiss
 * @Description: 通过jManager获取目标结果
 * @Date: 2018/10/8.
 */
@Service("queryAnalysisResultService" + AbstractService.KS)
public class QueryAnalysisResultKeensenseServiceImpl extends AbstractService implements QueryAnalysisResultService {

    @Autowired
    private CameraMapper cameraMapper;

    @Autowired
    private HumanColorModelMapper humanColorModelMapper;

    @Autowired
    private IVsdTaskRelationService vsdTaskRelationService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private EnumerationConfig enumerationConfig;

    @Override
    public Map<String, Object> doHttpService(Map<String, Object> paramMap, ResultQueryVo paramBo) {
        AnalysisResultBo inParam = initInputParam(paramMap, paramBo);
        String resultJson = StandardRequestUtil.queryAnalysisResult(initKeensenseUrl(), inParam);
        return dealResultData(resultJson, inParam.getObjType(), inParam.getOrder());
    }

    @Override
    public Map<String, Object> dealResultData(String resultJson, String objType, String order) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 0);
        JSONObject var = JSONObject.parseObject(resultJson);
        List<ResultQueryVo> resultBoList = new ArrayList<>();
        int count = 0;
        JSONObject json = null;
        JSONArray weekArray = null;
        if (null == objType) {
            json = var.getJSONObject("UnitListObject");
            count = json.getInteger("Count");
            weekArray = json.getJSONArray("UnitObject");
        } else if (ObjTypeEnum.PERSON.getValue() == Integer.parseInt(objType)) {
            json = var.getJSONObject("PersonListObject");
            count = json.getInteger("Count");
            weekArray = json.getJSONArray("PersonObject");
        } else if (ObjTypeEnum.CAR.getValue() == Integer.parseInt(objType)) {
            json = var.getJSONObject("MotorVehicleListObject");
            count = json.getInteger("Count");
            weekArray = json.getJSONArray("MotorVehicleObject");
        } else if (ObjTypeEnum.BIKE.getValue() == Integer.parseInt(objType)) {
            json = var.getJSONObject("NonMotorVehicleListObject");
            count = json.getInteger("Count");
            weekArray = json.getJSONArray("NonMotorVehicleObject");
        } else if (ObjTypeEnum.FACES.getValue() == Integer.parseInt(objType)) {
            json = var.getJSONObject("FaceListObject");
            count = json.getInteger("Count");
            weekArray = json.getJSONArray("FaceObject");
        }
        resultMap.put("totalNum", count);
        if (null == objType) {
            //查询全部的时候会有多种类型数据，先封装后检索目的为了减少请求次数
            Map<String, String> query = new HashMap<>();
            for (int i = 0; i < weekArray.size(); i++) {
                JSONObject resultVar = weekArray.getJSONObject(i);
                String ot = resultVar.getString("ObjType");
                String id = resultVar.getString("Id");
                query.put(ot, query.get(ot) + "," + id);
            }
            for (Map.Entry e : query.entrySet()) {
                AnalysisResultBo inParam = new AnalysisResultBo();
                inParam.setIds((String) e.getValue());
                inParam.setObjType((String) e.getKey());
                inParam.setPageSize("3000");
                inParam.setPageNo("1");
                inParam.setOrder(order);
                String personJson = StandardRequestUtil.queryAnalysisResult(initKeensenseUrl(), inParam);
                List<ResultQueryVo> personRow = transAllResultToBo(personJson);
                resultBoList.addAll(personRow);
            }
            //人,骑行,车重新排序
            if ("asc".equals(order)) {
                Collections.sort(resultBoList, new Comparator<ResultQueryVo>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public int compare(ResultQueryVo v1, ResultQueryVo v2) {
                        Date date1 = v1.getCreatetime();
                        Date date2 = v2.getCreatetime();
                        return date1.compareTo(date2);//升序排列
                    }
                });
            } else {
                Collections.sort(resultBoList, new Comparator<ResultQueryVo>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public int compare(ResultQueryVo v1, ResultQueryVo v2) {
                        Date date1 = v1.getCreatetime();
                        Date date2 = v2.getCreatetime();
                        return date2.compareTo(date1);//倒序排列
                    }
                });
            }
        } else {
            resultBoList = transAllResultToBo(resultJson);
        }
        resultMap.put("resultBoList", resultBoList);
        return resultMap;
    }

    @Override
    public String doHttpService(Map<String, Object> paramMap) {
        AnalysisResultBo inParam = initInputParamCommon(paramMap);
        return StandardRequestUtil.queryAnalysisResult(initKeensenseUrl(), inParam);
    }

    public AnalysisResultBo initInputParam(Map<String, Object> paramMap, ResultQueryVo paramBo) {
        AnalysisResultBo inParam = initInputParamCommon(paramMap);
        String objType = inParam.getObjType();
        if (paramBo != null) {
            if (CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_PERSON.equals(objType)) {
                inParam = initInputParamPerson(paramBo, inParam);
            } else if (CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_CAR.equals(objType)) {
                inParam = initInputParamCar(paramBo, inParam);
            } else if (CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_CAR_PERSON.equals(objType)) {
                inParam = initInputParamCarAndPerson(paramBo, inParam);
            } else if (CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_FACE.equals(objType)) {
                inParam = initInputParamFace(paramBo, inParam);
            } else if (CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_OTHER.equals(objType)) {
                inParam = initInputParamOther(paramBo, inParam);
            }
        }
        return inParam;
    }


    /**
     * 初始化人查询 接口参数
     *
     * @param paramBo
     * @param inParam
     * @return
     */
    private AnalysisResultBo initInputParamPerson(ResultQueryVo paramBo, AnalysisResultBo inParam) {
        PersonResult personResult = new PersonResult();
        String upcolorStr = paramBo.getUpcolorStr();
        if (StringUtils.isNotEmptyString(upcolorStr)) {//上衣颜色
            personResult.setCoatColor(getColorParam(upcolorStr));
        }
        String lowcolorStr = paramBo.getLowcolorStr();
        if (StringUtils.isNotEmptyString(lowcolorStr)) {//下衣颜色
            personResult.setTrousersColor(getColorParam(lowcolorStr));
        }

        if (null != paramBo.getSex()) {//性别
            personResult.setGenderCode(paramBo.getSex());
        }
        if (null != paramBo.getAge()) {//年龄
            personResult.setAge(paramBo.getAge());
        }

        if (null != paramBo.getAngle()) {//方向
            personResult.setAngle(paramBo.getAngle());
        }

        if (null != paramBo.getBag()) {//是否背包
            personResult.setBag(paramBo.getBag());
        }

        String coatStyle = paramBo.getCoatStyle();
        if (StringUtils.isNotEmptyString(coatStyle)) {//上衣款式
            personResult.setCoatStyle(getCoatStyle(coatStyle));
        }
        String trousersStyle = paramBo.getTrousersStyle();
        if (StringUtils.isNotEmptyString(trousersStyle)) {//下衣款式
            personResult.setTrousersStyle(getTrousersStyle(trousersStyle));
        }
        if (null != paramBo.getUmbrella()) {//是否打伞
            personResult.setUmbrella(paramBo.getUmbrella());
        }

        if (paramBo.getGlasses() != null) {//是否戴眼镜
            personResult.setGlasses(paramBo.getGlasses());
        }
        if (paramBo.getHandbag() != null) {//手提包
            personResult.setHandbag(paramBo.getHandbag());
        }
        //是否戴帽子
        if (StringUtils.isNotEmptyString(paramBo.getCap())) {
            personResult.setCap(paramBo.getCap());
        }
        //是否戴口罩
        if (StringUtils.isNotEmptyString(paramBo.getRespirator())) {
            personResult.setRespirator(paramBo.getRespirator());
        }
        /**
         * 新加+
         */
        if (paramBo.getHairStyle() != null) {
            personResult.setHairStyle(paramBo.getHairStyle());
        }
        if (paramBo.getTrolley() != null) {
            personResult.setTrolley(paramBo.getTrolley());
        }
        if (paramBo.getLuggage() != null) {
            personResult.setLuggage(paramBo.getLuggage());
        }
        if (paramBo.getCoatTexture() != null) {
            personResult.setCoatTexture(paramBo.getCoatTexture());
        }
        if (paramBo.getTrousersTexture() != null) {
            personResult.setTrousersTexture(paramBo.getTrousersTexture());
        }
        if (paramBo.getHasKnife() != null) {
            personResult.setHasKnife(paramBo.getHasKnife());
        }
        //是否抱小孩
        if (null != paramBo.getChestHold()) {
            personResult.setChestHold(Integer.valueOf(paramBo.getChestHold()));
        }
        //体态
        if (null != paramBo.getShape()) {
            personResult.setShape(Integer.valueOf(paramBo.getShape()));
        }
        //民族
        if (null != paramBo.getMinority()) {
            personResult.setMinority(Integer.valueOf(paramBo.getMinority()));
        }
        inParam.setPersonResult(personResult);
        return inParam;
    }

    /**
     * 初始化人骑车 查询接口参数
     *
     * @param paramBo
     * @param inParam
     */
    private AnalysisResultBo initInputParamCarAndPerson(ResultQueryVo paramBo, AnalysisResultBo inParam) {
        NonMotorVehiclesResult nonMotorVehiclesResult = new NonMotorVehiclesResult();
        if (null != paramBo.getBikeSex()) {//性别
            nonMotorVehiclesResult.setSex(paramBo.getBikeSex());
        }

        if (null != paramBo.getBikeAge()) {//年龄
            nonMotorVehiclesResult.setAge(paramBo.getBikeAge());
        }

        if (null != paramBo.getBikeAngle()) {//方向
            nonMotorVehiclesResult.setAngle(paramBo.getBikeAngle());
        }

        if (null != paramBo.getWheels()) {//车辆类别
            nonMotorVehiclesResult.setWheels(paramBo.getWheels());
        }

        if (null != paramBo.getBikeGenre()) {//车辆类型
            nonMotorVehiclesResult.setBikeGenre(paramBo.getBikeGenre());
        }

        if (null != paramBo.getBikeUmbrella()) { //打伞
            nonMotorVehiclesResult.setUmbrella(paramBo.getBikeUmbrella());
        }

        if (StringUtils.isNotEmptyString(paramBo.getPassengersUpColorStr())) {//上衣颜色
            nonMotorVehiclesResult.setCoatColor1(getColorParam(paramBo.getPassengersUpColorStr()));//
        }

        if (StringUtils.isNotEmptyString(paramBo.getBikeCoatStyle())) {//上衣款式
            nonMotorVehiclesResult.setCoatStyle(getCoatStyle(paramBo.getBikeCoatStyle()));
        }

        if (StringUtils.isNotEmptyString(paramBo.getBikeCoatTexture())) {//上衣纹理
            nonMotorVehiclesResult.setCoatTexture(Integer.valueOf(paramBo.getBikeCoatTexture()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getBikeGlasses())) {//眼镜
            nonMotorVehiclesResult.setGlasses(Integer.valueOf(paramBo.getBikeGlasses()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getBikeRespirator())) {//口罩
            nonMotorVehiclesResult.setRespirator(Integer.valueOf(paramBo.getBikeRespirator()));
        }
        if (StringUtils.isNotEmptyString(paramBo.getBikeColor())) {//车身颜色
            nonMotorVehiclesResult.setVehicleColor(paramBo.getBikeColor());
        }
        if (StringUtils.isNotEmptyString(paramBo.getBikeBag())) {//背包
            nonMotorVehiclesResult.setBag(Integer.valueOf(paramBo.getBikeBag()));
        }
        if (null != paramBo.getHelmet()) {//是否带头盔
            nonMotorVehiclesResult.setHelmet(paramBo.getHelmet());
        }
        if (StringUtils.isNotEmptyString(paramBo.getHelmetcolorStr())) {//头盔颜色
            nonMotorVehiclesResult.setHelmetColorTag1(getColorParam(paramBo.getHelmetcolorStr()));
        }
        if (null != paramBo.getBikeHasPlate()) {//是否挂牌
            nonMotorVehiclesResult.setHasPlate(paramBo.getBikeHasPlate());
        }
        if (StringUtils.isNotEmptyString(paramBo.getLampShape())) {
            nonMotorVehiclesResult.setLampShape(paramBo.getLampShape());
        }
        if (StringUtils.isNotEmptyString(paramBo.getCarryPassenger())) {
            nonMotorVehiclesResult.setCarryPassenger(paramBo.getCarryPassenger());
        }
        if (null != paramBo.getSocialAttribute()) {
            nonMotorVehiclesResult.setSocialAttribute(paramBo.getSocialAttribute());
        }
        if (null != paramBo.getEnterprise()) {
            nonMotorVehiclesResult.setEnterprise(paramBo.getEnterprise());
        }
        inParam.setNonMotorVehiclesResult(nonMotorVehiclesResult);
        return inParam;

    }

    /**
     * 初始化车 查询接口参数
     *
     * @param paramBo
     * @param inParam
     */
    private AnalysisResultBo initInputParamCar(ResultQueryVo paramBo, AnalysisResultBo inParam) {
        VlprResult vlprResult = new VlprResult();
        if (StringUtils.isNotEmptyString(paramBo.getLicense())) {//车牌号码
            vlprResult.setPlateNo(paramBo.getLicense());
        }

        if (null != paramBo.getPlateColorCode()) {//车牌颜色
            vlprResult.setPlateColor(paramBo.getPlateColorCode());
        }

        if (StringUtils.isNotEmptyString(paramBo.getCarlogo())) {//品牌
            vlprResult.setVehicleBrand(paramBo.getCarlogo());
        }

        if (StringUtils.isNotEmptyString(paramBo.getVehicleseries())) {//车系
            vlprResult.setVehicleModel(paramBo.getVehicleseries());
        }

        if (StringUtils.isNotEmptyString(paramBo.getVehiclekind())) {//车型
            vlprResult.setVehicleClass(paramBo.getVehiclekind());
        }

        if (StringUtils.isNotEmptyString(paramBo.getCarcolor())) {//车身颜色
            vlprResult.setVehicleColor(paramBo.getCarcolor());
        }
        if (StringUtils.isNotEmptyString(paramBo.getDecoration())) { //摆件
            vlprResult.setDecoration(paramBo.getDecoration());
        }
        if (null != paramBo.getSunRoof()) { //天窗
            vlprResult.setSunRoof(Integer.parseInt(paramBo.getSunRoof()));
        }
        if (null != paramBo.getDirection()) { //行驶方向
            vlprResult.setDirection(paramBo.getDirection());
        }

        if (null != paramBo.getMainDriver()) { //主驾驶是否系安全带
            vlprResult.setSafetyBelt(Integer.parseInt(paramBo.getMainDriver()));
        }
        if (null != paramBo.getCoDriver()) { //副驾驶安全带状态
            vlprResult.setSecondBelt(Integer.parseInt(paramBo.getCoDriver()));
        }
        if (null != paramBo.getHasCall()) { //是否打电话
            vlprResult.setCalling(paramBo.getHasCall());
        }
        if (null != paramBo.getRack()) { // 是否有行李架
            vlprResult.setRack(Integer.parseInt(paramBo.getRack()));
        }
        if (null != paramBo.getTagNum()) { // 是否有年检标
            vlprResult.setTagNum(String.valueOf(paramBo.getTagNum()));
        }
        if (null != paramBo.getSun()) { //遮阳板状态
            vlprResult.setSunvisor(Integer.parseInt(paramBo.getSun()));
        }
        if (null != paramBo.getTissueBox()) { //纸巾盒
            vlprResult.setPaper(paramBo.getTissueBox());
        }
        if (null != paramBo.getDrop()) { //挂饰
            vlprResult.setDrop(String.valueOf(paramBo.getDrop()));
        }
        //车牌类型
        if (StringUtils.isNotEmptyString(paramBo.getPlateClass())) {
            vlprResult.setPlateClass(paramBo.getPlateClass());
        }
        //撞损车
        if (StringUtils.isNotEmptyString(paramBo.getHasCrash())) {
            vlprResult.setHasCrash(paramBo.getHasCrash());
        }
        //危化品车
        if (StringUtils.isNotEmptyString(paramBo.getHasDanger())) {
            vlprResult.setHasDanger(paramBo.getHasDanger());
        }
        //姿态
        if (StringUtils.isNotEmptyString(paramBo.getVehicleAngle())) {
            Integer angle = 128;
            if ("1".equals(paramBo.getVehicleAngle())) {
                angle = 128;
            } else if ("2".equals(paramBo.getVehicleAngle())) {
                angle = 256;
            } else if ("3".equals(paramBo.getVehicleAngle())) {
                angle = 512;
            }
            vlprResult.setAngle(angle);
        }
        //挂牌
        if (StringUtils.isNotEmptyString(paramBo.getVehicleHasPlate())) {
            vlprResult.setHasPlate(paramBo.getVehicleHasPlate());
        }
        //天线
        if (StringUtils.isNotEmptyString(paramBo.getAerial())) {
            vlprResult.setAerial(paramBo.getAerial());
        }
//        /**
//         * 新加+
//         */
//        if (null != paramBo.getMainbelt()) {
//            inParam.set("mainDriver", paramBo.getMainDriver());
//        }
//        if (null != paramBo.getCoDriver()) {
//            inParam.set("coDriver", paramBo.getCoDriver());
//        }
//        if (null != paramBo.getDrop()) {
//            inParam.set("drop", paramBo.getDrop());
//        }
//        if (null != paramBo.getPaper()) {
//            inParam.set("paper", paramBo.getPaper());
//        }
//        if (null != paramBo.getBikeAge()) {
//            inParam.set("hasCall", paramBo.getHasCall());
//        }
//        if (null != paramBo.getBikeAge()) {
//            inParam.set("tagNum", paramBo.getTagNum());
//        }
//        if (null != paramBo.getBikeAge()) {
//            inParam.set("sun", paramBo.getSun());
//        }
//        if (null != paramBo.getPlateColorCode()) {
//            inParam.set("plateColorCode", paramBo.getPlateColorCode());
//        }
        inParam.setVlprResult(vlprResult);
        return inParam;
    }

    /**
     * 初始化 其他 查询接口参数
     *
     * @param map
     * @param inParam
     */
    private AnalysisResultBo initInputParamOther(ResultQueryVo map, AnalysisResultBo inParam) {
        return inParam;
    }

    /**
     * 初始化人脸 查询接口参数
     *
     * @param paramBo
     * @param inParam
     */
    private AnalysisResultBo initInputParamFace(ResultQueryVo paramBo, AnalysisResultBo inParam) {

        return inParam;

    }


    /**
     * 初始化 全部 查询接口参数
     *
     * @param map
     * @return
     */
    private AnalysisResultBo initInputParamCommon(Map<String, Object> map) {
        AnalysisResultBo inParam = new AnalysisResultBo();
        String objType = MapUtil.getString(map, "objType");
        String startTime = MapUtil.getString(map, "startTime");
        String endTime = MapUtil.getString(map, "endTime");
        String sorting = MapUtil.getString(map, "sorting");
        String serialnumber = MapUtil.getString(map, "serialnumber");
        String cameraId = MapUtil.getString(map, "cameraId");
        String uuid = MapUtil.getString(map, "uuid");
        String wheels = MapUtil.getString(map, "wheels");
        String cartype = MapUtil.getString(map, "cartype");
        String bikegenre = MapUtil.getString(map, "bikegenre");
        String vehicleColor = MapUtil.getString(map, "vehiclecolor");
        String coatcolor1 = MapUtil.getString(map, "coatcolor1");
        String sex = MapUtil.getString(map, "sex");
        NonMotorVehiclesResult nonMotorVehiclesResult = new NonMotorVehiclesResult();
        VlprResult vlprResult = new VlprResult();
        PersonResult personResult = new PersonResult();

        if (StringUtils.isNotEmpty(wheels)) {
            nonMotorVehiclesResult.setWheels(Byte.parseByte(wheels));
        }
        if (StringUtils.isNotEmpty(bikegenre)) {
            nonMotorVehiclesResult.setBikeGenre(Integer.parseInt(bikegenre));
        }
        if (StringUtils.isNotEmpty(coatcolor1)) {
            nonMotorVehiclesResult.setCoatColor1(coatcolor1);
        }
        inParam.setNonMotorVehiclesResult(nonMotorVehiclesResult);
        if (StringUtils.isNotEmpty(vehicleColor)) {
            vlprResult.setVehicleColor(vehicleColor);
        }
        if (StringUtils.isNotEmpty(cartype)) {
            vlprResult.setVehicleClass(cartype);
        }

        inParam.setVlprResult(vlprResult);

        if (StringUtils.isNotEmpty(sex)) {
            personResult.setGenderCode(Integer.parseInt(sex));
        }
        inParam.setPersonResult(personResult);

        if (StringUtils.isNotEmpty(uuid)) {
            inParam.setIds(uuid);
        }
        if (StringUtils.isEmpty(objType)) {
            objType = null;
        }
        inParam.setObjType(objType);
        if (StringUtils.isEmptyString(sorting)) {
            sorting = "asc";
        }
        inParam.setOrder(sorting);//排序

        if (StringUtil.isNotNull(serialnumber)) {
            inParam.setSerialnumber(serialnumber);//任务序列号
        }

        //入侵跨界检测没有任务时不传任务号过去
        if (StringUtils.isEmpty(inParam.getSerialnumber()) && StringUtils.isEmpty(inParam.getIds())) {
            List<VsdTaskRelation> taskRelations = vsdTaskRelationService.list(new QueryWrapper<VsdTaskRelation>().isNull("del_flag"));
            Set<String> serialnumbers = new HashSet<>();
            for (VsdTaskRelation vsdTaskRelation : taskRelations) {
                serialnumbers.add(vsdTaskRelation.getSerialnumber());
            }
            inParam.setSerialnumber(String.join(",", serialnumbers));
        }
        if (StringUtils.isNotEmptyString(startTime)) {
            inParam.setStartTime(startTime);//起始时间
        }
        if (StringUtils.isNotEmptyString(endTime)) {
            inParam.setEndTime(endTime);//结束时间
        }


        if (null != map && map.containsKey("rows")) {
            inParam.setPageSize(MapUtil.getString(map, "rows"));
        }
        if (null != map && map.containsKey("page")) {
            inParam.setPageNo(MapUtil.getString(map, "page"));
        }

        if (StringUtil.isNotNull(cameraId)) {
            inParam.setDeviceId(cameraId);//监控点
        }
        return inParam;
    }

    @Override
    public List<ResultQueryVo> transAllResultToBo(String retJson) {
        List<ResultQueryVo> restBoLst = new ArrayList<>();
        JSONObject var = JSONObject.parseObject(retJson);
        JSONArray weekArray = null;
        int len = 0;
        JSONObject object = null;
        if (len == 0) {
            object = var.getJSONObject("UnitListObject");
            if (object != null) {
                weekArray = object.getJSONArray("UnitObject");
                len = weekArray.size();
            }
        }
        if (len == 0) {
            object = var.getJSONObject("PersonListObject");
            if (object != null) {
                weekArray = object.getJSONArray("PersonObject");
                len = weekArray.size();
            }
        }
        if (len == 0) {
            object = var.getJSONObject("MotorVehicleListObject");
            if (object != null) {
                weekArray = object.getJSONArray("MotorVehicleObject");
                len = weekArray.size();
            }
        }
        if (len == 0) {
            object = var.getJSONObject("NonMotorVehicleListObject");
            if (object != null) {
                weekArray = object.getJSONArray("NonMotorVehicleObject");
                len = weekArray.size();
            }
        }
        if (len == 0) {
            object = var.getJSONObject("FaceListObject");
            if (object != null) {
                weekArray = object.getJSONArray("FaceObject");
                len = weekArray.size();
            }
        }

        Map<String, Camera> cameraIdRelNameMaps = new HashMap<>();
        Map<String, String> cameraIdRelTypeMaps = new HashMap<>();
        boolean getFace = weekArray.size() < 30;
        for (int i = 0; i < weekArray.size(); i++) {
            JSONObject obj = weekArray.getJSONObject(i);
            ResultQueryVo resultBo = this.transResultToBo(obj, getFace);
            String cameraId = obj.getString("DeviceID");
            if (StringUtils.isNotEmptyString(cameraId)) {
                String cameraName = "";
                String cameratype = "";
                // 获取监控点名称，如果已经查询过则不在查询数据库，直接从Map获取
                Camera camera = null;
                if (cameraIdRelNameMaps.containsKey(cameraId) && cameraIdRelTypeMaps.containsKey(cameraId)) {
                    camera = cameraIdRelNameMaps.get(cameraId);
                } else {
                    if (StringUtils.isNumeric(cameraId)) {
                        camera = cameraMapper.selectById(cameraId);
                        if (null != camera) {
                            cameraIdRelNameMaps.put(cameraId, camera);
                        } else {
                            camera = new Camera();
                            camera.setName("监控点不存在");
                            cameraIdRelNameMaps.put(cameraId, camera);
                        }
                    }
                }
                resultBo.setCameraId(cameraId);
                resultBo.setCameraType(String.valueOf(camera.getCameratype()));
                resultBo.setCameraName(camera.getName());
                resultBo.setLongitude(camera.getLongitude());
                resultBo.setLatitude(camera.getLatitude());
            }

            restBoLst.add(resultBo);
        }
        return restBoLst;
    }

    /**
     * 接口输出参数转换成结果Bo
     *
     * @param rs
     * @return
     */
    private ResultQueryVo transResultToBo(JSONObject rs, boolean getFace) {
        AnalysisResultBo analysisResultBo = JSONObject.parseObject(rs.toString(), AnalysisResultBo.class);
        ResultQueryVo objextResult = new ResultQueryVo();
        objextResult.setId(analysisResultBo.getId()); //id
        objextResult.setResultId(analysisResultBo.getId());
        objextResult.setSerialnumber(analysisResultBo.getSerialnumber());
        objextResult.setRecogId(analysisResultBo.getId());
        objextResult.setAnalysisid(analysisResultBo.getAnalysisId());
        //TODO 后续
        String traces = analysisResultBo.getTraces();
        //objextResult.setVideoHttpUrl("http://172.16.1.66:8081/orgvideos/3892642116.mp4");
        //objextResult.setTrackUrl("http://172.16.1.63:8082/20190809/14/thumb/bike_1120012134-000002_st1240_end6040_thumb.json");
        if (StringUtils.isNotEmpty(traces)) {
            int fromOfflineType = 2;
            objextResult.setVideoHttpUrl(VsdTaskUtil.getVideoHttpByParam(analysisResultBo.getSerialnumber(), fromOfflineType));
            if (StringUtils.isNotEmpty(objextResult.getVideoHttpUrl())) {
                JSONObject tracesJson = new JSONObject();
                tracesJson.put("objType", analysisResultBo.getObjType());
                tracesJson.put("traces", JSONArray.parse(traces));
                objextResult.setTrackUrl(tracesJson.toJSONString());
                objextResult.setFromType(fromOfflineType);
            } else {
                int fromVideoType = 5;
                objextResult.setVideoHttpUrl(VsdTaskUtil.getVideoHttpByParam(analysisResultBo.getSerialnumber(), fromVideoType));
                if (StringUtils.isNotEmpty(objextResult.getVideoHttpUrl())) {
                    objextResult.setFromType(fromVideoType);
                }
            }
        }

        String objTypeStr = analysisResultBo.getObjType();
        int objType = 0;
        if (StringUtils.isEmpty(objTypeStr)) {
            objTypeStr = "0";
        }
        objType = Integer.parseInt(objTypeStr);
        objextResult.setObjtype(Short.parseShort(objTypeStr));
        objextResult.setObjid(analysisResultBo.getObjId());

        objextResult.setImgurl(analysisResultBo.getImgUrl());
        objextResult.setBigImgurl(analysisResultBo.getBigImgUrl());
        objextResult.setStartframeidx(analysisResultBo.getStartFrameidx());
        objextResult.setEndframeidx(analysisResultBo.getEndFrameidx());
        if (analysisResultBo.getAppearTime() != null) {
            objextResult.setStartframepts(analysisResultBo.getAppearTime().getTime());
        }
        if (analysisResultBo.getDisappearTime() != null) {
            objextResult.setEndframepts(analysisResultBo.getDisappearTime().getTime());
        }
        objextResult.setCreatetime(analysisResultBo.getMarkTime());
        objextResult.setCreatetimeStr(DateUtil.getFormat(objextResult.getCreatetime(), DateFormatConst.YMDHMS_));
        objextResult.setResulttime(analysisResultBo.getMarkTime());

        // 1-人
        if (objType == ObjTypeEnum.PERSON.getValue()) {
            PersonResult personResult = JSONObject.parseObject(rs.toString(), PersonResult.class);
            //封装上下半身颜色
            String coatColor = personResult.getCoatColor();
            if (StringUtils.isNotEmptyString(coatColor)) {
                objextResult.setUpcolorStr(getColorResult(coatColor));
            } else {
                objextResult.setUpcolorStr("未知");
            }

            String trousersColor = personResult.getTrousersColor();
            if (StringUtils.isNotEmptyString(trousersColor)) {
                objextResult.setLowcolorStr(getColorResult(trousersColor));
            } else {
                objextResult.setLowcolorStr("未知");
            }
            //上衣款式
            if (personResult.getCoatStyle() != null) {
                objextResult.setCoatStyle(getCoatStyleReuslt(personResult.getCoatStyle()));
            }
            //下衣款式
            if (personResult.getTrousersStyle() != null) {
                objextResult.setTrousersStyle(getTrousersStyleResult(personResult.getTrousersStyle()));
            }
            //性别
            if (personResult.getGenderCode() != null) {
                objextResult.setSex(personResult.getGenderCode());
            }
            //年龄
            if (personResult.getAgeUpLimit() != null) {
                objextResult.setAge(parseAge(personResult));
            }
            //姿态
            if (personResult.getAngle() != null) {
                objextResult.setAngle(personResult.getAngle());
            }
            //背包
            if (personResult.getBag() != null) {
                objextResult.setBag(personResult.getBag());
            }
            //手提包
            if (personResult.getHandbag() != null) {
                objextResult.setHandbag(personResult.getHandbag());
            }
            //眼镜
            if (personResult.getGlasses() != null) {
                objextResult.setGlasses(personResult.getGlasses());
            }
            //雨伞
            if (personResult.getUmbrella() != null) {
                objextResult.setUmbrella(personResult.getUmbrella());
            }
            //口罩

            if (StringUtils.isNotEmptyString(personResult.getRespirator())) {
                String respirator = FeatureFormatUtil.getFeatureDesc(Integer.parseInt(personResult.getRespirator()), "respirator");
                objextResult.setRespirator(respirator);
            }

            //带帽子
            if (StringUtils.isNotEmpty(personResult.getCap())) {
                String cap = FeatureFormatUtil.getFeatureDesc(Integer.parseInt(personResult.getCap()), "cap");
                objextResult.setCap(cap);
            }
            //发型
            if (StringUtils.isNotEmptyString(personResult.getHairStyle())) {
                String hairStyle = MapUtils.getString(RgbConstants.HAIRSTYLE_CODEFORMAT, personResult.getHairStyle());
                objextResult.setHairStyle(hairStyle);
            }
            //上衣纹理
            if (StringUtils.isNotEmptyString(personResult.getCoatTexture())) {
                String coatTexture = FeatureFormatUtil.getFeatureDesc(Integer.parseInt(personResult.getCoatTexture()), "coat_texture");
                objextResult.setCoatTexture(coatTexture);
            }
            //下衣纹理
            if (StringUtils.isNotEmptyString(personResult.getTrousersTexture())) {
                String trousersTexture = FeatureFormatUtil.getFeatureDesc(Integer.parseInt(personResult.getTrousersTexture()), "trousers_texture");
                objextResult.setTrousersTexture(trousersTexture);
            }
            //抱小孩
            Integer chestHold = personResult.getChestHold();
            if (null != chestHold) {
                if (-1 == chestHold) {
                    objextResult.setChestHold("未知");
                } else {
                    Map<String, String> personChestHold =
                            StringUtils.splitEnumerationCodeForName(enumerationConfig.getChestHold());
                    objextResult.setChestHold(personChestHold.get(String.valueOf(chestHold)));
                }
            } else {
                objextResult.setChestHold("未知");
            }
            //体态
            Integer shape = personResult.getShape();
            if (null != shape) {
                if (-1 == shape) {
                    objextResult.setBodyCharacter("未知");
                } else {
                    Map<String, String> personShape =
                            StringUtils.splitEnumerationCodeForName(enumerationConfig.getShape());
                    objextResult.setBodyCharacter(personShape.get(String.valueOf(shape)));
                }
            } else {
                objextResult.setBodyCharacter("未知");
            }
            //民族
            Integer minority = personResult.getMinority();
            if (null != minority) {
                if (-1 == minority) {
                    objextResult.setMinority("未知");
                } else {
                    Map<String, String> personMinority =
                            StringUtils.splitEnumerationCodeForName(enumerationConfig.getMinority());
                    objextResult.setMinority(personMinority.get(String.valueOf(minority)));
                }
            } else {
                objextResult.setMinority("未知");
            }
            //拉杆箱
            if (personResult.getLuggage() != null) {
                Integer suitCase = personResult.getLuggage();
                objextResult.setLuggage(suitCase);
            }
            //手推车
            if (personResult.getTrolley() != null) {
                Integer trolley = personResult.getTrolley();
                objextResult.setTrolley(trolley);
            }
            //手持刀棍
            if (personResult.getHasKnife() != null) {
                Integer hasKnife = personResult.getHasKnife();
                objextResult.setHasKnife(hasKnife);
            }
            if (getFace) {
                objextResult.setFaceUUID(personResult.getFaceUUID());
            }

        }

        // 4-人骑车
        if (objType == ObjTypeEnum.BIKE.getValue()) {
            NonMotorVehiclesResult nonMotorVehiclesResult = JSONObject.parseObject(rs.toString(), NonMotorVehiclesResult.class);
            //性别
            if (nonMotorVehiclesResult.getSex() != null) {
                objextResult.setBikeSex(nonMotorVehiclesResult.getSex());
            }
            //非机动车类型
            Integer bikeGenre = nonMotorVehiclesResult.getBikeGenre();
            if (bikeGenre != null) {
                objextResult.setBikeGenre(bikeGenre);
                objextResult.setBikeGenreName(FeatureFormatUtil.getFeatureDesc(bikeGenre, "bike_genre"));
            }
            //上身颜色
            if (StringUtils.isNotEmptyString(nonMotorVehiclesResult.getCoatColor1())) {
                objextResult.setPassengersUpColorStr(getColorResult(nonMotorVehiclesResult.getCoatColor1()));
            }
            //方向
            if (nonMotorVehiclesResult.getAngle() != null) {
                objextResult.setBikeAngle(nonMotorVehiclesResult.getAngle());
            }
            //年龄
            if (nonMotorVehiclesResult.getAge() != null) {
                objextResult.setBikeAge(nonMotorVehiclesResult.getAge());
            }
            //上衣款式
            if (nonMotorVehiclesResult.getCoatStyle() != null) {
                objextResult.setBikeCoatStyle(getCoatStyleReuslt(nonMotorVehiclesResult.getCoatStyle()));
            }
            //上衣纹理
            if (nonMotorVehiclesResult.getCoatTexture() != null) {
                String coatTexture = FeatureFormatUtil.getFeatureDesc(nonMotorVehiclesResult.getCoatTexture(), "coat_texture");
                objextResult.setBikeCoatTexture(coatTexture);
            }
            //眼镜
            if (nonMotorVehiclesResult.getGlasses() != null) {
                objextResult.setBikeGlasses(nonMotorVehiclesResult.getGlasses().toString());
            }
            //口罩
            if (nonMotorVehiclesResult.getRespirator() != null) {
                String respirator = FeatureFormatUtil.getFeatureDesc(nonMotorVehiclesResult.getRespirator(), "respirator");
                objextResult.setBikeRespirator(respirator);
            }

            //类别
            Byte wheels = nonMotorVehiclesResult.getWheels();
            if (wheels != null) {
                objextResult.setWheels(wheels);
                objextResult.setWheelsName(getWheels(wheels));
            }
            //车身颜色
            String bikeColor = nonMotorVehiclesResult.getMainColor1();
            if (StringUtils.isNotEmptyString(bikeColor)) {
                objextResult.setBikeColor(getColorResult(bikeColor));
            }
            // 人骑车是否挂牌
            if (nonMotorVehiclesResult.getHasPlate() != null) {
                objextResult.setBikeHasPlate(nonMotorVehiclesResult.getHasPlate());
            }
            //戴头盔
            Integer helmet = nonMotorVehiclesResult.getHelmet();
            if (helmet != null) {
                String helmetStr = (helmet == 0 ? "否" : (helmet == 1 ? "是" : "未知"));
                objextResult.setHelmetStr(helmetStr);
                objextResult.setHelmet(helmet);
            }
            //头盔颜色
            String helmetColorTag1 = nonMotorVehiclesResult.getHelmetColorTag1();
            if (StringUtils.isNotEmptyString(helmetColorTag1)) {
                objextResult.setHelmetcolorStr(getColorResult(nonMotorVehiclesResult.getHelmetColorTag1()));
            }
            //背包
            if (nonMotorVehiclesResult.getBag() != null) {
                objextResult.setBikeBag(nonMotorVehiclesResult.getBag().toString());
            }
            //车篷
            if (nonMotorVehiclesResult.getUmbrella() != null) {
                objextResult.setBikeUmbrella(nonMotorVehiclesResult.getUmbrella());
            }
            //车灯形状
            String lampShape = nonMotorVehiclesResult.getLampShape();
            if (StringUtils.isNotEmptyString(lampShape)) {
                if (CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_OTHER.equals(lampShape)) {
                    objextResult.setLampShape("未知");
                } else {
                    Map<String, String> lampShapeMap =
                            StringUtils.splitEnumerationCodeForName(enumerationConfig.getLampShape());
                    objextResult.setLampShape(lampShapeMap.get(lampShape));
                }
            } else {
                objextResult.setLampShape("未知");
            }
            //载客
            String carryPassenger = nonMotorVehiclesResult.getCarryPassenger();
            if (StringUtils.isNotEmptyString(lampShape)) {
                if (CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_OTHER.equals(carryPassenger)) {
                    objextResult.setCarryPassenger("未知");
                } else {
                    Map<String, String> carryPassengerMap =
                            StringUtils.splitEnumerationCodeForName(enumerationConfig.getCarryPassenger());
                    objextResult.setCarryPassenger(carryPassengerMap.get(carryPassenger));
                }
            } else {
                objextResult.setCarryPassenger("未知");
            }


            String faceUuid = nonMotorVehiclesResult.getFaceUUID();
            if (getFace) {
                objextResult.setFaceUUID(faceUuid);
            }

            //车辆社会属性
            objextResult.setSocialAttribute(nonMotorVehiclesResult.getSocialAttribute());
            objextResult.setSocialAttributeStr(FeatureFormatUtil.getFeatureDesc(nonMotorVehiclesResult.getSocialAttribute(),"social_attribute"));

            //车辆所属单位
            objextResult.setEnterprise(nonMotorVehiclesResult.getEnterprise());
            objextResult.setEnterpriseStr(FeatureFormatUtil.getFeatureDesc(nonMotorVehiclesResult.getEnterprise(),"enterprise"));
        }
        //2 -车辆
        if (objType == ObjTypeEnum.CAR.getValue()) {
            VlprResult vlprResult = JSONObject.parseObject(rs.toString(), VlprResult.class);
            //品牌
            String carLogo = vlprResult.getVehicleBrand();
            if (StringUtils.isNumeric(carLogo)) {
                objextResult.setCarlogo(RgbConstants.CARBRAND_CODEFROMAT.get(carLogo));
            } else {
                if (StringUtils.isNotEmptyString(carLogo)) {
                    objextResult.setCarlogo(carLogo);
                } else {
                    objextResult.setCarlogo("未知");
                }
            }
            //车系
            String vehicleModel = vlprResult.getVehicleModel();
            if (StringUtils.isNotEmptyString(vehicleModel)) {
                objextResult.setVehicleseries(vehicleModel);
            }
            //车辆年款
            String vehicleStyle = vlprResult.getVehicleStyles();
            if (StringUtils.isNotEmptyString(vehicleStyle)) {
                objextResult.setVehicleStyle(vehicleStyle);
            }
            //是否有车牌
            String vehicleHasPlate = vlprResult.getHasPlate();
            if (StringUtils.isNotEmptyString(vehicleHasPlate)) {
                String vehicleHasPlateStr = "1".equals(vehicleHasPlate) ? "是" : ("0".equals(vehicleHasPlate) ? "否" :
                        "未知");
                objextResult.setVehicleHasPlate(vehicleHasPlateStr);
            }
            //车牌号码
            String plateNo = vlprResult.getPlateNo();
            if (StringUtils.isNotEmptyString(plateNo)) {
                if (plateNo.equals("-1")) {
                    objextResult.setLicense("未知");
                } else {
                    objextResult.setLicense(plateNo);
                }
            }

            //号牌颜色
            String plateColorCode = vlprResult.getPlateColor();
            if (StringUtils.isNotEmptyString(plateColorCode)) {
                String plateColorName = getColorResult(plateColorCode);
                objextResult.setPlateColorCode(plateColorName);
            }

            //车牌类型
            String plateClassCode = vlprResult.getPlateClass();
            if (StringUtils.isNotEmptyString(plateClassCode)) {
                objextResult.setPlateType(RgbConstants.PLATECLASS_CODEFORMAT.get(plateClassCode));
            }

            //车辆类型
            String type = vlprResult.getVehicleClass();
            if (StringUtils.isNotEmptyString(type)) {
                if (CommonConstants.ObjextTypeConstants.OBJEXT_TYPE_OTHER.equals(type)) {
                    objextResult.setVehiclekind("未知");
                } else {
                    objextResult.setVehiclekind(VehicleClassConstants.CARTYPE_CODEFORMAT.get(type));
                }
            }

            //车身颜色
            String vehicleColor = vlprResult.getVehicleColor();
            if (StringUtils.isNotEmptyString(vehicleColor)) {
                String vehicleColorName = getColorResult(vehicleColor);
                objextResult.setCarcolor(vehicleColorName);
            }
            //是否打电话
            Integer hasCall = vlprResult.getCalling();
            if (null != hasCall) {
                objextResult.setHasCall(hasCall);
            }
            //撞损车
            String hasCrash = vlprResult.getHasCrash();
            if (StringUtils.isNotEmptyString(hasCrash)) {
                objextResult.setCrash(Byte.parseByte(hasCrash));
            }
            //危化车
            String hasDanger = vlprResult.getHasDanger();
            if (StringUtils.isNotEmptyString(hasDanger)) {
                objextResult.setDanger(Byte.parseByte(hasDanger));
            }
            //年检标数量
            String tagNum = vlprResult.getTagNum();
            if (StringUtils.isNotEmptyString(tagNum)) {
                if (!"-1".equals(tagNum)) {
                    objextResult.setTagNum(Integer.parseInt(tagNum));
                }
            }
            //主驾驶安全带
            Integer safetyBeltCode = vlprResult.getSafetyBelt();
            if (null != safetyBeltCode) {
                String safetyBelt = (1 == safetyBeltCode ? "是" : (0 == safetyBeltCode) ? "否" : "未知");
                objextResult.setMainDriver(safetyBelt);
            }
            //副驾驶安全带
            Integer secondBeltCode = vlprResult.getSecondBelt();
            if (null != secondBeltCode) {
                String secondBelt = (1 == secondBeltCode ? "是" : (0 == secondBeltCode) ? "否" : "未知");
                objextResult.setCoDriver(secondBelt);
            }
            //遮阳板
            String sunCode = vlprResult.getSun();
            if (null != sunCode) {
                String sun = ("1".equals(sunCode) ? "放下" : ("0".equals(sunCode)) ? "收起" : "未知");
                objextResult.setSun(sun);
            }
            //挂饰
            String drop = vlprResult.getDrop();
            if (StringUtils.isNotEmptyString(drop)) {
                objextResult.setDrop(Byte.parseByte(drop));
            }
            //纸巾盒
            String paper = vlprResult.getPaper();
            if (StringUtils.isNotEmptyString(paper)) {
                objextResult.setPaper(Byte.parseByte(paper));
            }
            //行驶方向
            Integer vlprAngle = vlprResult.getAngle();
            if (null != vlprAngle) {
                objextResult.setDirection(RgbConstants.CARDIRECTION_CODEFORMAT.get(String.valueOf(vlprAngle)));
            }
            //行李架
            Integer rackCode = vlprResult.getRack();
            if (null != rackCode) {
                String rack = rackCode == 1 ? "是" : (0 == rackCode) ? "否" : "未知";
                objextResult.setRack(rack);
            }
            //天窗
            Integer sunRoofCode = vlprResult.getSunRoof();
            if (null != sunRoofCode) {
                String sunRoof = sunRoofCode == 1 ? "是" : (0 == sunRoofCode) ? "否" : "未知";
                objextResult.setSunRoof(sunRoof);
            } else {
                objextResult.setSunRoof("未知");
            }
            //行驶速度
            Double speed = vlprResult.getSpeed();
            if (null != speed) {
                objextResult.setCarSpeed(speed);
            }
            //摆件
            String DecorationCode = vlprResult.getDecoration();
            if (StringUtils.isNotEmptyString(DecorationCode)) {
                String Decoration = "1".equals(DecorationCode) ? "是" : ("0".equals(DecorationCode) ? "否" : "未知");
                objextResult.setDecoration(Decoration);
            }
            //挂牌
            String hasPlate = vlprResult.getHasPlate();
            if (StringUtils.isNotEmptyString(hasPlate)) {
                Map<String, String> hasPlateMap =
                        StringUtils.splitEnumerationCodeForName(enumerationConfig.getVehicleHasPlate());
                if (StringUtils.isNotEmptyString(hasPlateMap.get(hasPlate))) {
                    objextResult.setVehicleHasPlate(hasPlateMap.get(hasPlate));
                } else {
                    objextResult.setVehicleHasPlate("未知");
                }
            } else {
                objextResult.setVehicleHasPlate("未知");
            }
            //天线
            String aerial = vlprResult.getAerial();
            if (StringUtils.isNotEmptyString(aerial)) {
                String aerialName = "1".equals(aerial) ? "是" : ("0".equals(aerial) ? "否" : "未知");
                objextResult.setAerial(aerialName);
            } else {
                objextResult.setAerial("未知");
            }

            String faceUuid = vlprResult.getFaceUUID1();
            if (getFace) {
                objextResult.setFaceUUID(faceUuid);
            }
        }
        if (objType == ObjTypeEnum.FACES.getValue()) {
            FaceResult faceResult = JSONObject.parseObject(rs.toString(), FaceResult.class);
            String connectObjectId = faceResult.getConnectObjectId();
            Integer connectObjectType = faceResult.getConnectObjectType();
            objextResult.setConnectObjectId(connectObjectId);
            objextResult.setConnectObjectType(connectObjectType);
        }
        //快照坐标
        Integer leftTopX = analysisResultBo.getLeftTopX() == null ? 0 : analysisResultBo.getLeftTopX();
        Integer rightBtmX = analysisResultBo.getRightBtmX() == null ? 0 : analysisResultBo.getRightBtmX();
        Integer leftTopY = analysisResultBo.getLeftTopY() == null ? 0 : analysisResultBo.getLeftTopY();
        Integer rightBtmY = analysisResultBo.getRightBtmY() == null ? 0 : analysisResultBo.getRightBtmY();
        Double dw = (double) (rightBtmX - leftTopX);//宽
        Double dh = (double) (rightBtmY - leftTopY);//高
        objextResult.setX(leftTopX.shortValue());
        objextResult.setY(leftTopY.shortValue());
        objextResult.setW(dw.shortValue());
        objextResult.setH(dh.shortValue());
        objextResult.setFaceUrl1(getFaceUrl(objextResult.getFaceUUID()));
        objextResult.setFaceUUID(objextResult.getFaceUUID());
        return objextResult;
    }

    private int parseAge(PersonResult personResult) {
        //年龄
        int ageUpLimit = personResult.getAgeUpLimit();
        int ageLowerLimit = personResult.getAgeLowerLimit();
        if (ageUpLimit >= 1 && ageLowerLimit <= 16) {
            return Age.CHILD.getValue();
        } else if (ageUpLimit >= 17 && ageLowerLimit <= 30) {
            return Age.YOUNG.getValue();
        } else if (ageUpLimit >= 31 && ageLowerLimit <= 55) {
            return Age.MIDDLE.getValue();
        } else if (ageUpLimit >= 56 && ageLowerLimit <= 100) {
            return Age.OLD.getValue();
        } else {
            return Age.UNKNOW.getValue();
        }
    }

    private String getColorParam(String upcolorStr) {
        String[] split = upcolorStr.split(",");
        List<HumanColorModel> humanColorModels = humanColorModelMapper.selectList(new QueryWrapper<HumanColorModel>().in("color_bgr_tag", split));
        Set<String> colors = new HashSet<>();
        for (HumanColorModel humanColorModel : humanColorModels) {
            colors.add(humanColorModel.getInfo1());
        }
        return String.join(",", colors);
    }

    private String getColorResult(String info1) {
        return RgbConstants.COLOR_CODEFORMAT.get(info1);
    }

    //TODO 不需要每次查询数据库
    private String getColorResultOld(String colorBarTag) {
        List<HumanColorModel> humanColorModels = humanColorModelMapper.selectList(new QueryWrapper<HumanColorModel>().eq("color_bgr_tag", colorBarTag));
        if (humanColorModels != null && !humanColorModels.isEmpty()) {
            return humanColorModels.get(0).getColorName();
        } else {
            return "未知";
        }
    }

    //上身款式  类型string 长袖98，短袖97, 未知99
    private String getCoatStyle(String coatStyle) {
        if (coatStyle.equals("1")) {
            return "98";
        } else if (coatStyle.equals("2")) {
            return "97";
        } else {
            return "99";
        }
    }

    //下身款式  类型string  长裤98，短裤97，裙子96, 未知99
    private String getTrousersStyle(String trousersStyle) {
        if (trousersStyle.equals("1")) {
            return "98";
        } else if (trousersStyle.equals("2")) {
            return "97";
        } else if (trousersStyle.equals("3")) {
            return "96";
        } else {
            return "99";
        }
    }

    //上身款式  类型string 长袖98，短袖97, 未知99
    private String getCoatStyleReuslt(String coatStyle) {
        if (coatStyle.equals("98")) {
            return "1";
        } else if (coatStyle.equals("97")) {
            return "2";
        } else {
            return "-1";
        }
    }

    //获取人脸信息
    private String getFaceUrl(String faceUUID) {
        String faceImgURL = null;
        if (StringUtils.isNotEmpty(faceUUID)) {
            ResultQueryVo resultQueryVo = resultService.getResultBoById("3", faceUUID);
            if (resultQueryVo != null) {
                faceImgURL = resultQueryVo.getImgurl();
            }
        }
        return faceImgURL;
    }

    //下身款式  类型string  长裤98，短裤97，裙子96, 未知99
    private String getTrousersStyleResult(String trousersStyle) {
        if (trousersStyle.equals("98")) {
            return "1";
        } else if (trousersStyle.equals("97")) {
            return "2";
        } else if (trousersStyle.equals("96")) {
            return "3";
        } else {
            return "-1";
        }
    }

    @Override
    public String groupResult(Map<String, Object> paramMap) {
        AnalysisResultBo inParam = initInputParamCommon(paramMap);
        return StandardRequestUtil.groupByQuery(initKeensenseUrl(), inParam);
    }

    @Override
    public AnalysisResultBo transOneResultToBo(String retJson) {
        Var var = Var.fromJson(retJson);
        WeekArray weekArray = null;
        int len = 0;
        if (len == 0) {
            weekArray = var.getArray("UnitListObject.UnitObject");
            len = weekArray.getSize();
        }
        if (len == 0) {
            weekArray = var.getArray("PersonListObject.PersonObject");
            len = weekArray.getSize();
        }
        if (len == 0) {
            weekArray = var.getArray("MotorVehicleListObject.MotorVehicleObject");
            len = weekArray.getSize();
        }
        if (len == 0) {
            weekArray = var.getArray("NonMotorVehicleListObject.NonMotorVehicleObject");
            len = weekArray.getSize();
        }
        if (len == 0) {
            weekArray = var.getArray("FaceListObject.FaceObject");
        }
        Var reVar = weekArray.get("0");
        return JSONObject.parseObject(reVar.toString(), AnalysisResultBo.class);
    }

    public static String getWheels(Byte value) {
        switch (value) {
            case 1:
                return "二轮车";
            case 2:
                return "二轮车";
            case 3:
                return "三轮车";
            default:
                return "未知";
        }
    }
}
