package com.keensense.commonlib.starter;

import com.alibaba.fastjson.JSONObject;
import com.keensense.common.config.SpringContext;
import com.keensense.commonlib.config.NacosConfig;
import com.keensense.sdk.algorithm.init.AlgoStartup;
import com.keensense.sdk.sys.utils.DbPropUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/5/21 15:49
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info(" ********     AlgoStartup start   ******** ");
        //数据库初始化配置加载
        ApplicationContext context = event.getApplicationContext();
        if (context instanceof AnnotationConfigServletWebServerApplicationContext) {
            DbPropUtil.start("u2s_recog");
            getAlgo();
            log.info(" ********     AlgoStartup end     ******** " );
        }

    }

    private void getAlgo(){
        NacosConfig nacosConfig = SpringContext.getBean(NacosConfig.class);
        AlgoStartup.initAlgo((JSONObject) JSONObject.toJSON(nacosConfig));
    }

}