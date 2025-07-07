package com.keensense;

import com.alibaba.fastjson.JSONObject;
import com.keensense.alarm.service.consumer.AlarmHandler;
import com.keensense.common.config.HandlerMethodArgumentResolver1400;
import com.keensense.common.config.HandlerMethodReturnValueHandler1400;
import com.keensense.common.config.NacosConfigCenter;
import com.keensense.common.config.SpringContext;
import com.keensense.sdk.algorithm.init.AlgoStartup;
import com.keensense.sdk.sys.utils.DbPropUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ycl
 * @date 2019/5/22
 */
@Slf4j
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("started");
        ApplicationContext context = event.getApplicationContext();
        if (context instanceof AnnotationConfigServletWebServerApplicationContext) {
            //开启数据库配置
            DbPropUtil.start("u2s_recog");

            //开启redis读取数据
            SpringContext.getBean(AlarmHandler.class).start();
            //初始化算法配置
            NacosConfigCenter nacosConfig = SpringContext.getBean(NacosConfigCenter.class);
            AlgoStartup.initAlgo((JSONObject) JSONObject.toJSON(nacosConfig));
            //开启自定义资源协商
            initHandler();
        }
    }

    private void initHandler() {
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = SpringContext.getBean(RequestMappingHandlerAdapter.class);
        // 获取当前 RequestMappingHandlerAdapter 所有的 ArgumentResolver对象
        List<HandlerMethodArgumentResolver> argumentResolvers = requestMappingHandlerAdapter.getArgumentResolvers();
        List<HandlerMethodArgumentResolver> newArgumentResolvers = new ArrayList<>(argumentResolvers.size() + 1);
        // 添加 HandlerMethodArgumentResolver1400 到集合第一个位置
        newArgumentResolvers.add(0, new HandlerMethodArgumentResolver1400());
        // 将原 ArgumentResolver 添加到集合中
        newArgumentResolvers.addAll(argumentResolvers);
        // 重新设置 ArgumentResolver对象集合
        requestMappingHandlerAdapter.setArgumentResolvers(newArgumentResolvers);

        List<HandlerMethodReturnValueHandler> returnValueHandlers = requestMappingHandlerAdapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> newReturnValueHandlers = new ArrayList<>(returnValueHandlers.size()+1);
        for (HandlerMethodReturnValueHandler returnValueHandler : returnValueHandlers) {
            if (returnValueHandler instanceof RequestResponseBodyMethodProcessor) {
                newReturnValueHandlers.add(new HandlerMethodReturnValueHandler1400(returnValueHandler));
                newReturnValueHandlers.add(returnValueHandler);
            } else {
                newReturnValueHandlers.add(returnValueHandler);
            }
        }
        requestMappingHandlerAdapter.setReturnValueHandlers(newReturnValueHandlers);
    }
}
