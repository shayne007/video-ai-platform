package com.keensense.densecrowd.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.densecrowd.entity.sys.SysModule;

import java.util.List;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 21:15:12
 */
public interface ISysModuleService extends IService<SysModule> {
    /**
     * 获取用户菜单列表
     */
    List<SysModule> getUserMenuList(Long roleId);

    List<SysModule> selectMenuByRoleId(Long roleId);

    /**
     * 获取子节点数据
     *
     * @param parentId
     * @return
     */
    List<SysModule> queryListParentId(Long parentId, Integer state) ;

    void deleteModule(Long moduleId);

    /**
     * 根据对应角色的菜单列表
     * @param roleId
     * @return
     */
    List<SysModule> getMenuListByRoleId(Long roleId);

    /**
     * 根据模块名返回菜单列表
     * @param moduleName
     * @return
     */
    List<SysModule> queryListModuleName(String moduleName);

    List<SysModule> selectButtonByRoleId(Long roleId);

    List<SysModule> selectMenuLsitByLevel(Long level);
}

