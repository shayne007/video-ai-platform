package com.keensense.admin.constants;

import com.keensense.admin.util.PropertiesUtil;

/**
 * 上传数据到佳都常量类
 *
 * @author:dufy
 * @version:1.0.0
 * @date 2018/12/19
 */
public class UploadConstants {
    private UploadConstants() {
    }

    /**
     * 涉案图片上传类型
     */
    public static final String UPLOAD_IMAGE_TYPE = "upload_image";
    /**
     * 涉案视频上传类型
     */
    public static final String UPLOAD_VIDEO_TYPE = "upload_video";
    /**
     * 涉案视频点位信息上传类型
     */
    public static final String UPLOAD_VIDEO_POINT_TYPE = "upload_video_point";

    /**
     * 涉案图片上传地址
     */
    public static final String UPLOAD_IMAGE_URL = PropertiesUtil.getParameterKey("upload_image_url");
    /**
     * 涉案视频上传地址
     */
    public static final String UPLOAD_VIDEO_URL = PropertiesUtil.getParameterKey("upload_video_url");
    /**
     * 涉案视频点位信息上传地址
     */
    public static final String UPLOAD_VIDEO_POINT_URL = PropertiesUtil.getParameterKey("upload_video_point_url");

    /**
     * 成功码
     */
    public static final Long SUCCESSCODE = 0L;

    /**
     * 图片大图
     */
    public static final String PIC_BG_TYPE = "PIC_BG";

    /**
     * 图片小图
     */
    public static final String PIC_THUMB_TYPE = "PIC_THUMB";

    /**
     * 视频封面
     */
    public static final String VIDEO_COVER_TYPE = "VIDEO_COVER";

    /**
     * 批量下载类型-图片
     */
    public static final int DOWNLOAD_PIC = 1;

    /**
     * 批量下载类型-视频
     */
    public static final int DOWNLOAD_VIDEO = 2;

}
