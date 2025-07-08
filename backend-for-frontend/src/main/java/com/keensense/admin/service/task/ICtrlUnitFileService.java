package com.keensense.admin.service.task;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.admin.dto.Chunk;
import com.keensense.admin.entity.task.CtrlUnitFile;
import com.keensense.admin.request.Plupload;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 21:15:12
 */
public interface ICtrlUnitFileService extends IService<CtrlUnitFile> {
    /**
     * 查询附件CtrlUnitFile列表(分页)
     *
     * @param page 分页信息
     * @param map  查询条件
     * @return
     */
    Page<CtrlUnitFile> queryOfflineVideo(Page<CtrlUnitFile> page, Map<String, Object> map);

    /**
     * 批量删除文件
     *
     * @param fileIds id1,id2
     * @return
     */
    int deleteFiles(String fileIds);

    /**
     * 文件分块上传，将所有快存到同一临时文件中
     *
     * @param plupload
     * @return
     */
    File saveUploadFileToTemp(Plupload plupload, String token, MultipartFile multipartFile) throws Exception;

    /**
     * 文件分块上传，将所有快存到同一临时文件中
     *
     * @param chunk
     * @return
     * @throws Exception
     */
    File saveUploadFileToTemp(Chunk chunk) throws Exception;

    /**
     * 插入视频
     *
     * @param resource
     * @return
     */
    boolean insertCameraMedia(CtrlUnitFile resource);

    /**
     * 根据文件ID 查询视频文件转码进度等信息
     *
     * @param fileId
     * @return
     */
    CtrlUnitFile updateTranscodeInfoById(String fileId);

    /**
     * 查询详细信息
     *
     * @param fileId
     * @return
     */
    CtrlUnitFile queryOfflineVideoByFileId(String fileId);

    /**
     * 查询未转码完成数据
     *
     * @return
     */
    List<CtrlUnitFile> selectAllUntrascodingVideos();

    /**
     * 查询已删除文件
     *
     * @return
     */
    List<CtrlUnitFile> selectAllDelVideos();


    Boolean updateCtrlUnitFileTranscodingProgress(CtrlUnitFile ctrlUnitFile);

    /**
     * 查询需要添加后台任务的文件
     *
     * @return
     */
    List<CtrlUnitFile> selectCtrlUnitFileByTask();

    void clearFileDate(String id);

    CtrlUnitFile selectRecentInterest(String cameraId);
}

