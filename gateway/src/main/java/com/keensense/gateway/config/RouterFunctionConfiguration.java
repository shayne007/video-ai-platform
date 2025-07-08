package com.keensense.gateway.config;

import com.keensense.gateway.handler.HystrixFallbackHandler;
import com.keensense.gateway.handler.SwaggerResourceHandler;
import com.keensense.gateway.handler.SwaggerSecurityHandler;
import com.keensense.gateway.handler.SwaggerUiHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

/**
 * @ClassName: RouterFunctionConfiguration
 * @Description: TODO
 * @Author: cuiss
 * @CreateDate: 2019/5/12 10:41
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class RouterFunctionConfiguration {
    private final HystrixFallbackHandler hystrixFallbackHandler;
    private final SwaggerResourceHandler swaggerResourceHandler;
    private final SwaggerSecurityHandler swaggerSecurityHandler;
    private final SwaggerUiHandler swaggerUiHandler;

    @Bean
    public RouterFunction routerFunction() {
        return RouterFunctions.route(
                RequestPredicates.path("/fallback")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), hystrixFallbackHandler)
                .andRoute(RequestPredicates.GET("/swagger-resources")
                        .and(RequestPredicates.accept(MediaType.ALL)), swaggerResourceHandler)
                .andRoute(RequestPredicates.GET("/swagger-resources/configuration/ui")
                        .and(RequestPredicates.accept(MediaType.ALL)), swaggerUiHandler)
                .andRoute(RequestPredicates.GET("/swagger-resources/configuration/security")
                        .and(RequestPredicates.accept(MediaType.ALL)), swaggerSecurityHandler);

    }

}
