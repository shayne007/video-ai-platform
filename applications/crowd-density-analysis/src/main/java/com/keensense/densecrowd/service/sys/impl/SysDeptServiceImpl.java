package com.keensense.densecrowd.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.densecrowd.entity.sys.SysDept;
import com.keensense.densecrowd.mapper.sys.SysDeptMapper;
import com.keensense.densecrowd.request.SysDeptRequest;
import com.keensense.densecrowd.service.sys.ISysDeptService;
import com.keensense.densecrowd.util.CommonConstants;
import com.keensense.densecrowd.util.EntityObjectConverter;
import com.keensense.densecrowd.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service("sysDeptService")
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {


    @Override
    public List<SysDept> findAllChildren(Long deptId, SysDeptRequest dept) {
        List<SysDept> deptList = baseMapper.selectList(new QueryWrapper<>());
        fomateDept(deptList, dept);
        List<SysDept> result = dept.getChildren();
        if (result == null) {
            result = new ArrayList<>();
        }
        return result;
    }

    private static void fomateDept(List<SysDept> tmp, SysDeptRequest dept) {
        for (SysDept row : tmp) {
            if (row.getParentId() != null && row.getParentId().equals(dept.getDeptId())) {
                List<SysDept> list = dept.getChildren();
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(row);
                dept.setChildren(list);
                fomateDept(tmp, EntityObjectConverter.getObject(row, SysDeptRequest.class));
            }

        }
    }

    @Override
    public List<SysDept> findChildren(Long level, Long parentId) {
        if (parentId != null) {
            return baseMapper.selectList(new QueryWrapper<SysDept>().eq("parent_id", parentId).eq("dept_level", level + 1));
        } else {
            return baseMapper.selectList(new QueryWrapper<SysDept>().eq("dept_level", level));
        }
    }

    @Override
    public List<SysDept> findEnableChildren(Long level, Long parentId) {
        if (parentId != null) {
            return baseMapper.selectList(new QueryWrapper<SysDept>().eq("parent_id", parentId).eq("dept_level", level + 1).eq("dept_state", 1));
        } else {
            return baseMapper.selectList(new QueryWrapper<SysDept>().eq("dept_level", level).eq("dept_state", 1));
        }
    }

    @Override
    public void deleteDept(Long id) {
        SysDept m = baseMapper.selectById(id);
        if (m.getIsLeaf() == 1) {//叶子节点
            List<SysDept> deptList = baseMapper.selectList(new QueryWrapper<SysDept>().eq("Long_number", StringUtils.getParentLongNumber(m.getLongNumber(), CommonConstants.MODULE_SPLIT)));
            if (deptList != null && !deptList.isEmpty()) {
                SysDept parent = deptList.get(0);
                if (parent != null) {
                    List<SysDept> modules = baseMapper.selectList(new QueryWrapper<SysDept>().eq("parent_id", parent.getDeptId()).eq("dept_level", m.getDeptLevel()).notIn("dept_id", id));
                    if (modules == null || modules.isEmpty()) {//没有其他叶子节点
                        parent.setIsLeaf(Long.valueOf(1));
                        baseMapper.updateById(parent);//更新leaf状态
                    }
                }
            }
            baseMapper.deleteById(id);
        } else {//非叶子节点
            List<SysDept> allDepts = baseMapper.selectList(new QueryWrapper<SysDept>().like("Long_number", m.getLongNumber()));
            List<Long> ids = new ArrayList<>();
            for (SysDept sysDept : allDepts) {
                ids.add(sysDept.getDeptId());
            }
            baseMapper.deleteBatchIds(ids);
        }

    }
}
