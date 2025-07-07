package com.keensense.commonlib.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.common.exception.VideoException;
import com.keensense.commonlib.entity.CommonInfo;
import com.keensense.commonlib.entity.dto.CommonLibDTO;
import com.keensense.commonlib.entity.dto.CommonLibQueryDto;
import com.keensense.commonlib.entity.dto.CommonSearchResultDTO;
import com.keensense.commonlib.entity.dto.PageDto;
import com.keensense.commonlib.entity.vo.CommonLibVO;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/5/21 15:49
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public interface ICommonInfoService extends IService<CommonInfo> {

    String createLibrary(CommonLibDTO commonLibDTO) throws VideoException;

    Map<String,String> deleteLibrary(String[] idArr) throws VideoException;

    List<CommonSearchResultDTO> searchLibrary(CommonLibDTO commonLibDTO) throws VideoException;

    IPage<CommonLibVO> listLibrary(CommonLibQueryDto dto, PageDto page);

    boolean uploadToDevice(String repo, Integer type);
}
