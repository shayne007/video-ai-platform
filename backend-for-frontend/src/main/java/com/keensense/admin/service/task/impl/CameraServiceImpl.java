package com.keensense.admin.service.task.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.constants.CameraCacheConstants;
import com.keensense.admin.constants.CameraConstants;
import com.keensense.admin.constants.CommonConstants;
import com.keensense.admin.constants.VideoTaskConstant;
import com.keensense.admin.dto.FileBo;
import com.keensense.admin.dto.TaskParamBo;
import com.keensense.admin.dto.TreeNode;
import com.keensense.admin.entity.task.Camera;
import com.keensense.admin.entity.task.CtrlUnit;
import com.keensense.admin.entity.task.CtrlUnitFile;
import com.keensense.admin.entity.task.MonitorGroupDetail;
import com.keensense.admin.entity.task.VsdTaskRelation;
import com.keensense.admin.mapper.task.CameraMapper;
import com.keensense.admin.mapper.task.CtrlUnitFileMapper;
import com.keensense.admin.mapper.task.CtrlUnitMapper;
import com.keensense.admin.mapper.task.MonitorGroupDetailMapper;
import com.keensense.admin.mapper.task.VsdTaskRelationMapper;
import com.keensense.admin.request.CameraRequest;
import com.keensense.admin.service.ext.FtpTranscodeService;
import com.keensense.admin.service.ext.PictureObjectTaskService;
import com.keensense.admin.service.ext.VideoObjextTaskService;
import com.keensense.admin.service.sys.ICfgMemPropsService;
import com.keensense.admin.service.task.ICameraService;
import com.keensense.admin.service.task.IVsdTaskRelationService;
import com.keensense.admin.service.task.IVsdTaskService;
import com.keensense.admin.util.AesUtil;
import com.keensense.admin.util.DbPropUtil;
import com.keensense.admin.util.EhcacheUtils;
import com.keensense.admin.util.EntityObjectConverter;
import com.keensense.admin.util.ExcelHandleUtils;
import com.keensense.admin.util.FTPUtils;
import com.keensense.admin.util.FileUtils;
import com.keensense.admin.util.IdUtils;
import com.keensense.admin.util.MasSnapshotUtil;
import com.keensense.admin.util.PolygonUtil;
import com.keensense.admin.util.RandomUtils;
import com.keensense.admin.util.SnapshotUtil;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.util.ValidateHelper;
import com.keensense.admin.vo.CameraVo;
import com.keensense.admin.vo.VsdTaskVo;
import com.keensense.common.exception.VideoException;
import com.keensense.common.util.R;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.util.ListUtil;
import com.loocme.sys.util.MapUtil;
import com.loocme.sys.util.PatternUtil;
import com.loocme.sys.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service("cameraService")
public class CameraServiceImpl extends ServiceImpl<CameraMapper, Camera> implements ICameraService {

    @Resource
    private PictureObjectTaskService pictureObjectTaskService;

    @Resource
    private VsdTaskRelationMapper vsdTaskRelationMapper;

    @Resource
    private CtrlUnitFileMapper ctrlUnitFileMapper;

    @Resource
    private CtrlUnitMapper ctrlUnitMapper;
    @Resource
    private IVsdTaskService vsdTaskService;

    @Autowired
    private MonitorGroupDetailMapper monitorGroupDetailMapper;

    @Autowired
    @Qualifier("videoObjextTaskService")
    private VideoObjextTaskService videoObjextTaskService;

    @Resource
    private IVsdTaskRelationService vsdTaskRelationService;

    @Autowired
    private FtpTranscodeService ftpTranscodeService;

    @Autowired
    private ICfgMemPropsService cfgMemPropsService;

    @Override
    public Page<CameraVo> selectPicCameraListByPage(Page<CameraVo> pages, Map<String, Object> map) {
        List<CameraVo> records = null;
        pages.setRecords(records);
        return pages;
    }

    @Override
    public Page<CameraVo> selectOnlineAndIpCCameraListByPage(Page<CameraVo> pages, Map<String, Object> map) {
        List<CameraVo> records = baseMapper.selectOnlineAndIpCCameraList(pages, map);
        if (records != null && !records.isEmpty()) {
            for (CameraVo record : records) {
                CtrlUnit ctrlUnit = ctrlUnitMapper.selectOne(new QueryWrapper<CtrlUnit>().eq("unit_identity", record.getRegion()));
                if (ctrlUnit != null) {
                    record.setRegionName(ctrlUnit.getUnitName());
                }
            }
        }
        pages.setRecords(records);
        return pages;
    }

    @Override
    public Page<CameraVo> selectGateCameraList(Page<CameraVo> pages, Map<String, Object> map) {
        List<CameraVo> records = baseMapper.selectGateCameraList(pages, map);
        if (records != null && records.size() > 0) {
            for (CameraVo record : records) {
                CtrlUnit ctrlUnit = ctrlUnitMapper.selectOne(new QueryWrapper<CtrlUnit>().eq("unit_identity", record.getRegion()));
                if (ctrlUnit != null) {
                    record.setRegionName(ctrlUnit.getUnitName());
                }
            }
        }
        pages.setRecords(records);
        return pages;
    }

    @Override
    public List<Camera> selectOnlineAndIpCCameraList(Map<String, Object> map) {
        List<CameraVo> records = baseMapper.selectOnlineAndIpCCameraList(map);
        List<Camera> cameras = new ArrayList<>();
        for (CameraVo cameraVo : records) {
            Camera camera = new Camera();
            BeanUtils.copyProperties(cameraVo, camera);
            cameras.add(camera);
        }
        return cameras;
    }

