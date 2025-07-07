package com.keensense.alarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.alarm.dto.Disposition;
import com.keensense.alarm.entity.DispositionEntity;

import java.util.List;

/**
 * <p>
 * 布控对象 服务类
 * </p>
 *
 * @author ycl
 * @since 2019-05-14
 */
public interface IDispositionService extends IService<DispositionEntity> {

    /**
     * 保存或更新
     * @param dispositions 布控对象(包含子图像列表）
     * @return 成功与否
     */
    boolean addDispositions(List<Disposition> dispositions);

    /**
     * 移除布控（包含子图像列表）
     * @param ids 布控id
     * @return 成功与否
     */
    boolean removeDispositions(String ids);

    /**
     * 查找布控
     * @param param url参数
     * @return 布控对象(包含子图像列表）
     */
    List<Disposition> findDispositionByParam(String param);

}
