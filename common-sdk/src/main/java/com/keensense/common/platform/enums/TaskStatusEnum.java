package com.keensense.common.platform.enums;

public enum TaskStatusEnum {
    // 注释
    WAIT(0, "等待分析"),

    ANALYSIS(1, "分析中"),

    COMPLETED(2, "分析完成"),

    FAIL(3, "分析失败");

    private int value;
    private String desc;

    TaskStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static TaskStatusEnum get(int value) {
        for (TaskStatusEnum type : TaskStatusEnum.values()) {
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
