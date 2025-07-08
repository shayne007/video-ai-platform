package com.keensense.admin.util;
 
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class Ftp {
	static SimpleDateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
	/**
	 * 获取配置文件中的配置
	 *
	 * @return
	 */
	public static Map<String, String> findProperties() {
		Map<String, String> map = new HashMap<String, String>();
		Properties prop = new Properties();
		try {
			InputStream inStream = Ftp.class.getClassLoader().getResourceAsStream("reader.properties");
			prop.load(inStream); /// 加载属性列表
			Iterator<String> it = prop.stringPropertyNames().iterator();
			while (it.hasNext()) {
				String key = it.next();
				map.put(key, prop.getProperty(key));
			}
			inStream.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return map;
	}
    /**  
     * Description: 向FTP服务器上传文件  
     * @param filename 上传到FTP服务器上的文件名(图片的名字)  
     * @param catalog 模块目录名字（功能模块名称）  
     * @param input 输入流（文件流）  
     * @return 返回路径
     */

    private static String ftp_ip = "172.16.1.66";//服务IP
    private static String ftp_port = "21";//端口
    private static String ftp_user = "chiwailam";//账号
    private static String ftp_password = "abcd1234!";//密码
    private static String ftp_upload = "/uploadUrl";//上传目录

    public static String uploadFile(String filename, InputStream input,String catalog) {
    	
		String url = ftp_ip;//IP地址
		String username = ftp_user;//用户名
		String password = ftp_password;//密码
		String basePath = ftp_upload;//路径
		String por = ftp_port;//端口号
		String filePath = dfm.format(new Date());//当前时间
		int port = Integer.parseInt(por);
    	String tempPath = ""; //保存图片进入的路径
    	String path = "";//返回全路径
        FTPClient ftp = new FTPClient();  
        try {  
            int reply;
            ftp.connect(url, port);// 连接FTP服务器
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器  
            ftp.login(username, password);// 登录
            //设置上传文件的类型为二进制类型  
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return tempPath;  
            }
            //切换到上传目录  
            if (!ftp.changeWorkingDirectory(catalog)) {//判断是否有模块目录
            	//创建模块文件夹
                String[] mkds1 = catalog.split("/"); 
                for(int i =0 ;i<mkds1.length;i++){  
                    if(mkds1[i]!=null && mkds1[i]!=""){  
                        ftp.makeDirectory(mkds1[i]);//添加目录  
                    }
                   ftp.changeWorkingDirectory(mkds1[i]); //添加完目录后 必须进入目录 才能在创建目录
                } 
                if(!ftp.changeWorkingDirectory(filePath)) {//进入模块目录后判断是否有日期目录
                	  String[] mkds = filePath.split("/");  
      	              for(int y =0 ;y<mkds.length;y++){  
      	                  if(mkds[y]!=null && mkds[y]!=""){  
      	                      ftp.makeDirectory(mkds[y]);  
      	                  } 
      	                ftp.changeWorkingDirectory(mkds[y]); //添加完目录后 必须进入目录 才能在创建目录
      	              } 
              	}
            }else if(!ftp.changeWorkingDirectory(filePath)){//如果有进入目录创建日期目录
            	  String[] mkds = filePath.split("/");  
	              for(int i =0 ;i<mkds.length;i++){  
	                  if(mkds[i]!=null && mkds[i]!=""){  
	                      ftp.makeDirectory(mkds[i]);  
	                  }
	                  ftp.changeWorkingDirectory(mkds[i]); //添加完目录后 必须进入目录 才能在创建目录
	              } 
            }
            tempPath = basePath+"/"+catalog+"/"+filePath;
      	    ftp.changeWorkingDirectory(tempPath);
      	    ftp.storeFile(filename, input);//存入图片
            input.close(); 
            ftp.logout();
            path = "http://"+url+":8001"+"/"+catalog+"/"+filePath+"/"+filename;
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
        }  
        return path;  
    }
} 
