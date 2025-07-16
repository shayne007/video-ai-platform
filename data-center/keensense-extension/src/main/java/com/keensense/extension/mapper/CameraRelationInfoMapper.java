package com.keensense.extension.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keensense.extension.entity.ArchivesInfo;
import com.keensense.extension.entity.CameraRelationInfo;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2019-05-06
 */
@Mapper
public interface CameraRelationInfoMapper extends BaseMapper<CameraRelationInfo> {
    
    int batchInsert(List<CameraRelationInfo> list);
    
    void deleteByCondition(@Param("ids") List<String> ids,
        @Param("deviceIds") List<String> deviceIds, @Param("updateTimes") List<Date> updateTimes);
    
    List<CameraRelationInfo> queryByCondition(@Param("ids") List<String> ids,
        @Param("deviceIds") List<String> deviceIds, @Param("updateTimes") List<Date> updateTimes);
}
