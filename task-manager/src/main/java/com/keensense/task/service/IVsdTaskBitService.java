package com.keensense.task.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.task.entity.VsdTaskBit;

import java.util.List;

/**
 * @Description: bit盒子与任务关联关系表
 * @Author: wujw
 * @CreateDate: 2019/5/23 17:07
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public interface IVsdTaskBitService {
    
    /***
     * 根据ID删除任务
     * @param id ID
     * @return: int
     */
    void deleteById(Long id);

    /***
     * 添加关联对象
     * @param vsdTaskBit 任务与盒子关联对象
     * @return: boolean
     */
    boolean insert(VsdTaskBit vsdTaskBit);

    /***
     * 根据条件查询
     * @param wrapper
     * @return: java.util.List<com.keensense.task.entity.VsdTaskBit>
     */
    List<VsdTaskBit> selectByWrapper(QueryWrapper<VsdTaskBit> wrapper);
}
