package com.keensense.dataconvert.framework.config.swagger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
 * @ClassName：Swagger2Config
 * @Description： <p> Swagger - @Url{http://localhost:9095/swagger-ui.html} or {http://localhost:9095/doc.html}  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/23 - 11:52
 * @Modify By：
 * @ModifyTime： 2019/7/23
 * @Modify marker：
 * @version V1.0
*/
@EnableSwagger2
@Configuration
public class Swagger2Config {

    private Logger logger = LoggerFactory.getLogger(Swagger2Config.class);

    /**
     * 需要扫描的路径
     */
    private static  final  String SWAGGER_PACKAGE_PATH = "com.keensense.dataconvert";

    /**
     * 是否开启swagger
     */
    @Value(value = "${swagger.enabled:true}")
    Boolean swaggerEnabled;

    /**
     * createRestApi
     * @return
     */
    @Bean
    public Docket createRestApi() {
        logger.info(">>> [初始化]Swagger2 enable status : {} ...",swaggerEnabled);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(swaggerEnabled)
                .select()
                .apis(RequestHandlerSelectors.basePackage(SWAGGER_PACKAGE_PATH))
                .paths(PathSelectors.any())
                .build().pathMapping("/");
    }

    /**
     * 构建api信息
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("--- KeenSense data-convert [数据转换]  ---")
                .description("千视通 | 数据转换模块")
                .contact(new Contact(
                        "Wu_Bin", "https://qianshitong.320.io:13085/cuiss/keensense-u2s.git",
                        "wub@1000video.com.cn"))
                .version("V1.0.1")
                .description("*** KeenSense data-convert module  Api Document ***")
                .build();
    }
}