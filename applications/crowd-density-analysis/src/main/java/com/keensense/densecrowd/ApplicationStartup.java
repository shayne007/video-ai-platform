package com.keensense.densecrowd;

import com.keensense.densecrowd.task.AlarmManagerTask;
import com.keensense.densecrowd.task.TechnologyManagerTask;
import com.keensense.densecrowd.util.DbPropUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import java.time.temporal.Temporal;

/**
 * 初始化加载类
 *
 * @description:
 * @author: luowei
 * @createDate:2019年5月9日 下午2:39:05
 * @company:
 */
@Slf4j
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info(" ********     ApplicationStartup start   ******** " + event.getApplicationContext().getParent());
//    	数据库初始化配置加载
        if (event.getApplicationContext().getParent() instanceof AnnotationConfigApplicationContext) {
            DbPropUtil.start("qst_densecrowd");
            AlarmManagerTask.start();
            TechnologyManagerTask.start();
        }
        // 初始化数据输出实现方式
        /*IResultSend resultSend = new ResultSendImpl();
        ResultSendThreads.initCapacity(3, resultSend);
		new ResultSendThreads();*/
        log.info(" ********     ApplicationStartup end     ******** ");
    }
}