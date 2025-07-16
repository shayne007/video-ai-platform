package com.keensense.glstsdk;

/**
 * Created by memory_fu on 2019/5/16.
 */
public class TrackFunctionUtil {

    /**
     * currentTime return
     * @return
     */
    public static long getCurrentTime(){
        return System.currentTimeMillis();
    }

    /**
     * compute take up time
     * @param startTime
     * @return
     */
    public static long getSumTime(long startTime){
        return System.currentTimeMillis() - startTime;
    }

}