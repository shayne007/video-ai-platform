package com.keensense.admin.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.admin.entity.sys.CfgMemProps;
import com.keensense.admin.mapper.sys.CfgMemPropsMapper;
import com.loocme.sys.util.ListUtil;
import com.loocme.sys.util.MapUtil;
import com.loocme.sys.util.ThreadUtil;
import com.loocme.sys.util.ThreadUtil.ExecutorService;
import com.keensense.common.config.SpringContext;

import lombok.extern.slf4j.Slf4j;

/**
 * 数据库初始化配置加载
 *
 * @description:
 * @author: luowei
 * @createDate:2019年5月9日 下午3:57:54
 * @company:
 */
@Slf4j
public class DbPropUtil {
    private static long LAST_UPDATE_TIME = 0L;
    public static Map<String, Object> PROP_MAP = new HashMap<String, Object>();
    private static ExecutorService ESERVICE = null;
    public static String MODULE_NAME = "";
    private static CfgMemPropsMapper mapper = SpringContext.getBean(CfgMemPropsMapper.class);

    public DbPropUtil() {
    }

    public static void start(final String moduleName) {
        ESERVICE = ThreadUtil.newSingleThreadExecutor();
        MODULE_NAME = moduleName;
        reload(moduleName);
        ESERVICE.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10000L);
                    } catch (Exception e) {
                        log.error("DbPropUtil execute error" + e);
                        Thread.currentThread().interrupt();
                    }
                    DbPropUtil.reload(moduleName);
                }
            }
        });
    }

    public static List<CfgMemProps> loadAllProp() {
        QueryWrapper<CfgMemProps> queryWrapper = new QueryWrapper<CfgMemProps>().eq("module_name", MODULE_NAME).orderByAsc("propKey");
        return mapper.selectList(queryWrapper);
    }

    public static void reload(String moduleName) {
        QueryWrapper<CfgMemProps> queryWrapper = new QueryWrapper<CfgMemProps>().eq("module_name", moduleName);
        if (0L < LAST_UPDATE_TIME) {
            queryWrapper.gt("update_time", new Date(LAST_UPDATE_TIME));
        }
        queryWrapper = queryWrapper.orderByAsc("update_time");
        List<CfgMemProps> propList = mapper.selectList(queryWrapper);
        if (ListUtil.isNotNull(propList)) {
            StringBuffer logBuff = new StringBuffer();
            for (int i = 0; i < propList.size(); ++i) {
                CfgMemProps prop = propList.get(i);
                Long updateTime = prop.getUpdateTime().getTime();
                PROP_MAP.put(prop.getPropKey(), prop.getPropValue());
                logBuff.append("[").append(prop.getPropKey()).append(",").append(prop.getPropValue()).append("]");
                if (LAST_UPDATE_TIME < updateTime) {
                    LAST_UPDATE_TIME = updateTime;
                }
            }
            log.info("[db_mem_prop_changed]:" + logBuff.toString());
            log.info("[db_mem_prop_all]:" + PROP_MAP.toString());
        }
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static int getInt(String key, int defaultValue) {
        return PROP_MAP.containsKey(key) ? MapUtil.getInteger(PROP_MAP, key) : defaultValue;
    }

    public static String getString(String key) {
        return getString(key, "");
    }

    public static String getString(String key, String defaultValue) {
        return PROP_MAP.containsKey(key) ? MapUtil.getString(PROP_MAP, key) : defaultValue;
    }

    public static boolean getBoolean(String key) {
        return MapUtil.getBoolean(PROP_MAP, key);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return PROP_MAP.containsKey(key) ? MapUtil.getBoolean(PROP_MAP, key) : defaultValue;
    }

    public static double getDouble(String key) {
        return getDouble(key, 0.0D);
    }

    public static double getDouble(String key, double defaultValue) {
        return PROP_MAP.containsKey(key) ? MapUtil.getDouble(PROP_MAP, key) : defaultValue;
    }

    public static float getFloat(String key) {
        return getFloat(key, 0.0F);
    }

    public static float getFloat(String key, float defaultValue) {
        return PROP_MAP.containsKey(key) ? MapUtil.getFloat(PROP_MAP, key) : defaultValue;
    }
}
