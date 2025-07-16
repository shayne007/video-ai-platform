package com.keensense.video.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by zhanx xiaohui on 2019-05-24.
 */
@FeignClient(name = "keensense-task")
public interface FeignToTask {
    @PostMapping("/rest/taskManage/getTaskForSearch")
    String getSerialNumber(@RequestBody String body);

    @PostMapping("/callback/delete/task/image")
    String setDeleteTaskStatus(@RequestBody String body);

}
