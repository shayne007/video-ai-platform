package com.keensense.admin.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keensense.admin.entity.sys.TokenEntity;

/**
 * 用户Token
 *
 * @author zengyc
 */
public interface ITokenService extends IService<TokenEntity> {

	TokenEntity queryByToken(String token);

	TokenEntity queryByUserId(long userId);
	/**
	 * 生成token
	 * @param userId  用户ID
	 * @return        返回token信息
	 */
	TokenEntity createToken(long userId);

	/**
	 * 设置token过期
	 * @param userId 用户ID
	 */
	void expireToken(long userId);

}
