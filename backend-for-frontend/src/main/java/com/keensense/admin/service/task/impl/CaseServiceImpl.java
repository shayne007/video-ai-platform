package com.keensense.admin.service.task.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.admin.entity.lawcase.AppPoliceComprehensive;
import com.keensense.admin.entity.lawcase.CaseCameraMedia;
import com.keensense.admin.entity.task.PersistPicture;
import com.keensense.admin.mapper.lawcase.AppPoliceComprehensiveMapper;
import com.keensense.admin.mapper.lawcase.CaseCameraMediaMapper;
import com.keensense.admin.mapper.task.CameraMapper;
import com.keensense.admin.mapper.task.PersistPictureMapper;
import com.keensense.admin.service.task.CaseService;
import com.keensense.admin.util.EntityObjectConverter;
import com.keensense.admin.util.PropertiesUtil;
import com.keensense.admin.util.StringUtils;
import com.keensense.admin.vo.CameraVo;
import com.keensense.admin.vo.CaseCameraMediaVo;
import com.keensense.admin.vo.PoliceComprehensiveVo;
import com.keensense.admin.vo.ResultQueryVo;
import com.keensense.admin.enums.CaseCommonState;
import com.loocme.security.encrypt.Base64;
import com.loocme.sys.util.ArrayUtil;
import com.loocme.sys.util.PatternUtil;
import com.loocme.sys.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.net.www.protocol.ftp.FtpURLConnection;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CaseServiceImpl implements CaseService {
	@Resource
	private AppPoliceComprehensiveMapper policeComprehensiveMapper;
	@Resource
	private CaseCameraMediaMapper caseCameraMediaMapper;
	@Resource
	private CameraMapper cameraMapper;
	@Resource
	private PersistPictureMapper persistPictureMapper;

	@Override
	public void addCase(AppPoliceComprehensive police) {
		policeComprehensiveMapper.insert(police);
	}

	@Override
	public void updateCase(AppPoliceComprehensive police) {
		policeComprehensiveMapper.updateById(police);
	}

	@Override
	public void closeCase(String lawcaseId) {
		AppPoliceComprehensive police = policeComprehensiveMapper.selectById(lawcaseId);
		police.setCaseState(String.valueOf(CaseCommonState.FINISH.value));
		if(null != police){
			if(null != police.getEndTime()){
			}else{
				police.setEndTime(new Date()); //设置结案时间
			}
		}
		policeComprehensiveMapper.updateById(police);
	}

	@Override
	public void deleteCase(String lawcaseId) {
		policeComprehensiveMapper.deleteById(lawcaseId);
	}

	/**
	 * 根据案件id查询所有监控点
	 */
	@Override
	public List<CaseCameraMediaVo> queryCurrentTaskCameras(String id) {
		List<CaseCameraMediaVo> result = caseCameraMediaMapper.queryCurrentTaskCameras(id);
		String flag = PropertiesUtil.getParameterPackey("track.permit.active");
		if (StringUtils.isNotEmptyString(flag) && "true".equals(flag)) {
			List<CaseCameraMediaVo> temp = new ArrayList<CaseCameraMediaVo>();
			// 过滤监控点
			String permitMoniters = PropertiesUtil.getParameterPackey("track.permit.moniterids");
			if (CollectionUtils.isNotEmpty(result) && StringUtils.isNotEmptyString(permitMoniters)) {
				String[] ids = permitMoniters.split(",");
				List<String> list = Arrays.asList(ids);
				for (CaseCameraMediaVo obj : result) {
					String cameraId = String.valueOf(obj.getC6());
					for (String str : list) {
						if (str.equals(cameraId)) {
							temp.add(obj);
							break;
						}
					}
				}

			}
			return temp;
		}
		// 剔除相邻一致的监控点
		List<CaseCameraMediaVo> date = new ArrayList<CaseCameraMediaVo>();
		/*long cameraId = 0l;
		for (CaseCameraMediaVo objextTrack : result) {
			long current = Long.valueOf(objextTrack.getC6());// 当前监控点
			if (cameraId == current) {
				continue;
			}
			CameraVo camera = cameraMapper.selectByPrimaryKey(current);
			if (camera == null) {
				continue;
			}
			if(StringUtils.isEmptyString(camera.getLongitude()) || StringUtils.isEmptyString(camera.getLatitude())){
				continue;
			}
			objextTrack.setLongitude(camera.getLongitude());
			objextTrack.setLatitude(camera.getLatitude());
			cameraId = current;
			date.add(objextTrack);
		}*/
		for (CaseCameraMediaVo objextTrack : result) {
			String pictureBase64 = getPictureStr(objextTrack.getFileNameafterupload());
			objextTrack.setPictureBase64(pictureBase64);
			//判断图片是否被删除修改为通过url查询图片是否存在
			if (urlNotExist(objextTrack.getFilePathafterupload())) {
				//已删除
				objextTrack.setDeleted(1);
			}
			if(StringUtils.isEmptyString(objextTrack.getLongitude()) || StringUtils.isEmptyString(objextTrack.getLatitude())){
				continue;
			}
			date.add(objextTrack);
		}
		return date;
	}

	@Override
	public List<CaseCameraMediaVo> selectCluesByCaseId(String lawcaseId) {
		List<CaseCameraMedia> caseCameraMediaList = caseCameraMediaMapper.selectList(new QueryWrapper<CaseCameraMedia>().eq("lawcaseId",lawcaseId).orderByDesc("create_time"));
		List<CaseCameraMediaVo> clues = EntityObjectConverter.getList(caseCameraMediaList,CaseCameraMediaVo.class);
		for (CaseCameraMediaVo clue : clues) {
			String picInfo = clue.getPicInfo();
			if(StringUtils.isNotEmpty(picInfo)){
				clue.setResultQueryVo(JSON.parseObject(picInfo, ResultQueryVo.class));
			}
			if (urlNotExist(clue.getFilePathafterupload())) {
				clue.setDeleted(1);//已删除
			}
		}
		return clues;
	}


	@Override
	public Page<PoliceComprehensiveVo> selectCaseByPage(Page<PoliceComprehensiveVo> pages, Map<String, Object> params) {
		List<PoliceComprehensiveVo> records = policeComprehensiveMapper.selectCaseByPage(pages, params);
		pages.setRecords(records);
		return pages;
	}

	@Override
	public List<CaseCameraMediaVo> selectByList(String lawcaseid) {
		return caseCameraMediaMapper.selectByList(lawcaseid);
	}

	@Override
	public String addPersistPicture(String pictureData) {
		if (StringUtil.isNull(pictureData)) {
			return "";
		} else {
			if (PatternUtil.isMatch(pictureData, "^(http|ftp).*")) {
				pictureData = downloadImage(pictureData);
			}

			if (StringUtil.isNull(pictureData)) {
				return "";
			} else {
				String id = UUID.randomUUID().toString().replace("-", "");
				PersistPicture persistPicture = new PersistPicture();
				persistPicture.setId(id);
				persistPicture.setPictureData(pictureData);
				int successNumber = persistPictureMapper.insert(persistPicture);
				return successNumber >= 1 ? "persist_picture" + "-" + id : "";
			}
		}
	}

	private static String downloadImage(String path) {
		byte[] by = downloadImageBy(path);
		return by == null ? "" : new String(Base64.encode(by));
	}

	private static byte[] downloadImageBy(String path) {
		byte[] by = null;
		URLConnection conn = null;

		try {
			URL url = new URL(path);
			conn = url.openConnection();
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(10000);
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			InputStream inputStream = conn.getInputStream();
			if (inputStream != null) {
				by = input2byte(inputStream);
			}
		} catch (Exception var9) {
			var9.printStackTrace();
		} finally {
			if (conn != null) {
				if (conn instanceof HttpURLConnection) {
					HttpURLConnection conn1 = (HttpURLConnection)conn;
					conn1.disconnect();
				} else if (conn instanceof FtpURLConnection) {
					FtpURLConnection conn1 = (FtpURLConnection)conn;
					conn1.close();
				}
			}

		}

		return by;
	}

	private static final byte[] input2byte(InputStream inStream) {
		try {
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
			byte[] buff = new byte[100];
			boolean var3 = false;

			int rc;
			while((rc = inStream.read(buff, 0, 100)) > 0) {
				swapStream.write(buff, 0, rc);
			}

			byte[] in2b = swapStream.toByteArray();
			IOUtils.closeQuietly(swapStream);
			IOUtils.closeQuietly(inStream);
			return in2b;
		} catch (IOException var5) {
			var5.printStackTrace();
			return null;
		}
	}

	@Override
	public Integer selectExistsImage( CaseCameraMedia record){
		return  caseCameraMediaMapper.selectExistsImage(record);
	}

    @Override
    public String getPictureStr(String url) {
        if (StringUtil.isNull(url)) {
            return "";
        } else {
            String data = "";
            if (PatternUtil.isMatch(url, "^(http|ftp).*")) {
                data = downloadImage(url);
            } else {
                String[] matches = PatternUtil.getMatch(url, "^([0-9a-zA-Z-_]+)-([0-9A-Za-z]+)$");
                if (ArrayUtil.isNotNull(matches)) {
                    PersistPicture persistPicture = persistPictureMapper.selectById(matches[2]);
                    data = persistPicture.getPictureData();
                }
            }
            return data;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
	public void deleteClue(String clueId) {
		String[] clueIdStr = clueId.split(",");
		for (int i = 0; i < clueIdStr.length; i++) {
			caseCameraMediaMapper.deleteById(clueIdStr[i]);
		}
	}

	public boolean urlNotExist(String source) {
		try {
			URL url = new URL(source);
			URLConnection uc = url.openConnection();
			InputStream in = uc.getInputStream();
			if (in != null) {
				in.close();
			}
			return false;
		} catch (Exception e) {
			return true;
		}
	}
}
