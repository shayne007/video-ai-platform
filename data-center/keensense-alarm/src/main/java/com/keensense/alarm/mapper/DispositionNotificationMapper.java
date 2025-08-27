package com.keensense.alarm.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.alarm.entity.DispositionNotificationEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ycl
 * @since 2019-05-14
 */
@Mapper
public interface DispositionNotificationMapper extends BaseMapper<DispositionNotificationEntity> {

    IPage<DispositionNotificationEntity> selectDispositionNotiBySql(Page page, @Param("sql") String sql);

//    @Select("SELECT COUNT(1) FROM alarm_disposition_notification force index(trigger_time_index)  ${ew.customSqlSegment}")
    List<Integer> selectNotisCount(@Param(Constants.WRAPPER) Wrapper wrapper);

}
