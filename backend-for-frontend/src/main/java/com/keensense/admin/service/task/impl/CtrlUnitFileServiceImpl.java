package com.keensense.admin.service.task.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.dto.Chunk;
import com.keensense.admin.entity.task.Camera;
import com.keensense.admin.entity.task.CtrlUnitFile;
import com.keensense.admin.entity.task.VsdTaskRelation;
import com.keensense.admin.mapper.task.CameraMapper;
import com.keensense.admin.mapper.task.CtrlUnitFileMapper;
import com.keensense.admin.request.Plupload;
import com.keensense.admin.service.ext.FtpTranscodeService;
import com.keensense.admin.service.ext.VideoObjextTaskService;
import com.keensense.admin.service.task.ICtrlUnitFileService;
import com.keensense.admin.service.task.IVsdTaskRelationService;
import com.keensense.admin.service.task.IVsdTaskService;
import com.keensense.admin.util.FTPUtils;
import com.keensense.admin.util.JSONUtil;
import com.loocme.sys.util.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("ctrlUnitFileService")
public class CtrlUnitFileServiceImpl extends ServiceImpl<CtrlUnitFileMapper, CtrlUnitFile> implements ICtrlUnitFileService {

    @Autowired
    private IVsdTaskRelationService vsdTaskRelationService;

    @Autowired
    private VideoObjextTaskService videoObjextTaskService;

    @Autowired
    private CameraMapper cameraMapper;

    @Autowired
    private FtpTranscodeService ftpTranscodeService;

    /**
     * 查询附件CtrlUnitFile列表(分页)
     *
     * @param page 分页信息
     * @param map  查询条件
     * @return
     */
    @Override
    public Page<CtrlUnitFile> queryOfflineVideo(Page<CtrlUnitFile> page, Map<String, Object> map) {
        List<CtrlUnitFile> records = baseMapper.queryOfflineVideo(page, map);
        for (CtrlUnitFile ctrlUnitFile : records) {
            Camera camera = cameraMapper.selectById(ctrlUnitFile.getCameraId());
            if (null != camera) {
                ctrlUnitFile.setCameraName(camera.getName());
            }
            if (ctrlUnitFile.getFilePathafterupload() != null && ctrlUnitFile.getFilePathafterupload().startsWith("http")) {
                ctrlUnitFile.setFilePathafterupload(ctrlUnitFile.getFilePathafterupload());
                ctrlUnitFile.setThumbNail(ctrlUnitFile.getThumbNail());
            }
        }
        page.setRecords(records);
        return page;
    }

