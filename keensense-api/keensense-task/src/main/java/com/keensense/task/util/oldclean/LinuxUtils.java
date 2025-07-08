package com.keensense.task.util.oldclean;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class LinuxUtils {

	public static int execueteCommand(String command) throws IOException{
	    log.info("execute command is :" + command);
		int exitStatus = -1;
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec(command);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));  
	        StringBuffer sb = new StringBuffer();  
	        String line;  
	        while ((line = br.readLine()) != null) {  
	            sb.append(line).append("\n");  
	        }  
	        String result = sb.toString();
	        if(result.length()> 0){
	        	log.info("command:"+ command +"\n"+ result);
	        }
	        BufferedReader errorBuffer = new BufferedReader(new InputStreamReader(p.getErrorStream()));  
	        while ((line = errorBuffer.readLine()) != null) {  
	            sb.append(line).append("\n");  
	        }  
	        result = sb.toString(); 
	        if(result.length() > 0){
				log.error("command:"+ command +" exitStatus:"+exitStatus+" result:"+ result);
			}
			p.waitFor();
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}
		exitStatus = p.exitValue();
		return exitStatus;
	}
	
	public static int execueteCommand(String[] command) throws IOException{
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec(command);
		int exitStatus = -1;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));  
	        StringBuffer sb = new StringBuffer();  
	        String line;  
	        while ((line = br.readLine()) != null) {  
	            sb.append(line).append("\n");  
	        }  
	        String result = sb.toString();
	        if(result.length()> 0){
	        	log.info("command:"+ command[2] + result);
	        }
	        BufferedReader errorBuffer = new BufferedReader(new InputStreamReader(p.getErrorStream()));  
	        while ((line = errorBuffer.readLine()) != null) {  
	            sb.append(line).append("\n");  
	        }  
	        result = sb.toString();  
	        p.waitFor();
			exitStatus = p.exitValue();
			if(result.length() > 0){
				log.error("command:"+ command[2] +" exitStatus:"+exitStatus+" result:"+ result);
			}
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		} 
		return exitStatus;
	}
	
	public static String execueteCommand(String command,int flag) throws IOException{
	    log.info("execute command is :" + command);
	    JSONObject json = new JSONObject();
		int exitStatus = -1;
		String message = "success";
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec(command);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));  
			BufferedReader errorBuffer = new BufferedReader(new InputStreamReader(p.getErrorStream()));  
	        StringBuffer sb = new StringBuffer();  
	        String line;  
	        while ((line = br.readLine()) != null) {  
	            sb.append(line).append("\n");  
	        }  
	        String result = sb.toString();
	        
	        while ((line = errorBuffer.readLine()) != null) {  
	            sb.append(line).append("\n");  
	        }  
	        result = sb.toString();
	        if(result.length()>0){
	        	message = result;
	        }
			p.waitFor();
			exitStatus = p.exitValue();
			if(exitStatus != 0){
				exitStatus = -1;
			}
			json.put("exitStatus", exitStatus);
	    	json.put("message", message);
		} catch (InterruptedException e) {
			log.error(e.getMessage());
			message = e.getMessage();
			json.put("exitStatus", exitStatus);
        	json.put("message", message);
		}
		
    	return json.toString();
	}
	
	public static String execueteCommand(String[] command,int flag) throws IOException{
		JSONObject json = new JSONObject();
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec(command);
		int exitStatus = -1;
		String message = "success";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));  
	        StringBuffer sb = new StringBuffer();  
	        String line;  
	        while ((line = br.readLine()) != null) {  
	            sb.append(line).append("\n");  
	        }  
	        String result = sb.toString();

	        
	        if(result.length()>0){
	        	json.put("exitStatus", 0);
	        	message = result;
	        }else{
	        	BufferedReader errorBuffer = new BufferedReader(new InputStreamReader(p.getErrorStream()));  
		        while ((line = errorBuffer.readLine()) != null) {  
		            sb.append(line).append("\n");  
		        }  
		        result = sb.toString();
		        if(result.length()>0){
		        	message = result;
		        }else{
		        	message = "未找到此进程信息";
		        }
		        json.put("exitStatus", -1);
	        }
	        p.waitFor();
			exitStatus = p.exitValue();
	    	json.put("message", message);
		} catch (InterruptedException e) {
			log.error(e.getMessage());
			message = e.getMessage();
			json.put("exitStatus", exitStatus);
        	json.put("message", message);
		} 
		return json.toString();
	}

}
