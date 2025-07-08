package com.keensense.admin.mqtt.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.admin.mqtt.constants.CarConstant;
import com.keensense.admin.mqtt.constants.ColorConstant;
import com.keensense.admin.mqtt.domain.Result;
import com.keensense.admin.mqtt.domain.VlprResult;
import com.keensense.admin.mqtt.enums.ResultEnums;
import com.keensense.admin.mqtt.utils.IDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@Slf4j
public class CarConvertService extends AbstractConvertService{

    @SuppressWarnings("all")
    @Override
    public void dataConvert(JSONObject jsonObject, Result result){
        VlprResult car = (VlprResult) result;
        // 存取公共特性
        super.setCommonProperties(jsonObject, car, ResultEnums.CAR_NAME.getValue(), null);

        // 1400 机动车特征id 48位
        car.setMotorVehicleID(IDUtil.getLongId());
        car.setId(car.getMotorVehicleID());

        JSONObject features = (JSONObject) jsonObject.get("features");
        //JSONObject safetyBelt = (JSONObject) features.get("safetyBelt");

        // 主驾
        //car.setMainDriver(String.valueOf(safetyBelt.get("mainDriver")));
        // 副驾
        //car.setCoDriver(String.valueOf(safetyBelt.get("coDriver")));

        // 品牌名
        String brandName = String.valueOf(features.get("brandName"));
        if(StringUtils.isNotEmpty(brandName)){
            // 该字段包含三部分信息 分别是 品牌 款式 年份
            String[] brandArr = brandName.split("-");
            String vehicleBrand = CarConstant.CARBRAND.get(brandArr[0]);
            if(StringUtils.isEmpty(vehicleBrand)){
                // 0 代表 其他
                vehicleBrand = "0";
            }
            if(brandArr.length >= 3){
                car.setVehicleBrand(vehicleBrand);
                car.setVehicleModel(brandArr[1]);
                car.setVehicleStyles(brandArr[2]);
            }else if(brandArr.length == 2){
                car.setVehicleBrand(vehicleBrand);
                car.setVehicleModel(brandArr[1]);
            }else {
                car.setVehicleBrand(vehicleBrand);
            }
        }

        // 是否在打电话
        Integer hasCall = Integer.parseInt(String.valueOf(features.get("hasCall")));
        if("没打电话".equals(CarConstant.CALLING.get(hasCall))){
            car.setCalling(0);
        }else if("在打电话".equals(CarConstant.CALLING.get(hasCall))){
            car.setCalling(1);
        }

        //
        car.setHasCrash(String.valueOf(features.get("hasCrash")));

        //
        car.setHasDanger(String.valueOf(features.get("hasDanger")));

        // 车辆类型
        String type = String.valueOf(features.get("type"));
        //car.setVehicleKind(type);

        // 车辆类型 根据GA/T 16.4来转化
        for (Map.Entry<String,String> typeEntry : CarConstant.CARTYPE.entrySet()) {
            if(typeEntry.getValue().equals(type)){
                car.setVehicleClass(CarConstant.CARCLASS.get(typeEntry.getKey()));
                break;
            }
        }

        // 车辆类型编码
        car.setTypeCode(String.valueOf(features.get("typeCode")));

        // 车身颜色id
        car.setColorCode(String.valueOf(features.get("colorCode")));

        // 1400 车身颜色
        String vehicleColor = String.valueOf(features.get("colorName"));
        for (Map.Entry<Integer,String> entry : ColorConstant.COLOR.entrySet()) {
            if(vehicleColor.contains(entry.getValue())){
                car.setVehicleColor(String.valueOf(entry.getKey()));
                break;
            }
        }

        // 车窗标志物数目
        car.setTagNum(String.valueOf(features.get("tagNum")));

        // 车前部物品：遮阳板
        Integer sun = Integer.parseInt(String.valueOf(features.get("sun")));
        car.setSun(String.valueOf(sun));
        if("有遮阳板".equals(CarConstant.SUN_PLATE.get(sun))){
            if(StringUtils.isNotEmpty(car.getVehicleFrontItem())){
                car.setVehicleFrontItem(car.getVehicleFrontItem() + ",4");
                car.setDescOfFrontItem(car.getDescOfFrontItem() + ",有遮阳板");
            }else {
                car.setVehicleFrontItem("4");
                car.setDescOfFrontItem("有遮阳板");
            }
        }

        // 车前部物品：有无挂饰
        Integer drop = Integer.parseInt(String.valueOf(features.get("drop")));
        car.setDrop(String.valueOf(drop));
        if("有挂饰".equals(CarConstant.DROP.get(drop))){
            if(StringUtils.isNotEmpty(car.getVehicleFrontItem())){
                car.setVehicleFrontItem(car.getVehicleFrontItem() + ",3");
                car.setDescOfFrontItem(car.getDescOfFrontItem() + ",有挂饰");
            }else {
                car.setVehicleFrontItem("3");
                car.setDescOfFrontItem("有挂饰");
            }
        }

        // 车前部物品：有无纸巾盒
        Integer paper = Integer.parseInt(String.valueOf(features.get("paper")));
        car.setPaper(String.valueOf(paper));
        if("有纸巾盒".equals(CarConstant.PAPER.get(paper))){
            if(StringUtils.isNotEmpty(car.getVehicleFrontItem())){
                car.setVehicleFrontItem(car.getVehicleFrontItem() + ",7");
                car.setDescOfFrontItem(car.getDescOfFrontItem() + ",有纸巾盒");
            }else {
                car.setVehicleFrontItem("7");
                car.setDescOfFrontItem("有纸巾盒");
            }
        }

        // 车牌颜色
        String plateColorName = String.valueOf(features.get("plateColorName"));
        for (Map.Entry<Integer,String> entry : ColorConstant.COLOR.entrySet()) {
            if(plateColorName.contains(entry.getValue())){
                car.setPlateColor(String.valueOf(entry.getKey()));
                break;
            }
        }

        // 车牌颜色编码
        car.setPlateColorCode(String.valueOf(features.get("plateColorCode")));

        // 车牌号
        car.setPlateNo(String.valueOf(features.get("plateLicence")));

        // 有无车牌
        Integer hasPlate = Integer.parseInt(String.valueOf(features.get("hasPlate")));
        if("无车牌".equals(CarConstant.PLATE.get(hasPlate))){
            car.setHasPlate("0");
        }else if("有车牌".equals(CarConstant.PLATE.get(hasPlate))){
            car.setHasPlate("1");
            // 存入1400的车前置物品
            if(StringUtils.isNotEmpty(car.getVehicleFrontItem())){
                car.setVehicleFrontItem(car.getVehicleFrontItem() + ",8");
                car.setDescOfFrontItem(car.getDescOfFrontItem() + ",有车牌");
            }else {
                car.setVehicleFrontItem("8");
                car.setDescOfFrontItem("有车牌");
            }
        }

        // 1400 号牌种类 待转
        car.setPlateClass(String.valueOf(features.get("plateClass")));

        // 车牌类型id
        car.setPlateClassCode(String.valueOf(features.get("plateClassCode")));

        // 是否有天窗
        Integer sunRoof = (Integer) features.get("sunRoof");
        car.setSunRoof(sunRoof);
        if("有天窗".equals(CarConstant.SUNROOF.get(sunRoof))){
            if(StringUtils.isNotEmpty(car.getVehicleFrontItem())){
                car.setVehicleFrontItem(car.getVehicleFrontItem() + ",10");
                car.setDescOfFrontItem(car.getDescOfFrontItem() + ",有天窗");
            }else {
                car.setVehicleFrontItem("10");
                car.setDescOfFrontItem("有天窗");
            }
        }

        // 备用轮胎
        Integer spareTire = (Integer) features.get("spareTire");
        car.setSpareTire(spareTire);
        if("有备胎".equals(CarConstant.SPARETIRE.get(spareTire))){
            if(StringUtils.isNotEmpty(car.getVehicleRearItem())){
                car.setVehicleRearItem(car.getVehicleRearItem() + ";99");
                car.setDescOfRearItem(car.getDescOfRearItem() + ";有备胎");
            }else {
                car.setVehicleRearItem("99");
                car.setDescOfRearItem("有备胎");
            }
        }

        // 行李架
        Integer rack = (Integer) features.get("rack");
        car.setRack(rack);
        if("有行李架".equals(CarConstant.RACK.get(rack))){
            if(StringUtils.isNotEmpty(car.getVehicleRearItem())){
                car.setVehicleFrontItem(car.getVehicleRearItem() + ",11");
                car.setDescOfRearItem(car.getDescOfRearItem() + ",有行李架");
            }else {
                car.setVehicleRearItem("11");
                car.setDescOfRearItem("有行李架");
            }
        }

        //car.setPlateLocationInfo(((JSONObject) features.get("plateLocationInfo")).toJSONString());

        // 车型自信度
        car.setVehicleConfidence((Integer) features.get("vehicleConfidence"));

        // 车牌自信度
        car.setPlateConfidence(String.valueOf(features.get("plateConfidence")));

        // 乘客信息
        JSONArray passengers = (JSONArray) features.get("passengers");
        for (Object object : passengers) {
            JSONObject passenger = (JSONObject) object;
            if("主驾".equals(CarConstant.MAIA_DRIVER.get(Integer.parseInt(String.valueOf(passenger.get("driver")))))){
                // 是否主驾
                car.setMainDriver("1");
                // 是否有系安全带
                car.setSafetyBelt(Integer.parseInt(String.valueOf(passenger.get("hasBelt"))));
                // 是否有吸烟
                car.setHasSmoke(String.valueOf(passenger.get("hasSmoke")));
                // 面部是否被遮挡
                car.setHasFaceCover(String.valueOf(passenger.get("hasFaceCover")));
                // 性别
                car.setSex(String.valueOf(passenger.get("sex")));
                // 是否有打电话
                car.setCalling(Integer.parseInt(String.valueOf(passenger.get("hasCall"))));
            }else if("副驾".equals(CarConstant.MAIA_DRIVER.get(Integer.parseInt(String.valueOf(passenger.get("driver")))))){
                // 是否副驾
                car.setCoDriver("1");
                car.setSecondBelt(Integer.parseInt(String.valueOf(passenger.get("hasBelt"))));
            }
        }
    }
}