    @Override
    public CameraVo selectNewStartCamera(Integer enablePartial, String cameraEventType) {
        Map<String, Object> param = new HashMap<>();
        if (null != enablePartial) {
            param.put("enablePartial", enablePartial);
        }
        if (StringUtils.isNotEmptyString(cameraEventType)) {
            param.put("cameraEventType", cameraEventType);
        }
        return baseMapper.selectNewStartCamera(param);
    }

    @Override
    public List<TreeNode> bulidTree(List<CtrlUnit> tlist) {
        List<TreeNode> list = new ArrayList<TreeNode>();
        for (CtrlUnit area : tlist) {
            TreeNode treeNode = new TreeNode();
            treeNode.setId(area.getUnitIdentity());
            treeNode.setName(area.getDisplayName());
            treeNode.setpId(area.getUnitParentId());
            treeNode.setUnitLevel(area.getUnitLevel().toString());
            treeNode.setNodeType(1);

            if (area.getIsLeaf() == 1) {
                List<Camera> cameras = findCameraByAreaId(area.getUnitIdentity().toString());
                if (cameras.size() > 0) {
                    treeNode.setIsParent(true);
                } else {
                    treeNode.setIsParent(false);
                }
            } else {
                treeNode.setIsParent(true);
            }
            list.add(treeNode);
        }
        return list;
    }

    @Override
    public List<Camera> findCameraByAreaIdVas(String areaId) {
        List<Camera> cameraList = baseMapper.selectList(new QueryWrapper<Camera>().eq("region", areaId).like("url", "vas://"));
        return cameraList;
    }

    @Override
    public List<Camera> findCameraByAreaId(String areaId) {
        List<Camera> cameraList = baseMapper.selectList(new QueryWrapper<Camera>().eq("region", areaId));
        return cameraList;
    }

    @Override
    public List<Camera> findVasCameraByAreaId(String areaId) {
        List<Camera> cameraList = baseMapper.selectList(new QueryWrapper<Camera>().eq("region", areaId).like("url", "vas://"));
        return cameraList;
    }

    @Override
    public Camera selectCameraById(Serializable id) {
        return baseMapper.selectById(id);
    }

    @Override
    public List<TreeNode> bulidCameraTree(List<CtrlUnit> areaList, List<Camera> cameraList) {
        List<TreeNode> list = new ArrayList<TreeNode>();
        for (CtrlUnit area : areaList) {
            TreeNode treeNode = new TreeNode();
            treeNode.setId(area.getUnitIdentity());
            treeNode.setName(area.getDisplayName());
            treeNode.setpId(area.getUnitParentId());
            treeNode.setUnitLevel(area.getUnitLevel().toString());
            treeNode.setNodeType(1);
            if (area.getIsLeaf() == 1) {
                treeNode.setIsParent(false);
            } else {
                treeNode.setIsParent(true);
            }
            if (!treeNode.getIsParent() && hasCameraByArea(area.getUnitIdentity())) {
                treeNode.setIsParent(true);
            }
            list.add(treeNode);
        }
        if (cameraList != null && cameraList.size() > 0) {
            for (Camera camera : cameraList) {
                TreeNode treeNode = new TreeNode();
                treeNode.setId(camera.getId().toString());
                treeNode.setName(camera.getName());
                treeNode.setpId(camera.getRegion());
                treeNode.setIsParent(false);
                treeNode.setNodeType(2);
                treeNode.setUrl(camera.getUrl());
                treeNode.setSerialnumber(camera.getSerialnumber());
                list.add(treeNode);
            }
        }
        return list;
    }

    @Override
    public FileBo uploadFileChuck(File file, CtrlUnitFile resource) {
        FileBo returnFlag = new FileBo();

        Map<String, Object> map = FTPUtils.getWindowsTranscodDiskSpace();

        if (MapUtil.isNotNull(map) && map.get("freeSpace") != null) {
            double cleanSpace = DbPropUtil.getDouble("ftp-clean-space", 100);
            double freeSpace = (Double) map.get("freeSpace");
            if (cleanSpace > freeSpace) {
                log.error("请注意，转码服务器磁盘空间已满！当前文件名fileName : " + file.getName() + ", fileId :" + resource.getId());
                returnFlag.setRetFlag("4");
                return returnFlag;
            }
        }
        returnFlag = saveFileToTransCodingFtpChuck(file, resource);
        returnFlag.setCtrlUnitFile(resource);
        return returnFlag;
    }

    public boolean hasCameraByArea(String areaId) {
        List<Camera> cameraList = findCameraByAreaId(areaId);
        if (cameraList.size() > 0) {
            return true;
        }
        return false;
    }


