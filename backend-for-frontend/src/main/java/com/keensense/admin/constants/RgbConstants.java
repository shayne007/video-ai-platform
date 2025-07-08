package com.keensense.admin.constants;

import java.util.HashMap;
import java.util.Map;

public class RgbConstants {

	public final static Map<String,Object> COATCOLOR_RGBFORMAT = new HashMap<String,Object>();
	static {
		COATCOLOR_RGBFORMAT.put("5263440",  "黑（深灰）");
		COATCOLOR_RGBFORMAT.put("0",  "黑（深灰）");
		COATCOLOR_RGBFORMAT.put("11842740",  "灰");
		COATCOLOR_RGBFORMAT.put("343174",  "棕（卡其）");
		COATCOLOR_RGBFORMAT.put("16724484",  "蓝");
		COATCOLOR_RGBFORMAT.put("8327170",  "蓝");
		COATCOLOR_RGBFORMAT.put("16743167",  "红");
		COATCOLOR_RGBFORMAT.put("9983",  "红");
		COATCOLOR_RGBFORMAT.put("12423793",  "蓝");
		COATCOLOR_RGBFORMAT.put("15311656",  "蓝");
		COATCOLOR_RGBFORMAT.put("16777215",  "白");
		COATCOLOR_RGBFORMAT.put("5287936",  "绿（青）");
		COATCOLOR_RGBFORMAT.put("65535",  "黄");
		COATCOLOR_RGBFORMAT.put("8761028",  "棕（卡其）");
		COATCOLOR_RGBFORMAT.put("9576596",  "紫");
		COATCOLOR_RGBFORMAT.put("16776448",  "绿（青）");
		COATCOLOR_RGBFORMAT.put("37887",  "黄");
		COATCOLOR_RGBFORMAT.put("2111058",  "棕（卡其）");
	}

	public final static Map<String,Object> MAINCOLOR_RGBFORMAT = new HashMap<String,Object>();
	static {
		MAINCOLOR_RGBFORMAT.put("16777215",  "白");
		MAINCOLOR_RGBFORMAT.put("5263440",  "黑（深灰）");
		MAINCOLOR_RGBFORMAT.put("11842740",  "灰");
		MAINCOLOR_RGBFORMAT.put("65535",  "黄");
		MAINCOLOR_RGBFORMAT.put("16743167",  "红");
		MAINCOLOR_RGBFORMAT.put("9983",  "红");
		MAINCOLOR_RGBFORMAT.put("9576596",  "紫");
		MAINCOLOR_RGBFORMAT.put("8761028",  "棕（卡其）");
		MAINCOLOR_RGBFORMAT.put("5287936",  "绿（青）");
		MAINCOLOR_RGBFORMAT.put("8327170",  "蓝");
		MAINCOLOR_RGBFORMAT.put("16724484",  "蓝");
		MAINCOLOR_RGBFORMAT.put("12423793",  "蓝");
		MAINCOLOR_RGBFORMAT.put("15311656",  "蓝");
		MAINCOLOR_RGBFORMAT.put("16776448",  "绿（青）");
		MAINCOLOR_RGBFORMAT.put("343174",  "棕（卡其）");
		MAINCOLOR_RGBFORMAT.put("2111058",  "棕（卡其）");
		MAINCOLOR_RGBFORMAT.put("0",  "黑（深灰）");
		MAINCOLOR_RGBFORMAT.put("11711154",  "灰");
		MAINCOLOR_RGBFORMAT.put("37887",  "黑");
	}

	public final static Map<String,Object> PLATECOLOR_CODEFORMAT = new HashMap<String,Object>();
	static {
		PLATECOLOR_CODEFORMAT.put("黄",  "6");
		PLATECOLOR_CODEFORMAT.put("蓝",  "5");
		PLATECOLOR_CODEFORMAT.put("黑",  "1");
		PLATECOLOR_CODEFORMAT.put("白",  "2");
		PLATECOLOR_CODEFORMAT.put("绿",  "9");
		PLATECOLOR_CODEFORMAT.put("黄绿",  "100");
	}

