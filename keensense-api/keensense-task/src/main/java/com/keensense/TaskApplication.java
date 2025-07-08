package com.keensense;

import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * task 服务应用启动
 *
 * @description:
 * @author: luowei
 * @createDate:2019年5月7日 下午5:54:15
 * @company:
 */
@EnableTransactionManagement
@SpringCloudApplication
@EnableScheduling
@EnableFeignClients
@EnableSwagger2
public class TaskApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(TaskApplication.class);
        springApplication.setAllowBeanDefinitionOverriding(true);
        springApplication.run(args);
    }

//    @Bean
//    public ServletRegistrationBean<StatViewServlet> druidStatViewServlet() {
//        ServletRegistrationBean<StatViewServlet> registrationBean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
//        // IP白名单 (没有配置或者为空，则允许所有访问)
//        registrationBean.addInitParameter("allow", "");
//        // IP黑名单 (存在共同时，deny优先于allow)
//        registrationBean.addInitParameter("deny", "");
//
//        registrationBean.addInitParameter("loginUsername", "qst");
//        registrationBean.addInitParameter("loginPassword", "qst**0808");
//        registrationBean.addInitParameter("resetEnable", "false");
//        return registrationBean;
//    }
}
