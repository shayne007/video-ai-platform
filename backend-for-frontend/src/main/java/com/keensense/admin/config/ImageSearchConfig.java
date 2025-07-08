package com.keensense.admin.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @ClassName: ImageSearchConfig
 * @Description: ImageSearchConfig配置属性
 * @Author: fengsy
 * @CreateDate: 2019/11/15 14:06
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@ConfigurationProperties("imagesearch")
//@RefreshScope
@Data
@Component
public class ImageSearchConfig {
	/**
	 * 搜图返回数据量
	 **/
	private String resultLimit;
	/**
	 * 搜图返回数据相似度阈值
	 **/
	private String threshold;

	/**
	 * 搜图返回数据相似度阈值
	 **/
	private String thresholdPerson;
	/**
	 * 搜图返回数据相似度阈值
	 **/
	private String thresholdBike;
	/**
	 * 搜图返回数据相似度阈值
	 **/
	private String thresholdVehicle;

	/**
	 * 过滤开关
	 **/
	private Boolean enable;
	/**
	 * 过滤类型 1,2,4
	 **/
	private String filterType;
	/**
	 * 优化搜图，过滤属性项
	 **/
	private String optimizeItems;
	/**
	 * 不对前top位目标进行属性过滤
	 **/
	private String top;
	/**
	 * 颜色色系
	 **/
	private String[] colorGroups;
	/**
	 * 车辆类型分组
	 **/
	private String[] carTypeGroups;

	/**
	 * 佛山版本置顶开关
	 */
	private Boolean topgetMem;


}
