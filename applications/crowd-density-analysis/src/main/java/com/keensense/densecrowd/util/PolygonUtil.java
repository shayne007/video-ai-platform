package com.keensense.densecrowd.util;

import java.awt.geom.*;
import java.util.*;

public class PolygonUtil {
    private PolygonUtil() {
    }

    /**
     * 返回一个点是否在一个多边形区域内
     *
     * @param point   点
     * @param polygon 多边形
     * @return
     */
    public static boolean checkWithJdkGeneralPath(Point2D.Double point, List<Point2D.Double> polygon) {
        java.awt.geom.GeneralPath p = new java.awt.geom.GeneralPath();
        Point2D.Double first = polygon.get(0);
        p.moveTo(first.x, first.y);
        polygon.remove(0);
        for (Point2D.Double d : polygon) {
            p.lineTo(d.x, d.y);
        }
        polygon.add(0, first);
        p.lineTo(first.x, first.y);

        p.closePath();

        return p.contains(point);
    }

    /**
     * 将字符串格式化组装成多边形数组
     *
     * @param polygon 格式(lon:lat,lon:lat) 多个点用','分割,每个点位经纬度信息用":"隔开
     * @return
     */
    public static List<Point2D.Double> paresPolygonList(String polygon) {
        String[] polygonArr = polygon.split(",");
        List<Point2D.Double> polygonList = new ArrayList<>();
        for (String point : polygonArr) {
            if (StringUtils.isNotEmptyString(point)) {
                String[] pointLonLat = point.split(":");
                Point2D.Double point2d = pareseDouble(pointLonLat[0], pointLonLat[1]);
                polygonList.add(point2d);
            }
        }
        return polygonList;
    }

    /**
     * 封装Point2D.Double
     *
     * @param lon
     * @param lat
     * @return
     */
    public static Point2D.Double pareseDouble(String lon, String lat) {
        try {
            if (StringUtils.isNotEmptyString(lat) && StringUtils.isNotEmptyString(lon)) {
                return new Point2D.Double(Double.parseDouble(lon), Double.parseDouble(lat));
            }
        } catch (Exception e) {
            throw e;
        }

        return null;
    }
}
