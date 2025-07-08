package com.keensense.admin.controller.oss;

import com.keensense.admin.constants.CommonConstants;
import com.keensense.admin.dto.Chunk;
import com.keensense.admin.dto.FTPResultBo;
import com.keensense.admin.dto.FileBo;
import com.keensense.admin.dto.FileInfo;
import com.keensense.admin.service.task.FtpService;
import com.keensense.admin.util.DbPropUtil;
import com.keensense.admin.util.StringUtils;
import com.keensense.common.exception.VideoException;
import com.keensense.common.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/ftp")
@Api(tags = "ftp控制器")
public class FtpController {
    private String uploadFolder = System.getProperty("user.dir") + File.separator + "data" + File.separator + "tmp";
    @Resource
    private FtpService ftpService;

    /**
     * 分片上传操作  主要针对浓缩检索中的离线文件
     * 上传文件目录为转码ftp目录
     *
     * @param chunk
     * @return
     */
    @PostMapping("/chunk")
    @ApiOperation("分片上传操作")
    public R uploadChunk(Chunk chunk) throws Exception {
        if (StringUtils.isEmpty(chunk.getFilename().trim())) {
            return R.error("不允许全为空格");
        }
        if (StringUtils.checkRegex_false(chunk.getFilename(),
                CommonConstants.REGEX.CAMERA_CASE_RELINE_FILE_NAME_NOT_SUPPORT)) {
            R.error("视频名称不符合规则");
        }
        R result = R.ok();
        MultipartFile file = chunk.getFile();
        System.out.println("file originName: {}, chunkNumber: {}" + file.getOriginalFilename() + "," + chunk.getChunkNumber());
        log.debug("file originName: {}, chunkNumber: {}", file.getOriginalFilename(), chunk.getChunkNumber());
        byte[] bytes = file.getBytes();
        Path path = Paths.get(generatePath(uploadFolder, chunk));
        //文件写入指定路径
        Files.write(path, bytes);
        log.info("文件 {} 写入成功, uuid:{}" + chunk.getFilename() + "," + chunk.getIdentifier());
        log.info("文件 {} 写入成功, uuid:{}", chunk.getFilename(), chunk.getIdentifier());
        log.info("文件 {} 写入成, 第 {} 分片大小:{}", chunk.getFilename(), chunk.getChunkNumber(), chunk.getChunkSize());
        long starttime = System.currentTimeMillis();
        log.info("chunk.getChunkNumber().intValue():" + chunk.getChunkNumber().intValue() + " chunk.getTotalChunks().intValue():" + chunk.getTotalChunks().intValue());
        if (chunk.getChunkNumber().intValue() == chunk.getTotalChunks().intValue()) {
            String targetFile = uploadFolder + File.separator + chunk.getIdentifier() + File.separator + chunk.getFilename();
            System.out.println("fileName---" + file);
            String folder = uploadFolder + File.separator + chunk.getIdentifier();
            merge(targetFile, folder, chunk);


            FileInfo fileInfo = new FileInfo();
            fileInfo.setIdentifier(chunk.getIdentifier());
            fileInfo.setLocation(targetFile);
            fileInfo.setFilename(chunk.getFilename());
            String ftppath = "/";
            FTPResultBo data = ftpService.uploadLocalFileToFtp(fileInfo, ftppath);
            result.put("fileId", data.getFileId());
        }
        log.info("chunkupload-cost:" + (System.currentTimeMillis() - starttime));
        return result;
    }

    @GetMapping("/chunk")
    public Object checkChunk(Chunk chunk, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        return chunk;
    }

