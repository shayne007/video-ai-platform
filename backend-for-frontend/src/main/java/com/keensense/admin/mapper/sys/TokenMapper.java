package com.keensense.admin.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keensense.admin.entity.sys.TokenEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Token
 *
 * @author zengyc
 */
@Mapper
public interface TokenMapper extends BaseMapper<TokenEntity> {
	
}
