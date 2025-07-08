package com.keensense.job.util;

/**
 * 地图的工具类
 *
 * @author:duf
 * @version:1.0.0
 * @date 2018/12/7
 */
public class ChinaMapUtils {

    /**
     *
     * <p>
     * 粗略判断输入的经纬度是否在中国
     * lat:(18.19777,53.54361)
     * lng:(73.65569,135.999939)
     * </p>
     * <p>注：此方法只是简单选取了中国经度和纬度的大致范围。不在此范围经纬度，可认为是错误的！
     * 因为xml可能会同步过来异常数据，导致所有点位在地图上不展示。 如：
     *     lat：112.8114569
     *     lng：28.33632868
     *<p/>
     * @param Latitude  纬度
     * @param Longitude 经度
     * @return
     */
    public static boolean IsInsideChina(double Latitude, double Longitude) {

        boolean result = false;

        if ((Latitude > 18.19777 && Latitude < 53.54361) &&
                (Longitude > 73.65569 && Longitude < 135.999939)) {
            result = true;
        }

        return result;
    }

}
