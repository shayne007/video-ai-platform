package com.keensense.admin.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.admin.entity.task.CtrlUnitFile;
import com.keensense.admin.entity.task.VsdTaskRelation;
import com.keensense.admin.service.task.ICtrlUnitFileService;
import com.keensense.admin.service.task.IVsdTaskRelationService;
import com.keensense.common.config.SpringContext;
import com.loocme.sys.util.MapUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VsdTaskUtil {
    private VsdTaskUtil() {
    }

    private static IVsdTaskRelationService vsdTaskRelationService = SpringContext.getBean(IVsdTaskRelationService.class);

    private static ICtrlUnitFileService ctrlUnitFileService = SpringContext.getBean(ICtrlUnitFileService.class);
    private static Map<String, Integer> taskFromTypeMap = new HashMap<>();
    private static Map<String, String> taskVideoHttpMap = new HashMap<>();

    private static long lastCleanFromTypeTime = 0;
    private static long lastCleanVideoHttpTime = 0;

    public static Integer getFromTypeByParam(String serialnumber) {
        if (0 == lastCleanFromTypeTime) {
            lastCleanFromTypeTime = new Date().getTime();
        } else {
            if (new Date().getTime() - lastCleanFromTypeTime > 86400 * 1000) {
                lastCleanFromTypeTime = new Date().getTime();
                taskFromTypeMap = new HashMap<>();
            }
        }

        if (!taskFromTypeMap.containsKey(serialnumber)) {
            Map<String, Object> obj = vsdTaskRelationService.getMap(new QueryWrapper<VsdTaskRelation>().eq("serialnumber", serialnumber));
            if (null != obj) {
                taskFromTypeMap.put(serialnumber, MapUtil.getInteger(obj, "from_type"));
            }
        }

        return taskFromTypeMap.get(serialnumber);
    }

    public static String getVideoHttpByParam(String serialnumber, int fromType) {
        String key = serialnumber + fromType;
        if (0 == lastCleanVideoHttpTime) {
            lastCleanVideoHttpTime = new Date().getTime();
        } else {
            if (new Date().getTime() - lastCleanVideoHttpTime > 1 * 60 * 60 * 1000) {
                lastCleanVideoHttpTime = new Date().getTime();
                taskVideoHttpMap = new HashMap<>();
            }
        }

        if (!taskVideoHttpMap.containsKey(key)) {
            VsdTaskRelation obj2 = vsdTaskRelationService.getOne(new QueryWrapper<VsdTaskRelation>().eq("serialnumber", serialnumber).eq("from_type", fromType));
            if (obj2 != null) {
                Map<String, Object> obj = ctrlUnitFileService.getMap(new QueryWrapper<CtrlUnitFile>().eq("id", obj2.getCameraFileId()));
                if (null != obj) {
                    taskVideoHttpMap.put(key, MapUtil.getString(obj, "filePathafterupload"));
                } else {
                    taskVideoHttpMap.put(key, null);
                }
            }
        }

        return taskVideoHttpMap.get(key);
    }
}
