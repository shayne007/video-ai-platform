package com.keensense;

import com.alibaba.druid.support.http.StatViewServlet;
import com.keensense.common.swagger.annotation.EnableCommonSwagger;
import com.keensense.extension.starter.ApplicationStartup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @ClassName: ExtensionApplication
 * @Description: 扩展服务启动类
 * @Author: cuiss
 * @CreateDate: 2019/5/12 13:41
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@SpringCloudApplication
@EnableCommonSwagger
@EnableFeignClients
@EnableScheduling
public class ExtensionApplication {

    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication(ExtensionApplication.class);
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
