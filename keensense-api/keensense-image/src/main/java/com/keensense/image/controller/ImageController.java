package com.keensense.image.controller;

import com.alibaba.fastjson.JSONObject;
import com.keensense.image.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhanx xiaohui on 2019-07-22.
 */
@RestController
@Slf4j
public class ImageController {

    @Autowired
    ImageService service;

    /**
     * upload image to server
     *
     * @param imageId     image id
     * @param imageBase64 base64 string of image
     * @return response json
     */
    @PostMapping(value = "/VIID/Images/{imageId}/Data", produces = "application/json;charset=UTF-8")
    public String image(@PathVariable String imageId, @RequestBody String imageBase64) {
        return service.image(imageId, imageBase64, "jpg", "ImageUrl", "");
    }

    /**
     * delete image from server
     *
     * @param serialnumber serial number of device
     * @param time         time of image
     * @return response json
     */
    @DeleteMapping(value = "/VIID/Images", produces = "application/json;charset=UTF-8")
    public String batchDeleteAsync(@RequestParam("Serialnumber") String serialnumber,
                                   @RequestParam(value = "Time", required = false) String time) {
        return service.batchDeleteImage(serialnumber, time);
    }
}