package com.keensense.search.es;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;

/**
 * @ClassName：ElasticSearchClient
 * @Description： <p> ElasticSearchClient </p>
 * @Author： - Jason
 * @CreatTime：2019/7/23 - 13:51
 * @Modify By：
 * @ModifyTime： 2019/7/23
 * @Modify marker：
 * @version V1.0
*/
public class ElasticSearchClient implements Closeable {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchClient.class);

    /**
     * 搜索上下文的时间,用来支持该批次
     */
    private static final Long SCROLL_ALIVE_TIME = 5L;

    /**
     * host es 主机
     */
    private String host;

    /**
     * es 端口
     */
    private int port;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 推荐的客户端方式
     */
    private RestHighLevelClient restHighLevelClient;

    public ElasticSearchClient() {}


    /**
     * 初始化连接
     */
    public void init() {
        if (restHighLevelClient == null) {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
            RestClientBuilder builder = RestClient.builder(new HttpHost(host, port))
                    .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                        @Override
                        public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                            return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                        }
                    });
            restHighLevelClient = new RestHighLevelClient(builder);
        } else {
            logger.warn("ElasticSearch client 重复连接,host={},port={}", host, port);
        }
    }

    /**
     * 关闭连接
     */
    @Override
    public void close() {
        try {
            logger.info("Closing elasticSearch client");
            if (restHighLevelClient != null) {
                restHighLevelClient.close();
            }
        } catch (final Exception e) {
            logger.error("Error closing ElasticSearch client: ", e);
        }
    }

    /**
     * Setter method for property <tt>host</tt>.
     *
     * @param host value to be assigned to property host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Setter method for property <tt>port</tt>.
     *
     * @param port value to be assigned to property port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Setter method for property <tt>username</tt>.
     *
     * @param username value to be assigned to property username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Setter method for property <tt>password</tt>.
     *
     * @param password value to be assigned to property password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter method for property <tt>restHighLevelClient</tt>.
     *
     * @return property value of restHighLevelClient
     */
    public RestHighLevelClient getRestHighLevelClient() {
        return restHighLevelClient;
    }



}
