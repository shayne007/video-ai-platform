package com.keensense.admin.mapper.task;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.admin.entity.task.MonitorGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 9:39 2019/6/15
 * @Version v0.1
 */
@Mapper
public interface MonitorGroupMapper extends BaseMapper<MonitorGroup>{
    List<MonitorGroup> selectMonitorGroupByPage(Page<MonitorGroup> pages, @Param("params") Map<String, Object> params);

    MonitorGroup queryMonitorGroupIdByName(String groupName);
}
