package com.keensense.densecrowd.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.keensense.common.exception.VideoException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * FTP工具类
 *
 * @author YANGXQ
 */
@Slf4j
public class FTPUtils {
    /**
     * 获取研判系统配置的ftp连接
     *
     * @return
     */
    public FTPClient getJudgeFTP() {
        FTPClient ftp = getConnectionFTP(DbPropUtil.getString("ftp-server-ip", "192.168.0.86"),
                DbPropUtil.getInt("ftp-server-port", 21),
                DbPropUtil.getString("ftp-server-user", "chiwailam"),
                DbPropUtil.getString("ftp-server-pwd", "abcd1234!"));

        return ftp;
    }

    /**
     * 获取OCX系统配置的ftp连接
     *
     * @return
     */
    public FTPClient getOCXFTP() {
        FTPClient ocxftp = getConnectionFTP(DbPropUtil.getString("ftp-server-ip"),
                DbPropUtil.getInt("ftp-server-port"),
                DbPropUtil.getString("ftp-server-user"),
                DbPropUtil.getString("ftp-server-pwd"));

        return ocxftp;
    }

    /**
     * 获取图帧ftp连接
     *
     * @return
     */
    public FTPClient getVideoFTP() {
        FTPClient ocxftp = getConnectionFTP(DbPropUtil.getString("ftp-server-ip"),
                DbPropUtil.getInt("ftp-server-port"),
                DbPropUtil.getString("ftp-server-user"),
                DbPropUtil.getString("ftp-server-pwd"));

        return ocxftp;
    }

