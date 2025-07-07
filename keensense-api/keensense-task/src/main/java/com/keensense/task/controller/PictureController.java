package com.keensense.task.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.common.exception.VideoException;
import com.keensense.common.util.ResultUtils;
import com.keensense.task.constants.PictureConstants;
import com.keensense.task.util.TaskParamValidUtil;
import com.keensense.task.util.VideoExceptionUtil;
import com.keensense.task.util.picture.SdkUtils;
import com.keensense.task.util.picture.StringUtils;
import com.loocme.security.encrypt.Base64;
import com.loocme.sys.util.PatternUtil;
import com.loocme.sys.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;

/**
 * @Description: 图片分析COntroller
 * @Author: wujw
 * @CreateDate: 2019/5/27 15:02
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Api(value = "图片分析接口")
@RestController
@Slf4j
@RequestMapping("/rest/feature")
public class PictureController extends BaseController {

    private static final String BASE_URL = "/rest/feature";

    private static final String EXTRACT_METHOD = "/extractFromPicture";

    private static final String STRUCT_METHOD = "/structPicture";

    @ApiOperation(value = "图片结构化", notes = "图片结构化")
    @PostMapping(value = "/structPicture")
    public String structPicture(@RequestBody String body) {
        JSONObject paramJson = getJsonByBody(body,BASE_URL+STRUCT_METHOD);
        Integer objType = TaskParamValidUtil.isPositiveInteger(paramJson, "objtype",
                PictureConstants.OBJ_TYPE_ALL, PictureConstants.OBJ_TYPE_ALL, PictureConstants.OBJ_TYPE_BIKE);
        String picture = TaskParamValidUtil.validString(paramJson, "picture", true);
        try {
            byte[] picBy = null;
            // 获取图片
            if (PatternUtil.isMatch(picture, "^(http|ftp).*$", Pattern.CASE_INSENSITIVE)) {
                picBy = downloadPicture(picture);
            } else {
                // base64解析
                picBy = Base64.decode(picture.getBytes());
            }
            picBy = this.getPictureByte(picBy);
            if (picBy.length > PictureConstants.MAX_UPLOAD_SIZE) {
                log.info("=============picture length: " + picBy.length);
                throw VideoExceptionUtil.getValidException("图片大小不能超过2M");
            }

            String structResult = SdkUtils.objectDetectionOnImage(picBy);
            if (StringUtil.isNull(structResult)) {
                throw VideoExceptionUtil.getValidException("图片结构化失败");
            }
            JSONObject resultObj = JSON.parseObject(structResult);
            JSONArray objexts;
            JSONObject result = new JSONObject(3);

            if(objType == PictureConstants.OBJ_TYPE_ALL){
                objexts = getObjexts(objType,resultObj);
                getFaces(objexts, resultObj);
            }else if(objType == PictureConstants.OBJ_TYPE_FACE){
                objexts = new JSONArray();
                getFaces(objexts,resultObj);
            }else{
                objexts = getObjexts(objType,resultObj);
            }
            result.put("objexts", objexts);
            return ResultUtils.returnSuccess(result);
        }catch (VideoException e) {
            throw e;
        } catch (Exception e) {
            log.error("structPicture failed", e);
            throw VideoExceptionUtil.getValidException("系统异常！");
        }
    }

    @ApiOperation(value = "图片特征提取", notes = "图片特征提取")
    @PostMapping(value = "/extractFromPicture")
    public String extractFromPicture(@RequestBody String body) {
        JSONObject paramJson = getJsonByBody(body,BASE_URL+EXTRACT_METHOD);
        Integer objType = TaskParamValidUtil.isPositiveInteger(paramJson, "objtype", -1,
                PictureConstants.OBJ_TYPE_HUMAN, PictureConstants.OBJ_TYPE_BIKE);
        String picture = TaskParamValidUtil.validString(paramJson, "picture", true);
        byte[] picBy = Base64.decode(picture.getBytes());
        picBy = this.getPictureByte(picBy);
        if (picBy.length > PictureConstants.MAX_UPLOAD_SIZE) {
            log.info("=============picture length: " + picBy.length);
            throw VideoExceptionUtil.getValidException("图片太大，只能提取单个目标特征");
        }
        JSONObject result = SdkUtils.getFeatureFrom(objType, picBy);
        return ResultUtils.returnSuccess(result);
    }

    /***
     * @description: 获取人脸数据
     * @param resultArray 结果返回集合
     * @param resultObj 调用结构化返回的结果
     * @return: void
     */
    private void getFaces(JSONArray resultArray, JSONObject resultObj){
        JSONArray jsonArray = resultObj.getJSONArray("faces");
        if (jsonArray != null && !jsonArray.isEmpty()) {
            JSONObject temp;
            for (int i = 0; i < jsonArray.size(); i++) {
                temp = jsonArray.getJSONObject(i);
                Integer mainType = temp.getInteger("mainType");
                resultArray.add(getObject(mainType, temp));
            }
        }
    }

    /***
     * @description: 获取人脸数据
     * @param objType 类型
     * @param resultObj 调用结构化返回的结果
     * @return: void
     */
    private JSONArray getObjexts(Integer objType, JSONObject resultObj){
        JSONArray objexts = new JSONArray();
        JSONArray jsonArray = resultObj.getJSONArray("objexts");
        if (jsonArray != null && !jsonArray.isEmpty()) {
            objexts = new JSONArray();
            JSONObject temp;
            for (int i = 0; i < jsonArray.size(); i++) {
                temp = jsonArray.getJSONObject(i);
                Integer mainType = temp.getInteger("mainType");
                if (objType == 0 || objType.equals(mainType)) {
                    objexts.add(getObject(mainType, temp));
                }
            }
        }
        return objexts;
    }

    /***
     * @description: 返回到前端的结构化模型
     * @param objType 结构化类型
     * @param objext  结构化对象
     * @return: com.alibaba.fastjson.JSONObject
     */
    private JSONObject getObject(Integer objType, JSONObject objext) {
        JSONObject result = new JSONObject(6);
        result.put("objType", objType);

        JSONObject snapshot = new JSONObject(1);
        JSONObject boundingBox = new JSONObject(4);
        boundingBox.put("x", objext.get("x"));
        boundingBox.put("y", objext.get("y"));
        boundingBox.put("w", objext.get("w"));
        boundingBox.put("h", objext.get("h"));
        snapshot.put("boundingBox", boundingBox);
        result.put("snapshot", snapshot);

        JSONObject features = new JSONObject(1);
        features.put("featureData", objext.get("featureVector"));
        result.put("features", features);
        return result;
    }

    /***
     * @description: 获取图片Base64编码，如果是png、gif则转换成Jpg格式
     * @param picBy 图片Base64编码
     * @return: void
     */
    private byte[] getPictureByte(byte[] picBy) {
        String suffix = StringUtils.getExtension(picBy);
        log.info("=================根据base64获取图片的格式：" + suffix);
        if (StringUtil.isNull(suffix)) {
            throw VideoExceptionUtil.getValidException("不支持的图片格式");
        }

        if (PatternUtil.isNotMatch(suffix, "^(jpg|jpeg)$", Pattern.CASE_INSENSITIVE)) {
            picBy = StringUtils.forJpg(picBy);
            if (picBy.length == 0) {
                throw VideoExceptionUtil.getValidException("不支持的图片格式");
            }
        }
        return picBy;
    }

    /***
     * @description: 下载图片
     * @param pictureUrl 图片地址
     * @return: byte[]
     */
    private byte[] downloadPicture(String pictureUrl) throws IOException {
        URLConnection conn;
        try {
            // 下载图片
            URL url = new URL(pictureUrl);
            conn = url.openConnection();
            // 设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            conn.setReadTimeout(10 * 1000);
            // 防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            InputStream inputStream = conn.getInputStream();
            if (null != inputStream) {
                return StringUtils.input2byte(inputStream);
            }
        } catch (RuntimeException e) {
            log.error("downloadPicture error", e);
        }
        throw VideoExceptionUtil.getValidException("下载图片失败");
    }

}
