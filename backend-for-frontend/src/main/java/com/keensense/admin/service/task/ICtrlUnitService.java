package com.keensense.admin.service.task;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.admin.entity.task.Camera;
import com.keensense.admin.entity.task.CtrlUnit;

import java.util.List;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 21:15:12
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

    List<CtrlUnit> findUnitByCameraRegion(String id, List<Camera> cameras, String level);

    List<CtrlUnit> findUnitByNameParentId(String parentId, String name);

    List<CtrlUnit> findUnitByName(String name);
}

