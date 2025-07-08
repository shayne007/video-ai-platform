package com.keensense.admin.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class FTPResultBo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6347953699369625767L;

	/**
	 * 上传后文件ftp地址
	 */
	private String fileUrl;
	/**
	 * 原始文件名
	 */
	private String fileName;
	/**
	 * 返回消息
	 */
	private String msg;

	/**
	 * 文件大小
	 */
	private String fileSize;

	/**
	 * 上传后文件名
	 */
	private String uploadedName;

	/**
	 * 是否上传成功
	 */
	private boolean uploadSuccess;

	/**
	 * //文件表Id
	 */
	private Long fileId;

}
