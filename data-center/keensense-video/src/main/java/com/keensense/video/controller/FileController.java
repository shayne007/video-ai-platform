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
 * Video File Controller
 */
@RestController
public class FileController {
    @Autowired
    FileService service;

    @PostMapping(value = "/VIID/File/{fileId}/Data")
    public String file(@PathVariable String fileId,
                       @RequestParam(value = "ext", required = false) String ext,
                       @RequestBody byte[] stream) {
        return service.upload(fileId, stream, ext, "Url");
    }

    @DeleteMapping(value = "/VIID/File", produces = "application/json;charset=UTF-8")
    public String batchDeleteAsync(@RequestParam("serialNumber") String serialNumber,
                                   @RequestParam(value = "Time", required = false) String time) {
        return service.batchDeleteImage(serialNumber, time);
    }
}
