package com.keensense.task.util.picture;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.keensense.common.image.GstlRecogHttp;
import com.keensense.common.image.OesObjectHandleManagerUtil;
import com.keensense.task.constants.CarConst;
import com.keensense.task.constants.PictureConstants;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @Description: 图片分析COntroller
 * @Author: wujw
 * @CreateDate: 2019/5/27 15:02
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class SdkUtils {

    private SdkUtils(){}

	public static JSONObject getFeatureFrom(Integer objType,byte[] pictureByte){
		JSONObject resultMap = new JSONObject();
		if(OesObjectHandleManagerUtil.getIsGstl() && objType == PictureConstants.OBJ_TYPE_VEHICLE){
			//GSLT车辆图片特征获取 type默认为1
			String result = GstlRecogHttp.getExtractFromPictureByGstl(1,pictureByte);
			JSONObject jsonObject = JSON.parseObject(result);
			resultMap.put("feature", jsonObject.getString("Features"));
            resultMap.put("color", -1);
		}else{
			String result = getFeatureFromSDK(objType,pictureByte);
			JSONObject jsonObject = JSON.parseObject(result);
			resultMap.put("feature", jsonObject.getString("featureVector"));
            JSONObject features = jsonObject.getJSONObject("features");
            if(objType == PictureConstants.OBJ_TYPE_VEHICLE){
                Integer SDKType = Optional.ofNullable(features).map(p -> p.getInteger("subType")).orElse(0);
                resultMap.put("VehicleClass", CarConst.getGBCarType(SDKType));
                resultMap.put("color", getColor(features, "dominantColor"));
            } else if(objType == PictureConstants.OBJ_TYPE_BIKE){
                resultMap.put("bikeGenre", features.getInteger("subType"));
                resultMap.put("color", getBikeColor(features, "upperBodyColor"));
            }else if(objType == PictureConstants.OBJ_TYPE_HUMAN){
            	resultMap.put("sexType",jsonObject.getString("sexType"));
			}
		}
		return resultMap;
	}

	public static String objectDetectionOnImage(byte[] picture){
        return ObjextAppHttp.objectDetectionOnImageByRest(picture);
    }

    private static String getFeatureFromSDK(Integer objType,byte[] pictureByte){
        return ObjextAppHttp.getExtractFromPictureByRest(objType,pictureByte);
    }

	private static int getColor(JSONObject objextVar, String key){
		Integer result = -1;
		try {
			JSONObject colerObject = Optional.ofNullable(objextVar.getJSONArray(key)).map(p -> p.getJSONObject(0)).orElse(null);
			if(colerObject != null){
//				int r = colerObject.getInteger("r");
//				int b = colerObject.getInteger("b");
//				int g = colerObject.getInteger("g");
//				int sdkCode = SdkUtils.getColor(r, g, b);
//				result = ColorConst.getSDKColorByCode(sdkCode);
				if(colerObject.getInteger("colorTag") != null){
					result = colerObject.getInteger("colorTag").intValue();
				}
			}
		} catch (Exception e){
			log.error("图片结构化解析颜色失败！");
		}
		return result;
	}

	private static int getBikeColor(JSONObject objextVar, String key){
		Integer result = -1;
		try {
			JSONObject colerObject = Optional.ofNullable(objextVar.getJSONArray(key)).map(p -> p.getJSONObject(0)).orElse(null);
			if(colerObject != null){
//				int r = colerObject.getInteger("r");
//				int b = colerObject.getInteger("b");
//				int g = colerObject.getInteger("g");
//				return SdkUtils.getColor(r, g, b);
				if(colerObject.getInteger("colorTag") != null){
					result = colerObject.getInteger("colorTag").intValue();
				}
			}
		} catch (Exception e){
			log.error("图片结构化解析颜色失败！");
		}
		return result;
	}

	private static int getColor(int r, int g, int b){
		return ( r | ( (short)(g) << 8 ) ) | ( ( (short)b ) << 16 );
	}
}
