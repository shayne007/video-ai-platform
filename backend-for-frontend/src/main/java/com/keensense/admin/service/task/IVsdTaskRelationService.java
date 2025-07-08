package com.keensense.admin.service.task;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.admin.entity.task.VsdTaskRelation;

import java.util.List;
import java.util.Map;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 21:15:11
 */
public interface IVsdTaskRelationService extends IService<VsdTaskRelation> {

    /**
     * 通过serialnumber查询TaskRelation信息
     *
     * @param serialnumber
     * @return
     */
    VsdTaskRelation queryVsdTaskRelationBySerialnumber(String serialnumber);

    /**
     * 通过serialnumber查询TaskRelation信息
     *
     * @param cameraFileId
     * @return
     */
    VsdTaskRelation queryVsdTaskRelationByCameraFileId(String cameraFileId, String taskUserId);


    /**
     * 生成一个任务列表中不存在的新的serialnumber
     *
     * @return
     */
    String getSerialnumber();

    /**
     * 查询任务信息
     *
     * @param requestParams
     * @return
     */
    IPage<VsdTaskRelation> queryVsdTaskAllService(Map<String, Object> requestParams);

    /**
     * 统计实时任务数量
     *
     * @param isvalid 启动状态
     * @return
     */
    int countRealTask(long isvalid);

    /**
     * 查询有检测标志的任务
     *
     * @param overlineType
     * @return
     */
    String selectOverlineTypeSerialNumber(Integer overlineType);

    /**
     * 通过点位获取任务列表
     *
     * @param cameras
     * @return
     */
    List<VsdTaskRelation> queryVsdTaskRelationByCameraId(String cameras);
}

