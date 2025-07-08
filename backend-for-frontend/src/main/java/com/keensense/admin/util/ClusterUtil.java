package com.keensense.admin.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.admin.constants.GlobalConstant;
import com.keensense.admin.dto.cluster.FeatureVo;
import com.keensense.common.platform.enums.ObjTypeEnum;
import com.loocme.security.encrypt.Base64;
import com.loocme.sys.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.keensense.common.platform.enums.ObjTypeEnum.*;

/**
 * @author jiuling
 * @date 2019/8/22
 */
@Slf4j
public class ClusterUtil {
    private static int featrueLen = 0;

    /**
     * 执行聚类方法
     *
     * @param operateId   任务号
     * @param featureVos  特征值
     * @param threshold   阈值
     * @param timeout     超时时间
     * @param type        聚类类型
     * @param companyType 人脸类型
     * @return
     * @throws IOException
     */
    public static JSONObject cluster(String operateId, List<FeatureVo> featureVos, double threshold,
                                     Long timeout, ObjTypeEnum type, String companyType) throws IOException {
        long start = System.currentTimeMillis();
        if (!featureVos.isEmpty()) {
            featrueLen = featureVos.get(featureVos.size() - 1).getFeat().getBytes().length;
        }
        String param = getHeadParam(threshold, type, companyType);
        String path = GlobalConstant.DATA_DIRECTORY + File.separator + operateId;
        String reqPath = path.concat("_req.json");
        String resultPath = path.concat("_req_result.json");
        File file = new File(reqPath);
        FileUtil.write(file, param);
        for (int i = 0; i < featureVos.size(); i++) {
            FeatureVo feature = featureVos.get(i);
            if (feature.getFeat().getBytes().length != featrueLen) {
                break;
            }
            JSONObject jo = new JSONObject();
            jo.put("uuid", feature.getUuid());
            jo.put("featureVector", feature.getFeat());

            String obj = jo.toJSONString();
            if (i < featureVos.size() - 1) {
                obj += ",";
            }
            FileUtil.write(file, obj, true);
        }
        long end = System.currentTimeMillis();

        FileUtil.write(file, "]}", true);
        log.info("write time " + (end - start));
        String os = System.getProperty("os.name");
        String command = "cluster";
        if (os.toLowerCase().startsWith("win")) {  //如果是Windows系统
            command += ".exe";
        }
        executeCommand(GlobalConstant.CLUSTER_DIRECTORY + File.separator + command + " --path " + reqPath, timeout);
        long excute = System.currentTimeMillis();
        log.info("excute time " + (excute - end));
        File resultFile = new File(resultPath);
        if (!resultFile.exists()) {
            log.error("聚类失败或已超时");
            return null;
        }
        JSONObject jo = JSON.parseObject(FileUtil.read(new File(resultPath)));
        JSONArray data = jo.getJSONArray("data");
        Map<String, List<String>> map = new HashMap<>();
        for (int i = 0; i < data.size(); i++) {
            JSONObject bean = data.getJSONObject(i);
            String clusterIndex = bean.getString("clusterIndex");
            if ("-1".equals(clusterIndex)) {
                List<String> list = new ArrayList<>();
                list.add(bean.getString("uuid"));
                map.put("single" + i, list);
                continue;
            }
            List<String> list = map.computeIfAbsent(clusterIndex, k -> new ArrayList<>());
            list.add(bean.getString("uuid"));
        }

        List<List<String>> clusters = new ArrayList<>();
        map.forEach((k, v) -> clusters.add(v));
        JSONObject result = new JSONObject();
        result.put("clusters", JSON.parse(JSON.toJSONString(clusters)));
        log.info("end time:" + (System.currentTimeMillis() - start));
        return result;
    }

    private static String convertStFeature(String feature) {
        byte[] decode = Base64.decode(feature.getBytes());
        if (decode.length == 1036) {
            byte[] bytes = Arrays.copyOfRange(decode, 12, decode.length);
            return new String(Base64.encode(bytes));
        }
        return feature;
    }


    /**
     * 获取聚类参数
     *
     * @param threshold   阈值
     * @param type        聚类类型
     * @param companyType 人脸类型
     * @return
     */
    public static String getHeadParam(double threshold, ObjTypeEnum type, String companyType) {
        JSONObject config = new JSONObject();
        switch (type) {
            case FACES:
                if (featrueLen == 2048) {
                    config.put("featureLen", 384);
                    config.put("featureType", "float");
                } else {
                    config.put("featureLen", 512);
                    config.put("featureType", "binary");
                }
                break;
            case CAR:
                config.put("featureType", "binary");
                config.put("featureLen", 512);
                break;
            default:
                config.put("featureType", "binary");
                config.put("featureLen", 1030);
                break;
        }

        config.put("clusterType", "2");
        config.put("tholdhold", threshold);
        JSONObject paramObj = new JSONObject();
        paramObj.put("config", config);
        String s = paramObj.toJSONString();
        return s.substring(0, s.lastIndexOf('}')) + ",\"data\":[";
    }

    /**
     * 执行脚本命令
     *
     * @param command
     * @param timeout
     * @return
     * @throws IOException
     */
    private static int executeCommand(String command, Long timeout) throws IOException {
        log.info("execute command is : {}", command);
        int exitStatus = -1;
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec(command);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
             BufferedReader errorBuffer = new BufferedReader(new InputStreamReader(p.getErrorStream()))
        ) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            if (result.length() > 0) {
                log.info("command:" + command + "\n" + result);
            }

            while ((line = errorBuffer.readLine()) != null) {
                sb.append(line).append("\n");
            }
            result = sb.toString();
            if (result.length() > 0) {
                log.error("command:" + command + " exitStatus:" + exitStatus + " result:" + result);
            }
            p.waitFor(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
        exitStatus = p.exitValue();
        return exitStatus;
    }

}
