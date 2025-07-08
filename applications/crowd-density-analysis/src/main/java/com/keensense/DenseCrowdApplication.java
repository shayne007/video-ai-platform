package com.keensense;

import com.alibaba.druid.support.http.StatViewServlet;
import com.keensense.common.swagger.annotation.EnableCommonSwagger;
import com.keensense.densecrowd.ApplicationStartup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * admin 服务应用启动
 *
 * @description:
 * @author: luowei
 * @createDate:2019年5月7日 下午5:54:15
 * @company:
 */
@SpringBootApplication
@EnableTransactionManagement
@SpringCloudApplication
@EnableScheduling
@EnableCommonSwagger
public class DenseCrowdApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(DenseCrowdApplication.class);
        springApplication.setAllowBeanDefinitionOverriding(true);
        springApplication.addListeners(new ApplicationStartup());
        springApplication.run(args);
    }

    @Bean
    public ServletRegistrationBean<StatViewServlet> druidStatViewServlet() {
        ServletRegistrationBean<StatViewServlet> registrationBean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        // IP白名单 (没有配置或者为空，则允许所有访问)
        registrationBean.addInitParameter("allow", "");
        // IP黑名单 (存在共同时，deny优先于allow)
        registrationBean.addInitParameter("deny", "");
        registrationBean.addInitParameter("loginUsername", "qst");
        registrationBean.addInitParameter("loginPassword", "qst**0808");
        registrationBean.addInitParameter("resetEnable", "false");
        return registrationBean;
    }
}
