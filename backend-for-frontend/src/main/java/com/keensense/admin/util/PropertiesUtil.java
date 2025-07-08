package com.keensense.admin.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 获取当前应用properties文件内容
 *
 * @author Administrator
 */
@Slf4j
public class PropertiesUtil {
    private static PropertiesUtil propertiesUtil = null;

    public static PropertiesUtil getInstance() {
        if (propertiesUtil == null) {
            propertiesUtil = new PropertiesUtil();
        }
        return propertiesUtil;
    }


	/*	*/

    /**
     * 获取 appftpserver 文件的内容
     *
     * @param key
     * @return
     */

    public static String getFtpPackey(String key) {
        String result = null;
        Properties props;
        try {
            props = PropertiesLoaderUtils
                    .loadAllProperties("appftpserver.properties");
            result = props.getProperty(key);// 根据name得到对应的value
        } catch (IOException e) {
            log.error("getFtpPackey", e);
        }
        return result;

    }

    public static String getParameterPackey(String key) {
        return getParameterKey(key, null);
    }

    /**
     * 获取中文配置
     *
     * @param key
     * @param resource
     * @return
     */
    public static String getParameterKey(String key, String resource) {
        String value = "";
        Properties p = new Properties();
        InputStreamReader reader = null;
        InputStream u = null;
        try {
            ClassLoader classLoader = PropertiesUtil.class.getClassLoader();
            if (resource == null) {
                u = classLoader.getResourceAsStream("parameter.properties");
            } else {
                u = classLoader.getResourceAsStream(resource);
            }
            reader = new InputStreamReader(u, "utf-8");
            p.load(reader);
            value = p.getProperty(key);
        } catch (Exception e) {
            log.error("读取属性文件出错!!", e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (u != null) {
                    u.close();
                }
            } catch (IOException e) {
                log.error("IO异常", e);
            }
        }
        return value;
    }

    /**
     * 获取中文配置
     *
     * @param key
     * @return
     */
    public static String getParameterKey(String key) {
        return getParameterKey(key, null);
    }

}
