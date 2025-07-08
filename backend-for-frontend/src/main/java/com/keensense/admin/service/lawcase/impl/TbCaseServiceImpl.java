package com.keensense.admin.service.lawcase.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.lawcase.TbCase;
import com.keensense.admin.enums.CaseJdStatus;
import com.keensense.admin.mapper.lawcase.TbCaseMapper;
import com.keensense.admin.service.lawcase.ITbCaseService;
import com.keensense.admin.service.sys.ISysUserService;
import com.keensense.admin.vo.TbCaseVo;
import com.loocme.sys.constance.DateFormatConst;
import com.loocme.sys.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("tbCaseService")
public class TbCaseServiceImpl extends ServiceImpl<TbCaseMapper, TbCase> implements ITbCaseService {

    @Autowired
    private ISysUserService userService;

    @Override
    public boolean saveCaseData() {
        //后续
        return false;
    }

    public String getLatestUpdateTime() {
        Timestamp latestUpdateTime = baseMapper.getLatestUpdateTime();
        if (latestUpdateTime != null) {
            return DateUtil.getFormat(latestUpdateTime, DateFormatConst.YMDHMS_);
        }
        return null;
    }

    @Override
    public Page<TbCaseVo> selectCaseByPage(Page<TbCaseVo> pages, Map<String, Object> params) {
        List<TbCaseVo> tbCases = baseMapper.selectTbCaseByPage(pages, params);
        for (TbCaseVo tbCase : tbCases) {
            String statusDesc = CaseJdStatus.getDescByValue(tbCase.getCaseStatus());
            tbCase.setCaseStatusStr(statusDesc);
        }
        pages.setRecords(tbCases);
        return pages;
    }
}
