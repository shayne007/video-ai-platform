package com.keensense.admin.mapper.lawcase;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keensense.admin.entity.lawcase.AppLawcaseBasicoption;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 20:11:51
 */
@Mapper
public interface AppLawcaseBasicoptionMapper extends BaseMapper<AppLawcaseBasicoption> {

    List<AppLawcaseBasicoption> queryAppLawcaseList(String typeCode);

}
