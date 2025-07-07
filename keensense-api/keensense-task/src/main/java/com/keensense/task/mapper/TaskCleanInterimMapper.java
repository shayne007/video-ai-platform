package com.keensense.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keensense.task.entity.TaskCleanInterim;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 清理机制临时类
 * @Author: wujw
 * @CreateDate: 2019/9/28 10:19
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Mapper
public interface TaskCleanInterimMapper extends BaseMapper<TaskCleanInterim> {

    void resetRunningStatus(@Param("oldStatus") Integer oldStatus, @Param("newStatus") Integer newStatus);

    int updateStatus(@Param("serialnumber")String serialnumber, @Param("state")Integer state, @Param("ymd")String ymd);

    int updateStatusFailed(@Param("serialnumber")String serialnumber, @Param("state")Integer state, @Param("ymd")String ymd);
}
