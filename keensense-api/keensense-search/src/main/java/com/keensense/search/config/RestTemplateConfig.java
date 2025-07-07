package com.keensense.search.config;

import lombok.Data;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConfigurationProperties(prefix = "http-pool")
@RefreshScope
@Data
public class RestTemplateConfig {

    private Integer maxTotal;// 连接池的最大连接数
    private Integer defaultMaxPerRoute;// 每个route默认的连接数(并发数)
    private Integer connectTimeout;// 连接上服务器(握手成功)的超时时间(毫秒)
    private Integer connectionRequestTimeout;// 从连接池中获取连接的超时时间(毫秒)
    private Integer socketTimeout;// 服务器返回数据(response)的超时时间(毫秒)
    private Integer validateAfterInactivity;// 空闲永久连接检查间隔(毫秒);官方推荐使用这个来检查永久链接的可用性，而不推荐每次请求的时候才去检查

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate(httpRequestFactory());
    }

    @Bean
    public ClientHttpRequestFactory httpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    @Bean
    public HttpClient httpClient() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        // 连接池的最大连接数
        connectionManager.setMaxTotal(maxTotal);
        // 每个route默认的连接数(并发数)
        connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        // 空闲永久连接检查间隔(毫秒);官方推荐使用这个来检查永久链接的可用性，而不推荐每次请求的时候才去检查
        connectionManager.setValidateAfterInactivity(validateAfterInactivity);
        // 服务器返回数据(response)的超时时间(毫秒)，超时抛出read
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout)
                // timeout
                // 连接上服务器(握手成功)的超时时间(毫秒)，超时抛出connect timeout
                .setConnectTimeout(connectTimeout)
                // 从连接池中获取连接的超时时间(毫秒)，超时间未拿到可用连接，会抛出org.apache.http.conn.ConnectionPoolTimeoutException:
                .setConnectionRequestTimeout(connectionRequestTimeout)
                // Timeout waiting for connection from pool
                .build();
        return HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setConnectionManager(connectionManager)
                .build();
    }

}
