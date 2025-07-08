package com.keensense.admin.constants;

/**
 * 第三方接口常量
 * @author admin
 *
 */
public class InterfaceConstants
{
    /**
     * 实时视频快照获取接口
     */
    public static final String GET_REAL_CAMERA_SNAPSHOT = "getrealcamerasnapshot.php";
    
    
    /**
     * 添加转码任务接口
     */
    public static final String ADD_TRANSCODE_TASK = "addtranscodetask.php";

    /**
     * 删除转码文件接口
     */
    public static final String DELETE_TRANSCODE_TASK = "deletetranscodetask.php";
    
    
    
    /**
     * 查询转码任务进度接口
     */
    public static final String QUERY_TRANSCODE_STATUS = "querytranscodestatus.php";
    
    
    /**
     * 查询视频类型接口获取可以转码的类型
     */
    public static final String GET_TRANSCODE_VIDEO_TYPE = "gettranscodevideotype.php";
    
    /**
     * 远程在slave执行命令接口
     */
    public static final String EXEC_SLAVE_COMMAND_SERVICE = "slaveSystemSet.php";


    /**
     * 获取转码服务器ftp磁盘空间
     */
    public static final String GET_DISK_SPACE = "getDiskSpace.php";
    
}
