package com.keensense.task.listener;

import com.keensense.task.async.listener.QueueListener;
import com.keensense.task.async.AsyncQueueConstants;
import com.keensense.task.config.NacosConfig;
import com.keensense.task.config.ZkDistributeLockConfig;
import com.keensense.task.lock.zk.ZkDistributeLock;
import com.keensense.task.schedule.MasterSchedule;
import com.keensense.task.search.SearchHttp;
import com.keensense.task.util.DeleteTaskUtil;
import com.keensense.task.util.ValidUtil;
import com.keensense.task.util.VideoExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @Description: 启动加载
 * @Author: wujw
 * @CreateDate: 2019/9/28 17:02
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
@Component
public class StartListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private NacosConfig nacosConfig;

//    private static ExecutorService threadPool = new ThreadPoolExecutor(5, 5,
//            0L, TimeUnit.MILLISECONDS,
//            new LinkedBlockingQueue<Runnable>());

    public static ZkDistributeLock zkDistributeLock ;
            //= SpringContext.getBean(ZkDistributeLock.class);
            //new ZkDistributeLock(ZkDistributeLock.DEFAULT_LOCK_NAME);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("====================项目启动，开始加载=========================");
        try {
            loadHostId();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MasterSchedule.reloadLocalIp();
        loadNacosIp();
        log.info("====================获取本机IP配置："+MasterSchedule.getMasterIp()+"=========================");
        log.info("====================获取本机MasterId = "+MasterSchedule.getMasterId()+"=========================");
        if(nacosConfig.getAnalysisTrackSwitch()){
            log.info(">>>>>>>>>>>>>>>初始化分析打点队列线程 QueueListener ....... ");
            initTrackQueue();
        }
        getZkLock();
        log.info("====================项目启动，加载结束=========================");
    }

    /**
     * 初始化打点分析队列
     */
    private void initTrackQueue() {
        QueueListener trackQueue = new QueueListener(AsyncQueueConstants.AYALYSIS_TRACK_QUEUE_NAME,
                        AsyncQueueConstants.AYALYSIS_TRACK_CLASS);
        trackQueue.start();
    }

    private static void loadNacosIp(){
        String nacosIp = System.getenv("NACOS_HOST");
        //String nacosIp = "172.16.1.29";
        if(ValidUtil.isIp(nacosIp)){
            nacosIp = "http://" + nacosIp;
            log.info("====================获取NacosIP配置："+nacosIp+"=========================");
            SearchHttp.setNacosUrl(nacosIp);
        } else {
            throw VideoExceptionUtil.getValidException("获取nacos地址失败，请检查环境变量中是否存在NACOS_HOST");
        }
    }

    private static void loadHostId() throws IOException {
        File hostIdFile = null;
        if (SystemUtils.getEnvironmentVariable("os","").equalsIgnoreCase("linux")) {
            hostIdFile = new File("/etc/slaveHostId.txt");
        } else {
            hostIdFile = new File("D:/slaveHostId.txt");
        }
        if (!Files.exists(hostIdFile.toPath())) {
            MasterSchedule.setMasterId(DeleteTaskUtil.getUuid());
            if (!hostIdFile.exists()) {
                hostIdFile.getParentFile().mkdirs();
            }
            try {
                boolean isCreate = hostIdFile.createNewFile();
                if (isCreate) {
                    Files.write(hostIdFile.toPath(), MasterSchedule.getMasterId().getBytes());
                }
            } catch (IOException e) {
            }
        } else {
            MasterSchedule.setMasterId(Files.readAllLines(hostIdFile.toPath()).get(0));
        }
    }

    /**
     *  获取任务的锁
     */
    private void getZkLock(){
        ApplicationContext context = new AnnotationConfigApplicationContext(ZkDistributeLockConfig.class);
        zkDistributeLock = (ZkDistributeLock)context.getBean("zkDistributeLock");
        zkDistributeLock.lock();
    }

//    public static void initDeleteThread(ContextRefreshedEvent contextRefreshedEvent){
//        ApplicationContext ac = contextRefreshedEvent.getApplicationContext();
//        for(int i=0;i<5;i++){
//            //把线程先加载进来 springboot初始化装配之后直接启动。
//            TaskCleanRetryThread taskCleanRetryThread = ac.getBean("taskCleanRetryThread",TaskCleanRetryThread.class);
//            taskCleanRetryThread.setEnableLog(new AtomicBoolean(true));
//            taskCleanRetryThread.setExecuteCount(new AtomicLong(0));
//            taskCleanRetryThread.setThreadCount(new AtomicInteger(5));
//            taskCleanRetryThread.setThreadIndex(new AtomicInteger(i+1));
//            taskCleanRetryThread.setThreadGroupName("TaskCleanRetryThread");
//            Thread thread = new Thread(taskCleanRetryThread);
//            threadPool.execute(thread);
//        }
//    }

}
