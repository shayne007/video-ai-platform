package com.keensense.extension.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keensense.extension.entity.ArchivesInfo;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2019-05-06
 */
@Mapper
public interface ArchivesInfoMapper extends BaseMapper<ArchivesInfo> {
    
    List<ArchivesInfo> queryByCondition(
        @Param("archivesIDList") List<String> archivesIDList,
        @Param("angleList") List<Integer> angleList,
        @Param("pIdList") List<String> pIdList,
        @Param("createTimeList") List<Date> createTimeList);
    
    void updateByArchivesID(@Param("pId") String pId,
        @Param("archivesIds") List<String> archivesIds);
    
    void deleteByPID(@Param("archivesIdsList") List<String> archivesIdsList);
    
}
