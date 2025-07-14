package com.keensense.task.constants;

public class ColorConst {

    private ColorConst(){}

    /****
     * @description: 颜色编码转成国标编码
     * @param code SDK颜色编码
     * @return:
     */
    public static int getSDKColorByCode(Integer code){
        if(code == null){return 99;}
        switch (code){
            case 5263440 :  return 1;
            case 0 :        return 1;
            case 11711154 : return 3;
            case 11842740 : return 3;
            case 6579300:   return 3;
            case 343174 :   return 8;
            case 16724484 : return 5;
            case 8327170 :  return 5;
            case 16743167 : return 4;
            case 9983 :     return 4;
            case 12423793 : return 5;
            case 15311656 : return 5;
            case 16777215 : return 2;
            case 5287936 :  return 9;
            case 65535 :    return 6;
            case 8761028 :  return 8;
            case 9576596 :  return 10;
            case 16776448 : return 9;
            case 37887 :    return 6;
            case 2111058 :  return 8;
            case 11306222 : return 12;
            default:        return 99;
        }
    }

}
