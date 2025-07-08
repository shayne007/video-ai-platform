package com.keensense.dataconvert.framework.common.utils.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.framework.common.utils.json
 * @Description： <p> JsonUtil </p>
 * @Author： - Jason
 * @CreatTime：2019/8/16 - 14:08
 * @Modify By：
 * @ModifyTime： 2019/8/16
 * @Modify marker：
 */
public class JsonUtil {

    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * 读取解析json文件
     * @param path
     * @param type
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T readJsonFromClassPath(String path, Type type) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        if (resource.exists()) {
            return JSON.parseObject(resource.getInputStream(), StandardCharsets.UTF_8, type,
                    // 自动关闭流
                    Feature.AutoCloseSource,
                    // 允许注释
                    Feature.AllowComment,
                    // 允许单引号
                    Feature.AllowSingleQuotes,
                    // 使用 Big decimal
                    Feature.UseBigDecimal);
        } else {
            logger.error("[IOException]readJsonFromClassPath:Please check your json file & path ... ");
            throw new IOException();
        }
    }


    /**
     * just for test
     * @param args
     */
    public static void main(String[] args) {
        try {
            JSONObject jsonObject = JsonUtil.readJsonFromClassPath("structure/4.0.3.7-index-bike.json", JSONObject.class);
            Set<String> strKey = jsonObject.keySet();
            for (String key :strKey) {
                JSONObject valueJson = jsonObject.getJSONObject(key);
                String index = valueJson.getString("index");
                String type = valueJson.getString("type");
                logger.info("[LoadJson]name:[{}],index:[{}],type:[{}] ...",key,index,type);
            }

        } catch (IOException e) {
        }
    }
}