    @Override
    public R startPictureTask(String cameraId) {
        R result = R.ok();
        String resultJson = "";
        Map<String, Object> paramMap = new HashedMap();
        Map<String, Object> param = new HashedMap();
        String serialnumber = String.valueOf(IdUtils.getTaskId());
        paramMap.put("serialnumber", serialnumber);//任务序列号
        paramMap.put("cameraId", cameraId + "");
        Camera camera = baseMapper.selectById(cameraId);
        try {
            if (camera != null) {
                param.put("name", camera.getName());
                param.put("deviceId", camera.getExtcameraid());//设备ID
                param.put("ip", camera.getIp());
                param.put("port", camera.getPort1());
                param.put("username", camera.getAccount());
                String PASSWORD_KEY = DbPropUtil.getString("capture.service.password.key", "abcdef0123456789");
                String pwd = AesUtil.encrypt(camera.getPassword(), PASSWORD_KEY);
                param.put("password", pwd);
                param.put("deviceNo", camera.getExtcameraid());
                param.put("deviceNo", "deviceID20bytesTotal");
                param.put("type", VideoTaskConstant.Type.PICTURE);//任务类型
                param.put("userId", VideoTaskConstant.USER_ID.CAPTURE);
            }
            paramMap.put("param", com.alibaba.fastjson.JSONObject.toJSONString(param));

            resultJson = pictureObjectTaskService.startPictureTaskService(paramMap);
        } catch (Exception e) {
            log.error("服务调用失败..." + e.getMessage());
            e.printStackTrace();
        }
        Var resultVar = Var.fromJson(resultJson);
        result.put("msg", resultVar.getString("desc"));
        return result;
    }

    //TODO
    @Override
    public int deleteCameraByIds(String cameraIds) {
        int result = 0;
        if (StringUtils.isEmptyString(cameraIds)) {
            return 0;
        }
        String[] cameraIdArray = cameraIds.split(",");
        if (null != cameraIdArray && cameraIdArray.length > 0) {
            for (int i = 0; i < cameraIdArray.length; i++) {
                String id = cameraIdArray[i];

                long cameraId = Long.valueOf(id);

                // 1、 删除点位记录 及其对应的任务信息
                Camera camera = baseMapper.selectById(cameraId);
                if (camera != null) {
                    CameraCacheConstants.cleanCameraCacheByCameraType(String.valueOf(camera.getCameratype()));
                }
                baseMapper.deleteById(cameraId);
                List<VsdTaskRelation> cameraVsdTaskRelationList = vsdTaskRelationMapper.selectList(new QueryWrapper<VsdTaskRelation>().eq("camera_file_id", cameraId));
                if (ValidateHelper.isNotEmptyList(cameraVsdTaskRelationList)) {
                    for (VsdTaskRelation vsdTaskRelation : cameraVsdTaskRelationList) {
                        String serialnumber = vsdTaskRelation.getSerialnumber();
                        deleteCameraRelTaskByJManager(serialnumber);
                    }
                }
                // 2 、 删除离线视频及其关联的任务task信息
                List<CtrlUnitFile> ctrlUnitFileList = ctrlUnitFileMapper.selectList(new QueryWrapper<CtrlUnitFile>().eq("camera_id", cameraId));
                if (ValidateHelper.isNotEmptyList(ctrlUnitFileList)) {
                    for (CtrlUnitFile ctrlUnitFile : ctrlUnitFileList) {
                        // 删除该离线文件关联的任务记录:只删除vsd_task表，vsd_task_relation
                        // 保存着监控点点位，离线文件和结果的关系
                        List<VsdTaskRelation> filevVsdTaskRelationList = vsdTaskRelationMapper.selectList(new QueryWrapper<VsdTaskRelation>().eq("camera_file_id", cameraId));
                        if (ValidateHelper.isNotEmptyList(filevVsdTaskRelationList)) {
                            for (VsdTaskRelation vsdTaskRelation : filevVsdTaskRelationList) {
                                String serialnumber = vsdTaskRelation.getSerialnumber();
                                deleteCameraRelTaskByJManager(serialnumber);
                            }
                        }

                        // 调用转码文件删除接口
                        ftpTranscodeService.deleteTransCodeTask(ctrlUnitFile.getTranscodingId());
                    }
                }
                //删除监控点同时删除监控组里的监控点
                monitorGroupDetailMapper.delete(new QueryWrapper<MonitorGroupDetail>().eq("camera_id", cameraId));
                // 删除离线视频信息
                result = ctrlUnitFileMapper.delete(new QueryWrapper<CtrlUnitFile>().eq("camera_id", cameraId));
            }
        }
        return result;
    }

