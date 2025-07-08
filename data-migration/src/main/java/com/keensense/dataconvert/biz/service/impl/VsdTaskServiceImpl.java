package com.keensense.dataconvert.biz.service.impl;

import com.keensense.dataconvert.biz.dao.VsdTaskMapper;
import com.keensense.dataconvert.biz.entity.VsdTask;
import com.keensense.dataconvert.biz.service.VsdTaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.service.impl
 * @Description： <p> TODO </p>
 * @Author： - Jason
 * @CreatTime：2019/8/15 - 16:25
 * @Modify By：
 * @ModifyTime： 2019/8/15
 * @Modify marker：
 */
@Service
public class VsdTaskServiceImpl implements VsdTaskService {


    @Resource
    private VsdTaskMapper vsdTaskMapper;

    @Override
    public List<VsdTask> selectAllList() {
        return vsdTaskMapper.selectAllList();
    }
}
