package com.keensense.alarm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.alarm.entity.DispositionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 布控对象 Mapper 接口
 * </p>
 *
 * @author ycl
 * @since 2019-05-14
 */
@Mapper
public interface DispositionMapper extends BaseMapper<DispositionEntity> {

    IPage<DispositionEntity> selectDispositionBySql(Page page, @Param("sql") String sql);

}
