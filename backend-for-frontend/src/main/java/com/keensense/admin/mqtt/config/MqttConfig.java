package com.keensense.admin.mqtt.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@RefreshScope
public class MqttConfig {

    /*public static final String HOST = "tcp://ShEastcoast.LeanLoop.net:8088";
	// 服务器内置主题，用来监测当前服务器上连接的客户端数量（$SYS/broker/clients/connected）
	public static final String TOPIC = "eastcoast/identification";
	private String userName = "donganuser";
	private String passWord = "da2018529";
	// 定义MQTT的ID，可以在MQTT服务配置中指定
	private static final String clientid = "server13";*/

    @Value("${mqtt.host}")
    private String host;

    @Value("${mqtt.username}")
    private String userName;

    @Value("${mqtt.password}")
    private String passWord;

    @Value("${mqtt.topic}")
    private String topic;

    @Value("${mqtt.clientid}")
    private String clientid;

    @Value("${mqtt.gbStandard}")
    private String gbStandard;



}
