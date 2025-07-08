package com.keensense.admin.service.task;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.admin.entity.task.ObjextTrack;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 21:15:12
 */
public interface IObjextTrackService extends IService<ObjextTrack> {
    /**
     * 根据 serialnumber 获取加入关联的目标库数据
     *
     * @param serialnumber
     * @return
     */
    Set<String> queryObjextTrackResultidsBySerialnumber(String serialnumber);

    List<Map<String, Object>> queryObjextTrackResultListBySerialnumber(String serialnumber);

    List<ObjextTrack> queryObjextTrackBySerialnumber(String serialnumber, Integer searchStatus);
}

