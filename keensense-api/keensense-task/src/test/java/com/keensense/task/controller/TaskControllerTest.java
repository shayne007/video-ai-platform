package com.keensense.task.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author fengsy
 * @date 6/12/20
 * @Description
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableFeignClients(basePackages = {"com.keensense.task.client"})
@Slf4j
public class TaskControllerTest {

    @Test
    void getPictureByteTest(){
        //前置准备

        //执行

        //断言

        //清理

    }
}
