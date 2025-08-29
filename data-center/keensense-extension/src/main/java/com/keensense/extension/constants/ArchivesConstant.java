package com.keensense.extension.constants;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.extension.entity.dto.ArchivesDTO;
import com.keensense.sdk.sys.utils.DbPropUtil;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/5/14 14:54
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class ArchivesConstant {
    public static final Integer TRACE_SOURCE_EXCEPTION = -1;
    public static final Integer TRACE_SOURCE_FACE = 1;
    public static final Integer TRACE_SOURCE_BODY = 2;

    public static final Float BODY_SCORE_DEFAULT = 0f;
    public static final Float FACE_SCORE_DEFAULT = 0f;
    public static final Float BODY_QUALITY_DEFAULT = 0f;
    public static final Float FACE_QUALITY_DEFAULT = 0f;

    public static final Float FACE_SCORE_COMPARE_DEFAULT = 100f;
    public static final Float BODY_SCORE_COMPARE_DEFAULT = 100f;

    public static final Integer BODY_PIC_NUM = 3;

    public static final Integer ARCHIVES_FACE_FRONT = 1;
    public static final Integer ARCHIVES_FACE_SIDE = 2;
    public static final Integer ARCHIVES_FACE_BOTTOM = 3;

    public static final String BODY_AUTO_SOURCE_TYPE = "1";
    public static final String BODY_EXT_SOURCE_TYPE = "2";

    public static final String KEENSENSE_SEARCH_FACE = "/VIID/Faces";
    public static final String KEENSENSE_SEARCH_BODY = "/VIID/Persons";


    public static float[] frontYaw = {0,10};
    public static float[] frontPitch = {0,15};

    public static float[] sideYaw = {15,30};
    public static float[] sidePitch = {0,10};

    public static float[] bottomYaw = {0,15};
    public static float[] bottomPitch = {10,15};

    /***
     * @description: 获取角度前需要先更新角度
     * @param
     * @return: void
     */
    public static void initAngle(){

        String[] fPitch = DbPropUtil.getString("archives.front.face.pitch","0,15").split(",");
        String[] fYaw = DbPropUtil.getString("archives.front.face.yaw","0,10").split(",");
        frontYaw[0] = Float.parseFloat(fYaw[0]);
        frontYaw[1] = Float.parseFloat(fYaw[1]);
        frontPitch[0] = Float.parseFloat(fPitch[0]);
        frontPitch[1] = Float.parseFloat(fPitch[1]);
        String[] sPitch = DbPropUtil.getString("archives.side.face.pitch","0,10").split(",");
        String[] sYaw = DbPropUtil.getString("archives.side.face.yaw","15,30").split(",");
        sideYaw[0] = Float.parseFloat(sYaw[0]);
        sideYaw[1] = Float.parseFloat(sYaw[1]);
        sidePitch[0] = Float.parseFloat(sPitch[0]);
        sidePitch[1] = Float.parseFloat(sPitch[1]);
        String[] bPitch = DbPropUtil.getString("archives.bottom.face.pitch","10,15").split(",");
        String[] bYaw = DbPropUtil.getString("archives.bottom.face.yaw","0,15").split(",");
        bottomYaw[0] = Float.parseFloat(bYaw[0]);
        bottomYaw[1] = Float.parseFloat(bYaw[1]);
        bottomPitch[0] = Float.parseFloat(bPitch[0]);
        bottomPitch[1] = Float.parseFloat(bPitch[1]);
    }

    private ArchivesConstant(){}
    /**
     * 获取搜图模块id
     * */
    public static String[] getSearchResultIds(Map<String, Object> var){
        JSONArray array = JSON.parseArray(var.toString());
        String[] sbr = new String[array.size()];
        for (int i = 0; i < array.size(); i++) {
            String str = array.get(i)+"";
            JSONObject object = JSON.parseObject(str);
            JSONObject object1 =JSON.parseObject(object.getString("face"));
            sbr[i] = object1.getString("id");
        }
        return sbr;
    }
    /**
     * 获取搜图模块id
     * */
    public static Map<String,Float> getSearchResultIdsAndScore(Map<String, Object> var){
        Map<String,Float> aidFaceScore = new HashMap<>();
        JSONArray array = JSON.parseArray(var.toString());
        for (int i = 0; i < array.size(); i++) {
            String str = array.get(i)+"";
            JSONObject object = JSON.parseObject(str);
            JSONObject object1 =JSON.parseObject(object.getString("face"));
            aidFaceScore.put(object1.getString("id"),object.getFloat("score"));
        }
        return aidFaceScore;
    }


    public static float calProportion(Float rightBtmX,Float rightBtmY,
        Float leftTopX,Float leftTopY){
        float proportion = 0f;
        if(rightBtmY!=null&&leftTopY!=null&&rightBtmX!=null&&leftTopX!=null){
            float denominator = (float)rightBtmX-leftTopX;
            float molecule = (float)rightBtmY-leftTopY;
            if(Float.floatToRawIntBits(denominator) !=0){
                proportion = formatFloat(molecule*1.000f/denominator);
            }
        }
        return proportion;
    }

    public static float calProportion(ArchivesDTO archivesDTO){
        float proportion = 0f;
        if(archivesDTO.getRightBtmY()!=null&&archivesDTO.getLeftTopY()!=null&&
			archivesDTO.getRightBtmX()!=null&&archivesDTO.getLeftTopX()!=null){
			float denominator = (float)archivesDTO.getRightBtmX()-archivesDTO.getLeftTopX();
			float molecule = (float)archivesDTO.getRightBtmY()-archivesDTO.getLeftTopY();
			if(Float.floatToRawIntBits(denominator) !=0){
			    proportion = formatFloat(molecule*1.000f/denominator);
				archivesDTO.setProportion(proportion);
			}
		}else{
			archivesDTO.setProportion(0f);
		}
        return proportion;
    }

    public static Float formatFloat(Float num){
        DecimalFormat df = new DecimalFormat("#.0000");
        if(num!=null){
            num = Float.parseFloat(df.format(num));
        }else{
            num = 0.0000f;
        }
        return num;
    }

}
