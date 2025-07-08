package com.keensense.dataconvert.biz.service.impl;

import cn.hutool.core.lang.Assert;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.keensense.dataconvert.biz.dao.VlprResultMapper;
import com.keensense.dataconvert.biz.entity.VlprResult;
import com.keensense.dataconvert.biz.service.VlprResultService;
import com.keensense.dataconvert.framework.common.enums.ExceptionEnums;
import com.keensense.dataconvert.framework.common.exception.SqlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.service.impl
 * @Description： <p> VlprResultServiceImpl </p>
 * @Author： - Jason
 * @CreatTime：2019/7/23 - 16:11
 * @Modify By：
 * @ModifyTime： 2019/7/23
 * @Modify marker：
 */
@Service("vlprResultService")
public class VlprResultServiceImpl implements VlprResultService {

    private static final Logger logger = LoggerFactory.getLogger(VlprResultServiceImpl.class);


    @Resource
    private VlprResultMapper vlprResultMapper;


    @Override
    public List<VlprResult> selectListByYmd(String ymd) {
        Assert.notNull(ymd,"ymd不允许为空!");
        return vlprResultMapper.selectListByYmd(ymd);
    }


    @Override
    public PageInfo<VlprResult> selectListByPage(String ymd, Page<VlprResult> page) {
        Assert.notNull(page,"page分页信息不允许Null.");
        Assert.notNull(ymd,"分表数据不允许为Null.");
        try {
            PageHelper.startPage(page.getPageNum(), page.getPageSize());
            List<VlprResult> list = vlprResultMapper.selectListByYmd(ymd);
            return new PageInfo<>(list);
        } catch (Exception e) {
            logger.error("=== selectListByPageVlprResult:error:{} ===",e.getMessage());
            throw new SqlException(ExceptionEnums.MYSQL_SELECT_EXCEPTION,e);
        }
    }

    @Override
    public void updateSerialNumberByMap(Map paramsMap) {
        Assert.notNull(paramsMap,"更新Map不能为Null.");
        try {
            vlprResultMapper.updateSerialNumberByMap(paramsMap);
        } catch (Exception e) {
            logger.error("=== updateSerialNumberByMap:VlprResultService:error:{} ===",e.getMessage());
            throw new SqlException(ExceptionEnums.MYSQL_UPDATE_EXCEPTION,e);
        }
    }


}
