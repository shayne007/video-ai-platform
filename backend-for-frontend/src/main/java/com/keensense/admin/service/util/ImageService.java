package com.keensense.admin.service.util;

import com.keensense.admin.constants.GlobalConstant;
import com.keensense.admin.service.task.CaseService;
import com.keensense.admin.util.FTPUtils;
import com.keensense.admin.util.RandomUtils;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.util.ZipUtils;
import com.keensense.common.util.R;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class ImageService {

    @Resource
    private CaseService caseService;

    public R batchDownLoadImg(HttpServletResponse response, List<String> imgUrls) {
        try {
            //压缩文件
            List<File> files = new ArrayList<>();
            FTPUtils fu = new FTPUtils();
            for (String imgurl : imgUrls) {
                File file = fu.downUrlFile(imgurl.substring(imgurl.lastIndexOf("/") + 1), imgurl);
                if (null != file && file.isFile()) {
                    files.add(file);
                }
            }
            String fileName = RandomUtils.get24TimeRandom() + ".zip";
            //在服务器端创建打包下载的临时文件
            String globalUploadPath = FTPUtils.getRootPath("/") + "/ftp";
            String outFilePath = globalUploadPath + File.separator + fileName;
            File file = new File(outFilePath);
            //文件输出流
            FileOutputStream outStream = new FileOutputStream(file);
            //压缩流
            ZipOutputStream toClient = new ZipOutputStream(outStream);
            //设置压缩文件内的字符编码，不然会变成乱码
            //toClient.setEncoding("GBK");
            ZipUtils.zipFile(files, toClient);
            toClient.close();
            outStream.close();
            ZipUtils.downloadZip(file, response);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
        return R.ok();
    }

    public R caseBatchDownLoadImg(HttpServletResponse response, String fileIds) {
        try {
            //压缩文件
            List<File> files = new ArrayList<>();
            String[] idArray = fileIds.split(",");
            for (String id : idArray) {
                // 解码，然后将字节转换为文件
                String picture = caseService.getPictureStr(id);
                byte[] bytes = null;
                if (StringUtils.isNotEmptyString(picture)) {
                    bytes = Base64.decode(picture.getBytes());
                }
                ByteArrayInputStream ins = new ByteArrayInputStream(bytes);
                BufferedInputStream fis = new BufferedInputStream(ins);//放到缓冲流里面
                String ctxPath = GlobalConstant.DATA_FTP;
                File saveDir = new File(ctxPath); // 文件保存位置
                if (!saveDir.exists()) {
                    saveDir.mkdir();
                }
                String filePath = saveDir + File.separator + RandomUtils.get24TimeRandom() + ".jpg";// 最终文件保存位置
                // result[0]=filePath;//返回最终保存本地位置
                File file = new File(filePath);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = fis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                fis.close();
                fos.close();
                if (null != file && file.isFile()) {
                    files.add(file);
                }
            }
            String fileName = RandomUtils.get24TimeRandom() + ".zip";
            //在服务器端创建打包下载的临时文件
            //String globalUploadPath = FTPUtils.getRootPath("/") + "/ftp";
            String globalUploadPath = GlobalConstant.DATA_FTP;
            String outFilePath = globalUploadPath + File.separator + fileName;
            File file = new File(outFilePath);
            //文件输出流
            FileOutputStream outStream = new FileOutputStream(file);
            //压缩流
            ZipOutputStream toClient = new ZipOutputStream(outStream);
            //设置压缩文件内的字符编码，不然会变成乱码
            //toClient.setEncoding("GBK");
            ZipUtils.zipFile(files, toClient);
            toClient.close();
            outStream.close();
            ZipUtils.downloadZip(file, response);
        } catch (Exception e) {
            log.error("批量下载图片失败:",e);
            return R.error(e.getMessage());
        }
        return null;
    }
}
