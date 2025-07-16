package com.keensense.search.constant;

import java.util.HashMap;
import java.util.Map;

public class RgbConstants {
    private static final String DARK_DEEP_GRAY = "黑（深灰）";
    private static final String BROWN = "棕（卡其）";

    private RgbConstants(){

    }

    protected static final Map<String,Object> COATCOLOR_RGBFORMAT = new HashMap();
    static {
        COATCOLOR_RGBFORMAT.put("5263440",  DARK_DEEP_GRAY);
        COATCOLOR_RGBFORMAT.put("0",  DARK_DEEP_GRAY);
        COATCOLOR_RGBFORMAT.put("11842740",  "灰");
        COATCOLOR_RGBFORMAT.put("343174",  BROWN);
        COATCOLOR_RGBFORMAT.put("16724484",  "蓝");
        COATCOLOR_RGBFORMAT.put("8327170",  "蓝");
        COATCOLOR_RGBFORMAT.put("16743167",  "红");
        COATCOLOR_RGBFORMAT.put("9983",  "红");
        COATCOLOR_RGBFORMAT.put("12423793",  "蓝");
        COATCOLOR_RGBFORMAT.put("15311656",  "蓝");
        COATCOLOR_RGBFORMAT.put("16777215",  "白");
        COATCOLOR_RGBFORMAT.put("5287936",  "绿（青）");
        COATCOLOR_RGBFORMAT.put("65535",  "黄");
        COATCOLOR_RGBFORMAT.put("8761028",  BROWN);
        COATCOLOR_RGBFORMAT.put("9576596",  "紫");
        COATCOLOR_RGBFORMAT.put("16776448",  "绿（青）");
        COATCOLOR_RGBFORMAT.put("37887",  "黄");
        COATCOLOR_RGBFORMAT.put("2111058",  BROWN);
    }

    protected static final Map<String,Object> MAINCOLOR_RGBFORMAT = new HashMap();
    static {
        MAINCOLOR_RGBFORMAT.put("16777215",  "白");
        MAINCOLOR_RGBFORMAT.put("5263440",  DARK_DEEP_GRAY);
        MAINCOLOR_RGBFORMAT.put("11842740",  "灰");
        MAINCOLOR_RGBFORMAT.put("65535",  "黄");
        MAINCOLOR_RGBFORMAT.put("16743167",  "红");
        MAINCOLOR_RGBFORMAT.put("9983",  "红");
        MAINCOLOR_RGBFORMAT.put("9576596",  "紫");
        MAINCOLOR_RGBFORMAT.put("8761028",  BROWN);
        MAINCOLOR_RGBFORMAT.put("5287936",  "绿（青）");
        MAINCOLOR_RGBFORMAT.put("8327170",  "蓝");
        MAINCOLOR_RGBFORMAT.put("16724484",  "蓝");
        MAINCOLOR_RGBFORMAT.put("12423793",  "蓝");
        MAINCOLOR_RGBFORMAT.put("15311656",  "蓝");
        MAINCOLOR_RGBFORMAT.put("16776448",  "绿（青）");
        MAINCOLOR_RGBFORMAT.put("343174",  BROWN);
        MAINCOLOR_RGBFORMAT.put("2111058",  BROWN);
        MAINCOLOR_RGBFORMAT.put("0",  DARK_DEEP_GRAY);
        MAINCOLOR_RGBFORMAT.put("11711154",  "灰");
        MAINCOLOR_RGBFORMAT.put("37887",  "黑");
    }

    protected static final Map<String,Object> PLATECOLOR_CODEFORMAT = new HashMap();
    static {
        PLATECOLOR_CODEFORMAT.put("黄",  "1");
        PLATECOLOR_CODEFORMAT.put("蓝",  "2");
        PLATECOLOR_CODEFORMAT.put("黑",  "3");
        PLATECOLOR_CODEFORMAT.put("白",  "4");
        PLATECOLOR_CODEFORMAT.put("绿",  "5");
        PLATECOLOR_CODEFORMAT.put("黄绿",  "6");
    }

