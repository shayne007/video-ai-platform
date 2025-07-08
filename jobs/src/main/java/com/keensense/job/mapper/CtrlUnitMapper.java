package com.keensense.job.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keensense.job.entity.CtrlUnit;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 组织机构表 Mapper 接口
 * </p>
 *
 * @author cuiss
 * @since 2018-11-05
 */
@Mapper
public interface CtrlUnitMapper extends BaseMapper<CtrlUnit> {

    public List<CtrlUnit> selectAllCtrlUnits();

}
