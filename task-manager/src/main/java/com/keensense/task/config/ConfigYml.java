package com.keensense.task.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class ConfigYml {
	@Value("${druid.wall.multiStatementAllow}")
	private String multiStatementAllow;
	
	@Value("${druid.wall.noneBaseStatementAllow}")
	private String noneBaseStatementAllow;
	
	@Value("${druid.loginUsername}")
	private String loginUsername;
	
	@Value("${druid.loginPassword}")
	private String loginPassword;
}
