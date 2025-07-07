package com.keensense.task.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.task.entity.VsdSlave;

import java.util.List;

/**
 * @Description:
 * @Author: wujw
 * @CreateDate: 2019/7/31 13:42
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public interface IVsdSlaveService {

    /***
     * 获取集群信息
     * @param page  分页对象
     * @return: JSONObject
     */
    JSONObject getVsdSlaveList(Page<VsdSlave> page);

    /***
     * 根据ID获取对应的服务器信息
     * @param id 请求参数
     * @return: JSONObject
     */
    VsdSlave getById(Long id);

    /***
     * 添加服务器信息
     * @param vsdSlave 服务器对象
     * @return: JSONObject
     */
    int insert(VsdSlave vsdSlave);

    /***
     * 更新服务器信息
     * @param vsdSlave 服务器对象
     * @return: JSONObject
     */
    int updateVsdSlave(VsdSlave vsdSlave);

    /***
     * 根据条件查询服务器信息
     * @param queryWrapper 请求参数
     * @return: JSONObject
     */
    VsdSlave selectByWrapper(QueryWrapper<VsdSlave> queryWrapper);

    /***
     * 获取分析节点信息
     * @return: JSONObject
     */
    List<String> getSlaveIdList();
}
