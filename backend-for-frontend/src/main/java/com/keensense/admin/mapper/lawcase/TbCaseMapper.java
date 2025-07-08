package com.keensense.admin.mapper.lawcase;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.admin.entity.lawcase.TbCase;
import com.keensense.admin.vo.TbCaseVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 20:11:51
 */
@Mapper
public interface TbCaseMapper extends BaseMapper<TbCase> {

    /**
     * 获取案件表中最新的更新时间
     */
    Timestamp getLatestUpdateTime();

    /**
     * 分页查询案件信息
     * @param pages
     * @param params
     * @return
     */
    List<TbCaseVo> selectTbCaseByPage(Page<TbCaseVo> pages, @Param("params")Map<String, Object> params);

}
