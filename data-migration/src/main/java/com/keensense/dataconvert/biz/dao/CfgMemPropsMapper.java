package com.keensense.dataconvert.biz.dao;

import com.keensense.dataconvert.biz.entity.CfgMemProps;
import com.keensense.dataconvert.biz.entity.CfgMemPropsKey;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName：CfgMemPropsMapper
 * @Description： <p> CfgMemPropsMapper  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/25 - 9:18
 * @Modify By：
 * @ModifyTime： 2019/7/25
 * @Modify marker：
 * @version V1.0
*/
@Mapper
public interface CfgMemPropsMapper {

    /**
     * deleteByPrimaryKey
     * @param key
     * @return
     */
    int deleteByPrimaryKey(CfgMemPropsKey key);

    /***
     * insert
     * @param record
     * @return
     */
    int insert(CfgMemProps record);

    /**
     * insertSelective
     * @param record
     * @return
     */
    int insertSelective(CfgMemProps record);

    /**
     * selectByPrimaryKey
     * @param key
     * @return
     */
    CfgMemProps selectByPrimaryKey(CfgMemPropsKey key);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(CfgMemProps record);

    /**
     * updateByPrimaryKey
     * @param record
     * @return
     */
    int updateByPrimaryKey(CfgMemProps record);
}