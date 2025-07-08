package com.keensense.admin.mapper.lawcase;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.admin.entity.lawcase.AppPoliceComprehensive;
import com.keensense.admin.vo.PoliceComprehensiveVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 20:11:51
 */
@Mapper
public interface AppPoliceComprehensiveMapper extends BaseMapper<AppPoliceComprehensive> {

    List<PoliceComprehensiveVo> selectCaseByPage(Page<PoliceComprehensiveVo> pages, @Param("params")Map<String, Object> params);

}
