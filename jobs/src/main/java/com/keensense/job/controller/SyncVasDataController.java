package com.keensense.job.controller;

import com.keensense.job.task.ISyncVasDataTask;
import com.keensense.job.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SyncVasDataController {

    @Autowired
    private ISyncVasDataTask syncVasDataTask;

    @PostMapping("syncVasData")
    public R syncVasData(){
        try {
            syncVasDataTask.syncVasData();
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("同步Vas监控点失败");
        }
        return R.ok();
    }

}
