package com.keensense.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.task.entity.VsdTaskBit;
import com.keensense.task.mapper.VsdTaskBitMapper;
import com.keensense.task.service.IVsdTaskBitService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: wujw
 * @CreateDate: 2019/5/23 17:15
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Service
public class VsdTaskBitServiceImpl extends ServiceImpl<VsdTaskBitMapper, VsdTaskBit> implements IVsdTaskBitService {

    @Override
    public void deleteById(Long id) {
        baseMapper.deleteById(id);
    }

    @Override
    public boolean insert(VsdTaskBit vsdTaskBit) {
        return baseMapper.insert(vsdTaskBit) > 0;
    }

    @Override
    public List<VsdTaskBit> selectByWrapper(QueryWrapper<VsdTaskBit> wrapper) {
        return baseMapper.selectList(wrapper);
    }
}
