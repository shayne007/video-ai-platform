package com.keensense.densecrowd.service.task;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.densecrowd.entity.task.Camera;
import com.keensense.densecrowd.entity.task.CtrlUnit;
import com.keensense.densecrowd.vo.CameraVo;

import java.util.List;

/**
 * @Author: shitao
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 9:51 2019/9/26
 * @Version v0.1
 */
public interface ICtrlUnitService extends IService<CtrlUnit> {

    List<CtrlUnit> queryTopNode(Long level);

    List<CtrlUnit> queryAreaChildrenByParentId(String areaId);

    /**
     * 查询所有子辖区
     */
    List<CtrlUnit> findUnitChildren(Integer level,String unitNumber);

    /**
     * 删除辖区
     */
    void deleteCtrlUnit(String id);

    CtrlUnit selectByUnitIdentity(String unitIdentity);

    List<CtrlUnit> findUnitByCameraRegion(String id, List<CameraVo> cameras, String level);

    List<CtrlUnit> findUnitByNameParentId(String parentId, String name);

    List<CtrlUnit> findUnitByName(String name);
}
