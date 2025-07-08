package com.keensense.admin.service.task.impl;

import com.keensense.admin.dto.FTPResultBo;
import com.keensense.admin.dto.FileBo;
import com.keensense.admin.dto.FileInfo;
import com.keensense.admin.dto.OperateImage;
import com.keensense.admin.entity.task.CtrlUnitFile;
import com.keensense.admin.service.ext.FtpTranscodeService;
import com.keensense.admin.service.ext.VideoObjextTaskService;
import com.keensense.admin.service.task.CaseService;
import com.keensense.admin.service.task.FtpService;
import com.keensense.admin.service.task.ICtrlUnitFileService;
import com.keensense.admin.util.DbPropUtil;
import com.keensense.admin.util.FTPUtils;
import com.keensense.admin.util.FileUtils;
import com.keensense.admin.util.JSONUtil;
import com.keensense.admin.util.RandomUtils;
import com.keensense.admin.util.StringUtils;
import com.keensense.common.exception.VideoException;
import com.keensense.common.util.ImageUtils;
import com.loocme.sys.util.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class FtpServiceImpl implements FtpService {

    @Resource
    private ICtrlUnitFileService ctrlUnitFileService;

    @Autowired
    private FtpTranscodeService ftpTranscodeService;

    @Resource
    private CaseService caseService;

    @Autowired
    private VideoObjextTaskService videoObjextTaskService;

    /**
     * 上传离线视频文件到转码服务器
     */
    @Override
    public FTPResultBo uploadLocalFileToFtp(FileInfo file, String ftpPath) {

        File urlFile = new File(file.getLocation());
        if (!urlFile.getParentFile().exists()) {
            urlFile.getParentFile().mkdirs();
        }

        Map<String, Object> disk = FTPUtils.getWindowsTranscodDiskSpace();
        log.info(disk.toString());
        if (disk == null || disk.size() == 0) {
            try {

                urlFile.delete();
                urlFile.getParentFile().delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw new VideoException(4001, "无法访问到转码服务器");
        }
        if (MapUtil.isNotNull(disk) && disk.get("freeSpace") != null) {
            double cleanSpace = DbPropUtil.getDouble("ftp-clean-space", 100);
            double freeSpace = (Double) disk.get("freeSpace");
            if (cleanSpace > freeSpace) {
                log.error("请注意，转码服务器磁盘空间已满！当前文件名fileName : " + file.getFilename());
                try {
                    urlFile.delete();
                    urlFile.getParentFile().delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                throw new VideoException(4001, "转码服务器磁盘空间已不足" + cleanSpace + "G");
            }
        }

        FTPResultBo result = new FTPResultBo();
        String fileOriginalName = file.getFilename();
        CtrlUnitFile ctrlUnitFile = new CtrlUnitFile();
        Long ctrlUnitFileId = Long.valueOf(RandomUtils.get8RandomValiteCode(10));
        // 文件后缀
        String suffix = "";
        if (fileOriginalName.contains(".")) {
            suffix = fileOriginalName.substring(fileOriginalName.lastIndexOf('.'), fileOriginalName.length());
        }
        // 重命名文件名
        String fileName = ctrlUnitFileId + suffix;

        // 文件大小的换算，图片用kb，视频用mb，原本是byte
        float mbSize = (urlFile.length() * 100) / (1024 * 1024);

        {
            ctrlUnitFile.setId(ctrlUnitFileId);
            ctrlUnitFile.setFileType("2");//离线视频类型
            ctrlUnitFile.setFileNameafterupload(fileName);
            ctrlUnitFile.setFileName(fileOriginalName);
            ctrlUnitFile.setCreateTime(new Date());
            ctrlUnitFile.setFileSuffix(suffix);
            ctrlUnitFile.setFileSize(String.valueOf(mbSize / 100.0));
            ctrlUnitFileService.save(ctrlUnitFile);
        }
        // 建立连接
        FTPUtils fu = new FTPUtils();
        // 上传到转码服务器ftp
        FTPClient judgeftp = fu.getJudgeFTP();
        FileInputStream vedioiss = null;
        try {
            vedioiss = new FileInputStream(urlFile);
        } catch (FileNotFoundException e) {
            result.setUploadSuccess(false);
            result.setMsg("找不到文件");
            e.printStackTrace();
        }
        String transcodingId = "";
        boolean uploadFtpFlag = false;
        try {
            uploadFtpFlag = FTPUtils.uploadPic(judgeftp, ftpPath, fileName, vedioiss);
        } catch (Exception e) {
            throw e;
        } finally {
            urlFile.delete();
            boolean s = urlFile.getParentFile().delete();
        }
        result.setFileName(fileOriginalName);
        result.setUploadedName(fileName);
        result.setUploadSuccess(uploadFtpFlag);
        result.setFileUrl(ftpPath + File.separator + fileName);

        ctrlUnitFile.setTranscodingId(transcodingId);
        ctrlUnitFileService.updateById(ctrlUnitFile);
        result.setFileId(ctrlUnitFileId);
        return result;
    }

    public String addTranscodeTask(String fileName, String videoType) {
        String transcodingId = null;
        String addtranscodetaskReponse = ftpTranscodeService.addTranscodeTask(fileName, videoType);

        log.info("转码文件: " + fileName + ",视频类型: " + videoType + ",添加转码返回报文：" + addtranscodetaskReponse);
        if (StringUtils.isNotEmptyString(addtranscodetaskReponse)) {

            System.out.println(addtranscodetaskReponse);
            Map<String, Object> resultMap = JSONUtil.getMap4Json(addtranscodetaskReponse);
            if (null != resultMap) {
                transcodingId = String.valueOf(resultMap.get("id"));
                if (StringUtils.isEmptyString(transcodingId)) {
                    log.error("获取的转码ID为空，请确保转码服务正常！");
                }
            }
            System.out.print(addtranscodetaskReponse);
        }
        return transcodingId;
    }

    /**
     * 图片截取
     *
     * @param width
     * @param height
     * @param imagePath ftp/fileName.png j
     * @return
     * @throws Exception
     */
    @Override
    public FileBo editImage(Integer x, Integer y, Integer width, Integer height, String imagePath) throws Exception {
        log.info("图片截取" + "x=" + x + ",y=" + y + ",height=" + height + ",imagePath:" + imagePath);
        FileBo fileResult = new FileBo();
        String fileFtpPath = "";
        File orgFile = null;
        FTPUtils ft = new FTPUtils();
        String imageBase64 = null;
        if (StringUtils.isNotEmptyString(imagePath) && imagePath.startsWith("http:")) {
            // 1、根据图片路径获取原始图片文件
            String[] str = ft.download(imagePath.substring(imagePath.lastIndexOf("/") + 1), imagePath);
            orgFile = new File(str[0]);
        } else {
            //来自案件管理里的图片
            if (StringUtils.isNotEmptyString(imagePath) && imagePath.startsWith("persist_picture")) {
                String picture = caseService.getPictureStr(imagePath);
                if (StringUtils.isNotEmptyString(picture)) {
                    imageBase64 = picture;
                }
            } else {
                //手动框选图片
                imageBase64 = imagePath;
            }
            String imageUrl = videoObjextTaskService.saveImageToFdfs(imageBase64);
            String[] str = ft.download(imageUrl.substring(imageUrl.lastIndexOf("/") + 1), imageUrl);
            orgFile = new File(str[0]);
        }
        if (x == null && y == null) {
            BufferedImage imageNew = ImageIO.read(orgFile);
            int paddingImageW = imageNew.getWidth();
            int paddingImageH = imageNew.getHeight();
            if (width == null && height == null) {
                width = paddingImageW;
                height = paddingImageH;
            }
            x = (paddingImageW - width) / 2;
            y = (paddingImageH - height) / 2;
        }
        fileFtpPath = cutImage(x, y, width, height, orgFile);
        fileResult.setFileFtpPath(fileFtpPath);
        return fileResult;
    }

    public String cutImage(Integer x, Integer y, Integer width, Integer height, File orgFile) {
        x = x < 0 ? 0 : x;
        y = y < 0 ? 0 : y;
        String fileFtpPath = "";
        FTPUtils fu = new FTPUtils();
        String subfileName = RandomUtils.get24TimeRandom() + ".jpg";
        String subPath = System.getProperty("user.dir") + File.separator + "ftp" + File.separator + subfileName;
        log.info("subPath:" + subPath);
        OperateImage operateImage = new OperateImage(x, y, width, height);
        if (null != orgFile) {
            operateImage.setSrcpath(orgFile.getPath());
        }
        operateImage.setSubpath(subPath);
        try {
            if (null != orgFile) {
                operateImage.cut(ImageUtils.getFormatName(orgFile));
                File subPathFile = null;
                subPathFile = new File(subPath);
                String imageBase64 = FileUtils.fileToBase64(subPathFile);
                fileFtpPath = videoObjextTaskService.saveImageToFdfs(imageBase64);
                try {
                    // 删除截图
                    subPathFile.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    // 删除下载的图
                    orgFile.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("截取失败");
            throw new VideoException("图片截取失败");
        }
        return fileFtpPath;
    }
}
