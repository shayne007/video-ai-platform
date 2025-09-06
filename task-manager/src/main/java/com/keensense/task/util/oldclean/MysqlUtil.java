package com.keensense.task.util.oldclean;

import com.keensense.common.config.SpringContext;
import com.keensense.task.config.NacosConfig;

/**
 * @Description: 分析模块为mysql模式下的工具类
 * @Author: wujw
 * @CreateDate: 2019/10/22 14:56
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
public class MysqlUtil {

    private MysqlUtil(){}

    protected static NacosConfig nacosConfig = SpringContext.getBean(NacosConfig.class);

    public static boolean isMysql(){
        return !nacosConfig.getZkSwitch() && !nacosConfig.getBitSwitch();
    }
}
