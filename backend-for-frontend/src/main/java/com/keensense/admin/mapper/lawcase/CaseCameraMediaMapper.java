package com.keensense.admin.mapper.lawcase;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keensense.admin.entity.lawcase.CaseCameraMedia;
import com.keensense.admin.vo.CaseCameraMediaVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 20:11:51
 */
@Mapper
public interface CaseCameraMediaMapper extends BaseMapper<CaseCameraMedia> {

    List<CaseCameraMediaVo> selectByList(String lawcaseid);

    List<CaseCameraMediaVo>  queryCurrentTaskCameras(String lawcaseid);

    Integer selectExistsImage(@Param("record")CaseCameraMedia record);
}
