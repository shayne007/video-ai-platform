package com.keensense.admin.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: fengsy
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 15:55 2019/12/29
 * @Version v0.1
 */
public class BikeGenreConstants {


    public final static Map<String, String> bikeGenreMapToES = new HashMap<>();

    public static String getGbBikeGenre(String bikeGenre){
        String result = bikeGenreMapToES.get(bikeGenre);
        if(result == null) {
            result = "-1";
        }
        return result;
    }

    // sdk返回非机动车类型：1-两轮 2-三轮 3-自行车 -1-其他
    static {
        bikeGenreMapToES.put("1", "2");
        bikeGenreMapToES.put("2", "5");
        bikeGenreMapToES.put("3", "3");
        bikeGenreMapToES.put("-1", "-1");

    }

}
