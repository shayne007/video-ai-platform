package com.keensense.admin.controller.common;

import com.keensense.admin.constants.CameraCacheConstants;
import com.keensense.admin.service.task.CaseService;
import com.keensense.admin.util.Ftp;
import com.keensense.admin.util.PropertiesUtil;
import com.keensense.admin.util.StringUtils;
import com.keensense.common.util.R;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 10:23 2019/6/12
 * @Version v0.1
 */
@Api(tags = "常规-项目基本信息")
@RestController
@Slf4j
public class CommonController {

    @Resource
    private CaseService caseService;

    /**
     * 版本信息
     */
    @ApiOperation("版本信息")
    @GetMapping(value = "/getVersion")
    @ResponseBody
    public R getVersion() {
        String version = PropertiesUtil.getParameterPackey("update.version");
        return R.ok().put("version", version);
    }

    /**
     * 下载图片（下载两张，大图+小图）
     *
     * @param imgurl
     * @return
     */
    @ApiOperation("下载图片（下载两张，大图+小图）")
    @PostMapping("downloadImage")
    @ApiImplicitParam(name = "imgurl", value = "图片Url地址")
    public void downloadImage(String imgurl, HttpServletResponse response) throws IOException {
        if (StringUtils.isNotEmptyString(imgurl)) {
            String suffix = imgurl.substring(imgurl.lastIndexOf("."), imgurl.length());
            String fileName = StringUtils.getRandom32PK();
            BufferedInputStream bis = null;
            HttpURLConnection httpUrl = null;
            URL url = null;
            // 替换中文
            StringBuffer imageUrlBuffer = new StringBuffer();
            for (int i = 0; i < imgurl.length(); i++) {
                char a = imgurl.charAt(i);
                if (a > 127) {
                    //将中文UTF-8编码
                    imageUrlBuffer.append(URLEncoder.encode(String.valueOf(a), "utf-8"));
                } else {
                    imageUrlBuffer.append(String.valueOf(a));
                }
            }

            url = new URL(imageUrlBuffer.toString());

            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();

            int responseCode = httpUrl.getResponseCode();

            if (HttpURLConnection.HTTP_OK == responseCode || HttpURLConnection.HTTP_CREATED == responseCode || HttpURLConnection.HTTP_ACCEPTED == responseCode) {
                bis = new BufferedInputStream(httpUrl.getInputStream());
            } else {
                bis = new BufferedInputStream(httpUrl.getErrorStream());
            }

            OutputStream outs = response.getOutputStream();//获取文件输出IO流
            BufferedOutputStream bouts = new BufferedOutputStream(outs);
            response.setContentType("image/jpeg");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + "." + suffix);//设置头部信息
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            //开始向网络传输文件流
            while ((bytesRead = bis.read(buffer, 0, 8192)) != -1) {
                bouts.write(buffer, 0, bytesRead);
            }
            bouts.flush();//这里一定要调用flush()方法
            bouts.close();
            outs.close();
            bis.close();
        }
    }

    /**
     * 删除Vas监控点缓存,同步更新
     */
    @PostMapping("cleanVasCameraCache")
    public R cleanVasCameraCache() {
        CameraCacheConstants.cleanCameraCacheByCameraType("1");
        return R.ok();
    }

    /**
     * 获取持久化图片
     */
    @GetMapping(value = "/getPersistPicture")
    public R getPersistPicture(HttpServletResponse response, String id) {
        try {
            // 解码，然后将字节转换为文件
            String picture = caseService.getPictureStr(id);
            byte[] bytes = null;
            if (StringUtils.isNotEmptyString(picture)) {
                bytes = Base64.decode(Arrays.toString(picture.getBytes()));
            }
            if (null == bytes) {
                return R.error("图片获取失败");
            }
            ByteArrayInputStream ins = new ByteArrayInputStream(bytes);
            BufferedInputStream bins = new BufferedInputStream(ins);//放到缓冲流里面
            OutputStream outs = response.getOutputStream();//获取文件输出IO流
            BufferedOutputStream bouts = new BufferedOutputStream(outs);
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            //开始向网络传输文件流
            while ((bytesRead = bins.read(buffer, 0, 8192)) != -1) {
                bouts.write(buffer, 0, bytesRead);
            }
            bouts.flush();//这里一定要调用flush()方法
            ins.close();
            bins.close();
            outs.close();
            bouts.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return R.error(e.getMessage());
        }
        return null;

    }

    /**
     * 批量上传图片
     * @param files
     * @param response
     */
    @ApiOperation("批量上传图片")
    @PostMapping("batchfile")
    public void addBatchSlide(@RequestParam("files") MultipartFile[] files, HttpServletResponse response){
        String paths = "";
        try {
            for(int i = 0;i<files.length;i++){
                String path= Ftp.uploadFile(files[i].getOriginalFilename(), files[i].getInputStream(), "uploadImage");
               /* FTPUtils fu = new FTPUtils();
                // 上传到ftp服务器
                FTPClient judgeftp = fu.getJudgeFTP();
                boolean uploadFtpFlag = FTPUtils.uploadPic(judgeftp, "/", files[i].getOriginalFilename(),  files[i].getInputStream());*/
                paths +=path+",";
            }
            if(paths.contains(",")){
                paths = paths.substring(0, paths.length()-1);
            }
            response.getWriter().write(paths);
            response.getWriter().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
