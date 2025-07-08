package com.keensense.densecrowd.service.task.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.densecrowd.entity.task.CtrlUnit;
import com.keensense.densecrowd.mapper.task.CtrlUnitMapper;
import com.keensense.densecrowd.service.task.ICtrlUnitService;
import com.keensense.densecrowd.util.CommonConstants;
import com.keensense.densecrowd.util.StringUtils;
import com.keensense.densecrowd.vo.CameraVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: shitao
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 9:56 2019/9/26
 * @Version v0.1
 */
@Slf4j
@Service("ctrlUnitService")
public class CtrlUnitServiceImpl extends ServiceImpl<CtrlUnitMapper, CtrlUnit> implements ICtrlUnitService {

    @Override
    public List<CtrlUnit> queryAreaChildrenByParentId(String areaId) {
        return baseMapper.selectList(new QueryWrapper<CtrlUnit>().eq("unit_parent_id", areaId));
    }

    @Override
    public List<CtrlUnit> findUnitChildren(Integer level, String unitNumber) {
        if (unitNumber != null) {
            return baseMapper.selectList(new QueryWrapper<CtrlUnit>().eq("unit_parent_id", unitNumber));
        } else {
            return baseMapper.selectList(new QueryWrapper<CtrlUnit>().eq("unit_level", Long.valueOf(level)));
        }
    }

    @Override
    public void deleteCtrlUnit(String id) {
        CtrlUnit m = baseMapper.selectById(id);
        if (1 == m.getIsLeaf()) {//叶子节点
            List<CtrlUnit> ctrlUnitList = baseMapper.selectList(new QueryWrapper<CtrlUnit>().eq("Long_number", StringUtils.getParentLongNumber(m.getLongNumber(), CommonConstants.MODULE_SPLIT)));
            if (ctrlUnitList != null && !ctrlUnitList.isEmpty()) {
                CtrlUnit parent = ctrlUnitList.get(0);
                if (parent != null) {
                    //父类所有下级，排除与本辖区相同等级又非本辖区
                    List<CtrlUnit> ctrlUnits = baseMapper.selectList(new QueryWrapper<CtrlUnit>().eq("unit_level", m.getUnitLevel()).like("long_number", parent.getLongNumber()).notIn("id", id));
                    if (ctrlUnits == null || ctrlUnits.isEmpty()) {//没有其他叶子节点
                        parent.setIsLeaf(1L);
                        baseMapper.updateById(parent);//更新leaf状态
                    }
                }
            }
            baseMapper.deleteById(Long.valueOf(id));
        } else {//非叶子节点
            List<CtrlUnit> allCtrl = baseMapper.selectList(new QueryWrapper<CtrlUnit>().like("long_number", m.getLongNumber()));
            List<Long> ids = new ArrayList<>();
            for (CtrlUnit ctrlUnit : allCtrl) {
                ids.add(ctrlUnit.getId());
            }
            baseMapper.deleteBatchIds(ids);
        }
    }

    @Override
    public CtrlUnit selectByUnitIdentity(String unitIdentity) {
        return baseMapper.selectOne(new QueryWrapper<CtrlUnit>().eq("unit_identity", unitIdentity));
    }

    @Override
    public List<CtrlUnit> queryTopNode(Long level) {
        return baseMapper.selectList(new QueryWrapper<CtrlUnit>().eq("unit_level", level));
    }

