package com.keensense.admin.dto;

import com.keensense.admin.base.BaseBo;
import com.keensense.admin.entity.task.CtrlUnitFile;
import lombok.Data;

@Data
public class FileBo extends BaseBo {

	private static final long serialVersionUID = 1667921700311759247L;
	
	/**
	 * 0 返回成功 -1 系统出错，文件上传失败  1 文件上传ftp服务器失败  2 文件转码失败 3 文件格式不正确 4 磁盘空间不足
	 */
	private String retFlag = "0";
	private String fileName;
	/**
	 * 小图地址
	 */
	private String fileUrl;
	private String fileFtpPath;
	private String fileSize;
	/**
	 * 大图地址
	 */
	private String bigPicUrl;
	/**
	 * 图片ID
	 */
	private String imgId;

	/**
	 * 图中目标的宽度
	 */
	private Integer imgWidth;

	/**
	 * 图中目标的高度
	 */
	private Integer imgHeight;

	private CtrlUnitFile ctrlUnitFile;
}
