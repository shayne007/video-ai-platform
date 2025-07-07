package com.keensense.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.task.constants.BitCommonConst;
import com.keensense.task.constants.TaskConstants;
import com.keensense.task.entity.VsdBitMachine;
import com.keensense.task.entity.VsdTask;
import com.keensense.task.entity.VsdTaskBit;
import com.keensense.task.mapper.VsdBitMachineMapper;
import com.keensense.task.mapper.VsdTaskBitMapper;
import com.keensense.task.mapper.VsdTaskMapper;
import com.keensense.task.service.IVsdBitMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: bit盒子service
 * @Author: wujw
 * @CreateDate: 2019/5/23 17:24
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Service
public class IVsdBitMachineServiceImpl extends ServiceImpl<VsdBitMachineMapper, VsdBitMachine> implements IVsdBitMachineService {

    @Autowired
    private VsdTaskBitMapper vsdTaskBitMapper;
    @Autowired
    private VsdTaskMapper vsdTaskMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stopTask(VsdBitMachine vsdBitMachine, VsdTaskBit vsdTaskBit) {
        vsdTaskBitMapper.deleteById(vsdTaskBit);
        vsdTaskMapper.updateStatusBySerialnumber(vsdTaskBit.getSerialnumber(), TaskConstants.TASK_STATUS_WAIT);
        if(vsdBitMachine != null){
            vsdBitMachine.setExecuteStatus(BitCommonConst.EXECUTE_STATUS_FALSE);
            vsdBitMachine.setUserRoad(vsdBitMachine.getUserRoad() - 1);
            baseMapper.updateById(vsdBitMachine);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void taskComplete(VsdBitMachine vsdBitMachine, VsdTaskBit vsdTaskBit, VsdTask vsdTask) {
        vsdTaskMapper.updateById(vsdTask);
        vsdTaskBitMapper.deleteById(vsdTaskBit);
        baseMapper.updateById(vsdBitMachine);
    }

    @Override
    public void taskReset(VsdTask vsdTask, VsdTaskBit vsdTaskBit) {
        vsdTaskMapper.updateById(vsdTask);
        vsdTaskBitMapper.deleteById(vsdTaskBit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startSuccess(VsdBitMachine vsdBitMachine, VsdTaskBit vsdTaskBit, VsdTask vsdTask) {
        vsdTaskMapper.updateById(vsdTask);
        vsdTaskBitMapper.updateById(vsdTaskBit);
        baseMapper.updateById(vsdBitMachine);
    }

    @Override
    public void doErrorMachine(VsdBitMachine vsdBitMachine, QueryWrapper<VsdTaskBit> vsdTaskBitQueryWrapper) {
        vsdTaskBitMapper.delete(vsdTaskBitQueryWrapper);
        baseMapper.updateById(vsdBitMachine);
    }

    @Override
    public void updateVsdBitMachine(VsdBitMachine vsdBitMachine) {
        baseMapper.updateById(vsdBitMachine);
    }

    @Override
    public List<VsdBitMachine> selectByWrapper(QueryWrapper<VsdBitMachine> wrapper) {
        return baseMapper.selectList(wrapper);
    }
}
