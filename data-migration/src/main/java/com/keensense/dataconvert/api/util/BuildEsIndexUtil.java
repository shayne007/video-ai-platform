package com.keensense.dataconvert.api.util;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.keensense.dataconvert.biz.common.consts.ConfigPathConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.api.util
 * @Description： <p> BuildEsIndexUtil - 构建es索引 </p>
 * @Author： - Jason
 * @CreatTime：2019/7/24 - 9:23
 * @Modify By：
 * @ModifyTime： 2019/7/24
 * @Modify marker：
 */
public class BuildEsIndexUtil {

    private static final Logger logger = LoggerFactory.getLogger(BuildEsIndexUtil.class);

    /**
     * fix bug
     *      springboot 加载不到对应的文件 需要做处理
     */

    /**
     * eg:
     *      字段 | 类型 | 是否索引
     *      name , text ,  true
     *  通过文件构建 index索引结构
     * @param path
     * @return
     */
    public static List<String> buildByPathFile(String path){
        InputStream buildEsStream = BuildEsIndexUtil.class.getClassLoader().getResourceAsStream(path);
        List<String> columns = null;
        try {
            columns = com.keensense.dataconvert.framework.common.utils.file.FileUtil.text2List(buildEsStream);
        } catch (IOException e) {
            logger.error("=== IoError:msg:{} ===",e.getMessage());
        }
        return columns;
    }

    /**
     * 通过网络加载配置
     * @param url
     * @return
     */
    public static List<String> buildByUrl(URL url){
        List<String> columns = FileUtil.readUtf8Lines(url);
        return columns;
    }

}
