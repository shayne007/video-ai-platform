package com.keensense.admin.mqtt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.keensense.admin.mqtt.constants.ColorConstant;
import com.keensense.admin.mqtt.constants.PersonConstant;
import com.keensense.admin.mqtt.domain.PersonResult;
import com.keensense.admin.mqtt.domain.Result;
import com.keensense.admin.mqtt.enums.ResultEnums;
import com.keensense.admin.mqtt.utils.IDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class PersonConvertService extends AbstractConvertService {

    @SuppressWarnings("all")
    @Override
    public void dataConvert(JSONObject jsonObject, Result result){
        PersonResult person = (PersonResult) result;

        // 存取公共特性
        super.setCommonProperties(jsonObject, person, ResultEnums.PERSON_NAME.getValue(), null);

        // 1400 人形特征id 48位
        person.setPersonID(IDUtil.getLongId());
        person.setId(person.getPersonID());

        // 处理 Person 特有字段
        person.setLocationMarkTime(person.getMarkTime());
        person.setPersonAppearTime(person.getAppearTime());
        person.setPersonDisAppearTime(person.getDisappearTime());

        // 上身衣着
        String upperClothing = String.valueOf(jsonObject.get("upperClothing"));
        person.setUpperClothing(upperClothing);
        // 1400 上衣款式 取值按园区拓展取值
        if("-1".equals(upperClothing)){
            // 未知
            person.setCoatStyle("99");
        }else if("1".equals(upperClothing)){
            // 长袖
            person.setCoatStyle("98");
        }else if("2".equals(upperClothing)){
            // 短袖
            person.setCoatStyle("97");
        }

        // 下身衣着
        String lowerClothing = String.valueOf(jsonObject.get("lowerClothing"));
        person.setLowerClothing(lowerClothing);
        // 1400 裤子款式 取值按园区拓展取值
        if("-1".equals(lowerClothing)){
            // 未知
            person.setTrousersStyle("99");
        }else if("1".equals(lowerClothing)){
            // 长裤
            person.setTrousersStyle("98");
        }else if("2".equals(lowerClothing)){
            // 短裤
            person.setTrousersStyle("97");
        }else if("3".equals(lowerClothing)){
            // 裙子
            person.setTrousersStyle("96");
        }

        JSONObject features = (JSONObject) jsonObject.get("features");

        // 附属物 是否戴口罩
        Integer respirator = (Integer) features.get("respirator");
        person.setRespirator(String.valueOf(respirator));
        if("有戴口罩".equals(PersonConstant.RESPIRATOR.get(respirator))){
            if(StringUtils.isNotEmpty(person.getAppendant())){
                person.setAppendant(person.getAppendant() + ";3");
                person.setAppendantDescription(person.getAppendantDescription() + ";戴口罩");
            }else {
                person.setAppendant("3");
                person.setAppendantDescription("戴口罩");
            }
        }

        // 上衣纹理
        person.setCoatTexture((Integer) features.get("coatTexture"));

        // 附属物 是否戴帽子
        Integer cap = (Integer) features.get("cap");
        person.setCap(cap);
        if("有戴帽子".equals(PersonConstant.CAP.get(cap))){
            if(StringUtils.isNotEmpty(person.getAppendant())){
                person.setAppendant(person.getAppendant() + ";7");
                person.setAppendantDescription(person.getAppendantDescription() + ";戴帽子");
            }else {
                person.setAppendant("7");
                person.setAppendantDescription("戴帽子");
            }
        }

        // 发型 转成1400要求的值
        Integer hairStyle = (Integer) features.get("hairStyle");
        if("未知".equals(PersonConstant.HAIRSTYLE.get(hairStyle))){
            person.setHairStyle("99");
        }else if("长发".equals(PersonConstant.HAIRSTYLE.get(hairStyle))){
            person.setHairStyle("11");
        }else if("短发".equals(PersonConstant.HAIRSTYLE.get(hairStyle))){
            person.setHairStyle("1");
        }

        // 下衣纹理
        person.setTrousersTexture((Integer) features.get("trousersTexture"));

        // 附属物 拉杆箱
        Integer luggage = (Integer) features.get("luggage");
        person.setLuggage(luggage);
        if("有拉杆箱".equals(PersonConstant.LUGGAGE.get(luggage))){
            if(StringUtils.isNotEmpty(person.getAppendant())){
                person.setAppendant(person.getAppendant() + ";99");
                person.setAppendantDescription(person.getAppendantDescription() + ";有拉杆箱");
            }else {
                person.setAppendant("99");
                person.setAppendantDescription("有拉杆箱");
            }
        }

        // 附属物 手推车
        Integer trolley = (Integer) features.get("trolley");
        person.setTrolley(trolley);
        if("有手推车".equals(PersonConstant.TROLLEY.get(trolley))){
            if(StringUtils.isNotEmpty(person.getAppendant())){
                person.setAppendant(person.getAppendant() + ";99");
                person.setAppendantDescription(person.getAppendantDescription() + ";有手推车");
            }else {
                person.setAppendant("99");
                person.setAppendantDescription("有手推车");
            }
        }

        // 上衣颜色主
        JSONObject coatColor1Obj = (JSONObject) features.get("coatColor1");
        String coatColor1 = String.valueOf(coatColor1Obj.get("code"));
        person.setUpcolorTag1(Integer.parseInt(coatColor1));
        String coatColorName1 = String.valueOf(coatColor1Obj.get("value"));
        for (Map.Entry<Integer,String> entry : ColorConstant.COLOR.entrySet()) {
            if(coatColorName1.contains(entry.getValue())){
                person.setCoatColor(String.valueOf(entry.getKey()));
                break;
            }
        }

        // 上衣颜色辅
        JSONObject coatColor2Obj = (JSONObject) features.get("coatColor2");
        String coatColor2 = String.valueOf(coatColor2Obj.get("code"));
        person.setUpcolorTag2(Integer.parseInt(coatColor2));

        // 裤子颜色主
        JSONObject trousersColor1Obj = (JSONObject) features.get("trousersColor1");
        String trousersColor1 = String.valueOf(trousersColor1Obj.get("code"));
        person.setLowcolorTag1(Integer.parseInt(trousersColor1));
        String trousersColorName1 = String.valueOf(trousersColor1Obj.get("value"));
        for (Map.Entry<Integer,String> entry : ColorConstant.COLOR.entrySet()) {
            if(trousersColorName1.contains(entry.getValue())){
                person.setTrousersColor(String.valueOf(entry.getKey()));
                break;
            }
        }

        // 裤子颜色辅
        JSONObject trousersColor2Obj = (JSONObject) features.get("trousersColor2");
        String trousersColor2 = String.valueOf(trousersColor2Obj.get("code"));
        person.setLowcolorTag2(Integer.parseInt(trousersColor2));

        // 性别代码
        Integer sex = (Integer) features.get("sex");
        if("未知".equals(PersonConstant.SEX.get(sex))){
            person.setGenderCode("0");
        }else if("男性".equals(PersonConstant.SEX.get(sex))){
            person.setGenderCode("1");
        }else if("女性".equals(PersonConstant.SEX.get(sex))){
            person.setGenderCode("2");
        }

        // 年龄
        Integer age = (Integer) features.get("age");
        switch (age){
            case -1:
                person.setAgeLowerLimit(-1);
                person.setAgeUpLimit(-1);
                break;
            case 4:
                person.setAgeLowerLimit(1);
                person.setAgeUpLimit(16);
                break;
            case 8:
                person.setAgeLowerLimit(17);
                person.setAgeUpLimit(30);
                break;
            case 16:
                person.setAgeLowerLimit(31);
                person.setAgeUpLimit(55);
                break;
            case 32:
                person.setAgeLowerLimit(56);
                person.setAgeUpLimit(100);
                break;
            default:
                person.setAgeUpLimit(-1);
                person.setAgeLowerLimit(-1);
        }

        // 角度
        person.setAngle((Integer) features.get("angle"));

        // 附属物：是否背包
        Integer bag = (Integer) features.get("bag");
        person.setBag(String.valueOf(bag));
        if("有背包".equals(PersonConstant.BAG.get(bag))){
            if(StringUtils.isNotEmpty(person.getAppendant())){
                person.setAppendant(person.getAppendant() + ";8");
                person.setAppendantDescription(person.getAppendantDescription() + ";有背包");
            }else {
                person.setAppendant("8");
                person.setAppendantDescription("有背包");
            }
        }

        // 附属物：是否有手提包
        Integer carryBag = (Integer) features.get("carryBag");
        person.setCarryBag(String.valueOf(carryBag));
        if("有手提包".equals(PersonConstant.CARRYBAG.get(carryBag))){
            if(StringUtils.isNotEmpty(person.getAppendant())){
                person.setAppendant(person.getAppendant() + ";8");
                person.setAppendantDescription(person.getAppendantDescription() + ";有手提包");
            }else {
                person.setAppendant("8");
                person.setAppendantDescription("有手提包");
            }
        }

        // 附属物：是否有眼镜
        Integer glasses = (Integer) features.get("glasses");
        person.setGlasses(String.valueOf(glasses));
        if("有戴眼镜".equals(PersonConstant.GLASSES.get(glasses))){
            if(StringUtils.isNotEmpty(person.getAppendant())){
                person.setAppendant(person.getAppendant() + ";6");
                person.setAppendantDescription(person.getAppendantDescription() + ";有戴眼镜");
            }else {
                person.setAppendant("6");
                person.setAppendantDescription("有戴眼镜");
            }
        }

        // 附属物：是否打伞
        Integer umbrella = (Integer) features.get("umbrella");
        person.setUmbrella(String.valueOf(umbrella));
        if("有打伞".equals(PersonConstant.UMBRELLA.get(umbrella))){
            if(StringUtils.isNotEmpty(person.getAppendant())){
                person.setAppendant(person.getAppendant() + ";2");
                person.setAppendantDescription(person.getAppendantDescription() + ";有打伞");
            }else {
                person.setAppendant("2");
                person.setAppendantDescription("有打伞");
            }
        }

        // 快检增加 附属物 行人是否手持刀具 字段
        Integer weapon = (Integer) features.get("weapon");
        person.setHasKnife(String.valueOf(weapon));
    }


}
