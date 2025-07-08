package com.keensense.admin.mqtt.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;


/**
 * @Author: shitao
 * @Description: 获取nacos配置的细类属性值数据
 * @Date: Created in 11:28 2019/11/28
 * @Version v0.1
 */
@Data
@Configuration
@RefreshScope
public class EnumerationConfig {

    /**
     * 人形
     */
    @Value("${enumeration.Persons.trousersStyle}")
    private String trousersStyle;

    @Value("${enumeration.Persons.handBag}")
    private String handBag;

    @Value("${enumeration.Persons.umbrella}")
    private String umbrella;

    @Value("${enumeration.Persons.cap}")
    private String cap;

    @Value("${enumeration.Persons.hairStyle}")
    private String hairStyle;

    @Value("${enumeration.Persons.trousersTexture}")
    private String trousersTexture;

    @Value("${enumeration.Persons.luggage}")
    private String luggage;

    @Value("${enumeration.Persons.trolley}")
    private String trolley;

    @Value("${enumeration.Persons.hasKnife}")
    private String hasKnife;

    @Value("${enumeration.Persons.chestHold}")
    private String chestHold;

    @Value("${enumeration.Persons.shape}")
    private String shape;

    @Value("${enumeration.Persons.minority}")
    private String minority;

    /**
     * 骑行
     */
    @Value("${enumeration.NonMotorVehicles.hasPlate}")
    private String hasPlate;

    @Value("${enumeration.NonMotorVehicles.bikeGenre}")
    private String bikeGenre;

    @Value("${enumeration.NonMotorVehicles.wheels}")
    private String wheels;

    @Value("${enumeration.NonMotorVehicles.helmet}")
    private String helmet;

    @Value("${enumeration.NonMotorVehicles.umbrella}")
    private String bikeUmbrella;

    @Value("${enumeration.NonMotorVehicles.lampShape}")
    private String lampShape;

    @Value("${enumeration.NonMotorVehicles.carryPassenger}")
    private String carryPassenger;

    /**
     * 车辆
     */
    @Value("${enumeration.MotorVehicles.hasCall}")
    private String hasCall;

    @Value("${enumeration.MotorVehicles.vehicleBrand}")
    private String vehicleBrand;

    @Value("${enumeration.MotorVehicles.plateClass}")
    private String plateClass;

    @Value("${enumeration.MotorVehicles.vehicleClass}")
    private String vehicleClass;

    @Value("${enumeration.MotorVehicles.hasCrash}")
    private String hasCrash;

    @Value("${enumeration.MotorVehicles.hasDanger}")
    private String hasDanger;

    @Value("${enumeration.MotorVehicles.safetyBelt}")
    private String safetyBelt;

    @Value("${enumeration.MotorVehicles.secondBelt}")
    private String secondBelt;

    @Value("${enumeration.MotorVehicles.sun}")
    private String sun;

    @Value("${enumeration.MotorVehicles.drop}")
    private String drop;

    @Value("${enumeration.MotorVehicles.paper}")
    private String paper;

    @Value("${enumeration.MotorVehicles.rack}")
    private String rack;

    @Value("${enumeration.MotorVehicles.sunRoof}")
    private String sunRoof;

    @Value("${enumeration.MotorVehicles.decoration}")
    private String decoration;

    @Value("${enumeration.MotorVehicles.vehicleHasPlate}")
    private String vehicleHasPlate;

    @Value("${enumeration.MotorVehicles.aerial}")
    private String aerial;

    @Value("${enumeration.MotorVehicles.vehicleModel}")
    private String vehicleModel;


    /**
     * 公共部分
     */
    @Value("${enumeration.common.sex}")
    private String sex;

    @Value("${enumeration.common.coatStyle}")
    private String coatStyle;

    @Value("${enumeration.common.colorGroups}")
    private String colorGroups;

    @Value("${enumeration.common.age}")
    private String age;

    @Value("${enumeration.common.angle}")
    private String angle;

    @Value("${enumeration.common.direction}")
    private String direction;

    @Value("${enumeration.common.coatTexture}")
    private String coatTexture;

    @Value("${enumeration.common.glasses}")
    private String glasses;

    @Value("${enumeration.common.respirator}")
    private String respirator;

    @Value("${enumeration.common.bag}")
    private String bag;

}
