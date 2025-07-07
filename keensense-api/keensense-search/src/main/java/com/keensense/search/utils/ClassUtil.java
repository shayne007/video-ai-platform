package com.keensense.search.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 * Created by memory_fu on 2019/5/9.
 */
public class ClassUtil {
    private ClassUtil(){}

    /**
     * 获取类属性
     * @param fieldStr
     * @param aClass
     * @param <T>
     * @return
     */
    public static <T> String getFieldStr(String fieldStr,Class<T> aClass){
        Map<String, String> filedNameMap = getFiledNameMap(aClass);
        return filedNameMap.get(StringUtils.lowerCase(fieldStr));
    }

    /**
     * 获取类对象属性映射关系
     * @param aClass
     * @param <T>
     * @return
     */
    public static <T> Map<String,String> getFiledNameMap(Class<T> aClass){
        Map<String, String> map = new HashMap<>();

        if(aClass == Object.class){
            return map;
        }

        for (Class<?> clazz = aClass; clazz != Object.class; clazz = clazz.getSuperclass()){
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field :declaredFields) {
                String fieldName = field.getName();
                map.put(StringUtils.lowerCase(fieldName),fieldName);
            }
        }

        return map;
    }


}
