package com.keensense.admin.mapper.task;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keensense.admin.entity.task.TbRelaytrackDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;


/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 20:11:51
 */
@Mapper
public interface TbRelaytrackDetailMapper extends BaseMapper<TbRelaytrackDetail> {

    Map<String,Object> getNameMap(String taskId);
}
