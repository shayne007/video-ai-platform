package com.keensense.search.es;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.framework.config.common
 * @Description： <p> EsConfigBean - es配置bean  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/23 - 14:04
 * @Modify By：
 * @ModifyTime： 2019/7/23
 * @Modify marker：
 */
@Configuration
@PropertySources(value = {@PropertySource("classpath:app-data-convert.properties")})
public class EsConfigBean {

    /**
     * source 源 es 数据源
     */
    @Value("${source.elasticsearch.host}")
    private String sourceHost;

    /**
     * source-端口
     */
    @Value("${source.elasticsearch.port}")
    private Integer sourcePort;

    /**
     * source-用户名
     */
    @Value("${source.elasticsearch.username}")
    private String sourceUsername;

    /**
     * source-密码
     */
    @Value("${source.elasticsearch.password}")
    private String sourcePassword;


    /**
     * target 目的 es 数据源
     */
    @Value("${target.elasticsearch.host}")
    private String targetHost;

    /**
     * source-端口
     */
    @Value("${target.elasticsearch.port}")
    private Integer targetPort;

    /**
     * source-用户名
     */
    @Value("${target.elasticsearch.username}")
    private String targetUsername;

    /**
     * source-密码
     */
    @Value("${target.elasticsearch.password}")
    private String targetPassword;

    /**
     * refresh 目的 es 数据源
     */
    @Value("${refresh.elasticsearch.host}")
    private String refreshHost;


    /**
     * refresh-端口
     */
    @Value("${refresh.elasticsearch.port}")
    private Integer refreshPort;

    /**
     * refresh-用户名
     */
    @Value("${refresh.elasticsearch.username}")
    private String refreshUsername;

    /**
     * refresh-密码
     */
    @Value("${refresh.elasticsearch.password}")
    private String refreshPassword;

    public String getSourceHost() {
        return sourceHost;
    }

    public void setSourceHost(String sourceHost) {
        this.sourceHost = sourceHost;
    }

    public Integer getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(Integer sourcePort) {
        this.sourcePort = sourcePort;
    }

    public String getSourceUsername() {
        return sourceUsername;
    }

    public void setSourceUsername(String sourceUsername) {
        this.sourceUsername = sourceUsername;
    }

    public String getSourcePassword() {
        return sourcePassword;
    }

    public void setSourcePassword(String sourcePassword) {
        this.sourcePassword = sourcePassword;
    }

    public String getTargetHost() {
        return targetHost;
    }

    public void setTargetHost(String targetHost) {
        this.targetHost = targetHost;
    }

    public Integer getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(Integer targetPort) {
        this.targetPort = targetPort;
    }

    public String getTargetUsername() {
        return targetUsername;
    }

    public void setTargetUsername(String targetUsername) {
        this.targetUsername = targetUsername;
    }

    public String getTargetPassword() {
        return targetPassword;
    }

    public void setTargetPassword(String targetPassword) {
        this.targetPassword = targetPassword;
    }

    public String getRefreshHost() {
        return refreshHost;
    }

    public void setRefreshHost(String refreshHost) {
        this.refreshHost = refreshHost;
    }

    public Integer getRefreshPort() {
        return refreshPort;
    }

    public void setRefreshPort(Integer refreshPort) {
        this.refreshPort = refreshPort;
    }

    public String getRefreshUsername() {
        return refreshUsername;
    }

    public void setRefreshUsername(String refreshUsername) {
        this.refreshUsername = refreshUsername;
    }

    public String getRefreshPassword() {
        return refreshPassword;
    }

    public void setRefreshPassword(String refreshPassword) {
        this.refreshPassword = refreshPassword;
    }
}
