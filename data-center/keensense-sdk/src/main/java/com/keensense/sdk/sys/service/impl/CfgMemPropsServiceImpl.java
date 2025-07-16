package com.keensense.sdk.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.sdk.sys.entity.CfgMemProps;
import com.keensense.sdk.sys.mapper.CfgMemPropsMapper;
import com.keensense.sdk.sys.service.ICfgMemPropsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 属性配置表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2019-05-09
 */
@Service
public class CfgMemPropsServiceImpl extends ServiceImpl<CfgMemPropsMapper, CfgMemProps> implements
    ICfgMemPropsService {

}
