package com.keensense.image.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fengsy
 * @date 7/5/21
 * @Description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class ImageControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    public void testImage() throws Exception {
        String imageId = "";
        mockMvc.perform(post("/VIID/Images/" + imageId + "/Data").content("")).andExpect(status().isOk()).andReturn();
    }
}
