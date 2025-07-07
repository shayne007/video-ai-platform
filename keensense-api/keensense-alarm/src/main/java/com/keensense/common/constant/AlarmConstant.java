package com.keensense.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author ycl
 * @date 2019/5/24
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AlarmConstant {

    public static final int PERSON = 1;
    public static final int MOTOR_VEHICLE = 2;
    public static final int NON_MOTOR_VEHICLE = 4;
    public static final int NOT_EXIST = -1;


    /**
     * redis db index
     */
    public static final int REDIS_DB_INDEX = 4;
    /**
     * 启动报警计算开关
     */
    public static final boolean ALARM_START_SWICH = true;
    /**
     * 报警调试开关
     */
    public static final boolean ALARM_DEBUG_SWITCH = false;

    public static final int THREAD_POOL_NUMBER = 6;


    /**
     * 陌生人布控
     */
    public static final int CONTROL_STRANGER = 3;


    public static final int RECEIVE_MESSAGE_SIZE = 100;

}
