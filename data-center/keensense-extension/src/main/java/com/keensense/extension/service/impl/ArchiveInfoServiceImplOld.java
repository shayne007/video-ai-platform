//package com.keensense.extension.service.impl;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.keensense.impl.ServiceImpl;
//import com.keensense.common.exception.VideoException;
//import com.keensense.extension.constants.ArchivesConstant;
//import com.keensense.extension.constants.LibraryConstant;
//import com.keensense.extension.entity.ArchivesBodyInfo;
//import com.keensense.extension.entity.ArchivesInfo;
//import com.keensense.extension.entity.ArchivesTrailFree;
//import com.keensense.extension.entity.dto.ArchivesDTO;
//import com.keensense.extension.entity.dto.TrailConditionDTO;
//import com.keensense.extension.entity.dto.TrailDTO;
//import com.keensense.extension.feign.IMicroSearchFeign;
//import com.keensense.extension.mapper.ArchivesInfoMapper;
//import com.keensense.extension.keensense.IArchivesBodyInfoService;
//import com.keensense.extension.keensense.IArchivesInfoService;
//import com.keensense.extension.keensense.IArchivesTrailFreeService;
//import com.keensense.extension.util.IDUtil;
//import com.keensense.extension.util.JsonPatterUtil;
//import com.keensense.sdk.algorithm.impl.GLQstFaceSdkInvokeImpl;
//import com.keensense.sdk.algorithm.impl.GlstFaceSdkInvokeImpl;
//import com.keensense.sdk.algorithm.impl.KsFaceSdkInvokeImpl;
//import com.keensense.sdk.algorithm.impl.QstFaceSdkInvokeImpl;
//import com.keensense.sdk.algorithm.impl.StFaceSdkInvokeImpl;
//import com.keensense.sdk.algorithm.impl.StQstFaceSdkInvokeImpl;
//import com.keensense.sdk.constants.BodyConstant;
//import com.keensense.sdk.constants.CommonConst;
//import com.keensense.sdk.constants.FaceConstant;
//import com.keensense.sdk.constants.SdkExceptionConst;
//import com.keensense.sdk.sys.utils.DbPropUtil;
//import com.keensense.sdk.util.ValidUtil;
//import com.loocme.sys.datastruct.Var;
//import com.loocme.sys.util.PatternUtil;
//import com.loocme.sys.util.StringUtil;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang.StringUtils;
//import org.apache.logging.log4j.util.Strings;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
///***
// * @description:
// * @author jingege
// * @return:
// */
//@Service
//@Slf4j
//public class ArchiveInfoServiceImplOld extends ServiceImpl<ArchivesInfoMapper,ArchivesInfo> implements
//	IArchivesInfoService {
//
//	@Autowired
//	private IArchivesBodyInfoService archivesBodyInfoService;
//	@Autowired
//	private IArchivesTrailFreeService archivesTrailFreeService;
//	@Autowired
//	private IMicroSearchFeign microSearchFeign;
//
//	private String archiveFaceLib = StringUtils.EMPTY;
//	private String archiveFrontBodyLib = StringUtils.EMPTY;
//	private String archiveSideBodyLib = StringUtils.EMPTY;
//	private String archiveBackBodyLib = StringUtils.EMPTY;
//
//	private static final String FEATURE_VECTOR = "featureVector";
//	private static final String FACE_FEATURE_ID = "face_feature_id";
//	private static final String BODY_ARCHIVESDTO_QUALITY_THRESHOID = "body.archives.quality.threshold";
//
//	private void initLib(){
//		archiveFaceLib = LibraryConstant.getFaceLibraryCache().getId();
//		archiveFrontBodyLib = LibraryConstant.getBodyLibraryByAngle(LibraryConstant.BOYD_ANGLE_FRONT, new Date()).getId();
//		archiveSideBodyLib = LibraryConstant.getBodyLibraryByAngle(LibraryConstant.BOYD_ANGLE_SIDE, new Date()).getId();
//		archiveBackBodyLib = LibraryConstant.getBodyLibraryByAngle(LibraryConstant.BOYD_ANGLE_BACK, new Date()).getId();
//	}
//	/**
//	 * 保存轨迹
//	 * @param archivesDTO archivesDTO
//	 */
//	@Override
//	public void saveObjext(ArchivesDTO archivesDTO) throws VideoException{
//		initLib();
//		String faceFeature = archivesDTO.getFaceFeature();
//		String bodyUrl = archivesDTO.getBodyImgUrl();
//		Float faceQuality = archivesDTO.getFaceQuality();
//		Float bodyQuality = archivesDTO.getBodyQuality();
//		saveTrailToResult(archivesDTO, Strings.EMPTY, ArchivesConstant.TRACE_SOURCE_EXCEPTION,
//			faceQuality, ArchivesConstant.FACE_SCORE_DEFAULT, ArchivesConstant.BODY_SCORE_DEFAULT);
//		if (StringUtils.isNotBlank(faceFeature)){
//			havingFaceUrl(archivesDTO);
//			/*人脸不可用，判断人形质量,人形质量达标，用人形比对，关联轨迹*/
//		}else if(StringUtils.isNotBlank(bodyUrl) && bodyQuality != null && bodyQuality >= DbPropUtil
//			.getFloat(BODY_ARCHIVESDTO_QUALITY_THRESHOID, 0.7f)) {
//			log.info("face no body ok");
//			useBodyToTrail(archivesDTO);
//		}
//		archivesDTO.setBodyQuality(formatFloat(archivesDTO.getBodyQuality()));
//	}
//
//	private void havingFaceUrl(ArchivesDTO archivesDTO){
//		String bodyUrl = archivesDTO.getBodyImgUrl();
//		Float faceQuality = archivesDTO.getFaceQuality();
//		Float bodyQuality = archivesDTO.getBodyQuality();
//		/*有人脸时*/
//		if (faceQuality >= DbPropUtil.getFloat("face.archives.quality.threshold", 0.7f)){
//			log.info("face ok");
//			/*人脸质量达标*/
//			Var similars = FaceConstant.getFaceSdkInvoke().getSimilars(archiveFaceLib,archivesDTO.getFaceFeature(),
//				DbPropUtil.getFloat("face.archives.compare.threshold",0.75f) * 100,1);
//			Float score = similars.isNull()? ArchivesConstant.FACE_SCORE_DEFAULT: similars.getFloat("[0].score");
//			archivesDTO.setFaceScore(score);
//			/*判断是否有与档案比对上的，若有，则根据人脸比对成功，添加至轨迹*/
//			String faceId = similars.isNull()? "": similars.getString("[0].face.id");
//			if (StringUtil.isNotNull(faceId))
//			{
//				addTrailByArchives(faceId,faceQuality,score,bodyQuality,archivesDTO);
//			}else{
//				/*人脸没有匹配上*/
//				addArchives(archivesDTO.getPitch(),archivesDTO.getRoll(),faceQuality,bodyQuality,archivesDTO.getFaceFeature(),archivesDTO);
//			}
//		}else if (StringUtils.isNotBlank(bodyUrl) && bodyQuality != null && bodyQuality >= DbPropUtil
//			.getFloat(BODY_ARCHIVESDTO_QUALITY_THRESHOID, 0.7f)){
//			log.info("face no body ok");
//			useBodyToTrail(archivesDTO);
//		}
//		log.info("face no body no");
//	}
//
//	private void addTrailByArchives(String faceId,Float faceQuality,Float score,Float bodyQuality,ArchivesDTO archivesDTO){
//		ArchivesInfo archivesInfo = baseMapper.selectOne(new QueryWrapper<ArchivesInfo>()
//			.isNotNull(FACE_FEATURE_ID).eq(FACE_FEATURE_ID,faceId));
//		/*添加分数、轨迹等信息*/
//		if (archivesInfo != null)
//		{
//			log.info("face ok exits");
//			String archivesId = archivesInfo.getId();
//			saveTrailToResult(archivesDTO, archivesId,ArchivesConstant.TRACE_SOURCE_FACE,
//				faceQuality,score,ArchivesConstant.BODY_SCORE_DEFAULT);
//			/*判断这个角度的人形是否绑定，未绑定则新增*/
//			ArchivesBodyInfo archiveBodyInfo = archivesBodyInfoService.getOne(
//				new QueryWrapper <ArchivesBodyInfo>().eq("archives_id",archivesId)
//					.eq("angle",archivesDTO.getAngle()));
//			if (archiveBodyInfo == null && bodyQuality!=null && bodyQuality >= DbPropUtil.getFloat(BODY_ARCHIVESDTO_QUALITY_THRESHOID, 0.7f)){
//				log.info("face ok exits body add");
//				addBodyInfoToArchive(archivesDTO.getAngle(), archivesDTO.getBodyImgUrl(), archivesInfo.getId());
//				archivesDTO.setBodyScore(ArchivesConstant.BODY_SCORE_COMPARE_DEFAULT);
//			}
//			log.info("face ok exits body noadd");
//		}else {
//			addArchives(archivesDTO.getPitch(),archivesDTO.getRoll(),faceQuality,bodyQuality,archivesDTO.getFaceFeature(),archivesDTO);
//		}
//	}
//	private void addArchives(float pitch,float roll,Float faceQuality,Float bodyQuality,
//		String featureVector,ArchivesDTO archivesDTO){
//		/*人脸没有匹配上*/
//		boolean isSuit = isSuitAlgo(pitch,roll,faceQuality);
//		if (isSuit) {
//			/*人脸角度正，可用于建档案，并新增人形*/
//			log.info("face ok body add");
//			String archivesId = IDUtil.uuid();
//			saveTrailToResult(archivesDTO, archivesId,ArchivesConstant.TRACE_SOURCE_FACE,
//				faceQuality,ArchivesConstant.FACE_SCORE_COMPARE_DEFAULT,ArchivesConstant.BODY_SCORE_DEFAULT);
//			String faceFeatureId = FaceConstant.getFaceSdkInvoke().addFaceToLib(archiveFaceLib,
//				featureVector,archivesDTO.getFaceImgUrl());
//			ArchivesInfo archivesInfo = new ArchivesInfo(
//				archivesId, archivesDTO.getFaceImgUrl(), faceFeatureId, archivesDTO.getObjType(),
//				archivesId,ArchivesConstant.ARCHIVES_FACE_FRONT);
//			baseMapper.insert(archivesInfo);
//			if(bodyQuality!=null && bodyQuality >= DbPropUtil.getFloat(BODY_ARCHIVESDTO_QUALITY_THRESHOID, 0.7f)) {
//
//				addBodyInfoToArchive(archivesDTO.getAngle(), archivesDTO.getBodyImgUrl(), archivesId);
//				archivesDTO.setBodyScore(ArchivesConstant.BODY_SCORE_COMPARE_DEFAULT);
//			}
//			log.info("pitch & roll" +pitch+"//"+roll+"//"+ archivesDTO.getFaceID()+"//");
//		}else if (StringUtils.isNotBlank(archivesDTO.getBodyImgUrl()) && bodyQuality != null && bodyQuality >= DbPropUtil
//			.getFloat(BODY_ARCHIVESDTO_QUALITY_THRESHOID,0.7f)){
//			log.info("face half ok body ok");
//			useBodyToTrail(archivesDTO);
//		}
//	}
//
//	/***
//	 * @description: 是否为可建档的人脸大角度
//	 * @param pitch
//	 * @param roll
//	 * @param faceQuality
//	 * @return: boolean
//	 */
//	private boolean isSuitAlgo(float pitch,float roll,Float faceQuality){
//		/*人脸没有匹配上*/
//		boolean qstAlgo = FaceConstant.getFaceSdkInvoke() instanceof QstFaceSdkInvokeImpl && faceQuality>=0.8;
//		boolean glstAlgo = FaceConstant.getFaceSdkInvoke() instanceof GlstFaceSdkInvokeImpl && faceQuality>=0.8;
//		boolean stAlgo = FaceConstant.getFaceSdkInvoke() instanceof StFaceSdkInvokeImpl && faceQuality>=0.8;
//		boolean ksAlgo = FaceConstant.getFaceSdkInvoke() instanceof KsFaceSdkInvokeImpl && faceQuality>=0.8
//			&& -0.26 < pitch && pitch < 0.26 && -0.26 < roll&& roll < 0.26;
//		boolean glstQstAlgo = FaceConstant.getFaceSdkInvoke() instanceof GLQstFaceSdkInvokeImpl && faceQuality>=0.8
//			&& -10 < pitch && pitch < 10 && -15 < roll&& roll < 15;
//		boolean stQstAlgo = FaceConstant.getFaceSdkInvoke() instanceof StQstFaceSdkInvokeImpl && faceQuality>=0.8
//			&& -0.26 < pitch && pitch < 0.26 && -0.26 < roll&& roll < 0.26;
//		if (ksAlgo || qstAlgo || glstAlgo ||glstQstAlgo ||stAlgo ||stQstAlgo) {
//			return true;
//		}else{
//			//人脸为大角度，且质量合格
//			glstQstAlgo = FaceConstant.getFaceSdkInvoke() instanceof GLQstFaceSdkInvokeImpl && faceQuality>=0.8
//				&&((-10 < pitch && pitch < 10 && -15 < roll&& roll < 15) ||(-10 < pitch && pitch < 10 && -15 < roll&& roll < 15));
//		}
//		return false;
//
//	}
//
//	/**
//	 * 给人形底库录入档案人形，并加入档案人形表
//	 * @param  angle angle
//	 * @param bodyUrl bodyUrl
//	 * @param archivesId archivesId
//	 */
//	private void addBodyInfoToArchive(Integer angle,String bodyUrl,String archivesId){
//
//		if(angle != null&&StringUtils.isNotBlank(bodyUrl)){
//			Var bodyFeatureInfo = BodyConstant.getBodySdkInvoke().getPicAnalyzeOne(BodyConstant.BODY_TYPE, bodyUrl);
//			if(bodyFeatureInfo!=null){
//
//				String bodyFeatureId = IDUtil.uuid();
//				String repoId = findBodyLibByAngle(angle);
//				if(StringUtils.isBlank(repoId)){
//					log.info("angle get repoid is null");
//					throw new VideoException(-1,"angle="+angle+" get repoid is null");
//				}
//				BodyConstant.getBodySdkInvoke().addBodyToLib(findBodyLibByAngle(angle), bodyFeatureId, BodyConstant.BODY_TYPE,
//					bodyFeatureInfo.getString(FEATURE_VECTOR));
//				ArchivesBodyInfo bodyInfo = new ArchivesBodyInfo(IDUtil.uuid(), bodyUrl, String.valueOf(angle),
//					bodyFeatureId, archivesId);
//				archivesBodyInfoService.saveOrUpdate(bodyInfo);
//			}
//
//		}
//	}
//
//	/**
//	 * 人形质量达标，用人形比对，关联轨迹
//	 * @param archivesDTO archivesDTO
//	 */
//	private void useBodyToTrail(ArchivesDTO archivesDTO) {
//		log.info("useBodyToTrail");
//		Var bodyInfoVar = BodyConstant.getBodySdkInvoke().getPicAnalyzeOne(BodyConstant.BODY_TYPE,
//			archivesDTO.getBodyImgUrl());
//		if(null != bodyInfoVar) {
//
//			// 人形比对质量达标
//			Var similars = BodyConstant.getBodySdkInvoke().getSimilars(BodyConstant.BODY_TYPE,
//				findBodyLibByAngle(archivesDTO.getAngle()),
//				bodyInfoVar.getString(FEATURE_VECTOR),
//				DbPropUtil.getFloat("body.archives.compare.threshold", 0.8f)
//					* 100,1, false);
//
//			Float score = similars.isNull() ? ArchivesConstant.BODY_SCORE_DEFAULT
//				: similars.getFloat("[0].score");
//			archivesDTO.setBodyScore(score);
//
//			String bodyId = similars.isNull() ? "": similars.getString("[0].uuid");
//			if (StringUtil.isNotNull(bodyId) &&
//				score-DbPropUtil.getFloat("body.archives.compare.threshold", 0.8f)>=0){
//				ArchivesBodyInfo archiveBodyInfo = archivesBodyInfoService.getOne(
//					new QueryWrapper<ArchivesBodyInfo>().eq("body_feature_id",bodyId));
//				String archiveId = archiveBodyInfo.getArchivesId();
//				if (StringUtil.isNotNull(archiveId)){
//					saveTrailToResult(archivesDTO, archiveId,ArchivesConstant.TRACE_SOURCE_BODY,
//						archivesDTO.getFaceQuality(),ArchivesConstant.FACE_SCORE_DEFAULT,score);
//				}else{
//					// 需要添加默认值
//					log.info("gb1400data donnot find archive by bodyid=" + bodyId);
//					//游离id，录入聚类中
//					addFreeTrailToDB(archivesDTO);
//				}
//			}
//		}
//	}
//
//	/***
//	 * @description: 新增人脸/人形的游离数据，其中人脸/人形仅添加一次
//	 * @param archivesDTO archivesDTO
//	 * @return: void
//	 */
//	private void addFreeTrailToDB(ArchivesDTO archivesDTO){
//		//判定是否有人脸、人形id，录入聚类中
//		String esId = StringUtils.EMPTY;
//		int objType = CommonConst.OBJ_TYPE_HUMAN;
//		if(StringUtils.isNotBlank(archivesDTO.getFaceID())){
//			esId = archivesDTO.getFaceID();
//		}else if(StringUtils.isNotBlank(archivesDTO.getBodyID())){
//			esId = archivesDTO.getBodyID();
//			objType = CommonConst.OBJ_TYPE_FACE;
//		}
//		if(StringUtils.isNotBlank(esId)){
//			ArchivesTrailFree archivesTrailFree = new ArchivesTrailFree(esId,objType);
//			archivesTrailFreeService.saveOrUpdate(archivesTrailFree);
//		}
//	}
//
//	/**
//	 * 根据角度选取底库 128-正面 256-侧面 512-背面
//	 * @param angle angle
//	 */
//	private String findBodyLibByAngle(Integer angle){
//
//		String rstLib = Strings.EMPTY;
//		switch (angle){
//			case LibraryConstant.BOYD_ANGLE_FRONT:
//				rstLib = archiveFrontBodyLib;
//				break;
//			case LibraryConstant.BOYD_ANGLE_SIDE:
//				rstLib = archiveSideBodyLib;
//				break;
//			case LibraryConstant.BOYD_ANGLE_BACK:
//				rstLib = archiveBackBodyLib;
//				break;
//			default:
//				break;
//		}
//		return rstLib;
//	}
//
//	/**
//	 * @description: 保存轨迹对象
//	 * @param archivesDTO archivesDTO
//	 * @param archivesId archivesId
//	 * @param trailSource trailSource
//	 * @param faceQuality faceQuality
//	 * @param faceScore faceScore
//	 * @param bodyScore bodyScore
//	 * @return: void
//	 */
//	private void saveTrailToResult(ArchivesDTO archivesDTO, String archivesId,
//		Integer trailSource, Float faceQuality, Float faceScore, Float bodyScore){
//		archivesDTO.setArchivesID(archivesId);
//		archivesDTO.setTrailSource(trailSource);
//		archivesDTO.setFaceQuality(formatFloat(faceQuality));
//		archivesDTO.setFaceScore(formatFloat(faceScore));
//		archivesDTO.setBodyScore(formatFloat(bodyScore));
//		if(archivesDTO.getRightBtmY()!=null&&archivesDTO.getLeftTopY()!=null&&
//			archivesDTO.getRightBtmX()!=null&&archivesDTO.getLeftTopX()!=null){
//			float denominator = (float)archivesDTO.getRightBtmX()-archivesDTO.getLeftTopX();
//			float molecule = (float)archivesDTO.getRightBtmY()-archivesDTO.getLeftTopY();
//			if(Float.floatToRawIntBits(denominator) !=0){
//				archivesDTO.setProportion( formatFloat(molecule*1.000f/denominator));
//			}
//		}else{
//			archivesDTO.setProportion(0f);
//		}
//	}
//
//	private Float formatFloat(Float num){
//		DecimalFormat df = new DecimalFormat("#.0000");
//		if(num!=null){
//			num = Float.parseFloat(df.format(num));
//		}else{
//			num = 0.0000f;
//		}
//		return num;
//	}
//
//	@Override
//	public String getArchivesIdInfos(TrailConditionDTO trailConditionDTO) throws VideoException {
//		StringBuilder archivesIds = new StringBuilder();
//		String baseData = trailConditionDTO.getBaseData();
//		Float thresholdStr = trailConditionDTO.getThreshold();
//		Integer maxArchivesNum = trailConditionDTO.getMaxArchivesNum();
//
//		if (StringUtil.isNotNull(baseData) && !ValidUtil.validImageSize(baseData, 3)) {
//			throw new VideoException(SdkExceptionConst.FAIL_CODE, "picture base64>3m");
//		}
//		Var faceInfoVar = FaceConstant.getFaceSdkInvoke().getPicAnalyzeOne(baseData);
//		String faceFeature = faceInfoVar.getString(FEATURE_VECTOR);
//
//		if (StringUtils.isEmpty(faceFeature)) {
//			throw new VideoException(SdkExceptionConst.FAIL_CODE, "donot have feature");
//		}
//
//		Var similars = FaceConstant.getFaceSdkInvoke()
//			.getSimilars(LibraryConstant.getFaceLibraryCache().getId(),
//				faceFeature, thresholdStr * 100, maxArchivesNum);
//		if (!similars.isNull()) {
//			String[] featureIds = ArchivesConstant.getSearchResultIds(similars);
//			if (featureIds != null && featureIds.length > 0) {
//				List<ArchivesInfo> archivesInfos = this.list(new QueryWrapper<ArchivesInfo>()
//					.in(FACE_FEATURE_ID, Arrays.asList(featureIds)).groupBy("id"));
//				for (ArchivesInfo archives : archivesInfos) {
//					archivesIds.append(",");
//					archivesIds.append(archives.getId());
//				}
//			}
//		}
//		if(StringUtils.isBlank(archivesIds.toString())){
//			return "";
//		}
//		return archivesIds.substring(1);
//	}
//
//
//	@Override
//	public List<TrailDTO> getTrailInfos(TrailConditionDTO trailConditionDTO) throws VideoException {
//		List<TrailDTO> trailDTOList = new ArrayList<>();
//		String baseData = trailConditionDTO.getBaseData();
//		String startTime = trailConditionDTO.getBeginTime();
//		String endTime = trailConditionDTO.getEndTime();
//		Float thresholdStr = trailConditionDTO.getThreshold();
//		Integer maxArchivesNum = trailConditionDTO.getMaxArchivesNum();
//
//		if(StringUtils.isNotBlank(startTime) && !PatternUtil.isMatch(startTime,JsonPatterUtil.DATE_PATTER)){
//			log.info("begin time is incompatible format --"+JsonPatterUtil.DATE_PATTER);
//			return trailDTOList;
//		}
//		if(StringUtils.isNotBlank(endTime) && !PatternUtil.isMatch(endTime,JsonPatterUtil.DATE_PATTER)){
//			log.info("endTime time is incompatible format --"+JsonPatterUtil.DATE_PATTER);
//			return trailDTOList;
//		}
//
//		if(StringUtil.isNotNull(baseData) && !ValidUtil.validImageSize(baseData, 3)){
//			log.info("picture base64>3m");
//			return trailDTOList;
//		}
//		Var faceInfoVar = FaceConstant.getFaceSdkInvoke().getPicAnalyzeOne(baseData);
//		String faceFeature = faceInfoVar.getString(FEATURE_VECTOR);
//
//		if(StringUtils.isEmpty(faceFeature)) {
//			log.info("donot have feature");
//			return trailDTOList;
//		}
//
//		Var similars = FaceConstant.getFaceSdkInvoke().getSimilars(LibraryConstant.getFaceLibraryCache().getId(),
//			faceFeature, thresholdStr*100, maxArchivesNum);
//		if(similars.isNull()){
//			log.info("face to archivesLib similars is null");
//			return trailDTOList;
//		}
//		String[] featureIds = ArchivesConstant.getSearchResultIds(similars);
//		if(featureIds==null|| featureIds.length<=0) {
//			log.info("donot have suitable archives ");
//			return trailDTOList;
//		}
//		List<ArchivesInfo> archivesInfos = this.list(new QueryWrapper<ArchivesInfo>()
//			.in(FACE_FEATURE_ID, Arrays.asList(featureIds)).groupBy("id"));
//		if(CollectionUtils.isEmpty(archivesInfos)){
//			log.info("donnot have suitable archives by face_featureid --"+Arrays.asList(featureIds));
//			return trailDTOList;
//		}
//		return getGbToTrail(trailConditionDTO,archivesInfos);
//	}
//
//	private List<TrailDTO> getGbToTrail(TrailConditionDTO trailConditionDTO,List<ArchivesInfo> archivesInfos){
//		List<TrailDTO> trailDTOList = new ArrayList<>();
//		List<TrailDTO> faceTrailList = getFaceToTrail(getGbUrlCondition("Faces",trailConditionDTO,archivesInfos));
//		List<TrailDTO> bodyTrailList = getBodyToTrail(getGbUrlCondition("Persons",trailConditionDTO,archivesInfos));
//		for(TrailDTO faceTrail:faceTrailList){
//			Iterator<TrailDTO> it = bodyTrailList.iterator();
//			while(it.hasNext()){
//				TrailDTO bodyTrail = it.next();
//				if(faceTrail.getConnectObjectId().equals(bodyTrail.getPersonID())){
//					faceTrail.setBodyScore(bodyTrail.getBodyScore());
//					faceTrail.setBodyQuality(bodyTrail.getBodyQuality());
//					faceTrail.setBodyImgUrl(bodyTrail.getBodyImgUrl());
//					faceTrail.setAngle(bodyTrail.getAngle());
//					it.remove();
//				}
//			}
//			trailDTOList.add(faceTrail);
//		}
//		if(CollectionUtils.isNotEmpty(bodyTrailList)){
//			trailDTOList.addAll(bodyTrailList);
//		}
//		return trailDTOList;
//	}
//
//	private Map<String, Object> getGbUrlCondition(String type, TrailConditionDTO trailConditionDTO,List<ArchivesInfo> archivesInfos){
//		StringBuilder archivesSbr = new StringBuilder();
//		Map<String, Object> map = new HashMap<>();
//		for (ArchivesInfo archives : archivesInfos) {
//			archivesSbr.append(",");
//			archivesSbr.append(archives.getId());
//		}
//		map.put(type+".ArchivesID.In",archivesSbr.substring(1));
//		map.put(type+".MarkTime.Order","desc");
//		map.put(type+".PageRecordNum",3000);
//		map.put(type+".RecordStartNo",1);
//		if (StringUtils.isNotBlank(trailConditionDTO.getMonitorIDList())) {
//			map.put(type+".DeviceID.In",trailConditionDTO.getMonitorIDList());
//		}
//		if (StringUtils.isNotBlank(trailConditionDTO.getBeginTime())) {
//			map.put(type+".MarkTime.Gte",trailConditionDTO.getBeginTime());
//		}
//		if (StringUtils.isNotBlank(trailConditionDTO.getEndTime())) {
//			map.put(type+".MarkTime.Lte",trailConditionDTO.getEndTime());
//		}
//		if (StringUtils.isNotBlank(trailConditionDTO.getTrailSource())) {
//			map.put(type+".TrailSource.In",trailConditionDTO.getTrailSource());
//		}
//		return map;
//	}
//
//	private List<TrailDTO> getFaceToTrail(Map<String, Object> paramSbr) {
//		List<TrailDTO> trailDTOList = new ArrayList<>();
//		int count = getFaceToTrail(trailDTOList,paramSbr);
//		if(count>3000){
//			for(int i=1;i<(count/3000)+1;i++){
//				paramSbr.put("Faces.PageRecordNum",3000);
//				paramSbr.put("Faces.RecordStartNo",i+1);
//				getFaceToTrail(trailDTOList,paramSbr);
//			}
//		}
//		return trailDTOList;
//	}
//
//	private Integer getFaceToTrail(List<TrailDTO> trailDTOList, Map<String, Object> paramSbr) {
//		String respContent = microSearchFeign.getFaces(paramSbr);
//		JSONArray faceJsonArray =  JSONObject.parseObject(respContent).getJSONObject("FaceListObject").getJSONArray("FaceObject");
//		int count = 0;
//		if(!faceJsonArray.isEmpty()) {
//			count = JSONObject.parseObject(respContent).getJSONObject("FaceListObject").getInteger("Count");
//			for (int i = 0; i < faceJsonArray.size(); i++) {
//				JSONObject jsonObject = faceJsonArray.getJSONObject(i);
//				TrailDTO trailDTO = new TrailDTO();
//				trailDTO.initFaceJSON(jsonObject);
//				trailDTOList.add(trailDTO);
//			}
//		}
//		return count;
//	}
//
//	private List<TrailDTO> getBodyToTrail(Map<String, Object> paramSbr) {
//		List<TrailDTO> trailDTOList = new ArrayList<>();
//		int count = getBodyToTrail(trailDTOList,paramSbr);
//		if(count>3000){
//			for(int i=1;i<(count/3000)+1;i++){
//				paramSbr.put("Persons.PageRecordNum",3000);
//				paramSbr.put("Persons.RecordStartNo",i+1);
//				getBodyToTrail(trailDTOList,paramSbr);
//			}
//		}
//		return trailDTOList;
//	}
//
//	private Integer getBodyToTrail(List<TrailDTO> trailDTOList, Map<String, Object> paramSbr) {
//		String respContent = microSearchFeign.getBodys(paramSbr);
//		JSONArray bodyJsonArray =  JSONObject.parseObject(respContent).getJSONObject("PersonListObject").getJSONArray("PersonObject");
//		int count = 0;
//		if(!bodyJsonArray.isEmpty()) {
//			count = JSONObject.parseObject(respContent).getJSONObject("PersonListObject").getInteger("Count");
//			for (int i = 0; i < bodyJsonArray.size(); i++) {
//				JSONObject jsonObject = bodyJsonArray.getJSONObject(i);
//				TrailDTO trailDTO = new TrailDTO();
//				trailDTO.initBodyJSON(jsonObject);
//				trailDTOList.add(trailDTO);
//			}
//		}
//		return count;
//	}
//
//}
