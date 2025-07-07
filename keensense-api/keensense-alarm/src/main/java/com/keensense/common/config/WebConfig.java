package com.keensense.common.config;

import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ycl
 * @date 2019/5/10
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/*.html").addResourceLocations("classpath:/static/");

    }


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0,fasJsonConverter());

    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setLimit(3000);
        return paginationInterceptor;
    }

    @Bean
    public FastJsonHttpMessageConverter fasJsonConverter() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(
                //List字段如果为null,输出为[],而非null
                SerializerFeature.WriteNullListAsEmpty,

                //字符类型字段如果为null,输出为"",而非null
                SerializerFeature.WriteNullStringAsEmpty,

                //Boolean字段如果为null,输出为false,而非null
                SerializerFeature.WriteNullBooleanAsFalse,

                //消除对同一对象循环引用的问题，默认为false（如果不配置有可能会进入死循环）
                SerializerFeature.DisableCircularReferenceDetect,

                //是否输出值为null的字段,默认为false。
                SerializerFeature.WriteMapNullValue
        );
        config.setSerializeFilters((NameFilter) (source, name, value) -> {
            if (name == null || name.length() == 0) {
                return name;
            }
            if ("id".equals(name)) {
                name = "ID";
            }
            if (name.contains("Id")) {
                name = name.replace("Id", "ID");
            }
            char[] chars = name.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            return new String(chars);
        });
        config.setDateFormat("yyyyMMddHHmmss");
        //处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        converter.setSupportedMediaTypes(fastMediaTypes);
        converter.setFastJsonConfig(config);
        return converter;
    }


}
