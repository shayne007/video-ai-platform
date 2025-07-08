package com.keensense.admin.service.task;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.admin.entity.task.HumanColorModel;
import com.keensense.admin.vo.ColorVo;

import java.util.List;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 21:15:12
 */
public interface IHumanColorModelService extends IService<HumanColorModel> {

    /**
     * 获取颜色列表
     * @return
     */
    List<ColorVo> getColorModelList(String type);
}

