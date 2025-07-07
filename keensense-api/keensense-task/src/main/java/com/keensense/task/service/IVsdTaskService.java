package com.keensense.task.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.task.entity.VsdTask;

import java.util.List;

/**
 * @Description: vsdTask Service
 * @Author: wujw
 * @CreateDate: 2019/5/17 11:22
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public interface IVsdTaskService {

    /***
     * 定时任务获取Detail, 写入vsdTask或者更新进度
     * @return: int
     */
    int insertVsdTask();

    /****
     * 根据ID重置状态
     * @param ids id数组
     * @param status 状态
     * @return: int
     */
    int updateStatusByIds(Long[] ids, int status);

    /****
     * 根据ID重置状态
     * @param id id
     * @param status 状态
     * @return: int
     */
    int updateStatusById(Long id, int status);

    /****
     * 更新对象
     * @param vsdTask vsdTask对象
     * @return: int
     */
    int updateVsdTask(VsdTask vsdTask);

    /****
     * 根据条件查询
     * @param queryWrapper 查询对象
     * @return: int
     */
    List<VsdTask> selectByWrapper(QueryWrapper<VsdTask> queryWrapper);

    /***
     * 更新感兴趣区域
     * @param serialnumber 任务ID
     * @param param 新的param参数
     */
    void updateUdrSetting(String serialnumber, JSONObject param);

    /***
     * 根据条件查询单个对象
     * @param queryWrapper 查询条件
     * @return VsdTask
     */
    VsdTask selectOne(QueryWrapper<VsdTask> queryWrapper);

    /****
     * 根据条件分页查询
     * @param page 分页参数
     * @param queryWrapper 查询对象
     * @return: int
     */
    List<VsdTask> selectByPage(Page<VsdTask> page, QueryWrapper<VsdTask> queryWrapper);

    /****
     * 更新对象的param值
     * @param serialnumber 任务号
     * @param param 要更新的参数
     * @return: int
     */
    boolean updateParam(String serialnumber, String param);
}
