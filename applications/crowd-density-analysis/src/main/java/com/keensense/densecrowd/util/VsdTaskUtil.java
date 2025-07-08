package com.keensense.densecrowd.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.common.config.SpringContext;
import com.keensense.densecrowd.entity.task.VsdTaskRelation;
import com.keensense.densecrowd.service.task.IVsdTaskRelationService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VsdTaskUtil {
    private VsdTaskUtil() {
    }

    private static IVsdTaskRelationService vsdTaskRelationService = SpringContext.getBean(IVsdTaskRelationService.class);

    private static Map<String, String> vsdTaskRelation = new HashMap<>();

    private static long lastCleanTaskRelation = 0;


    public static String getTaskName(String serialnumber) {
        if (0 == lastCleanTaskRelation) {
            lastCleanTaskRelation = new Date().getTime();
        } else {
            if (new Date().getTime() - lastCleanTaskRelation > 60 * 60 * 1000) {
                lastCleanTaskRelation = new Date().getTime();
                clearTaskName();
            }
        }

        if (!vsdTaskRelation.containsKey(serialnumber)) {
            VsdTaskRelation vsdTaskRelation = vsdTaskRelationService.getOne(new QueryWrapper<VsdTaskRelation>().eq("serialnumber", serialnumber));
            if (vsdTaskRelation != null) {
                VsdTaskUtil.vsdTaskRelation.put(serialnumber, vsdTaskRelation.getTaskName());
            } else {
                VsdTaskUtil.vsdTaskRelation.put(serialnumber, "监控点不存在");
            }
        }
        return vsdTaskRelation.get(serialnumber);
    }

    public static void clearTaskName() {
        vsdTaskRelation = new HashMap<>();
    }
}
