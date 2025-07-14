package com.keensense.task.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.keensense.common.config.SpringContext;
import com.keensense.common.exception.VideoException;
import com.keensense.task.config.NacosConfig;
import com.keensense.task.constants.ObjextTaskConstants;
import com.keensense.task.entity.VasUrlEntity;
import com.keensense.task.constants.TaskConstants;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 任务相关接口参数工具类
 * @Author: wujw
 * @CreateDate: 2019/5/10 10:30
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class TaskParamValidUtil {

    private TaskParamValidUtil() {
    }

    private static NacosConfig nacosConfig = SpringContext.getBean(NacosConfig.class);

    private static final String MATCH_SERIALNUMBER = "^[0-9a-zA-Z]{1,64}$";
    /**
     * density的最大值
     */
    private static final int DENSITY_MAX_VALUE = 20;
    /**
     * sensitivity的最大值
     */
    private static final int SENSITIYITY_MAX_VALUE = 5;
    /**
     * outputDSFactor的最小值
     */
    private static final int OUTPUTDSFACTOR_MIN_VALUE = 1;
    /**
     * outputDSFactor的最大值
     */
    private static final int OUTPUTDSFACTOR_MAX_VALUE = 4;
    /**
     * ObjMinSize的最大值
     */
    private static final int OBJMINSIZE_MAX_VALUE = 2073600;
    /**
     * ObjMinSize的最大值
     */
    private static final int OBJMAXSIZE_MAX_VALUE = 8000000;
    /**
     * dominantColor的最大值
     */
    private static final int DOMINANTCOLOR_MAX_VALUE = 9;
    /**
     * dir的最小值
     */
    private static final int DIR_MIN_VALUE = -1;
    /**
     * dir的最大值
     */
    private static final int DIR_MAX_VALUE = 1;

    /***
     * @description: 校验任务类型
     * @param paramJson 请求参数
     * @return: 任务类型
     */
    public static String validAnalyType(JSONObject paramJson) {
        String analyType = paramJson.getString("type");
        if (StringUtils.isEmpty(analyType)) {
            throw VideoExceptionUtil.getValidException("type不能为空!");
        } else if (!TaskConstants.getAnalyTypeList().contains(analyType)) {
            throw VideoExceptionUtil.getValidException("请求参数type非法");
        } else {
            return analyType;
        }
    }

    /***
     * @description: 校验参数中的字符串
     * @param paramJson  请求参数
     * @param key        key值
     * @param required  是否必填
     * @return: java.lang.String
     */
    public static String validString(JSONObject paramJson, String key, boolean required) {
        String val = paramJson.getString(key);
        if (required && StringUtils.isEmpty(val)) {
            throw VideoExceptionUtil.getValidException(key + " 不能为空！");
        }
        return val;
    }

    /***
     * @description: 校验时间字符串
     * @param paramJson  请求参数
     * @param key        key值
     * @param required  是否必填
     * @return: java.lang.String
     */
    public static Timestamp validTime(JSONObject paramJson, String key, boolean required) {
        String val = paramJson.getString(key);
        if (required && StringUtils.isEmpty(val)) {
            throw VideoExceptionUtil.getValidException(key + " 不能为空！");
        }
        if (StringUtils.isEmpty(val)) {
            return null;
        } else if (ValidUtil.isTimeLegal(val)) {
            return Timestamp.valueOf(val);
        } else {
            throw VideoExceptionUtil.getValidException(key + "时间格式不正确,格式为[yyyy-MM-dd HH:mm:ss]");
        }
    }

    /***
     * @description: 校验参数中的字符串
     * @param paramJson  请求参数
     * @param key        key值
     * @param maxLength 最大长度
     * @param required  是否必填
     * @return: java.lang.String
     */
    public static String validString(JSONObject paramJson, String key, int maxLength, boolean required) {
        String val = validString(paramJson, key, required);
        if (!StringUtils.isEmpty(val) && val.length() > maxLength) {
            throw VideoExceptionUtil.getValidException(key + " 最多只能输入" + maxLength + "位");
        }
        return val;
    }

    /***
     * @description: 校验任务ID
     * @param paramJson 请求参数
     * @param required 是否必填
     * @return: 任务号
     */
    public static String validSerialNumber(JSONObject paramJson, boolean required) {
        String serialnumber = paramJson.getString("serialnumber");
        if (StringUtils.isEmpty(serialnumber)) {
            if (required) {
                throw VideoExceptionUtil.getValidException("serialnumber不能为空!");
            }
        } else {
            if (PatternMatchUtils.simpleMatch(serialnumber, MATCH_SERIALNUMBER)) {
                throw VideoExceptionUtil.getValidException("serialnumber只能是最多64位的英文、数字");
            }
        }
        return serialnumber;
    }

    /***
     * @description: 根据入参获取感兴趣区域
     * @param paramJson 请求参数
     * @return: void
     */
    public static JSONObject getInterestedByParam(JSONObject paramJson) {
        String udrVerticesKey = "udrVertices";
        JSONArray udrVertices = paramJson.getJSONArray(udrVerticesKey);
        Boolean isInterested = TaskParamValidUtil.getBoolean(paramJson, "isInterested", true, false);
        if (udrVertices != null && !udrVertices.isEmpty()) {
            JSONObject udrSetting = getUdrSetting(udrVertices, isInterested);
            paramJson.remove(udrVerticesKey);
            return udrSetting;
        } else {
            return ObjextTaskConstants.getUdrsetDefaultMap();
        }
    }

    /***
     * @description: 获取感兴趣区域
     * @param udrVertices 感兴趣区域参数
     * @return: com.alibaba.fastjson.JSONObject
     */
    public static JSONObject getUdrSetting(JSONArray udrVertices, Boolean isInterested) {
        JSONObject udrSetting = new JSONObject(3);
        udrSetting.put("isInterested", isInterested);
        udrSetting.put("udrNum", udrVertices.size());
        List<Object> udrVerticesList = new ArrayList<>(udrVertices.size());
        for (int i = 0; i < udrVertices.size(); i++) {
            JSONObject udrVertice = new JSONObject(2);
            List<Float> verticesList = new ArrayList<>();
            String[] vertices = udrVertices.get(i).toString().split(",");
            for (int j = 0; j < vertices.length; j++) {
                if (ValidUtil.isFloat(vertices[j])) {
                    verticesList.add(Float.valueOf(vertices[j]));
                } else {
                    throw VideoExceptionUtil.getValidException("vertices[" + i + "][" + j + "] 只能为FLoat类型");
                }
            }
            udrVertice.put("vertices", verticesList);
            udrVertice.put("verticesNum", vertices.length / 2);
            udrVerticesList.add(udrVertice);
        }
        udrSetting.put("udrVertices", udrVerticesList);
        return udrSetting;
    }

    /****
     * @description: 获取抓拍机IP参数
     * @param paramJson 请求参数
     * @return: int
     */
    public static String getIp(JSONObject paramJson) {
        String ip = paramJson.getString("ip");
        if (StringUtils.isEmpty(ip)) {
            throw VideoExceptionUtil.getValidException("ip不能为空！");
        } else {
            if (ValidUtil.isIp(ip)) {
                return ip;
            } else {
                throw VideoExceptionUtil.getValidException("ip格式不正确！");
            }
        }
    }

    /****
     * @description: 获取抓拍机端口
     * @param paramJson 请求参数
     * @return: int
     */
    public static String getPort(JSONObject paramJson) {
        String port = paramJson.getString("port");
        if (StringUtils.isEmpty(port)) {
            throw VideoExceptionUtil.getValidException("port不能为空！");
        } else {
            if (ValidUtil.isPort(port)) {
                return port;
            } else {
                throw VideoExceptionUtil.getValidException("port格式不正确！");
            }
        }
    }

    /****
     * @description: 获取视频浓缩density参数
     * @param paramJson 请求参数
     * @param defaultVal 默认值
     * @return: int
     */
    public static Integer getDensity(JSONObject paramJson, String key, int defaultVal) {
        return isPositiveInteger(paramJson, key, defaultVal, 0, DENSITY_MAX_VALUE);
    }

    /***
     * @description: 从请求参数中获取sensitivity参数
     * @param paramJson 请求参数
     * @param key 键值
     * @param defaultVal 默认值
     * @return: int
     */
    public static Integer getSensitivity(JSONObject paramJson, String key, int defaultVal) {
        return isPositiveInteger(paramJson, key, defaultVal, 0, SENSITIYITY_MAX_VALUE);
    }

    /***
     * @description: 从请求参数中获取scene参数
     * @param paramJson 请求参数
     * @return: int
     */
    public static Integer getScene(JSONObject paramJson, String key, int defaultVal) {
        Integer scene = isPositiveInteger(paramJson, key, defaultVal, 0, 16);
        if (!TaskConstants.getSceneList().contains(scene)) {
            throw VideoExceptionUtil.getValidException("scene的值只能为" + TaskConstants.getSceneList().toString() + "中的一个");
        }
        return scene;
    }

    /***
     * @description: 从请求参数中获取outputDSFactor参数
     * @param paramJson 请求参数
     * @param key 键值
     * @param defaultVal 默认值
     * @return: int
     */
    public static Integer getOutputDSFactor(JSONObject paramJson, String key, int defaultVal) {
        return isPositiveInteger(paramJson, key, defaultVal, OUTPUTDSFACTOR_MIN_VALUE, OUTPUTDSFACTOR_MAX_VALUE);
    }

    /***
     * @description: 从请求参数中获取objMinSize参数
     * @param paramJson 请求参数
     * @param key 键值
     * @param defaultVal 默认值
     * @return: int
     */
    public static Integer getObjMinSize(JSONObject paramJson, String key, int defaultVal) {
        return isPositiveInteger(paramJson, key, defaultVal, 0, OBJMINSIZE_MAX_VALUE);
    }

    /***
     * @description: 从请求参数中获取objMaxSize参数
     * @param paramJson 请求参数
     * @param key 键值
     * @param defaultVal 默认值
     * @return: int
     */
    public static Integer getObjMaxSize(JSONObject paramJson, String key, int defaultVal) {
        return isPositiveInteger(paramJson, key, defaultVal, OBJMINSIZE_MAX_VALUE, OBJMAXSIZE_MAX_VALUE);
    }

    /***
     * @description: 从请求参数中获取objMaxSize参数
     * @param paramJson 请求参数
     * @param key 键值
     * @param defaultVal 默认值
     * @return: int
     */
    public static Integer getDominantColor(JSONObject paramJson, String key, int defaultVal) {
        return isPositiveInteger(paramJson, key, defaultVal, 0, DOMINANTCOLOR_MAX_VALUE);
    }

    /**
     * @param paramJson    请求参数
     * @param key          键值
     * @param defaultValue 默认值
     * @param minValue     最大值
     * @param maxValue     最小值
     * @description: 从请求参数中获取固定值，若没有则按默认值获取
     * @return: int
     */
    public static Integer isPositiveInteger(JSONObject paramJson, String key, Integer defaultValue, int minValue, int maxValue) {
        if (paramJson.containsKey(key)) {
            String value = paramJson.getString(key);
            if (ValidUtil.isInteger(value)) {
                BigDecimal val = new BigDecimal(value);
                BigDecimal min = new BigDecimal(minValue);
                BigDecimal max = new BigDecimal(maxValue);
                if (val.compareTo(min) == -1 || val.compareTo(max) == 1) {
                    throw VideoExceptionUtil.getValidException(key + "的值必须在为[" + minValue + "," + maxValue + "]之间的整数");
                }
                return val.intValue();
            } else {
                throw VideoExceptionUtil.getValidException(key + "的值必须在为[" + minValue + "," + maxValue + "]之间的整数");
            }
        }
        return defaultValue;
    }

    /**
     * @param paramJson 请求参数
     * @param key       键值
     * @param required  是否必填
     * @param minValue  最大值
     * @param maxValue  最小值
     * @description: 从请求参数中获取固定值，若没有则按默认值获取
     * @return: int
     */
    public static Integer getInteger(JSONObject paramJson, String key, int minValue, int maxValue, boolean required) {
        Integer result = null;
        if (paramJson.containsKey(key)) {
            result = isPositiveInteger(paramJson, key, null, minValue, maxValue);
        }
        if (required && result == null) {
            throw VideoExceptionUtil.getValidException(key + "不能为空");
        }
        return result;
    }

    /**
     * @param paramJson    请求参数
     * @param key          键值
     * @param defaultValue 默认值
     * @param minValue     最大值
     * @param maxValue     最小值
     * @description: 从请求参数中获取固定值，若没有则按默认值获取
     * @return: int
     */
    public static Long isPositiveLong(JSONObject paramJson, String key, long defaultValue, long minValue, long maxValue) {
        if (paramJson.containsKey(key)) {
            String value = paramJson.getString(key);
            if (ValidUtil.isInteger(value)) {
                BigDecimal val = new BigDecimal(value);
                BigDecimal min = new BigDecimal(minValue);
                BigDecimal max = new BigDecimal(maxValue);
                if (val.compareTo(min) == -1 || val.compareTo(max) == 1) {
                    throw VideoExceptionUtil.getValidException(key + "的值必须在为[" + minValue + "," + maxValue + "]之间的整数");
                }
                return val.longValue();
            } else {
                throw VideoExceptionUtil.getValidException(key + "的值必须在为[" + minValue + "," + maxValue + "]之间的整数");
            }
        }
        return defaultValue;
    }

    /**
     * @param paramJson    请求参数
     * @param key          键值
     * @param maxLength    最大长度
     * @param minValue     最大值
     * @param maxValue     最小值
     * @param defaultValue 默认值
     * @description: 从请求参数中获取固定值，若没有则按默认值获取
     * @return: int
     */
    public static Float getFloat(JSONObject paramJson, String key, int maxLength, Float minValue, Float maxValue, Float defaultValue) {
        if (paramJson.containsKey(key)) {
            String value = paramJson.getString(key);
            if (value.length() > maxLength) {
                throw VideoExceptionUtil.getValidException(key + "长度不能超过" + maxLength + "位");
            }
            Float val = Float.parseFloat(value);
            if (ValidUtil.isFloat(value)) {
                if (val < minValue || val > maxValue) {
                    throw VideoExceptionUtil.getValidException(key + "的值必须在为[" + minValue + "," + maxValue + "]之间的数字");
                }
                return val;
            } else {
                throw VideoExceptionUtil.getValidException(key + "的值必须在为[" + minValue + "," + maxValue + "]之间的数字");
            }
        }
        return defaultValue;
    }

    /***
     * @description: 获取Boolean
     * @param paramJson    请求参数
     * @param key          键值
     * @param defaultValue 默认值
     * @param required 是否必填
     * @return: boolean
     */
    public static Boolean getBoolean(JSONObject paramJson, String key, Boolean defaultValue, boolean required) {
        String value = paramJson.getString(key);
        if(StringUtils.isEmpty(value)){
            if(required){
                throw VideoExceptionUtil.getValidException(key + "的值不能为空!");
            }else{
                return defaultValue;
            }
        }else{
            value = value.toLowerCase();
            if(!value.equals("true") && !value.equals("false")){
                throw VideoExceptionUtil.getValidException(key + "的值只能为Boolean类型!");
            }else{
                return Boolean.parseBoolean(value);
            }
        }
    }

    /***
     * @description: 获取视频浓缩划线参数
     * @param tripWiresArr 划线参数
     * @return: com.alibaba.fastjson.JSONArray
     */
    public static JSONArray getTripWires(JSONArray tripWiresArr) {
        if (tripWiresArr != null && !tripWiresArr.isEmpty()) {
            JSONArray resultArr = new JSONArray(tripWiresArr.size());
            for (int i = 0; i < tripWiresArr.size(); i++) {
                String tripWireStr = tripWiresArr.getString(i);
                if (!StringUtils.isEmpty(tripWireStr)) {
                    String[] tripWireList = tripWireStr.split(",");
                    if (tripWireList.length != 5) {
                        throw getTripWireException(i, null);
                    } else {
                        JSONObject tripWire = new JSONObject(5);
                        setTripWireValue(tripWire, "stPointX", tripWireList[1], i, 1);
                        setTripWireValue(tripWire, "stPointY", tripWireList[2], i, 2);
                        setTripWireValue(tripWire, "edPointX", tripWireList[3], i, 3);
                        setTripWireValue(tripWire, "edPointY", tripWireList[4], i, 4);
                        setTripDirValue(tripWire, tripWireList[0], i);
                        resultArr.add(tripWire);
                    }
                }
            }
            return resultArr;
        }
        return new JSONArray(0);
    }

    /***
     * @description: 根据url获取录像分片的开始时间，结束时间和vasUrl
     * @param url 视频路径
     * @return: VasUrl
     */
    public static VasUrlEntity getVasParamByUrl(String url) {
        String startTime = "";
        String endTime = "";
        StringBuilder vasURL = new StringBuilder();
        String[] vasSliceArray = url.split("&");
        for (String vasSlice : vasSliceArray) {
            if (vasSlice.contains("startTime")) {
                startTime = vasSlice.split("=")[1];
            } else if (vasSlice.contains("starttime")) {
                startTime = vasSlice.split("=")[1];
            } else if (vasSlice.contains("endTime")) {
                endTime = vasSlice.split("=")[1];
            } else if (vasSlice.contains("endtime")) {
                endTime = vasSlice.split("=")[1];
            } else if (vasSlice.contains("srvip")) {
                vasURL.append(getAnalyIp(vasSlice));
            } else if (vasSlice.contains("srvport")) {
                vasURL.append(getAnalyPort(vasSlice));
            } else {
                vasURL.append(vasSlice).append("&");
            }
        }
        if (!ValidUtil.isTimeLegal(startTime)) {
            throw VideoExceptionUtil.getValidException("url的startTime格式不正确,格式为[yyyy-MM-dd HH:mm:ss]");
        }

        if (!ValidUtil.isTimeLegal(endTime)) {
            throw VideoExceptionUtil.getValidException("url的endTime格式不正确,格式为[yyyy-MM-dd HH:mm:ss]");
        }

        if (endTime.compareTo(startTime) <= 0) {
            throw VideoExceptionUtil.getValidException("url的endTime必须大于startTime!");
        }
        return new VasUrlEntity(DateUtil.getTimestampOfDateTime(DateUtil.parseDate(startTime)),
                DateUtil.getTimestampOfDateTime(DateUtil.parseDate(endTime)), vasURL.toString());
    }

    /***
     * @description: 获取vas中的IP值
     * @param vasSlice vasIp值
     * @return: java.lang.String
     */
    private static String getAnalyIp(String vasSlice) {
        String vasAnalyIp = nacosConfig.getVasIp();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(vasAnalyIp)) {
            return "srvip=" + vasAnalyIp + "&";
        } else {
            return vasSlice + "&";
        }
    }

    /***
     * @description: 获取vas中的port值
     * @param vasSlice port值
     * @return: java.lang.String
     */
    private static String getAnalyPort(String vasSlice) {
        String vasAnalyPort = nacosConfig.getVasPort();
        if (!StringUtils.isEmpty(vasAnalyPort)) {
            return "srvport=" + vasAnalyPort + "&";
        } else {
            return vasSlice + "&";
        }
    }

    /***
     * @description: 校验值是否是Float，是则置入JSONObject中
     * @param tripWire JSONObject
     * @param value 值
     * @param loopIndex 对象下标
     * @return: boolean
     */
    private static void setTripDirValue(JSONObject tripWire, String value, int loopIndex) {
        if (ValidUtil.isInteger(value)) {
            Integer dir = Integer.parseInt(value);
            if (dir < DIR_MIN_VALUE || dir > DIR_MAX_VALUE) {
                throw VideoExceptionUtil.getValidException("tripWire[" + loopIndex + "][0]的取值范围为[" + DIR_MIN_VALUE + "," + DIR_MAX_VALUE + "]");
            }
            tripWire.put("tripWireType", dir);
        } else {
            throw getTripWireException(loopIndex, 0);
        }
    }

    /***
     * @description: 校验值是否是Float，是则置入JSONObject中
     * @param tripWire JSONObject
     * @param key 存入的key
     * @param value 值
     * @param loopIndex 对象下标
     * @param index 数组下标
     * @return: boolean
     */
    private static void setTripWireValue(JSONObject tripWire, String key, String value, int loopIndex, int index) {
        if (ValidUtil.isFloat(value)) {
            tripWire.put(key, value);
        } else {
            throw getTripWireException(loopIndex, index);
        }
    }

    /***
     * @description: 获取划线异常
     * @param index 对象下标
     * @param num  数组下标
     * @return: com.keensense.common.exception.VideoException
     */
    private static VideoException getTripWireException(int index, Integer num) {
        throw VideoExceptionUtil.getValidException("tripWire[" + index + "][" + num + "]的值不合法！");
    }

}
