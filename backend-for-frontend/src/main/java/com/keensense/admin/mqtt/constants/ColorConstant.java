package com.keensense.admin.mqtt.constants;

import java.util.HashMap;
import java.util.Map;

public class ColorConstant {

    /**
     * 1400定义颜色
     */
    public final static Map<Integer,String> COLOR = new HashMap<>();
    static {
        COLOR.put(1,"黑");
        COLOR.put(2,"白");
        COLOR.put(3,"灰");
        COLOR.put(4,"红");
        COLOR.put(5,"蓝");
        COLOR.put(6,"黄");
        COLOR.put(7,"橙");
        COLOR.put(8,"棕");
        COLOR.put(9,"绿");
        COLOR.put(10,"紫");
        COLOR.put(11,"青");
        COLOR.put(12,"粉");
        COLOR.put(13,"透明");
        COLOR.put(99,"其他");
    }
}
