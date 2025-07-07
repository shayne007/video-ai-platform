/**
 * All rights Reserved, Designed By qianshitong
 *
 * @Title: AlarmHandleTimer.java
 * @Package cn.jiuling.recog.alarm
 * @Description: 告警处理类
 * @version V1.0
 * @Copyright: 2019 www.1000video.com.cn Inc. All rights reserved.
 * 注意：本内容仅限于苏州千视通视觉科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
package com.keensense.commonlib.timer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.keensense.commonlib.entity.CommonFeatureRecord;
import com.keensense.commonlib.mapper.CommonFeatureRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author ycl
 * @date 2019/9/10
 */
@Component
@Slf4j
@EnableScheduling
public class RecordCleanTimer {

    @Resource
    CommonFeatureRecordMapper recordMapper;

    @Scheduled(cron = "0 0 0 1/1 * ? ")
    public void featureRecordClean() {
        LocalDateTime weekAgo = LocalDateTime.now().minusWeeks(1);
        int delete = recordMapper.delete(Wrappers.<CommonFeatureRecord>lambdaQuery()
                .le(CommonFeatureRecord::getUpdateTime, weekAgo));
        log.info("featureRecordClean delete is {}", delete);

    }
}
