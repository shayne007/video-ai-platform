package com.keensense.admin;

import com.keensense.admin.picturestream.thread.ResultSendThreads;
import com.keensense.admin.util.DbPropUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

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
			DbPropUtil.start("qst_u2s");
		}
		//卡口任务是否启动分析; 0不启动, 1启动分析
		String kkstart = DbPropUtil.getString("kakou.analysis", "0");
		if ("1".equals(kkstart)){
			ResultSendThreads.initCapacity();
			new ResultSendThreads();
		}
		log.info(" ********     ApplicationStartup end     ******** ");
	}
}