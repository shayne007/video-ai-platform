package com.keensense.admin.mqtt.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * 车辆常量类
 */
public class CarConstant {

    /**
     * 是否打电话
     */
    public final static Map<Integer,String> CALLING = new HashMap<>();
    static {
        CALLING.put(-1,"未知");
        CALLING.put(0,"没打电话");
        CALLING.put(1,"在打电话");
    }

    /**
     * 车前部物品：遮阳板
     */
    public final static Map<Integer,String> SUN_PLATE = new HashMap<>();
    static {
        SUN_PLATE.put(-1,"未知");
        SUN_PLATE.put(0,"无遮阳板");
        SUN_PLATE.put(1,"有遮阳板");
    }

    /**
     * 车前部物品：挂饰
     */
    public final static Map<Integer,String> DROP = new HashMap<>();
    static {
        DROP.put(-1,"未知");
        DROP.put(0,"无挂饰");
        DROP.put(1,"有挂饰");
    }

    /**
     * 车前部物品：纸巾盒
     */
    public final static Map<Integer,String> PAPER = new HashMap<>();
    static {
        PAPER.put(-1,"未知");
        PAPER.put(0,"无纸巾盒");
        PAPER.put(1,"有纸巾盒");
    }

    /**
     * 有无车牌
     */
    public final static Map<Integer,String> PLATE = new HashMap<>();
    static {
        PLATE.put(0,"无车牌");
        PLATE.put(1,"有车牌");
    }

    /**
     * 是否有天窗
     */
    public final static Map<Integer,String> SUNROOF = new HashMap<>();
    static {
        SUNROOF.put(-1,"未知");
        SUNROOF.put(0,"没有天窗");
        SUNROOF.put(1,"有天窗");
    }

    /**
     * 是否有备胎
     */
    public final static Map<Integer,String> SPARETIRE = new HashMap<>();
    static {
        SPARETIRE.put(-1,"未知");
        SPARETIRE.put(0,"无备胎");
        SPARETIRE.put(1,"有备胎");
    }

    /**
     * 是否有行李架
     */
    public final static Map<Integer,String> RACK = new HashMap<>();
    static {
        RACK.put(-1,"未知");
        RACK.put(0,"无行李架");
        RACK.put(1,"有行李架");
    }

    /**
     * 是否为主驾
     */
    public final static Map<Integer,String> MAIA_DRIVER = new HashMap<>();
    static {
        MAIA_DRIVER.put(1,"主驾");
        MAIA_DRIVER.put(0,"副驾");
    }

    /**
     * kafka车辆类型
     */
    public final static Map<String,String> CARTYPE = new HashMap<>();
    static {
        CARTYPE.put("1","轿车");
        CARTYPE.put("2","越野车");
        CARTYPE.put("3","商务车");
        CARTYPE.put("4","小型货车");
        CARTYPE.put("5","中型货车");
        CARTYPE.put("6","大型货车");
        CARTYPE.put("7","轻客");
        CARTYPE.put("8","中型客车");
        CARTYPE.put("9","大型客车");
        CARTYPE.put("10","面包车");
        CARTYPE.put("11","皮卡");
        CARTYPE.put("12","罐车");
        CARTYPE.put("13","渣土车");
        CARTYPE.put("99","其他（专用车）");
        CARTYPE.put("-1","未知");
    }

    /**
     * 1400车辆类型
     */
    public final static Map<String,String> CARCLASS = new HashMap<>();
    static {
        CARCLASS.put("1","K31");
        CARCLASS.put("2","K32");
        CARCLASS.put("3","K17");
        CARCLASS.put("4","H31");
        CARCLASS.put("5","H21");
        CARCLASS.put("6","H11");
        CARCLASS.put("7","K41");
        CARCLASS.put("8","K21");
        CARCLASS.put("9","K11");
        CARCLASS.put("10","K27");
        CARCLASS.put("11","H38");
        CARCLASS.put("12","H24");
        CARCLASS.put("13","Z21");
        CARCLASS.put("99","X99");
    }

