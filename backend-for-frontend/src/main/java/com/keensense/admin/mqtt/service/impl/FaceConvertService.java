package com.keensense.admin.mqtt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.keensense.admin.mqtt.constants.PersonConstant;
import com.keensense.admin.mqtt.domain.FaceResult;
import com.keensense.admin.mqtt.domain.Result;
import com.keensense.admin.mqtt.enums.ResultEnums;
import com.keensense.admin.mqtt.utils.IDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class FaceConvertService extends AbstractConvertService {

    @SuppressWarnings("all")
    @Override
    public void dataConvert(JSONObject jsonObject, Result result){
        FaceResult personFace = (FaceResult) result;
        // 判断是否需要存储人脸对象 faceUrl是否为空
        JSONObject features = (JSONObject) jsonObject.get("features");

        String faceUrl = String.valueOf(features.get("faceUrl"));

        if(StringUtils.isEmpty(faceUrl)){
            return;
        }

        // 存取公共特性
        super.setCommonProperties(jsonObject, personFace, ResultEnums.FACE_NAME.getValue(), faceUrl);

        // 替换一些公有字段

        // objType
        personFace.setObjType(3);

        // 人脸图片
        personFace.setFaceUrl(faceUrl);

        // 人脸小图
        personFace.setImgUrl(faceUrl);

        // 1400 人脸特征id 48位
        personFace.setFaceID(IDUtil.getLongId());
        personFace.setId(personFace.getFaceID());

        // 处理 Face 特有字段
        personFace.setLocationMarkTime(personFace.getMarkTime());
        personFace.setFaceAppearTime(personFace.getAppearTime());
        personFace.setFaceDisAppearTime(personFace.getDisappearTime());

        // 人脸坐标
        JSONObject faceBoundingBox = (JSONObject) features.get("faceBoundingBox");
        if(!Objects.isNull(faceBoundingBox)){
            personFace.setLeftTopX((Integer.parseInt(String.valueOf(faceBoundingBox.get("x")))));
            personFace.setLeftTopY((Integer.parseInt(String.valueOf(faceBoundingBox.get("y")))));
            personFace.setRightBtmX(personFace.getLeftTopX() + (Integer.parseInt(String.valueOf(faceBoundingBox.get("w")))));
            personFace.setRightBtmY(personFace.getLeftTopY() + (Integer.parseInt(String.valueOf(faceBoundingBox.get("h")))));
        }

        // 发型 转成1400要求的值
        Integer hairStyle = (Integer) features.get("hairStyle");
        if("未知".equals(PersonConstant.HAIRSTYLE.get(hairStyle))){
            personFace.setHairStyle("99");
        }else if("长发".equals(PersonConstant.HAIRSTYLE.get(hairStyle))){
            personFace.setHairStyle("11");
        }else if("短发".equals(PersonConstant.HAIRSTYLE.get(hairStyle))){
            personFace.setHairStyle("1");
        }

        // 性别代码
        Integer sex = (Integer) features.get("sex");
        if("未知".equals(PersonConstant.SEX.get(sex))){
            personFace.setGenderCode("0");
        }else if("男性".equals(PersonConstant.SEX.get(sex))){
            personFace.setGenderCode("1");
        }else if("女性".equals(PersonConstant.SEX.get(sex))){
            personFace.setGenderCode("2");
        }

        // 年龄
        Integer age = (Integer) features.get("age");
        switch (age){
            case -1:
                personFace.setAgeLowerLimit(-1);
                personFace.setAgeUpLimit(-1);
                break;
            case 4:
                personFace.setAgeLowerLimit(1);
                personFace.setAgeUpLimit(16);
                break;
            case 8:
                personFace.setAgeLowerLimit(17);
                personFace.setAgeUpLimit(30);
                break;
            case 16:
                personFace.setAgeLowerLimit(31);
                personFace.setAgeUpLimit(55);
                break;
            case 32:
                personFace.setAgeLowerLimit(56);
                personFace.setAgeUpLimit(100);
                break;
            default:
                personFace.setAgeUpLimit(-1);
                personFace.setAgeLowerLimit(-1);
        }
    }
}
