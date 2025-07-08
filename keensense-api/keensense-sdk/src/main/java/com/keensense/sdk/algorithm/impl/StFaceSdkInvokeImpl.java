package com.keensense.sdk.algorithm.impl;


import com.keensense.common.exception.VideoException;
import com.keensense.sdk.algorithm.IFaceSdkInvoke;
import com.keensense.sdk.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class StFaceSdkInvokeImpl implements IFaceSdkInvoke {

	 private String stFaceServiceUrl = "";
	 private String stRootStorage = "";
	 private String fastSearch = "0";

	@Override
	public void initParams(Var param) {
		stFaceServiceUrl = param.getString("faceServiceUrl");
		stRootStorage = param.getString("stRootStorage");
		fastSearch = StringUtil.isNull(param.getString("stFastSearch"))?"0":param.getString("stFastSearch");
		if (StringUtil.isNull(stRootStorage)) {
			stRootStorage = Optional.ofNullable(ContextLoader.getCurrentWebApplicationContext())
					.map(WebApplicationContext::getServletContext)
					.map(context -> context.getRealPath("/"))
					.orElse("/");
		}
		if(!stFaceServiceUrl.endsWith("/")){
			stFaceServiceUrl += "/";
		}
	}

	@Override
	public String createRegistLib() {
		Map<String,String> paramMap = new HashMap();
		paramMap.put("dbName", RandomUtils.getRandom6ValiteCode(18));
		paramMap.put("fastSearch", fastSearch);
		String resp = "";
		try {
		    resp = HttpUtil.post(stFaceServiceUrl + "verify/target/add",paramMap);
		} catch (HttpConnectionException e) {
			log.error("请求人脸服务异常:" + e.getMessage(),e);
			return "";
		}
		Var jsonVar = Var.fromJson(resp);
		if("success".equals(jsonVar.getString("result"))){
			return jsonVar.getString("data.dbId");
		}else{
			log.error(String.format("创建目标库失败[%s]:%s",jsonVar.getString("result"),
					jsonVar.getString("errorMessage")));
			return "";
		}
	}

	@Override
	public String deleteRegistLib(String repoId) {
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("dbName", repoId);
		String resp = "";
		JSONObject result = new JSONObject();
		result.put("code", -1);
		try {
		    resp = HttpUtil.post(stFaceServiceUrl + "verify/target/deletes",paramMap);
		} catch (HttpConnectionException e) {
			log.error("请求人脸服务异常:" + e.getMessage(),e);
			return result.toString();
		}
		Var jsonVar = Var.fromJson(resp);
		if("success".equals(jsonVar.getString("result"))){
			result.put("code", 0);
			return result.toString();
		}else{
			log.error(String.format("删除目标库失败[%s]:%s",jsonVar.getString("result"),
					jsonVar.getString("errorMessage")));
			return result.toString();
		}
	}

	@Override
	public String getRegistLib(String repoId) throws VideoException {
		return repoId;
	}

	@Override
	public Var getPicAnalyze(String picture) {
		if(StringUtil.isNull(picture)){
			return null;
		}
		StringBuffer faceBase64 = new StringBuffer();
		picture = picture.replace("\n", "");
		Map<String, Integer> wh = ImageBaseUtil.getWH(picture,faceBase64);
		if (wh == null) {
			return null;
		}
		byte[] picBy = Base64.decode(faceBase64.toString().getBytes());
		//1、保存原图
		String suffix = PritureTypeUtil.readImageType(picBy);
		StringBuilder picPathBuilder = new StringBuilder(stRootStorage);
		picPathBuilder.append("stpic").append(File.separator);
		picPathBuilder.append(RandomUtils.uuid()).append(".").append(suffix);
		String picPath = picPathBuilder.toString();
		File orgFile = new File(picPath);
		File parentFile = orgFile.getParentFile();
		if (parentFile != null && !parentFile.exists()) {
			parentFile.mkdirs();
		}
		ImageBaseUtil.byte2image(picBy, picPath);
		Map<String, String> textMap = new HashMap<>();
		textMap.put("detectStrategy", "0");
		Map<String, File> fileMap = new HashMap<>();
		fileMap.put("imageData", orgFile);
		String sResponse = HttpUtils.httpMultipartFormData(textMap, fileMap,
				stFaceServiceUrl + "verify/face/detectAndQuality");
		Var jsonVar = Var.fromJson(sResponse);
		if ("success".equals(jsonVar.getString("result"))) {
			Var retVar = Var.newArray();
			WeekArray resultArr = jsonVar.getArray("data");
			int totalNum = resultArr.getSize();
			for (int i = 0; i < totalNum; i++) {
				Var faceVar = resultArr.get("[" + i + "]");
				int leftPixels = faceVar.getInt("rect[0]");
				int topPixels = faceVar.getInt("rect[1]");
				int widthPixels = faceVar.getInt("rect[2]") - leftPixels;
				int heightPixels = faceVar.getInt("rect[3]") - topPixels;
				File subFile = null;
				//人脸数量大于1需要抠出人脸图提取特征
				if(totalNum > 1){
					// 2、截图后的图片
					OperateImage operateImage = new OperateImage(leftPixels, topPixels, widthPixels, heightPixels);
					operateImage.setSrcpath(orgFile.getPath());
					operateImage
							.setSubpath(orgFile.getPath().substring(0, orgFile.getPath().lastIndexOf(".")) + i + ".jpg");
					try {
						operateImage.cut(suffix);
					} catch (IOException e) {
						log.error(e.getMessage(), e);
						continue;
					}
					subFile = new File(operateImage.getSubpath());
					fileMap.put("imageData", subFile);
				}
				String responseJson = HttpUtils.httpMultipartFormData(textMap, fileMap,
						stFaceServiceUrl + "verify/feature/gets");
				Var responseJsonVar = Var.fromJson(responseJson);
				deleteFile(subFile);
				if ("success".equals(responseJsonVar.getString("result"))) {
					retVar.set("[" + i + "].featureVector", responseJsonVar.getString("feature"));
					retVar.set("[" + i + "].quality", faceVar.getFloat("quality_score")/100);
					retVar.set("[" + i + "].x", leftPixels);
					retVar.set("[" + i + "].y", topPixels);
					retVar.set("[" + i + "].w", widthPixels);
					retVar.set("[" + i + "].h", heightPixels);
					retVar.set("[" + i + "].pose.pitch", faceVar.getString("pitch"));
					retVar.set("[" + i + "].pose.roll", faceVar.getString("roll"));
					retVar.set("[" + i + "].pose.yaw", faceVar.getString("yaw"));
				}else{
					log.error(
							String.format("获取特征数据失败[%s]:%s", responseJsonVar.getString("result"), responseJsonVar.getString("errorMessage")));
				}
			}
			deleteFile(orgFile);
			return retVar.isNull() ? null : retVar;
		} else {
			deleteFile(orgFile);
			log.error(
					String.format("人脸图片检测失败[%s]:%s", jsonVar.getString("result"), jsonVar.getString("errorMessage")));
			return null;
		}
	}

	@Override
	public Var getPicAnalyzeOne(String picture) {
		Var faces = this.getPicAnalyze(picture);
		if (null == faces) {
			return null;
		}
		return faces.get("[0]");
	}

	@Override
	public String addFaceToLib(String repoId, String feature, String url) {
		Map<String,String> paramMap = new HashMap<>();
		paramMap.put("dbName", repoId);
		paramMap.put("feature", feature);
		String resp = "";
		try {
			resp = HttpUtil.post(stFaceServiceUrl + "verify/feature/synAdd",paramMap);
		} catch (HttpConnectionException e) {
			log.error("请求人脸服务异常:" + e.getMessage(),e);
			return "";
		}
		Var jsonVar = Var.fromJson(resp);
		if("success".equals(jsonVar.getString("result"))){
			return jsonVar.getString("featureId");
		}else{
			log.error(String.format("人脸特征导入目标库失败[%s]:%s",jsonVar.getString("result"),
					jsonVar.getString("errorMessage")));
			return "";
		}
	}

	@Override
	public String addFaceToLib(String repoId, String feature, String url, String time) throws VideoException {
		return addFaceToLib(repoId, feature, url);
	}

	@Override
	public String delFaceFromLib(String repoId, String featureId) {
		Map<String,String> paramMap = new HashMap<>();
		paramMap.put("dbName", repoId);
		paramMap.put("imageId", featureId);
		String resp = "";
		try {
			resp = HttpUtil.post(stFaceServiceUrl + "verify/face/deletes",paramMap);
		} catch (HttpConnectionException e) {
			log.error("请求人脸服务异常:" + e.getMessage(),e);
			return "";
		}
		Var jsonVar = Var.fromJson(resp);
		if("success".equals(jsonVar.getString("result"))){
			return featureId;
		}else{
			log.error(String.format("删除人脸图片失败[%s]:%s",jsonVar.getString("result"),
					jsonVar.getString("errorMessage")));
			return "";
		}
	}

	@Override
	public String getFaceFeature(String repoId, String featureId) {
		return null;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public Var getSimilars(String regIds, String feature, float threshold, int maxResult) {
		Map<String,String> paramMap = new HashMap<>();
		paramMap.put("feature", feature);
		Var paramVar = Var.newObject();
		String[] regIdsArr = regIds.split(",");
		Var resultVar  = Var.newObject();
		for(int i=0; i< regIdsArr.length; i++){
			paramVar.set("searchOptions[" + i + "].dbName", regIdsArr[i]);
			paramVar.set("searchOptions[" + i + "].topNum", maxResult);
			paramVar.set("searchOptions[" + i + "].score", threshold/100);
		}
		paramMap.put("params", paramVar.toString());
		String resp = "";
		try {
			resp = HttpUtil.post(stFaceServiceUrl + "verify/face/multi_search",paramMap);
		} catch (HttpConnectionException e) {
			log.error("请求人脸服务异常:" + e.getMessage(),e);
			return resultVar;
		}
		Var jsonVar = Var.fromJson(resp);
		if("success".equals(jsonVar.getString("result"))){
			WeekArray resultArray = jsonVar.getArray("search_result");
			if(resultArray != null && resultArray.getSize() > 0){
				JSONArray resultJson = new JSONArray();
				for(int k =0 ;k < resultArray.getSize(); k++) {
					Var curVar = resultArray.get("[" + k + "]");
					WeekArray dataArray = curVar.getArray("data");
					for(int i=0; i<dataArray.getSize(); i++){
						Var tmpVar = dataArray.get("[" + i + "]");
						JSONObject tempJson = new JSONObject();
						Float score = tmpVar.getFloat("score") * 100;
						tempJson.put("score", score);
						JSONObject faceJson = new JSONObject();
						faceJson.put("id", tmpVar.getString("imageId"));
						faceJson.put("faceGroupId", tmpVar.getString("dbId"));
						tempJson.put("face", faceJson);
						resultJson.add(tempJson);
					}
				}
				resultJson.sort(Comparator.comparing(obj -> ((JSONObject) obj).getDouble("score")).reversed());
				JSONArray sortJsonArray = new JSONArray();
				for (int i = 0; i < resultJson.size(); i++) {
					if(i <= maxResult - 1){
						sortJsonArray.add(resultJson.get(i));
					}else{
						break;
					}
				}
				resultVar = Var.fromJson(sortJsonArray.toString());
			}
			return resultVar;
		}else{
			log.error(String.format("指定目标库中搜索相似目标失败[%s]:%s",jsonVar.getString("result"),
					jsonVar.getString("errorMessage")));
			return resultVar;
		}
	}

	@Override
	public Var getSimilars(String regIds, String feature, float threshold, int maxResult, String startTime, String endTime) throws VideoException {
		return getSimilars(regIds, feature, threshold, maxResult);
	}

	@Override
	public float compareFeature(String feature1, String feature2) {
		Map<String,String> paramMap = new HashMap<>();
		paramMap.put("feature1", feature1);
		paramMap.put("feature2", feature2);
		paramMap.put("detectStrategy", "0");
		String resp = "";
		try {
			resp = HttpUtil.post(stFaceServiceUrl + "verify/feature/compare",paramMap);
		} catch (HttpConnectionException e) {
			log.error("请求人脸服务异常:" + e.getMessage(),e);
			return 0;
		}
		Var jsonVar = Var.fromJson(resp);
		if("success".equals(jsonVar.getString("result"))){
			return jsonVar.getFloat("score") * 100;
		}else{
			log.error(String.format("特征比对失败[%s]:%s",jsonVar.getString("result"),
					jsonVar.getString("errorMessage")));
			return 0;
		}
	}

	private void deleteFile(File file) {
		try {
			Files.delete(file.toPath());
		} catch (IOException e) {
			log.error("delete file {} fail cause {}",file.getAbsolutePath(),e.getMessage());
		}
	}

}
