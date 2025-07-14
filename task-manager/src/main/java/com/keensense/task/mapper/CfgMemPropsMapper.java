package com.keensense.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keensense.task.entity.TaskCleanLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description:
 * @Author: wujw
 * @CreateDate: 2019/9/28 17:17
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Mapper
public interface CfgMemPropsMapper extends BaseMapper<TaskCleanLog> {

    void updateConfig();

    int changeMaster(@Param(value = "masterId") String masterId, @Param(value = "date") String date);

    int heart(@Param(value = "masterId") String masterId, @Param(value = "date") String date);
}
