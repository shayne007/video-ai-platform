package com.keensense.extension.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.keensense.extension.entity.ArchivesBodyInfo;
import com.keensense.extension.entity.CameraRelationInfo;
import com.keensense.extension.feign.IMicroSearchFeign;
import com.keensense.extension.mapper.ArchivesBodyInfoMapper;
import com.keensense.extension.service.IArchivesBodyInfoService;
import com.keensense.extension.util.ResponseUtil;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/***
 * @description:
 * @author jingege
 * @return:
 */
@Service
public class ArchivesBodyServiceImpl extends ServiceImpl<ArchivesBodyInfoMapper,ArchivesBodyInfo> implements
	IArchivesBodyInfoService {
	
}
