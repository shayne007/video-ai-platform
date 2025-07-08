package com.keensense.dataconvert.biz.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.dao
 * @Description： <p> AppSysMapper </p>
 * @Author： - Jason
 * @CreatTime：2019/8/26 - 11:35
 * @Modify By：
 * @ModifyTime： 2019/8/26
 * @Modify marker：
 */
@Mapper
public interface AppSysMapper {

    /**
     * selectAllDataTables 查询数据库里面对应的表 list
     * @param tableNamePrefix
     * @return
     */
    List<String> selectAllDataTables(@Param("tablePrefix") String tableNamePrefix);
}
