package com.keensense.admin.mqtt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.keensense.admin.mqtt.domain.NonMotorVehiclesResult;
import com.keensense.admin.mqtt.domain.Result;
import com.keensense.admin.mqtt.enums.ResultEnums;
import com.keensense.admin.mqtt.utils.IDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class BikeConvertService extends AbstractConvertService{

    @SuppressWarnings("unchecked")
    @Override
    public void dataConvert(JSONObject jsonObject, Result result){
        NonMotorVehiclesResult bike = (NonMotorVehiclesResult) result;
        // 存取公共特性
        super.setCommonProperties(jsonObject, bike, ResultEnums.BIKE_NAME.getValue(), null);

        // 1400 非机动车特征id 48位
        bike.setNonMotorVehicleID(IDUtil.getLongId());
        bike.setId(bike.getNonMotorVehicleID());

        // 上身衣着
        String upperClothing = String.valueOf(jsonObject.get("upperClothing"));
        bike.setUpperClothing(upperClothing);
        // 1400 上衣款式 取值按园区拓展取值
        if("-1".equals(upperClothing)){
            bike.setCoatStyle("99");
        }else if("1".equals(upperClothing)){
            bike.setCoatStyle("98");
        }else if("2".equals(upperClothing)){
            bike.setCoatStyle("97");
        }

        // 下身衣着
        String lowerClothing = String.valueOf(jsonObject.get("lowerClothing"));
        bike.setLowerClothing(lowerClothing);
        // 1400 裤子款式 取值按园区拓展取值
        if("-1".equals(lowerClothing)){
            bike.setTrousersStyle("99");
        }else if("1".equals(lowerClothing)){
            bike.setTrousersStyle("98");
        }else if("2".equals(lowerClothing)){
            bike.setTrousersStyle("97");
        }else if("3".equals(lowerClothing)){
            bike.setTrousersStyle("96");
        }

        JSONObject features = (JSONObject) jsonObject.get("features");

        // 人脸坐标 暂时先不记录
        /*JSONObject faceBoundingBox = (JSONObject) features.get("faceBoundingBox");
        if(!Objects.isNull(faceBoundingBox)){
            bike.setLeftTopX(Integer.parseInt(String.valueOf(faceBoundingBox.get("x"))));
            bike.setLeftTopY(Integer.parseInt(String.valueOf(faceBoundingBox.get("y"))));
            bike.setRightBtmX(bike.getLeftTopX() + Integer.parseInt(String.valueOf(faceBoundingBox.get("w"))));
            bike.setRightBtmY(bike.getLeftTopY() + Integer.parseInt(String.valueOf(faceBoundingBox.get("h"))));
        }*/

        // 人脸图片
        bike.setFaceUrl(String.valueOf(features.get("faceUrl")));

        // 是否戴口罩
        bike.setRespirator((Integer) features.get("respirator"));

        // 上衣纹理
        bike.setCoatTexture((Integer) features.get("coatTexture"));


        JSONObject mainColor1 = (JSONObject) features.get("mainColor1");
        bike.setMainColor1(String.valueOf(mainColor1.get("code")));

        JSONObject mainColor2 = (JSONObject) features.get("mainColor2");
        bike.setMainColor2(String.valueOf(mainColor2.get("code")));

        JSONObject coatColor1 = (JSONObject) features.get("coatColor1");
        bike.setCoatColor1(String.valueOf(coatColor1.get("code")));

        JSONObject coatColor2 = (JSONObject) features.get("coatColor2");
        bike.setCoatColor2(String.valueOf(coatColor2.get("code")));

        JSONObject trousersColor1 = (JSONObject) features.get("trousersColor1");
        bike.setTrousersColor1(String.valueOf(trousersColor1.get("code")));

        JSONObject trousersColor2 = (JSONObject) features.get("trousersColor2");
        bike.setTrousersColor2(String.valueOf(trousersColor2.get("code")));

        // 性别
        bike.setSex((Integer) features.get("sex"));

        // 年龄
        bike.setAge((Integer) features.get("age"));

        // 角度
        bike.setAngle((Integer) features.get("angle"));

        // 是否有背包
        bike.setBag((Integer) features.get("bag"));

        // 是否有手提包
        bike.setCarryBag(String.valueOf(features.get("carryBag")));

        // 是否有眼镜
        bike.setGlasses((Integer) features.get("glasses"));

        // 是否打伞
        bike.setUmbrella((Integer) features.get("umbrella"));

        //
        bike.setWheels(Long.parseLong(String.valueOf(features.get("wheels"))));

        //
        bike.setBikeGenre(Long.parseLong(String.valueOf(features.get("bikeGenre"))));

        // 有无车牌
        bike.setHasPlate(String.valueOf(features.get("bikeHasPlate")));


        JSONObject helmetObject = (JSONObject) features.get("helmet");

        // 是否戴头盔
        Integer have = Integer.parseInt(String.valueOf(helmetObject.get("have")));
        bike.setHelmet(have);

        // 乘客1头盔颜色
        if(have == 1){
            bike.setHelmetColorTag1(Integer.parseInt(String.valueOf(helmetObject.get("color"))));
        }

    }
}
