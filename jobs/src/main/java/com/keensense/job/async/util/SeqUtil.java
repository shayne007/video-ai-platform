package com.keensense.job.async.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: SeqUtil
 * @Description: TODO
 * @Author: cuiss
 * @CreateDate: 2019/8/10 16:51
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class SeqUtil {
    private static volatile Long BASE_ID = 1000L;

    public SeqUtil() {
    }

    public static synchronized String getSequence() {
        try {
            BASE_ID++;
            long curTime = System.currentTimeMillis() - 1000000000  ;
            long tempId = BASE_ID + curTime;
            return Long.toString(tempId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取序列异常:", e.getMessage());
        }
        return null;
    }
}
