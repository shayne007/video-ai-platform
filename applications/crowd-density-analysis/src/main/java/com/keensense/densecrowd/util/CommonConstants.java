package com.keensense.densecrowd.util;

import com.keensense.common.exception.VideoException;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 常量定义
 *
 * @author ShiXt  2016年1月18日
 */
@Slf4j
public class CommonConstants {
    private CommonConstants() {
    }

    public static class CameraType {
        private CameraType() {
        }

        /**
         * Vas点位类型
         */
        public static final long VAS = 1L;
        /**
         * IPC点位类型
         */
        public static final long RTSP = 2L;
        /**
         * 抓拍相机类型
         */
        public static final long CAPTURE = 3L;
        /**
         * 卡口类型
         */
        public static final long Gate = 4L;
    }

    public static final String TOKEN = "token";

    public static final String LOCAL_IP = "127.0.0.1";

    public static final long VAS_FROM_TYPE = 1L;

    public static final long IPC_FROM_TYPE = 4L;//IPC

    public static final String FROM_TYPE_REAL = "1";

    public static final Integer INTERREST = 1;

    /**
     * 实时视频分析
     */
    public static final String REALTIME_VIDEO = "u2s_realtime_video";//实时视频分析

    /**
     * 实时视频分析
     */
    public static final String IPC_VIDEO = "u2s_ipc";//IPC分析

    /**
     * 任务状态
     */
    public static class STATUS {
        /**
         * 已启动
         */
        public static final String RUNNING = "1";

        /**
         * 未启动
         */
        public static final String FINISHED = "0";

        /**
         * 所有状态
         */
        public static final Short ALL = -1;
    }

    /**
     * 任务启动状态
     */
    public static class CameraStatus {
        private CameraStatus() {
        }

        /**
         * 监控点未启动状态
         */
        public static final long STOP = 0L;
        /**
         * 监控点启动状态
         */
        public static final long START = 1L;
    }


    /**
     * 模块长编码分割符号
     */
    public static final String MODULE_SPLIT = "!";

    /**
     * 实时视频快照获取接口
     */
    public static final String GET_REAL_CAMERA_SNAPSHOT = "getrealcamerasnapshot.php";

    /**
     * 获取转码服务器ftp磁盘空间
     */
    public static final String GET_DISK_SPACE = "getDiskSpace.php";

    /**
     * 长编码分割符号
     */
    public static final String SPLIT_CHAR = "!";

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
}
