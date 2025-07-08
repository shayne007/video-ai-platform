package com.keensense.admin.util;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 9:56 2019/11/20
 * @Version v0.1
 */
@Slf4j
public class InterestUtil {
    public static String initInterestParam(String featurFollowarea) {
        List<JSONObject> udrVerticesList = new LinkedList<>();
        // 感兴趣区域节点
        if (StringUtils.isNotEmptyString(featurFollowarea)) {
            String[] featureArr = featurFollowarea.split(";");
            if (featureArr != null && featureArr.length > 0) {
                String[] verticesNumArray = null;
                String[] verticesArr = null;
                Double minRate = 0.01;
                Double maxRate = 0.98;
                try {
                    String minRateStr = PropertiesUtil.getParameterPackey("min.rate");
                    String maxRateStr = PropertiesUtil.getParameterPackey("max.rate");
                    if (StringUtils.isNotEmptyString(minRateStr)) {
                        minRate = Double.valueOf(minRateStr);
                    }
                    if (StringUtils.isNotEmptyString(maxRateStr)) {
                        maxRate = Double.valueOf(maxRateStr);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                // 每个区域循环
                for (int i = 0; i < featureArr.length; i++) {
                    JSONObject featureJSONObject = new JSONObject();
                    // 节点数量
                    verticesNumArray = featureArr[i].split("\\|");
                    String verticesStr = featureArr[i].replace("|", ",");
                    verticesArr = verticesStr.split(",");
                    // 点位信息
                    if (verticesArr != null) {
                        List<Double> verticesList = new LinkedList<>();
                        Double d = 0D;
                        for (int j = 0; j < verticesArr.length; j++) {
                            if (Double.valueOf(verticesArr[j]) > maxRate) {
                                d = Double.valueOf(1);
                            } else if (Double.valueOf(verticesArr[j]) < minRate) {
                                d = Double.valueOf(0);
                            } else {
                                d = Double.valueOf(verticesArr[j]);
                            }
                            verticesList.add(d);
                        }
                        featureJSONObject.put("vertices", verticesList);
                    }
                    udrVerticesList.add(featureJSONObject);
                }
            }
        }

        Set<String> verticesSet = new HashSet<>();
        for (int i = 0; i < udrVerticesList.size(); i++) {
            String vertice = "";
            JSONArray vertices = udrVerticesList.get(i).getJSONArray("vertices");
            for (int j = 0; j < vertices.size(); j++) {
                String v = vertices.getString(j);
                if (StringUtils.isEmpty(vertice)) {
                    vertice = v;
                } else {
                    vertice = vertice + "," + v;
                }
            }
            verticesSet.add("\"" + vertice + "\"");
        }
        return verticesSet.toString();
    }

    public static String initTripwires(String tripwires) {
        Set<String> tripwiresSet = new HashSet<>();
        // 支持画多个线
        String[] tripwiresArry = tripwires.split(";");
        for (int i = 0; i < tripwiresArry.length; i++) {
            StringBuilder sb = new StringBuilder();
            String[] tripwiresPoint = tripwiresArry[i].split(",");
            if (tripwiresPoint.length >= 5) {
                sb.append(String.valueOf(Integer.parseInt(tripwiresPoint[0].equals("2") ? "0" : tripwiresPoint[0])));
                sb.append(",");
                sb.append(String.valueOf(Double.parseDouble(tripwiresPoint[1])));
                sb.append(",");
                sb.append(String.valueOf(Double.parseDouble(tripwiresPoint[2])));
                sb.append(",");
                sb.append(String.valueOf(Double.parseDouble(tripwiresPoint[3])));
                sb.append(",");
                sb.append(String.valueOf(Double.parseDouble(tripwiresPoint[4])));
            }
            tripwiresSet.add("\"" + sb + "\"");
        }
        return tripwiresSet.toString();
    }
}