    /**
     * 1400车辆品牌
     */
    public final static Map<String,String> CARBRAND = new HashMap<>();
    static {
        CARBRAND.put("大众","1");
        CARBRAND.put("别克","2");
        CARBRAND.put("宝马","3");
        CARBRAND.put("本田","4");
        CARBRAND.put("标致","5");
        CARBRAND.put("丰田","6");
        CARBRAND.put("福特","7");
        CARBRAND.put("日产","8");
        CARBRAND.put("奥迪","9");
        CARBRAND.put("马自达","10");
        CARBRAND.put("雪佛兰","11");
        CARBRAND.put("雪铁龙","12");
        CARBRAND.put("现代","13");
        CARBRAND.put("奇瑞","14");
        CARBRAND.put("起亚","15");
        CARBRAND.put("荣威","16");
        CARBRAND.put("三菱","17");
        CARBRAND.put("斯柯达","18");
        CARBRAND.put("吉利","19");
        CARBRAND.put("中华","20");
        CARBRAND.put("沃尔沃","21");
        CARBRAND.put("雷克萨斯","22");
        CARBRAND.put("菲亚特","23");
        CARBRAND.put("吉利帝豪","24");
        CARBRAND.put("东风","25");
        CARBRAND.put("比亚迪","26");
        CARBRAND.put("铃木","27");
        CARBRAND.put("金杯","28");
        CARBRAND.put("海马","29");
        CARBRAND.put("五菱","30");
        CARBRAND.put("江淮","31");
        CARBRAND.put("斯巴鲁","32");
        CARBRAND.put("英伦","33");
        CARBRAND.put("长城","34");
        CARBRAND.put("哈飞","35");
        CARBRAND.put("庆铃（五十铃）","36");
        CARBRAND.put("东南","37");
        CARBRAND.put("长安","38");
        CARBRAND.put("福田","39");
        CARBRAND.put("夏利","40");
        CARBRAND.put("奔驰","41");
        CARBRAND.put("一汽","42");
        CARBRAND.put("依维柯","43");
        CARBRAND.put("力帆","44");
        CARBRAND.put("一汽奔腾","45");
        CARBRAND.put("皇冠","46");
        CARBRAND.put("雷诺","47");
        CARBRAND.put("JMC","48");
        CARBRAND.put("MG 名爵","49");
        CARBRAND.put("凯马","50");
        CARBRAND.put("众泰","51");
        CARBRAND.put("昌河","52");
        CARBRAND.put("厦门金龙","53");
        CARBRAND.put("上海汇众","54");
        CARBRAND.put("苏州金龙","55");
        CARBRAND.put("海格","56");
        CARBRAND.put("宇通","57");
        CARBRAND.put("中国重汽","58");
        CARBRAND.put("北奔重卡","59");
        CARBRAND.put("华菱星马汽车","60");
        CARBRAND.put("跃进汽车","61");
        CARBRAND.put("黄海汽车","62");
        CARBRAND.put("保时捷","65");
        CARBRAND.put("凯迪拉克","66");
        CARBRAND.put("英菲尼迪","67");
        CARBRAND.put("吉利全球鹰","68");
        CARBRAND.put("吉普","69");
        CARBRAND.put("路虎","70");
        CARBRAND.put("长丰猎豹","71");
        CARBRAND.put("时代汽车","73");
        CARBRAND.put("长安轿车","75");
        CARBRAND.put("陕汽重卡","76");
        CARBRAND.put("安凯","81");
        CARBRAND.put("申龙","82");
        CARBRAND.put("大宇","83");
        CARBRAND.put("中通","86");
        CARBRAND.put("宝骏","87");
        CARBRAND.put("北汽威旺","88");
        CARBRAND.put("广汽传祺","89");
        CARBRAND.put("陆风","90");
        CARBRAND.put("北京","92");
        CARBRAND.put("威麟","94");
        CARBRAND.put("欧宝","95");
        CARBRAND.put("开瑞","96");
        CARBRAND.put("华普","97");
        CARBRAND.put("讴歌","103");
        CARBRAND.put("启辰","104");
        CARBRAND.put("北汽制造","107");
        CARBRAND.put("纳智捷","108");
        CARBRAND.put("野马","109");
        CARBRAND.put("中兴","110");
        CARBRAND.put("克莱斯勒","112");
        CARBRAND.put("广汽吉奥","113");
        CARBRAND.put("瑞麟","115");
        CARBRAND.put("捷豹","117");
        CARBRAND.put("唐骏欧铃","119");
        CARBRAND.put("福迪","121");
        CARBRAND.put("莲花","122");
        CARBRAND.put("双环","124");
        CARBRAND.put("永源","128");
        CARBRAND.put("江南","136");
        CARBRAND.put("道奇","144");
        CARBRAND.put("大运汽车","155");
        CARBRAND.put("北方客车","167");
        CARBRAND.put("九龙","176");
        CARBRAND.put("宾利","191");
        CARBRAND.put("舒驰客车","201");
        CARBRAND.put("红旗","230");
    }


}


