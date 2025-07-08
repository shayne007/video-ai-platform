package com.keensense.dataconvert.biz.dao;

import com.keensense.dataconvert.biz.entity.ObjextResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @ClassName：ObjextResultMapper
 * @Description： <p> ObjextResultMapper </p>
 * @Author： - Jason
 * @CreatTime：2019/7/26 - 15:02
 * @Modify By：
 * @ModifyTime： 2019/7/26
 * @Modify marker：
 * @version V1.0
*/
@Mapper
public interface ObjextResultMapper {

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
    int insert(ObjextResult record);

    /**
     * insertSelective
     * @param record
     * @return
     */
    int insertSelective(ObjextResult record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return
     */
    ObjextResult selectByPrimaryKey(Integer id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ObjextResult record);

    /**
     * updateByPrimaryKey
     * @param record
     * @return
     */
    int updateByPrimaryKey(ObjextResult record);

    /**
     * 分表查询list数据
     * @param ymd
     * @return
     */
    List<ObjextResult> selectListByYmd(@Param("ymd") String ymd);


    /**
     * 更新数据
     * @param paramsMap
     */
    void updateSerialNumberByMap(@Param("params") Map paramsMap);
}