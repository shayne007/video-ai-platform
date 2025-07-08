package com.keensense.common.platform.enums;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 11:27 2019/9/26
 * @Version v0.1
 */
public enum TypeEnums {
    OBJEXT("objext", "全目标（包括行人，车辆，人脸，骑行）"),
    SUMMARY("summary", "视频浓缩"),
    PEROSON_DESITTY("personDensity", "人群密度任务"),
    GATE_OTHER("other", "卡口任务类型");
    private String value;
    private String desc;

    private TypeEnums(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static TypeEnums get(String value) {
        for(TypeEnums type: TypeEnums.values()) {
            if(type.value == value) {
                return type;
            }
        }
        return null;
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
