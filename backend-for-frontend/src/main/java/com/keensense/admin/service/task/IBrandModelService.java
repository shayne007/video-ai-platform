package com.keensense.admin.service.task;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.admin.entity.task.BrandModel;

import java.util.List;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 21:15:12
 */
public interface IBrandModelService extends IService<BrandModel> {

    /**
     * 获取车辆所有品牌
     */
    List<String> selectAllCarBrand();

    /**
     * 获取车辆车辆系列
     */
    List<BrandModel> selectAllCarSeries();
}

