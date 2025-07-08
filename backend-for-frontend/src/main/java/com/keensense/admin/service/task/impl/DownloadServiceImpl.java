package com.keensense.admin.service.task.impl;

import com.keensense.admin.service.task.DownloadService;
import com.keensense.admin.util.ExcelHandleUtils;
import com.keensense.admin.util.FTPUtils;
import com.keensense.admin.util.FileUtils;
import com.keensense.admin.util.FileZipCompressorUtil;
import com.keensense.admin.util.RandomUtils;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.vo.ResultQueryVo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DownloadServiceImpl implements DownloadService {
	@Override
	public String downloadTotalComprehensive(List<ResultQueryVo> resultList, String serialNum,Integer exportImgType) {
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
		// 人
		String personDir = resultFileDir + File.separator + "person";
		// 人骑车
		String bikeDir = resultFileDir + File.separator + "bike";
		// 车
		String carDir = resultFileDir + File.separator + "car";
		// 其他
		String othersDir = resultFileDir + File.separator + "others";
		//人脸图
		String faceDir = resultFileDir +  File.separator + "face";
		//创建人脸文件夹
		File faceFile = new File(faceDir);
		if(!faceFile.exists()){
			faceFile.mkdir();
		}
		// 创建人 文件夹
		File personFile = new File(personDir);
		if (!personFile.exists()) {
			personFile.mkdir();
		}
		// 创建人骑车文件夹
		File bikeFile = new File(bikeDir);
		if (!bikeFile.exists()) {
			bikeFile.mkdir();
		}
		// 创建车辆文件夹
		File carFile = new File(carDir);
		if (!carFile.exists()) {
			carFile.mkdir();
		}
		// 创建其他文件夹
		File othersFile = new File(othersDir);
		if (!othersFile.exists()) {
			othersFile.mkdir();
		}
		List<ResultQueryVo> personResultList = new ArrayList<ResultQueryVo>();
		List<ResultQueryVo> carResultList = new ArrayList<ResultQueryVo>();
		List<ResultQueryVo> bikeResultList = new ArrayList<ResultQueryVo>();
		List<ResultQueryVo> faceResultList = new ArrayList<ResultQueryVo>();
		List<ResultQueryVo> othersResultList = new ArrayList<ResultQueryVo>();
		int personNum = 0;
		int pfaceNum = 0;
		int faceNum = 0;
		int carNum = 0;
		int bikeNum = 0;
		int othersNum = 0;

		int personNumBig = 0;
		int faceNumBig = 0;
		int carNumBig = 0;
		int bikeNumBig = 0;
		if (null != resultList && resultList.size() > 0) {
			// 生成分类图片
			for (ResultQueryVo resultQueryVo : resultList) {
				String imageUrl = resultQueryVo.getImgurl();//小图url地址
				String bigImgurl = resultQueryVo.getBigImgurl();//大图url地址
				String timeStr = resultQueryVo.getCreatetimeStr().replace(" ", "_").replaceAll(":", "-");
                Short objtype = resultQueryVo.getObjtype();

                // 人
				if (1 == objtype) {
					String newPicName = "";
					if (exportImgType == 1){
						newPicName = downloadMinImage(imageUrl, personDir, timeStr, resultQueryVo, ++personNum);//小图下载
					}else if(exportImgType == 2){
						newPicName = downloadBigImage(bigImgurl,personDir,timeStr,resultQueryVo,++personNumBig);//大图下载
					}else if (exportImgType == 3){
                        newPicName = downloadMinImage(imageUrl, personDir, timeStr, resultQueryVo, ++personNum);//小图下载
                        downloadBigImage(bigImgurl,personDir,timeStr,resultQueryVo,++personNumBig);//大图下载
                    }
					String personRelativePicturePath = "person" + File.separator + newPicName;
					resultQueryVo.setPictureLocalPath(personRelativePicturePath);
					//人脸 人脸单独导出
					/*if (StringUtils.isNotEmptyString(resultQueryVo.getFaceUrl1())) {
						String newPicName1 = "face_"+ (++pfaceNum) + "_" + timeStr + "_" + resultQueryVo.getSerialnumber() + "_" + resultQueryVo.getId() + suffix1;
						String personPicturePath1 = faceDir + File.separator + newPicName1;
						FileUtils.dowloadFileFromUrl(resultQueryVo.getFaceUrl1(), personPicturePath1);
						String personRelativePicturePath1 = "face" + File.separator + newPicName1;
						resultQueryVo.setPictureLocalPath1(personRelativePicturePath1);
					}*/
					personResultList.add(resultQueryVo);
				}
				// 人脸
				else if (3 == objtype){
					String newPicName = "";
                    if (exportImgType == 1){
                        newPicName = downloadMinImage(imageUrl, faceDir, timeStr, resultQueryVo, ++faceNum);//小图下载
                    }else if(exportImgType == 2){
						newPicName = downloadBigImage(bigImgurl,faceDir,timeStr,resultQueryVo,++faceNumBig);//大图下载
                    }else if (exportImgType == 3){
                        newPicName = downloadMinImage(imageUrl, faceDir, timeStr, resultQueryVo, ++faceNum);//小图下载
                        downloadBigImage(bigImgurl,faceDir,timeStr,resultQueryVo,++faceNumBig);//大图下载
                    }
					String personRelativePicturePath1 = "face" + File.separator + newPicName;
					resultQueryVo.setPictureLocalPath1(personRelativePicturePath1);
					faceResultList.add(resultQueryVo);
				}
				// 车
				else if (2 == objtype) {
					String newPicName = "";
                    if (exportImgType == 1){
                        newPicName = downloadMinImage(imageUrl, carDir, timeStr, resultQueryVo, ++carNum);//小图下载
                    }else if(exportImgType == 2){
						newPicName = downloadBigImage(bigImgurl,carDir,timeStr,resultQueryVo,++carNumBig);//大图下载
                    }else if (exportImgType == 3){
                        newPicName = downloadMinImage(imageUrl, carDir, timeStr, resultQueryVo, ++carNum);//小图下载
                        downloadBigImage(bigImgurl,carDir,timeStr,resultQueryVo,++carNumBig);//大图下载
                    }
					String carRelativePicturePath = "car" + File.separator + newPicName;
					resultQueryVo.setPictureLocalPath(carRelativePicturePath);
                    //人脸 人脸单独导出
                    /*if (StringUtils.isNotEmptyString(resultQueryVo.getFaceUrl1())) {
                        String newPicName1 = "face_"+ (++pfaceNum) + "_" + timeStr + "_" + resultQueryVo.getSerialnumber() + "_" + resultQueryVo.getId() + suffix;
                        String personPicturePath1 = faceDir + File.separator + newPicName1;
                        FileUtils.dowloadFileFromUrl(resultQueryVo.getFaceUrl1(), personPicturePath1);
                        String personRelativePicturePath1 = "face" + File.separator + newPicName1;
                        resultQueryVo.setPictureLocalPath1(personRelativePicturePath1);
                    }*/
					carResultList.add(resultQueryVo);
				}
				// 人骑车
				else if (4 == objtype) {
					String newPicName = "";
                    if (exportImgType == 1){
                    	newPicName = downloadMinImage(imageUrl, bikeDir, timeStr, resultQueryVo, ++bikeNum);//小图下载
                    }else if(exportImgType == 2){
						newPicName = downloadBigImage(bigImgurl,bikeDir,timeStr,resultQueryVo,++bikeNumBig);//大图下载
                    }else if (exportImgType == 3){
                        newPicName = downloadMinImage(imageUrl, bikeDir, timeStr, resultQueryVo, ++bikeNum);//小图下载
                        downloadBigImage(bigImgurl,bikeDir,timeStr,resultQueryVo,++bikeNumBig);//大图下载
                    }
					String bikeRelativePicturePath = "bike" + File.separator + newPicName;
					resultQueryVo.setPictureLocalPath(bikeRelativePicturePath);
					bikeResultList.add(resultQueryVo);
				} else if (0 == objtype || -1 == objtype) {
					// 下载图片文件到本地
					String fileOriginalName = null;
					String[] picturUrlArray = imageUrl.split("/");
					if (null != picturUrlArray && picturUrlArray.length > 0) {
						fileOriginalName = picturUrlArray[picturUrlArray.length - 1];
					}
					String suffix = ".jpg";
					if (StringUtils.isNotEmptyString(fileOriginalName) && fileOriginalName.contains(".")) {
						suffix = fileOriginalName.substring(fileOriginalName.lastIndexOf('.'), fileOriginalName.length());
					}

					String newPicName = "others_"+ (++othersNum) + "_"  + timeStr + "_" + resultQueryVo.getSerialnumber() + "_" + resultQueryVo.getId() + suffix;
					String bikePicturePath = othersDir + File.separator + newPicName;
					FileUtils.dowloadFileFromUrl(imageUrl, bikePicturePath);
					String othersRelativePicturePath = "others" + File.separator + newPicName;
					resultQueryVo.setPictureLocalPath(othersRelativePicturePath);
					othersResultList.add(resultQueryVo);
				}
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
			String[] personHeaders = { "类型", "监控点", "经过时间", "上衣颜色", "下衣颜色", "性别", "年龄段", "是否背包",
					"姿态", "体态", "手持刀棍", "眼镜", "打伞", "拎东西", "拉杆箱", "手推车", "上衣款式", "下衣款式", "是否戴口罩",
					"是否戴帽子", "发型", "上衣纹理", "下衣纹理", "抱小孩", "民族", "目标图"};
			String[] carHeaders = { "类型", "监控点", "经过时间", "车牌号码", "车牌颜色","车牌类型", "品牌", "车系",
					"车身颜色", "车型", "年检标", "纸巾盒", "遮阳板", "挂件", "天线", "危险品车", "主驾驶安全带",
					"副驾驶安全带", "挂牌", "打电话", "姿态", "天窗", "行李架", "摆件", "年款", "撞损车", "车辆图片"};
			String[] bikeHeaders = { "类型", "监控点", "经过时间", "类别", "上衣颜色", "车辆类型", "是否戴头盔", "头盔颜色",
					"性别", "年龄段", "挂牌", "车篷", "姿态", "眼镜", "上衣款式", "戴口罩", "是否背包", "上衣纹理", "载客",
					"车灯形状", "目标图"};
			String[] faceHeaders = { "类型", "监控点", "经过时间", "目标图" };
			String[] othersHeaders = { "类型", "监控点", "经过时间", "目标图" };
			HSSFWorkbook workbook = new HSSFWorkbook();
			ExcelHandleUtils.exportPersonExcel(workbook, 0, "人", personHeaders,personResultList, out);
			ExcelHandleUtils.exportVlprExcel(workbook, 1, "车", carHeaders,carResultList, out);
			ExcelHandleUtils.exportBikeExcel(workbook, 2, "人骑车", bikeHeaders,bikeResultList, out);
			ExcelHandleUtils.exportFaceExcel(workbook, 3, "人脸", faceHeaders, faceResultList, out);
			ExcelHandleUtils.exportOthersExcel(workbook, 4, "其他",othersHeaders, othersResultList, out);
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

	//下载小图
	private String downloadMinImage(String imageUrl,String dir,String timeStr,ResultQueryVo resultQueryVo,int minNum){
		String fileOriginalName = null;
		String[] picturUrlArray = imageUrl.split("/");
		if (null != picturUrlArray && picturUrlArray.length > 0) {
			fileOriginalName = picturUrlArray[picturUrlArray.length - 1];
		}
		String suffix = ".jpg";
		if (StringUtils.isNotEmptyString(fileOriginalName) && fileOriginalName.contains(".")) {
			suffix = fileOriginalName.substring(fileOriginalName.lastIndexOf('.'), fileOriginalName.length());
		}
		Short objtype = resultQueryVo.getObjtype();
		String name = "";
		if (objtype == 1){
			name = "person_";
		}else if (objtype == 3){
			name = "face_";
		}else if (objtype == 2){
			name = "vlpr_";
		}else if (objtype == 4){
			name = "bike_";
		}else {
			name = "other_";
		}
		String newPicName = minNum + "_" + name + timeStr + "_" + resultQueryVo.getSerialnumber() + "_" + resultQueryVo.getId() + suffix;
		String picturePath = dir + File.separator + newPicName;
		FileUtils.dowloadFileFromUrl(imageUrl, picturePath);
		return newPicName;
	}

	//下载大图
	private String downloadBigImage(String bigImageUrl,String dir,String timeStr,ResultQueryVo resultQueryVo,int bigNum){
		String fileOriginalName = null;
		String[] picturUrlArray = bigImageUrl.split("/");
		if (null != picturUrlArray && picturUrlArray.length > 0) {
			fileOriginalName = picturUrlArray[picturUrlArray.length - 1];
		}
		String suffix = ".jpg";
		if (StringUtils.isNotEmptyString(fileOriginalName) && fileOriginalName.contains(".")) {
			suffix = fileOriginalName.substring(fileOriginalName.lastIndexOf('.'), fileOriginalName.length());
		}
		Short objtype = resultQueryVo.getObjtype();
		String name = "";
		if (objtype == 1){
			name = "big_person_";
		}else if (objtype == 3){
			name = "big_face_";
		}else if (objtype == 2){
			name = "big_vlpr_";
		}else if (objtype == 4){
			name = "big_bike_";
		}else {
			name = "big_other_";
		}
		String newPicName =  bigNum + "_" + name + timeStr + "_" + resultQueryVo.getSerialnumber() + "_" + resultQueryVo.getId() + suffix;
		String picturePath = dir + File.separator + newPicName;
		FileUtils.dowloadFileFromUrl(bigImageUrl, picturePath);
		return newPicName;
	}


}
