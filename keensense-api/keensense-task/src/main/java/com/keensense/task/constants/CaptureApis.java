package com.keensense.task.constants;

import com.alibaba.fastjson.JSONObject;
import com.keensense.common.config.SpringContext;
import com.keensense.common.util.HttpClientUtil;
import com.keensense.task.config.NacosConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 抓拍机配置
 * @Author: wujw
 * @CreateDate: 2019/5/15 17:07
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class CaptureApis {

    private CaptureApis() {
    }

    private static NacosConfig nacosConfig = SpringContext.getBean(NacosConfig.class);

    /***
     * 编码code的key值
     */
    public static final String STATUS_CODE = "statusCode";

    /**
     * 启动抓拍机API
     */
    private static final String START_CAPTURE = "/camera/startCapture";
    /**
     * 抓拍机停止
     */
    private static final String STOP_CAPTURE = "/camera/stopCapture";
    /**
     * 请求返回代码语义转换
     */
    private static Map<String, String> revers = new HashMap<>();

    private static List<String> successCode = new ArrayList<>(2);

    static {
        revers.put("0", "请求成功");
        revers.put("-1", "抓拍机申请内存错误");
        revers.put("-2", "连接抓拍机模块异常");
        revers.put("-3", "连接抓拍机模块连接异常");
        revers.put("-201", "错误的请求内容,请确认密钥是否正确");
        revers.put("-202", "任务ID不存在");
        revers.put("-203", "错误的相机IP,请确认IP是否存在或者已启动");
        revers.put("-204", "相机任务初始化失败，请确认用户名/密码/IP/端口是否正确，或抓拍机已启动");
        revers.put("-205", "停止相机任务失败");
        revers.put("-206", "错误的http请求类型");
        revers.put("-207", "http请求内容为空");

        successCode.add("0");
        successCode.add("-202");
    }

    /***
     * @description: 判断是否成功
     * @param statusCode 返回码
     * @return: boolean
     */
    public static boolean isSuccess(String statusCode) {
        return successCode.contains(statusCode);
    }

    /***
     * @description: 启动抓拍机
     * @param taskID 任务ID
     * @param ip     抓拍机地址
     * @param port   抓拍机端口
     * @param username 用户名
     * @param password 密码
     * @param deviceId 设备ID
     * @param cameraId 点位ID
     * @return: com.alibaba.fastjson.JSONObject
     */
    public static JSONObject startCapture(String taskID, String ip, String port, String username
            , String password, String deviceId, String cameraId) {
        JSONObject jo = new JSONObject();
        jo.put("taskID", taskID);
        jo.put("ip", ip);
        jo.put("port", port);
        jo.put("username", username);
        jo.put("password", password);
        jo.put("deviceID", deviceId);
        jo.put("monitorID", cameraId);
        return postWithJson(START_CAPTURE, jo.toString());
    }

    /**
     * 停止抓拍机
     *
     * @param taskID   任务号
     * @param deviceID 设备号
     * @return JSONObject
     */
    public static JSONObject stopCapture(String taskID, String deviceID) {
        JSONObject jo = new JSONObject();
        jo.put("taskID", taskID);
        jo.put("deviceID", deviceID);
        return postWithJson(STOP_CAPTURE, jo.toString());
    }

    private static JSONObject postWithJson(String url, String paramJson) {
        JSONObject result = new JSONObject();
        String resultPost;
        try {
            resultPost = HttpClientUtil.requestPost(nacosConfig.getCaptureUrl() + url, null, paramJson);
            if(StringUtils.isEmpty(resultPost)){
                result.put(CaptureApis.STATUS_CODE, "-2");
            }else{
                result = JSONObject.parseObject(resultPost);
            }
        } catch (Exception e) {
            log.error("connect capture failed", e);
            result.put(CaptureApis.STATUS_CODE, "-2");
        }
        result.put("message", revers.get(result.getString(CaptureApis.STATUS_CODE)));
        return result;
    }
}
