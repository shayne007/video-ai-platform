package com.keensense.densecrowd.test;

import lombok.Data;

/**
 * Created by liusj on 2019/4/23
 * <p>
 * 坐标转换.
 * <p>
 * 火星坐标系 GCJ-02  中国坐标偏移标准，Google Map、高德、腾讯使用
 * 国际坐标   WGS-84  国际标准，GPS坐标（Google Earth使用、或者GPS模块）
 * BD-09   百度坐标偏移标准，Baidu Map使用
 */
public class CoordinateConvertUtils {

    private double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
    private double pi = 3.1415926535897932384626;
    private double a = 6378245.0;  // 长半轴
    private double ee = 0.00669342162296594323;  // 偏心率平方

    /**
     * 火星坐标系(GCJ-02)转百度坐标系(BD-09).
     *
     * @param lng 经度
     * @param lat 纬度
     * @return Point point
     */
    public Point gcg02ToBd09(double lng, double lat) {
        double z = Math.sqrt(lng * lng + lat * lat) + 0.00002 * Math.sin(lat * x_pi);
        double theta = Math.atan2(lat, lng) + 0.000003 * Math.cos(lng * x_pi);
        double bd_lng = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;

        return new Point(bd_lng, bd_lat);
    }

    /**
     * 百度坐标系(BD-09)转火星坐标系(GCJ-02).
     *
     * @param lng 经度
     * @param lat 纬度
     * @return Point point
     */
    public Point bd09ToGcj02(double lng, double lat) {
        double x = lng - 0.0065;
        double y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lng = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new Point(gg_lng, gg_lat);
    }

    /**
     * 国际坐标(WGS84)转火星坐标系(GCJ02).
     *
     * @param lng 经度
     * @param lat 纬度
     * @return Point point
     */
    public Point wgs84ToGcj02(double lng, double lat) {
        if (outOfChina(lng, lat)) { // 判读是否在国内
            return new Point(lng, lat);
        }
        double dlat = transformlat(lng - 105.0, lat - 35.0);
        double dlng = transformlng(lng - 105.0, lat - 35.0);
        double radlat = lat / 180.0 * pi;
        double magic = Math.sin(radlat);
        magic = 1 - ee * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * pi);
        dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * pi);
        double mglat = lat + dlat;
        double mglng = lng + dlng;
        return new Point(mglng, mglat);
    }

    /**
     * 火星坐标系(GCJ02)转国际坐标(WGS84).
     *
     * @param lng 经度
     * @param lat 纬度
     * @return Point point
     */
    public Point gcj02ToWgs84(double lng, double lat) {
        if (outOfChina(lng, lat)) { // 判读是否在国内
            return new Point(lng, lat);
        }
        double dlat = transformlat(lng - 105.0, lat - 35.0);
        double dlng = transformlng(lng - 105.0, lat - 35.0);
        double radlat = lat / 180.0 * pi;
        double magic = Math.sin(radlat);
        magic = 1 - ee * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * pi);
        dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * pi);
        double mglat = lat + dlat;
        double mglng = lng + dlng;
        return new Point(lng * 2 - mglng, lat * 2 - mglat);
    }

    public double transformlat(double lng, double lat) {
        double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat +
                0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * pi) + 20.0 *
                Math.sin(2.0 * lng * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * pi) + 40.0 *
                Math.sin(lat / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * pi) + 320 *
                Math.sin(lat * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public double transformlng(double lng, double lat) {
        double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng +
                0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * pi) + 20.0 *
                Math.sin(2.0 * lng * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lng * pi) + 40.0 *
                Math.sin(lng / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lng / 12.0 * pi) + 300.0 *
                Math.sin(lng / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }


    /**
     * 判断是否在国内，不在国内不做偏移.
     */
    public boolean outOfChina(double lng, double lat) {
        return !(lng > 73.66 && lng < 135.05 && lat > 3.86 && lat < 53.55);
    }

    @Data
    class Point {
        private double lng;
        private double lat;

        public Point(double lng, double lat) {
            this.lng = lng;
            this.lat = lat;
        }
    }

    private static CoordinateConvertUtils coordinateConvertUtils;

    private CoordinateConvertUtils() {
    }

    public static synchronized CoordinateConvertUtils getInstance() {
        if (coordinateConvertUtils == null) {
            return new CoordinateConvertUtils();
        }
        return coordinateConvertUtils;
    }

    public static void main(String[] args) {
        CoordinateConvertUtils coordinateConvertUtils = CoordinateConvertUtils.getInstance();
        Point point = coordinateConvertUtils.wgs84ToGcj02(113.565303f, 24.803341f);
        System.out.println(point.getLat() + "," + point.getLng());
        Point point1 = coordinateConvertUtils.gcg02ToBd09(point.getLng(), point.getLat());
        System.out.println(point1.getLat() + "," + point1.getLng());
    }
}