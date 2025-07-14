package com.keensense.task.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.task.entity.TaskCleanInterim;
import com.keensense.task.entity.TaskCleanYmdData;
import com.keensense.task.entity.TbAnalysisTask;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @Description: 删除任务工具类
 * @Author: wujw
 * @CreateDate: 2019/6/24 15:12
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class DeleteTaskUtil {

    private DeleteTaskUtil() {
    }

    /***
     * @description: 查询最早的es记录入参
     * @param serialnumbers 任务号:x1,x2,x3
     * @return: java.util.Map<java.lang.String , java.lang.Object>
     */
    public static Map<String, Object> getEarliestParamMap(String serialnumbers) {
        Map<String, Object> map = new HashMap<>();
        map.put("Result.MarkTime.Order", "asc");
        map.put("Result.PageRecordNum", 100);
        map.put("Result.RecordStartNo", 1);
        map.put("Result.Serialnumber.In", serialnumbers);
        return map;
    }

    /***
     * @description: 删除任务数据
     * @param serialnumber 任务号
     * @param ymd 删除的时间
     * @return: java.util.Map<java.lang.String , java.lang.Object>
     */
    public static Map<String, Object> deleteTask(String serialnumber, String ymd) {
        Map<String, Object> map = new HashMap<>();
        map.put("Serialnumber", serialnumber);
        map.put("Time", ymd);
        return map;
    }

    /***
     * @description: 解析查询数据
     * @param dataJson 调用查询模块返回的json字符串
     * @return: com.alibaba.fastjson.JSONArray 没有查询到值或者出错时都返回null
     */
    public static JSONArray getDeleteList(String dataJson) {
        JSONObject dataMap = JSON.parseObject(dataJson);
        JSONObject unitListObject = dataMap.getJSONObject("UnitListObject");
        if (unitListObject == null) {
            log.error("get data failed, json = " + dataJson);
        } else {
            Integer count = unitListObject.getInteger("Count");
            if (count > 0) {
                JSONArray jsonArray = unitListObject.getJSONArray("UnitObject");
                if (!jsonArray.isEmpty()) {
                    return jsonArray;
                } else {
                    log.info("can not get data,json = " + dataJson);
                }
            } else {
                log.info("can not get data,json = " + dataJson);
            }
        }
        return null;
    }

    /***
     * @description: 获取删除返回状态
     * @param resultStr 返回结果
     * @return:
     */
    public static Integer getDeleteStatus(String resultStr) {
        JSONObject resultJson = JSON.parseObject(resultStr);
        return resultJson.getInteger("ErrorCode");
    }

    /***
     * @description: 获取任务列表中所有的任务号
     * @param list 任务列表
     * @return: java.lang.String
     */
    public static String getSerialnumbers(List<TbAnalysisTask> list) {
        StringBuilder sb = new StringBuilder();
        for (TbAnalysisTask tbAnalysisTask : list) {
            sb.append(tbAnalysisTask.getId()).append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }

    /***
     * @description: 获取创建时间
     * @param jsonObject 对象
     * @return: java.lang.String
     */
    public static String getCreateTime(JSONObject jsonObject) {
        return jsonObject.getString("MarkTime");
    }

    /***
     * @description: 根据创建时间获取字符串
     * @param createTime 创建时间
     * @return: java.lang.String
     */
    public static String getDayByCreateTime(String createTime) {
        return Optional.ofNullable(createTime).filter(p -> p.length() == 14)
                .map(p -> p.substring(0, 8)).orElse(null);
    }

    /***
     * @description: 获取删除截止时间
     * @param yyyyMMdd 时间
     * @return: java.time.LocalDate
     */
    public static LocalDateTime getEndTime(String yyyyMMdd) {
        try {
            LocalDate localDate = Optional.ofNullable(yyyyMMdd)
                    .map(p -> DateUtil.parseLocalDate(p, DateUtil.YYYYMMDD)).orElse(null);
            if (localDate != null) {
                return LocalDateTime.of(localDate, LocalTime.of(23, 59, 59));
            }
        } catch (DateTimeParseException e) {
            log.error("parse date failed, date = " + yyyyMMdd, e);
        }
        return null;
    }

    /***
     * @description: 获取删除截止时间
     * @param localDateTime 时间
     * @return: java.time.LocalDate
     */
    public static LocalDateTime getEndTime(LocalDateTime localDateTime) {
        LocalDate localDate = localDateTime.toLocalDate().plusDays(1);
        return LocalDateTime.of(localDate, LocalTime.of(0, 0, 0));
    }

    /***
     * @description: 计算删除截止时间
     * @param localDateTime 时间
     * @return: java.time.LocalDate
     */
    public static LocalDateTime plusEndTime(LocalDateTime localDateTime, long count) {
        return DeleteTaskUtil.getEndTime(localDateTime.plusDays(count));
    }

    /***
     * @description: 解析返回的数据获取
     * @param dataArray es数据
     * @param date      最早的日期
     * @return: java.util.List<java.lang.String>
     */
    public static List<String> getSerialnumberList(JSONArray dataArray, String date) {
        List<String> list = new ArrayList<>(dataArray.size());
        String serialnumber;
        for (int i = 0; i < dataArray.size(); i++) {
            if (DeleteTaskUtil.isSameDay(dataArray.getJSONObject(i), date)) {
                serialnumber = DeleteTaskUtil.getSerialnumber(dataArray.getJSONObject(i));
                if (!list.contains(serialnumber)) {
                    list.add(serialnumber);
                }
            } else {
                break;
            }
        }
        return list;
    }

    /***
     * @description: 获取任务号
     * @param jsonObject 对象
     * @return: java.lang.String
     */
    private static String getSerialnumber(JSONObject jsonObject) {
        return jsonObject.getString("Serialnumber");
    }

    /***
     * @description: 判断是否是同一天
     * @param day 时间
     * @param jsonObject 对象
     * @return: java.lang.String
     */
    private static boolean isSameDay(JSONObject jsonObject, String day) {
        String createTime = getCreateTime(jsonObject);
        return Optional.ofNullable(createTime).map(p -> p.contains(day)).orElse(false);
    }

    /***
     * @description: 获取UUID
     * @return: java.lang.String
     */
    public static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /***
     * @description: 获取临时删除任务列表
     * @param serialnumber 子任务号
     * @param userSerialnumber 主任务号
     * @param analyType 分析类型
     * @param ymd 时间
     * @param taskType 任务类型
     * @param createTime 创建时间
     * @param optSource 操作类型
     * @param slaveList 分析节点列表
     * @return: java.util.List<com.keensense.task.entity.TaskCleanInterim>
     */
    public static List<TaskCleanInterim> getTaskCleanList(String serialnumber, String userSerialnumber, String analyType, String ymd,
                                            Integer taskType, Timestamp createTime, Integer optSource, List<String> slaveList){
        if(slaveList.isEmpty()){
            return Collections.emptyList();
        }
        List<TaskCleanInterim> list = new ArrayList<>(slaveList.size());
        slaveList.forEach(slave -> list.add(new TaskCleanInterim(serialnumber, userSerialnumber, analyType, ymd,
                taskType, createTime, optSource, slave)));
        return list;
    }

}
