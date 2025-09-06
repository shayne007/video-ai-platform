package com.keensense.search.constant;

public class ColorConst {
    private ColorConst(){}

    //值颜色转成国标编码
    public static String getGbColorByValue(String value){
        if(value == null) return "99";
        switch (value){
            case "黑" :   return "1";
            case "白" :   return "2";
            case "灰" :   return "3";
            case "红" :   return "4";
            case "蓝" :   return "5";
            case "黄" :   return "6";
            case "橙" :   return "7";
            case "棕" :   return "8";
            case "绿" :   return "9";
            case "紫" :   return "10";
            case "青" :   return "11";
            case "粉" :   return "12";
            case "透明" : return "13";
            default: return "99";
        }
    }

    //国标编码转成颜色
    public static String getColorByGbCode(String value){
        if(value == null) return "其他";
        switch (value){
            case "1":   return "黑";
            case "2":   return "白";
            case "3":   return "灰";
            case "4":   return "红";
            case "5":   return "蓝";
            case "6":   return "黄";
            case "7":   return "橙";
            case "8":   return "棕";
            case "9":   return "绿";
            case "10":  return "紫";
            case "11":  return "青";
            case "12":  return "粉";
            case "13":  return "透明";
            default: return "其他";
        }
    }

    //SDK颜色转成值
    public static String getSDKColorByCode(Integer code){
        if(code == null) return "其他";
        switch (code){
            case 5263440 :  return "黑";
            case 0 :        return "黑";
            case 11842740 : return "灰";
            case 343174 :   return "棕";
            case 16724484 : return "蓝";
            case 8327170 :  return "蓝";
            case 16743167 : return "红";
            case 9983 :     return "红";
            case 12423793 : return "蓝";
            case 15311656 : return "蓝";
            case 16777215 : return "白";
            case 5287936 :  return "绿";
            case 65535 :    return "黄";
            case 8761028 :  return "棕";
            case 9576596 :  return "紫";
            case 16776448 : return "绿";
            case 37887 :    return "黄";
            case 2111058 :  return "棕";
            default:        return "其他";
        }
    }

    public static Integer getSDKCodeByValue(String code){
        if(code == null) return -1;
        switch (code){
            case "黑":	  return 5263440 ;
            case "灰":    return 11842740 ;
            case "棕":    return 343174 ;
            case "蓝":    return 8327170 ;
            case "红":    return 9983 ;
            case "白":    return 16777215 ;
            case "绿":    return 5287936 ;
            case "黄":    return 65535 ;
            case "紫":    return 9576596 ;
            default:      return -1;
        }
    }
}
