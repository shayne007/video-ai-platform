package com.keensense.task.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.common.config.SpringContext;
import com.keensense.task.entity.VsdTask;
import com.keensense.task.mapper.VsdTaskMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: wujw
 * @CreateDate: 2019/5/17 14:22
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Deprecated
public class VsdTaskCache {

    private VsdTaskCache(){}

    private static Map<String, VsdTask> vsdTaskInfoCache = new HashMap<>();

    private static VsdTaskMapper vsdTaskMapper = SpringContext.getBean(VsdTaskMapper.class);

    /***
     * @description: 根据serialnumber查询vsdTask对象
     * @param serialnumber 任务号
     * @return: com.keensense.task.entity.VsdTask
     */
    public static VsdTask getVsdTaskBySerialnumber(String serialnumber) {
        VsdTask vsdTask = vsdTaskInfoCache.get(serialnumber);
        if (vsdTask == null) {
            vsdTask = vsdTaskMapper.selectOne(new QueryWrapper<VsdTask>().eq("serialnumber", serialnumber));
            if (vsdTask != null) {
                vsdTaskInfoCache.put(serialnumber, vsdTask);
            }
        }
        return vsdTask;
    }

    public static void setVsdTaskCache(Map<String, VsdTask> vsdTaskCache) {
        VsdTaskCache.vsdTaskInfoCache = vsdTaskCache;
    }
}
