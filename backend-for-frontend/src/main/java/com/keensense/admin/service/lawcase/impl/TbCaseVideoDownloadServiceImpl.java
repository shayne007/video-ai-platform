package com.keensense.admin.service.lawcase.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.lawcase.TbCaseVideoDownload;
import com.keensense.admin.mapper.lawcase.TbCaseVideoDownloadMapper;
import com.keensense.admin.service.lawcase.ITbCaseVideoDownloadService;
import org.springframework.stereotype.Service;



@Service("tbCaseVideoDownloadService")
public class TbCaseVideoDownloadServiceImpl extends ServiceImpl<TbCaseVideoDownloadMapper, TbCaseVideoDownload> implements ITbCaseVideoDownloadService {
}
