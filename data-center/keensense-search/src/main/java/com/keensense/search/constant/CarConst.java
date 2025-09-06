package com.keensense.search.constant;

public class CarConst {
    private CarConst(){}

    //车牌种类 SDK转换成国标
    public static String getPlateCodeBySDKCode(Integer type){
        if(type == null) return "99";
        switch (type){
            case 1 : return "02";
            case 2 : return "04";
            case 3 : return "01";
            case 4 : return "23";
            case 5 : return "31";
            case 6 : return "32";
            case 7 : return "03";
            case 8 : return "26";
            case 9 : return "25";
            case 10 : return "16";
            case 11 : return "02";
            case 12 : return "01";
            default: return "99";
        }
    }

    //车牌种类 SDK转换成国标
    public static Integer getPlateCodeByGBCode(String type){
        if(type == null) return -1;
        switch (type){
            case "02": return 1;
            case "04": return 2;
            case "01": return 3;
            case "23": return 4;
            case "31": return 5;
            case "32": return 6;
            case "03": return 7;
            case "26": return 8;
            case "25": return 9;
            case "16": return 10;
            default: return -1;
        }
    }

    //车牌类型 SDK转换成国标
    public static String getVehicleTypeBySDKCode(Integer type){
        if(type == null) return "X99";
        switch (type){
            case 1 : return "K33";
            case 2 : return "K25";
            case 3 : return "K16";
            case 4 : return "H30";
            case 5 : return "H20";
            case 6 : return "H10";
            case 7 : return "K30";
            case 8 : return "K20";
            case 9 : return "K10";
            case 10 : return "K31";
            case 11 : return "H40";
            case 12 : return "H14";
            case 13 : return "H11";
            default: return "X99";
        }
    }

    //车牌类型 SDK转换成国标
    public static String getVehicleTypeByGBCode(String type){
        if(type == null) return "99";
        switch (type){
            case "K33":	  return "1" ;
            case "K25":   return "2" ;
            case "K16":   return "3" ;
            case "H30":   return "4" ;
            case "H20":   return "5" ;
            case "H10":   return "6" ;
            case "K30":   return "7" ;
            case "K20":   return "8" ;
            case "K10":   return "9" ;
            case "K31":   return "10" ;
            case "H40":   return "11" ;
            case "H14":   return "12" ;
            case "H11":   return "13" ;
            default: return "99";
        }
    }
}