    public static String generatePath(String uploadFolder, Chunk chunk) {
        StringBuilder sb = new StringBuilder();
        sb.append(uploadFolder).append("/").append(chunk.getIdentifier());
        //判断uploadFolder/identifier 路径是否存在，不存在则创建
        if (!Files.isWritable(Paths.get(sb.toString()))) {
            log.info("path not exist,create path: {}", sb.toString());
            try {
                Files.createDirectories(Paths.get(sb.toString()));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

        return sb.append("/")
                .append(chunk.getFilename())
                .append("-")
                .append(chunk.getChunkNumber()).toString();
    }

    /**
     * 文件合并
     *
     * @param targetFile
     * @param folder
     */
    public static void merge(String targetFile, String folder, Chunk chunk) {
        try {
            File f = new File(targetFile);
            if (!f.exists()) {
                Files.createFile(Paths.get(targetFile));
            }
            for (int i = 0; i < 600; i++) {
                long count = Files.list(Paths.get(folder)).filter(path -> !path.getFileName().toString().equals(chunk.getFilename())).count();
                if (count != chunk.getChunkNumber()) {
                    log.info("---------------fenxin-----error-------------" + count);
                    log.info("---------------------------------" + chunk.getChunkNumber());
                    log.info("----------------------------------" + chunk.getIdentifier() + "---------" + chunk.getFilename());
                } else {
                    break;
                }
                Thread.sleep(1000);
            }
            long count = Files.list(Paths.get(folder)).filter(path -> !path.getFileName().toString().equals(chunk.getFilename())).count();
            if (count != chunk.getChunkNumber()) {
                log.info("---------------error-------------" + count);
                log.info("---------------------------------" + chunk.getChunkNumber());
                log.info("----------------------------------" + chunk.getIdentifier() + "---------" + chunk.getFilename());
                f.getParentFile().delete();
                f.delete();
                throw new VideoException(300, "离线视频上传失败");
            }
            log.info("-------------merge_video---------------------" + chunk.getIdentifier() + "---------" + chunk.getFilename());
            Files.list(Paths.get(folder))
                    .filter(path -> !path.getFileName().toString().equals(chunk.getFilename()))
                    .sorted((o1, o2) -> {
                        String p1 = o1.getFileName().toString();
                        String p2 = o2.getFileName().toString();
                        int i1 = p1.lastIndexOf("-");
                        int i2 = p2.lastIndexOf("-");
                        return Integer.valueOf(p2.substring(i2)).compareTo(Integer.valueOf(p1.substring(i1)));
                    })
                    .forEach(path -> {
                        try {
                            //以追加的形式写入文件
                            log.info(chunk.getIdentifier() + "---------------" + path.getFileName().toString());
                            Files.write(Paths.get(targetFile), Files.readAllBytes(path), StandardOpenOption.APPEND);
                            //合并后删除该块
                            Files.delete(path);
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        }
                    });
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        long len = new File(targetFile).length();
        log.info("11111111111111111111111111111" + len);
        log.info("11111111111111111111111111111" + chunk.getTotalSize());
        if (len != chunk.getTotalSize()) {
            log.info("11111111111111111111111111111compare" + (chunk.getTotalSize() - len));
            log.info("11111111111111111111111111111" + chunk.getIdentifier());
            log.info("11111111111111111111111111111" + chunk.getFilename());
        }
    }

    /**
     * 图片上传，获取截取图片
     *
     * @return 图片保存的ftp路径
     */
    @PostMapping("editImageByXYWH")
    public Map<String, Object> editImageByXYWH(HttpServletRequest request, Integer width, Integer height, Integer x, Integer y, String imgUrl) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("state", true);
        try {
            String targetImageUrl = "";
            if (imgUrl.indexOf("http://") != -1) {
                targetImageUrl = imgUrl;
            } else {
                if (imgUrl.indexOf("ftppath") != -1) {
                    imgUrl = imgUrl.substring(8, imgUrl.length());
                }
                targetImageUrl = "http://" + DbPropUtil.getString("file.service") + ":" +
                        DbPropUtil.getString("ftp.file.service.port") + imgUrl;
            }
            log.info("targetImageUrl:{}", targetImageUrl);
            // 图片上传
            FileBo fileResult = ftpService.editImage(x, y, width, height, targetImageUrl);
            String imageUrl = fileResult.getFileFtpPath();
            imageUrl = imageUrl.replaceAll("\\\\", "/");
            String ftpPath = fileResult.getFileFtpPath();
            ftpPath = ftpPath.replaceAll("\\\\", "/");
            fileResult.setFileFtpPath(ftpPath);
            fileResult.setFileUrl(imageUrl);
            result.put("data", fileResult);
        } catch (Exception e) {
            result.put("state", false);
            e.printStackTrace();
        }
        return result;
    }
}
