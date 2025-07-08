
package com.keensense.admin.service.sys.impl;

import com.keensense.admin.service.sys.IShiroService;
import org.springframework.stereotype.Service;

@Service
public class ShiroServiceImpl implements IShiroService {
//    @Autowired
//    private SysMenuDao sysMenuDao;
//    @Autowired
//    private SysUserDao sysUserDao;
//    @Autowired
//    private SysUserTokenDao sysUserTokenDao;
//
//    @Override
//    public Set<String> getUserPermissions(long userId) {
//        List<String> permsList;
//
//        //系统管理员，拥有最高权限
//        if(userId == Constant.SUPER_ADMIN){
//            List<SysModule> menuList = sysMenuDao.selectList(null);
//            permsList = new ArrayList<>(menuList.size());
//            for(SysModule menu : menuList){
//                permsList.add(menu.getPerms());
//            }
//        }else{
//            permsList = sysUserDao.queryAllPerms(userId);
//        }
//        //用户权限列表
//        Set<String> permsSet = new HashSet<>();
//        for(String perms : permsList){
//            if(StringUtils.isBlank(perms)){
//                continue;
//            }
//            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
//        }
//        return permsSet;
//    }

//    @Override
//    public SysUserTokenEntity queryByToken(String token) {
//        return sysUserTokenDao.queryByToken(token);
//    }
//
//    @Override
//    public SysUserEntity queryUser(Long userId) {
//        return sysUserDao.selectById(userId);
//    }
}
