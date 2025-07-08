package com.keensense.dataconvert.api.util.migrate;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;
import java.util.Set;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.api.util.migrate
 * @Description： <p> EsConvertMapUtil  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/27 - 15:37
 * @Modify By：
 * @ModifyTime： 2019/7/27
 * @Modify marker：
 */
public class EsConvertMapUtil {

    /**
     * mapConvertMapCustomer
     * @param jsonObject      - Map之间的转换[原始map]
     * @param ignoreNullValue - 是否忽略为Null的字段
     * @param FieldEditor     - 字段编辑过滤器
     * @return
     */
    public static JSONObject mapConvertMapCustomer(JSONObject jsonObject, boolean ignoreNullValue, FieldEditor<String> FieldEditor){
        JSONObject targetObject = new JSONObject();
        if (jsonObject == null){
            return null;
        }
        String key;
        Object value;
        final Set<Map.Entry<String, Object>> entries = jsonObject.entrySet();
        for (Map.Entry<String, Object> entry :entries) {
            value = entry.getValue();
            key = entry.getKey();
            boolean flag = (null != value && false == value.equals(jsonObject));
            if (false == ignoreNullValue || flag) {
                key = FieldEditor.edit(key);
                if (null != key) {
                    targetObject.put(key,value);
                }
            }
        }
        return targetObject;
    }

}
