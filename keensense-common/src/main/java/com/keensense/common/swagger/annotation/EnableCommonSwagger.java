package com.keensense.common.swagger.annotation;

import com.keensense.common.swagger.config.SwaggerAutoConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @ClassName: EnableCommonSwagger注解
 * @Description: 通用的Swagger注解，在微服务启动类中加入此注解，即可用公共的swagger配置
 * @Author: cuiss
 * @CreateDate: 2019/5/12 14:03
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({SwaggerAutoConfig.class})
public @interface EnableCommonSwagger {
}
