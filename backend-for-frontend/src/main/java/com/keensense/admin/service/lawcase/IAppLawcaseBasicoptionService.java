package com.keensense.admin.service.lawcase;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.admin.entity.lawcase.AppLawcaseBasicoption;

import java.util.List;

/**
 * code generator
 *
 * @author code generator
 * @date 2019-06-08 21:15:12
 */
public interface IAppLawcaseBasicoptionService extends IService<AppLawcaseBasicoption> {

     List<AppLawcaseBasicoption> queryAppLawcaseList(String typeCode);
}

