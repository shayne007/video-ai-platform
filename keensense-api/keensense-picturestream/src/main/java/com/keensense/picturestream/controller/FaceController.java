package com.keensense.picturestream.controller;

import cn.jiuling.plugin.extend.FaceConstant;
import cn.jiuling.plugin.extend.picrecog.FaceAppMain;
import cn.jiuling.plugin.extend.picrecog.entity.RecogFaceResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.common.util.ResultUtils;
import com.keensense.picturestream.config.NacosConfig;
import com.keensense.picturestream.util.FaceUtil;
import com.keensense.picturestream.util.ValidUtil;
import com.keensense.picturestream.util.VideoExceptionUtil;
import com.keensense.picturestream.util.picture.SdkUtils;
import com.loocme.security.encrypt.Base64;
import com.loocme.sys.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 人脸图片结构化接口
 * @Author: wujw
 * @CreateDate: 2019/12/5 14:53
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@RestController
@RequestMapping("/picturestream/face")
@Api("图片数据控制器")
public class FaceController extends BaseController {

    @Autowired
    private NacosConfig nacosConfig;

    @ApiOperation(value = "人脸结构化", notes = "人脸结构化")
    @PostMapping(value = "/struct")
    public String struct(@RequestBody String body) {
        JSONObject paramJson = getJsonByBody(body);
        FaceAppMain faceApp = FaceUtil.getFaceAppMain();
        String picture = ValidUtil.validString(paramJson, "picture", true);
        Integer detectMode = ValidUtil.getInteger(paramJson, "detectMode",
                FaceConstant.TYPE_DETECT_MODE_BIG, FaceConstant.TYPE_DETECT_MODE_SMALL, false);
        JSONArray faces;
        if(nacosConfig.getFaceClassPath().contains(".QstFaceSdkInvokeImpl")){
            String structResult = SdkUtils.objectDetectionOnImage(Base64.decode(picture.getBytes()));
            if (StringUtil.isNull(structResult)) {
                throw VideoExceptionUtil.getValidException("提取人脸特征失败!");
            }
            JSONObject resultObj = JSON.parseObject(structResult);
            faces = getFaces(resultObj);
        } else {
            RecogFaceResult[] recogFaceResults;
            if(detectMode != null && detectMode == FaceConstant.TYPE_DETECT_MODE_SMALL){
                recogFaceResults = faceApp.recog(FaceConstant.TYPE_DETECT_MODE_SMALL, picture);
            } else {
                recogFaceResults = faceApp.recog(FaceConstant.TYPE_DETECT_MODE_BIG, picture);
            }
            faces = getFaceJson(recogFaceResults);
        }
        JSONObject result = new JSONObject(1);
        result.put("faces", faces);
        return ResultUtils.returnSuccess(result);
    }

    /***
     * @description: 获取人脸数据
     * @param resultObj 调用结构化返回的结果
     * @return: void
     */
    private JSONArray getFaces(JSONObject resultObj){
        JSONArray resultArray = new JSONArray(resultObj.size());
        JSONArray jsonArray = resultObj.getJSONArray("faces");
        if (jsonArray != null && !jsonArray.isEmpty()) {
            JSONObject temp;
            for (int i = 0; i < jsonArray.size(); i++) {
                temp = jsonArray.getJSONObject(i);
                resultArray.add(getObject(temp));
            }
        }
        return resultArray;
    }

    /***
     * @description: 返回到前端的结构化模型
     * @param objext  结构化对象
     * @return: com.alibaba.fastjson.JSONObject
     */
    private JSONObject getObject(JSONObject objext) {
        JSONObject result = new JSONObject(2);

        JSONObject snapshot = new JSONObject(1);
        JSONObject boundingBox = new JSONObject(4);
        boundingBox.put("x", objext.get("x"));
        boundingBox.put("y", objext.get("y"));
        boundingBox.put("w", objext.get("w"));
        boundingBox.put("h", objext.get("h"));
        snapshot.put("boundingBox", boundingBox);
        result.put("snapshot", snapshot);

        result.put("features", objext.get("featureVector"));
        return result;
    }

    private JSONArray getFaceJson(RecogFaceResult[] recogFaceResults){
        JSONArray faceJson = new JSONArray(recogFaceResults.length);
        for (RecogFaceResult recogFaceResult : recogFaceResults){
            JSONObject face = new JSONObject(2);
            face.put("features", recogFaceResult.getFeature());
            JSONObject snapshot = new JSONObject(1);
            JSONObject boundingBox = new JSONObject(4);
            boundingBox.put("x", recogFaceResult.getPointx());
            boundingBox.put("y", recogFaceResult.getPointy());
            boundingBox.put("h", recogFaceResult.getHeight());
            boundingBox.put("w", recogFaceResult.getWidth());
            snapshot.put("boundingBox", boundingBox);
            face.put("snapshot", snapshot);
            faceJson.add(face);
        }
        return faceJson;
    }
}
