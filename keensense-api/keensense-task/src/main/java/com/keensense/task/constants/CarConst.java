package com.keensense.task.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 车辆类型转换
 * @Author: wujw
 * @CreateDate: 2019/12/10 11:35
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class CarConst {

    private CarConst(){}

    private static Map<Integer,String> CarTypeMap = new HashMap<>();
    
    /***
     * @description: SDK车辆类型转换成国标类型
     * @param SDKType SDK类型
     * @return: java.lang.String
     */
    public static String getGBCarType(Integer SDKType){
        String result = CarTypeMap.get(SDKType);
        if(result == null) {
            result = "X99";
        }
        return result;
    }

    static{
        //104-轿车
        CarTypeMap.put(104, "K33");
        //105-面包车
        CarTypeMap.put(105, "K39");
        //106-大型客车
        CarTypeMap.put(106, "K11");
        //107-越野车
        CarTypeMap.put(107, "K32");
        //108-小型货车
        CarTypeMap.put(108, "H31");
        //109-中型客车
        CarTypeMap.put(109, "K21");
        //110-工程车
        CarTypeMap.put(110, null);
        //111-大型货车
        CarTypeMap.put(111, "H11");
        //112-巴士（不生效）
        CarTypeMap.put(112, null);
        //113-皮卡车
        CarTypeMap.put(113, "H38");
        //114-商务车
        CarTypeMap.put(114, "K34");
        //115-轻客
        CarTypeMap.put(115, "K31");
        //116-中型货车
        CarTypeMap.put(116, "H21");
        //117-公交车
        CarTypeMap.put(117, "K19");
        //118-校车
        CarTypeMap.put(118, "K29");
        //119-三轮车
        CarTypeMap.put(119, "N11");
        //120-小型货车
        CarTypeMap.put(120, "H31");
    }
}
