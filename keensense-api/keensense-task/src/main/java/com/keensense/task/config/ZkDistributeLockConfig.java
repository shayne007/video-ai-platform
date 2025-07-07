package com.keensense.task.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.keensense.task.lock.zk.ZkDistributeLock;

/**
 * @ClassName: ZkDistributeLockConfig
 * @Description: 初始化 ZkDistributeLock Bean
 * @Author: cuiss
 * @CreateDate: 2020/5/20 14:50
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Configuration
public class ZkDistributeLockConfig {

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public ZkDistributeLock zkDistributeLock() {
        return new ZkDistributeLock(ZkDistributeLock.DEFAULT_LOCK_NAME);
    }
}
