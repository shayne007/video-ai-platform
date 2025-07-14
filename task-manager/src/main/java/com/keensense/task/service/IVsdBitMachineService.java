package com.keensense.task.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.keensense.task.entity.VsdBitMachine;
import com.keensense.task.entity.VsdTask;
import com.keensense.task.entity.VsdTaskBit;

import java.util.List;

/**
 * @Description:
 * @Author: wujw
 * @CreateDate: 2019/5/23 17:23
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public interface IVsdBitMachineService {

    /***
     * 停止任务后更新数据库
     * @param vsdBitMachine 盒子对象
     * @param vsdTaskBit    盒子和任务关联对象
     * @return: int
     */
    void stopTask(VsdBitMachine vsdBitMachine, VsdTaskBit vsdTaskBit);

    /***
     * 任务处理完成
     * @param vsdBitMachine 盒子对象
     * @param vsdTaskBit    盒子和任务关联对象
     * @param vsdTask       任务对象
     * @return: int
     */
    void taskComplete(VsdBitMachine vsdBitMachine, VsdTaskBit vsdTaskBit, VsdTask vsdTask);

    /***
     * 任务处理完成
     * @param vsdTaskBit    盒子和任务关联对象
     * @param vsdTask       任务对象
     * @return: int
     */
    void taskReset(VsdTask vsdTask, VsdTaskBit vsdTaskBit);

    /***
     * 开始任务
     * @param vsdBitMachine 盒子对象
     * @param vsdTaskBit    盒子和任务关联对象
     * @param vsdTask       任务对象
     * @return: int
     */
    void startSuccess(VsdBitMachine vsdBitMachine, VsdTaskBit vsdTaskBit, VsdTask vsdTask);

    /***
     * 任务失败处理
     * @param vsdBitMachine 盒子对象
     * @param vsdTaskBitQueryWrapper vsdTaskBit检索条件对象
     * @return: int
     */
    void doErrorMachine(VsdBitMachine vsdBitMachine, QueryWrapper<VsdTaskBit> vsdTaskBitQueryWrapper);

    /***
     * 更新盒子
     * @param vsdBitMachine 盒子对象
     * @return: int
     */
    void updateVsdBitMachine(VsdBitMachine vsdBitMachine);

    /****
     * 根据条件查询盒子
     * @param wrapper 查询条件
     * @return: java.util.List<com.keensense.task.entity.VsdBitMachine>
     */
    List<VsdBitMachine> selectByWrapper(QueryWrapper<VsdBitMachine> wrapper);
}