    protected static final Map<String,Object> PLATECLASS_CODEFORMAT = new HashMap();
    static {
        PLATECLASS_CODEFORMAT.put("1",  "普通蓝牌");
        PLATECLASS_CODEFORMAT.put("2",  "普通黑牌");
        PLATECLASS_CODEFORMAT.put("3",  "普通黄牌");
        PLATECLASS_CODEFORMAT.put("4",  "警车车牌");
        PLATECLASS_CODEFORMAT.put("5",  "武警车牌");
        PLATECLASS_CODEFORMAT.put("6",  "军队车牌");
        PLATECLASS_CODEFORMAT.put("7",  "使馆车牌");
        PLATECLASS_CODEFORMAT.put("8",  "港澳车牌");
        PLATECLASS_CODEFORMAT.put("9",  "农用车牌（农用绿牌，农用黄牌）");
        PLATECLASS_CODEFORMAT.put("10",  "驾校车牌");
        PLATECLASS_CODEFORMAT.put("99",  "其他车牌");
        PLATECLASS_CODEFORMAT.put("-1",  "未知");
    }

    protected static final Map<String,Object> CARTYPE_CODEFORMAT = new HashMap();
    static {
        CARTYPE_CODEFORMAT.put("1",  "轿车");
        CARTYPE_CODEFORMAT.put("2",  "越野车");
        CARTYPE_CODEFORMAT.put("3",  "商务车");
        CARTYPE_CODEFORMAT.put("4",  "小型货车");
        CARTYPE_CODEFORMAT.put("5",  "中型货车");
        CARTYPE_CODEFORMAT.put("6",  "大型货车");
        CARTYPE_CODEFORMAT.put("7",  "轻客");
        CARTYPE_CODEFORMAT.put("8",  "中型客车");
        CARTYPE_CODEFORMAT.put("9",  "大型客车");
        CARTYPE_CODEFORMAT.put("10",  "面包车");
        CARTYPE_CODEFORMAT.put("11",  "皮卡");
        CARTYPE_CODEFORMAT.put("12",  "罐车");
        CARTYPE_CODEFORMAT.put("13",  "渣土车");
        CARTYPE_CODEFORMAT.put("99",  "其他（专用车）");
        CARTYPE_CODEFORMAT.put("-1",  "未知");

    }

    protected static final Map<String,Object> CARCOLOR_CODEFORMAT = new HashMap();
    static {
        CARCOLOR_CODEFORMAT.put("黑",  "1");
        CARCOLOR_CODEFORMAT.put("蓝",  "2");
        CARCOLOR_CODEFORMAT.put("棕",  "3");
        CARCOLOR_CODEFORMAT.put("绿",  "4");
        CARCOLOR_CODEFORMAT.put("灰",  "5");
        CARCOLOR_CODEFORMAT.put("橙",  "6");
        CARCOLOR_CODEFORMAT.put("粉",  "7");
        CARCOLOR_CODEFORMAT.put("紫",  "8");
        CARCOLOR_CODEFORMAT.put("红",  "9");
        CARCOLOR_CODEFORMAT.put("银",  "10");
        CARCOLOR_CODEFORMAT.put("白",  "11");
        CARCOLOR_CODEFORMAT.put("黄",  "12");
        CARCOLOR_CODEFORMAT.put("金",  "13");
    }

    protected static final Map<String,String> CARCOLOR_VALUEFORMAT = new HashMap();
    static {
        CARCOLOR_CODEFORMAT.put("1","黑");
        CARCOLOR_CODEFORMAT.put("2","蓝");
        CARCOLOR_CODEFORMAT.put("3","棕");
        CARCOLOR_CODEFORMAT.put("4","绿");
        CARCOLOR_CODEFORMAT.put("5","灰");
        CARCOLOR_CODEFORMAT.put("6","橙");
        CARCOLOR_CODEFORMAT.put("7","粉");
        CARCOLOR_CODEFORMAT.put("8","紫");
        CARCOLOR_CODEFORMAT.put("9","红");
        CARCOLOR_CODEFORMAT.put("10","银");
        CARCOLOR_CODEFORMAT.put("11","白");
        CARCOLOR_CODEFORMAT.put("12","黄");
        CARCOLOR_CODEFORMAT.put("13","金");
    }

    protected static final Map<String,String> COAT_STYLE_MAP = new HashMap();
    static {
        COAT_STYLE_MAP.put("0", "未知");
        COAT_STYLE_MAP.put("1", "长袖");
        COAT_STYLE_MAP.put("2", "短袖");
    }

    protected static final Map<String,String> TROUSERS_STYLE_MAP = new HashMap();
    static {
        TROUSERS_STYLE_MAP.put("0", "未知");
        TROUSERS_STYLE_MAP.put("1", "长裤");
        TROUSERS_STYLE_MAP.put("2", "短裤");
        TROUSERS_STYLE_MAP.put("3", "裙子");
    }

}
