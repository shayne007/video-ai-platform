package com.keensense.admin.mapper.lawcase;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keensense.admin.entity.lawcase.TbCaseArchiveVideo;
import com.keensense.admin.vo.CaseArchiveVideoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 20:11:51
 */
@Mapper
public interface TbCaseArchiveVideoMapper extends BaseMapper<TbCaseArchiveVideo> {

    /**
     * 根据案件编号查询归档数据列表。用于界面展示
     * @param caseCode
     */
    List<CaseArchiveVideoVo> getCaseArchiveInfoVideoList(String caseCode);

    /**
     * 判断数据是否已经归档
     * @param caseArchiveVideo
     * @return
     */
    TbCaseArchiveVideo selectExistsArchiveVideo(TbCaseArchiveVideo caseArchiveVideo);
}