	public final static Map<String,String> PLATECLASS_CODEFORMAT = new HashMap<String,String>();
	static {
		PLATECLASS_CODEFORMAT.put("01",  "大型汽车号牌");
		PLATECLASS_CODEFORMAT.put("02",  "小型汽车号牌");
		PLATECLASS_CODEFORMAT.put("03",  "使馆汽车号牌");
		PLATECLASS_CODEFORMAT.put("04",  "领馆汽车号牌");
		PLATECLASS_CODEFORMAT.put("16",  "教练汽车号牌");
		PLATECLASS_CODEFORMAT.put("23",  "警用汽车号牌");
		PLATECLASS_CODEFORMAT.put("25",  "原农机号牌");
		PLATECLASS_CODEFORMAT.put("26",  "香港入出境号牌");
		PLATECLASS_CODEFORMAT.put("28",  "港澳号牌");
		PLATECLASS_CODEFORMAT.put("31",  "武警号牌");
		PLATECLASS_CODEFORMAT.put("32",  "军队号牌");
		PLATECLASS_CODEFORMAT.put("97",  "新能源大车号牌");
		PLATECLASS_CODEFORMAT.put("98",  "新能源小车号牌");
		PLATECLASS_CODEFORMAT.put("99",  "其他号牌");
	}
	
	public final static Map<String,Object> PLATETYPE_CODEFORMAT = new HashMap<String,Object>();
	static {
		PLATETYPE_CODEFORMAT.put("1",  "普通蓝牌");
		PLATETYPE_CODEFORMAT.put("2",  "普通黑牌");
		PLATETYPE_CODEFORMAT.put("3",  "普通黄牌");
		PLATETYPE_CODEFORMAT.put("4",  "警车车牌");
		PLATETYPE_CODEFORMAT.put("5",  "武警车牌");
		PLATETYPE_CODEFORMAT.put("6",  "军队车牌");
		PLATETYPE_CODEFORMAT.put("7",  "使馆车牌");
		PLATETYPE_CODEFORMAT.put("8",  "港澳车牌");
		PLATETYPE_CODEFORMAT.put("9",  "农用车牌（农用绿牌，农用黄牌）");
		PLATETYPE_CODEFORMAT.put("10",  "驾校车牌");
		PLATETYPE_CODEFORMAT.put("99",  "其他车牌");
		PLATETYPE_CODEFORMAT.put("-1",  "未知");
	}
	
