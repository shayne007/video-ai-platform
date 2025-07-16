package com.keensense.commonlib.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.common.exception.VideoException;
import com.keensense.commonlib.entity.CommonFeatureInfo;
import com.keensense.commonlib.entity.CommonFeatureRecord;
import com.keensense.commonlib.entity.dto.CommonFeatureQueryDto;
import com.keensense.commonlib.entity.dto.CommonLibFeatureDTO;
import com.keensense.commonlib.entity.dto.PageDto;

import java.util.Map;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/5/21 15:49
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public interface ICommonFeatureService extends IService<CommonFeatureInfo> {

    String addFeature(CommonLibFeatureDTO commonLibFeatureDTO,Integer type) throws VideoException;

    Map<String,String> deleteFeature(String libId,Integer type,String[] idArr) throws VideoException;


    IPage<CommonFeatureRecord> listCommonFeature(CommonFeatureQueryDto dto, PageDto page);
}
