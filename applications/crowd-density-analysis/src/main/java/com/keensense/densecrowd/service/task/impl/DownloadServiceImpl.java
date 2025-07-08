package com.keensense.densecrowd.service.task.impl;

import com.keensense.common.platform.bo.video.CrowdDensity;
import com.keensense.common.util.RandomUtils;
import com.keensense.densecrowd.service.task.DownloadService;
import com.keensense.densecrowd.util.DateTimeUtils;
import com.keensense.densecrowd.util.ExcelHandleUtils;
import com.keensense.densecrowd.util.FTPUtils;
import com.keensense.densecrowd.util.FileUtils;
import com.keensense.densecrowd.util.FileZipCompressorUtil;
import com.keensense.densecrowd.util.StringUtils;
import com.loocme.sys.util.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.*;
import java.util.*;

@Service
public class DownloadServiceImpl implements DownloadService {
	@Override
	public String downloadTotalComprehensive(List<CrowdDensity> resultList, String serialNum) {
 		String rootDir = FTPUtils.getRootPath("/");
		File rootFiles = new File(rootDir);
		if (rootFiles.exists()) {
			
		} else {
			rootFiles.mkdirs();
		}
		// 结果文件夹路径
		String resultFileDir = null;
		if (StringUtils.isNotEmptyString(serialNum)) {
			resultFileDir = rootDir  +  "ObjextDownloadResult_" + serialNum;
		} else {
			resultFileDir = rootDir  + "ObjextDownloadResult"
					+ RandomUtils.get8RandomValiteCode(8);
		}
		File resultFile = new File(resultFileDir);
		if (!resultFile.exists()) {
			resultFile.mkdir();
		}
		// 文件夹路径
		String crowdDir = resultFileDir + File.separator + "crowd";
		//创建数据文件夹
		File crowdFile = new File(crowdDir);
		if(!crowdFile.exists()){
            crowdFile.mkdir();
		}
		List<CrowdDensity> crowdResultList = new ArrayList<CrowdDensity>();
		int crowdNum = 0;
		if (null != resultList && resultList.size() > 0) {
			// 生成分类图片
			for (CrowdDensity crowdDensity : resultList) {
				String imageUrl = crowdDensity.getPicUrl();
				String timeStr = crowdDensity.getCreateTime().replace(" ", "_").replaceAll(":", "-");

				// 下载图片文件到本地
				String fileOriginalName = null;
				String fileOriginalName1 = null;
				String[] picturUrlArray = imageUrl.split("/");
				if (null != picturUrlArray && picturUrlArray.length > 0) {
					fileOriginalName = picturUrlArray[picturUrlArray.length - 1];
				}
				String suffix = ".jpg";
				if (StringUtils.isNotEmptyString(fileOriginalName) && fileOriginalName.contains(".")) {
					suffix = fileOriginalName.substring(fileOriginalName.lastIndexOf('.'), fileOriginalName.length());
				}
				String newPicName = "crowd_"+ (++crowdNum) + "_" + timeStr + "_" + crowdDensity.getSerialnumber()
						+ "_" + crowdDensity.getId() + suffix;
				String personPicturePath = crowdDir + File.separator + newPicName;
				if (StringUtils.isNotEmptyString(imageUrl)) {
					FileUtils.dowloadFileFromUrl(imageUrl, personPicturePath);
					String personRelativePicturePath = "crowd" + File.separator + newPicName;
					crowdDensity.setPictureLocalPath(personRelativePicturePath);
				}
				crowdResultList.add(crowdDensity);
			}
		}
		
		OutputStream out = null;
		try {
			String excelFilenPath = resultFileDir + File.separator + "ExportResult.xls";
			File f = new File(excelFilenPath);
			// 创建文件
			if (!f.exists()) {
				try {
					f.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			out = new FileOutputStream(excelFilenPath);
			String[] personHeaders = { "监控点", "人数", "时间", "目标图"};
			HSSFWorkbook workbook = new HSSFWorkbook();
			ExcelHandleUtils.exportCrowdExcel(workbook, 0, "人群密度检索", personHeaders,crowdResultList, out);
			// 原理就是将所有的数据一起写入，然后再关闭输入流。
			workbook.write(out);
			// out.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (null != out) {
					out.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 压缩文件路径
		String zipFileDirName = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		// 生成的压缩文件名 rootDir.replace("\\target\\classes", "");
		zipFileDirName = rootDir + "DownloadResult_" + df.format(new Date()) + ".zip";
		File rarFile = new File(zipFileDirName);
		if (rarFile.exists()) {
			rarFile.delete();
		}
		// 生成压缩文件
		FileZipCompressorUtil fileZipUtil = new FileZipCompressorUtil(zipFileDirName);
		fileZipUtil.compressExe(resultFileDir);
		// 删除excel和源图片文件
		try {
			File f = new File(resultFileDir);
			FileUtils.deleteFileOrDirector(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zipFileDirName;
	}

}
