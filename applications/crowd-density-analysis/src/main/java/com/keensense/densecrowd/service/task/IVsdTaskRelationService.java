package com.keensense.densecrowd.service.task;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.densecrowd.entity.task.VsdTaskRelation;
import com.keensense.densecrowd.vo.AlarmTaskQueryRequest;

import java.util.List;

/**
 * @Author: shitao
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 10:55 2019/9/25
 * @Version v0.1
 */
public interface IVsdTaskRelationService extends IService<VsdTaskRelation> {
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
     * 添加TaskRelation信息
     *
     * @param serialnumber
     * @param fileId
     * @param fromType
     * @param c1
     * @return
     */
    boolean insertTaskRelation(String serialnumber, String fileId, Integer fromType, String c1);

    /**
     * 根据设备列表查询任务列表
     *
     * @param deviceIds
     * @return
     */
    List<VsdTaskRelation> queryListByDeviceIds(String[] deviceIds);

    /**
     * 根据条件查询任务列表
     *
     * @return
     */
    IPage<VsdTaskRelation> queryListByDeviceIds(AlarmTaskQueryRequest alarmTaskQueryRequest);

    /**
     * 统计实时任务数量
     *
     * @param isvalid 启动状态
     * @return
     */
    int countRealTask(long isvalid);

    /**
     * 通过serialnumber查询TaskRelation信息
     *
     * @param serialnumber
     * @return
     */
    VsdTaskRelation queryVsdTaskRelationBySerialnumber(String serialnumber);


}
