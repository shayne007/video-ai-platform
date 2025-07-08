package com.keensense.admin.constants;

public class VideoTaskConstant {
    private VideoTaskConstant() {
    }

    /**
     * 1：联网实时；2：离线；3：抓拍机；4：IPC;5:联网录像；6：卡口
     * */
    public static class TASK_TYPE {
        //所有任务类型
        public static final long ALL = 0;

        //联网实时
        public static final long REAL = 1;

        //离线视频
        public static final long OFFLINE = 2;

        //抓拍机
        public static final long CAPTURE = 3;

        //IPC
        public static final long IPC = 4;

        //联网录像
        public static final long ONLINE_VIDEO = 5;

        //卡口
        public static final Integer GATE = 6;
    }

    /**
     * 任务来源
     */
    public static class FROM_TYPE {

        /**
         * 实时和抓拍机
         */
        public static int REAL = 1;//联网实时,IPC,抓拍机
        /**
         * 离线
         */
        public static int OFFLINE = 2;//离线,联网录像



        public static int CAPTURE = 3;//抓拍机

        public static int IPC = 4;//IPC

        /**
         * 联网录像下载
         */
        public static int ONLINE_VIDEO = 5;//联网录像下载
    }

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
     * 任务类型
     */
    public static class Type {
        /**
         * objext类型
         */
        public static final String OBJEXT = "objext";

        /**
         * 浓缩类型
         */
        public static final String SUMMARY = "summary";

        /**
         * 抓拍机图片类型
         */
        public static final String PICTURE = "picture";

    }

    /**
     * 任务的userId字段枚举
     */
    public static class USER_ID {
        /**
         * 五种任务类型
         */
        public static final String FIVE_TASK_TYPE = "('u2s_online_video','2','u2s_realtime_video','u2s_ipc'," +
                "'u2s_capture')";

        /**
         * 离线视频分析
         */
        public static final String OFFLINE_VIDEO = "u2s_offline_video";

        /**
         * 联网录像分析
         */
        public static final String ONLINE_VIDEO = "u2s_online_video";//联网录像分析

        /**
         * 实时视频分析
         */
        public static final String REALTIME_VIDEO = "u2s_realtime_video";//实时视频分析

        /**
         * IPC直连
         */
        public static final String IPC = "u2s_ipc"; //IPC直连

        /**
         * 抓拍机分析
         */
        public static final String CAPTURE = "u2s_capture"; //抓拍机分析

        /**
         * 接力追踪
         */
        public static final String JLZZ = "u2s_jlzz"; //接力追踪
        /**
         * 卡口
         */
        public static final String GATE = "u2s_gate";
    }


}
