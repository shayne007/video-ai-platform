package com.keensense.task.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
/**
 * druid启动加载项
 * @description:
 * @author: luowei
 * @createDate:2019年5月20日 上午11:40:38
 * @company:
 */
@Configuration
public class DruidConfig {
	@Autowired
	ConfigYml configYml;
	
	@Bean(name = "wallFilter")
    @DependsOn("wallConfig")
    public WallFilter wallFilter(WallConfig wallConfig){
        WallFilter wallFilter = new WallFilter();
        wallFilter.setConfig(wallConfig);
        return wallFilter;
    }


    @Bean(name = "wallConfig")
	public WallConfig wallConfig() {
		WallConfig wallConfig = new WallConfig();
		// 允许一次执行多条语句
		wallConfig.setMultiStatementAllow(Boolean.valueOf(configYml.getMultiStatementAllow()));
		// 允许一次执行多条语句
		wallConfig.setNoneBaseStatementAllow(Boolean.valueOf(configYml.getNoneBaseStatementAllow()));
		return wallConfig;
	}
    
//    @Bean
//	public ServletRegistrationBean<StatViewServlet> druidStatViewServlet() {
//		ServletRegistrationBean<StatViewServlet> registrationBean = new ServletRegistrationBean<>(new StatViewServlet(),  "/druid/*");
//		// IP白名单 (没有配置或者为空，则允许所有访问)
//		registrationBean.addInitParameter("allow", "");
//		// IP黑名单 (存在共同时，deny优先于allow)
//		registrationBean.addInitParameter("deny", "");
//		registrationBean.addInitParameter("loginUsername", configYml.getLoginUsername());
//		registrationBean.addInitParameter("loginPassword", configYml.getLoginPassword());
//		registrationBean.addInitParameter("resetEnable", "false");
//		return registrationBean;
//	}
}
