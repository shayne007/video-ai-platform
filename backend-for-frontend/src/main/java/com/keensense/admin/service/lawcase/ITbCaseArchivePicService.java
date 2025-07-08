package com.keensense.admin.service.lawcase;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.admin.entity.lawcase.TbCaseArchivePic;
import com.keensense.admin.vo.CaseArchivePicVo;

import java.util.List;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 21:15:11
 */
public interface ITbCaseArchivePicService extends IService<TbCaseArchivePic> {

    /**
     * 根据案件编号查询归档数据列表。用于界面展示
     * @param caseCode
     * @return
     */
    List<CaseArchivePicVo> getCaseArchiveInfoPicList(String caseCode);

    /**
     * 查询监控点，用于轨迹展示
     * @param caseCode
     */
    List<CaseArchivePicVo> queryCurrentTaskCameras(String caseCode) ;

    /**
     * 判断图片数据是否已经归档
     */
    Integer selectExistsArchivePic(TbCaseArchivePic caseArchivePic);
}