	public final static Map<String,Object> CARTYPE_CODEFORMAT = new HashMap<String,Object>();
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
		CARTYPE_CODEFORMAT.put("13",  "渣土车");//渣土车、罐车、公交车、校车
		CARTYPE_CODEFORMAT.put("14",  "公交车");
		CARTYPE_CODEFORMAT.put("15",  "校车");
		CARTYPE_CODEFORMAT.put("99",  "其他（专用车）");
		CARTYPE_CODEFORMAT.put("-1",  "未知");
		
	}
	
	public final static Map<String,Object> CARCOLOR_CODEFORMAT = new HashMap<String,Object>();
	static {
		CARCOLOR_CODEFORMAT.put("黑",  "1");
		CARCOLOR_CODEFORMAT.put("蓝",  "5");
		CARCOLOR_CODEFORMAT.put("棕",  "8");
		CARCOLOR_CODEFORMAT.put("绿",  "9");
		CARCOLOR_CODEFORMAT.put("灰",  "3");
		CARCOLOR_CODEFORMAT.put("橙",  "7");
		CARCOLOR_CODEFORMAT.put("粉",  "12");
		CARCOLOR_CODEFORMAT.put("紫",  "10");
		CARCOLOR_CODEFORMAT.put("红",  "4");
		CARCOLOR_CODEFORMAT.put("白",  "2");
		CARCOLOR_CODEFORMAT.put("黄",  "6");
	}
	
	public final static Map<String,String> COAT_STYLE_MAP = new HashMap<String,String>();
	static {
		COAT_STYLE_MAP.put("0", "未知");
		COAT_STYLE_MAP.put("1", "长袖");
		COAT_STYLE_MAP.put("2", "短袖");
	}
	
	public final static Map<String,String> TROUSERS_STYLE_MAP = new HashMap<String,String>();
	static {
		TROUSERS_STYLE_MAP.put("0", "未知");
		TROUSERS_STYLE_MAP.put("1", "长裤");
		TROUSERS_STYLE_MAP.put("2", "短裤");
		TROUSERS_STYLE_MAP.put("3", "裙子");
	}

	public final static Map<String,String> COLOR_CODEFORMAT = new HashMap<String,String>();
	static {
		COLOR_CODEFORMAT.put("1", "黑");
		COLOR_CODEFORMAT.put("2", "白");
		COLOR_CODEFORMAT.put("3", "灰");
		COLOR_CODEFORMAT.put("4", "红");
		COLOR_CODEFORMAT.put("5", "蓝");
		COLOR_CODEFORMAT.put("6", "黄");
		COLOR_CODEFORMAT.put("7", "橙");
		COLOR_CODEFORMAT.put("8", "棕");
		COLOR_CODEFORMAT.put("9", "绿");
		COLOR_CODEFORMAT.put("10","紫");
		COLOR_CODEFORMAT.put("11","青");
		COLOR_CODEFORMAT.put("12","粉");
		COLOR_CODEFORMAT.put("13","透明");
		COLOR_CODEFORMAT.put("14","银");
		COLOR_CODEFORMAT.put("15","金");
		COLOR_CODEFORMAT.put("99","其他");
		COLOR_CODEFORMAT.put("100","黄绿");
		COLOR_CODEFORMAT.put("101","渐变绿");
		COLOR_CODEFORMAT.put("-1","未知");
	}

	public final static Map<String, String> CARDIRECTION_CODEFORMAT = new HashMap<String, String>();
	static {
		CARDIRECTION_CODEFORMAT.put("128", "正面");
		CARDIRECTION_CODEFORMAT.put("256", "侧面");
		CARDIRECTION_CODEFORMAT.put("512", "背面");
		CARDIRECTION_CODEFORMAT.put("1", "正面");
		CARDIRECTION_CODEFORMAT.put("2", "侧面");
		CARDIRECTION_CODEFORMAT.put("3", "背面");
		CARDIRECTION_CODEFORMAT.put("9", "其他");
	}

	public final static Map<String,String> HAIRSTYLE_CODEFORMAT = new HashMap<String,String>();
    static {
        /*HAIRSTYLE_CODEFORMAT.put("1", "平头");
        HAIRSTYLE_CODEFORMAT.put("2", "中分");
        HAIRSTYLE_CODEFORMAT.put("3", "偏分");
        HAIRSTYLE_CODEFORMAT.put("4", "额秃");
        HAIRSTYLE_CODEFORMAT.put("5", "项秃");
        HAIRSTYLE_CODEFORMAT.put("6", "全秃");
        HAIRSTYLE_CODEFORMAT.put("7", "卷发");
        HAIRSTYLE_CODEFORMAT.put("8", "波浪发");
        HAIRSTYLE_CODEFORMAT.put("9", "麻花辫");
        HAIRSTYLE_CODEFORMAT.put("10","盘发");
        HAIRSTYLE_CODEFORMAT.put("11","披肩");
        HAIRSTYLE_CODEFORMAT.put("99","其他");*/
		HAIRSTYLE_CODEFORMAT.put("97","长发");
		HAIRSTYLE_CODEFORMAT.put("98","短发");
    }

    public final static Map<String, String> CARBRAND_CODEFROMAT = new HashMap<String, String>();
    static {
		CARBRAND_CODEFROMAT.put("0", "其他");
		CARBRAND_CODEFROMAT.put("1", "大众");
		CARBRAND_CODEFROMAT.put("2", "别克");
		CARBRAND_CODEFROMAT.put("3", "宝马");
		CARBRAND_CODEFROMAT.put("4", "本田");
		CARBRAND_CODEFROMAT.put("5", "标致");
		CARBRAND_CODEFROMAT.put("6", "丰田");
		CARBRAND_CODEFROMAT.put("7", "福特");
		CARBRAND_CODEFROMAT.put("8", "日产");
		CARBRAND_CODEFROMAT.put("9", "奥迪");
		CARBRAND_CODEFROMAT.put("10", "马自达");
		CARBRAND_CODEFROMAT.put("11", "雪佛兰");
		CARBRAND_CODEFROMAT.put("12", "雪铁龙");
		CARBRAND_CODEFROMAT.put("13", "现代");
		CARBRAND_CODEFROMAT.put("14", "奇瑞");
		CARBRAND_CODEFROMAT.put("15", "起亚");
		CARBRAND_CODEFROMAT.put("16", "荣威");
		CARBRAND_CODEFROMAT.put("17", "三菱");
		CARBRAND_CODEFROMAT.put("18", "斯柯达");
		CARBRAND_CODEFROMAT.put("19", "吉利");
		CARBRAND_CODEFROMAT.put("20", "中华");
		CARBRAND_CODEFROMAT.put("21", "沃尔沃");
		CARBRAND_CODEFROMAT.put("22", "雷克萨斯");
		CARBRAND_CODEFROMAT.put("23", "菲亚特");
		CARBRAND_CODEFROMAT.put("24", "吉利帝豪");
		CARBRAND_CODEFROMAT.put("25", "东风");
		CARBRAND_CODEFROMAT.put("26", "比亚迪");
		CARBRAND_CODEFROMAT.put("27", "铃木");
		CARBRAND_CODEFROMAT.put("28", "金杯");
		CARBRAND_CODEFROMAT.put("29", "海马");
		CARBRAND_CODEFROMAT.put("30", "五菱");
		CARBRAND_CODEFROMAT.put("31", "江淮");
		CARBRAND_CODEFROMAT.put("32", "斯巴鲁");
		CARBRAND_CODEFROMAT.put("33", "英伦");
		CARBRAND_CODEFROMAT.put("34", "长城");
		CARBRAND_CODEFROMAT.put("35", "哈飞");
		CARBRAND_CODEFROMAT.put("36", "庆铃（五十铃）");
		CARBRAND_CODEFROMAT.put("37", "东南");
		CARBRAND_CODEFROMAT.put("38", "长安");
		CARBRAND_CODEFROMAT.put("39", "福田");
		CARBRAND_CODEFROMAT.put("40", "夏利");
		CARBRAND_CODEFROMAT.put("41", "奔驰");
		CARBRAND_CODEFROMAT.put("42", "一汽");
		CARBRAND_CODEFROMAT.put("43", "依维柯");
		CARBRAND_CODEFROMAT.put("44", "力帆");
		CARBRAND_CODEFROMAT.put("45", "一汽奔腾");
		CARBRAND_CODEFROMAT.put("46", "皇冠");
		CARBRAND_CODEFROMAT.put("47", "雷诺");
		CARBRAND_CODEFROMAT.put("48", "JMC");
		CARBRAND_CODEFROMAT.put("49", "MG名爵");
		CARBRAND_CODEFROMAT.put("50", "凯马");
		CARBRAND_CODEFROMAT.put("51", "众泰");
		CARBRAND_CODEFROMAT.put("52", "昌河");
		CARBRAND_CODEFROMAT.put("53", "厦门金龙");
		CARBRAND_CODEFROMAT.put("54", "上海汇众");
		CARBRAND_CODEFROMAT.put("55", "苏州金龙");
		CARBRAND_CODEFROMAT.put("56", "海格");
		CARBRAND_CODEFROMAT.put("57", "宇通");
		CARBRAND_CODEFROMAT.put("58", "中国重汽");
		CARBRAND_CODEFROMAT.put("59", "北奔重卡");
		CARBRAND_CODEFROMAT.put("60", "华菱星马汽车");
		CARBRAND_CODEFROMAT.put("61", "跃进汽车");
		CARBRAND_CODEFROMAT.put("62", "黄海汽车");
		CARBRAND_CODEFROMAT.put("65", "保时捷");
		CARBRAND_CODEFROMAT.put("66", "凯迪拉克");
		CARBRAND_CODEFROMAT.put("67", "英菲尼迪");
		CARBRAND_CODEFROMAT.put("68", "吉利全球鹰");
		CARBRAND_CODEFROMAT.put("69", "吉普");
		CARBRAND_CODEFROMAT.put("70", "路虎");
		CARBRAND_CODEFROMAT.put("71", "长丰猎豹");
		CARBRAND_CODEFROMAT.put("73", "时代汽车");
		CARBRAND_CODEFROMAT.put("75", "长安轿车");
		CARBRAND_CODEFROMAT.put("76", "陕汽重卡");
		CARBRAND_CODEFROMAT.put("81", "安凯");
		CARBRAND_CODEFROMAT.put("82", "申龙");
		CARBRAND_CODEFROMAT.put("83", "大宇");
		CARBRAND_CODEFROMAT.put("86", "中通");
		CARBRAND_CODEFROMAT.put("87", "宝骏");
		CARBRAND_CODEFROMAT.put("88", "北汽威旺");
		CARBRAND_CODEFROMAT.put("89", "广汽传祺");
		CARBRAND_CODEFROMAT.put("90", "陆风");
		CARBRAND_CODEFROMAT.put("92", "北京");
		CARBRAND_CODEFROMAT.put("94", "威麟");
		CARBRAND_CODEFROMAT.put("95", "欧宝");
		CARBRAND_CODEFROMAT.put("96", "开瑞");
		CARBRAND_CODEFROMAT.put("97", "华普");
		CARBRAND_CODEFROMAT.put("103", "讴歌");
		CARBRAND_CODEFROMAT.put("104", "启辰");
		CARBRAND_CODEFROMAT.put("107", "北汽制造");
		CARBRAND_CODEFROMAT.put("108", "纳智捷");
		CARBRAND_CODEFROMAT.put("109", "野马");
		CARBRAND_CODEFROMAT.put("110", "中兴");
		CARBRAND_CODEFROMAT.put("112", "克莱斯勒");
		CARBRAND_CODEFROMAT.put("113", "广汽吉奥");
		CARBRAND_CODEFROMAT.put("115", "瑞麟");
		CARBRAND_CODEFROMAT.put("117", "捷豹");
		CARBRAND_CODEFROMAT.put("119", "唐骏欧铃");
		CARBRAND_CODEFROMAT.put("121", "福迪");
		CARBRAND_CODEFROMAT.put("122", "莲花");
		CARBRAND_CODEFROMAT.put("124", "双环");
		CARBRAND_CODEFROMAT.put("128", "永源");
		CARBRAND_CODEFROMAT.put("136", "江南");
		CARBRAND_CODEFROMAT.put("144", "道奇");
		CARBRAND_CODEFROMAT.put("155", "大运汽车");
		CARBRAND_CODEFROMAT.put("167", "北方客车");
		CARBRAND_CODEFROMAT.put("176", "九龙");
		CARBRAND_CODEFROMAT.put("191", "宾利");
		CARBRAND_CODEFROMAT.put("201", "舒驰客车");
		CARBRAND_CODEFROMAT.put("230", "红旗");
	}
}
