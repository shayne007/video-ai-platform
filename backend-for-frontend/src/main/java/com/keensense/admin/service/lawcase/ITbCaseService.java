package com.keensense.admin.service.lawcase;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.admin.entity.lawcase.TbCase;
import com.keensense.admin.vo.TbCaseVo;

import java.util.Map;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 21:15:12
 */
public interface ITbCaseService extends IService<TbCase> {

    /**
     * 保存从佳都获取的案件数据
     */
    boolean saveCaseData();

    /**
     * 分页查询案件信息
     * @param pages
     * @param params
     * @return
     */
    Page<TbCaseVo> selectCaseByPage(Page<TbCaseVo> pages, Map<String, Object> params);
}

