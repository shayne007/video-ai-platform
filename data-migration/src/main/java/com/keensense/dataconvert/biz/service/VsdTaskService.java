package com.keensense.dataconvert.biz.service;

import com.keensense.dataconvert.biz.entity.VsdTask;

import java.util.List;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.service
 * @Description： <p> VsdTaskService </p>
 * @Author： - Jason
 * @CreatTime：2019/8/15 - 16:24
 * @Modify By：
 * @ModifyTime： 2019/8/15
 * @Modify marker：
 */
public interface VsdTaskService {

    /**
     * 查询所有的任务
     * @return
     */
    List<VsdTask> selectAllList();
}
