package com.keensense.admin.mqtt;

import com.keensense.admin.mqtt.config.MqttConfig;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Title:Server 这是发送消息的服务端 Description: 服务器向多个客户端推送主题，即不同客户端可向服务器订阅相同主题
 * 
 * @author rao
 */
public class ServerMQTT {

	private MqttConfig mqttConfig;

	// tcp://MQTT安装的服务器地址:MQTT定义的端口号
	/*public static final String HOST = "tcp://172.16.0.47:1883";
	// 服务器内置主题，用来监测当前服务器上连接的客户端数量（$SYS/broker/clients/connected）
	public static final String TOPIC = "mqtt";
	private String userName = "admin";
	private String passWord = "123456";
	// 定义MQTT的ID，可以在MQTT服务配置中指定
	private static final String clientid = "server13";//22006*/

	/*public static final String HOST = "tcp://ShEastcoast.LeanLoop.net:8088";
	// 服务器内置主题，用来监测当前服务器上连接的客户端数量（$SYS/broker/clients/connected）
	public static final String TOPIC = "eastcoast/identification";
	private String userName = "donganuser";
	private String passWord = "da2018529";
	// 定义MQTT的ID，可以在MQTT服务配置中指定
	private static final String clientid = "server13";*/

	private MqttClient client;
	private MqttTopic topic11;

	private MqttMessage message;

	/**
	 * 构造函数
	 * 
	 * @throws MqttException
	 */
	public ServerMQTT(MqttConfig mqtt) throws Exception {
		// MemoryPersistence设置clientid的保存形式，默认为以内存保存
		mqttConfig = mqtt;
		client = new MqttClient(mqttConfig.getHost(), mqttConfig.getClientid(), new MemoryPersistence());
		getOptions();
	}

	/**
	 * 用来连接服务器
	 */
	private MqttConnectOptions getOptions() throws Exception {
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(true);
		options.setUserName(mqttConfig.getUserName());
		options.setPassword(mqttConfig.getPassWord().toCharArray());
		// 设置超时时间
		options.setConnectionTimeout(10);
		// 设置会话心跳时间
		options.setKeepAliveInterval(10);
		options.isCleanSession();
		try {
			client.setCallback(new PushCallback());
			client.connect(options);

			topic11 = client.getTopic(mqttConfig.getTopic());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return options;
	}

	public void connect() throws Exception {
		// 防止重复创建MQTTClient实例
		if (client == null) {
			client = new MqttClient(mqttConfig.getHost(), mqttConfig.getClientid(), new MemoryPersistence());
			client.setCallback(new PushCallback());
		}
		MqttConnectOptions options = getOptions();
		// 判断拦截状态，这里注意一下，如果没有这个判断，是非常坑的
		if (!client.isConnected()) {
			client.connect(options);
			System.out.println("连接成功");
		} else {// 这里的逻辑是如果连接成功就重新连接
			client.disconnect();
			client.connect(getOptions());
			System.out.println("连接成功");
		}
	}

	/**
	 * 
	 * @param topic
	 * @param message
	 * @throws MqttPersistenceException
	 * @throws MqttException
	 */
	public void publish(MqttTopic topic, MqttMessage message) throws MqttPersistenceException, MqttException {
		MqttDeliveryToken token = topic.publish(message);
		token.waitForCompletion();
		System.out.println("message is published completely! " + token.isComplete());
	}

	/**
	 * 启动入口
	 * 
	 * @throws MqttException
	 */
	/*public static void main(String[] args) throws Exception {
		ServerMQTT server = new ServerMQTT();
		setmessage(server, "ttttttttt");
	}

	public static void doit() throws Exception {
		ServerMQTT server = new ServerMQTT();
		setmessage(server, "ttttttttt");

	}*/

	public static void setmessage(ServerMQTT server, String message) throws Exception {
		// while (true) {
		server.message = new MqttMessage();
		server.message.setQos(1); // 保证消息能到达一次
		server.message.setRetained(true);
		// java控制台输入
		// Scanner sc = new Scanner(System.in);
		// System.out.println("输入:");
		// String text = sc.next();
		String text = "ceshi";
		server.message.setPayload(message.getBytes());
		server.publish(server.topic11, server.message);
		System.out.println(server.message.isRetained() + "------ratained状态");
		 
	}

}