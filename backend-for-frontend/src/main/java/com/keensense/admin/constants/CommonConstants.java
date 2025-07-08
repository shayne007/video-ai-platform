package com.keensense.admin.constants;

import com.keensense.admin.util.PropertiesUtil;
import io.swagger.models.auth.In;
import scala.Int;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量定义
 *
 * @author ShiXt  2016年1月18日
 */
public class CommonConstants {
    private CommonConstants() {
    }

    public static final String LOCAL_IP = "127.0.0.1";
    public static final String TOKEN = "token";

    /**
     * 超级测试管理
     */
    public static final String SUPER_ADMIN_TEST = "admintest";

    /**
     * 超级管理员ID
     */
    public static final String SUPER_ADMIN = "admin";


    /**
     * 新增系统错误信息
     */
    public static final String SYSTEM_FAILURE = "系统错误";


    /**
     * 新增失败信息
     */
    public static final String CREATE_FAILURE = "新增失败";

    /**
     * 删除失败信息
     */
    public static final String DELETE_FAILURE = "删除失败";

    /**
     * 更新失败信息
     */
    public static final String UPDATE_FAILURE = "更新失败";

    /**
     * 查询失败信息
     */
    public static final String QUERY_FAILURE = "查询失败";

    /**
     * 模块长编码分割符号
     */
    public static final String MODULE_SPLIT = "!";
    /**
     * 长编码分割符号
     */
    public static final String SPLIT_CHAR = "!";

    /**
     * 基本特征类型
     */
    public static final String BASIC_FEATURES_TYPE = "BASIC";
    /**
     * 二级切割符
     */
    public static final String SECOND_SPLIT_CHAR = ",";
    /**
     * 人特征类型
     */
    public static final String PERSON_FEATURES_TYPE = "PERSON";
    /**
     * 车特征类型
     */
    public static final String CAR_FEATURES_TYPE = "CAR";
    /**
     * 物特征类型
     */
    public static final String ITEM_FEATURES_TYPE = "ITEM";
    /**
     * 更新操作关键字
     */
    public static final String UPDATE_METHOD = "UPDATE";
    /**
     * 删除操作关键字
     */
    public static final String DELETE_METHOD = "DELETE";

    /**
     * 上传文件类型 1： 图片
     */
    public static final String FILE_TYPE_IMAGE = "1";
    /**
     * 上传文件类型 2：视频
     */
    public static final String FILE_TYPE_VEDIO = "2";

    /**
     * 人员表来源类型 0：结构化页面录入
     */
    public static final String PERSON_TYPE_INPUT = "0";
    /**
     * 人员表来源类型 1：结构化页面人员库导入
     */
    public static final String PERSON_TYPE_IMPORT = "1";

    /**
     * 特征类型 T： 文本
     */
    public static final String FEATURE_TYPE_INPUT = "T";
    /**
     * 特征类型 S：选择
     */
    public static final String FEATURE_TYPE_SELECT = "S";
    /**
     * 卷宗创建类型(案件碰撞)
     */
    public static final String ARCHIVE_CREATE_AJPZ = "AJPZ";
    /**
     * 卷宗创建类型(个案研判)
     */
    public static final String ARCHIVE_CREATE_GAYP = "GAYP";
    /**
     * 卷宗创建类型(类案研判)
     */
    public static final String ARCHIVE_CREATE_LAYP = "LAYP";

    /**
     * 合成作战 流程类别  FF 分发 XZ 协作'
     */
    public static final String COMBINPROCESS_TYPE_FF = "FF";
    public static final String COMBINPROCESS_TYPE_XZ = "XZ";

    public static final String DEPT_TYPE = "DEPT";
    public static final String USER_TYPE = "USER";
    public static final String DIRECT_TYPE = "DIRECT";
    public static final String TRAN_TYPE = "DIRECT";

    public static final String YES_STATUS = "Y";
    public static final String NO_STATUS = "N";

    /**
     * 单机版设置访问地址
     */

    public static String SERVICE_DEPLOY_ADDRESS = PropertiesUtil.getParameterKey("service.deploy.address");


    private final static Map<String, String> FILEFORMAT = new HashMap<>();

    static {
        FILEFORMAT.put("MPG", "01");
        FILEFORMAT.put("MOV", "02");
        FILEFORMAT.put("AVI", "03");
        FILEFORMAT.put("RM", "04");
        FILEFORMAT.put("RMVB", "05");
        FILEFORMAT.put("FLV", "06");
        FILEFORMAT.put("VOB", "07");
        FILEFORMAT.put("M2TS", "08");
        FILEFORMAT.put("MP4", "09");
        FILEFORMAT.put("ES", "10");
        FILEFORMAT.put("PS", "11");
    }

    //接力追踪 目标库类型  1代表结果图片 2代表轨迹图片
    public static final Integer JLZZ_TRACKTYPE_RESULT = 1;
    public static final Integer JLZZ_TRACKTYPE_TRACK = 2;

    /**
     * 类型常量
     */
    public static class ObjextTypeConstants {

        /**
         * 人
         */
        public static final String OBJEXT_TYPE_PERSON = "1";

        /**
         * 车
         */
        public static final String OBJEXT_TYPE_CAR = "2";

        /**
         * 人脸
         */
        public static final String OBJEXT_TYPE_FACE = "3";

        /**
         * 人骑车
         */
        public static final String OBJEXT_TYPE_CAR_PERSON = "4";

        /**
         * 其他
         */
        public static final String OBJEXT_TYPE_OTHER = "-1";

    }

    public interface REGEX {
        //案件地点名称及离线视频名称长度
        public static final Integer CASE_LOCATION_NAME_LENGTH = 128;
        //案件描述长度
        public static final Integer CASE_DESC_LENGTH = 500;
        //监控点名称长度
        public static final Integer CAMERA_CASE_LENGTH = 32;
        //电话长度
        public static final Integer TEL_LENGTH = 13;
        //不允许?/*
        public static final String CAMERA_CASE_RELINE_FILE_NAME_NOT_SUPPORT = "[^\\\\\\*\\?]+";
        //中文字母数字下划线
        public static final String MONITOR_NAME_SUPPORT = "^[a-z0-9A-Z\u4e00-\u9fa5_]{1,32}$";
        //中文数字字母空格点
        public static final String USER_NAME = "^[a-z0-9A-Z\u4e00-\u9fa5·\\s]{1,32}$";
        //中文或字母开头 中文字母数字空格点
        public static final String USER_REAL_NAME = "^[a-zA-Z\u4e00-\u9fa5][a-z0-9A-Z\u4e00-\u9fa5·\\s]{0,31}$";
        //电话
        public static final String USER_TEL = "^[0-9][0-9\\-]{0,12}$";
        //身份证
        public static final String USER_IDCARD = "^[0-9][0-9X]{0,17}$";
    }

    /**
     * 版本发布的标识
     */
    public static final String PROJECT_RELEASE = "project_release_tag";

}
