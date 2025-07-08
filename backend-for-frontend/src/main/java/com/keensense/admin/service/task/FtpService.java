package com.keensense.admin.service.task;

import com.keensense.admin.dto.FTPResultBo;
import com.keensense.admin.dto.FileBo;
import com.keensense.admin.dto.FileInfo;
import com.keensense.admin.entity.task.CtrlUnitFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FtpService {

    FTPResultBo uploadLocalFileToFtp(FileInfo file, String ftpPath);

    /**
     * 自动框选后截取图片
     */
    FileBo editImage(Integer x, Integer y, Integer width, Integer height, String iamgePath) throws Exception;

    String addTranscodeTask(String fileName, String videoType);
}

