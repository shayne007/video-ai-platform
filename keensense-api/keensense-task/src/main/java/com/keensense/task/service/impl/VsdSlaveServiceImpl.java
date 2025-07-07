package com.keensense.task.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.task.entity.VsdSlave;
import com.keensense.task.mapper.VsdSlaveMapper;
import com.keensense.task.service.IVsdSlaveService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: wujw
 * @CreateDate: 2019/7/31 13:42
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Service
public class VsdSlaveServiceImpl  extends ServiceImpl<VsdSlaveMapper, VsdSlave> implements IVsdSlaveService {

    @Override
    public JSONObject getVsdSlaveList(Page<VsdSlave> page) {
        IPage<VsdSlave> vsdSlaveIPage = baseMapper.selectPage(page, new QueryWrapper<>());
        List<VsdSlave> list = vsdSlaveIPage.getRecords();
        JSONObject result = new JSONObject(4);
        result.put("totalCount", list.size());
        result.put("salves", list);
        return result;
    }

    @Override
    public VsdSlave getById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public int insert(VsdSlave vsdSlave) {
        return baseMapper.insert(vsdSlave);
    }

    @Override
    public int updateVsdSlave(VsdSlave vsdSlave) {
        return baseMapper.updateById(vsdSlave);
    }

    @Override
    public VsdSlave selectByWrapper(QueryWrapper<VsdSlave> queryWrapper) {
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<String> getSlaveIdList() {
        return baseMapper.getSlaveIdList();
    }

}