    /**
     * 获得连接-FTP方式
     *
     * @param hostName FTP服务器地址
     * @param port     FTP服务器端口
     * @param userName FTP登录用户名
     * @param passWord FTP登录密码
     * @return FTPClient
     */
    public FTPClient getConnectionFTP(String hostName, int port, String userName, String passWord) {
        //创建FTPClient对象  
        FTPClient ftp = new FTPClient();
        try {
            //连接FTP服务器  
            ftp.connect(hostName, port);
            //下面三行代码必须要，而且不能改变编码格式，否则不能正确下载中文文件  
            ftp.setControlEncoding("GBK");
            FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
            conf.setServerLanguageCode("zh");
            //登录ftp  
            ftp.login(userName, passWord);
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                ftp.disconnect();
                log.debug("FTPUtil getConnectionFTP method id =>连接服务器失败");
            }
            log.debug("FTPUtil getConnectionFTP method id =>登陆服务器成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ftp;
    }

    /**
     * 关闭连接-FTP方式
     *
     * @param ftp FTPClient对象
     * @return boolean
     */
    public boolean closeFTP(FTPClient ftp) {
        if (ftp.isConnected()) {
            try {
                ftp.disconnect();
                log.debug("FTPUtil closeFTP method id =>ftp已经关闭");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * 上传文件-FTP方式
     *
     * @param path        FTP服务器上传地址
     * @param fileName    本地文件路径
     * @param inputStream 输入流
     * @return boolean
     */
    public boolean uploadFile(String path, String fileName, InputStream inputStream) {
        boolean success = false;
        try {
            FTPClient ftp = getJudgeFTP();

            log.debug("------------ftpPath =>>" + path);
            //转到指定上传目录
            boolean flag = ftp.changeWorkingDirectory(path);//转移到指定FTP服务器目录  
            log.debug("------------changeWorkingDirectory - > flag =>>" + flag);
            //FTPFile[] fs = ftp.listFiles();//得到目录的相应文件列表  //deleted by pengmd
            //fileName = FTPUtil.changeName(fileName, fs);    //deleted by pengmd
            //fileName = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");  
            //path = new String(path.getBytes("UTF-8"), "ISO-8859-1");  

            //设置缓冲大小200M
            ftp.setBufferSize(204800);
            //将上传文件存储到指定目录  
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            log.debug("------------fileName =>>" + fileName);
            ftp.enterLocalPassiveMode();
            log.debug("------------upload begin =>>" + System.currentTimeMillis());
            //如果缺省该句 传输txt正常 但图片和其他格式的文件传输出现乱码  
            flag = ftp.storeFile(fileName, inputStream);
            log.debug("------------upload end =>>" + System.currentTimeMillis());
            //关闭输入流  
            inputStream.close();
            //退出ftp  
            ftp.logout();
            //表示上传成功  
            success = true;
            log.info("FTPUtil uploadFile method id =>上传成功。。。。。");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("FTPUtil uploadFile method 上传失败！" + e.toString());
        }
        return success;
    }

    public static boolean mkdirOnline(FTPClient ftp, String path) {
        boolean flag = false;
        String[] aryStr = path.split("/");
        try {
            for (int i = 0; i < aryStr.length; i++) {
                if (StringUtils.isNotEmptyString(aryStr[i])) {
                    if (ftp.makeDirectory(aryStr[i])) {
                        String newpath = new String(aryStr[i].getBytes(), FTP.DEFAULT_CONTROL_ENCODING);
                        ftp.changeWorkingDirectory(newpath);
                    }
                }
            }
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 上传文件-FTP方式
     *
     * @param ftp         FTPClient对象
     * @param path        FTP服务器上传地址
     * @param fileName    本地文件路径
     * @param inputStream 输入流
     * @return boolean
     */
    public static boolean uploadPic(FTPClient ftp, String path, String fileName, InputStream inputStream) {
        boolean success = false;
        long startTime = System.currentTimeMillis();
        log.info("fileNameStart:" + fileName);
        try {
            /*
             * ftp.changeWorkingDirectory(path);//转移到指定FTP服务器目录 FTPFile[] fs =
			 * ftp.listFiles();//得到目录的相应文件列表 if(isFileExist(fileName,fs)){
			 * deleteFile(ftp, path, fileName); }
			 */
            fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
            if (StringUtils.isNotEmptyString(path)) {
                path = new String(path.getBytes("GBK"), "ISO-8859-1");
            }
            // 设置缓冲大小1024 =1M
            ftp.setBufferSize(204800); // 200M
            // 转到指定上传目录
            String[] dirs = path.split("/");
            for (String dir : dirs) {
                if (StringUtils.isNotEmptyString(dir)) {
                    ftp.makeDirectory(dir);
                    ftp.changeWorkingDirectory(dir);
                }
            }
            // 将上传文件存储到指定目录
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            // 如果缺省该句 传输txt正常 但图片和其他格式的文件传输出现乱码
            ftp.storeFile(fileName, inputStream);
            // 关闭输入流
            inputStream.close();
            // 退出ftp
            ftp.logout();
            // 表示上传成功
            success = true;

            log.info("FTPUtil uploadFile method id =>上传成功。。。。。");
        } catch (ConnectException e){
            log.info("FTPUtil uploadFile method FTP连接失败！" + e.toString());
            throw new VideoException("FTP连接失败");
        }catch (Exception e) {
            //e.printStackTrace();

            log.info("FTPUtil uploadFile method 上传失败！" + e.toString());
            throw new VideoException("FTP上传失败");
        }
        log.info("fileNameEnd:" + fileName + " cost:" + (System.currentTimeMillis() - startTime));
        return success;
    }

    /**
     * 删除文件-FTP方式
     *
     * @param ftp      FTPClient对象
     * @param path     FTP服务器上传地址
     * @param fileName FTP服务器上要删除的文件名
     * @return
     */
    public static boolean deleteFile(FTPClient ftp, String path, String fileName) {
        boolean success = false;
        try {
            ftp.changeWorkingDirectory(path);//转移到指定FTP服务器目录  
            fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
            // path = new String(path.getBytes("GBK"), "ISO-8859-1");
            ftp.deleteFile(fileName);
//            ftp.logout();  
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 删除文件夹及所有文件-FTP方式
     *
     * @param ftpClient  FTPClient对象
     * @param folderName FTP服务器上要删除的文件名
     * @return
     */
    public boolean deleteFolder(FTPClient ftpClient, String folderName) {
        boolean success = false;
        try {

            ftpClient.changeWorkingDirectory("/");//转移到FTP服务器根目录  

            // 删除文件夹下所有文件
            String[] filesPath = ftpClient.listNames(folderName);
            if (filesPath != null && filesPath.length > 0) {
                for (int i = 0; i < filesPath.length; i++) {
                    String filePath = new String(filesPath[i].getBytes("GBK"), "ISO-8859-1");
                    ftpClient.deleteFile(filePath);
                }
            }

            folderName = new String(folderName.getBytes("GBK"), "ISO-8859-1");

            return ftpClient.removeDirectory(folderName);

        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }


    /**
     * 获取本地下载后文件
     *
     * @param ftp      FTPClient对象
     * @param path     FTP服务器上传地址
     * @param fileName 文件名
     * @return boolean
     */
    public File getDownFile(FTPClient ftp, String path, String fileName) {
        String root = getRootPath("/");

        File localFile = new File(root + File.separator + path + File.separator + fileName);
        //本地是否该存在
        if (!localFile.exists()) {
            //先判断文件夹是否存在
            if (!(new File(root + File.separator + path + File.separator)).exists()) {
                new File(root + File.separator + path + File.separator).mkdirs();
            }
            //先下载到本地
            downFile(ftp, path, fileName);
            // convertFlvFile(ftp,path,fileName);
            localFile = new File(root + File.separator + path + File.separator + fileName);
        }
        return localFile;

    }

    /**
     *获取本地下载后文件 针对vas对接
     * @param ftp FTPClient对象
     * @param ftpPath FTP服务器上传地址 
     * @param fileName 文件名 
     * @param localPath 本地临时路径 
     * @return boolean
     */  
    /*public File getDownFileNew(FTPClient ftp,String ftpPath,String localPath, String fileName) {  
         String root = getRootPath("/");
    	 
    	 File localFile = new File(root + "\\" + localPath + "\\" + fileName);  
    	 //本地是否该存在
    	 if (!localFile.exists())
    	 {
    	     //先判断文件夹是否存在
             if (!(new File(root + "\\" + localPath+ "\\")).exists())
             {
                 new File(root + "\\" + localPath + "\\").mkdirs();
             }
        	  //先下载到本地
        	  downFileNew(ftp,ftpPath,localPath,fileName);
        	 // convertFlvFile(ftp,path,fileName);
        	  localFile = new File(root + "\\" + localPath + "\\" + fileName);  
    	 }
    	return localFile;  
    	
    }*/

    /**
     * 下载文件-FTP方式 
     * @param ftp FTPClient对象
     * @param ftpPath FTP服务器上传地址 
     * @param localPath 本地临时路径 
     * @param fileName 文件名 
     * @return boolean
     *//*  
    public boolean downFileNew(FTPClient ftp,String ftpPath,String localPath, String fileName) {  
        boolean success = false;  
        String root = getRootPath("/");
        try {
        	
            ftp.changeWorkingDirectory(ftpPath);//转移到FTP服务器目录  
            ftp.enterLocalPassiveMode();
            FTPFile[] fs = ftp.listFiles(); //得到目录的相应文件列表  
            
            
           for (FTPFile ff : fs) {
             String ftpfileName = new String(ff.getName().getBytes("GBK"),"UTF-8");          
                if (ftpfileName.equals(fileName)) {  
                    File localFile = new File(root + "\\" + localPath + "\\" + fileName);  
                   
                    OutputStream outputStream = new FileOutputStream(localFile);  
                    //设置缓冲大小1024 =1M   
                    ftp.setBufferSize(204800); //200M
                    //将文件保存到输出流outputStream中  
                    ftp.retrieveFile(new String(fileName.getBytes("GBK"), "ISO-8859-1"), outputStream);  
                    outputStream.flush();  
                    outputStream.close();  
                    System.out.println("下载成功");  
                }  
            }  
            ftp.logout();  
            success = true;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return success;  
    }*/

    /**
     * 下载文件-FTP方式
     *
     * @param ftp      FTPClient对象
     * @param path     FTP服务器上传地址
     * @param fileName 文件名
     * @return boolean
     */
    public boolean downFile(FTPClient ftp, String path, String fileName) {
        boolean success = false;
        String root = getRootPath("/");
        try {

            ftp.changeWorkingDirectory(path);//转移到FTP服务器目录  
            ftp.enterLocalPassiveMode();
            FTPFile[] fs = ftp.listFiles(); //得到目录的相应文件列表  


            for (FTPFile ff : fs) {
                String ftpfileName = new String(ff.getName().getBytes("GBK"), "UTF-8");
                if (ftpfileName.equals(fileName)) {
                    File localFile = new File(root + File.separator + path + File.separator + fileName);

                    OutputStream outputStream = new FileOutputStream(localFile);
                    //设置缓冲大小1024 =1M   
                    ftp.setBufferSize(204800); //200M
                    ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                    //将文件保存到输出流outputStream中  
                    ftp.retrieveFile(new String(fileName.getBytes("GBK"), "ISO-8859-1"), outputStream);
                    outputStream.flush();
                    outputStream.close();
                    System.out.println("下载成功");
                }
            }
            ftp.logout();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 下载文件-FTP方式
     *
     * @param ftp         FTP服务器上传地址
     * @param path        本地临时路径
     * @param fileName    文件名
     * @param newFileName 重命名文件名
     * @return boolean
     */
    public boolean downFile(FTPClient ftp, String path, String fileName, String newFileName) {
        boolean success = false;
        String root = getRootPath("/");
        String ocx = PropertiesUtil.getFtpPackey("ocx.upfile.service.path");
        try {
            ftp.changeWorkingDirectory(path);//转移到FTP服务器目录  
            ftp.enterLocalPassiveMode();
            FTPFile[] fs = ftp.listFiles(); //得到目录的相应文件列表  


            for (FTPFile ff : fs) {
                if (ff.getName().equals(fileName)) {
                    File localFile = new File(root + File.separator + ocx + File.separator + newFileName);
                    if (!localFile.exists()) {
                        new File(root + File.separator + ocx + File.separator).mkdirs();
                    }
                    OutputStream outputStream = new FileOutputStream(localFile);
                    //设置缓冲大小1024 =1M   
                    ftp.setBufferSize(204800); //200M
                    //将文件保存到输出流outputStream中  
                    ftp.retrieveFile(new String(fileName.getBytes("GBK"), "ISO-8859-1"), outputStream);
                    outputStream.flush();
                    outputStream.close();
                    System.out.println("下载成功");
                    break;
                }
            }
            ftp.logout();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 下载文件
     *
     * @param ftp
     * @param path
     * @param fileName
     * @param outputStream
     * @return
     */
    public boolean downloadFile(FTPClient ftp, String path, String fileName, OutputStream outputStream) {
        boolean success = false;
        try {
            ftp.changeWorkingDirectory(path);//转移到FTP服务器目录  
            ftp.enterLocalPassiveMode();
            //2015-11-17 生产紧急bug修复 upd by pengmd
            FTPFile[] fs = ftp.listFiles(); //得到目录的相应文件列表  
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            for (FTPFile ff : fs) {
                if (ff.getName().equals(fileName)) {
                    //将文件保存到输出流outputStream中  
                    ftp.retrieveFile(new String(ff.getName().getBytes("GBK"), "ISO-8859-1"), outputStream);
                    outputStream.flush();
                    outputStream.close();
                }
            }
            ftp.logout();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }


    /**
     * 判断是否有重名文件
     *
     * @param fileName
     * @param fs
     * @return
     */
    public static boolean isFileExist(String fileName, FTPFile[] fs) {
        for (int i = 0; i < fs.length; i++) {
            FTPFile ff = fs[i];
            if (ff.getName().equals(fileName)) {
                return true; //如果存在返回 正确信号  
            }
        }
        return false; //如果不存在返回错误信号  
    }

    /**
     * 根据重名判断的结果 生成新的文件的名称
     *
     * @param fileName
     * @param fs
     * @return
     */
    public static String changeName(String fileName, FTPFile[] fs) {
        int n = 0;
//      fileName = fileName.append(fileName);  
        while (isFileExist(fileName.toString(), fs)) {
            n++;
            String a = "[" + n + "]";
            int b = fileName.lastIndexOf(".");//最后一出现小数点的位置  
            int c = fileName.lastIndexOf("[");//最后一次"["出现的位置  
            if (c < 0) {
                c = b;
            }
            StringBuffer name = new StringBuffer(fileName.substring(0, c));//文件的名字  
            StringBuffer suffix = new StringBuffer(fileName.substring(b + 1));//后缀的名称  
            fileName = name.append(a) + "." + suffix;
        }
        return fileName.toString();
    }

    /**
     * @return -1 - 未设置预警值，跳过检验返回
     * 0 - 磁盘空间使用率未达到预警值
     * 1 - 磁盘空间使用率达到预警值（即快满了）
     * 2 - 磁盘空间已满
     * @Title: checkDiskSpace
     * @Description: 检测磁盘空间使用情况
     * @ReturnType: Integer
     */
    public static Integer checkDiskSpace() {

        String warningValue = null;
        try {
            warningValue = PropertiesUtil.getFtpPackey("Disk_Space_WarningValue");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == warningValue || "".equals(warningValue)) {
            return -1;
        }
        Double spaceUsage = Double.parseDouble(warningValue);
        log.debug("检查磁盘空间大小");
        Integer result = 0;
        String root = getRootPath("/");//获取项目所在物理路径
        String diskName = root.substring(0, 3);//获取盘符
        log.debug("检查磁盘：" + diskName);
        File[] roots = File.listRoots();//获取磁盘分区列表
        for (File file : roots) {
            if (!file.getPath().equals(diskName)) continue;
            Double usedSpace = (file.getTotalSpace() - file.getUsableSpace()) / (double) file.getTotalSpace();
            log.debug("磁盘" + file.getPath() + "使用率：" + usedSpace);
            if (usedSpace > spaceUsage) {
                result = 1;
                log.debug("磁盘" + file.getPath() + "使用率已超过预警值");
            } else if (usedSpace > 0.98) {
                result = 2;
                log.debug("磁盘" + file.getPath() + "已满");
            }
        }
        return result;
    }

    /**
     * 获取windows转码服务器FTP磁盘信息
     *
     * @return
     */
    public static Map<String, Object> getWindowsTranscodDiskSpace() {
        Map<String, Object> resMap = new HashMap<>();
        try {
            String serviceName = CommonConstants.GET_DISK_SPACE;
            String respJson = FtpHttpGetUtils.getHttp(serviceName, "");
            JSONObject json = JSON.parseObject(respJson);
            resMap.put("diskName", json.getString("diskName"));
            resMap.put("freeSpace", json.getDouble("freeSpace"));
            resMap.put("totalSpace", json.getDouble("totalSpace"));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("get WindowsTranscodDiskSpace error");
        }
        return resMap;
    }


    /**
     * 获取获取项目所在物理路径（根目录）
     *
     * @param arg
     * @return
     */
    public static String getRootPath(String arg) {
        //return ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath(arg);
        // 获取跟目录
        File path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {

        }
        if (path == null || !path.exists()) {
            path = new File("");
        }

        String pathStr = path.getAbsolutePath() + File.separator;
        // 如果是在eclipse中运行，则和target同级目录,如果是jar部署到服务器，则默认和jar包同级
        //pathStr = pathStr.replace("\\target\\classes", "");
        return pathStr;
    }

    /**
     * 从ocxftp服务器同步到系统ftp服务器
     *
     * @param ocxfileName
     * @param ocxfilePath
     */
    public void copyFtpFile(String ocxfileName, String ocxfilePath, String newFileName) {

        FTPClient ftpfrom = getOCXFTP();

        //从下载到本地目录
        downFile(ftpfrom, ocxfilePath, ocxfileName, newFileName);
        //上传到系统对应ftp
        String ocx = PropertiesUtil.getFtpPackey("ocx.upfile.service.path");
        File localFile = new File(getRootPath("/") + File.separator + ocx + File.separator + newFileName);
        try {
            InputStream inputStream = new FileInputStream(localFile);
            uploadFile(ocx, newFileName, inputStream);
            //localFile.delete();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 获取本地下载后文件 针对vas对接
     *
     * @param ftp       FTPClient对象
     * @param ftpPath   FTP服务器上传地址
     * @param fileName  文件名
     * @param localPath 本地临时路径
     * @return boolean
     */
    public Map<String, String> getDownFileNew(FTPClient ftp, String localPath, String fileName) {

        return downFileNew(ftp, localPath, fileName);

    }

    /**
     * 下载文件-FTP方式
     *
     * @param ftp       FTPClient对象
     * @param localPath 本地临时路径
     * @param fileName  文件名
     * @return boolean
     */
    public Map<String, String> downFileNew(FTPClient ftp, String localPath, String fileName) {
        Map<String, String> reMap = new HashMap<String, String>();
        try {

            ftp.changeWorkingDirectory("");//转移到FTP服务器目录  
            ftp.enterLocalPassiveMode();
            FTPFile[] fs = ftp.listFiles(); //得到目录的相应文件列表  


            for (FTPFile ff : fs) {
                String ftpfileName = new String(ff.getName().getBytes("GBK"), "UTF-8");
                if (ftpfileName.equals(fileName)) {

                    String suffix = PropertiesUtil.getParameterPackey("vas.download.filename");
                    String newName = "";
                    if (StringUtils.isNotEmptyString(suffix)) {
                        if (fileName.contains(suffix)) {
                            newName = fileName.split(suffix)[0] + "_" + fileName.split(suffix)[1] + suffix;
                        } else {
                            newName = fileName.substring(0, fileName.length() - 32);
                        }
                    }

                    ftp.rename(fileName, newName);
                    reMap.put("fileSize", String.valueOf(ff.getSize() / 1024 / 1024));
                    reMap.put("fileName", newName);
                }
            }
            ftp.logout();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reMap;
    }

    public boolean makeDir(String path) {
        boolean success = false;
        String[] aryStr = path.split("/");
        try {
            FTPClient ftp = getJudgeFTP();
            //ftp.changeWorkingDirectory(aryStr[1]);
            for (int i = 0; i < aryStr.length; i++) {
                if (StringUtils.isNotEmptyString(aryStr[i])) {
                    success = ftp.makeDirectory(aryStr[i]);
                    String newpath = new String(aryStr[i].getBytes(), FTP.DEFAULT_CONTROL_ENCODING);
                    ftp.changeWorkingDirectory(newpath);
                }
            }
            ftp.logout();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    public String[] download(String fileName, String urlStr) throws Exception {
        String[] result = new String[2];
        String ctxPath = FTPUtils.getRootPath("") + "ftp\\";
        URL url = new URL(this.getEncodeUrl(urlStr)); // 必须要转码才能获取
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)"); // 防止屏蔽程序抓取而返回403错误
        InputStream inputStream = null; // 得到输入流
        try {
            inputStream = conn.getInputStream();
        } catch (Exception e) {
            throw new VideoException("图片地址不能访问");
        }
        BufferedInputStream bi = new BufferedInputStream(inputStream);
        File saveDir = new File(ctxPath); // 文件保存位置
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        String filePath = saveDir + File.separator + fileName;// 最终文件保存位置
        result[0] = filePath;// 返回最终保存本地位置
        File file = new File(filePath);
        FileOutputStream bs = new FileOutputStream(file);
        result[1] = conn.getContentLength() / 1024 + "";// 返回文件大小
        byte[] by = new byte[1024];
        int len = 0;
        while ((len = bi.read(by)) != -1) {
            bs.write(by, 0, len);
        }
        bs.close();
        bi.close();
        return result;
    }

    public String getEncodeUrl(String str) throws UnsupportedEncodingException {
        String head = str.substring(0, str.lastIndexOf("/") + 1);
        String param = str.substring(str.lastIndexOf("/") + 1, str.length());
        String url = head + URLEncoder.encode(param, "UTF-8");
        url = url.replace("+", "%20");// 空格会转换成+号，替换空格
        // System.out.println("url--"+url);
        return url;
    }

    /**
     * 获取图帧从服务器ftp连接
     *
     * @return
     */
    public FTPClient getSlaveVideoFTP(String slaveIp) {
        FTPClient ocxftp = getConnectionFTP(slaveIp, Integer.parseInt(PropertiesUtil.getFtpPackey("ocx.file.service.port")), PropertiesUtil.getFtpPackey("video.service.uid"),
                PropertiesUtil.getFtpPackey("video.service.pwd"));

        return ocxftp;
    }

    /**
     * 通过服务器IP获取图帧ftp连接
     *
     * @return
     */
    public FTPClient getVideoFTPBySlaveIP(String slaveIp) {
        FTPClient ocxftp = getConnectionFTP(slaveIp, Integer.parseInt(PropertiesUtil.getFtpPackey("ocx.file.service.port")), PropertiesUtil.getFtpPackey("video.service.uid"),
                PropertiesUtil.getFtpPackey("video.service.pwd"));

        return ocxftp;
    }

    /**
     * 保存base64编码图片至ftp
     *
     * @param base64Str
     * @param fileName
     * @param filepath
     * @return
     */
    public String saveBase64ImgToFtp(String base64Str, String fileName, String filepath) {
        try {
            String root = getRootPath("/");
            String saveDir = root + "\\base64\\";

            //如果文件夹不存在 创建
            File dir = new File(saveDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            //生成图片
            ImageUtils.str2Image(base64Str, saveDir + fileName);

            File file = new File(saveDir + fileName);
            InputStream inputStream = new FileInputStream(file);
            String saveftpdir = PropertiesUtil.getFtpPackey("clue.service.path") + filepath;

            makeDir(saveftpdir);

            uploadFile(saveftpdir, fileName, inputStream);

            return saveftpdir + fileName;
        } catch (Exception e) {

        }
        return null;
    }

    public File downUrlFile(String fileName, String urlStr) throws Exception {
        URL urlUrl = new URL(urlStr);
        HttpURLConnection urlConn = (HttpURLConnection) urlUrl.openConnection();
        urlConn.setRequestMethod("GET");
        urlConn.setRequestProperty("accept", "*/*");
        urlConn.setRequestProperty("connection", "Keep-Alive");

        if (urlConn.getResponseCode() == 200) {
            System.out.println("COME IN");
            DataInputStream fis = new DataInputStream(urlConn.getInputStream());
            String ctxPath = FTPUtils.getRootPath("") + "ftp\\";
            File saveDir = new File(ctxPath); // 文件保存位置
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }
            String filePath = saveDir + File.separator + fileName;// 最终文件保存位置
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
            return file;
        } else {
            throw new RuntimeException(" responseCode is not 200 ... ");
        }
    }
}  
