package com.keensense.admin.mqtt.enums;

public enum ResultEnums {

    // 人形
    PERSON_NAME("person","人形名称"),
    PERSON_LIST_OBJECT_NAME("PersonObject","人形内层名称"),
    PERSON_OBJECT_NAME("PersonListObject","人形外层名称"),
    PERSON_COUNT("personCount","人形统计数量"),

    // 人脸
    FACE_NAME("face","人脸名称"),
    FACE_LIST_OBJECT_NAME("FaceObject","人脸内层名称"),
    FACE_OBJECT_NAME("FaceListObject","人脸外层名称"),
    FACE_COUNT("faceCount","人脸统计数量"),

    // 机动车
    CAR_NAME("car","机动车名称"),
    MOTORVEHICLE_LIST_OBJECT_NAME("MotorVehicleObject","机动车内层名称"),
    MOTORVEHICLE_OBJECT_NAME("MotorVehicleListObject","机动车外层名称"),
    CAR_COUNT("carCount","机动车统计数量"),

    // 非机动车
    BIKE_NAME("bike","非机动车名称"),
    NONMOTORVEHICLE_LIST_OBJECT_NAME("NonMotorVehicleObject","非机动车内层名称"),
    NONMOTORVEHICLE_OBJECT_NAME("NonMotorVehicleListObject","非机动车外层名称"),
    BIKE_COUNT("bikeCount","非机动车统计数量");

    private String value;
    private String desc;

    ResultEnums(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
