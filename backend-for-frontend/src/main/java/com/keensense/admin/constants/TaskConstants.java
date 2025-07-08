package com.keensense.admin.constants;

/**
 * 任务请求路径常量定义
 */
public class TaskConstants {
    private TaskConstants() {
    }

    /**
     * 根据搜索类型返回搜索名称
     */
    public static String getNameByTargetType(String targetType) {
        switch (targetType) {
            case "1":
                return "搜人";
            case "2":
                return "搜车";
            case "3":
                return "搜脸";
            case "4":
                return "搜骑";
            default:
                return "搜图";
        }

    }
}
