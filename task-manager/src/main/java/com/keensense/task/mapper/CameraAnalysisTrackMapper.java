package com.keensense.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keensense.task.entity.CameraAnalysisTrack;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: CameraAnalysisTrackMapper
 * @Description: CameraAnalysisTrack Mapper
 * @Author: cuiss
 * @CreateDate: 2020/4/14 16:01
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Mapper
public interface CameraAnalysisTrackMapper extends BaseMapper<CameraAnalysisTrack> {

    int insertAnalysisTrackHisFromTrack(@Param("cameraId") String cameraId, @Param("ymd") String ymd);

    int deleteAnalysisTrack(@Param("cameraId") String cameraId, @Param("ymd") String ymd);

}