    @Override
    public List<CtrlUnit> findUnitByCameraRegion(String id, List<CameraVo> cameras, String level) {
        List<CtrlUnit> mList = new ArrayList<>();
        CtrlUnit ctrlUnitFirstRegion = baseMapper.selectOne(new QueryWrapper<CtrlUnit>().eq("unit_number", id));
        if (null == ctrlUnitFirstRegion && id != null) {
            return mList;
        }
        for (CameraVo camera : cameras) {
            CtrlUnit ctrlUnit = baseMapper.selectOne(new QueryWrapper<CtrlUnit>().eq("unit_number",
                    camera.getRegion()));
            if (ctrlUnit != null && ctrlUnit.getLongNumber() != null) {
                String[] area = ctrlUnit.getLongNumber().split("!");
                for (int i = 0; i < area.length; i++) {
                    CtrlUnit ctrlUnit1 = null;
                    //监控点列表第一次搜索点位 只查level为1的区域
                    if (id == null) {
                        if (StringUtils.isNotEmptyString(level)) {
                            ctrlUnit1 = baseMapper.selectOne(new QueryWrapper<CtrlUnit>().eq("unit_number", area[i])
                                    .eq("unit_level", Integer.valueOf(level)));
                        } else {
                            ctrlUnit1 = baseMapper.selectOne(new QueryWrapper<CtrlUnit>().eq("unit_number", area[i]));
                        }
                        if (ctrlUnit1 != null) {
                            mList.add(ctrlUnit1);
                        }
                    } else {
                        if (ctrlUnitFirstRegion.getLongNumber().split("!")[0].equals(area[0])) {
                            CtrlUnit ctrlUnit2 = null;
                            if (!id.equals(area[i])) {
                                ctrlUnit2 = baseMapper.selectOne(new QueryWrapper<CtrlUnit>()
                                        .eq("unit_parent_id", id).eq("unit_number", area[i]));
                            }
                            if (null != ctrlUnit2) {
                                mList.add(ctrlUnit2);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return removal(mList);
    }

    @Override
    public List<CtrlUnit> findUnitByNameParentId(String parentId, String name) {
        List<CtrlUnit> cList = baseMapper.selectList(new QueryWrapper<CtrlUnit>().like("display_name", name));
        CtrlUnit ctrlUnitParent = baseMapper.selectOne(new QueryWrapper<CtrlUnit>().eq("id", parentId));
        List<CtrlUnit> mList = new ArrayList<>();
        for (CtrlUnit ctrlUnit : cList) {
            String[] area = ctrlUnit.getLongNumber().split("!");
            for (int i = 0; i < area.length; i++) {
                CtrlUnit ctrlUnit1 = baseMapper.selectOne(new QueryWrapper<CtrlUnit>().eq("unit_number", area[i]));
                if (ctrlUnitParent.getUnitIdentity().equals(ctrlUnit1.getUnitParentId())) {
                    mList.add(ctrlUnit1);
                }
            }
        }
        return removal(mList);
    }

    @Override
    public List<CtrlUnit> findUnitByName(String name) {
        List<CtrlUnit> cList = baseMapper.selectList(new QueryWrapper<CtrlUnit>().like("display_name", name));
        Set<Long> sets = new HashSet<>();
        List<CtrlUnit> mList = new ArrayList<>();
        for (CtrlUnit ctrlUnit : cList) {
            String[] area = ctrlUnit.getLongNumber().split("!");
            for (int i = 0; i < area.length; i++) {
                CtrlUnit ctrlUnit1 = baseMapper.selectOne(new QueryWrapper<CtrlUnit>().eq("unit_number", area[i]));
                if (ctrlUnit1 != null && sets.add(ctrlUnit1.getId()) && ctrlUnit1.getUnitParentId() == null) {
                    sets.add(ctrlUnit1.getId());
                    mList.add(ctrlUnit1);
                    break;
                }
            }
        }
        return mList;
    }


    public List<CtrlUnit> removal(List<CtrlUnit> mList) {
        List<CtrlUnit> aList = new ArrayList<>();
        Set<Long> sets = new HashSet<>();
        for (CtrlUnit ctrlUnit : mList) {
            //如果可以加进去，说明没有重复
            if (sets.add(ctrlUnit.getId())) {
                sets.add(ctrlUnit.getId());
                aList.add(ctrlUnit);
            }
        }
        return aList;
    }
}
