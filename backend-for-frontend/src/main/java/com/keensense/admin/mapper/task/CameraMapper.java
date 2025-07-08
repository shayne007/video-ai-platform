package com.keensense.admin.mapper.task;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.admin.dto.TreeNode;
import com.keensense.admin.entity.task.Camera;
import com.keensense.admin.vo.CameraVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 20:11:51
 */
@Mapper
public interface CameraMapper extends BaseMapper<Camera> {

   /**
     * 查询实时点位信息
     * @param page
     * @param params
     * @return
     */
    List<CameraVo> selectOnlineAndIpCCameraList(Page<CameraVo> page, @Param("params") Map<String, Object> params);

    List<CameraVo> selectGateCameraList(Page<CameraVo> page, @Param("params") Map<String, Object> params);

    CameraVo selectNewStartCamera(@Param("params") Map<String, Object> params);

    /**
     * 查询实时点位信息
     * @param params
     * @return
     */
    List<CameraVo> selectOnlineAndIpCCameraList(@Param("params") Map<String, Object> params);
    /**
     * 根据指定条件查询监控点
     * @return
     */
    List<CameraVo> selectCameraByCameraType(@Param("cameraType") String cameraType);

    CameraVo selectByPrimaryKey(Long id);

    /**
     * 根据区域ID查询监控点列表
     * @param parentId
     * @return
     */
    List<Camera> selectCameraListWithParentId(String parentId);

    /**
     * 根据监控点名称查询监控点树
     * @return
     */
    List<TreeNode> selectCameraByName(Map<String,String> paramMap);

    /**
     * 根据监控点名称查询监控点列表
     * @return
     */
    List<TreeNode> selectCameraTreeByUnitIdentity(Map<String,List<String>> unitIdentityList);

    List<Camera> selectJdCamera();

}
