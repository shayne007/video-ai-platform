package com.keensense.sdk.util;

import com.loocme.sys.util.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@Slf4j
public class HttpUtils {
	
	public static String  httpMultipartFormData(Map<String,String> textMap,Map<String,File> fileMap,String url){
		log.info("开始http调用地址:" + url);
    	CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost uploadFile = new HttpPost(url);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		if (MapUtil.isNotNull(textMap)) {
			Iterator<String> it = textMap.keySet().iterator();
			while (it.hasNext()) {
				String textName = (String) it.next();
				builder.addTextBody(textName,(String)textMap.get(textName), ContentType.TEXT_PLAIN);
			}
		}
		Map<String,String> tempFileMap = new HashMap<String,String>();
		// 把文件加到HTTP的post请求中
		if (MapUtil.isNotNull(fileMap)) {
			Iterator<String> it = fileMap.keySet().iterator();
			while (it.hasNext()) {
				String inputName  = (String) it.next();
				File file = (File)fileMap.get(inputName);
				try {
					tempFileMap.put(inputName, file.getPath());
					builder.addBinaryBody(inputName , new FileInputStream(file), 
							ContentType.APPLICATION_OCTET_STREAM,file.getName());
				} catch (FileNotFoundException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		HttpEntity multipart = builder.build();
		uploadFile.setEntity(multipart);
		log.info("请求form text data信息:" + textMap);
		log.info("请求form file data信息:" + tempFileMap);
		CloseableHttpResponse response = null;
		try {
			log.info("开始调用:");
			response = httpClient.execute(uploadFile);
			int statusCode = response.getStatusLine().getStatusCode();
			log.info("返回http响应码:" + statusCode);
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
		HttpEntity responseEntity = response.getEntity();
		String sResponse = null;
		try {
			sResponse = EntityUtils.toString(responseEntity, "UTF-8");
		} catch (ParseException | IOException e) {
			log.error(e.getMessage(),e);
		}
		log.info("响应信息：" + sResponse);
		return sResponse;
    }

}
