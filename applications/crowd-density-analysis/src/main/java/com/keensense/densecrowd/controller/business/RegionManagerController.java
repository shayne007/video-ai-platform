package com.keensense.densecrowd.controller.business;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.common.util.R;
import com.keensense.densecrowd.base.BaseController;
import com.keensense.densecrowd.entity.task.CtrlUnit;
import com.keensense.densecrowd.request.CtrlUnitRequest;
import com.keensense.densecrowd.service.task.ICtrlUnitService;
import com.keensense.densecrowd.util.CommonConstants;
import com.keensense.densecrowd.util.EhcacheUtils;
import com.keensense.densecrowd.util.EntityObjectConverter;
import com.keensense.densecrowd.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 部门控制器
 */
@Slf4j
@Api(tags = "行政区域管理")
@RestController
@RequestMapping("/region")
public class RegionManagerController extends BaseController {

    @Resource
    private ICtrlUnitService ctrlUnitService;

    /**
     * 新增辖区
     * 先判断新增的部门是在上级基础上还是根结点
     * 如果是在上级基础上新增，则先查出上级部门，先判断上级部门是否为叶子节点，如果是叶子节点，则更改上级状态为非叶子节点，设置部门相关参数
     */
    @ApiOperation(value = "新增行政区域")
    @PostMapping(value = "/saveRegion")
    public R saveRegion(@RequestBody CtrlUnitRequest ctrlUnit) {
        String parentId = ctrlUnit.getParentId();
        Long ctrlId = System.currentTimeMillis();
        String unitNumber = String.valueOf(new Random().nextInt(100000000));
        CtrlUnit parentCtrlUnit = null;
        try {
            while (true) {
                List<CtrlUnit> ctrlUnits = ctrlUnitService.getBaseMapper().selectList(new QueryWrapper<CtrlUnit>().eq("unit_number", unitNumber));
                if (ctrlUnits != null && !ctrlUnits.isEmpty()) {
                    unitNumber = String.valueOf(new Random().nextInt(100000000));
                } else {
                    break;
                }
            }

            if (StringUtils.isNotEmptyString(parentId)) {
                parentCtrlUnit = ctrlUnitService.getById(parentId);
            }
            if (parentCtrlUnit != null) {// 有上级
                if (parentCtrlUnit.getIsLeaf() == 1) {// 父级是叶子节点
                    parentCtrlUnit.setIsLeaf(0L);
                    ctrlUnitService.updateById(parentCtrlUnit);// 更新父级节点状态
                }
                ctrlUnit.setUnitLevel(parentCtrlUnit.getUnitLevel() + 1);
                if (StringUtils.isNotEmptyString(unitNumber)) {
                    ctrlUnit.setLongNumber(parentCtrlUnit.getLongNumber() + CommonConstants.SPLIT_CHAR + unitNumber);
                }
                ctrlUnit.setDisplayName(ctrlUnit.getUnitName());
                ctrlUnit.setUnitParentId(parentCtrlUnit.getUnitIdentity());
            } else {// 无上级
                ctrlUnit.setUnitLevel(1L);
                ctrlUnit.setLongNumber(unitNumber);
                ctrlUnit.setDisplayName(ctrlUnit.getUnitName());
            }
            ctrlUnit.setUnitIdentity(unitNumber);
            ctrlUnit.setIsLeaf(0L);// 区域节点都为父节点
            ctrlUnit.setId(String.valueOf(ctrlId));
            ctrlUnit.setUnitState(1L);
            ctrlUnit.setUnitNumber(unitNumber);
            //判断区域名称是否重名
            if (StringUtils.isNotEmptyString(ctrlUnit.getUnitName())) {
                CtrlUnit hasCtrlUnit = ctrlUnitService.getOne(new QueryWrapper<CtrlUnit>().eq("unit_name", ctrlUnit.getUnitName()));
                if (hasCtrlUnit == null) {
                    ctrlUnitService.save(EntityObjectConverter.getObject(ctrlUnit, CtrlUnit.class));
                    // 清理缓存
                    EhcacheUtils.removeItem("CTRL_UNIT");
                    EhcacheUtils.removeItem("CTRL_UNIT_1");
                } else {
                    return R.error("区域名称已存在，请重新输入");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.error(e.getMessage());
        }
        return R.ok();
    }

    @ApiOperation(value = "查询行政区域")
    @PostMapping(value = "/getRegionTree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = ""),
            @ApiImplicitParam(name = "name", value = "")
    })
    public R getCtrlTree(String parentId, String name) {
        List<Map<String, Object>> listData = null;
        if (StringUtils.isNotEmptyString(parentId) && StringUtils.isNotEmptyString(name)) {
            List<CtrlUnit> cList = ctrlUnitService.findUnitByNameParentId(parentId, name);
            listData = bulidCtrlTree(cList);
        } else if (StringUtils.isNotEmptyString(name) && StringUtils.isEmptyString(parentId)) {
            List<CtrlUnit> cList = ctrlUnitService.findUnitByName(name);
            listData = bulidCtrlTree(cList);
        } else if (StringUtils.isNotEmptyString(parentId)) {
            CtrlUnit parentCtrlUnit = ctrlUnitService.getById(parentId);
            Integer unitLevel = new Integer(parentCtrlUnit.getUnitLevel() + "");
            List<CtrlUnit> mList = ctrlUnitService.findUnitChildren(unitLevel, parentCtrlUnit.getUnitNumber());
            listData = bulidCtrlTree(mList);
        } else {
            List<CtrlUnit> mList = ctrlUnitService.findUnitChildren(1, null);
            listData = bulidCtrlTree(mList);
        }
        return R.ok().put("list", listData);
    }

    private List<Map<String, Object>> bulidCtrlTree(List<CtrlUnit> tlist) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CtrlUnit ctrl : tlist) {
            Map<String, Object> treeNode = new HashMap<>();
            treeNode.put("id", ctrl.getId());
            treeNode.put("unitName", ctrl.getUnitName());
            treeNode.put("unitDescription", ctrl.getUnitDescription());
            treeNode.put("unitNumber", ctrl.getUnitNumber());
            treeNode.put("unitState", ctrl.getUnitState());
            treeNode.put("unitLevel", ctrl.getUnitLevel());
            treeNode.put("longNumber", ctrl.getLongNumber());
            List<CtrlUnit> hasCtrlUnit = ctrlUnitService.getBaseMapper().selectList(new QueryWrapper<CtrlUnit>().eq("unit_parent_id", ctrl.getUnitNumber()));
            if (hasCtrlUnit != null && !hasCtrlUnit.isEmpty()) {
                treeNode.put("state", "closed");
            } else {
                treeNode.put("state", "open");
            }
            list.add(treeNode);
        }
        return list;
    }

    @ApiOperation(value = "编辑行政区域")
    @PostMapping(value = "/editRegion")
    public R editCtrl(@RequestBody CtrlUnitRequest ctrlUnit) {
        try {
            CtrlUnit ctrlUnitSource = ctrlUnitService.getById(String.valueOf(ctrlUnit.getId()));
            ctrlUnitSource.setDisplayName(ctrlUnit.getUnitName());
            ctrlUnitSource.setUnitName(ctrlUnit.getUnitName());
            ctrlUnitSource.setUnitDescription(ctrlUnit.getUnitDescription());
            ctrlUnitService.updateById(ctrlUnitSource);
            // 清理缓存
            EhcacheUtils.removeItem("CTRL_UNIT");
            EhcacheUtils.removeItem("CTRL_UNIT_1");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return R.error(e.getMessage());
        }
        return R.ok();
    }

    @ApiOperation(value = "删除行政区域")
    @PostMapping(value = "/delRegion")
    @ApiImplicitParam(name = "id", value = "区域id")
    public R delCtrl(String id) {
        if (StringUtils.isEmptyString(id)) {
            return R.error("id不能为空");
        }
        ctrlUnitService.deleteCtrlUnit(id);
        return R.ok();
    }

}
