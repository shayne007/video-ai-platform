package com.keensense.common.swagger.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName: SwaggerConfig
 * @Description: 通用的Swagger配置类
 * @Author: cuiss
 * @CreateDate: 2019/5/12 14:03
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Configuration
@EnableSwagger2
@EnableAutoConfiguration
public class SwaggerAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public SwaggerProperties swaggerProperties() {
        return new SwaggerProperties();
    }

    /**
     * swagger文档中展示所有带有 ApiOperation 注解的API
     * @param swaggerProperties
     * @return
     */
    @Bean
    public Docket createRestApi(SwaggerProperties swaggerProperties) {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(swaggerProperties))
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 加载swagger开头的配置文件
     * @param swaggerProperties
     * @return
     */
    private ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .license(swaggerProperties.getLicense())
                .licenseUrl(swaggerProperties.getLicenseUrl())
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                .contact(new Contact(swaggerProperties.getContact().getName(), swaggerProperties.getContact().getUrl(), swaggerProperties.getContact().getEmail()))
                .version(swaggerProperties.getVersion())
                .build();
    }

}
