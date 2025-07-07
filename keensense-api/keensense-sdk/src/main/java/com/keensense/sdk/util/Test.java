package com.keensense.sdk.util;

import com.loocme.sys.util.DateUtil;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by memory_fu on 2019/8/21.
 */
public class Test {
    
    public static void main(String[] args) {
        Date day1 = DateUtil.getFirstSecondInDay(new Date());
        Date day2 = DateUtil.getLastSecondInDay(new Date());
    
        System.out.println(day1);
        System.out.println(day2);
    
        Date nextDay = DateUtil.getNextDay();
    
        System.out.println(nextDay);
    
        ArrayList<String> list = new ArrayList<>();
        list.add("fuhao");
        list.add("ssss");
        list.add("fuhao1");
        list.add("hhhhh");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
            if(list.get(i).equals("ssss")){
                list.remove(i);
            }
        }
    
        System.out.println('催');
        
    
    }
    
}
