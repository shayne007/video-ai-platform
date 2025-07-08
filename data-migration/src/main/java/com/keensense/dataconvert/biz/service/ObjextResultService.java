package com.keensense.dataconvert.biz.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.keensense.dataconvert.biz.entity.ObjextResult;

import java.util.List;
import java.util.Map;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.service
 * @Description： <p> ObjextResultService  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/23 - 15:22
 * @Modify By：
 * @ModifyTime： 2019/7/23
 * @Modify marker：
 */
public interface ObjextResultService {


    /**
     * selectListByYmd
     * @param ymd
     * @return
     */
    List<ObjextResult> selectListByYmd(String ymd);


    /**
     * 分页查询数据 - 滚动查询出所有数据
     * @param ymd
     * @param page
     * @return
     */
    PageInfo<ObjextResult> selectListByPage(String ymd, Page<ObjextResult> page);


    /**
     * 更新表里面 SerialNumber 之间的关系
     * @param paramsMap
     */
    void updateSerialNumberByMap(Map paramsMap);

}
