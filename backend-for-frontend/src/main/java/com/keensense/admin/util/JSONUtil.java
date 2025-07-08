package com.keensense.admin.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * json操作帮助类
 * 提供：json文件操作，json与String互转，json转java，json转map，json转list
 * @author Administrator
 *
 */
public class JSONUtil {
    
    /**
     * 读取整个json文件
     * @param filePath 
     * @return 字符串
     */
    public static String readJsonFile(String filePath)
    {
        File file = new File(filePath);
        Scanner scanner = null;
        StringBuilder buffer = new StringBuilder();
        try {
            scanner = new Scanner(file, "utf-8");
            while (scanner.hasNextLine()) {
                buffer.append(scanner.nextLine());
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return buffer.toString();
    }
    
    /**
     * 读取json文件中某个属性
     * @param filePath
     * @param attrName
     * @return  attrValue
     */
    public static String readJsonFile(String filePath,String attrName)
    {
        return null;
    }
    
	/**
	 * 从一个JSON 对象字符格式中得到一个java对象
	 * 
	 * @param jsonString
	 * @param pojoCalss
	 * @return java对象
	 */
	
	public static Object getObject4JsonString(String jsonString, Class<?> pojoCalss) {
		Object pojo;
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		pojo = JSONObject.toBean(jsonObject, pojoCalss);
		return pojo;
	}
	

	/**
	 * 从json HASH表达式中获取一个map，改map支持嵌套功能
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Map<String,Object> getMap4Json(String jsonString) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Iterator<?> keyIter = jsonObject.keys();
		String key;
		Object value;
		Map<String,Object> valueMap = new HashMap<String,Object>();

		while (keyIter.hasNext()) {
			key = (String) keyIter.next();
			value = jsonObject.get(key);
			valueMap.put(key, value);
		}

		return valueMap;
	}
	
	/**
	 * 从json HASH表达式中获取一个map，改map支持嵌套功能
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Map<String, String> getMapStr4Json(String jsonString) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Iterator<?> keyIter = jsonObject.keys();
		String key;
		String value;
		Map<String, String> valueMap = new LinkedHashMap<String, String>();

		while (keyIter.hasNext()) {
			key = (String) keyIter.next();
			value = (String) jsonObject.get(key);
			valueMap.put(key, value);
		}

		return valueMap;
	}
	
	
	/**
	 * 从json数组中得到相应java数组
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Object[] getObjectArray4Json(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		return jsonArray.toArray();

	}

	/** */
	/**
	 * 从json对象集合表达式中得到一个java对象列表
	 * 
	 * @param jsonString
	 * @param pojoClass
	 * @return
	 */
	public static List<Object> getList4Json(String jsonString, Class<?> pojoClass) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);

		JSONObject jsonObject;
		Object pojoValue;

		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < jsonArray.size(); i++) {

			jsonObject = jsonArray.getJSONObject(i);
			pojoValue = JSONObject.toBean(jsonObject, pojoClass);
			list.add(pojoValue);

		}
		return list;

	}

	/** */
	/**
	 * 从json数组中解析出java字符串数组
	 * 
	 * @param jsonString
	 * @return
	 */
	public static String[] getStringArray4Json(String jsonString) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		String[] stringArray = new String[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			stringArray[i] = jsonArray.getString(i);

		}

		return stringArray;
	}

	/**
	 * Map转String
	 * @param paramMap
	 * @return
	 */
   public static String getJSonString4Map(Map<String, Object> paramMap) {

       if(null != paramMap){
           JSONObject json = JSONObject.fromObject(paramMap);
           if(null != json){
               return json.toString();
           }
           else return null;
       }
       
        return null;
    }
	   
   public static String string2Json(String s) {
        StringBuffer sb = new StringBuffer ();      
        for (int i=0; i<s.length();i++){
            char c = s.charAt(i);      
            switch (c) {      
            case '"':      
                sb.append("\"");      
                break;      
            case '\\':      
                sb.append("\\\\");      
                break;      
            case '/':      
                sb.append("\\/");      
                break;      
            case '\b':      
                sb.append("\\b");      
                break;      
            case '\f':      
                sb.append("\\f");      
                break;      
            case '\n':      
                sb.append("\\n");      
                break;      
            case '\r':      
                sb.append("\\r");      
                break;      
            case '\t':      
                //sb.append("\\t");      
                break;      
            default:      
                sb.append(c);      
            } 
          
     }
        return sb.toString();    
 }
}
