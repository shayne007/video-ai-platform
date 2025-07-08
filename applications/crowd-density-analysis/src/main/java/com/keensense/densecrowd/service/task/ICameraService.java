package com.keensense.densecrowd.service.task;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.common.util.R;
import com.keensense.densecrowd.dto.TreeNode;
import com.keensense.densecrowd.entity.task.Camera;
import com.keensense.densecrowd.entity.task.CtrlUnit;
import com.keensense.densecrowd.request.CameraRequest;
import com.keensense.densecrowd.vo.CameraVo;
import com.keensense.densecrowd.vo.AlarmDeviceRequest;
import org.apache.ibatis.annotations.Param;


import java.awt.geom.*;
import java.util.*;

/**
 * @Author: shitao
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 9:13 2019/9/26
 * @Version v0.1
 */
public interface ICameraService extends IService<Camera> {

    /**
     * 按监控点名称模糊查询结果
     *
     * @param name
     * @return
     */
    List<CameraVo> selectCameraByName(String name, String id, String isAnalysis);

    /**
     * 像机分页查询
     *
     * @param deviceRequest
     * @return
     */
    IPage<Camera> selectCameraList(AlarmDeviceRequest deviceRequest);

    /**
     * 地图查询监控点列表
     *
     * @param page
     * @param map
     * @return
     */
    Page<CameraVo> selectOnlineAndIpCCameraListByPage(Page<CameraVo> page, Map<String, Object> map);

    /**
     * 构建点位树
     *
     * @param areaList
     * @param cameraList
     * @return
     */
    List<TreeNode> bulidCameraTree(List<CtrlUnit> areaList, List<CameraVo> cameraList);

    /**
     * 通过id查点位
     *
     * @param areaId
     * @return
     */
    List<CameraVo> selectCameraByAreaId(String areaId, String isAnalysis);

    /**
     * 区域点位列表树
     *
     * @param tlist
     * @return
     */
    List<TreeNode> selectBulidTree(List<CtrlUnit> tlist);

    /**
     * 查询最新的实时监控点位
     *
     * @param num
     * @return
     */
    List<CameraVo> selectNewStartCamera(Integer num);

    /**
     * 根据流水号查询点位名称
     *
     * @param serialnumber
     * @return
     */
    String selectNameBySerialnumber(String serialnumber);

    /**
     * 获取快照地址
     *
     * @param cameraUrl 快照地址
     * @return
     * @throws Exception
     */
    String getCameraSnapshotByUrl(String cameraUrl);


    /**
     * 查询对应类型全部的点位信息
     * @param status
     * @param polygonList
     * @return
     */
    List<CameraVo> selectCameraAll(String status, List<Point2D.Double> polygonList);

    List<Camera> selectCameraByArea(String id);

    /**
     * 根据设备编号查询数据
     *
     * @param deviceId
     * @return
     */
    List<Camera> selectCameraByDeviceId(String deviceId);

    /**
     * 获取有url地址无缩略图监控点
     * @return
     */
    List<Camera> selectTumbNailCameraList();

    R updateCamera(CameraRequest cameraRequest);

    void deleteCameraAndTaskById(String cameraId);

}
