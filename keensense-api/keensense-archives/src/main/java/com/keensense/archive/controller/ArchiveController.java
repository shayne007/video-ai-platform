package com.keensense.archive.controller;

import com.alibaba.fastjson.JSONObject;
import com.keensense.archive.service.ArchiveService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 档案数据
 * Created by memory_fu on 2019/12/26.
 */
@RestController
public class ArchiveController {

    @Resource
    private ArchiveService archiveService;

    @PostMapping(value = "/VIID/Archive/ArchiveTitleSearch", produces = "application/json;charset=UTF-8")
    public String archiveTitleSearch(@RequestBody JSONObject json) {
        return archiveService.archiveTitleSearch(json);
    }

    @PostMapping(value = "/VIID/Archive/UpdateArchive", produces = "application/json;charset=UTF-8")
    public String updateArchive(@RequestBody JSONObject json) {
        return archiveService.updateArchive(json);
    }


    @GetMapping(value = "/VIID/Archive/status", produces = "application/json;charset=UTF-8")
    public String status() {
        JSONObject object = new JSONObject();
        object.put("code", 0);
        object.put("msg", "OK");
        return object.toJSONString();
    }

}
