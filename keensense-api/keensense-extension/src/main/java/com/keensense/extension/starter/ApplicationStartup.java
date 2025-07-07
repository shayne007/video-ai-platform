package com.keensense.extension.starter;

import com.alibaba.fastjson.JSONObject;
import com.keensense.common.config.SpringContext;
import com.keensense.extension.config.NacosConfig;
import com.keensense.extension.constants.ArchivesConstant;
import com.keensense.extension.util.BodyLibraryUtil;
import com.keensense.extension.util.FaceLibraryUtil;
import com.keensense.extension.util.LibraryUtil;
import com.keensense.sdk.algorithm.init.AlgoStartup;
import com.keensense.sdk.sys.utils.DbPropUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @Description:
 * @Author: jingege
 * @CreateDate: 2019/5/14 14:54
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info(" ********     AlgoStartup start   ******** ");
        //数据库初始化配置加载
        if (event.getApplicationContext().getParent() instanceof AnnotationConfigApplicationContext) {
            DbPropUtil.start("u2s_recog");
            getAlgo();
            //GL抓拍库创建
            FaceLibraryUtil.createGLCaptureFaceLibrary();
            ArchivesConstant.initAngle();
            //预加载热点数据至redis
            SpringContext.getBean(RedisCluster.class).initRedisCluster();
        }
        log.info(" ********     AlgoStartup end     ******** ");
    }

    private void getAlgo() {
        NacosConfig nacosConfig = SpringContext.getBean(NacosConfig.class);
        AlgoStartup.initAlgo((JSONObject) JSONObject.toJSON(nacosConfig));
        //检查底库数据和更新缓存
        LibraryUtil.isExistLibrary();
        BodyLibraryUtil.deleteInvalidBodyLibrary();
        //删除过期的人形底库
        LibraryUtil.updateLibraryCache();
        log.info("start ok");
    }

}