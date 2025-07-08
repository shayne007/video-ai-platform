package com.keensense.dataconvert.biz.dao;

import com.keensense.dataconvert.biz.entity.VsdTask;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName：VsdTaskMapper
 * @Description： <p> VsdTaskMapper 操作任务 </p>
 * @Author： - Jason
 * @CreatTime：2019/8/15 - 16:20
 * @Modify By：
 * @ModifyTime： 2019/8/15
 * @Modify marker：
 * @version V1.0
*/
@Mapper
public interface VsdTaskMapper {

    /**
     * deleteByPrimaryKey
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * insert
     * @param record
     * @return
     */
    int insert(VsdTask record);

    /**
     * insertSelective
     * @param record
     * @return
     */
    int insertSelective(VsdTask record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return
     */
    VsdTask selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(VsdTask record);

    /**
     * updateByPrimaryKeyWithBLOBs
     * @param record
     * @return
     */
    int updateByPrimaryKeyWithBLOBs(VsdTask record);

    /**
     * updateByPrimaryKey
     * @param record
     * @return
     */
    int updateByPrimaryKey(VsdTask record);

    /**
     * 查询所有的任务列表
     * @return
     */
    List<VsdTask> selectAllList();
}