package com.keensense.picturestream.util;

/**
 * Created by memory_fu on 2019/7/4.
 */
public class ClassType {

    private ClassType(){}
    
    public static String coatStyle(String code) {
        String result = "";
        switch (code) {
            case "1":
                return "长袖";
            case "2":
                return "短袖";
            case "-1":
                return "未知";
            default:
                break;
        }
        
        return result;
    }
    
    public static String trousersStyle(String code) {
        String result = "";
        switch (code) {
            case "1":
                return "长裤";
            case "2":
                return "短裤";
            case "3":
                return "裙子";
            case "-1":
                return "未知";
            default:
                break;
        }
        return result;
    }

    /***
     * @description: 转换上衣样式Code获取国标编码
     * @param coatStyleCode 上衣样式Code 长袖-98，短袖-97
     * @return: java.lang.String
     */
    public static String getCoatStyleCode(String coatStyleCode) {
        switch (coatStyleCode) {
            case "1":
                return "98";
            case "2":
                return "97";
            default:
                return "99";
        }
    }

    /***
     * @description: 转换下衣样式Code获取国标编码
     * @param trousersLength 下衣样式Code （长裤-98，短裤-97，裙子-96）
     * @return: java.lang.String
     */
    public static String getTrousersStyleCode(String trousersLength) {
        switch (trousersLength) {
            case "1":
                return "98";
            case "2":
                return "97";
            case "3":
                return "96";
            default:
                return "99";
        }
    }

    /***
     * @description: 转换头发样式Code获取国标编码
     * @param hairStyleCode 头发样式Code 长发-98，短发-97
     * @return: java.lang.String
     */
    public static String getHairStyleCode(String hairStyleCode) {
        switch (hairStyleCode) {
            case "1":
                return "97";
            case "2":
                return "98";
            default:
                return "99";
        }
    }
    
    public static String getColorKeyByCode(String key) {
        String result;
        switch (key) {
            case "5263440":
                result = "1";
                break;
            case "0":
                result = "1";
                break;
            case "11842740":
                result = "2";
                break;
            case "343174":
                result = "3";
                break;
            case "16724484":
                result = "4";
                break;
            case "8327170":
                result = "4";
                break;
            case "16743167":
                result = "5";
                break;
            case "9983":
                result = "5";
                break;
            case "12423793":
                result = "4";
                break;
            case "15311656":
                result = "4";
                break;
            case "16777215":
                result = "6";
                break;
            case "5287936":
                result = "7";
                break;
            case "65535":
                result = "8";
                break;
            case "8761028":
                result = "3";
                break;
            case "9576596":
                result = "9";
                break;
            case "16776448":
                result = "7";
                break;
            case "37887":
                result = "8";
                break;
            case "2111058":
                result = "3";
                break;
            case "11711154":
                result = "2";
                break;
            default:
                result = "-1";
                break;
        }
        return result;
    }
    
    public static String getColorNameByCode(String key) {
        String result;
        String brown = "棕(卡其)";
        switch (key) {
            case "5263440":
                result = "黑(深灰)";
                break;
            case "0":
                result = "黑(深灰)";
                break;
            case "11842740":
                result = "灰";
                break;
            case "343174":
                result = brown;
                break;
            case "16724484":
                result = "蓝";
                break;
            case "8327170":
                result = "蓝";
                break;
            case "16743167":
                result = "红";
                break;
            case "9983":
                result = "红";
                break;
            case "12423793":
                result = "蓝";
                break;
            case "15311656":
                result = "蓝";
                break;
            case "16777215":
                result = "白";
                break;
            case "5287936":
                result = "绿(青)";
                break;
            case "65535":
                result = "黄";
                break;
            case "8761028":
                result = brown;
                break;
            case "9576596":
                result = "紫";
                break;
            case "16776448":
                result = "绿(青)";
                break;
            case "37887":
                result = "黄";
                break;
            case "2111058":
                result = brown;
                break;
            case "11711154":
                result = "灰";
                break;
            default:
                result = "";
                break;
        }
        return result;
    }
    
    
    /**
     * gstl车辆类型转换成我们自己的类型
     * @param type gstl车牌类型
     */
    public static Integer getVehicleTypeByGstl(Integer type) {
        switch (type) {
            case 0:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 11:
                return 4;
            case 10:
                return 4;
            case 13:
                return 6;
            case 12:
                return 6;
            case 14:
                return 6;
            case 15:
                return 6;
            case 5:
                return 7;
            case 6:
                return 8;
            case 7:
                return 9;
            case 8:
                return 9;
            case 9:
                return 9;
            case 1:
                return 10;
            case 2:
                return 11;
            case 16:
                return 99;
            case 17:
                return 99;
            case 18:
                return 99;
            case 19:
                return 99;
            default:
                return -1;
        }
    }
    