    /**
     * 调用Jmanager删除分析任务
     *
     * @param serialnumber
     */
    public void deleteCameraRelTaskByJManager(String serialnumber) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("serialnumber", serialnumber);
        try {
            String resultJson = videoObjextTaskService.deleteVsdTaskService(paramMap);
            if (StringUtils.isNotEmptyString(resultJson)) {
                Var reVar = Var.fromJson(resultJson);
                if (!"0".equals(reVar.getString("ret"))) {
                    log.info("调用Jmanager 删除监控点关联任务失败！ serialnumber: " + serialnumber);
                }
            }
        } catch (Exception e) {
            log.error("调用Jmanager 删除监控点关联任务出错！serialnumber: " + serialnumber);
        }

    }


    @Override
    public void deleteCameraAndTaskById(String cameraId) {
        //先删任务再删点位
        Camera camera = baseMapper.selectById(cameraId);
        if (camera == null) {
            return;
        }
        Long cameratype = camera.getCameratype();
        if (cameratype == null) {
            return;
        }
        if (cameratype == CameraConstants.CameraType.CAPTURE) {
            return;
        }
        List<VsdTaskRelation> taskRelations = vsdTaskRelationMapper.selectList(new QueryWrapper<VsdTaskRelation>().eq("camera_file_id", cameraId));
        if (taskRelations.size() > 0) {
            for (VsdTaskRelation relation : taskRelations) {
                String serialnumber = relation.getSerialnumber();
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("serialnumber", serialnumber);
                videoObjextTaskService.deleteVsdTaskService(paramMap);
            }
        }
        baseMapper.deleteById(cameraId);
        // 删除监控点，清除缓存
        CameraCacheConstants.cleanCameraCacheByCameraType(String.valueOf(camera.getCameratype()));
        //删除监控点同时删除监控组里的监控点
        monitorGroupDetailMapper.delete(new QueryWrapper<MonitorGroupDetail>().eq("camera_id", cameraId));

    }

    /*@Override
    public String getCameraSnapshotByUrl(String cameraId) {
        Camera camera = selectCameraById(cameraId);
        if (camera == null) {
            throw new VideoException("获取快照失败,请检查监控点是否存在！");
        }
        String cameraUrl = CameraConstants.transUrl(cfgMemPropsService.getWs2ServerIp(), cfgMemPropsService.getWs2ServerPort(), camera.getUrl(), "real_Platform", false);
        if (StringUtils.isEmptyString(cameraUrl)) {
            throw new VideoException("获取快照失败，请检查实时流地址是否正确！");
        }
        String camerasnapshot = ftpTranscodeService.getRealCameraSnapshot(cameraUrl);
        if (StringUtils.isNotEmptyString(camerasnapshot)) {
            Camera cameraUpdate = new Camera();
            cameraUpdate.setThumbNail(camerasnapshot);
            cameraUpdate.setId(camera.getId());
            updateById(cameraUpdate);
        }
        return camerasnapshot;
    }*/

    @Override
    public String getCameraSnapshotByUrl(String cameraId) {
        Camera camera = selectCameraById(cameraId);
        if (camera == null) {
            throw new VideoException("获取快照失败,请检查监控点是否存在！");
        }
        String camerasnapshot = getRealCameraUrl(camera.getUrl());
        if (StringUtils.isNotEmptyString(camerasnapshot)) {
            Camera cameraUpdate = new Camera();
            cameraUpdate.setThumbNail(camerasnapshot);
            cameraUpdate.setId(camera.getId());
            updateById(cameraUpdate);
        }
        return camerasnapshot;
    }

    //获取mas, rtsp快照
    public String getRealCameraUrl(String url) {
        String imageBase64 = "";

        if (url == null) {
            throw new VideoException("监控点URL地址");
        }
        if (url.indexOf("vas") == 0) {
            imageBase64 = MasSnapshotUtil.getMasSnapshotBase64(url);
        } else {
            imageBase64 = SnapshotUtil.catSnapshotBase64(url);
        }
        String imageUrl = "";
        if (imageBase64 != null) {
            imageUrl = videoObjextTaskService.saveImageToFdfs(imageBase64);
        }
        return imageUrl;
    }

    /**
     * 传远程ftp方式
     *
     * @param file
     * @param resource
     * @return
     * @throws Exception
     */
    private FileBo saveFileToTransCodingFtpChuck(File file, CtrlUnitFile resource) {

        FileBo fileBo = new FileBo();
        String fileOriginalName = file.getName();
        // 文件后缀
        String suffix = "";
        if (fileOriginalName.contains(".")) {
            suffix = fileOriginalName.substring(fileOriginalName.lastIndexOf('.'), fileOriginalName.length());
        }
        // 重命名文件名
        String fileName = resource.getId() + suffix;

        float mbSize = file.length() * 100 / 1048576;
        String transcodingId = "";
        boolean uploadFileFlag = false;
        if (DbPropUtil.getBoolean("windows-transcode-on-local")) {
            log.info("使用renameTo保存文件，fileName = " + fileName);

            String transcodePath = DbPropUtil.getString("windows-transcode-local-path");
            if (StringUtil.isNull(transcodePath)) {
                transcodePath = "/u2s/transcode/windows/www/";
            }
            uploadFileFlag = file.renameTo(new File(transcodePath + "upload/" + fileName));

        } else {
            log.info("使用FTPClient保存文件，fileName = " + fileName);
            // 建立连接
            FTPUtils fu = new FTPUtils();
            // 传到指定临时目录 ,上传到转码服务器ftp
            FTPClient judgeftp = fu.getJudgeFTP();
            FileInputStream vedioiss = null;
            try {
                vedioiss = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            uploadFileFlag = fu.uploadPic(judgeftp, "/", fileName, vedioiss);
            file.delete();

        }
        // 是否上传成功
        if (uploadFileFlag) {
            // 调转码接口 如果转码ID为空则直接返回失败
            transcodingId = getTranscodingId(fileName, resource.getVideoType());
            if (StringUtils.isEmptyString(transcodingId)) {
                fileBo.setRetFlag("2");
                return fileBo;
            }
        } else {
            log.info("uploadFtp is fail, fileName :" + fileName);
            fileBo.setRetFlag("1");
            return fileBo;
        }

        if (null != resource) {
            resource.setFileNameafterupload(fileName);
            resource.setFileName(fileOriginalName);
            resource.setFileSuffix(suffix);
            resource.setTranscodingId(transcodingId);
            // 文件大小的换算，图片用kb，视频用mb，原本是byte
            resource.setFileSize(String.valueOf(mbSize / 100.0));
        }
        return fileBo;
    }

    private String getTranscodingId(String fileName, String videoType) {
        String transcodingId = null;
        String addtranscodetaskReponse = ftpTranscodeService.addTranscodeTask(fileName, videoType);
        log.info("转码文件: " + fileName + ",视频类型: " + videoType + ",添加转码返回报文：" + addtranscodetaskReponse);
        if (StringUtils.isNotEmptyString(addtranscodetaskReponse)) {
            Map<String, Object> resultMap = com.keensense.admin.util.JSONUtil.getMap4Json(addtranscodetaskReponse);
            if (null != resultMap) {
                transcodingId = String.valueOf(resultMap.get("id"));
                if (StringUtils.isEmptyString(transcodingId)) {
                    log.error("获取的转码ID为空，请确保转码服务正常！");
                }
            }
        }
        return transcodingId;
    }

    @Override
    public List<CameraVo> queryCameraAll(String cameraType, String status, List<Point2D.Double> polygonList) {
        Map<String, String> cameraCacheMap = CameraCacheConstants.getCameraCacheKey(cameraType);
        String key = cameraCacheMap.get("key");
        String prevkey = cameraCacheMap.get("prevkey");

        /**
         * 每天更新一次缓存
         */
        List<CameraVo> data = (List<CameraVo>) EhcacheUtils.getItem(key);
        if (null == data || !data.isEmpty()) {

            //清除前一天缓存
            List<Camera> prevdata = (List<Camera>) EhcacheUtils.getItem(prevkey);
            if (null != prevdata && prevdata.size() > 0) {
                EhcacheUtils.removeItem(prevkey);
            }

            data = baseMapper.selectCameraByCameraType(cameraType);
            EhcacheUtils.putItem(key, data);
            if (null != data && data.size() > 0) {
                for (CameraVo datac : data) {
                    String devId = PatternUtil.getMatch(datac.getUrl(), "^vas://.+&devid=([^&]+)&.*$", Pattern.CASE_INSENSITIVE, 1);
                    if (StringUtil.isNotNull(devId)) {
                        datac.setAddress(devId);
                    }
                    if (datac.getRegion() == null) {
                        datac.setRegion("未知");
                    }
                }
            }
        }
        List<CameraVo> resultData = new ArrayList<CameraVo>();
        if (data != null && data.size() > 0) {
            for (CameraVo camera : data) {
                String lat = camera.getLatitude();
                String lon = camera.getLongitude();
                if (StringUtils.isNotEmptyString(lon) && StringUtils.isNotEmptyString(lat)) {
                    Point2D.Double point = PolygonUtil.pareseDouble(lon, lat);
                    if (point == null) {
                        continue;
                    }
                    boolean isContains = PolygonUtil.checkWithJdkGeneralPath(point, polygonList);
                    if (isContains) {
                        //状态 [0:离线，1:在线]
                        if (StringUtils.isEmptyString(status)) {
                            resultData.add(camera);
                        } else if (status.equals(String.valueOf(camera.getStatus()))) {
                            resultData.add(camera);
                        }

                    }
                }
            }
        }

        return resultData;
    }

    @Override
    public CameraVo selectByPrimaryKey(String id) {
        CameraVo camera = null;
        Camera camera1 = baseMapper.selectById(id);
        if (camera1 == null) {
            return null;
        }
        camera = EntityObjectConverter.getObject(camera1, CameraVo.class);
        String region = camera.getRegion();
        if (StringUtils.isNotEmptyString(region)) {
            CtrlUnit ctrlUnit = ctrlUnitMapper.selectOne(new QueryWrapper<CtrlUnit>().eq("unit_identity", region));
            if (null != ctrlUnit) {
                if (StringUtils.isNotEmptyString(ctrlUnit.getDisplayName())) {
                    camera.setRegionName(ctrlUnit.getDisplayName());
                } else {
                    camera.setRegionName(ctrlUnit.getUnitName());
                }
            }
        }
        return camera;
    }

    @Override
    public String handleQueryCameraIds(List<String> cameraAndAreaIdList) {
        {
            String cameraIds = "";
            if (ListUtil.isNotNull(cameraAndAreaIdList)) {
                Map<Long, Integer> cameraIdMap = new HashMap<Long, Integer>();
                Map<String, String[]> resultMap = getCameraAndAreaArr(cameraAndAreaIdList);
                if (resultMap.containsKey("areaArray")) {
                    for (String areaId : resultMap.get("areaArray")) {
                        List<Camera> tmpCameraList = findCameraByAreaParentId(areaId);
                        for (Camera camera : tmpCameraList) {
                            cameraIdMap.put(camera.getId(), 1);
                        }
                    }
                }
                if (resultMap.containsKey("cameraArray")) {
                    for (String cameraIdObj : resultMap.get("cameraArray")) {
                        cameraIdMap.put(StringUtil.getLong(cameraIdObj), 1);
                    }
                }
                Iterator<Long> cameraIdIt = cameraIdMap.keySet().iterator();
                while (cameraIdIt.hasNext()) {
                    long tmpCameraId = cameraIdIt.next();
                    cameraIds += tmpCameraId + ",";
                }

                cameraIds = cameraIds.length() > 1 ? cameraIds.substring(0, cameraIds.length() - 1) : cameraIds;
            }
            return cameraIds;
        }
    }

    public Map<String, String[]> getCameraAndAreaArr(List<String> cameraIdList) {
        Map<String, String[]> resultMap = new HashMap<String, String[]>();
        List<String> cameraList = new ArrayList<>();
        List<String> areaList = new ArrayList<>();
        Set<String> unitSet = new HashSet<>();
        if (cameraIdList.size() > 0) {
            for (int i = 0; i < cameraIdList.size(); i++) {
                String cameraAreaObj = cameraIdList.get(i);
                if (cameraAreaObj.startsWith("a_")) {
                    areaList.add(cameraAreaObj.substring(2));
                } else if (cameraAreaObj.startsWith("c_")) {
                    cameraList.add(cameraAreaObj.substring(2));
                } else if (cameraAreaObj.startsWith("m_")) {
                    List<String> cameraMon = monitorGroupDetailMapper.selectCameraByMonitorGroupId(Long.valueOf(cameraAreaObj.substring(2)));
                    cameraList.addAll(cameraMon);
                } else {
                    cameraList = cameraIdList;
                }
            }
            int cSize = cameraList.size();
            int aSize = areaList.size();
            if (aSize > 0) {
                for (int i = 0; i < aSize; i++) {
                    List<CtrlUnit> unitList = queryUnitByParentId(areaList.get(i));
                    for (CtrlUnit cuObj : unitList) {
                        String unitId = cuObj.getUnitIdentity();
                        if (hasCameraByArea(unitId)) {
                            unitSet.add(unitId);
                        }
                    }
                }
                String[] areaIdStringArr = unitSet.toArray(new String[unitSet.size()]);
                resultMap.put("areaArray", areaIdStringArr);
            }
            if (cSize > 0) {
                String[] cameraIdIntArr = new String[cSize];
                for (int i = 0; i < cSize; i++) {
                    cameraIdIntArr[i] = cameraList.get(i);
                }
                resultMap.put("cameraArray", cameraIdIntArr);
            }
            return resultMap;
        }
        return null;
    }

    public List<Camera> findCameraByAreaParentId(String parentId) {
        List<Camera> carmeraList = baseMapper.selectCameraListWithParentId("%" + parentId + "%");
        return carmeraList;
    }

    @Override
    public List<CtrlUnit> queryUnitByParentId(String parentId) {
        List<CtrlUnit> unitList = ctrlUnitMapper.selectList(new QueryWrapper<CtrlUnit>().eq("unit_number", parentId));
        return unitList;
    }

    @Override
    public R updateCamera(CameraRequest cameraRequest) {
        Long cameraId = cameraRequest.getId();
        if (null == cameraId) {
            return R.error("监控点Id不能为空");
        }
        Camera dataCamera = baseMapper.selectById(cameraId);
        if (dataCamera != null) {
            try {
                cameraRequest.setId(Long.valueOf(cameraId));
                int updateCount = baseMapper.updateById(EntityObjectConverter.getObject(cameraRequest, Camera.class));
                if (updateCount > 0) {
                    CameraCacheConstants.cleanCameraCacheByCameraType(String.valueOf(dataCamera.getCameratype()));
                    List<VsdTaskRelation> relationList = vsdTaskRelationMapper.selectList(new QueryWrapper<VsdTaskRelation>().eq("camera_file_id", cameraId).in("from_type", VideoTaskConstant.TASK_TYPE.REAL, VideoTaskConstant.TASK_TYPE.IPC, VideoTaskConstant.TASK_TYPE.CAPTURE));
                    for (VsdTaskRelation vsdTaskRelation : relationList) {
                        //更新完后停止任务
                        vsdTaskService.stopRealtimeTask(vsdTaskRelation.getSerialnumber());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return R.error(CommonConstants.UPDATE_FAILURE);
            }
        }
        return R.ok();
    }

    @Override
    public R exportCameraExcel(MultipartFile file, String cameraType) {
        try {
            OutputStream out = null;
            /*String fileUrl = fileJson.split("base64,")[1];
            BASE64Decoder decoder = new BASE64Decoder();
            decoder.decodeBuffer(fileUrl);
            byte[] bytes = decoder.decodeBuffer(fileUrl);
            for (int j = 0; j < bytes.length; ++j) {
                if (bytes[j] < 0) {// 调整异常数据
                    bytes[j] += 256;
                }
            }*/
            byte[] bytes = file.getBytes();
            String root = FTPUtils.getRootPath("/");
            // 声明文件保存的服务器路径
            String path = root + "temp" + File.separator;
            // 读取到的数据长度
            int len;
            // 输出的文件流保存到本地文件
            File tempFile = new File(path);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            // 将excel文件写入到webapp/temp下
            String fileName = file.getOriginalFilename();
            File excel = new File(path + fileName);
            out = new FileOutputStream(excel);
            out.write(bytes);
            out.flush();
            out.close();

            List<CameraRequest> cameraList = ExcelHandleUtils.readExcelFromCamera(path + fileName);

            if (cameraList.size() <= 0) {
                return R.error("该excel文档没有数据");
            }
            for (CameraRequest cameraRequest : cameraList) {
                Long cameraId = Long.parseLong(RandomUtils.get8RandomValiteCode(12));

                if (cameraType == null) {
                    cameraRequest.setCameratype(CameraConstants.CameraType.RTSP);
                } else {
                    cameraRequest.setCameratype(Long.valueOf(cameraType));
                }
                Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
                Matcher longitude = pattern.matcher(cameraRequest.getLongitude());
                Matcher latitude = pattern.matcher(cameraRequest.getLatitude());
                if (longitude.find() || latitude.find()) {
                    throw new VideoException("导入Excel模板格式错误");
                }
                cameraRequest.setType(1L);
                cameraRequest.setId(cameraId);
                cameraRequest.setCreateTime(new Date());
                Camera camera = EntityObjectConverter.getObject(cameraRequest, Camera.class);
                int insert = baseMapper.insert(camera);
                if (insert > 0) {
                    CameraCacheConstants.cleanCameraCacheByCameraType(String.valueOf(cameraRequest.getCameratype()));
                }
            }
            FileUtils.deleteFileOrDirector(tempFile);
        } catch (Exception e) {
            return R.error("该excel文档数据错误");
        }
        return R.ok();
    }


    @Override
    public R submitCamera(CameraRequest camera) {
        R result = new R();
        Long cameraId = System.currentTimeMillis();
        camera.setId(cameraId);
        camera.setStatus(CameraConstants.CameraStatus.START);
//        camera.setType(1L);//固定参数
        if (camera.getRegion() == null) {
            Camera camera1 = baseMapper.selectOne(new QueryWrapper<Camera>().eq("name", "默认监控点"));
            if (camera1 != null) {
                camera.setRegion(camera1.getRegion());
            }
        }
        int insert = baseMapper.insert(EntityObjectConverter.getObject(camera, Camera.class));
        if (insert > 0) {
            CameraCacheConstants.cleanCameraCacheByCameraType(String.valueOf(camera.getCameratype()));
        }
        result.put("camera", camera);
        return result;
    }

    @Override
    public R addVSDTask(TaskParamBo taskParamBo) {
        Integer fromType = -1;
        String cameraFileId = taskParamBo.getCameraFileId();
        String filefromtype = taskParamBo.getFilefromtype();
        if (StringUtils.isNotEmpty(filefromtype)) {
            fromType = Integer.parseInt(filefromtype);
        }
        String autoAnalysis = taskParamBo.getAutoAnalysis();
        String type = taskParamBo.getType();
        String param = taskParamBo.getParam();

        R result = R.ok();
        VsdTaskVo vsdTask = new VsdTaskVo();
        //不自动启动分析，直接返回
        if ("0".equals(autoAnalysis)) {
            return R.ok();
        }
        if (StringUtils.isEmptyString(cameraFileId)) {
            return R.error("id 不能为空");
        }
        if (StringUtils.isEmptyString(type)) {
            return R.error("type不能为空");
        }
        CameraVo camera = baseMapper.selectByPrimaryKey(Long.valueOf(cameraFileId));
        if (camera != null && camera.getCameratype() == CameraConstants.CameraType.CAPTURE) {
            return startPictureTask(cameraFileId);
        }
        if (VideoTaskConstant.FROM_TYPE.ONLINE_VIDEO == fromType) {
            List<VsdTaskRelation> relations = vsdTaskRelationService.list(new QueryWrapper<VsdTaskRelation>().eq("camera_file_id", cameraFileId).eq("from_type", filefromtype));
            if (relations.size() > 0) {
                return R.error("任务已经存在");
            }
        }

        String serialnumber = vsdTaskRelationService.getSerialnumber();
        vsdTask.setType(type);
        vsdTask.setFilefromtype(fromType);
        vsdTask.setParam(param);
        vsdTask.setCameraFileId(cameraFileId);
        String url = camera.getUrl();
        Map<String, Object> paramMap = new HashedMap();
        paramMap.put("serialnumber", serialnumber);//任务序列号
        paramMap.put("type", "objext");//任务类型
        paramMap.put("url", url);//视频路径
        paramMap.put("cameraId", cameraFileId);
        if (url.startsWith("rtsp://") || url.startsWith("rtmp://")) {
            paramMap.put("userId", VideoTaskConstant.USER_ID.IPC);
        } else {
            paramMap.put("userId", VideoTaskConstant.USER_ID.REALTIME_VIDEO);
        }

        if (camera != null) {
            paramMap.put("name", camera.getName());
            paramMap.put("deviceId", camera.getExtcameraid());//设备ID
        }
        String resultJson = videoObjextTaskService.addVsdTaskService(paramMap, true);
        JSONObject resultFormat = JSON.parseObject(resultJson);
        String ret = resultFormat.getString("ret");
        String desc = resultFormat.getString("desc");
        if ("0".equals(ret)) {
            result.put("taskInfo", vsdTask);
            return result;
        } else {
            return R.error(desc);
        }
    }

    /**
     * 根据监控点名称查询监控点树
     */
    @Override
    public List<TreeNode> selectCameraByName(Map<String, String> paramMap) {
        List<TreeNode> list = new ArrayList<>();
        List<TreeNode> reList = baseMapper.selectCameraByName(paramMap);
        if (paramMap.containsKey("showType")) {
            return reList;
        }

        // 将所有从longnumber中提取的pid添加到当前到pidList中
        List<String> unitIdentityList = new ArrayList<>();
        List<String> pidList = new ArrayList<>();

        TreeNode node = null;
        for (TreeNode map : reList) {
            node = new TreeNode();
            node.setId(map.getId());
            node.setUnitLevel(map.getUnitLevel());
            node.setpId(StringUtils.isEmptyString(map.getpId()) ? "0" : map.getpId());
            node.setName(map.getName());
            node.setNodeType(2);
            node.setIsParent(map.getIsParent());
            list.add(node);

            //TODO 根据监控点pid查询所有的Longnumber
            //String longnumber = cameraMapper.selectParentOrgbyCameraPid("%"+ node.getpId() + "%");
            String longnumber = "";
            CtrlUnit unit = ctrlUnitMapper.selectOne(new QueryWrapper<CtrlUnit>().eq("unit_identity", node.getpId()));
            if (unit != null) {
                longnumber = unit.getLongNumber();
            }

            if (StringUtils.isEmptyString(longnumber) && StringUtils.isNotEmptyString(node.getpId())) {
                List<String> treeNodePid = new ArrayList<>();
                // 点位同步数据没有longnumber 树结构，如 0000000022!522328!5223280!5223286
                String pid = node.getpId();
                treeNodePid.add(pid);
                do {
                    CtrlUnit ctrlUnit = ctrlUnitMapper.selectOne(new QueryWrapper<CtrlUnit>().eq("unit_identity", pid));
                    pid = ctrlUnit.getUnitParentId();
                    treeNodePid.add(pid);

                } while (StringUtils.isNotEmptyString(pid));

                for (int i = (treeNodePid.size() - 1); i >= 0; i--) {
                    longnumber += treeNodePid.get(i) + "!";
                }

                longnumber = longnumber.substring(0, longnumber.length() - 1);
            }

            pidList.add(longnumber);
        }

        HashSet<String> set = new HashSet<>();
        for (String longnumber : pidList) {
            if (StringUtils.isNotEmptyString(longnumber)) {
                String[] strArray = longnumber.split("!");
                for (String str : strArray) {
                    set.add(str);
                }
            }

        }

        if (CollectionUtils.isNotEmpty(set)) {
            unitIdentityList.addAll(set);
            Map<String, List<String>> param = new HashMap<>();
            param.put("unitIdentityList", unitIdentityList);
            List<TreeNode> queryList = baseMapper.selectCameraTreeByUnitIdentity(param);
            for (TreeNode map : queryList) {
                node = new TreeNode();
                node.setId(map.getId());
                node.setUnitLevel(map.getUnitLevel());
                node.setpId(StringUtils.isEmptyString(map.getpId()) ? "0" : map.getpId());
                node.setName(map.getName());
                node.setIsParent(map.getIsParent());
                node.setNodeType(1);
                list.add(node);
            }
        }

        return list;
    }

    @Override
    public List<Camera> selectTumbNailCameraList() {
        return baseMapper.selectList(new QueryWrapper<Camera>().isNull("thumb_nail").isNotNull("url"));
    }

    @Override
    public List<Camera> selectCameraByName(String name, String id) {
        return baseMapper.selectList(new QueryWrapper<Camera>().like("name", name).eq(id != null, "region", id));
    }

    @Override
    public List<Camera> selectVasCameraByName(String name, String id) {
        return baseMapper.selectList(new QueryWrapper<Camera>().like("name", name).eq(id != null, "region", id).like("url", "vas://"));
    }

    @Override
    public List<Camera> queryCameraListByArea(String id) {
        List<Camera> mList = baseMapper.selectList(new QueryWrapper<Camera>().eq("region", id));
        List<CtrlUnit> cList = ctrlUnitMapper.selectList(new QueryWrapper<CtrlUnit>().eq("unit_parent_id", id));
        if (cList != null && !cList.isEmpty()) {
            for (CtrlUnit ctrlUnit : cList) {
                List<Camera> mList_1 = this.queryCameraListByArea(ctrlUnit.getUnitIdentity());
                mList.addAll(mList_1);
            }
        }
        return mList;
    }

    @Override
    public List<TreeNode> bulidCameraTreeByCtrlUnit(List<CtrlUnit> mList, List<Camera> cList) {
        List<TreeNode> list = new ArrayList<TreeNode>();
        for (CtrlUnit area : mList) {
            TreeNode treeNode = new TreeNode();
            treeNode.setId(area.getUnitIdentity());
            treeNode.setName(area.getDisplayName());
            treeNode.setpId(area.getUnitParentId());
            treeNode.setUnitLevel(area.getUnitLevel().toString());
            treeNode.setNodeType(1);
            if (area.getIsLeaf() == 1) {
                treeNode.setIsParent(false);
            } else {
                treeNode.setIsParent(true);
            }
            if (!treeNode.getIsParent() && hasCameraByArea(area.getUnitIdentity())) {
                treeNode.setIsParent(true);
            }
            if (cList != null && !cList.isEmpty()) {
                for (Camera camera : cList) {
                    if (camera.getRegion().equals(area.getUnitIdentity())) {
                        TreeNode treeNode_1 = new TreeNode();
                        treeNode_1.setId(camera.getId().toString());
                        treeNode_1.setName(camera.getName());
                        treeNode_1.setpId(camera.getRegion());
                        treeNode_1.setIsParent(false);
                        treeNode_1.setNodeType(2);
                        treeNode_1.setUrl(camera.getUrl());
                        treeNode_1.setSerialnumber(camera.getSerialnumber());
                        treeNode_1.setSlaveip(camera.getSlaveip());
                        if (null == treeNode.getTreeNodeList()) {
                            List<TreeNode> treeList = new ArrayList<>();
                            treeList.add(treeNode_1);
                            treeNode.setTreeNodeList(treeList);
                        } else {
                            treeNode.getTreeNodeList().add(treeNode_1);
                        }
                    }
                }
            }
            if (list != null && !list.isEmpty() && area.getUnitLevel() > 1) {
                for (TreeNode node : list) {
                    node.setTreeNodeList(bulidTreeNode(node, treeNode));
                }
            }
            if (area.getUnitLevel() == 1) {
                list.add(treeNode);
            }
        }
        return list;
    }

    public List<TreeNode> bulidTreeNode(TreeNode node, TreeNode treeNode) {
        List<TreeNode> list = node.getTreeNodeList();
        if (list == null) {
            list = new ArrayList<>();
        }
        if (node.getId().equals(treeNode.getpId())) {
            list.add(treeNode);
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getNodeType() == 1) {
                    if (list.get(i).getId() == treeNode.getpId()) {
                        list.get(i).getTreeNodeList().add(treeNode);
                        break;
                    }
                    list.get(i).setTreeNodeList(bulidTreeNode(list.get(i), treeNode));
                }
            }
        }
        return list;
    }

    @Override
    //@DataSource(value = DataSourceType.SLAVE)
    public List<Camera> selectJdCamera() {
        return baseMapper.selectJdCamera();
    }

}
