package com.keensense.dataconvert.biz.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.keensense.dataconvert.biz.entity.VlprResult;

import java.util.List;
import java.util.Map;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.service
 * @Description： <p> VlprResultService  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/23 - 16:11
 * @Modify By：
 * @ModifyTime： 2019/7/23
 * @Modify marker：
 */
public interface VlprResultService {

    /**
     * selectListByYmd
     * @param ymd
     * @return
     */
    List<VlprResult> selectListByYmd(String ymd);

    /**
     * 分页查询数据 - 滚动查询出所有数据
     * @param ymd
     * @param page
     * @return
     */
    PageInfo<VlprResult> selectListByPage(String ymd, Page<VlprResult> page);


    /**
     * 更新表里面 SerialNumber 之间的关系
     * @param paramsMap
     */
    void updateSerialNumberByMap(Map paramsMap);
}