    public static String getVehicleNameByType(Integer type) {
        switch (type) {
            case 1:
                return "轿车";
            case 2:
                return "越野车";
            case 3:
                return "商务车";
            case 4:
                return "小型货车";
            case 5:
                return "中型货车";
            case 6:
                return "大型货车";
            case 7:
                return "轻客";
            case 8:
                return "中型客车";
            case 9:
                return "大型客车";
            case 10:
                return "面包车";
            case 11:
                return "皮卡";
            case 12:
                return "其他（专用车）";
            default:
                return "未知";
        }
    }
    
    /**
     * gstl车牌类型转换成我们自己的类型
     *
     * @param type gstl车牌类型
     */
    public static Integer getPlateTypeByGstl(Integer type) {
        switch (type) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            case 8:
                return 6;
            case 9:
                return 6;
            case 10:
                return 7;
            case 11:
                return 8;
            case 12:
                return 9;
            case 4:
                return 9;
            case 1000:
                return 10;
            case 13:
                return 11;
            case 14:
                return 12;
            case 7:
                return 99;
            default:
                return -1;
        }
    }
    
    public static String getPlateNameByType(Integer type) {
        switch (type) {
            case 1:
                return "普通蓝牌";
            case 2:
                return "普通黑牌";
            case 3:
                return "普通黄牌";
            case 4:
                return "警车车牌";
            case 5:
                return "武警车牌";
            case 6:
                return "军队车牌";
            case 7:
                return "使馆车牌";
            case 8:
                return "港澳车牌";
            case 9:
                return "农用车牌（农用绿牌，农用黄牌）";
            case 10:
                return "驾校车牌";
            case 11:
                return "小型新能源汽车";
            case 12:
                return "大型新能源汽车";
            case 99:
                return "其他车牌";
            default:
                return "未知";
        }
    }
    
    /**
     * glst车身颜色转视图库颜色
     */
    public static String convertGlstVehicleColor(String colorId) {
        switch (colorId) {
            case "1":
                return "1";
            case "2":
                return "5";
            case "3":
                return "8";
            case "4":
                return "9";
            case "5":
                return "3";
            case "6":
                return "7";
            case "7":
                return "12";
            case "8":
                return "10";
            case "9":
                return "4";
            case "10":
                return "99";
            case "11":
                return "2";
            case "12":
                return "6";
            case "-1":
                return "99";
            default:
                return "99";
        }
    }
    
    /**
     * glst车牌类型转视图库
     */
    public static String convertGlstPlateClass(String plateClassCode) {
        
        switch (plateClassCode) {
            case "1":
                return "99";
            case "2":
                return "99";
            case "3":
                return "99";
            case "4":
                return "23";
            case "5":
                return "31";
            case "6":
                return "32";
            case "7":
                return "04";
            case "8":
                return "99";
            case "9":
                return "25";
            case "10":
                return "16";
            case "99":
                return "99";
            case "-1":
                return "99";
            default:
                return "99";
        }
    }
    
    /**
     * qst车辆类型转成视图库车辆类型
     */
    public static String convertQstVehicleClass(String qstVehicleClass) {
        switch (qstVehicleClass) {
            case "1":
                return "K33";
            case "2":
                return "K32";
            case "3":
                return "K27";
            case "4":
                return "H30";
            case "5":
                return "H20";
            case "6":
                return "H10";
            case "7":
                return "K30";
            case "8":
                return "K20";
            case "9":
                return "K10";
            case "10":
                return "X";
            case "11":
                return "X";
            case "12":
                return "H14";
            case "13":
                return "X";
            case "99":
                return "X";
            default:
                return "X";
        }
    }
    
    
    /**
     * qst车身颜色代码转视图库颜色代码
     */
    public static String convertQstVehicleColorType(String qstColorType) {
        switch (qstColorType) {
            case "1":
                return "1";
            case "2":
                return "5";
            case "3":
                return "8";
            case "4":
                return "9";
            case "5":
                return "3";
            case "6":
                return "7";
            case "7":
                return "12";
            case "8":
                return "10";
            case "9":
                return "4";
            case "10":
                return "99";
            case "11":
                return "2";
            case "12":
                return "6";
            case "-1":
                return "99";
            default:
                return "99";
        }
    }
    
    /**
     * qst车牌颜色代码转视图库颜色代码
     */
    public static String convertQstPlateColorType(String qstColorType) {
        switch (qstColorType) {
            case "1":
                return "6";
            case "2":
                return "5";
            case "3":
                return "1";
            case "4":
                return "2";
            case "5":
                return "9";
            case "6":
                return "99";
            case "-1":
                return "99";
            default:
                return "99";
        }
    }
    
    /**
     * qst车辆品牌转视图库代码
     */
    public static String convertQstVehicleBrand(String qstVehicleBrand) {
        if (null == qstVehicleBrand) {
            return "";
        }
        
        if (qstVehicleBrand.contains("大众")) {
            return "1";
        }
        if (qstVehicleBrand.contains("别克")) {
            return "2";
        }
        if (qstVehicleBrand.contains("宝马")) {
            return "3";
        }
        if (qstVehicleBrand.contains("本田")) {
            return "4";
        }
        if (qstVehicleBrand.contains("标致")) {
            return "5";
        }
        if (qstVehicleBrand.contains("丰田")) {
            return "6";
        }
        if (qstVehicleBrand.contains("福特")) {
            return "7";
        }
        if (qstVehicleBrand.contains("日产")) {
            return "8";
        }
        if (qstVehicleBrand.contains("奥迪")) {
            return "9";
        }
        if (qstVehicleBrand.contains("马自达")) {
            return "10";
        }
        if (qstVehicleBrand.contains("雪佛兰")) {
            return "11";
        }
        if (qstVehicleBrand.contains("雪铁龙")) {
            return "12";
        }
        if (qstVehicleBrand.contains("现代")) {
            return "13";
        }
        if (qstVehicleBrand.contains("奇瑞")) {
            return "14";
        }
        if (qstVehicleBrand.contains("起亚")) {
            return "15";
        }
        if (qstVehicleBrand.contains("荣威")) {
            return "16";
        }
        if (qstVehicleBrand.contains("三菱")) {
            return "17";
        }
        if (qstVehicleBrand.contains("斯柯达")) {
            return "18";
        }
        if (qstVehicleBrand.contains("吉利")) {
            return "19";
        }
        if (qstVehicleBrand.contains("中华")) {
            return "20";
        }
        if (qstVehicleBrand.contains("沃尔沃")) {
            return "21";
        }
        if (qstVehicleBrand.contains("雷克萨斯")) {
            return "22";
        }
        if (qstVehicleBrand.contains("菲亚特")) {
            return "23";
        }
        if (qstVehicleBrand.contains("吉利帝豪")) {
            return "24";
        }
        if (qstVehicleBrand.contains("东风")) {
            return "25";
        }
        if (qstVehicleBrand.contains("比亚迪")) {
            return "26";
        }
        if (qstVehicleBrand.contains("铃木")) {
            return "27";
        }
        if (qstVehicleBrand.contains("金杯")) {
            return "28";
        }
        if (qstVehicleBrand.contains("海马")) {
            return "29";
        }
        if (qstVehicleBrand.contains("五菱")) {
            return "30";
        }
        if (qstVehicleBrand.contains("江淮")) {
            return "31";
        }
        if (qstVehicleBrand.contains("斯巴鲁")) {
            return "32";
        }
        if (qstVehicleBrand.contains("英伦")) {
            return "33";
        }
        if (qstVehicleBrand.contains("长城")) {
            return "34";
        }
        if (qstVehicleBrand.contains("哈飞")) {
            return "35";
        }
        if (qstVehicleBrand.contains("庆铃") || qstVehicleBrand.contains("五十铃")) {
            return "36";
        }
        if (qstVehicleBrand.contains("东南")) {
            return "37";
        }
        if (qstVehicleBrand.contains("长安")) {
            return "38";
        }
        if (qstVehicleBrand.contains("福田")) {
            return "39";
        }
        if (qstVehicleBrand.contains("夏利")) {
            return "40";
        }
        if (qstVehicleBrand.contains("奔驰")) {
            return "41";
        }
        if (qstVehicleBrand.contains("一汽")) {
            return "42";
        }
        if (qstVehicleBrand.contains("依维柯")) {
            return "43";
        }
        if (qstVehicleBrand.contains("力帆")) {
            return "44";
        }
        if (qstVehicleBrand.contains("一汽奔腾")) {
            return "45";
        }
        if (qstVehicleBrand.contains("皇冠")) {
            return "46";
        }
        if (qstVehicleBrand.contains("雷诺")) {
            return "47";
        }
        if (qstVehicleBrand.contains("JMC")) {
            return "48";
        }
        if (qstVehicleBrand.contains("MG名爵")) {
            return "49";
        }
        if (qstVehicleBrand.contains("凯马")) {
            return "50";
        }
        if (qstVehicleBrand.contains("众泰")) {
            return "51";
        }
        if (qstVehicleBrand.contains("昌河")) {
            return "52";
        }
        if (qstVehicleBrand.contains("厦门金龙")) {
            return "53";
        }
        if (qstVehicleBrand.contains("上海汇众")) {
            return "54";
        }
        if (qstVehicleBrand.contains("苏州金龙")) {
            return "55";
        }
        if (qstVehicleBrand.contains("海格")) {
            return "56";
        }
        if (qstVehicleBrand.contains("宇通")) {
            return "57";
        }
        if (qstVehicleBrand.contains("中国重汽")) {
            return "58";
        }
        if (qstVehicleBrand.contains("北奔重卡")) {
            return "59";
        }
        if (qstVehicleBrand.contains("华菱星马汽车")) {
            return "60";
        }
        if (qstVehicleBrand.contains("跃进汽车")) {
            return "61";
        }
        if (qstVehicleBrand.contains("黄海汽车")) {
            return "62";
        }
        if (qstVehicleBrand.contains("保时捷")) {
            return "65";
        }
        if (qstVehicleBrand.contains("凯迪拉克")) {
            return "66";
        }
        if (qstVehicleBrand.contains("英菲尼迪")) {
            return "67";
        }
        if (qstVehicleBrand.contains("吉利全球鹰")) {
            return "68";
        }
        if (qstVehicleBrand.contains("吉普")) {
            return "69";
        }
        if (qstVehicleBrand.contains("路虎")) {
            return "70";
        }
        if (qstVehicleBrand.contains("长丰猎豹")) {
            return "71";
        }
        if (qstVehicleBrand.contains("时代汽车")) {
            return "73";
        }
        if (qstVehicleBrand.contains("长安轿车")) {
            return "75";
        }
        if (qstVehicleBrand.contains("陕汽重卡")) {
            return "76";
        }
        if (qstVehicleBrand.contains("安凯")) {
            return "81";
        }
        if (qstVehicleBrand.contains("申龙")) {
            return "82";
        }
        if (qstVehicleBrand.contains("大宇")) {
            return "83";
        }
        if (qstVehicleBrand.contains("中通")) {
            return "86";
        }
        if (qstVehicleBrand.contains("宝骏")) {
            return "87";
        }
        if (qstVehicleBrand.contains("北汽威旺")) {
            return "88";
        }
        if (qstVehicleBrand.contains("广汽传祺")) {
            return "89";
        }
        if (qstVehicleBrand.contains("陆风")) {
            return "90";
        }
        if (qstVehicleBrand.contains("北京")) {
            return "92";
        }
        if (qstVehicleBrand.contains("威麟")) {
            return "94";
        }
        if (qstVehicleBrand.contains("欧宝")) {
            return "95";
        }
        if (qstVehicleBrand.contains("开瑞")) {
            return "96";
        }
        if (qstVehicleBrand.contains("华普")) {
            return "97";
        }
        if (qstVehicleBrand.contains("讴歌")) {
            return "103";
        }
        if (qstVehicleBrand.contains("启辰")) {
            return "104";
        }
        if (qstVehicleBrand.contains("北汽制造")) {
            return "107";
        }
        if (qstVehicleBrand.contains("纳智捷")) {
            return "108";
        }
        if (qstVehicleBrand.contains("野马")) {
            return "109";
        }
        if (qstVehicleBrand.contains("中兴")) {
            return "110";
        }
        if (qstVehicleBrand.contains("克莱斯勒")) {
            return "112";
        }
        if (qstVehicleBrand.contains("广汽吉奥")) {
            return "113";
        }
        if (qstVehicleBrand.contains("瑞麟")) {
            return "115";
        }
        if (qstVehicleBrand.contains("捷豹")) {
            return "117";
        }
        if (qstVehicleBrand.contains("唐骏欧铃")) {
            return "119";
        }
        if (qstVehicleBrand.contains("福迪")) {
            return "121";
        }
        if (qstVehicleBrand.contains("莲花")) {
            return "122";
        }
        if (qstVehicleBrand.contains("双环")) {
            return "124";
        }
        if (qstVehicleBrand.contains("永源")) {
            return "128";
        }
        if (qstVehicleBrand.contains("江南")) {
            return "136";
        }
        if (qstVehicleBrand.contains("道奇")) {
            return "144";
        }
        if (qstVehicleBrand.contains("大运汽车")) {
            return "155";
        }
        if (qstVehicleBrand.contains("北方客车")) {
            return "167";
        }
        if (qstVehicleBrand.contains("九龙")) {
            return "176";
        }
        if (qstVehicleBrand.contains("宾利")) {
            return "191";
        }
        if (qstVehicleBrand.contains("舒驰客车")) {
            return "201";
        }
        if (qstVehicleBrand.contains("红旗")) {
            return "230";
        }
        return "0";
    }
    
}
