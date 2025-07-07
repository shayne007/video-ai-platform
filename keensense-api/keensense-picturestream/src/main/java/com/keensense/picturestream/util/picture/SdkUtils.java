package com.keensense.picturestream.util.picture;

import com.keensense.picturestream.constants.PictureConstants;
import lombok.extern.slf4j.Slf4j;

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
	
	public static String getFeatureFrom(Integer objType,byte[] pictureByte){
		if(OesObjectHandleManagerUtil.getIsGstl() && objType == PictureConstants.OBJ_TYPE_VEHICLE){
            //GSLT车辆图片特征获取 type默认为1
			return  GstlRecogHttp.getExtractFromPictureByGstl(1,pictureByte);
		}else{
			return getFeatureFromSDK(objType,pictureByte);
		}
	}

	public static String objectDetectionOnImage(byte[] picture){
        return ObjextAppHttp.objectDetectionOnImageByRest(picture);
    }

    private static String getFeatureFromSDK(Integer objType,byte[] pictureByte){
        return ObjextAppHttp.getExtractFromPictureByRest(objType,pictureByte);
    }
}
