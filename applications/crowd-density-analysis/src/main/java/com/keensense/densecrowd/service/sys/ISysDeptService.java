package com.keensense.densecrowd.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.densecrowd.entity.sys.SysDept;
import com.keensense.densecrowd.request.SysDeptRequest;

import java.util.List;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 21:15:12
 */
public interface ISysDeptService extends IService<SysDept> {

    /**
     *
     */
    List<SysDept> findAllChildren(Long deptId, SysDeptRequest dept);

    /**
     * 查询所有子部门
     */
    List<SysDept> findChildren(Long level, Long parentId);

    List<SysDept> findEnableChildren(Long level, Long parentId);

    /**
     * 删除部门By id
     */
    void deleteDept(Long id);
}

