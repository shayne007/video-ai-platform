package com.keensense.densecrowd.util;

import com.loocme.sys.util.StringUtil;

public class GoogleMapUtil {
    private GoogleMapUtil() {
    }

    private static final double PI = 3.14159265358979324;

    public static String[] getPointByEncrypt(String latitude, String longitude) {
        if ("pgis".equals(DbPropUtil.getString("google-map-from"))) {
            double lon = StringUtil.getDouble(longitude);
            double lat = StringUtil.getDouble(latitude);

            if (isOutOfChina(lat, lon)) {
                return new String[]{latitude, longitude};
            }

            double[] d = delta(lat, lon);

            return new String[]{(lat + d[0]) + "", (lon + d[1]) + ""};
        } else {
            if (StringUtil.isNotNull(latitude) && StringUtil.isNotNull(longitude)) {
                return new String[]{latitude, longitude};
            } else {
                return new String[]{};
            }

        }
    }

    public static String[] getPointByDecrypt(String latitude, String longitude) {
        if ("pgis".equals(DbPropUtil.getString("google-map-from"))) {
            double lon = StringUtil.getDouble(longitude);
            double lat = StringUtil.getDouble(latitude);

            if (isOutOfChina(lat, lon)) {
                return new String[]{latitude, longitude};
            }

            double[] d = delta(lat, lon);

            return new String[]{(lat - 2 * d[0]) + "", (lon - 2 * d[1]) + ""};
        } else {
            return new String[]{latitude, longitude};
        }
    }

    private static boolean isOutOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347) {
            return true;
        } else if (lat < 0.8293 || lat > 55.8271) {
            return true;
        } else {
            return false;
        }
    }

    private static double[] delta(double lat, double lon) {
        double a = 6378245.0; // a: 卫星椭球坐标投影到平面地图坐标系的投影因子。
        double ee = 0.00669342162296594323; // ee: 椭球的偏心率。
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * PI);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * PI);
        return new double[]{dLat, dLon};
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI))
                * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0
                / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0))
                * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y
                + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI))
                * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0
                / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI)
                + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * <p>
     * 粗略判断输入的经纬度是否在中国
     * lat:(18.19777,53.54361)
     * lng:(73.65569,135.999939)
     * </p>
     * <p>注：此方法只是简单选取了中国经度和纬度的大致范围。不在此范围经纬度，可认为是错误的！
     * 因为xml可能会同步过来异常数据，导致所有点位在地图上不展示。 如：
     * lat：112.8114569
     * lng：28.33632868
     * <p/>
     *
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
