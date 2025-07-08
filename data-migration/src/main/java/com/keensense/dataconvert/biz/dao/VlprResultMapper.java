package com.keensense.dataconvert.biz.dao;

import com.keensense.dataconvert.biz.entity.VlprResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @ClassName：VlprResultMapper
 * @Description： <p> VlprResultMapper  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/26 - 14:02
 * @Modify By：
 * @ModifyTime： 2019/7/26
 * @Modify marker：
 * @version V1.0
*/
@Mapper
public interface VlprResultMapper {

    /**
     * deleteByPrimaryKey
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * insert
     * @param record
     * @return
     */
    int insert(VlprResult record);

    /**
     * insertSelective
     * @param record
     * @return
     */
    int insertSelective(VlprResult record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return
     */
    VlprResult selectByPrimaryKey(Integer id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(VlprResult record);

    /**
     * updateByPrimaryKey
     * @param record
     * @return
     */
    int updateByPrimaryKey(VlprResult record);

    /**
     * selectListByYmd
     * @param ymd
     * @return
     */
    List<VlprResult> selectListByYmd(@Param("ymd") String ymd);

    /**
     * 更新业务数据
     * @param paramsMap
     */
    void updateSerialNumberByMap(@Param("params") Map paramsMap);
}