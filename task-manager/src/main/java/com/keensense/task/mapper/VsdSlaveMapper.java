package com.keensense.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keensense.task.entity.VsdSlave;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:
 * @Author: wujw
 * @CreateDate: 2019/7/31 13:43
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Mapper
public interface VsdSlaveMapper extends BaseMapper<VsdSlave> {

    List<String> getSlaveIdList();

    void updateSlaveId(@Param(value = "slaveId")String slaveId, @Param(value = "slaveIp")String slaveIp);

    /***
     * @description: 更新超时后的节点状态，mysql版本使用
     * @return: void
     */
    void updateTimeOut();

}
