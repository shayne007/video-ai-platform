package com.keensense.admin.util;


import com.google.common.collect.Lists;
import org.apache.commons.beanutils.BeanMap;
import org.dozer.DozerBeanMapper;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;



public class EntityObjectConverter {

    /*
     * 实例化对象
     */
    private static DozerBeanMapper dozer = new DozerBeanMapper();

    /**
     * @Description: 将目标对象转换为指定对象，相同属性名进行属性值复制
     *
     * @Title: EntityObjectConverter.java
     * @Copyright: Copyright (c) 2013
     *
     * @author Comsys-LZP
     * @date 2018-1-4 下午02:32:34
     * @version V2.0
     */
    @SuppressWarnings("unchecked")
    public static <T> T getObject(Object source,Class<T> cls){
        if (source==null) {
            return null;
        }
        return (T) dozer.map(source, cls);
    }

    /**
     * @Description: 两个对象之间相同属性名的属性值复制
     *
     * @Title: EntityObjectConverter.java
     * @Copyright: Copyright (c) 2013
     *
     * @author Comsys-LZP
     * @date 2013-11-4 下午02:33:56
     * @version V2.0
     */
    public static void setObject(Object source,Object target){
        dozer.map(source, target);
    }
    /**
     * 基于Dozer转换Collection中对象的类型.
     */
    @SuppressWarnings("rawtypes")
    public static <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
        List<T> destinationList = Lists.newArrayList();
        for (Object sourceObject : sourceList) {
            T destinationObject = dozer.map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }


     /**
     * @Description: 对象集合中对象相同属性名的属性值复制
     *
     * @Title: EntityObjectConverter.java
     *
     * @Copyright: Copyright (c) 2013
     * @author Comsys-LZP
     * @date 2018-08-4 下午02:34:26
     * @version V2.0
     */
    @SuppressWarnings("unchecked")
    public static List getList(List source,Class cls){
        List listTarget = new ArrayList();
        if(source != null){
            for (Object object : source) {
                Object objTarget = EntityObjectConverter.getObject(object, cls);
                listTarget.add(objTarget);
            }
        }
        return listTarget;
    }

    private static final String JAVAP = "java.";
    private static final String JAVADATESTR = "java.util.Date";

    /**
     * 获取利用反射获取类里面的值和名称
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object obj){
        Map<String, Object> map = new HashMap<>();
        try {
            Class<?> clazz = obj.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = field.get(obj);
                map.put(fieldName, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 利用递归调用将Object中的值全部进行获取
     *
     * @param timeFormatStr 格式化时间字符串默认<strong>2017-03-10 10:21</strong>
     * @param obj           对象
     * @param excludeFields 排除的属性
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, String> objectToMapString(String timeFormatStr, Object obj, String... excludeFields) throws IllegalAccessException {
        Map<String, String> map = new HashMap<>();

        if (excludeFields.length!=0){
            List<String> list = Arrays.asList(excludeFields);
            objectTransfer(timeFormatStr, obj, map, list);
        }else{
            objectTransfer(timeFormatStr, obj, map,null);
        }
        return map;
    }


    /**
     * 递归调用函数
     *
     * @param obj           对象
     * @param map           map
     * @param excludeFields 对应参数
     * @return
     * @throws IllegalAccessException
     */
    private static Map<String, String> objectTransfer(String timeFormatStr, Object obj, Map<String, String> map, List<String> excludeFields) throws IllegalAccessException {
        boolean isExclude=false;
        //默认字符串
        String formatStr = "YYYY-MM-dd HH:mm:ss";
        //设置格式化字符串
        if (timeFormatStr != null && !timeFormatStr.isEmpty()) {
            formatStr = timeFormatStr;
        }
        if (excludeFields!=null){
            isExclude=true;
        }
        Class<?> clazz = obj.getClass();
        //获取值
        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = clazz.getSimpleName() + "." + field.getName();
            //判断是不是需要跳过某个属性
            if (isExclude&&excludeFields.contains(fieldName)){
                continue;
            }
            //设置属性可以被访问
            field.setAccessible(true);
            Object value = field.get(obj);
            Class<?> valueClass = value.getClass();
            if (valueClass.isPrimitive()) {
                map.put(fieldName, value.toString());

            } else if (valueClass.getName().contains(JAVAP)) {//判断是不是基本类型
                if (valueClass.getName().equals(JAVADATESTR)) {
                    //格式化Date类型
                    SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
                    Date date = (Date) value;
                    String dataStr = sdf.format(date);
                    map.put(fieldName, dataStr);
                } else {
                    map.put(fieldName, value.toString());
                }
            } else {
                objectTransfer(timeFormatStr, value, map,excludeFields);
            }
        }
        return map;
    }
    /**
     * 将Map对象通过反射机制转换成Bean对象
     *
     * @param map 存放数据的map对象
     * @param clazz 待转换的class
     * @return 转换后的Bean对象
     * @throws Exception 异常
     */
    public static Object mapToBean(Map<String, Object> map, Class<?> clazz) throws Exception {
        Object obj = clazz.newInstance();
        if(map != null && map.size() > 0) {
            for(Map.Entry<String, Object> entry : map.entrySet()) {
                String propertyName = entry.getKey();       //属性名
                Object value = entry.getValue();
                String setMethodName = "set"
                        + propertyName.substring(0, 1).toUpperCase()
                        + propertyName.substring(1);
                Field field = getClassField(clazz, propertyName);
                if(field==null) {
                    continue;
                }
                Class<?> fieldTypeClass = field.getType();
                value = convertValType(value, fieldTypeClass);
                try{
                    clazz.getMethod(setMethodName, field.getType()).invoke(obj, value);
                }catch(NoSuchMethodException e){
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }

    /**
     * 获取指定字段名称查找在class中的对应的Field对象(包括查找父类)
     *
     * @param clazz 指定的class
     * @param fieldName 字段名称
     * @return Field对象
     */
    private static Field getClassField(Class<?> clazz, String fieldName) {
        if( Object.class.getName().equals(clazz.getName())) {
            return null;
        }
        Field []declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }

        Class<?> superClass = clazz.getSuperclass();
        if(superClass != null) {// 简单的递归一下
            return getClassField(superClass, fieldName);
        }
        return null;
    }

    /**
     * 将Object类型的值，转换成bean对象属性里对应的类型值
     *
     * @param value Object对象值
     * @param fieldTypeClass 属性的类型
     * @return 转换后的值
     */
    private static Object convertValType(Object value, Class<?> fieldTypeClass) {
        Object retVal = null;
        if(Long.class.getName().equals(fieldTypeClass.getName())
                || long.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Long.parseLong(value.toString());
        } else if(Integer.class.getName().equals(fieldTypeClass.getName())
                || int.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Integer.parseInt(value.toString());
        } else if(Float.class.getName().equals(fieldTypeClass.getName())
                || float.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Float.parseFloat(value.toString());
        } else if(Double.class.getName().equals(fieldTypeClass.getName())
                || double.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Double.parseDouble(value.toString());
        } else {
            retVal = value;
        }
        return retVal;
    }

    /**
     * Object转Map，包含继承的属性
     * 返回的Map不含[value=null]键值对
     * Wade Shu 2018-11-05
     */
    public static Map<String, Object> toMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        } else {
            BeanMap beanMap = new BeanMap(obj);
            Iterator it = beanMap.keyIterator();
            while(it.hasNext()) {
                String name = (String)it.next();
                Object value = beanMap.get(name);
                if (value != null && !name.equals("class")) {
                    map.put(name, value);
                }
            }
            return map;
        }
    }
}
