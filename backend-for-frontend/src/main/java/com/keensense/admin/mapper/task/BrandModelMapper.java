package com.keensense.admin.mapper.task;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keensense.admin.entity.task.BrandModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 20:11:51
 */
@Mapper
public interface BrandModelMapper extends BaseMapper<BrandModel> {

    /**
     * 获取车辆所有品牌
     */
    List<String> selectAllCarBrand();

    /**
     * 获取车辆车辆系列
     */
    List<BrandModel> selectAllCarSeries();

    List<BrandModel> selectAllCarKind(Integer carKindId);
}
