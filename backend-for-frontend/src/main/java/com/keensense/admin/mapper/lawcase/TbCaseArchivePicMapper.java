package com.keensense.admin.mapper.lawcase;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keensense.admin.entity.lawcase.TbCaseArchivePic;
import com.keensense.admin.vo.CaseArchivePicVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 20:11:51
 */
@Mapper
public interface TbCaseArchivePicMapper extends BaseMapper<TbCaseArchivePic> {

    /**
     * 查询案件管理详情页面图片展示数据
     * @param caseCode
     * @return
     */
    List<CaseArchivePicVo> selectCaseInfoArchivePicList(String caseCode);

    /**
     * 查询当前案件图片相关的监控点信息
     * @param caseCode
     * @return
     */
    List<CaseArchivePicVo> queryCurrentPicCamerasByCaseCode(String caseCode) ;

    Integer selectExistsArchivePic(TbCaseArchivePic caseArchivePic);
}
