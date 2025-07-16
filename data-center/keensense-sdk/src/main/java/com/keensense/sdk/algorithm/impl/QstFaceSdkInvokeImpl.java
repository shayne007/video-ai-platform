package com.keensense.sdk.algorithm.impl;


import com.keensense.common.exception.VideoException;
import com.keensense.sdk.algorithm.IFaceSdkInvoke;
import com.keensense.sdk.constants.BodyConstant;
import com.keensense.sdk.constants.CommonConst;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class QstFaceSdkInvokeImpl implements IFaceSdkInvoke {

//	private FaceAppMain faceApp;

	@Override
	public void initParams(Map<String,Object> param) {
//		sdkInvoke.initParams(param);
//		this.faceApp = FaceAppMain.getInstance(FaceConstant.TYPE_COMPANY_QST, Var.newObject());
	}

	@Override
	public String createRegistLib() throws VideoException{
		return  BodyConstant.getBodySdkInvoke().createRegistLib();
	}

	@Override
	public String deleteRegistLib(String repoId) throws VideoException{
		return  BodyConstant.getBodySdkInvoke().deleteRegistLib(repoId);
	}

	@Override
	public String getRegistLib(String repoId) throws VideoException{
		return repoId;
	}

	@Override
	public Map<String,Object> getPicAnalyze(String picture) throws VideoException{
		return  BodyConstant.getBodySdkInvoke().getPicAnalyze(CommonConst.OBJ_TYPE_FACE, picture);
	}

	@Override
	public Map<String,Object> getPicAnalyzeOne(String picture) throws VideoException{
		return  BodyConstant.getBodySdkInvoke().getPicAnalyzeOne(CommonConst.OBJ_TYPE_FACE, picture);
	}

	@Override
	public String addFaceToLib(String repoId, String feature, String url) throws VideoException{
		return  BodyConstant.getBodySdkInvoke().addBodyToLib(repoId, "", CommonConst.OBJ_TYPE_FACE, feature);
	}

	@Override
	public String addFaceToLib(String repoId, String feature, String url, String time) throws VideoException {
		return null;
	}

	@Override
	public String delFaceFromLib(String repoId, String featureId) throws VideoException{
		return  BodyConstant.getBodySdkInvoke().delBodyFromLib(repoId, CommonConst.OBJ_TYPE_FACE, featureId);
	}

	@Override
	public String getFaceFeature(String repoId, String featureId) throws VideoException {
		return null;
	}

	@Override
	public Map<String,Object> getSimilars(String regIds, String feature, float threshold, int maxResult) throws VideoException{
//		Var result =  BodyConstant.getBodySdkInvoke().getSimilars(CommonConst.OBJ_TYPE_FACE, regIds, feature, threshold, maxResult, false);
//		JSONArray resultJson = new JSONArray();
//		if(result != null){
//			result.foreach(new IVarForeachHandler() {
//				private static final long serialVersionUID = 1L;
//				@Override
//				public void execute(String paramString, Var tempVar) {
//					JSONObject tempJson = new JSONObject();
//					Float score = tempVar.getFloat("score") *100;
//					if(score >= threshold){
//						tempJson.put("score", score);
//						JSONObject faceJson = new JSONObject();
//						faceJson.put("id", tempVar.getString("uuid"));
//						faceJson.put("faceGroupId", tempVar.getString("task"));
//						tempJson.put("face", faceJson);
//						resultJson.add(tempJson);
//					}
//				}
//			});
//		}
//		Var resultVar = Var.fromJson(resultJson.toString());
		return new HashMap<>();
	}

	@Override
	public Map<String,Object> getSimilars(String regIds, String feature, float threshold, int maxResult, String startTime, String endTime) throws VideoException {
		return null;
	}

	@Override
	public float compareFeature(String feature1, String feature2) throws VideoException{
//		return (float) this.faceApp.compare(feature1, feature2) * 100;
		return 0;
	}

}
