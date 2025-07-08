package com.keensense.dataconvert.api.util.migrate;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Editor;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.keensense.dataconvert.biz.common.consts.ConfigPathConstant;
import com.keensense.dataconvert.biz.entity.VlprResult;
import com.keensense.dataconvert.framework.common.utils.properties.PropertiesHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.api.util.migrate
 * @Description： <p> EsFieldConvertUtil - 字段转换工具  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/27 - 10:24
 * @Modify By：
 * @ModifyTime： 2019/7/27
 * @Modify marker：
 */
public class EsFieldConvertUtil {

    private static final Logger logger = LoggerFactory.getLogger(EsFieldConvertUtil.class);

    /**
     * 构造函数初始化数据
     * @param resourcesPaths
     */
    public EsFieldConvertUtil(String... resourcesPaths) {
        logger.info("=== 初始化ES-ES字段映射关系,通过配置文件[{}]Loading ...",resourcesPaths);
        initFieldMap(resourcesPaths);
    }

    /**
     * 全局 字段匹配map
     */
    private Map<String,String>  FILED_MAP;

    /**
     * 需要转换的map
     */
    private Map<String,Object>  TARGET_MAP = new HashMap<>();


    /**
     * 字段转换 - need 判断字段是否下划线 是否驼峰 大小是否敏感
     * @param convertStr
     * @param notMatch  是否处理映射不到的字段
     * @return
     */
    private  String convertProp(String convertStr,boolean notMatch){
        if (notMatch){
            return FILED_MAP.get(convertStr) == null? convertStr:FILED_MAP.get(convertStr);
        }else{
            return FILED_MAP.get(convertStr);
        }
    }


    /**
     * bean 转换为指定的map 然后可以存入 es - es到es数据的转换
     * @param bean
     * @param ignoreNullValue  - 匹配不上的就丢弃
     * @return
     */
    private  Map<String,Object> beanConvertMapCustomer(Object bean,boolean ignoreNullValue,boolean notMatch){
        BeanUtil.beanToMap(bean, TARGET_MAP, ignoreNullValue, new Editor<String>() {
            @Override
            public String edit(String s) {
                //判断字段是否下划线 是否驼峰 大小是否敏感
                return convertProp(s,notMatch);
            }
        });
        return TARGET_MAP;
    }


    /**
     * map 映射成另外一个map [字段变化]
     * @param sourceObject
     * @param ignoreNullValue
     * @return
     */
    public  JSONObject mapConvertMapCustomer(JSONObject sourceObject, boolean ignoreNullValue,boolean notMatch){
        JSONObject jsonObject = EsConvertMapUtil.mapConvertMapCustomer(sourceObject, ignoreNullValue, new FieldEditor<String>() {
            @Override
            public String edit(String s) {
                return convertProp(s,notMatch);
            }
        });
        return jsonObject;
    }

    /**
     * 初始化Map
     */
    private  void initFieldMap(String... resourcesPaths){
        FILED_MAP = PropertiesHelper.getInstance().loadPropertiesMap(resourcesPaths);
    }


    /**
     * just for test
     * @param args
     */
    public static void main(String[] args) {
        EsFieldConvertUtil esFieldConvertUtil = new EsFieldConvertUtil(ConfigPathConstant.CONFIG_SUBMETER_VLPR_MIGRATE_ES_ES);
        VlprResult vlprResult = new VlprResult();
        vlprResult.setCameraid(1000L);
        vlprResult.setId(222);
        vlprResult.setSerialnumber("1001010086");
        vlprResult.setTaskid(10010L);
        Map<String, Object> map = esFieldConvertUtil.beanConvertMapCustomer(vlprResult, false,true);
        String jsonStr = JSON.toJSONString(vlprResult);
        JSONObject json = esFieldConvertUtil.mapConvertMapCustomer(JSONObject.parseObject(jsonStr),true,true);
        logger.info("=== \n[源Map]转换后的map数据:\n{}\n[目Map]转换后的map数据:\n{}", jsonStr,JSON.toJSONString(json));
        logger.info("=== \n[源]转换后的map数据:\n{}\n[目]转换后的map数据:\n{}", jsonStr,JSON.toJSONString(map));
    }
    //output
    //[源]转换后的map数据:
    //{"cameraid":1000,"id":222,"serialnumber":"1001010086","taskid":10010}
    //[目]转换后的map数据:
    //{"cameraidTest":1000,"idTest":222,"serialnumberTest":"1001010086","taskidTest":10010}
}
