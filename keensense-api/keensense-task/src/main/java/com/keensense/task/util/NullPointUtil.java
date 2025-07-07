package com.keensense.task.util;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * @Description: 空指针工具类
 * @Author: wujw
 * @CreateDate: 2019/5/20 10:54
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class NullPointUtil {

    private NullPointUtil(){}
    
    /***
     * @description: 格式化时间
     * @param timestamp  时间
     * @return: int
     */
    public static String formatTimestamp(Timestamp timestamp){
        return Optional.ofNullable(timestamp).map(p -> DateUtil.formatDate(p.getTime())).orElse(null);
    }
}
