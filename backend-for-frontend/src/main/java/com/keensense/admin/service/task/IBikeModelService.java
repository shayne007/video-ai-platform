package com.keensense.admin.service.task;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.admin.entity.task.BikeModel;

import java.util.List;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 21:15:12
 */
public interface IBikeModelService extends IService<BikeModel> {

    /**
     * 非机动车类型
     * @return
     */
    List<BikeModel> getBikeModelList();
}

