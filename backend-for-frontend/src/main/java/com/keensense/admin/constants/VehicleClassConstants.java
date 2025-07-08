package com.keensense.admin.constants;

import java.util.*;

/**
 * @Author: shitao
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 15:55 2019/7/29
 * @Version v0.1
 */
public class VehicleClassConstants {


    public final static Map<String, String> CARTYPE_CODEFORMAT = new HashMap<>();
    static {
        CARTYPE_CODEFORMAT.put("K11", "大型客车");
        CARTYPE_CODEFORMAT.put("K19", "公交车");
        CARTYPE_CODEFORMAT.put("K21", "中型客车");
        CARTYPE_CODEFORMAT.put("K29", "校车");
        CARTYPE_CODEFORMAT.put("K30", "小型客车");
        CARTYPE_CODEFORMAT.put("K31", "轻客");
        CARTYPE_CODEFORMAT.put("K32", "越野车");
        CARTYPE_CODEFORMAT.put("K33", "轿车");
        CARTYPE_CODEFORMAT.put("K34", "商务车");
        CARTYPE_CODEFORMAT.put("K39", "面包车");
        CARTYPE_CODEFORMAT.put("H11", "大型货车");
        CARTYPE_CODEFORMAT.put("H21", "中型货车");
        CARTYPE_CODEFORMAT.put("H24", "罐车");
        CARTYPE_CODEFORMAT.put("H31", "小型货车");
        CARTYPE_CODEFORMAT.put("H38", "皮卡");
        CARTYPE_CODEFORMAT.put("Z19", "渣土车");
        CARTYPE_CODEFORMAT.put("Q19", "运输车");
        CARTYPE_CODEFORMAT.put("X99", "其他");
    }

    public final static Map<String, Object> CARTYPE_MATFORCODE = new HashMap<>();
    static {
        CARTYPE_MATFORCODE.put("大型客车", "K11");
        CARTYPE_MATFORCODE.put("公交车", "K19");
        CARTYPE_MATFORCODE.put("中型客车", "K21");
        CARTYPE_MATFORCODE.put("校车", "K29");
        CARTYPE_MATFORCODE.put("小型客车", "K30");
        CARTYPE_MATFORCODE.put("轻客", "K31");
        CARTYPE_MATFORCODE.put("越野车", "K32");
        CARTYPE_MATFORCODE.put("轿车", "K33");
        CARTYPE_MATFORCODE.put("商务车", "K34");
        CARTYPE_MATFORCODE.put("面包车", "K39");
        CARTYPE_MATFORCODE.put("大型货车", "H11");
        CARTYPE_MATFORCODE.put("中型货车", "H21");
        CARTYPE_MATFORCODE.put("罐车", "H24");
        CARTYPE_MATFORCODE.put("小型货车", "H31");
        CARTYPE_MATFORCODE.put("皮卡", "H38");
        CARTYPE_MATFORCODE.put("渣土车", "Z19");
        CARTYPE_MATFORCODE.put("运输车", "Q19");
        CARTYPE_MATFORCODE.put("其他", "X99");
    }
}
