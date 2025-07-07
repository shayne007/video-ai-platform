package com.keensense.video.controller;

import com.keensense.video.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhanx xiaohui on 2019-12-17.
 */
@RestController
public class FileController {
    @Autowired
    FileService service;

    @PostMapping(value = "/VIID/File/{fileId}/Data")
    public String file(@PathVariable String fileId,
                       @RequestParam(value = "FileExtentionName", required = false) String fileExtentionName,
                       @RequestBody byte[] stream) {
//
        return service.file(fileId, stream, fileExtentionName, "Url");
    }

    @DeleteMapping(value = "/VIID/File", produces = "application/json;charset=UTF-8")
    public String batchDeleteAsync(@RequestParam("Serialnumber") String serialnumber,
                                   @RequestParam(value = "Time", required = false) String time) {
        return service.batchDeleteImage(serialnumber, time);
    }
}
