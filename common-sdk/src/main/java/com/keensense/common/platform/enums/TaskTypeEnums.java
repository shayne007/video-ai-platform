package com.keensense.common.platform.enums;

import org.springframework.web.servlet.support.RequestContext;

public enum TaskTypeEnums {
    // 注释
    REAL_LINE(1, "实时流"),

    OFF_LINE(2, "离线视频"),

    VIDEO_LINE(3, "录像任务"),

    SUMMARY_LINE(4, "浓缩视频");

    private int value;
    private String desc;

    private TaskTypeEnums(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }


    public static TaskTypeEnums get(int value) {
        for (TaskTypeEnums type : TaskTypeEnums.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

}
