package com.keensense.admin.service.task;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.admin.dto.FileBo;
import com.keensense.admin.dto.TaskParamBo;
import com.keensense.admin.dto.TreeNode;
import com.keensense.admin.entity.task.Camera;
import com.keensense.admin.entity.task.CtrlUnit;
import com.keensense.admin.entity.task.CtrlUnitFile;
import com.keensense.admin.request.CameraRequest;
import com.keensense.admin.vo.CameraVo;
import com.keensense.common.util.R;
import org.springframework.web.multipart.MultipartFile;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 21:15:12
 */
public interface ICameraService extends IService<Camera> {

    Page<CameraVo> selectPicCameraListByPage(Page<CameraVo> page, Map<String, Object> map);

    Page<CameraVo> selectOnlineAndIpCCameraListByPage(Page<CameraVo> page, Map<String, Object> map);

    Page<CameraVo> selectGateCameraList(Page<CameraVo> page, Map<String, Object> map);

    List<Camera> selectOnlineAndIpCCameraList(Map<String, Object> map);

    CameraVo selectNewStartCamera(Integer enablePartial, String cameraEventType);

    List<TreeNode> bulidTree(List<CtrlUnit> tlist);

    List<Camera> findCameraByAreaIdVas(String areaId);

    List<Camera> findCameraByAreaId(String areaId);

    List<Camera> findVasCameraByAreaId(String areaId);

    List<TreeNode> bulidCameraTree(List<CtrlUnit> areaList,List<Camera> cameraList);

    Map<String, Object> startPictureTask(String cameraId);

    CameraVo selectByPrimaryKey(String id);

    /**
     * 批量删除 删除监控点位以及删除监控点位关联的任务信息，且删除监控点位下所有离线视频及其关联的任务信息，
     * @param cameraIds
     * @return
     */
    int deleteCameraByIds(String cameraIds);

    void deleteCameraAndTaskById(String cameraId);

    FileBo uploadFileChuck(File file, CtrlUnitFile resource);

    Camera selectCameraById(Serializable id);

    /**
     * 获取快照地址
     * @param cameraId 点位id
     * @return
     * @throws Exception
     */
    String getCameraSnapshotByUrl(String cameraId);

    /**
     * 查询对应类型全部的点位信息
     * @param cameraType
     * @return
     */
    List<CameraVo> queryCameraAll(String cameraType,String status,List<Point2D.Double> polygonList);

    /**
     * 封装查询的监控点
     * @param cameraAndAreaIdList
     * @return
     */
    String handleQueryCameraIds(List<String> cameraAndAreaIdList);

    List<CtrlUnit> queryUnitByParentId(String parentId);

    R updateCamera(CameraRequest cameraRequest);

    R exportCameraExcel(MultipartFile file, String cameraType);

    R submitCamera(CameraRequest cameraRequest);

    R addVSDTask(TaskParamBo taskParamBo);

    /**
     * 根据监控点名称查询监控点列表
     * @return
     */
    List<TreeNode> selectCameraByName(Map<String, String> paramMap);

    /**
     * 获取有url地址无缩略图监控点
     * @return
     */
    List<Camera> selectTumbNailCameraList();

    /**
     * 按监控点名称模糊查询结果
     * @param name
     * @return
     */
    List<Camera> selectCameraByName(String name, String id);

    List<Camera> selectVasCameraByName(String name, String id);

    List<Camera> queryCameraListByArea(String id);

    /**
     * 实时感知返回树形结构区域点位信息
     * @param mList
     * @param cList
     * @return
     */
    List<TreeNode> bulidCameraTreeByCtrlUnit(List<CtrlUnit> mList, List<Camera> cList);

    List<Camera> selectJdCamera();

}

