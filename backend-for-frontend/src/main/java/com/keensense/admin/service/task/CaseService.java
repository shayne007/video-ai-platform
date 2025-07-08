package com.keensense.admin.service.task;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.admin.entity.lawcase.AppPoliceComprehensive;
import com.keensense.admin.entity.lawcase.CaseCameraMedia;
import com.keensense.admin.vo.CaseCameraMediaVo;
import com.keensense.admin.vo.PoliceComprehensiveVo;

import java.util.List;
import java.util.Map;

public interface CaseService {
	void addCase(AppPoliceComprehensive police);

    void closeCase(String lawcaseId);

	void deleteCase(String lawcaseId);

	void updateCase(AppPoliceComprehensive police);

	List<CaseCameraMediaVo> queryCurrentTaskCameras(String id) ;

    List<CaseCameraMediaVo> selectCluesByCaseId(String lawcaseId);

	Page<PoliceComprehensiveVo> selectCaseByPage(Page<PoliceComprehensiveVo> pages, Map<String, Object> params);

	List<CaseCameraMediaVo> selectByList(String lawcaseid);

	String addPersistPicture(String pictureData);

	Integer selectExistsImage(CaseCameraMedia media);

	String getPictureStr(String url);

	void deleteClue(String clueId);
}
