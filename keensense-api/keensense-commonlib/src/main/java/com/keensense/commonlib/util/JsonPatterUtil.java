package com.keensense.commonlib.util;

import com.alibaba.fastjson.JSONObject;
import com.keensense.common.exception.VideoException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/5/21 15:49
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class JsonPatterUtil {

    public static final String CAN_NULL = "NULL";
    public static final String NOT_CAN_NULL = "NOTNULL";

    public static final String SEPARATOR = "@";

    public static final String TYPE_PATTER = "^[1-4]{1}$";
    public static final String ID_PATTER = "^.{1,64}$";
    public static final String NAME_PATTER = "^.{1,32}$";
    public static final Float SEARCH_THRESHOLD_DEFAULT_VALUE = 0.75f;
    public static final Integer SEARCH_MAXSIZE_DEFAULT_VALUE = 300;
    public static final String NONE_PATTER_OR_DEFAULT_VALUE = "NULL";

    private static final String INTEGER = "java.lang.Integer";
    private static final String STRING = "java.lang.String";
    private static final String FLOAT = "java.lang.Float";

    private JsonPatterUtil(){}

    public static Object JsonPatter(Object object,Map<String,String> attrAndDefValues,
        JSONObject inputJsonObject,String jsonName)throws VideoException{
        String json;
        JSONObject jsonObject;
        try{
            json = inputJsonObject.getString(jsonName);
            jsonObject = JSONObject.parseObject(json);
            object = JSONObject.parseObject(json, object.getClass());
            if(jsonObject == null){
                throw new VideoException(-1,"input error");
            }
        }catch(Exception e) {
            log.error("JsonPatter error init",e);
            throw new VideoException(-1,e.getMessage());
        }
        for(Entry<String,String> adValue: attrAndDefValues.entrySet()){
            try {
                String isNullFlag = adValue.getValue().split(SEPARATOR)[0];
                String upperKey = adValue.getKey().substring(0,1).toUpperCase()+adValue.getKey().substring(1);
                upperKey = "id".equalsIgnoreCase(upperKey)?"ID":upperKey;
                Object inputValue = jsonObject.get(upperKey);
                String defVal = adValue.getValue().split(SEPARATOR)[1];
                if(isNullFlag.equals(CAN_NULL)&&inputValue == null &&!defVal.equals(NONE_PATTER_OR_DEFAULT_VALUE)){
                    canNull(object,adValue.getKey(),defVal);
                    continue;
                }
                if(isNullFlag.equals(NOT_CAN_NULL)){
                    nonCanNull(inputValue,upperKey,defVal);
                }
            } catch (Exception e) {
                log.error("JsonPatter error",e);
                throw new VideoException(-1,e.getMessage());
            }
        }
        return object;
    }


    private static void nonCanNull(Object inputValue,String upperKey,String defVal){
        if(inputValue == null){
            throw new VideoException(-1,upperKey+" donnot allowed to be empty");
        }
        if(!defVal.equals(NONE_PATTER_OR_DEFAULT_VALUE)&& !PatternMatchUtils.simpleMatch(inputValue.toString(),defVal)){
            throw new VideoException(-1,upperKey+" is non-compliant");
        }
    }

    private static void canNull(Object object,String key,String defVal)
        throws NoSuchFieldException,IllegalAccessException{
        Field field = object.getClass().getDeclaredField(key);
        field.setAccessible(true);
        String typeName = field.getGenericType().getTypeName();
        switch (typeName){
            case INTEGER:
                field.set(object,new Integer(defVal));
                break;
            case STRING:
                field.set(object,defVal);
                break;
            case FLOAT:
                field.set(object,new Float(defVal));
                break;
            default:break;
        }
    }

}