    @Override
    public int deleteFiles(String fileIds) {
        int result = 0;
        if (StringUtils.isNotEmpty(fileIds)) {
            String[] fileIdArray = fileIds.split(",");
            if (null != fileIdArray && fileIdArray.length > 0) {
                for (int i = 0; i < fileIdArray.length; i++) {
                    if (StringUtils.isNotEmpty(fileIdArray[i])) {
                        CtrlUnitFile delete = new CtrlUnitFile();
                        delete.setId(Long.parseLong(fileIdArray[i]));
                        delete.setDelFlag(0);
                        baseMapper.updateById(delete);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void clearFileDate(String id) {
        CtrlUnitFile ctrlUnitFile = baseMapper.selectById(id);
        if (ctrlUnitFile == null) {
            return;
        }
        // 删除该离线文件关联的任务记录:只删除vsd_task表，vsd_task_relation 保存着监控点点位，离线文件和结果的关系
        List<VsdTaskRelation> vsdTaskRelationList = vsdTaskRelationService.list(new QueryWrapper<VsdTaskRelation>().eq("camera_file_id", id));
        if (vsdTaskRelationList == null || vsdTaskRelationList.isEmpty()) {
            ftpTranscodeService.deleteTransCodeTask(ctrlUnitFile.getTranscodingId());
            String retMsg = ftpTranscodeService.getTranscodingProgress(ctrlUnitFile.getTranscodingId());
            JSONObject ret = JSONObject.parseObject(retMsg);
            if (ret.size() > 0 && "3".equals(ret.getString("ret"))) {
                baseMapper.deleteById(id);
            }
        } else {
            for (VsdTaskRelation vsdTaskRelation : vsdTaskRelationList) {
                String serialnumber = vsdTaskRelation.getSerialnumber();
                deleteVsdTaskAndRela(serialnumber);
            }
        }
    }

    @Override
    public File saveUploadFileToTemp(Plupload plupload, String token, MultipartFile multipartFile) throws Exception {
        String fileNane = multipartFile.getOriginalFilename();
        int chunks = plupload.getChunks();//用户上传文件被分隔的总块数
        int nowChunk = plupload.getChunk();//当前块，从0开始
        String filePath = FTPUtils.getRootPath("/") + "pluploadDir";
        log.info("filePath:" + filePath);
        if (com.keensense.admin.util.StringUtils.isNotEmptyString(token)) {
            filePath = filePath + File.separator + token;    //根据token创建不同的临时文件夹,解决并发上传窜并的问题
        }
        File pluploadDir = new File(filePath);
        if (!pluploadDir.exists()) {
            pluploadDir.mkdirs();
        }
        //plupload.setMultipartFile(multipartFile);//手动向Plupload对象传入MultipartFile属性值
        String targetFilePath = pluploadDir + File.separator + fileNane;
        if (com.keensense.admin.util.StringUtils.isNotEmptyString(token)) {
            targetFilePath = pluploadDir.getParentFile() + File.separator + fileNane;
        }
        File targetFile = new File(targetFilePath);//新建目标文件，只有被流写入时才会真正存在
        if (chunks > 1) {//用户上传资料总块数大于1，要进行合并
            File tempFile = new File(pluploadDir.getPath() + File.separator + fileNane);
            //第一块直接从头写入，不用从末端写入
            savePluploadFile(multipartFile.getInputStream(), tempFile, nowChunk == 0 ? false : true);

            if (chunks - nowChunk == 1) {//全部块已经上传完毕，此时targetFile因为有被流写入而存在，要改文件名字
                tempFile.renameTo(targetFile);
                return targetFile;
            }
        } else {
            //只有一块，就直接拷贝文件内容
            multipartFile.transferTo(targetFile);
            return targetFile;
        }
        return null;
    }

    @Override
    public boolean insertCameraMedia(CtrlUnitFile resource) {
        return retBool(baseMapper.insert(resource));
    }

    @Override
    public CtrlUnitFile updateTranscodeInfoById(String fileId) {
        if (StringUtils.isEmpty(fileId) || Long.valueOf(fileId) <= 0) {
            return null;
        }
        return baseMapper.selectById(fileId);
    }


    /**
     * 删除任务
     *
     * @param serialnumber
     * @return
     */
    public int deleteVsdTaskAndRela(String serialnumber) {
        int returnCode = 0;
        if (com.keensense.admin.util.StringUtils.isNotEmptyString(serialnumber)) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("serialnumber", serialnumber);
            String delResp = videoObjextTaskService.deleteVsdTaskService(paramMap);
            JSONObject json = JSONObject.parseObject(delResp);
            if ("0".equals(json.getString("ret")) || "7".equals(json.getString("ret"))) {
                vsdTaskRelationService.getBaseMapper().delete(new QueryWrapper<VsdTaskRelation>().eq("serialnumber", serialnumber));
            }
        }

        return returnCode;
    }

    private void savePluploadFile(InputStream inputStream, File tempFile, boolean flag) {
        OutputStream outputStream = null;
        try {
            if (flag == false) {
                //从头写入
                outputStream = new BufferedOutputStream(new FileOutputStream(tempFile));
            } else {
                //从末端写入
                outputStream = new BufferedOutputStream(new FileOutputStream(tempFile, true));
            }
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = (inputStream.read(bytes))) > 0) {
                outputStream.write(bytes, 0, len);
            }
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public CtrlUnitFile queryOfflineVideoByFileId(String fileId) {
        return baseMapper.queryOfflineVideoByFileId(fileId);
    }

    /**
     * 忽略转码成功：1和转码失败3状态
     *
     * @return
     */
    @Override
    public List<CtrlUnitFile> selectAllUntrascodingVideos() {
        return baseMapper.selectList(new QueryWrapper<CtrlUnitFile>().eq("file_type", 2).isNull("del_flag").isNotNull("transcoding_id").and(i -> i.notIn("transcode_status", 1, 3).or().isNull("transcode_status")));
    }

    @Override
    public List<CtrlUnitFile> selectAllDelVideos() {
        return baseMapper.selectList(new QueryWrapper<CtrlUnitFile>().eq("file_type", 2).isNotNull("del_flag"));
    }

    @Override
    public Boolean updateCtrlUnitFileTranscodingProgress(CtrlUnitFile transcodeCtrlUnitFile) {
        //新建对象做数据更新
        CtrlUnitFile ctrlUnitFile = new CtrlUnitFile();
        ctrlUnitFile.setId(transcodeCtrlUnitFile.getId());
        ctrlUnitFile.setTranscodingId(transcodeCtrlUnitFile.getTranscodingId());
        boolean isSuccess = true;
        String respTransProgress = ftpTranscodeService.getTranscodingProgress(ctrlUnitFile.getTranscodingId());

        if (com.keensense.admin.util.StringUtils.isEmptyString(respTransProgress)) {
            String transcodingId = ctrlUnitFile.getTranscodingId();
            log.error("respTransProgress is empty or null, transcodingId: " + transcodingId);
            return false;
        }

        Map<String, Object> respTransProgressMap = null;
        try {
            respTransProgressMap = JSONUtil.getMap4Json(respTransProgress);
        } catch (Exception e) {
            log.error("JSON转换失败,error msg  : " + e.toString());
            return false;
        }

        // 对JSON解析出对应的值
        String progress = null;
        int transcodeStatus = 0;
        String snapshot = null;
        String filePathafterupload = null;
        String fileLocalPath = null;
        String resolution = null;
        String fileFtpPath = null;
        int frameCount = 0;
        int framerate = 0;

        if (null != respTransProgressMap) {
            progress = String.valueOf(respTransProgressMap.get("progress"));
            transcodeStatus = MapUtil.getInteger(respTransProgressMap, "status");
            ctrlUnitFile.setTranscodeStatus(transcodeStatus);

            ctrlUnitFile.setTranscodingProgress(progress);
            ctrlUnitFile.setTranscodingProgressStr(progress + "%");
        } else {
            ctrlUnitFile.setTranscodingProgressStr("0%");
        }
        int ret = MapUtil.getInteger(respTransProgressMap, "ret");
        if (ret == 0) {
            // 完成进度则更新文件内容： 快照  文件url 以及总帧数
            if ("100".equals(progress)) {
                snapshot = String.valueOf(respTransProgressMap.get("snapshot"));
                filePathafterupload = String.valueOf(respTransProgressMap.get("url"));
                fileFtpPath = String.valueOf(respTransProgressMap.get("ftp_url"));
                frameCount = MapUtil.getInteger(respTransProgressMap, "framecount");
                framerate = MapUtil.getInteger(respTransProgressMap, "framerate");
                fileLocalPath = String.valueOf(respTransProgressMap.get("filepath"));
                resolution = String.valueOf(respTransProgressMap.get("resolution"));

                // 更新数据库字段
                ctrlUnitFile.setFrameCount(frameCount);
                ctrlUnitFile.setFramerate(framerate);
                ctrlUnitFile.setTranscodingProgress(progress);
                ctrlUnitFile.setThumbNail(snapshot);
                ctrlUnitFile.setFileFtpPath(fileFtpPath);
                ctrlUnitFile.setFilePathafterupload(filePathafterupload);
                ctrlUnitFile.setFileLocalPath(fileLocalPath);
                ctrlUnitFile.setResolution(resolution);
                baseMapper.updateById(ctrlUnitFile);
            } else {
                baseMapper.updateById(ctrlUnitFile);
            }
        } else {
            ctrlUnitFile.setTranscodeStatus(5);
            baseMapper.updateById(ctrlUnitFile);
        }
        return isSuccess;
    }

    @Override
    public List<CtrlUnitFile> selectCtrlUnitFileByTask() {
        return baseMapper.selectCtrlUnitFileByTask();
    }

    @Override
    public CtrlUnitFile selectRecentInterest(String cameraId) {
        return baseMapper.selectRecentInterest(cameraId);
    }

    @Override
    public File saveUploadFileToTemp(Chunk chunk) throws Exception {
        return null;
    }
}
