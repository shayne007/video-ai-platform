package com.keensense.dataconvert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert
 * @Description： <p> DataConvertApplication - 数据转换模块  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/23 - 9:45
 * @Modify By：
 * @ModifyTime： 2019/7/23
 * @Modify marker：
 */
@SpringBootApplication
@EnableScheduling
public class DataConvertApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataConvertApplication.class, args);
    }

}
