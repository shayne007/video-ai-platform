package com.keensense.densecrowd.mapper.task;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keensense.densecrowd.entity.task.VsdTaskRelation;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

/**
 * @Author: shitao
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 10:53 2019/9/25
 * @Version v0.1
 */
@Mapper
public interface VsdTaskRelationMapper extends BaseMapper<VsdTaskRelation> {

    List<VsdTaskRelation> queryLatelyTask();
}
