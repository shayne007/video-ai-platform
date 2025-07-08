package com.keensense.admin.controller.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.admin.base.BaseController;
import com.keensense.admin.constants.CommonConstants;
import com.keensense.admin.entity.sys.SysDept;
import com.keensense.admin.request.SysDeptRequest;
import com.keensense.admin.service.sys.ISysDeptService;
import com.keensense.admin.util.EntityObjectConverter;
import com.keensense.admin.util.RandomUtils;
import com.keensense.admin.util.StringUtils;
import com.keensense.common.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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

/**
 * 部门控制器
 */
@Slf4j
@Api(tags = "组织机构管理")
@RestController
@RequestMapping("/dept")
public class DeptController extends BaseController {

    @Resource
    private ISysDeptService deptService;

    /**
     * 新增组织机构
     * 先判断新增的部门是在上级基础上还是根结点
     * 如果是在上级基础上新增，则先查出上级部门，先判断上级部门是否为叶子节点，如果是叶子节点，则更改上级状态为非叶子节点，设置部门相关参数
     */
    @ApiOperation(value = "新增组织机构")
    @PostMapping(value = "/saveDept")
    public R saveDept(@RequestBody SysDeptRequest dept) {
        dept.setDeptNumber(String.valueOf(System.currentTimeMillis()));
        Long deptId = Long.valueOf(RandomUtils.get8RandomValiteCode(8));
        try {
            Long parentId = dept.getParentId();
            SysDept parentDept = null;
            String deptName = dept.getDeptName();
            if (parentId != null) {
                parentDept = deptService.getById(parentId);
            }
            if (parentDept != null) {// 有上级
                if (parentDept.getIsLeaf() == 1) {// 父级是叶子节点
                    parentDept.setIsLeaf(0L);
                    deptService.updateById(parentDept);// 更新父级节点状态
                }
                dept.setDeptLevel(parentDept.getDeptLevel() + 1);
                if (StringUtils.isNotEmptyString(dept.getDeptNumber())) {
                    dept.setLongNumber(parentDept.getLongNumber() + CommonConstants.SPLIT_CHAR + dept.getDeptNumber());
                }
                dept.setDisplayName(parentDept.getDisplayName() + CommonConstants.SPLIT_CHAR + dept.getDeptName());
                dept.setParentId(parentDept.getDeptId());

            } else {// 无上级
                dept.setDeptLevel(1L);
                if (StringUtils.isNotEmptyString(dept.getDeptNumber())) {
                    dept.setLongNumber(dept.getDeptNumber());
                }
                dept.setDisplayName(dept.getDeptName());
            }
            dept.setIsLeaf(1L);// 新增的模块都为叶子节点
            dept.setDeptId(deptId);
            //判断部门名称是否重名
            if (StringUtils.isNotEmptyString(deptName)) {
                SysDept hasSysDept = deptService.getOne(new QueryWrapper<SysDept>().eq("dept_name", deptName));
                if (hasSysDept == null) {
                    deptService.save(EntityObjectConverter.getObject(dept, SysDept.class));
                } else {
                    return R.error("部门名称已存在，请重新输入");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.error(e.getMessage());
        }
        return R.ok();
    }

    @ApiOperation(value = "编辑组织机构")
    @PostMapping(value = "/editDept")
    public R editDept(@RequestBody SysDeptRequest sysDept) {
        try {
            Long deptLevel = sysDept.getDeptLevel();
            if (deptLevel == null) {
                return R.error("部门等级不能为空");
            }
            SysDept sysDeptSource = deptService.getById(sysDept.getDeptId());
            SysDept hasSysDept = deptService.getOne(new QueryWrapper<SysDept>().eq("dept_name", sysDept.getDeptName()).notIn("dept_id", sysDept.getDeptId()));
            if (hasSysDept == null) {
                List<SysDept> mList = this.deptService.findAllChildren(sysDept.getDeptId(), sysDept);// 查出所有子部门
                if (mList != null && !mList.isEmpty()) {
                    for (SysDept child : mList) {
                        child.setDisplayName(StringUtils.replaceStrWithLevel(child.getDisplayName(), CommonConstants.SPLIT_CHAR, sysDept.getDeptLevel().intValue(),
                                sysDept.getDeptName()));
                        child.setLongNumber(StringUtils.replaceStrWithLevel(child.getLongNumber(), CommonConstants.SPLIT_CHAR, sysDept.getDeptLevel().intValue(),
                                sysDept.getDeptNumber()));
                        deptService.updateById(child);
                    }
                }
                sysDeptSource.setDisplayName(StringUtils.replaceStrWithLevel(sysDeptSource.getDisplayName(), CommonConstants.SPLIT_CHAR,
                        sysDept.getDeptLevel().intValue(), sysDept.getDeptName()));
                sysDeptSource.setDeptName(sysDept.getDeptName());
                sysDeptSource.setDescription(sysDept.getDescription());
                sysDeptSource.setTel(sysDept.getTel());
                sysDeptSource.setDeptState(sysDept.getDeptState());
                deptService.updateById(sysDeptSource);
            } else {
                return R.error("部门名称已存在，请重新输入");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.error(e.getMessage());
        }
        return R.ok();
    }

    @ApiOperation(value = "查询组织机构")
    @PostMapping(value = "/getTree")
    @ApiImplicitParam(name = "parentId", value = "父节点Id")
    public R moduleTree(String parentId) {
        List<Map<String, Object>> listData = new ArrayList<>();
        if (StringUtils.isEmptyString(parentId)) {
            List<SysDept> mList = this.deptService.findChildren(Long.parseLong("1"), null);
            listData = bulidTree(mList);
        } else {
            SysDept parentDept = this.deptService.getById(Long.parseLong(parentId));
            List<SysDept> mList = this.deptService.findChildren(parentDept.getDeptLevel(), Long.parseLong(parentId));
            listData = bulidTree(mList);
        }
        return R.ok().put("listData", listData);
    }


    @ApiOperation(value = "查看启用的部门列表")
    @PostMapping(value = "/getEnableDept")
    @ApiImplicitParam(name = "parentId", value = "父节点Id")
    public R getEnableDept(String parentId) {
        List<Map<String, Object>> listData = new ArrayList<>();
        if (StringUtils.isEmptyString(parentId)) {
            List<SysDept> mList = this.deptService.findEnableChildren(Long.parseLong("1"), null);
            listData = bulidTree(mList);
        } else {
            SysDept parentDept = this.deptService.getById(Long.parseLong(parentId));
            List<SysDept> mList = this.deptService.findEnableChildren(parentDept.getDeptLevel(), Long.parseLong(parentId));
            listData = bulidTree(mList);
        }
        return R.ok().put("listData", listData);
    }

    /**
     * 构建树
     *
     * @param tlist
     */
    private List<Map<String, Object>> bulidTree(List<SysDept> tlist) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SysDept dept : tlist) {
            Map<String, Object> treeNode = new HashMap<>();
            treeNode.put("deptId", dept.getDeptId());
            treeNode.put("deptName", dept.getDeptName());
            treeNode.put("description", dept.getDescription());
            treeNode.put("parentId", dept.getParentId());
            treeNode.put("deptNumber", dept.getDeptNumber());
            treeNode.put("deptState", dept.getDeptState());
            treeNode.put("tel", dept.getTel());
            treeNode.put("deptLevel", dept.getDeptLevel());
            List<SysDept> hasSysDept = deptService.getBaseMapper().selectList(new QueryWrapper<SysDept>().eq("parent_id", dept.getDeptId()));
            if (hasSysDept != null && !hasSysDept.isEmpty()) {
                treeNode.put("state", "closed");
            } else {
                treeNode.put("state", "open");
            }
            list.add(treeNode);
        }
        return list;
    }

    @ApiOperation(value = "删除组织机构")
    @PostMapping(value = "/delDept")
    @ApiImplicitParam(name = "id", value = "组织机构id")
    public R del(String id) {
        try {
            if (StringUtils.isEmptyString(id)) {
                return R.error("id不能为空");
            }
            deptService.deleteDept(Long.parseLong(id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.error(CommonConstants.DELETE_FAILURE);
        }
        return R.ok();
    }


}
